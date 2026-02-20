dependencies {
    api(project(":lunaticlib-api"))
    api(project(":lunaticlib-sender"))

    compileOnly(libs.io.papermc.paper.paper.api)
}
