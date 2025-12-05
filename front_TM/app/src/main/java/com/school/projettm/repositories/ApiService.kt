package com.school.projettm.repositories

import com.school.projettm.models.LoginRequest
import com.school.projettm.models.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("user/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}