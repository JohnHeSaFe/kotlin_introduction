package com.example.intro_proyecto_dam2.ui

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.intro_proyecto_dam2.R
import com.example.intro_proyecto_dam2.ui.viewmodels.NurseViewModel
import com.example.intro_proyecto_dam2.utils.decodeBase64ToBitmap
import com.example.intro_proyecto_dam2.utils.encodeUriToBase64

@Composable
fun ProfileScreen(
    viewModel: NurseViewModel,
    navController: NavController,
    // Nuevos parámetros de configuración
    isDarkMode: Boolean,
    isSpanish: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onLanguageChange: (Boolean) -> Unit,
    onNavigateBack: () -> Unit
) {
    val user = viewModel.currentUser
    val context = LocalContext.current

    // --- LÓGICA DE ESTILO (Igual que en ShowAllNurses) ---
    val currentBackgroundColor = colorResource(if (isDarkMode) R.color.background_night else R.color.background)
    val mainTitleColor = if (isDarkMode) Color.White else Color.Black
    val textColor = if (isDarkMode) Color.White else Color.Black

    // Textos del menú
    val textLangOption = stringResource(if (isSpanish) R.string.menu_lang_to_en else R.string.menu_lang_to_es)
    val textThemeOption = stringResource(
        if (isSpanish) {
            if (isDarkMode) R.string.menu_theme_light_es else R.string.menu_theme_dark_es
        } else {
            if (isDarkMode) R.string.menu_theme_light_en else R.string.menu_theme_dark_en
        }
    )

    var menuExpanded by remember { mutableStateOf(false) }
    var isPhotoBusy by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            val base64 = encodeUriToBase64(context, uri)
            if (base64.isNullOrBlank()) {
                Toast.makeText(context, if (isSpanish) "No se pudo leer la imagen" else "Could not read image", Toast.LENGTH_SHORT).show()
            } else {
                isPhotoBusy = true
                viewModel.updateProfilePicture(
                    base64Image = base64,
                    onSuccess = {
                        isPhotoBusy = false
                        Toast.makeText(context, if (isSpanish) "Foto actualizada" else "Photo updated", Toast.LENGTH_SHORT).show()
                    },
                    onError = {
                        isPhotoBusy = false
                        Toast.makeText(context, if (isSpanish) "Error al subir la foto" else "Upload failed", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    // Redirección si no hay usuario
    if (user == null) {
        Box(modifier = Modifier.fillMaxSize().background(currentBackgroundColor), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("No hay sesión activa", color = textColor)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.navigate("login") }) { Text("Ir al Login") }
            }
        }
        return
    }

    // Estados del formulario
    var firstName by remember(user.id) { mutableStateOf(user.first_name) }
    var lastName by remember(user.id) { mutableStateOf(user.last_name) }
    var email by remember(user.id) { mutableStateOf(user.email) }
    var password by remember(user.id) { mutableStateOf(user.password) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(currentBackgroundColor) // Fondo dinámico
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Habilitar scroll si la pantalla es pequeña
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // --- CABECERA COMÚN ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón Atrás + Título
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = mainTitleColor
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isSpanish) "Mi Perfil" else "My Profile",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = mainTitleColor
                )
            }

            // Botón Configuración
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Image(
                        painterResource(if (isDarkMode) R.drawable.settings_icon_dark else R.drawable.settings_icon_light),
                        contentDescription = "Configuración",
                        modifier = Modifier.size(40.dp)
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                ) {
                    DropdownMenuItem(
                        text = { Text(text = textLangOption) },
                        onClick = { onLanguageChange(!isSpanish); menuExpanded = false },
                        leadingIcon = {
                            Image(
                                painter = painterResource(if (isSpanish) R.drawable.uk_language_icon else R.drawable.spain_languange_icon),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = textThemeOption) },
                        onClick = { onDarkModeChange(!isDarkMode); menuExpanded = false },
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

        // --- CONTENIDO DEL PERFIL ---

        // Imagen
        val bitmap = remember(user.profile_picture) {
            user.profile_picture?.let { decodeBase64ToBitmap(it) }
        }

        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = textColor
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = if (isSpanish) "ID de usuario: ${user.id}" else "User ID: ${user.id}",
            fontSize = 14.sp,
            color = textColor
        )

        Spacer(modifier = Modifier.height(16.dp))

        val uploadLabel = if (user.profile_picture == null) {
            if (isSpanish) "Subir foto" else "Upload photo"
        } else {
            if (isSpanish) "Cambiar foto" else "Change photo"
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { imagePickerLauncher.launch("image/*") },
                modifier = Modifier.weight(1f),
                enabled = !isPhotoBusy
            ) {
                Text(uploadLabel)
            }

            OutlinedButton(
                onClick = {
                    isPhotoBusy = true
                    viewModel.deleteProfilePicture(
                        onSuccess = {
                            isPhotoBusy = false
                            Toast.makeText(context, if (isSpanish) "Foto eliminada" else "Photo removed", Toast.LENGTH_SHORT).show()
                        },
                        onError = {
                            isPhotoBusy = false
                            Toast.makeText(context, if (isSpanish) "Error al eliminar la foto" else "Delete failed", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                modifier = Modifier.weight(1f),
                enabled = user.profile_picture != null && !isPhotoBusy
            ) {
                Text(if (isSpanish) "Eliminar foto" else "Remove photo")
            }
        }

        if (isPhotoBusy) {
            Spacer(modifier = Modifier.height(12.dp))
            CircularProgressIndicator()
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Campos de texto (Adaptados al tema oscuro si es necesario)
        val textFieldColors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = textColor,
            unfocusedTextColor = textColor,
            focusedLabelColor = textColor,
            unfocusedLabelColor = Color.Gray,
            cursorColor = textColor,
            focusedBorderColor = textColor,
            unfocusedBorderColor = Color.Gray
        )

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text(if(isSpanish) "Nombre" else "First Name") },
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text(if(isSpanish) "Apellidos" else "Last Name") },
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(if(isSpanish) "Contraseña" else "Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botones
        Button(
            onClick = {
                val updatedNurse = user.copy(
                    first_name = firstName,
                    last_name = lastName,
                    email = email,
                    password = password
                )
                viewModel.updateProfile(
                    updatedNurse,
                    onSuccess = {
                        Toast.makeText(context, if (isSpanish) "Actualizado" else "Updated", Toast.LENGTH_SHORT).show()
                    },
                    onError = {
                        Toast.makeText(context, if (isSpanish) "Error al actualizar" else "Update failed", Toast.LENGTH_SHORT).show()
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if(isSpanish) "Guardar Cambios" else "Save Changes")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón Borrar Cuenta / Delete Account Button
        Button(
            onClick = {
                viewModel.deleteAccount(
                    onSuccess = {
                        Toast.makeText(context, if (isSpanish) "Cuenta eliminada" else "Account deleted", Toast.LENGTH_SHORT).show()
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onError = {
                        Toast.makeText(context, if (isSpanish) "Error al eliminar" else "Delete failed", Toast.LENGTH_SHORT).show()
                    }
                )
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if(isSpanish) "Eliminar Cuenta" else "Delete Account")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = {
            viewModel.logout()
            navController.navigate("login") {
                popUpTo("login") { inclusive = true }
            }
        }) {
            Text(if(isSpanish) "Cerrar Sesión" else "Log Out", color = textColor)
        }
    }
}
