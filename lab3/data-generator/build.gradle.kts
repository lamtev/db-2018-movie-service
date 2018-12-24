plugins {
    java
}

group = "com.lamtev.movie-service"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
}

dependencies {
    compile("com.intellij:annotations:12.0")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
}
