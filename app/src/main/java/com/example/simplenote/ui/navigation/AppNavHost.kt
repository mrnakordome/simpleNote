package com.example.simplenote.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.simplenote.ui.onboarding.OnboardingScreen
import com.example.simplenote.ui.login.LoginScreen
import com.example.simplenote.ui.register.RegisterScreen
import com.example.simplenote.ui.home.HomeScreen

private object Routes {
    const val Onboarding = "onboarding"
    const val Login = "login"
    const val Register = "register"
    const val Home = "home"
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Onboarding
    ) {
        composable(Routes.Onboarding) {
            OnboardingScreen(
                onGetStarted = { navController.navigate(Routes.Login) }
            )
        }

        composable(Routes.Login) {
            LoginScreen(
                onLogin = { email, pass ->
                    navController.navigate(Routes.Home) {
                        popUpTo(Routes.Onboarding) { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(Routes.Register) }
            )
        }

        composable(Routes.Register) {
            RegisterScreen(
                onRegister = { first, last, user, email, pass ->
                    navController.navigate(Routes.Home) {
                        popUpTo(Routes.Onboarding) { inclusive = true }
                    }
                },
                onBackToLogin = { navController.popBackStack() }
            )
        }

        composable(Routes.Home) {
            HomeScreen(
                onAddNote = { /* TODO: open create note later */ },
                onOpenSettingsSystem = { /* TODO */ }
            )
        }
    }
}
