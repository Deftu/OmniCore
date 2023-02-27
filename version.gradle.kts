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
}

publishing.repositories {
    mavenLocal()
}
