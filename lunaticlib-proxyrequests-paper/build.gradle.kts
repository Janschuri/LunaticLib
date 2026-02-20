dependencies {
    api(project(":lunaticlib-sender-paper"))
    api(project(":lunaticlib-proxyrequests"))
    api(project(":lunaticlib-utils-paper"))

    compileOnly(project(":lunaticlib-inventorygui-paper"))
    compileOnly(libs.io.papermc.paper.paper.api)
    compileOnly(libs.com.github.milkbowl.vaultapi)
}