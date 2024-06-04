package com.example.localpros.di

import android.content.Context
import android.content.SharedPreferences
import com.example.localpros.data.model.PreferenciasYAjustesUsuario
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("LocalProsPrefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providePreferenciasYAjustesUsuario(sharedPreferences: SharedPreferences): PreferenciasYAjustesUsuario {
        return PreferenciasYAjustesUsuario(sharedPreferences)
    }
}
