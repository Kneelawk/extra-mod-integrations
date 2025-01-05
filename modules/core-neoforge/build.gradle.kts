plugins {
    id("com.kneelawk.versioning")
    id("com.kneelawk.submodule")
    id("com.kneelawk.kpublish")
}

submodule {
    applyXplatConnection(":core-xplat")
}

kpublish {
    createPublication()
}