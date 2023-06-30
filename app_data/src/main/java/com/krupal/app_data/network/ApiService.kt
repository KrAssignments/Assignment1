package com.krupal.app_data.network

import com.krupal.app_data.network.entity.APIResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface ApiService {
    @GET("/iranjith4/ad-assignment/db")
    fun getData() : Single<APIResponse>
}