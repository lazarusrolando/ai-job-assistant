package io.lazar.jobassistant.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.lazar.jobassistant.data.api.OllamaClient
import io.lazar.jobassistant.data.model.JobSuggestion
import io.lazar.jobassistant.data.model.SuggestionType
import io.lazar.jobassistant.data.repository.SettingsRepository
import io.lazar.jobassistant.data.repository.SuggestionRepository
import io.lazar.jobassistant.util.AiPromptTemplates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class CareerPathUiState(
    val currentRole: String = "",
    val targetRole: String = "",
    val skills: String = "",
    val isLoading: Boolean = false,
    val result: String? = null,
    val error: String? = null
)

class CareerPathViewModel(
    private val ollamaClient: OllamaClient,
    private val settingsRepository: SettingsRepository,
    private val suggestionRepository: SuggestionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CareerPathUiState())
    val uiState: StateFlow<CareerPathUiState> = _uiState.asStateFlow()

    fun updateCurrentRole(role: String) {
        _uiState.value = _uiState.value.copy(currentRole = role)
    }

    fun updateTargetRole(role: String) {
        _uiState.value = _uiState.value.copy(targetRole = role)
    }

    fun updateSkills(skills: String) {
        _uiState.value = _uiState.value.copy(skills = skills)
    }

    fun generateCareerPath() {
        val currentRole = _uiState.value.currentRole
        val skills = _uiState.value.skills

        if (currentRole.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Please enter your current role")
            return
        }
        if (skills.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Please enter your skills")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val model = settingsRepository.model.first()
            val prompt = AiPromptTemplates.careerPathPrompt(
                currentRole,
                _uiState.value.targetRole,
                skills
            )

            ollamaClient.generate(model, prompt).fold(
                onSuccess = { result ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        result = result
                    )
                    saveSuggestion(result)
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to generate career path"
                    )
                }
            )
        }
    }

    private fun saveSuggestion(output: String) {
        viewModelScope.launch {
            val suggestion = JobSuggestion(
                type = SuggestionType.CAREER_PATH,
                input = "${_uiState.value.currentRole} | ${_uiState.value.targetRole}",
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
