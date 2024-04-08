import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.localpros.data.Firebase
import com.example.localpros.data.model.Oferta
import com.example.localpros.data.model.Particular
import com.google.firebase.database.*

@Composable
fun MainScreenParticular(userId: String) {
    val (particular, setParticular) = remember { mutableStateOf<Particular?>(null) }
    val (isLoading, setIsLoading) = remember { mutableStateOf(true) }
    val (error, setError) = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(userId) {
        val databaseReference = Firebase.instance.getReference("particulares/$userId")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val particularResult = snapshot.getValue(Particular::class.java)
                setParticular(particularResult)
                setIsLoading(false)
            }

            override fun onCancelled(error: DatabaseError) {
                setError("Error al cargar los datos: ${error.message}")
                setIsLoading(false)
            }
        })
    }

    when {
        isLoading -> CircularProgressIndicator()
        error != null -> Text(error, style = MaterialTheme.typography.bodyLarge)
        particular != null -> {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Bienvenido, ${particular.nombre}", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Text("Tus ofertas publicadas:", style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(8.dp))
                ListaOfertas(particular.historialOfertas)
            }
        }
    }
}

@Composable
fun ListaOfertas(ofertas: List<Oferta>) {
    Column {
        ofertas.forEach { oferta ->
            ItemOferta(oferta)
            Divider()
        }
    }
}

@Composable
fun ItemOferta(oferta: Oferta) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text("Descripción: ${oferta.descripcion}", style = MaterialTheme.typography.bodyMedium)
        Text("Ubicación: ${oferta.ubicacion}", style = MaterialTheme.typography.bodySmall)
        Text("Presupuesto: ${oferta.presupuestoDisponible}€", style = MaterialTheme.typography.bodySmall)
        Text("Estado: ${oferta.estado}", style = MaterialTheme.typography.bodySmall)
    }
}
