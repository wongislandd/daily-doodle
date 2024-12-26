package com.wongislandd.dailydoodle.explore

import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val exploreModule = module {
    viewModelOf(::ExploreViewModel)
    factoryOf(::ExploreScreenStateSlice)
    factoryOf(::ExploreHistoryResolutionSlice)
}