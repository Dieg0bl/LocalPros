package com.example.localpros.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localpros.data.model.Evaluacion
import com.example.localpros.data.repository.RepositorioEvaluaciones
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EvaluacionViewModel @Inject constructor(
    private val repositorioEvaluaciones: RepositorioEvaluaciones
) : ViewModel() {

    private val _evaluaciones = MutableStateFlow<List<Evaluacion>>(emptyList())
    val evaluaciones: StateFlow<List<Evaluacion>> get() = _evaluaciones

    fun cargarEvaluacionesPorUsuario(idUsuario: String) {
        viewModelScope.launch {
            val result = repositorioEvaluaciones.listarEvaluacionesPorUsuario(idUsuario)
            _evaluaciones.value = result
        }
    }

    fun cargarEvaluacionesPorOferta(idOferta: String) {
        viewModelScope.launch {
            val result = repositorioEvaluaciones.listarEvaluacionesPorOferta(idOferta)
            _evaluaciones.value = result
        }
    }

    fun crearEvaluacion(evaluacion: Evaluacion) {
        viewModelScope.launch {
            repositorioEvaluaciones.crearEvaluacion(evaluacion)
            cargarEvaluacionesPorUsuario(evaluacion.idEvaluacion)
        }
    }
}