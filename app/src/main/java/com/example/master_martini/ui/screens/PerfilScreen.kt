package com.example.master_martini.ui.screens

import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.master_martini.viewmodel.RegistroViewModel

@Composable
fun PerfilScreen(
    viewModel: RegistroViewModel,
    onVolverClick: () -> Unit = {},
    onIrARegistroClick: () -> Unit = {}
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

    Scaffold(containerColor = Color(0xFFFAF7F0)) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Mi Espacio", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF244D3B))

            if (uiState.nombre.isBlank()) {
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("👋", fontSize = 48.sp)
                        Text("Aún no tienes un perfil creado.", fontSize = 16.sp, color = Color(0xFF6B6966))
                        Button(
                            onClick = onIrARegistroClick,
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9AD50)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Unirse a la comunidad", fontSize = 16.sp, color = Color(0xFF18372B), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {

                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth().shadow(6.dp, RoundedCornerShape(20.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(110.dp).clip(CircleShape).background(Color(0xFFF4F5F0)).border(3.dp, Color(0xFFD9AD50), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (bitmap != null) {
                                Image(bitmap = bitmap.asImageBitmap(), contentDescription = "Foto elegida", contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                            } else {
                                Text("👤", fontSize = 40.sp)
                            }
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(uiState.nombre, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF18372B))
                            Text(uiState.correo, fontSize = 15.sp, color = Color.Gray)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:+56912345678") }
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF244D3B)),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("📞 Hablar con un Asesor", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.SemiBold) }

                OutlinedButton(
                    onClick = {
                        viewModel.cerrarSesion(context)
                        onVolverClick()
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD32F2F)),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("🚪 Cerrar Sesión", fontSize = 16.sp, fontWeight = FontWeight.Bold) }
            }

            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = onVolverClick) { Text("Volver al Inicio", fontSize = 16.sp, color = Color(0xFF6B6966), fontWeight = FontWeight.SemiBold) }
        }
    }
}