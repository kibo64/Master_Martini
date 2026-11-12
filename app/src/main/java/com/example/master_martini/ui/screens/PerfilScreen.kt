package com.example.master_martini.ui.screens

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.master_martini.data.AppDatabase
import com.example.master_martini.viewmodel.RegistroViewModel
import kotlinx.coroutines.flow.flowOf

private val PerfilGreen = Color(0xFF244D3B)
private val PerfilDarkGreen = Color(0xFF18372B)
private val PerfilGold = Color(0xFFD9AD50)
private val PerfilCream = Color(0xFFFAF7F0)
private val PerfilBorder = Color(0xFFE6E0D4)

@Composable
fun PerfilScreen(
    viewModel: RegistroViewModel,
    onVolverClick: () -> Unit = {},
    onIrARegistroClick: () -> Unit = {},
    onIrALoginClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val dao = remember { AppDatabase.getDatabase(context).favoritoDao() }

    val favoritosFlow = remember(dao, uiState.usuarioId) {
        val id = uiState.usuarioId
        if (id != null) dao.obtenerFavoritos(id) else flowOf(emptyList())
    }
    val favoritos by favoritosFlow.collectAsState(initial = emptyList())

    val completadas = uiState.recetasCompletadas
    val totalCatalogo = videosMartini.size
    val rangoTecnico = when {
        completadas.size >= 6 -> "Maestro Artesano Martini"
        completadas.size >= 3 -> "Especialista Técnico"
        completadas.size >= 1 -> "Pastelero en Formación"
        else -> "Emprendedor Asociado"
    }

    val bitmap = remember(uiState.fotoUri) {
        uiState.fotoUri?.let { uri ->
            try {
                context.contentResolver.openInputStream(uri)?.use {
                    BitmapFactory.decodeStream(it)?.asImageBitmap()
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PerfilCream)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Mi Perfil",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = PerfilGreen
        )

        if (!uiState.sesionActiva) {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(22.dp),
                border = BorderStroke(1.dp, PerfilBorder),
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = PerfilGold.copy(alpha = 0.16f),
                        border = BorderStroke(1.5.dp, PerfilGold.copy(alpha = 0.45f)),
                        modifier = Modifier.size(96.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Rounded.AccountCircle,
                                contentDescription = null,
                                tint = PerfilGold,
                                modifier = Modifier.size(52.dp)
                            )
                        }
                    }

                    Text(
                        text = "Bienvenido a Master Martini",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = PerfilDarkGreen,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Inicia sesión o crea una cuenta para registrar tu progreso técnico y recetas guardadas.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = onIrALoginClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PerfilGreen
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            "Iniciar sesión",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    OutlinedButton(
                        onClick = onIrARegistroClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        border = BorderStroke(1.5.dp, PerfilGreen),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = PerfilGreen
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            "Crear cuenta",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, PerfilBorder),
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
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
                            .border(2.dp, PerfilGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (bitmap != null) {
                            Image(
                                bitmap = bitmap,
                                contentDescription = "Foto de perfil",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Rounded.Person,
                                contentDescription = null,
                                tint = PerfilGreen,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = uiState.nombre,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = PerfilDarkGreen
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = uiState.correo,
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = PerfilGold.copy(alpha = 0.25f)
                        ) {
                            Text(
                                text = rangoTecnico.uppercase(),
                                color = PerfilDarkGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Surface(
                color = PerfilDarkGreen,
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.EmojiEvents,
                            contentDescription = null,
                            tint = PerfilGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Academia Técnica Martini",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            color = Color.White.copy(alpha = 0.1f)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "${completadas.size} / $totalCatalogo",
                                    color = PerfilGold,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = "Talleres finalizados",
                                    color = PerfilCream.copy(alpha = 0.85f),
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            color = Color.White.copy(alpha = 0.1f)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "${favoritos.size}",
                                    color = Color.White,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = "Recetas guardadas",
                                    color = PerfilCream.copy(alpha = 0.85f),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    LinearProgressIndicator(
                        progress = { (completadas.size.toFloat() / totalCatalogo.toFloat()).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = PerfilGold,
                        trackColor = Color.White.copy(alpha = 0.2f),
                        strokeCap = StrokeCap.Round
                    )

                    if (completadas.isNotEmpty()) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Preparaciones certificadas:",
                                color = PerfilGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            completadas.forEach { titulo ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Rounded.CheckCircle,
                                        contentDescription = null,
                                        tint = PerfilGold,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = titulo,
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "Completa una preparación desde el Modo Taller para sumar certificaciones a tu perfil.",
                            color = PerfilCream.copy(alpha = 0.75f),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Ajustes y soporte",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 4.dp)
                )
                MenuOptionRow(
                    icon = Icons.Rounded.Call,
                    title = "Hablar con un asesor",
                    iconColor = PerfilGreen,
                    textColor = PerfilDarkGreen,
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:+56912345678")
                        }
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
                        viewModel.cerrarSesion()
                        onVolverClick()
                    }
                )
            }
        }
    }
}

@Composable
fun MenuOptionRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    iconColor: Color,
    textColor: Color,
    showArrow: Boolean = true,
    onClick: () -> Unit
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, PerfilBorder),
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
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
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
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