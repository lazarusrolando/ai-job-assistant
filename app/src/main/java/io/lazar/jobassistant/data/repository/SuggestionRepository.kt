package io.lazar.jobassistant.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import io.lazar.jobassistant.data.model.JobSuggestion
import io.lazar.jobassistant.data.model.SuggestionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.suggestionStore: DataStore<Preferences> by preferencesDataStore(name = "suggestions")

class SuggestionRepository(private val context: Context) {

    companion object {
        val SUGGESTIONS = stringPreferencesKey("suggestions")
    }

    private val json = Json { ignoreUnknownKeys = true }

    val suggestions: Flow<List<JobSuggestion>> = context.suggestionStore.data.map { preferences ->
        val jsonStr = preferences[SUGGESTIONS] ?: return@map emptyList()
        try {
            val listType = object : kotlinx.serialization.types.GenericType<List<String>>() {}
            val encoded = json.decodeFromString<List<String>>(jsonStr)
            encoded.mapNotNull { json.decodeFromString<JobSuggestion>(it) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addSuggestion(suggestion: JobSuggestion) {
        context.suggestionStore.edit { preferences ->
            val current = preferences[SUGGESTIONS] ?: "[]"
            val list = try {
                json.decodeFromString<List<String>>(current).toMutableList()
            } catch (e: Exception) {
                mutableListOf()
            }
            list.add(0, json.encodeToString(suggestion))
            preferences[SUGGESTIONS] = json.encodeToString(list)
        }
    }

    suspend fun deleteSuggestion(id: String) {
        context.suggestionStore.edit { preferences ->
            val current = preferences[SUGGESTIONS] ?: return@edit
            val list = try {
                json.decodeFromString<List<String>>(current).toMutableList()
            } catch (e: Exception) {
                return@edit
            }
            list.removeAll { it.contains("\"id\":\"$id\"") }
            preferences[SUGGESTIONS] = json.encodeToString(list)
        }
    }

    suspend fun clearAll() {
        context.suggestionStore.edit { preferences ->
            preferences.remove(SUGGESTIONS)
        }
    }
}
