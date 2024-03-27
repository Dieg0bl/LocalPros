package com.example.localpros.data.model

data class Profesional(
    val id: String,
    val nombre: String,
    val especializacion: String,
    val fotoPerfil: String,
    val indicadoresDesempeño: IndicadoresDesempeno,
    val historialCandidaturas: List<Candidatura>,
    val zonaTrabajo: String,
    val disponibilidad: String
)