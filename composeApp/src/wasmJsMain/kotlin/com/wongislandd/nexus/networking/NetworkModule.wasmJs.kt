package com.wongislandd.nexus.networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import org.koin.core.module.Module
import org.koin.dsl.module

actual val networkModule: Module = module {
    single<HttpClient> { createHttpClient(Js.create()) }
}