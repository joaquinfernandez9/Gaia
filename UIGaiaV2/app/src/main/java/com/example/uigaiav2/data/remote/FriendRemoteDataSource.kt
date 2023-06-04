package com.example.uigaiav2.data.remote

import com.example.uigaiav2.data.remote.response.BaseApiResponse
import com.example.uigaiav2.network.service.FriendApi
import javax.inject.Inject

class FriendRemoteDataSource @Inject constructor(
  private val api: FriendApi
): BaseApiResponse(){
    suspend fun sendRequest(username: String, friendUsername: String) = safeApiCall(apiCall = { api.sendRequest(username, friendUsername) })
    suspend fun acceptRequest(username: String, friendUsername: String) = safeApiCall(apiCall = { api.acceptRequest(username, friendUsername) })
    suspend fun declineRequest(username: String, friendUsername: String) = safeApiCall(apiCall = { api.rejectRequest(username, friendUsername) })
    suspend fun getFriends(username: String) = safeApiCall(apiCall = { api.getFriends(username) })
    suspend fun getRequests(username: String) = safeApiCall(apiCall = { api.getRequests(username) })
    suspend fun getFriendsTree(username: String) = safeApiCall(apiCall = { api.getFriendsTree(username) })


}