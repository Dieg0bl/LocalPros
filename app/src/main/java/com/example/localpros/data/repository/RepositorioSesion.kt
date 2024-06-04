
package com.example.localpros.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositorioSesion @Inject constructor(
    private val auth: FirebaseAuth
) {
    fun iniciarSesion(correoElectronico: String, contrasena: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(correoElectronico, contrasena)
    }

    fun cerrarSesion(): Task<Void> {
        auth.signOut()
        return Tasks.forResult(null)
    }

    fun obtenerUsuarioActual(): FirebaseUser? {
        return auth.currentUser
    }
}
