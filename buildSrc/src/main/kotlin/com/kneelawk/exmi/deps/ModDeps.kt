package com.kneelawk.exmi.deps

import org.gradle.api.Project
import org.gradle.kotlin.dsl.*

open class ModDeps(private val project: Project, private val modDev: Boolean) {
    private var modrinth = false

    // Mod Dependency importers (sorted alphabetically)

    fun actuallyAdditions() {
        project.repositories {
            maven {
                name = "MiKeY"
                url = project.uri("https://maven.saps.dev/releases")
            }
            maven {
                name = "Octo Studios"
                url = project.uri("https://maven.octo-studios.com/releases")
            }
        }

        project.dependencies {
            val actually_additions_version: String by project
            mod("de.ellpeck:actuallyadditions:$actually_additions_version")
        }
    }

    fun chipped() {
        project.repositories {
            if (!modrinth) {
                maven {
                    name = "ModrinthMaven"
                    url = project.uri("https://api.modrinth.com/maven/")
                    content {
                        includeGroup("maven.modrinth")
                    }
                }
            }
        }

        project.dependencies {
            val platform = project.property("submodule.platform")
            when (platform) {
                "fabric", "xplat" -> {
                    val chipped_id_fabric: String by project
                    mod("maven.modrinth:chipped:$chipped_id_fabric")
                    val athena_id_fabric: String by project
                    mod("maven.modrinth:athena-ctm:$athena_id_fabric")
                    val resourceful_lib_id_fabric: String by project
                    mod("maven.modrinth:resourceful-lib:$resourceful_lib_id_fabric")
                }
                "neoforge" -> {
                    val chipped_id_neoforge: String by project
                    mod("maven.modrinth:chipped:$chipped_id_neoforge")
                    val athena_id_neoforge: String by project
                    mod("maven.modrinth:athena-ctm:$athena_id_neoforge")
                    val resourceful_lib_id_neoforge: String by project
                    mod("maven.modrinth:resourceful-lib:$resourceful_lib_id_neoforge")
                }
            }
        }
    }

    fun farmersDelight() {
        project.repositories {
            if (!modrinth) {
                maven {
                    name = "ModrinthMaven"
                    url = project.uri("https://api.modrinth.com/maven/")
                    content {
                        includeGroup("maven.modrinth")
                    }
                }
            }
        }

        project.dependencies {
            val farmers_delight_id: String by project
            mod("maven.modrinth:farmers-delight:$farmers_delight_id")
        }
    }

    fun techReborn() {
        project.repositories {
            maven {
                name = "CurseMaven"
                url = project.uri("https://cursemaven.com")
                content {
                    includeGroup("curse.maven")
                }
            }
        }

        project.dependencies {
            val tech_reborn_id: String by project
            mod("curse.maven:techreborn-233564:$tech_reborn_id")
            val reborn_core_id: String by project
            mod("curse.maven:reborncore-237903:$reborn_core_id")
            val energy_version: String by project
            mod("teamreborn:energy:$energy_version")
        }
    }

    fun DependencyHandlerScope.mod(dep: String) {
        if (modDev) {
            add("compileOnly", dep) {
                exclude(group = "net.fabricmc")
                exclude(group = "net.fabricmc.fabric-api")
                exclude(group = "io.github.prospector")
                exclude(group = "me.shedaniel")
                exclude(group = "mezz.jei")
            }
            add("localRuntime", dep) {
                exclude(group = "net.fabricmc")
                exclude(group = "net.fabricmc.fabric-api")
                exclude(group = "io.github.prospector")
                exclude(group = "me.shedaniel")
                exclude(group = "mezz.jei")
            }
        } else {
            add("modCompileOnly", dep) {
                exclude(group = "net.fabricmc")
                exclude(group = "net.fabricmc.fabric-api")
                exclude(group = "io.github.prospector")
                exclude(group = "me.shedaniel")
                exclude(group = "mezz.jei")
            }
            add("modLocalRuntime", dep) {
                exclude(group = "net.fabricmc")
                exclude(group = "net.fabricmc.fabric-api")
                exclude(group = "io.github.prospector")
                exclude(group = "me.shedaniel")
                exclude(group = "mezz.jei")
            }
        }
    }
}
