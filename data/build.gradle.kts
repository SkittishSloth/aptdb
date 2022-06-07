
plugins {
    id("aptdb.kotlin-library-conventions")
    id("io.arrow-kt.analysis.kotlin")
}

dependencies {
    api(project(":core"))
    
    implementation("org.jetbrains.xodus:dnq:_")
    
    api("org.kodein.memory:kodein-memory:_")
    implementation("org.kodein.db:kodein-db-jvm:_")
    implementation("org.kodein.db:kodein-leveldb-jni-jvm-linux:_")
}