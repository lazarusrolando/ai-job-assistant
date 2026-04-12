package io.lazar.jobassistant.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.lazar.jobassistant.data.api.OllamaClient
import io.lazar.jobassistant.data.model.JobSuggestion
import io.lazar.jobassistant.data.model.SuggestionType
import io.lazar.jobassistant.data.repository.SuggestionRepository
import io.lazar.jobassistant.util.AiPromptTemplates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class JobMatchUiState(
    val skills: String = "",
    val isLoading: Boolean = false,
    val result: String? = null,
    val error: String? = null
)

class JobMatchViewModel(
    private val ollamaClient: OllamaClient,
    private val settingsRepository: SettingsRepository,
    private val suggestionRepository: SuggestionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(JobMatchUiState())
    val uiState: StateFlow<JobMatchUiState> = _uiState.asStateFlow()

    fun updateSkills(skills: String) {
        _uiState.value = _uiState.value.copy(skills = skills)
    }

    fun generateSuggestions() {
        val skills = _uiState.value.skills
        if (skills.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Please enter your skills")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val model = settingsRepository.model.first()
            val prompt = AiPromptTemplates.jobMatchPrompt(skills)

            ollamaClient.generate(model, prompt).fold(
                onSuccess = { result ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        result = result
                    )
                    saveSuggestion(skills, result)
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to generate suggestions"
                    )
                }
            )
        }
    }

    private fun saveSuggestion(input: String, output: String) {
        viewModelScope.launch {
            val suggestion = JobSuggestion(
                type = SuggestionType.JOB_MATCH,
                input = input,
                output = output
            )
            suggestionRepository.addSuggestion(suggestion)
        }
    }

    fun clearResult() {
        _uiState.value = _uiState.value.copy(result = null, error = null)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
