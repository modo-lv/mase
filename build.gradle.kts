plugins {
    id("org.openjfx.javafxplugin") version("0.1.0")
}

/*
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

plugins {
    kotlin("jvm") version "2.0.0"
    id("application")
    id("org.openjfx.javafxplugin") version("0.1.0")
}

group = "lv.modo.adom"
version = "1.0-SNAPSHOT"

application {
    mainClass = "Main"
}

configurations.matching { it.name.contains("downloadSources") }
    .configureEach {
        attributes {
            val os = DefaultNativePlatform.getCurrentOperatingSystem().toFamilyName()
            val arch = DefaultNativePlatform.getCurrentArchitecture().name
            attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage::class, Usage.JAVA_RUNTIME))
            attribute(OperatingSystemFamily.OPERATING_SYSTEM_ATTRIBUTE, objects.named(OperatingSystemFamily::class, os))
            attribute(MachineArchitecture.ARCHITECTURE_ATTRIBUTE, objects.named(MachineArchitecture::class, arch))
        }
    }

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
    logging.captureStandardOutput(LogLevel.DEBUG)
}

dependencies {
    implementation("commons-io:commons-io:2.17.0")

    // Logging
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.0")
    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("ch.qos.logback:logback-classic:1.5.12")

    // Testing
    testImplementation(kotlin("test"))
    testImplementation("org.amshove.kluent:kluent:1.73")
}

javafx {
    version = "23.0.1"
    modules = listOf("javafx.controls", "javafx.fxml")
}

kotlin {
    jvmToolchain(21)
}*/
