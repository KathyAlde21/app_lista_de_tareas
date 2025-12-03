package com.example.listadetareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.example.listadetareas.ui.tasklist.TaskListScreen
import com.example.listadetareas.ui.theme.ListaDeTareasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ListaDeTareasApp()
        }
    }
}

@Composable
fun ListaDeTareasApp() {
    ListaDeTareasTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            TaskListScreen()
        }
    }
}