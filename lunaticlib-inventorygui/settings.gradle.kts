rootProject.name = "lunaticlib-inventorygui"

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