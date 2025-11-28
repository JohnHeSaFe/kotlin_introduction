package com.example.intro_proyecto_dam2.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.example.intro_proyecto_dam2.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp





@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(onNavToSearch = {})
}


@Composable
fun MainScreen(onNavToSearch: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.nurse_surface_blue)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Presentación
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.nurse_logo),
                contentDescription = "Nurse logo",
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "NurseApp",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.nurse_primary)
            )

            Text(
                text = "Gestión de guardia inteligente",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = colorResource(id = R.color.nurse_text_secondary)
            )
        }

        // Botones
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 50.dp, start = 30.dp, end = 30.dp), // Márgenes laterales
            verticalArrangement = Arrangement.spacedBy(16.dp) // Espacio entre botones
        ) {
            BotonPrincipal(text = "Iniciar Sesión", onClick = {})
            BotonPrincipal(text = "Registrarse", onClick = {})
            BotonPrincipal(text = "Buscar Enfermero", onClick = { onNavToSearch() })
            BotonSecundario(text = "Información Enfermeros", onClick = {})
        }
    }
}

@Composable
fun BotonPrincipal(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.nurse_primary),
            contentColor = colorResource(R.color.nurse_background_light)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 6.dp,
            pressedElevation = 2.dp
        )
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun BotonSecundario(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.nurse_background_light),
            contentColor = colorResource(R.color.nurse_primary)
        )
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}