dependencies {
    api(project(":lunaticlib-api"))
    api(project(":lunaticlib-commands"))
    api(project(":lunaticlib-utils"))

    api(libs.org.reflections.reflections)
    api(libs.org.yaml.snakeyaml)
    compileOnly(libs.net.kyori.adventure.api)
    compileOnly(libs.net.kyori.adventure.text.serializer.legacy)
}