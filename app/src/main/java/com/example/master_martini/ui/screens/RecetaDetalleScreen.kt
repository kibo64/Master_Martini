package com.example.master_martini.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.master_martini.data.AppDatabase
import com.example.master_martini.data.FavoritoEntity
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

private val MartiniGreen = Color(0xFF244D3B)
private val MartiniDarkGreen = Color(0xFF18372B)
private val MartiniGold = Color(0xFFD9AD50)
private val MartiniCream = Color(0xFFFAF7F0)
private val MartiniText = Color(0xFF302D28)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecetaDetalleScreen(
    youtubeId: String,
    onVolverClick: () -> Unit,
    usuarioId: Int? = null,
    onNecesitaSesion: () -> Unit = {},
    onRecetaCompletada: (VideoMartini) -> Unit = {}
) {
    val receta = remember(youtubeId) {
        videosMartini.find { it.youtubeId == youtubeId } ?: videosMartini.first()
    }

    val context = LocalContext.current
    val dao = remember { AppDatabase.getDatabase(context).favoritoDao() }
    val scope = rememberCoroutineScope()

    val favoritosFlow = remember(dao, usuarioId) {
        if (usuarioId != null) dao.obtenerFavoritos(usuarioId) else flowOf(emptyList())
    }
    val favoritos by favoritosFlow.collectAsState(initial = emptyList())
    val esFav = favoritos.any { it.titulo == receta.titulo }

    var tabSeleccionada by remember { mutableIntStateOf(0) }
    val ingredientesMarcados = remember { mutableStateMapOf<Int, Boolean>() }
    var pantallaCompleta by remember { mutableStateOf(false) }
    var modoTallerActivo by remember { mutableStateOf(false) }

    val density = LocalDensity.current
    val maxVideoHeight = 215.dp
    val minVideoHeight = 105.dp
    val maxCollapsePx = with(density) { (maxVideoHeight - minVideoHeight).toPx() }
    var videoCollapseOffsetPx by remember { mutableFloatStateOf(0f) }
    val listState = rememberLazyListState()

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                if (delta < 0f && videoCollapseOffsetPx > -maxCollapsePx) {
                    val newOffset = (videoCollapseOffsetPx + delta).coerceIn(-maxCollapsePx, 0f)
                    val consumed = newOffset - videoCollapseOffsetPx
                    videoCollapseOffsetPx = newOffset
                    return Offset(0f, consumed)
                }
                if (delta > 0f &&
                    videoCollapseOffsetPx < 0f &&
                    listState.firstVisibleItemIndex == 0 &&
                    listState.firstVisibleItemScrollOffset == 0
                ) {
                    val newOffset = (videoCollapseOffsetPx + delta).coerceIn(-maxCollapsePx, 0f)
                    val consumed = newOffset - videoCollapseOffsetPx
                    videoCollapseOffsetPx = newOffset
                    return Offset(0f, consumed)
                }
                return Offset.Zero
            }
        }
    }

    val currentVideoHeight = with(density) {
        (maxVideoHeight.toPx() + videoCollapseOffsetPx).toDp()
    }
    val mostrarControlesNativos = currentVideoHeight > 145.dp

    val exoPlayer = remember(receta.videoUrl) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(receta.videoUrl))
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(exoPlayer) {
        onDispose { exoPlayer.release() }
    }

    BackHandler(enabled = pantallaCompleta || modoTallerActivo) {
        if (pantallaCompleta) pantallaCompleta = false
        else if (modoTallerActivo) modoTallerActivo = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MartiniCream)
            .nestedScroll(nestedScrollConnection)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(currentVideoHeight)
                .background(Color.Black)
        ) {
            if (!pantallaCompleta) {
                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            player = exoPlayer
                            useController = mostrarControlesNativos
                        }
                    },
                    update = { playerView ->
                        playerView.useController = mostrarControlesNativos
                    },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Reproduciendo en pantalla completa...", color = Color.White, fontSize = 13.sp)
                }
            }

            IconButton(
                onClick = onVolverClick,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .statusBarsPadding()
                    .padding(8.dp)
                    .size(36.dp)
                    .background(Color.Black.copy(alpha = 0.55f), CircleShape)
            ) {
                Icon(Icons.Rounded.ArrowBack, "Volver", tint = Color.White, modifier = Modifier.size(19.dp))
            }

            IconButton(
                onClick = { pantallaCompleta = true },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(8.dp)
                    .size(36.dp)
                    .background(Color.Black.copy(alpha = 0.55f), CircleShape)
            ) {
                Icon(Icons.Rounded.Fullscreen, "Pantalla completa", tint = Color.White, modifier = Modifier.size(21.dp))
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MartiniGold.copy(alpha = 0.25f)
                        ) {
                            Text(
                                text = receta.categoria.uppercase(),
                                color = MartiniDarkGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Rounded.Schedule, null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(receta.duracion, color = Color.Gray, fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(Icons.Rounded.Restaurant, null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(receta.rendimiento, color = Color.Gray, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = receta.titulo,
                            color = MartiniText,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = {
                                if (usuarioId == null) onNecesitaSesion()
                                else scope.launch {
                                    if (esFav) dao.eliminarFavorito(receta.titulo, usuarioId)
                                    else dao.guardarFavorito(
                                        FavoritoEntity(
                                            titulo = receta.titulo,
                                            descripcion = receta.descripcion,
                                            duracion = receta.duracion,
                                            nivel = receta.nivel,
                                            url = receta.url,
                                            categoria = receta.categoria,
                                            usuarioId = usuarioId
                                        )
                                    )
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (esFav) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                                contentDescription = "Favorito",
                                tint = if (esFav) Color(0xFFD94848) else MartiniGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            exoPlayer.pause()
                            modoTallerActivo = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MartiniDarkGreen,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(Icons.Rounded.MenuBook, contentDescription = null, tint = MartiniGold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Iniciar Modo Taller (Paso a paso)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            stickyHeader {
                TabRow(
                    selectedTabIndex = tabSeleccionada,
                    containerColor = Color.White,
                    contentColor = MartiniDarkGreen,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[tabSeleccionada]),
                            color = MartiniGold,
                            height = 3.dp
                        )
                    }
                ) {
                    Tab(
                        selected = tabSeleccionada == 0,
                        onClick = { tabSeleccionada = 0 },
                        text = {
                            Text(
                                "Ingredientes (${receta.ingredientes.size})",
                                fontWeight = if (tabSeleccionada == 0) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    )
                    Tab(
                        selected = tabSeleccionada == 1,
                        onClick = { tabSeleccionada = 1 },
                        text = {
                            Text(
                                "Preparación (${receta.pasos.size})",
                                fontWeight = if (tabSeleccionada == 1) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            if (tabSeleccionada == 0) {
                itemsIndexed(receta.ingredientes) { index, ingrediente ->
                    val marcado = ingredientesMarcados[index] == true
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 5.dp)
                            .clickable { ingredientesMarcados[index] = !marcado },
                        shape = RoundedCornerShape(12.dp),
                        color = if (marcado) Color.White.copy(alpha = 0.6f) else Color.White,
                        shadowElevation = 1.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = marcado,
                                onCheckedChange = { ingredientesMarcados[index] = it },
                                colors = CheckboxDefaults.colors(checkedColor = MartiniGreen)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = ingrediente,
                                fontSize = 14.sp,
                                color = if (marcado) Color.Gray else MartiniText,
                                textDecoration = if (marcado) TextDecoration.LineThrough else TextDecoration.None
                            )
                        }
                    }
                }
            } else {
                itemsIndexed(receta.pasos) { index, paso ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 5.dp),
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White,
                        shadowElevation = 1.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MartiniDarkGreen,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "${index + 1}",
                                        color = MartiniGold,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = paso,
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                color = MartiniText,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }

    if (pantallaCompleta) {
        Dialog(
            onDismissRequest = { pantallaCompleta = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            player = exoPlayer
                            useController = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .align(Alignment.Center)
                )

                IconButton(
                    onClick = { pantallaCompleta = false },
                    modifier = Modifier
                        .padding(top = 48.dp, start = 16.dp)
                        .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                ) {
                    Icon(Icons.Rounded.FullscreenExit, "Salir de pantalla completa", tint = Color.White)
                }
            }
        }
    }

    if (modoTallerActivo) {
        ModoTallerDialog(
            receta = receta,
            onCerrar = { modoTallerActivo = false },
            onFinalizar = {
                modoTallerActivo = false
                onRecetaCompletada(receta)
            }
        )
    }
}

@Composable
fun ModoTallerDialog(
    receta: VideoMartini,
    onCerrar: () -> Unit,
    onFinalizar: () -> Unit
) {
    val view = LocalView.current
    var pasoActual by remember { mutableIntStateOf(0) }
    var verIngredientesRapido by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        view.keepScreenOn = true
        onDispose { view.keepScreenOn = false }
    }

    Dialog(
        onDismissRequest = onCerrar,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MartiniDarkGreen)
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "MODO TALLER • PANTALLA ACTIVA",
                        color = MartiniGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = receta.titulo,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
                IconButton(
                    onClick = onCerrar,
                    modifier = Modifier.background(Color.White.copy(alpha = 0.12f), CircleShape)
                ) {
                    Icon(Icons.Rounded.Close, "Salir del Modo Taller", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = { (pasoActual + 1).toFloat() / receta.pasos.size.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MartiniGold,
                trackColor = Color.White.copy(alpha = 0.2f),
                strokeCap = StrokeCap.Round
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MartiniCream)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MartiniDarkGreen
                        ) {
                            Text(
                                text = "PASO ${pasoActual + 1} DE ${receta.pasos.size}",
                                color = MartiniGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        TextButton(onClick = { verIngredientesRapido = !verIngredientesRapido }) {
                            Icon(Icons.Rounded.ReceiptLong, null, tint = MartiniGreen, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (verIngredientesRapido) "Ver paso" else "Ver gramajes",
                                color = MartiniGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (verIngredientesRapido) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            itemsIndexed(receta.ingredientes) { _, item ->
                                Text(
                                    text = "• $item",
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MartiniText
                                )
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            AnimatedContent(targetState = pasoActual, label = "pasoAnim") { index ->
                                Text(
                                    text = receta.pasos[index],
                                    fontSize = 24.sp,
                                    lineHeight = 34.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MartiniText
                                )
                            }
                        }
                    }

                    Text(
                        text = "Rendimiento: ${receta.rendimiento}",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { if (pasoActual > 0) pasoActual-- },
                    enabled = pasoActual > 0,
                    modifier = Modifier
                        .weight(1f)
                        .height(58.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color.White.copy(alpha = 0.4f))
                ) {
                    Icon(Icons.Rounded.ArrowBack, null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Anterior", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        if (pasoActual < receta.pasos.lastIndex) pasoActual++
                        else onFinalizar()
                    },
                    modifier = Modifier
                        .weight(1.3f)
                        .height(58.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MartiniGold,
                        contentColor = MartiniDarkGreen
                    )
                ) {
                    Text(
                        text = if (pasoActual < receta.pasos.lastIndex) "Siguiente paso" else "Finalizar receta",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = if (pasoActual < receta.pasos.lastIndex) Icons.Rounded.ArrowForward else Icons.Rounded.CheckCircle,
                        contentDescription = null
                    )
                }
            }
        }
    }
}