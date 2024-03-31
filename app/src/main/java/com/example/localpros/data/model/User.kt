package com.example.localpros.data.model

sealed class User(
    open val id: String,
    open val nombre: String,
    open val fotoPerfil: String,
    fotoPerfil1: String
)
