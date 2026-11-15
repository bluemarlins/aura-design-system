# 01 · Component Evaluation Rule Set

## 1. Purpose

Defines the mandatory quality gate (**`EVAL-001` – `EVAL-008`**) used to audit both **component specifications** (`docs/components/<category>/<component>.md`) and **Kotlin implementations** (`design-system/src/main/kotlin/com/example/ds/component/*.kt`).

Every Aura component must satisfy all eight rules before reaching `status: "ready"`, guaranteeing:
- Explicit design intent and `Do / Don't` usage boundaries across all target apps (**My Planner**, **My Finance**, **My Drink Diary**, **Home Launcher**).
- 100% adherence to `AuraTheme` (`colors`, `typography`, `layout`, `motion`, `elevation`) with zero Material 3 dependency.
- Full `Smallest Width × Display Ratio` responsiveness, `AuraTheme.motion` dual-track feedback (`Standard` / `Expressive`), and `AuraMaterial` layer & fallback compliance (`VIS-RULE-001`–`007`).
- Continuous **Specs ↔ Code** synchronization and deterministic `mainClock` motion verification validated automatically by `SpecCodeConsistencyTest` and `ComponentMotionBehaviorTest`.

---

## 2. Mandatory Evaluation Rules (`EVAL-001` – `EVAL-008`)

### `EVAL-001` · Usage Principles & `Do / Don't`

| Dimension | Requirement |
|---|---|
| **Spec Check** | Must start with `## 1. Intent & Usage Principles` containing `### When to Use`, `### When NOT to Use & Alternatives`, explicit `### Do / Don't` comparison rows (`\| Do \| Don't \|`), and traceable Context Rule IDs (`*-RULE-*`). |
| **Review Question** | Can an engineer or AI agent choose between this component and sibling components cleanly, and see concrete Do vs. Don't examples? |
| **Automated Gate** | Spec contains `## 1. Intent & Usage Principles`, `When to Use`, `When NOT to Use`, and `Do / Don't`. |

---

### `EVAL-002` · Theme Token Compliance (`AuraColors` & `AuraTypography`)

| Dimension | Requirement |
|---|---|
| **Spec Check** | YAML frontmatter `tokens:` declares consumed token groups (`brand.*`, `surface.*`, `content.*`, `typography.*`, etc.), and a `Semantic Token Bindings` section maps parts/variants/states strictly to `AuraTheme.colors.*` and `AuraTheme.typography.*` (or `AuraTheme.layout.*`). |
| **Code Check** | `:design-system` component code must consume `AuraTheme.colors` and `AuraTheme.typography` / `AuraText`, contain zero `androidx.compose.material3` imports, and avoid raw `Color(0x...)` literals outside `@Preview` blocks. |
| **Automated Gate** | Spec declares `tokens:` and `Token Bindings`; Kotlin implementation has zero `material3` imports and zero non-preview `Color(0x` literals. |

---

### `EVAL-003` · Responsive & Adaptive Layout (`Smallest Width × Display Ratio`)

| Dimension | Requirement |
|---|---|
| **Spec Check** | Must contain a `Responsive Layout Behavior` or `Adaptive Behavior Matrix` section explicitly addressing adaptation across `ExtraSmall` (Cover), `Compact` (Phone Portrait), `Medium` / `Square` (Unfolded Foldable `~1:1`), `UltraWide` (Phone Landscape), and `Expanded` (Tablet / DeX). |
| **Code Check** | Components must consume `AuraTheme.layout` or adaptive modifiers (`fillMaxWidth` / `fullWidth`, `widthIn`, `WindowInsets`, `weight`) so layouts never clip on cover displays or over-stretch across `1:1` foldables and tablets. |
| **Automated Gate** | Spec contains `Responsive Layout Behavior` or `Adaptive Behavior Matrix` and explicitly addresses `ExtraSmall`, `Compact`, `Square`, `UltraWide`, and `Expanded`. |

---

### `EVAL-004` · Interaction Motion Contract (`AuraTheme.motion`)

| Dimension | Requirement |
|---|---|
| **Spec Check** | Interactive components (clickable, focusable, toggleable) must include an `Interaction Motion Contract` (or `Interaction & Focus Motion Contract`) specifying press/focus/state/spatial transition tokens (`pressInSpring`, `pressOutSpring`, `spatialSpring`, `enterSpec`, `exitSpec`, `pressInDurationMillis`, `stateChangeDurationMillis`, `minPressDurationMillis`) and Reduced Motion fallback (`ANIMATOR_DURATION_SCALE == 0`). Passive layout containers must explicitly state passive/no-motion rationale (`motion_track: "none"`). |
| **Code Check** | Production Kotlin (`@Preview` stripped) must read `AuraTheme.motion` and **must never contain hardcoded numeric `tween(...)` or `spring(dampingRatio/stiffness = ...)` literals** (`MOT-RULE-002`), while preserving state/loading feedback under Reduced Motion (`MOT-RULE-007`). |
| **Automated Gate** | `SpecCodeConsistencyTest` enforces `EVAL-004` section checks plus zero numeric `tween(` / `spring(` literal matches in component production Kotlin files. |

---

### `EVAL-005` · Deterministic State Precedence & Accessibility Guardrails

| Dimension | Requirement |
|---|---|
| **Spec Check** | Must document deterministic state resolution precedence (e.g., `Loading > Disabled > Pressed > Focused > Rest` or `Disabled > ReadOnly > Error > Focused > Rest`) for stateful controls, plus an `Accessibility` section covering semantic roles, screen-reader descriptions/errors, `>= 48dp` (`44pt`) minimum hit targets, and WCAG AA contrast. |
| **Code Check** | Implementation state resolution order and `Modifier.semantics` / `defaultMinSize` must match the spec. |
| **Automated Gate** | Spec contains an `Accessibility` section; stateful specs document `State Precedence`. |

---

### `EVAL-006` · Bidirectional `Specs ↔ Code` Platform Sync

| Dimension | Requirement |
|---|---|
| **Spec Check** | Must include cross-platform signature blocks (`<!-- API:ANDROID:BEGIN / END -->`, `<!-- API:IOS:BEGIN / END -->`, `<!-- API:WEB:BEGIN / END -->`) so contracts remain portable to iOS SwiftUI and Web React/TS. |
| **Code Check** | Every public `@Composable fun Aura*` signature and parameter name inside `<!-- API:ANDROID:BEGIN --> ... <!-- API:ANDROID:END -->` must match the live Kotlin declaration in `design-system/src/main/kotlin/com/example/ds/component/`. |
| **Automated Gate** | `SpecCodeConsistencyTest` parses Android signature names and parameter lists from Markdown and verifies their presence in Kotlin source files. |

---

### `EVAL-007` · Material & Layer Compliance (`VIS-RULE-001`–`006`)

| Dimension | Requirement |
|---|---|
| **Spec Check** | Ready specs must declare `layer` (`canvas` \| `content` \| `chrome` \| `spatial`) and `material_allowed` (array of `Solid`, `Glass`, `LiquidGlass`) in YAML frontmatter. Any component whose Kotlin implementation uses `GlassSurface` / `AuraMaterial.Glass` / `AuraMaterial.LiquidGlass` must explicitly document `Reduced Transparency` degradation and `4.5:1` contrast requirements in the spec body. |
| **Code Check** | Components in `layer: "content"` or `"canvas"` must not invoke `GlassSurface` (`VIS-RULE-001`). Floating glass containers route materiality through `AuraTheme.effectiveMaterial(...)` and `AuraTheme.elevation`. |
| **Automated Gate** | `SpecCodeConsistencyTest` verifies frontmatter fields, zero `GlassSurface` usage in `content`/`canvas` layer components, and `Reduced Transparency` + `4.5:1` documentation for `GlassSurface` components; `MaterialResolutionTest` verifies the SDK × transparency matrix. |

---

### `EVAL-008` · Motion Track & Behavior Verification (`MOT-RULE-003`–`007`)

| Dimension | Requirement |
|---|---|
| **Spec Check** | Ready specs must declare `motion_track` (`Standard` \| `Expressive` \| `none`) in YAML frontmatter. Interactive components must specify `Standard` or `Expressive`; passive containers specify `none`. |
| **Code Check** | Components with spatial transitions use `AuraTheme.motion` spatial tokens; components listed in [`07-interaction-verification.md`](../foundation/07-interaction-verification.md) §3 (`AuraButton`, `AuraSwitch`, `AuraFab`, `AuraBottomIsland`, `AuraDialog`, `GlassSurface`) have deterministic Robolectric + Compose `mainClock` assertions under `design-system/src/test/kotlin/com/example/ds/motion/`. |
| **Automated Gate** | `SpecCodeConsistencyTest` validates `motion_track` and `ComponentMotionBehaviorTest.kt` coverage; `MotionTokenInvariantTest` and `ComponentMotionBehaviorTest` execute on JVM + Robolectric. |

---

## 3. Component Evaluation Scorecard

| Component | Spec Path | `EVAL-001` Do/Don't | `EVAL-002` Color/Type | `EVAL-003` Responsive | `EVAL-004` Motion | `EVAL-005` State/A11y | `EVAL-006` API Sync | `EVAL-007` Material/Layer | `EVAL-008` Track/Test | Status |
|---|---|---|---|---|---|---|---|---|---|---|
| `AuraButton` | [aura-button.md](actions/aura-button.md) | Pass | Pass | Pass | Pass (`AuraTheme.motion`) | Pass | Pass | Pass (`content`, `Solid\|Glass`) | Pass (`Standard` · `mainClock`) | Ready |
| `AuraIconButton` | [aura-icon-button.md](actions/aura-icon-button.md) | Pass | Pass | Pass | Pass (`AuraTheme.motion`) | Pass | Pass | Pass (`chrome`, `Solid\|Glass`) | Pass (`Standard` · Unit) | Ready |
| `AuraFab` | [aura-fab.md](actions/aura-fab.md) | Pass | Pass | Pass | Pass (`AuraTheme.motion`) | Pass | Pass | Pass (`chrome`, `Solid\|Glass`) | Pass (`Expressive` · `mainClock`) | Ready |
| `AuraSegmentedButton` | [aura-segmented-button.md](actions/aura-segmented-button.md) | Pass | Pass | Pass | Pass (`AuraTheme.motion`) | Pass | Pass | Pass (`content`, `Solid\|Glass`) | Pass (`Standard` · Unit) | Ready |
| `AuraChipButton` | [aura-chip-button.md](actions/aura-chip-button.md) | Pass | Pass | Pass | Pass (`AuraTheme.motion`) | Pass | Pass | Pass (`content`, `Solid\|Glass`) | Pass (`Standard` · Unit) | Ready |
| `AuraTextField` | [aura-text-field.md](inputs/aura-text-field.md) | Pass | Pass | Pass | Pass (`AuraTheme.motion`) | Pass | Pass | Pass (`content`, `Solid`) | Pass (`Standard` · Unit) | Ready |
| `AuraNumberField` | [aura-number-field.md](inputs/aura-number-field.md) | Pass | Pass | Pass | Pass (`AuraTheme.motion`) | Pass | Pass | Pass (`content`, `Solid`) | Pass (`Standard` · Unit) | Ready |
| `AuraSearchBar` | [aura-search-bar.md](inputs/aura-search-bar.md) | Pass | Pass | Pass | Pass (`AuraTheme.motion`) | Pass | Pass | Pass (`chrome`, `Solid\|Glass`) | Pass (`Standard` · Unit) | Ready |
| `AuraDateTimePicker` | [aura-date-time-picker.md](inputs/aura-date-time-picker.md) | Pass | Pass | Pass | Pass (`AuraTheme.motion`) | Pass | Pass | Pass (`content`, `Solid`) | Pass (`Standard` · Unit) | Ready |
| `AuraSwitch` | [aura-switch.md](inputs/aura-switch.md) | Pass | Pass | Pass | Pass (`AuraTheme.motion`) | Pass | Pass | Pass (`content`, `Solid`) | Pass (`Standard` · `mainClock`) | Ready |
| `AuraCheckbox` | [aura-checkbox.md](inputs/aura-checkbox.md) | Pass | Pass | Pass | Pass (`AuraTheme.motion`) | Pass | Pass | Pass (`content`, `Solid`) | Pass (`Standard` · Unit) | Ready |
| `AuraRadio` | [aura-radio.md](inputs/aura-radio.md) | Pass | Pass | Pass | Pass (`AuraTheme.motion`) | Pass | Pass | Pass (`content`, `Solid`) | Pass (`Standard` · Unit) | Ready |
| `AuraSlider` | [aura-slider.md](inputs/aura-slider.md) | Pass | Pass | Pass | Pass (`AuraTheme.motion`) | Pass | Pass | Pass (`content`, `Solid`) | Pass (`Standard` · Unit) | Ready |
| `AuraRatingBar` | [aura-rating-bar.md](inputs/aura-rating-bar.md) | Pass | Pass | Pass | Pass (`AuraTheme.motion`) | Pass | Pass | Pass (`content`, `Solid`) | Pass (`Standard` · Unit) | Ready |
| `AuraCard` | [aura-card.md](containment/aura-card.md) | Pass | Pass | Pass | Pass (`AuraTheme.motion`) | Pass | Pass | Pass (`content`, `Solid\|Glass*`) | Pass (`Standard` · Unit) | Ready |
| `AuraDialog` | [aura-dialog.md](containment/aura-dialog.md) | Pass | Pass | Pass | Pass (`AuraTheme.motion`) | Pass | Pass | Pass (`spatial`, `Solid\|Glass`) | Pass (`Expressive` · `mainClock`) | Ready |
| `AuraBadge` | [aura-badge.md](feedback/aura-badge.md) | Pass | Pass | Pass | Pass (`AuraTheme.motion`) | Pass | Pass | Pass (`content`, `Solid`) | Pass (`Standard` · Unit) | Ready |
| `AuraScaffold` | [aura-scaffold.md](layout/aura-scaffold.md) | Pass | Pass | Pass | Pass (Passive container) | Pass | Pass | Pass (`canvas`, `Solid`) | Pass (`none` · Spec) | Ready |
| `AuraAdaptiveTwoPane` | [aura-adaptive-two-pane.md](layout/aura-adaptive-two-pane.md) | Pass | Pass | Pass (`AuraTheme.layout`) | Pass (Passive container) | Pass | Pass | Pass (`canvas`, `Solid`) | Pass (`none` · Spec) | Ready |
| `AuraTopAppBar` | [aura-top-app-bar.md](navigation/aura-top-app-bar.md) | Pass | Pass | Pass | Pass (`AuraTheme.motion` + Spring Physics) | Pass | Pass | Pass (`chrome`, `Solid\|Glass\|LiquidGlass`) | Pass (`Standard` · Unit) | Ready |
| `AuraBottomIsland` | [aura-bottom-island.md](navigation/aura-bottom-island.md) | Pass | Pass | Pass | Pass (`AuraTheme.motion` + Squish/Stretch) | Pass | Pass | Pass (`chrome`, `Solid\|Glass\|LiquidGlass`) | Pass (`Expressive` · `mainClock`) | Ready |

---

## 4. Automated Verification

- **Contract Suite:** [`SpecCodeConsistencyTest.kt`](../../design-system/src/test/kotlin/com/example/ds/contract/SpecCodeConsistencyTest.kt) (`EVAL-001`–`EVAL-008`)
- **Token Invariants & Material Matrix:** [`MotionTokenInvariantTest.kt`](../../design-system/src/test/kotlin/com/example/ds/contract/MotionTokenInvariantTest.kt), [`MaterialResolutionTest.kt`](../../design-system/src/test/kotlin/com/example/ds/foundation/theme/MaterialResolutionTest.kt)
- **Robolectric `mainClock` Behavior:** [`ComponentMotionBehaviorTest.kt`](../../design-system/src/test/kotlin/com/example/ds/motion/ComponentMotionBehaviorTest.kt)
- **Command:** `.\gradlew.bat :design-system:testDebugUnitTest`
