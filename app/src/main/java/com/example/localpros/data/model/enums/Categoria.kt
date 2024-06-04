package com.example.localpros.data.model.enums

enum class Categoria(val id: String, val nombre: String) {
    ELECTRICISTA("1", "Electricista"),
    FONTANERO("2", "Fontanero"),
    ALBANIL("3", "Albañil"),
    CARPINTERO("4", "Carpintero"),
    PINTOR("5", "Pintor"),
    JARDINERO("6", "Jardinero"),
    MECANICO("7", "Mecánico"),
    PALISTA("8", "Palista"),
    SOLDADOR("9", "Soldador"),
    TECNICO_CLIMATIZACION("10", "Técnico en Climatización"),
    CERRAJERO("11", "Cerrajero"),
    INSTALADOR_GAS("12", "Instalador de Gas"),
    TECNICO_REFRIGERACION("13", "Técnico en Refrigeración"),
    TECNICO_ELECTRODOMESTICOS("14", "Técnico de Electrodomésticos"),
    LIMPIEZA_MANTENIMIENTO("15", "Limpieza y Mantenimiento"),
    TECNICO_INFORMATICA("16", "Técnico de Informática"),
    TECNICO_REDES_TELECOMUNICACIONES("17", "Técnico de Redes y Telecomunicaciones"),
    TECNICO_SEGURIDAD("18", "Técnico de Seguridad"),
    TECNICO_ENERGIAS_RENOVABLES("19", "Técnico de Energías Renovables"),
    REFORMAS_INTEGRALES("20", "Reformas Integrales"),
    OTROS("21","Otros oficios");

    companion object {
        fun buscarPorId(id: String): Categoria? {
            return values().find { it.id == id }
        }
    }

    override fun toString(): String {
        return nombre
    }
}
