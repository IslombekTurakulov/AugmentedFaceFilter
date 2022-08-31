package com.iuturakulov.augmentedfacefilter.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ModelsInfo(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "path") val path: String
)