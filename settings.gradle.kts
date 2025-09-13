pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        maven { url = uri("https://maven.myket.ir") }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        maven { url = uri("https://maven.myket.ir") }
    }
}
rootProject.name = "My Application"
include(":app")