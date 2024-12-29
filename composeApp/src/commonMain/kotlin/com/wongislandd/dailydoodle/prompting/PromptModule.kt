package com.wongislandd.dailydoodle.prompting

import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val promptModule = module {
    viewModelOf(::PromptViewModel)
    singleOf(::PromptRepository)
    factoryOf(::PromptResolutionSlice)
    factoryOf(::PromptScreenStateSlice)

}