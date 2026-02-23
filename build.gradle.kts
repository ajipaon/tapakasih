plugins {
    id("com.android.library") version "8.2.2"
    `maven-publish`
}

android {
    namespace = "com.paondev.lib.tapakasih"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
    
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    
    // OkHttp for networking
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    
    // Gson for JSON serialization
    implementation("com.google.code.gson:gson:2.10.1")
    
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "com.paondev.lib"
            artifactId = "tapakasih"
            version = "1.0.0"

            afterEvaluate {
                from(components["release"])
            }
            
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