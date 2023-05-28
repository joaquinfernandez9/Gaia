package com.example.uigaiav2.network.service

import com.example.uigaiav2.domain.model.Tree
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TreeApi {
    @GET("/ServidorGaia-1.0-SNAPSHOT/api/tree/getTree/{username}")
    suspend fun getTree(@Path("username") user: String): Response<Tree>
}