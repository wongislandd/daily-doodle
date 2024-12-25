plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
}

group = "com.wongislandd.dailydoodle"
version = "1.0.0"
application {
    mainClass.set("com.wongislandd.dailydoodle.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.content.negotation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.koin.ktor)
    implementation(libs.kermit)

    testImplementation(libs.ktor.server.test.host)
//    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)
}