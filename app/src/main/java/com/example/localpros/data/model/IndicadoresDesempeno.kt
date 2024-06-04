package com.example.localpros.data.model

data class IndicadoresDesempeno(
    val cumplimientoPlazos: Double = 0.0,
    val adherenciaPresupuesto: Double = 0.0,
    val calidadMateriales: Double = 0.0,
    val calidadProcedimientos: Double = 0.0,
    val cumplimientoSeguridad: Double = 0.0,
    val compromisoFiabilidad: Double = 0.0,
    val transparenciaEticaComunicacion: Double = 0.0
) : Comparable<IndicadoresDesempeno> {
    override fun compareTo(other: IndicadoresDesempeno): Int {
        return when {
            this.cumplimientoPlazos < other.cumplimientoPlazos -> -1
            this.cumplimientoPlazos > other.cumplimientoPlazos -> 1
            this.adherenciaPresupuesto < other.adherenciaPresupuesto -> -1
            this.adherenciaPresupuesto > other.adherenciaPresupuesto -> 1
            this.calidadMateriales < other.calidadMateriales -> -1
            this.calidadMateriales > other.calidadMateriales -> 1
            this.calidadProcedimientos < other.calidadProcedimientos -> -1
            this.calidadProcedimientos > other.calidadProcedimientos -> 1
            this.cumplimientoSeguridad < other.cumplimientoSeguridad -> -1
            this.cumplimientoSeguridad > other.cumplimientoSeguridad -> 1
            this.compromisoFiabilidad < other.compromisoFiabilidad -> -1
            this.compromisoFiabilidad > other.compromisoFiabilidad -> 1
            this.transparenciaEticaComunicacion < other.transparenciaEticaComunicacion -> -1
            this.transparenciaEticaComunicacion > other.transparenciaEticaComunicacion -> 1
            else -> 0
        }
    }
}
