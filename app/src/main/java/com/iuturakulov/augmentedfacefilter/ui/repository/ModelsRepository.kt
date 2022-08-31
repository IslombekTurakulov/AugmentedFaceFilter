package com.iuturakulov.augmentedfacefilter.ui.repository

import androidx.annotation.WorkerThread
import com.iuturakulov.augmentedfacefilter.models.ModelsInfo
import com.iuturakulov.augmentedfacefilter.network.ModelsClient
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class ModelsRepository @Inject constructor(
    private val modelsClient: ModelsClient,
) {

    @WorkerThread
    suspend fun getArchitectureList(): ApiResponse<List<ModelsInfo>> {
        return modelsClient.fetchModelsInfo()
    }

}