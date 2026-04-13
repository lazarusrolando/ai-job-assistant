package io.lazar.jobassistant.data.api

import io.lazar.jobassistant.data.model.OllamaRequest
import io.lazar.jobassistant.data.model.OllamaOptions
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import okhttp3.MediaType.Companion.toMediaType

class OllamaClient(
    private val baseUrl: String = "http://10.0.2.2:11434/"
) {
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val api = retrofit.create(OllamaApi::class.java)

    suspend fun generate(
        model: String,
        prompt: String,
        temperature: Float = 0.7f,
        maxTokens: Int = 2048
    ): Result<String> {
        return try {
            val request = OllamaRequest(
                model = model,
                prompt = prompt,
                stream = false,
                options = OllamaOptions(
                    temperature = temperature,
                    num_predict = maxTokens
                )
            )
            val response = api.generate(request)
            Result.success(response.response.trim())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getModels(): Result<List<String>> {
        return try {
            val response = api.getModels()
            Result.success(response.models.map { it.name })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
