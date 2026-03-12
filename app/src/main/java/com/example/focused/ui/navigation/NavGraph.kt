package com.yourpackage.name.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.focused.ui.screens.AnalyticsScreen
import com.example.focused.ui.screens.SplashScreen
import com.example.focused.ui.screens.DashboardScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash_route")      // Make these unique and clear
    object Dashboard : Screen("dashboard_route")
    object Analytics : Screen("analytics_route")
    object AppSelection : Screen("app_selection_route")
}
@Composable
fun SetupNavGraph(navController: NavHostController) {


    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route // Setting Splash as the first screen
    ) {
        // Splash Screen Route
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = navController)
        }

        // Dashboard Route
        composable(route = Screen.Dashboard.route) {
            DashboardScreen(navController = navController)
        }

        // Analytics Route
        composable(route = Screen.Analytics.route) {
            AnalyticsScreen(navController = navController)
        }

        // App Selection Route (We will build this next)
        composable(route = Screen.AppSelection.route) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("App Selection Screen", color = Color.White)
            }
        }
    }
}