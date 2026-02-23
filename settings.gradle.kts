pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "legitslimepaper"

include("legitslimepaper-api")
include("legitslimepaper-server")

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
