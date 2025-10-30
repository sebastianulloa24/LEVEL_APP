package com.grupo_7_kotlin.level_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.grupo_7_kotlin.level_app.navigation.AppNav
import com.grupo_7_kotlin.level_app.ui.theme.LEVEL_UP_Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LEVEL_UP_Theme{
                AppNav()
            }
        }
    }
}
