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
version = lunaticlibVersion

repositories {
    mavenLocal()
    mavenCentral()
}

val submodulesToPublish = listOf(
    "lunaticlib-api",
    "lunaticlib-utils",
    "lunaticlib-utils-paper",
    "lunaticlib-inventorygui",
    "lunaticlib-config",
    "lunaticlib-commands",
    "lunaticlib-commands-paper",
    "lunaticlib-commands-velocity",
    "lunaticlib-commands-waterfall",
    "lunaticlib-sender",
    "lunaticlib-sender-paper",
    "lunaticlib-sender-velocity",
    "lunaticlib-sender-waterfall",
    "lunaticlib-proxyrequests",
    "lunaticlib-proxyrequests-paper",
    "lunaticlib-proxyrequests-velocity",
    "lunaticlib-proxyrequests-waterfall",
    "lunaticlib-paper",
    "lunaticlib-velocity",
    "lunaticlib-waterfall",
)

tasks.register("publishModulesToMavenLocal") {
    group = "publishing"
    description = "Publishes selected submodules to Maven Local"
    dependsOn(
        submodulesToPublish.map { ":$it:publishToMavenLocal" }
    )
}