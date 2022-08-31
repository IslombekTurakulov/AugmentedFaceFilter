package com.iuturakulov.augmentedfacefilter.di

import com.squareup.moshi.Moshi
import dagger.Provides
import javax.inject.Singleton

object PersistenceModule {
    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }
}