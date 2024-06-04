
package com.example.localpros.data.repository

import com.example.localpros.data.model.Evaluacion
import com.google.firebase.database.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

@Singleton
class RepositorioEvaluaciones @Inject constructor(
    db: FirebaseDatabase
) {
    private val refEvaluaciones: DatabaseReference = db.getReference("Evaluaciones")

    suspend fun crearEvaluacion(evaluacion: Evaluacion) {
        val nuevaEvaluacionRef = refEvaluaciones.push()
        nuevaEvaluacionRef.setValue(evaluacion.copy(idEvaluacion = nuevaEvaluacionRef.key ?: "")).await()
    }

    suspend fun obtenerEvaluacion(idEvaluacion: String): Evaluacion? {
        val snapshot = refEvaluaciones.child(idEvaluacion).get().await()
        return snapshot.getValue(Evaluacion::class.java)
    }

    suspend fun listarEvaluacionesPorUsuario(idUsuario: String): List<Evaluacion> {
        val snapshot = refEvaluaciones.orderByChild("idUsuario").equalTo(idUsuario).get().await()
        return snapshot.children.mapNotNull { it.getValue(Evaluacion::class.java) }
    }

    suspend fun listarEvaluacionesPorOferta(idOferta: String): List<Evaluacion> {
        val snapshot = refEvaluaciones.orderByChild("idOferta").equalTo(idOferta).get().await()
        return snapshot.children.mapNotNull { it.getValue(Evaluacion::class.java) }
    }

    suspend fun calcularIndicadoresDesempeno(idUsuario: String): Map<String, Double> {
        // Implementación para calcular indicadores de desempeño
        return mapOf()
    }
}
