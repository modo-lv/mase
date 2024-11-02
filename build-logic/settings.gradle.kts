rootProject.name = "build-logic"

dependencyResolutionManagement {
    // In modules Maven central is added by the core plugin, but for plugins themselves we need to set it manually.
    repositories {
        mavenCentral()
    }
}