---
id: "aura.component.floating-glass-scaffold"
name: "FloatingGlassScaffold"
category: "layout"
status: "ready"
version: "0.1.0"
layer: "canvas"
material_allowed: ["Solid"]
motion_track: "none"
platforms:
  android: "com.example.ds.component.FloatingGlassScaffold"
  ios: "AuraDS.FloatingGlassScaffold (planned)"
  web: "@aura-ds/react.FloatingGlassScaffold (planned)"
tokens:
  - "colors.background.primary"
  - "colors.content.primary"
  - "colors.interaction.scrim"
  - "elevation.level1"
  - "elevation.level3"
  - "typography.bodyMedium"
  - "window.heightClass"
---

# FloatingGlassScaffold

> Cross-platform specification for Aura's edge-to-edge spatial scaffold. Content scrolls full-bleed under floating glass chrome, which samples it through a shared backdrop. Counterpart of [`AuraScaffold`](aura-scaffold.md) for floating navigation. See [06-materiality-and-depth.md](../../foundation/06-materiality-and-depth.md).

---

## 1. Intent & Usage Principles

### Problem

Floating top pills and bottom islands only look like glass when real content passes behind them. That requires (a) a full-bleed scrolling canvas recorded as a backdrop, (b) contrast scrims at the edges, (c) content padding that clears the floating chrome, and (d) one owner for synchronized show/hide. `AuraScaffold` stacks bars beside content and cannot provide any of these.

### When to Use

- Feed, gallery, journal, and dashboard screens using floating `AuraTopAppBar` (`FloatingCapsule` / `FloatingSplit`) and/or `AuraBottomIsland`.
- Screens using `AuraTopAppBarScrollBehavior.AdaptiveFloating` (pass `adaptiveBarState`).
- Screens with a morphing overlay (e.g., `MorphingMorePopover`) that needs tap-to-dismiss.

### When NOT to Use & Alternatives

| Situation | Alternative | Reason |
|---|---|---|
| Dense forms, settings, spreadsheets with docked bars | `AuraScaffold` | `NAV-RULE-001`: docked chrome for structured rows; no blur cost |
| Two-pane tablet layout with pane-scoped chrome | `AuraScaffold` + `AuraAdaptiveTwoPane` | `NAV-RULE-004` |
| Modal flows | `AuraDialog` | Spatial layer, not a screen root |

### Context Rules

- **`FGS-RULE-001` (Single Backdrop Owner):** One `FloatingGlassScaffold` per screen. It records Layer 0 via `glassBackdropSource` and provides `LocalGlassBackdropState`; nested scaffolds would record competing backdrops.
- **`FGS-RULE-002` (Apply `contentPadding`):** Scrollable content must apply the `PaddingValues` passed to `content` (top clearance + bottom island clearance) so the first and last items are never hidden behind chrome.
- **`FGS-RULE-003` (Synchronized Chrome):** When `adaptiveBarState` is provided, the scaffold installs its `nestedScrollConnection`, provides `LocalAuraAdaptiveBarState`, and moves the bottom bar with `hideFraction` in the same frame as the top bar.
- **`FGS-RULE-004` (Overlay Pins Chrome):** `overlayVisible = true` pins the adaptive bars (`updatePinnedFlags(menuOpen = true)`) and shows a scrim that calls `onDismissOverlay` on tap.

### Do / Don't

| Do | Don't |
|---|---|
| `LazyColumn(contentPadding = padding)` with the `padding` passed to `content`. | Wrap content in `Modifier.padding(top = 64.dp)`, which cuts the canvas and leaves nothing behind the glass. |
| Use `rememberFloatingScrollProgress(listState)` to drive custom chrome and keep the backdrop fresh. | Use `FloatingGlassScaffold` for a settings form where nothing scrolls under the bars. |
| Pass `hasBottomIsland = false` on screens without an island so bottom padding falls back to nav-bar inset + `16dp`. | Place a second `FloatingGlassScaffold` inside a pane or dialog. |

---

## 2. Anatomy & Canonical Part IDs

```text
root (Box: containerColor, nestedScroll(adaptiveBarState))
 ├── Layer 0  canvas       content(contentPadding) recorded by glassBackdropSource
 ├── Layer 1  edgeScrims   top + bottom gradient scrims (elevation.level1 / level3 scrimAlpha)
 ├── Layer 2  chrome       topBar(backdropState) · bottomBar(backdropState) · snackbarHost
 └── Layer 3  overlay      tap-to-dismiss scrim (overlayVisible) · overlay(backdropState)
```

| Part ID | Responsibility | Required |
|---|---|---|
| `canvas` | Full-bleed content, recorded as the glass backdrop | Yes |
| `edgeScrims` | Contrast protection behind chrome; top scrim fades with morph/hide, bottom scrim with hide | Auto |
| `topBar` / `bottomBar` | Floating chrome slots receiving `GlassBackdropState` | No |
| `snackbarHost` | Bottom-aligned, offset above the visible island and nav bar | No |
| `overlay` | Popover / menu layer above a dismiss scrim | No |

---

## 3. Variant, Size & Material Matrix

| Edge | Formula |
|---|---|
| Top | `statusBars + topBarClearance` (`topBarClearance` default: `64dp`; with `adaptiveBarState`: `112dp`, or `48dp` when `window.heightClass == Compact`) |
| Bottom (island) | `calculateBottomIslandContentPadding(navBar, bottomIslandHeight, bottomIslandMargin, 16dp)` = `navBar + islandHeight + margin + 16dp` |
| Bottom (no island) | `navBar + 16dp` |

---

## 4. State Precedence & Behavior

Passive container: no pointer states of its own. Layer visibility resolves as `overlayVisible` (dismiss scrim + `overlay` on top) > chrome driven by `adaptiveBarState` (`hideFraction` / `morphFraction`) > static chrome.

---

## 5. Semantic Token Bindings

| Part | Token Binding |
|---|---|
| `root` background | `AuraTheme.colors.background.primary` (`containerColor`) |
| Inherited content color / text style | `AuraTheme.colors.content.primary`, `AuraTheme.typography.bodyMedium` via `ProvideAuraContentColorTextStyle` |
| `edgeScrims` base | `lerp(background.primary, interaction.scrim, 0.22)` |
| `edgeScrims` alpha ladder | `AuraTheme.elevation.level3.scrimAlpha` → `level1.scrimAlpha` → transparent |
| `overlay` dismiss scrim | `interaction.scrim` × `elevation.level1.scrimAlpha` |
| Compact clearance switch | `AuraTheme.window.heightClass` |

---

## 6. Interaction Motion Contract

- **Passive container (`motion_track: "none"`):** The scaffold owns no timeline. Bottom chrome translation and scrim alpha follow `AuraAdaptiveBarState.hideFraction` / `morphFraction`, which are animated by the state with `AuraTheme.motion` tokens (see [aura-adaptive-bar-state.md](../navigation/aura-adaptive-bar-state.md)).
- **Reduced Motion:** Inherited from `AuraAdaptiveBarState` (snaps instead of animating).

---

## 7. Responsive Layout Behavior (`Smallest Width × Display Ratio`)

| Profile | Behavior |
|---|---|
| **`ExtraSmall`** (Cover `sw < 360dp`) | Same 4-layer model; chrome components shrink, scrims keep contrast |
| **`Compact`** (Phone Portrait) | Default clearances (`64dp` / `112dp` top, island bottom padding) |
| **`Medium` / `Square`** (Foldable `~1:1`) | Canvas stays full-bleed; floating chrome caps its own width so content remains visible at the sides |
| **`UltraWide`** (Phone Landscape, short height) | `heightClass == Compact` → `48dp` top clearance; adaptive bars prefer `CompactDocked` |
| **`Expanded`** (Tablet / DeX) | Prefer pane-scoped chrome (`NAV-RULE-004`); use `AuraScaffold` + `AuraAdaptiveTwoPane` when panes need separate bars |

---

## 8. Accessibility & Guardrails

- Focus and TalkBack order follow draw order: content, then top chrome, bottom chrome, snackbar, overlay.
- Edge scrims protect `>= 4.5:1` text contrast on glass chrome over busy content (`VIS-RULE-003`).
- When `overlayVisible`, the dismiss scrim consumes taps outside the overlay; the overlay component must expose its own dismiss action for screen readers.
- `contentPadding` keeps the last list item reachable above the bottom island and gesture navigation bar.

---

## 9. Platform Bindings

### 9.1 Android (Jetpack Compose)

- **Source:** [`FloatingGlassScaffold.kt`](../../../design-system/src/main/kotlin/com/example/ds/component/FloatingGlassScaffold.kt)

<!-- API:ANDROID:BEGIN -->
```kotlin
@Composable
fun FloatingGlassScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable BoxScope.(GlassBackdropState) -> Unit = {},
    bottomBar: @Composable BoxScope.(GlassBackdropState) -> Unit = {},
    overlay: @Composable BoxScope.(GlassBackdropState) -> Unit = {},
    overlayVisible: Boolean = false,
    onDismissOverlay: () -> Unit = {},
    hasBottomIsland: Boolean = true,
    bottomIslandHeight: Dp = 60.dp,
    bottomIslandMargin: Dp = 16.dp,
    snackbarHost: @Composable () -> Unit = {},
    containerColor: Color = AuraTheme.colors.background.primary,
    contentColor: Color = AuraTheme.colors.content.primary,
    adaptiveBarState: AuraAdaptiveBarState? = null,
    topBarClearance: Dp = if (adaptiveBarState != null) {
        if (AuraTheme.window.heightClass == AuraHeightClass.Compact) 48.dp else 112.dp
    } else {
        64.dp
    },
    content: @Composable (PaddingValues) -> Unit,
)

fun calculateBottomIslandContentPadding(
    navBarBottomInset: Dp,
    islandHeight: Dp = 60.dp,
    bottomMargin: Dp = 16.dp,
    extraBreathing: Dp = 16.dp,
): Dp

@Composable
fun rememberFloatingScrollProgress(
    listState: LazyListState,
    threshold: Dp = 64.dp,
): Float

@Composable
fun rememberFloatingScrollProgress(
    scrollState: ScrollState,
    threshold: Dp = 64.dp,
): Float
```
<!-- API:ANDROID:END -->

### 9.2 iOS (SwiftUI Reference)

<!-- API:IOS:BEGIN -->
```swift
struct FloatingGlassScaffold<Content: View, Top: View, Bottom: View>: View {
    var hasBottomIsland: Bool = true
    var adaptiveBarState: AuraAdaptiveBarState? = nil
    @ViewBuilder var topBar: () -> Top
    @ViewBuilder var bottomBar: () -> Bottom
    @ViewBuilder var content: (EdgeInsets) -> Content
}
```
<!-- API:IOS:END -->

### 9.3 Web (React / TypeScript Reference)

<!-- API:WEB:BEGIN -->
```tsx
export interface FloatingGlassScaffoldProps {
  topBar?: React.ReactNode;
  bottomBar?: React.ReactNode;
  overlay?: React.ReactNode;
  overlayVisible?: boolean;
  onDismissOverlay?: () => void;
  hasBottomIsland?: boolean;
  children: (contentPadding: { top: number; bottom: number }) => React.ReactNode;
}
```
<!-- API:WEB:END -->
