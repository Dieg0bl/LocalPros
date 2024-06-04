package com.example.localpros.data.model

data class DisponibilidadGeografica(
    val ubicacion: PosicionYRadio = PosicionYRadio(0.0, 0.0, 10.0),
    val oficina: PosicionYRadio = PosicionYRadio(0.0, 0.0, 10.0)
)
