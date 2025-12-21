package com.example.intro_proyecto_dam2

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.intro_proyecto_dam2.ui.ForgotPasswordScreen
import com.example.intro_proyecto_dam2.ui.HomeScreen
import com.example.intro_proyecto_dam2.ui.LoginScreen
import com.example.intro_proyecto_dam2.ui.NurseDetailScreen
import com.example.intro_proyecto_dam2.ui.RegisterScreen
import com.example.intro_proyecto_dam2.ui.ShowAllNurses
import com.example.intro_proyecto_dam2.ui.viewmodels.NurseViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val nurseViewModel: NurseViewModel = viewModel()

    var isDarkMode by remember { mutableStateOf(false) }
    var isSpanish by remember { mutableStateOf(true) }


    NavHost(navController = navController, startDestination = "home") {


        composable("home") {
            HomeScreen(
                isDarkMode = isDarkMode,
                isSpanish = isSpanish,
                onDarkModeChange = { isDarkMode = it },
                onLanguageChange = { isSpanish = it },
                onNavigateToLogin = { navController.navigate("login") },
                onNavigateToRegister = { navController.navigate("register") },
                onNavigateToSearch = { navController.navigate("search_nurse") },
                onNavigateToShowall = {navController.navigate("show_all_nurses")}
            )
        }

        composable("login") {
            LoginScreen(
                viewModel = nurseViewModel,
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


        composable("register") {
            RegisterScreen(
                viewModel = nurseViewModel,
                isDarkMode = isDarkMode,
                isSpanish = isSpanish,
                onDarkModeChange = { isDarkMode = it },
                onLanguageChange = { isSpanish = it },
                onNavigateToLogin = { navController.navigate("login") }, // O popBackStack()
                onNavigateBack = { navController.popBackStack() }
            )
        }


        composable("forgot_password") {
            ForgotPasswordScreen(
                isDarkMode = isDarkMode,
                isSpanish = isSpanish,
                onDarkModeChange = { isDarkMode = it },
                onLanguageChange = { isSpanish = it },
                onNavigateBack = { navController.popBackStack() }
            )
        }


        composable("search_nurse") {
            SearchNurse(isDarkMode = isDarkMode,
                viewModel = nurseViewModel,
                isSpanish = isSpanish,
                onDarkModeChange = { isDarkMode = it },
                onLanguageChange = { isSpanish = it },
                onNavigateBack = { navController.popBackStack()},
                onNurseClick = {

                    nurseId ->
                    navController.navigate("nurse_details/$nurseId")
                }
            )
        }

        composable("show_all_nurses") {
            ShowAllNurses(
                viewModel = nurseViewModel,
                isDarkMode = isDarkMode,
                isSpanish = isSpanish,
                onDarkModeChange = { isDarkMode = it },
                onLanguageChange = { isSpanish = it },
                onNavigateBack = { navController.popBackStack()},
                onNurseClick = {
                        nurseId ->
                    navController.navigate("nurse_details/$nurseId")
                })
        }


        composable(
            route = "nurse_details/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            NurseDetailScreen(
                isDarkMode = isDarkMode,
                isSpanish = isSpanish,
                onDarkModeChange = { isDarkMode = it},
                onLanguageChange = { isSpanish = it},
                nurseId = id,
                onNavigateBack = { navController.popBackStack() })
        }
    }
}