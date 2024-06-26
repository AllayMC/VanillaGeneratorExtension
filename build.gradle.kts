plugins {
    java
    `maven-publish`
    application
    id("io.github.goooler.shadow") version "8.1.7"
}

project.group = "org.allaymc"
project.version = "0.0.1"
val mainClass = "io.papermc.paperclip.Main"
project.setProperty("mainClassName", mainClass)

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.opencollab.dev/maven-releases/")
    maven("https://repo.opencollab.dev/maven-snapshots/")
}

dependencies {
    implementation("io.sigpipe:jbsdiff:1.0")
    compileOnly("com.github.PowerNukkitX:PowerNukkitX:dcda360364")
}

val isSnapshot = project.version.toString().endsWith("-SNAPSHOT")

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    withSourcesJar()
}

tasks.wrapper {
    version = 8.7
}

tasks.shadowJar {
    val prefix = "paperclip.libs"
    listOf("org.apache", "org.tukaani", "io.sigpipe").forEach { pack ->
        relocate(pack, "$prefix.$pack")
    }

    exclude("META-INF/LICENSE.txt")
    exclude("META-INF/NOTICE.txt")
    archiveClassifier.set("")
}

tasks.distTar {
    enabled = false
}

tasks.startScripts {
    enabled = false
}

tasks.distZip {
    enabled = false
}

tasks.startShadowScripts {
    enabled = false
}

tasks.shadowDistTar {
    enabled = false
}

tasks.shadowDistZip {
    enabled = false
}

val sourcesJar = tasks.named("sourcesJar");

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.register("printVersion") {
    doFirst {
        println(version)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            from(components["java"])
        }
    }
}

