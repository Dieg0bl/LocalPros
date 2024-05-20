package com.example.localpros.di

import com.example.localpros.data.repository.UserRepository
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserRepositoryModule {
    @Provides
    @Singleton
    fun provideUserRepository(firebaseDatabase: FirebaseDatabase): UserRepository {
        return UserRepository(firebaseDatabase)
    }
}
