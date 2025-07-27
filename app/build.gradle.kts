import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.androidXNavigation)
    alias(libs.plugins.kotlinKapt)
}

android {
    namespace = "ro.alingrosu.stockmanagement"
    compileSdk = 36

    defaultConfig {
        applicationId = "ro.alingrosu.stockmanagement"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
}

dependencies {
    implementation(libs.bundles.app)
    implementation(libs.legacy.support.v4)
    kapt(libs.bundles.kapt)

    testImplementation(libs.bundles.appTest)
    androidTestImplementation(libs.bundles.appAndroidTest)
}