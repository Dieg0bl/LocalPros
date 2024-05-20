package com.example.localpros.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localpros.data.model.*
import com.example.localpros.data.repository.UserRepository
import com.example.localpros.data.repository.RepositorioCandidaturas
import com.example.localpros.data.repository.RepositorioOfertas
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import org.threeten.bp.LocalDate

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val repositorioCandidaturas: RepositorioCandidaturas,
    private val repositorioOfertas: RepositorioOfertas
) : ViewModel() {

    // Estado del usuario
    private val _userData = MutableStateFlow<DataState<Usuario?>>(DataState.Loading)
    val userData: StateFlow<DataState<Usuario?>> = _userData

    // Estado de candidaturas
    private val _candidaturaData = MutableStateFlow<DataState<Candidatura?>>(DataState.Loading)
    val candidaturaData: StateFlow<DataState<Candidatura?>> = _candidaturaData

    // Estado de ofertas
    private val _ofertaData = MutableStateFlow<DataState<Oferta?>>(DataState.Loading)
    val ofertaData: StateFlow<DataState<Oferta?>> = _ofertaData

    // Cargar datos del usuario según el rol
    fun loadUserData(userId: String, userRole: UserRole) {
        _userData.value = DataState.Loading
        viewModelScope.launch {
            when (userRole) {
                UserRole.Particular -> userRepository.getParticularById(userId) { result ->
                    _userData.value = result
                }
                UserRole.Profesional -> userRepository.getProfesionalById(userId) { result ->
                    _userData.value = result
                }
            }
        }
    }

    // Cargar datos de una candidatura por ID
    fun loadCandidaturaData(candidaturaId: String) {
        _candidaturaData.value = DataState.Loading
        viewModelScope.launch {
            repositorioCandidaturas.obtenerCandidaturaPorId(candidaturaId) { result ->
                _candidaturaData.value = result
            }
        }
    }

    // Cargar datos de una oferta por ID
    fun loadOfertaData(ofertaId: String) {
        _ofertaData.value = DataState.Loading
        viewModelScope.launch {
            repositorioOfertas.obtenerOfertaPorId(ofertaId) { result ->
                _ofertaData.value = result
            }
        }
    }

    // Función para actualizar datos del particular
    fun updateParticular(userId: String, particular: Particular) {
        viewModelScope.launch {
            userRepository.updateParticular(userId, particular) { result ->
                if (result is DataState.Success) {
                    _userData.value = DataState.Success(particular)
                }
            }
        }
    }

    // Función para actualizar datos del profesional
    fun updateProfesional(userId: String, profesional: Profesional) {
        viewModelScope.launch {
            userRepository.updateProfesional(userId, profesional) { result ->
                if (result is DataState.Success) {
                    _userData.value = DataState.Success(profesional)
                }
            }
        }
    }

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
        selectedDate: LocalDate,
        callback: (Result<Boolean>) -> Unit
    ) {
        viewModelScope.launch {
            userRepository.updateUserPreferences(
                userId, userRole, cif, notificationsNewOffers, notificationsCandidatures,
                notificationsMessages, profileVisibility, accountStatus, contactInfoSharing, language,
                serviceCategories, selectedLocation, selectedDate, callback
            )
        }
    }
}
