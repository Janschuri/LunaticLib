plugins {
    id("java-library")
    `maven-publish`
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
    api(project(":lunaticlib-commands-waterfall"))
    api(project(":lunaticlib-proxyrequests-waterfall"))

    implementation(libs.org.bstats.bstats.bungeecord)
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