package com.example.simplenote.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.simplenote.ui.home.HomeScreen
import com.example.simplenote.ui.home.HomeViewModel
import com.example.simplenote.ui.login.LoginScreen
import com.example.simplenote.ui.note.NoteScreen
import com.example.simplenote.ui.note.NoteViewModel
import com.example.simplenote.ui.note.NoteViewModelFactory
import com.example.simplenote.ui.onboarding.OnboardingScreen
import com.example.simplenote.ui.register.RegisterScreen
import com.example.simplenote.ui.settings.ChangePasswordScreen
import com.example.simplenote.ui.settings.SettingsScreen
import com.example.simplenote.ui.settings.SettingsViewModel

object Routes {
    const val Onboarding = "onboarding"
    const val Login = "login"
    const val Register = "register"
    const val Home = "home"
    const val CreateNote = "create_note"
    const val NoteDetail = "note_detail"
    const val Settings = "settings"
    const val ChangePassword = "change_password"

    fun noteDetail(noteId: String) = "$NoteDetail/$noteId"
}

@Composable
fun AppNavHost(navController: NavHostController) {
    val homeViewModel: HomeViewModel = viewModel()

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
                    homeViewModel.fetchNotes()
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
                homeViewModel = homeViewModel,
                onAddNote = { navController.navigate(Routes.CreateNote) },
                onNoteClick = { noteId ->
                    navController.navigate(Routes.noteDetail(noteId))
                },
                onOpenSettings = { navController.navigate(Routes.Settings) }
            )
        }

        composable(Routes.CreateNote) {
            val viewModel: NoteViewModel = viewModel(factory = NoteViewModelFactory(noteId = null))
            NoteScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    homeViewModel.fetchNotes()
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "${Routes.NoteDetail}/{noteId}",
            arguments = listOf(navArgument("noteId") { type = NavType.StringType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")
            val viewModel: NoteViewModel = viewModel(factory = NoteViewModelFactory(noteId = noteId))
            NoteScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    homeViewModel.fetchNotes()
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.Settings) {
            val settingsViewModel: SettingsViewModel = viewModel()
            SettingsScreen(
                viewModel = settingsViewModel,
                onNavigateBack = { navController.popBackStack() },
                onChangePasswordClick = { navController.navigate(Routes.ChangePassword) },
                onLogout = {
                    settingsViewModel.logout()
                    navController.navigate(Routes.Login) {
                        popUpTo(Routes.Home) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.ChangePassword) {
            val settingsViewModel: SettingsViewModel = viewModel(
                viewModelStoreOwner = navController.previousBackStackEntry!!
            )
            ChangePasswordScreen(
                viewModel = settingsViewModel,
                onNavigateBack = { navController.popBackStack() },
                onSubmitSuccess = { navController.popBackStack() }
            )
        }
    }
}