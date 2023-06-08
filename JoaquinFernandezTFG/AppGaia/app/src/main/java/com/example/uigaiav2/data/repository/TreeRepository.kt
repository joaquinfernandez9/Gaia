package com.example.uigaiav2.data.repository

import com.example.uigaiav2.data.remote.TreeRemoteDataSource
import com.example.uigaiav2.domain.model.Tree
import com.example.uigaiav2.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TreeRepository @Inject constructor(
    private val remoteDataSource: TreeRemoteDataSource
) {

    fun getTree(user: String): Flow<NetworkResult<Tree>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.getTree(user)
            if (result is NetworkResult.Success) {
                result.data?.let {
                    emit(result)
                }
            } else if (result is NetworkResult.Error) {
                emit(result)
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
}