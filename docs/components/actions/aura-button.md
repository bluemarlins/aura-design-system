---
component_id: "aura.component.button"
name: "AuraButton"
category: "actions"
status: "ready"
version: "0.2.0"
platforms:
  android: "com.example.ds.component.AuraButton"
  ios: "AuraDS.AuraButton (planned)"
  web: "@aura-ds/react.AuraButton (planned)"
tokens:
  - "brand.*"
  - "content.*"
  - "border.*"
  - "status.error.*"
  - "interaction.*"
---

# AuraButton

> Cross-platform specification for Aura's labeled action button. See [00-button-principles.md](00-button-principles.md) for category-level selection rules and [00-spec-architecture.md](../00-spec-architecture.md) for the bidirectional `Specs ↔ Code` contract.

---

## 1. Intent & Usage Principles

### Problem

Users need an unmistakable, accessible control with an explicit verb label to trigger primary, secondary, cancel, or destructive actions.

### When to Use

- Submitting forms or saving records (e.g., **My Planner** save schedule, **My Finance** record transaction, **My Drink Diary** save tasting note).
- Confirming or dismissing modal dialogs and bottom sheets.
- Primary or secondary navigation steps inside guided workflows.

### When NOT to Use & Alternatives

| Situation | Alternative | Reason |
|---|---|---|
| Icon-only toolbar or media overlay action | `AuraIconButton` | Compact icon hit region with mandatory `contentDescription` |
| Persistent screen-wide `+ Create` shortcut | `AuraFab` | Floating entry point independent of scroll position |
| Switching view modes (`Day \| Week \| Month`) | `AuraSegmentedButton` | Immediate mutually exclusive toggle without submit |
| Multi-select tags or inline filters | `AuraChipButton` | Lightweight attribute/filter capsule |

### Context & Hierarchy Rules

- **`BTN-RULE-001`:** Maximum 1 `Primary` button per screen or modal dialog.
- **`BTN-RULE-002`:** Do not place `Primary` inside repeated cards, list rows, or home launcher widgets; use `Secondary`, `Outline`, or `Ghost`.
- **`BTN-RULE-003`:** Reserve `Destructive` for final confirmation steps; use `Outline` or `Ghost` on first-level surfaces.

---

## 2. Anatomy & Canonical Part IDs

```text
+----------------------------------------------------------------------+
|  touchTarget (min 48x48 logical px, invisible hit-box if Small)      |
|  +----------------------------------------------------------------+  |
|  |  root (visual container: background, border, shape)            |  |
|  |                                                                |  |
|  |   [leadingIcon]  --gap--  [label]  --gap--  [trailingIcon]     |  |
|  |                                                                |  |
|  |   [progressIndicator] (centered overlay when loading == true)  |  |
|  +----------------------------------------------------------------+  |
+----------------------------------------------------------------------+
```

| Part ID | Responsibility | Required | Semantics Owner |
|---|---|---|---|
| `touchTarget` | Guarantees minimum interactive hit box (`48×48px` mobile) | Yes | `root` (`Role.Button`) |
| `root` | Visual container fill, border stroke, corner radius, horizontal/vertical padding | Yes | Inherited |
| `leadingIcon` | Optional visual cue before text label | No | Decorative (merged) |
| `label` | Single-line action verb with ellipsis overflow | Yes | Accessible name |
| `trailingIcon` | Optional directional/disclosure cue after text label | No | Decorative (merged) |
| `progressIndicator` | Centered spinner visible when `loading == true` | Conditional | `stateDescription = "In progress"` |

---

## 3. Variant, Size & Material Matrix

### Variants (`AuraButtonVariant`)

| Variant | Visual Weight | Intended Usage |
|---|---|---|
| `Primary` | Highest | Single main CTA per screen or modal (`Save`, `Confirm`, `Apply`) |
| `Secondary` | Medium-High (Tonal) | Complementary high-value action (`Scan Label`, `Analyze Portfolio`) |
| `Outline` | Medium-Low (Bordered) | Neutral alternative or cancel action (`Cancel`, `Reset Filter`) |
| `Ghost` | Lowest (Borderless) | Low-emphasis inline or header action (`Jump to Today`, `See All`) |
| `Destructive` | High Alert | Irreversible delete/reset confirmation (`Delete Note`, `Remove Asset`) |

### Sizes (`AuraButtonSize`) — Logical Units

| Size | Visual Height | Min Touch Target | Horizontal Padding | Vertical Padding | Corner Radius | Label Size / Line Height | Icon / Spinner Size | Icon Gap |
|---|---|---|---|---|---|---|---|---|
| `Small` | `36px` | `48px` (Android) / `44pt` (iOS) | `12px` | `6px` | `8px` | `13px / 18px` SemiBold | `16px` | `6px` |
| `Medium` (Default) | `48px` | `48px` / `44pt` | `16px` | `10px` | `10px` | `14px / 20px` SemiBold | `18px` | `8px` |
| `Large` | `56px` | `56px` | `20px` | `12px` | `12px` | `16px / 22px` SemiBold | `20px` | `8px` |

### Valid & Invalid Combinations

- **`BTN-001`:** Callers must never override height below `minTouchTarget` or inject raw hex colors; use `variant` and `size`.
- **`BTN-002`:** `text` must be non-blank. Icon-only actions must use `AuraIconButton`.
- **`BTN-003`:** Full-width bottom CTAs should use `size = Large` and `fullWidth = true`.

---

## 4. State Precedence & Behavior

Concurrent states resolve in strict deterministic order:

$$\text{Loading} \succ \text{Disabled} \succ \text{Pressed} \succ \text{Focused} \succ \text{Hovered} \succ \text{Rest}$$

1. **`Loading` (`loading == true`):**
   - Retains the active variant's rest container color and exact measured button width/height (prevents layout shift).
   - Sets content (`leadingIcon`, `label`, `trailingIcon`) opacity to `0.0f` and renders centered `progressIndicator`.
   - Blocks all pointer/click events and sets accessibility state to `"In progress"`.
2. **`Disabled` (`enabled == false` and `!loading`):**
   - Ignores all clicks and press visual feedback.
   - Uses `interaction.disabledContainer` (or `Transparent` for `Outline`/`Ghost`) and `content.disabled`.
3. **`Pressed`:**
   - Applies `brand.primaryPressed` for `Primary` or composites `interaction.pressedOverlay` over the rest container for other variants.
4. **Duplicate-Click Prevention (`debounceIntervalMs`):**
   - Defaults to `400ms` monotonic uptime debounce so rapid double taps cannot trigger duplicate saves or transactions. Passing `0L` opts out when immediate repeat taps are intentional.

---

## 5. Semantic Token Bindings (`AuraColors`)

| Part | Variant | State | `AuraColors` Token Path |
|---|---|---|---|
| `root` (bg) | `Primary` | `Rest` / `Loading` | `brand.primary` |
| `root` (bg) | `Primary` | `Pressed` | `brand.primaryPressed` |
| `label` / `progressIndicator` | `Primary` | `Rest` / `Pressed` / `Loading` | `brand.onPrimary` |
| `root` (bg) | `Secondary` | `Rest` / `Loading` | `brand.subtle` |
| `root` (bg) | `Secondary` | `Pressed` | `interaction.pressedOverlay` over `brand.subtle` |
| `label` / `progressIndicator` | `Secondary` | `Rest` / `Pressed` / `Loading` | `brand.onSubtle` |
| `root` (bg) | `Outline` | `Rest` / `Loading` | `Transparent` |
| `root` (bg) | `Outline` | `Pressed` | `interaction.pressedOverlay` |
| `root` (border) | `Outline` | `Enabled` / `Loading` | `1px` `border.default` |
| `root` (border) | `Outline` | `Disabled` | `1px` `border.subtle` |
| `label` / `progressIndicator` | `Outline` | `Rest` / `Pressed` / `Loading` | `content.primary` |
| `root` (bg) | `Ghost` | `Rest` / `Loading` | `Transparent` |
| `root` (bg) | `Ghost` | `Pressed` | `interaction.pressedOverlay` |
| `label` / `progressIndicator` | `Ghost` | `Rest` / `Pressed` / `Loading` | `brand.primary` |
| `root` (bg) | `Destructive` | `Rest` / `Loading` | `status.error.solid` |
| `root` (bg) | `Destructive` | `Pressed` | `interaction.pressedOverlay` over `status.error.solid` |
| `label` / `progressIndicator` | `Destructive` | `Rest` / `Pressed` / `Loading` | `status.error.onSolid` |
| `root` (bg) | `Primary` / `Secondary` / `Destructive` | `Disabled` | `interaction.disabledContainer` |
| `root` (bg) | `Outline` / `Ghost` | `Disabled` | `Transparent` |
| `label` | `*` | `Disabled` | `content.disabled` |

---

## 6. Accessibility & Guardrails

- **Role:** `Role.Button` (Android), `.accessibilityAddTraits(.isButton)` (iOS), `<button type="button">` (Web).
- **Accessible Name:** Derived from `text` (`maxLines = 1`, `TextOverflow.Ellipsis`). Leading/trailing icons are decorative and merged with the button semantics.
- **Loading State:** Announces `stateDescription = "In progress"` (Android) / `aria-busy="true"` (Web) / loading accessibility value (iOS).
- **Contrast:** Every variant's enabled content/container pair satisfies WCAG AA (≥ 4.5:1) via `AuraColorSchemes`.
- **Touch Target (`BTN-RULE-005`):** Outer hit box enforces `>= 48dp` minimum width and height even for `Small (36dp)`.

---

## 7. Platform Bindings

### 7.1. Android (Jetpack Compose)

- **Source:** [`AuraButton.kt`](../../../design-system/src/main/kotlin/com/example/ds/component/AuraButton.kt)
- **Dependency Policy:** Uses Compose Foundation/UI/Animation primitives only (`material3` independent).

<!-- API:ANDROID:BEGIN -->
```kotlin
package com.example.ds.component

enum class AuraButtonVariant {
    Primary,
    Secondary,
    Outline,
    Ghost,
    Destructive,
}

enum class AuraButtonSize {
    Small,
    Medium,
    Large,
}

@Composable
fun AuraButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: AuraButtonVariant = AuraButtonVariant.Primary,
    size: AuraButtonSize = AuraButtonSize.Medium,
    enabled: Boolean = true,
    loading: Boolean = false,
    fullWidth: Boolean = false,
    debounceIntervalMs: Long = 400L,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
)
```
<!-- API:ANDROID:END -->

### 7.2. iOS (SwiftUI Reference Contract — Planned)

<!-- API:IOS:BEGIN -->
```swift
public enum AuraButtonVariant {
    case primary, secondary, outline, ghost, destructive
}

public enum AuraButtonSize {
    case small, medium, large
}

public struct AuraButton<LeadingIcon: View, TrailingIcon: View>: View {
    public init(
        _ text: String,
        variant: AuraButtonVariant = .primary,
        size: AuraButtonSize = .medium,
        isEnabled: Bool = true,
        isLoading: Bool = false,
        fullWidth: Bool = false,
        debounceInterval: TimeInterval = 0.4,
        @ViewBuilder leadingIcon: () -> LeadingIcon = { EmptyView() },
        @ViewBuilder trailingIcon: () -> TrailingIcon = { EmptyView() },
        action: @escaping () -> Void
    )
}
```
<!-- API:IOS:END -->

### 7.3. Web (React / TypeScript Reference Contract — Planned)

<!-- API:WEB:BEGIN -->
```ts
export type AuraButtonVariant =
  | "primary"
  | "secondary"
  | "outline"
  | "ghost"
  | "destructive";

export type AuraButtonSize = "small" | "medium" | "large";

export interface AuraButtonProps {
  text: string;
  onClick: () => void;
  variant?: AuraButtonVariant;
  size?: AuraButtonSize;
  disabled?: boolean;
  loading?: boolean;
  fullWidth?: boolean;
  debounceIntervalMs?: number;
  leadingIcon?: React.ReactNode;
  trailingIcon?: React.ReactNode;
  className?: string;
}
```
<!-- API:WEB:END -->
