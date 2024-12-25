package com.wongislandd.dailydoodle.drawingboard

import com.wongislandd.dailydoodle.NetworkCanvasState
import com.wongislandd.nexus.networking.HttpMethod
import com.wongislandd.nexus.networking.NetworkClient
import io.ktor.client.HttpClient

class CanvasRepository(okHttpClient: HttpClient) : NetworkClient(okHttpClient) {

    suspend fun saveCanvas(networkCanvasState: NetworkCanvasState) {
        makeRequest<String>(
            "http://localhost:8080/",
            HttpMethod.GET
        )
    }
}