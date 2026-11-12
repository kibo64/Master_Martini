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
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.PlayArrow
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

private val MartiniGreen = Color(0xFF244D3B)
private val MartiniDarkGreen = Color(0xFF18372B)
private val MartiniGold = Color(0xFFD9AD50)
private val MartiniCream = Color(0xFFFAF7F0)
private val MartiniText = Color(0xFF302D28)

data class VideoMartini(
    val titulo: String,
    val descripcion: String,
    val duracion: String,
    val nivel: String,
    val url: String,
    val categoria: String
)
private val videosMartini = listOf(
    VideoMartini(
        "Pudin de Chía | ORASI COCO",
        "Preparación con productos ORASI COCO",
        "",
        "Técnico",
        "https://www.youtube.com/watch?v=8n7kAGbS5uE",
        "Chocolatería"
    ),
    VideoMartini(
        "Mousse chocolate blanco | ARIBA",
        "Preparación de mousse de chocolate blanco",
        "",
        "Técnico",
        "https://www.youtube.com/watch?v=-8KFtkMyntY",
        "Chocolatería"
    ),
    VideoMartini(
        "Torta Chocolate Avellana | CARAVELLA DECO",
        "Preparación de torta de chocolate y avellana",
        "",
        "Técnico",
        "https://www.youtube.com/watch?v=ZwOkabO177M",
        "Chocolatería"
    ),
    VideoMartini(
        "Tarta de coco | ORASÍ COCO",
        "Preparación de tarta de coco",
        "",
        "Técnico",
        "https://www.youtube.com/watch?v=vmC2JpTvrLM",
        "Pastelería"
    ),
    VideoMartini(
        "¿Cómo utilizar crema pastelera, merengue o crema chantilly EN POLVO? | DELICE",
        "Técnicas de preparación con productos DELICE",
        "",
        "Técnico",
        "https://www.youtube.com/watch?v=1U1twaw6bP0",
        "Pastelería"
    ),
    VideoMartini(
        "Queque navideño | VITA BAKE",
        "Preparación de queque navideño",
        "",
        "Técnico",
        "https://www.youtube.com/watch?v=xoTHn4_-Fhs",
        "Panadería"
    ),
    VideoMartini(
        "Helado Artesanal Frutos del Bosque | GELATO MARTINI",
        "Preparación de helado artesanal de frutos del bosque",
        "",
        "Técnico",
        "https://www.youtube.com/watch?v=R2N_oOG0xI8",
        "Gelatería"
    ),
    VideoMartini(
        "Helado Artesanal de Avellana | BASE BLANCA MARTINI GELATO",
        "Preparación de helado artesanal de avellana",
        "",
        "Técnico",
        "https://www.youtube.com/watch?v=KhhA9pAJZxA",
        "Gelatería"
    ),
    VideoMartini(
        "Café helado | BASE VAINILLA PARA HELADOS | MARTINI GELATO",
        "Preparación de café helado",
        "",
        "Técnico",
        "https://www.youtube.com/watch?v=mGTlgfD_x5Y",
        "Gelatería"
    )
)
@Composable
fun BibliotecaScreen(
    categoriaInicial: String? = null,
    onVolverClick: () -> Unit = {},
    onFavoritosClick: () -> Unit = {},
    usuarioId: Int? = null,
    onNecesitaSesion: () -> Unit = {}
) {
    val context = LocalContext.current
    val dao = remember {
        AppDatabase.getDatabase(context).favoritoDao()
    }
    val scope = rememberCoroutineScope()
    val favoritosFlow = remember(dao, usuarioId) {
        if (usuarioId != null) {
            dao.obtenerFavoritos(usuarioId)
        } else {
            flowOf(emptyList<FavoritoEntity>())
        }
    }
    val favoritos by favoritosFlow.collectAsState(initial = emptyList())
    var categoriaSeleccionada by remember(categoriaInicial) {
        mutableStateOf(categoriaInicial)
    }
    var mensaje by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbarHostState.showSnackbar(it)
            mensaje = null
        }
    }
    val videosFiltrados = remember(categoriaSeleccionada) {
        if (categoriaSeleccionada.isNullOrBlank()) {
            videosMartini
        } else {
            videosMartini.filter {
                it.categoria.equals(
                    categoriaSeleccionada,
                    ignoreCase = true
                )
            }
        }
    }
    fun esFavorito(video: VideoMartini): Boolean {
        return favoritos.any { it.titulo == video.titulo }
    }
    fun abrirVideo(video: VideoMartini) {
        try {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(video.url)
            )
            context.startActivity(intent)
        } catch (e: Exception) {
            mensaje = "No se pudo abrir el video."
        }
    }
    fun alternarFavorito(video: VideoMartini) {
        val id = usuarioId
        if (id == null) {
            mensaje = "Inicia sesión para guardar tus favoritos."
            return
        }
        scope.launch {
            try {
                if (esFavorito(video)) {
                    dao.eliminarFavorito(
                        titulo = video.titulo,
                        usuarioId = id
                    )
                    mensaje = "Video eliminado de tus favoritos."
                } else {
                    dao.guardarFavorito(
                        FavoritoEntity(
                            titulo = video.titulo,
                            descripcion = video.descripcion,
                            duracion = video.duracion,
                            nivel = video.nivel,
                            url = video.url,
                            categoria = video.categoria,
                            usuarioId = id
                        )
                    )
                    mensaje = "Video agregado a tus favoritos."
                }
            } catch (e: Exception) {
                mensaje = "No se pudo actualizar el favorito."
            }
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        containerColor = MartiniCream
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MartiniCream)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MartiniGreen)
                    .padding(horizontal = 16.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onVolverClick) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Academia Martini",
                        color = Color.White,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Cursos y videos técnicos",
                        color = MartiniCream,
                        fontSize = 13.sp
                    )
                }
                IconButton(onClick = onFavoritosClick) {
                    Icon(
                        imageVector = Icons.Rounded.Favorite,
                        contentDescription = "Ver favoritos",
                        tint = MartiniGold
                    )
                }
            }
            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 8.dp
                )
            ) {
                Text(
                    text = categoriaSeleccionada ?: "Todos los cursos",
                    color = MartiniText,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val categorias = listOf(
                        "Todos",
                        "Chocolatería",
                        "Pastelería",
                        "Panadería",
                        "Gelatería"
                    )

                    categorias.forEach { categoria ->
                        val seleccionada =
                            if (categoria == "Todos") {
                                categoriaSeleccionada == null
                            } else {
                                categoriaSeleccionada == categoria
                            }

                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    categoriaSeleccionada =
                                        if (categoria == "Todos") {
                                            null
                                        } else {
                                            categoria
                                        }
                                },
                            shape = RoundedCornerShape(12.dp),
                            color = if (seleccionada) {
                                MartiniGreen
                            } else {
                                Color.White
                            },
                            shadowElevation = 2.dp
                        ) {
                            Box(
                                modifier = Modifier.padding(
                                    horizontal = 3.dp,
                                    vertical = 10.dp
                                ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = when (categoria) {
                                        "Chocolatería" -> "Chocolate"
                                        else -> categoria
                                    },
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (seleccionada) {
                                        Color.White
                                    } else {
                                        MartiniText
                                    },
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
            if (videosFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay videos en esta categoría.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 24.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(
                        items = videosFiltrados,
                        key = { it.titulo }
                    ) { video ->
                        VideoCardPro(
                            video = video,
                            guardado = esFavorito(video),
                            onReproducir = { abrirVideo(video) },
                            onFavoritoClick = {
                                alternarFavorito(video)
                            }
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun VideoCardPro(
    video: VideoMartini,
    guardado: Boolean,
    onReproducir: () -> Unit,
    onFavoritoClick: () -> Unit
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
                    .height(145.dp)
                    .background(MartiniDarkGreen)
                    .clickable(onClick = onReproducir),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MartiniGold,
                        modifier = Modifier.size(54.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Rounded.PlayArrow,
                                contentDescription = "Reproducir video",
                                tint = MartiniDarkGreen,
                                modifier = Modifier.size(38.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "VER VIDEO EN YOUTUBE",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = MartiniGold
                ) {
                    Text(
                        text = video.categoria.uppercase(),
                        color = MartiniDarkGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(
                            horizontal = 9.dp,
                            vertical = 6.dp
                        )
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = video.titulo,
                        color = MartiniText,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = video.descripcion,
                        color = Color.Gray,
                        fontSize = 12.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.School,
                            contentDescription = null,
                            tint = MartiniGreen,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = video.nivel,
                            color = MartiniGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = onFavoritoClick) {
                    Icon(
                        imageVector = if (guardado) {
                            Icons.Rounded.Favorite
                        } else {
                            Icons.Rounded.FavoriteBorder
                        },
                        contentDescription = if (guardado) {
                            "Quitar de favoritos"
                        } else {
                            "Agregar a favoritos"
                        },
                        tint = if (guardado) {
                            Color(0xFFD94848)
                        } else {
                            MartiniGreen
                        },
                        modifier = Modifier.size(27.dp)
                    )
                }
            }
        }
    }
}
