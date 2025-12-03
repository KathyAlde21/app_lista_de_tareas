package com.example.listadetareas.data.repository

import com.example.listadetareas.data.model.Task

class InMemoryTaskRepository : TaskRepository {
    private val tasks = mutableListOf<Task>()

    override fun getTasks(): List<Task> = tasks.toList()

    override fun addTask(task: Task) {
        tasks.add(task)
    }

    override fun updateTask(task: Task) {
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            tasks[index] = task
        }
    }

    override fun deleteTask(task: Task) {
        tasks.removeAll { it.id == task.id }
    }
}