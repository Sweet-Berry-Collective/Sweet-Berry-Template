plugins {
    alias(libs.plugins.loom)
}

evaluationDependsOn(":common")

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

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())

    compileOnly(project(":common"))
    clientCompileOnly(project(":common"))

    modImplementation(libs.bundles.fabric)
    include(libs.bundles.include.fabric)
}

fun configureJavaCompileForSourceSet(sourceSet: SourceSet, action: Action<JavaCompile>) {
    tasks.named<JavaCompile>(sourceSet.compileJavaTaskName).configure(action)
}

configureJavaCompileForSourceSet(client) {
    source(project(":common").sourceSets["client"].allSource)
}
configureJavaCompileForSourceSet(main) {
    source(project(":common").sourceSets["main"].allSource)
}

tasks.withType(JavaCompile::class).configureEach {
    options.encoding = "UTF-8"
    options.release = 21
}
