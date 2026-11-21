---
id: "aura.component.fab"
name: "AuraFab"
category: "actions"
status: "ready"
version: "1.0.0"
layer: "chrome"
material_allowed: ["Solid", "Glass"]
motion_track: "Expressive"
platforms:
  android: "com.example.ds.component.AuraFab"
  ios: "AuraDS.AuraFab (planned)"
  web: "@aura-ds/react.AuraFab (planned)"
tokens:
  - "colors.brand.primary"
  - "colors.brand.onPrimary"
  - "colors.brand.subtle"
  - "colors.brand.onSubtle"
  - "colors.surface.floating"
  - "colors.content.primary"
  - "colors.content.disabled"
  - "colors.border.subtle"
  - "colors.border.focus"
  - "colors.interaction.disabledContainer"
  - "colors.interaction.pressedOverlay"
  - "typography.labelLarge"
  - "typography.labelMedium"
  - "typography.titleSmall"
  - "motion.pressInSpring"
  - "motion.pressOutSpring"
  - "motion.iconSpring"
  - "motion.stateChangeDurationMillis"
  - "motion.loadingCrossfadeDurationMillis"
  - "motion.spinnerRotationDurationMillis"
  - "motion.spinnerSweepDurationMillis"
  - "motion.minPressDurationMillis"
  - "motion.pressedScaleDefault"
  - "motion.pressedScaleLarge"
  - "motion.leadingIconPressedScale"
---

# AuraFab

> Persistent floating action button for the single primary creation entry point on a browse, schedule, ledger, or journal screen.

---

## 1. Intent & Usage Principles

Aligned with [00-button-principles.md](00-button-principles.md), `AuraFab` floats at the predictable creation anchor of a screen (`AuraScaffold` bottom-end slot on handsets, or top-pane action anchor on large tablets) so users can initiate their primary creation flow (`+ New Event/Diary`, `+ Add Asset`, `+ Log Bottle`) immediately while scrolling. It supports both compact icon-only silhouettes and Extended pill labels (`text` + `expanded`).

### Problem

Browse and collection screens need one persistent, thumb-reachable entry point for the primary creation action that stays visible regardless of scroll position.

### When to Use

- Launching creation sheets or editors from a feed/grid/calendar (`+ New Diary`, `+ Add Transaction`, `+ Log Tasting Note`).
- Persistent single-thumb creation control sitting inside `AuraScaffold(floatingActionButton = ...)`.
- Scroll-responsive Extended FAB that clarifies ambiguous creation actions (`"+ Add Asset"`) before collapsing to compact `+`.

### When NOT to Use & Alternatives

- Form bottom bars (`Save`, `Confirm`, `Cancel`) -> use `AuraButton`.
- Repeated item cards or list row inline controls -> use `AuraIconButton` or `AuraButton(Secondary/Ghost)`.
- Filtering, tagging, or switching modes -> use `AuraChipButton` or `AuraSegmentedButton`.

### Context Rules

- **`FAB-RULE-001` (Single Screen Creation Anchor)**: A screen may render **at most one `AuraFab`**, reserved strictly for the primary constructive creation task—never for destructive triggers (`Delete`), secondary navigation, or multi-step form submissions (use `AuraButton`).
- **`FAB-RULE-002` (Extended-on-Rest, Compact-on-Scroll)**: Prefer `text != null` with `expanded = true` when first entering list/ledger screens so new users recognize the creation action at a glance; collapse smoothly to icon-only (`expanded = false`) as vertical scroll offset increases so underlying content stays unobstructed.
- **`FAB-RULE-003` (Surface Materiality Restraint)**: Default to `Primary` or `Secondary` over solid lists and forms. Reserve `Glass` strictly for translucent wallpaper/camera/3D launcher canvases (**Home Launcher**, **Drink Diary** hero cards) where frosted depth preserves legibility without heavy slabs.
- **`FAB-RULE-004` (Mandatory Collapsed Accessibility Label)**: Whenever `text` is null OR `expanded == false`, callers MUST pass a descriptive `contentDescription` (e.g., `"Create diary entry"`) so screen readers announce the exact creation target even when the visible pill is collapsed.

### Do / Don't

| Do | Don't |
|---|---|
| Limit screens to a single `AuraFab` mapped to the core constructive creation flow. | Stack two or more FABs vertically or use `AuraFab` for delete/reset actions. |
| Supply `contentDescription` even when `text` is passed, so collapsing (`expanded = false`) preserves TalkBack clarity. | Rely only on `text` for semantics and leave `contentDescription = null` on collapsible Extended FABs. |
| Collapse extended FAB labels cleanly (`expanded = false`) when the user scrolls down a dense list. | Keep wide text-heavy Extended FABs blocking cards and bottom metrics while scrolling. |
| Use `Glass` only above rich imagery, wallpapers, or 3D overlays with sufficient edge border definition. | Place translucent `Glass` FABs over plain monochrome backgrounds where `Primary` or `Surface` reads stronger. |

---

## 2. Anatomy & Canonical Part IDs

```text
  Icon-only:        Extended (expanded = true, text != null):
   ╭──────╮          ╭─────────────────────────╮
   │  [+] │          │  [+]   New Tasting Note │
   ╰──────╯          ╰─────────────────────────╯
```

| Part ID | Responsibility | Required | Semantics Owner |
|---|---|---|---|
| `root` | Circular (icon-only) or capsule (extended) container with shadow | Yes | `Role.Button` |
| `icon` | Required glyph slot | Yes | `contentDescription` when icon-only |
| `label` | `text` revealed when `expanded = true` | No | Accessible name when shown |
| `progressIndicator` | Spinner replacing the icon when `loading = true` | Conditional | `stateDescription = "In progress"` |

---

## 3. Variant, Size & Material Matrix

### Variants (`AuraFabVariant`)

1. **`Primary`** — Solid high-emphasis brand pill (`brand.primary` / `brand.onPrimary`), default for the primary creation CTA.
2. **`Secondary`** — Soft brand tint (`brand.subtle` / `brand.onSubtle`) with refined shadow for calm surfaces or split-pane secondary creation.
3. **`Surface`** — Elevated floating neutral pill (`surface.floating` / `content.primary`) + subtle `border.subtle` for monochrome or neutral-first editorial tools.
4. **`Glass`** — Frosted translucent overlay pill (`surface.floating @ 0.80 alpha` + luminous `border.subtle`) crafted for photography, bottle labels, and launcher canvases.

### Sizes (`AuraFabSize`)

| Size | Visual Container Height / Width | Min Hit Target | Icon Size | Label Typography (Extended) | Corner Radius |
|---|---|---|---|---|---|
| `Small` | `44dp` | `48dp × 48dp` | `20dp` | `AuraTheme.typography.labelMedium` | `CircleShape` (`50%`) |
| `Regular` | `56dp` | `56dp × 56dp` | `24dp` | `AuraTheme.typography.labelLarge` | `CircleShape` (`50%`) |
| `Large` | `64dp` | `64dp × 64dp` | `28dp` | `AuraTheme.typography.titleSmall` | `CircleShape` (`50%`) |

Extended FABs maintain the exact visual height (`44dp` / `56dp` / `64dp`) while smoothly expanding horizontal padding (`start = 16.dp`, `end = 20.dp` on `Regular`) to house the label.

---

## 4. State Precedence & Behavior

1. **Disabled (`!enabled`)**: Clicks suppressed, `interaction.disabledContainer`, `content.disabled`, `0dp` shadow, no press scale.
2. **Loading (`loading == true`)**: Clicks suppressed, retains active variant surface & content colors, crossfades icon to compact spinner (`stateDescription = "In progress"`), collapses label smoothly if extended or locks footprint safely.
3. **Pressed**: Tactile scale spring (`pressedScaleDefault` / `pressedScaleLarge`) + `interaction.pressedOverlay` wash latched for `minPressDurationMillis`.
4. **Focused / Hovered**: Keyboard focus draws `2dp` `border.focus` outline; pointer hover adds subtle `pressedOverlay` tint.
5. **Extended (`!text.isNullOrEmpty() && expanded`) vs Compact**: Smooth horizontal expansion/collapse.
6. **Default**: Resting floating elevation & variant colors.

Rapid duplicate creations are prevented by `debounceIntervalMs` (default `400L` ms).

---

## 5. Semantic Token Bindings

All colors, typography, and motion resolve through `AuraTheme`:

| Variant | Container | Content (Icon / Label / Spinner) | Border | Elevation Shadow | Disabled (`enabled == false`) |
|---|---|---|---|---|---|
| `Primary` | `brand.primary` | `brand.onPrimary` | `border.focus` when focused | `8dp` (`0dp` disabled) | `interaction.disabledContainer` / `content.disabled` |
| `Secondary` | `brand.subtle` | `brand.onSubtle` | `border.focus` when focused | `6dp` (`0dp` disabled) | `interaction.disabledContainer` / `content.disabled` |
| `Surface` | `surface.floating` | `content.primary` | `1dp` `border.subtle` (`2dp` `border.focus` focused) | `8dp` (`0dp` disabled) | `interaction.disabledContainer` / `content.disabled` |
| `Glass` | `surface.floating` (`0.80f` alpha) | `content.primary` | `1dp` `border.subtle` (`2dp` `border.focus` focused) | `6dp` (`0dp` disabled) | `interaction.disabledContainer` / `content.disabled` |

---

## 6. Interaction Motion Contract

- **Press In / Out**: `Regular` and `Small` compress to `AuraTheme.motion.pressedScaleDefault` (`0.96f`), while `Large` compresses to `AuraTheme.motion.pressedScaleLarge` (`0.975f`) via `pressInSpring()` / `pressOutSpring()`, and icon micro-scales to `leadingIconPressedScale` (`0.92f`) via `iconSpring()`.
- **Minimum Press Latch**: Holds pressed state for at least `AuraTheme.motion.minPressDurationMillis` (`110ms`) on fast taps.
- **Extend / Collapse Transition**: Bound to the Expressive track (`AuraTheme.motion.forTrack(AuraMotionProfile.Expressive)`), the label container animates via `expandHorizontally(spatialSpring()) + fadeIn(enterSpec())` / `shrinkHorizontally(spatialSpring()) + fadeOut(exitSpec())`.
- **Loading Transition**: Icon crossfades with `loadingCrossfadeDurationMillis` (`120ms`) into animated stroke spinner (`spinnerRotationDurationMillis`, `spinnerSweepDurationMillis`).
- **Reduced Motion (`ANIMATOR_DURATION_SCALE == 0`)**: Disables press scaling (`1.0f`) and replaces animated expand/shrink transitions with instant `EnterTransition.None` / `ExitTransition.None`.

---

## 7. Responsive Layout Behavior (`Smallest Width × Display Ratio`)

| Profile | Width / Ratio Condition | Placement & Extended Adaptation |
|---|---|---|
| `ExtraSmall` | `sw < 360dp` | Prefer `Small` (`44dp` visual, `48dp` hit) or compact `Regular` icon-only (`expanded = false`) at `12dp` edge margin so narrow 320–359dp feeds stay legible. |
| `Compact` | `360–599dp` | Standard `Regular` (`56dp`) Extended on initial load (`expanded = true`), collapsing automatically once scroll exceeds threshold (`16dp` margin). |
| `Medium · Square` | `600–839dp`, `ratio < 1.35` | Maintain bottom-end `Regular` or `Large` placement inside foldable square panes with `24dp` outer clearance (`AuraScaffold` safe insets). |
| `Medium · UltraWide` | `ratio >= 2.1` | Collapse extended label (`expanded = false`) or use `Small` (`44dp` visual / `48dp` hit) on bottom-end so shallow landscape viewports stay unobstructed. |
| `Expanded` | `width >= 840dp` | Anchor `Regular` / `Large` Extended FAB within the bottom-end of the primary browse pane or top-start rail header; supports pointer hover lift and keyboard focus ring. |

---

## 8. Accessibility & Guardrails

- **Minimum Touch Target**: Enforces at least `48dp × 48dp` even when `Small` (`44dp`) visual frame is used.
- **Semantics**: Exposes `Role.Button`; sets `contentDescription` directly on semantic node when provided; sets `stateDescription = "In progress"` during `loading`; disables interaction semantics automatically when `!enabled || loading`.
- **Contrast**: Ensures `>= 4.5:1` label contrast and `>= 3:1` icon/container separation across both Light and Dark themes.

---

## 9. Platform Bindings

### 9.1 Android (Jetpack Compose)

- **Source:** [`AuraFab.kt`](../../../design-system/src/main/kotlin/com/example/ds/component/AuraFab.kt)

<!-- API:ANDROID:BEGIN -->
```kotlin
package com.example.ds.component

enum class AuraFabVariant {
    Primary,
    Secondary,
    Surface,
    Glass,
}

enum class AuraFabSize {
    Small,
    Regular,
    Large,
}

@Composable
fun AuraFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String? = null,
    expanded: Boolean = true,
    variant: AuraFabVariant = AuraFabVariant.Primary,
    size: AuraFabSize = AuraFabSize.Regular,
    enabled: Boolean = true,
    loading: Boolean = false,
    contentDescription: String? = null,
    debounceIntervalMs: Long = 400L,
    interactionSource: MutableInteractionSource? = null,
    icon: @Composable () -> Unit,
)
```
<!-- API:ANDROID:END -->

### 9.2 iOS (SwiftUI Reference)

<!-- API:IOS:BEGIN -->
```swift
public enum AuraFabVariant {
    case primary, secondary, surface, glass
}

public enum AuraFabSize {
    case small, regular, large
}

public struct AuraFab<Icon: View>: View {
    public init(
        text: String? = nil,
        isExpanded: Bool = true,
        variant: AuraFabVariant = .primary,
        size: AuraFabSize = .regular,
        isEnabled: Bool = true,
        isLoading: Bool = false,
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
export type AuraFabVariant = 'primary' | 'secondary' | 'surface' | 'glass';
export type AuraFabSize = 'small' | 'regular' | 'large';

export interface AuraFabProps {
  onClick: () => void;
  text?: string;
  expanded?: boolean;
  variant?: AuraFabVariant;
  size?: AuraFabSize;
  disabled?: boolean;
  loading?: boolean;
  'aria-label'?: string;
  debounceMs?: number;
  icon: React.ReactNode;
}
```
<!-- API:WEB:END -->
