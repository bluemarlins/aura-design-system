# 07. Interaction & Visual Verification Strategy (`Solid` · `Glass Morphism` · `Liquid Glass`)

Aura Design System verifies Motion Interaction (`MOT-RULE-001`–`MOT-RULE-010`) and Visual Materiality (`VIS-RULE-001`–`VIS-RULE-007`) across all three materials (**`Solid`**, **`Glass` [Glass Morphism]**, **`LiquidGlass`**) and both color modes (**`Light`**, **`Dark`**) through a **4-stage automated & visual verification methodology**.

---

## 1. Three-Tier Automated Verification Architecture

| Tier | Mechanism | Execution Environment | Rules Verified |
|---|---|---|---|
| **Tier 1: Static Contract (`EVAL-004`, `EVAL-007`, `EVAL-008`)** | [`SpecCodeConsistencyTest.kt`](../../design-system/src/test/kotlin/io/github/bluemarlins/aura/contract/SpecCodeConsistencyTest.kt) parses Markdown frontmatter/bodies and production Kotlin source (stripping `@Preview`) | Pure JVM (`:design-system:testDebugUnitTest`) | Zero numeric `tween`/`spring` literals (`MOT-RULE-002`); `layer` & `material_allowed` frontmatter; no `GlassSurface` in `content` layer (`VIS-RULE-001`); `"Reduced Transparency"` & `"4.5:1"` contrast contracts (`VIS-RULE-003`, `VIS-RULE-004`); `motion_track` & test coverage (`MOT-RULE-003`) |
| **Tier 2: Token, Contrast & Material Matrix Invariants** | [`AuraGlassTest.kt`](../../design-system/src/test/kotlin/io/github/bluemarlins/aura/foundation/glass/AuraGlassTest.kt), [`MotionTokenInvariantTest.kt`](../../design-system/src/test/kotlin/io/github/bluemarlins/aura/contract/MotionTokenInvariantTest.kt), [`MaterialResolutionTest.kt`](../../design-system/src/test/kotlin/io/github/bluemarlins/aura/foundation/theme/MaterialResolutionTest.kt) | Pure JVM (`:design-system:testDebugUnitTest`) | Worst-case composite contrast (`>= 4.5:1` for `Glass Morphism` & `Liquid Glass` across Light/Dark over pure white & pure black backdrops, `VIS-RULE-003`); Enter > Exit duration asymmetry (`MOT-RULE-005`); full SDK × Material × `reduceTransparency` × theme ceiling truth table (`VIS-RULE-004`) |
| **Tier 3: Deterministic `mainClock` & Accessibility Fallback Tests** | [`ComponentMotionBehaviorTest.kt`](../../design-system/src/test/kotlin/io/github/bluemarlins/aura/motion/ComponentMotionBehaviorTest.kt), [`ReduceTransparencyTest.kt`](../../design-system/src/test/kotlin/io/github/bluemarlins/aura/foundation/theme/ReduceTransparencyTest.kt), [`FloatingGlassNavigationTest.kt`](../../design-system/src/test/kotlin/io/github/bluemarlins/aura/component/FloatingGlassNavigationTest.kt) | JVM + Robolectric (`:design-system:testDebugUnitTest`) | Mid-flight spring scales, monotonic progress, Reduced Motion instant snap + semantic preservation (`MOT-RULE-007`), `minimizeOnScrollDown` fluid compacting, Reduced Transparency fallback path (`aura_glass_surface_solid_fallback`) |

---

## 2. Four-Stage Verification Methodology for Glass Morphism & Liquid Glass

### Stage 1: Worst-Case Composite Contrast Verification (`VIS-RULE-003`)

Because translucent surfaces (`Glass Morphism` and `Liquid Glass`) expose shifting backgrounds, evaluating raw tint colors in isolation is invalid. Every mode (`Light` and `Dark`) must be mathematically verified by compositing its translucent tint (`frostedTint` / `liquidTint`) over **extreme boundary backdrops** ($B \in \{\text{Pure White } \#\text{FFFFFF}, \text{Pure Black } \#\text{000000}\}$):

$$\text{CompositeColor}(B) = \text{TintColor} \;\text{over}\; B$$

$$\text{ContrastRatio}(T, B) = \frac{\max(L(T), L(\text{CompositeColor}(B))) + 0.05}{\min(L(T), L(\text{CompositeColor}(B))) + 0.05}$$

- **Mandatory Thresholds (Enforced in [`AuraGlassTest.kt`](../../design-system/src/test/kotlin/io/github/bluemarlins/aura/foundation/glass/AuraGlassTest.kt)):**
  - **Primary Foreground Typography (`content.primary`):** $\text{ContrastRatio} \ge 4.5:1$ (WCAG AA) across both `Light` and `Dark` modes for both `frostedTint` (`Glass Morphism`) and `liquidTint` (`Liquid Glass`).
  - **Icons, Glyphs & Active Indicators:** $\text{ContrastRatio} \ge 3.0:1$ (WCAG AA Non-text).
  - **Optical Invariants:**
    - `0 < liquidTint.alpha < frostedTint.alpha < 1` (Liquid Glass lens interior is clearer than Glass Morphism frosted sheet).
    - `liquidBlurRadius < blurRadius` (Glass Morphism diffuses broadly at `26–28.dp`; Liquid Glass preserves interior clarity at `10–12.dp`).
    - `strokeStartAlpha > strokeEndAlpha` (Asymmetric directional specular rim from top-start lit corner to bottom-end shaded corner).
    - `solidFallbackAlpha >= 0.95f` (Opaque `Solid` fallback guarantee).

### Stage 2: Optical Layer Integrity & Visual Inspection

To verify that `Glass Morphism` (`AuraMaterial.Glass`) and `Liquid Glass` (`AuraMaterial.LiquidGlass`) render with authentic optical physics rather than flat alpha boxes:

| Verification Check | `Glass Morphism` (`AuraMaterial.Glass`) Criterion | `Liquid Glass` (`AuraMaterial.LiquidGlass`) Criterion |
|---|---|---|
| **1. High-Frequency Backdrop Occlusion vs. Lensing** | Scrolling body text or high-contrast cards behind the surface must be uniformly diffused by the `26–28.dp` blur into soft pastel/obsidian color fields so background glyphs cannot be read through the glass. | Background shapes remain recognizable inside the soft-blurred center but visibly bend, magnify, and chromatize along the `11.dp` convex outer bevel (`refraction = 7.5.dp`, `dispersion = 1.45.dp`). |
| **2. Foreground Sharpness Isolation** | `RenderEffect` applies strictly to the isolated `backdrop` child layer; foreground typography (`content`) and the `1.dp` specular border (`stroke`) must remain 100% razor-sharp with zero blur leakage. | Foreground icons, labels, and the 5-step Fresnel / prism rim overlay remain crisp above the AGSL-warped backdrop layer. |
| **3. Mode-Specific Rim & Banding Discipline** | **Light Mode:** `White 55% → 8%` specular border over milky frost. **Dark Mode:** Restrained `White 34% → 6%` border over smoked obsidian (`#1E1E22`) with zero harsh white halo or gradient banding. | Directional Fresnel rim + top-left droplet catchlight + bottom-right secondary bounce reflection + velocity-boosted cyan/magenta prism fringe. |
| **4. No Nested Blur Stacking (`VIS-RULE-006`)** | Child controls inside a `Glass Morphism` container (e.g., chips, segmented thumbs) must use luminance/tint offsets rather than invoking a nested live-blur `GlassSurface`. | The sliding active tab indicator inside `AuraBottomIsland` uses `SlidingGlassLensIndicator` optical canvas drawing rather than a nested backdrop `RenderEffect`. |

### Stage 3: Accessibility & Hardware Fallback Matrix (`VIS-RULE-004`)

Verified automatically via [`MaterialResolutionTest.kt`](../../design-system/src/test/kotlin/io/github/bluemarlins/aura/foundation/theme/MaterialResolutionTest.kt) and [`ReduceTransparencyTest.kt`](../../design-system/src/test/kotlin/io/github/bluemarlins/aura/foundation/theme/ReduceTransparencyTest.kt):

1. **System Reduce Transparency / High-Contrast Text (`reduceTransparency = true`):** Immediately bypasses backdrop recording, `RenderEffect`, and AGSL shaders, rendering `AuraMaterial.Solid` (`colors.surface.floating` at `98%` alpha + `1.dp` `colors.border.default`) and exposing `Modifier.testTag("aura_glass_surface_solid_fallback")`.
2. **Battery Saver (`PowerManager.isPowerSaveMode`):** Observed live via `BroadcastReceiver`; dynamically switches active `Glass Morphism` and `Liquid Glass` surfaces to `Solid` without restarting the activity.
3. **Theme Ceiling (`AuraTheme.material`):** A theme configured with `material = AuraMaterial.Glass` (Glass Morphism theme) automatically caps all `LiquidGlass` hero requests down to `AuraMaterial.Glass`; `material = AuraMaterial.Solid` caps all translucent requests to `AuraMaterial.Solid`.
4. **SDK Capability Downgrade:**
   - **API 33+:** Full `LiquidGlass` (AGSL + soft blur) and `Glass` (`26.dp` blur + saturation).
   - **API 31–32:** `LiquidGlass` degrades gracefully to `Glass` (`RenderEffect` blur).
   - **API 26–30:** Both `LiquidGlass` and `Glass` degrade to opaque `Solid` (never renders unblurred translucent washes over raw text).

### Stage 4: Live Scroll Invalidation & Frame Budget (`VIS-RULE-006`)

1. **Continuous Backdrop Refresh:** Scrollable containers hosted inside `FloatingGlassScaffold` must drive `rememberFloatingScrollProgress` or `BindAdaptiveBarTo*` so `GlassBackdropState.invalidateBackdrop()` increments `contentDrawTick` on every scroll offset change, preventing stale frozen backdrops behind floating bars.
2. **Progressive Scroll Edge Effect:** Top and bottom viewport edges apply a mode-aware luminance veil (`edgeScrimBase` in `FloatingGlassScaffold`) so system status-bar icons stay legible while scrolled cards remain visible behind floating glass capsules.
3. **Viewport Blur Budget:** At most **3 simultaneous live-blur surfaces** (`Glass Morphism` + `Liquid Glass`) and at most **1 `LiquidGlass` hero surface** may be visible in a single viewport.

---

## 3. Component Motion & Visual Test Matrix (`EVAL-008`)

Every component below has dedicated deterministic `mainClock` assertions in `ComponentMotionBehaviorTest.kt` (referenced by `EVAL-008`):

| Component | Test Cases Verified | Rules Covered |
|---|---|---|
| `AuraButton` | Press (`pressedScaleDefault`), Release settle (`1.0f`), Mid-flight press->release continuity, `reduceMotion = true` instant snap with `stateDescription = "In progress"` preserved | `MOT-RULE-002`, `MOT-RULE-004`, `MOT-RULE-005`, `MOT-RULE-007` |
| `AuraSwitch` | Unchecked (`0f`) -> Checked (`1f`) monotonic spring progress and settle; `selectionSpring` peak progress `<= 1.01f`; `reduceMotion = true` immediate snap | `MOT-RULE-002`, `MOT-RULE-004`, `MOT-RULE-007`, `MOT-RULE-008` |
| `AuraFab` | Extended collapse/expand progress transition; press scale settle | `MOT-RULE-002`, `MOT-RULE-003`, `MOT-RULE-004` |
| `AuraBottomIsland` | Active indicator transition between index `0` and index `2` under `Expressive` profile, velocity-driven elastic stretch + settle (`1f`), `minimizeOnScrollDown` fluid compacting, `reduceMotion = true` snap | `MOT-RULE-003`, `MOT-RULE-004`, `MOT-RULE-007` |
| `AuraDialog` | `visible = true` spatial entry scale/alpha (`Expressive` profile); `visible = false` animates Hide (alpha mid-flight, scale `>= overlayExitScale`) and then leaves composition | `MOT-RULE-003`, `MOT-RULE-005`, `MOT-RULE-010` |
| `GlassSurface` | `reduceTransparency = true` renders `"aura_glass_surface_solid_fallback"`; `reduceTransparency = false` omits solid fallback tag | `VIS-RULE-004` |

---

## 4. Verification Commands & Manual Checklist

```powershell
.\gradlew.bat :design-system:testDebugUnitTest
.\gradlew.bat :design-system-preview:testDebugUnitTest
.\gradlew.bat assembleDebug
```

**Manual Device Checklist (`:design-system-preview` & Consumer Apps):**
- Switch across all 6 theme combinations (**`Solid` / `Glass` (Glass Morphism) / `LiquidGlass` × `Light` / `Dark`**) and verify distinct optical signatures.
- Verify `Glass` (`26.dp` frosted `RenderEffect` blur + saturation + specular hairline) on Android API 31+ and `LiquidGlass` (AGSL refractive bevel + chromatic dispersion + sliding glass lens) on API 33+ devices.
- Toggle system **Remove animations** ON -> verify spatial motion snaps immediately while loading/color states remain legible.
- Toggle system **Battery Saver** or **High-contrast text** ON -> verify `Glass` and `LiquidGlass` degrade cleanly to crisp `Solid` surfaces.
- Inspect Light and Dark `AuraColorMode` contrast (`>= 4.5:1` text, `>= 3:1` borders/icons) over busy scrolling backdrops.
