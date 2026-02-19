plugins {
    alias(libs.plugins.android.library)
    `maven-publish`
}

android {
    namespace = "com.paondev.lib.tapakasih"
    compileSdk = 36

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    
    // OkHttp for networking
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    
    // Gson for JSON serialization
    implementation("com.google.code.gson:gson:2.10.1")
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "com.paondev.lib"
            artifactId = "tapakasih"
            version = "1.0.0"

            from(components["release"])
            
            pom {
                name.set("TapakAsih SDK")
                description.set("Activity tracking SDK for Android applications")
                url.set("https://github.com/paondev/tapakasih")
                
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                
                developers {
                    developer {
                        id.set("paondev")
                        name.set("PaonDev")
                        email.set("support@paondev.com")
                    }
                }
                
                scm {
                    connection.set("scm:git:github.com:paondev/tapakasih.git")
                    developerConnection.set("scm:git:github.com:paondev/tapakasih.git")
                    url.set("https://github.com/paondev/tapakasih")
                }
            }
        }
    }
}
