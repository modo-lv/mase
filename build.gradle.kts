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

dependencies {
    implementation("org.kotlincrypto.endians:endians:0.3.1")
    implementation("commons-io:commons-io:2.17.0")
    testImplementation(kotlin("test"))
}

javafx {
    version = "23.0.1"
    modules = listOf("javafx.controls")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}