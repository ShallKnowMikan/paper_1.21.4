plugins {
    id("java")
    id ("com.gradleup.shadow") version "8.3.0"
    kotlin("jvm") version "2.2.0"
}

group = "dev.mikan"
version = "1.0"

val outputDir = file("/home/mikan/Desktop/builds")

tasks.register<Copy>("copy"){
    dependsOn(tasks.named("jar"))
    from(tasks.named("jar").get().outputs.files)
    into(outputDir)
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>(){
    archiveClassifier.set("")
    relocate("dev.mikan.altairkit", "dev.mikan.shaded.altairkit")
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(21)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenLocal()
    mavenCentral()
    flatDir {
        dirs ("/home/mikan/.m2/repository/dev/mikan/AltairKit/1.21.4")
    }

    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.6-R0.1-SNAPSHOT")

    compileOnly(kotlin("stdlib-jdk8"))
    compileOnly(kotlin("reflect"))

    implementation("dev.mikan:AltairKit:1.21.4")
}

tasks.named("build"){
    dependsOn(tasks.named("shadowJar"))
    finalizedBy("copy")

}
