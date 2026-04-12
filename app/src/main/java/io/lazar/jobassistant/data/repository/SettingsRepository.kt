package io.lazar.jobassistant.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepository(private val context: Context) {

    companion object {
        val OLLAMA_URL = stringPreferencesKey("ollama_url")
        val MODEL = stringPreferencesKey("model")
        val TEMPERATURE = floatPreferencesKey("temperature")
        val MAX_TOKENS = intPreferencesKey("max_tokens")
    }

    val ollamaUrl: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[OLLAMA_URL] ?: "http://10.0.2.2:11434"
    }

    val model: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[MODEL] ?: "llama3.2"
    }

    val temperature: Flow<Float> = context.dataStore.data.map { preferences ->
        preferences[TEMPERATURE] ?: 0.7f
    }

    val maxTokens: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[MAX_TOKENS] ?: 2048
    }

    suspend fun saveSettings(ollamaUrl: String, model: String, temperature: Float, maxTokens: Int) {
        context.dataStore.edit { preferences ->
            preferences[OLLAMA_URL] = ollamaUrl
            preferences[MODEL] = model
            preferences[TEMPERATURE] = temperature
            preferences[MAX_TOKENS] = maxTokens
        }
    }
}
