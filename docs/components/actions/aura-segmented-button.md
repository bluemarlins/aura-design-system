---
id: "aura.component.segmented-button"
name: "AuraSegmentedButton"
category: "actions"
status: "ready"
version: "1.0.0"
layer: "content"
material_allowed: ["Solid", "Glass"]
motion_track: "Standard"
platforms:
  android: "com.example.ds.component.AuraSegmentedButton"
  ios: "AuraDS.AuraSegmentedButton (planned)"
  web: "@aura-ds/react.AuraSegmentedButton (planned)"
tokens:
  - "colors.brand.primary"
  - "colors.brand.onPrimary"
  - "colors.surface.default"
  - "colors.surface.sunken"
  - "colors.surface.floating"
  - "colors.content.primary"
  - "colors.content.secondary"
  - "colors.content.disabled"
  - "colors.border.subtle"
  - "colors.border.default"
  - "colors.border.focus"
  - "colors.interaction.selectedContainer"
  - "colors.interaction.onSelected"
  - "colors.interaction.disabledContainer"
  - "colors.interaction.pressedOverlay"
  - "typography.labelLarge"
  - "typography.labelMedium"
  - "typography.labelSmall"
  - "motion.pressInSpring"
  - "motion.pressOutSpring"
  - "motion.selectionSpring"
  - "motion.iconSpring"
  - "motion.stateChangeDurationMillis"
  - "motion.minPressDurationMillis"
  - "motion.pressedScaleDefault"
  - "motion.leadingIconPressedScale"
---

# AuraSegmentedButton

> Immediate mutually exclusive view-mode, timeframe, currency, or canvas layer switcher (`2` to `5` fixed options) backed by a spring-animated sliding thumb indicator.

---

## 1. Intent & Usage Principles

Aligned with [00-button-principles.md](00-button-principles.md), `AuraSegmentedButton` lets users switch the active representation, scale, or unit of the **current screen** instantaneously without pressing a confirmation CTA (e.g., `Day | Week | Month` in **My Planner**, `1W | 1M | 1Y | All` or `KRW | USD` in **My Finance**, `All | Wine | Whiskey` in **My Drink Diary**, `Icons | Widgets | 3D` in **Home Launcher**).

### Problem

Users need to switch immediately between 2–5 mutually exclusive views or modes (timeframe, currency, layer) without a submit step.

### When to Use

- Switching chart timeframes (`1W | 1M | 1Y | All`) or display currencies (`KRW | USD`) in **My Finance**.
- Switching calendar granularity (`Day | Week | Month`) in **My Planner**.
- Switching collection scope (`All | Wine | Whiskey`) in **My Drink Diary**.
- Switching wallpaper/desktop customization modes (`Icons | Widgets | 3D`) in **Home Launcher** (`Glass` variant).

### When NOT to Use & Alternatives

- Additive multi-filter selection or tasting attribute tags -> use `AuraChipButton`.
- Global top-level application tabs -> use dedicated navigation tabs / `AuraNavigationBar`.
- Binary on/off setting inside preferences -> use `AuraSwitch` (`planned`).

### Context Rules

- **`SEG-RULE-001` (2 to 5 Mutually Exclusive Options)**: Always use `AuraSegmentedButton` for **2 to 5 fixed, peer, mutually exclusive choices**. If selections are non-exclusive (multi-select) or exceed 5 items, use `AuraChipButton` strips instead.
- **`SEG-RULE-002` (Concise Equal-Width Labels)**: Keep each option label short (`1` word or `<= 8` characters, e.g., `"1M"`, `"Wine"`, `"Week"`) so equal-width segments never truncate awkwardly across narrow devices.
- **`SEG-RULE-003` (Immediate Screen Effect, Never Form Submit or Top-Level Tabs)**: Selecting a segment updates the active viewport immediately; do not use `AuraSegmentedButton` as primary app-wide destination navigation (`AuraNavigationBar`) or destructive/save form commands (`AuraButton`).
- **`SEG-RULE-004` (Accessible Touch Height)**: Regardless of slim visual track heights (`Small` `32dp`, `Medium` `40dp`), each segment enforces a `>= 48dp` interactive touch height centered cleanly around the capsule track.

### Do / Don't

| Do | Don't |
|---|---|
| Restrict segments to `2–5` fixed mutually exclusive options with crisp 1-word labels. | Put `6+` options or wrap multi-line sentences inside `AuraSegmentedButton`. |
| Update the visible chart, list, or view immediately upon segment selection. | Require clicking a separate "Apply" button after toggling a segmented view switcher. |
| Use `Glass` over wallpapers/3D canvases and `Surface`/`Outlined` on card/form headers. | Drop translucent `Glass` tracks onto plain flat white settings cards where `Surface` provides cleaner contrast. |
| Preserve equal-width segments (`fullWidth = true` on mobile) so sliding thumb motion feels balanced. | Mix wide paragraph segments next to 2-letter segments causing jarring thumb width jumps. |

---

## 2. Anatomy & Canonical Part IDs

```text
  ╭───────────────────────────────────────────╮
  │ ╭─────────╮                               │  <- track
  │ │  Day    │    Week          Month        │  <- sliding thumb under selected segment
  │ ╰─────────╯                               │
  ╰───────────────────────────────────────────╯
```

| Part ID | Responsibility | Required | Semantics Owner |
|---|---|---|---|
| `root` | Track container (`fullWidth` by default) | Yes | `selectableGroup()` |
| `thumb` | Sliding indicator under the selected segment | Yes | Decorative |
| `segment` | One option (`options[i]`) with optional icon | Yes | `Role.Tab` (`selected`) |
| `segmentIcon` | Optional icon from `optionIcons[i]` | No | Decorative (merged) |

---

## 3. Variant, Size & Material Matrix

### Variants (`AuraSegmentedVariant`)

1. **`Surface`** — Default recessed pill track (`surface.sunken`) paired with a crisp elevated sliding thumb (`surface.floating` + `2dp` subtle shadow and active `content.primary` text).
2. **`Outlined`** — Clean transparent/default track framed by a `1dp` hairline border (`border.default`) and brand-tinted selected thumb (`interaction.selectedContainer` / `interaction.onSelected`).
3. **`Glass`** — Frosted translucent track (`surface.floating @ 0.72 alpha`, `border.subtle`) with solid/bright selected pill (`brand.primary` / `brand.onPrimary`) built for wallpaper/3D canvases and floating headers.

### Sizes (`AuraSegmentedSize`)

| Size | Visual Track Height | Min Hit Height | Segment Inner Padding | Icon Size | Typography Token |
|---|---|---|---|---|---|
| `Small` | `32dp` | `48dp` | `8dp` | `14dp` | `AuraTheme.typography.labelSmall` |
| `Medium` | `40dp` | `48dp` | `12dp` | `16dp` | `AuraTheme.typography.labelMedium` |
| `Large` | `48dp` | `48dp` | `16dp` | `18dp` | `AuraTheme.typography.labelLarge` |

---

## 4. State Precedence & Behavior

1. **Disabled (`!enabled`)**: All segments non-interactive, muted thumb, `content.disabled` typography, zero press scale or slide reaction.
2. **Pressed (Segment)**: Individual pressed segment content scales cleanly using `pressedScaleDefault` (`0.96f`) while latched for `minPressDurationMillis`, plus subtle `interaction.pressedOverlay` tint.
3. **Selected (`index == selectedIndex`)**: Sliding thumb indicator aligns under the active option; active text/icon transitions smoothly to active token colors.
4. **Focused / Hovered**: Keyboard focus draws `2dp` `border.focus` ring inside the segment bounds; pointer hover lightly tints inactive segments.
5. **Default**: Resting unselected segment colors over track.

---

## 5. Semantic Token Bindings

All colors, typography, and motion resolve through `AuraTheme`:

| Variant | Track Background | Track Border | Active Thumb Fill | Active Content | Inactive Content |
|---|---|---|---|---|---|
| `Surface` | `surface.sunken` | `border.subtle` (`1dp`) | `surface.floating` (`2dp` shadow) | `content.primary` | `content.secondary` |
| `Outlined` | `surface.default` | `border.default` (`1dp`) | `interaction.selectedContainer` | `interaction.onSelected` | `content.secondary` |
| `Glass` | `surface.floating` (`0.72f` alpha) | `border.subtle` (`1dp`) | `brand.primary` | `brand.onPrimary` | `content.primary` |

Disabled (`enabled == false`) renders track container in `interaction.disabledContainer`, border in `border.subtle`, inactive labels in `content.disabled`, and a softened active thumb without shadow.

---

## 6. Interaction Motion Contract

- **Sliding Active Indicator**: Active indicator offset animates horizontally across equal-width segment slots using `AuraTheme.motion.selectionSpring()` (`0.6%` overshoot, `< 1dp` on a `100dp` segment). The track itself never scales on press.
- **Tactile Segment Press**: Pressed segment content scales to `AuraTheme.motion.pressedScaleDefault` (`0.96f`) via `pressInSpring()` / `pressOutSpring()` with `minPressDurationMillis` (`110ms`) press latch, while optional segment icon scales via `iconSpring()` to `leadingIconPressedScale` (`0.92f`).
- **Color Crossfade**: Selected/unselected label and icon colors crossfade via `tween(AuraTheme.motion.stateChangeDurationMillis)`.
- **Reduced Motion (`ANIMATOR_DURATION_SCALE == 0`)**: Replaces sliding spring and color/scale curves with `snap()` instant index switching so screen-reader and motion-sensitive users experience zero horizontal travel.

---

## 7. Responsive Layout Behavior (`Smallest Width × Display Ratio`)

| Profile | Width / Ratio Condition | Track Width & Sizing Strategy |
|---|---|---|
| `ExtraSmall` | `sw < 360dp` | Use `fullWidth = true` + `Small` (`32dp` visual, `48dp` hit); limit labels to concise short forms (`1W/1M/1Y`) and omit secondary `optionIcons` if `options.size >= 4`. |
| `Compact` | `360–599dp` | Default `Medium` (`40dp` visual, `48dp` hit) with `fullWidth = true` inside page headers and cards. |
| `Medium · Square` | `600–839dp`, `ratio < 1.35` | Cap `AuraSegmentedButton` max width cleanly around `420–480dp` (`fullWidth = false` or parent `widthIn(max = 480.dp)`) so `2–3` segments don't stretch overly wide. |
| `Medium · UltraWide` | `ratio >= 2.1` | Switch to `Small` (`32dp` visual) and compact intrinsic width (`fullWidth = false`) inside inline horizontal toolbars to preserve vertical chart height. |
| `Expanded` | `width >= 840dp` | Pair `Medium` or `Large` with `fullWidth = false` in toolbar strips or fixed-width inspector panels (`280–420dp`), highlighting pointer hover and keyboard `Tab` focus rings. |

---

## 8. Accessibility & Guardrails

- **Selectable Group Semantics**: Container exposes `Modifier.selectableGroup()` so screen readers announce item positions (e.g., `"Week, 2 of 3"`).
- **Per-Segment Role & State**: Each segment exposes `Role.Tab` and `selected = (index == selectedIndex)` plus disabled state when `!enabled`.
- **Touch Bounds**: Enforces `minHeight = 48.dp` touch bounds vertically across every segment size (`Small`, `Medium`, `Large`).
- **Truncation**: Single-line ellipsis truncation keeps equal segment proportions stable even under enlarged system font scales.

---

## 9. Platform Bindings

### 9.1 Android (Jetpack Compose)

- **Source:** [`AuraSegmentedButton.kt`](../../../design-system/src/main/kotlin/com/example/ds/component/AuraSegmentedButton.kt)

<!-- API:ANDROID:BEGIN -->
```kotlin
package com.example.ds.component

enum class AuraSegmentedVariant {
    Surface,
    Outlined,
    Glass,
}

enum class AuraSegmentedSize {
    Small,
    Medium,
    Large,
}

@Composable
fun AuraSegmentedButton(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    variant: AuraSegmentedVariant = AuraSegmentedVariant.Surface,
    size: AuraSegmentedSize = AuraSegmentedSize.Medium,
    enabled: Boolean = true,
    fullWidth: Boolean = true,
    optionIcons: List<(@Composable () -> Unit)?>? = null,
)
```
<!-- API:ANDROID:END -->

### 9.2 iOS (SwiftUI Reference)

<!-- API:IOS:BEGIN -->
```swift
public enum AuraSegmentedVariant {
    case surface, outlined, glass
}

public enum AuraSegmentedSize {
    case small, medium, large
}

public struct AuraSegmentedButton: View {
    public init(
        options: [String],
        selectedIndex: Int,
        variant: AuraSegmentedVariant = .surface,
        size: AuraSegmentedSize = .medium,
        isEnabled: Bool = true,
        fullWidth: Bool = true,
        onOptionSelected: @escaping (Int) -> Void
    )
}
```
<!-- API:IOS:END -->

### 9.3 Web (React / TypeScript Reference)

<!-- API:WEB:BEGIN -->
```ts
export type AuraSegmentedVariant = 'surface' | 'outlined' | 'glass';
export type AuraSegmentedSize = 'small' | 'medium' | 'large';

export interface AuraSegmentedButtonProps {
  options: string[];
  selectedIndex: number;
  onOptionSelected: (index: number) => void;
  variant?: AuraSegmentedVariant;
  size?: AuraSegmentedSize;
  disabled?: boolean;
  fullWidth?: boolean;
  optionIcons?: Array<React.ReactNode | null>;
}
```
<!-- API:WEB:END -->
