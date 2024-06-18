import dev.deftu.gradle.utils.VersionType

plugins {
    java
    kotlin("jvm")
    `maven-publish`
    id("dev.deftu.gradle.multiversion")
    id("dev.deftu.gradle.tools")
    id("dev.deftu.gradle.tools.resources")
    id("dev.deftu.gradle.tools.bloom")
    id("dev.deftu.gradle.tools.publishing.maven")
    id("dev.deftu.gradle.tools.minecraft.loom")
    id("dev.deftu.gradle.tools.minecraft.api")
    id("dev.deftu.gradle.tools.minecraft.releases")
}

kotlin.explicitApi()
toolkitLoomApi.setupTestClient()
toolkitMavenPublishing.forceLowercase.set(true)
if (mcData.isForge && mcData.version >= 1_15_02) {
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
    modImplementation("dev.deftu:textile-$mcData:0.3.1")

    if (mcData.isFabric) {
        modImplementation("net.fabricmc.fabric-api:fabric-api:${mcData.dependencies.fabric.fabricApiVersion}")
        modImplementation("net.fabricmc:fabric-language-kotlin:${mcData.dependencies.fabric.fabricLanguageKotlinVersion}")
    }
}

tasks.jar {
    destinationDirectory.set(rootProject.layout.buildDirectory.asFile.get().resolve("jars"))
}
