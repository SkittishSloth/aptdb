
plugins {
    id("aptdb.kotlin-library-conventions")
    id("io.arrow-kt.analysis.kotlin") version "2.0.2"
}

dependencies {
    api(project(":core"))
    
    implementation("org.jetbrains.xodus:dnq:2.0.0")
    
    api("org.kodein.memory:kodein-memory:0.11.0")
    implementation("org.kodein.db:kodein-db-jvm:0.8.1-beta")
    implementation("org.kodein.db:kodein-leveldb-jni-jvm-linux:0.8.1-beta")
}