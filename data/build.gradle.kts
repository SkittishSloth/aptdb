
plugins {
    id("aptdb.kotlin-library-conventions")
    id("io.arrow-kt.analysis.kotlin") version "2.0.2"
}

dependencies {
    implementation("org.jetbrains.xodus:dnq:2.0.0")
    api(project(":core"))
}