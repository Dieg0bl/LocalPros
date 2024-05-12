package com.example.localpros.ui.viewModel

import androidx.lifecycle.ViewModel
import com.example.localpros.data.model.*
import com.example.localpros.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _userData = MutableStateFlow<DataState<User?>>(DataState.Loading)
    val userData: StateFlow<DataState<User?>> = _userData

    fun loadUserData(userId: String, userRole: UserRole) {
        when (userRole) {
            UserRole.Particular -> loadParticular(userId)
            UserRole.Profesional -> loadProfesional(userId)
        }
    }

    private fun loadParticular(userId: String) {
        userRepository.getParticularById(userId) { result ->
            _userData.value = result
        }
    }

    private fun loadProfesional(userId: String) {
        userRepository.getProfesionalById(userId) { result ->
            _userData.value = result
        }
    }
}
