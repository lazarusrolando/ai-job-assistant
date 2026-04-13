package io.lazar.jobassistant.data.api

import io.lazar.jobassistant.data.model.OllamaRequest
import io.lazar.jobassistant.data.model.OllamaTagsResponse
import io.lazar.jobassistant.data.model.OllamaResponse
import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.POST

interface OllamaApi {

    @POST("api/generate")
    suspend fun generate(@Body request: OllamaRequest): OllamaResponse

    @GET("api/tags")
    suspend fun getModels(): OllamaTagsResponse
}
