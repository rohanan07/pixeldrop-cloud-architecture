package com.example.pixeldrop.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pixeldrop.presentation.Home.MainScreen
import com.example.pixeldrop.presentation.SplashScreen

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.SplashScreen.name){
        composable(Screens.SplashScreen.name) {
            SplashScreen(navController)
        }
        composable(Screens.MainScreen.name) {
            MainScreen()
        }
    }
}