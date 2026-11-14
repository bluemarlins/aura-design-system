---
component_id: "aura.component.text-field"
name: "AuraTextField"
category: "inputs"
status: "ready"
version: "0.1.0"
platforms:
  android: "com.example.ds.component.AuraTextField"
  ios: "AuraDS.AuraTextField (planned)"
  web: "@aura-ds/react.AuraTextField (planned)"
tokens:
  - "surface.*"
  - "content.*"
  - "border.*"
  - "brand.*"
  - "status.error.*"
  - "interaction.*"
---

# AuraTextField

> Cross-platform specification for Aura's single-line and multi-line text input component. See [00-input-principles.md](00-input-principles.md) for category-level selection principles and [00-spec-architecture.md](../00-spec-architecture.md) for the bidirectional `Specs ↔ Code` contract.

---

## 1. Intent & Usage Principles

### Problem

Users need a clear, accessible text input surface that scales from structured single-line form fields to calm multi-line diary and tasting-note writing across all target apps.

### When to Use

- Short single-line metadata: schedule title (**My Planner**), asset name (**My Finance**), bottle name/region (**My Drink Diary**).
- Multi-line reflective writing: paper-style daily diary (**My Planner**), wine/whiskey tasting notes (**My Drink Diary**).
- Constrained text inputs needing prefix/suffix units, character limits, or inline validation feedback.

### When NOT to Use & Alternatives

| Situation | Alternative | Reason |
|---|---|---|
| Instant search / live archive filtering | `AuraSearchBar` | Requires clear button, search affordance, and `Glass` overlay support |
| Formatted currency or stepper numeric entry | `AuraNumberField` | Needs thousands separators, decimal clamping, and numeric keypad patterns |
| Selecting from a fixed list of options | `AuraRadio` / `AuraChipButton` | Prevents typing errors and speeds up selection |
| Picking dates, times, or vintage years | `AuraDateTimePicker` | Provides structured calendar/wheel pickers |

### Context Rules

- **`INP-RULE-001`:** Use `Outlined` or `Filled` for structured forms; use `Paperless` for calm long-form writing (`singleLine = false`, `minLines >= 3`).
- **`INP-RULE-004`:** Always supply a non-empty `label` for form fields; do not rely solely on `placeholder`.
- **`INP-RULE-005`:** Pair error borders with explicit `errorText` so validation is clear without relying solely on color.

---

## 2. Anatomy & Canonical Part IDs

```text
+-----------------------------------------------------------------------+
|  root                                                                 |
|  [label]                                                              |
|  +-----------------------------------------------------------------+  |
|  |  fieldContainer (min 48px height, border / fill / underline)    |  |
|  |                                                                 |  |
|  |  [leadingIcon] [prefix] [inputText / placeholder] [suffix]      |  |
|  |                                                  [trailingIcon] |  |
|  +-----------------------------------------------------------------+  |
|  footerRow: [supportingText / errorText]          [characterCounter]  |
+-----------------------------------------------------------------------+
```

| Part ID | Responsibility | Required | Semantics Owner |
|---|---|---|---|
| `root` | Outer vertical container grouping label, field, and footer | Yes | Container |
| `label` | Persistent descriptive field title | Recommended | Label text |
| `fieldContainer` | Visual box (`Outlined`/`Filled`) or bottom-ruled line (`Paperless`), `>= 48px` hit height | Yes | `BasicTextField` |
| `leadingIcon` | Optional leading visual cue | No | Decorative |
| `prefix` | Optional static unit/currency prefix (`₩`, `$`) | No | Text |
| `inputText` | Editable text value | Yes | Input value |
| `placeholder` | Hint displayed only when `value` is empty | No | Hint |
| `suffix` | Optional static unit suffix (`원`, `% ABV`, `ml`) | No | Text |
| `trailingIcon` | Optional trailing action/status icon | No | Child / Action |
| `footerRow` | Bottom feedback row | Conditional | Container |
| `supportingText` | Helper guidance shown when no error is active | No | Helper text |
| `errorText` | Validation error message taking priority over `supportingText` | Conditional | `semantics.error` |
| `characterCounter` | Displays `"{length}/{maxLength}"` when `maxLength != null` | Conditional | Counter text |

---

## 3. Variant & Line Mode Matrix

### Visual Variants (`AuraTextFieldVariant`)

| Variant | Visual Treatment | Recommended Usage |
|---|---|---|
| `Outlined` (Default) | Rounded `10px` box (`surface.default`) with full border (`border.default` -> `border.focus`) | Structured forms in **My Finance** & **My Planner** |
| `Filled` | Rounded `10px` recessed fill (`surface.sunken`) with subtle border | Compact card metadata in **My Drink Diary** & settings |
| `Paperless` | Transparent background with bottom horizontal rule (`1px` -> `2px` active) | Calm multi-line diary pages (**Planner**) and tasting notes (**Drink Diary**) |

### Line Modes

| Mode | Configuration | Behavior |
|---|---|---|
| **Single-Line** | `singleLine = true` | `effectiveMinLines = 1`, `effectiveMaxLines = 1`, vertical center alignment, min height `48px` |
| **Multi-Line** | `singleLine = false`, `minLines >= 3` | Top-aligned icons/affixes, grows vertically up to `maxLines` |

### Validation & Input Constraints

- **`TF-001` (Single-Line Precedence):** When `singleLine = true`, `effectiveMinLines` and `effectiveMaxLines` clamp to `1` to prevent invalid line range exceptions.
- **`TF-002` (Character Limit Enforcement):** When `maxLength != null`, additions exceeding `maxLength` are truncated or blocked while deletions are always permitted even if initial text exceeded `maxLength`.
- **`TF-003` (Error Overrides Supporting Text):** When `errorText` is non-blank, `isError = true` and `errorText` renders in place of `supportingText`.
- **`TF-004` (Cursor Suppression on ReadOnly):** Read-only fields keep readable `content.primary` text without showing an active focus ring or blinking cursor.

---

## 4. State Precedence & Behavior

Concurrent states resolve in strict deterministic order:

$$\text{Disabled} \succ \text{ReadOnly} \succ \text{Error} \succ \text{Focused} \succ \text{Rest}$$

1. **`Disabled` (`enabled == false`):** Ignores input/focus, applies `interaction.disabledContainer` (`Outlined`/`Filled`) and `content.disabled` text.
2. **`ReadOnly` (`readOnly == true`):** Preserves `content.primary` legibility, suppresses active focus border thickening and cursor.
3. **`Error` (`!errorText.isNullOrBlank()`):** Applies `status.error.solid` to border/underline, label, error text, and accessibility error semantics.
4. **`Focused`:** Thickens border/underline (`2px` for `Outlined`/`Paperless`, `1.5px` for `Filled`) with `border.focus` and tints label with `brand.primary`.
5. **`Rest`:** Default neutral border and `content.secondary` label.

---

## 5. Semantic Token Bindings (`AuraColors`)

| Part | Variant | State | `AuraColors` Token Path |
|---|---|---|---|
| `fieldContainer` (bg) | `Outlined` | `Enabled` / `ReadOnly` | `surface.default` |
| `fieldContainer` (bg) | `Filled` | `Enabled` / `ReadOnly` | `surface.sunken` |
| `fieldContainer` (bg) | `Outlined` / `Filled` | `Disabled` | `interaction.disabledContainer` |
| `fieldContainer` (bg) | `Paperless` | `*` | `Transparent` |
| `fieldContainer` (border/line) | `*` | `Disabled` / `ReadOnly` | `border.subtle` |
| `fieldContainer` (border/line) | `*` | `Error` | `status.error.solid` |
| `fieldContainer` (border/line) | `*` | `Focused` | `border.focus` |
| `fieldContainer` (border/line) | `Outlined` / `Paperless` | `Rest` | `border.default` |
| `fieldContainer` (border/line) | `Filled` | `Rest` | `border.subtle` |
| `label` | `*` | `Disabled` | `content.disabled` |
| `label` | `*` | `Error` | `status.error.solid` |
| `label` | `*` | `Focused` | `brand.primary` |
| `label` | `*` | `Rest` / `ReadOnly` | `content.secondary` |
| `inputText` | `*` | `Enabled` / `ReadOnly` | `content.primary` |
| `inputText` | `*` | `Disabled` | `content.disabled` |
| `placeholder` | `*` | `Enabled` / `ReadOnly` | `content.tertiary` |
| `prefix` / `suffix` | `*` | `Enabled` / `ReadOnly` | `content.secondary` |
| `errorText` | `*` | `Error` | `status.error.solid` |
| `supportingText` | `*` | `Non-Error` | `content.secondary` |
| `characterCounter` | `*` | `Rest` / `Error` | `content.tertiary` / `status.error.solid` |

---

## 6. Accessibility & Guardrails

- **Touch Target:** `fieldContainer` enforces minimum `48dp` height (`44pt` on iOS).
- **Error Semantics:** Sets Compose `Modifier.semantics { if (isError) error(errorText!!) }`, iOS accessibility hint/label, and Web `aria-invalid="true"` + `aria-describedby`.
- **Contrast:** All enabled text/container pairs comply with WCAG AA (≥ 4.5:1) via `AuraColorSchemes`.

---

## 7. Platform Bindings

### 7.1. Android (Jetpack Compose)

- **Source:** [`AuraTextField.kt`](../../../design-system/src/main/kotlin/com/example/ds/component/AuraTextField.kt)
- **Dependency Policy:** Built strictly on Compose Foundation `BasicTextField` without `material3`.

<!-- API:ANDROID:BEGIN -->
```kotlin
package com.example.ds.component

enum class AuraTextFieldVariant {
    Outlined,
    Filled,
    Paperless,
}

@Composable
fun AuraTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    variant: AuraTextFieldVariant = AuraTextFieldVariant.Outlined,
    label: String? = null,
    placeholder: String? = null,
    supportingText: String? = null,
    errorText: String? = null,
    prefixText: String? = null,
    suffixText: String? = null,
    maxLength: Int? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
)
```
<!-- API:ANDROID:END -->

### 7.2. iOS (SwiftUI Reference Contract — Planned)

<!-- API:IOS:BEGIN -->
```swift
public enum AuraTextFieldVariant {
    case outlined, filled, paperless
}

public struct AuraTextField<LeadingIcon: View, TrailingIcon: View>: View {
    public init(
        text: Binding<String>,
        variant: AuraTextFieldVariant = .outlined,
        label: String? = nil,
        placeholder: String? = nil,
        supportingText: String? = nil,
        errorText: String? = nil,
        prefixText: String? = nil,
        suffixText: String? = nil,
        maxLength: Int? = nil,
        isEnabled: Bool = true,
        isReadOnly: Bool = false,
        lineLimit: ClosedRange<Int>? = 1...1,
        @ViewBuilder leadingIcon: () -> LeadingIcon = { EmptyView() },
        @ViewBuilder trailingIcon: () -> TrailingIcon = { EmptyView() }
    )
}
```
<!-- API:IOS:END -->

### 7.3. Web (React / TypeScript Reference Contract — Planned)

<!-- API:WEB:BEGIN -->
```ts
export type AuraTextFieldVariant = "outlined" | "filled" | "paperless";

export interface AuraTextFieldProps {
  value: string;
  onValueChange: (value: string) => void;
  variant?: AuraTextFieldVariant;
  label?: string;
  placeholder?: string;
  supportingText?: string;
  errorText?: string;
  prefixText?: string;
  suffixText?: string;
  maxLength?: number;
  disabled?: boolean;
  readOnly?: boolean;
  multiline?: boolean;
  minRows?: number;
  maxRows?: number;
  leadingIcon?: React.ReactNode;
  trailingIcon?: React.ReactNode;
  className?: string;
}
```
<!-- API:WEB:END -->
