
plugins {
    kotlin("jvm") version "1.9.10"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.formdev:flatlaf:3.2.5")
}

application {
    mainClass.set("bke.tracker.MainKt")
}
