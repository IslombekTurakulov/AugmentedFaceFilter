package com.iuturakulov.augmentedfacefilter.network

import com.iuturakulov.augmentedfacefilter.models.ModelsInfo
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class ModelsClient @Inject constructor(
    private val modelsService: ModelsService
) {

    suspend fun fetchModelsInfo(): ApiResponse<List<ModelsInfo>> {
        return modelsService.fetchModelsInfo()
    }
}