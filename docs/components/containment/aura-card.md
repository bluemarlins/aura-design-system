---
id: "aura.component.card"
name: "AuraCard"
category: "containment"
status: "ready"
version: "1.0.0"
layer: "content"
material_allowed: ["Solid", "Glass"]
motion_track: "Standard"
platforms:
  android: "com.example.ds.component.AuraCard"
  ios: "AuraDS.AuraCard (planned)"
  web: "@aura-ds/react.AuraCard (planned)"
tokens:
  - "colors.surface.default"
  - "colors.surface.raised"
  - "colors.surface.sunken"
  - "colors.surface.floating"
  - "colors.content.primary"
  - "colors.border.subtle"
  - "colors.border.default"
  - "colors.border.strong"
  - "colors.brand.primary"
  - "colors.interaction.selectedContainer"
  - "colors.interaction.pressedOverlay"
  - "colors.interaction.disabledContainer"
  - "motion.pressInSpring"
  - "motion.pressOutSpring"
  - "motion.stateChangeDurationMillis"
  - "motion.minPressDurationMillis"
  - "motion.pressedScaleLarge"
---

# AuraCard

> Grouped surface container for structuring related metadata, summaries, interactive catalog entries, and **Home Launcher** `Glass` widgets cleanly across Light and Dark themes.

---

## 1. Intent & Usage Principles

### Problem

`AuraCard` replaces generic Material cards with Aura's semantic surface stepping (`Elevated`, `Filled`, `Outlined`, `Glass`) and standardized internal padding presets (`None`, `Compact`, `Medium`, `Large`). When `onClick != null`, `AuraCard` automatically gains `Role.Button` accessibility, debounced click protection, and subtle large-surface tactile spring compression (`pressedScaleLarge = 0.975f`); when `onClick == null`, it behaves as a calm, passive container.

### When to Use

- Grouping cohesive domain records (e.g., **My Finance** portfolio summary, **My Drink Diary** bottle card, **My Planner** daily block).
- Presenting interactive grid tiles in responsive catalogs (`CatalogHomeScreen` grid cards).
- Grouping preview matrices, inspector panels, or preference sections without visual clutter.

### When NOT to Use & Alternatives

- **Wrapping an entire full-screen page in one giant card:** Use `AuraScaffold` and `background.primary` instead (`CNT-RULE-004`).
- **Simple single-line actions or buttons:** Use `AuraButton` or `AuraChipButton`.
- **Deeply nested card-inside-card hierarchies:** Avoid placing `Outlined` or `Elevated` cards inside other `Outlined`/`Elevated` cards; use spacing or a `Filled` (`surface.sunken`) nested callout block at most 1 level deep.

### Context Rules

- **`CARD-RULE-001` (Choose Emphasis by Surface Context):** Default to `Outlined` for scannable lists and catalog grids, `Elevated` for hero summaries (**My Finance** net worth), `Filled` for quiet nested callout info panels, and `Glass` only over rich backdrops (**Home Launcher**).
- **`CARD-RULE-002` (Interactive vs. Passive Honesty):** Only supply `onClick != null` when the entire card performs an action (navigation, selection toggle). Never make decorative summary cards artificially clickable.
- **`CARD-RULE-003` (Standardized Internal Padding):** Prefer `AuraCardPadding` (`Compact` 12dp, `Medium` 16dp, `Large` 24dp) rather than ad-hoc padding on child columns; use `None` (0dp) only when hosting edge-to-edge media headers.
- **`CARD-RULE-004` (Gentle Tactile Scale on Large Surfaces):** Interactive cards must scale at `motion.pressedScaleLarge` (`0.975f`), never aggressive small-button scales (`0.95f`), so large containers feel refined rather than jittery.

### Do / Don't

| Do | Don't |
|---|---|
| Use `AuraCard(onClick = ...)` for tappable list/grid items such as tasting logs or catalog entries. | Wrap a passive Material `Card` inside `Modifier.clickable(...)` bypassing Aura tactile motion. |
| Use `AuraCard(variant = Filled)` with `Compact` padding for rule readouts and subtle summaries. | Nest `Elevated` cards three levels deep with overlapping drop shadows. |
| Use `selected = true` to highlight an active card choice with `brand.primary` border & tint. | Rely on color alone without structural border thickness change on selected cards. |
| Pass `padding = AuraCardPadding.None` for full-bleed banner/image headers paired with inner text padding. | Stack double `16dp` padding inside both `AuraCard` and its root child `Column`. |

---

## 2. Anatomy & Canonical Part IDs

```text
  Outlined (Default)        Elevated                  Filled                    Glass
  ╭──────────────────╮      ╭──────────────────╮      ╭──────────────────╮      ╭──────────────────╮
  │ surface.default  │      │ surface.raised   │      │ surface.sunken   │      │ surface.floating │
  │ 1dp border.def.  │      │ 1dp border.subtle│      │ 1dp border.subtle│      │ 1dp border.subtle│
  ╰──────────────────╯      │ + 4dp elevation  │      ╰──────────────────╯      │ 20dp corner rad. │
                            ╰──────────────────╯                                ╰──────────────────╯
```

| Part ID | Responsibility | Required | Semantics Owner |
|---|---|---|---|
| `root` | Container fill, border, corner radius, elevation; clickable when `onClick != null` | Yes | `Role.Button` when clickable, otherwise none |
| `content` | `ColumnScope` slot padded by `AuraCardPadding` | Yes | Children keep their own semantics |
| `selectionTint` | `interaction.selectedContainer` overlay when `selected = true` | Conditional | `selected` state |

---

## 3. Variant, Size & Material Matrix

| Variant (`AuraCardVariant`) | Container Token | Border Token (`Default` -> `Selected`) | Elevation | Corner Radius |
|---|---|---|---|---|
| `Outlined` (default) | `surface.default` | `border.default` (`1dp`) -> `brand.primary` (`1.5dp`) | `0dp` | `16dp` |
| `Elevated` | `surface.raised` | `border.subtle` (`1dp`) -> `brand.primary` (`1.5dp`) | `4dp` | `16dp` |
| `Filled` | `surface.sunken` | `border.subtle` (`1dp`) -> `brand.primary` (`1.5dp`) | `0dp` | `16dp` |
| `Glass` | `surface.floating` | `border.subtle` (`1dp`) -> `brand.primary` (`1.5dp`) | `2dp` | `20dp` |

| Padding (`AuraCardPadding`) | Value | Intended Use |
|---|---|---|
| `None` | `0dp` | Full-bleed imagery, map previews, custom child row layouts |
| `Compact` | `12dp` | Dense metadata blocks, narrow split panes, `ExtraSmall` screens |
| `Medium` (default) | `16dp` | Standard catalog cards, domain record cards, preview matrices |
| `Large` | `24dp` | Featured hero summary cards on tablets (`Expanded`) |

- **Material:** `Glass` variant renders a translucent `surface.floating` fill without a live-blur `GlassSurface` (`layer: content`, `VIS-RULE-001`).

---

## 4. State Precedence & Behavior

**State Precedence:** `Disabled` > `Pressed` (`onClick != null`) > `Selected` > `Default` (`Passive` when `onClick == null`).

```mermaid
stateDiagram-v2
    [*] --> Default
    Default --> Pressed: Pointer Down (onClick != null)
    Pressed --> Default: Pointer Up / Cancel
    Default --> Selected: selected = true
    Selected --> Pressed: Pointer Down (onClick != null)
    Default --> Disabled: enabled = false
    Selected --> Disabled: enabled = false
```

---

## 5. Semantic Token Bindings

| Element | State | Light / Dark Semantic Token |
|---|---|---|
| Container Surface | `Default` | Mapped by `AuraCardVariant` (`surface.default` / `raised` / `sunken` / `floating`) |
| Container Surface | `Selected` | `interaction.selectedContainer` blended gently or `surface.default` + `brand.primary` border (`Glass` stays `surface.floating`) |
| Container Surface | `Disabled` | `AuraTheme.colors.interaction.disabledContainer` |
| Press Overlay | `Pressed` (`onClick != null`) | `AuraTheme.colors.interaction.pressedOverlay` |
| Border | `Default` / `Selected` / `Disabled` | `border.default` or `border.subtle` / `brand.primary` (`1.5dp`) / `border.subtle` |
| Default Content Tint | `All` | Provided via `LocalAuraContentColor` -> `AuraTheme.colors.content.primary` |

---

## 6. Interaction Motion Contract

Consumes `AuraTheme.motion`:

1. **Interactive Tactile Compression (`onClick != null`):** Tracks pointer press via `rememberAuraTactilePressed(interactionSource, motion.minPressDurationMillis)` and animates card scale to `AuraTheme.motion.pressedScaleLarge` (`0.975f`) using `pressInSpring()` / `pressOutSpring()`, plus `interaction.pressedOverlay` tint.
2. **Passive Stability (`onClick == null`):** Remains completely static (`1.0f` scale, zero hover/press tint) so non-interactive containers never trick users into tapping them.
3. **Selection & State Color Transitions:** Border and container colors crossfade smoothly over `AuraTheme.motion.stateChangeDurationMillis` (`150ms`).
4. **Reduced Motion:** Uses `snap()` when system `ANIMATOR_DURATION_SCALE == 0f`.

---

## 7. Responsive Layout Behavior (`Smallest Width × Display Ratio`)

`AuraCard` integrates cleanly with `AuraAdaptiveGrid` and `AuraAdaptiveTwoPane` across every `AuraWindowInfo` profile:

| Width / Display Profile | Grid & Width Adaptation | Internal Padding & Layout Guidance |
|---|---|---|
| `ExtraSmall` (`< 360dp`) | Single-column (`1 col`) spanning available pane width cleanly without horizontal overflow. | Prefer `Compact` (`12dp`) or `Medium` (`16dp`) padding to maximize text line measure. |
| `Compact` (`360–599dp`) | Full-width card lists or 1-column feeds with `16dp` screen margins. | Standard `Medium` (`16dp`) padding; `16dp` corner radius balances content framing and height. |
| `Square` (`~1:1` Pop-up / Foldable) | Flexes into `2-col` (`AuraAdaptiveGrid`) when width >= `600dp`, or stacks cleanly inside split pop-ups. | Keep internal child rows responsive (`FlowRow` or flexible weights) so metadata never truncates awkwardly. |
| `UltraWide` (`> 21:9`) | Remains crisp in single-column cover strips or expands cleanly in wide landscape multi-column feeds. | Bound hero cards with parent `maxContentWidth` (`600dp–1200dp`) so typography lines never become excessively long. |
| `Expanded` (`>= 840dp` Tablet) | Displays inside `2–3` column adaptive grids (`AuraAdaptiveGrid`) and secondary inspector panes (`AuraAdaptiveTwoPane`). | Ideal home for `Elevated` summary cards with `Large` (`24dp`) padding alongside detail forms. |

---

## 8. Accessibility & Guardrails

- [ ] Interactive `AuraCard` (`onClick != null`) registers `Role.Button`, debounces rapid taps via `rememberAuraDebouncedClick`, and sets `enabled` semantics.
- [ ] Passive `AuraCard` (`onClick == null`) does NOT attach clickable semantics or intercept child focus/accessibility navigation.
- [ ] `selected = true` updates border thickness (`1.5dp`) and `brand.primary` color cleanly in both Light and Dark themes.
- [ ] Child typography defaults cleanly via `LocalAuraContentColor` (`content.primary`) without inheriting Material surface defaults.

---

## 9. Platform Bindings

### 9.1 Android (Jetpack Compose)

- **Source:** [`AuraCard.kt`](../../../design-system/src/main/kotlin/com/example/ds/component/AuraCard.kt)

<!-- API:ANDROID:BEGIN -->
```kotlin
enum class AuraCardVariant { Elevated, Filled, Outlined, Glass }
enum class AuraCardPadding { None, Compact, Medium, Large }

@Composable
fun AuraCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    variant: AuraCardVariant = AuraCardVariant.Outlined,
    padding: AuraCardPadding = AuraCardPadding.Medium,
    enabled: Boolean = true,
    selected: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
)
```
<!-- API:ANDROID:END -->

### 9.2 iOS (SwiftUI Reference)

<!-- API:IOS:BEGIN -->
```swift
public enum AuraCardVariant: Sendable {
    case elevated
    case filled
    case outlined
    case glass
}

public enum AuraCardPadding: Sendable {
    case none
    case compact
    case medium
    case large
}

public struct AuraCard<Content: View>: View {
    public init(
        variant: AuraCardVariant = .outlined,
        padding: AuraCardPadding = .medium,
        isEnabled: Bool = true,
        isSelected: Bool = false,
        onTap: (() -> Void)? = nil,
        @ViewBuilder content: () -> Content
    )
}
```
<!-- API:IOS:END -->

### 9.3 Web (React / TypeScript Reference)

<!-- API:WEB:BEGIN -->
```ts
export type AuraCardVariant = 'elevated' | 'filled' | 'outlined' | 'glass';
export type AuraCardPadding = 'none' | 'compact' | 'medium' | 'large';

export interface AuraCardProps {
  variant?: AuraCardVariant;
  padding?: AuraCardPadding;
  disabled?: boolean;
  selected?: boolean;
  onClick?: () => void;
  className?: string;
  children?: React.ReactNode;
}
```
<!-- API:WEB:END -->
