plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.shinydiscoballsdev.kifossk"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.shinydiscoballsdev.kifossk"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }

    applicationVariants.all {
        outputs.all {
            val variantName = if (buildType.name == "release") "release" else "debug"
            val fileName = "kiFOSSk-${versionName}-${variantName}.apk"
            (this as com.android.build.gradle.internal.api.BaseVariantOutputImpl).outputFileName = fileName
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file("../kiFOSSk.keystore")
            storePassword = project.property("kifossk.storePassword") as String
            keyAlias = project.property("kifossk.keyAlias") as String
            keyPassword = project.property("kifossk.keyPassword") as String
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
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
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.webkit:webkit:1.9.0")
}