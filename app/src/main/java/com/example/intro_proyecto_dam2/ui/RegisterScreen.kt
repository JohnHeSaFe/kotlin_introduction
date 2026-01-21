package com.example.intro_proyecto_dam2.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.example.intro_proyecto_dam2.Nurse
import kotlin.random.Random

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(
        isDarkMode = false,
        isSpanish = true,
        onDarkModeChange = {},
        onLanguageChange = {},
        onNavigateToLogin = {},
        onNavigateBack = {},
        onNavigateToDashboard = {}
    )
}

@Composable
fun RegisterScreen(
    viewModel: NurseViewModel = viewModel(),
    isDarkMode: Boolean,
    isSpanish: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onLanguageChange: (Boolean) -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToDashboard: () -> Unit
) {
    // Estados del formulario
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var numColegiado by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var menuExpanded by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    // Colores según el tema
    val bgColor = colorResource(if (isDarkMode) R.color.background_night else R.color.background)
    val primaryColor = colorResource(if (isDarkMode) R.color.primary_night else R.color.primary)
    val secondaryColor = colorResource(if (isDarkMode) R.color.secondary_night else R.color.light)
    val tertiaryColor = colorResource(if (isDarkMode) R.color.tertiary else R.color.tertiary_night)

    // Textos según idioma
    val title = stringResource(if (isSpanish) R.string.register_title_es else R.string.register_title_en)
    val nombreLabel = stringResource(if (isSpanish) R.string.register_name_es else R.string.register_name_en)
    val emailLabel = stringResource(if (isSpanish) R.string.register_email_es else R.string.register_email_en)
    val numColegiadoLabel = stringResource(if (isSpanish) R.string.register_nurse_id_es else R.string.register_nurse_id_en)
    val passwordLabel = stringResource(if (isSpanish) R.string.register_password_es else R.string.register_password_en)
    val confirmPasswordLabel = stringResource(if (isSpanish) R.string.register_confirm_password_es else R.string.register_confirm_password_en)
    val submitBtn = stringResource(if (isSpanish) R.string.register_submit_es else R.string.register_submit_en)
    val hasAccount = stringResource(if (isSpanish) R.string.register_has_account_es else R.string.register_has_account_en)
    val loginLink = stringResource(if (isSpanish) R.string.register_login_link_es else R.string.register_login_link_en)
    val errorEmpty = stringResource(if (isSpanish) R.string.register_error_empty_es else R.string.register_error_empty_en)
    val errorPassword = stringResource(if (isSpanish) R.string.register_error_password_es else R.string.register_error_password_en)
    val success = stringResource(if (isSpanish) R.string.register_success_es else R.string.register_success_en)
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

    // Efecto para navegar al dashboard tras registro exitoso
    LaunchedEffect(successMessage) {
        if (successMessage.isNotEmpty()) {
            kotlinx.coroutines.delay(1500) // Esperar 1.5 segundos para que el usuario vea el mensaje
            onNavigateToDashboard()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .verticalScroll(scrollState),
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

        Spacer(modifier = Modifier.height(10.dp))

        // Logo
        Image(
            painter = painterResource(if (isDarkMode) R.drawable.nurse_logo_dark else R.drawable.nurse_logo_light),
            contentDescription = "Logo",
            modifier = Modifier.size(100.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Título
        Text(
            text = title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Formulario
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Campo nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text(nombreLabel) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = inputColors,
                singleLine = true
            )

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

            // Campo número colegiado
            OutlinedTextField(
                value = numColegiado,
                onValueChange = { numColegiado = it },
                label = { Text(numColegiadoLabel) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = inputColors,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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

            // Campo confirmar contraseña
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text(confirmPasswordLabel) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = inputColors,
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Image(
                            painter = painterResource(
                                if (confirmPasswordVisible) R.drawable.visibility_on_icon
                                else R.drawable.visibility_off_icon
                            ),
                            contentDescription = "Toggle password",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )

            // Mensaje de error
            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = Color.Red, fontSize = 14.sp)
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

            // Botón registrarse
            Button(
                onClick = {
                    when {
                        nombre.isEmpty() || email.isEmpty() || numColegiado.isEmpty() ||
                                password.isEmpty() || confirmPassword.isEmpty() -> {
                            errorMessage = errorEmpty
                            successMessage = ""
                        }
                        password != confirmPassword -> {
                            errorMessage = errorPassword
                            successMessage = ""
                        }
                        else -> {
                            val newNurse = Nurse(
                                id = viewModel.generateNextId(),
                                first_name = nombre,
                                last_name = "",
                                email = email,
                                password = password,
                                profile_picture = null
                            )

                            val registerSuccess = viewModel.register(newNurse)

                            if (registerSuccess) {
                                errorMessage = ""
                                successMessage = success

                            } else {
                                errorMessage = if (isSpanish) "El email ya existe" else "Email already exists"
                                successMessage = ""
                            }
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

        Spacer(modifier = Modifier.height(20.dp))

        // Link a login
        Row(
            modifier = Modifier.padding(bottom = 30.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "$hasAccount ", color = tertiaryColor, fontSize = 14.sp)
            Text(
                text = loginLink,
                color = primaryColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { onNavigateToLogin() }
            )
        }
    }
}