import org.jetbrains.kotlin.gradle.dsl.JvmTarget

// Module de documentation : il n'est pas embarqué dans l'appli. Paparazzi y affiche les écrans
// sans téléphone ni émulateur, pour les visuels marketing (voir pilmil-core/marketing/capture.sh).
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("app.cash.paparazzi")
}

android {
    namespace = "fr.sauve.screenshots"
    compileSdk = 35
    defaultConfig { minSdk = 26 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures { compose = true }
}

kotlin {
    compilerOptions { jvmTarget.set(JvmTarget.JVM_17) }
}

dependencies {
    implementation(project(":shared"))
    testImplementation("junit:junit:4.13.2")
    // Déjà dans le graphe de :shared ; sert ici à initialiser les ressources Compose hors appareil.
    testImplementation("org.jetbrains.compose.components:components-resources:1.8.2")
}

// Trace complète en cas d'échec, lisible directement dans le journal.
tasks.withType<Test>().configureEach {
    // Un processus par classe : les libellés traduits sont figés au premier accès (une langue par JVM, comme sur un téléphone).
    forkEvery = 1
    testLogging {
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        events("failed")
    }
}
