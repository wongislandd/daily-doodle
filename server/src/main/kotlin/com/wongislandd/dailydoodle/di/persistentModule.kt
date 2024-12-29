package com.wongislandd.dailydoodle.di

import com.wongislandd.dailydoodle.PromptService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val persistentModule = module {
    singleOf(::PromptService)
}