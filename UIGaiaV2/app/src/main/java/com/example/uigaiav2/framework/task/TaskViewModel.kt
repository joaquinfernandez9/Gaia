package com.example.uigaiav2.framework.task

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uigaiav2.data.repository.TaskRepository
import com.example.uigaiav2.data.repository.TreeRepository
import com.example.uigaiav2.domain.model.Task
import com.example.uigaiav2.domain.model.dto.TaskDTO
import com.example.uigaiav2.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    @ApplicationContext val appContext: Context,
    private val repoTask: TaskRepository,
) : ViewModel() {

    private val _state: MutableStateFlow<TaskState> by lazy {
        MutableStateFlow(TaskState())
    }

    val state: StateFlow<TaskState> get() = _state

    fun handleEvent(event: TaskEvent) {
        when (event) {
            is TaskEvent.GetTasks -> getTasks(event.username)
            is TaskEvent.AddTask -> addTask(event.task)
            is TaskEvent.DeleteTask -> deleteTask(event.task)
            is TaskEvent.UpdateTask -> updateTask(event.task)
            is TaskEvent.ErrorShown -> clearError()
        }
    }

    // TODO: with gestures
    private fun updateTask(task: TaskDTO) {
        TODO("Not yet implemented")
    }

    private fun deleteTask(task: TaskDTO) {
        TODO("Not yet implemented")
    }

    private fun addTask(taskDTO: TaskDTO) {
        val task = taskDTO.toTask()
        viewModelScope.launch {
            if (Utils.hasInternetConnection(context = appContext)) {
                repoTask.addTask(task).collect {
                    when (it) {
                        is com.example.uigaiav2.utils.NetworkResult.Error -> {
                            _state.value = TaskState(error = it.message)

                        }
                        is com.example.uigaiav2.utils.NetworkResult.Success -> {
                            _state.value = TaskState(error = null, task = it.data)
                        }
                        is com.example.uigaiav2.utils.NetworkResult.Loading -> {
                            _state.value = TaskState(error = null)
                        }
                    }
                }
            } else {
                _state.value = TaskState(error = "No internet connection")
            }
        }
    }

    private fun getTasks(username: String) {
        viewModelScope.launch {
            if (Utils.hasInternetConnection(context = appContext)) {
                repoTask.getTasks(username).collect {
                    when (it) {
                        is com.example.uigaiav2.utils.NetworkResult.Error -> {
                            _state.value = TaskState(error = it.message)
                        }
                        is com.example.uigaiav2.utils.NetworkResult.Success -> {
                            _state.value = TaskState(error = null, taskList = it.data)
                        }
                        is com.example.uigaiav2.utils.NetworkResult.Loading -> {
                            _state.value = TaskState(error = null)
                        }
                    }
                }
            } else {
                _state.value = TaskState(error = "No internet connection")
            }
        }
    }

    private fun clearError() {
        _state.value = TaskState(error = null)
    }
}