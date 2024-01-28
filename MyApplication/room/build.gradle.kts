plugins {
    id("com.android.application")
}

android {
    namespace = "com.sunmeat.services"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sunmeat.services"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.karumi:dexter:6.2.3")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("androidx.recyclerview:recyclerview:1.4.0-alpha01")
    implementation("androidx.recyclerview:recyclerview-selection:1.1.0")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("com.google.code.gson:gson:2.8.9")

    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")

    // room components
    implementation("android.arch.persistence.room:runtime:1.1.1")
    annotationProcessor("android.arch.persistence.room:compiler:1.1.1")
    androidTestImplementation("android.arch.persistence.room:testing:1.1.1")

    // lifecycle components
    implementation ("android.arch.lifecycle:extensions:1.1.1")
    annotationProcessor ("android.arch.lifecycle:compiler:1.1.1")
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.7.0")
    implementation ("androidx.lifecycle:lifecycle-livedata:2.7.0")
}