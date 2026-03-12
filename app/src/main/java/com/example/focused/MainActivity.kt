package com.example.focused

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.focused.ui.components.CustomBottomBar
import com.example.focused.ui.theme.AccentYellow
import com.example.focused.ui.theme.BgDark
import com.example.focused.ui.theme.Black
import com.example.focused.ui.theme.FocusedTheme
import com.example.focused.ui.theme.White
import com.yourpackage.name.ui.navigation.Screen
import com.yourpackage.name.ui.navigation.SetupNavGraph
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            FocusedTheme { // This is your app's theme wrapper
//                SetupNavGraph() // Calling our navigation brain
//            }
//        }
//    }
//}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make the app "Edge-to-Edge" for a premium look
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            FocusedTheme {
                // Background surface to match your theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BgDark // Or GrayBg depending on your Analytics screen
                ) {
                    MainAppContainer()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FocusedTheme {
        Greeting("Android")
    }
}
@Composable
fun MainAppContainer() {
    val navController = rememberNavController()

    // Observe current destination to highlight the right icon
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Black,


        bottomBar = {
            // Hide bottom bar on Splash screen
            if (currentRoute != Screen.Splash.route) {
                CustomBottomBar(currentRoute) { route ->
                    navController.navigate(route) {
                        // Avoid building up a large stack of destinations
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination
                        launchSingleTop = true
                        // Restore state when re-selecting a previously selected item
                        restoreState = true
                    }
                }
            }
        }
    ) { innerPadding ->
        // Apply the scaffold's padding to our NavHost
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            SetupNavGraph(navController)
        }
    }
}