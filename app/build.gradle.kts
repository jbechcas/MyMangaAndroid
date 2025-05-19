plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.mymanga"
    compileSdk = 35

    buildFeatures {
        viewBinding = true
    }
    defaultConfig {
        applicationId = "com.example.mymanga"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    //Dependencias de material design
    implementation(libs.material.v190)

    //Dependencias hilt
    implementation(libs.hilt.android.v2511)
    kapt(libs.dagger.hilt.android.compiler)

    //Dependencias del gráfico de navegación
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //Dependencias de retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)

    //Dependencias de google maps
    implementation(libs.google.maps)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    //Depencias glide
    implementation(libs.glide)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

