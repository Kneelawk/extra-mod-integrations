package com.kneelawk.exmi.deps

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

class ModDepsPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val platform = project.property("submodule.platform") as String
        val modDev = when (platform) {
            "xplat", "fabric", "mojmap" -> false
            "neoforge" -> true
            else -> throw IllegalArgumentException("Unrecognized submodule platform $platform")
        }

        project.extensions.create("modDeps", ModDeps::class, project, modDev)
    }
}