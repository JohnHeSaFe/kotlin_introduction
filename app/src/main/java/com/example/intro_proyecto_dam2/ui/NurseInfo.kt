package com.example.intro_proyecto_dam2.ui // Asegúrate de que el paquete sea correcto

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.intro_proyecto_dam2.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.intro_proyecto_dam2.ui.viewmodels.NurseViewModel
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale

@Composable
fun NurseDetailScreen(
    viewModel: NurseViewModel = viewModel(),
    nurseId: Int,
    onNavigateBack: () -> Unit,
    isDarkMode: Boolean,
    isSpanish: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onLanguageChange: (Boolean) -> Unit
) {
    val nurseList by viewModel.nurseList.collectAsState()

    val nurse = nurseList.find { it.id == nurseId }
    var menuExpanded by remember { mutableStateOf(false) }

    // --- RECURSOS Y COLORES ---
    val currentBackgroundColor = colorResource(if (isDarkMode) R.color.background_night else R.color.background)
    val textLangOption = stringResource(if (isSpanish) R.string.menu_lang_to_en else R.string.menu_lang_to_es)
    val textThemeOption = stringResource(
        if (isSpanish) {
            if (isDarkMode) R.string.menu_theme_light_es else R.string.menu_theme_dark_es
        } else {
            if (isDarkMode) R.string.menu_theme_light_en else R.string.menu_theme_dark_en
        }
    )

    val mainTextColor = if (isDarkMode) Color.White else Color.Black
    val secondaryTextColor = if (isDarkMode) Color.LightGray else Color.Gray
    val cardBackgroundColor = if (isDarkMode) Color(0xFF2D2D2D) else Color.White

    // --- ESTRUCTURA PRINCIPAL ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(currentBackgroundColor)
            .padding(16.dp)
    ) {
        // --- CABECERA (Botón Atrás + Título + Configuración) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Botón Atrás
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = mainTextColor
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isSpanish) "Detalles" else "Details",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = mainTextColor
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

        // --- CONTENIDO (Tarjeta del Enfermero) ---
        if (nurse == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = if(isSpanish) "¡Error! Enfermero no encontrado" else "Error! Nurse not found",
                    color = Color.Red,
                    fontSize = 18.sp
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f), // Ocupa el espacio restante
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Imagen de Perfil
                        if (nurse.profile_picture != null) {
                            Image(
                                painter = painterResource(id = nurse.profile_picture),
                                contentDescription = "Foto de perfil",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray)
                            )
                        } else {
                            // Icono por defecto si no tiene foto (Tu código original)
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(100.dp)
                                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                                    .padding(10.dp),
                                tint = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Nombre
                        Text(
                            text = "${nurse.first_name} ${nurse.last_name}",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = mainTextColor,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Detalles adicionales
                        HorizontalDivider(color = secondaryTextColor, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "ID: ${nurse.id}",
                            fontSize = 16.sp,
                            color = secondaryTextColor
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = nurse.email,
                            fontSize = 16.sp,
                            color = secondaryTextColor
                        )
                    }
                }
            }
        }
    }
}