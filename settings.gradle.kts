rootProject.name = "compose-augmented-context-menu"

include("demo", "lib")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
