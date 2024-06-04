package com.example.localpros.data.model

import android.content.SharedPreferences
import com.example.localpros.data.model.enums.Rol
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenciasYAjustesUsuario @Inject constructor(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val PREFS_USER_ID = "prefs_user_id"
        private const val PREFS_ROLES = "prefs_roles"
        private const val PREFS_CIF = "prefs_cif"
        private const val PREFS_NOTIF_NUEVAS_OFERTAS = "prefs_notif_nuevas_ofertas"
        private const val PREFS_NOTIF_CANDIDATURAS = "prefs_notif_candidaturas"
        private const val PREFS_NOTIF_MENSAJES = "prefs_notif_mensajes"
        private const val PREFS_VISIBILIDAD_PERFIL = "prefs_visibilidad_perfil"
        private const val PREFS_COMPARTIR_CONTACTO = "prefs_compartir_contacto"
        private const val PREFS_UBICACION_LAT = "prefs_ubicacion_lat"
        private const val PREFS_UBICACION_LNG = "prefs_ubicacion_lng"
        private const val PREFS_UBICACION_RADIO = "prefs_ubicacion_radio"
        private const val PREFS_ESTADO_CUENTA = "prefs_estado_cuenta"
        private const val PREFS_INITIAL_ROL = "prefs_initial_rol"
    }

    var estadoCuenta: Boolean
        get() = sharedPreferences.getBoolean(PREFS_ESTADO_CUENTA, false)
        set(value) {
            sharedPreferences.edit().putBoolean(PREFS_ESTADO_CUENTA, value).apply()
        }

    var idUsuario: String?
        get() = sharedPreferences.getString(PREFS_USER_ID, null)
        set(value) {
            sharedPreferences.edit().putString(PREFS_USER_ID, value).apply()
        }
    fun getInitialRol(): Rol {
        val rolName = sharedPreferences.getString(PREFS_INITIAL_ROL, Rol.PARTICULAR.name)
        return Rol.valueOf(rolName!!)
    }

    var roles: Set<Rol>
        get() = sharedPreferences.getStringSet(PREFS_ROLES, emptySet())!!.map { Rol.valueOf(it) }.toSet()
        set(value) {
            sharedPreferences.edit().putStringSet(PREFS_ROLES, value.map { it.name }.toSet()).apply()
        }

    var cif: String?
        get() = sharedPreferences.getString(PREFS_CIF, null)
        set(value) {
            sharedPreferences.edit().putString(PREFS_CIF, value).apply()
        }

    var notificacionesNuevasOfertas: Boolean
        get() = sharedPreferences.getBoolean(PREFS_NOTIF_NUEVAS_OFERTAS, false)
        set(value) {
            sharedPreferences.edit().putBoolean(PREFS_NOTIF_NUEVAS_OFERTAS, value).apply()
        }

    var notificacionesCandidaturas: Boolean
        get() = sharedPreferences.getBoolean(PREFS_NOTIF_CANDIDATURAS, false)
        set(value) {
            sharedPreferences.edit().putBoolean(PREFS_NOTIF_CANDIDATURAS, value).apply()
        }

    var notificacionesMensajes: Boolean
        get() = sharedPreferences.getBoolean(PREFS_NOTIF_MENSAJES, false)
        set(value) {
            sharedPreferences.edit().putBoolean(PREFS_NOTIF_MENSAJES, value).apply()
        }

    var visibilidadPerfil: Boolean
        get() = sharedPreferences.getBoolean(PREFS_VISIBILIDAD_PERFIL, true)
        set(value) {
            sharedPreferences.edit().putBoolean(PREFS_VISIBILIDAD_PERFIL, value).apply()
        }

    var compartirInformacionContacto: Boolean
        get() = sharedPreferences.getBoolean(PREFS_COMPARTIR_CONTACTO, true)
        set(value) {
            sharedPreferences.edit().putBoolean(PREFS_COMPARTIR_CONTACTO, value).apply()
        }

    var ubicacion: PosicionYRadio
        get() {
            val lat = sharedPreferences.getFloat(PREFS_UBICACION_LAT, 0.0f).toDouble()
            val lng = sharedPreferences.getFloat(PREFS_UBICACION_LNG, 0.0f).toDouble()
            val radio = sharedPreferences.getFloat(PREFS_UBICACION_RADIO, 10.0f).toDouble()
            return PosicionYRadio(lat, lng, radio)
        }
        set(value) {
            sharedPreferences.edit().putFloat(PREFS_UBICACION_LAT, value.latitud.toFloat())
                .putFloat(PREFS_UBICACION_LNG, value.longitud.toFloat())
                .putFloat(PREFS_UBICACION_RADIO, value.radioKm.toFloat()).apply()
        }
}
