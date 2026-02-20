dependencies {
    api(project(":lunaticlib-api"))
    api(project(":lunaticlib-utils-paper"))

    api(libs.de.rapha149.signgui.signgui)
    compileOnly(libs.io.papermc.paper.paper.api)
}