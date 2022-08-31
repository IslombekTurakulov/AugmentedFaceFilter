package com.iuturakulov.augmentedfacefilter.di

import com.iuturakulov.augmentedfacefilter.utils.Constants
import com.iuturakulov.augmentedfacefilter.network.HttpRequestInterceptor
import com.iuturakulov.augmentedfacefilter.network.ModelsClient
import com.iuturakulov.augmentedfacefilter.network.ModelsService
import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpRequestInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
        fun provideModelsService(retrofit: Retrofit): ModelsService {
        return retrofit.create(ModelsService::class.java)
    }

    @Provides
    @Singleton
    fun provideModelsClient(modelsService: ModelsService): ModelsClient {
        return ModelsClient(modelsService)
    }
}