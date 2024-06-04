package com.example.localpros.data.repository

import com.example.localpros.data.model.*
import com.example.localpros.data.model.enums.EstadoOferta
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositorioParticular @Inject constructor(
    db: FirebaseDatabase
) {

    private val refOfertas = db.getReference("Ofertas")
    fun establecerRequisitosOferta(userId: String, oferta: Oferta): Task<Void> {
        return if (oferta.idPublicador == userId) {
            refOfertas.child(oferta.id).setValue(oferta)
        } else {
            throw IllegalAccessException("No tiene permiso para modificar esta oferta.")
        }
    }

    fun especificarUbicacionExacta(userId: String, oferta: Oferta): Task<Void> {
        return if (oferta.idPublicador == userId) {
            refOfertas.child(oferta.id).child("ubicacion").setValue(oferta.ubicacion)
        } else {
            throw IllegalAccessException("No tiene permiso para modificar esta oferta.")
        }
    }

    fun establecerPresupuestoDisponible(userId: String, oferta: Oferta, presupuesto: Double): Task<Void> {
        return if (oferta.idPublicador == userId) {
            refOfertas.child(oferta.id).child("presupuestoDisponible").setValue(presupuesto)
        } else {
            throw IllegalAccessException("No tiene permiso para modificar esta oferta.")
        }
    }

    fun definirCalidadEsperada(userId: String, oferta: Oferta, calidad: String): Task<Void> {
        return if (oferta.idPublicador == userId) {
            refOfertas.child(oferta.id).child("calidadEsperada").setValue(calidad)
        } else {
            throw IllegalAccessException("No tiene permiso para modificar esta oferta.")
        }
    }

    fun establecerPlazoEjecucion(userId: String, oferta: Oferta, plazo: String): Task<Void> {
        return if (oferta.idPublicador == userId) {
            refOfertas.child(oferta.id).child("plazoEjecucion").setValue(plazo)
        } else {
            throw IllegalAccessException("No tiene permiso para modificar esta oferta.")
        }
    }

    fun publicarOferta(userId: String, oferta: Oferta): Task<Void> {
        return if (oferta.idPublicador == userId) {
            oferta.estado = EstadoOferta.PUBLICADA
            refOfertas.child(oferta.id).setValue(oferta)
        } else {
            throw IllegalAccessException("No tiene permiso para modificar esta oferta.")
        }
    }

    fun verificarCandidaturas(userId: String, ofertaId: String): Task<DataSnapshot> {
        return refOfertas.child(ofertaId).child("userId").equalTo(userId).ref.child("candidaturas").get()
    }

    fun evaluarPropuestas(userId: String, ofertaId: String): Task<DataSnapshot> {
        return refOfertas.child(ofertaId).child("userId").equalTo(userId).ref.child("candidaturas").get()
    }

    fun seleccionarCandidato(userId: String, ofertaId: String, candidato: Candidatura): Task<Void> {
        return refOfertas.child(ofertaId).child("userId").equalTo(userId).ref.child("candidatoSeleccionado").setValue(candidato)
    }

    fun asignarTrabajo(userId: String, ofertaId: String, candidato: Candidatura): Task<Void> {
        return if (refOfertas.child(ofertaId).child("userId").equalTo(userId).ref != null) {
            refOfertas.child(ofertaId).child("estado").setValue(EstadoOferta.ACEPTADA)
        } else {
            throw IllegalAccessException("No tiene permiso para modificar esta oferta.")
        }
    }

    suspend fun obtenerOfertas(userId: String): List<Oferta> {
        return try {
            val snapshot = refOfertas.orderByChild("userId").equalTo(userId).get().await()
            snapshot.children.mapNotNull { it.getValue(Oferta::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun eliminarOferta(userId: String, ofertaId: String): Task<Void> {
        return refOfertas.child(ofertaId).child("userId").equalTo(userId).ref.removeValue()
    }
}
