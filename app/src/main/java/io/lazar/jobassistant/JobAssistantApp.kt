package io.lazar.jobassistant

import android.app.Application
import io.lazar.jobassistant.data.api.OllamaClient
import io.lazar.jobassistant.data.repository.SettingsRepository
import io.lazar.jobassistant.data.repository.SuggestionRepository

class JobAssistantApp : Application() {

    lateinit var settingsRepository: SettingsRepository
        private set

    lateinit var suggestionRepository: SuggestionRepository
        private set

    lateinit var ollamaClient: OllamaClient
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        settingsRepository = SettingsRepository(this)
        suggestionRepository = SuggestionRepository(this)
        ollamaClient = OllamaClient()
    }

    companion object {
        lateinit var instance: JobAssistantApp
            private set
    }
}
