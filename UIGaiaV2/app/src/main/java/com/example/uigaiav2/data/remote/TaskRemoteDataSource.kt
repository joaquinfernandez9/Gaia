package com.example.uigaiav2.data.remote

import com.example.uigaiav2.data.remote.response.BaseApiResponse
import com.example.uigaiav2.domain.model.Task
import com.example.uigaiav2.network.service.TaskApi
import javax.inject.Inject

class TaskRemoteDataSource @Inject  constructor(
    private val api: TaskApi
): BaseApiResponse(){
    suspend fun addTask(task: Task) = safeApiCall(apiCall = { api.addTask(task) })
    suspend fun getTask(task: Task) = safeApiCall(apiCall = { api.getTask(task.username, task.taskName) })
    suspend fun deleteTask(task: Task) = safeApiCall(apiCall = { api.deleteTask(task.username, task.taskName) })
    suspend fun getTasks(username: String) = safeApiCall(apiCall = { api.getTasks(username) })
    suspend fun updateTask(task: Task) = safeApiCall(apiCall = { api.updateTask(task) })
    suspend fun deleteCompletedTasks(username: String) = safeApiCall(apiCall = { api.deleteCompletedTasks(username) })
}