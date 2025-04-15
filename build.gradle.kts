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

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    val textileVersion = "0.13.0"
    api(includeOrShade("dev.deftu:textile:$textileVersion")!!)
    modImplementation(includeOrShade("dev.deftu:textile-$mcData:$textileVersion")!!)

    api("com.mojang:brigadier:1.0.18")
    api(includeOrShade("dev.deftu:enhancedeventbus:2.0.0")!!)

    if (mcData.isForge && mcData.version <= MinecraftVersions.VERSION_1_12_2) {
        includeOrShade(kotlin("stdlib-jdk8"))
        includeOrShade("com.mojang:brigadier:1.0.18")
    }

    if (mcData.isFabric) {
        modImplementation("net.fabricmc:fabric-language-kotlin:${mcData.dependencies.fabric.fabricLanguageKotlinVersion}")

        if (mcData.isLegacyFabric) {
            // 1.8.9 - 1.13
            modImplementation("net.legacyfabric.legacy-fabric-api:legacy-fabric-api:${mcData.dependencies.legacyFabric.legacyFabricApiVersion}")
        } else {
            // 1.16.5+
            modImplementation("net.fabricmc.fabric-api:fabric-api:${mcData.dependencies.fabric.fabricApiVersion}")
        }
    }
}
