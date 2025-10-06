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
        }
    }
}

//
// 🧩 Define the fat JARs *only once* in the root project
//
tasks.register<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("lunaticlibPaperFatJar") {
    archiveBaseName.set("lunaticlib-paper")
    archiveClassifier.set("paper")
    from(project(":lunaticlib-config").tasks.named("jar"))
    from(project(":lunaticlib-inventorygui-paper").tasks.named("jar"))
    from(project(":lunaticlib-proxyrequests-paper").tasks.named("jar"))
    from(project(":lunaticlib-utils-paper").tasks.named("jar"))
    from(project(":lunaticlib-commands-paper").tasks.named("jar"))
}

tasks.register<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("lunaticlibVelocityFatJar") {
    archiveBaseName.set("lunaticlib-velocity")
    archiveClassifier.set("velocity")
    from(project(":lunaticlib-config").tasks.named("jar"))
    from(project(":lunaticlib-proxyrequests-paper").tasks.named("jar"))
    from(project(":lunaticlib-commands-paper").tasks.named("jar"))
}

tasks.register<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("lunaticlibWaterfallFatJar") {
    archiveBaseName.set("lunaticlib-waterfall")
    archiveClassifier.set("waterfall")
    from(project(":lunaticlib-config").tasks.named("jar"))
    from(project(":lunaticlib-proxyrequests-waterfall").tasks.named("jar"))
    from(project(":lunaticlib-commands-waterfall").tasks.named("jar"))
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