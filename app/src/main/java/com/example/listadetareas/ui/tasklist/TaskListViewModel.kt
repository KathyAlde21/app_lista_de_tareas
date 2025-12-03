package com.example.listadetareas.ui.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.listadetareas.data.model.Task
import com.example.listadetareas.data.repository.InMemoryTaskRepository
import com.example.listadetareas.data.repository.TaskRepository

class TaskListViewModel(
    private val repository: TaskRepository = InMemoryTaskRepository()
) : ViewModel() {

    private val _tasks = MutableLiveData<List<Task>>(repository.getTasks())
    val tasks: LiveData<List<Task>> = _tasks

    private var nextId = 1L

    fun addTask(title: String) {
        if (title.isBlank()) return

        val newTask = Task(
            id = nextId++,
            title = title.trim()
        )
        repository.addTask(newTask)
        refreshTasks()
    }

    fun toggleTaskCompleted(task: Task) {
        val updated = task.copy(isCompleted = !task.isCompleted)
        repository.updateTask(updated)
        refreshTasks()
    }

    fun deleteTask(task: Task) {
        repository.deleteTask(task)
        refreshTasks()
    }

    private fun refreshTasks() {
        _tasks.value = repository.getTasks()
    }
}