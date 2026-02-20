dependencies {
    api(project(":lunaticlib-api"))
    api(project(":lunaticlib-sender"))

    compileOnly(libs.io.github.waterfallmc.waterfall.api)
    api(libs.net.kyori.adventure.platform.bungeecord)
}