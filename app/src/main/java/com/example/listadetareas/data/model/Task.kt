package com.example.listadetareas.data.model

data class Task(
    val id: Long,
    val title: String,
    val isCompleted: Boolean = false
)