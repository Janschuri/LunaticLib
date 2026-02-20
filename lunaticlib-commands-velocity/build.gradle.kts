dependencies {
    api(project(":lunaticlib-api"))
    api(project(":lunaticlib-sender-velocity"))
    api(project(":lunaticlib-commands"))

    compileOnly(libs.com.velocitypowered.velocity.api)
}