package com.example.intro_proyecto_dam2

    data class Nurse(
        val id: Int,
        val email: String,
        val first_name: String,
        val last_name: String,
        val password: String,
        val profile_picture: String? = null
    )
