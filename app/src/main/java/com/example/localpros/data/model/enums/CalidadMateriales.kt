package com.example.localpros.data.model.enums

enum class CalidadMateriales(val descripcion: String) {
    BAJA("Baja calidad"),
    MEDIA("Calidad media"),
    ALTA("Alta calidad");

    override fun toString(): String {
        return descripcion
    }

    companion object {
        fun buscarDescripcion(descripcion: String): CalidadMateriales? {
            return values().find { it.descripcion == descripcion }
        }

        fun listarTodas(): List<CalidadMateriales> {
            return values().toList()
        }
    }
}
