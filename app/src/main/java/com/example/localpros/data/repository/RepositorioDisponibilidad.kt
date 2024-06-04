package com.example.localpros.data.repository

import com.example.localpros.data.model.PosicionYRadio
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositorioDisponibilidad @Inject constructor(
    private val db: FirebaseDatabase
) {
    suspend fun guardarDisponibilidad(
        idUsuario: String,
        inicio: LocalDateTime,
        fin: LocalDateTime,
        ubicacion: PosicionYRadio,
        oficina: String,
        radioAccion: Double
    ): Result<Boolean> {
        return try {
            val disponibilidad = hashMapOf(
                "inicio" to inicio.toEpochSecond(ZoneOffset.UTC),
                "fin" to fin.toEpochSecond(ZoneOffset.UTC),
                "ubicacion" to mapOf(
                    "latitud" to ubicacion.latitud,
                    "longitud" to ubicacion.longitud,
                    "radioKm" to ubicacion.radioKm
                ),
                "oficina" to oficina,
                "radioAccion" to radioAccion
            )

            db.getReference("disponibilidad")
                .child(idUsuario)
                .setValue(disponibilidad)
                .await()

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
