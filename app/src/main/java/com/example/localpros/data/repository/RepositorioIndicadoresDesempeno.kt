package com.example.localpros.data.repository

import com.example.localpros.data.model.DataState
import com.example.localpros.data.model.IndicadoresDesempeno
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue


class RepositorioIndicadoresDesempeno(private val database: FirebaseDatabase){
    fun actualizarIndicadoresDesempeño(profesionalId: String, indicadores: IndicadoresDesempeno, callback: (DataState<Boolean>) -> Unit) {
        database.getReference("indicadoresDesempeño").child(profesionalId).setValue(indicadores)
            .addOnSuccessListener {
                callback(DataState.Success(true))
            }.addOnFailureListener { exception ->
                callback(DataState.Error(exception))
            }
    }

    fun obtenerIndicadoresDesempeño(profesionalId: String, callback: (DataState<IndicadoresDesempeno>) -> Unit) {
        database.getReference("indicadoresDesempeño").child(profesionalId)
            .get().addOnSuccessListener { dataSnapshot ->
                val indicadores = dataSnapshot.getValue<IndicadoresDesempeno>()
                if (indicadores != null) {
                    callback(DataState.Success(indicadores))
                } else {
                    callback(DataState.Error(Exception("Indicadores no encontrados")))
                }
            }.addOnFailureListener { exception ->
                callback(DataState.Error(exception))
            }
    }
}
