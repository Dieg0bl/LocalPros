package com.example.localpros.data.model

data class Oferta(
    val id: String,
    val publicador: Particular,
    val descripcion: String,
    val ubicacion: String,
    val presupuestoDisponible: Double,
    val calidadMateriales: String,
    val calidadProcedimientos: String,
    val descripcionLibre: String,
    val estado: EstadoOferta,
    val candidaturas: List<Candidatura>
)