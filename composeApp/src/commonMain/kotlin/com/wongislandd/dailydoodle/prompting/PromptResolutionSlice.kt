package com.wongislandd.dailydoodle.prompting

import com.wongislandd.nexus.events.BackChannelEvent
import com.wongislandd.nexus.util.Resource
import com.wongislandd.nexus.viewmodel.ViewModelSlice
import kotlinx.coroutines.launch

data class PromptResolutionUpdate(val prompt: Resource<String>) : BackChannelEvent

class PromptResolutionSlice(private val promptRepository: PromptRepository) : ViewModelSlice() {

    override fun afterInit() {
        sliceScope.launch {
            val prompt = promptRepository.getPrompt()
            backChannelEvents.sendEvent(PromptResolutionUpdate(prompt))
        }
    }
}