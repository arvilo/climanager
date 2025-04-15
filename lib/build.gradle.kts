plugins {
    `java-library`
}

group = "az.arvilo"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    api("org.apache.commons:commons-math3:3.6.1")
    implementation("com.google.guava:guava:32.1.2-jre")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

