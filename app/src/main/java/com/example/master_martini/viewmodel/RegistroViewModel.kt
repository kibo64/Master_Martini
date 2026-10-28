package com.example.master_martini.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.master_martini.data.AppDatabase
import com.example.master_martini.data.UsuarioEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
        val dao = AppDatabase.getDatabase(context).usuarioDao()
        viewModelScope.launch {
            dao.obtenerUsuario().collect { usuario ->
                if (usuario != null) {
                    _uiState.update {
                        it.copy(
                            nombre = usuario.nombre,
                            correo = usuario.correo,
                            fotoUri = if (usuario.fotoUri.isNotEmpty()) Uri.parse(usuario.fotoUri) else null
                        )
                    }
                }
            }
        }
    }

    fun guardarDatosLocales(context: Context) {
        val dao = AppDatabase.getDatabase(context).usuarioDao()
        viewModelScope.launch {
            val usuario = UsuarioEntity(
                nombre = _uiState.value.nombre,
                correo = _uiState.value.correo,
                fotoUri = _uiState.value.fotoUri?.toString() ?: ""
            )
            dao.guardarUsuario(usuario)
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
            _uiState.update { it.copy(fotoUri = uri) }
        }
    }

    fun cerrarSesion(context: Context) {
        val dao = AppDatabase.getDatabase(context).usuarioDao()
        viewModelScope.launch {
            dao.eliminarUsuario()
            _uiState.update { RegistroUiState() }
        }
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