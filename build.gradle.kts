plugins {
    java
    application
    `maven-publish`
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
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
}

val mainClass = "io.papermc.paperclip.Main"

tasks.jar {
    val java17Jar = project(":java17").tasks.named("shadowJar")
    dependsOn(java17Jar)

    from(zipTree(java17Jar.map { it.outputs.files.singleFile }))

    manifest {
        attributes(
            "Main-Class" to mainClass
        )
    }

    from(file("license.txt")) {
        into("META-INF/license")
        rename { "paperclip-LICENSE.txt" }
    }
    rename { name ->
        if (name.endsWith("-LICENSE.txt")) {
            "META-INF/license/$name"
        } else {
            name
        }
    }
}

val sourcesJar by tasks.registering(Jar::class) {
    val java17Sources = project(":java17").tasks.named("sourcesJar")
    dependsOn(java17Sources)
    from(zipTree(java17Sources.map { it.outputs.files.singleFile }))
    archiveClassifier.set("sources")
}

val isSnapshot = project.version.toString().endsWith("-SNAPSHOT")

tasks.register("printVersion") {
    doFirst {
        println(version)
    }
}
