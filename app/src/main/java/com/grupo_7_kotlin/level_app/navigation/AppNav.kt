package com.grupo_7_kotlin.level_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.grupo_7_kotlin.level_app.view.PantallaCanje
import com.grupo_7_kotlin.level_app.ui.perfil.ProfilScreen
import com.grupo_7_kotlin.level_app.ui.admin.DebugScreen
import com.grupo_7_kotlin.level_app.view.SplashScreen
import com.grupo_7_kotlin.level_app.ui.components.ScannerScreen
import com.grupo_7_kotlin.level_app.view.LoginScreen
import com.grupo_7_kotlin.level_app.ui.menu.MenuPrincipal
import com.grupo_7_kotlin.level_app.ui.catalogo.PantallaCatalogo
import com.grupo_7_kotlin.level_app.ui.catalogo.PantallaDetalleProducto
import com.grupo_7_kotlin.level_app.ui.register.RegisterScreen
import com.grupo_7_kotlin.level_app.viewmodel.ProductoViewModel
import com.grupo_7_kotlin.level_app.viewmodel.ProductoViewModelFactory
import com.grupo_7_kotlin.level_app.viewmodel.UsuarioViewModel
import com.grupo_7_kotlin.level_app.viewmodel.UsuarioViewModelFactory

object AppRoutes {
    const val MENU_PRINCIPAL = "menu_principal"
    const val LOGIN_SCREEN = "login_screen"
    const val REGISTER_SCREEN = "register_screen"
    const val CATALOG_SCREEN = "catalog_screen"
    const val PROFILE_SCREEN = "profile_screen"
    const val PRODUCT_DETAIL_BASE = "product_detail"
    const val PRODUCT_DETAIL_SCREEN = "$PRODUCT_DETAIL_BASE/{productId}"
    const val CANJE_SCREEN = "canje"
    const val DEBUG_SCREEN = "debug_admin_tool"
    const val SPLASH_SCREEN = "splash"
    const val SCANNER_SCREEN = "qr_scanner"

}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val usuarioViewModel: UsuarioViewModel = viewModel(
        factory = UsuarioViewModelFactory(context)
    )

    val productoViewModel: ProductoViewModel = viewModel(
        factory = ProductoViewModelFactory(context)
    )

    NavHost(
        navController = navController,
        startDestination = AppRoutes.SPLASH_SCREEN

    ) {


        composable(route = AppRoutes.SPLASH_SCREEN) {
            SplashScreen(
                onTimeout = {
                    navController.navigate(AppRoutes.MENU_PRINCIPAL) {
                        popUpTo(AppRoutes.SPLASH_SCREEN) { inclusive = true }
                    }
                }
            )
        }

        composable(route = AppRoutes.LOGIN_SCREEN) {
            LoginScreen(
                viewModel = usuarioViewModel,
                onLoginSuccess = {
                    navController.navigate(AppRoutes.MENU_PRINCIPAL) {
                        popUpTo(AppRoutes.LOGIN_SCREEN) { inclusive = true }
                    }
                },
                onGoToRegister = { navController.navigate(AppRoutes.REGISTER_SCREEN) }
            )
        }

        composable(route = AppRoutes.REGISTER_SCREEN) {
            RegisterScreen(
                viewModel = usuarioViewModel,
                onRegisterSuccess = {
                    navController.navigate(AppRoutes.CATALOG_SCREEN) {
                        popUpTo(AppRoutes.LOGIN_SCREEN) { inclusive = true }
                    }
                },
                onShowLogin = { navController.popBackStack() }
            )
        }

        composable(route = AppRoutes.CATALOG_SCREEN) {
            PantallaCatalogo(
                productoViewModel = productoViewModel,
                usuarioViewModel = usuarioViewModel,
                onProductoClick = { productId ->
                    navController.navigate("${AppRoutes.PRODUCT_DETAIL_BASE}/$productId")
                },
                onProfileClick = { navController.navigate(AppRoutes.PROFILE_SCREEN) },
                onScanQrClick = { navController.navigate(AppRoutes.SCANNER_SCREEN) }
            )
        }

        composable(
            route = AppRoutes.PRODUCT_DETAIL_SCREEN,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            PantallaDetalleProducto(
                productId = productId,
                usuarioViewModel = usuarioViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(route = AppRoutes.PROFILE_SCREEN) {
            ProfilScreen(
                viewModel = usuarioViewModel,
                onLogoutSuccess = {
                    navController.navigate(AppRoutes.LOGIN_SCREEN) {
                        popUpTo(AppRoutes.CATALOG_SCREEN) { inclusive = true }
                    }
                },
                onNavigateToCanje = {
                    navController.navigate(AppRoutes.CANJE_SCREEN)
                },
                onNavigateToDebugScreen = {
                    navController.navigate(AppRoutes.DEBUG_SCREEN)
                }
            )
        }

        composable(route = AppRoutes.CANJE_SCREEN) {
            PantallaCanje(
                navController = navController,
                usuarioViewModel = usuarioViewModel
            )
        }

        composable(route = AppRoutes.DEBUG_SCREEN) {
            DebugScreen(
                usuarioViewModel = usuarioViewModel,
                onNavigateToLogin = {
                    navController.navigate(AppRoutes.LOGIN_SCREEN) {
                        popUpTo(AppRoutes.CATALOG_SCREEN) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(route = AppRoutes.SCANNER_SCREEN) {
            val context = LocalContext.current  // Contexto correcto para abrir navegador

            ScannerScreen(
                navController = navController,
                viewModel = usuarioViewModel,
                onQrScanned = { qrContent ->
                    if (qrContent.startsWith("https://www.tiendaslevelup.cl/tienda")) {
                        // Abre el enlace en el navegador
                        val intent = android.content.Intent(
                            android.content.Intent.ACTION_VIEW,
                            android.net.Uri.parse(qrContent)
                        )
                        context.startActivity(intent)

                        // Luego de abrir el navegador, cierra el escáner
                        navController.popBackStack()
                    } else {
                        // Si no es una URL válida, simplemente cierra el escáner
                        navController.popBackStack()
                    }
                }
            )
        }
        composable(route = AppRoutes.MENU_PRINCIPAL) {
            MenuPrincipal(
                navController = navController,
                usuarioViewModel = usuarioViewModel,
                onProductoDestacadoClick = { productId ->
                    navController.navigate("${AppRoutes.PRODUCT_DETAIL_BASE}/$productId")
                }
            )
        }





    }

}

