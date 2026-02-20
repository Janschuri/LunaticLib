dependencies {
    api(project(":lunaticlib-api"))
    api(project(":lunaticlib-sender-waterfall"))
    api(project(":lunaticlib-commands"))

    compileOnly(libs.io.github.waterfallmc.waterfall.api)
    api(libs.net.kyori.adventure.platform.bungeecord)
}