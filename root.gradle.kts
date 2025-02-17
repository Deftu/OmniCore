plugins {
    id("dev.deftu.gradle.multiversion-root")
}

preprocess {
    val neoforge_1_21_4 = createNode("1.21.4-neoforge", 1_21_04, "srg")
    val fabric_1_21_4 = createNode("1.21.4-fabric", 1_21_04, "yarn")

    val neoforge_1_21_1 = createNode("1.21.1-neoforge", 1_21_01, "srg")
    val fabric_1_21_1 = createNode("1.21.1-fabric", 1_21_01, "yarn")

    val neoforge_1_20_06 = createNode("1.20.6-neoforge", 1_20_06, "srg")
    val fabric_1_20_06 = createNode("1.20.6-fabric", 1_20_06, "yarn")

    val forge_1_20_04 = createNode("1.20.4-forge", 1_20_04, "srg")
    val neoforge_1_20_04 = createNode("1.20.4-neoforge", 1_20_04, "srg")
    val fabric_1_20_04 = createNode("1.20.4-fabric", 1_20_04, "yarn")

    val forge_1_20_02 = createNode("1.20.2-forge", 1_20_02, "srg")
    val neoforge_1_20_02 = createNode("1.20.2-neoforge", 1_20_02, "srg")
    val fabric_1_20_02 = createNode("1.20.2-fabric", 1_20_02, "yarn")

    val forge_1_20_01 = createNode("1.20.1-forge", 1_20_01, "srg")
    val fabric_1_20_01 = createNode("1.20.1-fabric", 1_20_01, "yarn")

    val forge_1_19_04 = createNode("1.19.4-forge", 1_19_04, "srg")
    val fabric_1_19_04 = createNode("1.19.4-fabric", 1_19_04, "yarn")

    val forge_1_19_02 = createNode("1.19.2-forge", 1_19_02, "srg")
    val fabric_1_19_02 = createNode("1.19.2-fabric", 1_19_02, "yarn")

    val forge_1_18_02 = createNode("1.18.2-forge", 1_18_02, "srg")
    val fabric_1_18_02 = createNode("1.18.2-fabric", 1_18_02, "yarn")

    val forge_1_17_01 = createNode("1.17.1-forge", 1_17_01, "srg")
    val fabric_1_17_01 = createNode("1.17.1-fabric", 1_17_01, "yarn")

    val forge_1_16_05 = createNode("1.16.5-forge", 1_16_05, "srg")
    val fabric_1_16_05 = createNode("1.16.5-fabric", 1_16_05, "yarn")

    val forge_1_12_02 = createNode("1.12.2-forge", 1_12_02, "srg")
    val fabric_1_12_02 = createNode("1.12.2-fabric", 1_12_02, "yarn")

    val forge_1_08_09 = createNode("1.8.9-forge", 1_08_09, "srg")
    val fabric_1_08_09 = createNode("1.8.9-fabric", 1_08_09, "yarn")

    neoforge_1_21_4.link(fabric_1_21_4, file("versions/1.21.4-neoforge+1.21.4-fabric.txt"))
    fabric_1_21_4.link(fabric_1_21_1, file("versions/1.21.1-fabric+1.21.4-fabric.txt"))
    fabric_1_21_1.link(neoforge_1_21_1)
    fabric_1_21_1.link(fabric_1_20_06, file("versions/1.21-fabric+1.20.6-fabric.txt"))
    fabric_1_20_06.link(neoforge_1_20_06, file("versions/1.20.6-fabric+1.20.6-neoforge.txt"))
    fabric_1_20_06.link(fabric_1_20_04)
    forge_1_20_04.link(fabric_1_20_04)
    neoforge_1_20_04.link(fabric_1_20_04)
    fabric_1_20_04.link(fabric_1_20_02)
    forge_1_20_02.link(fabric_1_20_02)
    neoforge_1_20_02.link(fabric_1_20_02)
    fabric_1_20_02.link(fabric_1_20_01)
    forge_1_20_01.link(fabric_1_20_01)
    fabric_1_20_01.link(fabric_1_19_04)
    forge_1_19_04.link(fabric_1_19_04)
    fabric_1_19_04.link(fabric_1_19_02, file("versions/1.19.4-fabric+1.19.2-fabric.txt"))
    forge_1_19_02.link(fabric_1_19_02)
    fabric_1_19_02.link(fabric_1_18_02, file("versions/1.19.2-fabric+1.18.2-fabric.txt"))
    forge_1_18_02.link(fabric_1_18_02)
    fabric_1_18_02.link(fabric_1_17_01)
    forge_1_17_01.link(fabric_1_17_01)
    fabric_1_17_01.link(fabric_1_16_05, file("versions/1.17.1-fabric+1.16.5-fabric.txt"))
    fabric_1_16_05.link(forge_1_16_05)
    forge_1_16_05.link(forge_1_12_02, file("versions/1.16.5-forge+1.12.2-forge.txt"))
    forge_1_12_02.link(fabric_1_12_02)
    fabric_1_12_02.link(fabric_1_08_09, file("versions/1.12.2-fabric+1.8.9-fabric.txt"))
    fabric_1_08_09.link(forge_1_08_09)
}

val versions = listOf(
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

    "1.19.4-forge",
    "1.19.4-fabric",

    "1.20.1-forge",
    "1.20.1-fabric",

    "1.20.2-forge",
    "1.20.2-neoforge",
    "1.20.2-fabric",

    "1.20.4-forge",
    "1.20.4-neoforge",
    "1.20.4-fabric",

    "1.20.6-neoforge",
    "1.20.6-fabric",

    "1.21.1-neoforge",
    "1.21.1-fabric",

    "1.21.4-neoforge",
    "1.21.4-fabric"
)

val buildVersions by tasks.creating {
    group = "deftu"
    dependsOn(versions.map { ":$it:build" })
}

listOf(
    "DeftuReleasesRepository",
    "DeftuSnapshotsRepository",
    "MavenLocalRepository"
).forEach { repository ->
    versions.forEach { version ->
        project(":$version").tasks.register("fullReleaseWith$repository") {
            group = "deftu"

            dependsOn(":$version:publishAllPublicationsTo$repository")
            dependsOn(":$version:publishMod")
        }
    }

    project.tasks.register("fullReleaseWith$repository") {
        group = "deftu"

        dependsOn(versions.map { ":$it:fullReleaseWith$repository" })
    }
}
