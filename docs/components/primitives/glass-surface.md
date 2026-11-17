---
component_id: "aura.component.glass-surface"
name: "GlassSurface"
category: "primitives"
status: "ready"
layer: "chrome"
material_allowed: ["Solid", "Glass", "LiquidGlass"]
motion_track: "Standard"
version: "0.1.0"
platforms:
  android: "com.example.ds.component.GlassSurface"
  ios: "AuraDS.GlassSurface (planned, .ultraThinMaterial)"
  web: "@aura-ds/react.GlassSurface (planned, backdrop-filter)"
tokens:
  - "color.surface.floating"
  - "elevation.level2"
  - "motion.stateChangeDurationMillis"
  - "motion.pressInSpring"
  - "motion.spatialSpring"
  - "motion.minPressDurationMillis"
---

# GlassSurface

> Cross-platform specification for Aura's floating material primitive. Renders `Glass` (frosted blur) or `LiquidGlass` (AGSL refraction) over the live backdrop, and degrades to `Solid` through the single `AuraTheme.effectiveMaterial(...)` pipeline. See [00-primitives-principles.md](00-primitives-principles.md) and [06-materiality-and-depth.md](../../foundation/06-materiality-and-depth.md).

---

## 1. Intent & Usage Principles

### Problem

Floating chrome (top pill bar, bottom island, dialogs, snackbars) needs real translucency: content scrolling behind it should blur or refract while foreground text stays sharp. Building blur per component would fragment fallback behavior across API levels and accessibility settings.

### When to Use

- As the container of floating `chrome` (Layer 2) and `spatial` (Layer 3) surfaces: `AuraTopAppBar`, `AuraBottomIsland`, `AuraDialog` (`Glass`), `AuraSearchBar` (`Glass`), `AuraSnackbar`.
- For a custom floating control that sits over live content inside `FloatingGlassScaffold`.

### When NOT to Use & Alternatives

| Situation | Alternative | Reason |
|---|---|---|
| Inline content (cards in a list, form fields, settings rows) | `AuraCard` (`Filled` / `Outlined`) | `VIS-RULE-001`: content layer stays `Solid` |
| Static opaque panel | Plain `Box` with `AuraTheme.colors.surface.*` | No backdrop to sample; blur costs GPU budget |
| More than 3 live-blur surfaces on screen | Merge chrome or use `Solid` | `VIS-RULE-006` performance budget |

### Context Rules

- **`GLS-RULE-001` (Chrome & Spatial Only):** Invoke `GlassSurface` only from `chrome` / `spatial` layer components (`PRM-RULE-004`, `VIS-RULE-001`).
- **`GLS-RULE-002` (One Liquid Hero):** At most one `LiquidGlass` surface per screen (`VIS-RULE-002`, `VIS-RULE-006`).
- **`GLS-RULE-003` (Backdrop Source Required for Real Blur):** Place the scrolling canvas under `Modifier.glassBackdropSource(state)` (done automatically by `FloatingGlassScaffold`). Without a recorded backdrop, `GlassSurface` renders tint and stroke only.
- **`GLS-RULE-004` (Material via Theme):** `glassStyle` is a compatibility hint; the rendered material is always `AuraTheme.effectiveMaterial(glassStyle.toMaterial())`.

### Do / Don't

| Do | Don't |
|---|---|
| Host floating bars inside `FloatingGlassScaffold` so `LocalGlassBackdropState` is provided. | Stack `GlassSurface` inside an `AuraCard` list item for decoration. |
| Call `backdropState.invalidateBackdrop()` (or use `rememberFloatingScrollProgress`) when custom scroll content changes. | Build a separate `RenderEffect.createBlurEffect` in a component. |
| Strengthen tint (`surfaceAlphaMultiplier`) or rely on edge scrims when backdrops are busy. | Lower tint opacity until text drops below `4.5:1`. |

---

## 2. Anatomy & Canonical Part IDs

```text
root (Box: dual shadow → clip(shape))
 ├── backdrop   (graphicsLayer renderEffect: blur / AGSL refraction; draws translated Layer 0 snapshot)
 ├── tint       (adaptive surface wash; surface.floating at 98% in Solid fallback)
 ├── stroke     (diagonal specular gradient border)
 └── content    (BoxScope slot, rendered sharp above the effect layer)
```

| Part ID | Responsibility | Required |
|---|---|---|
| `root` | Shape, clip, two-layer shadow from `AuraTheme.elevation.level2` | Yes |
| `backdrop` | Samples `GlassBackdropState` and applies the material `RenderEffect` | No (skipped in `Solid` fallback) |
| `tint` | Translucent wash keeping foreground legible | Yes |
| `stroke` | Specular edge highlight (`borderWidth`) | Yes |
| `content` | Caller content | Yes |

---

## 3. Material Matrix & Fallbacks

| Requested (`glassStyle`) | API 33+ | API 31–32 | API 26–30 | Reduced Transparency |
|---|---|---|---|---|
| `LIQUID` → `LiquidGlass` | AGSL bevel refraction + dispersion | Frosted soft blur | `Solid` effect: tint over unblurred backdrop | `Solid` (`surface.floating` 98%, no backdrop) |
| `FROSTED` → `Glass` | Blur `26px` + saturation boost | Blur + saturation | `Solid` effect: tint over unblurred backdrop | `Solid` (`surface.floating` 98%, no backdrop) |

`GlassStyle` is `@Deprecated` in favor of `AuraMaterial`; it remains as the parameter type for compatibility and maps through `GlassStyle.toMaterial()`.

---

## 4. State Precedence

$$\text{Reduced Transparency (Solid fallback)} \succ \text{SDK capability downgrade} \succ \text{requested material} $$

Tint color changes animate with `motion.stateChangeDurationMillis` (snap under Reduced Motion). Press feedback is opt-in through `Modifier.fluidPressScale`.

---

## 5. Semantic Token Bindings

| Part | Property | Token Binding |
|---|---|---|
| `tint` | Solid fallback fill | `AuraTheme.colors.surface.floating` |
| `tint` | Override | `tintColorOverride` (callers pass semantic colors only) |
| `root` | Shadow ladder | `AuraTheme.elevation.level2` (ambient + key) |
| `tint` | Color transition | `AuraTheme.motion.stateChangeDurationMillis` |
| `fluidPressScale` | Press in / release | `AuraTheme.motion.pressInSpring`, `spatialSpring`, `minPressDurationMillis` |

---

## 6. Interaction Motion Contract

- **Track (`Standard`):** Material tint cross-fades with `stateChangeDurationMillis`; `Modifier.fluidPressScale` compresses with `pressInSpring` and rebounds with `spatialSpring`, holding at least `minPressDurationMillis`.
- **`LiquidMetaballBridge`:** Connects two pills during merge/split with a blur + alpha-threshold neck; progress is driven by the caller's `mergeProgress` (no internal timeline).
- **Reduced Motion (`ANIMATOR_DURATION_SCALE == 0`):** Tint changes snap; `fluidPressScale` stays at `1f` (`MOT-RULE-007`).
- **Verification:** `ComponentMotionBehaviorTest` asserts the Reduced Transparency `Solid` fallback (`aura_glass_surface_solid_fallback`) under `mainClock`.

---

## 7. Responsive Layout Behavior (`Smallest Width × Display Ratio`)

`GlassSurface` has no intrinsic size; its bounds come from the composing chrome. Material cost scales with area, so composing components cap width.

| Profile | Behavior |
|---|---|
| **`ExtraSmall`** (Cover `sw < 360dp`) | Composing chrome shrinks; corner radius is clamped to half the shortest side |
| **`Compact`** (Phone Portrait) | Default blur radius (`26px`) |
| **`Medium` / `Square`** (Foldable `~1:1`) | Floating chrome caps width (e.g., top bar `640px`) so blur area stays bounded |
| **`UltraWide`** (Phone Landscape) | Same as `Square`; short heights favour compact docked chrome over large glass panels |
| **`Expanded`** (Tablet / DeX) | Pane-scoped chrome (`NAV-RULE-004`); at most 3 live-blur surfaces in view (`VIS-RULE-006`) |

---

## 8. Accessibility

- **Reduced Transparency:** When `AuraTheme.reduceTransparency` is active (or Battery Saver / High-Contrast Text), the surface renders `Solid` (`surface.floating` at 98%) with no backdrop sampling (`VIS-RULE-004`).
- **Contrast:** Foreground text on `Glass` / `LiquidGlass` must keep `>= 4.5:1` contrast; icons and hairline borders `>= 3:1` (`VIS-RULE-003`). Composing components raise `surfaceAlphaMultiplier` or rely on `FloatingGlassScaffold` edge scrims when backdrops are busy.
- The effect layer is isolated from `content`, so text and icons are never blurred or refracted.
- `GlassSurface` adds no semantics; roles and labels belong to the composing component.

---

## 9. Platform Bindings (Layer B)

### 9.1 Android (Jetpack Compose)

<!-- API:ANDROID:BEGIN -->
```kotlin
@Deprecated("Use AuraMaterial (Glass / LiquidGlass) via GlassStyle.toMaterial().")
enum class GlassStyle { FROSTED, LIQUID }

fun GlassStyle.toMaterial(): AuraMaterial

@Stable
class GlassBackdropState {
    fun invalidateBackdrop()
}

@Composable
fun rememberGlassBackdropState(): GlassBackdropState

val LocalGlassBackdropState: ProvidableCompositionLocal<GlassBackdropState?>

fun Modifier.glassBackdropSource(state: GlassBackdropState): Modifier

fun Modifier.fluidPressScale(
    interactionSource: InteractionSource,
    enabled: Boolean = true,
    pressedScale: Float = 0.92f,
): Modifier

@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    glassStyle: GlassStyle = GlassStyle.LIQUID,
    shape: Shape = RoundedCornerShape(50),
    cornerRadiusHint: Dp = 28.dp,
    backdropState: GlassBackdropState? = LocalGlassBackdropState.current,
    dispersionBoost: Float = 1f,
    surfaceAlphaMultiplier: Float = 1f,
    showShadow: Boolean = true,
    blurRadius: Dp = 26.dp,
    saturationBoost: Float = 1.2f,
    tintColorOverride: Color? = null,
    borderColorOverride: Color? = null,
    borderWidth: Dp = 1.dp,
    elevationAlphaMultiplier: Float = 1f,
    content: @Composable BoxScope.() -> Unit,
)

@Composable
fun LiquidMetaballBridge(
    gapWidthDp: Dp,
    heightDp: Dp,
    glassStyle: GlassStyle,
    mergeProgress: Float,
    modifier: Modifier = Modifier,
)
```
<!-- API:ANDROID:END -->

### 9.2 iOS (SwiftUI Reference)

<!-- API:IOS:BEGIN -->
```swift
struct GlassSurface<Content: View>: View {
    var material: AuraMaterial = .liquidGlass   // resolved via auraTheme.effectiveMaterial
    var shape: AnyShape = AnyShape(Capsule())
    var showShadow: Bool = true
    @ViewBuilder var content: () -> Content
}
```
<!-- API:IOS:END -->

### 9.3 Web (React / TypeScript Reference)

<!-- API:WEB:BEGIN -->
```tsx
export interface GlassSurfaceProps {
  material?: 'Glass' | 'LiquidGlass';   // Solid when prefers-reduced-transparency
  radius?: number;
  blurRadius?: number;
  showShadow?: boolean;
  children: React.ReactNode;
}
```
<!-- API:WEB:END -->
