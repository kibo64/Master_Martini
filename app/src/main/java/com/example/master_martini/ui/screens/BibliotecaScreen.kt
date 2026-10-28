package com.example.master_martini.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SignalCellularAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BibliotecaScreen(onVolverClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF7F0))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        Text(
            text = "Academia Martini",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF244D3B)
        )

        VideoCardPro(
            titulo = "Técnicas de Temperado",
            descripcion = "Aprende a cristalizar chocolate Caravella para bombones perfectos.",
            duracion = "12 min",
            nivel = "Intermedio"
        )

        VideoCardPro(
            titulo = "Cremas Vegetales",
            descripcion = "Logra el batido ideal y mayor rendimiento con Decor Up.",
            duracion = "8 min",
            nivel = "Básico"
        )
    }
}

@Composable
fun VideoCardPro(titulo: String, descripcion: String, duracion: String, nivel: String) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .clickable {  }
    ) {
        Column {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Color(0xFF18372B)),
                contentAlignment = Alignment.Center
            ) {

                Surface(
                    shape = CircleShape,
                    color = Color(0xFFD9AD50).copy(alpha = 0.95f),
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Icon(
                            imageVector = Icons.Rounded.PlayArrow,
                            contentDescription = "Reproducir $titulo",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF18372B)
                )

                Text(
                    text = descripcion,
                    fontSize = 14.sp,
                    color = Color(0xFF6B6966),
                    lineHeight = 20.sp
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                    Surface(
                        color = Color(0xFFF4F5F0),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.AccessTime,
                                contentDescription = "Duración",
                                tint = Color(0xFF244D3B),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = duracion,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF244D3B)
                            )
                        }
                    }

                    Surface(
                        color = Color(0xFFFFF7E6),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.SignalCellularAlt,
                                contentDescription = "Nivel",
                                tint = Color(0xFFD9AD50),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = nivel,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFD9AD50)
                            )
                        }
                    }
                }
            }
        }
    }
}