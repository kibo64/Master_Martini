package com.example.master_martini

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.master_martini.ui.screens.HomeScreen
import com.example.master_martini.ui.screens.PerfilScreen
import com.example.master_martini.ui.screens.RegistroScreen
import com.example.master_martini.ui.theme.Master_MartiniTheme
import com.example.master_martini.viewmodel.RegistroViewModel

class MainActivity : ComponentActivity() {

    private val registroViewModel: RegistroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        registroViewModel.cargarDatosLocales(this)

        setContent {
            Master_MartiniTheme {
                var pantallaActual by remember { mutableStateOf("home") }

                when (pantallaActual) {
                    "home" -> {
                        HomeScreen(
                            onRegistroClick = { pantallaActual = "registro" },
                            onPerfilClick = { pantallaActual = "perfil" }
                        )
                    }
                    "registro" -> {
                        RegistroScreen(
                            viewModel = registroViewModel,
                            onRegistroExitoso = { pantallaActual = "home" },
                            onVolverClick = { pantallaActual = "home" }
                        )
                    }

                    "perfil" -> {
                        PerfilScreen(
                            viewModel = registroViewModel,
                            onVolverClick = { pantallaActual = "home" },
                            onIrARegistroClick = { pantallaActual = "registro" }
                        )
                    }
                }
            }
        }
    }
}