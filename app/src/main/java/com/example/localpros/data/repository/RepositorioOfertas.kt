package com.example.localpros.data.repository

import com.example.localpros.data.model.DataState
import com.example.localpros.data.model.Oferta
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import javax.inject.Inject

class RepositorioOfertas@Inject constructor(private val database: FirebaseDatabase) {

    fun crearOferta(oferta: Oferta, callback: (DataState<Boolean>) -> Unit) {
        database.getReference("ofertas").child(oferta.id).setValue(oferta)
            .addOnSuccessListener {
                callback(DataState.Success(true))
            }.addOnFailureListener { exception ->
                callback(DataState.Error(exception))
            }
    }

    fun actualizarOferta(oferta: Oferta, callback: (DataState<Boolean>) -> Unit) {
        database.getReference("ofertas").child(oferta.id).setValue(oferta)
            .addOnSuccessListener {
                callback(DataState.Success(true))
            }.addOnFailureListener { exception ->
                callback(DataState.Error(exception))
            }
    }

    fun eliminarOferta(ofertaId: String, callback: (DataState<Boolean>) -> Unit) {
        database.getReference("ofertas").child(ofertaId).removeValue()
            .addOnSuccessListener {
                callback(DataState.Success(true))
            }.addOnFailureListener { exception ->
                callback(DataState.Error(exception))
            }
    }

    fun obtenerOfertaPorId(ofertaId: String, callback: (DataState<Oferta>) -> Unit) {
        database.getReference("ofertas").child(ofertaId)
            .get().addOnSuccessListener { dataSnapshot ->
                val oferta = dataSnapshot.getValue<Oferta>()
                if (oferta != null) {
                    callback(DataState.Success(oferta))
                } else {
                    callback(DataState.Error(Exception("Oferta no encontrada")))
                }
            }.addOnFailureListener { exception ->
                callback(DataState.Error(exception))
            }
    }

    fun obtenerOfertas(callback: (DataState<List<Oferta>>) -> Unit) {
        database.getReference("ofertas")
            .get().addOnSuccessListener { dataSnapshot ->
                val ofertas = dataSnapshot.children.mapNotNull { it.getValue<Oferta>() }
                callback(DataState.Success(ofertas))
            }.addOnFailureListener { exception ->
                callback(DataState.Error(exception))
            }
    }
}
