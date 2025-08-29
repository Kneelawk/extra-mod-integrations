plugins {
    id("com.kneelawk.versioning")
    id("com.kneelawk.submodule")
    id("com.kneelawk.kpublish")
}

submodule {
    val emi_version: String by project
    xplatExternalDependency(include = false) { "dev.emi:emi-$it:$emi_version" }
}

kpublish {
    createPublication("intermediary")
}
