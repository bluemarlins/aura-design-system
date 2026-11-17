---
component_id: "aura.component.adaptive-bar-state"
name: "AuraAdaptiveBarState"
category: "navigation"
status: "ready"
layer: "chrome"
material_allowed: ["Solid", "Glass", "LiquidGlass"]
motion_track: "Standard"
version: "0.1.0"
platforms:
  android: "com.example.ds.component.AuraAdaptiveBarState"
  ios: "AuraDS.AuraAdaptiveBarState (planned, @Observable)"
  web: "@aura-ds/react.useAuraAdaptiveBar (planned hook)"
tokens:
  - "motion.floatingBarAppearSpec"
  - "motion.floatingBarDisappearSpec"
  - "motion.adaptiveBarMorphSpring"
  - "window.heightDp"
---

# AuraAdaptiveBarState

> Cross-platform specification for the scroll-linked state coordinator behind `AuraTopAppBarScrollBehavior.AdaptiveFloating`. One state drives the top bar's dock → pill morph and the synchronized hide/reveal of the top bar and `FloatingBottomIsland`. See [aura-top-app-bar.md](aura-top-app-bar.md), [aura-bottom-island.md](aura-bottom-island.md), and [floating-glass-scaffold.md](../layout/floating-glass-scaffold.md).

---

## 1. Intent & Usage Principles

### Problem

When the top bar and bottom island each listen to scroll on their own, they hide on different frames, disagree about short content, and fight over IME or menu state. A single deterministic state machine keeps both chrome surfaces in the same phase.

### When to Use

- Any screen whose `AuraTopAppBar` uses `scrollBehavior = AuraTopAppBarScrollBehavior.AdaptiveFloating`.
- Screens where the top bar and `FloatingBottomIsland` must hide and reveal together.

### When NOT to Use & Alternatives

| Situation | Alternative | Reason |
|---|---|---|
| Bar should never move | `AuraTopAppBarScrollBehavior.Pinned` | No state needed |
| Simple shrink on scroll without hiding | `CompactOnScroll` / `EnterAlways` | Lighter behaviors handled inside `AuraTopAppBar` |
| Only a scroll progress value for custom chrome | `rememberFloatingScrollProgress` | Returns `[0f, 1f]` without phases |

### Context Rules

- **`ABS-RULE-001` (One State per Screen):** Create the state once with `rememberAuraAdaptiveBarState()` and pass it to `FloatingGlassScaffold(adaptiveBarState = …)`; chrome reads it from `LocalAuraAdaptiveBarState`.
- **`ABS-RULE-002` (Bind the Real Scroll Container):** Call exactly one `BindAdaptiveBarTo…` for the screen's primary scroll container so short-content detection (Edge Case 1) uses the true `maxScrollOffset`. Without a binding, the scaffold's nested-scroll deltas are the only input.
- **`ABS-RULE-003` (Pin on Focus or Menu):** Report search focus and open menus with `updatePinnedFlags(focused, menuOpen)`; pinned bars ignore scroll (`FloatingPinned`). `FloatingGlassScaffold` does this automatically for `overlayVisible`.
- **`ABS-RULE-004` (Reset on Destination Change):** Call `reset()` when the screen's content is replaced (tab switch, new query) so the bar returns to `DockedExpanded`.

### Do / Don't

| Do | Don't |
|---|---|
| `val barState = rememberAuraAdaptiveBarState()` + `BindAdaptiveBarToLazyListState(listState)` inside `FloatingGlassScaffold(adaptiveBarState = barState)`. | Create separate states for the top bar and the bottom island. |
| Let short lists stay `DockedExpanded` (no hide) automatically. | Force the floating pill on a screen whose content fits without scrolling. |
| Keep thresholds at defaults unless a content reason is documented. | Tune `hideScrollDelta` below `jitterSlop`, which makes the bars flicker. |

---

## 2. Phase Model (`AdaptiveFloatingBarPhase`)

| Phase | Entry Condition | Visual Result |
|---|---|---|
| `DockedExpanded` | `Y == 0` or content not scrollable (`maxScrollOffset < collapseThreshold`) | Full-width flat header with expanded title, no blur |
| `CollapsingTransition` | `0 < Y < 160px` | Title fades out and collapses; left/right action buttons morph into individual circular pills (`morphFraction = collapseProgress`) |
| `FloatingVisible` | `Y >= 160px` and scrolling up (`ΔY < -12px`) or settled | Title hidden, open center, individual floating glass icon pills |
| `FloatingHidden` | `Y >= 160px` and scrolling down (`ΔY > +24px`) | Top bar and bottom island slide out (`hideFraction → 1`) |
| `FloatingPinned` | `Y >= 160px` and (`isFocused || isMenuOpen`) | Locked visible individual floating pills |
| `CompactDocked` | `isImeVisible || windowHeightDp < 480` | Compact docked bar with compact title, no floating margin |

The phase enum is declared with the top bar API ([aura-top-app-bar.md](aura-top-app-bar.md) §8).

---

## 3. State Precedence

Phases resolve in this strict order (first match wins), implemented by `resolveAdaptiveFloatingBarPhase`:

$$\text{DockedExpanded (non-scrollable)} \succ \text{CompactDocked} \succ \text{DockedExpanded (}Y = 0\text{)} \succ \text{CollapsingTransition} \succ \text{FloatingPinned} \succ \text{jitter hold} \succ \text{FloatingHidden} \succ \text{FloatingVisible}$$

- *jitter hold*: when `|accumulated ΔY| < jitterSlop`, the current hidden/visible phase is kept.
- Drag or fling end snaps partial progress: `>= 0.5` completes to `1`, `< 0.5` returns to `0`.

---

## 4. Semantic Token Bindings

| Output | Token Binding |
|---|---|
| Hide (disappear) animation | `AuraTheme.motion.floatingBarDisappearSpec` |
| Reveal (appear) animation | `AuraTheme.motion.floatingBarAppearSpec` |
| Settled dock ↔ pill morph | `AuraTheme.motion.adaptiveBarMorphSpring` |
| Compact-height switch | `AuraTheme.window.heightDp` (< `480px`) |

Exposed read-only values: `phase`, `collapseProgress`, `morphFraction`, `hideFraction`, `isPinned`, `isScrollableContent`, `scrollOffsetPx`, `maxScrollOffsetPx`, `isFocused`, `isMenuOpen`, `isImeVisible`, `windowHeightDp`.

---

## 5. Interaction Motion Contract

- **Track (`Standard`):** Hide and reveal animate with `floatingBarDisappearSpec` / `floatingBarAppearSpec` (entrance longer than exit, `MOT-RULE-005`); settle morph uses `adaptiveBarMorphSpring`. Animations are interruptible and continue from the current value (`MOT-RULE-004`).
- **Jitter guard:** Direction changes smaller than `jitterSlop` (`12px`) are ignored.
- **Reduced Motion:** The injected `AuraTheme.motion` resolves its specs to immediate transitions, so phases switch without spatial travel while the final visibility stays correct (`MOT-RULE-007`).

---

## 6. Responsive Layout Behavior (`Smallest Width × Display Ratio`)

| Profile | Behavior |
|---|---|
| **`ExtraSmall`** (Cover `sw < 360dp`) | Same phases; tall cover screens keep floating behavior, short ones fall to `CompactDocked` |
| **`Compact`** (Phone Portrait) | Full phase model with default thresholds |
| **`Medium` / `Square`** (Foldable `~1:1`) | Full phase model; floating pill width is capped by the top bar (`640px`) |
| **`UltraWide`** (Phone Landscape) | `windowHeightDp < 480` → `CompactDocked`, preserving vertical space |
| **`Expanded`** (Tablet / DeX) | Full phase model; with two panes prefer pane-scoped chrome (`NAV-RULE-004`) |

---

## 7. Accessibility

- `FloatingPinned` keeps chrome visible while search has focus or a menu is open, so focused controls never slide off-screen.
- IME visibility forces `CompactDocked`, keeping the focused field and bar on screen together.
- Hidden chrome is restored on any upward scroll beyond `12px`; screen-reader users navigating by focus trigger scroll-driven reveal like touch users.
- The state exposes no semantics itself; the bars keep their own roles and labels.

---

## 8. Platform Bindings (Layer B)

### 8.1 Android (Jetpack Compose)

<!-- API:ANDROID:BEGIN -->
```kotlin
val LocalAuraAdaptiveBarState: ProvidableCompositionLocal<AuraAdaptiveBarState?>

@Composable
fun rememberAuraAdaptiveBarState(
    collapseThreshold: Dp = AdaptiveBarThresholds.CollapseThreshold,
    hideScrollDelta: Dp = AdaptiveBarThresholds.HideScrollDelta,
    revealScrollDelta: Dp = AdaptiveBarThresholds.RevealScrollDelta,
    jitterSlop: Dp = AdaptiveBarThresholds.JitterSlop,
): AuraAdaptiveBarState

@Stable
class AuraAdaptiveBarState {
    val phase: AdaptiveFloatingBarPhase
    val collapseProgress: Float
    val morphFraction: Float
    val hideFraction: Float
    val isPinned: Boolean
    val isScrollableContent: Boolean
    val nestedScrollConnection: NestedScrollConnection

    fun updatePinnedFlags(
        focused: Boolean = this.isFocused,
        menuOpen: Boolean = this.isMenuOpen,
    )

    fun updateScrollBounds(
        scrollOffsetPx: Float,
        maxScrollOffsetPx: Float = this.maxScrollOffsetPx,
    )

    fun reset()
}

@Composable
fun BindAdaptiveBarToScrollState(
    scrollState: ScrollState,
    adaptiveState: AuraAdaptiveBarState? = LocalAuraAdaptiveBarState.current,
)

@Composable
fun BindAdaptiveBarToLazyListState(
    listState: LazyListState,
    adaptiveState: AuraAdaptiveBarState? = LocalAuraAdaptiveBarState.current,
)

@Composable
fun BindAdaptiveBarToLazyGridState(
    gridState: LazyGridState,
    adaptiveState: AuraAdaptiveBarState? = LocalAuraAdaptiveBarState.current,
)
```
<!-- API:ANDROID:END -->

### 8.2 iOS (SwiftUI Reference)

<!-- API:IOS:BEGIN -->
```swift
@Observable
final class AuraAdaptiveBarState {
    private(set) var phase: AdaptiveFloatingBarPhase
    private(set) var morphFraction: CGFloat
    private(set) var hideFraction: CGFloat
    func updatePinnedFlags(focused: Bool? = nil, menuOpen: Bool? = nil)
    func reset()
}
```
<!-- API:IOS:END -->

### 8.3 Web (React / TypeScript Reference)

<!-- API:WEB:BEGIN -->
```tsx
export function useAuraAdaptiveBar(
  scrollRef: React.RefObject<HTMLElement>,
  options?: { collapseThreshold?: number; hideScrollDelta?: number; revealScrollDelta?: number },
): {
  phase: AdaptiveFloatingBarPhase;
  morphFraction: number;
  hideFraction: number;
  updatePinnedFlags(flags: { focused?: boolean; menuOpen?: boolean }): void;
  reset(): void;
};
```
<!-- API:WEB:END -->
