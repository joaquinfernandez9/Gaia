package com.example.uigaiav2.network.service

import com.example.uigaiav2.domain.model.Account
import com.example.uigaiav2.domain.model.dto.AccountDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AccountApi {

    @POST("/ServidorGaia-1.0-SNAPSHOT/api/login/log")
    suspend fun login(@Body acc: AccountDTO): Response<Account>

    @POST("/ServidorGaia-1.0-SNAPSHOT/api/login/register")
    suspend fun register(@Body account: Account): Response<Account>
}