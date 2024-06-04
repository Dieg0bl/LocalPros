package com.example.localpros.data.model

import org.threeten.bp.LocalDate

data class Evaluacion(
    val idEvaluacion: String,
    val fechaFinalizacionReal: LocalDate,
    val fechaFinalizacionAcordada: LocalDate,
    val costoFinal: Double,
    val presupuestoAcordado: Double,
    val calidadMateriales: String,
    val calidadProcedimientos: String,
    val cumplimientoSeguridad: String,
    val fiabilidad: String,
    val transparencia: String
)
