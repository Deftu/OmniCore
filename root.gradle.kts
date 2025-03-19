plugins {
    id("dev.deftu.gradle.multiversion-root")
}

preprocess {
    "1.21.4-neoforge"(1_21_04, "srg") {
        "1.21.4-fabric"(1_21_04, "yarn") {
            "1.21.3-fabric"(1_21_04, "yarn") {
                "1.21.3-neoforge"(1_21_03, "srg") {
                    "1.21.2-neoforge"(1_21_02, "srg") {
                        "1.21.2-fabric"(1_21_02, "yarn") {
                            "1.21.1-fabric"(1_21_01, "yarn", file("versions/mappings/1.21.2-fabric+1.21.1-fabric.txt")) {
                                "1.21.1-neoforge"(1_21_01, "srg") {
                                    "1.20.6-neoforge"(1_20_06, "srg", file("versions/mappings/1.21.1-neoforge+1.20.6-neoforge.txt")) {
                                        "1.20.6-fabric"(1_20_06, "yarn") {
                                            "1.20.4-fabric"(1_20_04, "yarn") {
                                                "1.20.4-neoforge"(1_20_04, "srg") {
                                                    "1.20.4-forge"(1_20_04, "srg", file("versions/mappings/1.20.4-neoforge+1.20.4-forge.txt")) {
                                                        "1.20.1-forge"(1_20_01, "srg", file("versions/mappings/1.20.4-forge+1.20.1-forge.txt")) {
                                                            "1.20.1-fabric"(1_20_01, "yarn") {
                                                                "1.19.4-fabric"(1_19_04, "yarn") {
                                                                    "1.19.4-forge"(1_19_04, "srg") {
                                                                        "1.19.2-forge"(1_19_02, "srg", file("versions/mappings/1.19.4-forge+1.19.2-forge.txt")) {
                                                                            "1.19.2-fabric"(1_19_02, "yarn") {
                                                                                "1.18.2-fabric"(1_18_02, "yarn", file("versions/mappings/1.19.2-fabric+1.18.2-fabric.txt")) {
                                                                                    "1.18.2-forge"(1_18_02, "srg") {
                                                                                        "1.17.1-forge"(1_17_01, "srg") {
                                                                                            "1.17.1-fabric"(1_17_01, "yarn") {
                                                                                                "1.16.5-fabric"(1_16_05, "yarn", file("versions/mappings/1.17.1-fabric+1.16.5-fabric.txt")) {
                                                                                                    "1.16.5-forge"(1_16_05, "srg") {
                                                                                                        "1.12.2-forge"(1_12_02, "srg", file("versions/mappings/1.16.5-forge+1.12.2-forge.txt")) {
                                                                                                            "1.12.2-fabric"(1_12_02, "yarn") {
                                                                                                                "1.8.9-fabric"(1_08_09, "yarn", file("versions/mappings/1.12.2-fabric+1.8.9-fabric.txt")) {
                                                                                                                    "1.8.9-forge"(1_08_09, "srg")
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

val versions = listOf(
    "1.8.9-forge",
    "1.8.9-fabric",

    "1.12.2-forge",
    "1.12.2-fabric",

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

    "1.20.4-forge",
    "1.20.4-neoforge",
    "1.20.4-fabric",

    "1.20.6-neoforge",
    "1.20.6-fabric",

    "1.21.1-neoforge",
    "1.21.1-fabric",

    "1.21.2-neoforge",
    "1.21.2-fabric",

    "1.21.3-neoforge",
    "1.21.3-fabric",

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
