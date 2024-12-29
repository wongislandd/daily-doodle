package com.wongislandd.dailydoodle.explore

import com.wongislandd.nexus.events.BackChannelEvent
import com.wongislandd.nexus.util.Resource
import com.wongislandd.nexus.viewmodel.ViewModelSlice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class ExploreScreenState(
    val history: Resource<List<CanvasPreview>> = Resource.Loading()
)

class ExploreScreenStateSlice : ViewModelSlice() {

    private val _exploreScreenState = MutableStateFlow(ExploreScreenState())
    val exploreScreenState: StateFlow<ExploreScreenState> = _exploreScreenState

    override fun handleBackChannelEvent(event: BackChannelEvent) {
        super.handleBackChannelEvent(event)
        when (event) {
            is HistoryResolutionUpdate -> {
                _exploreScreenState.update {
                    it.copy(history = event.savedCanvasesRes)
                }
            }
        }
    }
}