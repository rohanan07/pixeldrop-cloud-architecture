package com.example.pixeldrop.presentation

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import com.example.pixeldrop.R // Make sure to import your R file
import com.example.pixeldrop.presentation.navigation.Screens

@Composable
fun SplashScreen(navController: NavController) {
    // Animation State
    val scale = remember { Animatable(0f) }

    // Animation Logic
    LaunchedEffect(key1 = true) {
        // 1. Animate the logo (Scale up with a bounce)
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 800,
                easing = {
                    OvershootInterpolator(2f).getInterpolation(it)
                }
            )
        )

        // 2. Wait for a moment (e.g., 2 seconds total)
        delay(1500L)

        // 3. Navigate to Home
        navController.navigate(Screens.MainScreen.name){
            navController.popBackStack()
        }
    }

    // UI Layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // Or your app's background color
        contentAlignment = Alignment.Center
    ) {
        // Logo with Animation
        Image(
            painter = painterResource(id = R.drawable.pixeldrop_logo), // Use your PNG here
            contentDescription = "Logo",
            modifier = Modifier
                .size(150.dp) // You can make this AS BIG as you want!
                .scale(scale.value)
        )

        // Optional: App Name below logo
        /* Text(
            text = "PixelDrop",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 180.dp)
        )
        */
    }
}