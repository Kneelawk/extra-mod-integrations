package com.kneelawk.exmi.deps

import org.gradle.api.Project
import org.gradle.kotlin.dsl.*

open class ModDeps(private val project: Project, private val modDev: Boolean) {
    private var modrinth = false

    // JEI compile-time for figuring out how things were using JEI
    fun jei() {
        project.repositories {
            maven {
                name = "BlameJared"
                url = project.uri("https://maven.blamejared.com/")
            }
        }

        project.dependencies {
            val platform = project.property("submodule.platform")
            val jei_version: String by project
            val jei_mc_version: String by project
            when (platform) {
                "xplat" -> {
                    compileMod("mezz.jei:jei-$jei_mc_version-common-api-intermediary:$jei_version")
                }
                "fabric" -> {
                    compileMod("mezz.jei:jei-$jei_mc_version-fabric-api:$jei_version")
                }
                "neoforge" -> {
                    compileMod("mezz.jei:jei-$jei_mc_version-neoforge-api:$jei_version")
                }
            }
        }
    }

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
            maven {
                name = "Team Resourceful"
                url = project.uri("https://maven.resourcefulbees.com/repository/maven-public/")
            }
        }

        project.dependencies {
            val platform = project.property("submodule.platform")
            val resourceful_lib_version: String by project
            val resourceful_lib_minecraft: String by project
            when (platform) {
                "fabric", "xplat" -> {
                    val chipped_id_fabric: String by project
                    mod("maven.modrinth:chipped:$chipped_id_fabric")
                    val athena_id_fabric: String by project
                    mod("maven.modrinth:athena-ctm:$athena_id_fabric")
                    mod("com.teamresourceful.resourcefullib:resourcefullib-fabric-$resourceful_lib_minecraft:$resourceful_lib_version")
                }
                "neoforge" -> {
                    val chipped_id_neoforge: String by project
                    mod("maven.modrinth:chipped:$chipped_id_neoforge")
                    val athena_id_neoforge: String by project
                    mod("maven.modrinth:athena-ctm:$athena_id_neoforge")
                    mod("com.teamresourceful.resourcefullib:resourcefullib-neoforge-$resourceful_lib_minecraft:$resourceful_lib_version")
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

    fun ironsSpellsNSpellbooks() {
        project.repositories {
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
                maven {
                    name = "GeckoLib"
                    url = project.uri("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
                    content {
                        includeGroup("software.bernie.geckolib")
                    }
                }
                maven {
                    name = "KosmX's Maven"
                    url = project.uri("https://maven.kosmx.dev/")
                }
                maven {
                    name = "Illusive Soulworks Maven"
                    url = project.uri("https://maven.theillusivec4.top/")
                }
            }
        }

        project.dependencies {
            val isns_id: String by project
            mod("maven.modrinth:irons-spells-n-spellbooks:$isns_id")
            val geckolib_version: String by project
            val geckolib_mc_version: String by project
            mod("software.bernie.geckolib:geckolib-neoforge-$geckolib_mc_version:$geckolib_version")
            val playeranimator_version: String by project
            mod("dev.kosmx.player-anim:player-animation-lib-forge:$playeranimator_version")
            val curios_version: String by project
            mod("top.theillusivec4.curios:curios-neoforge:$curios_version")
        }
    }

    fun pneumaticCraft() {
        project.repositories {
            maven {
                name = "ModMaven"
                url = project.uri("https://modmaven.dev/artifactory/local-releases/")
            }
        }

        project.dependencies {
            val pneumaticcraft_version: String by project
            mod("me.desht.pneumaticcraft:pneumaticcraft-repressurized:$pneumaticcraft_version")
        }
    }

    fun rechiseled() {
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
                    val rechiseled_id_fabric: String by project
                    mod("maven.modrinth:rechiseled:$rechiseled_id_fabric")
                    val fusion_id_fabric: String by project
                    mod("maven.modrinth:fusion-connected-textures:$fusion_id_fabric")
                    val sm_config_id_fabric: String by project
                    mod("maven.modrinth:supermartijn642s-config-lib:$sm_config_id_fabric")
                    val sm_core_id_fabric: String by project
                    mod("maven.modrinth:supermartijn642s-core-lib:$sm_core_id_fabric")
                }
                "neoforge" -> {
                    val rechiseled_id_neoforge: String by project
                    mod("maven.modrinth:rechiseled:$rechiseled_id_neoforge")
                    val fusion_id_neoforge: String by project
                    mod("maven.modrinth:fusion-connected-textures:$fusion_id_neoforge")
                    val sm_config_id_neoforge: String by project
                    mod("maven.modrinth:supermartijn642s-config-lib:$sm_config_id_neoforge")
                    val sm_core_id_neoforge: String by project
                    mod("maven.modrinth:supermartijn642s-core-lib:$sm_core_id_neoforge")
                }
            }
        }
    }

    fun reliquary() {
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
            val reliquary_id: String by project
            mod("maven.modrinth:reliquary-reincarnations:$reliquary_id")
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

    fun DependencyHandlerScope.compileMod(dep: String) {
        if (modDev) {
            add("compileOnly", dep) {
                exclude(group = "net.fabricmc")
                exclude(group = "net.fabricmc.fabric-api")
                exclude(group = "io.github.prospector")
                exclude(group = "me.shedaniel")
            }
        } else {
            add("modCompileOnly", dep) {
                exclude(group = "net.fabricmc")
                exclude(group = "net.fabricmc.fabric-api")
                exclude(group = "io.github.prospector")
                exclude(group = "me.shedaniel")
            }
        }
    }
}
