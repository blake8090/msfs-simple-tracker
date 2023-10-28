
plugins {
    kotlin("jvm") version "1.9.10"
    id("org.openjfx.javafxplugin") version "0.1.0"
    application
}

repositories {
    mavenCentral()
    maven {
        setUrl("https://esri.jfrog.io/artifactory/arcgis")
    }
}

dependencies {
    implementation("com.formdev:flatlaf:3.2.5")
    implementation("org.openjfx:javafx-swing:22-ea+11")

    implementation("com.gluonhq:maps:2.0.0-ea+6")

    runtimeOnly("com.esri.arcgisruntime:arcgis-java-jnilibs:200.2.0")
    runtimeOnly("com.esri.arcgisruntime:arcgis-java-resources:200.2.0")
}

javafx {
    version = "17.0.8"
    modules("javafx.controls", "javafx.graphics", "javafx.fxml", "javafx.web", "javafx.media")
}

application {
    mainClass.set("bke.tracker.MainKt")
}
