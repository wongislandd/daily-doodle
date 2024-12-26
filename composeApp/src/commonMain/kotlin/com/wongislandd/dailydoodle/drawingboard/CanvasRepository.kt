package com.wongislandd.dailydoodle.drawingboard

import com.wongislandd.dailydoodle.NetworkCanvasState
import com.wongislandd.nexus.networking.HttpMethod
import com.wongislandd.nexus.networking.NetworkClient
import com.wongislandd.nexus.util.Empty
import com.wongislandd.nexus.util.Resource
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class CanvasRepository(okHttpClient: HttpClient) : NetworkClient(okHttpClient) {

    // Fire and forget, should probably expect some signal back
    suspend fun saveCanvas(networkCanvasState: NetworkCanvasState): Resource<Empty> {
        return makeRequest<Empty>(
            "http://localhost:8080/canvas",
            HttpMethod.POST
        ) {
            contentType(ContentType.Application.Json)
            setBody(networkCanvasState)
        }
    }
}