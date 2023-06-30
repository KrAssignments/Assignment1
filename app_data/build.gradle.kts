plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("realm-android")
}

android {
    namespace = "com.krupal.app_data"
    compileSdk = 33

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        buildConfig = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    api("androidx.core:core-ktx:1.10.1")
    api("androidx.appcompat:appcompat:1.6.1")
    api("com.google.android.material:material:1.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

//    val realm = "1.8.0"
//    api("io.realm.kotlin:library-base:$realm")
//    api("io.realm.kotlin:library-sync:$realm")

    api("io.reactivex.rxjava3:rxkotlin:3.0.1")
    api("io.reactivex.rxjava3:rxandroid:3.0.2")

    val workVersion = "2.8.1"
    api("androidx.work:work-runtime-ktx:$workVersion")
    api("androidx.work:work-rxjava3:$workVersion")
    api("androidx.concurrent:concurrent-futures-ktx:1.1.0")

    val retrofit = "2.9.0"
    api("com.squareup.retrofit2:retrofit:$retrofit")
    api("com.squareup.retrofit2:adapter-rxjava3:$retrofit")
    api("com.squareup.retrofit2:converter-gson:$retrofit")

    api(platform("com.squareup.okhttp3:okhttp-bom:4.11.0"))
    api("com.squareup.okhttp3:okhttp")
    api("com.squareup.okhttp3:logging-interceptor")
}