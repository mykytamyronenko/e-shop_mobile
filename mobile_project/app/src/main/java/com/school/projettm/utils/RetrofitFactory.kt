package com.school.projettm.utils

import android.content.Context.MODE_PRIVATE
import com.school.projettm.AppContext
import com.school.projettm.repositories.ApiService
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy

object RetrofitFactory {
    private val url: String = "http://10.0.2.2:5185/api/"

    // get the JWT from SharedPreferences
    private fun getJwtToken(): String? {
        val sharedPreferences = AppContext.instance.getSharedPreferences("JWT", MODE_PRIVATE)
        return sharedPreferences.getString("value", null)
    }

    // add the token to the header
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val requestBuilder: Request.Builder = chain.request().newBuilder()

            getJwtToken()?.let { token ->
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }

            chain.proceed(requestBuilder.build())
        }
        .cookieJar(JavaNetCookieJar(CookieManager().apply {
            setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        }))
        .build()

    // Instance Retrofit
    val instance: Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Service API
    val apiService: ApiService by lazy {
        instance.create(ApiService::class.java)
    }
}
