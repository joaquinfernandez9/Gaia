package com.example.uigaiav2.data.remote

import com.example.uigaiav2.data.remote.response.BaseApiResponse
import com.example.uigaiav2.network.service.TreeApi
import javax.inject.Inject

class TreeRemoteDataSource @Inject constructor(
    private val treeApi: TreeApi
) : BaseApiResponse() {

    suspend fun getTree(user: String) = safeApiCall(apiCall = { treeApi.getTree(user) })

}