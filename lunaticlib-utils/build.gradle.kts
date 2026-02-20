dependencies {
    api(project(":lunaticlib-api"))

    compileOnly(libs.org.slf4j.slf4j.api)
    compileOnly(libs.net.kyori.adventure.api)
    compileOnly(libs.net.kyori.adventure.text.serializer.legacy)
}