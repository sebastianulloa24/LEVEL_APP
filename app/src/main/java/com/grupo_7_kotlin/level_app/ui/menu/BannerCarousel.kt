package com.grupo_7_kotlin.level_app.ui.menu

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grupo_7_kotlin.level_app.R
import kotlinx.coroutines.delay

@Composable
fun BannerCarousel() {
    val imagenes = listOf(
        R.drawable.banner_minecraft,
        R.drawable.banner_gta_vl,
        R.drawable.banner_juegos
    )

    var index by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(4000) // cambia cada 4 segundos
            index = (index + 1) % imagenes.size
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()   // ocupa todo el ancho
            .height(200.dp)   // puedes aumentar si quieres
    ) {
        Crossfade(targetState = imagenes[index], label = "bannerFade") { imagen ->
            Image(
                painter = painterResource(id = imagen),
                contentDescription = "Banner rotativo",
                modifier = Modifier.fillMaxSize(),

                contentScale = ContentScale.Fit
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBannerCarousel() {
    BannerCarousel()
}
