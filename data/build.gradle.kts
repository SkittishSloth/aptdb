
plugins {
    id("aptdb.kotlin-library-conventions")
    id("io.arrow-kt.analysis.kotlin") version "2.0.2"
}

dependencies {
    api(project(":core"))
    
    implementation("com.arcadedb:arcadedb-engine:21.10.1")
}