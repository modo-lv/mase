plugins {
    id("mase-component")
    id("application")
}

application {
    mainClass = "MainKt"
}

dependencies {
    implementation(project(":mase-core"))
    implementation(libs.logback)

    testImplementation(testFixtures(project(":mase-core")))
}