plugins {
    id("com.android.application") version "9.5.1" apply false
    id("com.android.library") version "8.10.1" apply false
    id("org.jetbrains.kotlin.android") version "2.1.21" apply false
    id("org.jetbrains.kotlin.multiplatform") version "2.1.21" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.21" apply false
    id("org.jetbrains.compose") version "1.8.2" apply false
    id("org.jetbrains.kotlinx.kover") version "0.9.1"
    id("io.gitlab.arturbosch.detekt") version "1.23.8" apply false
}

dependencies {
    kover(project(":app"))
    kover(project(":shared"))
}

subprojects {
    apply(plugin = "org.jetbrains.kotlinx.kover")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        buildUponDefaultConfig = true
        config.setFrom(rootProject.files("config/detekt/detekt.yml"))
        parallel = true
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        setSource(files("src/main/java", "src/main/kotlin", "src/commonMain/kotlin", "src/androidMain/kotlin", "src/iosMain/kotlin"))
        include("**/*.kt")
        exclude("**/build/**")
        reports {
            html.required.set(true)
            xml.required.set(false)
            txt.required.set(true)
        }
    }

    tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
        setSource(files("src/main/java", "src/main/kotlin", "src/commonMain/kotlin", "src/androidMain/kotlin", "src/iosMain/kotlin"))
        include("**/*.kt")
        exclude("**/build/**")
    }
}
