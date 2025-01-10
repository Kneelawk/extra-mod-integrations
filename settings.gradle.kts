pluginManagement {
    repositories {
        maven("https://maven.quiltmc.org/repository/release") {
            name = "Quilt"
        }
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        maven("https://maven.architectury.dev/") {
            name = "Architectury"
        }
        maven("https://maven.neoforged.net/releases/") {
            name = "NeoForged"
        }
        maven("https://maven.kneelawk.com/releases/") {
            name = "Kneelawk"
        }
        gradlePluginPortal()
    }
    plugins {
        val loom_version: String by settings
        id("fabric-loom") version loom_version
        val moddev_version: String by settings
        id("net.neoforged.moddev") version moddev_version
        val remapcheck_version: String by settings
        id("com.kneelawk.remapcheck") version remapcheck_version
        val versioning_version: String by settings
        id("com.kneelawk.versioning") version versioning_version
        val kpublish_version: String by settings
        id("com.kneelawk.kpublish") version kpublish_version
        val submodule_version: String by settings
        id("com.kneelawk.submodule") version submodule_version
        val minotaur_version: String by settings
        id("com.modrinth.minotaur") version minotaur_version
        val curse_gradle_version: String by settings
        id("com.matthewprenger.cursegradle") version curse_gradle_version
    }
}

rootProject.name = "extra-mod-integrations"

fun module(enabled: Boolean, name: String) {
    if (!enabled) return
    include(name)
    project(":$name").projectDir = File(rootDir, "modules/${name.replace(':', '/')}")
}

fun module(name: String, vararg submodules: Pair<Boolean, String>) {
    include(name)
    project(":$name").projectDir = File(rootDir, "modules/$name")

    for ((enabled, submodule) in submodules) {
        if (!enabled) continue
        include("$name:$submodule")
        project(":$name:$submodule").projectDir = File(rootDir, "modules/$name/${submodule.replace(':', '/')}")
    }
}

val xplat = true
val mojmap = true
val fabric = true
val neoforge = true

module(xplat, "core-xplat")
module(mojmap, "core-xplat-mojmap")
module(fabric, "core-fabric")
module(neoforge, "core-neoforge")

// Actually Additions
val actually_additions_enabled: String by settings
module(neoforge && actually_additions_enabled.toBoolean(), "actually-additions-neoforge")

// Tech Reborn
val tech_reborn_enabled: String by settings
module(fabric && tech_reborn_enabled.toBoolean(), "tech-reborn-fabric")

// Final artifacts
module(fabric, "all-fabric")
module(neoforge, "all-neoforge")