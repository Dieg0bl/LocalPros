package com.example.localpros.data.repository

import com.example.localpros.data.model.ObtencionDatos
import com.example.localpros.data.model.PosicionYRadio
import com.example.localpros.data.model.enums.Rol
import com.example.localpros.data.model.usuarioModel.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositorioUsuarios @Inject constructor(
    private val databaseReference: DatabaseReference,
    private val auth: FirebaseAuth
) {

    private fun usuarioRef(userId: String): DatabaseReference {
        return databaseReference.child("usuarios").child(userId)
    }

    private fun rolesRef(userId: String): DatabaseReference {
        return usuarioRef(userId).child("roles")
    }

    fun obtenerUsuarioPorId(userId: String, callback: (Usuario?) -> Unit) {
        val ref = usuarioRef(userId)
        ref.get().addOnSuccessListener {
            val usuario = it.getValue(Usuario::class.java)
            callback(usuario)
        }.addOnFailureListener {
            callback(null)
        }
    }

    fun asignarRol(userId: String, rol: Rol, callback: (ObtencionDatos<Boolean>) -> Unit) {
        val ref = rolesRef(userId).child(rol.name)
        ref.setValue(true).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(ObtencionDatos.Exitosa(true))
            } else {
                callback(ObtencionDatos.Error(task.exception!!))
            }
        }
    }

    fun eliminarRol(userId: String, rol: Rol, callback: (ObtencionDatos<Boolean>) -> Unit) {
        val ref = rolesRef(userId).child(rol.name)
        ref.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(ObtencionDatos.Exitosa(true))
            } else {
                callback(ObtencionDatos.Error(task.exception!!))
            }
        }
    }

    fun desactivarRol(userId: String, rol: Rol, callback: (ObtencionDatos<Boolean>) -> Unit) {
        val ref = rolesRef(userId).child(rol.name)
        ref.setValue("INACTIVO").addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(ObtencionDatos.Exitosa(true))
            } else {
                callback(ObtencionDatos.Error(task.exception!!))
            }
        }
    }

    fun actualizarUbicacionUsuario(userId: String, nuevaUbicacion: PosicionYRadio, callback: (ObtencionDatos<Boolean>) -> Unit) {
        val ref = usuarioRef(userId).child("ubicacion")
        ref.setValue(nuevaUbicacion).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(ObtencionDatos.Exitosa(true))
            } else {
                callback(ObtencionDatos.Error(task.exception!!))
            }
        }
    }

    suspend fun eliminarUsuario(userId: String) {
        try {
            val user = auth.currentUser
            if (user != null && user.uid == userId) {
                user.delete().await()
            }

            val userRef = usuarioRef(userId)
            userRef.removeValue().await()

            val ofertasRef = databaseReference.child("ofertas").orderByChild("idPublicador").equalTo(userId)
            ofertasRef.get().await().children.forEach { snapshot ->
                snapshot.ref.removeValue().await()
            }

            val candidaturasRef = databaseReference.child("candidaturas").orderByChild("idCandidato").equalTo(userId)
            candidaturasRef.get().await().children.forEach { snapshot ->
                snapshot.ref.removeValue().await()
            }
        } catch (e: Exception) {
            throw e
        }
    }

    fun activarRolProfesional(userId: String, cif: String, callback: (ObtencionDatos<Boolean>) -> Unit) {
        val ref = rolesRef(userId).child(Rol.PROFESIONAL.name)
        val updates = mapOf("cif" to cif, "rol" to true)

        ref.updateChildren(updates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(ObtencionDatos.Exitosa(true))
            } else {
                callback(ObtencionDatos.Error(task.exception!!))
            }
        }
    }

    fun obtenerRolesUsuario(userId: String, callback: (Set<Rol>) -> Unit) {
        val ref = rolesRef(userId)
        ref.get().addOnSuccessListener { dataSnapshot ->
            val roles = dataSnapshot.children.mapNotNull { snapshot ->
                Rol.values().find { it.name == snapshot.key }
            }.toSet()
            callback(roles)
        }.addOnFailureListener {
            callback(emptySet())
        }
    }
}
