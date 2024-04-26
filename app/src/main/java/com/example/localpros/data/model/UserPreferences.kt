package com.example.localpros.data.model

import android.content.SharedPreferences
import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(private val sharedPreferences: SharedPreferences) {
    var userId: String
        get() = sharedPreferences.getString("UserId", "") ?: ""
        set(value) {
            Log.d("UserPreferences", "Setting user ID to $value")
            sharedPreferences.edit().putString("UserId", value).apply()
        }

    var userRole: String
        get() = sharedPreferences.getString("UserRole", UserRole.Particular.name) ?: UserRole.Particular.name
        set(value) {
            Log.d("UserPreferences", "Setting user role to $value")
            sharedPreferences.edit().putString("UserRole", value).apply()
        }
}
