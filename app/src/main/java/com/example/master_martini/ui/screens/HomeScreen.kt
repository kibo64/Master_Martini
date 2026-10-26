package com.example.master_martini.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.master_martini.R

private val MartiniGreen = Color(0xFF244D3B)
private val MartiniDarkGreen = Color(0xFF18372B)
private val MartiniGold = Color(0xFFD9AD50)
private val MartiniCream = Color(0xFFFAF7F0)
private val MartiniText = Color(0xFF302D28)

@Composable
fun HomeScreen(
    onBibliotecaClick: () -> Unit = {},
    onRegistroClick: () -> Unit = {},
    onPerfilClick: () -> Unit = {}) {
    Scaffold(containerColor = MartiniCream,
        bottomBar = { NavigationBar(containerColor = Color.White, contentColor = MartiniGreen) {
            NavigationBarItem(selected = true, onClick = {}, icon = { Text("⌂", fontSize = 24.sp) }, label = { Text("Inicio") })
            NavigationBarItem(selected = false, onClick = onBibliotecaClick, icon = { Text("▤", fontSize = 22.sp) }, label = { Text("Biblioteca") })
            NavigationBarItem(selected = false, onClick = {}, icon = { Text("♡", fontSize = 24.sp) }, label = { Text("Favoritos") })
            NavigationBarItem(selected = false, onClick = onPerfilClick, icon = { Text("♙", fontSize = 22.sp) }, label = { Text("Perfil") })
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "MASTER MARTINI", fontSize = 23.sp, fontWeight = FontWeight.ExtraBold, color = MartiniGreen)

                    Text(text = "Inspiración para profesionales", fontSize = 13.sp, color = MartiniText)
                }

                Surface(shape = RoundedCornerShape(50), color = Color(0xFFE9EBDD)) {
                    Text(text = "\u2728", modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp), color = MartiniGreen, fontSize = 23.sp)
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth().background(brush = Brush.linearGradient(listOf(MartiniGreen, MartiniDarkGreen)), shape = RoundedCornerShape(26.dp)).padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp))
            {
                Surface(color = MartiniGold, shape = RoundedCornerShape(50)) {
                    Text(text = "ESCUELA DE SABORES", modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), color = MartiniDarkGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Text(text = "Tu próxima creación comienza aquí", color = Color.White, fontSize = 27.sp, lineHeight = 33.sp, fontWeight = FontWeight.Bold)
                Text(text = "Descubre recetas, aprende nuevas técnicas " + "y lleva tus ideas al siguiente nivel.", color = Color(0xFFE6EDE7), fontSize = 15.sp, lineHeight = 22.sp)

                Button(onClick = onBibliotecaClick, colors = ButtonDefaults.buttonColors(containerColor = MartiniGold, contentColor = MartiniDarkGreen), shape = RoundedCornerShape(12.dp)) {
                    Text(text = "Explorar recetas  →", fontWeight = FontWeight.Bold)
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(text = "Descubre nuestro catálogo", fontSize = 21.sp, fontWeight = FontWeight.Bold, color = MartiniText)
                Text(text = "Encuentra inspiración para cada creación.", fontSize = 14.sp, color = Color.DarkGray)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    CategoryCard(imageRes = R.drawable.panaderia, title = "Panadería", modifier = Modifier.weight(1f), onClick = onBibliotecaClick)
                    CategoryCard(imageRes = R.drawable.pasteleria, title = "Pastelería", modifier = Modifier.weight(1f), onClick = onBibliotecaClick)
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    CategoryCard(imageRes = R.drawable.chocolate, title = "Chocolate", modifier = Modifier.weight(1f), onClick = onBibliotecaClick)
                    CategoryCard(imageRes = R.drawable.gelateria, title = "Gelatería", modifier = Modifier.weight(1f), onClick = onBibliotecaClick)
                }
            }

            Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onRegistroClick), colors = CardDefaults.cardColors(containerColor = Color(0xFFE9EBDD)), shape = RoundedCornerShape(18.dp)) {
                Row(modifier = Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Personaliza tu experiencia", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = MartiniGreen)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = "Selecciona tus intereses y descubre " + "contenidos de tu interes.", fontSize = 13.sp, color = MartiniText, lineHeight = 19.sp)
                    }
                    Text(text = "→", fontSize = 27.sp, color = MartiniGreen)
                }
            }
            Text(text = "Aprende · Crea · Inspírate", modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp), textAlign = TextAlign.Center, fontSize = 13.sp, color = MartiniGreen)
        }
    }
}

@Composable
private fun CategoryCard(imageRes: Int, title: String, modifier: Modifier = Modifier,onClick: () -> Unit) {
    Card(modifier = modifier.height(155.dp).clickable(onClick = onClick), shape = RoundedCornerShape(18.dp), elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(painter = painterResource(id = imageRes), contentDescription = title, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.80f)))))
            Text(text = title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.BottomStart).padding(14.dp))
        }
    }
}
