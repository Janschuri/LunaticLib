dependencies {
    api(project(":lunaticlib-api"))
    api(project(":lunaticlib-proxyrequests"))
    api(project(":lunaticlib-sender-waterfall"))

    compileOnly(libs.io.github.waterfallmc.waterfall.api)
}