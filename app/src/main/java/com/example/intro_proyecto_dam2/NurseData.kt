package com.example.intro_proyecto_dam2

    data class Nurse(
        val id: Int,
        val email: String,
        val first_name: String,
        val last_name: String,
        val password: String,
        val profile_picture: Int? = null
    )

val Nurses = listOf(
    Nurse(id=1,first_name = "Justin", last_name = "Suarez", email = "zjs.suarez@asd.com",password="asd1", profile_picture = R.drawable.asd),
    Nurse(id=2,first_name = "John", last_name= "Salango", email = "henardsalango@asd.com",password="asd2", profile_picture = R.drawable.dawdsawdsawd),
    Nurse(id=3, first_name = "Angelo", last_name = "Pozo" ,email = "apozonigga@asd.com",password="asd3", profile_picture = R.drawable.dsadasd),
    Nurse(id=4, first_name = "Marc", last_name ="Munta", email = "marcmunta@asd.com",password="asd4", profile_picture = R.drawable.wdsawdsad)
)