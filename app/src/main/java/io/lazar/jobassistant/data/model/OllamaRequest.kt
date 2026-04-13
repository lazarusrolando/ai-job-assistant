package io.lazar.jobassistant.data.model

import kotlinx.serialization.Serializable

@Serializable
data class OllamaRequest(
    val model: String,
    val prompt: String,
    val stream: Boolean = false,
    val options: OllamaOptions? = null
)

@Serializable
data class OllamaOptions(
    val temperature: Float? = null,
    val top_p: Float? = null,
    val num_predict: Int? = null
)

@Serializable
data class OllamaResponse(
    val model: String,
    val response: String,
    val done: Boolean
)

@Serializable
data class OllamaTagsResponse(
    val models: List<OllamaModel> = emptyList()
)

@Serializable
data class OllamaModel(
    val name: String
)
