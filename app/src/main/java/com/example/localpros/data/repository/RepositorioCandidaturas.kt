package com.example.localpros.data.repository


import com.example.localpros.data.model.Candidatura
import com.example.localpros.data.model.DataState
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import javax.inject.Inject

class RepositorioCandidaturas @Inject constructor(private val database: FirebaseDatabase) {

    fun presentarCandidatura(candidatura: Candidatura, callback: (DataState<Boolean>) -> Unit) {
        database.getReference("candidaturas").child(candidatura.id).setValue(candidatura)
            .addOnSuccessListener {
                callback(DataState.Success(true))
            }.addOnFailureListener { exception ->
                callback(DataState.Error(exception))
            }
    }

    fun actualizarCandidatura(candidatura: Candidatura, callback: (DataState<Boolean>) -> Unit) {
        database.getReference("candidaturas").child(candidatura.id).setValue(candidatura)
            .addOnSuccessListener {
                callback(DataState.Success(true))
            }.addOnFailureListener { exception ->
                callback(DataState.Error(exception))
            }
    }

    fun retirarCandidatura(candidaturaId: String, callback: (DataState<Boolean>) -> Unit) {
        database.getReference("candidaturas").child(candidaturaId).removeValue()
            .addOnSuccessListener {
                callback(DataState.Success(true))
            }.addOnFailureListener { exception ->
                callback(DataState.Error(exception))
            }
    }

    fun obtenerCandidaturaPorId(candidaturaId: String, callback: (DataState<Candidatura>) -> Unit) {
        database.getReference("candidaturas").child(candidaturaId)
            .get().addOnSuccessListener { dataSnapshot ->
                val candidatura = dataSnapshot.getValue<Candidatura>()
                if (candidatura != null) {
                    callback(DataState.Success(candidatura))
                } else {
                    callback(DataState.Error(Exception("Candidatura no encontrada")))
                }
            }.addOnFailureListener { exception ->
                callback(DataState.Error(exception))
            }
    }

    fun obtenerCandidaturas(callback: (DataState<List<Candidatura>>) -> Unit) {
        database.getReference("candidaturas")
            .get().addOnSuccessListener { dataSnapshot ->
                val candidaturas = dataSnapshot.children.mapNotNull { it.getValue<Candidatura>() }
                callback(DataState.Success(candidaturas))
            }.addOnFailureListener { exception ->
                callback(DataState.Error(exception))
            }
    }
}
