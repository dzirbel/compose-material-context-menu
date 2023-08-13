plugins {
    alias(libs.plugins.compose)
    alias(libs.plugins.detekt)
    kotlin("jvm") version libs.versions.kotlin
}

dependencies {
    implementation(project(":lib"))

    implementation(compose.desktop.currentOs)
}

compose.desktop {
    application {
        mainClass = "com.dzirbel.contextmenu.MainKt"
    }
}
