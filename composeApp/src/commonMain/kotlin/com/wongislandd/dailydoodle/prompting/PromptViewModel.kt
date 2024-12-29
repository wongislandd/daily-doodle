package com.wongislandd.dailydoodle.prompting

import com.wongislandd.nexus.events.BackChannelEvent
import com.wongislandd.nexus.events.EventBus
import com.wongislandd.nexus.events.UiEvent
import com.wongislandd.nexus.viewmodel.SliceableViewModel

class PromptViewModel(
    val promptScreenStateSlice: PromptScreenStateSlice,
    promptResolutionSlice: PromptResolutionSlice,
    uiEventBus: EventBus<UiEvent>,
    backChannelEventBus: EventBus<BackChannelEvent>
) : SliceableViewModel(
    uiEventBus,
    backChannelEventBus
) {

    init {
        registerSlices(promptScreenStateSlice, promptResolutionSlice)
    }
}