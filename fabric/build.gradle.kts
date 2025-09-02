plugins {
    alias(libs.plugins.loom)
}

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/" )
    maven("https://mvn.devos.one/releases/")
    maven("https://mvn.devos.one/snapshots/")
}

loom {
    splitEnvironmentSourceSets()

    mods {
        register("sweet_berry_template") {
            sourceSet("main")
            sourceSet("client")
        }
    }

    runs {
        named("server") {
            configName = "Fabric Server"
            server()
        }

        named("client") {
            configName = "Fabric Client"
            client()
        }

        configureEach {
            isIdeConfigGenerated = true
            appendProjectPathToConfigName = false

            property("mixin.debug.export", "true")
        }
    }
}

val client by sourceSets.getting
val main by sourceSets.getting

val clientCompileOnly by configurations.getting {
    extendsFrom(configurations.compileOnly.get())
}

val commonJava by configurations.dependencyScope("commonJava")
val commonResources by configurations.dependencyScope("commonResources")
val clientCommonJava by configurations.dependencyScope("clientCommonJava")
val clientCommonResources by configurations.dependencyScope("clientCommonResources")

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())

    compileOnly(project(":common"))
    clientCompileOnly(project(":common"))
    clientCompileOnly(project(":common", configuration="clientApiElements"))

    commonJava(project(":common", configuration="commonJava"))
    commonResources(project(":common", configuration="commonResources"))

    clientCommonJava(project(":common", configuration="clientCommonJava"))
    clientCommonResources(project(":common", configuration="clientCommonResources"))

    modImplementation(libs.bundles.fabric)
    include(libs.bundles.include.fabric)
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

val resolvableCommonJava: Configuration by configurations.resolvable("resolvableCommonJava") {
    extendsFrom(commonJava)
}

val resolvableClientCommonJava: Configuration by configurations.resolvable("resolvableClientCommonJava") {
    extendsFrom(clientCommonJava)
}

val resolvableCommonResources: Configuration by configurations.resolvable("resolvableCommonResources") {
    extendsFrom(commonResources)
}

val resolvableClientCommonResources: Configuration by configurations.resolvable("resolvableClientCommonResources") {
    extendsFrom(clientCommonResources)
}

inline fun <reified T : Task> configureForSourceSet(task: String, action: Action<T>) {
    tasks.named<T>(task).configure(action)
}

configureForSourceSet<JavaCompile>(main.compileJavaTaskName) {
    dependsOn(resolvableCommonJava)
    source(resolvableCommonJava)
}

configureForSourceSet<ProcessResources>(main.processResourcesTaskName) {
    dependsOn(resolvableCommonResources)
    from(resolvableCommonResources)
}

configureForSourceSet<JavaCompile>(client.compileJavaTaskName) {
    dependsOn(resolvableClientCommonJava)
    source(resolvableClientCommonJava)
}

configureForSourceSet<ProcessResources>(client.processResourcesTaskName) {
    dependsOn(resolvableClientCommonResources)
    from(resolvableClientCommonResources)
}
