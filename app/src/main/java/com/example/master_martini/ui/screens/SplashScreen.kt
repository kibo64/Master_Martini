package com.example.master_martini.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.master_martini.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val MartiniDarkGreen = Color(0xFF18372B)
private val MartiniCream = Color(0xFFFAF7F0)

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    val scale = remember { Animatable(0.5f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch {
            scale.animateTo(targetValue = 1f, animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing))
        }
        launch {
            alpha.animateTo(targetValue = 1f, animationSpec = tween(durationMillis = 1000))
        }
        delay(1500)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MartiniDarkGreen),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo Master Martini",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(240.dp)
                .scale(scale.value)
                .alpha(alpha.value)
                .clip(RoundedCornerShape(16.dp))
        )

        Text(
            text = "TU ASISTENTE DE TALLER",
            color = MartiniCream.copy(alpha = 0.8f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 3.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp)
                .alpha(alpha.value)
        )
    }
}