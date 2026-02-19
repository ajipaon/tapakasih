# Publishing Guide

This guide explains how to publish TapakAsih SDK packages so developers can install them.

## Prerequisites

### JitPack (Android SDK)

1. **JitPack Account**
   - Sign up at https://jitpack.io
   - Link your GitHub account

2. **Repository Setup**
   - Ensure repository is public
   - Repository name: `paondev/tapakAsih3432432`

### pub.dev (Flutter SDK)

1. **pub.dev Account**
   - Sign up at https://pub.dev
   - Verify your email

2. **Publishing Credentials**
   - Generate a publishing token at https://pub.dev/account
   - Add token as GitHub Secret: `PUB_DEV_CREDENTIALS`

## Publishing Android SDK

### Method 1: Automatic via GitHub Release (Recommended)

1. **Create a new release** on GitHub:
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```
2. **Go to GitHub Releases** and create a new release:
   - Tag: `v1.0.0`
   - Title: `v1.0.0`
   - Description: Release notes

3. **GitHub Actions** will automatically:
   - Build the SDK
   - Trigger JitPack build
   - Upload AAR artifact to release

4. **JitPack will automatically**:
   - Build the library
   - Publish to Maven Central
   - Make it available for download

5. **Check build status** at: https://jitpack.io/paondev/tapakasih

### Method 2: Manual via JitPack

1. **Go to JitPack**: https://jitpack.io
2. **Enter repository**: `paondev/tapakasih`
3. **Click "Look up"**
4. **Click "Get it"** on the version you want to build
5. **Wait for build to complete**

### Method 3: Manual via GitHub Actions

1. **Go to Actions** tab in GitHub
2. **Select "Publish Android SDK"** workflow
3. **Click "Run workflow"**
4. **Enter version**: `1.0.0`
5. **Click "Run workflow"**

### Publishing Locally

If you want to publish locally:

```bash
# Build AAR
./gradlew assembleRelease
```

Note: For JitPack, publishing is done automatically from GitHub releases.

## Publishing Flutter SDK

### Method 1: Automatic via GitHub Release

1. **Create a new release** on GitHub:
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```
2. **Go to GitHub Releases** and create a new release:
   - Tag: `v1.0.0`
   - Title: `v1.0.0`
   - Description: Release notes

3. **GitHub Actions** will automatically:
   - Format and analyze code
   - Run tests
   - Build plugin
   - Publish to pub.dev

### Method 2: Manual via GitHub Actions

1. **Add PUB_DEV_CREDENTIALS** to GitHub Secrets:
   - Go to Settings → Secrets and variables → Actions
   - Click "New repository secret"
   - Name: `PUB_DEV_CREDENTIALS`
   - Value: Your pub.dev publishing token

2. **Go to Actions** tab in GitHub
3. **Select "Publish Flutter SDK"** workflow
4. **Click "Run workflow"**
5. **Enter version**: `1.0.0`
6. **Click "Run workflow"**

### Publishing Locally

If you want to publish locally:

```bash
cd tapak_asih_flutter

# Format code
dart format .

# Analyze code
flutter analyze

# Run tests
flutter test

# Dry run (without publishing)
flutter pub publish --dry-run

# Publish to pub.dev
flutter pub publish
```

## Installation for Developers

### Android SDK

After publishing to JitPack/Maven Central, developers can install the SDK **without any authentication**:

#### Option 1: Maven Central (Recommended - After JitPack Sync)

Add dependency to your app's `build.gradle`:

```gradle
dependencies {
    implementation 'com.paondev.lib:tapakasih:1.0.0'
}
```

Make sure `mavenCentral()` is included in your repositories:

```gradle
// settings.gradle (for Gradle 7.0+) or build.gradle
repositories {
    google()
    mavenCentral()
}
```

**Note**: After JitPack builds your library, it automatically syncs to Maven Central. This may take a few hours.

#### Option 2: JitPack Direct (Immediately Available)

Add JitPack repository to your `settings.gradle`:

```gradle
repositories {
    google()
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}
```

Add dependency to your app's `build.gradle`:

```gradle
dependencies {
    implementation 'com.github.paondev:tapakasih:1.0.0'
}
```

**No authentication required!**

### Flutter SDK

After publishing to pub.dev, developers can install:

```yaml
# pubspec.yaml
dependencies:
  tapak_asih_flutter: ^1.0.0
```

Then run:

```bash
flutter pub get
```

## Version Management

### Android SDK (build.gradle.kts)

```kotlin
publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.paondev.lib"
            artifactId = "tapakasih"
            version = "1.0.0"  // Update this for each release
        }
    }
}
```

### Flutter SDK (pubspec.yaml)

```yaml
name: tapak_asih_flutter
description: "TapakAsih Flutter SDK..."
version: 1.0.0 # Update this for each release
```

## Version Bumping

### Semantic Versioning

Follow semantic versioning (MAJOR.MINOR.PATCH):

- **MAJOR**: Breaking changes
- **MINOR**: New features, backward compatible
- **PATCH**: Bug fixes, backward compatible

### Example

```bash
# Patch release (1.0.0 -> 1.0.1)
git tag v1.0.1

# Minor release (1.0.1 -> 1.1.0)
git tag v1.1.0

# Major release (1.1.0 -> 2.0.0)
git tag v2.0.0

git push origin --tags
```

## Troubleshooting

### Android Publishing Failed

**Error**: 401 Unauthorized

- Check GitHub token has `read:packages` and `write:packages` scopes
- Verify credentials in `build.gradle.kts`

**Error**: Package already exists

- Increment version number
- Use `./gradlew clean publish`

### Flutter Publishing Failed

**Error**: 403 Forbidden

- Verify `PUB_DEV_CREDENTIALS` secret is set correctly
- Check pub.dev account is active

**Error**: Package name conflicts

- Ensure package name is unique on pub.dev
- Check `name` in `pubspec.yaml`

**Error**: Validation failed

- Run `flutter pub publish --dry-run` to check for issues
- Fix formatting: `dart format .`
- Fix analysis issues: `flutter analyze`

### GitHub Actions Failed

**Check workflow logs**:

1. Go to Actions tab
2. Click on failed workflow run
3. Click on failed job
4. Review logs for errors

**Common issues**:

- Missing secrets (GITHUB_TOKEN, PUB_DEV_CREDENTIALS)
- Version conflicts
- Code analysis failures

## Best Practices

1. **Always test before publishing**

   ```bash
   # Android
   ./gradlew test

   # Flutter
   flutter test
   ```

2. **Use semantic versioning**
   - Increment version appropriately
   - Document changes in release notes

3. **Check compatibility**
   - Test on different Android versions
   - Test with different Flutter versions

4. **Maintain CHANGELOG**
   - Document all changes
   - Include migration guides for breaking changes

5. **Secure secrets**
   - Never commit tokens
   - Rotate tokens regularly
   - Use GitHub Secrets for CI/CD

## Rollback Procedure

If you need to unpublish a package:

### GitHub Packages

```bash
# Delete package version via GitHub UI
# 1. Go to repository
# 2. Click "Packages" tab
# 3. Select package
# 4. Click version to delete
# 5. Click "Delete version"
```

### pub.dev

```bash
# Contact pub.dev support to unpublish
# Email: support@pub.dev
# Include: package name, version, reason
```

## Support

For issues with publishing:

- GitHub Issues: https://github.com/paondev/tapakasih/issues
- Email: support@paondev.com
