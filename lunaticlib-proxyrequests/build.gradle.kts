dependencies {
    api(project(":lunaticlib-api"))
    api(project(":lunaticlib-utils"))
    api(project(":lunaticlib-sender"))

    compileOnly(libs.com.google.guava.guava)
}