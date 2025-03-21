plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.moviesearchapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.moviesearchapp"
        minSdk = 34
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures { viewBinding = true }

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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.okhttp)
    implementation(libs.glide)
    annotationProcessor(libs.compiler)
    //implementation("com.squareup.okhttp3:okhttp:4.12.0")
    //implementation("com.github.bumptech.glide:glide:4.16.0")
    //annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
}