package com.example.localpros.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositorioAutenticacion @Inject constructor(
    private val auth: FirebaseAuth
) {
    fun autenticarUsuario(correoElectronico: String, contrasena: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(correoElectronico, contrasena)
    }

    fun cambiarContrasena(nuevaContrasena: String): Task<Void>? {
        val user = auth.currentUser
        return user?.updatePassword(nuevaContrasena)
    }

    fun recuperarContrasena(correoElectronico: String): Task<Void> {
        return auth.sendPasswordResetEmail(correoElectronico)
    }

    fun registrarUsuario(correoElectronico: String, contrasena: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(correoElectronico, contrasena)
    }
}