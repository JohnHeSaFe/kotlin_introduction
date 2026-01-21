package com.example.intro_proyecto_dam2.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    // 10.0.2.2 is the alias for "localhost"
    private const val BASE_URL = "http://10.0.2.2:8080/"

    val api: NurseApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NurseApiService::class.java)
    }
}