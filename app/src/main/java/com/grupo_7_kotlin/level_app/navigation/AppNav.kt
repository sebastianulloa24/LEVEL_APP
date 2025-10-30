package com.grupo_7_kotlin.level_app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun AppNav(){
    //reamos controlador
    val navController = rememberNavController()
    NavHost( navController= navController, startDestination = "login"){
        composable("login"){
            LoginScreen(navController= navController)
        }//fin composable
    }
}