package com.wongislandd.dailydoodle.di

import com.wongislandd.dailydoodle.AppViewModel
import com.wongislandd.dailydoodle.drawingboard.drawingModule
import com.wongislandd.navigation.NavigationSlice
import com.wongislandd.navigation.supportedNavigationItems
import com.wongislandd.nexus.di.infraModule
import com.wongislandd.nexus.navigation.NavigationItem
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::AppViewModel)
    factoryOf(::NavigationSlice)
    single<Set<NavigationItem>> { supportedNavigationItems.values.toSet() }
}

fun initializeKoin(context: Any? = null) =
    startKoin {
        modules( *infraModule.toTypedArray(), appModule, drawingModule)
    }