---
id: "aura.component.icon-button"
name: "AuraIconButton"
category: "actions"
status: "ready"
version: "1.0.0"
layer: "chrome"
material_allowed: ["Solid", "Glass"]
motion_track: "Standard"
platforms:
  android: "com.example.ds.component.AuraIconButton"
  ios: "AuraDS.AuraIconButton (planned)"
  web: "@aura-ds/react.AuraIconButton (planned)"
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
  - "colors.status.error.solid"
  - "colors.status.error.onSolid"
  - "colors.status.error.subtle"
  - "colors.status.error.onSubtle"
  - "colors.interaction.selectedContainer"
  - "colors.interaction.onSelected"
  - "colors.interaction.disabledContainer"
  - "colors.interaction.pressedOverlay"
  - "motion.pressInSpring"
  - "motion.pressOutSpring"
  - "motion.iconSpring"
  - "motion.stateChangeDurationMillis"
  - "motion.loadingCrossfadeDurationMillis"
  - "motion.spinnerRotationDurationMillis"
  - "motion.spinnerSweepDurationMillis"
  - "motion.minPressDurationMillis"
  - "motion.pressedScaleDefault"
  - "motion.pressedScaleGhost"
  - "motion.leadingIconPressedScale"
---

# AuraIconButton

> Compact, icon-only action control for app bars, toolbars, list rows, inline field adornments, and floating overlays across Aura applications.

---

## 1. Intent & Usage Principles

`AuraIconButton` separates visual compactness (`32dp`–`48dp` painted frame) from ergonomics by always enforcing a minimum `48dp × 48dp` accessible touch target. Its emphasis follows the 5-level hierarchy established in [00-button-principles.md](00-button-principles.md) so crowded toolbars remain scannable without competing with primary `AuraButton` CTAs.

### Problem

App bars, toolbars, list rows, and field adornments need icon-only actions that stay compact visually while keeping an accessible `48dp` touch target and a spoken label.

### When to Use

- App bar navigation (`Back`, `Close`, `Menu`) and trailing action clusters (`Search`, `Theme`, `Filter`, `Settings`).
- Compact list row, card header, or inline `AuraTextField` trailing actions (`Visibility`, `Clear`, `Copy`).
- Overlay actions over hero imagery or canvas editors (e.g., zoom, reset camera, bookmark tasting photo) using `Floating`.

### When NOT to Use & Alternatives

- Primary multi-word actions where an icon alone can be misinterpreted -> use `AuraButton`.
- Attribute filters or multi-select category pills with text labels -> use `AuraChipButton`.
- Non-interactive status indicators or decorative badges -> use `AuraIcon`.

### Context Rules

- **`IBTN-RULE-001` (Self-Describing Accessibility Label)**: Every standalone `AuraIconButton` MUST convey a clear verb or toggle target via `contentDescription` (e.g., `"Filter transactions"`, `"Toggle theme"`, `"Navigate back"`). Leave `contentDescription = null` on the button only when an adjacent visual label unambiguously describes the control and is merged into the same focus node.
- **`IBTN-RULE-002` (Visual Hierarchy & Density)**:
  - **`Filled`**: Reserved for a single high-emphasis compact action inside a card or floating bar (max 1 per cluster).
  - **`Tonal`**: Medium-emphasis grouped toolbar or toggle actions (calendar mode, bookmark, sort).
  - **`Outlined`**: Crisp bounded utility button on dense or high-contrast utility panels.
  - **`Ghost`**: Default for `AuraTopAppBar` navigation/action clusters, list row affordances, and text-field trailing controls.
  - **`Floating`**: Translucent/elevated island action anchored above imagery, maps, wallpapers, tasting photos, or 3D launcher canvases.
  - **`Destructive`**: Irreversible removal or discard icon trigger inside confirmable toolbars or item rows.
- **`IBTN-RULE-003` (Touch Target Isolation)**: Visual sizes (`Small` `32dp`, `Medium` `40dp`, `Large` `48dp`) MUST preserve at least `48dp × 48dp` hit bounds with a minimum `4dp`–`8dp` visual gap (`48dp` center-to-center spacing) between adjacent icon buttons.
- **`IBTN-RULE-004` (Explicit Selection State)**: Use `selected: Boolean?` (`true` / `false`) whenever the control represents a persistent toggle (favorite, pin, lock, grid/list mode) rather than mutating icon tint imperatively without accessibility state.

### Do / Don't

| Do | Don't |
|---|---|
| Provide an imperative action `contentDescription` on every icon-only action button. | Leave standalone icon buttons without semantic labels for screen readers. |
| Default to `Ghost` in app bars and `Floating` over photos or 3D launcher canvases. | Place multiple `Filled` icon buttons side by side in a single toolbar cluster. |
| Let `AuraIconButton` expand its transparent hit slop to `48dp × 48dp` around `Small`/`Medium` frames. | Wrap `Small` icon buttons inside tight `32dp` clipping boxes that cut off touch targets or press springs. |
| Pass `selected = true / false` for persistent toggles like bookmark or pin. | Represent binary toggle state purely by swapping raw colors without semantic state. |

---

## 2. Anatomy & Canonical Part IDs

```text
  ┌──────────────┐   <- 48dp min hit target (invisible when visual < 48dp)
  │  ╭────────╮  │
  │  │  [ic]  │  │   <- 32 / 40 / 48dp visual container (Circle or RoundedSquare)
  │  ╰────────╯  │
  └──────────────┘
```

| Part ID | Responsibility | Required | Semantics Owner |
|---|---|---|---|
| `touchTarget` | `48dp × 48dp` minimum hit box | Yes | `root` |
| `root` | Visual container fill, border, and shape | Yes | `Role.Button` (+ `selected` semantics when `selected != null`) |
| `icon` | Glyph slot | Yes | `contentDescription` (required) |
| `progressIndicator` | Spinner replacing `icon` when `loading = true` | Conditional | `stateDescription = "In progress"` |

---

## 3. Variant, Size & Material Matrix

### Variants (`AuraIconButtonVariant`)

1. **`Filled`** — Solid brand fill (`brand.primary`) for high-emphasis single icon actions.
2. **`Tonal`** — Subtle brand tint (`brand.subtle`) for calm medium-emphasis toolbar controls.
3. **`Outlined`** — Transparent fill with `1dp` hairline border (`border.default`) for bounded utility controls.
4. **`Ghost`** — Containerless hover/press surface for clean `AuraTopAppBar` and inline row clusters.
5. **`Floating`** — Elevated frosted island (`surface.floating @ 0.92 alpha`, `1dp` `border.subtle`, soft ambient shadow) for overlays and detached Floating AppBar controls.
6. **`Destructive`** — Error-tinted container (`status.error.subtle` / `status.error.onSubtle`) for delete/discard icon triggers.

### Shapes (`AuraIconButtonShape`)

- **`Circle`** (`CircleShape`) — Default pill/circular silhouette matching capsule `AuraTopAppBar` navigation and media overlays.
- **`RoundedSquare`** (`10dp` / `12dp` / `14dp` corner on `Small` / `Medium` / `Large`) — Soft squircle matching launcher utility tiles, calculator keys, and dense inspector pads.

### Sizes (`AuraIconButtonSize`)

| Size | Visual Container | Icon Size | Min Hit Target | Corner Radius (`RoundedSquare`) |
|---|---|---|---|---|
| `Small` | `32dp` | `18dp` | `48dp × 48dp` | `10dp` |
| `Medium` | `40dp` | `20dp` | `48dp × 48dp` | `12dp` |
| `Large` | `48dp` | `24dp` | `48dp × 48dp` | `14dp` |

---

## 4. State Precedence & Behavior

Strict visual precedence ensures conflicting states never render ambiguously:

1. **Disabled (`!enabled`)**: Non-interactive (`clickable` disabled), `interaction.disabledContainer`, `content.disabled` icon tint, `0dp` shadow, no press scale.
2. **Loading (`loading == true`)**: Clicks suppressed, retains active variant surface/tint colors, crossfades icon to `AuraLoadingSpinner`, announces `stateDescription = "In progress"`.
3. **Pressed**: Tactile scale spring + subtle `interaction.pressedOverlay` wash while finger/pointer is held or latched for `minPressDurationMillis`.
4. **Selected (`selected == true`)**: Active selected palette and accent border as defined in the token table.
5. **Focused / Hovered**: Keyboard focus draws `2dp` `border.focus` focus ring; pointer hover adds subtle surface highlight.
6. **Default**: Resting variant tokens.

Rapid taps are protected by `debounceIntervalMs` (default `300L` ms).

---

## 5. Semantic Token Bindings

All colors resolve through `AuraTheme.colors` with zero direct Material 3 theme reads or hardcoded hex literals inside `:design-system`.

| Variant | Unselected / Default Container | Selected (`selected == true`) Container | Content (Icon / Spinner) | Border | Disabled (`enabled == false`) |
|---|---|---|---|---|---|
| `Filled` | `brand.primary` | `brand.primary` | `brand.onPrimary` | None | `interaction.disabledContainer` / `content.disabled` |
| `Tonal` | `brand.subtle` | `brand.primary` (content `brand.onPrimary`) | `brand.onSubtle` | None | `interaction.disabledContainer` / `content.disabled` |
| `Outlined` | `transparent` | `interaction.selectedContainer` | `content.primary` (`interaction.onSelected` when selected) | `1dp` `border.default` (`brand.primary` when selected) | `transparent` / `content.disabled` / `border.subtle` |
| `Ghost` | `transparent` | `interaction.selectedContainer` | `content.primary` (`interaction.onSelected` when selected) | None | `transparent` / `content.disabled` |
| `Floating` | `surface.floating` (`0.92f` alpha) | `brand.primary` (content `brand.onPrimary`) | `content.primary` | `1dp` `border.subtle` (`6dp` shadow) | `interaction.disabledContainer` / `content.disabled` |
| `Destructive` | `status.error.subtle` | `status.error.solid` (content `status.error.onSolid`) | `status.error.onSubtle` | `1dp` `border.subtle` when selected/focused | `interaction.disabledContainer` / `content.disabled` |

---

## 6. Interaction Motion Contract

- **Press In**: Scales visual container cleanly using `AuraTheme.motion.pressInSpring()` to `pressedScaleDefault` (`0.96f`) for solid/outlined/floating variants or `pressedScaleGhost` (`0.95f`) for `Ghost`, while inner icon micro-compresses to `leadingIconPressedScale` (`0.92f`) via `AuraTheme.motion.iconSpring()`.
- **Minimum Press Latch**: Quick taps inside scrollable containers (`verticalScroll`, `LazyRow`) hold the pressed state for at least `AuraTheme.motion.minPressDurationMillis` (`110ms`) before releasing via `AuraTheme.motion.pressOutSpring()`, matching `AuraButton`.
- **Selection & Color Transitions**: Container, border, and icon tint animate smoothly with `tween(AuraTheme.motion.stateChangeDurationMillis)`.
- **Loading Transition**: Icon-to-spinner switch uses `AuraTheme.motion.loadingCrossfadeDurationMillis`, and the spinner drives `spinnerRotationDurationMillis` (`900ms`) + `spinnerSweepDurationMillis` (`650ms`).
- **Reduced Motion (`ANIMATOR_DURATION_SCALE == 0`)**: Disables tactile scale bounces (`1.0f`) and replaces timed color/loading transitions with `0ms` instant state swaps while preserving contrast changes.

---

## 7. Responsive Layout Behavior (`Smallest Width × Display Ratio`)

`AuraIconButton` adapts layout density and clustering guidance across all 5 `AuraWindowInfo` viewport profiles:

| Profile | Width / Ratio Condition | Recommended Sizing & Clustering Guidance |
|---|---|---|
| `ExtraSmall` | `sw < 360dp` | Prefer `Small` (`32dp` visual, `48dp` hit) or `Medium` in toolbars; cap inline clusters to 2 visible actions and overflow extras into a `More` icon button. |
| `Compact` | `360–599dp` | Default `Medium` (`40dp` visual, `48dp` hit) in headers and forms; use `Floating` `Large` (`48dp`) for primary canvas quick triggers. |
| `Medium · Square` | `600–839dp`, `ratio < 1.35` | Maintain `Medium`/`Large` with `>= 8dp` cluster gap so symmetric two-hand foldable grips do not mis-tap neighboring icon buttons. |
| `Medium · UltraWide` | `ratio >= 2.1` | Use `Small` (`32dp` visual) inside compact horizontal bars (`height` stays constrained while preserving `48dp` touch target) to maximize vertical canvas area. |
| `Expanded` | `width >= 840dp` | Prefer `Medium` or `Large` with pointer hover feedback and keyboard focus rings across toolbar strips, inspector headers, and two-pane detail bars. |

---

## 8. Accessibility & Guardrails

- **Minimum Touch Target**: Always `>= 48dp × 48dp` regardless of `Small` (`32dp`) or `Medium` (`40dp`) visual sizing.
- **Semantics**: Exposes `Role.Button`; propagates `selected` when `selected != null`; sets `stateDescription = "In progress"` during `loading`; marks `disabled()` automatically when `!enabled || loading`.
- **Content Description**: Applies `contentDescription` on the semantic node when provided so TalkBack announces `"<action>, Selected/Not selected, Button"`.
- **Contrast**: Ensures `>= 3:1` icon/border contrast against container background and `4.5:1` / `3:1` overlay separation in `Floating` mode via semi-opaque `surface.floating` + `border.subtle`.

---

## 9. Platform Bindings

### 9.1 Android (Jetpack Compose)

- **Source:** [`AuraIconButton.kt`](../../../design-system/src/main/kotlin/com/example/ds/component/AuraIconButton.kt)

<!-- API:ANDROID:BEGIN -->
```kotlin
package com.example.ds.component

enum class AuraIconButtonVariant {
    Filled,
    Tonal,
    Outlined,
    Ghost,
    Floating,
    Destructive,
}

enum class AuraIconButtonSize {
    Small,
    Medium,
    Large,
}

enum class AuraIconButtonShape {
    Circle,
    RoundedSquare,
}

@Composable
fun AuraIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: AuraIconButtonVariant = AuraIconButtonVariant.Ghost,
    size: AuraIconButtonSize = AuraIconButtonSize.Medium,
    shape: AuraIconButtonShape = AuraIconButtonShape.Circle,
    enabled: Boolean = true,
    loading: Boolean = false,
    selected: Boolean? = null,
    contentDescription: String? = null,
    debounceIntervalMs: Long = 300L,
    interactionSource: MutableInteractionSource? = null,
    icon: @Composable () -> Unit,
)
```
<!-- API:ANDROID:END -->

### 9.2 iOS (SwiftUI Reference)

<!-- API:IOS:BEGIN -->
```swift
public enum AuraIconButtonVariant {
    case filled, tonal, outlined, ghost, floating, destructive
}

public enum AuraIconButtonSize {
    case small, medium, large
}

public enum AuraIconButtonShape {
    case circle, roundedSquare
}

public struct AuraIconButton<Icon: View>: View {
    public init(
        variant: AuraIconButtonVariant = .ghost,
        size: AuraIconButtonSize = .medium,
        shape: AuraIconButtonShape = .circle,
        isEnabled: Bool = true,
        isLoading: Bool = false,
        isSelected: Bool? = nil,
        accessibilityLabel: String?,
        action: @escaping () -> Void,
        @ViewBuilder icon: () -> Icon
    )
}
```
<!-- API:IOS:END -->

### 9.3 Web (React / TypeScript Reference)

<!-- API:WEB:BEGIN -->
```ts
export type AuraIconButtonVariant = 'filled' | 'tonal' | 'outlined' | 'ghost' | 'floating' | 'destructive';
export type AuraIconButtonSize = 'small' | 'medium' | 'large';
export type AuraIconButtonShape = 'circle' | 'roundedSquare';

export interface AuraIconButtonProps {
  onClick: () => void;
  variant?: AuraIconButtonVariant;
  size?: AuraIconButtonSize;
  shape?: AuraIconButtonShape;
  disabled?: boolean;
  loading?: boolean;
  selected?: boolean;
  'aria-label'?: string;
  debounceMs?: number;
  children: React.ReactNode;
}
```
<!-- API:WEB:END -->
