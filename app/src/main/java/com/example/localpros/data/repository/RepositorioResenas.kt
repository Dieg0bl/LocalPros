package com.example.localpros.data.repository


import com.example.localpros.data.model.DataState
import com.example.localpros.data.model.Resena
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import javax.inject.Inject

class RepositorioReseñas@Inject constructor(private val database: FirebaseDatabase) {

    fun crearReseña(resena: Resena, callback: (DataState<Boolean>) -> Unit) {
        database.getReference("resenas").child(resena.reseñaId).setValue(resena)
            .addOnSuccessListener {
                callback(DataState.Success(true))
            }.addOnFailureListener { exception ->
                callback(DataState.Error(exception))
            }
    }

    fun actualizarReseña(resena: Resena, callback: (DataState<Boolean>) -> Unit) {
        database.getReference("resenas").child(resena.reseñaId).setValue(resena)
            .addOnSuccessListener {
                callback(DataState.Success(true))
            }.addOnFailureListener { exception ->
                callback(DataState.Error(exception))
            }
    }

    fun eliminarReseña(resenaId: String, callback: (DataState<Boolean>) -> Unit) {
        database.getReference("resenas").child(resenaId).removeValue()
            .addOnSuccessListener {
                callback(DataState.Success(true))
            }.addOnFailureListener { exception ->
                callback(DataState.Error(exception))
            }
    }

    fun obtenerReseñaPorId(resenaId: String, callback: (DataState<Resena>) -> Unit) {
        database.getReference("resenas").child(resenaId)
            .get().addOnSuccessListener { dataSnapshot ->
                val resena = dataSnapshot.getValue<Resena>()
                if (resena != null) {
                    callback(DataState.Success(resena))
                } else {
                    callback(DataState.Error(Exception("Reseña no encontrada")))
                }
            }.addOnFailureListener { exception ->
                callback(DataState.Error(exception))
            }
    }

    fun obtenerResenas(callback: (DataState<List<Resena>>) -> Unit) {
        database.getReference("resenas")
            .get().addOnSuccessListener { dataSnapshot ->
                val resenas = dataSnapshot.children.mapNotNull { it.getValue<Resena>() }
                callback(DataState.Success(resenas))
            }.addOnFailureListener { exception ->
                callback(DataState.Error(exception))
            }
    }
}
