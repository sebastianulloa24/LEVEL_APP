package com.grupo_7_kotlin.level_app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo_7_kotlin.level_app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBar(onNavigate: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.logo_level_up),
                    contentDescription = "Logo Level-Up",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { onNavigate("menuPrincipal") }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Level-Up Gamer",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        },
        actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menú",
                    tint = Color.White
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color(0xFF1B1B1B))
            ) {
                DropdownMenuItem(
                    text = { Text("Inicio", color = Color.White) },
                    onClick = {
                        expanded = false
                        onNavigate("menuPrincipal")
                    }
                )
                DropdownMenuItem(
                    text = { Text("Catálogo", color = Color.White) },
                    onClick = {
                        expanded = false
                        onNavigate("catalogo")
                    }
                )
                DropdownMenuItem(
                    text = { Text("Perfil", color = Color.White) },
                    onClick = {
                        expanded = false
                        onNavigate("perfil")
                    }
                )
                DropdownMenuItem(
                    text = { Text("Iniciar Sesión", color = Color.White) },
                    onClick = {
                        expanded = false
                        onNavigate("inicioSesion")
                    }
                )
                DropdownMenuItem(
                    text = { Text("Registrarse", color = Color.White) },
                    onClick = {
                        expanded = false
                        onNavigate("registrarse")
                    }
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF1B1B1B),
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        modifier = Modifier.fillMaxWidth()
    )
}
