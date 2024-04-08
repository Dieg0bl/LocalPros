package com.example.localpros.data.model

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(private val sharedPreferences: SharedPreferences) {

    val userId: String
        get() = sharedPreferences.getString("UserId", "") ?: ""

    var userRole: String
        get() = sharedPreferences.getString("UserRole", UserRole.Particular.name) ?: UserRole.Particular.name
        set(value) = sharedPreferences.edit().putString("UserRole", value).apply()
}
