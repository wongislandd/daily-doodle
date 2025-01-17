package com.wongislandd.dailydoodle

import androidx.navigation.NavController
import com.wongislandd.navigation.NavigationItemKey
import com.wongislandd.navigation.NavigationSlice
import com.wongislandd.nexus.events.BackChannelEvent
import com.wongislandd.nexus.events.EventBus
import com.wongislandd.nexus.viewmodel.SliceableViewModel
import com.wongislandd.nexus.events.UiEvent

class AppViewModel(
    private val navigationSlice: NavigationSlice,
    uiEventBus: EventBus<UiEvent>,
    backChannelEventBus: EventBus<BackChannelEvent>
): SliceableViewModel(uiEventBus, backChannelEventBus) {

    init {
        registerSlices(navigationSlice)
    }

    fun navigate(
        navigationController: NavController,
        navigationKey: NavigationItemKey,
        args: Map<String, Any?> = emptyMap(),
        removeSelfFromStack: Boolean = false
    ) {
        navigationSlice.navigationHelper.navigate(
            navigationController,
            navigationKey.name,
            args,
            removeSelfFromStack
        )
    }
}