package com.example.localpros.data.model.usuarioModel

import com.example.localpros.data.model.IndicadoresDesempeno
import com.example.localpros.data.model.PosicionYRadio
import com.example.localpros.data.model.enums.Rol

data class Usuario(
    val id: String = "",
    val nombre: String,
    val datosContacto: DatosContacto,
    val fotoPerfil: FotoPerfil? = null,
    val ubicacion: PosicionYRadio? = null,
    var roles: Set<Rol> = emptySet(),
    var rolEnUso: Rol? = null,
    val esParticular: Boolean = false,
    val esProfesional: Boolean = false,
    val indicadoresDesempeno: IndicadoresDesempeno? = if (esProfesional) IndicadoresDesempeno() else null,
    val cif: String? = if (esProfesional) "" else null
)
