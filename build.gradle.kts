plugins {
    id("xyz.deftu.gradle.multiversion-root")
}

preprocess {
    val fabric11903 = createNode("1.19.3-fabric", 11903, "yarn")
    val fabric11902 = createNode("1.19.2-fabric", 11902, "yarn")
    val fabric11802 = createNode("1.18.2-fabric", 11802, "yarn")
    val fabric11701 = createNode("1.17.1-fabric", 11701, "yarn")
    val fabric11605 = createNode("1.16.5-fabric", 11605, "yarn")
    val fabric11404 = createNode("1.14.4-fabric", 11404, "yarn")
    val forge11404 = createNode("1.14.4-forge", 11404, "srg")
    val forge11202 = createNode("1.12.2-forge", 11202, "srg")
    val forge10809 = createNode("1.8.9-forge", 10809, "srg")

    fabric11903.link(fabric11902)
    fabric11902.link(fabric11802)
    fabric11802.link(fabric11701)
    fabric11701.link(fabric11605)
    fabric11605.link(fabric11404)
    fabric11404.link(forge11404)
    forge11404.link(forge11202)
    forge11202.link(forge10809)
}

listOf(
    "MavenLocalRepository",
    "DeftuReleasesRepository",
    "DeftuSnapshotsRepository"
).forEach { repo ->
    tasks.register("publishAllVersionsTo$repo") {
        group = "publishing"

        listOf(
            "1.19.3-fabric",
            "1.19.2-fabric",
            "1.18.2-fabric",
            "1.17.1-fabric",
            "1.16.5-fabric",
            "1.12.2-forge",
            "1.8.9-forge"
        ).forEach { version ->
            dependsOn(":$version:publishAllPublicationsTo$repo")
        }
    }
}
