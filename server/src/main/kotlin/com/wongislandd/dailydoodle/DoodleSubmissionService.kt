package com.wongislandd.dailydoodle

import co.touchlab.kermit.Logger

class DoodleSubmissionService(private val requestContextProvider: RequestContextProvider) {

    fun submitDoodle() {
        Logger.i(tag = "DoodleSubmissionService", null) {
            "submitDoodle"
        }
    }
}