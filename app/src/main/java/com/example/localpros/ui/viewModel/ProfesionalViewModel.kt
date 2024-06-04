package com.example.localpros.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localpros.data.model.Candidatura
import com.example.localpros.data.model.Oferta
import com.example.localpros.data.model.usuarioModel.Usuario
import com.example.localpros.data.repository.RepositorioProfesional
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfesionalViewModel @Inject constructor(
    private val repositorioProfesional: RepositorioProfesional,
) : ViewModel() {

    private val _ofertasDisponibles = MutableStateFlow<List<Oferta>>(emptyList())
    val ofertasDisponibles: StateFlow<List<Oferta>> get() = _ofertasDisponibles

    private val _candidaturas = MutableStateFlow<List<Candidatura>>(emptyList())
    val candidaturas: StateFlow<List<Candidatura>> get() = _candidaturas

    fun cargarOfertasDisponibles(profesional: Usuario) {
        viewModelScope.launch {
            _ofertasDisponibles.value = repositorioProfesional.obtenerOfertasDisponibles(profesional)
        }
    }

    fun cargarCandidaturas(profesionalId: String) {
        viewModelScope.launch {
            _candidaturas.value = repositorioProfesional.obtenerCandidaturas(profesionalId)
        }
    }


    fun eliminarCandidatura(profesionalId: String, candidaturaId: String) {
        viewModelScope.launch {
            repositorioProfesional.eliminarCandidatura(profesionalId, candidaturaId)
            cargarCandidaturas(profesionalId)
        }
    }
}