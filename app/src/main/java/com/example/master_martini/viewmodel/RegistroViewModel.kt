package com.example.master_martini.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import java.io.FileOutputStream

data class RegistroUiState(
    val nombre: String = "",
    val correo: String = "",
    val fotoUri: Uri? = null,
    val errorNombre: Boolean = false,
    val errorCorreo: Boolean = false,
    val mensajeError: String = ""
)

class RegistroViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState.asStateFlow()

    fun cargarDatosLocales(context: Context) {
        val prefs = context.getSharedPreferences("MisDatos", Context.MODE_PRIVATE)
        val nombreGuardado = prefs.getString("nombre", "") ?: ""
        val correoGuardado = prefs.getString("correo", "") ?: ""
        val uriGuardada = prefs.getString("fotoUri", "") ?: ""

        _uiState.update {
            it.copy(
                nombre = nombreGuardado,
                correo = correoGuardado,
                fotoUri = if (uriGuardada.isNotEmpty()) Uri.parse(uriGuardada) else null
            )
        }
    }

    fun guardarDatosLocales(context: Context) {
        val prefs = context.getSharedPreferences("MisDatos", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString("nombre", _uiState.value.nombre)
            putString("correo", _uiState.value.correo)
            putString("fotoUri", _uiState.value.fotoUri?.toString() ?: "")
            apply()
        }
    }

    fun procesarYGuardarFoto(context: Context, uri: Uri?) {
        if (uri == null) return
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.filesDir, "foto_perfil_master.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)

            inputStream?.close()
            outputStream.close()

            val uriLocal = Uri.fromFile(file)
            _uiState.update { it.copy(fotoUri = uriLocal) }
        } catch (e: Exception) {
            _uiState.update { it.copy(fotoUri = uri) } // Respaldo
        }
    }

    fun cerrarSesion(context: Context) {
        val prefs = context.getSharedPreferences("MisDatos", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
        _uiState.update { RegistroUiState() } // Reinicia la pantalla a blanco
    }

    fun onNombreChange(nuevoNombre: String) {
        _uiState.update { it.copy(nombre = nuevoNombre, errorNombre = false, mensajeError = "") }
    }

    fun onCorreoChange(nuevoCorreo: String) {
        _uiState.update { it.copy(correo = nuevoCorreo, errorCorreo = false, mensajeError = "") }
    }

    fun validarFormulario(): Boolean {
        val estadoActual = _uiState.value
        var esValido = true
        var mensaje = ""

        if (estadoActual.nombre.isBlank()) {
            _uiState.update { it.copy(errorNombre = true) }
            mensaje = "El nombre no puede estar vacío."
            esValido = false
        } else if (!estadoActual.correo.contains("@")) {
            _uiState.update { it.copy(errorCorreo = true) }
            mensaje = "Ingresa un correo válido."
            esValido = false
        }

        if (!esValido) {
            _uiState.update { it.copy(mensajeError = mensaje) }
        }

        return esValido
    }
}