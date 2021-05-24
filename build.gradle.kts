import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
  java
  application
  id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "com.example"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val vertxVersion = "4.0.3"
val junitJupiterVersion = "5.7.0"

val mainVerticleName = "com.example.starter.MainVerticle"
val mainClassName = "com.example.starter.Main"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
  mainClassName = launcherClassName
}

dependencies {
  compile("org.hamcrest:hamcrest:2.2")
  compile("org.slf4j:slf4j-api:1.7.25")
  testCompile("io.cucumber:cucumber-java:6.8.1")
  testCompile("io.cucumber:cucumber-junit:6.8.1")
  compile("io.vertx:vertx-redis-client:4.0.3")
  compile("io.vertx:vertx-mongo-client:4.0.3")
  compile("io.vertx:vertx-rx-java2:4.0.3")
  compile(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  compile("io.vertx:vertx-web")
  testCompile("io.vertx:vertx-junit5")
  testCompile("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles()
}

val test = tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  args = listOf("run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
}

sourceSets.create("acceptance") {
  java.srcDir(file("src/acceptance/java"))
  resources.srcDir(file("src/acceptance/resources"))
  compileClasspath += sourceSets.main.get().output + sourceSets.test.get().output + configurations.testRuntime
  runtimeClasspath += output + compileClasspath
}
tasks.register<Test>("acceptance") {
  description = "Runs the acceptance tests"
  group = "verification"
  testClassesDirs = sourceSets.getByName("acceptance").output.classesDirs
  classpath = sourceSets.getByName("acceptance").runtimeClasspath
  dependsOn(test)
}
tasks.check {
  dependsOn(tasks.getByName("acceptance"))
}
