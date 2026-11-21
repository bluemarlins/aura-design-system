---
id: "aura.component.divider"
name: "AuraDivider"
category: "containment"
status: "ready"
version: "1.1.0"
layer: "content"
material_allowed: ["Solid"]
motion_track: "Standard"
platforms:
  android: "com.example.ds.component.AuraDivider"
  ios: "AuraDS.AuraDivider (planned)"
  web: "@aura-ds/react.AuraDivider (planned)"
tokens:
  - "colors.border.subtle"
  - "colors.border.default"
  - "colors.border.strong"
  - "motion.stateChangeDurationMillis"
---

# AuraDivider

> Subtle hairline separator (`Horizontal` and `Vertical`) built on `AuraTheme.colors.border` tones with zero Material 3 dependency.

---

## 1. Intent & Usage Principles

### Problem

Dense lists and grouped sections need a quiet visual boundary between items when spacing alone does not separate them clearly.

### When to Use

- Separating logical sections inside dense detail cards, tasting records, or settings lists (`CNT-RULE-001`).
- Separating side-by-side compact metrics or actions horizontally using `AuraDividerOrientation.Vertical`.
- Creating indented list dividers (`startIndent = 16.dp`) aligned to text content rather than leading thumbnails.

### When NOT to Use & Alternatives

- **Top-level card grouping:** Wrap grouped items in `AuraCard` (`Outlined` or `Filled`) instead of stacking dividers between unrelated blocks.
- **Generous section rhythm:** Prefer `AuraTheme.layout.sectionSpacing` whitespace over heavy horizontal rules between independent sections.

### Context Rules

- **`DIV-RULE-001` (Border Restraint):** Default to `AuraDividerTone.Subtle` inside cards and lists; reserve `Strong` for high-contrast summary totals or structural boundaries.
- **`DIV-RULE-002` (1dp Hairline Default):** Keep `thickness = 1.dp` for standard separation; never inflate dividers into thick decorative bars.

### Do / Don't

| Do | Don't |
|---|---|
| Use `AuraDivider(tone = AuraDividerTone.Subtle)` to separate metadata rows inside an `AuraCard`. | Hardcode `Box(Modifier.height(1.dp).background(Color(0xFF...)))` in feature screens. |
| Use `startIndent` when separating rows with leading icons or thumbnails. | Stack consecutive dividers with no spacing or content between them. |
| Bound vertical dividers inside a fixed-height or `IntrinsicSize.Min` `Row`. | Place an unbounded `Vertical` divider inside a scrollable column without height constraints. |

---

## 2. Anatomy & Canonical Part IDs

```text
  Horizontal (Subtle / Default / Strong):
  ────────────────────────────────────────────────

  Horizontal (Indented):
       ──────────────────────────────────────

  Vertical (inside bounded Row):
  Item A  │  Item B
```

| Part ID | Responsibility | Required | Semantics Owner |
|---|---|---|---|
| `root` | Single hairline (`thickness`, default `1dp`) with optional `startIndent` / `endIndent` | Yes | Decorative (no semantics node) |

---

## 3. Variant, Size & Material Matrix

| Variant (`AuraDividerTone`) | Semantic Token | Intended Usage |
|---|---|---|
| `Subtle` (default) | `AuraTheme.colors.border.subtle` | Quiet list and card row dividers |
| `Default` | `AuraTheme.colors.border.default` | Standard section breaks on `surface.default` |
| `Strong` | `AuraTheme.colors.border.strong` | Summary totals and primary structural dividers |

| Orientation (`AuraDividerOrientation`) | Layout |
|---|---|
| `Horizontal` (default) | Fills available width minus indents |
| `Vertical` | Fills available height inside a bounded `Row` |

- **Material:** `Solid` only (`layer: content`).

---

## 4. State Precedence & Behavior

Passive component: no pointer, focus, or pressed states. Appearance depends only on `tone`, `orientation`, `thickness`, and indents.

---

## 5. Semantic Token Bindings

| Element | Tone / State | Light / Dark Semantic Token |
|---|---|---|
| Hairline Stroke | `Subtle` | `AuraTheme.colors.border.subtle` |
| Hairline Stroke | `Default` | `AuraTheme.colors.border.default` |
| Hairline Stroke | `Strong` | `AuraTheme.colors.border.strong` |

---

## 6. Interaction Motion Contract

Consumes `AuraTheme.motion`:

1. **Tone Transitions:** Changes between `Subtle`, `Default`, and `Strong` interpolate via `tween(durationMillis = AuraTheme.motion.stateChangeDurationMillis)`.
2. **Reduced Motion:** Resolves instantaneously with `snap()` when `AuraTheme.motion.reduceMotion` is `true`.

---

## 7. Responsive Layout Behavior (`Smallest Width × Display Ratio`)

| Width / Display Profile | Divider Adaptation |
|---|---|
| `ExtraSmall` (`< 360dp`) | Full-width `fillMaxWidth()` hairline or compact `8dp` indents to preserve narrow text columns. |
| `Compact` (`360–599dp`) | Standard `fillMaxWidth()` with optional `16dp` content-aligned indents. |
| `Square` (`~1:1` Foldable) | Scales cleanly inside split-pane cards and form sections without fixed pixel widths. |
| `UltraWide` (`> 21:9`) | Adapts to parent container width (`maxContentWidth` rails) cleanly. |
| `Expanded` (`>= 840dp`) | Respects parent pane boundaries inside `AuraAdaptiveTwoPane` (`primaryPane` / `secondaryPane`). |

---

## 8. Accessibility & Guardrails

- Purely decorative visual separator (`focusable = false`, no interactive click semantics) so screen readers focus on adjacent content rather than announcing hairline rules.
- Color tokens automatically adapt across Light and Dark `AuraColorMode` without obscuring adjacent text.

---

## 9. Platform Bindings

### 9.1 Android (Jetpack Compose)

- **Source:** [`AuraDivider.kt`](../../../design-system/src/main/kotlin/com/example/ds/component/AuraDivider.kt)

<!-- API:ANDROID:BEGIN -->
```kotlin
enum class AuraDividerOrientation {
    Horizontal,
    Vertical,
}

enum class AuraDividerTone {
    Subtle,
    Default,
    Strong,
}

@Composable
fun AuraDivider(
    modifier: Modifier = Modifier,
    orientation: AuraDividerOrientation = AuraDividerOrientation.Horizontal,
    tone: AuraDividerTone = AuraDividerTone.Subtle,
    thickness: Dp = 1.dp,
    startIndent: Dp = 0.dp,
    endIndent: Dp = 0.dp,
)
```
<!-- API:ANDROID:END -->

### 9.2 iOS (SwiftUI Reference)

<!-- API:IOS:BEGIN -->
```swift
public enum AuraDividerOrientation: Sendable {
    case horizontal
    case vertical
}

public enum AuraDividerTone: Sendable {
    case subtle
    case `default`
    case strong
}

public struct AuraDivider: View {
    public init(
        orientation: AuraDividerOrientation = .horizontal,
        tone: AuraDividerTone = .subtle,
        thickness: CGFloat = 1,
        startIndent: CGFloat = 0,
        endIndent: CGFloat = 0
    )
}
```
<!-- API:IOS:END -->

### 9.3 Web (React / TypeScript Reference)

<!-- API:WEB:BEGIN -->
```ts
export type AuraDividerOrientation = 'Horizontal' | 'Vertical';
export type AuraDividerTone = 'Subtle' | 'Default' | 'Strong';

export interface AuraDividerProps {
  orientation?: AuraDividerOrientation;
  tone?: AuraDividerTone;
  thickness?: number;
  startIndent?: number;
  endIndent?: number;
}
```
<!-- API:WEB:END -->
