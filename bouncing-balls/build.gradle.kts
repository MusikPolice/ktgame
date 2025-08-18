plugins {
    kotlin("jvm")
}

group = "ca.jonathanfritz"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    implementation(project(":engine"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

// copies src/main/resources from the engine project to the bouncing-balls project so that those resources
// exist in the classpath when running the bouncing-balls project.
// this may not be necessary if/when the engine dependency is compiled into a fat jar for distribution.
tasks.processResources {
    from(project(":engine").projectDir.resolve("src/main/resources")) {
        into(".")
    }
}
