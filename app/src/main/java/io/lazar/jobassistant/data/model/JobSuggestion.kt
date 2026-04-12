package io.lazar.jobassistant.data.model

data class JobSuggestion(
    val id: String = System.currentTimeMillis().toString(),
    val type: SuggestionType,
    val input: String,
    val output: String,
    val timestamp: Long = System.currentTimeMillis()
)

enum class SuggestionType {
    JOB_MATCH,
    JOB_DESCRIPTION,
    CAREER_PATH
}
