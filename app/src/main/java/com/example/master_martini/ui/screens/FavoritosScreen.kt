package com.example.master_martini.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Schedule
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.master_martini.data.AppDatabase
import com.example.master_martini.data.FavoritoEntity
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

private val FavoritosGreen = Color(0xFF244D3B)
private val FavoritosDarkGreen = Color(0xFF18372B)
private val FavoritosGold = Color(0xFFD9AD50)
private val FavoritosCream = Color(0xFFFAF7F0)
private val FavoritosBorder = Color(0xFFE6E0D4)
private val FavoritosText = Color(0xFF302D28)

@Composable
fun FavoritosScreen(
    onExplorarClick: () -> Unit = {},
    onVolverClick: () -> Unit = onExplorarClick,
    onVideoClick: (String) -> Unit = {},
    usuarioId: Int? = null,
    onNecesitaSesion: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dao = remember {
        AppDatabase.getDatabase(context).favoritoDao()
    }
    val favoritosFlow = remember(dao, usuarioId) {
        if (usuarioId != null) {
            dao.obtenerFavoritos(usuarioId)
        } else {
            flowOf(emptyList<FavoritoEntity>())
        }
    }
    val favoritos by favoritosFlow.collectAsState(
        initial = emptyList()
    )
    val snackbarHostState = remember { SnackbarHostState() }
    var mensaje by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbarHostState.showSnackbar(it)
            mensaje = null
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        contentWindowInsets = WindowInsets(0.dp),
        containerColor = FavoritosCream
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(FavoritosCream)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FavoritosGreen)
                    .statusBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onVolverClick) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Mis Favoritos",
                        color = Color.White,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Tus recetas técnicas guardadas",
                        color = FavoritosCream.copy(alpha = 0.85f),
                        fontSize = 13.sp
                    )
                }
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.12f),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Rounded.Favorite,
                            contentDescription = null,
                            tint = FavoritosGold,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            if (usuarioId == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = FavoritosGold.copy(alpha = 0.16f),
                            border = BorderStroke(1.5.dp, FavoritosGold.copy(alpha = 0.45f)),
                            modifier = Modifier.size(104.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Rounded.Favorite,
                                    contentDescription = null,
                                    tint = FavoritosGold,
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Inicia sesión para ver tus favoritos",
                            color = FavoritosDarkGreen,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Accede a tu cuenta para sincronizar las recetas que guardes en tu taller.",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(22.dp))
                        Button(
                            onClick = onNecesitaSesion,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = FavoritosGreen,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.height(50.dp),
                            contentPadding = PaddingValues(horizontal = 28.dp)
                        ) {
                            Text(
                                text = "Iniciar sesión",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            } else if (favoritos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = FavoritosGold.copy(alpha = 0.16f),
                            border = BorderStroke(1.5.dp, FavoritosGold.copy(alpha = 0.45f)),
                            modifier = Modifier.size(104.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Rounded.Favorite,
                                    contentDescription = null,
                                    tint = FavoritosGold,
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Aún no tienes favoritos",
                            color = FavoritosDarkGreen,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Guarda tus recetas técnicas desde la biblioteca para acceder rápido en tu taller.",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            lineHeight = 21.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = onExplorarClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = FavoritosGreen,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.height(50.dp),
                            contentPadding = PaddingValues(horizontal = 28.dp)
                        ) {
                            Text(
                                text = "Explorar recetas",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = "${favoritos.size} recetas guardadas",
                        color = FavoritosDarkGreen,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        items(
                            items = favoritos,
                            key = { "${it.usuarioId}_${it.titulo}" }
                        ) { favorito ->
                            val recetaAsociada = remember(favorito) {
                                videosMartini.find {
                                    it.titulo == favorito.titulo || it.url == favorito.url
                                }
                            }
                            val youtubeId = recetaAsociada?.youtubeId
                                ?: favorito.url.substringAfter("v=").substringBefore("&")

                            FavoritoCard(
                                favorito = favorito,
                                thumbnailUrl = recetaAsociada?.thumbnailUrl ?: "",
                                onAbrir = { onVideoClick(youtubeId) },
                                onEliminar = {
                                    scope.launch {
                                        try {
                                            dao.eliminarFavorito(
                                                titulo = favorito.titulo,
                                                usuarioId = usuarioId
                                            )
                                            mensaje = "Receta eliminada de favoritos."
                                        } catch (e: Exception) {
                                            mensaje = "No se pudo eliminar el favorito."
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoritoCard(
    favorito: FavoritoEntity,
    thumbnailUrl: String,
    onAbrir: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onAbrir),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, FavoritosBorder),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(175.dp)
                    .background(FavoritosDarkGreen)
            ) {
                if (thumbnailUrl.isNotBlank()) {
                    AsyncImage(
                        model = thumbnailUrl,
                        contentDescription = favorito.titulo,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.65f)),
                                startY = 80f
                            )
                        )
                )

                Surface(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopStart),
                    shape = RoundedCornerShape(8.dp),
                    color = FavoritosGold
                ) {
                    Text(
                        text = favorito.categoria.uppercase(),
                        color = FavoritosDarkGreen,
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
                            tint = FavoritosGold,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = favorito.duracion,
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
                        .size(52.dp)
                        .align(Alignment.Center)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.PlayArrow,
                        contentDescription = "Abrir receta",
                        tint = FavoritosDarkGreen,
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxSize()
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = favorito.titulo,
                        color = FavoritosText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = favorito.descripcion,
                        color = Color.Gray,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                IconButton(onClick = onEliminar) {
                    Icon(
                        imageVector = Icons.Rounded.DeleteOutline,
                        contentDescription = "Eliminar de favoritos",
                        tint = Color(0xFFD94848),
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }
    }
}