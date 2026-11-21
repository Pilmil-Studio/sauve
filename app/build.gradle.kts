plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "fr.sauve.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "fr.sauve.app"
        minSdk = 26
        targetSdk = 36
        versionCode = System.getenv("SAUVE_VERSION_CODE")?.toIntOrNull() ?: 10000
        versionName = System.getenv("SAUVE_VERSION_NAME") ?: "1.0.0-dev"
        manifestPlaceholders["allowAutoBackup"] =
            providers.gradleProperty("pilmil.allowAutoBackup").getOrElse("false")
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("sauve-debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
        val keystore = System.getenv("RELEASE_KEYSTORE")
        if (keystore != null && file(keystore).exists()) {
            create("release") {
                storeFile = file(keystore)
                storePassword = System.getenv("RELEASE_STORE_PASSWORD")
                keyAlias = System.getenv("RELEASE_KEY_ALIAS")
                keyPassword = System.getenv("RELEASE_STORE_PASSWORD")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            // Jamais de repli sur la clé de test : voir le garde-fou plus bas.
            signingConfig = signingConfigs.findByName("release")
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

// Une variante release sans clé d'envoi échoue au lieu d'être signée avec la clé de test publique.
// Les tests unitaires et les builds debug ne sont pas concernés.
gradle.taskGraph.whenReady {
    val buildsRelease = allTasks.any { t ->
        t.project == project && Regex("^(assemble|bundle|package|sign|validateSigning).*Release$").matches(t.name)
    }
    if (buildsRelease && project.extensions.getByType<com.android.build.api.dsl.ApplicationExtension>()
            .signingConfigs.findByName("release") == null) {
        throw GradleException(
            "Signature release impossible : clé d'envoi absente. Définir les variables d'environnement " +
                "RELEASE_KEYSTORE (chemin d'un fichier existant), RELEASE_STORE_PASSWORD et RELEASE_KEY_ALIAS. " +
                "Pour un test local, utiliser assembleDebug."
        )
    }
}

dependencies {
    // Écrans, contenus et cadence du massage : communs avec l'iPhone.
    implementation(project(":shared"))
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("androidx.core:core-ktx:1.19.1")
    implementation("androidx.work:work-runtime-ktx:2.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
}
