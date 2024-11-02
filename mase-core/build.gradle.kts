plugins {
    id("mase-component")
    `java-test-fixtures`
}

dependencies {
    implementation("org.kotlincrypto.endians:endians:0.3.1")

    testImplementation(libs.logback)
}