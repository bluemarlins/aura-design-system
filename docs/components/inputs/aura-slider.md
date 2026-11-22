---
id: "aura.component.slider"
name: "AuraSlider"
category: "inputs"
status: "ready"
version: "1.0.0"
layer: "content"
material_allowed: ["Solid"]
motion_track: "Standard"
platforms:
  android: "io.github.bluemarlins.aura.component.AuraSlider"
  ios: "AuraDS.AuraSlider (planned)"
  web: "@aura-ds/react.AuraSlider (planned)"
tokens:
  - "colors.brand.primary"
  - "colors.brand.onPrimary"
  - "colors.surface.sunken"
  - "colors.surface.default"
  - "colors.content.primary"
  - "colors.content.secondary"
  - "colors.content.disabled"
  - "colors.border.default"
  - "colors.border.subtle"
  - "typography.labelLarge"
  - "typography.labelMedium"
  - "typography.bodySmall"
  - "typography.monoMedium"
  - "motion.pressInSpring"
  - "motion.pressOutSpring"
  - "motion.selectionSpring"
  - "motion.stateChangeDurationMillis"
  - "motion.pressedScaleDefault"
---

# AuraSlider

> Continuous and stepped range slider designed for sensory flavor profiling (**My Drink Diary** Body/Tannin/Acidity/Peat), wallpaper blur/3D parallax depth (**Home Launcher**), and portfolio allocation percentages (**My Finance**).

---

## 1. Intent & Usage Principles

### Problem

Users need to choose a value whose relative position on a range matters more than its exact number, by dragging rather than typing.

### When to Use

- Selecting a continuous or quantized numeric value inside a fixed visual range (`0f..1f`, `1..5`, `6°C..20°C`, `0%..100%`).
- Adjusting tasting intensity notes where relative position along a spectrum is meaningful (**My Drink Diary**).
- Tuning live visual controls such as blur strength or haptic amplitude (**Home Launcher**).

### When NOT to Use & Alternatives

- **Precise monetary amounts (`₩15,430,200`) or ABV decimals (`46.3%`):** Use `AuraNumberField` for exact keyboard entry.
- **5-star satisfaction rating:** Use `AuraRatingBar`.
- **Binary On/Off toggle:** Use `AuraSwitch`.

### Context Rules

- **`SLD-RULE-001` (Paired Header Readout):** Supply `label` and live formatted `valueLabel` whenever exact numeric feedback helps comprehension so users see the active value above the thumb without covering it with their finger.
- **`SLD-RULE-002` (Stepped Snap When Discrete):** Use `steps > 0` for discrete scales (e.g., `steps = 3` for a `1..5` sensory rubric -> 5 discrete stops: `1, 2, 3, 4, 5`) with crisp track tick markers.
- **`SLD-RULE-003` (48dp Minimum Interactive Track Box):** Keep the visual track sleek (`6dp`) while preserving a full `48dp` height touch box for effortless dragging.

### Do / Don't

| Do | Don't |
|---|---|
| Pair `label = "Body"` and `valueLabel = "Full (4 / 5)"` with `steps = 3` for tasting notes. | Leave users guessing what numerical value a bare slider represents. |
| Provide `onValueChangeFinished` for expensive saves/network calls rather than mutating storage on every drag frame. | Trigger heavy database writes on every single pixel drag delta. |
| Snap discrete values deterministically across both tap and drag gestures. | Allow stepped sliders to visually park halfway between ticks. |
| Render a visible ring around the thumb and high-contrast active fill. | Rely solely on hairline track ticks with poor contrast in Dark theme. |

---

## 2. Anatomy & Canonical Part IDs

```text
  Body                                         4 / 5
  ───────●═══════════════════════════════════════════   (48dp touch height)
  Light-bodied                                 Full
```

| Part ID | Responsibility | Required | Semantics Owner |
|---|---|---|---|
| `root` | `48dp` touch row hosting track and thumb; owns drag/tap gestures | Yes | `setProgress` / progress range |
| `label` | Header text describing the value (`"Body"`) | No | Merged into `root` |
| `valueLabel` | Live formatted readout (`"4 / 5"`) aligned to the header end | No | Merged into `root` |
| `track` | Inactive `6dp` rail across the full width | Yes | Decorative |
| `activeTrack` | Filled portion from `valueRange.start` to the thumb | Yes | Decorative |
| `tick` | Step dots rendered along the track when `steps > 0` | Conditional | Decorative |
| `thumb` | `22dp` draggable knob (`11dp` radius) with ring | Yes | Decorative |
| `thumbHalo` | `40dp` translucent halo (`20dp` radius) visible while dragging | Conditional | Decorative |
| `supportingText` | Helper text below the track | No | Merged into `root` |

---

## 3. Variant, Size & Material Matrix

### Quick Spec

Measured against the rendered component by `SpecMeasurementTest`. Keys are `<Part ID>.<metric>`; dimensions in `dp`, type in `sp`.

<!-- QUICK-SPEC:BEGIN -->
```yaml
sizes:
  Default:
    root.height: 48
```
<!-- QUICK-SPEC:END -->

| Variant | Condition | Behavior |
|---|---|---|
| **Continuous** | `steps == 0` | Smooth floating-point scrub from `valueRange.start` to `valueRange.endInclusive`. |
| **Stepped** | `steps > 0` | Divides `valueRange` into `steps + 1` equal intervals with tick dots rendered directly along the track. |

- **Size:** Single size — `6dp` track, `22dp` thumb, `48dp` minimum touch height.
- **Material:** `Solid` only (`layer: content`).

---

## 4. State Precedence & Behavior

`Disabled` (`!enabled`) > `Dragging / Pressed` > `Focused` > `Rest`.

---

## 5. Semantic Token Bindings

| Element | State | Light / Dark Semantic Token |
|---|---|---|
| Inactive Track | `Enabled` / `Disabled` | `surface.sunken` / `interaction.disabledContainer` |
| Active Track Fill | `Enabled` / `Disabled` | `brand.primary` / `content.disabled` |
| Step Tick Markers | `Active` / `Inactive` | `brand.onPrimary` (`78%`) / `content.tertiary` (`65%`) |
| Thumb Core | `Enabled` / `Disabled` | `surface.default` fill + `brand.primary` (`2.5dp`) ring / `content.disabled` |
| Thumb Glow Halo | `Dragging / Pressed` | `brand.primary` (`18%` alpha) |
| `label` & `valueLabel` | `Enabled` / `Disabled` | `content.primary` (`typography.labelLarge`) & `brand.primary` (`typography.monoMedium`) |

---

## 6. Interaction Motion Contract

Consumes `AuraTheme.motion`:

1. **Thumb Drag / Press Expansion:** Thumb expands gently (`1.0f -> AuraTheme.motion.grabScale`, `1.18f`) and reveals a translucent halo ring via `AuraTheme.motion.pressInSpring()` / `pressOutSpring()` while active.
2. **Tap Jump Smoothness:** Tapping directly on the track animates the fraction with `selectionSpring()`; during live drags the fraction tracks the finger 1:1 (`snap`) so the thumb never lags.
3. **Reduced Motion:** Uses `snap()` under `rememberAuraReducedMotion()`.

---

## 7. Responsive Layout Behavior (`Smallest Width × Display Ratio`)

| Width / Display Profile | Track & Header Adaptation |
|---|---|
| `ExtraSmall` (`< 360dp`) | Track expands across full card/pane width minus `12dp` thumb edge inset so endpoints remain easily reachable near bezels. |
| `Compact` (`360–599dp`) | Full-width control (`fillMaxWidth()`) with clear spacing between `label` and `valueLabel` above the thumb. |
| `Square` (`~1:1` Pop-up / Foldable) | Adapts seamlessly inside modal inspector sheets without shrinking thumb radius. |
| `UltraWide` (`> 21:9`) | Maintains compact `48dp` track row footprint in horizontal landscape panels. |
| `Expanded` (`>= 840dp` Tablet) | Sits cleanly inside `AuraAdaptiveTwoPane` inspector panes bounded by `maxContentWidth` (`<= 520dp`) so drags never require overly long arm travel. |

---

## 8. Accessibility & Guardrails

- Exposes Compose `semantics { setProgress { target -> ... } }` so TalkBack volume-key increments adjust `value` cleanly across `valueRange` (respecting `steps`).
- Tracks and thumb ensure at least `48dp` interactive touch height.

---

## 9. Platform Bindings

### 9.1 Android (Jetpack Compose)

- **Source:** [`AuraSlider.kt`](../../../design-system/src/main/kotlin/io/github/bluemarlins/aura/component/AuraSlider.kt)

<!-- API:ANDROID:BEGIN -->
```kotlin
@Composable
fun AuraSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    label: String? = null,
    valueLabel: String? = null,
    supportingText: String? = null,
    interactionSource: MutableInteractionSource? = null,
)
```
<!-- API:ANDROID:END -->

### 9.2 iOS (SwiftUI Reference)

<!-- API:IOS:BEGIN -->
```swift
public struct AuraSlider: View {
    public init(
        value: Binding<Float>,
        enabled: Bool = true,
        valueRange: ClosedRange<Float> = 0...1,
        steps: Int = 0,
        onValueChangeFinished: (() -> Void)? = nil,
        label: String? = nil,
        valueLabel: String? = nil,
        supportingText: String? = nil
    )
}
```
<!-- API:IOS:END -->

### 9.3 Web (React / TypeScript Reference)

<!-- API:WEB:BEGIN -->
```ts
export interface AuraSliderProps {
  value: number;
  onValueChange: (value: number) => void;
  enabled?: boolean;
  valueRange?: [number, number];
  steps?: number;
  onValueChangeFinished?: () => void;
  label?: string;
  valueLabel?: string;
  supportingText?: string;
}
```
<!-- API:WEB:END -->
