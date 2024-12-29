package com.wongislandd.dailydoodle.prompting

import com.wongislandd.nexus.networking.HttpMethod
import com.wongislandd.nexus.networking.NetworkClient
import com.wongislandd.nexus.util.Resource
import io.ktor.client.HttpClient

class PromptRepository(httpClient: HttpClient) : NetworkClient(httpClient) {
    suspend fun getPrompt(): Resource<String> {
        return makeRequest(
            "prompt",
            HttpMethod.GET
        )
    }
}