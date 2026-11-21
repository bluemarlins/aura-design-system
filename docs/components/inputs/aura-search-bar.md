---
id: "aura.component.search-bar"
name: "AuraSearchBar"
category: "inputs"
status: "ready"
version: "1.0.0"
layer: "chrome"
material_allowed: ["Solid", "Glass"]
motion_track: "Standard"
platforms:
  android: "com.example.ds.component.AuraSearchBar"
  ios: "AuraDS.AuraSearchBar (planned)"
  web: "@aura-ds/react.AuraSearchBar (planned)"
tokens:
  - "colors.surface.sunken"
  - "colors.surface.default"
  - "colors.surface.floating"
  - "colors.brand.primary"
  - "colors.brand.onPrimary"
  - "colors.content.primary"
  - "colors.content.secondary"
  - "colors.content.tertiary"
  - "colors.content.disabled"
  - "colors.border.subtle"
  - "colors.border.focus"
  - "colors.interaction.disabledContainer"
  - "typography.bodyLarge"
  - "typography.labelSmall"
  - "motion.pressInSpring"
  - "motion.pressOutSpring"
  - "motion.stateChangeDurationMillis"
  - "motion.minPressDurationMillis"
  - "motion.pressedScaleDefault"
---

# AuraSearchBar

> Instant query bar engineered for real-time filtering, universal search, and quick cellar/archive lookup, supporting both crisp `Solid` surfaces and dual-materiality `Glass` floating surfaces (`FROSTED` / `LIQUID`).

---

## 1. Intent & Usage Principles

### Problem

Users need to filter or search a collection instantly as they type, with quick clear and filter access in one control.

### When to Use

- Real-time keyword filtering of lists, wine/whiskey cellars (**My Drink Diary**), stock tickers (**My Finance**), or schedule archives (**My Planner**).
- Universal app/widget launcher search over colorful wallpaper Layer 0 canvases (**Home Launcher**) using `AuraSearchBarSurface.Glass`.
- Pairing search with quick-clear (`×`) and optional active-filter count indicator (`onFilterClick`).

### When NOT to Use & Alternatives

- **Persistent form fields requiring a field label, error validation, and submit saving:** Use `AuraTextField`.
- **Entering purely numeric amounts or currencies:** Use `AuraNumberField`.
- **Mutually exclusive 2–5 filter pills with no typing needed:** Use `AuraChipButton` or `AuraSegmentedButton`.

### Context Rules

- **`SRCH-RULE-001` (Instant Feedback):** Query changes update filtered results as the user types via `onQueryChange`; also wire `ImeAction.Search` (`onSearch`) for explicit keyboard confirmation.
- **`SRCH-RULE-002` (Glass Over Rich Media Only):** Use `AuraSearchBarSurface.Glass` (`GlassStyle.FROSTED` or `GlassStyle.LIQUID`) only when floating above colorful imagery or scrollable Layer 0 cards; default to `AuraSearchBarSurface.Solid` (`surface.sunken`) on standard white/dark content pages.
- **`SRCH-RULE-003` (One-Tap Clear & Filter Affordance):** Render a `>= 48dp` clear button automatically whenever `query.isNotEmpty() && enabled`, and display an active count indicator when `activeFilterCount > 0`.

### Do / Don't

| Do | Don't |
|---|---|
| Use `AuraSearchBarSurface.Solid` above flat lists (`My Drink Diary` inventory) for calm legibility. | Put `Glass` surfaces on plain uniform backgrounds where backdrop refraction is invisible. |
| Surface `activeFilterCount` on the trailing filter icon so users know hidden filters are narrowing results. | Force users to type exact filter syntax (`abv:>40`) without a filter trigger affordance. |
| Trigger `onSearch?.invoke(query)` from the keyboard Search action. | Require tapping a separate external "Search" button before filtering works. |
| Maintain `>= 48dp` interactive height across all breakpoints. | Crop clear or filter buttons below `48dp` inside dense headers. |

---

## 2. Anatomy & Canonical Part IDs

```text
  ┌─────────────────────────────────────────────────────────┐
  │  (Q)   Search bottles, distilleries...   (×)   [≡  2 ]  │
  └─────────────────────────────────────────────────────────┘
   Leading      Single-line query input     Clear  Filter + badge
```

| Part ID | Responsibility | Required | Semantics Owner |
|---|---|---|---|
| `root` | `52dp` full-capsule container (`Solid` or `Glass`) | Yes | Inherited |
| `leadingIcon` | Search glyph | Yes | Decorative |
| `input` | Single-line query text with `placeholder` | Yes | Text field (IME action `Search`) |
| `clearButton` | Clears `query`; visible only when `query` is not empty | Conditional | `Role.Button` (`clearContentDescription`) |
| `filterButton` | Opens filters; shown when `onFilterClick != null` | No | `Role.Button` (`"Filter (N active)"`) |
| `filterBadge` | Count of `activeFilterCount` on `filterButton` | Conditional | Merged into `filterButton` |

---

## 3. Variant, Size & Material Matrix

- `AuraSearchBarSurface.Solid`: Pill (`50%` corner radius) backed by `surface.sunken`, elevating stroke to `border.focus` (`2dp`) while focused.
- `AuraSearchBarSurface.Glass`: Full-capsule `GlassSurface` supporting `GlassStyle.FROSTED` and `GlassStyle.LIQUID` with live Layer 0 backdrop sampling (`LocalGlassBackdropState`).
- **Size:** Single size — `52dp` minimum height, `48dp` touch targets for `clearButton` and `filterButton`.

---

## 4. State Precedence & Behavior

`Disabled` (`!enabled`) > `Focused` > `HasQuery` (`query.isNotEmpty()`) > `Rest`.

---

## 5. Semantic Token Bindings

| Element | State | Light / Dark Semantic Token |
|---|---|---|
| Container (`Solid`) | `Rest` / `Disabled` | `surface.sunken` / `interaction.disabledContainer` |
| Container (`Glass`) | `FROSTED` / `LIQUID` | `GlassSurface(glassStyle)` with adaptive tint & specular rim |
| Stroke (`Solid`) | `Focused` / `Rest` | `border.focus` (`2dp`) / `border.subtle` (`1dp`) |
| Leading Magnifier Icon | `Focused` / `Rest` / `Disabled` | `brand.primary` / `content.secondary` / `content.disabled` |
| Query & Placeholder Text | `Rest` / `Placeholder` / `Disabled` | `content.primary` / `content.tertiary` / `content.disabled` (`typography.bodyLarge`) |
| Filter Badge Pill | `activeFilterCount > 0` | `brand.primary` background + `brand.onPrimary` (`typography.labelSmall`) |

---

## 6. Interaction Motion Contract

Consumes `AuraTheme.motion`:

1. **Focus Border & Icon Accent:** Border width/color and leading magnifier icon tint transition over `AuraTheme.motion.stateChangeDurationMillis` (`150ms`).
2. **Clear Button Reveal & Tactile Press:** Clear button scales smoothly (`0f -> 1f`) with `AuraTheme.motion.iconSpring()`; interactive trailing buttons scale on press using `rememberAuraTactilePressed(..., motion.minPressDurationMillis)` and `pressInSpring()` / `pressOutSpring()`.
3. **Reduced Motion:** Uses `snap()` under `rememberAuraReducedMotion()`.

---

## 7. Responsive Layout Behavior (`Smallest Width × Display Ratio`)

| Width / Display Profile | Layout & Chrome Adaptation |
|---|---|
| `ExtraSmall` (`< 360dp`) | Expands to `fillMaxWidth()`, prioritizing query text area (`weight(1f)`) with compact `12dp` inner horizontal padding. |
| `Compact` (`360–599dp`) | Full-width `52dp` pill bar ideal beneath `AuraTopAppBar` or docked inside `FloatingGlassScaffold`. |
| `Square` (`~1:1` Pop-up / Foldable) | Fits comfortably within single-pane search headers or master pane of `AuraAdaptiveTwoPane` without clipping trailing filter pill. |
| `UltraWide` (`> 21:9`) | Maintains compact `50dp` height so vertical list viewport stays generous in landscape split windows. |
| `Expanded` (`>= 840dp` Tablet) | Bounded smoothly inside `AuraTheme.layout.maxContentWidth` or master pane column (typically `360–560dp`), never stretching awkwardly edge-to-edge. |

---

## 8. Accessibility & Guardrails

- Uses `ImeAction.Search` (`KeyboardOptions(imeAction = ImeAction.Search)`).
- Clear (`×`) and Filter (`onFilterClick`) buttons provide `Role.Button` with descriptive `contentDescription` (`"Clear search"` and `"Filter (${activeFilterCount} active)"`) and minimum `48dp` tap area.
- The clear button's description is `clearContentDescription` (Android, since `1.1.2`), so apps whose UI language is not English can pass their own label (for example `"검색어 지우기"`).
- **Reduced Transparency & Contrast (`VIS-RULE-003`–`004`):** `AuraSearchBarSurface.Glass` maintains `>= 4.5:1` query/placeholder contrast (`>= 3:1` borders/icons) over dynamic backdrops and falls back cleanly to opaque `AuraMaterial.Solid` (`colors.surface.floating`) via `AuraTheme.effectiveMaterial(...)` when Reduced Transparency, Battery Saver, or High-Contrast Text is enabled.

---

## 9. Platform Bindings

### 9.1 Android (Jetpack Compose)

- **Source:** [`AuraSearchBar.kt`](../../../design-system/src/main/kotlin/com/example/ds/component/AuraSearchBar.kt)

<!-- API:ANDROID:BEGIN -->
```kotlin
enum class AuraSearchBarSurface {
    Solid,
    Glass,
}

@Composable
fun AuraSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onSearch: ((String) -> Unit)? = null,
    placeholder: String = "Search...",
    surface: AuraSearchBarSurface = AuraSearchBarSurface.Solid,
    glassStyle: GlassStyle = GlassStyle.FROSTED,
    enabled: Boolean = true,
    activeFilterCount: Int = 0,
    onFilterClick: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    clearContentDescription: String = "Clear search",
)
```
<!-- API:ANDROID:END -->

### 9.2 iOS (SwiftUI Reference)

<!-- API:IOS:BEGIN -->
```swift
public enum AuraSearchBarSurface: Sendable {
    case solid
    case glass
}

public struct AuraSearchBar: View {
    public init(
        query: Binding<String>,
        onSearch: ((String) -> Void)? = nil,
        placeholder: String = "Search...",
        surface: AuraSearchBarSurface = .solid,
        glassStyle: GlassStyle = .frosted,
        enabled: Bool = true,
        activeFilterCount: Int = 0,
        onFilterClick: (() -> Void)? = nil
    )
}
```
<!-- API:IOS:END -->

### 9.3 Web (React / TypeScript Reference)

<!-- API:WEB:BEGIN -->
```ts
export type AuraSearchBarSurface = 'Solid' | 'Glass';

export interface AuraSearchBarProps {
  query: string;
  onQueryChange: (query: string) => void;
  onSearch?: (query: string) => void;
  placeholder?: string;
  surface?: AuraSearchBarSurface;
  glassStyle?: GlassStyle;
  enabled?: boolean;
  activeFilterCount?: number;
  onFilterClick?: () => void;
}
```
<!-- API:WEB:END -->
