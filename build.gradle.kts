plugins {
    kotlin("jvm") version "2.1.10"
    application
}

group = "ca.jonathanfritz"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    applicationDefaultJvmArgs = listOf(
        "-Dorg.lwjgl.librarypath=${layout.buildDirectory}/natives"
    )
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}