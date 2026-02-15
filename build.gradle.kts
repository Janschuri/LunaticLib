plugins {
    id("java-library")
    id("maven-publish")
    id("io.github.goooler.shadow") version "8.1.8"
}

val lunaticlibVersion: String by lazy {
    providers.gradleProperty("lunaticlibVersion").orNull ?: run {
        val tomlFile = file("gradle/libs.versions.toml")
        val versionRegex = Regex("""de-janschuri-lunaticlib\s*=\s*"([^"]+)"""")
        tomlFile.readLines()
            .firstNotNullOf { versionRegex.find(it)?.groupValues?.get(1) }
    }
}
extra["lunaticlibVersion"] = lunaticlibVersion

group = "de.janschuri"
version = lunaticlibVersion

repositories {
    mavenLocal()
    mavenCentral()
}

fun org.gradle.api.publish.PublishingExtension.configurePublishRepositories() {
    repositories {
        mavenLocal()

        val githubActor = System.getenv("GITHUB_ACTOR")
        val githubToken = System.getenv("GITHUB_TOKEN")
        val githubRepository = System.getenv("GITHUB_REPOSITORY")

        if (!githubActor.isNullOrBlank() && !githubToken.isNullOrBlank() && !githubRepository.isNullOrBlank()) {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/$githubRepository")
                credentials {
                    username = githubActor
                    password = githubToken
                }
            }
        }
    }
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                from(components["java"])
            }
        }
        configurePublishRepositories()
    }
}

//
// 🧩 Define the fat JARs *only once* in the root project
//
tasks.register<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("lunaticlibPaperFatJar") {
    archiveBaseName.set("lunaticlib-paper")
    val modules = listOf(
        ":lunaticlib-api",
        ":lunaticlib-commands",
        ":lunaticlib-sender",
        ":lunaticlib-utils",
        ":lunaticlib-config",
        ":lunaticlib-sender-waterfall",
        ":lunaticlib-proxyrequests-paper",
        ":lunaticlib-sender-paper",
        ":lunaticlib-commands-paper",
        ":lunaticlib-utils-paper",
        ":lunaticlib-inventorygui-paper"
    )
    modules.forEach { module ->
        from(project(module).tasks.named("jar"))
        configurations += project(module).configurations.getByName("runtimeClasspath")
    }
}

tasks.register<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("lunaticlibVelocityFatJar") {
    archiveBaseName.set("lunaticlib-velocity")
    val modules = listOf(
        ":lunaticlib-api",
        ":lunaticlib-commands",
        ":lunaticlib-sender",
        ":lunaticlib-utils",
        ":lunaticlib-config",
        ":lunaticlib-sender-waterfall",
        ":lunaticlib-proxyrequests-velocity",
        ":lunaticlib-sender-velocity",
        ":lunaticlib-commands-velocity"
    )
    modules.forEach { module ->
        from(project(module).tasks.named("jar"))
        configurations += project(module).configurations.getByName("runtimeClasspath")
    }
}

tasks.register<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("lunaticlibWaterfallFatJar") {
    archiveBaseName.set("lunaticlib-waterfall")
    val modules = listOf(
        ":lunaticlib-api",
        ":lunaticlib-commands",
        ":lunaticlib-sender",
        ":lunaticlib-utils",
        ":lunaticlib-config",
        ":lunaticlib-sender-waterfall",
        ":lunaticlib-proxyrequests-waterfall",
        ":lunaticlib-sender-waterfall",
        ":lunaticlib-commands-waterfall"
    )
    modules.forEach { module ->
        from(project(module).tasks.named("jar"))
        configurations += project(module).configurations.getByName("runtimeClasspath")
    }
}

publishing {
    publications {
        create<MavenPublication>("paperFatJar") {
            artifact(tasks.named("lunaticlibPaperFatJar"))
            groupId = group.toString()
            artifactId = "lunaticlib-paper"
            version = version.toString()
        }
        create<MavenPublication>("velocityFatJar") {
            artifact(tasks.named("lunaticlibVelocityFatJar"))
            groupId = group.toString()
            artifactId = "lunaticlib-velocity"
            version = version.toString()
        }
        create<MavenPublication>("waterfallFatJar") {
            artifact(tasks.named("lunaticlibWaterfallFatJar"))
            groupId = group.toString()
            artifactId = "lunaticlib-waterfall"
            version = version.toString()
        }
    }

    configurePublishRepositories()
}

tasks.register("publishAllModulesToMavenLocal") {
    group = "publishing"
    description = "Publishes all submodules and fat jars to Maven Local"
    dependsOn(
        subprojects.map { it.path + ":publishToMavenLocal" } +
                listOf(
                    "publishPaperFatJarPublicationToMavenLocal",
                    "publishVelocityFatJarPublicationToMavenLocal",
                    "publishWaterfallFatJarPublicationToMavenLocal"
                )
    )
}
