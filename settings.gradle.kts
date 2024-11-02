rootProject.name = "mase"

pluginManagement {
    includeBuild("build-logic")
}

include(
    "mase-core",
    "mase-cli",
    "mase-gui",
)

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("build-logic/gradle/libs.versions.toml"))
        }
    }
}
