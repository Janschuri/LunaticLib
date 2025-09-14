plugins {
    id("java-library")
    id("maven-publish")
}
val lunaticlibVersion: String by rootProject.extra

group = "de.janschuri"
version = "$lunaticlibVersion"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    api(project(":lunaticlib-interfaces"))
    api(project(":lunaticlib-proxyrequests"))
    api(project(":lunaticlib-sender-waterfall"))

    compileOnly(libs.io.github.waterfallmc.waterfall.api)
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



publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}