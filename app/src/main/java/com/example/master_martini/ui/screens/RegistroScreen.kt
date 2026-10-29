package com.example.master_martini.ui.screens

import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.master_martini.viewmodel.RegistroViewModel

private val RegistroGreen = Color(0xFF244D3B)
private val RegistroDarkGreen = Color(0xFF18372B)
private val RegistroGold = Color(0xFFD9AD50)
private val RegistroCream = Color(0xFFFAF7F0)
private val RegistroText = Color(0xFF302D28)

@Composable
fun RegistroScreen(
    viewModel: RegistroViewModel,
    onRegistroExitoso: () -> Unit = {},
    onVolverClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var mostrarContrasena by remember { mutableStateOf(false) }
    var mostrarConfirmacion by remember { mutableStateOf(false) }
    var mensajeRegistro by remember { mutableStateOf("") }

    val bitmap = remember(uiState.fotoUri) {
        uiState.fotoUri?.let { uri ->
            try {
                if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(
                        context.contentResolver,
                        uri
                    )
                } else {
                    val source = ImageDecoder.createSource(
                        context.contentResolver,
                        uri
                    )
                    ImageDecoder.decodeBitmap(source)
                }
            } catch (e: Exception) {
                null
            }
        }
    }
    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.procesarYGuardarFoto(context, uri)
        }
    }
    val coloresTexto = OutlinedTextFieldDefaults.colors(
        focusedTextColor = RegistroDarkGreen,
        unfocusedTextColor = RegistroText,
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedBorderColor = RegistroGreen,
        unfocusedBorderColor = Color.LightGray,
        focusedLabelColor = RegistroGreen,
        unfocusedLabelColor = Color.Gray,
        cursorColor = RegistroGreen,
        errorBorderColor = Color.Red,
        errorLabelColor = Color.Red
    )
    Scaffold(
        containerColor = RegistroCream
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "MASTER MARTINI",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp,
                color = RegistroGold
            )
            Text(
                text = "Crea tu cuenta",
                fontSize = 29.sp,
                fontWeight = FontWeight.ExtraBold,
                color = RegistroGreen
            )
            Text(
                text = "Únete a nuestra comunidad",
                fontSize = 15.sp,
                color = Color(0xFF6B6966)
            )
            Box(
                modifier = Modifier
                    .size(118.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(3.dp, RegistroGold, CircleShape)
                    .clickable {
                        galeriaLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Foto de perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PhotoCamera,
                            contentDescription = "Seleccionar foto",
                            tint = RegistroGreen,
                            modifier = Modifier.size(34.dp)
                        )
                        Text(
                            text = "Foto opcional",
                            fontSize = 11.sp,
                            color = RegistroGreen
                        )
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                OutlinedTextField(
                    value = uiState.nombre,
                    onValueChange = { viewModel.onNombreChange(it) },
                    label = { Text("Nombre del emprendedor") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = uiState.errorNombre,
                    shape = RoundedCornerShape(12.dp),
                    colors = coloresTexto,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    )
                )
                OutlinedTextField(
                    value = uiState.correo,
                    onValueChange = { viewModel.onCorreoChange(it) },
                    label = { Text("Correo Gmail") },
                    placeholder = { Text("usuario@gmail.com") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = uiState.errorCorreo,
                    shape = RoundedCornerShape(12.dp),
                    colors = coloresTexto,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    )
                )
                OutlinedTextField(
                    value = uiState.contrasena,
                    onValueChange = {
                        viewModel.onContrasenaChange(it)
                        mensajeRegistro = ""
                    },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = coloresTexto,
                    visualTransformation = if (mostrarContrasena) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
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
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    )
                )
                OutlinedTextField(
                    value = uiState.confirmarContrasena,
                    onValueChange = {
                        viewModel.onConfirmarContrasenaChange(it)
                        mensajeRegistro = ""
                    },
                    label = { Text("Confirmar contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = coloresTexto,
                    visualTransformation = if (mostrarConfirmacion) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                mostrarConfirmacion = !mostrarConfirmacion
                            }
                        ) {
                            Icon(
                                imageVector = if (mostrarConfirmacion) {
                                    Icons.Rounded.VisibilityOff
                                } else {
                                    Icons.Rounded.Visibility
                                },
                                contentDescription = "Mostrar u ocultar confirmación"
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    )
                )
            }
            if (uiState.mensajeError.isNotBlank() ||
                mensajeRegistro.isNotBlank()
            ) {
                Surface(
                    color = Color(0xFFFFEAEA),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = mensajeRegistro.ifBlank {
                            uiState.mensajeError
                        },
                        color = Color(0xFFD32F2F),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
            Button(
                onClick = {
                    mensajeRegistro = ""

                    viewModel.registrarCuenta(
                        context = context,
                        contrasena = uiState.contrasena,
                        confirmarContrasena = uiState.confirmarContrasena
                    ) { exito, mensaje ->
                        if (exito) {
                            mensajeRegistro = ""
                            onRegistroExitoso()
                        } else {
                            mensajeRegistro = mensaje
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RegistroGreen
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = "Crear cuenta",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            TextButton(onClick = onVolverClick) {
                Text(
                    text = "Volver al inicio",
                    color = RegistroGreen,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Text(
                text = "Tu experiencia, al estilo Martini.",
                color = RegistroGold,
                fontSize = 12.sp
            )
        }
    }
}
