package com.example.localpros.data.model

data class Oferta(
    val id: String = "",
    val publicador: Particular,
    val descripcion: String = "",
    val ubicacion: String = "",
    val presupuestoDisponible: Double = 0.0,
    val calidadMateriales: String = "",
    val calidadProcedimientos: String = "",
    val descripcionLibre: String = "",
    val estado: EstadoOferta = EstadoOferta.BORRADOR,
    val candidaturas: List<Candidatura> = listOf()
)