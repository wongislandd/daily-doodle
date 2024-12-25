package com.wongislandd.dailydoodle.di

import com.wongislandd.dailydoodle.DoodleSubmissionService
import com.wongislandd.dailydoodle.RequestContextProvider
import com.wongislandd.dailydoodle.RequestContextProviderImpl
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module
import org.koin.ktor.plugin.RequestScope

val requestModule = module {
    scope<RequestScope> {
        scopedOf(::RequestContextProviderImpl)
        scoped<RequestContextProvider> { get<RequestContextProviderImpl>() }
        scopedOf(::DoodleSubmissionService)
    }
}