package com.example.master_martini.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Patterns
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
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec


data class RegistroUiState(
    val usuarioId: Int? = null,
    val nombre: String = "",
    val correo: String = "",
    val fotoUri: Uri? = null,
    val contrasena: String = "",
    val confirmarContrasena: String = "",
    val errorNombre: Boolean = false,
    val errorCorreo: Boolean = false,
    val mensajeError: String = "",
    val sesionActiva: Boolean = false
)
class RegistroViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState.asStateFlow()
    private fun crearSalt(): ByteArray =
        ByteArray(16).also { SecureRandom().nextBytes(it) }
    private fun bytesHex(bytes: ByteArray): String =
        bytes.joinToString("") { "%02x".format(it) }
    private fun hexBytes(hex: String): ByteArray =
        hex.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
    private fun generarHash(
        contrasena: String,
        salt: ByteArray
    ): String {
        val spec = PBEKeySpec(
            contrasena.toCharArray(),
            salt,
            120_000,
            256
        )
        return try {
            SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA256")
                .generateSecret(spec)
                .encoded
                .let(::bytesHex)
        } finally {
            spec.clearPassword()
        }
    }
    private fun correoValido(correo: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(correo).matches() &&
                correo.count { it == '@' } == 1 &&
                correo.endsWith("@gmail.com", ignoreCase = true)
    }
    fun cargarDatosLocales(context: Context) {
        cerrarSesion()
    }
    fun registrarCuenta(
        context: Context,
        contrasena: String,
        confirmarContrasena: String,
        onResultado: (Boolean, String) -> Unit
    ) {
        val estado = _uiState.value
        val nombre = estado.nombre.trim()
        val correo = estado.correo.trim().lowercase()
        when {
            nombre.isBlank() -> {
                onResultado(false, "Ingresa tu nombre.")
                return
            }
            !correoValido(correo) -> {
                onResultado(false, "Ingresa un correo Gmail válido.")
                return
            }
            contrasena.length < 8 -> {
                onResultado(
                    false,
                    "Usa al menos 8 caracteres en tu contraseña."
                )
                return
            }
            contrasena != confirmarContrasena -> {
                onResultado(false, "Las contraseñas no coinciden.")
                return
            }
        }
        viewModelScope.launch {
            try {
                val dao = AppDatabase
                    .getDatabase(context)
                    .usuarioDao()
                if (dao.buscarPorCorreo(correo) != null) {
                    onResultado(
                        false,
                        "Ya existe una cuenta con ese correo. Inicia sesión."
                    )
                    return@launch
                }
                val salt = crearSalt()
                val hash = generarHash(contrasena, salt)
                val usuario = UsuarioEntity(
                    id = 0,
                    nombre = nombre,
                    correo = correo,
                    fotoUri = estado.fotoUri?.toString() ?: "",
                    passwordHash = hash,
                    passwordSalt = bytesHex(salt)
                )
                val idGuardado = dao.guardarUsuario(usuario).toInt()
                _uiState.update {
                    it.copy(
                        usuarioId = idGuardado,
                        nombre = nombre,
                        correo = correo,
                        fotoUri = usuario.fotoUri
                            .takeIf { uri -> uri.isNotBlank() }
                            ?.let(Uri::parse),
                        contrasena = "",
                        confirmarContrasena = "",
                        sesionActiva = true,
                        mensajeError = ""
                    )
                }
                onResultado(true, "Cuenta creada correctamente.")
            } catch (e: Exception) {
                onResultado(false, "No se pudo guardar la cuenta.")
            }
        }
    }
    fun iniciarSesion(
        context: Context,
        correoIngresado: String,
        contrasena: String,
        onResultado: (Boolean, String) -> Unit
    ) {
        val correo = correoIngresado.trim().lowercase()
        if (!correoValido(correo)) {
            onResultado(false, "Ingresa un correo Gmail válido.")
            return
        }
        if (contrasena.isBlank()) {
            onResultado(false, "Ingresa tu contraseña.")
            return
        }
        viewModelScope.launch {
            try {
                val dao = AppDatabase
                    .getDatabase(context)
                    .usuarioDao()
                val usuario = dao.buscarPorCorreo(correo)
                if (usuario == null) {
                    onResultado(
                        false,
                        "No existe una cuenta con ese correo."
                    )
                    return@launch
                }
                if (
                    usuario.passwordHash.isBlank() ||
                    usuario.passwordSalt.isBlank()
                ) {
                    onResultado(
                        false,
                        "Esta cuenta antigua necesita crear una contraseña. Usa Crear cuenta con el mismo correo."
                    )
                    return@launch
                }
                val hashIngresado = generarHash(
                    contrasena,
                    hexBytes(usuario.passwordSalt)
                )
                val coincide = MessageDigest.isEqual(
                    hashIngresado.toByteArray(Charsets.UTF_8),
                    usuario.passwordHash.toByteArray(Charsets.UTF_8)
                )
                if (!coincide) {
                    onResultado(false, "La contraseña es incorrecta.")
                    return@launch
                }
                _uiState.update {
                    it.copy(
                        usuarioId = usuario.id,
                        nombre = usuario.nombre,
                        correo = usuario.correo,
                        fotoUri = usuario.fotoUri
                            .takeIf { uri -> uri.isNotBlank() }
                            ?.let(Uri::parse),
                        contrasena = "",
                        confirmarContrasena = "",
                        sesionActiva = true,
                        mensajeError = ""
                    )
                }
                onResultado(true, "Sesión iniciada correctamente.")
            } catch (e: Exception) {
                onResultado(false, "No se pudo iniciar sesión.")
            }
        }
    }
    fun cerrarSesion() {
        _uiState.update {
            RegistroUiState()
        }
    }
    fun onNombreChange(nuevoNombre: String) {
        _uiState.update {
            it.copy(
                nombre = nuevoNombre,
                errorNombre = false,
                mensajeError = ""
            )
        }
    }
    fun onCorreoChange(nuevoCorreo: String) {
        _uiState.update {
            it.copy(
                correo = nuevoCorreo,
                errorCorreo = false,
                mensajeError = ""
            )
        }
    }
    fun onContrasenaChange(valor: String) {
        _uiState.update {
            it.copy(
                contrasena = valor,
                mensajeError = ""
            )
        }
    }
    fun onConfirmarContrasenaChange(valor: String) {
        _uiState.update {
            it.copy(
                confirmarContrasena = valor,
                mensajeError = ""
            )
        }
    }
    fun procesarYGuardarFoto(
        context: Context,
        uri: Uri?
    ) {
        if (uri == null) return
        try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                val archivo = File(
                    context.filesDir,
                    "foto_perfil_master.jpg"
                )
                FileOutputStream(archivo).use { output ->
                    input.copyTo(output)
                }
                _uiState.update {
                    it.copy(fotoUri = Uri.fromFile(archivo))
                }
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    mensajeError = "No se pudo guardar la foto."
                )
            }
        }
    }
}

