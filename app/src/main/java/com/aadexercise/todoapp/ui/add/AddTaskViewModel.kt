package com.aadexercise.todoapp.ui.add

import androidx.lifecycle.*
import com.aadexercise.todoapp.data.Task
import com.aadexercise.todoapp.data.TaskRepository
import kotlinx.coroutines.launch

class AddTaskViewModel(private val taskRepository: TaskRepository): ViewModel() {
    fun addTask(task: Task) = viewModelScope.launch{ taskRepository.insertTask(task) }
}