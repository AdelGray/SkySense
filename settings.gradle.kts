pluginManagement {
    repositories {
        google()

        gradlePluginPortal()
        jcenter()

    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        maven ("https://jitpack.io" )
        jcenter()

    }
}

rootProject.name = "SkySense"
include(":app")
