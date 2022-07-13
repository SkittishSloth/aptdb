
plugins {
    id("aptdb.kotlin-library-conventions")
    id("io.arrow-kt.analysis.kotlin")
}

dependencies {
    api(project(":core"))
    
    implementation("org.jetbrains.xodus:dnq:_")
}