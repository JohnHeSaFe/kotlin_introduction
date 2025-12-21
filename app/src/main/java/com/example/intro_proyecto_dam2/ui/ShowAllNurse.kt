package com.example.intro_proyecto_dam2.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.intro_proyecto_dam2.Nurse
import com.example.intro_proyecto_dam2.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.intro_proyecto_dam2.ui.viewmodels.NurseViewModel
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.layout.ContentScale

@Composable
fun ShowAllNurses(
    viewModel: NurseViewModel = viewModel(),
    onNurseClick: (Int) -> Unit,
    isDarkMode: Boolean,
    isSpanish: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onLanguageChange: (Boolean) -> Unit,
    onNavigateBack: () -> Unit
) {
    val nurseList by viewModel.nurseList.collectAsState()

    var menuExpanded by remember { mutableStateOf(false) }
    val currentBackgroundColor = colorResource(if (isDarkMode) R.color.background_night else R.color.background)


    val textLangOption = stringResource(if (isSpanish) R.string.menu_lang_to_en else R.string.menu_lang_to_es)
    val textThemeOption = stringResource(
        if (isSpanish) {
            if (isDarkMode) R.string.menu_theme_light_es else R.string.menu_theme_dark_es
        } else {
            if (isDarkMode) R.string.menu_theme_light_en else R.string.menu_theme_dark_en
        }
    )


    val mainTitleColor = if (isDarkMode) Color.White else Color.Black

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
                        tint = mainTitleColor
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isSpanish) "Lista de Enfermeros" else "Nurses List",
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

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(nurseList) { nurse ->
                NurseItemCard(onNurseClick = onNurseClick, nurse = nurse, isDarkMode = isDarkMode)
            }
        }
    }
}

@Composable
fun NurseItemCard(onNurseClick: (Int) -> Unit,nurse: Nurse, isDarkMode: Boolean) {

    val cardBackgroundColor = if (isDarkMode) Color(0xFF2D2D2D) else Color.White // Gris oscuro vs Blanco
    val nameTextColor = if (isDarkMode) Color.White else Color.Black
    val emailTextColor = if (isDarkMode) Color.LightGray else Color.Gray

    val primaryColor = colorResource(if (isDarkMode) R.color.primary_night else R.color.primary)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable{
                    onNurseClick(nurse.id)
                }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,

        ) {
            if (nurse.profile_picture != null) {
                Image(
                    painter = painterResource(id = nurse.profile_picture),
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(40.dp)
                        .background(primaryColor, shape = MaterialTheme.shapes.small)
                        .padding(4.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${nurse.first_name} ${nurse.last_name}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = nameTextColor
                )
                Text(
                    text = nurse.email,
                    fontSize = 14.sp,
                    color = emailTextColor
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowAllNursesPreview() {

    ShowAllNurses(
        isDarkMode = false,
        isSpanish = true,
        onDarkModeChange = {},
        onLanguageChange = {},
        onNurseClick = {},
        onNavigateBack = {}
    )
}