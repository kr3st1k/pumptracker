
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {


    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    jvm()
    
//    listOf(
//        iosX64(),
//        iosArm64(),
//        iosSimulatorArm64()
//    ).forEach { iosTarget ->
//        iosTarget.binaries.framework {
//            baseName = "ComposeApp"
//            isStatic = true
//        }
//    }
    
    sourceSets {

//        iosMain.dependencies {
//            implementation(libs.ktor.client.darwin)
//        }
        
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.androidx.browser)
            implementation(libs.android.core.splashscreen)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.decompose.base)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(libs.windowSize)
            implementation(compose.materialIconsExtended)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.multiplatform.settings.coroutines)
            implementation(libs.krypto)
            implementation(libs.kotlinx.datetime)
            implementation(libs.multiplatform.settings.no.arg)
            implementation(libs.kotlinx.io.core)
            implementation(libs.navigation.compose)
            implementation(libs.coil.compose)
            implementation(libs.coil.ktor)
            implementation(libs.materialKolor)
            implementation(libs.ktor.client.core)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(libs.kotlinx.serialization.json.v163)
            implementation(libs.konnection)
            implementation(libs.ksoup)
            implementation(libs.bundles.ktor)
            implementation(libs.androidx.room.runtime)
            implementation(libs.sqlite.bundled)
            implementation(libs.bundles.decompose)
            implementation(libs.sqlite)
            implementation(compose.components.uiToolingPreview)
        }
        jvmMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(compose.desktop.currentOs)
            implementation(compose.desktop.common)
            implementation(compose.uiTooling)
            implementation(libs.accents)
            implementation(libs.jewel.int.ui.standalone.x41)
            implementation(libs.jewel.int.ui.decorated.window.x41)
            implementation(compose.desktop.currentOs) { exclude(group = "org.jetbrains.compose.material") }
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

android {
    namespace = "com.kr3st1k.pumptracker"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.kr3st1k.kmp.pumptracker"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 117
        versionName = "0.7"

        ndk {
            abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64"))
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
        implementation(libs.ktor.client.okhttp)
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg,
                TargetFormat.Deb, TargetFormat.Rpm,
                TargetFormat.Exe
            )
            packageName = "PumpTracker"
            packageVersion = "1.1.0"
            description = "Pump It Up Companion App"
            copyright = "© 2024 Kr3st1k"
            vendor = "Kr3st1k"

            val pathToIcon = project.file("icons")

            macOS {
                iconFile.set(pathToIcon.resolve("icon.icns"))
                dockName = "PumpTracker"
            }
            windows {
                perUserInstall = true
                shortcut = true
                iconFile.set(pathToIcon.resolve("icon.ico"))
            }
            linux {
                shortcut = true
                iconFile.set(pathToIcon.resolve("icon.png"))
            }
        }


        
        buildTypes.release {
            proguard {
                configurationFiles.from("compose.desktop.pro")
            }
        }
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
//    implementation(libs.androidx.navigation.runtime.ktx)
    add("kspCommonMainMetadata", libs.androidx.room.compiler)
    add("kspJvm", libs.androidx.room.compiler)
    add("kspAndroid", libs.androidx.room.compiler)
//    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
//    add("kspIosX64", libs.androidx.room.compiler)
//    add("kspIosArm64", libs.androidx.room.compiler)
}