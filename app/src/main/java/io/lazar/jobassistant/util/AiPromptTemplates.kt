package io.lazar.jobassistant.util

object AiPromptTemplates {

    fun jobMatchPrompt(skills: String): String {
        return """
You are an expert career advisor. Based on the following skills, experience, and interests, suggest 5-7 specific job titles that would be a great match.

User's skills and interests:
$skills

For each job title, provide:
1. The job title
2. A brief explanation (1-2 sentences) of why it matches their profile
3. Key skills from their profile that are relevant

Format your response as a numbered list with clear headings. Be specific and practical.
""".trimIndent()
    }

    fun jobDescriptionPrompt(requirements: String): String {
        return """
You are an expert HR professional and job description writer. Create a compelling, detailed job description based on the following requirements.

Requirements and details:
$requirements

Include the following sections:
1. Job Title (suggest an appropriate title)
2. About the Role (2-3 sentences)
3. Key Responsibilities (5-7 bullet points)
4. Required Qualifications (4-6 bullet points)
5. Preferred Qualifications (3-4 bullet points)
6. Nice to Have Skills (2-3 bullet points)
7. What We Offer (3-4 bullet points)

Make it professional, engaging, and inclusive. Use clear, modern language.
""".trimIndent()
    }

    fun careerPathPrompt(currentRole: String, targetRole: String?, skills: String): String {
        val targetInfo = if (targetRole.isNullOrBlank()) "" else "They want to reach: $targetRole"

        return """
You are an expert career coach. Create a detailed career progression plan for someone in the following situation.

Current role: $currentRole
$targetInfo
Skills and experience:
$skills

Provide a comprehensive career path plan including:
1. Current level assessment (what roles they're qualified for now)
2. Short-term goals (6-12 months) - specific skills to develop
3. Mid-term goals (1-2 years) - target roles and transitions
4. Long-term goals (3-5 years) - senior/leadership opportunities
5. Recommended certifications or courses
6. Key networking and visibility strategies
7. Potential roadblocks and how to overcome them

Format with clear headings and bullet points. Be specific and actionable.
""".trimIndent()
    }
}
