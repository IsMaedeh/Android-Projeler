plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.finalprojesicamerax3"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.finalprojesicamerax3"
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
    buildFeatures {
        mlModelBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.tensorflow.lite.support)
    implementation(libs.tensorflow.lite.metadata)
//    implementation(libs.linkedin.litr)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)

    // TensorFlow Lite Bağımlılıkları
    implementation(libs.tensorflow.lite)
    implementation(libs.tensorflow.lite.task.vision)
//    implementation(libs.tensorflow.lite.support)
//    implementation(libs.tensorflow.lite.metadata)

//    implementation(libs.glide)

//    implementation(libs.picasso)

//    implementation(libs.cloudinary)
//    implementation(libs.okhttp)

    implementation(libs.cloudinary)
    implementation(libs.litr)
//    implementation(libs.camerax.core)



}