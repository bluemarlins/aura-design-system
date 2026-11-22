# Aura Design System

**Version `2.1.0`** · One UI-inspired, zero-Material 3 Jetpack Compose design system featuring Glassmorphism, tactile spring physics, and two-axis adaptive layouts.

---

## Overview

**Aura** (`aura-design-system`) is a general-purpose Android (Jetpack Compose) design system built directly on Compose Foundation (`androidx.compose.foundation`) and Compose UI without any `androidx.compose.material3` dependency in the `:design-system` library module.

Inspired by Samsung One UI's calm surface hierarchy and blue-led clarity, Aura pairs high-legibility solid surfaces with first-class **Dual-Materiality Glassmorphism** (`AuraMaterial.Solid` / `Glass` / `LiquidGlass` + `reduceTransparency` fallbacks), **Dual-Track Interaction Physics** (`AuraMotionProfile.Standard` / `Expressive` via `AuraMotion`), a 5-step **Z-Axis Depth Ladder** (`AuraElevation`), and **Smallest Width × Display Ratio** responsive layouts (`AuraWindowInfo` / `AuraLayout`).

Every component in `:design-system` is backed by a bidirectional **`Specs ↔ Code`** Markdown contract under [`docs/components/`](docs/components/), verified automatically on every build by [`SpecCodeConsistencyTest`](design-system/src/test/kotlin/io/github/bluemarlins/aura/contract/SpecCodeConsistencyTest.kt), [`MotionTokenInvariantTest`](design-system/src/test/kotlin/io/github/bluemarlins/aura/contract/MotionTokenInvariantTest.kt), [`MaterialResolutionTest`](design-system/src/test/kotlin/io/github/bluemarlins/aura/foundation/theme/MaterialResolutionTest.kt), and Robolectric `mainClock` tests ([`ComponentMotionBehaviorTest`](design-system/src/test/kotlin/io/github/bluemarlins/aura/motion/ComponentMotionBehaviorTest.kt)) against rules `EVAL-001` through `EVAL-008`.

### Repository Modules & Assets

| Module | Description |
|---|---|
| [`:design-system`](design-system/) | Publishable Android library (`io.github.bluemarlins.aura`) containing foundation tokens, `AuraTheme`, `GlassSurface`, and all UI components. |
| [`:design-system-preview`](design-system-preview/) | Interactive catalog app showcasing Light/Dark themes, Interaction Principles playgrounds, token matrices, and live components. |
| [`docs/`](docs/) | Foundation specifications (`docs/foundation/`) and bidirectional component specs (`docs/components/`). |
| [`skills/aura-design-system/`](skills/aura-design-system/) | Distributable **Consumer App Skill Kit** (`SKILL.md`, API/token references, recipes, AAR sync & app compliance verification scripts) for AI vibe-coding in external Android apps. |

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
    implementation("com.github.bluemarlins:aura-design-system:2.1.0")
}
```

Compose `ui`, `ui-graphics`, and `foundation` come in transitively. Aura does not export an icon set, so if your app uses `Icons.*` glyphs (as the samples below do), also add `implementation("androidx.compose.material:material-icons-core")`.

#### Option 2 — Direct AAR from GitHub Releases (`2.1.0`)

1. Download `aura-design-system-2.1.0.aar` (and optionally `aura-design-system-2.1.0-sources.jar`) from the [GitHub Releases (`2.1.0`)](https://github.com/bluemarlins/aura-design-system/releases/tag/2.1.0) page.
2. Place the `.aar` file inside your app module's `libs/` directory (e.g., `app/libs/aura-design-system-2.1.0.aar`).
3. Declare the AAR alongside Compose Foundation/UI dependencies in `app/build.gradle.kts`:

```kotlin
dependencies {
    implementation(files("libs/aura-design-system-2.1.0.aar"))

    implementation(platform("androidx.compose:compose-bom:2025.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.compose.material:material-icons-core") // only for Icons.* glyphs
}
```

#### Option 3 — Local Maven (`mavenLocal()`)

Publish the `2.1.0` artifact locally from a clone of this repository:

```powershell
.\gradlew.bat :design-system:publishToMavenLocal
```

Then enable `mavenLocal()` in `settings.gradle.kts` and add:

```kotlin
implementation("com.github.bluemarlins:aura-design-system:2.1.0")
```

### Migrating from 1.x to 2.0.0

`2.0.0` renames the Kotlin package from `com.example.ds` to `io.github.bluemarlins.aura`. The Maven coordinates (`com.github.bluemarlins:aura-design-system`) and every public API name are unchanged, so the upgrade is a find-and-replace on imports:

1. Bump the dependency (or the local AAR file name) to `2.0.0` or later.
2. Replace `com.example.ds.` with `io.github.bluemarlins.aura.` in your Kotlin sources, for example:

```powershell
Get-ChildItem -Recurse -Include *.kt,*.kts | ForEach-Object { (Get-Content $_ -Raw) -replace 'com\.example\.ds\.', 'io.github.bluemarlins.aura.' | Set-Content $_ -NoNewline -Encoding utf8 }
```

### Quick Start

#### 1. Wrap your UI with `AuraTheme`

`AuraTheme` injects semantic colors (`AuraTheme.colors`), responsive typography (`AuraTheme.typography`), spring motion physics (`AuraTheme.motion`), window metrics (`AuraTheme.window`), and layout tokens (`AuraTheme.layout`):

```kotlin
import androidx.compose.runtime.Composable
import io.github.bluemarlins.aura.foundation.theme.AuraColorMode
import io.github.bluemarlins.aura.foundation.theme.AuraTheme

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
import io.github.bluemarlins.aura.component.AuraButton
import io.github.bluemarlins.aura.component.AuraButtonVariant
import io.github.bluemarlins.aura.component.AuraCard
import io.github.bluemarlins.aura.component.AuraCardVariant
import io.github.bluemarlins.aura.component.AuraNumberField
import io.github.bluemarlins.aura.component.AuraRatingBar
import io.github.bluemarlins.aura.component.AuraScaffold
import io.github.bluemarlins.aura.component.AuraSlider
import io.github.bluemarlins.aura.component.AuraText
import io.github.bluemarlins.aura.component.AuraTextField
import io.github.bluemarlins.aura.component.AuraTopAppBar
import io.github.bluemarlins.aura.foundation.theme.AuraTheme

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
import io.github.bluemarlins.aura.component.AuraBottomIslandItem
import io.github.bluemarlins.aura.component.FloatingBottomIsland
import io.github.bluemarlins.aura.component.FloatingGlassScaffold
import io.github.bluemarlins.aura.component.FloatingTopPillBar
import io.github.bluemarlins.aura.component.GlassStyle
import io.github.bluemarlins.aura.component.glassBackdropSource
import io.github.bluemarlins.aura.component.rememberFloatingScrollProgress

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
        val backdropState = io.github.bluemarlins.aura.component.LocalGlassBackdropState.current
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

### AI Agent Skill for App Developers (`skills/aura-design-system/`)

Building or vibe-coding an external Android app with Aura? Install the standalone **Consumer Skill Kit** from [`skills/aura-design-system/`](skills/aura-design-system/) so coding agents automatically apply `AuraTheme` tokens, `Standard`/`Expressive` motion rules, 4-layer `Glass`/`LiquidGlass` discipline, and zero-M3 guardrails.

#### Install the skill only (no build required)

Run one of these from the root of your Android app, then ask your agent: *"Apply Aura Design System to my app."* The skill walks the agent through adding the JitPack dependency, wrapping the root in `AuraTheme`, migrating Material 3 screens, and auditing the result.

```bash
# Any agent supported by the skills CLI (Claude Code, Codex, Gemini CLI, ...)
npx skills add bluemarlins/aura-design-system
```

```bash
# Or copy it manually (use .agents/skills/ instead for Codex / Gemini CLI)
git clone --depth 1 https://github.com/bluemarlins/aura-design-system.git .aura-tmp
mkdir -p .claude/skills && cp -r .aura-tmp/skills/aura-design-system .claude/skills/
rm -rf .aura-tmp
```

#### Build the AAR and sync it with the skill (Windows, from a clone of this repository)

```powershell
# 1. Build latest :design-system release AAR and sync AAR + skill into your consumer app
#    (Installs AAR to <app>/app/libs/ and skills to .agents/skills/ & .claude/skills/)
powershell -ExecutionPolicy Bypass -File skills/aura-design-system/scripts/install-aura-to-app.ps1 `
    -TargetAppDir C:\path\to\your-android-app

# 2. Audit consumer app Kotlin files for Material 3 imports, hardcoded Color(0x...), .sp, or tween/spring literals
powershell -ExecutionPolicy Bypass -File skills/aura-design-system/scripts/verify-aura-app.ps1 `
    -TargetAppDir C:\path\to\your-android-app `
    -FailOnViolation
```

**Skill Kit Contents:**
- [`SKILL.md`](skills/aura-design-system/SKILL.md) — Step-by-step "apply Aura to an app" workflow, Gradle setup, core rules, Material 3 → Aura component map, and progressive disclosure guide
- [`references/tokens-and-theme.md`](skills/aura-design-system/references/tokens-and-theme.md) — `AuraColors`, 16 `AuraTypography` roles, `AuraLayout`, `AuraMotion`, `AuraElevation`, and `AuraMaterial`
- [`references/components-api.md`](skills/aura-design-system/references/components-api.md) — Complete Kotlin signatures across all Ready components and `GlassSurface` helpers
- [`references/motion-and-visual-rules.md`](skills/aura-design-system/references/motion-and-visual-rules.md) — 4-layer surface discipline (`VIS-RULE-001`–`007`) & motion rules (`MOT-RULE-001`–`007`)
- [`references/recipes-and-patterns.md`](skills/aura-design-system/references/recipes-and-patterns.md) — Copy-ready `FloatingGlassScaffold`, `AuraScaffold`, `AuraAdaptiveTwoPane`, `AuraDialog`, and app gap-component templates

### Running the Preview App & Verifying Contracts

```powershell
# Run all unit tests (SpecCodeConsistencyTest EVAL-001..008, MotionTokenInvariantTest, MaterialResolutionTest, ComponentMotionBehaviorTest)
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
- **Tactile Spring Cushion & Dual-Track Motion (`AuraMotion` / `AuraMotionProfile`)**: Replaces Material radial ripples with GPU-accelerated spring compression (`pressInSpring`, `pressOutSpring`, `iconSpring`), dual-track profiles (`Standard` for restrained productivity flows vs. `Expressive` for spatial chrome and modal choreography), `spatialSpring`/`elasticStretchSpring`, staggered delays capped at `400ms`, asymmetric attack/release durations (`90ms` / `180ms`), `110ms` minimum visual press latch, and automatic Reduced Motion fallback (`ANIMATOR_DURATION_SCALE == 0f` or `AuraMotion.reduceMotion`).
- **Two-Axis Responsive Layout (`Smallest Width × Display Ratio`)**: Classifies windows across `AuraWidthClass` (`ExtraSmall`, `Compact`, `Medium`, `Expanded`), `AuraHeightClass`, and `AuraDisplayRatio` (`UltraTall`, `Portrait`, `Square`, `Landscape`, `UltraWide`) to provide deterministic margins, column counts (`gridColumns`), `maxContentWidth`, and two-pane split ratios (`AuraAdaptiveTwoPane`, `AuraAdaptiveGrid`).
- **Z-Axis Depth Ladder (`AuraElevation`)**: 5 stepped elevation & protective scrim levels (`level0`–`level4`, `0.dp`–`24.dp`) governing shadows and gradient edge scrims cleanly across layers.

### 2. Dual-Materiality Glassmorphism & Spatial Navigation

- **`AuraMaterial` & `GlassSurface` (`Solid`, `Glass`/`FROSTED`, `LiquidGlass`/`LIQUID`)**: Live Layer 0 pixel sampling (`rememberGlassBackdropState` + `Modifier.glassBackdropSource`) routed through `resolveEffectiveMaterial(...)` and `AuraTheme.reduceTransparency`:
  - **API 33+**: AGSL `RuntimeShader` droplet bevel refraction, chromatic RGB dispersion, and inner Fresnel highlights in `LiquidGlass` mode (`<= 1` hero per screen).
  - **API 31–32**: `RenderEffect` backdrop blur and saturation boost in `Glass` mode (`<= 3` live-blur surfaces per viewport).
  - **API 26–30 & Reduced Transparency (`PowerSaveMode` / High-Contrast Text)**: Clean opaque `Solid` (`aura_glass_surface_solid_fallback`) or translucent bordered fallback.
- **`FloatingGlassScaffold`**: 4-layer edge-to-edge spatial container coordinating Layer 0 live backdrop recording, Layer 1 edge scrims, Layer 2 floating chrome clearance (`calculateBottomIslandContentPadding`), and Layer 3 dismissible overlays.
- **`AuraTopAppBar` / `FloatingTopPillBar`**: Supports `FloatingCapsule`, `FloatingSplit`, and `SurfaceDocked` modes, scroll-reactive opacity/pill transitions (`rememberFloatingScrollProgress`), `MorphingMorePopover`, and Metaball action splitting. `inlineExpandedTitle` (1.2.0) keeps the large title on the action row.
- **`AuraDropdownMenu`** (1.2.0): Anchored overflow / filter menu with titled `AuraMenuSection`s and single-choice `AuraMenuItem` check marks; opens below its trigger and flips above when needed.
- **`AuraFloatingActionBar`** (1.3.0): Floating glass action dock for task screens (editors, wizards, detail). Rides above the IME and pairs with `FloatingGlassScaffold(bottomIslandHeight = AuraFloatingActionBarDefaults.Height)`.
- **`AuraSkeleton` / `AuraSkeletonText`** (1.3.0): Pulsing loading placeholders so screens never show a fake empty state before their first load.
- **`AuraBottomIsland` / `FloatingBottomIsland`**: Stadium capsule navigation bar with spring-driven sliding active indicator pill, velocity-coupled **Elastic Squish & Stretch**, and `LiquidMetaballBridge` split auxiliary FAB.

### 3. Complete Component Catalog (30 Ready Specs)

The full Foundation → Primitives → Components → Patterns tree is in [`02-component-hierarchy.md`](docs/components/02-component-hierarchy.md).

| Category | Component | Description | Spec |
|---|---|---|---|
| **Primitives** | `AuraText` / `AuraIcon` | Token-bound text and icon primitives that inherit content color and text style | [`aura-text.md`](docs/components/primitives/aura-text.md) · [`aura-icon.md`](docs/components/primitives/aura-icon.md) |
| **Primitives** | `GlassSurface` | Single `Glass` / `LiquidGlass` rendering pipeline with `Solid` fallback, backdrop sampling, and `LiquidMetaballBridge` | [`glass-surface.md`](docs/components/primitives/glass-surface.md) |
| **Foundation** | `AuraColors` / `AuraPalette` | One UI-inspired Light & OLED Dark semantic color system | [`02-color.md`](docs/foundation/02-color.md) |
| **Foundation** | `AuraTypography` & `AuraLayout` | 16-role typography scale & 2-axis `Smallest Width × Display Ratio` layout | [`04-typography.md`](docs/foundation/04-typography.md) · [`05-responsive-layout.md`](docs/foundation/05-responsive-layout.md) |
| **Foundation** | `AuraShapes` & spacing | 6-step corner radius scale and `4dp` spacing rhythm | [`08-shape-and-spacing.md`](docs/foundation/08-shape-and-spacing.md) |
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
| **Navigation** | `AuraAdaptiveBarState` | Scroll-linked phase state that syncs the top bar and bottom island (`BindAdaptiveBarTo*`) | [`aura-adaptive-bar-state.md`](docs/components/navigation/aura-adaptive-bar-state.md) |
| **Containment** | `AuraCard` | `Elevated`, `Filled`, `Outlined`, and `Glass` containers with tactile press response | [`aura-card.md`](docs/components/containment/aura-card.md) |
| **Containment** | `AuraDialog` | `Standard`, `Destructive`, and `Glass` modal confirmation surfaces with adaptive button stacking | [`aura-dialog.md`](docs/components/containment/aura-dialog.md) |
| **Containment** | `AuraDivider` | `Horizontal` / `Vertical` hairline separators with `Subtle`, `Default`, and `Strong` tones | [`aura-divider.md`](docs/components/containment/aura-divider.md) |
| **Feedback** | `AuraBadge` / `AuraBadgedBox` | `Dot`, `Small`, and `Medium` semantic pills and `"99+"` count badges | [`aura-badge.md`](docs/components/feedback/aura-badge.md) |
| **Feedback** | `AuraProgressBar` (`Linear` / `Step`) | Continuous & segmented step progress bars with semantic status variants | [`aura-progress-bar.md`](docs/components/feedback/aura-progress-bar.md) |
| **Feedback** | `AuraSnackbar` / `AuraSnackbarHost` | Queued transient feedback banners with `Solid` / `Glass` materiality and scaffold clearance | [`aura-snackbar.md`](docs/components/feedback/aura-snackbar.md) |
| **Layout** | `AuraScaffold` & `AuraAdaptiveTwoPane` & `AuraAdaptiveGrid` | Zero-M3 screen scaffolds and adaptive split/grid containers | [`aura-scaffold.md`](docs/components/layout/aura-scaffold.md) · [`aura-adaptive-two-pane.md`](docs/components/layout/aura-adaptive-two-pane.md) · [`aura-adaptive-grid.md`](docs/components/layout/aura-adaptive-grid.md) |
| **Layout** | `FloatingGlassScaffold` | 4-layer edge-to-edge canvas for floating glass chrome | [`floating-glass-scaffold.md`](docs/components/layout/floating-glass-scaffold.md) |

### 4. Automated `Specs ↔ Code` Quality Gate (`EVAL-001` – `EVAL-008`)

Every component specification and Kotlin source file is verified by [`SpecCodeConsistencyTest`](design-system/src/test/kotlin/io/github/bluemarlins/aura/contract/SpecCodeConsistencyTest.kt), [`MotionTokenInvariantTest`](design-system/src/test/kotlin/io/github/bluemarlins/aura/contract/MotionTokenInvariantTest.kt), [`MaterialResolutionTest`](design-system/src/test/kotlin/io/github/bluemarlins/aura/foundation/theme/MaterialResolutionTest.kt), and [`ComponentMotionBehaviorTest`](design-system/src/test/kotlin/io/github/bluemarlins/aura/motion/ComponentMotionBehaviorTest.kt) against [`01-component-evaluation-ruleset.md`](docs/components/01-component-evaluation-ruleset.md):

- **`EVAL-001`** — Explicit usage principles (`When to Use`, `When NOT to Use`, and `Do / Don't` rows).
- **`EVAL-002`** — Semantic token compliance (`AuraTheme.colors` / `AuraTheme.typography`, zero `material3` imports, zero hardcoded hex colors in production code).
- **`EVAL-003`** — Responsive behavior across `ExtraSmall`, `Compact`, `Square`, `UltraWide`, and `Expanded` profiles.
- **`EVAL-004`** — `AuraTheme.motion` interaction contract, zero hardcoded numeric `tween`/`spring` literals in production components, and Reduced Motion fallback.
- **`EVAL-005`** — Deterministic state precedence and `48dp` minimum accessible hit targets.
- **`EVAL-006`** — Cross-platform API blocks (`ANDROID`, `IOS`, `WEB`) with automated Kotlin parameter signature verification.
- **`EVAL-007`** — Layer & material compliance (`layer` & `material_allowed` frontmatter, no `GlassSurface` in `content`/`canvas` layers, `Reduced Transparency` & `4.5:1` contrast docs, SDK fallback matrix).
- **`EVAL-008`** — Motion track governance (`motion_track` frontmatter) and deterministic Robolectric + Compose `mainClock` verification (`ComponentMotionBehaviorTest`).

[`SpecMeasurementTest`](design-system/src/test/kotlin/io/github/bluemarlins/aura/contract/SpecMeasurementTest.kt) also renders every component whose spec has a `Quick Spec` block (§3) and compares heights, widths, padding, type sizes, and corner radii with the spec, in the default window and in the `ExtraSmall`, `Compact`, `Square`, `UltraWide`, and `Expanded` profiles.
