
package com.example.localpros.data.repository

import com.example.localpros.data.model.PosicionYRadio
import com.google.firebase.database.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await


@Singleton
class RepositorioUbicacion @Inject constructor(
    db: FirebaseDatabase
) {
    private val refUsuarios: DatabaseReference = db.getReference("Usuarios")

    suspend fun actualizarZonaTrabajo(idUsuario: String, posicionYRadio: PosicionYRadio) {
        refUsuarios.child(idUsuario).child("zonaTrabajo").setValue(posicionYRadio).await()
    }

    suspend fun obtenerZonaTrabajo(idUsuario: String): PosicionYRadio? {
        val snapshot = refUsuarios.child(idUsuario).child("zonaTrabajo").get().await()
        return snapshot.getValue(PosicionYRadio::class.java)
    }

    suspend fun definirRadioAccion(idUsuario: String, radio: Double) {
        refUsuarios.child(idUsuario).child("radioAccion").setValue(radio).await()
    }

    suspend fun obtenerRadioAccion(idUsuario: String): Double? {
        val snapshot = refUsuarios.child(idUsuario).child("radioAccion").get().await()
        return snapshot.getValue(Double::class.java)
    }
}