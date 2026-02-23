package com.example.myapplication

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    var startAnimation by remember { mutableStateOf(false) }

    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1500),
        label = "fade_in_animation"
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2500)

        navController.navigate("main_list") {
            popUpTo("splash_screen") { inclusive = true }
        }
    }

    val finnishBlue = Color(0xFF002F6C)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(220.dp, 160.dp)
                .border(1.dp, Color(0xFFEEEEEE))
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(finnishBlue.copy(alpha = alphaAnim.value))
                    .align(Alignment.Center)
            )

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 60.dp)
                    .width(40.dp)
                    .background(finnishBlue.copy(alpha = alphaAnim.value))
                    .align(Alignment.TopStart)
            )
        }
    }
}