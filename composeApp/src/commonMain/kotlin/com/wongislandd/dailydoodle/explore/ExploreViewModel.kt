package com.wongislandd.dailydoodle.explore

import com.wongislandd.nexus.events.BackChannelEvent
import com.wongislandd.nexus.events.EventBus
import com.wongislandd.nexus.events.UiEvent
import com.wongislandd.nexus.viewmodel.SliceableViewModel

class ExploreViewModel(
    val exploreScreenStateSlice: ExploreScreenStateSlice,
    exploreHistoryResolutionSlice: ExploreHistoryResolutionSlice,
    uiEventBus: EventBus<UiEvent>,
    backChannelEventBus: EventBus<BackChannelEvent>
): SliceableViewModel(uiEventBus, backChannelEventBus) {

    init {
        registerSlices(exploreScreenStateSlice, exploreHistoryResolutionSlice)
    }
}