import pl.allegro.tech.build.axion.release.domain.properties.VersionProperties
import pl.allegro.tech.build.axion.release.domain.scm.ScmPosition

plugins {
    id("java-library")
    id("maven-publish")
    id("io.github.goooler.shadow") version "8.1.8"
    id("pl.allegro.tech.build.axion-release") version "1.21.1"
}

scmVersion {
    tag {
        tag {
            prefix.set("v")
        }
        initialVersion({config, position -> "2.0.0"})
    }

    branchVersionCreator.putAll(mapOf("develop" to "simple"))
}

val lunaticlibVersion: String by extra(scmVersion.version)
val targetJavaVersion: Int by extra(21)

group = "de.janschuri"
version = lunaticlibVersion

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    group = "de.janschuri"
    version = "$lunaticlibVersion"

    repositories {
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
        maven {
            url = uri("https://repo.papermc.io/repository/maven-public/")
        }
        maven {
            url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        }
    }

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

    val targetJavaVersion = 17
    java {
        val javaVersion = JavaVersion.toVersion(targetJavaVersion)
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
        if (JavaVersion.current() < javaVersion) {
            toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
            options.release.set(targetJavaVersion)
        }
    }
}

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