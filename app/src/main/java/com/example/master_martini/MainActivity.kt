package com.example.master_martini

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.master_martini.ui.screens.BibliotecaScreen
import com.example.master_martini.ui.screens.FavoritosScreen
import com.example.master_martini.ui.screens.HomeScreen
import com.example.master_martini.ui.screens.LoginScreen
import com.example.master_martini.ui.screens.PerfilScreen
import com.example.master_martini.ui.screens.RegistroScreen
import com.example.master_martini.ui.theme.Master_MartiniTheme
import com.example.master_martini.viewmodel.RegistroViewModel

class MainActivity : ComponentActivity() {
    private val registroViewModel: RegistroViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val creamColor = android.graphics.Color.parseColor("#FAF7F0")
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                creamColor,
                creamColor
            ),
            navigationBarStyle = SystemBarStyle.light(
                creamColor,
                creamColor
            )
        )
        registroViewModel.cargarDatosLocales(this)
        setContent {
            Master_MartiniTheme {
                val navController = rememberNavController()
                val navBackStackEntry by
                navController.currentBackStackEntryAsState()
                val rutaActual = navBackStackEntry?.destination?.route
                val uiState by registroViewModel.uiState.collectAsState()
                val context = LocalContext.current
                Scaffold(
                    containerColor = Color(0xFFFAF7F0),
                    bottomBar = {
                        if (
                            rutaActual != "registro" &&
                            rutaActual != "login"
                        ) {
                            NavigationBar(
                                containerColor = Color.White,
                                modifier = Modifier.shadow(16.dp)
                            ) {
                                val colores = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Color(0xFF18372B),
                                    selectedTextColor = Color(0xFF18372B),
                                    indicatorColor = Color(0xFFD9AD50)
                                        .copy(alpha = 0.3f),
                                    unselectedIconColor = Color(0xFF6B6966),
                                    unselectedTextColor = Color(0xFF6B6966)
                                )
                                NavigationBarItem(
                                    selected = rutaActual == "home",
                                    onClick = {
                                        navController.navigate("home") {
                                            popUpTo("home") {
                                                inclusive = false
                                            }
                                            launchSingleTop = true
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            Icons.Rounded.Home,
                                            contentDescription = "Inicio"
                                        )
                                    },
                                    label = { Text("Inicio") },
                                    colors = colores
                                )
                                NavigationBarItem(
                                    selected = rutaActual?.startsWith(
                                        "biblioteca"
                                    ) == true,
                                    onClick = {
                                        navController.navigate("biblioteca/Todas") {
                                            launchSingleTop = true
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            Icons.Rounded.VideoLibrary,
                                            contentDescription = "Biblioteca"
                                        )
                                    },
                                    label = { Text("Biblioteca") },
                                    colors = colores
                                )
                                NavigationBarItem(
                                    selected = rutaActual == "favoritos",
                                    onClick = {
                                        navController.navigate("favoritos") {
                                            launchSingleTop = true
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            Icons.Rounded.FavoriteBorder,
                                            contentDescription = "Favoritos"
                                        )
                                    },
                                    label = { Text("Favoritos") },
                                    colors = colores
                                )
                                NavigationBarItem(
                                    selected = rutaActual == "perfil",
                                    onClick = {
                                        navController.navigate("perfil") {
                                            launchSingleTop = true
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            Icons.Rounded.Person,
                                            contentDescription = "Perfil"
                                        )
                                    },
                                    label = { Text("Perfil") },
                                    colors = colores
                                )
                            }
                        }
                    }
                ) { paddingGlobal ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingGlobal),
                        enterTransition = { EnterTransition.None },
                        exitTransition = { ExitTransition.None }
                    ) {
                        composable("home") {
                            HomeScreen(
                                nombreUsuario = if (uiState.sesionActiva) {
                                    uiState.nombre.ifBlank { "Emprendedor" }
                                } else {
                                    "Emprendedor"
                                },
                                onBibliotecaClick = { categoria ->
                                    navController.navigate(
                                        "biblioteca/$categoria"
                                    )
                                },
                                onFavoritosClick = {
                                    navController.navigate("favoritos")
                                },
                                onRegistroClick = {
                                    navController.navigate("registro")
                                },
                                onPerfilClick = {
                                    navController.navigate("perfil")
                                }
                            )
                        }
                        composable("registro") {
                            RegistroScreen(
                                viewModel = registroViewModel,
                                onRegistroExitoso = {
                                    navController.navigate("perfil") {
                                        popUpTo("registro") {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                },
                                onVolverClick = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable("login") {
                            LoginScreen(
                                onVolverClick = {
                                    navController.popBackStack()
                                },
                                onCrearCuentaClick = {
                                    navController.navigate("registro")
                                },
                                onIniciarSesion = { correo, contrasena, resultado ->
                                    registroViewModel.iniciarSesion(
                                        context = context,
                                        correoIngresado = correo,
                                        contrasena = contrasena
                                    ) { exito, mensaje ->
                                        resultado(exito, mensaje)
                                        if (exito) {
                                            navController.navigate("perfil") {
                                                popUpTo("login") {
                                                    inclusive = true
                                                }
                                                launchSingleTop = true
                                            }
                                        }
                                    }
                                }
                            )
                        }
                        composable("perfil") {
                            PerfilScreen(
                                viewModel = registroViewModel,
                                onVolverClick = {
                                    navController.navigate("home") {
                                        popUpTo("home") {
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                    }
                                },
                                onIrARegistroClick = {
                                    navController.navigate("registro")
                                },
                                onIrALoginClick = {
                                    navController.navigate("login")
                                }
                            )
                        }
                        composable(
                            route = "biblioteca/{categoria}",
                            arguments = listOf(
                                navArgument("categoria") {
                                    type = NavType.StringType
                                }
                            )
                        ) {backStackEntry ->
                            val categoria = backStackEntry.arguments
                                ?.getString("categoria")
                                ?: "Todas"
                            BibliotecaScreen(
                                categoriaInicial = categoria,
                                onVolverClick = {
                                    navController.navigate("home") {
                                        popUpTo("home") {
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                    }
                                },
                                onFavoritosClick = {
                                    navController.navigate("favoritos")
                                },
                                usuarioId = uiState.usuarioId,
                                onNecesitaSesion = {
                                    navController.navigate("login")
                                }
                            )
                        }
                        composable("favoritos") {
                            FavoritosScreen(
                                onVolverClick = {
                                    navController.navigate("home") {
                                        popUpTo("home") {
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                    }
                                },
                                onExplorarClick = {
                                    navController.navigate("biblioteca/Todas")
                                },
                                usuarioId = uiState.usuarioId,
                                onNecesitaSesion = {
                                    navController.navigate("login")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
