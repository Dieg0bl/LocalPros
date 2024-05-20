package com.example.localpros.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.example.localpros.data.model.UserPreferences
import com.example.localpros.data.model.UserRole
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: DatabaseReference = FirebaseDatabase.getInstance().reference

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Success(val user: FirebaseUser, val role: UserRole) : AuthState()
        data class Error(val message: String) : AuthState()
    }

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> get() = _authState

    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null) {
                            fetchUserRole(user)
                        } else {
                            _authState.value = AuthState.Error("Usuario no encontrado.")
                        }
                    } else {
                        _authState.value = AuthState.Error(task.exception?.localizedMessage ?: "Error desconocido.")
                    }
                }
        }
    }

    private fun fetchUserRole(user: FirebaseUser) {
        val userId = user.uid
        db.child("Usuarios").child(userId).child("rol").get()
            .addOnSuccessListener { snapshot ->
                val roleString = snapshot.getValue(String::class.java)
                if (roleString != null) {
                    val role = UserRole.valueOf(roleString)
                    userPreferences.userId = userId
                    userPreferences.userRole = role
                    _authState.value = AuthState.Success(user, role)
                } else {
                    _authState.value = AuthState.Error("Rol no encontrado para el usuario.")
                }
            }
            .addOnFailureListener { exception ->
                _authState.value = AuthState.Error(exception.localizedMessage ?: "Error al obtener el rol del usuario.")
            }
    }
}
