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
    maven {
        name = "papermc-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    api(project(":lunaticlib-sender-velocity"))
    api(project(":lunaticlib-proxyrequests"))

    compileOnly(libs.com.velocitypowered.velocity.api)
    compileOnly(libs.net.kyori.adventure.api)
    compileOnly(libs.com.google.guava.guava)
}

val targetJavaVersion: Int by rootProject.extra
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