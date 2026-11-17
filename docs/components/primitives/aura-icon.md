---
component_id: "aura.component.icon"
name: "AuraIcon"
category: "primitives"
status: "ready"
layer: "content"
material_allowed: ["Solid"]
motion_track: "none"
version: "0.1.0"
platforms:
  android: "com.example.ds.component.AuraIcon"
  ios: "AuraDS.AuraIcon (planned)"
  web: "@aura-ds/react.AuraIcon (planned)"
tokens:
  - "color.content.primary"
---

# AuraIcon

> Cross-platform specification for Aura's icon primitive. Draws an `ImageVector` or `Painter` glyph tinted with the inherited content color. See [00-primitives-principles.md](00-primitives-principles.md).

---

## 1. Intent & Usage Principles

### Problem

Icon slots across buttons, bars, and list rows must pick up the same foreground color as neighbouring text (white on `Primary`, `content.secondary` on `Ghost`, red on `Destructive`) without every call site passing a tint.

### When to Use

- Filling `leadingIcon` / `trailingIcon` / `icon` slots of Aura components.
- Standalone decorative or informative glyphs inside rows, cards, and empty states.

### When NOT to Use & Alternatives

| Situation | Alternative | Reason |
|---|---|---|
| Tappable icon-only control | `AuraIconButton` | Needs `48dp` hit target, press motion, and button semantics |
| Multi-color illustration or photo | `Image` with `contentScale` | Tinting would flatten brand colors |
| Status count or dot on an icon | `AuraBadgedBox` wrapping `AuraIcon` | Badge positioning and semantics are owned by `AuraBadge` |

### Context Rules

- **`ICN-RULE-001` (Inherited Tint):** Omit `tint` so `LocalAuraContentColor` applies; pass `Color.Unspecified` only for multi-color assets.
- **`ICN-RULE-002` (Meaningful Description):** Provide `contentDescription` for informative icons; pass `null` for decorative icons or when an adjacent label already describes the action.
- **`ICN-RULE-003` (Default 24px Box):** Vector/painter assets without intrinsic size render at `24px`. Size with `Modifier.size(...)` only to match the composing component's icon token.

### Do / Don't

| Do | Don't |
|---|---|
| `AuraIcon(Icons.Rounded.Add, contentDescription = null)` inside `AuraButton(leadingIcon = …)` next to a "Add" label. | Duplicate the label in `contentDescription` ("Add icon"), producing double announcements. |
| Let `AuraIconButton` provide the tint for its glyph. | Hardcode `tint = Color.White` inside a button that also supports `Ghost` / `Outline` variants. |

---

## 2. Anatomy & Canonical Part IDs

| Part ID | Responsibility | Required |
|---|---|---|
| `root` / `icon` | Glyph drawn via `Image(painter, colorFilter = tint)` with `ContentScale.Fit` | Yes |

---

## 3. Variant & Size Matrix

| Overload | Source | Default Size |
|---|---|---|
| `AuraIcon(imageVector, …)` | `ImageVector` (converted with `rememberVectorPainter`) | Intrinsic vector size |
| `AuraIcon(painter, …)` | Any `Painter` | Intrinsic size, or `24px` when the painter has no finite intrinsic size |

---

## 4. Tint Resolution Precedence

$$\text{explicit } tint \succ \text{LocalAuraContentColor} \succ \text{AuraTheme.colors.content.primary}$$

`tint == Color.Unspecified` disables the color filter (original asset colors).

---

## 5. Semantic Token Bindings

| Part | Property | Token Binding |
|---|---|---|
| `icon` | Inherited tint | `LocalAuraContentColor` |
| `icon` | Fallback tint | `AuraTheme.colors.content.primary` |

---

## 6. Interaction Motion Contract

- **Passive (`motion_track: "none"`):** `AuraIcon` has no interaction of its own. Press scale (e.g., `motion.leadingIconPressedScale`) and color transitions are applied by the composing component.
- **Reduced Motion:** Not applicable.

---

## 7. Responsive Layout Behavior (`Smallest Width × Display Ratio`)

| Profile | Behavior |
|---|---|
| **`ExtraSmall`** (Cover) | Fixed glyph size; composing components drop optional icons before truncating labels |
| **`Compact`** (Phone Portrait) | Default icon tokens |
| **`Medium` / `Square`** (Foldable `~1:1`) | Unchanged; icons do not scale with width |
| **`UltraWide`** (Phone Landscape) | Unchanged |
| **`Expanded`** (Tablet / DeX) | Unchanged; larger sizes come only from the composing component's size variant |

---

## 8. Accessibility

- Informative icons carry `contentDescription`; decorative icons pass `null` (`ICN-RULE-002`).
- Icon tint must meet `>= 3:1` non-text contrast against its surface (WCAG 1.4.11).
- `AuraIcon` itself is not a touch target; interactive use goes through `AuraIconButton` (`>= 48dp`).

---

## 9. Platform Bindings (Layer B)

### 9.1 Android (Jetpack Compose)

<!-- API:ANDROID:BEGIN -->
```kotlin
@Composable
fun AuraIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalAuraContentColor.current.takeOrElse { AuraTheme.colors.content.primary },
)

@Composable
fun AuraIcon(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalAuraContentColor.current.takeOrElse { AuraTheme.colors.content.primary },
)
```
<!-- API:ANDROID:END -->

### 9.2 iOS (SwiftUI Reference)

<!-- API:IOS:BEGIN -->
```swift
struct AuraIcon: View {
    let image: Image
    let accessibilityLabel: String?
    var tint: Color? = nil   // nil -> @Environment(\.auraContentColor)
}
```
<!-- API:IOS:END -->

### 9.3 Web (React / TypeScript Reference)

<!-- API:WEB:BEGIN -->
```tsx
export interface AuraIconProps {
  icon: React.ComponentType<React.SVGProps<SVGSVGElement>>;
  label?: string;   // omitted -> aria-hidden="true"
  tint?: string;    // defaults to currentColor
}
```
<!-- API:WEB:END -->
