package com.example.localpros.di

import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase {
        val databaseUrl = "https://localpros-91ffa-default-rtdb.europe-west1.firebasedatabase.app"
        return FirebaseDatabase.getInstance(databaseUrl)
    }
}
