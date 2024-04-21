import dev.deftu.gradle.utils.VersionType

plugins {
    java
    kotlin("jvm")
    `maven-publish`
    id("dev.deftu.gradle.multiversion")
    id("dev.deftu.gradle.tools")
    id("dev.deftu.gradle.tools.resources")
    id("dev.deftu.gradle.tools.blossom")
    id("dev.deftu.gradle.tools.maven-publishing")
    id("dev.deftu.gradle.tools.minecraft.loom")
    id("dev.deftu.gradle.tools.minecraft.api")
    id("dev.deftu.gradle.tools.minecraft.releases")
}

kotlin.explicitApi()
toolkitLoomApi.setupTestClient()
if (mcData.isForge && mcData.version >= 1_15_02) {
    toolkitLoomHelper.useKotlinForForge()
}

toolkitMavenPublishing {
    artifactName.set(modData.name.lowercase())
}

toolkitReleases {
    if (modData.version.startsWith("0.")) {
        versionType.set(VersionType.BETA)
    }

    modrinth {
        projectId.set("MaDESStl")
    }
}

dependencies {
    implementation(kotlin("reflect"))
    modImplementation("dev.deftu:textile-${mcData.versionStr}-${mcData.loader.name}:0.1.0")

    if (mcData.isFabric) {
        modImplementation("net.fabricmc.fabric-api:fabric-api:${mcData.fabricApiVersion}")
        modImplementation("net.fabricmc:fabric-language-kotlin:${mcData.fabricLanguageKotlinVersion}")
    }
}
