plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.services)
}


android {
    namespace = "com.baotran.winsnack_group2"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.baotran.winsnack_group2"
        minSdk = 26
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation ("com.google.android.gms:play-services-tasks:18.0.2")
    implementation ("com.google.android.gms:play-services-base")
    implementation ("com.google.firebase:firebase-database:20.3.0")
    implementation ("com.google.firebase:firebase-firestore:24.10.0")
    implementation ("com.google.firebase:firebase-storage:21.0.0")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")
    implementation ("androidx.appcompat:appcompat:1.7.0")
    implementation ("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.transition:transition:1.4.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")



}