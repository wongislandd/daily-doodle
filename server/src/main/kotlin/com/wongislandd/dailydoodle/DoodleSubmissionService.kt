package com.wongislandd.dailydoodle

import co.touchlab.kermit.Logger

class DoodleSubmissionService(private val requestContextProvider: RequestContextProvider) {

    fun saveCanvas(canvas: NetworkCanvasState) {
        Logger.i(tag = "DoodleSubmissionService", null) {
            "submitDoodle"
        }
    }
}