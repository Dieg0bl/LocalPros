package com.example.localpros.data.model.enums

enum class CalidadProcedimientos(val descripcion: String) {
    OBSOLETO("Procedimientos obsoletos"),
    ESTANDAR("Procedimientos estándar"),
    AVANZADO("Procedimientos avanzados");

    override fun toString(): String {
        return descripcion
    }

    companion object {
        fun buscarPorDescripcion(descripcion: String): CalidadProcedimientos? {
            return values().find { it.descripcion == descripcion }
        }

        fun listarTodas(): List<CalidadProcedimientos> {
            return values().toList()
        }
    }
}