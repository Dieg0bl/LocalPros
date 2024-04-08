import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.localpros.data.Firebase
import com.example.localpros.data.model.Profesional
import com.google.firebase.database.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreenProfesional(userId: String) {
    val (profesional, setProfesional) = remember { mutableStateOf<Profesional?>(null) }
    val (isLoading, setIsLoading) = remember { mutableStateOf(true) }
    val (error, setError) = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(userId) {
        val databaseReference = Firebase.instance.getReference("profesionales/$userId")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val profesionalResult = snapshot.getValue(Profesional::class.java)
                setProfesional(profesionalResult)
                setIsLoading(false)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                setError("Error al cargar los datos: ${databaseError.message}")
                setIsLoading(false)
            }
        })
    }

    Scaffold(
        topBar = { TopBarProfesional() }
    ) {
        when {
            isLoading -> CircularProgressIndicator()
            error != null -> Text(error, style = MaterialTheme.typography.bodyLarge)
            profesional != null -> {
                Text("Bienvenido, ${profesional.nombre}", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarProfesional() {
    TopAppBar(
        title = { Text("Perfil Profesional") },
        actions = {
            IconButton(onClick = { /* TODO:ajustes */ }) {
                Icon(Icons.Filled.Settings, "Ajustes")
            }
            IconButton(onClick = { /* TODO: ayuda */ }) {
                Icon(Icons.Filled.Info, "Ayuda")
            }
            IconButton(onClick = { /* TODO: cerrar sesión */ }) {
                Icon(Icons.Filled.ExitToApp, "Salir")
            }
        }
    )
}
