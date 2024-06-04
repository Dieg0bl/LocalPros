package com.example.localpros.ui.viewModel

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localpros.data.model.Oferta
import com.example.localpros.data.model.ObtencionDatos
import com.example.localpros.data.repository.RepositorioOfertas
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfertaViewModel @Inject constructor(
    private val repositorioOfertas: RepositorioOfertas
) : ViewModel() {

    private val _estadoOferta = MutableStateFlow<ObtencionDatos<Oferta>>(ObtencionDatos.Cargando)
    val estadoOferta: StateFlow<ObtencionDatos<Oferta>> get() = _estadoOferta

    fun crearNuevaOfertaReferencia(): DatabaseReference {
        return repositorioOfertas.crearNuevaOfertaReferencia()
    }


    fun cargarDatosOferta(ofertaId: String) {
        viewModelScope.launch {
            _estadoOferta.value = ObtencionDatos.Cargando
            repositorioOfertas.obtenerOfertaPorId(ofertaId) { state ->
                _estadoOferta.value = state
            }
        }
    }

    fun crearOferta(oferta: Oferta, nuevaOfertaRef: DatabaseReference, param: @Composable (Any) -> Unit) {
        val nuevaOfertaRef = crearNuevaOfertaReferencia()
        crearOferta(oferta, nuevaOfertaRef) { estado ->
            when (estado) {
                is ObtencionDatos.Exitosa<*> -> {
                    _estadoOferta.value = ObtencionDatos.Exitosa(oferta)
                }
                is ObtencionDatos.Error -> {
                    _estadoOferta.value = estado
                }
                else -> {
                    // No hacer nada
                }
            }
        }
    }
}
