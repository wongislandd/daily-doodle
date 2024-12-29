package com.wongislandd.dailydoodle.prompting

import com.wongislandd.nexus.events.BackChannelEvent
import com.wongislandd.nexus.util.Resource
import com.wongislandd.nexus.viewmodel.ViewModelSlice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class PromptScreenState(val promptRes: Resource<String> = Resource.Loading())

class PromptScreenStateSlice : ViewModelSlice() {

    private val _screenState: MutableStateFlow<PromptScreenState> =
        MutableStateFlow(PromptScreenState())
    val screenState: StateFlow<PromptScreenState> = _screenState

    override fun handleBackChannelEvent(event: BackChannelEvent) {
        super.handleBackChannelEvent(event)
        when (event) {
            is PromptResolutionUpdate -> {
                _screenState.value = _screenState.value.copy(promptRes = event.prompt)
            }
        }
    }
}