pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Traq"

include(":app")

// Core modules
include(":core:common")
include(":core:data")
include(":core:location")
include(":core:sensors")
include(":core:ai")
include(":core:export")
include(":core:ui")
include(":core:permissions")
include(":core:maps")

// Feature modules
include(":feature:tracking")
include(":feature:dashboard")
include(":feature:history")
include(":feature:trip-detail")
include(":feature:settings")
include(":feature:onboarding")
