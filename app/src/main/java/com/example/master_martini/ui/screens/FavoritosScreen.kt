package com.example.master_martini.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FavoritosScreen(onVolverClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF7F0))
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Text(
            text = "Mis Favoritos",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF244D3B)
        )

        Spacer(modifier = Modifier.weight(1f))

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

                Surface(
                    shape = CircleShape,
                    color = Color(0xFFFFEAEA),
                    modifier = Modifier.size(72.dp)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Icon(
                            imageVector = Icons.Rounded.FavoriteBorder,
                            contentDescription = "Sin favoritos",
                            tint = Color(0xFFD32F2F), // Rojo elegante
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                Text(
                    text = "Aún no hay favoritos",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF18372B)
                )

                Text(
                    text = "Guarda tus recetas y técnicas de Master Martini para tenerlas siempre a mano.",
                    fontSize = 14.sp,
                    color = Color(0xFF6B6966),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onVolverClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9AD50)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "Explorar contenido",
                        fontSize = 16.sp,
                        color = Color(0xFF18372B),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1.5f))
    }
}