---
id: "aura.component.number-field"
name: "AuraNumberField"
category: "inputs"
status: "ready"
version: "1.0.0"
layer: "content"
material_allowed: ["Solid"]
motion_track: "Standard"
platforms:
  android: "io.github.bluemarlins.aura.component.AuraNumberField"
  ios: "AuraDS.AuraNumberField (planned)"
  web: "@aura-ds/react.AuraNumberField (planned)"
tokens:
  - "colors.surface.default"
  - "colors.surface.sunken"
  - "colors.brand.primary"
  - "colors.content.primary"
  - "colors.content.secondary"
  - "colors.content.tertiary"
  - "colors.content.disabled"
  - "colors.border.subtle"
  - "colors.border.default"
  - "colors.border.focus"
  - "colors.status.error.solid"
  - "colors.interaction.disabledContainer"
  - "typography.bodyLarge"
  - "typography.labelMedium"
  - "typography.monoMedium"
  - "motion.pressInSpring"
  - "motion.pressOutSpring"
  - "motion.stateChangeDurationMillis"
  - "motion.minPressDurationMillis"
  - "motion.pressedScaleDefault"
---

# AuraNumberField

> Structured numeric, currency, and measurement input featuring tabular numerals (`tnum`), optional thousands formatting, explicit unit affixes (`prefixText` / `suffixText`), and optional `-` / `+` stepper buttons.

---

## 1. Intent & Usage Principles

### Problem

Numeric, currency, and measurement values need precise keyboard entry with units, bounds, and optional stepping that free-form text fields do not enforce.

### When to Use

- Entering monetary amounts, asset quantities, prices, and interest rates (**My Finance**).
- Entering bottle vintage year, ABV (`%`), bottle volume (`ml`), and bottle price (**My Drink Diary**).
- Adjusting countdown durations or repeat counts with quick `-` / `+` steppers (**My Planner**).
- Whenever numeric values benefit from monospace tabular digits, bounds clamping (`minValue`..`maxValue`), or formatted thousands separators (`1,250,000`).

### When NOT to Use & Alternatives

- **Free-form names, titles, descriptions, or prose:** Use `AuraTextField`.
- **Bounded continuous or quick relative scales (`0..100%`, `Body 1..5`):** Use `AuraSlider` when visual proportionality matters more than exact digit entry.
- **Star enjoyment scores:** Use `AuraRatingBar`.
- **Real-time list filtering:** Use `AuraSearchBar`.

### Context Rules

- **`NUM-RULE-001` (Explicit Unit Clarity — `INP-RULE-003`):** Always declare units via `prefixText` (`₩`, `$`, `€`) or `suffixText` (`%`, `% ABV`, `ml`, `원`, `shares`) rather than burying units only inside `placeholder`.
- **`NUM-RULE-002` (Steppers for Bounded Quantities):** Enable `showSteppers = true` when small incremental adjustments (vintage year, bottle quantity, guest count, minutes) are frequent; omit steppers for high-variance financial amounts (`15,000,000`) where keyboard entry dominates.
- **`NUM-RULE-003` (Tabular Alignment):** Render numeric values with monospace/tabular figure alignment (`typography.monoMedium`) so digits do not jitter horizontally when stepping or typing.

### Do / Don't

| Do | Don't |
|---|---|
| Pair `prefixText = "₩"` and `formatThousands = true` for **My Finance** asset amounts. | Use generic `AuraTextField` without numeric filtering or currency affixes for money. |
| Provide `minValue` and `maxValue` when `showSteppers = true` (e.g., `1950.0..2030.0` for wine vintages). | Allow steppers to increment past nonsensical bounds (`-1` bottles or `120%` ABV). |
| Show explicit validation errors via `errorMessage` below the field so layout stays anchored. | Replace the field label with error text or rely solely on color to show invalid numbers. |
| Guarantee `>= 48dp` minimum height on both the text field box and `-` / `+` stepper targets. | Shrink inline `-` / `+` steppers to tiny `24dp` icons that users repeatedly mistap. |

---

## 2. Anatomy & Canonical Part IDs

```text
  Label (optional)
  ┌─────────────────────────────────────────────────────┐
  │ [ - ]  ₩   1,250,000                   원     [ + ] │
  └─────────────────────────────────────────────────────┘
  Supporting / Error text
```

| Part ID | Responsibility | Required | Semantics Owner |
|---|---|---|---|
| `label` | Optional header above the field | No | Field accessible name |
| `root` | `48dp` minimum-height field container (`10dp` radius) | Yes | Text field (`error` when `errorMessage != null`) |
| `decrementButton` / `incrementButton` | `−` / `+` steppers shown when `showSteppers = true` | Conditional | `Role.Button` |
| `prefix` / `suffix` | Unit or currency text (`₩`, `원`, `%`) | No | Merged into field |
| `input` | Numeric text with tabular numerals | Yes | Text field |
| `supportingText` / `errorText` | Guidance or validation message below the field | No | Merged / `error` |

---

## 3. Variant, Size & Material Matrix

### Quick Spec

Measured against the rendered component by `SpecMeasurementTest`. Keys are `<Part ID>.<metric>`; dimensions in `dp`, type in `sp`.

<!-- QUICK-SPEC:BEGIN -->
```yaml
sizes:
  Default:
    root.minHeight: 48
    root.cornerRadius: 10
```
<!-- QUICK-SPEC:END -->

Reuses `AuraTextFieldVariant`:
- `Outlined` (default): Structured forms, financial records, cellar entries.
- `Filled`: Recessed compact metadata rows (`surface.sunken`).
- `Paperless`: Quiet bottom-ruled inline numeric edit inside editorial sheets.

- **Size:** Single size — `48dp` minimum height.
- **Material:** `Solid` only (`layer: content`).

---

## 4. State Precedence & Behavior

`Disabled` > `ReadOnly` > `Error` (`errorMessage != null`) > `Focused` > `Rest`.

Steppers are individually disabled when clicking would cross `minValue` or `maxValue`, or when `!enabled || readOnly`.

---

## 5. Semantic Token Bindings

| Element | State | Light / Dark Semantic Token |
|---|---|---|
| Container Fill | `Outlined` / `Filled` / `Paperless` | `surface.default` / `surface.sunken` / `Transparent` (`interaction.disabledContainer` when disabled) |
| Border / Bottom Rule | `Disabled` / `ReadOnly` / `Error` / `Focused` / `Rest` | `border.subtle` / `border.subtle` / `status.error.solid` / `border.focus` / `border.default` |
| Numeric Input Text | `Enabled` / `Disabled` | `content.primary` / `content.disabled` (`typography.monoMedium`) |
| Prefix / Suffix Affixes | `Enabled` / `Disabled` | `content.secondary` / `content.disabled` (`typography.labelMedium`) |
| Stepper Buttons (`-`, `+`) | `Enabled` / `Bound Reached` | `surface.sunken` fill + `border.subtle` stroke + `content.primary` / `content.disabled` |
| Footer Supporting / Error | `Rest` / `Error` | `content.secondary` / `status.error.solid` |

---

## 6. Interaction Motion Contract

Consumes `AuraTheme.motion`:

1. **Focus & Error Border Transition:** Border thickness and stroke color animate via `tween(durationMillis = AuraTheme.motion.stateChangeDurationMillis)` (`150ms`).
2. **Stepper Tactile Press:** Both `-` and `+` stepper buttons use `rememberAuraTactilePressed(interactionSource, motion.minPressDurationMillis)` and compress to `AuraTheme.motion.pressedScaleDefault` (`0.96f`) via `pressInSpring()` / `pressOutSpring()`.
3. **Reduced Motion:** Uses `snap()` when system animations are disabled (`rememberAuraReducedMotion()`).

---

## 7. Responsive Layout Behavior (`Smallest Width × Display Ratio`)

| Width / Display Profile | Field & Stepper Adaptation |
|---|---|
| `ExtraSmall` (`< 360dp`) | Full-width field (`fillMaxWidth()`); compact affix gaps (`6dp`) keep `48dp` steppers plus digits legible on narrow cover screens. |
| `Compact` (`360–599dp`) | Standard single-row numeric field with balanced `12dp` horizontal padding and comfortable right-aligned or start-aligned affixes. |
| `Square` (`~1:1` Pop-up / Foldable) | Expands smoothly inside 2-column inspector forms (`AuraAdaptiveTwoPane`) without truncating `-` / `+` stepper hit boxes. |
| `UltraWide` (`> 21:9`) | Preserves single-line entry and keyboard action (`ImeAction.Done`) with compact vertical footprint (`48dp`). |
| `Expanded` (`>= 840dp` Tablet) | Pairs cleanly side-by-side inside multi-field **My Finance** or **My Drink Diary** detail editors bounded by `AuraTheme.layout.maxContentWidth`. |

---

## 8. Accessibility & Guardrails

- Stepper `-` and `+` buttons expose `Role.Button` with localized `contentDescription` (`"Decrease"` / `"Increase"`) and `>= 48dp × 48dp` touch targets.
- Error states attach `semantics { error(errorMessage) }` for TalkBack announcement.
- Uses numeric/decimal virtual keyboard (`KeyboardType.Decimal` when `allowDecimals = true`, `KeyboardType.Number` otherwise).

---

## 9. Platform Bindings

### 9.1 Android (Jetpack Compose)

- **Source:** [`AuraNumberField.kt`](../../../design-system/src/main/kotlin/io/github/bluemarlins/aura/component/AuraNumberField.kt)

<!-- API:ANDROID:BEGIN -->
```kotlin
@Composable
fun AuraNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    variant: AuraTextFieldVariant = AuraTextFieldVariant.Outlined,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    label: String? = null,
    placeholder: String? = null,
    prefixText: String? = null,
    suffixText: String? = null,
    supportingText: String? = null,
    errorMessage: String? = null,
    showSteppers: Boolean = false,
    step: Double = 1.0,
    minValue: Double? = null,
    maxValue: Double? = null,
    allowDecimals: Boolean = true,
    formatThousands: Boolean = false,
    interactionSource: MutableInteractionSource? = null,
)
```
<!-- API:ANDROID:END -->

### 9.2 iOS (SwiftUI Reference)

<!-- API:IOS:BEGIN -->
```swift
public struct AuraNumberField: View {
    public init(
        value: Binding<String>,
        variant: AuraTextFieldVariant = .outlined,
        enabled: Bool = true,
        readOnly: Bool = false,
        label: String? = nil,
        placeholder: String? = nil,
        prefixText: String? = nil,
        suffixText: String? = nil,
        supportingText: String? = nil,
        errorMessage: String? = nil,
        showSteppers: Bool = false,
        step: Double = 1.0,
        minValue: Double? = nil,
        maxValue: Double? = nil,
        allowDecimals: Bool = true,
        formatThousands: Bool = false
    )
}
```
<!-- API:IOS:END -->

### 9.3 Web (React / TypeScript Reference)

<!-- API:WEB:BEGIN -->
```ts
export interface AuraNumberFieldProps {
  value: string;
  onValueChange: (value: string) => void;
  variant?: AuraTextFieldVariant;
  enabled?: boolean;
  readOnly?: boolean;
  label?: string;
  placeholder?: string;
  prefixText?: string;
  suffixText?: string;
  supportingText?: string;
  errorMessage?: string;
  showSteppers?: boolean;
  step?: number;
  minValue?: number;
  maxValue?: number;
  allowDecimals?: boolean;
  formatThousands?: boolean;
}
```
<!-- API:WEB:END -->
