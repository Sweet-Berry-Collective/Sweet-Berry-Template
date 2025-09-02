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

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())

    modImplementation(libs.bundles.fabric)
    include(libs.bundles.include.fabric)
}
