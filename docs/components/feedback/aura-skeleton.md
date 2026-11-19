---
id: "aura-skeleton"
name: "AuraSkeleton"
category: "feedback"
status: "ready"
layer: "content"
material_allowed: ["Solid"]
motion_track: "Standard"
version: "1.3.0"
platforms:
  android: "com.example.ds.component.AuraSkeleton"
  ios: "AuraSkeleton"
  web: "AuraSkeleton"
tokens:
  - "color.surface.sunken"
  - "color.surface.raised"
  - "shapes.small"
  - "shapes.extraSmall"
  - "motion.skeletonPulseDurationMillis"
---

# AuraSkeleton

Loading placeholders (`AuraSkeleton` block and `AuraSkeletonText` paragraph) that hold the shape of content while its first load is still running.

## 1. Intent & Usage Principles

### When to Use

- The first load of a local or remote query that can take more than a frame (cold start, database warm-up).
- Cards and list rows whose layout is known before their data arrives.

### When NOT to Use

- **Known-empty data:** once the query has answered "nothing", show the real empty state copy, not a skeleton.
- **Determinate progress:** use `AuraLinearProgressBar` / `AuraStepProgressBar`.
- **Button-level waiting:** use `AuraButton(loading = true)`.

### Usage Rules

- **`SKL-RULE-001` (Never fake emptiness):** Render skeletons — not empty-state text — until the first emission arrives. An empty state shown during loading tells the user their data is gone.
- **`SKL-RULE-002` (Shape fidelity):** Match the size and corner of the content it stands in for so nothing jumps when data lands.

### Do / Don't

| Do | Don't |
|---|---|
| Show skeleton cards on a dashboard until its first query emits. | Show "No records yet" during the first second after launch. |
| Use `AuraSkeletonText(lines = n)` for paragraphs. | Stack differently sized ad-hoc gray boxes per screen. |

---

## 2. Anatomy

```text
 AuraSkeleton:      [████████████████████]   shape = shapes.small (default)
 AuraSkeletonText:  [████████████████████]
                    [████████████        ]   last line 60% width, shapes.extraSmall
```

---

## 3. Semantic Token Bindings

| Element | State | Token |
|---|---|---|
| Block fill | Rest | `AuraTheme.colors.surface.sunken` |
| Block fill | Pulse peak | 60% toward `AuraTheme.colors.surface.raised` |
| Corner | Block / Text line | `AuraTheme.shapes.small` / `AuraTheme.shapes.extraSmall` |

---

## 4. Accessibility

- Pass `contentDescription` (e.g. "Loading summary") on the outer skeleton so TalkBack announces that content is loading; inner lines are merged.
- The pulse is a low-contrast luminance shift only; it carries no information on its own.

---

## 5. Interaction Motion Contract

1. **Pulse:** an infinite reverse `tween(durationMillis = AuraTheme.motion.skeletonPulseDurationMillis)` between rest and peak.
2. **Reduced Motion:** with `rememberAuraReducedMotion()` the block holds a static `surface.sunken` fill.

---

## 6. Responsive Layout Behavior

| Width / Display Profile | Adaptation |
|---|---|
| `ExtraSmall` (`< 360dp`) | Fills the parent width; keep text skeletons to 2 lines. |
| `Compact` (`360–599dp`) | Mirrors the card / row it replaces. |
| `Square` (`~1:1` Foldable) | Mirrors the grid cell it replaces. |
| `UltraWide` (`> 21:9`) | Bounded by the parent `maxContentWidth`. |
| `Expanded` (`>= 840dp`) | Mirrors multi-column card layouts. |

---

## 7. Cross-Platform API Contract

<!-- API:ANDROID:BEGIN -->
```kotlin
@Composable
fun AuraSkeleton(
    modifier: Modifier = Modifier,
    shape: Shape = AuraTheme.shapes.small,
    contentDescription: String? = null,
)

@Composable
fun AuraSkeletonText(
    modifier: Modifier = Modifier,
    lines: Int = 2,
    lineHeight: Dp = 14.dp,
    lineSpacing: Dp = 8.dp,
    contentDescription: String? = null,
)
```
<!-- API:ANDROID:END -->

<!-- API:IOS:BEGIN -->
```swift
public struct AuraSkeleton: View {
    public init(cornerRadius: CGFloat = 8, contentDescription: String? = nil)
}

public struct AuraSkeletonText: View {
    public init(lines: Int = 2, lineHeight: CGFloat = 14, lineSpacing: CGFloat = 8, contentDescription: String? = nil)
}
```
<!-- API:IOS:END -->

<!-- API:WEB:BEGIN -->
```ts
export interface AuraSkeletonProps {
  radius?: number;
  contentDescription?: string;
}

export interface AuraSkeletonTextProps {
  lines?: number;
  lineHeight?: number;
  lineSpacing?: number;
  contentDescription?: string;
}
```
<!-- API:WEB:END -->
