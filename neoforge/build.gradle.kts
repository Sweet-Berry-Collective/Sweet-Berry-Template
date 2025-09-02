plugins {
    alias(libs.plugins.mod.dev.gradle)
}

repositories {
    mavenCentral()
    maven("https://mvn.devos.one/releases/")
    maven("https://mvn.devos.one/snapshots/")
}

val main by sourceSets.getting
val client by sourceSets.creating

neoForge {
    version = libs.versions.neo.forge.get()

    validateAccessTransformers = true
    runs {
        create("client") {
            client()
        }
        create("server") {
            server()
        }
    }

    parchment {
        minecraftVersion = libs.versions.minecraft.get()
        mappingsVersion = libs.versions.mojmap.get()
    }

    mods {
        create(rootProject.properties["modid"].toString()) {
            sourceSet(main)
            sourceSet(client)
        }
    }
}

val clientImplementation by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

dependencies {
    implementation(libs.bundles.common)
    clientImplementation(main.output)
}

inline fun <reified T : Task> TaskContainer.configureFor(task: String, action: Action<T>) {
    named<T>(task).configure(action)
}

tasks.configureFor<JavaCompile>("compileJava") {
    source(client.allSource)
}
