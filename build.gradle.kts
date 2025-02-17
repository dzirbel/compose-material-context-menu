plugins {
    alias(libs.plugins.detekt)
    kotlin("jvm") version libs.versions.kotlin
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
    }

    configurations.all {
        resolutionStrategy {
            failOnNonReproducibleResolution()
        }
    }

    afterEvaluate {
        configureKotlin()
        configureDetekt()
    }
}

fun Project.configureDetekt() {
    detekt {
        config.from(rootProject.files("detekt-config.yml"))
    }

    dependencies {
        detektPlugins(libs.detekt.formatting)
        detektPlugins(libs.twitter.compose.rules)
    }
}

fun Project.configureKotlin() {
    kotlin {
        compilerOptions {
            allWarningsAsErrors = true

            // enable Compose compiler metrics and reports:
            // https://github.com/androidx/androidx/blob/androidx-main/compose/compiler/design/compiler-metrics.md
            val composeCompilerReportsDir = layout.buildDirectory.dir("compose").get()
            freeCompilerArgs.addAll(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=$composeCompilerReportsDir"
            )

            freeCompilerArgs.addAll(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=$composeCompilerReportsDir"
            )
        }
    }
}
