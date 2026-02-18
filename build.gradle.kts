plugins {
    id("java-library")
    id("maven-publish")
    id("io.github.goooler.shadow") version "8.1.8"
}

val lunaticlibVersion: String by lazy {
    val tomlFile = file("gradle/libs.versions.toml")
    val versionRegex = Regex("""de-janschuri-lunaticlib\s*=\s*"([^"]+)"""")
    tomlFile.readLines()
        .firstNotNullOf { versionRegex.find(it)?.groupValues?.get(1) }
}
extra["lunaticlibVersion"] = lunaticlibVersion
val targetJavaVersion = 21
extra["targetJavaVersion"] = targetJavaVersion

group = "de.janschuri"
version = lunaticlibVersion

repositories {
    mavenLocal()
    mavenCentral()
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

        repositories {
            mavenLocal()
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/janschuri/lunaticlib")
                credentials {
                    username = System.getenv("GITHUB_ACTOR")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }
        }
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

    repositories {
        mavenLocal()
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/janschuri/lunaticlib")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
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

tasks.register("publishAllModulesToGitHubPackages") {
    group = "publishing"
    description = "Publishes all submodules and fat jars to GitHub Packages"
    dependsOn(
        subprojects.map { it.path + ":publishAllPublicationsToGitHubPackagesRepository" } +
                listOf(
                    "publishPaperFatJarPublicationToGitHubPackagesRepository",
                    "publishVelocityFatJarPublicationToGitHubPackagesRepository",
                    "publishWaterfallFatJarPublicationToGitHubPackagesRepository"
                )
    )
}