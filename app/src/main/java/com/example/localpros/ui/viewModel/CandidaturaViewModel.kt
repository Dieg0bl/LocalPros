package com.example.localpros.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localpros.data.model.Candidatura
import com.example.localpros.data.model.ObtencionDatos
import com.example.localpros.data.repository.RepositorioCandidaturas
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CandidaturaViewModel @Inject constructor(
    private val candidaturaRepository: RepositorioCandidaturas
) : ViewModel() {

    private val _estadoCandidatura = MutableStateFlow<ObtencionDatos<Candidatura>>(ObtencionDatos.Cargando)
    val estadoCandidatura: StateFlow<ObtencionDatos<Candidatura>> get() = _estadoCandidatura

    fun cargarDatosCandidatura(candidaturaId: String) {
        viewModelScope.launch {
            _estadoCandidatura.value = ObtencionDatos.Cargando
            candidaturaRepository.obtenerCandidaturaPorId(candidaturaId) { state ->
                _estadoCandidatura.value = state
            }
        }
    }
}