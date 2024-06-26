plugins {
    id("com.android.application")
}

android {
    namespace = "org.hstlp.yourmemory"
    compileSdk = 34

    defaultConfig {
        applicationId = "org.hstlp.yourmemory"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        mlModelBinding = true
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // add dependency
    implementation ( "com.github.bumptech.glide:glide:4.12.0" )
    implementation ("me.xdrop:fuzzywuzzy:1.2.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

    //RecylerView
    implementation ( "androidx.navigation:navigation-fragment:2.7.5" )
    implementation ( "androidx.navigation:navigation-ui:2.7.5" )
    implementation ("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")

    //UI circle
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    implementation ("com.github.Spikeysanju:ZoomRecylerLayout:1.0")

    // mlkit
    implementation ( "com.google.mlkit:image-labeling:17.0.7" )
    implementation ( "com.google.mlkit:text-recognition:16.0.0" )

    // gms
    implementation ("com.google.android.gms:play-services-tasks:16.0.1")

    implementation ("com.github.Spikeysanju:ZoomRecylerLayout:1.0")

    // this one use for copy image
    implementation ( "com.github.yalantis:ucrop:2.2.8-native" )

    implementation ("androidx.work:work-runtime:2.7.1")
    implementation ("com.google.androidbrowserhelper:androidbrowserhelper:2.5.0")

    // for translate languauge (english only)
    implementation("com.google.mlkit:translate:17.0.2")
}