package com.school.projettm.repositories

import com.school.projettm.dtos.CreateArticleCommand
import com.school.projettm.dtos.CreateArticleResponse
import com.school.projettm.dtos.GetAllArticleResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface IArticleRepository {
    @GET("articles")
    suspend fun getAll(): List<GetAllArticleResponse>

    @Multipart
    @POST("articles")
    suspend fun createArticle(
        @Part("Title") title: RequestBody,
        @Part("Description") description: RequestBody,
        @Part("Price") price: RequestBody,
        @Part("Category") category: RequestBody,
        @Part("State") state: RequestBody,
        @Part("UserId") userId: RequestBody,
        @Part("CreatedAt") createdAt: RequestBody,
        @Part("UpdatedAt") updatedAt: RequestBody,
        @Part("Status") status: RequestBody,
        @Part("Quantity") quantity: RequestBody,
        @Part image: MultipartBody.Part
    ): CreateArticleResponse}