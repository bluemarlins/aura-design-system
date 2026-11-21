---
id: "aura.component.chip-button"
name: "AuraChipButton"
category: "actions"
status: "ready"
version: "1.0.0"
layer: "content"
material_allowed: ["Solid", "Glass"]
motion_track: "Standard"
platforms:
  android: "io.github.bluemarlins.aura.component.AuraChipButton"
  ios: "AuraDS.AuraChipButton (planned)"
  web: "@aura-ds/react.AuraChipButton (planned)"
tokens:
  - "colors.brand.primary"
  - "colors.brand.onPrimary"
  - "colors.brand.subtle"
  - "colors.brand.onSubtle"
  - "colors.surface.default"
  - "colors.surface.raised"
  - "colors.surface.sunken"
  - "colors.surface.floating"
  - "colors.content.primary"
  - "colors.content.secondary"
  - "colors.content.disabled"
  - "colors.border.default"
  - "colors.border.subtle"
  - "colors.border.focus"
  - "colors.interaction.selectedContainer"
  - "colors.interaction.onSelected"
  - "colors.interaction.disabledContainer"
  - "colors.interaction.pressedOverlay"
  - "typography.labelMedium"
  - "typography.labelSmall"
  - "motion.pressInSpring"
  - "motion.pressOutSpring"
  - "motion.iconSpring"
  - "motion.stateChangeDurationMillis"
  - "motion.minPressDurationMillis"
  - "motion.pressedScaleDefault"
  - "motion.pressedScaleGhost"
  - "motion.leadingIconPressedScale"
---

# AuraChipButton

> Compact capsule control for dataset filtering, category selection, attribute tagging, and lightweight contextual assist actions.

---

## 1. Intent & Usage Principles

`AuraChipButton` bridges static metadata badges and full `AuraButton` triggers. Unlike `AuraButton` (designed for discrete transactional commitments) or `AuraIconButton` (single-glyph triggers), `AuraChipButton` keeps labels short (`1–2` words) inside a slim pill silhouette while upholding `48dp` accessible vertical touch targets.

### Problem

Users need a compact, scannable way to filter a dataset, pick a category, or tag an item without the visual weight of a full button.

### When to Use

- Filtering lists, charts, or component catalogs (`Filter` variant).
- Toggling compact tasting notes, moods, or organizer tags (`Tag` variant).
- Presenting inline contextual suggestions or quick-jump shortcuts (`Assist` variant).
- Floating period or layer toggles over media cards or Floating AppBars (`Glass` variant).

### When NOT to Use & Alternatives

- Primary screen commitment (`Save`, `Publish`, `Delete`) -> use `AuraButton`.
- Compact control with no text label -> use `AuraIconButton`.
- Mutually exclusive long-running multi-page primary navigation -> use dedicated navigation tabs / `AuraTopAppBar`.

### Context Rules

- **`CHIP-RULE-001` (Concise Single-Line Labels)**: Keep `label` under `1–3` short words (`maxLines = 1`). Never wrap paragraphs inside a chip or treat a chip as a replacement for a full-width form button.
- **`CHIP-RULE-002` (Variant Semantic Separation)**:
  - **`Filter`**: Dataset segmentation and category tabs (e.g., Catalog category ribbon, My Finance expense/income filter, Responsive WindowPreset switcher). Communicates explicit checked/selected state.
  - **`Tag`**: Attribute or tasting/journal descriptor pills (e.g., My Drink Diary flavor notes `"Smoky"`, `"Berry"`, `"Oak"`, My Planner topic tags). Can toggle active highlight or remain calm neutral pills.
  - **`Assist`**: Lightweight contextual shortcut or reset trigger (e.g., `"Today"`, `"Reset"`, `"Add note"`), typically paired with a leading icon and clear action affordance.
  - **`Glass`**: Frosted translucent capsule built for placement directly over hero tasting photography, launcher wallpapers, or inside Floating AppBar sub-ribbons.
- **`CHIP-RULE-003` (Strip Arrangement Across Breakpoints)**: Horizontal scrollable strips (`LazyRow` / `horizontalScroll`) suit unbounded filter catalogs; wrapping `FlowRow` clusters suit finite `3–8` option sets where comparing all choices at once matters.
- **`CHIP-RULE-004` (Accessible Touch Wrapper)**: Visual capsule height (`28dp` `Small`, `34dp` `Medium`) MUST sit centered inside a minimum `48dp` interactive height wrapper so rapid taps never miss in dense strips.

### Do / Don't

| Do | Don't |
|---|---|
| Keep chip text concise (`"Actions"`, `"1M"`, `"Smoky"`) so strips scan rapidly. | Put multi-line descriptions or primary CTA labels inside `AuraChipButton`. |
| Use `Filter` for mutually-comparable filter criteria and `Tag` for descriptor attributes. | Mix clickable `Filter` chips and non-interactive status pills with identical appearance without clear state styling. |
| Provide a clear visual contrast jump (`interaction.selectedContainer` / `brand.primary`) when `selected == true`. | Rely on a tiny `1dp` border color tint alone to signal active selection. |
| Use `Glass` over busy imagery/gradients and `Filter`/`Tag` inside solid forms. | Drop transparent `Glass` chips onto plain monochrome backgrounds where `outlined`/`subtle` border definition gets lost. |

---

## 2. Anatomy & Canonical Part IDs

```text
  ┌──────────────────────────────┐   <- 48dp min hit height (invisible)
  │ ╭──────────────────────────╮ │
  │ │ [icon]  Label   [icon]   │ │   <- 28dp / 34dp visual capsule
  │ ╰──────────────────────────╯ │
  └──────────────────────────────┘
```

| Part ID | Responsibility | Required | Semantics Owner |
|---|---|---|---|
| `touchTarget` | `48dp` minimum hit height around the capsule | Yes | `root` |
| `root` | Capsule fill, border, and horizontal padding | Yes | `Role.Checkbox` (`Filter`) / `Role.Button` (others) |
| `leadingIcon` | Optional cue before `label` | No | Decorative (merged) |
| `label` | Single-line chip text | Yes | Accessible name |
| `trailingIcon` | Optional cue after `label` (e.g. remove) | No | Decorative (merged) |

---

## 3. Variant, Size & Material Matrix

### Quick Spec

Measured against the rendered component by `SpecMeasurementTest`. Keys are `<Part ID>.<metric>`; dimensions in `dp`, type in `sp`.

<!-- QUICK-SPEC:BEGIN -->
```yaml
sizes:
  Small:
    touchTarget.height: 48
    root.height: 28
    root.paddingHorizontal: 10
    leadingIcon.size: 14
    label.fontSize: 11
    label.lineHeight: 16
  Medium:
    touchTarget.height: 48
    root.height: 34
    root.paddingHorizontal: 14
    leadingIcon.size: 16
    label.fontSize: 13
    label.lineHeight: 18
```
<!-- QUICK-SPEC:END -->

### Variants (`AuraChipVariant`)

1. **`Filter`** — Interactive segmentation capsule with crisp selected container inversion and checkbox/toggle accessibility semantics.
2. **`Tag`** — Soft attribute capsule (`surface.default` resting -> `brand.subtle` / `brand.onSubtle` selected) tailored for tasting notes and diary tags.
3. **`Assist`** — Subtle elevated suggestion pill (`surface.raised` + `border.default`) designed for quick actions (`Today`, `Clear`).
4. **`Glass`** — Semi-transparent frosted pill (`surface.floating @ 0.78 alpha` resting -> `brand.primary @ 0.92 alpha` selected) with luminous `border.subtle`.

### Sizes (`AuraChipSize`)

| Size | Visual Capsule Height | Min Hit Height | Horizontal Padding | Icon Size | Typography Token |
|---|---|---|---|---|---|
| `Small` | `28dp` | `48dp` | `10dp` | `14dp` | `AuraTheme.typography.labelSmall` |
| `Medium` | `34dp` | `48dp` | `14dp` | `16dp` | `AuraTheme.typography.labelMedium` |

---

## 4. State Precedence & Behavior

1. **Disabled (`!enabled`)**: Non-interactive, `interaction.disabledContainer`, `content.disabled`, zero press scale.
2. **Pressed**: Tactile scale compression + `interaction.pressedOverlay` tint, latched for `minPressDurationMillis`.
3. **Selected (`selected == true`)**: Active selected surface, `1dp` highlighted border (or solid fill on `Glass`), elevated content contrast.
4. **Focused / Hovered**: Focus outline (`border.focus`) for keyboard navigation and subtle surface lift on pointer hover.
5. **Default**: Resting unselected tokens.

Clicks are debounced via `debounceIntervalMs` (default `200L` ms).

---

## 5. Semantic Token Bindings

All colors, typography, and motion resolve through `AuraTheme`:

| Variant | Unselected Container | Selected Container | Unselected Content | Selected Content | Border (`1dp`) |
|---|---|---|---|---|---|
| `Filter` | `surface.sunken` | `interaction.selectedContainer` | `content.secondary` | `interaction.onSelected` | `border.subtle` -> `brand.primary` |
| `Tag` | `surface.default` | `brand.subtle` | `content.primary` | `brand.onSubtle` | `border.default` -> `brand.primary` |
| `Assist` | `surface.raised` | `interaction.selectedContainer` | `content.primary` | `interaction.onSelected` | `border.default` -> `brand.primary` |
| `Glass` | `surface.floating` (`0.78f` alpha) | `brand.primary` (`0.92f` alpha) | `content.primary` | `brand.onPrimary` | `border.subtle` (`1dp`) |

Disabled (`enabled == false`) across all variants resolves to `interaction.disabledContainer`, `content.disabled`, and `border.subtle`.

---

## 6. Interaction Motion Contract

- **Tactile Press**: Spring-driven scale down to `pressedScaleDefault` (`0.96f`) via `AuraTheme.motion.pressInSpring()`, with optional `leadingIcon` micro-scale to `leadingIconPressedScale` (`0.92f`) via `iconSpring()`.
- **Minimum Press Latch**: Holds pressed feedback for at least `AuraTheme.motion.minPressDurationMillis` (`110ms`) before releasing via `AuraTheme.motion.pressOutSpring()`, preventing vanished taps inside scrollable `LazyRow` strips.
- **Selection Morph**: Container background, border color, and label/icon content color crossfade with `tween(AuraTheme.motion.stateChangeDurationMillis)`.
- **Reduced Motion (`ANIMATOR_DURATION_SCALE == 0`)**: Keeps scale fixed at `1.0f` and switches selection/hover colors instantaneously (`0ms`).

---

## 7. Responsive Layout Behavior (`Smallest Width × Display Ratio`)

| Profile | Width / Ratio Condition | Layout & Strip Behavior |
|---|---|---|
| `ExtraSmall` | `sw < 360dp` | Switch multi-chip sets (>3 items) to horizontal scroll (`LazyRow`) or tight `FlowRow`; consider `Small` (`28dp` visual) to avoid awkward single-chip wrapping lines. |
| `Compact` | `360–599dp` | Default `Medium` (`34dp` visual) inside `FlowRow` (`8dp` horizontal gap) or scrollable filter ribbons under `AuraTopAppBar`. |
| `Medium · Square` | `600–839dp`, `ratio < 1.35` | Display entire `3–8` chip groups in balanced multi-row `FlowRow` grids so foldables reveal all options without hidden horizontal scroll. |
| `Medium · UltraWide` | `ratio >= 2.1` | Arrange chips in a single horizontal strip (`LazyRow` / single-line `FlowRow`) paired with `Small` sizing to preserve vertical content height. |
| `Expanded` | `width >= 840dp` | Align full filter bars cleanly within single rows inside primary/inspector headers, exposing pointer hover states and keyboard `Tab` focus rings. |

---

## 8. Accessibility & Guardrails

- **Touch Target**: Enforces `minHeight = 48.dp` around every chip regardless of `Small` or `Medium` visual height.
- **Role & Selection Semantics**:
  - `Filter` variant exposes `Role.Checkbox` paired with `ToggleableState.On / Off` so screen readers announce exact selection changes (`"Checked"` / `"Unchecked"`).
  - `Tag` variant exposes `Role.Button` + `selected = selected`.
  - `Assist` and `Glass` expose `Role.Button` (plus `selected` state when `selected == true`).
- **Text Contrast & Truncation**: Guarantees `>= 4.5:1` label contrast in both default and selected states across Light and Dark modes, single-line ellipsis truncation without clipping icons.

---

## 9. Platform Bindings

### 9.1 Android (Jetpack Compose)

- **Source:** [`AuraChipButton.kt`](../../../design-system/src/main/kotlin/io/github/bluemarlins/aura/component/AuraChipButton.kt)

<!-- API:ANDROID:BEGIN -->
```kotlin
package io.github.bluemarlins.aura.component

enum class AuraChipVariant {
    Filter,
    Tag,
    Assist,
    Glass,
}

enum class AuraChipSize {
    Small,
    Medium,
}

@Composable
fun AuraChipButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    variant: AuraChipVariant = AuraChipVariant.Filter,
    size: AuraChipSize = AuraChipSize.Medium,
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    debounceIntervalMs: Long = 200L,
    interactionSource: MutableInteractionSource? = null,
)
```
<!-- API:ANDROID:END -->

### 9.2 iOS (SwiftUI Reference)

<!-- API:IOS:BEGIN -->
```swift
public enum AuraChipVariant {
    case filter, tag, assist, glass
}

public enum AuraChipSize {
    case small, medium
}

public struct AuraChipButton<LeadingIcon: View, TrailingIcon: View>: View {
    public init(
        _ label: String,
        isSelected: Bool = false,
        variant: AuraChipVariant = .filter,
        size: AuraChipSize = .medium,
        isEnabled: Bool = true,
        @ViewBuilder leadingIcon: () -> LeadingIcon,
        @ViewBuilder trailingIcon: () -> TrailingIcon,
        action: @escaping () -> Void
    )
}
```
<!-- API:IOS:END -->

### 9.3 Web (React / TypeScript Reference)

<!-- API:WEB:BEGIN -->
```ts
export type AuraChipVariant = 'filter' | 'tag' | 'assist' | 'glass';
export type AuraChipSize = 'small' | 'medium';

export interface AuraChipButtonProps {
  label: string;
  onClick: () => void;
  selected?: boolean;
  variant?: AuraChipVariant;
  size?: AuraChipSize;
  disabled?: boolean;
  leadingIcon?: React.ReactNode;
  trailingIcon?: React.ReactNode;
  debounceMs?: number;
}
```
<!-- API:WEB:END -->
