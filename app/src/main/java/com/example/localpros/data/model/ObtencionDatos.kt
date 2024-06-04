package com.example.localpros.data.model

sealed class ObtencionDatos<out T> {
    object Cargando : ObtencionDatos<Nothing>()
    data class Exitosa<T>(val datos: T) : ObtencionDatos<T>()
    data class Error(val excepcion: Throwable) : ObtencionDatos<Nothing>()
}
