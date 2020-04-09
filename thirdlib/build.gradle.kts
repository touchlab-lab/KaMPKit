import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
plugins {
    kotlin("multiplatform")
    id("kotlinx-serialization")
    id("com.android.library")
    id("com.squareup.sqldelight")
}

android {
    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(Versions.min_sdk)
        targetSdkVersion(Versions.target_sdk)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

kotlin {
    android()

    val iosX64 = iosX64("iosX64")

    configure(listOf(iosX64)) {
        binaries.framework {
            baseName = "ThirdLib"
            isStatic = true

        }
    }

    val buildForDevice = project.findProperty("device") as? Boolean ?: false
    val iosTarget = if(buildForDevice) iosArm64("ios") else iosX64("ios")
    iosTarget.binaries {
        framework {
            // Disable bitcode embedding for the simulator build.
            if (!buildForDevice) {
                embedBitcode("disable")
            }
            isStatic = true
        }
    }

    targets.getByName<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>("ios").compilations["main"].kotlinOptions.freeCompilerArgs +=
        listOf("-Xobjc-generics", "-Xg0")

    version = "1.1"

    sourceSets["commonMain"].dependencies {
        implementation(kotlin("stdlib-common", Versions.kotlin))

    }

    sourceSets["commonTest"].dependencies {
    }

    sourceSets["androidMain"].dependencies {
        implementation(kotlin("stdlib", Versions.kotlin))
    }

    sourceSets["androidTest"].dependencies {
    }

    sourceSets["iosMain"].dependencies {

    }

    tasks.create("debugFatFramework", org.jetbrains.kotlin.gradle.tasks.FatFrameworkTask::class) {
        baseName = "ThirdLib"
        destinationDir = buildDir.resolve("fat-framework/debug")
        from(
            iosX64.binaries.getFramework("DEBUG")
        )
    }
}

