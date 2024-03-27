import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.localpros.R
import com.example.localpros.ui.navigation.AppScreens

@Composable
fun RoleSelectionScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = { navController.navigate(AppScreens.MainParticularScreen.route) },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Image(painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Particular",
                modifier = Modifier.fillMaxSize())
        }

        Button(
            onClick = { navController.navigate(AppScreens.MainProfesionalScreen.route) },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Image(painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "Profesional",
                modifier = Modifier.fillMaxSize())
        }
    }
}


