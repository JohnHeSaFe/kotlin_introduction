package com.example.intro_proyecto_dam2

    data class Nurse(
        val id: Int,
        val name: String,
        val email: String
    )


    val Nurses = listOf(
        Nurse(id=1,name = "Justin Suarez", email = "zjs.suarez@asd.com"),
        Nurse(id=2,name = "John Henard", email = "henardsalango@asd.com"),
        Nurse(id=3,name = "Angelo Pozo", email = "apozonigga@asd.com"),
        Nurse(id=4,name = "Marc Munta", email = "marcmunta@asd.com")
    )