package com.example.fittrack.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fittrack.auth.AuthViewModel
import com.example.fittrack.ui.screens.AddWorkoutScreen
import com.example.fittrack.ui.screens.HomeScreen
import com.example.fittrack.ui.screens.LoginScreen
import com.example.fittrack.ui.screens.ProfileScreen
import com.example.fittrack.ui.screens.RegisterScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val ADD_WORKOUT = "add_workout"
    const val PROFILE = "profile"
}

@Composable
fun FitTrackNavGraph(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = AuthViewModel()
) {
    val startDestination = if (authViewModel.currentUserId != null) Routes.HOME else Routes.LOGIN

    NavHost(navController = navController, startDestination = startDestination) {

        composable(Routes.LOGIN) {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Routes.REGISTER) }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                authViewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                userId = authViewModel.currentUserId.orEmpty(),
                onAddWorkout = { navController.navigate(Routes.ADD_WORKOUT) },
                onOpenProfile = { navController.navigate(Routes.PROFILE) }
            )
        }

        composable(Routes.ADD_WORKOUT) {
            AddWorkoutScreen(
                userId = authViewModel.currentUserId.orEmpty(),
                onDone = { navController.popBackStack() }
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                authViewModel = authViewModel,
                onLoggedOut = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
