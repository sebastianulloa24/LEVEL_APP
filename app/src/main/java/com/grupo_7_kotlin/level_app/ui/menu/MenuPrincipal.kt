package com.grupo_7_kotlin.level_app.ui.menu
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.grupo_7_kotlin.level_app.navigation.AppRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuPrincipal(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Menú Principal") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { navController.navigate(AppRoutes.CATALOG_SCREEN) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ir al Catálogo de Productos")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate(AppRoutes.SCANNER_SCREEN) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Escanear Código QR")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Otras opciones, por ejemplo: navController.navigate(AppRoutes.PROFILE_SCREEN) */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Configuración")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewMenuPrincipal() {
    val navController = rememberNavController()
    MenuPrincipal(navController = navController)
}