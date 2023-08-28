pluginManagement {
    repositories {
        // Snapshots
        maven("https://maven.deftu.xyz/snapshots")
        maven("https://s01.oss.sonatype.org/content/groups/public/")
        mavenLocal()

        // Repositories
        maven("https://maven.deftu.xyz/releases")
        maven("https://maven.fabricmc.net")
        maven("https://maven.architectury.dev/")
        maven("https://maven.minecraftforge.net")
        maven("https://repo.essential.gg/repository/maven-public")
        maven("https://jitpack.io/")

        // Default repositories
        gradlePluginPortal()
        mavenCentral()
    }

    plugins {
        kotlin("jvm") version("1.9.0")
        id("xyz.deftu.gradle.multiversion-root") version("1.19.3")
    }
}

rootProject.name = "MultiCraft"
rootProject.buildFileName = "root.gradle.kts"

listOf(
    "1.8.9-forge",
    "1.12.2-forge",
    "1.16.5-forge",
    "1.16.5-fabric",
    "1.17.1-forge",
    "1.17.1-fabric",
    "1.18.2-forge",
    "1.18.2-fabric",
    "1.19.2-forge",
    "1.19.2-fabric",
    "1.19.3-forge",
    "1.19.3-fabric",
    "1.19.4-forge",
    "1.19.4-fabric",
    "1.20.1-forge",
    "1.20.1-fabric"
).forEach { version ->
    include(":$version")
    project(":$version").apply {
        projectDir = file("versions/$version")
        buildFileName = "../../build.gradle.kts"
    }
}
