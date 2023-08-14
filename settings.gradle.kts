rootProject.name = "compose-material-context-menu"

include("demo", "lib")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
