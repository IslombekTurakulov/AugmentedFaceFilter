package com.iuturakulov.augmentedfacefilter.network

import com.iuturakulov.augmentedfacefilter.models.ModelsInfo
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET

interface ModelsService {

    @GET("models.json")
    suspend fun fetchModelsInfo(): ApiResponse<List<ModelsInfo>>
}