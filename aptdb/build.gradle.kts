plugins {
  id("org.jetbrains.kotlin.jvm")
  application
}

repositories {
  mavenCentral()
}

val kotestVersion: String by extra("5.2.3")

dependencies {
  implementation("com.lordcodes.turtle:turtle:_")
  implementation("dev.dirs:directories:26")
  
  // Align versions of all Kotlin components
  implementation(platform("org.jetbrains.kotlin:kotlin-bom:_"))

  // Use the Kotlin JDK 8 standard library.
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  
  // Arrow
  implementation(platform("io.arrow-kt:arrow-stack:_"))

  implementation("io.arrow-kt:arrow-core")
  implementation("io.arrow-kt:arrow-fx-coroutines")
  
  testImplementation(Testing.kotest.runner.junit5)
  testImplementation(Testing.kotest.assertions.core)
  testImplementation(Testing.kotest.property)
  testImplementation("io.kotest.extensions:kotest-assertions-arrow:_")
  
  testImplementation(Testing.strikt.core)
  testImplementation(Testing.strikt.mockk)
  testImplementation(Testing.strikt.arrow)

  testImplementation(Testing.mockK)
  
}

application {
  mainClass.set("aptdb.AppKt")
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