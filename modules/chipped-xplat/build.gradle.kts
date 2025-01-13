plugins {
    id("com.kneelawk.versioning")
    id("com.kneelawk.submodule")
    id("com.kneelawk.kpublish")
    id("exmi-deps")
}

submodule {
    xplatProjectDependency(":core", include = false)
}

kpublish {
    createPublication("intermediary")
}

modDeps {
    chipped()
}
