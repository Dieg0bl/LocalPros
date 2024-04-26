package com.example.localpros.ui.viewModel

import android.content.SharedPreferences
import android.util.Log
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
    private val firebaseDatabase: FirebaseDatabase,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _particularData = MutableStateFlow<DataState<Particular?>>(DataState.Loading)
    val particularData: StateFlow<DataState<Particular?>> = _particularData

    private val _profesionalData = MutableStateFlow<DataState<Profesional?>>(DataState.Loading)
    val profesionalData: StateFlow<DataState<Profesional?>> = _profesionalData

    fun loadUserData(userId: String?, userRole: UserRole) {
        userId?.let {
            viewModelScope.launch {
                Log.d("UserViewModel", "Loading user data for $userId with role $userRole")
                val databaseReference = when (userRole) {
                    UserRole.Particular -> firebaseDatabase.reference.child("particulares")
                        .child(userId)

                    UserRole.Profesional -> firebaseDatabase.reference.child("profesionales")
                        .child(userId)
                }
                try {
                    val dataSnapshot = databaseReference.get().await()
                    when (userRole) {
                        UserRole.Particular -> {
                            val particular = dataSnapshot.getValue(Particular::class.java)
                            _particularData.value = DataState.Success(particular)
                            Log.d("UserViewModel", "Particular data loaded successfully")
                        }

                        UserRole.Profesional -> {
                            val profesional = dataSnapshot.getValue(Profesional::class.java)
                            _profesionalData.value = DataState.Success(profesional)
                            Log.d("UserViewModel", "Profesional data loaded successfully")
                        }
                    }
                } catch (e: Exception) {
                    when (userRole) {
                        UserRole.Particular -> {
                            _particularData.value = DataState.Error(e)
                            Log.e("UserViewModel", "Error loading particular data", e)
                        }

                        UserRole.Profesional -> {
                            _profesionalData.value = DataState.Error(e)
                            Log.e("UserViewModel", "Error loading profesional data", e)
                        }
                    }
                }
            }
        }
    }
}
