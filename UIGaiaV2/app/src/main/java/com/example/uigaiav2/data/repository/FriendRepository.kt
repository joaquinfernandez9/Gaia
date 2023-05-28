package com.example.uigaiav2.data.repository

import com.example.uigaiav2.data.remote.FriendRemoteDataSource
import com.example.uigaiav2.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FriendRepository @Inject constructor(
    private val remoteDataSource: FriendRemoteDataSource
) {
    fun sendRequest(username: String, friendUsername: String): Flow<NetworkResult<Int>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.sendRequest(username, friendUsername)
            if (result is NetworkResult.Success) {
                result.data?.let {
                    emit(result)
                }
            } else if (result is NetworkResult.Error) {
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }

    fun acceptRequest(username: String, friendUsername: String): Flow<NetworkResult<Int>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.acceptRequest(username, friendUsername)
            if (result is NetworkResult.Success) {
                result.data?.let {
                    emit(result)
                }
            } else if (result is NetworkResult.Error) {
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }

    fun declineRequest(username: String, friendUsername: String): Flow<NetworkResult<Int>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.declineRequest(username, friendUsername)
            if (result is NetworkResult.Success) {
                result.data?.let {
                    emit(result)
                }
            } else if (result is NetworkResult.Error) {
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getFriends(username: String): Flow<NetworkResult<List<String>>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.getFriends(username)
            if (result is NetworkResult.Success) {
                result.data?.let {
                    emit(result)
                }
            } else if (result is NetworkResult.Error) {
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getRequests(username: String): Flow<NetworkResult<List<String>>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.getRequests(username)
            if (result is NetworkResult.Success) {
                result.data?.let {
                    emit(result)
                }
            } else if (result is NetworkResult.Error) {
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }
}