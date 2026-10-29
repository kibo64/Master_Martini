package com.example.master_martini.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.master_martini.data.AppDatabase
import com.example.master_martini.data.FavoritoEntity
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

private val FavoritosGreen = Color(0xFF244D3B)
private val FavoritosDarkGreen = Color(0xFF18372B)
private val FavoritosGold = Color(0xFFD9AD50)
private val FavoritosCream = Color(0xFFFAF7F0)
private val FavoritosText = Color(0xFF302D28)
@Composable
fun FavoritosScreen(
    onExplorarClick: () -> Unit = {},
    onVolverClick: () -> Unit = onExplorarClick,
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
                        text = "Tus videos técnicos guardados",
                        color = FavoritosCream,
                        fontSize = 13.sp
                    )
                }
                Icon(
                    imageVector = Icons.Rounded.Favorite,
                    contentDescription = null,
                    tint = FavoritosGold,
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(28.dp)
                )
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
                        Icon(
                            imageVector = Icons.Rounded.Favorite,
                            contentDescription = null,
                            tint = FavoritosGold,
                            modifier = Modifier.size(72.dp)
                        )
                        Spacer(modifier = Modifier.height(18.dp))
                        Text(
                            text = "Inicia sesión para ver tus favoritos",
                            color = FavoritosText,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = onNecesitaSesion,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = FavoritosGreen
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Iniciar sesión")
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
                        Icon(
                            imageVector = Icons.Rounded.Favorite,
                            contentDescription = null,
                            tint = FavoritosGold,
                            modifier = Modifier.size(72.dp)
                        )
                        Spacer(modifier = Modifier.height(18.dp))
                        Text(
                            text = "Aún no tienes favoritos",
                            color = FavoritosText,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Guarda tus videos técnicos favoritos desde la biblioteca y aparecerán aquí.",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            lineHeight = 21.sp
                        )
                        Spacer(modifier = Modifier.height(22.dp))

                        Button(
                            onClick = onExplorarClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = FavoritosGreen
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Explorar videos")
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
                        text = "${favoritos.size} videos guardados",
                        color = FavoritosText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        items(
                            items = favoritos,
                            key = { "${it.usuarioId}_${it.titulo}" }
                        ) { favorito ->

                            FavoritoCard(
                                favorito = favorito,
                                onAbrir = {
                                    try {
                                        val intent = Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse(favorito.url)
                                        )
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        mensaje = "No se pudo abrir el video."
                                    }
                                },
                                onEliminar = {
                                    scope.launch {
                                        try {
                                            dao.eliminarFavorito(
                                                titulo = favorito.titulo,
                                                usuarioId = usuarioId
                                            )
                                            mensaje = "Favorito eliminado."
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
    onAbrir: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(125.dp)
                    .background(FavoritosDarkGreen)
                    .clickable(onClick = onAbrir),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        modifier = Modifier.size(50.dp),
                        shape = RoundedCornerShape(50),
                        color = FavoritosGold
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Rounded.PlayArrow,
                                contentDescription = "Reproducir video",
                                tint = FavoritosDarkGreen,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "VER EN YOUTUBE",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
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
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = favorito.descripcion,
                        color = Color.Gray,
                        fontSize = 12.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.School,
                            contentDescription = null,
                            tint = FavoritosGreen,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = favorito.categoria,
                            color = FavoritosGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                IconButton(onClick = onEliminar) {
                    Icon(
                        imageVector = Icons.Rounded.DeleteOutline,
                        contentDescription = "Eliminar de favoritos",
                        tint = Color(0xFFD94848),
                        modifier = Modifier.size(27.dp)
                    )
                }
            }
        }
    }
}
