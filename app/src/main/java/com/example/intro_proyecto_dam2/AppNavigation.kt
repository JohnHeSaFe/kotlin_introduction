package com.example.intro_proyecto_dam2
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.intro_proyecto_dam2.ui.MainScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "menu_principal") {
        composable("menu_principal") {
            MainScreen(
                onNavToSearch = {
                    navController.navigate("buscador_enfermeros")
                }
            )
        }
        composable("buscador_enfermeros") {
            SearchNurse(
                onNurseClick = { idEnfermero ->
                    navController.navigate("detalle_enfermero/$idEnfermero")
                }
            )
        }
        composable(
            route = "detalle_enfermero/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            NurseDetailScreen(nurseId = id)
        }
    }

    }

