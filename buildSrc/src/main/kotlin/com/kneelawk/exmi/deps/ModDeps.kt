package com.kneelawk.exmi.deps

import org.gradle.api.Project
import org.gradle.kotlin.dsl.*

open class ModDeps(private val project: Project, private val modDev: Boolean) {
    fun techReborn() {
        project.repositories {
            maven {
                // TechReborn
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