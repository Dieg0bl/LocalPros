package com.example.localpros.data.model

data class Profesional(
    override val id: String,
    override val nombre: String,
    override val contacto: String,
    override val fotoPerfil: String,
    val historialCandidaturas: List<Candidatura>,
    val reseñasRecibidas: List<Resena>,
    val indicadoresDesempeno: IndicadoresDesempeno
) : User(id, nombre, contacto, fotoPerfil)