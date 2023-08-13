plugins {
    alias(libs.plugins.compose)
    alias(libs.plugins.detekt)
    kotlin("jvm") version libs.versions.kotlin
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutines.core)
}
