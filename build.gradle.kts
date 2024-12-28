plugins {
    kotlin("jvm") version "1.9.10" // Upewnij się, że używasz odpowiedniej wersji
    id("org.jetbrains.compose") version "1.5.10" // Jeśli używasz Jetpack Compose
    application
}

repositories {
    mavenCentral() // Repozytorium Maven
    google() // Jeśli korzystasz z Jetpack Compose
}

dependencies {
    implementation("org.jetbrains.compose.desktop:desktop-jvm:1.5.10") // Dodaj wersję
    implementation("org.jetbrains.exposed:exposed-core:0.40.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.40.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.40.1")
    implementation("org.xerial:sqlite-jdbc:3.40.1.0")
}

application {
    // Zmień to na główną klasę swojej aplikacji
    mainClass.set("man-application.kt")
}
