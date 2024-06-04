package com.example.localpros.data.model

import com.example.localpros.data.model.enums.Categoria
import com.example.localpros.data.model.enums.EstadoOferta
import com.example.localpros.data.model.enums.CalidadMateriales
import com.example.localpros.data.model.enums.CalidadProcedimientos
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

data class Oferta(
    val id: String = "",
    val idPublicador: String = "",
    val titulo: String,
    val descripcion: String = "",
    val ubicacion: PosicionYRadio = PosicionYRadio(0.0, 0.0),
    val presupuestoDisponible: Double = 0.0,
    val calidadMateriales: CalidadMateriales = CalidadMateriales.MEDIA,
    val calidadProcedimientos: CalidadProcedimientos = CalidadProcedimientos.ESTANDAR,
    val descripcionLibre: String = "",
    var estado: EstadoOferta = EstadoOferta.PUBLICADA,
    val categoria: Categoria = Categoria.ELECTRICISTA,
    val tiempoEstimado: String = "",
    val fechaBorrado: LocalDate = LocalDate.now().plusDays(30),
    val inicioEjecucion: LocalDateTime = LocalDateTime.now(),
    val finalizacionEjecucion: LocalDateTime = LocalDateTime.now().plusDays(30),
    val indicadoresDesempeno: IndicadoresDesempeno = IndicadoresDesempeno(),
    val estadoOferta: EstadoOferta = EstadoOferta.PUBLICADA,
    val listaCandidaturas: List<Candidatura> = emptyList(),
    val candidatoSeleccionado: String? = null
)
