pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

rootProject.name = "legitslimepaper"

include("legitslimepaper-api", "legitslimepaper-server")

include("core", "api")
//include("api")
//project(":api").projectDir = file(".gradle/caches/paperweight/upstreams/slimeworldmanager/api")
//include(".gradle.caches.paperweight.upstreams.slimeworldmanager.plugin", ".gradle.caches.paperweight.upstreams.slimeworldmanager.plugin.core", ".gradle.caches.paperweight.upstreams.slimeworldmanager.plugin.api", ".gradle.caches.paperweight.upstreams.slimeworldmanager.plugin.importer")
//include(".gradle.caches.paperweight.upstreams.slimeworldmanager.plugin.slimeworldmanager-api", ".gradle.caches.paperweight.upstreams.slimeworldmanager.plugin.slimeworldmanager-server")