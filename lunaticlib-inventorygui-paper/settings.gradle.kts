rootProject.name = "lunaticlib-inventorygui-paper"

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml")) // adjust path
        }
    }
}