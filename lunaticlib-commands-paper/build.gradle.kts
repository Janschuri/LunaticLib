dependencies {
    api(project(":lunaticlib-api"))
    api(project(":lunaticlib-sender-paper"))
    api(project(":lunaticlib-commands"))

    compileOnly(libs.io.papermc.paper.paper.api)
}