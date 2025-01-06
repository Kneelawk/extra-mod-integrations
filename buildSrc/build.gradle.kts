plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("exmi-deps") {
            id = "exmi-deps"
            implementationClass = "com.kneelawk.exmi.deps.ModDepsPlugin"
        }
    }
}
