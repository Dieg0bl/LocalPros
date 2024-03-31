import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.localpros.data.model.UserPreferences
import com.example.localpros.ui.navigation.AppScreens
import com.example.localpros.ui.view.LoginScreen
import com.example.localpros.ui.view.MainScreenParticular
import com.example.localpros.ui.view.MainScreenProfesional
import com.example.localpros.ui.view.RegisterScreen
import com.example.localpros.ui.view.ResetPasswordScreen
import com.example.localpros.ui.viewModel.UserViewModel

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
            val particularData by userViewModel.particularData.collectAsState()
            particularData?.let {
                MainScreenParticular(particular = it)
            }
        }

        composable(AppScreens.MainProfesionalScreen.route) {
            val profesionalData by userViewModel.profesionalData.collectAsState()
            profesionalData?.let {
                MainScreenProfesional(profesional = it)
            }
        }
    }
}
