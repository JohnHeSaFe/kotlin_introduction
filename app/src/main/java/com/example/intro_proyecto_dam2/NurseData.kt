package com.example.intro_proyecto_dam2

    data class Nurse(
        val id: Int,
        val email: String,
        val first_name: String,
        val last_name: String,
        val password: String
    )


    val Nurses = listOf(
        Nurse(id=1,first_name = "Justin", last_name = "Suarez", email = "zjs.suarez@asd.com",password="asd1"),
        Nurse(id=2,first_name = "John Henard", last_name= "Henard", email = "henardsalango@asd.com",password="asd2"),
        Nurse(id=3, first_name = "Angelo Pozo", last_name = "Pozo" ,email = "apozonigga@asd.com",password="asd3"),
        Nurse(id=4, first_name = "Marc Munta", last_name ="Munta", email = "marcmunta@asd.com",password="asd4")
    )