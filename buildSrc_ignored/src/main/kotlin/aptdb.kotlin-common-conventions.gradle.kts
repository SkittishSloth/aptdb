plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm")
}

repositories {
  mavenCentral()
}

val kotestVersion: String by extra("5.2.3")

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.6.20"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    
    // Arrow
    implementation(platform("io.arrow-kt:arrow-stack:1.0.1"))

    implementation("io.arrow-kt:arrow-core")
    implementation("io.arrow-kt:arrow-fx-coroutines")
    
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
    }
}

tasks.withType<JavaCompile> {
  this.sourceCompatibility = "11"
  this.targetCompatibility = "11"
}

testing {
    suites {
        // Configure the built-in test suite
        val test by getting(JvmTestSuite::class) {
            // Use JUnit Jupiter test framework
            useJUnitJupiter("5.8.1")
        }
    }
}

tasks.withType<Test> {
    this.testLogging {
        outputs.upToDateWhen { false }
        this.showStandardStreams = true
    }
}