import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val packagePrefix by properties
val appName by properties
val sharedVersionName by properties
val sharedVersionCode by properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.gson)
            implementation(libs.androidx.material)
            implementation(libs.androidx.material3)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(libs.androidx.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.okhttp)
            implementation(libs.jsoup)
            implementation(compose.materialIconsExtended)
            implementation(libs.gson)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.exposed.core)
            implementation(libs.exposed.crypt)
            implementation(libs.exposed.dao)
            implementation(libs.exposed.jdbc)
            implementation(libs.exposed.json)
            implementation(libs.h2.database)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.gson)
            implementation(compose.material3)
        }
    }
}

android {
    namespace = "$packagePrefix.android"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "$packagePrefix.android"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = sharedVersionCode.toString().toInt()
        versionName = sharedVersionName.toString()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    signingConfigs {
        create( "release" ) {
            val passwords = File( File("$rootDir").parentFile , "password" ).readLines()
            storeFile = File( File("$rootDir").parentFile , "androidSigningKey.jks" )
            storePassword = passwords.first()
            keyAlias = "release"
            keyPassword = passwords[1]
        }
    }
    buildTypes {
        getByName( "debug" ) {
            isDebuggable = true
            applicationIdSuffix += ".debug"
            versionNameSuffix += "-debug"
            resValue( "string" , "app_name" , "$appName debug" )
        }
        getByName("release") {
            resValue( "string" , "app_name" , "$appName" )
            signingConfig = signingConfigs.getByName( "release" )
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}

compose.desktop {
    application {
        mainClass = "$packagePrefix.desktop.MyApplication"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "$packagePrefix.desktop"
            packageVersion = sharedVersionName.toString()
        }
    }
}