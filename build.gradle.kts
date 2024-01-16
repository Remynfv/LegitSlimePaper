import io.papermc.paperweight.util.Git

plugins {
    java
    `maven-publish`
    id("org.kordamp.gradle.profiles") version "0.47.0"

    // Nothing special about this, just keep it up to date
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false

    // In general, keep this version in sync with upstream. Sometimes a newer version than upstream might work, but an older version is extremely likely to break.
    id("io.papermc.paperweight.patcher") version "1.5.11"
}

val paperMavenPublicUrl = "https://repo.papermc.io/repository/maven-public/"

repositories {
    mavenCentral()
    maven(paperMavenPublicUrl) {
        content { onlyForConfigurations(configurations.paperclip.name) }
    }
}

dependencies {
    remapper("net.fabricmc:tiny-remapper:0.8.10:fat") // Must be kept in sync with upstream
    decompiler("net.minecraftforge:forgeflower:2.0.627.2") // Must be kept in sync with upstream
    paperclip("io.papermc:paperclip:3.0.3") // You probably want this to be kept in sync with upstream
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(17)
        }
    }
}

subprojects {
    tasks.withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release = 17
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
        maven("https://repo.infernalsuite.com/repository/maven-snapshots/")
        maven("https://repo.rapture.pw/repository/maven-releases/")
    }
}

//paperweight {
//    serverProject = project(":legitslimepaper-server")
//
//    remapRepo = paperMavenPublicUrl
//    decompileRepo = paperMavenPublicUrl
//
//    usePaperUpstream(providers.gradleProperty("advancedslimepaperRef")) {
//        withPaperPatcher {
//            apiPatchDir = layout.projectDirectory.dir("patches/api")
//            apiOutputDir = layout.projectDirectory.dir("legitslimepaper-api")
//
//            serverPatchDir = layout.projectDirectory.dir("patches/server")
//            serverOutputDir = layout.projectDirectory.dir("legitslimepaper-server")
//
//        }
//        patchTasks.register("generatedApi") {
//            isBareDirectory = true
//            upstreamDirPath = "paper-api-generator/generated"
//            patchDir = layout.projectDirectory.dir("patches/generatedApi")
//            outputDir = layout.projectDirectory.dir("paper-api-generator/generated")
//        }
//    }
//}

//paperweight {
//    serverProject.set(project(":legitslimepaper-server"))
//
//    remapRepo.set(paperMavenPublicUrl)
//    decompileRepo.set(paperMavenPublicUrl)
//
//    useStandardUpstream("slimeworldmanager") {
//        url.set(github("infernalsuite", "advancedslimepaper"))
//        ref.set(providers.gradleProperty("advancedslimepaperRef"))
//
//        patchTasks.register("core") {
//            isBareDirectory = true
//            upstreamDirPath = "core"
//            patchDir = layout.projectDirectory.dir("patches/core")
//            outputDir = layout.projectDirectory.dir("core")
//        }
//
//        patchTasks.register("aswmApi") {
//            isBareDirectory = true
//            upstreamDirPath = "api"
//            patchDir = layout.projectDirectory.dir("patches/aswmApi")
//            outputDir = layout.projectDirectory.dir("aswmApi")
//        }
//
//        withStandardPatcher {
//            apiSourceDirPath = "slimeworldmanager-api"
//            serverSourceDirPath = "slimeworldmanager-server"
//
//
//            buildDataDir
//            apiPatchDir = layout.projectDirectory.dir("patches/api")
//            apiOutputDir = layout.projectDirectory.dir("legitslimepaper-api")
//
//            serverPatchDir = layout.projectDirectory.dir("patches/server")
//            serverOutputDir = layout.projectDirectory.dir("legitslimepaper-server")
//        }
//    }
//}

val paperDir = layout.projectDirectory.dir("AdvancedSlimePaper")
val initSubmodules by tasks.registering {
    outputs.upToDateWhen { false }
    doLast {
        Git(layout.projectDirectory)("submodule", "update", "--init").executeOut()
    }
}

paperweight {
    serverProject = project(":legitslimepaper-server")

    remapRepo = paperMavenPublicUrl
    decompileRepo = paperMavenPublicUrl

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
//                register("generatedApi") {
//                    isBareDirectory = true
//                    upstreamDir = paperDir.dir("paper-api-generator/generated")
//                    patchDir = layout.projectDirectory.dir("patches/generatedApi")
//                    outputDir = layout.projectDirectory.dir("paper-api-generator/generated")
//                }
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