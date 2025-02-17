import java.util.Properties

plugins {
    alias(libs.plugins.compose)
    alias(libs.plugins.detekt)
    kotlin("jvm") version libs.versions.kotlin

    `maven-publish`
    signing
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.components.resources)
    implementation(libs.kotlinx.coroutines.core)
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.github.dzirbel"
            artifactId = rootProject.name
            version = "0.2.0"

            from(components["java"])

            pom {
                name.set("Compose Material Context Menus")
                description.set("Material context menu implementation for Compose Multiplatform")
                url.set("https://github.com/dzirbel/compose-material-context-menu")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("http://www.opensource.org/licenses/mit-license")
                    }
                }

                developers {
                    developer {
                        id.set("dzirbel")
                        name.set("Dominic Zirbel")
                        email.set("dominiczirbel@gmail.com")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com:dzirbel/compose-material-context-menu.git")
                    developerConnection.set("scm:git:ssh://github.com:dzirbel/compose-material-context-menu.git")
                    url.set("https://github.com/dzirbel/compose-material-context-menu")
                }
            }
        }
    }

    repositories {
        maven {
            name = "mavenCentralStaging"
            setUrl("https://oss.sonatype.org/service/local/staging/deploy/maven2/")

            credentials {
                val secretsFile = rootDir.resolve("secrets.properties")
                val secrets = Properties()
                if (secretsFile.exists()) {
                    secretsFile.reader().use { reader -> secrets.load(reader) }
                    logger.lifecycle("Loaded ${secrets.size} secrets from $secretsFile")

                    username = secrets["ossrhUsername"] as? String
                    password = secrets["ossrhPassword"] as? String
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["maven"])
}
