package com.example.master_martini

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.sp
import com.example.master_martini.ui.screens.BibliotecaScreen
import com.example.master_martini.ui.screens.FavoritosScreen
import com.example.master_martini.ui.screens.HomeScreen
import com.example.master_martini.ui.screens.PerfilScreen
import com.example.master_martini.ui.screens.RegistroScreen
import com.example.master_martini.ui.theme.Master_MartiniTheme
import com.example.master_martini.viewmodel.RegistroViewModel

class MainActivity : ComponentActivity() {

    private val registroViewModel: RegistroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val martiniCreamColor = android.graphics.Color.parseColor("#FAF7F0")

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(martiniCreamColor, martiniCreamColor),
            navigationBarStyle = SystemBarStyle.light(martiniCreamColor, martiniCreamColor)
        )

        registroViewModel.cargarDatosLocales(this)

        setContent {
            Master_MartiniTheme {
                var pantallaActual by remember { mutableStateOf("home") }

                Scaffold(
                    containerColor = Color(0xFFFAF7F0),
                    bottomBar = {
                        if (pantallaActual != "registro") {
                            NavigationBar(containerColor = Color.White) {
                                val coloresPro = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Color(0xFF18372B),
                                    selectedTextColor = Color(0xFF18372B),
                                    indicatorColor = Color(0xFFD9AD50).copy(alpha = 0.3f),
                                    unselectedIconColor = Color(0xFF6B6966),
                                    unselectedTextColor = Color(0xFF6B6966)
                                )

                                NavigationBarItem(
                                    selected = pantallaActual == "home",
                                    onClick = { pantallaActual = "home" },
                                    icon = { Text("⌂", fontSize = 24.sp) },
                                    label = { Text("Inicio") },
                                    colors = coloresPro
                                )
                                NavigationBarItem(
                                    selected = pantallaActual == "biblioteca",
                                    onClick = { pantallaActual = "biblioteca" },
                                    icon = { Text("▤", fontSize = 24.sp) },
                                    label = { Text("Biblioteca") },
                                    colors = coloresPro
                                )
                                NavigationBarItem(
                                    selected = pantallaActual == "favoritos",
                                    onClick = { pantallaActual = "favoritos" },
                                    icon = { Text("🤍", fontSize = 24.sp) },
                                    label = { Text("Favoritos") },
                                    colors = coloresPro
                                )
                                NavigationBarItem(
                                    selected = pantallaActual == "perfil",
                                    onClick = { pantallaActual = "perfil" },
                                    icon = { Text("♙", fontSize = 24.sp) },
                                    label = { Text("Perfil") },
                                    colors = coloresPro
                                )
                            }
                        }
                    }
                ) { paddingGlobal ->
                    Box(modifier = Modifier.fillMaxSize().padding(paddingGlobal)) {
                        when (pantallaActual) {
                            "home" -> {
                                HomeScreen(
                                    onRegistroClick = { pantallaActual = "registro" }
                                )
                            }
                            "registro" -> {
                                RegistroScreen(
                                    viewModel = registroViewModel,
                                    onRegistroExitoso = { pantallaActual = "perfil" },
                                    onVolverClick = { pantallaActual = "perfil" }
                                )
                            }
                            "perfil" -> {
                                PerfilScreen(
                                    viewModel = registroViewModel,
                                    onVolverClick = { pantallaActual = "perfil" },
                                    onIrARegistroClick = { pantallaActual = "registro" }
                                )
                            }
                            "biblioteca" -> {
                                BibliotecaScreen()
                            }
                            "favoritos" -> {
                                FavoritosScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}