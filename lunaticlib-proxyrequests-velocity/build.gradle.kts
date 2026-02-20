dependencies {
    api(project(":lunaticlib-sender-velocity"))
    api(project(":lunaticlib-proxyrequests"))

    compileOnly(libs.com.velocitypowered.velocity.api)
    compileOnly(libs.net.kyori.adventure.api)
    compileOnly(libs.com.google.guava.guava)
}