package com.example.uigaiav2.data.repository

import com.example.uigaiav2.data.remote.TaskRemoteDataSource
import com.example.uigaiav2.domain.model.Task
import com.example.uigaiav2.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val remoteDataSource: TaskRemoteDataSource
){

    fun addTask(task: Task): Flow<NetworkResult<Task>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.addTask(task)
            if (result is NetworkResult.Success) {
                result.data?.let {
                    emit(result)
                }
            } else if (result is NetworkResult.Error) {
                emit(result)
            }
        }
    }

    fun getTask(task: Task): Flow<NetworkResult<Task>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.getTask(task)
            if (result is NetworkResult.Success) {
                result.data?.let {
                    emit(result)
                }
            } else if (result is NetworkResult.Error) {
                emit(result)
            }
        }
    }

    fun deleteTask(task: Task): Flow<NetworkResult<Task>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.deleteTask(task)
            if (result is NetworkResult.Success) {
                result.data?.let {
                    emit(result)
                }
            } else if (result is NetworkResult.Error) {
                emit(result)
            }
        }
    }

    fun getTasks(username: String): Flow<NetworkResult<List<Task>>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.getTasks(username)
            if (result is NetworkResult.Success) {
                result.data?.let {
                    emit(result)
                }
            } else if (result is NetworkResult.Error) {
                emit(result)
            }
        }
    }

    fun updateTask(task: Task): Flow<NetworkResult<Task>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.updateTask(task)
            if (result is NetworkResult.Success) {
                result.data?.let {
                    emit(result)
                }
            } else if (result is NetworkResult.Error) {
                emit(result)
            }
        }
    }

    fun deleteCompletedTasks(username: String): Flow<NetworkResult<Int>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.deleteCompletedTasks(username)
            if (result is NetworkResult.Success) {
                result.data?.let {
                    emit(result)
                }
            } else if (result is NetworkResult.Error) {
                emit(result)
            }
        }
    }

}