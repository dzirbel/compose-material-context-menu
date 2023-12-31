# Publishing

This file is a guide to publishing new versions of the library. Semantic versioning is used: in this
document the version name `1.2.3` is used as a placeholder for the Maven artifact version.

1. Ensure Gradle is configured to run with Java 11
1. Run `./gradlew clean check jar` to ensure the code compiles and passes linting
1. Update the library version in `lib/build.gradle.kts`
1. Update the `README.md` with the new version number
1. Commit the changes with commit message `Release v1.2.3` and push
1. Build the JAR again with `./gradlew clean :lib:jar` to update its version
1. Create a new release
   [on GitHub](https://github.com/dzirbel/compose-material-context-menu/releases/new) with tag name
   `v1.2.3`, release title `v1.2.3` and a bullet-pointed changelog. Attach the built JAR from
   `lib/builds/libs` and update its name to `compose-material-context-menu-1.2.3.jar`
1. Ensure `secrets.properties` exists locally and has all the fields requested in the
   `build.gradle.kts` for signing and uploading
1. Run `./gradlew :lib:publishMavenPublicationToMavenCentralStagingRepository`
1. Open https://oss.sonatype.org/#stagingRepositories, close the repository, and release it
