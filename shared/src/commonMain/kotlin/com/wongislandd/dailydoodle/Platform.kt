package com.wongislandd.dailydoodle

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform