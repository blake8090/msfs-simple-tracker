
plugins {
    kotlin("jvm") version "1.9.20"
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("io.ktor.plugin") version "2.3.5"
    application
}

repositories {
    mavenCentral()
    maven {
        setUrl("https://esri.jfrog.io/artifactory/arcgis")
    }
    maven {
        setUrl("https://olympus.esri.com/artifactory/arcgisruntime-repo")
    }
}

val arcgisVersion by extra { "200.4.0" }

val natives = configurations.create("natives")

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.7.3")

    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-network")

    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation("org.slf4j:slf4j-api:2.0.7")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

    implementation("com.formdev:flatlaf:3.2.5")
    implementation("org.openjfx:javafx-swing:22-ea+11")

    implementation("com.esri.arcgisruntime:arcgis-java:$arcgisVersion")
    natives("com.esri.arcgisruntime:arcgis-java-jnilibs:$arcgisVersion")
    natives("com.esri.arcgisruntime:arcgis-java-resources:$arcgisVersion")
}

javafx {
    version = "17.0.8"
    modules("javafx.controls", "javafx.graphics", "javafx.fxml", "javafx.web", "javafx.media")
}

application {
    mainClass.set("bke.tracker.MainKt")
}

task<Copy>("copyNatives") {
    description = "Copies the arcgis native libraries into the project build directory for development."
    group = "build"
    val directory = "${System.getProperty("user.home")}/.arcgis/$arcgisVersion"
    for (file in configurations.getByName("natives").asFileTree) {
        println("unzipping file '${file.name}' into '$directory'")
        from(zipTree(file))
        into(directory)
    }
}
