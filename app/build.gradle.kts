plugins {
    id("aptdb.kotlin-application-conventions")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":data"))
    
    implementation("dev.dirs:directories:26")
}

application {
    // Define the main class for the application.
    mainClass.set("aptdb.app.AppKt")
}