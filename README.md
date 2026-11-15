# Aura Design System

**Version `1.0.0`** · One UI-inspired, zero-Material 3 Jetpack Compose design system featuring Glassmorphism, tactile spring physics, and two-axis adaptive layouts.

---

## Overview

**Aura** (`aura-design-system`) is a general-purpose Android (Jetpack Compose) design system built directly on Compose Foundation (`androidx.compose.foundation`) and Compose UI without any `androidx.compose.material3` dependency in the `:design-system` library module.

Inspired by Samsung One UI's calm surface hierarchy and blue-led clarity, Aura pairs high-legibility solid surfaces with first-class **Dual-Materiality Glassmorphism** (`GlassStyle.FROSTED` & `GlassStyle.LIQUID`), **Tactile Spring Cushion** interaction motion (`AuraMotion`), and **Smallest Width × Display Ratio** responsive layouts (`AuraWindowInfo` / `AuraLayout`).

Every component in `:design-system` is backed by a bidirectional **`Specs ↔ Code`** Markdown contract under [`docs/components/`](docs/components/), verified automatically on every build by [`SpecCodeConsistencyTest`](design-system/src/test/kotlin/com/example/ds/contract/SpecCodeConsistencyTest.kt) against rules `EVAL-001` through `EVAL-006`.

### Repository Modules

| Module | Description |
|---|---|
| [`:design-system`](design-system/) | Publishable Android library (`com.example.ds`) containing foundation tokens, `AuraTheme`, `GlassSurface`, and all UI components. |
| [`:design-system-preview`](design-system-preview/) | Interactive catalog app showcasing Light/Dark themes, token matrices, responsive breakpoints, and live component playgrounds. |
| [`docs/`](docs/) | Foundation specifications (`docs/foundation/`) and bidirectional component specs (`docs/components/`). |

---

## How to Use

### Requirements

- **minSdk:** `26` (Android 8.0+)
- **compileSdk:** `35`
- **JDK:** `17`
- **Kotlin:** `2.1.20`
- **Jetpack Compose BOM:** `2025.02.00` (`ui`, `ui-graphics`, `foundation`)

### Installation

#### Option 1 — Gradle via JitPack

Add JitPack to your root `settings.gradle.kts`:

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

Add the library dependency to your module's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.bluemarlins:aura-design-system:1.0.0")
}
```

#### Option 2 — Direct AAR from GitHub Releases (`1.0.0`)

1. Download `aura-design-system-1.0.0.aar` (and optionally `aura-design-system-1.0.0-sources.jar`) from the [GitHub Releases (`1.0.0`)](https://github.com/bluemarlins/aura-design-system/releases/tag/1.0.0) page.
2. Place the `.aar` file inside your app module's `libs/` directory (e.g., `app/libs/aura-design-system-1.0.0.aar`).
3. Declare the AAR alongside Compose Foundation/UI dependencies in `app/build.gradle.kts`:

```kotlin
dependencies {
    implementation(files("libs/aura-design-system-1.0.0.aar"))

    implementation(platform("androidx.compose:compose-bom:2025.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.core:core-ktx:1.15.0")
}
```

#### Option 3 — Local Maven (`mavenLocal()`)

Publish the `1.0.0` artifact locally from a clone of this repository:

```powershell
.\gradlew.bat :design-system:publishToMavenLocal
```

Then enable `mavenLocal()` in `settings.gradle.kts` and add:

```kotlin
implementation("com.github.bluemarlins:aura-design-system:1.0.0")
```

### Quick Start

#### 1. Wrap your UI with `AuraTheme`

`AuraTheme` injects semantic colors (`AuraTheme.colors`), responsive typography (`AuraTheme.typography`), spring motion physics (`AuraTheme.motion`), window metrics (`AuraTheme.window`), and layout tokens (`AuraTheme.layout`):

```kotlin
import androidx.compose.runtime.Composable
import com.example.ds.foundation.theme.AuraColorMode
import com.example.ds.foundation.theme.AuraTheme

@Composable
fun MyApp(isDark: Boolean, content: @Composable () -> Unit) {
    AuraTheme(
        colorMode = if (isDark) AuraColorMode.Dark else AuraColorMode.Light,
    ) {
        content()
    }
}
```

#### 2. Standard Screen with `AuraScaffold` & Core Controls

```kotlin
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ds.component.AuraButton
import com.example.ds.component.AuraButtonVariant
import com.example.ds.component.AuraCard
import com.example.ds.component.AuraCardVariant
import com.example.ds.component.AuraNumberField
import com.example.ds.component.AuraRatingBar
import com.example.ds.component.AuraScaffold
import com.example.ds.component.AuraSlider
import com.example.ds.component.AuraText
import com.example.ds.component.AuraTextField
import com.example.ds.component.AuraTopAppBar
import com.example.ds.foundation.theme.AuraTheme

@Composable
fun SampleEntryScreen() {
    var title by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("45000") }
    var intensity by remember { mutableFloatStateOf(0.75f) }
    var rating by remember { mutableFloatStateOf(4.5f) }

    val layout = AuraTheme.layout
    val typography = AuraTheme.typography

    AuraScaffold(
        topBar = {
            AuraTopAppBar(
                title = "New Entry",
                subtitle = "${AuraTheme.window.widthClass} · ${AuraTheme.window.displayRatio}",
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = layout.screenMarginHorizontal, vertical = layout.screenMarginVertical),
            verticalArrangement = Arrangement.spacedBy(layout.sectionSpacing),
        ) {
            AuraCard(variant = AuraCardVariant.Outlined) {
                AuraText(text = "Details", style = typography.titleMedium)
                AuraTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = "Title",
                    placeholder = "Enter a name...",
                )
                AuraNumberField(
                    value = price,
                    onValueChange = { price = it },
                    label = "Price",
                    prefixText = "₩",
                    formatThousands = true,
                    showSteppers = true,
                )
                AuraSlider(
                    value = intensity,
                    onValueChange = { intensity = it },
                    label = "Balance",
                    valueLabel = "${(intensity * 100).toInt()}%",
                )
                AuraRatingBar(
                    rating = rating,
                    onRatingChange = { rating = it },
                    label = "Rating",
                )
            }

            AuraButton(
                text = "Save Record",
                onClick = { /* persist entry */ },
                variant = AuraButtonVariant.Primary,
                fullWidth = true,
            )
        }
    }
}
```

#### 3. Spatial Edge-to-Edge Navigation with `FloatingGlassScaffold`

```kotlin
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.ds.component.AuraBottomIslandItem
import com.example.ds.component.FloatingBottomIsland
import com.example.ds.component.FloatingGlassScaffold
import com.example.ds.component.FloatingTopPillBar
import com.example.ds.component.GlassStyle
import com.example.ds.component.glassBackdropSource
import com.example.ds.component.rememberFloatingScrollProgress

@Composable
fun SpatialHomeScreen() {
    val listState = rememberLazyListState()
    var selectedTab by remember { mutableIntStateOf(0) }
    val scrollProgress = rememberFloatingScrollProgress(listState)

    val navItems = remember {
        listOf(
            AuraBottomIslandItem(id = "home", label = "Home", icon = Icons.Filled.Home),
            AuraBottomIslandItem(id = "explore", label = "Search", icon = Icons.Filled.Search),
        )
    }

    FloatingGlassScaffold(
        topBar = {
            FloatingTopPillBar(
                title = "Collection",
                scrollProgress = scrollProgress,
                glassStyle = GlassStyle.LIQUID,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        },
        bottomBar = {
            FloatingBottomIsland(
                items = navItems,
                selectedIndex = selectedTab,
                onItemSelected = { selectedTab = it },
                glassStyle = GlassStyle.LIQUID,
                splitActionIcon = Icons.Filled.Add,
                splitActionLabel = "New item",
                onSplitActionClick = { /* open composer */ },
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        },
    ) { contentPadding ->
        val backdropState = com.example.ds.component.LocalGlassBackdropState.current
        LazyColumn(
            state = listState,
            contentPadding = contentPadding,
            modifier = if (backdropState != null) {
                Modifier.glassBackdropSource(backdropState)
            } else {
                Modifier
            },
        ) {
            // Full-bleed scrollable items that blur & refract beneath the floating bars
        }
    }
}
```

### Running the Preview App & Verifying Contracts

```powershell
# Run all unit tests (including SpecCodeConsistencyTest EVAL-001..EVAL-006)
.\gradlew.bat testDebugUnitTest

# Build release AAR and preview APK
.\gradlew.bat assembleRelease

# Install the interactive catalog app on a connected device/emulator
.\gradlew.bat :design-system-preview:installDebug
```

---

## Features

### 1. Three-Tier Token Architecture & Two-Axis Theming

- **Primitive (`AuraPalette`) → Semantic (`AuraColors`)**: Cool-neutral One UI-aligned palette (`Neutral50` `#F2F3F7` Light canvas, OLED-true `Neutral1000` `#000000` Dark canvas, vivid `Blue600`/`Blue400` brand accents) mapped to role groups (`background`, `surface`, `content`, `brand`, `border`, `status`, `interaction`).
- **Typography (`AuraTypography` + `AuraText`)**: 16 semantic text roles (`displayLarge`..`displayMedium`, `headline*`, `title*`, `body*`, `label*`, `monoMedium`, `monoSmall` with `tnum` tabular numerals). Display, Headline, and Title roles scale automatically via `AuraTheme.layout.typographyScale` (`0.92f`–`1.08f`) while preserving Body, Label, and Mono legibility.
- **Tactile Spring Cushion (`AuraMotion`)**: Replaces Material radial ripples with GPU-accelerated spring compression (`pressInSpring`, `pressOutSpring`, `iconSpring`), asymmetric attack/release durations (`90ms` / `180ms`), `110ms` minimum visual press latch inside scrollable lists, and automatic Reduced Motion fallback (`ANIMATOR_DURATION_SCALE == 0f`).
- **Two-Axis Responsive Layout (`Smallest Width × Display Ratio`)**: Classifies windows across `AuraWidthClass` (`ExtraSmall`, `Compact`, `Medium`, `Expanded`), `AuraHeightClass`, and `AuraDisplayRatio` (`UltraTall`, `Portrait`, `Square`, `Landscape`, `UltraWide`) to provide deterministic margins, column counts (`gridColumns`), `maxContentWidth`, and two-pane split ratios (`AuraAdaptiveTwoPane`, `AuraAdaptiveGrid`).

### 2. Dual-Materiality Glassmorphism & Spatial Navigation

- **`GlassSurface` (`FROSTED` & `LIQUID`)**: Live Layer 0 pixel sampling (`rememberGlassBackdropState` + `Modifier.glassBackdropSource`) with graceful multi-API degradation:
  - **API 33+**: AGSL `RuntimeShader` droplet bevel refraction, chromatic RGB dispersion, and inner Fresnel highlights in `LIQUID` mode.
  - **API 31–32**: `RenderEffect` backdrop blur and saturation boost.
  - **API 26–30**: Crisp translucent surface fallback with specular highlight borders.
- **`FloatingGlassScaffold`**: 4-layer edge-to-edge spatial container coordinating Layer 0 live backdrop recording, Layer 1 edge scrims, Layer 2 floating chrome clearance (`calculateBottomIslandContentPadding`), and Layer 3 dismissible overlays.
- **`AuraTopAppBar` / `FloatingTopPillBar`**: Supports `FloatingCapsule`, `FloatingSplit`, and `SurfaceDocked` modes, scroll-reactive opacity/pill transitions (`rememberFloatingScrollProgress`), `MorphingMorePopover`, and Metaball action splitting.
- **`AuraBottomIsland` / `FloatingBottomIsland`**: Stadium capsule navigation bar with spring-driven sliding active indicator pill, velocity-coupled **Elastic Squish & Stretch**, and `LiquidMetaballBridge` split auxiliary FAB.

### 3. Complete Component Catalog (21 Ready Items)

| Category | Component | Description | Spec |
|---|---|---|---|
| **Foundation** | `AuraColors` / `AuraPalette` | One UI-inspired Light & OLED Dark semantic color system | [`02-color.md`](docs/foundation/02-color.md) |
| **Foundation** | `AuraTypography` & `AuraLayout` | 16-role typography scale & 2-axis `Smallest Width × Display Ratio` layout | [`04-typography.md`](docs/foundation/04-typography.md) · [`05-responsive-layout.md`](docs/foundation/05-responsive-layout.md) |
| **Actions** | `AuraButton` | 5 variants (`Primary`, `Secondary`, `Outline`, `Ghost`, `Destructive`), 3 sizes, loading spinner, click debounce | [`aura-button.md`](docs/components/actions/aura-button.md) |
| **Actions** | `AuraIconButton` | 6 variants, `Circle` / `RoundedSquare` shapes, toggle selection state | [`aura-icon-button.md`](docs/components/actions/aura-icon-button.md) |
| **Actions** | `AuraFab` | `Primary`, `Secondary`, `Surface`, `Glass` FAB with scroll-collapsible Extended pill | [`aura-fab.md`](docs/components/actions/aura-fab.md) |
| **Actions** | `AuraSegmentedButton` | Immediate 2–5 option view/unit switcher with spring sliding thumb | [`aura-segmented-button.md`](docs/components/actions/aura-segmented-button.md) |
| **Actions** | `AuraChipButton` | `Filter`, `Tag`, `Assist`, and `Glass` capsule controls | [`aura-chip-button.md`](docs/components/actions/aura-chip-button.md) |
| **Inputs** | `AuraTextField` | `Outlined`, `Filled`, and `Paperless` variants with affixes, validation error, and counters | [`aura-text-field.md`](docs/components/inputs/aura-text-field.md) |
| **Inputs** | `AuraNumberField` | Sanitized numeric & currency input with thousands separator and `+`/`−` steppers | [`aura-number-field.md`](docs/components/inputs/aura-number-field.md) |
| **Inputs** | `AuraSearchBar` | Instant query search bar (`Solid` & `Glass`) with animated clear and filter badge slot | [`aura-search-bar.md`](docs/components/inputs/aura-search-bar.md) |
| **Inputs** | `AuraDateTimePicker` | Calendar `Date`, `DateTime`, and 12-year `YearVintage` grid selector | [`aura-date-time-picker.md`](docs/components/inputs/aura-date-time-picker.md) |
| **Inputs** | `AuraCheckbox` | Tri-state (`Checked`, `Unchecked`, `Indeterminate`) with strikethrough and `48dp` hit bounds | [`aura-checkbox.md`](docs/components/inputs/aura-checkbox.md) |
| **Inputs** | `AuraSwitch` | Physical thumb slide & stretch boolean toggle (`Medium`, `Small`) | [`aura-switch.md`](docs/components/inputs/aura-switch.md) |
| **Inputs** | `AuraRadio` | Single-choice radio control with animated inner dot spring | [`aura-radio.md`](docs/components/inputs/aura-radio.md) |
| **Inputs** | `AuraSlider` | Continuous & stepped discrete range slider with drag halo and accessibility actions | [`aura-slider.md`](docs/components/inputs/aura-slider.md) |
| **Inputs** | `AuraRatingBar` | Interactive tap/scrub or read-only star & half-star rating bar | [`aura-rating-bar.md`](docs/components/inputs/aura-rating-bar.md) |
| **Navigation** | `AuraTopAppBar` / `FloatingTopPillBar` | Scroll-adaptive top pill bar with Dual Glass & Morphing Popover | [`aura-top-app-bar.md`](docs/components/navigation/aura-top-app-bar.md) |
| **Navigation** | `AuraBottomIsland` / `FloatingBottomIsland` | Detached floating bottom island with Metaball split action & elastic pill | [`aura-bottom-island.md`](docs/components/navigation/aura-bottom-island.md) |
| **Containment** | `AuraCard` | `Elevated`, `Filled`, `Outlined`, and `Glass` containers with tactile press response | [`aura-card.md`](docs/components/containment/aura-card.md) |
| **Containment** | `AuraDialog` | `Standard`, `Destructive`, and `Glass` modal confirmation surfaces with adaptive button stacking | [`aura-dialog.md`](docs/components/containment/aura-dialog.md) |
| **Feedback** | `AuraBadge` / `AuraBadgedBox` | `Dot`, `Small`, and `Medium` semantic pills and `"99+"` count badges | [`aura-badge.md`](docs/components/feedback/aura-badge.md) |
| **Layout** | `AuraScaffold` & `AuraAdaptiveTwoPane` & `AuraAdaptiveGrid` | Zero-M3 screen scaffolds and adaptive split/grid containers | [`aura-scaffold.md`](docs/components/layout/aura-scaffold.md) · [`aura-adaptive-two-pane.md`](docs/components/layout/aura-adaptive-two-pane.md) |

### 4. Automated `Specs ↔ Code` Quality Gate (`EVAL-001` – `EVAL-006`)

Every component specification and Kotlin source file is verified by [`SpecCodeConsistencyTest`](design-system/src/test/kotlin/com/example/ds/contract/SpecCodeConsistencyTest.kt) against [`01-component-evaluation-ruleset.md`](docs/components/01-component-evaluation-ruleset.md):

- **`EVAL-001`** — Explicit usage principles (`When to Use`, `When NOT to Use`, and `Do / Don't` rows).
- **`EVAL-002`** — Semantic token compliance (`AuraTheme.colors` / `AuraTheme.typography`, zero `material3` imports, zero hardcoded hex colors in production code).
- **`EVAL-003`** — Responsive behavior across `ExtraSmall`, `Compact`, `Square`, `UltraWide`, and `Expanded` profiles.
- **`EVAL-004`** — `AuraTheme.motion` interaction contract and Reduced Motion fallback.
- **`EVAL-005`** — Deterministic state precedence and `48dp` minimum accessible hit targets.
- **`EVAL-006`** — Cross-platform API blocks (`ANDROID`, `IOS`, `WEB`) with automated Kotlin parameter signature verification.
