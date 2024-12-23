package com.wongislandd.nexus.viewmodel

import androidx.lifecycle.ViewModel
import com.wongislandd.nexus.events.BackChannelEvent
import com.wongislandd.nexus.events.EventBus
import com.wongislandd.nexus.events.UiEvent

abstract class SliceableViewModel(
    val uiEventBus: EventBus<UiEvent>,
    val backChannelEventBus: EventBus<BackChannelEvent>
) : ViewModel() {

    fun registerSlice(viewModelSlice: ViewModelSlice) {
        viewModelSlice.register(this)
    }

    override fun onCleared() {
        super.onCleared()
    }
}