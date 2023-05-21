package com.example.uigaia.network.service

import com.example.uigaia.domain.model.Account
import retrofit2.Response
import retrofit2.http.*

interface AccountService {
    @POST("/login")
    suspend fun login(@Header("Authorization") authorization: String): Response<Account>
}