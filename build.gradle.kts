import dev.deftu.gradle.utils.MinecraftVersion

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
if (mcData.isForgeLike && mcData.version >= MinecraftVersion.VERSION_1_16_5) {
    toolkitLoomHelper.useKotlinForForge()
}

toolkitReleases {
    detectVersionType.set(true)
    modrinth {
        projectId.set("MaDESStl")
    }
}

dependencies {
    implementation(kotlin("reflect"))

    val textileVersion = "0.5.1"
    api("dev.deftu:textile:$textileVersion")
    modImplementation("dev.deftu:textile-$mcData:$textileVersion")

    if (mcData.isFabric) {
        modImplementation("net.fabricmc.fabric-api:fabric-api:${mcData.dependencies.fabric.fabricApiVersion}")
        modImplementation("net.fabricmc:fabric-language-kotlin:${mcData.dependencies.fabric.fabricLanguageKotlinVersion}")
    }
}
