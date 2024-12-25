package com.wongislandd.dailydoodle

import com.wongislandd.dailydoodle.di.persistentModule
import com.wongislandd.dailydoodle.di.requestModule
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import org.koin.ktor.plugin.Koin
import org.koin.ktor.plugin.scope

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::main)
        .start(wait = true)
}

fun Application.main() {
    install(Koin) {
        modules(requestModule, persistentModule)
    }
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
    install(RequestContextPlugin)
    defaultModule()
}

val RequestContextPlugin = createApplicationPlugin(name = "RequestContextPlugin") {
    onCall { call ->
        val requestContextProvider = call.scope.get<RequestContextProviderImpl>()
        requestContextProvider.setContext(call)
    }
}

private fun Application.defaultModule() {

    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }
        get("/canvas") {
            val canvas = call.receive<NetworkCanvasState>()
            val doodleSubmissionService = call.scope.get<DoodleSubmissionService>()
            doodleSubmissionService.saveCanvas(canvas)
            call.respondText("Doodle submitted")
        }
    }
}
