package com.example.master_martini.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.master_martini.R
import kotlinx.coroutines.delay

private val MartiniGreen = Color(0xFF244D3B)
private val MartiniDarkGreen = Color(0xFF18372B)
private val MartiniGold = Color(0xFFD9AD50)
private val MartiniCream = Color(0xFFFAF7F0)
private val MartiniText = Color(0xFF302D28)

@Composable
fun HomeScreen(
    nombreUsuario: String = "Emprendedor",
    onBibliotecaClick: () -> Unit = {},
    onFavoritosClick: () -> Unit = {},
    onRegistroClick: () -> Unit = {},
    onPerfilClick: () -> Unit = {}
) {
    var mostrarBanner by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        mostrarBanner = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MartiniCream)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "MASTER MARTINI", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = MartiniGreen)
                Text(text = "Hola, $nombreUsuario", fontSize = 18.sp, color = MartiniText)
            }
            Surface(
                shape = CircleShape,
                color = Color(0xFFE9EBDD),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(
                        imageVector = Icons.Rounded.AutoAwesome,
                        contentDescription = "Premium",
                        tint = MartiniGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(26.dp))
                .background(brush = Brush.linearGradient(listOf(MartiniGreen, MartiniDarkGreen)), shape = RoundedCornerShape(26.dp))
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(color = MartiniGold, shape = RoundedCornerShape(50)) {
                Text(text = "ESCUELA DE SABORES", modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp), color = MartiniDarkGreen, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
            }
            Text(text = "Tu próxima creación comienza aquí", color = Color.White, fontSize = 28.sp, lineHeight = 34.sp, fontWeight = FontWeight.Bold)
            Text(text = "Descubre recetas y aprende nuevas técnicas.", color = Color(0xFFE6EDE7), fontSize = 16.sp, lineHeight = 22.sp)

            Button(
                onClick = onBibliotecaClick,
                colors = ButtonDefaults.buttonColors(containerColor = MartiniGold, contentColor = MartiniDarkGreen),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Explorar recetas", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = "Ir a recetas",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(text = "Catálogo de Productos", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MartiniText)

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CategoryCard(imageRes = R.drawable.panaderia, title = "Panadería", modifier = Modifier.weight(1f), onClick = onBibliotecaClick)
                CategoryCard(imageRes = R.drawable.pasteleria, title = "Pastelería", modifier = Modifier.weight(1f), onClick = onBibliotecaClick)
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(text = "Cursos y Videos Técnicos", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MartiniText)

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CategoryCard(imageRes = R.drawable.chocolate, title = "Chocolatería", modifier = Modifier.weight(1f), onClick = onBibliotecaClick)
                CategoryCard(imageRes = R.drawable.gelateria, title = "Gelatería", modifier = Modifier.weight(1f), onClick = onBibliotecaClick)
            }
        }

        AnimatedVisibility(
            visible = mostrarBanner,
            enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn()
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(18.dp))
                    .clip(RoundedCornerShape(18.dp))
                    .clickable(onClick = onRegistroClick),
                color = Color(0xFFE9EBDD)
            ) {
                Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Personaliza tu experiencia", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MartiniGreen)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Selecciona tus intereses comerciales.", fontSize = 15.sp, color = MartiniText, lineHeight = 20.sp)
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = "Personalizar",
                        tint = MartiniGreen,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        Text(text = "Aprende · Crea · Inspírate", modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp, top = 8.dp), textAlign = TextAlign.Center, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = MartiniGreen.copy(alpha = 0.7f))
    }
}

@Composable
private fun CategoryCard(imageRes: Int, title: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier
            .height(160.dp)
            .shadow(4.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick),
        color = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))))
            )
            Text(
                text = title,
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            )
        }
    }
}