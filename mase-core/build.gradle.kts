plugins {
    id("mase-component")
    `java-test-fixtures`
    id("org.openjfx.javafxplugin") version("0.1.0")
}

dependencies {
    implementation("org.kotlincrypto.endians:endians:0.3.1")
    implementation("com.hanggrian.ktfx:ktfx:0.3")

    testImplementation(libs.logback)
}

javafx {
    version = "23.0.1"
    modules = listOf("javafx.base") // To write observable properties in core
}
