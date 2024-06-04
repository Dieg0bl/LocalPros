package com.example.localpros.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localpros.data.model.enums.Categoria
import com.example.localpros.data.repository.RepositorioCategorias
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CategoríaViewModel @Inject constructor(
    private val repositorioCategorias: RepositorioCategorias
) : ViewModel() {

    private val _categorias = MutableStateFlow<List<Categoria>>(emptyList())
    val categorias: StateFlow<List<Categoria>> get() = _categorias

    init {
        cargarCategorias()
    }

    private fun cargarCategorias() {
        viewModelScope.launch {
            _categorias.value = repositorioCategorias.obtenerCategorias()
        }
    }

    fun agregarCategoria(categoria: Categoria) {
        viewModelScope.launch {
            repositorioCategorias.agregarCategoria(categoria)
            cargarCategorias()
        }
    }
}
