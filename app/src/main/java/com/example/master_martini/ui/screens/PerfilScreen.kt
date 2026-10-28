package com.example.master_martini.ui.screens

import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Person
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
import androidx.compose.ui.graphics.vector.ImageVector
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF7F0))
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Mi Perfil",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF244D3B)
        )

        if (uiState.nombre.isBlank()) {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(20.dp))
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AccountCircle,
                        contentDescription = "Sin perfil",
                        tint = Color(0xFFD9AD50).copy(alpha = 0.5f),
                        modifier = Modifier.size(72.dp)
                    )
                    Text(
                        text = "Aún no has creado tu perfil",
                        fontSize = 16.sp,
                        color = Color(0xFF6B6966),
                        fontWeight = FontWeight.Medium
                    )
                    Button(
                        onClick = onIrARegistroClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9AD50)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = "Unirse a la comunidad",
                            fontSize = 16.sp,
                            color = Color(0xFF18372B),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {

            Surface(
                color = Color.White,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(20.dp))
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF4F5F0))
                            .border(2.dp, Color(0xFFD9AD50), CircleShape),
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
                            Icon(
                                imageVector = Icons.Rounded.Person,
                                contentDescription = "Usuario",
                                tint = Color(0xFF244D3B).copy(alpha = 0.6f),
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = uiState.nombre,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF18372B)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = uiState.correo,
                            fontSize = 14.sp,
                            color = Color(0xFF6B6966)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Ajustes y Soporte",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6B6966),
                    modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
                )

                MenuOptionRow(
                    icon = Icons.Rounded.Call,
                    title = "Hablar con un asesor",
                    iconColor = Color(0xFF244D3B),
                    textColor = Color(0xFF18372B),
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:+56912345678") }
                        context.startActivity(intent)
                    }
                )

                MenuOptionRow(
                    icon = Icons.AutoMirrored.Rounded.ExitToApp,
                    title = "Cerrar sesión",
                    iconColor = Color(0xFFD32F2F),
                    textColor = Color(0xFFD32F2F),
                    showArrow = false,
                    onClick = {
                        viewModel.cerrarSesion(context)
                        onVolverClick()
                    }
                )
            }
        }
    }
}

@Composable
fun MenuOptionRow(
    icon: ImageVector,
    title: String,
    iconColor: Color,
    textColor: Color,
    showArrow: Boolean = true,
    onClick: () -> Unit
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = iconColor.copy(alpha = 0.1f),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = iconColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor,
                modifier = Modifier.weight(1f)
            )

            if (showArrow) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = "Ir",
                    tint = Color.LightGray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}