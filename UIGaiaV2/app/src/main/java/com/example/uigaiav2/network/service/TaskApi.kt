package com.example.uigaiav2.network.service

import com.example.uigaiav2.domain.model.Task
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskApi {

    @POST("/ServidorGaia-1.0-SNAPSHOT/api/task/addTask")
    suspend fun addTask(@Body task: Task): Response<Task>

    @GET("/ServidorGaia-1.0-SNAPSHOT/api/task/getTask/{username}/{taskName}")
    suspend fun getTask(
        @Path("username") user: String,
        @Path("taskName") taskName: String
    ): Response<Task>

    @DELETE("/ServidorGaia-1.0-SNAPSHOT/api/task/deleteTask/{username}/{taskName}")
    suspend fun deleteTask(
        @Path("username") user: String,
        @Path("taskName") taskName: String
    ): Response<Task>

    @GET("/ServidorGaia-1.0-SNAPSHOT/api/task/getTasks/{username}")
    suspend fun getTasks(@Path("username") user: String): Response<List<Task>>

    @PUT("/ServidorGaia-1.0-SNAPSHOT/api/task/updateTask")
    suspend fun updateTask(@Body task: Task): Response<Task>

    @PUT("/ServidorGaia-1.0-SNAPSHOT/api/task/deleteCompletedTasks/{username}")
    suspend fun deleteCompletedTasks(@Path("username") user: String): Response<Int>

}