plugins {
    java
    kotlin("jvm")
    `maven-publish`
    id("xyz.deftu.gradle.multiversion")
    id("xyz.deftu.gradle.tools")
    id("xyz.deftu.gradle.tools.blossom")
    id("xyz.deftu.gradle.tools.minecraft.loom")
    id("xyz.deftu.gradle.tools.minecraft.api")
    id("xyz.deftu.gradle.tools.minecraft.releases")
}

toolkitLoomApi.setupTestClient()
if (mcData.isForge && mcData.version <= 1_15_02) {
    toolkitLoomHelper.useKotlinForForge()
}

dependencies {
    implementation(kotlin("reflect"))
//    modImplementation("xyz.deftu:TextCraft-${mcData.versionStr}-${mcData.loader.name}:1.0.1")

    if (mcData.isFabric) {
        modImplementation("net.fabricmc.fabric-api:fabric-api:${mcData.fabricApiVersion}")
        modImplementation("net.fabricmc:fabric-language-kotlin:${mcData.fabricLanguageKotlinVersion}")
    }
}
