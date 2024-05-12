package com.example.localpros.data.repository

import com.example.localpros.data.model.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue


class UserRepository(private val db: FirebaseDatabase) {

    fun getParticularById(userId: String, callback: (DataState<Particular>) -> Unit) {
        db.getReference("particulares").child(userId).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val particular = dataSnapshot.getValue<Particular>()
                if (particular != null) {
                    callback(DataState.Success(particular))
                } else {
                    callback(DataState.Error(Exception("Particular not found")))
                }
            } else {
                callback(DataState.Error(Exception("No such document")))
            }
        }.addOnFailureListener { exception ->
            callback(DataState.Error(exception))
        }
    }

    fun getProfesionalById(userId: String, callback: (DataState<Profesional>) -> Unit) {
        db.getReference("profesionales").child(userId).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val profesional = dataSnapshot.getValue<Profesional>()
                if (profesional != null) {
                    callback(DataState.Success(profesional))
                } else {
                    callback(DataState.Error(Exception("Profesional not found")))
                }
            } else {
                callback(DataState.Error(Exception("No such document")))
            }
        }.addOnFailureListener { exception ->
            callback(DataState.Error(exception))
        }
    }
}
