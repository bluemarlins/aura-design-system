---
id: "aura.component.badge"
name: "AuraBadge"
category: "feedback"
status: "ready"
version: "1.0.0"
layer: "content"
material_allowed: ["Solid"]
motion_track: "Standard"
platforms:
  android: "io.github.bluemarlins.aura.component.AuraBadge"
  ios: "AuraDS.AuraBadge (planned)"
  web: "@aura-ds/react.AuraBadge (planned)"
tokens:
  - "colors.brand.subtle"
  - "colors.brand.onSubtle"
  - "colors.brand.primary"
  - "colors.brand.onPrimary"
  - "colors.surface.sunken"
  - "colors.content.secondary"
  - "colors.status.success.subtle"
  - "colors.status.success.onSubtle"
  - "colors.status.warning.subtle"
  - "colors.status.warning.onSubtle"
  - "colors.status.error.solid"
  - "colors.status.error.onSolid"
  - "colors.status.error.subtle"
  - "colors.status.error.onSubtle"
  - "colors.status.info.subtle"
  - "colors.status.info.onSubtle"
  - "typography.labelSmall"
  - "typography.labelMedium"
  - "motion.pressOutSpring"
  - "motion.selectionSpring"
  - "motion.stateChangeDurationMillis"
---

# AuraBadge

> Compact status pill, numeric counter, and unread dot indicator (`AuraBadge`) paired with an overlay anchor helper (`AuraBadgedBox`).

---

## 1. Intent & Usage Principles

### Problem

Users need to notice status, counts, or unread items at a glance, attached to the element they describe, without opening it.

### When to Use

- Communicating component/record status (`"Ready"`, `"Planned"`, `"In Cellar"`, `"Dividend"`, `"Overdue"`) cleanly inside cards and lists.
- Rendering numeric unread/filter counts (`count = 4`, clamped to `"99+"` when `count > maxCount`).
- Anchoring a compact dot or count indicator over an icon or navigation item via `AuraBadgedBox`.

### When NOT to Use & Alternatives

- **Interactive toggleable filter tags or dismissible chips:** Use `AuraChipButton` (which guarantees a `>= 48dp` interactive hit box).
- **Full-width modal confirmation or blocking warnings:** Use `AuraDialog`.
- **View switching tabs:** Use `AuraSegmentedButton`.

### Context Rules

- **`BDG-RULE-001` (Short 1–2 Word Labels):** Keep `label` strictly scannable (`"Ready"`, `"New"`, `"3"`, `"99+"`); never place sentences inside an `AuraBadge`.
- **`BDG-RULE-002` (Overflow Clamp):** Numeric counts above `maxCount` (default `99`) format automatically as `"${maxCount}+"` so overlaid badges never collide with neighboring icons.
- **`BDG-RULE-003` (Dot Mode Zero-Text):** Use `AuraBadgeSize.Dot` for subtle unread presence cues without numeric cognitive load.

### Do / Don't

| Do | Don't |
|---|---|
| Use `AuraBadgeVariant.Success` for `"Ready"` or `"Completed"` status pills and `Error` for urgent unread counts. | Attach `Modifier.clickable` to tiny `Small` badges pretending they are buttons (`AuraChipButton`). |
| Wrap icons in `AuraBadgedBox(badge = { AuraBadge(count = 3, variant = Error, size = Small) })` for clean top-end anchoring. | Manually position magic-number negative offsets inside every screen header. |
| Format counts above `99` as `"99+"` using `count` & `maxCount`. | Render `"1428"` overflowing an icon corner. |
| Choose `Small` for tight metadata tags and `Medium` for card headers (`CatalogHomeScreen`). | Mix giant multi-line pill banners inside compact table rows. |

---

## 2. Anatomy & Canonical Part IDs

```text
  Dot:      ●
  Small:   [ 4 ]   [ Ready ]
  Medium:  [  In Cellar  ]   [  99+  ]
```

| Part ID | Responsibility | Required | Semantics Owner |
|---|---|---|---|
| `root` | `Dot` circle or capsule pill sized by `AuraBadgeSize` | Yes | Text content, or `"Unread status indicator"` for `Dot` |
| `label` | Status text or formatted count (`"99+"` above `maxCount`) | Conditional (not `Dot`) | Merged into `root` |
| `cutoutStroke` | `1.5dp` ring separating a `Dot` from its anchor | Conditional (`Dot`) | Decorative |
| `anchor` | `AuraBadgedBox` `content` the badge overlaps | No | Anchor keeps its own semantics |

---

## 3. Variant, Size & Material Matrix

| Variant (`AuraBadgeVariant`) | `Dot` / Unlabeled Numeric Tone | Labeled / `Medium` Pill Tone | Intended Semantics |
|---|---|---|---|
| `Brand` | `brand.primary` / `brand.onPrimary` | `brand.subtle` / `brand.onSubtle` | Featured tags, catalog brand badges |
| `Neutral` | `surface.sunken` / `content.secondary` | `surface.sunken` / `content.secondary` | `"Planned"`, `"Draft"`, muted counts |
| `Success` | `status.success.solid` / `onSolid` | `status.success.subtle` / `status.success.onSubtle` | `"Ready"`, `"In Stock"`, `"Gain"` |
| `Warning` | `status.warning.solid` / `onSolid` | `status.warning.subtle` / `status.warning.onSubtle` | `"Low Stock"`, `"Pending"`, `"Caution"` |
| `Error` | `status.error.solid` / `onSolid` | `status.error.subtle` / `status.error.onSubtle` | Unread alert counts, `"Overdue"`, `"Loss"` |
| `Info` | `status.info.solid` / `onSolid` | `status.info.subtle` / `status.info.onSubtle` | `"Syncing"`, `"New"` informational notes |

| Size (`AuraBadgeSize`) | Height | Min Width | Horizontal Padding |
|---|---|---|---|
| `Dot` | `10dp` | `10dp` | `0dp` |
| `Small` | `18dp` | `18dp` | `6dp` |
| `Medium` (default) | `24dp` | `24dp` | `10dp` |

- **Emphasis rule:** `Dot` and unlabeled counts use the solid tone; labeled pills use the subtle tone.
- **Material:** `Solid` only (`layer: content`).

---

## 4. State Precedence & Behavior

Passive component: no pointer, focus, or pressed states. Display resolves as `Dot` (`size == Dot`) > count (`count != null`, clamped to `maxCount+`) > `label` > empty (renders nothing). Appearing badges scale in from `motion.presenceEnterScale`.

---

## 5. Semantic Token Bindings

| Element | State | Light / Dark Semantic Token |
|---|---|---|
| Pill / Dot Background | `By Variant & Size` | `brand.*`, `surface.sunken`, `status.success.*`, `status.warning.*`, `status.error.*`, `status.info.*` |
| Pill / Count Text | `Medium` / `Small` | Matching `onSubtle` / `onSolid` + `typography.labelSmall` / `typography.labelMedium` |
| Border Ring (`Small` Overlays) | `Solid Count / Dot` | Subtle `surface.default` `1.5dp` ring when anchored inside `AuraBadgedBox` for clean cutout separation |

---

## 6. Interaction Motion Contract

Consumes `AuraTheme.motion`:

1. **Count / Label Transition & Entrance:** Presence scale (`AuraTheme.motion.presenceEnterScale` `0.90f -> 1f`) animates with `AuraTheme.motion.selectionSpring()`; container color changes use `tween(durationMillis = AuraTheme.motion.stateChangeDurationMillis)`.
2. **Reduced Motion:** Uses `snap()` when `rememberAuraReducedMotion()` is enabled.

---

## 7. Responsive Layout Behavior (`Smallest Width × Display Ratio`)

| Width / Display Profile | Badge Sizing & Placement |
|---|---|
| `ExtraSmall` (`< 360dp`) | Uses `Small` pill or `Dot` inside dense list titles to avoid pushing row text into narrow ellipses. |
| `Compact` (`360–599dp`) | Standard trailing card status pill (`CatalogHomeScreen`) or `AuraBadgedBox` corner anchor over top-bar icons. |
| `Square` (`~1:1` Pop-up / Foldable) | Intrinsic `wrapContentSize()` keeps badge dimensions compact beside multi-column headings. |
| `UltraWide` (`> 21:9`) | Maintains single-line capsule shape (`maxLines = 1`) without wrapping status labels across lines. |
| `Expanded` (`>= 840dp` Tablet) | Scales cleanly in card headers and split-pane master items without inflating capsule height. |

---

## 8. Accessibility & Guardrails

- Exposes `contentDescription` on numeric badges when overlaid inside `AuraBadgedBox` (e.g., `"3 unread"` or `"99+ notifications"`).
- Maintains >= 4.5:1 foreground-to-container text contrast across all six semantic variants in both Light and Dark themes.
- Strictly passive (`Role` unset / non-clickable) so screen readers announce the status value without suggesting an action.

---

## 9. Platform Bindings

### 9.1 Android (Jetpack Compose)

- **Source:** [`AuraBadge.kt`](../../../design-system/src/main/kotlin/io/github/bluemarlins/aura/component/AuraBadge.kt)

<!-- API:ANDROID:BEGIN -->
```kotlin
enum class AuraBadgeVariant {
    Brand,
    Neutral,
    Success,
    Warning,
    Error,
    Info,
}

enum class AuraBadgeSize {
    Dot,
    Small,
    Medium,
}

@Composable
fun AuraBadge(
    modifier: Modifier = Modifier,
    label: String? = null,
    count: Int? = null,
    maxCount: Int = 99,
    variant: AuraBadgeVariant = AuraBadgeVariant.Brand,
    size: AuraBadgeSize = AuraBadgeSize.Medium,
)

@Composable
fun AuraBadgedBox(
    badge: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
)
```
<!-- API:ANDROID:END -->

### 9.2 iOS (SwiftUI Reference)

<!-- API:IOS:BEGIN -->
```swift
public enum AuraBadgeVariant: Sendable {
    case brand
    case neutral
    case success
    case warning
    case error
    case info
}

public enum AuraBadgeSize: Sendable {
    case dot
    case small
    case medium
}

public struct AuraBadge: View {
    public init(
        label: String? = nil,
        count: Int? = nil,
        maxCount: Int = 99,
        variant: AuraBadgeVariant = .brand,
        size: AuraBadgeSize = .medium
    )
}
```
<!-- API:IOS:END -->

### 9.3 Web (React / TypeScript Reference)

<!-- API:WEB:BEGIN -->
```ts
export type AuraBadgeVariant = 'Brand' | 'Neutral' | 'Success' | 'Warning' | 'Error' | 'Info';
export type AuraBadgeSize = 'Dot' | 'Small' | 'Medium';

export interface AuraBadgeProps {
  label?: string;
  count?: number;
  maxCount?: number;
  variant?: AuraBadgeVariant;
  size?: AuraBadgeSize;
}
```
<!-- API:WEB:END -->
