package com.grupo_7_kotlin.level_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.grupo_7_kotlin.level_app.navigation.AppNavigation
import com.grupo_7_kotlin.level_app.ui.theme.LEVEL_UP_Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            LEVEL_UP_Theme{
                AppNavigation()
                /*val postViewModel: com.example.apirest004.viewmodel.PostsViewModel = viewModel()
                PostScreen(viewModel = postViewModel)*/
            }
        }
    }
}

