# 06. Materiality & Depth Principles

> **Aura-Original Architecture:** `AuraMaterial` (`Solid`, `Glass`, `LiquidGlass`), the 4-layer surface model, and `AuraElevation` (`level0`–`level4`) are **Aura-original concepts** designed specifically for the Aura Design System. They are **not** Android Material Design (`material3`) concepts or APIs.

---

## 1. Material Axis (`AuraMaterial`)

Aura separates **Color Theme** (`AuraColorMode`: `Light` / `Dark`) from **Surface Materiality** (`AuraMaterial`):

| Material | Optical Behavior | Primary Role | Active Limit |
|---|---|---|---|
| `AuraMaterial.Solid` | Opaque tokenized surface fill (`colors.surface.*`) with crisp 1.dp subtle border | Long-lived content, forms, cards, settings, fallback states | Unlimited |
| `AuraMaterial.Glass` | Live backdrop gaussian blur (`RenderEffect`, API 31+) with tinted frost wash (`68–72%` opacity) and specular border | Floating navigation chrome, search capsules, transient sheets & dialogs | `<= 3` live-blur surfaces per viewport |
| `AuraMaterial.LiquidGlass` | Optical convex-bevel shader with peripheral chromatic dispersion + refraction (AGSL `RuntimeShader`, API 33+) and viscous metaball/squish coupling | Interactive floating hero surfaces (e.g., `AuraBottomIsland`, active pill capsule) | `<= 1` hero surface per screen (`VIS-RULE-002`, `VIS-RULE-006`) |

**Theme ceiling:** `AuraTheme(material = ...)` is the maximum material any surface below it may render. Components keep their own requested material (hero `LiquidGlass`, standard `Glass`), and the theme lowers it: a `Glass` theme renders `LiquidGlass` requests as `Glass`, a `Solid` theme renders every surface `Solid`. The default `LiquidGlass` imposes no limit.

> **Deprecation Note:** `GlassStyle` (`FROSTED`, `LIQUID`) is deprecated in favor of `AuraMaterial`. Existing component parameters remain binary-compatible through `GlassStyle.toMaterial()`.

---

## 2. Four-Layer Surface Model (`VIS-RULE-001`)

Every Aura screen composes UI across 4 explicit Z-layers so translucent chrome never blurs sibling chrome or competes with primary content:

| Layer | Name | Allowed Materials | Typical `AuraElevation` | Representative Components |
|---|---|---|---|---|
| **Layer 0** | `canvas` | `Solid` | `level0` (`0.dp`) | `AuraScaffold`, `AuraAdaptiveTwoPane`, scrolling feed/editor background |
| **Layer 1** | `content` | `Solid` (edge scrim zone via `FloatingGlassScaffold`) | `level0`–`level1` (`0–2.dp`, scrim `0.16f`–`0.40f`) | `AuraButton`, `AuraTextField`, `AuraNumberField`, `AuraSwitch`, `AuraCheckbox`, `AuraRadio`, `AuraSlider`, `AuraRatingBar`, `AuraDateTimePicker`, `AuraSegmentedButton`, `AuraCard` (`*`) |
| **Layer 2** | `chrome` | `Solid`, `Glass`, `LiquidGlass` (`<= 1`) | `level1`–`level3` (`2–12.dp`) | `AuraTopAppBar`, `AuraBottomIsland`, `AuraSearchBar`, `AuraFab`, `AuraIconButton` (`Floating`), `AuraChipButton` (`Glass`), `AuraBadge` (`Glass`) |
| **Layer 3** | `spatial` | `Solid`, `Glass`, `LiquidGlass` (`<= 1`) | `level3`–`level4` (`12–24.dp`, scrim `0.40f`–`0.56f`) | `AuraDialog`, `MorphingMorePopover`, future `AuraBottomSheet` |

> **`*` Scoped Exception (`AuraCardVariant.Glass`):** `AuraCard` belongs to `layer: "content"` (`Layer 1`) by default and uses `Solid` variants (`Filled`, `Outlined`, `Elevated`) on inline canvases. Its `Glass` variant (`material_allowed: ["Solid", "Glass"]`) is strictly reserved for **Home Launcher** wallpaper-backed widgets or isolated overlay tiles (`CNT-RULE-003`)—never standard scrollable list items.

---

## 3. Core Visual Interaction Rules (`VIS-RULE-001`–`VIS-RULE-007`)

| Rule ID | Principle | Enforcement Requirement |
|---|---|---|
| **`VIS-RULE-001`** | **Layer Boundary Discipline** | `GlassSurface` / `Glass` / `LiquidGlass` are permitted **only** in `chrome` (Layer 2) and `spatial` (Layer 3), plus documented wallpaper-backed widget exceptions. Inline content items (`AuraScaffold` body, form fields, list items) must remain `Solid`. |
| **`VIS-RULE-002`** | **Material by Surface Lifetime** | `Solid` governs persistent workspace content; `Glass` governs persistent/transient floating navigation & overlays; `LiquidGlass` is restricted to at most **one focal hero surface per screen**. |
| **`VIS-RULE-003`** | **Contrast & Legibility on Glass** | Foreground text over any `Glass` or `LiquidGlass` surface must maintain `>= 4.5:1` contrast (WCAG AA); icons and hairline borders must maintain `>= 3:1`. Strengthen tint opacity or progressive edge scrim (`FloatingGlassScaffold`) if dynamic backdrops reduce contrast. |
| **`VIS-RULE-004`** | **Hardware & Accessibility Degradation** | Resolve requested material through the single pure pipeline `resolveEffectiveMaterial(requested, sdkInt, reduceTransparency, ceiling)` matching the matrix in §4. **Reduced Transparency**, Battery Saver (`PowerManager.isPowerSaveMode`), or High-Contrast Text (`high_text_contrast_enabled`) must immediately fall back to `Solid`. |
| **`VIS-RULE-005`** | **Elevation & Scrim Ladder** | All shadows and protective scrims must read from `AuraTheme.elevation` (`level0`–`level4`). Hardcoded drop-shadow `Dp` or ad-hoc scrim alpha literals in components are forbidden. |
| **`VIS-RULE-006`** | **Live-Blur Performance Budget** | Render at most **3 simultaneous live-blur (`RenderEffect` / AGSL) surfaces** in a visible viewport and at most **1 `LiquidGlass`** surface per screen. |
| **`VIS-RULE-007`** | **State & Focus Effects** | Pressed, hovered, focused, and selected feedback must be driven exclusively through `AuraColors.interaction` state layers and `AuraTheme.motion` tokens. |

---

## 4. Material Fallback Matrix (`VIS-RULE-004`)

All components resolve effective materiality via `resolveEffectiveMaterial(requested, sdkInt, reduceTransparency, ceiling)` in `AuraTheme.kt`. The requested material is first capped to `ceiling` (`AuraTheme.material`), then resolved through this matrix:

| Requested Material | `reduceTransparency = true` (All APIs) | Android API 33+ (TIRAMISU+) | Android API 31–32 (S / S_V2) | Android API 26–30 (Pre-S) |
|---|---|---|---|---|
| `AuraMaterial.Solid` | `Solid` | `Solid` | `Solid` | `Solid` |
| `AuraMaterial.Glass` | `Solid` | `Glass` (`26.dp` blur `RenderEffect`) | `Glass` (`26.dp` blur `RenderEffect`) | `Solid` |
| `AuraMaterial.LiquidGlass` | `Solid` | `LiquidGlass` (AGSL shader + soft blur) | `Glass` (soft `10.dp` blur `RenderEffect`) | `Solid` |

- **Accessibility / Battery Saver (`reduceTransparency = true`):** `GlassSurface` disables blur/AGSL shaders, switches to near-opaque `colors.surface.floating` (`AuraTheme.glass.solidFallbackAlpha`, `0.98f`), and exposes `Modifier.testTag("aura_glass_surface_solid_fallback")` for automated verification.
- **Hardware Pre-Blur Fallback (`API 26–30` with `reduceTransparency = false`):** `resolveEffectiveMaterial` returns `Solid` (`RenderEffect` is unavailable), and `GlassSurface` renders the same opaque fallback as above: no backdrop sampling, no translucent tint over unblurred content.
- **Live updates:** `rememberAuraReduceTransparency()` observes `ACTION_POWER_SAVE_MODE_CHANGED` and the accessibility settings, so surfaces switch to `Solid` while the app is running.

---

## 5. Depth & Elevation Ladder (`AuraElevation`)

| Token | Elevation (`Dp`) | Key Shadow Alpha | Ambient Shadow Alpha | Scrim Alpha | Intended Surface Role |
|---|---|---|---|---|---|
| `level0` | `0.dp` | `0.00f` | `0.00f` | `0.00f` | Canvas, inline inputs, outlined/filled cards |
| `level1` | `2.dp` | `0.06f` | `0.04f` | `0.16f` | Elevated/glass cards, segmented thumb, glass chips, circular action pods, edge scrim tail / popover scrim |
| `level2` | `6.dp` | `0.14f` | `0.08f` | `0.28f` | Scrolled `AuraTopAppBar`, `Floating` icon button, `Secondary`/`Glass` `AuraFab` |
| `level3` | `12.dp` | `0.14f` | `0.08f` | `0.40f` | `Primary`/`Surface` `AuraFab`, protective top/bottom edge scrim peaks in `FloatingGlassScaffold` |
| `level4` | `24.dp` | `0.20f` | `0.12f` | `0.56f` | Modal spatial overlays (`AuraDialog`, future `AuraBottomSheet`) |

---

## 6. Glass Optical Tokens (`AuraGlass`)

`GlassSurface` and `LiquidMetaballBridge` read every optical value from `AuraTheme.glass`. `AuraTheme` selects `AuraGlassDefaults.Light` / `Dark` from `colorMode`; pass `glass = ...` to override. Hardcoded glass tint, blur, or stroke literals in components are forbidden.

| Token | Light | Dark | Role |
|---|---|---|---|
| `frostedTint` | White `72%` | `#1E1E22` `68%` | `Glass` wash over the blurred backdrop |
| `liquidTint` | White `54%` | `#1E1E22` `66%` | `LiquidGlass` wash over the refracted backdrop |
| `liquidBridgeTint` | White `68%` | `#22242A` `62%` | `LiquidGlass` metaball neck fill |
| `strokeStartAlpha` / `strokeEndAlpha` | `0.55f` / `0.08f` | `0.34f` / `0.06f` | Diagonal white specular stroke |
| `solidFallbackAlpha` | `0.98f` | `0.98f` | `surface.floating` opacity in `Solid` fallback |
| `blurRadius` / `liquidBlurRadius` | `26.dp` / `10.dp` | same | Backdrop blur |
| `saturation` / `liquidSaturation` | `1.2f` / `1.25f` | same | Backdrop saturation multiplier |
| `bevelWidth` / `refraction` / `dispersion` | `8.dp` / `5.5.dp` / `1.15.dp` | same | `LiquidGlass` edge lens |

**Contrast floor (`VIS-RULE-003`):** `frostedTint` and `liquidTint` keep `content.primary` at `>= 4.5:1` over both a pure white and a pure black backdrop; `AuraGlassTest` fails the build if a tint change breaks this. The guarantee covers the tint only. `content.secondary` and lower roles are not covered and still depend on `FloatingGlassScaffold` edge scrims or a stronger `surfaceAlphaMultiplier` over extreme backdrops.

---

## 7. Planned Component Governance (Principles Only)

- **`AuraBottomSheet` (`status: planned`):**
  - **Layer:** `spatial` (Layer 3).
  - **Elevation:** `AuraTheme.elevation.level4` (`24.dp`, backdrop scrim `scrimAlpha = 0.56f`).
  - **Allowed Materials:** `["Solid", "Glass"]` (must honor Reduced Transparency -> `Solid`, `>= 4.5:1` text contrast).
  - **Motion Profile:** `Expressive` default for spatial sheet presentation and drag-to-dismiss velocity continuity (`MOT-RULE-004`). Not yet implemented in `:design-system`.

---

## 8. Implementation Files

- [`AuraElevation.kt`](../../design-system/src/main/kotlin/com/example/ds/foundation/elevation/AuraElevation.kt)
- [`AuraGlass.kt`](../../design-system/src/main/kotlin/com/example/ds/foundation/glass/AuraGlass.kt)
- [`AuraTheme.kt`](../../design-system/src/main/kotlin/com/example/ds/foundation/theme/AuraTheme.kt)
- [`GlassSurface.kt`](../../design-system/src/main/kotlin/com/example/ds/component/GlassSurface.kt)
