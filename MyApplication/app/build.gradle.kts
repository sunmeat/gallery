plugins {
    id("com.android.application")
}

android {
    namespace = "com.sunmeat.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sunmeat.myapplication"
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
}