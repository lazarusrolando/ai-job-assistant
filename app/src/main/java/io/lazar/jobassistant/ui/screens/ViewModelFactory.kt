package io.lazar.jobassistant.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.lazar.jobassistant.JobAssistantApp
import io.lazar.jobassistant.data.repository.SuggestionRepository

class ViewModelFactory(
    private val app: JobAssistantApp,
    private val suggestionRepository: SuggestionRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            JobMatchViewModel::class.java -> {
                JobMatchViewModel(
                    app.ollamaClient,
                    app.settingsRepository,
                    suggestionRepository
                ) as T
            }
            JobDescriptionViewModel::class.java -> {
                JobDescriptionViewModel(
                    app.ollamaClient,
                    app.settingsRepository,
                    suggestionRepository
                ) as T
            }
            CareerPathViewModel::class.java -> {
                CareerPathViewModel(
                    app.ollamaClient,
                    app.settingsRepository,
                    suggestionRepository
                ) as T
            }
            SettingsViewModel::class.java -> {
                SettingsViewModel(
                    app.settingsRepository,
                    app.ollamaClient
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
