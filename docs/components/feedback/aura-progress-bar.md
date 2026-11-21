---
id: "aura.component.progress-bar"
name: "AuraProgressBar"
category: "feedback"
status: "ready"
version: "1.1.0"
layer: "content"
material_allowed: ["Solid"]
motion_track: "Standard"
platforms:
  android: "io.github.bluemarlins.aura.component.AuraProgressBar"
  ios: "AuraDS.AuraProgressBar (planned)"
  web: "@aura-ds/react.AuraProgressBar (planned)"
tokens:
  - "colors.brand.primary"
  - "colors.surface.sunken"
  - "colors.status.success.solid"
  - "colors.status.warning.solid"
  - "colors.status.error.solid"
  - "colors.status.info.solid"
  - "shapes.extraSmall"
  - "shapes.small"
  - "motion.stateChangeDurationMillis"
---

# AuraProgressBar

> Deterministic progress indicators (`AuraLinearProgressBar` and `AuraStepProgressBar`) backed by `AuraTheme.colors`, `AuraTheme.shapes`, and `AuraTheme.motion`.

---

## 1. Intent & Usage Principles

### Problem

Users need to see how far a determinate task or multi-step flow has advanced so waiting or step-by-step work feels predictable.

### When to Use

- **`AuraLinearProgressBar`:** Communicating continuous completion fraction (`0f..1f`), readiness thresholds, or quota usage inside content cards.
- **`AuraStepProgressBar`:** Communicating discrete multi-step wizard or onboarding progress (`currentStep` out of `totalSteps`).

### When NOT to Use & Alternatives

- **Interactive value scrubbing:** Use `AuraSlider` when the user drags to adjust a value.
- **Inline status labels or unread counts:** Use `AuraBadge`.
- **Transient operation results:** Use `AuraSnackbar`.

### Context Rules

- **`PRG-RULE-001` (Bounded Progress):** Clamp continuous `progress` to `0f..1f` and discrete `currentStep` to `0..totalSteps` so layout widths never overflow.
- **`PRG-RULE-002` (Semantic Color Alignment):** Use `Brand` for neutral goal completion and `Success` / `Warning` / `Error` / `Info` only when communicating explicit semantic status (`FBK-RULE-001`).

### Do / Don't

| Do | Don't |
|---|---|
| Pair `AuraLinearProgressBar` with clear text context (e.g., `"4 / 6 entries"`). | Render a bare progress bar with no explanation of what is progressing. |
| Use `AuraStepProgressBar` for 2–8 discrete questionnaire or onboarding steps. | Render `AuraStepProgressBar` with 30+ tiny slivers; switch to `AuraLinearProgressBar`. |
| Consume `AuraTheme.shapes` and `AuraTheme.colors.surface.sunken` track tokens. | Hardcode corner radii or raw hex colors inside app-local progress boxes. |

---

## 2. Anatomy & Canonical Part IDs

```text
  AuraLinearProgressBar:
  [████████████░░░░░░░░░░░░░]   6.dp height, AuraTheme.shapes.small

  AuraStepProgressBar (currentStep = 3, totalSteps = 5):
  [████] [████] [████] [░░░░] [░░░░]   4.dp height, 4.dp gap, AuraTheme.shapes.extraSmall
```

| Part ID | Responsibility | Required | Semantics Owner |
|---|---|---|---|
| `track` | Full-width background (`surface.sunken`) | Yes | `progressBarRangeInfo` + `contentDescription` |
| `indicator` | Filled portion for `progress` (`AuraLinearProgressBar`) | Conditional | Merged into `track` |
| `segment` | One of `totalSteps` blocks; active up to `currentStep` (`AuraStepProgressBar`) | Conditional | Merged into `track` |

---

## 3. Variant, Size & Material Matrix

| Variant (`AuraProgressVariant`) | Active Indicator Token | Track Token |
|---|---|---|
| `Brand` (default) | `AuraTheme.colors.brand.primary` | `AuraTheme.colors.surface.sunken` |
| `Success` | `AuraTheme.colors.status.success.solid` | `AuraTheme.colors.surface.sunken` |
| `Warning` | `AuraTheme.colors.status.warning.solid` | `AuraTheme.colors.surface.sunken` |
| `Error` | `AuraTheme.colors.status.error.solid` | `AuraTheme.colors.surface.sunken` |
| `Info` | `AuraTheme.colors.status.info.solid` | `AuraTheme.colors.surface.sunken` |

| Component | Default Height | Shape | Gap |
|---|---|---|---|
| `AuraLinearProgressBar` | `6dp` | `shapes.small` | — |
| `AuraStepProgressBar` | `4dp` | `shapes.extraSmall` | `4dp` |

- **Material:** `Solid` only (`layer: content`).

---

## 4. State Precedence & Behavior

Passive component: no pointer, focus, or pressed states. `progress` is clamped to `0f..1f`; `currentStep` is clamped to `0..totalSteps`. Changes animate the indicator and are announced through `progressBarRangeInfo`.

---

## 5. Semantic Token Bindings

| Element | Variant / State | Light / Dark Semantic Token |
|---|---|---|
| Inactive Track | `Rest` | `AuraTheme.colors.surface.sunken` |
| Active Indicator | `By Variant` | `brand.primary` / `status.{success,warning,error,info}.solid` |
| Corner Shape | `Linear` / `Step` | `AuraTheme.shapes.small` / `AuraTheme.shapes.extraSmall` |

---

## 6. Interaction Motion Contract

Consumes `AuraTheme.motion`:

1. **Progress & Step Transitions:** `AuraLinearProgressBar` animates fraction changes and `AuraStepProgressBar` animates segment activation colors via `tween(durationMillis = AuraTheme.motion.stateChangeDurationMillis)`.
2. **Reduced Motion:** Snaps immediately (`snap()`) when `AuraTheme.motion.reduceMotion` is `true` while preserving semantic `ProgressBarRangeInfo`.

---

## 7. Responsive Layout Behavior (`Smallest Width × Display Ratio`)

| Width / Display Profile | Progress Bar Adaptation |
|---|---|
| `ExtraSmall` (`< 360dp`) | Expands `fillMaxWidth()` within narrow margins while keeping `4dp` segment gaps legible. |
| `Compact` (`360–599dp`) | Fills container or card inner width cleanly. |
| `Square` (`~1:1` Foldable) | Equal-weight step segments (`Modifier.weight(1f)`) distribute evenly across wider viewports. |
| `UltraWide` (`> 21:9`) | Bounded by parent `maxContentWidth` form/card container to avoid excessively wide bars. |
| `Expanded` (`>= 840dp`) | Scales cleanly inside multi-column cards and detail panes. |

---

## 8. Accessibility & Guardrails

- Exposes `ProgressBarRangeInfo` via `Modifier.semantics(mergeDescendants = true)` so TalkBack announces completion ratio accurately.
- Supports optional `contentDescription` for meaningful spoken summaries (e.g., `"Step 2 of 4"`).
- Track (`surface.sunken`) and active indicator (`brand.primary` / `status.*.solid`) maintain crisp visual separation in both Light and Dark themes.

---

## 9. Platform Bindings

### 9.1 Android (Jetpack Compose)

- **Source:** [`AuraProgressBar.kt`](../../../design-system/src/main/kotlin/io/github/bluemarlins/aura/component/AuraProgressBar.kt)

<!-- API:ANDROID:BEGIN -->
```kotlin
enum class AuraProgressVariant {
    Brand,
    Success,
    Warning,
    Error,
    Info,
}

@Composable
fun AuraLinearProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    variant: AuraProgressVariant = AuraProgressVariant.Brand,
    height: Dp = 6.dp,
    shape: Shape = AuraTheme.shapes.small,
    trackColor: Color = AuraTheme.colors.surface.sunken,
    indicatorColor: Color = Color.Unspecified,
    contentDescription: String? = null,
)

@Composable
fun AuraStepProgressBar(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier,
    variant: AuraProgressVariant = AuraProgressVariant.Brand,
    height: Dp = 4.dp,
    segmentSpacing: Dp = 4.dp,
    shape: Shape = AuraTheme.shapes.extraSmall,
    activeColor: Color = Color.Unspecified,
    inactiveColor: Color = AuraTheme.colors.surface.sunken,
    contentDescription: String? = null,
)
```
<!-- API:ANDROID:END -->

### 9.2 iOS (SwiftUI Reference)

<!-- API:IOS:BEGIN -->
```swift
public enum AuraProgressVariant: Sendable {
    case brand
    case success
    case warning
    case error
    case info
}

public struct AuraLinearProgressBar: View {
    public init(
        progress: Double,
        variant: AuraProgressVariant = .brand,
        height: CGFloat = 6,
        contentDescription: String? = nil
    )
}

public struct AuraStepProgressBar: View {
    public init(
        currentStep: Int,
        totalSteps: Int,
        variant: AuraProgressVariant = .brand,
        height: CGFloat = 4,
        segmentSpacing: CGFloat = 4,
        contentDescription: String? = nil
    )
}
```
<!-- API:IOS:END -->

### 9.3 Web (React / TypeScript Reference)

<!-- API:WEB:BEGIN -->
```ts
export type AuraProgressVariant = 'Brand' | 'Success' | 'Warning' | 'Error' | 'Info';

export interface AuraLinearProgressBarProps {
  progress: number;
  variant?: AuraProgressVariant;
  height?: number;
  contentDescription?: string;
}

export interface AuraStepProgressBarProps {
  currentStep: number;
  totalSteps: number;
  variant?: AuraProgressVariant;
  height?: number;
  segmentSpacing?: number;
  contentDescription?: string;
}
```
<!-- API:WEB:END -->
