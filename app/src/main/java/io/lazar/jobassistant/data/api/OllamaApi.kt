package io.lazar.jobassistant.data.api

import io.lazar.jobassistant.data.model.OllamaRequest
import io.lazar.jobassistant.data.model.OllamaResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface OllamaApi {

    @POST("api/generate")
    suspend fun generate(@Body request: OllamaRequest): OllamaResponse

    @POST("api/tags")
    suspend fun getModels(): Map<String, List<Map<String, Any>>>
}
