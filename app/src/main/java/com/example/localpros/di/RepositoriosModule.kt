package com.example.localpros.di

import com.example.localpros.data.repository.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoriosModule {

    @Provides
    @Singleton
    fun provideRepositorioUsuarios(
        firebaseDatabase: FirebaseDatabase,
        firebaseAuth: FirebaseAuth
    ): RepositorioUsuarios = RepositorioUsuarios(firebaseDatabase.reference, firebaseAuth)

    @Provides
    @Singleton
    fun provideRepositorioProfesional(
        firebaseDatabase: FirebaseDatabase
    ): RepositorioProfesional = RepositorioProfesional(firebaseDatabase)

    @Provides
    @Singleton
    fun provideRepositorioCategorias(
        firebaseDatabase: FirebaseDatabase
    ): RepositorioCategorias = RepositorioCategorias(firebaseDatabase)

    @Provides
    @Singleton
    fun provideRepositorioAutenticacion(firebaseAuth: FirebaseAuth): RepositorioAutenticacion =
        RepositorioAutenticacion(firebaseAuth)

    @Provides
    @Singleton
    fun provideRepositorioCalendario(firebaseDatabase: FirebaseDatabase): RepositorioCalendario =
        RepositorioCalendario(firebaseDatabase)

    @Provides
    @Singleton
    fun provideRepositorioCandidaturas(firebaseDatabase: FirebaseDatabase): RepositorioCandidaturas =
        RepositorioCandidaturas(firebaseDatabase)

    @Provides
    @Singleton
    fun provideRepositorioEvaluaciones(firebaseDatabase: FirebaseDatabase): RepositorioEvaluaciones =
        RepositorioEvaluaciones(firebaseDatabase)

    @Provides
    @Singleton
    fun provideRepositorioNotificaciones(firebaseDatabase: FirebaseDatabase): RepositorioNotificaciones =
        RepositorioNotificaciones(firebaseDatabase)

    @Provides
    @Singleton
    fun provideRepositorioOfertas(firebaseDatabase: FirebaseDatabase): RepositorioOfertas =
        RepositorioOfertas(firebaseDatabase)

    @Provides
    @Singleton
    fun provideRepositorioParticularImpl(firebaseDatabase: FirebaseDatabase): RepositorioParticular =
        RepositorioParticular(firebaseDatabase)

    @Provides
    @Singleton
    fun provideRepositorioSesion(firebaseAuth: FirebaseAuth): RepositorioSesion =
        RepositorioSesion(firebaseAuth)

    @Provides
    @Singleton
    fun provideRepositorioUbicacion(firebaseDatabase: FirebaseDatabase): RepositorioUbicacion =
        RepositorioUbicacion(firebaseDatabase)
}
