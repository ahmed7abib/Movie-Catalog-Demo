plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.ahmed.a.habib.moviecatalogapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ahmed.a.habib.moviecatalogapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        multiDexEnabled = true
        testInstrumentationRunner = "com.ahmed.a.habib.moviecatalogapp.HiltTestRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        buildFeatures {
            buildConfig = true
        }
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"https://api.themoviedb.org/3/\"")
            buildConfigField("String", "API_KEY", "\"737fa690e0b082a189535d1b1df6614f\"")
            buildConfigField("String", "IMAGES_BASE_URL", "\"https://image.tmdb.org/t/p/w500\"")
            buildConfigField(
                "String",
                "BEAR_TOKEN",
                "\"eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI3MzdmYTY5MGUwYjA4MmExODk1MzVkMWIxZGY2NjE0ZiIsIm5iZiI6MTcyODY3MzU3MC4wNTA0OTcsInN1YiI6IjVmNWI2NjUzNzMxNGExMDAzNmUwMTU3OSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.YMbMIxs9mLq5QuOFrJHGsGuHX-CAU7LQ5U6gulNep-4\""
            )
        }

        release {
            buildConfigField("String", "BASE_URL", "\"https://api.themoviedb.org/3/\"")
            buildConfigField("String", "API_KEY", "\"737fa690e0b082a189535d1b1df6614f\"")
            buildConfigField("String", "IMAGES_BASE_URL", "\"https://image.tmdb.org/t/p/w500\"")
            buildConfigField(
                "String",
                "BEAR_TOKEN",
                "\"eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI3MzdmYTY5MGUwYjA4MmExODk1MzVkMWIxZGY2NjE0ZiIsIm5iZiI6MTcyODY3MzU3MC4wNTA0OTcsInN1YiI6IjVmNWI2NjUzNzMxNGExMDAzNmUwMTU3OSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.YMbMIxs9mLq5QuOFrJHGsGuHX-CAU7LQ5U6gulNep-4\""
            )

            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    // Built-In libs
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.multidex)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.turbine) // For test flow.
    testImplementation(libs.mockito.core)
    testImplementation(libs.core.testing)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)

    // Android Testing
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.core.testing)
    androidTestImplementation(libs.mockito.android)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.espresso.contrib)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.fragment.testing)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
    ksp(libs.room.compiler)

    // Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // Nav Component
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Glide
    implementation(libs.glide)
    ksp(libs.glide.compiler)

    // Paging 3
    implementation(libs.paging.runtime)

    // Swipe Refresh
    implementation(libs.swiperefreshlayout)

    // Ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.gson)
    implementation(libs.gson)
}