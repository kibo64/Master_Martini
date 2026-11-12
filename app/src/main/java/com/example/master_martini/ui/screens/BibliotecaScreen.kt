package com.example.master_martini.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.master_martini.data.AppDatabase
import com.example.master_martini.data.FavoritoEntity
import java.text.Normalizer
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

private val MartiniGreen = Color(0xFF244D3B)
private val MartiniDarkGreen = Color(0xFF18372B)
private val MartiniGold = Color(0xFFD9AD50)
private val MartiniCream = Color(0xFFFAF7F0)
private val MartiniBorder = Color(0xFFE6E0D4)
private val MartiniText = Color(0xFF302D28)

private fun normalizarTexto(texto: String): String {
    val sinAcentos = Normalizer.normalize(texto, Normalizer.Form.NFD)
        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
    return sinAcentos.lowercase().trim()
}

@Composable
fun BibliotecaScreen(
    categoriaInicial: String? = null,
    onVolverClick: () -> Unit = {},
    onFavoritosClick: () -> Unit = {},
    onVideoClick: (VideoMartini) -> Unit = {},
    usuarioId: Int? = null,
    onNecesitaSesion: () -> Unit = {}
) {
    val context = LocalContext.current
    val dao = remember { AppDatabase.getDatabase(context).favoritoDao() }
    val scope = rememberCoroutineScope()

    val favoritosFlow = remember(dao, usuarioId) {
        if (usuarioId != null) dao.obtenerFavoritos(usuarioId) else flowOf(emptyList())
    }
    val favoritos by favoritosFlow.collectAsState(initial = emptyList())

    var categoriaSeleccionada by remember(categoriaInicial) { mutableStateOf(categoriaInicial) }
    var textoBusqueda by remember { mutableStateOf("") }

    val videosFiltrados = remember(categoriaSeleccionada, textoBusqueda) {
        val porCategoria = if (
            categoriaSeleccionada.isNullOrBlank() ||
            categoriaSeleccionada == "Todas" ||
            categoriaSeleccionada == "Todos"
        ) {
            videosMartini
        } else {
            videosMartini.filter { it.categoria.equals(categoriaSeleccionada, ignoreCase = true) }
        }

        val consultaNormalizada = normalizarTexto(textoBusqueda)
        if (consultaNormalizada.isBlank()) {
            porCategoria
        } else {
            porCategoria.filter { video ->
                normalizarTexto(video.titulo).contains(consultaNormalizada) ||
                        normalizarTexto(video.descripcion).contains(consultaNormalizada) ||
                        normalizarTexto(video.categoria).contains(consultaNormalizada) ||
                        video.ingredientes.any { normalizarTexto(it).contains(consultaNormalizada) }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MartiniCream)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MartiniGreen)
                .statusBarsPadding()
                .padding(top = 18.dp, bottom = 18.dp, start = 12.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolverClick, modifier = Modifier.size(40.dp)) {
                Icon(Icons.Rounded.ArrowBack, "Volver", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Academia Martini",
                    color = Color.White,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Técnicas y preparaciones oficiales",
                    color = MartiniCream.copy(alpha = 0.85f),
                    fontSize = 13.sp
                )
            }
            IconButton(
                onClick = { if (usuarioId == null) onNecesitaSesion() else onFavoritosClick() },
                modifier = Modifier
                    .size(44.dp)
                    .background(Color.White.copy(alpha = 0.12f), CircleShape)
            ) {
                Icon(Icons.Rounded.Favorite, "Ver favoritos", tint = MartiniGold)
            }
        }

        OutlinedTextField(
            value = textoBusqueda,
            onValueChange = { textoBusqueda = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 14.dp),
            placeholder = {
                Text(
                    text = "Buscar receta o marca (Ariba, Caravella, Orasí...)",
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Buscar",
                    tint = MartiniGreen
                )
            },
            trailingIcon = {
                if (textoBusqueda.isNotBlank()) {
                    IconButton(onClick = { textoBusqueda = "" }) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Limpiar búsqueda",
                            tint = Color.Gray
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MartiniDarkGreen,
                unfocusedTextColor = MartiniText,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = MartiniGreen,
                unfocusedBorderColor = MartiniBorder,
                cursorColor = MartiniGreen
            )
        )

        val categorias = listOf("Todos", "Chocolatería", "Pastelería", "Panadería", "Gelatería")
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categorias) { categoria ->
                val esTodos = categoria == "Todos"
                val seleccionada = if (esTodos) {
                    categoriaSeleccionada == null || categoriaSeleccionada == "Todas" || categoriaSeleccionada == "Todos"
                } else {
                    categoriaSeleccionada == categoria
                }

                Surface(
                    onClick = {
                        categoriaSeleccionada = if (esTodos) "Todos" else categoria
                    },
                    shape = RoundedCornerShape(24.dp),
                    color = if (seleccionada) MartiniDarkGreen else Color.White,
                    border = if (!seleccionada) BorderStroke(1.dp, MartiniBorder) else null
                ) {
                    Text(
                        text = categoria,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (seleccionada) Color.White else MartiniText,
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 9.dp)
                    )
                }
            }
        }

        if (videosFiltrados.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(28.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        shape = CircleShape,
                        color = MartiniGold.copy(alpha = 0.16f),
                        border = BorderStroke(1.5.dp, MartiniGold.copy(alpha = 0.45f)),
                        modifier = Modifier.size(96.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = null,
                                tint = MartiniGold,
                                modifier = Modifier.size(44.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Sin recetas para \"$textoBusqueda\"",
                        color = MartiniDarkGreen,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Prueba con otra marca o cambia la categoría seleccionada.",
                        color = Color.Gray,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            textoBusqueda = ""
                            categoriaSeleccionada = "Todos"
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MartiniGreen,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "Mostrar todas las recetas",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.None
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(videosFiltrados, key = { it.youtubeId }) { video ->
                    val esFav = favoritos.any { it.titulo == video.titulo }
                    VideoCardPremium(
                        video = video,
                        guardado = esFav,
                        onReproducir = { onVideoClick(video) },
                        onFavoritoClick = {
                            if (usuarioId == null) {
                                onNecesitaSesion()
                            } else {
                                scope.launch {
                                    if (esFav) {
                                        dao.eliminarFavorito(video.titulo, usuarioId)
                                    } else {
                                        dao.guardarFavorito(
                                            FavoritoEntity(
                                                titulo = video.titulo,
                                                descripcion = video.descripcion,
                                                duracion = video.duracion,
                                                nivel = video.nivel,
                                                url = video.url,
                                                categoria = video.categoria,
                                                usuarioId = usuarioId
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun VideoCardPremium(
    video: VideoMartini,
    guardado: Boolean,
    onReproducir: () -> Unit,
    onFavoritoClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onReproducir),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, MartiniBorder),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
            ) {
                AsyncImage(
                    model = video.thumbnailUrl,
                    contentDescription = video.titulo,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.65f)),
                                startY = 90f
                            )
                        )
                )

                Surface(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopStart),
                    shape = RoundedCornerShape(8.dp),
                    color = MartiniGold
                ) {
                    Text(
                        text = video.categoria.uppercase(),
                        color = MartiniDarkGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                Surface(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.BottomEnd),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Black.copy(alpha = 0.7f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Schedule,
                            contentDescription = null,
                            tint = MartiniGold,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = video.duracion,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.92f),
                    modifier = Modifier
                        .size(54.dp)
                        .align(Alignment.Center)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.PlayArrow,
                        contentDescription = "Reproducir",
                        tint = MartiniDarkGreen,
                        modifier = Modifier
                            .padding(11.dp)
                            .fillMaxSize()
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = video.titulo,
                        color = MartiniText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = video.descripcion,
                        color = Color.Gray,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                IconButton(onClick = onFavoritoClick) {
                    Icon(
                        imageVector = if (guardado) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (guardado) Color(0xFFD94848) else Color.LightGray,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}