package com.example.intro_proyecto_dam2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.intro_proyecto_dam2.ui.Boton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchNurse(
    onNurseClick: (Int) -> Unit,
    // Nuevos parámetros de estado
    isDarkMode: Boolean,
    isSpanish: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onLanguageChange: (Boolean) -> Unit,
    onNavigateBack:  () -> Unit
) {
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }

    // --- RECURSOS Y COLORES (Igual que en las otras pantallas) ---
    val currentBackgroundColor = colorResource(if (isDarkMode) R.color.background_night else R.color.background)
    val textLangOption = stringResource(if (isSpanish) R.string.menu_lang_to_en else R.string.menu_lang_to_es)
    val textThemeOption = stringResource(
        if (isSpanish) {
            if (isDarkMode) R.string.menu_theme_light_es else R.string.menu_theme_dark_es
        } else {
            if (isDarkMode) R.string.menu_theme_light_en else R.string.menu_theme_dark_en
        }
    )
    val searchPlaceholder = if (isSpanish) "Buscar enfermero..." else "Search Nurse..."
    val titleText = if (isSpanish) "Buscador" else "Search"
    val mainTitleColor = if (isDarkMode) Color.White else Color.Black

    // Colores específicos para la barra de búsqueda y lista
    val searchBarContainerColor = if (isDarkMode) Color(0xFF2D2D2D) else MaterialTheme.colorScheme.surface
    val searchBarContentColor = if (isDarkMode) Color.White else Color.Black
    val listItemContainerColor = if (isDarkMode) Color(0xFF1E1E1E) else Color.Transparent // Fondo de items

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(currentBackgroundColor)
            .padding(16.dp)
    ) {
        // --- CABECERA (Título + Configuración) ---
        // Solo mostramos la cabecera si la búsqueda NO está activa (expandida)
        if (!active) {
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
                            tint = mainTitleColor
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isSpanish) "Detalles" else "Details",
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
        }

        // --- SEARCH BAR ---
        SearchBar(
            query = query,
            onQueryChange = { query = it },
            onSearch = { active = false },
            active = active,
            onActiveChange = { active = it },
            placeholder = { Text(searchPlaceholder) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = searchBarContentColor) },
            trailingIcon = {
                if (active) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = searchBarContentColor,
                        modifier = Modifier.clickable {
                            if (query.isNotEmpty()) query = "" else active = false
                        }
                    )
                }
            },
            // Personalizamos los colores de la barra
            colors = SearchBarDefaults.colors(
                containerColor = searchBarContainerColor,
                inputFieldColors = TextFieldDefaults.colors(
                    focusedTextColor = searchBarContentColor,
                    unfocusedTextColor = searchBarContentColor,
                    cursorColor = searchBarContentColor
                )
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Lógica de filtrado
            val filteredNurses = Nurses.filter { Nurse ->
                (Nurse.first_name + " " + Nurse.last_name).contains(query, ignoreCase = true) ||
                        Nurse.email.contains(query, ignoreCase = true)
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(currentBackgroundColor) // Fondo de la lista desplegada
            ) {
                items(filteredNurses) { nurse ->
                    ListItem(
                        headlineContent = {
                            Text(
                                text = "${nurse.first_name} ${nurse.last_name}",
                                color = searchBarContentColor // Texto adaptable
                            )
                        },
                        supportingContent = {
                            Text(
                                text = nurse.email,
                                color = if (isDarkMode) Color.Gray else Color.DarkGray
                            )
                        },
                        leadingContent = {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = searchBarContentColor
                            )
                        },
                        colors = ListItemDefaults.colors(
                            containerColor = listItemContainerColor // Fondo del item
                        ),
                        modifier = Modifier.clickable {
                            query = "${nurse.first_name} ${nurse.last_name}"
                            active = false
                            onNurseClick(nurse.id)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchNursePreview() {
    SearchNurse(
        onNurseClick = {},
        isDarkMode = false,
        isSpanish = true,
        onDarkModeChange = {},
        onLanguageChange = {},
        onNavigateBack = {}
    )
}