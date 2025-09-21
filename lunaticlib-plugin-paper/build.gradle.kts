plugins {
    id("java-library")
    `maven-publish`
    id("io.github.goooler.shadow") version "8.1.8"
}
val lunaticlibVersion: String by rootProject.extra

group = "de.janschuri"
version = "$lunaticlibVersion"
repositories {
    mavenCentral()
    maven {
        name = "papermc-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    api(project(":lunaticlib-config"))
    api(project(":lunaticlib-utils-paper"))
    api(project(":lunaticlib-commands-paper"))
    api(project(":lunaticlib-inventorygui-paper"))
    api(project(":lunaticlib-proxyrequests-paper"))

    implementation(libs.org.bstats.bstats.bukkit)
    compileOnly(libs.io.papermc.paper.paper.api)
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    jar {
        enabled = false
    }
    shadowJar {
        archiveBaseName.set("LunaticStorage")
        archiveClassifier.set("")
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    relocate("org.bstats", "de.janschuri.lunaticlib.platform.paper.libs.org.bstats")
}

tasks.named<ProcessResources>("processResources") {
    filesMatching("plugin.yml") {
        expand("project" to mapOf("version" to project.version))
    }
}