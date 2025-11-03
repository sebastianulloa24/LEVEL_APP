package com.grupo_7_kotlin.level_app.ui.components

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
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
    onQrScanned: (String) -> Unit,
    viewModel: UsuarioViewModel
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasCameraPermission = isGranted
            if (!isGranted) navController.popBackStack()
        }
    )

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    if (hasCameraPermission) {
        CameraPreview(
            lifecycleOwner = lifecycleOwner,
            context = context,
            onQrScanned = { result ->
                Log.d("QR_RESULT", "QR Detectado: $result")

                // Si el QR contiene un link, abrimos el navegador
                if (result.startsWith("http")) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(result))

                    // Opción 1: abrir en Chrome si está disponible
                    intent.setPackage("com.android.chrome")
                    try {
                        context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        // Si no hay Chrome, abrir con el navegador por defecto
                        context.startActivity(
                            Intent(Intent.ACTION_VIEW, Uri.parse(result))
                        )
                    }
                } else {
                    Toast.makeText(
                        context,
                        "QR detectado: $result",
                        Toast.LENGTH_LONG
                    ).show()
                }

                // Vuelve atrás después de abrir el link
                navController.popBackStack()
            },
            onNavigateBack = { navController.popBackStack() }
        )
    } else {
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


                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {

                    cameraProvider.unbindAll()


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
