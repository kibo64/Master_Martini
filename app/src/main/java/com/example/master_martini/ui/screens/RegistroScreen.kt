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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.master_martini.viewmodel.RegistroViewModel

@Composable
fun RegistroScreen(
    viewModel: RegistroViewModel,
    onRegistroExitoso: () -> Unit = {},
    onVolverClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val bitmap = remember(uiState.fotoUri) {
        uiState.fotoUri?.let { uri ->
            try {
                if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(source)
                }
            } catch (e: Exception) { null }
        }
    }

    val galeriaLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) viewModel.procesarYGuardarFoto(context, uri)
    }

    val coloresTexto = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color(0xFF18372B),
        unfocusedTextColor = Color(0xFF302D28),
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedBorderColor = Color(0xFF244D3B),
        unfocusedBorderColor = Color.LightGray,
        focusedLabelColor = Color(0xFF244D3B),
        unfocusedLabelColor = Color.Gray,
        cursorColor = Color(0xFF244D3B),
        errorBorderColor = Color.Red,
        errorLabelColor = Color.Red
    )

    Scaffold(containerColor = Color(0xFFFAF7F0)) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Crea tu Perfil", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF244D3B))
                Text("Personaliza tu experiencia Martini", fontSize = 16.sp, color = Color(0xFF6B6966))
            }

            Box(
                modifier = Modifier
                    .size(130.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(3.dp, Color(0xFFD9AD50), CircleShape)
                    .clickable { galeriaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                contentAlignment = Alignment.Center
            ) {
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Foto elegida",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Icon(
                            imageVector = Icons.Rounded.PhotoCamera,
                            contentDescription = "Subir foto",
                            tint = Color(0xFF244D3B).copy(alpha = 0.5f),
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Subir Logo",
                            fontSize = 12.sp,
                            color = Color(0xFF6B6966),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = uiState.nombre,
                    onValueChange = { viewModel.onNombreChange(it) },
                    label = { Text("Nombre del emprendedor", fontSize = 15.sp) },
                    isError = uiState.errorNombre,
                    colors = coloresTexto,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = uiState.correo,
                    onValueChange = { viewModel.onCorreoChange(it) },
                    label = { Text("Correo de contacto", fontSize = 15.sp) },
                    isError = uiState.errorCorreo,
                    colors = coloresTexto,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            if (uiState.mensajeError.isNotEmpty()) {
                Surface(color = Color(0xFFFFEAEA), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                    Text(uiState.mensajeError, color = Color(0xFFD32F2F), fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (viewModel.validarFormulario()) {
                        viewModel.guardarDatosLocales(context)
                        onRegistroExitoso()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF244D3B)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Comenzar Experiencia", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            TextButton(onClick = onVolverClick) {
                Text("Volver al Inicio", fontSize = 16.sp, color = Color(0xFF6B6966), fontWeight = FontWeight.SemiBold)
            }
        }
    }
}