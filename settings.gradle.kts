rootProject.name = "Sweet-Berry-Template"

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        maven("https://maven.fabricmc.net/" )
        maven("https://mvn.devos.one/releases/")
        maven("https://mvn.devos.one/snapshots/")
        maven("https://maven.neoforged.net/releases")
    }

    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://maven.fabricmc.net/" )
        maven("https://mvn.devos.one/releases/")
        maven("https://mvn.devos.one/snapshots/")
        maven("https://maven.neoforged.net/releases")
    }
}

include(":common")
include(":fabric")
include(":neoforge")
