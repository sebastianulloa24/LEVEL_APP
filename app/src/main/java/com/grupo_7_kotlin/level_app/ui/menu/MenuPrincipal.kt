package com.grupo_7_kotlin.level_app.ui.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.grupo_7_kotlin.level_app.R
import com.grupo_7_kotlin.level_app.data.dao.UsuarioDao
import com.grupo_7_kotlin.level_app.data.model.Usuario
import com.grupo_7_kotlin.level_app.navigation.AppRoutes
import com.grupo_7_kotlin.level_app.ui.components.MapScreen
import com.grupo_7_kotlin.level_app.ui.components.NavBar
import com.grupo_7_kotlin.level_app.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuPrincipal(
    navController: NavController,
    usuarioViewModel: UsuarioViewModel,
    onProductoDestacadoClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            // NavBar personalizada
            NavBar { destino ->
                val route = when (destino) {
                    "inicioSesion" -> AppRoutes.LOGIN_SCREEN
                    "registrarse" -> AppRoutes.REGISTER_SCREEN
                    "catalogo" -> AppRoutes.CATALOG_SCREEN
                    "perfil" -> AppRoutes.PROFILE_SCREEN
                    "menuPrincipal" -> AppRoutes.MENU_PRINCIPAL
                    else -> AppRoutes.MENU_PRINCIPAL
                }

                navController.navigate(route) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        },
        containerColor = Color.Black
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .background(Color.Black.copy(alpha = 0.8f)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Banner
                BannerCarousel()

                Spacer(modifier = Modifier.height(24.dp))

                // Productos destacados
                Text(
                    text = "🎮 Productos Destacados",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp)
                )

                val productos = listOf(
                    Pair("CG001", R.drawable.pc_gamer_asus),
                    Pair("CO001", R.drawable.playstation_5),
                    Pair("SG001", R.drawable.silla_gamer_titan)
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(productos) { (id, imagen) ->
                        Card(
                            modifier = Modifier
                                .width(200.dp)
                                .height(150.dp)
                                .clickable { onProductoDestacadoClick(id) },
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Image(
                                painter = painterResource(id = imagen),
                                contentDescription = "Producto destacado",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Comunidad y Noticias
                Text(
                    text = "Comunidad y Noticias",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "🎮 ¡Nuevo torneo de eSports!",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            "Participa en el próximo evento de Level-Up Gamer y demuestra tus habilidades. Cupos limitados.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Foro de jugadores", style = MaterialTheme.typography.titleMedium)
                        Text(
                            "Conversa con otros gamers sobre estrategias, reseñas y lanzamientos de nuevos productos.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Mapa interactivo
                MapScreen(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp) // altura fija para interactuar correctamente
                        .padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Bienvenido a Level-Up",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

class FakeUsuarioDao : UsuarioDao {
    override suspend fun insertarUsuario(usuario: Usuario) {}
    override suspend fun obtenerUsuarioPorEmail(email: String): Usuario? {
        return Usuario(
            id = 1,
            email = "nami@example.com",
            passwordHash = "fakeHash",
            birthDate = System.currentTimeMillis(),
            hasDuocDiscount = true,
            levelUpPoints = 250,
            myReferralCode = "LVL-UP-001"
        )
    }
    override suspend fun getUserByReferralCode(code: String): Usuario? = null
    override suspend fun updateUser(usuario: Usuario) {}
    override suspend fun deleteAllUsers() {}
}

// -----------------------------
// Preview
// -----------------------------
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewMenuPrincipal() {
    val navController = rememberNavController()
    val fakeDao = FakeUsuarioDao()
    val fakeViewModel = UsuarioViewModel(fakeDao)

    MenuPrincipal(
        navController = navController,
        usuarioViewModel = fakeViewModel,
        onProductoDestacadoClick = {}
    )
}