package com.example.localpros.data.model

data class IndicadoresDesempeno(
    val cumplimientoPrazos: Double = 0.0,
    val ajustePresupuesto: Double = 0.0,
    val calidadMateriales: Double = 0.0,
    val calidadProcedimientos: Double = 0.0,
    val cumplimientoSeguridad: Double = 0.0,
    val fiabilidadCompromiso: Double = 0.0,
    val transparenciaEticaComunicacion: Double = 0.0
)