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
    compile(group = "org.postgresql", name = "postgresql", version = "42.2.5")
    compile(group = "com.github.javafaker", name = "javafaker", version = "0.16")

    implementation("com.google.code.gson:gson:2.8.5")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
}
