package com.example.simplenote.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.simplenote.ui.home.HomeScreen
import com.example.simplenote.ui.login.LoginScreen
import com.example.simplenote.ui.note.NoteScreen
import com.example.simplenote.ui.onboarding.OnboardingScreen
import com.example.simplenote.ui.register.RegisterScreen
object Routes {
    const val Onboarding = "onboarding"
    const val Login = "login"
    const val Register = "register"
    const val Home = "home"
    const val CreateNote = "create_note"
    const val NoteDetail = "note_detail"

    fun noteDetail(noteId: String) = "$NoteDetail/$noteId"
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Onboarding
    ) {
        composable(Routes.Onboarding) {
            OnboardingScreen(
                onGetStarted = {
                    navController.navigate(Routes.Login) { popUpTo(Routes.Onboarding) { inclusive = true } }
                }
            )
        }

        composable(Routes.Login) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.Home) { popUpTo(Routes.Login) { inclusive = true } }
                },
                onRegisterClick = { navController.navigate(Routes.Register) }
            )
        }

        composable(Routes.Register) {
            RegisterScreen(
                onRegisterSuccess = { navController.popBackStack() },
                onBackToLogin = { navController.popBackStack() }
            )
        }

        composable(Routes.Home) {
            HomeScreen(
                onAddNote = { navController.navigate(Routes.CreateNote) },
                onNoteClick = { noteId ->
                    navController.navigate(Routes.noteDetail(noteId))
                }
            )
        }

        composable(Routes.CreateNote) {
            NoteScreen(
                noteId = null,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "${Routes.NoteDetail}/{noteId}",
            arguments = listOf(navArgument("noteId") { type = NavType.StringType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")
            NoteScreen(
                noteId = noteId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}