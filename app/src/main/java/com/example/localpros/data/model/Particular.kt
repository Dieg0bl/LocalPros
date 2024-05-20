package com.example.localpros.data.model

data class Particular(
    override val usuarioId: String = "",
    override val nombre: String = "",
    override val contacto: String = "",
    override val fotoPerfil: String = "",
    override val rol: String = "com.example.localpros.data.model.Particular",
    val reseñasEmitidas: Map<String, Boolean> = mapOf(),
    val historialOfertas: Map<String, Boolean> = mapOf(),
    val ofertasBorrador: Map<String, Boolean> = mapOf()
) : Usuario(usuarioId, nombre, contacto, fotoPerfil, rol)