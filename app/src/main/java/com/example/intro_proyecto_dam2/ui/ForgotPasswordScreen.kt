package com.example.intro_proyecto_dam2.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.intro_proyecto_dam2.R

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    ForgotPasswordScreen(
        isDarkMode = false,
        isSpanish = true,
        onDarkModeChange = {},
        onLanguageChange = {},
        onNavigateBack = {}
    )
}

@Composable
fun ForgotPasswordScreen(
    isDarkMode: Boolean,
    isSpanish: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onLanguageChange: (Boolean) -> Unit,
    onNavigateBack: () -> Unit
) {
    // Estados del formulario
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var menuExpanded by remember { mutableStateOf(false) }

    // Colores según el tema
    val bgColor = colorResource(if (isDarkMode) R.color.background_night else R.color.background)
    val primaryColor = colorResource(if (isDarkMode) R.color.primary_night else R.color.primary)
    val secondaryColor = colorResource(if (isDarkMode) R.color.secondary_night else R.color.light)
    val textColor = if (isDarkMode) Color.White else Color(0xFF1A3A5C) // Blanco en oscuro, azul oscuro en claro

    // Textos según idioma
    val title = stringResource(if (isSpanish) R.string.forgot_title_es else R.string.forgot_title_en)
    val subtitle = stringResource(if (isSpanish) R.string.forgot_subtitle_es else R.string.forgot_subtitle_en)
    val emailLabel = stringResource(if (isSpanish) R.string.forgot_email_es else R.string.forgot_email_en)
    val submitBtn = stringResource(if (isSpanish) R.string.forgot_submit_es else R.string.forgot_submit_en)
    val backToLogin = stringResource(if (isSpanish) R.string.forgot_back_es else R.string.forgot_back_en)
    val errorEmpty = stringResource(if (isSpanish) R.string.forgot_error_empty_es else R.string.forgot_error_empty_en)
    val errorInvalid = stringResource(if (isSpanish) R.string.forgot_error_invalid_es else R.string.forgot_error_invalid_en)
    val success = stringResource(if (isSpanish) R.string.forgot_success_es else R.string.forgot_success_en)
    val langOption = stringResource(if (isSpanish) R.string.menu_lang_to_en else R.string.menu_lang_to_es)
    val themeOption = stringResource(
        if (isSpanish) {
            if (isDarkMode) R.string.menu_theme_light_es else R.string.menu_theme_dark_es
        } else {
            if (isDarkMode) R.string.menu_theme_light_en else R.string.menu_theme_dark_en
        }
    )

    // Colores para los inputs
    val inputColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedBorderColor = primaryColor,
        unfocusedBorderColor = if (isDarkMode) Color.White else Color(0xFF1A3A5C),
        focusedLabelColor = primaryColor,
        unfocusedLabelColor = if (isDarkMode) Color.White else Color(0xFF1A3A5C),
        cursorColor = primaryColor,
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Barra superior
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Botón volver
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Image(
                    painter = painterResource(if (isDarkMode) R.drawable.back_icon_dark else R.drawable.back_icon_light),
                    contentDescription = "Volver",
                    modifier = Modifier.size(32.dp)
                )
            }

            // Menú configuración
            Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                IconButton(onClick = { menuExpanded = true }) {
                    Image(
                        painterResource(if (isDarkMode) R.drawable.settings_icon_dark else R.drawable.settings_icon_light),
                        contentDescription = "Configuración",
                        modifier = Modifier.size(46.dp)
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(langOption) },
                        onClick = {
                            onLanguageChange(!isSpanish)
                            menuExpanded = false
                        },
                        leadingIcon = {
                            Image(
                                painter = painterResource(if (isSpanish) R.drawable.uk_language_icon else R.drawable.spain_languange_icon),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(themeOption) },
                        onClick = {
                            onDarkModeChange(!isDarkMode)
                            menuExpanded = false
                        },
                        leadingIcon = {
                            Image(
                                painter = painterResource(if (isDarkMode) R.drawable.theme_icon_light else R.drawable.theme_icon_dark),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Logo
        Image(
            painter = painterResource(if (isDarkMode) R.drawable.nurse_logo_dark else R.drawable.nurse_logo_light),
            contentDescription = "Logo",
            modifier = Modifier.size(120.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Título
        Text(
            text = title,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Subtítulo explicativo
        Text(
            text = subtitle,
            fontSize = 14.sp,
            color = textColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 40.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Formulario
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Campo email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(emailLabel) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = inputColors,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )

            // Mensaje de error
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp
                )
            }

            // Mensaje de éxito
            if (successMessage.isNotEmpty()) {
                Text(
                    text = successMessage,
                    color = Color(0xFF4CAF50),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón enviar
            Button(
                onClick = {
                    when {
                        email.isEmpty() -> {
                            errorMessage = errorEmpty
                            successMessage = ""
                        }
                        !email.contains("@") || !email.contains(".") -> {
                            errorMessage = errorInvalid
                            successMessage = ""
                        }
                        else -> {
                            errorMessage = ""
                            successMessage = success
                            // TODO: lógica de recuperar contraseña
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor,
                    contentColor = secondaryColor
                )
            ) {
                Text(text = submitBtn, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón volver al login
            TextButton(
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = backToLogin,
                    color = primaryColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
