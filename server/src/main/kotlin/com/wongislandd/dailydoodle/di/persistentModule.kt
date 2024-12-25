package com.wongislandd.dailydoodle.di

import com.wongislandd.dailydoodle.DoodleSubmissionService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val persistentModule = module {
    singleOf(::DoodleSubmissionService)
}