package com.example.mymanga

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@HiltAndroidApp
class MyApp : Application()

@Module
@InstallIn(SingletonComponent::class)
object TestModule {

    @Provides
    @Singleton
    fun provideTestString(): String = "Hilt está funcionando correctamente"
}