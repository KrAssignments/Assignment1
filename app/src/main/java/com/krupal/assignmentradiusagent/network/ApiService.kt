package com.krupal.assignmentradiusagent.network

import com.krupal.assignmentradiusagent.network.model.APIResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface ApiService {
    @GET("/iranjith4/ad-assignment/db")
    fun getData() : Single<APIResponse>
}