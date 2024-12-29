package com.wongislandd.dailydoodle

class PromptService {

    fun getPrompt(): String {
        val randomIndex = drawingPrompts.indices.random()
        val prompt = drawingPrompts[randomIndex]
        return prompt
    }
}