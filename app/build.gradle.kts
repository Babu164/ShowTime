plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.showtime"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.showtime"
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

    // -------------------- Firebase Dependencies --------------------

    // Firebase BOM (Bill of Materials) to manage Firebase library versions
    implementation(platform(libs.firebase.bom))

    // Firebase Analytics - for event tracking and user analytics
    implementation(libs.firebase.analytics)

    // Firebase Authentication (Kotlin Extensions)
    implementation(libs.firebase.auth.ktx)

    // Firebase Firestore (Kotlin Extensions) - NoSQL cloud database
    implementation(libs.firebase.firestore.ktx)

    // Firebase Cloud Storage (Kotlin Extensions) - file storage
    implementation(libs.firebase.storage.ktx)

    // Google Firebase Authentication (alternate package)
    implementation(libs.google.firebase.auth.ktx)

    // -------------------- Jetpack Compose & AndroidX --------------------

    // Core KTX - Kotlin extensions for Android core libraries
    implementation(libs.androidx.core.ktx)

    // Lifecycle management for Compose (lifecycle-aware components)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Compose support inside activities
    implementation(libs.androidx.activity.compose)

    // Compose BOM to manage all Compose versions
    implementation(platform(libs.androidx.compose.bom))

    // Jetpack Compose UI framework
    implementation(libs.androidx.ui)

    // Compose graphics support
    implementation(libs.androidx.ui.graphics)

    // Compose UI tooling for preview
    implementation(libs.androidx.ui.tooling.preview)

    // Material Design 3 components for Compose
    implementation(libs.androidx.material3)

    // Extended Material Icons for Compose
    implementation(libs.androidx.material.icons.extended)

    // Jetpack Compose Navigation component
    implementation(libs.androidx.navigation.compose)

    // -------------------- Testing Dependencies --------------------

    // JUnit for unit testing
    testImplementation(libs.junit)

    // AndroidX Test - JUnit integration for Android tests
    androidTestImplementation(libs.androidx.junit)

    // Espresso - Android UI testing framework
    androidTestImplementation(libs.androidx.espresso.core)

    // Compose BOM for UI testing consistency
    androidTestImplementation(platform(libs.androidx.compose.bom))

    // Jetpack Compose UI testing with JUnit4
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // -------------------- Debugging Dependencies --------------------

    // Compose UI tooling for design-time preview and inspection
    debugImplementation(libs.androidx.ui.tooling)

    // Compose Test Manifest for managing testing environments
    debugImplementation(libs.androidx.ui.test.manifest)
}
