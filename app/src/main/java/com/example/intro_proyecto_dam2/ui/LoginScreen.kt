package com.example.intro_proyecto_dam2.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.intro_proyecto_dam2.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.intro_proyecto_dam2.ui.viewmodels.NurseViewModel

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        isDarkMode = false,
        isSpanish = true,
        onDarkModeChange = {},
        onLanguageChange = {},
        onNavigateToRegister = {},
        onNavigateToForgotPassword = {},
        onNavigateBack = {},
        onNavigateToDashboard = {}
    )
}

@Composable
fun LoginScreen(
    viewModel: NurseViewModel = viewModel(),
    isDarkMode: Boolean,
    isSpanish: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onLanguageChange: (Boolean) -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToDashboard: () -> Unit
) {
    // Estados del formulario
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var menuExpanded by remember { mutableStateOf(false) }

    // Colores según el tema
    val bgColor = colorResource(if (isDarkMode) R.color.background_night else R.color.background)
    val primaryColor = colorResource(if (isDarkMode) R.color.primary_night else R.color.primary)
    val secondaryColor = colorResource(if (isDarkMode) R.color.secondary_night else R.color.light)
    val tertiaryColor = colorResource(if (isDarkMode) R.color.tertiary else R.color.tertiary_night)

    // Textos según idioma
    val title = stringResource(if (isSpanish) R.string.login_title_es else R.string.login_title_en)
    val emailLabel = stringResource(if (isSpanish) R.string.login_email_es else R.string.login_email_en)
    val passwordLabel = stringResource(if (isSpanish) R.string.login_password_es else R.string.login_password_en)
    val forgotPassword = stringResource(if (isSpanish) R.string.login_forgot_password_es else R.string.login_forgot_password_en)
    val submitBtn = stringResource(if (isSpanish) R.string.login_submit_es else R.string.login_submit_en)
    val noAccount = stringResource(if (isSpanish) R.string.login_no_account_es else R.string.login_no_account_en)
    val registerLink = stringResource(if (isSpanish) R.string.login_register_link_es else R.string.login_register_link_en)
    val errorEmpty = stringResource(if (isSpanish) R.string.login_error_empty_es else R.string.login_error_empty_en)

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
                    // Cambiar idioma
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
                    // Cambiar tema
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

        Spacer(modifier = Modifier.height(20.dp))

        // Logo
        Image(
            painter = painterResource(if (isDarkMode) R.drawable.nurse_logo_dark else R.drawable.nurse_logo_light),
            contentDescription = "Logo",
            modifier = Modifier.size(120.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Título
        Text(
            text = title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor
        )

        Spacer(modifier = Modifier.height(30.dp))

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

            // Campo contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(passwordLabel) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = inputColors,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Image(
                            painter = painterResource(
                                if (passwordVisible) R.drawable.visibility_on_icon
                                else R.drawable.visibility_off_icon
                            ),
                            contentDescription = "Toggle password",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )

            // Olvidé contraseña
            Text(
                text = forgotPassword,
                color = primaryColor,
                fontSize = 14.sp,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { onNavigateToForgotPassword() }
            )

            // Mensaje de error
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón entrar
            Button(
                onClick = {
                    if (email.isEmpty() || password.isEmpty()) {
                        errorMessage = errorEmpty
                    } else {
                        if (viewModel.login(email, password)) {
                            errorMessage = ""
                            onNavigateToDashboard()
                        } else {
                            errorMessage = if (isSpanish) "Credenciales incorrectas" else "Invalid credentials"
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
        }

        Spacer(modifier = Modifier.weight(1f))

        // Link a registro
        Row(
            modifier = Modifier.padding(bottom = 40.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "$noAccount ", color = tertiaryColor, fontSize = 14.sp)
            Text(
                text = registerLink,
                color = primaryColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { onNavigateToRegister() }
            )
        }
    }
}