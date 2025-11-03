package com.grupo_7_kotlin.level_app.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
// ✅ IMPORTACIÓN CORREGIDA: Necesaria para fillMaxSize()
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.grupo_7_kotlin.level_app.viewmodel.UsuarioViewModel
import java.util.concurrent.Executors

private const val TAG = "ScannerScreen"

@Composable
fun ScannerScreen(
    navController: NavController,
    onQrScanned: (String) -> Unit, // Callback para manejar el resultado del escaneo
    viewModel: UsuarioViewModel
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Estado para gestionar si tenemos el permiso de cámara
    var hasCameraPermission by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
    }

    // Lanza la solicitud de permiso si no lo tenemos
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasCameraPermission = isGranted
            if (!isGranted) {
                // Si el usuario deniega, volvemos atrás
                navController.popBackStack()
            }
        }
    )

    LaunchedEffect(key1 = true) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    if (hasCameraPermission) {
        // Vista principal del escáner con la vista previa de la cámara
        CameraPreview(
            lifecycleOwner = lifecycleOwner,
            context = context,
            onQrScanned = onQrScanned,
            // Permite volver atrás si el usuario presiona el botón de retroceso en la pantalla
            onNavigateBack = { navController.popBackStack() }
        )
    } else {
        // Muestra un mensaje si no hay permiso (aunque el LaunchedEffect ya navegaría hacia atrás)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Se necesita permiso de cámara para escanear QR.")
        }
    }
}

// Composable que contiene la lógica de CameraX y ML Kit
@Composable
fun CameraPreview(
    lifecycleOwner: LifecycleOwner,
    context: Context,
    onQrScanned: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    val scanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
    )

    // Bandera para asegurar que solo procesamos el primer QR encontrado
    var qrProcessed by remember { mutableStateOf(false) }

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                // Preview
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                // Image Analyzer (para procesar los frames de la cámara)
                val imageAnalyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor, QrCodeAnalyzer(scanner, onQrCodeScanned = { result ->
                            // Solo procesamos si no hemos procesado un QR antes
                            if (!qrProcessed) {
                                qrProcessed = true
                                // Detenemos el escaneo
                                cameraProvider.unbindAll()
                                // Devolvemos el resultado al composable principal
                                onQrScanned(result)
                            }
                        }))
                    }

                // Select back camera
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    // Unbind any previous usages
                    cameraProvider.unbindAll()

                    // Bind use cases to camera
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalyzer
                    )
                } catch (exc: Exception) {
                    Log.e(TAG, "Binding failed", exc)
                    // Si falla, volvemos atrás
                    onNavigateBack()
                }
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        },
        modifier = Modifier.fillMaxSize()
    )
}

// Clase para analizar los frames de la imagen y buscar códigos QR (ML Kit)
class QrCodeAnalyzer(
    private val scanner: com.google.mlkit.vision.barcode.BarcodeScanner,
    private val onQrCodeScanned: (String) -> Unit
) : ImageAnalysis.Analyzer {

    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        val rawValue = barcode.rawValue
                        if (rawValue != null) {
                            // Se encontró un QR. Notificamos el resultado.
                            onQrCodeScanned(rawValue)
                            break
                        }
                    }
                }
                .addOnFailureListener {
                    Log.e(TAG, "ML Kit Error: ${it.localizedMessage}")
                }
                .addOnCompleteListener {
                    // Cierra el ImageProxy para liberar el buffer
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }
}
