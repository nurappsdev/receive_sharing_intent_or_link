import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.gradle.api.JavaVersion
import org.gradle.api.plugins.JavaPluginExtension

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

/**
 * Force JVM 17 for ALL subprojects (including plugins like receive_sharing_intent)
 */
subprojects {

    // ---- JAVA TOOLCHAIN (for Java / Android) ----
    plugins.withType<org.gradle.api.plugins.JavaPlugin> {
        extensions.configure<JavaPluginExtension> {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(17))
            }
        }
    }

    // ---- KOTLIN JVM ----
    plugins.withId("org.jetbrains.kotlin.jvm") {
        extensions.configure<KotlinJvmProjectExtension> {
            jvmToolchain(17)
        }
    }

    // ---- KOTLIN ANDROID ----
    plugins.withId("org.jetbrains.kotlin.android") {
        extensions.configure<KotlinAndroidProjectExtension> {
            jvmToolchain(17)
        }
    }

    // ---- FORCE KotlinCompile JVM target ----
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
}

/**
 * Flutter build directory relocation (SAFE)
 */
val newBuildDir = rootProject.layout.buildDirectory.dir("../../build").get()
rootProject.layout.buildDirectory.value(newBuildDir)

subprojects {
    val newSubprojectBuildDir = newBuildDir.dir(project.name)
    project.layout.buildDirectory.value(newSubprojectBuildDir)
    project.evaluationDependsOn(":app")
}

/**
 * Clean task
 */
tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
