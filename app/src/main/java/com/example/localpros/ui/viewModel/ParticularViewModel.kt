package com.example.localpros.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localpros.data.model.Oferta
import com.example.localpros.data.repository.RepositorioParticular
import com.google.maps.GeoApiContext
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParticularViewModel @Inject constructor(
    private val repositorioParticular: RepositorioParticular,
) : ViewModel() {

    private val _ofertas = MutableStateFlow<List<Oferta>>(emptyList())
    val ofertas: StateFlow<List<Oferta>> get() = _ofertas

    fun cargarOfertas(userId: String) {
        viewModelScope.launch {
            _ofertas.value = repositorioParticular.obtenerOfertas(userId)
        }
    }

    fun eliminarOferta(userId: String, ofertaId: String) {
        viewModelScope.launch {
            repositorioParticular.eliminarOferta(userId, ofertaId)
            cargarOfertas(userId)
        }
    }
}






