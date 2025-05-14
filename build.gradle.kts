import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    alias(libs.plugins.paperweight.patcher)
}

paperweight {
    upstreams.register("aspaper") {
        repo = github("InfernalSuite", "AdvancedSlimePaper")
        ref = providers.gradleProperty("aspRef")

        patchFile {
            path = "aspaper-server/build.gradle.kts"
            outputFile = file("legitslimepaper-server/build.gradle.kts")
            patchFile = file("legitslimepaper-server/build.gradle.kts.patch")
        }
        patchFile {
            path = "aspaper-api/build.gradle.kts"
            outputFile = file("legitslimepaper-api/build.gradle.kts")
            patchFile = file("legitslimepaper-api/build.gradle.kts.patch")
        }
        patchRepo("paperApi") {
            upstreamPath = "paper-api"
            patchesDir = file("legitslimepaper-api/paper-patches")
            outputDir = file("paper-api")
        }
        patchDir("aspaperApi") {
            upstreamPath = "aspaper-api"
            excludes = listOf("build.gradle.kts", "build.gradle.kts.patch", "paper-patches")
            patchesDir = file("legitslimepaper-api/aspaper-patches")
            outputDir = file("aspaper-api")
        }

        //ASP :api
        patchFile {
            path = "api/build.gradle.kts"
            outputFile = file("api/build.gradle.kts")
            patchFile = file("api/build.gradle.kts.patch")
        }
        patchDir("aspApi") {
            upstreamPath = "api/src"
            excludes = listOf("build.gradle.kts")
            patchesDir = file("api/api-patches")
            outputDir = file("api/src")
        }

        //ASP :core
        patchFile {
            path = "core/build.gradle.kts"
            outputFile = file("core/build.gradle.kts")
            patchFile = file("core/build.gradle.kts.patch")
        }
        patchDir("aspCore") {
            upstreamPath = "core/src"
            excludes = listOf("build.gradle.kts")
            patchesDir = file("core/core-patches")
            outputDir = file("core/src")
        }



        patchFile {
            path = "buildSrc/build.gradle.kts"
            outputFile = file("buildSrc/build.gradle.kts")
            patchFile = file("build-data/buildSrc.build.gradle.kts.patch")
        }
        patchDir("aspBuildSrc") {
            upstreamPath = "buildSrc"
            excludes = listOf("build.gradle.kts")
//            patchesDir = file("core/core-patches")
            outputDir = file("buildSrc")
        }

    }
}

val paperMavenPublicUrl = "https://repo.papermc.io/repository/maven-public/"

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    repositories {
        mavenCentral()
        maven(paperMavenPublicUrl)

        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.codemc.io/repository/nms/")
        maven("https://repo.rapture.pw/repository/maven-releases/")
        maven("https://repo.glaremasters.me/repository/concuncan/")
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        mavenLocal()
    }

    tasks.withType<AbstractArchiveTask>().configureEach {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }
    tasks.withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release = 21
        options.isFork = true
    }
    tasks.withType<Javadoc> {
        options.encoding = Charsets.UTF_8.name()
    }
    tasks.withType<ProcessResources> {
        filteringCharset = Charsets.UTF_8.name()
    }
    tasks.withType<Test> {
        testLogging {
            showStackTraces = true
            exceptionFormat = TestExceptionFormat.FULL
            events(TestLogEvent.STANDARD_OUT)
        }
    }

    extensions.configure<PublishingExtension> {
        repositories {
            /*
            maven("https://repo.papermc.io/repository/maven-snapshots/") {
                name = "paperSnapshots"
                credentials(PasswordCredentials::class)
            }
             */
        }
    }
}
