package com.example.localpros.data.repository

import com.example.localpros.data.model.Oferta
import com.example.localpros.data.model.DataState
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import javax.inject.Inject

class RepositorioBorradores@Inject constructor(private val database: FirebaseDatabase) {

    fun crearBorrador(oferta: Oferta, callback: (DataState<Boolean>) -> Unit) {
        database.getReference("borradores").child(oferta.id).setValue(oferta)
            .addOnSuccessListener {
                callback(DataState.Success(true))
            }.addOnFailureListener { exception ->
                callback(DataState.Error(exception))
            }
    }

    fun actualizarBorrador(oferta: Oferta, callback: (DataState<Boolean>) -> Unit) {
        database.getReference("borradores").child(oferta.id).setValue(oferta)
            .addOnSuccessListener {
                callback(DataState.Success(true))
            }.addOnFailureListener { exception ->
                callback(DataState.Error(exception))
            }
    }

    fun eliminarBorrador(ofertaId: String, callback: (DataState<Boolean>) -> Unit) {
        database.getReference("borradores").child(ofertaId).removeValue()
            .addOnSuccessListener {
                callback(DataState.Success(true))
            }.addOnFailureListener { exception ->
                callback(DataState.Error(exception))
            }
    }

    fun obtenerBorradores(callback: (DataState<List<Oferta>>) -> Unit) {
        database.getReference("borradores")
            .get().addOnSuccessListener { dataSnapshot ->
                val borradores = dataSnapshot.children.mapNotNull { it.getValue<Oferta>() }
                callback(DataState.Success(borradores))
            }.addOnFailureListener { exception ->
                callback(DataState.Error(exception))
            }
    }
}
