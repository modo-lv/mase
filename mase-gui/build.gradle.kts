import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

plugins {
    id("mase-component")
    id("application")
    id("org.openjfx.javafxplugin") version("0.1.0")
}

application {
    mainClass = "Main"
}

dependencies {
    implementation(project(":mase-core"))
    implementation(libs.logback)

    testImplementation(testFixtures(project(":mase-core")))
}

javafx {
    version = "23.0.1"
    modules = listOf("javafx.controls", "javafx.fxml")
}


// JavaFX has different sources for different platforms, so we need to tell Gradle which ones to download.
configurations.matching { it.name.contains("downloadSources") }.configureEach {
    attributes {
        val os = DefaultNativePlatform.getCurrentOperatingSystem().toFamilyName()
        val arch = DefaultNativePlatform.getCurrentArchitecture().name
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage::class, Usage.JAVA_RUNTIME))
        attribute(OperatingSystemFamily.OPERATING_SYSTEM_ATTRIBUTE, objects.named(OperatingSystemFamily::class, os))
        attribute(MachineArchitecture.ARCHITECTURE_ATTRIBUTE, objects.named(MachineArchitecture::class, arch))
    }
}
