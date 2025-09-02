evaluationDependsOnChildren()

class Properties(project: Project) {
    val version: String by project
    val modName: String by project
    val modDescription: String by project
    val group: String by project
    val modId: String by project
    val license: String by project
    val repo: String by project
    val minMinecraft: String by project
    val maxMinecraft: String by project
}

inline fun <reified T : Task> TaskContainer.configureFor(task: String, action: Action<T>) {
    named<T>(task).configure(action)
}

fun ConfigurationContainer.findOrCreateDependencyScopeByName(name: String): Configuration {
    return findByName(name) ?: dependencyScope(name).get()
}

subprojects {
    val properties = Properties(rootProject)

    val buildNum = providers.environmentVariable("GITHUB_RUN_NUMBER")
        .filter(String::isNotEmpty)
        .map { "build.$it" }
        .orElse("local")
        .get()

    extensions.getByType<BasePluginExtension>().archivesName = properties.modId

    group = properties.group

    version = "${properties.version}+$buildNum-mc.${libs.versions.minecraft.get()}-$name"

    extensions.getByType<JavaPluginExtension>().run {
        toolchain.languageVersion = JavaLanguageVersion.of(21)
    }

    if (name == "common")
        return@subprojects

    tasks.named<ProcessResources>("processResources") {
        val replace: Map<String, Any> = mapOf(
            "mod_id" to properties.modId,
            "group" to properties.group,
            "mod_description" to properties.modDescription,
            "mod_name" to properties.modName,
            "version" to version,
            "fabric_loader_version" to libs.versions.fabric.loader.get(),
            "fabric_api_version" to libs.versions.fabric.api.get(),
            "minecraft_version" to libs.versions.minecraft.get(),
            "license" to properties.license,
            "repo" to properties.repo,
            "min_minecraft" to properties.minMinecraft,
            "max_minecraft" to properties.maxMinecraft
        )

        inputs.properties(replace)

        filesMatching(listOf("fabric.mod.json", "META-INF/neoforge.mods.toml")) {
            expand(replace)
        }
    }

    configurations.findByName("commonJava")

    val commonJava = configurations.findOrCreateDependencyScopeByName("commonJava")
    val commonResources = configurations.findOrCreateDependencyScopeByName("commonResources")
    val clientCommonJava = configurations.findOrCreateDependencyScopeByName("clientCommonJava")
    val clientCommonResources = configurations.findOrCreateDependencyScopeByName("clientCommonResources")

    val compileOnly: Configuration = configurations.getByName("compileOnly")
    val clientCompileOnly: Configuration = configurations.getByName("clientCompileOnly") {
        extendsFrom(compileOnly)
    }

    dependencies {
        compileOnly(project(":common"))
        clientCompileOnly(project(":common"))
        clientCompileOnly(project(":common", configuration="clientApiElements"))

        commonJava(project(":common", configuration="commonJava"))
        commonResources(project(":common", configuration="commonResources"))

        clientCommonJava(project(":common", configuration="clientCommonJava"))
        clientCommonResources(project(":common", configuration="clientCommonResources"))
    }

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

    tasks.configureFor<JavaCompile>("compileJava") {
        dependsOn(resolvableCommonJava)
        source(resolvableCommonJava)
    }

    tasks.configureFor<ProcessResources>("processResources") {
        dependsOn(resolvableCommonResources)
        from(resolvableCommonResources)
    }

    tasks.configureFor<JavaCompile>("compileClientJava") {
        dependsOn(resolvableClientCommonJava)
        source(resolvableClientCommonJava)
    }

    tasks.configureFor<ProcessResources>("processClientResources") {
        dependsOn(resolvableClientCommonResources)
        from(resolvableClientCommonResources)
    }
}
