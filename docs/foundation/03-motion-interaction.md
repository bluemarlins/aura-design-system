# 03 · Interaction Motion

> **Aura-Original Motion System:** `Standard` and `Expressive` motion profiles (`AuraMotionProfile`) and `AuraMotion` physics tokens are **Aura-original concepts** defined specifically for Aura Design System. They are **not** Android Material Design (`material3`) motion tokens or schemes.

> **Status legend:** **Shipped** = present in [`AuraMotion.kt`](../../design-system/src/main/kotlin/com/example/ds/foundation/motion/AuraMotion.kt) today. **Planned** = adopted by this guide but not yet in code; see §11 for the migration backlog. Until a Planned item ships, components keep their current behavior.

## 1. Purpose & Scope

Defines Aura's **physical motion model**, the **Tier 2 semantic motion tokens (`AuraMotion`)**, the dual-track profiles (`Standard` / `Expressive`), and how those tokens map to every **press interaction** and **state/spatial transition** in the system. The goal is one tactile signature with differences that come from each component's role, visual weight, and frequency of use, never from one-off values.

### 1.1 Core Motion Rules (`MOT-RULE-001`–`MOT-RULE-010`)

| Rule ID | Principle | Enforcement Requirement |
|---|---|---|
| **`MOT-RULE-001`** | **Purposeful Motion** | Every animation must map to `Orient`, `Focus`, `Express`, or `Connect`. Decorative motion that fits none of these is removed. |
| **`MOT-RULE-002`** | **Tokens Only** | Zero numeric `tween(...)`, `spring(...)`, or duration literals inside component production code; consume `AuraTheme.motion`. |
| **`MOT-RULE-003`** | **Dual-Track Separation** | `Standard` governs routine tasks, inputs, and high-frequency actions (`0–3%` overshoot). `Expressive` governs spatial entrances, navigation chrome, and focal moments (`<= 1` Expressive hero per screen). |
| **`MOT-RULE-004`** | **Interruptible Springs** | Gesture and state-driven geometry uses velocity-preserving springs; reversing mid-flight must be continuous without jumps. |
| **`MOT-RULE-005`** | **Timing Asymmetry** | Entrances stay `30–50%` longer than exits (`enterDurationMillis > exitDurationMillis`), and press compression is faster than release (`pressInDurationMillis < pressOutDurationMillis`). |
| **`MOT-RULE-006`** | **Choreography Budget** | Elements adopt roles (`Outgoing`, `Incoming`, `Persistent`); stagger interval stays `20–100ms`, total stagger caps at `staggerBudgetMillis` (`400ms`), and `<= 1/3` of screen elements move simultaneously. |
| **`MOT-RULE-007`** | **Reduced Motion** | When `ANIMATOR_DURATION_SCALE == 0f` or `AuraMotion.reduceMotion == true`, spatial motion (scale/translation/parallax/stagger) resolves to rest while color, opacity, and loading semantics remain clear (§9). |
| **`MOT-RULE-008`** | **Overshoot Budget** *(new)* | Visible overshoot = travel distance × overshoot ratio. `Standard` ≤ `1dp`; `Expressive` ≤ `3dp`; any **exit** = `0`. |
| **`MOT-RULE-009`** | **Amplitude Tokens** *(new)* | Scale/stretch amplitudes (`0.94f`, `1.18f`, …) are tokens like durations and springs. No float literal as an animation `targetValue` in component code. |
| **`MOT-RULE-010`** | **Exit Parity** *(new)* | A component that animates its Show must animate its Hide. Removing an element from composition before its exit spec finishes is a defect. |

---

## 2. Design Philosophy: Tactile Spring Cushion over Material Ripple

Instead of Material Design's radial clipped ripple, Aura uses **Tactile Spring Compression + Smooth State-Layer Interpolation** (`indication = null` on every interactive Aura component):

1. **Harmonizes with `Solid` and `Glass` materials:** Radial ripples clip awkwardly on translucent `Glass` and `LiquidGlass` layers; GPU-accelerated scale compression and color tinting work across all material treatments.
2. **Unifies 4 application personalities:** A physical stamp/cushion feel in **My Planner**, crisp confirmation in **My Finance**, refined tactility in **My Drink Diary**, and responsive 3D elasticity in **Home Launcher**.
3. **Cross-platform portable:** Scale + spring physics + state-layer color interpolation translate to Jetpack Compose, SwiftUI, and Web React without platform-specific ripple shaders.

---

## 3. Physical Model

Every spatial token is a **damped harmonic spring with unit mass** (`m = 1`), described by damping ratio `ζ` and stiffness `k`. Colors and opacity use **duration + easing** instead, because a color cannot meaningfully overshoot.

| Quantity | Formula | Meaning |
|---|---|---|
| Natural frequency | `ω₀ = √k` | How fast the spring wants to move |
| Overshoot | `OS = e^(−ζπ / √(1−ζ²))` | Fraction of travel past the target |
| Settle time (±2%) | `Tₛ ≈ 4 / (ζ·ω₀)` | Approximate time until visually at rest |
| SwiftUI `response` | `2π / ω₀` | Used with `dampingFraction = ζ` |
| Web (Motion) `damping` | `c = 2ζ·ω₀` | Used with `stiffness = k`, `mass = 1` |

### 3.1 Physical Principles (`P1`–`P5`)

| ID | Principle | Quantified Rule |
|---|---|---|
| **P1 · Mass** | The heavier an element looks, the shallower it compresses. | Press depth by mass class: **Light** (≤ `40dp`, Ghost, Chip, Glass chrome) `0.95` · **Medium** `0.96` · **Heavy** (≥ `56dp`, `fullWidth`, Card) `0.975` |
| **P2 · Energy Conservation** | Geometry can be interrupted, so it must carry velocity; color cannot. | Geometry (scale, offset, size, index) → **spring**. Color, alpha, border color → **tween**. Never the reverse. |
| **P3 · Damping by Frequency** | The more often a control is used, the closer to critical damping it settles. | Used many times per session → `ζ >= 0.85` · Focal, about once per screen → `ζ = 0.75` · `LiquidGlass` only → `ζ = 0.58` |
| **P4 · Asymmetry** | Contact is fast, letting go is slower; arrivals decelerate, departures accelerate. | press-in < press-out; enter = exit × `1.3–1.5`; enter uses decelerate easing, exit uses accelerate easing |
| **P5 · Overshoot Budget** | Overshoot is measured in visible distance, not percent (`MOT-RULE-008`). | Press release on a `4%` scale delta × `6.8%` OS ≈ `0.27%` (allowed). Switch thumb `20dp` × `6.8%` ≈ `1.4dp` (not allowed in Standard). |

---

## 4. Token Ladder (`AuraMotion`)

Exposed via `AuraTheme.motion` (`AuraMotionDefaults.Standard`).

### 4.1 Spring Tokens

| Tier | Token | `ζ` / `k` | OS | `Tₛ` | Use | Status |
|---|---|---|---|---|---|---|
| **micro-snappy** | `pressInSpring()` | `0.85` / `800` | `0.6%` | `≈166ms` | Press compression | Shipped |
| | `pressOutSpring()` | `0.65` / `500` | `6.8%` | `≈275ms` | **Press-release scale return only** | Shipped |
| | `iconSpring()` | `0.75` / `600` | `2.8%` | `≈218ms` | Icon, glyph, dot accents (8–24dp elements) | Shipped |
| **standard-fluid** | `selectionSpring()` | `0.85` / `600` | `0.6%` | `≈192ms` | Selection travel: Switch/Segmented thumb, Slider settle, Badge presence | **Planned** |
| | `spatialSpring()` (Standard) | `0.90` / `400` | `0.15%` | `≈222ms` | Overlays, popovers, chrome travel, scroll-linked smoothing | Shipped |
| | `adaptiveBarMorphSpring()` | `0.85` / `380` | `0.6%` | `≈241ms` | Topology morphs (Docked ↔ Floating) | Shipped |
| **macro-expressive** | `spatialSpring()` (Expressive) | `0.75` / `350` | `2.8%` | `≈285ms` | Dialog, Bottom Island, FAB expand | Shipped |
| | `elasticStretchSpring()` | `0.58` / `320` | `10.7%` | `≈385ms` | **`LiquidGlass` material only**: island stretch, metaball split | Shipped |

### 4.2 Cross-Platform Spring Mapping

Derived with the formulas in §3. This replaces the earlier table, whose iOS `response` and Web `damping` values did not match the Compose springs.

| Token | Compose | SwiftUI | Web (Motion) |
|---|---|---|---|
| `pressInSpring` | `spring(0.85f, 800f)` | `.spring(response: 0.222, dampingFraction: 0.85)` | `{ stiffness: 800, damping: 48.1, mass: 1 }` |
| `pressOutSpring` | `spring(0.65f, 500f)` | `.spring(response: 0.281, dampingFraction: 0.65)` | `{ stiffness: 500, damping: 29.1, mass: 1 }` |
| `iconSpring` | `spring(0.75f, 600f)` | `.spring(response: 0.257, dampingFraction: 0.75)` | `{ stiffness: 600, damping: 36.7, mass: 1 }` |
| `selectionSpring` | `spring(0.85f, 600f)` | `.spring(response: 0.257, dampingFraction: 0.85)` | `{ stiffness: 600, damping: 41.6, mass: 1 }` |
| `spatialSpring` (Std) | `spring(0.90f, 400f)` | `.spring(response: 0.314, dampingFraction: 0.90)` | `{ stiffness: 400, damping: 36.0, mass: 1 }` |
| `spatialSpring` (Exp) | `spring(0.75f, 350f)` | `.spring(response: 0.336, dampingFraction: 0.75)` | `{ stiffness: 350, damping: 28.1, mass: 1 }` |
| `adaptiveBarMorphSpring` | `spring(0.85f, 380f)` | `.spring(response: 0.322, dampingFraction: 0.85)` | `{ stiffness: 380, damping: 33.1, mass: 1 }` |
| `elasticStretchSpring` | `spring(0.58f, 320f)` | `.spring(response: 0.351, dampingFraction: 0.58)` | `{ stiffness: 320, damping: 20.8, mass: 1 }` |

### 4.3 Easing Tokens

| Token | Curve | Use | Status |
|---|---|---|---|
| `easeStandard` | `cubic-bezier(0.4, 0, 0.2, 1)` (`FastOutSlowInEasing`) | Color, border, state tint, crossfade | Shipped (as `FastOutSlowInEasing`) |
| `easeDecelerate` | `cubic-bezier(0.22, 1, 0.36, 1)` | All entrances: `enterSpec()`, `floatingBarAppearSpec()` | Shipped in `floatingBarAppearSpec`; **Planned** for `enterSpec` |
| `easeAccelerate` | `cubic-bezier(0.4, 0, 1, 1)` | All exits: `exitSpec()`, `floatingBarDisappearSpec()` | Shipped in `floatingBarDisappearSpec`; **Planned** for `exitSpec` |
| `LinearEasing` | `linear` | Continuous loops only (spinner rotation, skeleton pulse) | Shipped |

### 4.4 Duration Tokens

| Token | `Standard` | `Expressive` | Easing | Use |
|---|---|---|---|---|
| `pressInDurationMillis` | `90` | `90` | `easeStandard` | Press tint in; focus border in |
| `loadingCrossfadeDurationMillis` | `120` | `120` | `easeStandard` | `Rest ↔ Loading` content swap; Transform/Morph content crossfade; Reduced Motion fade |
| `stateChangeDurationMillis` | `150` | `150` | `easeStandard` | Selected/checked/enabled color change; focus border out |
| `pressOutDurationMillis` | `180` | `180` | `easeStandard` | Press tint out |
| `scrimFadeDurationMillis` | `180` | `220` | `easeStandard` | Modal scrim fade |
| `exitDurationMillis` | `200` | `260` | `easeAccelerate` | Overlay/container exit |
| `floatingBarDisappearDurationMillis` | `180` | `180` | `easeAccelerate` | Floating bar hide |
| `floatingBarAppearDurationMillis` | `250` | `250` | `easeDecelerate` | Floating bar reveal |
| `enterDurationMillis` | `280` | `380` | `easeDecelerate` | Overlay/container entrance |
| `minPressDurationMillis` | `110` | `110` | — | Minimum visible press latch for quick taps |
| `spinnerSweepDurationMillis` | `650` | `650` | `FastOutSlowInEasing` (Reverse) | Spinner arc `45° ↔ 240°` |
| `spinnerRotationDurationMillis` | `900` | `900` | `LinearEasing` | Spinner rotation |
| `skeletonPulseDurationMillis` | `900` | `900` | `LinearEasing` (Reverse) | Skeleton half-cycle |

### 4.5 Amplitude Tokens (`MOT-RULE-009`)

| Token | Value | Applies to | Status |
|---|---|---|---|
| `pressedScaleGhost` | `0.95f` | Light mass: Ghost buttons, Tag chips, Snackbar action | Shipped |
| `pressedScaleDefault` | `0.96f` | Medium mass: default buttons, icon buttons, Switch track, Checkbox, Radio, segments | Shipped |
| `pressedScaleLarge` | `0.975f` | Heavy mass: `Large` / `fullWidth` buttons, extended FAB, Card | Shipped |
| `glassPressedScale` | `0.95f` | `fluidPressScale` on Glass chrome (Bottom Island, Top App Bar pill, Dropdown items) | **Planned** (replaces `0.92f` default and `0.96f`/`0.97f` overrides) |
| `leadingIconPressedScale` | `0.92f` | Leading/standalone icon compression | Shipped |
| `trailingIconNudge` | `2.dp` | Trailing icon forward nudge | Shipped |
| `grabScale` | `1.18f` | Switch thumb width while pressed; Slider thumb while dragging | **Planned** |
| `scrubScale` | `1.12f` | Rating star under the finger while scrubbing | **Planned** |
| `selectedIconScale` | `1.08f` | Selected Bottom Island tab icon | **Planned** |
| `presenceEnterScale` | `0.90f` | Badge appearing with content | **Planned** |
| `overlayEnterScale` / `overlayExitScale` | `0.94f` / `0.97f` | Dialog Show / Hide | **Planned** |
| `popoverEnterScale` | `0.92f` | Dropdown Show (anchored origin) | **Planned** |
| `loadingContentScale` / `spinnerEnterScale` | `0.92f` / `0.75f` | Button/Fab/IconButton loading swap | **Planned** |
| `gelStretchX` / `gelSquashY` / `pressGlowAlpha` | `0.14f` / `0.06f` / `0.24f` | Glass gel anisotropy and touch glow | **Planned** |

```kotlin
// Planned shape inside AuraMotion (Standard values; shared by both profiles)
val selectionDampingRatio: Float = 0.85f,
val selectionStiffness: Float = 600f,

fun <T> selectionSpring(): FiniteAnimationSpec<T> = if (reduceMotion) {
    snap()
} else {
    spring(dampingRatio = selectionDampingRatio, stiffness = selectionStiffness)
}
```

---

## 5. Dual-Track Profiles: `Standard` vs. `Expressive` (`MOT-RULE-003`)

Both tracks share the **in-place tactile tokens** (all of §4.1 micro-snappy, `selectionSpring`, §4.4 press/state durations, §4.5 amplitudes) and diverge on **spatial, choreographed, and hero surface transitions**:

| Token | `Standard` | `Expressive` | Usage Guidance |
|---|---|---|---|
| `enterDurationMillis` | `280` | `380` | Container / overlay entrance (`30–50%` longer than exit) |
| `exitDurationMillis` | `200` | `260` | Container / overlay dismissal |
| `spatialDampingRatio` | `0.90f` | `0.75f` | `Standard` ≈ `0.15%` OS; `Expressive` ≈ `2.8%` OS |
| `spatialStiffness` | `400f` | `350f` | Task cadence vs. unhurried spatial expansion |
| `elasticDampingRatio` | `0.58f` | `0.58f` | `LiquidGlass` stretch/squish recovery |
| `elasticStiffness` | `320f` | `320f` | Impulse rebound for liquid island and morph capsules |
| `staggerDelayMillis` | `40` | `60` | Per-item cascade delay (`staggerDelay(index)`) |
| `staggerBudgetMillis` | `400` | `400` | Maximum cumulative stagger |
| `scrimFadeDurationMillis` | `180` | `220` | Modal backdrop fade |

- **Global vs. scoped:** Keep `AuraTheme(motionProfile = AuraMotionProfile.Standard)` at the root for forms, lists, and productivity flows. `Expressive` is allowed **only on spatial/chrome layers** (Dialog, Dropdown, FAB, Bottom Island, future `AuraBottomSheet`), with at most **1 Expressive hero** per screen.
- **Track binding:** A component whose spec declares `motion_track: "Expressive"` must read its spatial and scrim tokens from the Expressive profile. Today these components read `AuraTheme.motion` and only become Expressive inside a nested `AuraTheme`; see §11.

---

## 6. Interaction Taxonomy: Touch & Press

All press interactions share one sequence. Only the **mass class (P1)** and the **secondary behavior** change between components.

```
Touch down  → scale → pressedScale{mass}   via pressInSpring()      ; tint in  pressInDurationMillis (90ms)
Hold        → visual press latched for at least minPressDurationMillis (110ms)
Touch up    → scale → 1.0                   via pressOutSpring()     ; tint out pressOutDurationMillis (180ms)
State flip  → geometry travel               via selectionSpring()    ; color stateChangeDurationMillis (150ms)
```

| Pattern | Components | Press (P1) | Release | Color | Secondary | Why it differs |
|---|---|---|---|---|---|---|
| **Single Button** | `AuraButton`, `AuraIconButton`, `AuraFab`, `AuraChipButton` | Light `0.95` (Ghost, Tag) · Medium `0.96` · Heavy `0.975` (`Large`, `fullWidth`, extended FAB) | `pressOutSpring` | `90 → 180ms` | Leading icon `0.92` + trailing `2dp` nudge via `iconSpring`; loading swap `120ms` crossfade + `loadingContentScale`/`spinnerEnterScale` | A single "commit" action. The small release rebound (≈ `0.27%` visible) confirms the tap. |
| **Card** | `AuraCard` (clickable) | Heavy `0.975` | `pressOutSpring` | `150ms` | — | Large area: deeper compression would visibly shift the surrounding layout. |
| **Grouped Button** | `AuraSegmentedButton` | **Pressed segment's content only** `0.96`; the track never scales | `pressOutSpring` | `150ms` | Thumb travels by `selectionSpring` (≈ `0.6%`, < `1dp` on a 100dp segment) | The track is one shared body, so it stays rigid. Selection is long-distance travel, so overshoot must stay under the `MOT-RULE-008` budget. |
| | `[Proposed / Future Component]` `AuraButtonGroup` | Each member follows **Single Button** | `pressOutSpring` | `90 → 180ms` | Neighbors never move | A group is a layout unit, not one physical body. |
| **Toggle / Switch** | `AuraSwitch` | Track `0.96` + thumb width × `grabScale` | `pressOutSpring` | `150ms` | Thumb travel `0 ↔ 1` by `selectionSpring` | A persistent state change: settling exactly matters more than rebound. |
| | `AuraIconButton(selected = …)` | Single Button rules | `pressOutSpring` | `150ms` | Icon accent via `iconSpring` on select | Small glyph: `2.8%` overshoot is sub-pixel. |
| **Selection marks** | `AuraCheckbox`, `AuraRadio` | Box/ring `0.96` | `pressOutSpring` | `150ms` | Check draw / dot scale `0 → 1` via `iconSpring` | 8–12dp marks: visible overshoot < `0.3dp`. |
| **Continuous controls** | `AuraSlider`, `AuraRatingBar` | Thumb × `grabScale` / star × `scrubScale` | `pressOutSpring` | `150ms` | **1:1 tracking (`snap`) while dragging**; `selectionSpring` settle on release | Any lag behind the finger breaks the physical link. |
| **Glass chrome** | `AuraBottomIsland`, `AuraTopAppBar` pills, `AuraDropdownMenu` items (`fluidPressScale`) | `glassPressedScale` `0.95` + gel (`gelStretchX`/`gelSquashY`) | `pressOutSpring` | Glow `pressGlowAlpha` | Indicator via `spatialSpring`; split via `elasticStretchSpring` (`LiquidGlass` only) | Floating material shares the same finger physics; gel and glow carry its identity. |
| **Text focus** | `AuraTextField`, `AuraSearchBar`, `AuraNumberField` | **No scale** | — | Focus in `90ms` / out `150ms` | Border width `1 → 2dp` on the same timing as color | Moving a field while typing shifts the caret. |
| **Secondary action** | `AuraSnackbar` action | Light `0.95` with the `110ms` press latch | `pressOutSpring` | `90 → 180ms` | — | Quick taps on transient surfaces still need visible feedback. |

---

## 7. Transition Grammar: Show · Hide · Transform · Morph

| Type | Definition | Geometry | Opacity | Reduced Motion | Components |
|---|---|---|---|---|---|
| **Show** | A new element enters | `*EnterScale → 1` or translate `50%` of own height, via `spatialSpring` (profile per §5) | `enterSpec()` (`easeDecelerate`) | Geometry snaps; opacity `120ms` fade | `AuraDialog` (`overlayEnterScale` + scrim `0 → scrim`), `AuraDropdownMenu` (`popoverEnterScale`, origin = anchor), `AuraSnackbar` (slide + fade), `AuraFloatingActionBar` (`floatingBarAppearSpec`), `AuraSearchBar` clear button (`150ms` fade + `iconSpring` scale), `AuraBadge` (`presenceEnterScale`) |
| **Hide** | An element leaves | **No spring, no overshoot.** `1 → overlayExitScale` or translate `50%`, via `exitSpec()` | `exitSpec()` (`easeAccelerate`) | Geometry snaps; opacity `120ms` fade | `AuraSnackbar`, `AuraFloatingActionBar` (`floatingBarDisappearSpec`), `AuraSearchBar` clear button; `AuraDialog` and `AuraDropdownMenu` must follow `MOT-RULE-010` |
| **Transform** | The same element changes size, position, or value | `selectionSpring` (controls) or `spatialSpring` (containers) | Inner content crossfades `120ms`: outgoing content fades out first, incoming content fades in after the geometry is ~60% complete | Geometry snaps; content `120ms` crossfade | `AuraFab` extended label, `AuraSwitch`/`AuraSegmentedButton` thumb, `AuraBottomIsland` indicator, `AuraTopAppBar` capsule |
| **Morph** | Shape or topology changes, or one identity hands off to another | `adaptiveBarMorphSpring`; `elasticStretchSpring` only when material is `LiquidGlass` | `120ms` crossfade between the swapped layers | `120ms` crossfade, no geometry | `AuraAdaptiveBarState` (Docked ↔ Floating), `AuraBottomIsland` action split, `AuraTopAppBar` metaball split and `MorphingMorePopover` |

**Choreography (`MOT-RULE-006`):** Give each surface one role. `Outgoing` uses `exitSpec()` and starts first. `Incoming` uses `enterSpec()` + `staggerDelay(index)`. `Persistent` chrome stays anchored. Keep at most `1/3` of visible elements in motion at once.

**Gesture continuity (`MOT-RULE-004`):** Drive interruptible geometry with `Animatable` / `animate*AsState` and never hard-reset the target on cancel. Interrupting a press or switching tabs mid-flight continues from the current value and velocity.

---

## 8. Do & Don't

| Do | Don't |
|---|---|
| Spring for geometry, tween for color (P2) | Spring on color, or tween on an interruptible geometry change |
| `selectionSpring()` for any state travel | Reuse `pressOutSpring()` for thumbs, indices, values, or scroll smoothing |
| Pick the press depth from the mass class (P1) | Pass an ad-hoc `pressedScale = 0.97f` at a call site |
| 1:1 tracking while a finger drags | Animate toward the finger with a spring during the drag |
| Keep the track or group rigid; compress only the touched part | Scale a whole group, track, or row on press |
| Use `elasticStretchSpring()` only on `LiquidGlass` | Use `>= 10%` overshoot on `Solid` or `Glass` |
| Latch every press for `minPressDurationMillis` | Read `collectIsPressedAsState()` directly for press visuals |
| Animate Hide whenever Show is animated (`MOT-RULE-010`) | `if (!visible) return` before the exit finishes |
| Name every amplitude as a token (`MOT-RULE-009`) | `animateFloatAsState(targetValue = if (x) 0.9f else 1f)` |
| Read `AuraTheme.motion.reduceMotion` | Read the system setting in each component |

---

## 9. Accessibility: Reduced Motion (`MOT-RULE-007`)

**Single source of truth:** `AuraTheme` reads the system `ANIMATOR_DURATION_SCALE` (centralized in [`AuraReducedMotion.kt`](../../design-system/src/main/kotlin/com/example/ds/foundation/motion/AuraReducedMotion.kt)) and merges it into `AuraTheme.motion.reduceMotion`. Components read **only** `AuraTheme.motion.reduceMotion`, so an app-level `AuraMotion(reduceMotion = true)` override is honored everywhere.

| Motion type | Default | Reduced Motion |
|---|---|---|
| Press scale, icon nudge, gel, glow | Springs | `1.0f` / `0.dp` / `0f` (snap) |
| Press and state color | `90 / 150 / 180ms` tween | **Kept** |
| Selection / Transform geometry | `selectionSpring` / `spatialSpring` | Snap |
| Show / Hide | Scale or translate + fade | Geometry snaps; opacity fades `120ms` |
| Morph | Morph spring | `120ms` crossfade |
| Stagger | `staggerDelay(index)` | `0ms` (built in) |
| Spinner | Rotation + sweep loop | **Kept** (it communicates loading) with `stateDescription = "In progress"` |
| Skeleton pulse | `900ms` loop | Static fill |

---

## 10. Verification

| Layer | Check | Status |
|---|---|---|
| [`SpecCodeConsistencyTest.kt`](../../design-system/src/test/kotlin/com/example/ds/contract/SpecCodeConsistencyTest.kt) | No numeric `tween`/`spring` literals (`MOT-RULE-002`); `motion_track` declared | Shipped |
| | No float literal as animation `targetValue` (`MOT-RULE-009`); `motion_track: "Expressive"` components reference Expressive tokens | Planned |
| [`MotionTokenInvariantTest.kt`](../../design-system/src/test/kotlin/com/example/ds/contract/MotionTokenInvariantTest.kt) | Enter > exit × 1.3; press-in < press-out; stagger bounds; Standard `spatialDampingRatio >= 0.85` | Shipped |
| | Computed OS ≤ `3%` for Standard `selection`/`spatial`/`morph`; `pressedScale*` within `[0.94, 0.98]` | Planned |
| [`ComponentMotionBehaviorTest.kt`](../../design-system/src/test/kotlin/com/example/ds/motion/ComponentMotionBehaviorTest.kt) | Button press/release, Switch progress, FAB expand, Island indicator, Dialog entry, Reduced Motion snaps | Shipped |
| | Dialog/Dropdown Hide progress; scrim alpha starts at `0`; Switch thumb progress peak ≤ `1.01` | Planned |

See [`07-interaction-verification.md`](07-interaction-verification.md) for the full verification methodology.

---

## 11. Adoption Status & Migration Backlog

Decisions adopted on 2026-11-20: `glassPressedScale = 0.95f`, decelerate/accelerate easing for `enterSpec()`/`exitSpec()`, and a new `selectionSpring()` (`0.85` / `600`). Current code still deviates from this guide in these places:

| # | Gap | Location | Rule |
|---|---|---|---|
| 1 | `pressOutSpring()` (6.8% OS) used for state travel | `AuraSwitch` thumb, `AuraSegmentedButton` thumb index, `AuraSlider` settle, `AuraBadge` presence, `AuraTopAppBar` scroll smoothing | `MOT-RULE-003`, `MOT-RULE-008` |
| 2 | Hide not animated (`if (!visible) return`) | `AuraDialog`, `AuraDropdownMenu` | `MOT-RULE-010` |
| 3 | Scrim target is constant, so it never fades | `AuraDialog` | `MOT-RULE-005` |
| 4 | `motion_track: "Expressive"` not bound in code | `AuraFab`, `AuraDialog`, `AuraDropdownMenu`, `AuraBottomIsland` | `MOT-RULE-003` |
| 5 | Components read the system flag directly instead of `AuraTheme.motion.reduceMotion`; the system flag is not observed for runtime changes | `rememberAuraReducedMotion()` call sites | `MOT-RULE-007` |
| 6 | `fluidPressScale` uses `0.92f` and releases with `spatialSpring()`; call sites override to `0.96f`/`0.97f` | `GlassSurface`, `AuraDropdownMenu`, `AuraTopAppBar` | P1, `MOT-RULE-009` |
| 7 | Amplitude literals (`1.18f`, `1.12f`, `1.08f`, `0.9f`, `0.94f`, `0.92f`, `0.75f`, gel/glow) | `AuraSwitch`, `AuraSlider`, `AuraRatingBar`, `AuraBottomIsland`, `AuraBadge`, `AuraDialog`, `AuraDropdownMenu`, `AuraButton`, `GlassSurface` | `MOT-RULE-009` |
| 8 | Two press-latch implementations; Snackbar action has none | `AuraButton`, `AuraIconButton`, `AuraSnackbar` | §6 |
| 9 | `enterSpec()` / `exitSpec()` both use `FastOutSlowInEasing` | `AuraMotion` | P4 |
| 10 | Search bar focus border uses `90ms` in both directions | `AuraSearchBar` | §6 Text focus |

---

## 12. Implementation Files

- [`AuraMotion.kt`](../../design-system/src/main/kotlin/com/example/ds/foundation/motion/AuraMotion.kt)
- [`AuraReducedMotion.kt`](../../design-system/src/main/kotlin/com/example/ds/foundation/motion/AuraReducedMotion.kt)
- [`AuraMotionSemantics.kt`](../../design-system/src/main/kotlin/com/example/ds/foundation/motion/AuraMotionSemantics.kt)
- [`AuraTheme.kt`](../../design-system/src/main/kotlin/com/example/ds/foundation/theme/AuraTheme.kt)
