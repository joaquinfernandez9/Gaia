package com.example.uigaiav2.data.repository

import com.example.uigaiav2.data.remote.LoginRemoteDataSource
import com.example.uigaiav2.domain.model.Account
import com.example.uigaiav2.domain.model.dto.AccountDTO
import com.example.uigaiav2.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class LoginRepository @Inject constructor(
    private val remoteDataSource: LoginRemoteDataSource
) {

    fun login(acc: AccountDTO): Flow<NetworkResult<Account>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.login(acc)
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


    fun register(acc: Account): Flow<NetworkResult<Account>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.register(acc)
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

