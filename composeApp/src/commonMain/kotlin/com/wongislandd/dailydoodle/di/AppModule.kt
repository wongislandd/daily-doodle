package com.wongislandd.dailydoodle.di

import com.wongislandd.nexus.di.infraModule
import org.koin.core.context.startKoin

fun initializeKoin(context: Any? = null) =
    startKoin {
        modules( *infraModule.toTypedArray() )
    }