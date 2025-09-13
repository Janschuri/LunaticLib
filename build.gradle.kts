plugins {
    id("java-library")
    id("maven-publish")
}


val lunaticlibVersion: String by lazy {
    val tomlFile = file("gradle/libs.versions.toml")
    val versionRegex = Regex("""de-janschuri-lunaticlib\s*=\s*"([^"]+)"""")
    tomlFile.readLines()
        .firstNotNullOf { versionRegex.find(it)?.groupValues?.get(1) }
}
extra["lunaticlibVersion"] = lunaticlibVersion

group = "de.janschuri"
version = "2.0.0"

repositories {
    mavenLocal()
    mavenCentral()
}

val submodulesToPublish = listOf(
    "lunaticlib-interfaces",
    "lunaticlib-utils",
    "lunaticlib-utils-paper",
    "lunaticlib-inventorygui",
    "lunaticlib-config",
    "lunaticlib-commands",
    "lunaticlib-commands-paper",
    "lunaticlib-commands-velocity",
    "lunaticlib-commands-waterfall",
)

tasks.register("publishModulesToMavenLocal") {
    group = "publishing"
    description = "Publishes selected submodules to Maven Local"
    dependsOn(
        submodulesToPublish.map { ":$it:publishToMavenLocal" }
    )
}