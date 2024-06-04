package com.example.localpros.data.repository

import com.example.localpros.data.model.*
import com.example.localpros.data.model.enums.Disponibilidad
import com.example.localpros.data.model.enums.Rol
import com.example.localpros.data.model.usuarioModel.Usuario
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import org.threeten.bp.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@Singleton
class RepositorioProfesional @Inject constructor(
    private val db: FirebaseDatabase
) {
    private val refOfertas: DatabaseReference = db.getReference("Ofertas")
    private val refUsuarios: DatabaseReference = db.getReference("Usuarios")
    private val refDisponibilidad: DatabaseReference = db.getReference("Disponibilidad")
    private val refCandidaturas: DatabaseReference = db.getReference("Candidaturas")

    suspend fun agregarProfesional(profesional: Usuario) {
        refUsuarios.child(profesional.id).setValue(profesional).await()
    }

    suspend fun obtenerProfesionales(): List<Usuario> {
        val snapshot = refUsuarios.get().await()
        return snapshot.children.mapNotNull { it.getValue(Usuario::class.java) }
            .filter { it.roles.contains(Rol.PROFESIONAL) }
    }

    suspend fun buscarOfertas(filtro: Map<String, Any>): List<Oferta> {
        val snapshot = refOfertas.orderByChild(filtro.keys.first()).equalTo(filtro.values.first() as String).get().await()
        return snapshot.children.mapNotNull { it.getValue(Oferta::class.java) }
    }

    suspend fun filtrarOfertas(tipoTrabajo: String, ubicacion: String, presupuesto: Double): List<Oferta> {
        val snapshot = refOfertas.orderByChild("tipoTrabajo").equalTo(tipoTrabajo).get().await()
        return snapshot.children.filter {
            it.child("ubicacion").getValue(PosicionYRadio::class.java)?.let { pos ->
                pos.radioKm >= ubicacion.toDouble() // Comparar con el radio de la ubicación
            } ?: false &&
                    it.child("presupuestoDisponible").getValue(Double::class.java) == presupuesto
        }.mapNotNull { it.getValue(Oferta::class.java) }
    }

    suspend fun verificarDisponibilidad(idProfesional: String, fecha: String): Boolean {
        val snapshot = db.getReference("Usuarios").child(idProfesional).child("disponibilidad").get().await()
        val disponibilidad = snapshot.getValue(String::class.java)
        return disponibilidad == fecha
    }

    suspend fun presentarCandidatura(ofertaId: String, profesionalId: String, detallesCandidatura: String, propuestaEconomica: Double): Boolean {
        val oferta = refOfertas.child(ofertaId).get().await().getValue(Oferta::class.java)
        oferta?.let {
            val candidatura = Candidatura(
                id = "$ofertaId-$profesionalId",
                idEditor = profesionalId,
                idOfertaPretendida = ofertaId,
                propuestaEconomica = propuestaEconomica,
                cartaPresentacion = detallesCandidatura
            )
            refCandidaturas.child(candidatura.id).setValue(candidatura).await()
            refOfertas.child(ofertaId).child("listaCandidaturas").child(candidatura.id).setValue(candidatura).await()
            return true
        }
        return false
    }

    suspend fun eliminarCandidatura(profesionalId: String, candidaturaId: String) {
        val candidaturaRef = refCandidaturas.child(candidaturaId)
        val snapshot = candidaturaRef.child("idEditor").get().await()
        if (snapshot.getValue(String::class.java) == profesionalId) {
            candidaturaRef.removeValue().await()
            val ofertaId = candidaturaId.substringBeforeLast("-")
            refOfertas.child(ofertaId).child("listaCandidaturas").child(candidaturaId).removeValue().await()
        } else {
            throw IllegalAccessException("No tiene permiso para eliminar esta candidatura.")
        }
    }

    suspend fun recibirNotificaciones(idProfesional: String): List<Notificacion> {
        val snapshot = db.getReference("Notificaciones").orderByChild("userId").equalTo(idProfesional).get().await()
        return snapshot.children.mapNotNull { it.getValue(Notificacion::class.java) }
    }

    suspend fun obtenerOfertasDisponibles(profesional: Usuario): List<Oferta> {
        val snapshot = refOfertas.get().await()
        return snapshot.children.mapNotNull { it.getValue(Oferta::class.java) }
            .filter { oferta ->
                cumpleRequisitos(profesional, oferta)
            }
    }

    private suspend fun cumpleRequisitos(profesional: Usuario, oferta: Oferta): Boolean {
        val cumpleDesempeno = profesional.indicadoresDesempeno!! >= oferta.indicadoresDesempeno
        val dentroDelRadio = profesional.ubicacion?.let { oferta.ubicacion.estaDentroDelRadio(it) }
        val disponible = profesional.estaDisponible(oferta.inicioEjecucion, oferta.finalizacionEjecucion)
        val noEsMismoUsuario = oferta.idPublicador != profesional.id

        return cumpleDesempeno && dentroDelRadio == true && disponible && noEsMismoUsuario
    }

    private suspend fun Usuario.estaDisponible(inicio: LocalDateTime, fin: LocalDateTime): Boolean {
        val snapshot = refDisponibilidad.child(this.id).get().await()
        val disponibilidades = snapshot.children.mapNotNull { it.getValue(DisponibilidadTemporal::class.java) }
        return disponibilidades.any { it.inicio <= inicio && it.fin >= fin && it.estado == Disponibilidad.DISPONIBLE }
    }

    private fun PosicionYRadio.estaDentroDelRadio(otraPosicion: PosicionYRadio): Boolean {
        val earthRadius = 6371.0 // radio de la Tierra en km

        val dLat = Math.toRadians(otraPosicion.latitud - this.latitud)
        val dLng = Math.toRadians(otraPosicion.longitud - this.longitud)

        val a = sin(dLat / 2).pow(2.0) + cos(Math.toRadians(this.latitud)) * cos(Math.toRadians(otraPosicion.latitud)) * sin(dLng / 2).pow(2.0)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        val distance = earthRadius * c
        return distance <= this.radioKm
    }

    suspend fun obtenerCandidaturas(profesionalId: String): List<Candidatura> {
        val snapshot = db.getReference("Candidaturas").orderByChild("profesionalId").equalTo(profesionalId).get().await()
        return snapshot.children.mapNotNull { it.getValue(Candidatura::class.java) }
    }

}
