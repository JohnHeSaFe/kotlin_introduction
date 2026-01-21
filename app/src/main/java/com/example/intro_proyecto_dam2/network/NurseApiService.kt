package com.example.intro_proyecto_dam2.network

import com.example.intro_proyecto_dam2.Nurse
import retrofit2.Response
import retrofit2.http.*

interface NurseApiService {
    // Login
    @POST("nurse/login")
    suspend fun login(@Body credentials: Map<String, String>): Response<Map<String, Boolean>>

    // Register
    @POST("nurse/register")
    suspend fun register(@Body nurseData: Map<String, String>): Response<Map<String, Any>>

    // Obtain all
    @GET("nurse/index")
    suspend fun getAllNurses(): List<Nurse>

    // Obtain one by ID
    @GET("nurse/{id}")
    suspend fun getNurseById(@Path("id") id: Int): Response<Map<String, Any>>

    // Update
    @PUT("nurse/{id}")
    suspend fun updateNurse(@Path("id") id: Int, @Body nurse: Nurse): Response<Map<String, String>>

    // Delete
    @DELETE("nurse/{id}")
    suspend fun deleteNurse(@Path("id") id: Int): Response<Map<String, String>>
}