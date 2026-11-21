---
id: "aura.component.radio"
name: "AuraRadio"
category: "inputs"
status: "ready"
version: "1.0.0"
layer: "content"
material_allowed: ["Solid"]
motion_track: "Standard"
platforms:
  android: "com.example.ds.component.AuraRadio"
  ios: "AuraDS.AuraRadio (planned)"
  web: "@aura-ds/react.AuraRadio (planned)"
tokens:
  - "colors.brand.primary"
  - "colors.surface.default"
  - "colors.content.primary"
  - "colors.content.secondary"
  - "colors.content.disabled"
  - "colors.border.strong"
  - "colors.border.subtle"
  - "colors.interaction.selectedContainer"
  - "colors.interaction.disabledContainer"
  - "colors.interaction.pressedOverlay"
  - "typography.bodyLarge"
  - "typography.bodySmall"
  - "motion.pressInSpring"
  - "motion.pressOutSpring"
  - "motion.iconSpring"
  - "motion.stateChangeDurationMillis"
  - "motion.minPressDurationMillis"
  - "motion.pressedScaleDefault"
---

# AuraRadio

> Mutually exclusive single-choice selection control for choosing exactly one option from a small list (`2–5` descriptive choices) inside forms, settings sheets, and configuration panels.

---

## 1. Intent & Usage Principles

### Problem

`AuraRadio` renders a precision `20dp` circular ring with an animated `10dp` center indicator dot inside a `>= 48dp` hit target. It is tailored for descriptive single-selection flows such as **My Finance** base currency (`KRW / USD / EUR`), **Home Launcher** icon grid density (`4×5 / 4×6 / 5×5`), and **My Drink Diary** serving temperature presets.

### When to Use

- Picking **exactly 1 option** from `2–5` mutually exclusive items that benefit from descriptive labels or secondary subtext (`description != null`).
- Inside bottom sheets, dialogs, or settings groups enclosed in a `Modifier.selectableGroup()` parent.

### When NOT to Use & Alternatives

- **Multiple simultaneous selections:** Use `AuraCheckbox`.
- **Single binary On/Off toggle:** Never use a single radio button to turn one setting on/off (a selected radio cannot be deselected by tapping it again); use `AuraSwitch` or `AuraCheckbox`.
- **Compact 1–2 word header mode switching (`Day | Week | Month`):** Prefer `AuraSegmentedButton`.
- **Lists exceeding 6–8 items:** Use a searchable picker sheet or `AuraSearchBar` list instead of a tall radio column.

### Context Rules

- **`RD-RULE-001` (Mutual Exclusion & Default Selection):** Radio groups should always wrap options in `Modifier.selectableGroup()` and pre-select a sensible initial default unless deliberate empty-state validation is required.
- **`RD-RULE-002` (Non-Deselectable Tap Semantics):** Tapping an already selected `AuraRadio` must keep it selected (`Role.RadioButton` `selectable` contract), never toggling back to empty.
- **`RD-RULE-003` (48dp Hit Box & Row Expansion):** Standalone circles guarantee `>= 48dp` hit bounds; when `label != null`, the full row acts as the selectable container.

### Do / Don't

| Do | Don't |
|---|---|
| Group 2–5 mutually exclusive `AuraRadio` items in a `Column` with `selectableGroup()`. | Render a single standalone `AuraRadio` as a checkbox substitute. |
| Use `description` to explain the impact of choosing a preset (`"4×5 · Balanced dock spacing"`). | Wrap entire multi-paragraph forms inside one radio option row. |
| Use `AuraSegmentedButton` for short horizontal view toggles in headers. | Stack 2–3 word horizontal radio buttons inside a dense app toolbar. |
| Keep unselected ring stroke at `border.strong` so the affordance stays clear in Dark & Light modes. | Hide the outer ring border until selected. |

---

## 2. Anatomy & Canonical Part IDs

```text
  Unselected:     Selected:       Labeled Row Mode:
     ╭───╮           ╭───╮        ╭────────────────────────────────────────╮
    │     │         │  ●  │       │ (●)  Korean Won (KRW)                  │
     ╰───╯           ╰───╯        │      Format amounts with ₩ and comma   │
                                  ╰────────────────────────────────────────╯
   20dp ring     10dp inner dot   >= 48dp hit target row (Role.RadioButton)
```

| Part ID | Responsibility | Required | Semantics Owner |
|---|---|---|---|
| `root` | Standalone `48dp` hit box, or the full labeled row when `label != null` | Yes | `Role.RadioButton` (`selectable`) |
| `ring` | Outer circle stroke | Yes | Decorative |
| `dot` | Inner filled circle shown when selected | Conditional | Decorative |
| `label` | Primary row text | No | Merged into `root` |
| `description` | Secondary row text below `label` | No | Merged into `root` |

---

## 3. Variant, Size & Material Matrix

- **Outer Ring Diameter:** `20dp`, `2dp` stroke (`1.75dp` when unselected).
- **Inner Dot Diameter:** `10dp` filled circle centered inside the ring, scaling smoothly `0f -> 1f` when selected.
- **Layout modes:** Standalone (no `label`) or Labeled Row (`label` / `description`).
- **Material:** `Solid` only (`layer: content`).

---

## 4. State Precedence & Behavior

**State Precedence:** `Disabled` > `Pressed` > `Selected` / `Unselected`.

```mermaid
stateDiagram-v2
    [*] --> Unselected
    Unselected --> Pressed: Pointer Down (onClick != null)
    Pressed --> Selected: Pointer Up / Click
    Selected --> Pressed: Pointer Down (keeps Selected)
    Unselected --> Disabled: enabled = false
    Selected --> Disabled: enabled = false
```

---

## 5. Semantic Token Bindings

| Element | State | Light / Dark Semantic Token |
|---|---|---|
| Outer Ring Stroke | Selected (`Enabled`) | `AuraTheme.colors.brand.primary` |
| Outer Ring Stroke | Unselected (`Enabled`) | `AuraTheme.colors.border.strong` |
| Outer Ring Stroke | `Disabled` | `AuraTheme.colors.content.disabled` |
| Outer Ring Interior | Unselected (`Enabled`) | `AuraTheme.colors.surface.default` |
| Outer Ring Interior | `Disabled` | `AuraTheme.colors.interaction.disabledContainer` |
| Inner Dot Fill | Selected (`Enabled`) | `AuraTheme.colors.brand.primary` |
| Inner Dot Fill | Selected (`Disabled`) | `AuraTheme.colors.content.disabled` |
| Press Overlay | `Pressed` | `AuraTheme.colors.interaction.pressedOverlay` |
| `label` Text | `Enabled` / `Disabled` | `AuraTheme.colors.content.primary` / `content.disabled` (`typography.bodyLarge`) |
| `description` Text | `Enabled` / `Disabled` | `AuraTheme.colors.content.secondary` / `content.disabled` (`typography.bodySmall`) |

---

## 6. Interaction Motion Contract

Consumes `AuraTheme.motion`:

1. **Inner Dot Spring Scale:** When `selected` changes (`false <-> true`), the inner `10dp` dot scales between `0f` and `1f` with `AuraTheme.motion.iconSpring()`, creating a crisp elastic pop without sluggishness.
2. **Tactile Press Feedback:** Uses `rememberAuraTactilePressed(interactionSource, motion.minPressDurationMillis)` to compress the radio visual circle to `AuraTheme.motion.pressedScaleDefault` (`0.96f`) via `pressInSpring()` / `pressOutSpring()`.
3. **Color Transitions:** Ring stroke color and background tint transition over `AuraTheme.motion.stateChangeDurationMillis` (`150ms`).
4. **Reduced Motion:** Uses `snap()` when system animations are disabled (`ANIMATOR_DURATION_SCALE == 0f`).

---

## 7. Responsive Layout Behavior (`Smallest Width × Display Ratio`)

`AuraRadio` adapts layout geometry cleanly across all Aura viewport width and display profiles (`AuraWindowInfo`):

| Width / Display Profile | Standalone Ring Behavior | Labeled Radio Row Behavior |
|---|---|---|
| `ExtraSmall` (`< 360dp`) | Centered `20dp` ring inside `48dp × 48dp` accessible hit bounds. | Top-aligned indicator stays anchored to the first line of `label` when narrow widths wrap labels onto 2 lines. |
| `Compact` (`360–599dp`) | Vertical radio list fills full column width (`12dp` horizontal gap between circle and label). | Full-row selectable target with generous `48dp+` vertical height prevents mis-taps between adjacent rows. |
| `Square` (`~1:1` Multi-window) | Retains full `48dp` minimum hit bounds inside pop-up floating panels. | Vertical radio options remain scannable even when side-by-side preview panes tighten horizontally. |
| `UltraWide` (`> 21:9`) | Maintains crisp leading alignment inside compact list containers. | Caps horizontal expansion to parent `AuraCard` / pane constraints so labels and descriptions remain cohesive. |
| `Expanded` (`>= 840dp` Tablet) | Displays inside secondary inspector forms or multi-column preference groups. | Pairs cleanly inside `AuraCard` selection containers without drifting apart on wide tablet viewports. |

---

## 8. Accessibility & Guardrails

- [ ] Registers `Role.RadioButton` via `Modifier.selectable(selected = selected, ...)`.
- [ ] Enforces `>= 48dp` minimum touch height and width on standalone and row modes.
- [ ] Grouping helper in screens applies `Modifier.selectableGroup()` so TalkBack announces `"in list, 1 of N"`.
- [ ] Verified reduced-motion mode sets dot scale immediately without animation lag.

---

## 9. Platform Bindings

### 9.1 Android (Jetpack Compose)

- **Source:** [`AuraRadio.kt`](../../../design-system/src/main/kotlin/com/example/ds/component/AuraRadio.kt)

<!-- API:ANDROID:BEGIN -->
```kotlin
@Composable
fun AuraRadio(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    description: String? = null,
)
```
<!-- API:ANDROID:END -->

### 9.2 iOS (SwiftUI Reference)

<!-- API:IOS:BEGIN -->
```swift
public struct AuraRadio: View {
    public init(
        isSelected: Bool,
        action: (() -> Void)? = nil,
        isEnabled: Bool = true,
        label: String? = nil,
        description: String? = nil
    )
}
```
<!-- API:IOS:END -->

### 9.3 Web (React / TypeScript Reference)

<!-- API:WEB:BEGIN -->
```ts
export interface AuraRadioProps {
  selected: boolean;
  onSelect?: () => void;
  disabled?: boolean;
  label?: string;
  description?: string;
  name?: string;
  className?: string;
}
```
<!-- API:WEB:END -->
