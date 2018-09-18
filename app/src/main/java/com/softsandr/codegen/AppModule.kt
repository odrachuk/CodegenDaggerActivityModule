package com.softsandr.codegen

import android.app.Application
import android.content.Context
import com.softsandr.codegen.ui.UiModule
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Module(includes = [AndroidInjectionModule::class, UiModule::class])
class AppModule {

    @Provides
    @Singleton
    fun provideAppContext(app: Application): Context = app.applicationContext
}