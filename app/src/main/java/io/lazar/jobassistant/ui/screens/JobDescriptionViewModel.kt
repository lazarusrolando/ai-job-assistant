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

data class JobDescriptionUiState(
    val requirements: String = "",
    val isLoading: Boolean = false,
    val result: String? = null,
    val error: String? = null
)

class JobDescriptionViewModel(
    private val ollamaClient: OllamaClient,
    private val settingsRepository: SettingsRepository,
    private val suggestionRepository: SuggestionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(JobDescriptionUiState())
    val uiState: StateFlow<JobDescriptionUiState> = _uiState.asStateFlow()

    fun updateRequirements(requirements: String) {
        _uiState.value = _uiState.value.copy(requirements = requirements)
    }

    fun generateDescription() {
        val requirements = _uiState.value.requirements
        if (requirements.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Please enter job requirements")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val model = settingsRepository.model.first()
            val prompt = AiPromptTemplates.jobDescriptionPrompt(requirements)

            ollamaClient.generate(model, prompt).fold(
                onSuccess = { result ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        result = result
                    )
                    saveSuggestion(requirements, result)
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to generate description"
                    )
                }
            )
        }
    }

    private fun saveSuggestion(input: String, output: String) {
        viewModelScope.launch {
            val suggestion = JobSuggestion(
                type = SuggestionType.JOB_DESCRIPTION,
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
