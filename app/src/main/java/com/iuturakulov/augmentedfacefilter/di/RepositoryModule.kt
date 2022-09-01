package com.iuturakulov.augmentedfacefilter.di

import com.iuturakulov.augmentedfacefilter.network.ModelsClient
import com.iuturakulov.augmentedfacefilter.ui.repository.ModelsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideDetailRepository(
        modelsClient: ModelsClient
    ): ModelsRepository {
        return ModelsRepository(modelsClient)
    }
}