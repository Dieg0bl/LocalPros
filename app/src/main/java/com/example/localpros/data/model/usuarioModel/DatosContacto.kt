package com.example.localpros.data.model.usuarioModel

import com.example.localpros.data.model.PosicionYRadio

data class DatosContacto(
    val nombreCompleto: String,
    val correoElectronico: String,
    val numeroTelefono: String,
    val direccionFisica: PosicionYRadio? = null
)