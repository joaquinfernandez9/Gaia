package com.example.uigaiav2.framework.tree

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uigaiav2.data.repository.LoginRepository
import com.example.uigaiav2.data.repository.TaskRepository
import com.example.uigaiav2.data.repository.TreeRepository
import com.example.uigaiav2.domain.model.Tree
import com.example.uigaiav2.framework.login.LoginState
import com.example.uigaiav2.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TreeViewModel @Inject constructor(
    @ApplicationContext val appContext: Context,
    private val repo: TreeRepository,
    private val repoTask: TaskRepository,
) : ViewModel() {

    private val _state: MutableStateFlow<TreeState> by lazy {
        MutableStateFlow(TreeState())
    }

    val state: StateFlow<TreeState> get() = _state

    fun handleEvent(event: TreeEvent) {
        when (event) {
            is TreeEvent.GetTree -> getTree(event.username)
            is TreeEvent.DeleteCompletedTasks -> deleteCompletedTasks(event.username)
            is TreeEvent.ClearError -> clearError()
            is TreeEvent.ClearCompletedTasks -> clearCompletedTasks()
        }
    }

    private fun clearCompletedTasks() {
        _state.value = _state.value.copy(completedTasks = 0)
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }


    private fun getTree(username: String) {
        viewModelScope.launch {
            if (Utils.hasInternetConnection(context = appContext)){
                repo.getTree(username).collect {
                    when(it){
                        is com.example.uigaiav2.utils.NetworkResult.Error -> {
                            _state.value = _state.value.copy(
                                error = it.message,
                                tree = null
                            )
                        }
                        is com.example.uigaiav2.utils.NetworkResult.Success -> {
                            _state.value = _state.value.copy(
                                error = null,
                                tree = it.data
                            )
                        }
                        is com.example.uigaiav2.utils.NetworkResult.Loading -> {
                            _state.value = _state.value.copy(
                                error = null,
                                tree = null
                            )
                        }
                    }
                }
            } else{
                _state.value = _state.value.copy(
                    error = "No internet connection",
                    tree = null
                )
            }
        }
    }

    private fun deleteCompletedTasks(username: String) {
        viewModelScope.launch {
            if(Utils.hasInternetConnection(context = appContext)) {
                repoTask.deleteCompletedTasks(username).collect {
                    when(it){
                        is com.example.uigaiav2.utils.NetworkResult.Error -> {
                            _state.value = _state.value.copy(
                                error = it.message,
                                tree = null
                            )
                        }
                        is com.example.uigaiav2.utils.NetworkResult.Success -> {
                            _state.value = _state.value.copy(
                                error = null,
                                completedTasks = it.data
                            )
                        }
                        is com.example.uigaiav2.utils.NetworkResult.Loading -> {
                            _state.value = _state.value.copy(
                                error = null,
                                tree = null
                            )
                        }
                    }
                }
            } else{
                _state.value = _state.value.copy(
                    error = "No internet connection",
                    tree = null
                )
            }
        }
    }
}