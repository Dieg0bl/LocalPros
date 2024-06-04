
package com.example.localpros.data.repository

import com.example.localpros.data.model.DisponibilidadTemporal
import com.google.firebase.database.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

@Singleton
class RepositorioCalendario @Inject constructor(
    db: FirebaseDatabase
) {
    private val refUsuarios: DatabaseReference = db.getReference("Usuarios")

    suspend fun agregarDisponibilidad(idUsuario: String, disponibilidad: DisponibilidadTemporal) {
        refUsuarios.child(idUsuario).child("disponibilidad").push().setValue(disponibilidad).await()
    }

    suspend fun eliminarDisponibilidad(idUsuario: String, idDisponibilidad: String) {
        refUsuarios.child(idUsuario).child("disponibilidad").child(idDisponibilidad).removeValue().await()
    }

    suspend fun actualizarDisponibilidad(idUsuario: String, disponibilidad: DisponibilidadTemporal) {
        refUsuarios.child(idUsuario).child("disponibilidad").child(disponibilidad.id).setValue(disponibilidad).await()
    }

    suspend fun consultarDisponibilidad(idUsuario: String): List<DisponibilidadTemporal> {
        val snapshot = refUsuarios.child(idUsuario).child("disponibilidad").get().await()
        return snapshot.children.mapNotNull { it.getValue(DisponibilidadTemporal::class.java) }
    }
}