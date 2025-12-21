package com.example.intro_proyecto_dam2.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.intro_proyecto_dam2.R

@Composable
fun HomeScreen(
    isDarkMode: Boolean,
    isSpanish: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onLanguageChange: (Boolean) -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    val currentBackgroundColor = colorResource(
        if (isDarkMode) R.color.background_night else R.color.background
    )

    val currentPrimaryColor = colorResource(
        if (isDarkMode) R.color.primary_night else R.color.primary
    )

    val currentSecondaryColor = colorResource(
        if (isDarkMode) R.color.secondary_night else R.color.light
    )

    val currentTertiaryColor = colorResource(
        if (isDarkMode) R.color.tertiary else R.color.tertiary_night
    )

    val textSubtitle = stringResource(if (isSpanish) R.string.subtitle_es else R.string.subtitle_en)
    val textLogin = stringResource(if (isSpanish) R.string.btn_login_es else R.string.btn_login_en)
    val textRegister = stringResource(if (isSpanish) R.string.btn_register_es else R.string.btn_register_en)
    val textLangOption = stringResource(if (isSpanish) R.string.menu_lang_to_en else R.string.menu_lang_to_es)
    val textThemeOption = stringResource(
        if (isSpanish) {
            if (isDarkMode) R.string.menu_theme_light_es else R.string.menu_theme_dark_es
        } else {
            if (isDarkMode) R.string.menu_theme_light_en else R.string.menu_theme_dark_en
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(currentBackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Settings
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(26.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
            ) {
                IconButton(
                    onClick = { menuExpanded = true },
                ) {
                    Image(
                        painterResource(if (isDarkMode) R.drawable.settings_icon_dark else R.drawable.settings_icon_light),
                        contentDescription = "Configuración",
                        modifier = Modifier.size(46.dp)
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                ) {
                    // Language option
                    DropdownMenuItem(
                        text = { Text(text = textLangOption) },
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

                    // Theme option
                    DropdownMenuItem(
                        text = { Text(text = textThemeOption) },
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

        // Presentation
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = if (isDarkMode) painterResource(R.drawable.nurse_logo_dark) else painterResource(R.drawable.nurse_logo_light),
                contentDescription = "Nurse logo",
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "NurseApp",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = currentPrimaryColor
            )

            Text(
                text = textSubtitle,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = currentTertiaryColor
            )
        }

        // Botones (solo Login y Registro)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 50.dp, start = 30.dp, end = 30.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Boton(
                text = textLogin,
                bgColor = currentPrimaryColor,
                textColor = currentSecondaryColor,
                onClick = onNavigateToLogin
            )
            Boton(
                text = textRegister,
                bgColor = currentPrimaryColor,
                textColor = currentSecondaryColor,
                onClick = onNavigateToRegister
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        isDarkMode = false,
        isSpanish = true,
        onDarkModeChange = {},
        onLanguageChange = {},
        onNavigateToLogin = {},
        onNavigateToRegister = {}
    )
}

@Composable
fun Boton(text: String, bgColor: androidx.compose.ui.graphics.Color, textColor: androidx.compose.ui.graphics.Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = bgColor,
            contentColor = textColor
        )
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}