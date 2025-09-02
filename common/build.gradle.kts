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
    clientImplementation(sourceSets.main)

    implementation(libs.bundles.common)
}
