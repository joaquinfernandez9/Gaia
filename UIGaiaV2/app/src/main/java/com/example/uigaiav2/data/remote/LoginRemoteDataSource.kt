package com.example.uigaiav2.data.remote

import com.example.uigaiav2.data.remote.response.BaseApiResponse
import com.example.uigaiav2.domain.model.Account
import com.example.uigaiav2.domain.model.dto.AccountDTO
import com.example.uigaiav2.network.service.AccountApi
import javax.inject.Inject

class LoginRemoteDataSource  @Inject constructor(
    private val api: AccountApi
): BaseApiResponse(){

    suspend fun login(acc: AccountDTO) = safeApiCall(apiCall = { api.login(acc) })

    suspend fun register(acc: Account) = safeApiCall(apiCall = { api.register(acc) })

}