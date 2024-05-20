package com.example.localpros.data.repository

import com.example.localpros.data.model.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import javax.inject.Inject

class RepositorioBusqueda@Inject constructor(private val database: FirebaseDatabase) {

    fun buscarOfertas(filtro: Map<String, Any>, callback: (DataState<List<Oferta>>) -> Unit) {
        val ref = database.getReference("ofertas")
        var query = ref.orderByKey()

        filtro.forEach { (clave, valor) ->
            query = when (clave) {
                "ubicacion" -> query.orderByChild("ubicacion").equalTo(valor as String)
                "categoria" -> query.orderByChild("categoria").equalTo(valor as String)
                else -> query
            }
        }

        query.get().addOnSuccessListener { dataSnapshot ->
            val ofertas = dataSnapshot.children.mapNotNull { it.getValue<Oferta>() }
            callback(DataState.Success(ofertas))
        }.addOnFailureListener { exception ->
            callback(DataState.Error(exception))
        }
    }

    fun buscarProfesionales(filtro: Map<String, Any>, callback: (DataState<List<Profesional>>) -> Unit) {
        val ref = database.getReference("profesionales")
        var query = ref.orderByKey()

        filtro.forEach { (clave, valor) ->
            query = when (clave) {
                "ubicacion" -> query.orderByChild("ubicacion").equalTo(valor as String)
                "especialidad" -> query.orderByChild("especialidad").equalTo(valor as String)
                else -> query
            }
        }

        query.get().addOnSuccessListener { dataSnapshot ->
            val profesionales = dataSnapshot.children.mapNotNull { it.getValue<Profesional>() }
            callback(DataState.Success(profesionales))
        }.addOnFailureListener { exception ->
            callback(DataState.Error(exception))
        }
    }
}
