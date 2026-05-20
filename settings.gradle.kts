pluginManagement {
    repositories {
        maven("https://repo.papermc.io/repository/maven-public/")
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "legitslimepaper"

fun optionalProject(path: String) {
    val d = file(path)
    if (d.isDirectory) {
        include(path)
    } else {
        logger.lifecycle("Skipping $path (missing dir: ${d.path})")
    }
}

optionalProject("core")
optionalProject("api")

include("legitslimepaper-api")
include("legitslimepaper-server")
