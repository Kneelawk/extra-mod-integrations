import com.matthewprenger.cursegradle.*

plugins {
    id("com.kneelawk.versioning")
    id("com.kneelawk.submodule")
    id("com.kneelawk.kpublish")
    id("com.modrinth.minotaur")
    id("com.matthewprenger.cursegradle")
    id("exmi-deps")
}

submodule {
    setLibsDirectory()
    neoforgeProjectDependency(":core")
    generateRuns()
}

kpublish {
    createPublication()
}

// enabled variables (sorted alphabetically)
val actually_additions_enabled: String by project
val farmers_delight_enabled: String by project

modDeps {
    // mod dependencies (sorted alphabetically)
    if (actually_additions_enabled.toBoolean()) {
        actuallyAdditions()
    }
    if (farmers_delight_enabled.toBoolean()) {
        farmersDelight()
    }
}

dependencies {
    // integration project dependencies (sorted alphabetically)
    if (actually_additions_enabled.toBoolean()) {
        implementation(project(":actually-additions-neoforge"))
        jarJar(project(":actually-additions-neoforge"))
    }
    if (farmers_delight_enabled.toBoolean()) {
        implementation(project(":farmers-delight-neoforge"))
        jarJar(project(":farmers-delight-neoforge"))
    }
}

val regex = Regex("""\s*,\s*""")

modrinth {
    token = System.getenv("MODRINTH_TOKEN")
    val mrProjectId: String by project
    projectId.set(mrProjectId)
    val version_extra: String by project
    versionNumber.set(project.version.toString() + "." + version_extra)
    val publish_display_name: String by project
    versionName.set("$publish_display_name $version_extra ${project.version}")
    val mrVersionType: String by project
    versionType.set(mrVersionType)
    val file = rootProject.file("changelogs/changelog-v${project.version}.md")
    if (file.exists()) {
        changelog.set(file.readText())
    }
    uploadFile.set(tasks.jar)
    additionalFiles.set(listOf(tasks.sourcesJar))
    val mrGameVersions: String by project
    gameVersions.set(mrGameVersions.split(regex))
    val mrLoaders: String by project
    loaders.set(mrLoaders.split(regex))
    dependencies {
        val mrDependencies: String by project
        for (projectId in mrDependencies.split(regex)) {
            required.project(projectId)
        }
    }
    syncBodyFrom.set(rootProject.file("README.md").readText())
}

val curseApiKey = System.getenv("CURSE_API_KEY")
if (curseApiKey != null) {
    curseforge {
        apiKey = curseApiKey
        project(closureOf<CurseProject> {
            val cfProjectId: String by project
            id = cfProjectId
            changelogType = "markdown"
            changelog = rootProject.file("changelogs/changelog-v${project.version}.md")
            val cfReleaseType: String by project
            releaseType = cfReleaseType
            val cfMinecraftVersions: String by project
            for (version in cfMinecraftVersions.split(regex)) {
                addGameVersion(version)
            }
            mainArtifact(tasks.jar, closureOf<CurseArtifact> {
                val publish_display_name: String by project
                val version_extra: String by project
                displayName = "$publish_display_name $version_extra ${project.version}"
            })
            addArtifact(tasks.sourcesJar)
            relations(closureOf<CurseRelation> {
                val cfDependencies: String by project
                for (dependency in cfDependencies.split(regex)) {
                    requiredDependency(dependency)
                }
            })
        })
        options(closureOf<Options> {
            forgeGradleIntegration = false
        })
    }
    tasks.named<CurseUploadTask>("curseforge739970") {
        doLast {
            val version_extra: String by project
            rootProject.file("curse-file-id-$version_extra.txt").writeText(mainArtifact.fileID.toString())
        }
    }
}
