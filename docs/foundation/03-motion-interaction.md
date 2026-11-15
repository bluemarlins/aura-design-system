# 03 · Interaction Motion

> **Aura-Original Motion System:** `Standard` and `Expressive` motion profiles (`AuraMotionProfile`) and `AuraMotion` physics tokens are **Aura-original concepts** defined specifically for Aura Design System. They are **not** Android Material Design (`material3`) motion tokens or schemes.

## 1. Purpose & Scope

Defines Aura's **Tier 2 semantic motion tokens (`AuraMotion`)**, dual-track physics profiles (`Standard` / `Expressive`), spatial transition grammar, choreography budget, and cross-platform timing rules covering both **in-place interactive feedback** and **spatial, choreographed, and gesture-driven transitions**.

### 1.1 Core Motion Rules (`MOT-RULE-001`–`MOT-RULE-007`)

| Rule ID | Principle | Enforcement Requirement |
|---|---|---|
| **`MOT-RULE-001`** | **Purposeful Motion** | Every animation must map to `Orient`, `Focus`, `Express`, or `Connect`. Decorative unclassifiable motion is removed. |
| **`MOT-RULE-002`** | **Tokens Only** | Zero numeric `tween(...)`, `spring(...)`, or duration literals inside component production code; consume `AuraTheme.motion`. |
| **`MOT-RULE-003`** | **Dual-Track Separation** | `Standard` governs routine tasks, inputs, and high-frequency actions (`0–3%` overshoot). `Expressive` governs spatial entrances, navigation chrome, and focal moments (`<= 1` Expressive hero per screen). |
| **`MOT-RULE-004`** | **Interruptible Springs** | Gesture and state-driven transitions use velocity-preserving physics springs; reversing mid-flight must be smooth and continuous without abrupt jumps. |
| **`MOT-RULE-005`** | **Timing Asymmetry** | Entrances stay `30–50%` longer than exits (`enterDurationMillis > exitDurationMillis`), and press compression is faster than release rebound (`pressInDurationMillis < pressOutDurationMillis`). |
| **`MOT-RULE-006`** | **Choreography Budget** | Elements adopt roles (`Outgoing`, `Incoming`, `Persistent`); stagger interval stays `20–100ms`, total stagger caps at `staggerBudgetMillis` (`400ms`), and `<= 1/3` of screen elements move simultaneously. |
| **`MOT-RULE-007`** | **Reduced Motion** | When `ANIMATOR_DURATION_SCALE == 0f` or `AuraMotion.reduceMotion == true`, spatial motion (scale/translation/parallax/stagger) resolves directly to rest while color, opacity, and loading semantics remain clear. |

---

## 2. Design Philosophy: Tactile Spring Cushion over Material Ripple

Instead of Material Design's radial clipped ripple, Aura uses **Tactile Spring Compression + Smooth State-Layer Interpolation**:

1. **Harmonizes with `Solid` and `Glass` materials:** Radial ripples clip awkwardly on translucent `Glass` and `LiquidGlass` layers; GPU-accelerated scale compression and smooth color tinting feel cohesive across all material treatments.
2. **Unifies 4 application personalities:** Delivers a physical stamp/cushion feel in **My Planner**, crisp confirmation feedback in **My Finance**, refined tactility in **My Drink Diary**, and responsive 3D elasticity in **Home Launcher**.
3. **Cross-platform portable:** Scale + spring physics + state-layer color interpolation translate cleanly across Jetpack Compose, SwiftUI, and Web React without platform-specific ripple shaders.

---

## 3. Interaction Motion Token Architecture (`AuraMotion`)

Exposed via `AuraTheme.motion` (`AuraMotionDefaults.Standard`).

### 3.1. Spring Physics Tokens

| Token | Compose (`SpringSpec`) | iOS (`SwiftUI`) | Web (`Motion`) | Purpose |
|---|---|---|---|---|
| `pressInSpring` | `dampingRatio = 0.85f, stiffness = 800f` | `.spring(response: 0.16, dampingFraction: 0.85)` | `stiffness: 800, damping: 35` | Fast, firm press compression without wobble |
| `pressOutSpring` | `dampingRatio = 0.65f, stiffness = 500f` | `.spring(response: 0.26, dampingFraction: 0.65)` | `stiffness: 500, damping: 22` | Subtle elastic rebound on release |
| `iconSpring` | `dampingRatio = 0.75f, stiffness = 600f` | `.spring(response: 0.21, dampingFraction: 0.75)` | `stiffness: 600, damping: 28` | Secondary icon micro-nudge and spinner pop |

### 3.2. Asymmetric Duration Tokens

Interactive transitions use **fast attack (`pressIn`)** and **gradual settle (`pressOut`)**:

| Token | Value | Easing | Purpose |
|---|---|---|---|
| `pressInDurationMillis` | `90ms` | `FastOutSlowInEasing` | Container/border press tint |
| `pressOutDurationMillis` | `180ms` | `FastOutSlowInEasing` | Container/border release fade-back |
| `minPressDurationMillis` | `110ms` | — | Minimum pressed visual latch so quick taps inside scrollable containers render complete press-and-release motion |
| `stateChangeDurationMillis` | `150ms` | `FastOutSlowInEasing` | Enabled ↔ Disabled transitions |
| `loadingCrossfadeDurationMillis` | `120ms` | `FastOutSlowInEasing` | In-place `Rest ↔ Loading` content crossfade |
| `spinnerRotationDurationMillis` | `900ms` | `LinearEasing` | Continuous spinner rotation |
| `spinnerSweepDurationMillis` | `650ms` | `FastOutSlowInEasing` (Reverse) | Breathing spinner arc modulation (`45° ↔ 240°`) |

### 3.3. Interactive Scale & Micro-Nudge Tokens

| Token | Value | Target |
|---|---|---|
| `pressedScaleDefault` | `0.96f` | `Small` / `Medium` interactive controls |
| `pressedScaleLarge` | `0.975f` | `Large` / `fullWidth` controls (prevents excessive shrinkage on wide CTAs) |
| `pressedScaleGhost` | `0.95f` | Borderless `Ghost` buttons where scale carries primary tactile feedback |
| `leadingIconPressedScale` | `0.92f` | Subtle icon depth compression on press |
| `trailingIconNudge` | `2.dp` (`2px`) | Directional forward nudge on press |

---

## 4. Accessibility: Reduced Motion Policy (`MOT-RULE-007`)

- **System Animation Scale (`ANIMATOR_DURATION_SCALE == 0f`) / `AuraTheme.motion.reduceMotion`:**
  - Centralized in [`AuraReducedMotion.kt`](../../design-system/src/main/kotlin/com/example/ds/foundation/motion/AuraReducedMotion.kt) (`rememberAuraReducedMotion()`) and composed with `AuraTheme.motion.reduceMotion`.
  - Spatial transformations (`scale`, `translationX` icon nudge, parallax, dialog entrance zoom, stagger offsets) resolve directly to rest (`snap()` / `1.0f` / `0.dp` / `0ms` delay) to avoid vestibular discomfort.
  - Color/border/state-layer transitions and explicit loading state semantics (`stateDescription = "In progress"`) remain active so interactive feedback is never lost.

---

## 5. Dual-Track Profiles: `Standard` vs. `Expressive` (`MOT-RULE-003`)

Both tracks share the same responsive **in-place tactile tokens** (`pressInSpring`, `pressOutSpring`, `pressedScaleDefault`, `pressInDurationMillis`, `pressOutDurationMillis`) and diverge on **spatial, choreographed, and hero surface transitions**:

| Token | `Standard` | `Expressive` | Usage Guidance |
|---|---|---|---|
| `enterDurationMillis` | `280` | `380` | Container / overlay entrance duration (always `30–50%` longer than exit) |
| `exitDurationMillis` | `200` | `260` | Container / overlay dismissal duration |
| `spatialDampingRatio` | `0.90f` | `0.75f` | `Standard` stays restrained (`0–3%` overshoot); `Expressive` permits buoyant spring settle |
| `spatialStiffness` | `400f` | `350f` | Snappy task cadence vs. unhurried spatial expansion |
| `elasticDampingRatio` | `0.58f` | `0.58f` | `LiquidGlass` transient stretch / squish recovery |
| `elasticStiffness` | `320f` | `320f` | Impulse rebound stiffness for liquid island & morph capsules |
| `staggerDelayMillis` | `40` | `60` | Per-item cascade delay (`staggerDelay(index)`) |
| `staggerBudgetMillis` | `400` | `400` | Maximum cumulative stagger cap across a group |
| `scrimFadeDurationMillis` | `180` | `220` | Protective modal backdrop fade |

- **Global vs. Scoped Override Policy:** Set `AuraTheme(motionProfile = AuraMotionProfile.Standard)` globally by default for forms, lists, and productivity flows. Switching to `AuraMotionProfile.Expressive` via a nested `AuraTheme` override is permitted **only on spatial/chrome layers** (Dialog, Floating Bottom Island, future `AuraBottomSheet`) with at most **1 Expressive hero** active per screen.

---

## 6. Spatial, Choreography & Gesture Continuity Rules (`MOT-RULE-004`–`MOT-RULE-006`)

1. **Spatial Transitions (`spatialSpring()`, `enterSpec()`, `exitSpec()`):** Floating popovers (`MorphingMorePopover`), `AuraDialog` scale/scrim, `AuraFab` extended label expand/collapse, and sliding chrome (`AuraBottomIsland`, `FloatingTopPillBar`) consume `AuraTheme.motion.spatialSpring()`, `enterSpec()`, `exitSpec()`, and `scrimFadeDurationMillis`.
2. **Choreography Roles & Budget (`MOT-RULE-006`):** Assign each transitioning surface one explicit role: `Outgoing` (fast exit via `exitSpec()`), `Incoming` (ordered entrance via `enterSpec()` + `staggerDelay(index)`), or `Persistent` (anchored header/chrome). Keep `staggerDelayMillis` within `20–100ms`, total cascade `<= 400ms`, and simultaneously moving elements `<= 1/3` of visible items.
3. **Gesture Continuity (`MOT-RULE-004`):** Interactive state changes and drags drive velocity-continuous springs (`Animatable` / `animate*AsState` without hard-resetting targets on cancellation). Interrupting a press or switching navigation tabs mid-flight carries existing momentum seamlessly toward the new target.

---

## 7. Implementation Files

- [`AuraMotion.kt`](../../design-system/src/main/kotlin/com/example/ds/foundation/motion/AuraMotion.kt)
- [`AuraReducedMotion.kt`](../../design-system/src/main/kotlin/com/example/ds/foundation/motion/AuraReducedMotion.kt)
- [`AuraMotionSemantics.kt`](../../design-system/src/main/kotlin/com/example/ds/foundation/motion/AuraMotionSemantics.kt)
- [`AuraTheme.kt`](../../design-system/src/main/kotlin/com/example/ds/foundation/theme/AuraTheme.kt)
