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

    /*
    //Revert to just ios() when gradle plugin can properly resolve it
    val onPhone = System.getenv("SDK_NAME")?.startsWith("iphoneos")?:false
    if(onPhone){
        iosArm64("ios")
    }else{
        iosX64("ios")
    }

    // Create and configure the targets.
    val ios32 = iosArm32("ios32")
    val ios64 = iosArm64("ios64")
    val iosX64 = iosX64("iosX64")

    configure(listOf(ios32, ios64, iosX64)) {
        binaries.framework {
            baseName = "SecondLib"
            isStatic = true

        }
    }*/

    val buildForDevice = project.findProperty("device") as? Boolean ?: false
    val iosTarget = if(buildForDevice) iosArm64("ios") else iosX64("ios")
    iosTarget.binaries {
        framework {
            // Disable bitcode embedding for the simulator build.
            if (!buildForDevice) {
                embedBitcode("disable")
            }
        }
    }

    targets.getByName<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>("ios").compilations["main"].kotlinOptions.freeCompilerArgs +=
        listOf("-Xobjc-generics", "-Xg0")

    version = "1.1"

    sourceSets {
        all {
            languageSettings.apply {
                useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }
    }

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
/*
    cocoapodsext {
        summary = "Common library for the KaMP starter kit"
        homepage = "https://github.com/touchlab/KaMPStarter"
        isStatic = true
        frameworkName = "ThirdLib"
    }
*/

    /*
    tasks.create("debugFatFramework", org.jetbrains.kotlin.gradle.tasks.FatFrameworkTask::class) {
        baseName = "SecondLib"
        destinationDir = buildDir.resolve("fat-framework/debug")
        from(
            ios32.binaries.getFramework("DEBUG"),
            ios64.binaries.getFramework("DEBUG"),
            iosX64.binaries.getFramework("DEBUG")
        )
    }*/

    tasks.register("copyFramework") {
        val buildType = project.findProperty("kotlin.build.type") as? String ?: "DEBUG"
        dependsOn("link${buildType.toLowerCase().capitalize()}FrameworkIos")

        doLast {
            val srcFile = (kotlin.targets["ios"] as KotlinNativeTarget).binaries.getFramework(buildType).outputFile
            val targetDir = project.property("configuration.build.dir")!!
            copy {
                from(srcFile.parent)
                into(targetDir)
                include( "thirdlib.framework/**")
                include("thirdlib.framework.dSYM")
            }
        }
    }
}

