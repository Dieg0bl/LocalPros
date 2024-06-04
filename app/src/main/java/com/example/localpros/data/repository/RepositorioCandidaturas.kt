
package com.example.localpros.data.repository

import com.example.localpros.data.model.Candidatura
import com.example.localpros.data.model.ObtencionDatos
import com.google.firebase.database.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

@Singleton
class RepositorioCandidaturas @Inject constructor(
    db: FirebaseDatabase
) {
    private val refCandidaturas: DatabaseReference = db.getReference("Candidaturas")

    suspend fun presentarCandidatura(candidatura: Candidatura) {
        val nuevaCandidaturaRef = refCandidaturas.push()
        nuevaCandidaturaRef.setValue(candidatura.copy(id = nuevaCandidaturaRef.key ?: "")).await()
    }

    suspend fun obtenerDetallesCandidatura(idCandidatura: String): Candidatura? {
        val snapshot = refCandidaturas.child(idCandidatura).get().await()
        return snapshot.getValue(Candidatura::class.java)
    }

    suspend fun listarCandidaturasPorOferta(idOferta: String): List<Candidatura> {
        val snapshot = refCandidaturas.orderByChild("idOferta").equalTo(idOferta).get().await()
        return snapshot.children.mapNotNull { it.getValue(Candidatura::class.java) }
    }

    suspend fun listarCandidaturasPorProfesional(idProfesional: String): List<Candidatura> {
        val snapshot = refCandidaturas.orderByChild("idProfesional").equalTo(idProfesional).get().await()
        return snapshot.children.mapNotNull { it.getValue(Candidatura::class.java) }
    }

    suspend fun aceptarCandidatura(idCandidatura: String) {
        refCandidaturas.child(idCandidatura).child("estado").setValue("Aceptada").await()
    }

    suspend fun rechazarCandidatura(idCandidatura: String) {
        refCandidaturas.child(idCandidatura).child("estado").setValue("Rechazada").await()
    }

    suspend fun notificarCandidaturaAceptada(idCandidatura: String) {
        // Implementación para notificar que una candidatura ha sido aceptada
    }

    suspend fun notificarCambioEstadoCandidatura(idCandidatura: String, nuevoEstado: String) {
        refCandidaturas.child(idCandidatura).child("estado").setValue(nuevoEstado).await()
    }

    fun obtenerCandidaturaPorId(candidaturaId: String, callback: (ObtencionDatos<Candidatura>) -> Unit) {
        val ref = refCandidaturas.child(candidaturaId)
        ref.get().addOnSuccessListener {
            val candidatura = it.getValue(Candidatura::class.java)
            callback(ObtencionDatos.Exitosa(candidatura!!))
        }.addOnFailureListener {
            callback(ObtencionDatos.Error(it))
        }
    }
}