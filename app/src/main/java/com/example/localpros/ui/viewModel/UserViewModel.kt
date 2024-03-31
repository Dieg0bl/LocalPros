package com.example.localpros.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localpros.data.model.Particular
import com.example.localpros.data.model.Profesional
import com.example.localpros.data.model.UserPreferences
import com.example.localpros.data.model.UserRole
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : ViewModel() {

    private val _particularData = MutableStateFlow<Particular?>(null)
    val particularData: StateFlow<Particular?> = _particularData

    private val _profesionalData = MutableStateFlow<Profesional?>(null)
    val profesionalData: StateFlow<Profesional?> = _profesionalData

    fun loadUserData(userId: String, userRole: UserRole) {
        viewModelScope.launch {
            when (userRole) {
                UserRole.Particular -> {
                    val dataSnapshot = firebaseDatabase.reference.child("particulares").child(userId).get().await()
                    _particularData.value = dataSnapshot.getValue(Particular::class.java)
                }
                UserRole.Profesional -> {
                    val dataSnapshot = firebaseDatabase.reference.child("profesionales").child(userId).get().await()
                    _profesionalData.value = dataSnapshot.getValue(Profesional::class.java)
                }
            }
        }
    }
}
