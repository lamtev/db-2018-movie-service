plugins {
    java
}

group = "com.lamtev.movie-service"
version = "1.0.RELEASE"

repositories {
    jcenter()
}

dependencies {
    compile("com.intellij:annotations:12.0")
    compile("org.postgresql:postgresql:42.2.5")
    compile("com.github.javafaker:javafaker:0.16")
    compile("net.sf.trove4j:trove4j:3.0.3")
    compile("com.google.code.gson:gson:2.8.5")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
}

val fatJar = task("fatJar", type = Jar::class) {
    baseName = "${project.group}.${project.name}"
    manifest {
        attributes["Implementation-Title"] = "Movie service data generator"
        attributes["Implementation-Version"] = version
        attributes["Main-Class"] = "com.lamtev.movie_service.datagen.Launcher"
    }
    from(configurations["compile"].map { if (it.isDirectory) it else zipTree(it) })
    with(tasks["jar"] as CopySpec)
}

tasks {
    "build" {
        dependsOn(fatJar)
    }
}
