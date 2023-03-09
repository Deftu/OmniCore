plugins {
    java
    kotlin("jvm")
    `maven-publish`
    id("xyz.deftu.gradle.multiversion")
    id("xyz.deftu.gradle.tools")
    id("xyz.deftu.gradle.tools.blossom")
    id("xyz.deftu.gradle.tools.minecraft.loom")
    id("xyz.deftu.gradle.tools.minecraft.releases")
}

loom {
    runConfigs {
        "client" {
            isIdeConfigGenerated = true
        }
    }
}

dependencies {
    implementation(kotlin("reflect"))
    modImplementation("xyz.deftu:TextCraft-${mcData.versionStr}-${mcData.loader.name}:1.0.0")
}

publishing.repositories {
    mavenLocal()
}
