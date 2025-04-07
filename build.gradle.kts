plugins {
    kotlin("jvm") version "2.1.10"
    application
}

group = "ca.jonathanfritz"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val lwjglVersion = "3.2.3"

dependencies {
    // lwjgl is our interface to opengl
    implementation("org.lwjgl:lwjgl:$lwjglVersion")
    implementation("org.lwjgl:lwjgl-glfw:$lwjglVersion")
    implementation("org.lwjgl:lwjgl-opengl:$lwjglVersion")
    implementation("org.lwjgl:lwjgl-stb:$lwjglVersion")
    implementation("org.lwjgl:lwjgl-nanovg:$lwjglVersion")

    // we need windows-specific native libraries
    runtimeOnly("org.lwjgl:lwjgl:$lwjglVersion:natives-windows")
    runtimeOnly("org.lwjgl:lwjgl-glfw:$lwjglVersion:natives-windows")
    runtimeOnly("org.lwjgl:lwjgl-opengl:$lwjglVersion:natives-windows")
    runtimeOnly("org.lwjgl:lwjgl-stb:$lwjglVersion:natives-windows")
    runtimeOnly("org.lwjgl:lwjgl-nanovg:$lwjglVersion:natives-windows")

    testImplementation(kotlin("test"))
}

application {
    mainClass.set("ca.jonathanfritz.MainKt")

    applicationDefaultJvmArgs = listOf(
        "-Dorg.lwjgl.librarypath=${layout.buildDirectory}/natives"
    )
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(22)
}