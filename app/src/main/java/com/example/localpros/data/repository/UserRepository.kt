package com.example.localpros.data.repository

import com.example.localpros.data.model.*
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import javax.inject.Inject

class UserRepository @Inject constructor(private val db: FirebaseDatabase) {

    // Obtener datos de un particular por ID
    fun getParticularById(userId: String, callback: (DataState<Particular>) -> Unit) {
        db.getReference("Usuarios").child(userId).child("rolesAsignados").child("particular")
            .get().addOnSuccessListener { dataSnapshot ->
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

    // Obtener datos de un profesional por ID
    fun getProfesionalById(userId: String, callback: (DataState<Profesional>) -> Unit) {
        db.getReference("Usuarios").child(userId).child("rolesAsignados").child("profesional")
            .get().addOnSuccessListener { dataSnapshot ->
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

    // Actualizar datos de un particular
    fun updateParticular(userId: String, particular: Particular, callback: (DataState<Boolean>) -> Unit) {
        db.getReference("Usuarios").child(userId).child("rolesAsignados").child("particular")
            .setValue(particular)
            .addOnSuccessListener {
                callback(DataState.Success(true))
            }.addOnFailureListener { exception ->
                callback(DataState.Error(exception))
            }
    }

    // Eliminar un usuario particular o profesional
    fun deleteUser(userId: String, callback: (DataState<Boolean>) -> Unit) {
        db.getReference("Usuarios").child(userId)
            .removeValue()
            .addOnSuccessListener {
                callback(DataState.Success(true))
            }.addOnFailureListener { exception ->
                callback(DataState.Error(exception))
            }
    }

    // Consultar reseñas emitidas por un particular
    fun getReviewsByParticular(userId: String, callback: (DataState<List<Resena>>) -> Unit) {
        db.getReference("Usuarios").child(userId).child("rolesAsignados").child("particular").child("ReseñasEmitidas")
            .get().addOnSuccessListener { dataSnapshot ->
                val reviews = mutableListOf<Resena>()
                dataSnapshot.children.forEach { childSnapshot ->
                    childSnapshot.getValue<Resena>()?.let {
                        reviews.add(it)
                    }
                }
                callback(DataState.Success(reviews))
            }.addOnFailureListener { exception ->
                callback(DataState.Error(exception))
            }
    }

    // Actualizar datos de un profesional
    fun updateProfesional(userId: String, profesional: Profesional, callback: (DataState<Boolean>) -> Unit) {
        db.getReference("Usuarios").child(userId).child("rolesAsignados").child("profesional")
            .setValue(profesional)
            .addOnSuccessListener {
                callback(DataState.Success(true))
            }.addOnFailureListener { exception ->
                callback(DataState.Error(exception))
            }
    }

    // Obtener reseñas recibidas por un profesional
    fun getReceivedReviews(profesionalId: String, callback: (DataState<List<Resena>>) -> Unit) {
        db.getReference("Usuarios").child(profesionalId).child("rolesAsignados").child("profesional").child("resenaRecibida")
            .get().addOnSuccessListener { dataSnapshot ->
                val reviews = mutableListOf<Resena>()
                dataSnapshot.children.forEach { childSnapshot ->
                    childSnapshot.getValue<Resena>()?.let {
                        reviews.add(it)
                    }
                }
                callback(DataState.Success(reviews))
            }.addOnFailureListener { exception ->
                callback(DataState.Error(exception))
            }
    }

    // Función para actualizar las preferencias del usuario
    fun updateUserPreferences(
        userId: String,
        userRole: UserRole,
        cif: String,
        notificationsNewOffers: Boolean,
        notificationsCandidatures: Boolean,
        notificationsMessages: Boolean,
        profileVisibility: Boolean,
        accountStatus: Boolean,
        contactInfoSharing: Boolean,
        language: String,
        serviceCategories: Set<String>,
        selectedLocation: LatLng,
        selectedDate: org.threeten.bp.LocalDate,
        callback: (Result<Boolean>) -> Unit
    ) {
        val ref = db.getReference("Usuarios").child(userId)
        val userPreferences = mapOf(
            "userRole" to userRole.name,
            "cif" to cif,
            "notificationsNewOffers" to notificationsNewOffers,
            "notificationsCandidatures" to notificationsCandidatures,
            "notificationsMessages" to notificationsMessages,
            "profileVisibility" to profileVisibility,
            "accountStatus" to accountStatus,
            "contactInfoSharing" to contactInfoSharing,
            "language" to language,
            "serviceCategories" to serviceCategories,
            "selectedLocation" to mapOf("lat" to selectedLocation.latitude, "lng" to selectedLocation.longitude),
            "selectedDate" to selectedDate.toString()
        )

        ref.updateChildren(userPreferences).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(Result.success(true))
            } else {
                callback(Result.failure(task.exception ?: Exception("Error desconocido.")))
            }
        }
    }
}
