<img width="108" height="228" alt="Screenshot_20260703-212800" src="https://github.com/user-attachments/assets/1c79b5aa-c3e8-4064-93f0-7b6871fd8f57" />
<img width="108" height="228" alt="Screenshot_20260703-212755" src="https://github.com/user-attachments/assets/1b8df3bf-0fac-4199-a9d0-c5cd1ae6d1a2" />
<img width="108" height="228" alt="Screenshot_20260703-212824" src="https://github.com/user-attachments/assets/4b97f86c-756c-456c-a063-b200a171803f" />
<img width="108" height="228" alt="Screenshot_20260703-212814" src="https://github.com/user-attachments/assets/ff3ee506-9a39-443e-995a-6e29a7e7a829" />
<img width="108" height="228" alt="Screenshot_20260703-212809" src="https://github.com/user-attachments/assets/4c78b6b1-7bdb-41be-9f00-3636031a9d6b" />
<img width="108" height="228" alt="Screenshot_20260703-213147 (1)" src="https://github.com/user-attachments/assets/2034be02-dc4d-4aa1-ba6d-aad19f9a81da" />



# SI Match Center Library Integration Guide

This guide provides step-by-step instructions on how to integrate the `si_matchcenter` library into your Android application.

## Prerequisites

Before you begin, ensure your project meets the following requirements:
*   **Java Version**: JDK 17 or higher.
*   **Android Gradle Plugin**: Version 8.0 or higher.
*   **Compose**: Jetpack Compose enabled.
*   **Hilt**: Dagger Hilt configured for dependency injection.
*   **Kotlin Serialization**: Required for data models.

## Step 1: Add Plugins and Repository

Add the KSP, Hilt, and Kotlin Serialization plugins to your **project-level** `build.gradle.kts`:

```kotlin
plugins {
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false // Match your Kotlin version
    id("com.google.dagger.hilt.android") version "2.60" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21" apply false // Match your Kotlin version
}
```

Add the JitPack repository to your `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

## Step 2: Add Dependency and Configuration

In your **application-level** `build.gradle.kts`, apply the plugins and add the dependencies:

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    // ...
    compileOptions {
        // Required for java.time support on older APIs (API < 26)
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Library dependency - Replace '1.0.0' with the latest release version
    implementation("com.github.si-narendra-gupta:SI-Match-Center:1.0.0")
    
    // Core Library Desugaring (Required for java.time support)
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.4")
    
    // Hilt
    implementation("com.google.dagger:hilt-android:2.60")
    ksp("com.google.dagger:hilt-compiler:2.60")
    
    // Kotlin Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    
    // Compose
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
}
```

## Step 3: Add Internet Permission

Ensure your `AndroidManifest.xml` has the internet permission:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

## Step 4: Implement Configuration Contracts

The library requires certain configurations to be provided by the host application. You must implement both `BaseConfigContract` and `SiFeedFixtureConfigContract`.

```kotlin
import com.sportz.base.helper.BaseConfigContract
import com.sportz.si_matchcenter.data.remote.SiFeedFixtureConfigContract
import com.sportz.base.business.domain.model.MenuItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppConfig @Inject constructor() : BaseConfigContract, SiFeedFixtureConfigContract {
    
    // --- BaseConfigContract ---
    override fun appName(): Int = R.string.app_name
    override fun chromeToolbarColor(): Int = android.R.color.white
    override fun getBaseUrl(): String = "https://your-api-base-url.com/"
    override fun getApiAuthKey(): String = ""
    override fun getIsDebugMode(): Boolean = true // Use BuildConfig.DEBUG
    override fun getPreferenceDataStoreName(): String = "app_preferences"
    override fun getCaptchaSiteKey(): String = ""
    override fun getBottomMenuItems(): List<MenuItem>? = null

    // --- SiFeedFixtureConfigContract ---
    override fun getTeamLogo(teamId: String): String {
        return "${getBaseUrl()}static-assets/images/teams/${teamId}.png"
    }

    override fun getCurrentTeamId(): String = "1110"

    override fun getMatchCenterUrl(gameId: String): String {
        return "${getBaseUrl()}cricket/live/json/${gameId}.json"
    }

    override fun getBallByBallUrl(matchId: String, inning: Int): String {
        return "${getBaseUrl()}commentary/${matchId}/${inning}.json"
    }

    override fun getManhattanUrl(gameId: String, inning: Int): String {
        return "${getBaseUrl()}graphs/manhattan/${gameId}_overbyover_${inning}.json"
    }

    override fun getSpiderUrl(gameId: String, inning: Int): String {
        return "${getBaseUrl()}graphs/spider/${gameId}_batsman_splits_${inning}.json"
    }
}
```

## Step 5: Provide Configuration via Hilt

Create a Hilt module to bind your implementation to the library's contracts:

```kotlin
import com.sportz.base.helper.BaseConfigContract
import com.sportz.si_matchcenter.data.remote.SiFeedFixtureConfigContract
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ConfigModule {

    @Binds
    @Singleton
    abstract fun bindBaseConfigContract(appConfig: AppConfig): BaseConfigContract

    @Binds
    @Singleton
    abstract fun bindSiFeedFixtureConfig(appConfig: AppConfig): SiFeedFixtureConfigContract
}
```

## Step 6: Initialize Hilt

Your `Application` class must be annotated with `@HiltAndroidApp`:

```kotlin
@HiltAndroidApp
class MyApplication : Application()
```

## Step 7: Configure the Activity

The Activity hosting the Match Center must be annotated with `@AndroidEntryPoint`:

```kotlin
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Your Theme {
                MatchCenterContent(gameId = "rrck03302026267884")
            // }
        }
    }
}
```

## Step 8: Component Usage

The main entry point is the `MatchCenterContent` composable.

### Parameters:
*   `gameId`: The unique identifier for the match.
*   `theme`: (Optional) String for applying specific theme colors.

## Step 9: Theming

The library supports dynamic theming. Use constants from `ThemeConstants` (e.g., `THEME_PINK_NAVY`, `DEFAULT_THEME`).

---
*Developed by Sportz Interactive*
