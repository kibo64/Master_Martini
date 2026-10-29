package com.example.master_martini.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val LoginGreen = Color(0xFF244D3B)
private val LoginDarkGreen = Color(0xFF18372B)
private val LoginGold = Color(0xFFD9AD50)
private val LoginCream = Color(0xFFFAF7F0)

@Composable
fun LoginScreen(
    onVolverClick: () -> Unit = {},
    onCrearCuentaClick: () -> Unit = {},
    onIniciarSesion: (
        String,
        String,
        (Boolean, String) -> Unit
    ) -> Unit
) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var mostrarContrasena by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LoginCream)
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolverClick) {
                Icon(
                    Icons.Rounded.ArrowBack,
                    contentDescription = "Volver",
                    tint = LoginGreen
                )
            }
            Text("Volver", color = LoginGreen, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            "MASTER MARTINI",
            color = LoginGold,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 3.sp
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            "¡Qué bueno verte!",
            color = LoginDarkGreen,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Inicia sesión para continuar",
            color = Color.Gray,
            fontSize = 15.sp
        )
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(
            value = correo,
            onValueChange = {
                correo = it
                mensajeError = ""
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Correo Gmail") },
            leadingIcon = {
                Icon(Icons.Rounded.Mail, contentDescription = null)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = LoginGreen,
                focusedLabelColor = LoginGreen,
                cursorColor = LoginGreen
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = contrasena,
            onValueChange = {
                contrasena = it
                mensajeError = ""
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Contraseña") },
            leadingIcon = {
                Icon(Icons.Rounded.Lock, contentDescription = null)
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        mostrarContrasena = !mostrarContrasena
                    }
                ) {
                    Icon(
                        imageVector = if (mostrarContrasena) {
                            Icons.Rounded.VisibilityOff
                        } else {
                            Icons.Rounded.Visibility
                        },
                        contentDescription = "Mostrar u ocultar contraseña"
                    )
                }
            },
            singleLine = true,
            visualTransformation = if (mostrarContrasena) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = LoginGreen,
                focusedLabelColor = LoginGreen,
                cursorColor = LoginGreen
            )
        )
        if (mensajeError.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = mensajeError,
                color = Color(0xFFD32F2F),
                fontSize = 13.sp
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            enabled = !cargando,
            onClick = {
                val email = correo.trim()

                if (
                    !android.util.Patterns.EMAIL_ADDRESS
                        .matcher(email).matches() ||
                    email.count { it == '@' } != 1 ||
                    !email.endsWith("@gmail.com", ignoreCase = true)
                ) {
                    mensajeError = "Ingresa un correo Gmail válido."
                } else if (contrasena.isBlank()) {
                    mensajeError = "Ingresa tu contraseña."
                } else {
                    cargando = true
                    mensajeError = ""

                    onIniciarSesion(email, contrasena) { exito, mensaje ->
                        cargando = false
                        mensajeError = if (exito) "" else mensaje
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LoginGreen
            )
        ) {
            Text(
                if (cargando) "Comprobando..." else "Iniciar sesión",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(18.dp))
        HorizontalDivider(color = Color(0xFFE4DFD5))
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            "¿Todavía no tienes una cuenta?",
            color = Color.Gray,
            fontSize = 14.sp
        )
        TextButton(onClick = onCrearCuentaClick) {
            Text(
                "Crear cuenta",
                color = LoginGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            "Tu experiencia, al estilo Martini.",
            color = LoginGold,
            fontSize = 13.sp
        )
    }
}
