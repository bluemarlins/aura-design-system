---
id: "aura.component.text"
name: "AuraText"
category: "primitives"
status: "ready"
version: "0.1.0"
layer: "content"
material_allowed: ["Solid"]
motion_track: "none"
platforms:
  android: "io.github.bluemarlins.aura.component.AuraText"
  ios: "AuraDS.AuraText (planned)"
  web: "@aura-ds/react.AuraText (planned)"
tokens:
  - "colors.content.primary"
  - "typography.*"
---

# AuraText

> Cross-platform specification for Aura's text primitive. Renders text with `AuraTheme` typography and inherited content color, with zero Material 3 dependency. See [00-primitives-principles.md](00-primitives-principles.md) and [04-typography.md](../../foundation/04-typography.md).

---

## 1. Intent & Usage Principles

### Problem

Text color and style must follow the surface it sits on (a `Primary` button label, a `Glass` snackbar message, a `Destructive` dialog title) without every call site re-resolving tokens. Material's `Text` would pull in `material3`; raw `BasicText` ignores Aura's content color.

### When to Use

- Any text rendered inside `:design-system` components or consumer screens built on `AuraTheme`.
- Labels, titles, body copy, supporting/error text, counters, and numeric readouts.

### When NOT to Use & Alternatives

| Situation | Alternative | Reason |
|---|---|---|
| Editable text | `AuraTextField` / `AuraNumberField` | Text entry needs cursor, IME, validation, and input semantics |
| Icon-only meaning | `AuraIcon` | Glyphs use tint, not text style |
| Rich annotated text with spans/links | `BasicText(AnnotatedString)` inside a documented wrapper | `AuraText` accepts plain `String` only |

### Context Rules

- **`TXT-RULE-001` (Token Styles Only):** Pass `AuraTheme.typography.*` styles (`titleMedium`, `bodyMedium`, `labelSmall`, …). Ad-hoc `TextStyle(fontSize = …)` literals are forbidden in components.
- **`TXT-RULE-002` (Inherit Color by Default):** Leave `color` unspecified so the container's `LocalAuraContentColor` applies. Override only for semantic intent (`content.secondary`, `AuraTheme.colors.status.*` roles).
- **`TXT-RULE-003` (Bounded Lines in Controls):** Inside buttons, chips, segments, and bars, set `maxLines = 1` with `TextOverflow.Ellipsis`; multi-line wrapping is for body content.

### Do / Don't

| Do | Don't |
|---|---|
| `AuraText("Save", style = AuraTheme.typography.labelLarge)` and let the button provide color. | `AuraText("Save", color = Color(0xFF3366FF))` with a raw hex literal. |
| Use `ProvideAuraContentColorTextStyle` once on a custom container so all descendant text inherits. | Repeat the same `color = …` argument on every `AuraText` inside a container. |
| Use `color = AuraTheme.colors.content.secondary` for supporting text. | Lower alpha (`copy(alpha = 0.5f)`) to fake secondary text; it breaks contrast guarantees. |

---

## 2. Anatomy & Canonical Part IDs

| Part ID | Responsibility | Required |
|---|---|---|
| `root` / `label` | Single text run rendered by `BasicText` | Yes |

---

## 3. Variant, Size & Material Matrix

`AuraText` has no visual variants. Appearance is fully determined by the `style` argument (a typography token) merged on top of the inherited `LocalAuraTextStyle`.

| Usage | Recommended Token |
|---|---|
| Screen / dialog titles | `typography.titleLarge`, `titleMedium` |
| Body copy | `typography.bodyLarge`, `bodyMedium` |
| Control labels | `typography.labelLarge`, `labelMedium`, `labelSmall` |

---

## 4. State Precedence & Behavior

`AuraText` resolves its color in a strict order (first specified wins):

$$\text{explicit } color \succ style.color \succ \text{LocalAuraContentColor} \succ \text{AuraTheme.colors.content.primary}$$

Style resolution: `LocalAuraTextStyle.current.merge(style)`, then `textAlign` (when non-null) is applied on top.

---

## 5. Semantic Token Bindings

| Part | Property | Token Binding |
|---|---|---|
| `label` | Fallback color | `AuraTheme.colors.content.primary` |
| `label` | Inherited color | `LocalAuraContentColor` (published by `AuraScaffold`, `FloatingGlassScaffold`, `AuraButton`, …) |
| `label` | Text style | `AuraTheme.typography.*` via `style` / `LocalAuraTextStyle` |

---

## 6. Interaction Motion Contract

- **Passive (`motion_track: "none"`):** `AuraText` is a non-interactive primitive with no press, focus, or state animation of its own. Color transitions are driven by the composing component (e.g., `AuraButton` animates its content color and `AuraText` simply re-renders).
- **Reduced Motion:** Not applicable; no spatial motion is produced.

---

## 7. Responsive Layout Behavior (`Smallest Width × Display Ratio`)

`AuraText` sizes to its content and never imposes width. Responsive behavior comes from the typography scale and the parent container.

| Profile | Behavior |
|---|---|
| **`ExtraSmall`** (Cover `sw < 360dp`) | Single-line control labels ellipsize (`TXT-RULE-003`); body text wraps within parent width |
| **`Compact`** (Phone Portrait) | Default typography scale |
| **`Medium` / `Square`** (Foldable `~1:1`) | Body text width is capped by the parent's `maxContentWidth`, not by `AuraText` |
| **`UltraWide`** (Phone Landscape) | Same as `Compact`; parent containers prevent over-long line lengths |
| **`Expanded`** (Tablet / DeX) | Typography tokens may scale per `AuraTypography`; `AuraText` applies them unchanged |

---

## 8. Accessibility & Guardrails

- Text scales with the system font scale (`sp` units in typography tokens); never fix heights that clip enlarged text.
- Contrast: content tokens are paired with their surfaces to meet WCAG AA (`>= 4.5:1` body, `>= 3:1` large text). On `Glass` hosts the composing component guarantees contrast (`VIS-RULE-003`).
- Screen readers read the text as-is; containers that merge semantics (`AuraButton`) expose it as the control label.

---

## 9. Platform Bindings

### 9.1 Android (Jetpack Compose)

- **Source:** [`AuraText.kt`](../../../design-system/src/main/kotlin/io/github/bluemarlins/aura/component/AuraText.kt)

<!-- API:ANDROID:BEGIN -->
```kotlin
val LocalAuraContentColor: ProvidableCompositionLocal<Color>
val LocalAuraTextStyle: ProvidableCompositionLocal<TextStyle>

@Composable
fun ProvideAuraContentColorTextStyle(
    contentColor: Color,
    textStyle: TextStyle = LocalAuraTextStyle.current,
    content: @Composable () -> Unit,
)

@Composable
fun AuraText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    style: TextStyle = LocalAuraTextStyle.current,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
)
```
<!-- API:ANDROID:END -->

### 9.2 iOS (SwiftUI Reference)

<!-- API:IOS:BEGIN -->
```swift
struct AuraText: View {
    let text: String
    var color: Color? = nil            // nil -> @Environment(\.auraContentColor)
    var style: AuraTextStyle? = nil    // nil -> @Environment(\.auraTextStyle)
    var alignment: TextAlignment? = nil
    var lineLimit: Int? = nil
}
```
<!-- API:IOS:END -->

### 9.3 Web (React / TypeScript Reference)

<!-- API:WEB:BEGIN -->
```tsx
export interface AuraTextProps {
  children: string;
  color?: string;             // defaults to var(--aura-content-color, var(--aura-content-primary))
  variant?: AuraTypographyToken;
  align?: 'start' | 'center' | 'end';
  maxLines?: number;
}
```
<!-- API:WEB:END -->
