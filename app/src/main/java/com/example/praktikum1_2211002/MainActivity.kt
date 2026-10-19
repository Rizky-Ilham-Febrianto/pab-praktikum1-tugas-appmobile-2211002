package com.example.praktikum1_2211002

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.praktikum1_2211002.ui.AppNavigation
import com.example.praktikum1_2211002.ui.theme.Praktikum1_2211002Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Praktikum1_2211002Theme { AppNavigation() }
        }
    }
}