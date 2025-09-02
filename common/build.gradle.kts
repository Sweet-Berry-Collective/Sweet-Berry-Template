import fish.cichlidmc.cichlid_gradle.util.Distribution

plugins {
    java
    alias(libs.plugins.cichlid.gradle)
}

repositories {
    mavenCentral()
    minecraft.libraries()
    minecraft.versions()
    maven("https://mvn.devos.one/releases/")
    maven("https://mvn.devos.one/snapshots/")
}

val mcServer by minecraft.creating {
    version = libs.versions.minecraft.get()
    distribution = Distribution.SERVER
}

val mcClient by minecraft.creating {
    version = libs.versions.minecraft.get()
    distribution = Distribution.CLIENT
}

val main by sourceSets.getting
val client by sourceSets.creating

val clientImplementation by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

dependencies {
    implementation(mcServer.dependency)
    clientImplementation(mcClient.dependency)
    clientImplementation(main.output)

    implementation(libs.bundles.common)
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)

    // Feacher!
    registerFeature("client") {
        usingSourceSet(client)
    }
}

configurations.consumable("commonJava")
configurations.consumable("commonResources")
configurations.consumable("clientCommonJava")
configurations.consumable("clientCommonResources")

artifacts {
    add("commonJava", main.java.sourceDirectories.singleFile)
    add("commonResources", main.resources.sourceDirectories.singleFile)
    add("clientCommonJava", client.java.sourceDirectories.singleFile)
    add("clientCommonResources", client.resources.sourceDirectories.singleFile)
}
