package com.example.localpros.data.repository

import com.example.localpros.data.model.Candidatura
import com.example.localpros.data.model.ObtencionDatos
import com.example.localpros.data.model.Oferta
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositorioOfertas @Inject constructor(
    db: FirebaseDatabase
) {
    private val refOfertas: DatabaseReference = db.getReference("Ofertas")

    suspend fun crearOferta(oferta: Oferta, callback: (ObtencionDatos<Oferta>) -> Unit) {
        val nuevaOfertaRef = refOfertas.push()
        nuevaOfertaRef.setValue(oferta.copy(id = nuevaOfertaRef.key ?: "")).await()
        callback(ObtencionDatos.Exitosa(oferta))
    }

    fun crearNuevaOfertaReferencia(): DatabaseReference {
        return refOfertas.push()
    }

    suspend fun eliminarOferta(idOferta: String) {
        refOfertas.child(idOferta).removeValue().await()
    }

    suspend fun listarOfertas(): List<Oferta> {
        val snapshot = refOfertas.get().await()
        return snapshot.children.mapNotNull { it.getValue(Oferta::class.java) }
    }

    suspend fun obtenerDetallesOferta(idOferta: String): Oferta? {
        val snapshot = refOfertas.child(idOferta).get().await()
        return snapshot.getValue(Oferta::class.java)
    }

    suspend fun actualizarPlazoEjecucion(idOferta: String, nuevoPlazo: Long) {
        refOfertas.child(idOferta).child("plazoEjecucion").setValue(nuevoPlazo).await()
    }

    suspend fun modificarPresupuestoOferta(idOferta: String, nuevoPresupuesto: Double) {
        refOfertas.child(idOferta).child("presupuesto").setValue(nuevoPresupuesto).await()
    }

    suspend fun definirCalidadEsperada(idOferta: String, nuevaCalidad: String) {
        refOfertas.child(idOferta).child("calidadEsperada").setValue(nuevaCalidad).await()
    }

    suspend fun obtenerCandidaturasRecibidas(idOferta: String): List<Candidatura> {
        val snapshot = refOfertas.child(idOferta).child("candidaturas").get().await()
        return snapshot.children.mapNotNull { it.getValue(Candidatura::class.java) }
    }

    suspend fun seleccionarCandidato(idOferta: String, idCandidato: String) {
        refOfertas.child(idOferta).child("candidatoSeleccionado").setValue(idCandidato).await()
    }

    fun obtenerOfertaPorId(ofertaId: String, callback: (ObtencionDatos<Oferta>) -> Unit) {
        val ref = refOfertas.child(ofertaId)
        ref.get().addOnSuccessListener {
            val oferta = it.getValue(Oferta::class.java)
            callback(ObtencionDatos.Exitosa(oferta!!))
        }.addOnFailureListener {
            callback(ObtencionDatos.Error(it))
        }
    }
}
