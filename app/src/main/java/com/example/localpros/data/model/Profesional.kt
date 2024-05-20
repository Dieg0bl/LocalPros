package com.example.localpros.data.model

data class Profesional(
    override val usuarioId: String = "",
    override val nombre: String = "",
    override val contacto: String = "",
    override val fotoPerfil: String = "",
    override val rol: String = "Profesional",
    val cif: String = "",
    val disponibilidadTemporal: Map<String, Disponibilidad> = mapOf(),
    val historialCandidaturas: Map<String, Boolean> = mapOf(),
    val indicadoresDesempeno: IndicadoresDesempeno = IndicadoresDesempeno()
) : Usuario(usuarioId, nombre, contacto, fotoPerfil, rol)