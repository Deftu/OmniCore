import com.modrinth.minotaur.dependencies.DependencyType
import com.modrinth.minotaur.dependencies.ModDependency
import dev.deftu.gradle.tools.minecraft.CurseRelation
import dev.deftu.gradle.tools.minecraft.CurseRelationType
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
    id("dev.deftu.gradle.tools.minecraft.releases")
}

kotlin.explicitApi()
toolkitLoomApi.setupTestClient()
toolkitMultiversion.moveBuildsToRootProject.set(true)
toolkitMavenPublishing.forceLowercase.set(true)
if (mcData.isForgeLike && mcData.version >= MinecraftVersions.VERSION_1_16_5) {
    logger.lifecycle("==> Applying Mixin configs and Kotlin For Forge")
    toolkitLoomHelper.useKotlinForForge()

    if (mcData.isForge) {
        toolkitLoomHelper.useForgeMixin(modData.id)
    }
}

toolkitReleases {
    detectVersionType.set(true)

    modrinth {
        projectId.set("MaDESStl")
        dependencies.add(ModDependency("textile-lib", DependencyType.REQUIRED))

        if (mcData.version >= MinecraftVersions.VERSION_1_16_5) {
            val kotlinWrapperId = if (mcData.isForgeLike) "kotlin-for-forge" else "fabric-language-kotlin"
            dependencies.add(ModDependency(kotlinWrapperId, DependencyType.REQUIRED))
        }

        if (mcData.isFabric) {
            val fapiId = if (mcData.isLegacyFabric) "legacy-fabric-api" else "fabric-api"
            dependencies.add(ModDependency(fapiId, DependencyType.REQUIRED))
        }
    }

    curseforge {
        projectId.set("1215299")
        relations.add(CurseRelation("textile", CurseRelationType.REQUIRED))

        if (mcData.version >= MinecraftVersions.VERSION_1_16_5) {
            val kotlinWrapperId = if (mcData.isForgeLike) "kotlin-for-forge" else "fabric-language-kotlin"
            relations.add(CurseRelation(kotlinWrapperId, CurseRelationType.REQUIRED))
        }

        if (mcData.isFabric) {
            val fapiId = if (mcData.isLegacyFabric) "legacy-fabric-api" else "fabric-api"
            relations.add(CurseRelation(fapiId, CurseRelationType.REQUIRED))
        }
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    val textileVersion = "0.8.0"
    api(includeOrShade("dev.deftu:textile:$textileVersion")!!)
    modImplementation(includeOrShade("dev.deftu:textile-$mcData:$textileVersion")!!)

    api("com.mojang:brigadier:1.0.18")

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
