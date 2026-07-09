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
*   **Gradle Version**: 9.4.1 or higher.
*   **Android Gradle Plugin**: Version 9.2.1 or higher.
*   **Min SDK**: API 24 or higher.
*   **Kotlin Version**: 2.4.0 or higher.
*   **Compose**: Jetpack Compose enabled.
*   **Kotlin Serialization**: Required for data models.

## Step 1: Add Repository

It is recommended to use a **Version Catalog** (`libs.versions.toml`) to manage your dependencies.

Add the following to your `gradle/libs.versions.toml`:

```toml
[versions]
kotlin = "2.4.0"

[libraries]
si-matchcenter = { group = "com.github.si-narendra-gupta", name = "SI-Match-Center", version = "1.0.3" }
kotlinx-serialization-json = { group = "org.jetbrains.kotlinx", name = "kotlinx-serialization-json", version = "1.11.0" }

[plugins]
kotlin-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
```

Then, add the plugin to your **project-level** `build.gradle.kts`:

```kotlin
plugins {
    alias(libs.plugins.kotlin.serialization) apply false
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
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
}

android {
    // ...
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Library dependency
    implementation(libs.si.matchcenter)
    
    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)
    
    // Lifecycle and ViewModel Support
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
}
```

## Step 3: Add Internet Permission

Ensure your `AndroidManifest.xml` has the internet permission:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

## Step 4: Implement Configuration Contract

The library requires certain configurations to be provided by the host application. You must implement the `SiFeedFixtureConfigContract`.

```kotlin
import com.sportz.si_matchcenter.data.remote.SiFeedFixtureConfigContract
import com.sportz.si_matchcenter.business.domain.model.MatchTabList

class AppConfig : SiFeedFixtureConfigContract {
    
    override fun getBaseUrl(): String = "https://your-api-base-url.com/"
    
    override fun getIsDebugMode(): Boolean = true 

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

    override fun getMatchTabs(): MatchTabList? = null
}
```

## Step 5: Initialize the SDK

Initialize the `MatchCenterSDK` in your `Application` class:

```kotlin
import android.app.Application
import com.sportz.si_matchcenter.MatchCenterSDK

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize with your config implementation
        MatchCenterSDK.init(AppConfig())
    }
}
```

## Step 6: Configure the Activity and ViewModel

Since the library is DI-agnostic, you need to provide the ViewModel manually using the SDK's factory.

```kotlin
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.sportz.si_matchcenter.MatchCenterSDK
import com.sportz.si_matchcenter.presentation.ui.screen.MatchCenterContent
import com.sportz.si_matchcenter.presentation.ui.viewmodel.MatchCenterViewModel
import com.sportz.si_matchcenter.helper.ThemeConstants

class MainActivity : ComponentActivity() {

    // Retrieve the ViewModel using MatchCenterSDK's factory
    private val matchCenterViewModel: MatchCenterViewModel by viewModels {
        MatchCenterSDK.provideViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MatchCenterContent(
                gameId = "rrck03302026267884",
                viewModel = matchCenterViewModel,
                theme = ThemeConstants.THEME_ROYAL_GOLD
            )
        }
    }
}
```

## Step 7: Component Usage

The main entry point is the `MatchCenterContent` composable.

### Parameters:
*   `gameId`: The unique identifier for the match (Required).
*   `viewModel`: The `MatchCenterViewModel` instance (Required).
*   `theme`: A JSON string or a constant from `ThemeConstants` to apply specific theme colors (Optional).

## Step 8: Theming

The library supports dynamic theming. You can use pre-defined constants from `ThemeConstants`:
*   `ThemeConstants.THEME_PINK_NAVY` (Default)
*   `ThemeConstants.THEME_ROYAL_GOLD`
*   `ThemeConstants.THEME_DARK_EMERALD`
*   `ThemeConstants.DEFAULT_THEME`

---
*Developed by Sportz Interactive*
