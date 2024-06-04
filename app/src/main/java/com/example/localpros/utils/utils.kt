package com.example.localpros.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi
import com.google.maps.model.GeocodingResult
import com.example.localpros.data.model.IndicadoresDesempeno
import com.example.localpros.data.model.DisponibilidadGeografica
import com.example.localpros.data.model.DisponibilidadTemporal
import com.example.localpros.data.model.PosicionYRadio
import com.example.localpros.data.model.enums.Disponibilidad
import org.threeten.bp.LocalDateTime
import java.util.Locale
import kotlin.math.*

fun obtenerDireccionDesdeLatLng(lat: Double, lng: Double, geoApiContext: GeoApiContext): String {
    return try {
        val resultados = GeocodingApi.newRequest(geoApiContext)
            .latlng(com.google.maps.model.LatLng(lat, lng))
            .await()
        if (resultados.isNotEmpty()) {
            resultados[0].formattedAddress
        } else {
            "Dirección desconocida"
        }
    } catch (e: Exception) {
        e.printStackTrace()
        "Dirección desconocida"
    }
}

fun buscarUbicacion(query: String, context: GeoApiContext): LatLng? {
    return try {
        val resultados: Array<GeocodingResult> = GeocodingApi.geocode(context, query).await()
        if (resultados.isNotEmpty()) {
            val resultado = resultados[0].geometry.location
            LatLng(resultado.lat, resultado.lng)
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun buscarSugerencias(query: String, context: GeoApiContext): List<String> {
    return try {
        val resultados: Array<GeocodingResult> = GeocodingApi.geocode(context, query).await()
        resultados.map { it.formattedAddress }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}

fun obtenerDireccion(context: Context, latLng: LatLng): String {
    val geocoder = Geocoder(context, Locale.getDefault())
    val direcciones: List<Address>? = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
    return if (!direcciones.isNullOrEmpty()) {
        direcciones[0].getAddressLine(0)
    } else {
        "Dirección desconocida"
    }
}

fun validarContrasena(password: String): Boolean {
    val passwordRegex = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%^&+=.]).{6,}$".toRegex()
    return passwordRegex.matches(password)
}

fun validarCIF(cif: String): Boolean {
    if (cif.length != 9) return false
    val letras = "ABCDEFGHJKLMNPQRSUVW"
    val numero = cif.substring(1, 8)
    val letra = cif[0].uppercaseChar()
    val digito = cif[8]

    if (!letras.contains(letra)) return false

    val sumaPares = numero.filterIndexed { index, _ -> index % 2 == 1 }.sumOf { it.toString().toInt() }
    val sumaImpares = numero.filterIndexed { index, _ -> index % 2 == 0 }.sumOf { it.toString().toInt() * 2 % 10 + it.toString().toInt() * 2 / 10 }
    val sumaTotal = sumaPares + sumaImpares
    val digitoControl = (10 - (sumaTotal % 10)) % 10

    return digito == digitoControl.toString()[0]
}

fun calcularDistancia(pos1: PosicionYRadio, pos2: PosicionYRadio): Double {
    val earthRadius = 6371.0 // Radio de la tierra en km

    val dLat = Math.toRadians(pos2.latitud - pos1.latitud)
    val dLng = Math.toRadians(pos2.latitud - pos1.latitud)

    val a = sin(dLat / 2).pow(2.0) + cos(Math.toRadians(pos1.latitud)) * cos(Math.toRadians(pos2.latitud)) * sin(dLng / 2).pow(2.0)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return earthRadius * c
}

fun ComprobacionProfesionalCumpleRequisitosMinimosParaVisualizarOferta(
    idProfesional: String,
    idPublicador: String,
    indicadoresProfesional: IndicadoresDesempeno,
    indicadoresOferta: IndicadoresDesempeno,
    ubicacionOferta: PosicionYRadio,
    disponibilidadGeografica: DisponibilidadGeografica,
    disponibilidadTemporal: List<DisponibilidadTemporal>,
    rangoFechasOferta: Pair<LocalDateTime, LocalDateTime>
): Boolean {
    // Validar que el profesional no sea el publicador de la oferta
    if (idProfesional == idPublicador) return false

    // Validar indicadores de desempeño
    val cumpleIndicadores = indicadoresProfesional >= indicadoresOferta

    // Validar si la oferta está dentro del radio de trabajo del profesional
    val dentroDelRadio = calcularDistancia(ubicacionOferta, disponibilidadGeografica.ubicacion) <= disponibilidadGeografica.ubicacion.radioKm ||
            calcularDistancia(ubicacionOferta, disponibilidadGeografica.oficina) <= disponibilidadGeografica.oficina.radioKm

    // Validar disponibilidad temporal
    val disponible = disponibilidadTemporal.any { disponibilidad ->
        disponibilidad.estado == Disponibilidad.DISPONIBLE &&
                !disponibilidad.inicio.isAfter(rangoFechasOferta.second) &&
                !disponibilidad.fin.isBefore(rangoFechasOferta.first)
    }

    return cumpleIndicadores && dentroDelRadio && disponible
}
