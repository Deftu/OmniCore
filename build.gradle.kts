import dev.deftu.gradle.utils.DependencyType
import dev.deftu.gradle.utils.version.MinecraftVersions
import dev.deftu.gradle.utils.includeOrShade

plugins {
    java
    kotlin("jvm")
    id("dev.deftu.gradle.multiversion")
    id("dev.deftu.gradle.tools")
    id("dev.deftu.gradle.tools.resources")
    id("dev.deftu.gradle.tools.bloom")
    id("dev.deftu.gradle.tools.shadow")
    id("dev.deftu.gradle.tools.publishing.maven")
    id("dev.deftu.gradle.tools.minecraft.loom")
    id("dev.deftu.gradle.tools.minecraft.api")
    id("dev.deftu.gradle.tools.minecraft.releases-v2")
}

kotlin.explicitApi()
toolkitLoomApi.setupTestClient()
toolkitMultiversion.moveBuildsToRootProject.set(true)
toolkitMavenPublishing.forceLowercase.set(true)
if (mcData.isForgeLike && mcData.version >= MinecraftVersions.VERSION_1_16_5) {
    toolkitLoomHelper.useKotlinForForge()

    if (mcData.isForge) {
        toolkitLoomHelper.useForgeMixin(modData.id)
    }
}

toolkitReleasesV2 {
    detectVersionType.set(true)

    projectIds {
        modrinth.set("MaDESStl")
        curseforge.set("1215299")
    }

    mod("textile", DependencyType.REQUIRED) {
        projectId.set("textile")
        modrinth.projectId.set("textile-lib") // Override
    }

    if (mcData.isFabric) {
        mod("fabric-api", DependencyType.REQUIRED) {
            projectId.set(if (mcData.isLegacyFabric) "legacy-fabric-api" else "fabric-api")
        }
    }

    if (mcData.version >= MinecraftVersions.VERSION_1_16_5) {
        mod("kotlin", DependencyType.REQUIRED) {
            projectId.set(if (mcData.isForgeLike) "kotlin-for-forge" else "fabric-language-kotlin")
        }
    }
}

java {
    withSourcesJar()
}

repositories {
    exclusiveContent {
        forRepository {
            maven("https://api.modrinth.com/maven") {
                name = "Modrinth"
            }
        }

        filter {
            includeGroup("maven.modrinth")
        }
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    with(libs.textile.get()) {
        // The intention here is for the Textile API to be used as a dependency, but not to be included in the final artifact.
        // This is so that the user can install it of their own accord.

        api(this)
        modImplementation("${module.group}:${module.name}-$mcData:${versionConstraint.requiredVersion}")
    }

    api(libs.brigadier)
    api(includeOrShade(libs.enhancedeventbus.get())!!)

    if (mcData.version >= MinecraftVersions.VERSION_1_21_5) {
        val irisVersion = when {
            mcData.version >= MinecraftVersions.VERSION_1_21_6 -> "1.9.1+1.21.7-${mcData.loader.friendlyString}"
            mcData.version >= MinecraftVersions.VERSION_1_21_5 -> "1.8.11+1.21.5-${mcData.loader.friendlyString}"
            else -> throw IllegalStateException("Unsupported Minecraft version: ${mcData.version}")
        }

        modCompileOnly("maven.modrinth:iris:$irisVersion")
    }

    if (mcData.version <= MinecraftVersions.VERSION_1_12_2) {
        if (mcData.isForge) {
            includeOrShade(kotlin("stdlib-jdk8"))
        }

        includeOrShade(libs.brigadier)

        api(libs.dfu) {
            exclude(group = "com.google.guava", module = "guava")
        }

        includeOrShade(libs.dfu) {
            exclude(group = "com.google.guava", module = "guava")
        }
    }

    if (mcData.version <= MinecraftVersions.VERSION_1_8_9) {
        // fastutil for DFU
        api(libs.fastutil)
        includeOrShade(libs.fastutil)
    }

    if (mcData.isFabric) {
        modImplementation(libs.flk.map { "${it.module.group}:${it.module.name}:${mcData.dependencies.fabric.fabricLanguageKotlinVersion}" })

        if (mcData.isLegacyFabric) {
            // 1.8.9 - 1.13
            modImplementation(libs.lfapi.map { "${it.module.group}:${it.module.name}:${mcData.dependencies.legacyFabric.legacyFabricApiVersion}" })
        } else {
            // 1.16.5+
            modImplementation(libs.fapi.map { "${it.module.group}:${it.module.name}:${mcData.dependencies.fabric.fabricApiVersion}" })
        }
    }
}
