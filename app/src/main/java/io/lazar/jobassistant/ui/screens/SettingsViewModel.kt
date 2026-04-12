package io.lazar.jobassistant.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.lazar.jobassistant.data.api.OllamaClient
import io.lazar.jobassistant.data.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class SettingsUiState(
    val ollamaUrl: String = "http://10.0.2.2:11434",
    val model: String = "llama3.2",
    val temperature: Float = 0.7f,
    val maxTokens: Int = 2048,
    val availableModels: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val saved: Boolean = false,
    val error: String? = null
)

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val ollamaClient: OllamaClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
        loadAvailableModels()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            val url = settingsRepository.ollamaUrl.first()
            val model = settingsRepository.model.first()
            val temp = settingsRepository.temperature.first()
            val maxTokens = settingsRepository.maxTokens.first()

            _uiState.value = _uiState.value.copy(
                ollamaUrl = url,
                model = model,
                temperature = temp,
                maxTokens = maxTokens
            )
        }
    }

    fun loadAvailableModels() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            ollamaClient.getModels().fold(
                onSuccess = { models ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        availableModels = models
                    )
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            )
        }
    }

    fun updateUrl(url: String) {
        _uiState.value = _uiState.value.copy(ollamaUrl = url, saved = false)
    }

    fun updateModel(model: String) {
        _uiState.value = _uiState.value.copy(model = model, saved = false)
    }

    fun updateTemperature(temp: Float) {
        _uiState.value = _uiState.value.copy(temperature = temp, saved = false)
    }

    fun updateMaxTokens(tokens: Int) {
        _uiState.value = _uiState.value.copy(maxTokens = tokens, saved = false)
    }

    fun saveSettings() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)
            settingsRepository.saveSettings(
                _uiState.value.ollamaUrl,
                _uiState.value.model,
                _uiState.value.temperature,
                _uiState.value.maxTokens
            )
            _uiState.value = _uiState.value.copy(isSaving = false, saved = true)
        }
    }
}
