package com.example.localpros.data.model

import android.content.SharedPreferences

class UserPreferences(private val sharedPreferences: SharedPreferences) {
    var userId: String
        get() = sharedPreferences.getString("userId", "") ?: ""
        set(value) = sharedPreferences.edit().putString("userId", value).apply()

    var userRole: UserRole
        get() {
            val roleString = sharedPreferences.getString("userRole", UserRole.Particular.name)
            return UserRole.valueOf(roleString ?: UserRole.Particular.name)
        }
        set(value) = sharedPreferences.edit().putString("userRole", value.name).apply()

    var notificationsNewOffers: Boolean
        get() = sharedPreferences.getBoolean("notificationsNewOffers", true)
        set(value) = sharedPreferences.edit().putBoolean("notificationsNewOffers", value).apply()

    var notificationsCandidatures: Boolean
        get() = sharedPreferences.getBoolean("notificationsCandidatures", true)
        set(value) = sharedPreferences.edit().putBoolean("notificationsCandidatures", value).apply()

    var notificationsMessages: Boolean
        get() = sharedPreferences.getBoolean("notificationsMessages", true)
        set(value) = sharedPreferences.edit().putBoolean("notificationsMessages", value).apply()

    var profileVisibility: Boolean
        get() = sharedPreferences.getBoolean("profileVisibility", true)
        set(value) = sharedPreferences.edit().putBoolean("profileVisibility", value).apply()

    var accountStatus: Boolean
        get() = sharedPreferences.getBoolean("accountStatus", true)
        set(value) = sharedPreferences.edit().putBoolean("accountStatus", value).apply()

    var language: String
        get() = sharedPreferences.getString("language", "es") ?: "es"
        set(value) = sharedPreferences.edit().putString("language", value).apply()

    var serviceCategories: Set<String>
        get() = sharedPreferences.getStringSet("serviceCategories", setOf()) ?: setOf()
        set(value) = sharedPreferences.edit().putStringSet("serviceCategories", value).apply()

    var workRadius: Int
        get() = sharedPreferences.getInt("workRadius", 50)
        set(value) = sharedPreferences.edit().putInt("workRadius", value).apply()

    var availability: String
        get() = sharedPreferences.getString("availability", "Full-time") ?: "Full-time"
        set(value) = sharedPreferences.edit().putString("availability", value).apply()
}
