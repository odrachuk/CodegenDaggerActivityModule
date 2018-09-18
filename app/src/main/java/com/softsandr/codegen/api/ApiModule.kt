package com.softsandr.codegen.api

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun provideSuperApi(appContext: Context) = ApiController(appContext)
}