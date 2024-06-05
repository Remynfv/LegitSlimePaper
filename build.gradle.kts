import io.papermc.paperweight.util.Git

plugins {
    java
    `maven-publish`
    id("io.github.goooler.shadow") version "8.1.7" apply false
    id("io.papermc.paperweight.patcher") version "1.7.1"
    id("org.kordamp.gradle.profiles") version "0.47.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

val paperMavenPublicUrl = "https://repo.papermc.io/repository/maven-public/"

repositories {
    mavenCentral()
    maven(paperMavenPublicUrl) {
        content { onlyForConfigurations(configurations.paperclip.name) }
    }
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    repositories {
        mavenLocal()
        mavenCentral()

        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.codemc.io/repository/nms/")
        maven("https://repo.rapture.pw/repository/maven-releases/")
        maven("https://repo.glaremasters.me/repository/concuncan/")
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }
}

dependencies {
    remapper("net.fabricmc:tiny-remapper:0.10.1:fat") // Must be kept in sync with upstream
    decompiler("org.vineflower:vineflower:1.10.1") // Must be kept in sync with upstream
    paperclip("io.papermc:paperclip:3.0.3") // You probably want this to be kept in sync with upstream
}

subprojects {
    tasks.withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release = 21
    }
    tasks.withType<Javadoc> {
        options.encoding = Charsets.UTF_8.name()
    }
    tasks.withType<ProcessResources> {
        filteringCharset = Charsets.UTF_8.name()
    }

    repositories {
        mavenCentral()
        maven(paperMavenPublicUrl)
    }
}

val paperDir = layout.projectDirectory.dir("AdvancedSlimePaper")
val initSubmodules by tasks.registering {
    outputs.upToDateWhen { false }
    doLast {
        Git(layout.projectDirectory)("submodule", "update", "--init", "--remote").executeOut()
    }
}

paperweight {
    serverProject = project(":legitslimepaper-server")

    remapRepo.set(paperMavenPublicUrl)
    decompileRepo.set(paperMavenPublicUrl)

    upstreams {
        register("slimeworldmanager") {
//            url = github("infernalsuite", "advancedslimepaper")
//            ref = providers.gradleProperty("advancedslimepaperRef")

            upstreamDataTask {
                dependsOn(initSubmodules)
                projectDir = paperDir
            }

            patchTasks {
                register("api") {
                    upstreamDir = paperDir.dir("slimeworldmanager-api")
                    patchDir = layout.projectDirectory.dir("patches/api")
                    outputDir = layout.projectDirectory.dir("legitslimepaper-api")
                }
                register("server") {
                    upstreamDir = paperDir.dir("slimeworldmanager-server")
                    patchDir = layout.projectDirectory.dir("patches/server")
                    outputDir = layout.projectDirectory.dir("legitslimepaper-server")
                    importMcDev = true
                }
                register("generatedApi") {
                    isBareDirectory = true
                    upstreamDir = paperDir.dir("paper-api-generator/generated")
                    patchDir = layout.projectDirectory.dir("patches/generatedApi")
                    outputDir = layout.projectDirectory.dir("paper-api-generator/generated")
                }
                register("core") {
                    isBareDirectory = true
                    upstreamDir = paperDir.dir("core")
                    patchDir = layout.projectDirectory.dir("patches/core")
                    outputDir = layout.projectDirectory.dir("core")
                }

                register("aswmApi") {
                    isBareDirectory = true
                    upstreamDir = paperDir.dir("api")
                    patchDir = layout.projectDirectory.dir("patches/aswmApi")
                    outputDir = layout.projectDirectory.dir("api")
                }
            }
        }
    }
}