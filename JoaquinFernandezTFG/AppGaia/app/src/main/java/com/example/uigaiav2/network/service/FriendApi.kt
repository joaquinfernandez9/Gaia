package com.example.uigaiav2.network.service

import com.example.uigaiav2.domain.model.Friend
import com.example.uigaiav2.domain.model.Task
import com.example.uigaiav2.domain.model.Tree
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FriendApi {
    @POST("/ServidorGaia-1.0-SNAPSHOT/api/friend/sendRequest/{username}/{friend}")
    suspend fun sendRequest(
        @Path("username") user: String,
        @Path("friend") friend: String
    ): Response<Int>
    @POST("/ServidorGaia-1.0-SNAPSHOT/api/friend/acceptRequest/{username}/{friend}")
    suspend fun acceptRequest(
        @Path("username") user: String,
        @Path("friend") friend: String
    ): Response<Int>
    @DELETE("/ServidorGaia-1.0-SNAPSHOT/api/friend/rejectRequest/{username1}/{username2}")
    suspend fun rejectRequest(
        @Path("username1") user: String,
        @Path("username2") friend: String
    ): Response<Int>
    @GET("/ServidorGaia-1.0-SNAPSHOT/api/friend/getFriends/{username}")
    suspend fun getFriends(
        @Path("username") user: String
    ): Response<List<Friend>>
    @GET("/ServidorGaia-1.0-SNAPSHOT/api/friend/getRequests/{username}")
    suspend fun getRequests(
        @Path("username") user: String
    ): Response<List<Friend>>

    @GET("/ServidorGaia-1.0-SNAPSHOT/api/friend/getFriendsTree/{username}")
    suspend fun getFriendsTree(@Path("username") user: String): Response<List<Tree>>

}