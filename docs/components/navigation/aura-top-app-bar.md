---
id: "aura.component.top-app-bar"
name: "AuraTopAppBar"
category: "navigation"
status: "ready"
version: "1.0.0"
layer: "chrome"
material_allowed: ["Solid", "Glass", "LiquidGlass"]
motion_track: "Standard"
platforms:
  android: "com.example.ds.component.AuraTopAppBar"
  ios: "AuraDS.AuraTopAppBar (planned)"
  web: "@aura-ds/react.AuraTopAppBar (planned)"
tokens:
  - "colors.surface.default"
  - "colors.surface.raised"
  - "colors.surface.floating"
  - "colors.content.primary"
  - "colors.content.secondary"
  - "colors.border.subtle"
  - "colors.border.default"
  - "colors.interaction.scrim"
  - "typography.titleLarge"
  - "typography.titleMedium"
  - "typography.labelMedium"
  - "motion.pressInSpring"
  - "motion.pressOutSpring"
  - "motion.stateChangeDurationMillis"
  - "motion.minPressDurationMillis"
---

# AuraTopAppBar & FloatingTopPillBar

> Canvas-first top navigation system combining an Adaptive Scroll-Responsive **Floating Top Pill Bar** (`FloatingTopPillBar` with `GlassStyle.FROSTED` / `GlassStyle.LIQUID`, Metaball capsule merging, and Layer 3 Morphing `[More]` Popover) with standardized **Floating Capsule / Split / Docked** headers (`AuraTopAppBar`).

---

## 1. Intent & Usage Principles

Traditional Material top bars span edge-to-edge as opaque rectangles, cutting off vertical canvas continuity. `AuraTopAppBar` applies the Aura **Floating Concept** (see [00-navigation-principles.md](00-navigation-principles.md)): navigation sits inside sculpted floating pills inset from screen edges and below status bars, allowing My Planner journals, My Finance charts, My Drink Diary tasting photos, and Home Launcher canvases to breathe beneath.

### Problem

Every screen needs a consistent place for its title, back navigation, and screen-level actions that adapts as the content scrolls without hiding the content behind it.

### When to Use

- Top-level navigation headers and detail screen bars in every Aura application and `:design-system-preview`.
- Media-forward detail headers (Drink Diary bottles, Planner daily reflections) needing minimal obstruction (`FloatingSplit`).
- Filterable dashboards combining title context with an inline `AuraChipButton` sub-ribbon.

### When NOT to Use & Alternatives

- Multi-destination bottom switching -> use `AuraNavigationBar` (planned).
- In-page section headers inside a scrollable body -> use `AuraText` typography tokens.
- Floating bottom contextual toolbars / composer bars -> use dedicated floating bottom bar patterns.

### Context Rules

- **`TBAR-RULE-001` (Style Selection by Canvas)**:
  - **`FloatingCapsule`**: Default across catalog, dashboard, diary, and analytics screens. Wraps navigation icon, title (+ optional subtitle), and up to `2–3` trailing actions in a unified pill container floating above scrolled content.
  - **`FloatingSplit`**: Detaches the leading back/title island from trailing `AuraIconButton` / `AuraChipButton` action islands so the upper-center viewport stays unobstructed over photography, wine labels, calendar hero art, or 3D launcher canvases.
  - **`SurfaceDocked`**: Classic full-bleed docked surface with a crisp bottom `1dp` divider; used strictly for dense tabular entry forms or split-pane secondary editors where floating elevation would clash with top-aligned input fields.
- **`TBAR-RULE-002` (Slot Purity)**: Leading navigation and trailing utility slots MUST be populated with `AuraIconButton` (`Ghost` inside `FloatingCapsule`/`SurfaceDocked`, `Floating` inside `FloatingSplit`) or compact `AuraChipButton` (`Filter`/`Glass`/`Assist`) — never raw `Material3 IconButton` or oversized `AuraButton`s.
- **`TBAR-RULE-003` (Underlap Scroll Scrim & Content Padding)**: Screens using floating bars MUST pass `AuraTopAppBar` total top clearance (status bar inset + floating outer margin + capsule height + optional chip ribbon) into content top padding so initial items align cleanly below the bar, while scrolled items glide visibly underneath.
- **`TBAR-RULE-004` (Single-Screen Chrome Budget)**: Never stack two full floating bars vertically. When quick filtering belongs to the bar (e.g., My Finance `"Week | Month | Year"` or Catalog category strip), render an integrated `subContent` ribbon of `AuraChipButton`s spaced `8dp` below the capsule (collapsing gracefully under `CompactOnScroll` / `EnterAlways`).
- **`TBAR-RULE-005` (Accessibility Contrast Guarantee)**: When `TranslucentGlass` is selected, text/icon contrast MUST hold `>= 4.5:1` / `>= 3:1` over scrolled high-variance photos by combining a subtle backdrop scrim (`color.interaction.scrim`) with an elevated frosted container tint; degrade automatically to `SolidElevated` under Reduced Transparency or unsupported GPU blur tiers.

### Do / Don't

| Do | Don't |
|---|---|
| Use `FloatingCapsule` on standard list/catalog/finance screens and `FloatingSplit` over photography or 3D canvases. | Stack opaque docked bars on top of dark/light edge-to-edge photos where floating pills preserve depth. |
| Compose leading/trailing slots exclusively from `AuraIconButton` and compact `AuraChipButton`. | Place wide `AuraButton` CTAs inside the top app bar trailing slot. |
| Provide top content padding equal to the resting bar footprint so the first list item never starts hidden beneath the capsule. | Overlap interactive first-row controls under the floating capsule at initial scroll position `0`. |
| Cap trailing visible actions at `2–3` and fold overflow items into a `More` icon menu. | Pack `4+` crowded icons into `FloatingCapsule`, truncating the screen title to 3 letters. |

---

## 2. Anatomy & Canonical Part IDs

```text
  FloatingCapsule:
  ╭────────────────────────────────────────────────────────╮
  │ [←]   Title                              [⌕] [⋯]       │
  │       Subtitle                                         │
  ╰────────────────────────────────────────────────────────╯
   navigationIcon   title / subtitle            actions
  [ subContent ribbon (optional chips) ]
```

| Part ID | Responsibility | Required | Semantics Owner |
|---|---|---|---|
| `root` | Bar container (capsule, split islands, or docked surface by `style`) | Yes | Inherited |
| `navigationIcon` | Leading slot (usually back `AuraIconButton`) | No | Child `Role.Button` |
| `title` | Primary bar text (`titleMedium` / `titleLarge`) | Yes | `heading()` |
| `subtitle` | Optional single-line secondary text | No | Merged with `title` |
| `actions` | Trailing `RowScope` action cluster | No | Child semantics |
| `subContent` | Optional ribbon below the bar (e.g. `AuraChipButton` filters) | No | Child semantics |
| `expandedTitle` | Large `headlineMedium` title shown in `DockedExpanded` (`AdaptiveFloating`) | No | `heading()` |

---

## 3. Variant, Size & Material Matrix

### Layout Styles (`AuraTopAppBarStyle`)

1. **`FloatingCapsule`**
   - Continuous pill (`CircleShape` / `28dp` radius), `56dp` height (`48dp` compact), horizontal outer margin `16dp` (`Compact`) / `24dp` (`Expanded`), `8dp` clearance below `statusBars` inset.
   - Leading slot (`AuraIconButton`), center/start-aligned title (`typography.titleMedium` / `titleLarge`) + optional single-line subtitle (`typography.labelMedium`), trailing action cluster (`AuraIconButton` + optional 1 `AuraChipButton`).
2. **`FloatingSplit`**
   - Independent left island (Circular `AuraIconButton`, or compact Back + Title capsule) and right island (Floating `AuraIconButton` / `AuraChipButton` cluster), leaving the horizontal middle completely open to underlying artwork.
   - Every standalone island enforces `>= 48dp` height and matching radius/elevation.
3. **`SurfaceDocked`**
   - Full-width flush surface anchored to the top viewport edge with `statusBarsPadding`, `56dp` content row, and `1dp` `border.subtle` bottom rule once scrolled (`scrollOffset > 0`).

### Surface Materiality (`AuraTopAppBarSurface`)

- **`SolidElevated`**: `surface.floating` fill + `1dp` `border.subtle` hairline + `8dp` ambient shadow (`0dp` flat blend on `SurfaceDocked` when unscrolled). Default fallback across all API levels.
- **`TranslucentGlass` (`GlassStyle.FROSTED` / `GlassStyle.LIQUID`)**:
  - **Layer 0 -> Layer 1 -> Layer 2 Spatial Pass-Through:** Scrolls live Layer 0 canvas content directly behind the floating capsules through a progressive Layer 1 top edge scrim (`interaction.scrim`).
  - **`GlassStyle.FROSTED`:** `28dp` uniform backdrop blur + `1.3x` saturation boost (API 31+), Light `White 72%` / Dark `#1E1E22 68%` tint, `1dp` diagonal specular stroke (`White 55% -> 8%`), dual shadow (`Y-offset 10dp, Blur 24dp, Alpha 15%`).
  - **`GlassStyle.LIQUID`:** `8dp` convex edge bevel AGSL `RuntimeShader` refraction + `~1.2dp` chromatic dispersion + `6dp` Fresnel inner glow (`White 35%`) over a soft Safe-Zone blur (`>= 48dp` touch target on `40×40dp` Back & `42dp` grouped right action pill), plus viscous Metaball merging/splitting (`metaballSplitProgress`) and spring-driven Layer 3 **Morphing Popover** for `[More]`.
  - Falls back cleanly without crash on API levels without AGSL (`RuntimeShader`) or hardware backdrop blur.

### Scroll Behaviors (`AuraTopAppBarScrollBehavior`)

- **`Pinned`**: Stays anchored at top while content scrolls underneath; transitions resting separation (`SolidElevated` border/shadow opacity and blur intensity) smoothly as `firstVisibleItemScrollOffset > 0`.
- **`EnterAlways`**: Translates upward smoothly into negative offset (clipped safely below system status bar region or faded cleanly) on upward scroll, snapping right back into view on downward scroll.
- **`CompactOnScroll`**: Starts expanded (`titleLarge`, optional rich subtitle or attached `subContent` `AuraChipButton` ribbon) and compresses smoothly toward a compact `48dp` capsule (`titleMedium`, condensed actions) as scroll progresses.
- **`AdaptiveFloating`** *(One UI State-Driven Split-Pill Morphing)*:
  - **`DockedExpanded` (`Y == 0dp` or `maxScrollOffset < 160dp`)**: Full-width (`0dp` margin, `0dp` radius), flat background, expanded Viewing Area header (`headlineMedium`), flat Ghost buttons, blur off, elevation `0dp`.
  - **`CollapsingTransition` (`0dp < Y < 160dp`)**: Fades out the expanded title (`alpha 1 -> 0`) and collapses the title area (`height -> 0dp`), leaving the upper-center viewport completely open while leading (`navigationIcon`) and trailing (`actions`) `AuraIconButton` controls individually morph into circular floating glass pills (`CircleShape`), snapping at `50%` on release (`FAB-06`).
  - **`FloatingVisible` / `FloatingPinned` (`Y >= 160dp`, `ΔY < -12dp` or `isFocused || isMenuOpen`)**: Title remains hidden and center remains unobstructed; leading/trailing action buttons float as individual circular pods (`26dp` backdrop blur, `1.2x` saturation boost, `surface.floatingGlass`, `border.glassSpecular`, `elevation.level2`).
  - **`FloatingHidden` (`Y >= 160dp`, `ΔY > +24dp`, jitter `< 12dp` ignored)**: Floating button pods slide upward off-screen (`180ms`, `CubicBezierEasing(0.4f, 0.0f, 1.0f, 1f)`) and reappear (`250ms`, `CubicBezierEasing(0.22f, 1.0f, 0.36f, 1f)`) in the same frame as `FloatingBottomIsland` when coordinated via `FloatingGlassScaffold`.
  - **`CompactDocked` (`IME Visible` or `heightDp < 480dp`)**: Restores a compact `48dp` docked bar with visible title and flat actions (`0dp` floating margins).
  - **Inline expanded title (`inlineExpandedTitle = true`, since 1.2.0)**: The `DockedExpanded` title (`headlineMedium`) and `expandedSubtitle` sit on the action row itself — title at the start, `actions` at the end, vertically centred — instead of in a second row below it. Use it for top-level screens with few or no actions, where the default layout leaves an empty `56dp` action row above the title. The title still fades as the bar collapses, and the floating title pill still appears in minimise mode.

---

## 4. State Precedence & Behavior

1. **Scrolled / Elevated (`scrollFraction > 0f`)**: Stronger shadow/border definition (or frost blur in `TranslucentGlass`) and compacted header height under `CompactOnScroll`.
2. **Collapsed / Hidden (`EnterAlways` hidden)**: Translated cleanly out of content lane while preserving layout stability; returns on minimal downward scroll delta.
3. **Modal / Menu Open**: Associated overflow menu or sheet locks bar visibility (`Pinned`) and suppresses auto-collapse.
4. **Resting (`scrollFraction == 0f`)**: Maximum transparency / minimal shadow weight so content canvas feels unified with the bar.

---

## 5. Semantic Token Bindings

| Element | `FloatingCapsule` / `FloatingSplit` (`SolidElevated`) | `TranslucentGlass` Mode | `SurfaceDocked` |
|---|---|---|---|
| Container | `surface.floating` | `surface.floating @ 0.78 / 0.72 alpha` + blur | `surface.default` (unscrolled) -> `surface.raised` (scrolled) |
| Border / Divider | `1dp` `border.subtle` | `1dp` luminous `border.subtle` | Bottom hairline `border.subtle` |
| Title Text | `content.primary` (`typography.titleMedium` / `titleLarge`) | `content.primary` | `content.primary` |
| Subtitle Text | `content.secondary` (`typography.labelMedium`) | `content.secondary` | `content.secondary` |
| Navigation & Actions | `AuraIconButton` (`Ghost` inside capsule, `Floating` in split) | `AuraIconButton` (`Ghost` / `Floating`) | `AuraIconButton` (`Ghost`) |
| Sub-ribbon Chips | `AuraChipButton` (`Filter` / `Tag`) | `AuraChipButton` (`Glass`) | `AuraChipButton` (`Filter`) |
| Underlap Edge Scrim | Optional `0.06f` fade | `interaction.scrim` progressive top fade | None |

---

## 6. Interaction Motion Contract

- **Collapse / Reveal (`EnterAlways` & `CompactOnScroll`)**: Scroll-linked smoothing and the `EnterAlways` slide use `AuraTheme.motion.spatialSpring()` (`0.15%` overshoot), so partial collapses settle onto `0%` or `100%` without sticking halfway or bouncing. Glass pills compress to `glassPressedScale` via `fluidPressScale`.
- **Surface Material Morph**: Transition between resting and scrolled elevation/border/frost uses `tween(AuraTheme.motion.stateChangeDurationMillis)`.
- **Reduced Motion (`ANIMATOR_DURATION_SCALE == 0`)**: Replaces sliding translation and height morphing with immediate visibility / opacity thresholds and crisp elevation border toggles.

---

## 7. Responsive Layout Behavior (`Smallest Width × Display Ratio`)

| Profile | Width / Ratio Condition | Floating Geometry Adaptation |
|---|---|---|
| `ExtraSmall` | `sw < 360dp` | `FloatingCapsule` uses `12dp` horizontal margin, `48dp` capsule height, max `1` visible action + overflow `AuraIconButton`. Avoid `FloatingSplit` unless title is omitted (icon-only islands). |
| `Compact` | `360–599dp` | `FloatingCapsule` (`16dp` margin, `56dp` height) or `FloatingSplit` for media headers; max `2–3` actions. Sub-ribbon chips scroll horizontally (`LazyRow`). |
| `Medium · Square` | `600–839dp`, `ratio < 1.35` | Constrain `FloatingCapsule` `maxWidth = 680.dp` (centered) or use `FloatingSplit` with `20dp` outer margins so the pill maintains sculptural proportions across a square foldable. |
| `Medium · UltraWide` | `ratio >= 2.1` | Switch to compact height (`48dp` capsule) and prefer `FloatingSplit` or `EnterAlways` scroll collapse so shallow vertical windows (`~350–420dp` tall) retain comfortable reading area. |
| `Expanded` | `width >= 840dp` | Pane-aware integration with `AuraAdaptiveTwoPane`: avoid spanning a single floating pill across both panes; cap standalone bar at `AuraLayout.maxContentWidth` (`24dp` outer margin) or mount pane-local `FloatingSplit` islands. |

---

## 8. Accessibility & Guardrails

- **Heading Landmark**: Title node MUST expose `semantics { heading() }` so screen-reader navigation jumps straight to screen headings inside floating pills.
- **Touch Bounds**: Both `FloatingCapsule` slot buttons and detached `FloatingSplit` islands enforce `>= 48dp × 48dp` hit targets; `FloatingSplit` maintains at least `16dp` unobstructed center clearance.
- **Focus Order**: Keyboard `Tab` traverses Leading Navigation -> Trailing Actions -> Optional Sub-ribbon `AuraChipButton`s -> Main Screen Content (skipping scrolled-underneath obscured items outside the visible viewport).
- **Reduced Transparency & High Contrast**: Automatically bypasses translucent blur layers in favor of opaque `SolidElevated` surfaces (`>= 7:1` headline contrast when High Contrast is active).

---

## 9. Platform Bindings

### 9.1 Android (Jetpack Compose)

- **Source:** [`AuraTopAppBar.kt`](../../../design-system/src/main/kotlin/com/example/ds/component/AuraTopAppBar.kt)

<!-- API:ANDROID:BEGIN -->
```kotlin
enum class AuraTopAppBarStyle {
    FloatingCapsule,
    FloatingSplit,
    SurfaceDocked,
}

enum class AuraTopAppBarSurface {
    SolidElevated,
    TranslucentGlass,
}

enum class AuraTopAppBarScrollBehavior {
    Pinned,
    EnterAlways,
    CompactOnScroll,
    AdaptiveFloating,
}

enum class AdaptiveFloatingBarPhase {
    DockedExpanded,
    CollapsingTransition,
    FloatingVisible,
    FloatingHidden,
    FloatingPinned,
    CompactDocked,
}

// rememberAuraAdaptiveBarState(), AuraAdaptiveBarState, and BindAdaptiveBarTo*():
// see aura-adaptive-bar-state.md

@Composable
fun FloatingTopPillBar(
    title: String,
    scrollProgress: Float,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    glassStyle: GlassStyle = GlassStyle.LIQUID,
    isScrollingDown: Boolean = false,
    hideTitleOnScrollDown: Boolean = true,
    metaballSplitProgress: Float = 0f,
    onBackClick: (() -> Unit)? = null,
    onSearchClick: (() -> Unit)? = null,
    isBookmarked: Boolean = false,
    onBookmarkClick: (() -> Unit)? = null,
    moreMenuExpanded: Boolean = false,
    onMoreMenuExpandedChange: (Boolean) -> Unit = {},
)

@Composable
fun AuraTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    style: AuraTopAppBarStyle = AuraTopAppBarStyle.FloatingCapsule,
    surface: AuraTopAppBarSurface = AuraTopAppBarSurface.SolidElevated,
    scrollBehavior: AuraTopAppBarScrollBehavior = AuraTopAppBarScrollBehavior.Pinned,
    scrollFraction: Float = 0f,
    centerTitle: Boolean = false,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    subContent: (@Composable () -> Unit)? = null,
    adaptiveState: AuraAdaptiveBarState? = LocalAuraAdaptiveBarState.current,
    expandedTitle: String? = null,
    expandedSubtitle: String? = null,
    inlineExpandedTitle: Boolean = false,
)
```
<!-- API:ANDROID:END -->

### 9.2 iOS (SwiftUI Reference)

<!-- API:IOS:BEGIN -->
```swift
public enum AuraTopAppBarStyle {
    case floatingCapsule, floatingSplit, surfaceDocked
}

public enum AuraTopAppBarSurface {
    case solidElevated, translucentGlass
}

public enum AuraTopAppBarScrollBehavior {
    case pinned, enterAlways, compactOnScroll
}

public struct AuraTopAppBar<NavigationIcon: View, Actions: View, SubContent: View>: View {
    public init(
        title: String,
        subtitle: String? = nil,
        style: AuraTopAppBarStyle = .floatingCapsule,
        surface: AuraTopAppBarSurface = .solidElevated,
        scrollBehavior: AuraTopAppBarScrollBehavior = .pinned,
        scrollFraction: CGFloat = 0,
        centerTitle: Bool = false,
        @ViewBuilder navigationIcon: () -> NavigationIcon,
        @ViewBuilder actions: () -> Actions,
        @ViewBuilder subContent: () -> SubContent
    )
}
```
<!-- API:IOS:END -->

### 9.3 Web (React / TypeScript Reference)

<!-- API:WEB:BEGIN -->
```ts
export type AuraTopAppBarStyle = 'floatingCapsule' | 'floatingSplit' | 'surfaceDocked';
export type AuraTopAppBarSurface = 'solidElevated' | 'translucentGlass';
export type AuraTopAppBarScrollBehavior = 'pinned' | 'enterAlways' | 'compactOnScroll';

export interface AuraTopAppBarProps {
  title: string;
  subtitle?: string;
  style?: AuraTopAppBarStyle;
  surface?: AuraTopAppBarSurface;
  scrollBehavior?: AuraTopAppBarScrollBehavior;
  scrollFraction?: number;
  centerTitle?: boolean;
  navigationIcon?: React.ReactNode;
  actions?: React.ReactNode;
  subContent?: React.ReactNode;
}
```
<!-- API:WEB:END -->
