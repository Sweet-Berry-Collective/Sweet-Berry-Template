plugins {
    alias(libs.plugins.mod.dev.gradle)
}

repositories {
    mavenCentral()
    maven("https://mvn.devos.one/releases/")
    maven("https://mvn.devos.one/snapshots/")
}

neoForge {
    neoFormVersion = libs.versions.neo.form.get()

    parchment {
        minecraftVersion = libs.versions.minecraft.get()
        mappingsVersion = libs.versions.mojmap.get()
    }
}

dependencies {
    implementation(libs.bundles.common)
}
