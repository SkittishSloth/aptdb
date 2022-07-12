plugins {
  id("org.jetbrains.kotlin.jvm")
  application
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("com.lordcodes.turtle:turtle:_")
  implementation("dev.dirs:directories:_")
  
  // Align versions of all Kotlin components
  implementation(platform("org.jetbrains.kotlin:kotlin-bom:_"))

  // Use the Kotlin JDK 8 standard library.
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  
  // Arrow
  implementation(platform("io.arrow-kt:arrow-stack:_"))

  implementation("io.arrow-kt:arrow-core")
  implementation("io.arrow-kt:arrow-fx-coroutines")
  
  // CLI Libraries
  implementation("com.github.ajalt.clikt:clikt:_")
  implementation("com.varabyte.kotter:kotter:_")
  
  // database stuff 
  implementation("com.h2database:h2:_")
  
  implementation("org.jetbrains.exposed:exposed-core:_")
  implementation("org.jetbrains.exposed:exposed-dao:_")
  implementation("org.jetbrains.exposed:exposed-jdbc:_")
  implementation("org.jetbrains.exposed:exposed-kotlin-datetime:_")
  
  // Dependency Injection 
  implementation(Koin.core)
  testImplementation(Koin.test)
  
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