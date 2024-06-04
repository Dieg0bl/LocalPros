
package com.example.localpros.data.repository

import com.example.localpros.data.model.Notificacion
import com.google.firebase.database.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

@Singleton
class RepositorioNotificaciones @Inject constructor(
    db: FirebaseDatabase
) {
    private val refNotificaciones: DatabaseReference = db.getReference("Notificaciones")

    suspend fun enviarNotificacion(notificacion: Notificacion) {
        val nuevaNotificacionRef = refNotificaciones.push()
        nuevaNotificacionRef.setValue(notificacion.copy(id = nuevaNotificacionRef.key ?: "")).await()
    }

    suspend fun listarNotificaciones(idUsuario: String): List<Notificacion> {
        val snapshot = refNotificaciones.orderByChild("userId").equalTo(idUsuario).get().await()
        return snapshot.children.mapNotNull { it.getValue(Notificacion::class.java) }
    }

    suspend fun obtenerDetallesNotificacion(idNotificacion: String): Notificacion? {
        val snapshot = refNotificaciones.child(idNotificacion).get().await()
        return snapshot.getValue(Notificacion::class.java)
    }

    suspend fun enviarNotificacionCandidatura(idCandidatura: String, notificacion: Notificacion) {
        refNotificaciones.child(idCandidatura).setValue(notificacion).await()
    }

    suspend fun enviarNotificacionCambioEstadoOferta(idOferta: String, notificacion: Notificacion) {
        refNotificaciones.child(idOferta).setValue(notificacion).await()
    }
}