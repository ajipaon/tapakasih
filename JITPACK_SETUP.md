# JitPack Setup Guide for TapakAsih Android SDK

## Overview

This guide explains how TapakAsih Android SDK uses JitPack to publish to Maven Central without requiring authentication from users.

## What is JitPack?

JitPack is a novel package repository for JVM libraries. It builds Git projects on demand and provides you with ready-to-use artifacts. Best of all:

✅ **FREE** - No cost for open source projects
✅ **NO AUTHENTICATION** - Users don't need tokens
✅ **AUTOMATIC** - Builds from GitHub releases
✅ **MAVEN CENTRAL SYNC** - Automatically syncs to Maven Central

## How It Works

```
GitHub Release → JitPack Build → Maven Central → Users Install (No Auth)
```

1. You create a GitHub release (e.g., v1.0.0)
2. JitPack automatically detects the release and builds your library
3. JitPack publishes the build artifacts to Maven Central
4. Users can install without any authentication

## Setup Instructions

### Step 1: Ensure Repository is Public

Your repository must be public for JitPack to access it:

```bash
# Make sure repository is public on GitHub
# Repository: paondev/tapakasih
```

### Step 2: Setup build.gradle.kts

Your `build.gradle.kts` should have the Maven publishing configuration:

```kotlin
plugins {
    alias(libs.plugins.android.library)
    `maven-publish`
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
```

This is already configured in your project! ✅

### Step 3: Link JitPack Account

1. Go to https://jitpack.io
2. Click "Sign in with GitHub"
3. Authorize JitPack to access your repositories
4. Find your repository: `paondev/tapakAsih3432432`
5. Click "Look up"

### Step 4: Verify GitHub Actions

Your `.github/workflows/publish-android-sdk.yml` automatically triggers JitPack:

```yaml
- name: Trigger JitPack build
  run: |
    curl -X POST "https://jitpack.io/build/paondev/tapakAsih3432432/${{ github.ref_name }}"
```

This is already configured! ✅

## Publishing a New Version

### Method 1: GitHub Release (Recommended)

```bash
# Create and push tag
git tag v1.0.0
git push origin v1.0.0

# Go to GitHub and create release
# JitPack will automatically build it
```

### Method 2: JitPack Manual Trigger

1. Go to https://jitpack.io
2. Enter repository: `paondev/tapakAsih3432432`
3. Click "Look up"
4. Click "Get it" on the version you want
5. Wait for build to complete

### Check Build Status

Visit: https://jitpack.io/paondev/tapakAsih3432432

## Installation for Users

Users can now install your SDK **without any authentication**:

### Option 1: JitPack Direct (Immediately Available)

```gradle
// settings.gradle
repositories {
    google()
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

// app/build.gradle
dependencies {
    implementation 'com.github.paondev:tapakAsih3432432:1.0.0'
}
```

### Option 2: Maven Central (After Sync)

After JitPack syncs to Maven Central (may take a few hours):

```gradle
// settings.gradle
repositories {
    google()
    mavenCentral()
}

// app/build.gradle
dependencies {
    implementation 'com.paondev.lib:tapakasih:1.0.0'
}
```

## Comparison: JitPack vs GitHub Packages

| Feature             | JitPack         | GitHub Packages            |
| ------------------- | --------------- | -------------------------- |
| Cost                | FREE            | FREE                       |
| User Authentication | ❌ NOT Required | ✅ Required                |
| Public Access       | ✅ Yes          | ❌ No (private by default) |
| Maven Central Sync  | ✅ Automatic    | ❌ No                      |
| Build Time          | ~5-10 minutes   | Immediate                  |
| Global Availability | ✅ Yes          | ❌ GitHub only             |

## Troubleshooting

### Build Failed on JitPack

**Check build logs** at https://jitpack.io/paondev/tapakAsih3432432

**Common issues**:

- Repository not public
- Gradle build errors
- Missing dependencies

**Solution**: Fix the issue in code, tag new version, try again.

### Package Not Found

**Wait time**: Maven Central sync can take 2-24 hours

**Solution**: Use JitPack direct repository for immediate access.

### Version Already Exists

**Error**: Cannot republish same version

**Solution**: Tag new version (e.g., v1.0.1) instead.

## Best Practices

1. **Always test locally first**

   ```bash
   ./gradlew assembleRelease
   ```

2. **Use semantic versioning**
   - MAJOR.MINOR.PATCH format
   - Increment appropriately

3. **Document changes**
   - Write release notes
   - Update CHANGELOG

4. **Monitor build status**
   - Check JitPack after release
   - Verify no build errors

## Advantages for Users

### Before (GitHub Packages)

```gradle
maven {
    url = uri("https://maven.pkg.github.com/paondev/tapakAsih3432432")
    credentials {
        username = "their-username"      // ❌ Required
        password = "their-token"         // ❌ Required
    }
}
```

### After (JitPack)

```gradle
repositories {
    maven { url = uri("https://jitpack.io") }  // ✅ No auth
}

dependencies {
    implementation 'com.github.paondev:tapakAsih3432432:1.0.0'
}
```

## Additional Resources

- **JitPack Documentation**: https://jitpack.io/docs/
- **JitPack Android Guide**: https://jitpack.io/docs/ANDROID/
- **Your Repository**: https://jitpack.io/paondev/tapakAsih3432432

## Support

For issues with JitPack:

- Check JitPack build logs
- Review JitPack documentation
- Contact support@paondev.com

## Summary

✅ **Setup Complete**: Your Android SDK is configured for JitPack
✅ **No Auth Required**: Users can install without tokens
✅ **Free**: No cost for open source
✅ **Automatic**: Builds on GitHub release
✅ **Maven Central**: Automatically synced

Your SDK is now truly open source and easy to install! 🎉
