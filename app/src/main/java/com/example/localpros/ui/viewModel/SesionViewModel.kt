package com.example.localpros.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localpros.data.repository.RepositorioSesion
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SesionViewModel @Inject constructor(
    private val repositorioSesiones: RepositorioSesion
) : ViewModel() {

    private val _sesion = MutableLiveData<FirebaseUser?>()
    val sesion: LiveData<FirebaseUser?> get() = _sesion

    fun iniciarSesion(correo: String, contrasena: String) {
        viewModelScope.launch {
            repositorioSesiones.iniciarSesion(correo, contrasena).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _sesion.postValue(task.result?.user)
                } else {
                    _sesion.postValue(null)
                }
            }
        }
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            repositorioSesiones.cerrarSesion().addOnCompleteListener {
                _sesion.postValue(null)
            }
        }
    }

    fun obtenerUsuarioActual(): FirebaseUser? {
        return repositorioSesiones.obtenerUsuarioActual()
    }
}