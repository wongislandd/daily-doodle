package com.wongislandd.dailydoodle.di

import com.wongislandd.nexus.networking.networkModule
import org.koin.core.context.startKoin
import org.koin.dsl.module

val appModule = module {

}

fun initializeKoin(context: Any? = null) =
    startKoin {
        modules(
            appModule, networkModule
        )
    }