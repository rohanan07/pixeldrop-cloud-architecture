package com.example.pixeldrop.presentation.Home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun MainScreen() {
    // State to track which tab is active
    var currentScreen by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Home) }

    Scaffold(
        topBar = { TopAppBar() },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = Color.White
    ) { paddingValues ->

        // Root Box to overlay the Bottom Bar on top of content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // 1. The Screen Content (Swaps based on selection)
            Box(modifier = Modifier.fillMaxSize()) {
                when (currentScreen) {
                    BottomNavItem.Home -> HomeScreen() // Your existing Home Screen
                    BottomNavItem.Search -> Text("Search Screen", modifier = Modifier.align(Alignment.Center))
                    BottomNavItem.Favorites -> Text("Favorites Screen", modifier = Modifier.align(Alignment.Center))
                }
            }

            // 2. The Floating Navigation Bar (Aligned to Bottom Center)
            Box(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                FloatingBottomNavigation(
                    currentScreen = currentScreen,
                    onScreenSelected = { newScreen -> currentScreen = newScreen }
                )
            }
        }
    }
}