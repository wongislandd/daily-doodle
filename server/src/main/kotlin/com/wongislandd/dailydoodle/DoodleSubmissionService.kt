package com.wongislandd.dailydoodle

class DoodleSubmissionService(private val requestContextProvider: RequestContextProvider) {

    fun saveCanvas(canvas: NetworkCanvasState) {
        PersistentStorage.canvasSubmissions.add(canvas)
    }

    fun getCanvas(): List<NetworkCanvasState> {
        return PersistentStorage.canvasSubmissions
    }
}