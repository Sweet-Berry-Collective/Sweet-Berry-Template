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

val clientImplementation by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

neoForge {
    version = libs.versions.neo.forge.get()

    addModdingDependenciesTo(client)

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

val commonJava = configurations.dependencyScope("commonJava")
val commonResources = configurations.dependencyScope("commonResources")

dependencies {
    implementation(libs.bundles.neoforge)
    clientImplementation(main.output)

    commonJava(project(":common", configuration="clientCommonJava"))
    commonResources(project(":common", configuration="clientCommonResources"))
}

inline fun <reified T : Task> TaskContainer.configureFor(task: String, action: Action<T>) {
    named<T>(task).configure(action)
}

tasks.configureFor<JavaCompile>("compileJava") {
    source(client.allSource)
}

tasks.configureFor<ProcessResources>("processResources") {
    from(client.resources)
}
