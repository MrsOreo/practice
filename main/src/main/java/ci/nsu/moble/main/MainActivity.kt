package ci.nsu.moble.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ci.nsu.moble.main.ui.theme.PracticeTheme

sealed class NavRoutes(val route: String) {
    object MainScreen : NavRoutes("main_screen")
    object InfoScreen : NavRoutes("info_screen")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.MainScreen.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(NavRoutes.MainScreen.route) {
                MainScreen(
                    onOpenSecondActivity = { colorName ->
                        val intent = Intent(context, SecondActivity::class.java).apply {
                            putExtra("KEY_COLOR", "Выбран цвет: $colorName")
                        }
                        context.startActivity(intent)
                    }
                )
            }
            composable(NavRoutes.InfoScreen.route) {
                InfoScreen()
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.ColorLens, "Colors") },
            label = { Text("Цвета") },
            selected = currentRoute == NavRoutes.MainScreen.route,
            onClick = {
                navController.navigate(NavRoutes.MainScreen.route)
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Info, "Info") },
            label = { Text("Инфо") },
            selected = currentRoute == NavRoutes.InfoScreen.route,
            onClick = {
                navController.navigate(NavRoutes.InfoScreen.route)
            }
        )
    }
}
@Composable
fun MainScreen(onOpenSecondActivity: (String) -> Unit) {
    var inputText by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf("") }

    val colors = listOf("red", "blue", "green", "yellow", "black", "white")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Введите цвет") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                val normalized = inputText.trim().lowercase()
                if (colors.contains(normalized)) {
                    selectedColor = normalized
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Найти цвет")
        }

        Button(
            onClick = {
                if (selectedColor.isNotEmpty()) {
                    onOpenSecondActivity(selectedColor)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(8.dp),
            enabled = selectedColor.isNotEmpty()
        ) {
            Text("Open SecondActivity")
        }

        Text("Доступные цвета:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(colors, key = { it }) { color ->
                Text(
                    text = color,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            when (color) {
                                "red" -> Color.Red
                                "blue" -> Color.Blue
                                "green" -> Color.Green
                                "yellow" -> Color.Yellow
                                "black" -> Color.Black
                                "white" -> Color.White
                                else -> Color.Gray
                            },
                            RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp),
                    color = if (color == "black" || color == "blue") Color.White else Color.Black
                )
            }
        }
    }
}
@Composable
fun InfoScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Экран информации", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("NavHost + NavController + Sealed Class")
    }
}
@Preview(showBackground = true)
@Composable
fun MainAppPreview() {
    PracticeTheme {
        MainApp()
    }
}