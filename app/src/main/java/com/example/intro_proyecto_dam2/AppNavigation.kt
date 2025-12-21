package com.example.intro_proyecto_dam2

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.intro_proyecto_dam2.ui.DashboardScreen
import com.example.intro_proyecto_dam2.ui.ForgotPasswordScreen
import com.example.intro_proyecto_dam2.ui.HomeScreen
import com.example.intro_proyecto_dam2.ui.LoginScreen
import com.example.intro_proyecto_dam2.ui.NurseDetailScreen
import com.example.intro_proyecto_dam2.ui.RegisterScreen
import com.example.intro_proyecto_dam2.ui.ShowAllNurses

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    var isDarkMode by remember { mutableStateOf(false) }
    var isSpanish by remember { mutableStateOf(true) }

    NavHost(navController = navController, startDestination = "home") {

        // Pantalla de inicio (solo entrada)
        composable("home") {
            HomeScreen(
                isDarkMode = isDarkMode,
                isSpanish = isSpanish,
                onDarkModeChange = { isDarkMode = it },
                onLanguageChange = { isSpanish = it },
                onNavigateToLogin = { navController.navigate("login") },
                onNavigateToRegister = { navController.navigate("register") },
                // Estas opciones ya no están disponibles desde Home
                onNavigateToSearch = { /* No hace nada */ },
                onNavigateToShowall = { /* No hace nada */ }
            )
        }

        // Pantalla de login
        composable("login") {
            LoginScreen(
                isDarkMode = isDarkMode,
                isSpanish = isSpanish,
                onDarkModeChange = { isDarkMode = it },
                onLanguageChange = { isSpanish = it },
                onNavigateToRegister = { navController.navigate("register") },
                onNavigateToForgotPassword = { navController.navigate("forgot_password") },
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDashboard = {
                    navController.navigate("dashboard") {
                        popUpTo("home") { inclusive = false }
                    }
                }
            )
        }

        // Pantalla de registro
        composable("register") {
            RegisterScreen(
                isDarkMode = isDarkMode,
                isSpanish = isSpanish,
                onDarkModeChange = { isDarkMode = it },
                onLanguageChange = { isSpanish = it },
                onNavigateToLogin = { navController.navigate("login") },
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDashboard = {
                    navController.navigate("dashboard") {
                        popUpTo("home") { inclusive = false }
                    }
                }
            )
        }

        // Pantalla de recuperar contraseña
        composable("forgot_password") {
            ForgotPasswordScreen(
                isDarkMode = isDarkMode,
                isSpanish = isSpanish,
                onDarkModeChange = { isDarkMode = it },
                onLanguageChange = { isSpanish = it },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // NUEVA: Pantalla Dashboard (después de login/registro exitoso)
        composable("dashboard") {
            DashboardScreen(
                isDarkMode = isDarkMode,
                isSpanish = isSpanish,
                onDarkModeChange = { isDarkMode = it },
                onLanguageChange = { isSpanish = it },
                onNavigateToSearch = { navController.navigate("search_nurse") },
                onNavigateToShowAll = { navController.navigate("show_all_nurses") },
                onLogout = {
                    // Volver a home y limpiar el back stack
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de búsqueda (solo accesible desde Dashboard)
        composable("search_nurse") {
            SearchNurse(
                isDarkMode = isDarkMode,
                isSpanish = isSpanish,
                onDarkModeChange = { isDarkMode = it },
                onLanguageChange = { isSpanish = it },
                onNavigateBack = { navController.popBackStack() },
                onNurseClick = { nurseId ->
                    navController.navigate("nurse_details/$nurseId")
                }
            )
        }

        // Pantalla de listar todos (solo accesible desde Dashboard)
        composable("show_all_nurses") {
            ShowAllNurses(
                isDarkMode = isDarkMode,
                isSpanish = isSpanish,
                onDarkModeChange = { isDarkMode = it },
                onLanguageChange = { isSpanish = it },
                onNavigateBack = { navController.popBackStack() },
                onNurseClick = { nurseId ->
                    navController.navigate("nurse_details/$nurseId")
                }
            )
        }

        // Pantalla de detalles de enfermera
        composable(
            route = "nurse_details/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            NurseDetailScreen(
                isDarkMode = isDarkMode,
                isSpanish = isSpanish,
                onDarkModeChange = { isDarkMode = it },
                onLanguageChange = { isSpanish = it },
                nurseId = id,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}