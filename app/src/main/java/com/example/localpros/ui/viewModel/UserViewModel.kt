package com.example.localpros.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localpros.data.model.DataState
import com.example.localpros.data.model.Particular
import com.example.localpros.data.model.Profesional
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


    private val _particularData = MutableStateFlow<DataState<Particular?>>(DataState.Loading)
    val particularData: StateFlow<DataState<Particular?>> = _particularData


    private val _profesionalData = MutableStateFlow<DataState<Profesional?>>(DataState.Loading)
    val profesionalData: StateFlow<DataState<Profesional?>> = _profesionalData


    fun loadUserData(userId: String, userRole: UserRole) {
        viewModelScope.launch {
            when (userRole) {
                UserRole.Particular -> {
                    _particularData.value = DataState.Loading
                    try {
                        val dataSnapshot =
                            firebaseDatabase.reference.child("particulares").child(userId).get()
                                .await()
                        val particular = dataSnapshot.getValue(Particular::class.java)
                        _particularData.value = DataState.Success(particular)
                    } catch (e: Exception) {
                        _particularData.value = DataState.Error(e)
                    }
                }

                UserRole.Profesional -> {
                    _profesionalData.value = DataState.Loading
                    try {
                        val dataSnapshot =
                            firebaseDatabase.reference.child("profesionales").child(userId).get()
                                .await()
                        val profesional = dataSnapshot.getValue(Profesional::class.java)
                        _profesionalData.value = DataState.Success(profesional)
                    } catch (e: Exception) {
                        _profesionalData.value = DataState.Error(e)
                    }
                }
            }
        }
    }
}