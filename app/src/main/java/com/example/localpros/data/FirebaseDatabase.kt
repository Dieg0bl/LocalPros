package com.example.localpros.data

import com.google.firebase.database.FirebaseDatabase

object Firebase {
    private const val DATABASE_URL = "https://console.firebase.google.com/u/0/project/localpros-91ffa/database/localpros-91ffa-default-rtdb/data/~2F?hl=es"

    val instance: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance(DATABASE_URL)
    }
}
