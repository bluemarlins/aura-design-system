---
id: "aura.component.snackbar"
name: "AuraSnackbar"
category: "feedback"
status: "ready"
version: "1.1.0"
layer: "chrome"
material_allowed: ["Solid", "Glass"]
motion_track: "Standard"
platforms:
  android: "com.example.ds.component.AuraSnackbar"
  ios: "AuraDS.AuraSnackbar (planned)"
  web: "@aura-ds/react.AuraSnackbar (planned)"
tokens:
  - "colors.surface.inverse"
  - "colors.content.inverse"
  - "colors.content.primary"
  - "colors.brand.primary"
  - "colors.border.subtle"
  - "colors.status.success.solid"
  - "colors.status.success.onSolid"
  - "colors.status.warning.solid"
  - "colors.status.warning.onSolid"
  - "colors.status.error.solid"
  - "colors.status.error.onSolid"
  - "typography.bodyMedium"
  - "typography.labelMedium"
  - "shapes.medium"
  - "shapes.small"
  - "elevation.level3"
  - "motion.enterSpec"
  - "motion.exitSpec"
  - "motion.spatialSpring"
  - "motion.pressInSpring"
  - "motion.pressOutSpring"
---

# AuraSnackbar

> Transient, non-blocking feedback banner (`AuraSnackbar`) with serialized queue management (`AuraSnackbarHostState`, `AuraSnackbarHost`) integrated into `AuraScaffold` and `FloatingGlassScaffold`.

---

## 1. Intent & Usage Principles

### Problem

After an action, users need brief, non-blocking confirmation (optionally with Undo) that does not interrupt what they are doing.

### When to Use

- Confirming completed operations (`"Saved"`, `"Exported"`, `"Deleted"`) with an optional single recovery action (`"Undo"`).
- Surfacing non-blocking transient errors (e.g., photo copy failure, temporary save error) without interrupting user flow.

### When NOT to Use & Alternatives

- **Irreversible destructive confirmations or choices requiring explicit acknowledgment:** Use `AuraDialog`.
- **Persistent inline status tags or counts:** Use `AuraBadge`.
- **Multi-step operation completion percentages:** Use `AuraLinearProgressBar`.

### Context Rules

- **`SNK-RULE-001` (Single Concise Message + At Most One Action):** Keep `message` scannable (1–3 lines max) and attach at most one short action verb (`"Undo"`, `"Retry"`).
- **`SNK-RULE-002` (Serialized Queueing):** Present snackbars strictly one at a time via `AuraSnackbarHostState` so concurrent notifications never stack over each other.
- **`SNK-RULE-003` (Bottom Chrome Clearance):** Render inside `AuraScaffold(snackbarHost = ...)` or `FloatingGlassScaffold(snackbarHost = ...)` so the snackbar automatically clears `FloatingBottomIsland` and system navigation bars.

### Do / Don't

| Do | Don't |
|---|---|
| Pass `AuraSnackbarHost(hostState)` to `AuraScaffold` or `FloatingGlassScaffold` `snackbarHost`. | Hardcode bottom dp offsets (`92dp`) to guess bottom island height. |
| Use `AuraSnackbarVariant.Error` for recoverable failure notices and `Neutral` for standard confirmations. | Trigger blocking `AuraDialog` alerts for trivial `"Saved"` feedback. |
| Keep `actionLabel` short (`"Undo"`, `"View"`) with a `>= 48dp` touch target. | Place multiple competing buttons inside a transient snackbar. |

---

## 2. Anatomy & Canonical Part IDs

```text
  ╭──────────────────────────────────────────────────────╮
  │  Tasting note saved to your cellar.          [ Undo ] │
  ╰──────────────────────────────────────────────────────╯
```

| Part ID | Responsibility | Required | Semantics Owner |
|---|---|---|---|
| `host` | `AuraSnackbarHost` slot that shows one snackbar at a time from `AuraSnackbarHostState` | Yes | Inherited |
| `root` | Capsule surface (`shapes.medium`, `Solid` or `Glass`) | Yes | `liveRegion = Polite` |
| `message` | One- or two-line feedback text | Yes | Announced by live region |
| `action` | Optional text action (`actionLabel`) | No | `Role.Button` |

---

## 3. Variant, Size & Material Matrix

| Variant (`AuraSnackbarVariant`) | Solid Container Token | Content / Action Token |
|---|---|---|
| `Neutral` (default) | `AuraTheme.colors.surface.inverse` | `AuraTheme.colors.content.inverse` |
| `Success` | `AuraTheme.colors.status.success.solid` | `AuraTheme.colors.status.success.onSolid` |
| `Warning` | `AuraTheme.colors.status.warning.solid` | `AuraTheme.colors.status.warning.onSolid` |
| `Error` | `AuraTheme.colors.status.error.solid` | `AuraTheme.colors.status.error.onSolid` |

| Duration (`AuraSnackbarDuration`) | Auto-dismiss |
|---|---|
| `Short` (default) | `3000ms` |
| `Long` | `5000ms` |
| `Indefinite` | Never; dismissed by action or `dismissCurrent()` |

- **Material:** `AuraMaterial.Solid` (default) or `Glass` via `material` (`layer: chrome`).

---

## 4. State Precedence & Behavior

`Hidden` > `Showing` (one at a time) > `Queued`. `showSnackbar` calls are serialized by a `Mutex`, so a new request waits until the current one is dismissed. Results resolve as `ActionPerformed` (action tapped) or `Dismissed` (timeout or `dismissCurrent()`).

---

## 5. Semantic Token Bindings

| Element | Variant / Material | Light / Dark Semantic Token |
|---|---|---|
| Surface Container | `Solid` (`Neutral` / Status) | `surface.inverse` / `status.{success,warning,error}.solid` + `elevation.level3` |
| Surface Container | `Glass` | `GlassSurface(FROSTED)` + `elevation.level3` |
| Message Text | `Solid` / `Glass` | `content.inverse` (`onSolid`) / `content.primary` + `typography.bodyMedium` |
| Action Button | `Solid` / `Glass` | `content.inverse` (`onSolid`) / `brand.primary` + `typography.labelMedium` |
| Corner Shape | `All` | `AuraTheme.shapes.medium` (`12dp`) |

---

## 6. Interaction Motion Contract

Consumes `AuraTheme.motion`:

1. **Entrance & Exit:** `AuraSnackbarHost` animates appearance with `fadeIn(motion.enterSpec()) + slideInVertically(motion.spatialSpring())` and dismissal with `fadeOut(motion.exitSpec()) + slideOutVertically(motion.exitSpec())`.
2. **Action Press Feedback:** Action button compresses tactilely with `motion.pressInSpring()` / `motion.pressOutSpring()` and `motion.pressedScaleGhost`.
3. **Reduced Motion:** Resolves spatial slides and press compression immediately with `snap()` when `AuraTheme.motion.reduceMotion` is `true` while preserving polite `LiveRegionMode` announcement.

---

## 7. Responsive Layout Behavior (`Smallest Width × Display Ratio`)

| Width / Display Profile | Snackbar Placement & Width |
|---|---|
| `ExtraSmall` (`< 360dp`) | Full-width within `screenMarginHorizontal` (`12dp`), wrapping message cleanly up to 3 lines. |
| `Compact` (`360–599dp`) | Full-width within `16dp` horizontal margins, anchored cleanly above `bottomBar` / `FloatingBottomIsland`. |
| `Square` (`~1:1` Foldable) | Centered bottom banner capped at `widthIn(max = 560.dp)` so lines remain comfortable to scan. |
| `UltraWide` (`> 21:9`) | Capped at `560dp` width and clears bottom navigation insets. |
| `Expanded` (`>= 840dp`) | Centered floating banner (`max = 560.dp`) without stretching across full tablet width. |

---

## 8. Accessibility & Guardrails

- Marks the snackbar container with `Modifier.semantics { liveRegion = LiveRegionMode.Polite }` so TalkBack announces transient messages automatically without stealing input focus.
- Optional action trigger enforces a `>= 48dp × 48dp` touch target (`EVAL-005`).
- **Reduced Transparency & Contrast (`VIS-RULE-003`–`004`):** Maintains `>= 4.5:1` contrast across all `Neutral` and semantic variants (`Solid` and `Glass`) and falls back automatically to opaque `Solid` via `AuraTheme.effectiveMaterial(...)` when Reduced Transparency, Battery Saver, or High-Contrast Text is enabled.

---

## 9. Platform Bindings

### 9.1 Android (Jetpack Compose)

- **Source:** [`AuraSnackbar.kt`](../../../design-system/src/main/kotlin/com/example/ds/component/AuraSnackbar.kt)

<!-- API:ANDROID:BEGIN -->
```kotlin
enum class AuraSnackbarVariant {
    Neutral,
    Success,
    Warning,
    Error,
}

enum class AuraSnackbarDuration(val durationMillis: Long) {
    Short(3_000L),
    Long(5_000L),
    Indefinite(kotlin.Long.MAX_VALUE),
}

enum class AuraSnackbarResult {
    Dismissed,
    ActionPerformed,
}

data class AuraSnackbarVisuals(
    val message: String,
    val actionLabel: String? = null,
    val duration: AuraSnackbarDuration = AuraSnackbarDuration.Short,
    val variant: AuraSnackbarVariant = AuraSnackbarVariant.Neutral,
)

@Composable
fun rememberAuraSnackbarHostState(): AuraSnackbarHostState

@Composable
fun AuraSnackbarHost(
    hostState: AuraSnackbarHostState,
    modifier: Modifier = Modifier,
    material: AuraMaterial = AuraMaterial.Solid,
    snackbar: @Composable (AuraSnackbarData) -> Unit = { data ->
        AuraSnackbar(
            message = data.visuals.message,
            actionLabel = data.visuals.actionLabel,
            onActionClick = { data.performAction() },
            variant = data.visuals.variant,
            material = material,
        )
    },
)

@Composable
fun AuraSnackbar(
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    variant: AuraSnackbarVariant = AuraSnackbarVariant.Neutral,
    material: AuraMaterial = AuraMaterial.Solid,
    shape: Shape = AuraTheme.shapes.medium,
)
```
<!-- API:ANDROID:END -->

### 9.2 iOS (SwiftUI Reference)

<!-- API:IOS:BEGIN -->
```swift
public enum AuraSnackbarVariant: Sendable {
    case neutral
    case success
    case warning
    case error
}

public enum AuraSnackbarDuration: Sendable {
    case short
    case long
    case indefinite
}

public struct AuraSnackbar: View {
    public init(
        message: String,
        actionLabel: String? = nil,
        onAction: (() -> Void)? = nil,
        variant: AuraSnackbarVariant = .neutral
    )
}
```
<!-- API:IOS:END -->

### 9.3 Web (React / TypeScript Reference)

<!-- API:WEB:BEGIN -->
```ts
export type AuraSnackbarVariant = 'Neutral' | 'Success' | 'Warning' | 'Error';
export type AuraSnackbarDuration = 'Short' | 'Long' | 'Indefinite';

export interface AuraSnackbarProps {
  message: string;
  actionLabel?: string;
  onActionClick?: () => void;
  variant?: AuraSnackbarVariant;
  duration?: AuraSnackbarDuration;
}
```
<!-- API:WEB:END -->
