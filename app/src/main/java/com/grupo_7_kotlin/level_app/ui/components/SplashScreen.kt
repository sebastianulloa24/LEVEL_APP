package com.grupo_7_kotlin.level_app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grupo_7_kotlin.level_app.R // Asegúrate de tener tu recurso drawable aquí
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    // 1. Estado de la animación (opacidad)
    val alpha = remember { Animatable(0f) }

    // 2. Control de la animación
    LaunchedEffect(key1 = true) {
        // Fade In: Animación que hace aparecer el logo
        alpha.animateTo(1f, animationSpec = tween(durationMillis = 1500))

        // Espera de 1 segundo para que el usuario vea el logo
        delay(1000)

        // Navega a la siguiente pantalla
        onTimeout()
    }

    // 3. Diseño de la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_level_up),
            contentDescription = "Logo de Level App",
            modifier = Modifier
                .size(150.dp)
                .alpha(alpha.value)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Level App",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            modifier = Modifier.alpha(alpha.value)
        )
    }


}
@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    // Usamos un tema simple para que los colores se muestren correctamente.
    // Reemplaza 'YourAppNameTheme' por el nombre de tu tema si lo tienes definido.
    // Si no tienes un tema personalizado, puedes usar un tema de ejemplo:
    MaterialTheme {
        // En la vista previa, onTimeout no hace nada, ya que solo queremos ver el diseño.
        SplashScreen(onTimeout = {})
    }
}