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

tasks.register("publishAllModulesToMavenLocal") {
    group = "publishing"
    description = "Publishes all submodules to Maven Local"
    dependsOn(
        subprojects.map { it.path + ":publishToMavenLocal" }
    )
}