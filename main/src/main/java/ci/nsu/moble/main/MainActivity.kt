package ci.nsu.moble.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.ui.theme.PracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                ColorApp()
            }
        }
    }
}
@Composable
fun ColorApp() {
    val colorPalette = mapOf(
        "red" to Color.Red,
        "blue" to Color.Blue,
        "green" to Color.Green,
        "yellow" to Color.Yellow,
        "black" to Color.Black,
        "white" to Color.White,
        "cyan" to Color.Cyan,
        "pink" to Color.Magenta
    )

    var inputColor by remember { mutableStateOf("") }
    var buttonColor by remember { mutableStateOf(Color(0xFF000000)) }
    var buttonText by remember { mutableStateOf("Найти и применить цвет") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(
            text = "Введите название цвета (red, blue, green...):",
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = inputColor,
            onValueChange = { inputColor = it },
            label = { Text("red") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)

        )

        Button(
            onClick = {
                val normalizedInput = inputColor.trim().lowercase()
                if (colorPalette.containsKey(normalizedInput)) {
                    val foundColor = colorPalette[normalizedInput]
                    foundColor?.let {
                        buttonColor = it
                    }
                    Log.d("ColorLab", "Цвет '$normalizedInput' найден и применен.")
                } else {
                    Log.d("ColorLab", "Пользовательский цвет '$normalizedInput' не найден.")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor // Цвет фона кнопки
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = buttonText,
                color = if (buttonColor == Color.Black || buttonColor == Color.Blue) Color.White else Color.Black
            )
        }

        Text(
            text = "Доступные цвета:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(colorPalette.keys.toList()) { colorName ->
                ColorItem(colorName, colorPalette[colorName] ?: Color.Gray)
            }
        }
    }
}

@Composable
fun ColorItem(colorName: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(
                color = color,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = colorName,
            color = if (color == Color.Black || color == Color.Blue) Color.White else Color.Black
        )
    }
}
@Preview(showBackground = true)
@Composable
fun ColorAppPreview() {
    PracticeTheme {
        ColorApp()
    }
}