import com.matthewprenger.cursegradle.CurseProject
import com.matthewprenger.cursegradle.CurseRelation
import com.matthewprenger.cursegradle.CurseUploadTask
import com.matthewprenger.cursegradle.Options

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
    fabricProjectDependency(":core")
    generateRuns()
}

kpublish {
    createPublication()
}

// enabled variables (sorted alphabetically)
val tech_reborn_enabled: String by project

modDeps {
    // mod dependencies (sorted alphabetically)
    if (tech_reborn_enabled.toBoolean()) {
        techReborn()
    }
}

dependencies {
    // integration project dependencies (sorted alphabetically)
    if (tech_reborn_enabled.toBoolean()) {
        implementation(project(":tech-reborn-fabric", configuration = "namedElements"))
        include(project(":tech-reborn-fabric"))
    }

    // non-integration mods
    val mod_menu_version: String by project
    modLocalRuntime("com.terraformersmc:modmenu:$mod_menu_version") {
        exclude(group = "net.fabricmc")
        exclude(group = "net.fabricmc.fabric-api")
    }
}

val regex = Regex("""\s*,\s*""")

modrinth {
    token = System.getenv("MODRINTH_TOKEN")
    val mrProjectId: String by project
    projectId.set(mrProjectId)
    versionNumber.set(project.version.toString())
    val mrVersionType: String by project
    versionType.set(mrVersionType)
    val file = rootProject.file("changelogs/changelog-v${project.version}.md")
    if (file.exists()) {
        changelog.set(file.readText())
    }
    uploadFile.set(tasks.remapJar)
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
            mainArtifact(tasks.remapJar)
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
            rootProject.file("curse-file-id.txt").writeText(mainArtifact.fileID.toString())
        }
    }
}
