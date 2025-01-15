plugins {
    id("com.kneelawk.versioning")
    id("com.kneelawk.submodule")
    id("com.kneelawk.kpublish")
    id("exmi-deps")
}

submodule {
    applyXplatConnection(":rechiseled-xplat")
}

kpublish {
    createPublication()
}

modDeps {
    rechiseled()
}
