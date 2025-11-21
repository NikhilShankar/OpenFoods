plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-parcelize")

    //kapt declaration for hilt
    kotlin("kapt")
    //plugin declaration for hilt version is mentioned in the root gradle file
    id("dagger.hilt.android.plugin")
    //kotlinx-serialization plugin for navigation
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.opentable.openfoods"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.opentable.openfoods"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Hilt & HiltViewModel
    // Hilt
    implementation(libs.hilt.android)
    //Hilt annotation processor library version
    kapt(libs.hilt.compiler)
    // Hilt Navigation Compose
    implementation(libs.androidx.hilt.navigation.compose)
    // ViewModels
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    //Retrofit
    implementation(libs.retrofit) // Or the latest stable version
    implementation(libs.kotlinx.serialization.json) // Or the latest stable version
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.converter.kotlinx.serialization)

    //Compose Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.material3)

    // Testing dependencies
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.androidx.core.testing)

    //Paging3
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging.runtime.ktx)
    //Test-Paging3
    testImplementation(libs.androidx.paging.testing)

    //Coil
    implementation(libs.coil.compose)



}