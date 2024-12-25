package com.wongislandd.dailydoodle

import io.ktor.server.application.PipelineCall

interface RequestContextProvider {
    fun getContext(): RequestContext
}

class RequestContextProviderImpl: RequestContextProvider {

    private var context: RequestContext? = null

    fun setContext(call: PipelineCall) {
        context = RequestContext(call)
    }

    override fun getContext(): RequestContext = requireNotNull(context)
}

data class RequestContext(val call: PipelineCall)