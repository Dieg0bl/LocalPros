import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.localpros.data.model.UserPreferences
import com.example.localpros.ui.navigation.AppScreens
import com.example.localpros.ui.view.LoginScreen
import com.example.localpros.ui.view.RegisterScreen
import com.example.localpros.ui.view.ResetPasswordScreen
import com.example.localpros.ui.viewModel.UserViewModel
import com.example.localpros.ui.view.MainScreenParticular
import com.example.localpros.ui.view.MainScreenProfesional
import com.example.localpros.ui.view.RoleSelectionScreen



@Composable
fun AppNavigation(navController: NavHostController, userPreferences: UserPreferences, userViewModel: UserViewModel) {
    NavHost(navController = navController, startDestination = AppScreens.LoginScreen.route) {
        composable(AppScreens.LoginScreen.route) {
            LoginScreen(navController = navController, userPreferences = userPreferences) {
                navController.navigate(AppScreens.MainScreen.route)
            }
        }

        composable(AppScreens.RegisterScreen.route) {
            RegisterScreen(
                navController = navController,
                onRegistrationSuccess = {
                    navController.navigate(AppScreens.MainScreen.route)
                }
            )
        }
        composable(AppScreens.ResetPasswordScreen.route) {
            ResetPasswordScreen(navController = navController)
        }
        composable(AppScreens.MainScreen.route) {
            RoleSelectionScreen(navController = navController)
        }

        composable(AppScreens.MainParticularScreen.route) {
            MainScreenParticular(userId = userPreferences.userId, navController = navController, userViewModel = userViewModel)
        }

        composable(AppScreens.MainProfesionalScreen.route) {
            MainScreenProfesional(userId = userPreferences.userId, navController = navController, userViewModel = userViewModel)
        }
    }
}
