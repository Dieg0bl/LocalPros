package com.example.localpros.data.model

sealed class Usuario(
    open val usuarioId: String = "",
    open val nombre: String = "",
    open val contacto: String = "",
    open val fotoPerfil: String = "",
    open val rol: String = ""
)