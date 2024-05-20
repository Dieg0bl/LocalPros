package com.example.localpros.data.repository

import com.example.localpros.data.model.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import javax.inject.Inject


class RepositorioNotificaciones@Inject constructor(private val database: FirebaseDatabase) {


            fun crearNotificacion(notificacion: Notificacion, callback: (DataState<Boolean>) -> Unit) {
                database.getReference("notificaciones").child(notificacion.id).setValue(notificacion)
                    .addOnSuccessListener {
                        callback(DataState.Success(true))
                    }.addOnFailureListener { exception ->
                        callback(DataState.Error(exception))
                    }
            }

            fun eliminarNotificacion(notificacionId: String, callback: (DataState<Boolean>) -> Unit) {
                database.getReference("notificaciones").child(notificacionId).removeValue()
                    .addOnSuccessListener {
                        callback(DataState.Success(true))
                    }.addOnFailureListener { exception ->
                        callback(DataState.Error(exception))
                    }
            }

            fun obtenerNotificaciones(usuarioId: String, callback: (DataState<List<Notificacion>>) -> Unit) {
                database.getReference("notificaciones").orderByChild("usuarioId").equalTo(usuarioId)
                    .get().addOnSuccessListener { dataSnapshot ->
                        val notificaciones = dataSnapshot.children.mapNotNull { it.getValue<Notificacion>() }
                        callback(DataState.Success(notificaciones))
                    }.addOnFailureListener { exception ->
                        callback(DataState.Error(exception))
                    }
            }
        }
