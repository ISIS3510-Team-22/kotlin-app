package com.example.exchangeapp.model.service.module

import android.content.Context
import android.content.SharedPreferences
import com.example.exchangeapp.DataStorage.SharedPreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    @Provides
    @Singleton
    fun provideSharedPreferencesRepository(
        @ApplicationContext context: Context) : SharedPreferencesManager{
        val sharedPreferences = context.getSharedPreferences("info_layout_order", Context.MODE_PRIVATE)
        return SharedPreferencesManager(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }
}