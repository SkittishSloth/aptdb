
plugins {
    id("aptdb.kotlin-library-conventions")
}

dependencies {
    implementation("org.jetbrains.xodus:dnq:2.0.0")
    api(project(":core"))
}