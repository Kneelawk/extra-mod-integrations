plugins {
    id("com.kneelawk.versioning")
    id("com.kneelawk.submodule")
    id("com.kneelawk.kpublish")
    id("exmi-deps")
}

submodule {
    neoforgeProjectDependency(":core", include = false)
}

kpublish {
    createPublication()
}

modDeps {
    ironsSpellsNSpellbooks()
}
