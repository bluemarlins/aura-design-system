# 06. Materiality & Depth Principles (`Solid` · `Glass Morphism` · `Liquid Glass`)

> **Aura-Original Architecture:** `AuraMaterial` (`Solid`, `Glass` [Glass Morphism], `LiquidGlass`), the 3×2 Material × ColorMode (`Light` / `Dark`) optical matrix, the 4-layer surface model, and `AuraElevation` (`level0`–`level4`) are **Aura-original concepts** designed specifically for the Aura Design System. They are **not** Android Material Design (`material3`) concepts or APIs.

---

## 1. The Three-Material Axis & 3×2 Mode Matrix (`AuraMaterial` × `AuraColorMode`)

Aura separates **Color Mode** (`AuraColorMode`: `Light` / `Dark`) from **Surface Materiality** (`AuraMaterial`), defining **6 distinct optical surface recipes** (`3 Materials × 2 Color Modes`):

| Material (`AuraMaterial`) | Physical Metaphor & Optical Mechanism | Primary Role | Active Limit |
|---|---|---|---|
| **`AuraMaterial.Solid`** | **Opaque Ceramic / Matte Paper (Reflection):** 100% opaque tokenized surface fill (`colors.surface.*`) with crisp `1.dp` subtle hairline border | Long-lived content, forms, cards, settings, accessibility & legacy fallback states | Unlimited |
| **`AuraMaterial.Glass`** *(Glass Morphism)* | **Frosted Acrylic / Etched Glass (Uniform Scattering & Diffusion):** Wide gaussian backdrop blur (`26–28.dp`, `RenderEffect` API 31+), vibrancy saturation/luminosity matrix, translucent frost wash (`68–72%`), micro-noise grain (`1.8–2.2%`), and `1.dp` asymmetric specular hairline border | Floating navigation chrome, search capsules, contextual toolbars, transient sheets, dialogs, and wallpaper widgets | `<= 3` live-blur surfaces per viewport |
| **`AuraMaterial.LiquidGlass`** | **Refractive Optical Lens / Fluid Gel (Refraction & Lensing):** Soft interior safe-zone blur (`10–12.dp`) paired with `11.dp` convex-bevel AGSL refraction (`7.5.dp` UV displacement), `1.45.dp` chromatic RGB dispersion, 5-step inner Fresnel volume rings, secondary bounce rim, radial interactive touch glow, and viscous metaball/squish physics | Interactive floating hero surfaces (e.g., `AuraBottomIsland` + sliding glass lens indicator, active hero pill capsules) | `<= 1` hero surface per screen (`VIS-RULE-002`, `VIS-RULE-006`) |

**Theme ceiling:** `AuraTheme(material = ...)` is the maximum material any surface below it may render. Components keep their own requested material (hero `LiquidGlass`, standard `Glass`), and the theme lowers it: a `Glass` theme renders `LiquidGlass` requests as `Glass` (Glass Morphism), a `Solid` theme renders every surface `Solid`. The default `LiquidGlass` imposes no limit.

> **Deprecation Note:** `GlassStyle` (`FROSTED`, `LIQUID`) is deprecated in favor of `AuraMaterial` (`Glass`, `LiquidGlass`). Existing component parameters remain binary-compatible through `GlassStyle.toMaterial()`.

---

## 2. Glass Morphism (`AuraMaterial.Glass`) Deep Dive

### 2.1 Industry Reference Benchmarks

Aura's **Glass Morphism** (`AuraMaterial.Glass`) synthesizes four industry-proven frosted-glass architectures into a unified mobile/tablet specification:

1. **Microsoft Fluent 2 — Acrylic Material Recipe:**
   - Defines a 5-stage physical compositing pipeline: **Live Backdrop Capture → Gaussian Blur (`26–30dp`) → Luminosity/Exclusion Blend → Mode-Specific Tint Wash (`70–80%`) → Micro-Noise Grain Texture (`~2%`)**.
   - Prevents digital gradient banding and gives translucent flyouts and sidebars a tangible, sandblasted acrylic texture.
2. **Apple iOS / macOS — System Materials & Vibrancy (`UIBlurEffect`):**
   - Applies a **Color Saturation Boost (`1.22x–1.35x`)** prior to blurring so underlying content does not turn muddy gray, allowing warm and cool hues from photography or artwork to bleed vibrantly through the frosted plane.
3. **Apple visionOS — Spatial Frosted Glass Platters:**
   - Replaces heavy dark borders with an **asymmetric `1.dp` directional specular rim** (lit at top-start, tapering at bottom-end) combined with soft dual-layer ambient occlusion shadows to establish Z-axis depth without clutter.
4. **Modern Product & Dashboard Cards (Malewicz Glassmorphism):**
   - Establishes clear spatial hierarchy over colorful canvases while keeping nested child controls legible through subtle luminance shifts (`±8–12%`) rather than nested blur stacking.

### 2.2 Six Core Architectural Elements of Glass Morphism

1. **Backdrop Gaussian Blur (`blurRadius: 26.dp` Light / `28.dp` Dark):**
   - Eliminates high-frequency background detail (sharp typography strokes, icon edges) and converts underlying content into a smooth, low-frequency ambient color field.
2. **Vibrancy Saturation & Luminosity Matrix (`saturation: 1.35f` Light / `1.22f` Dark):**
   - **Light Mode:** Boosts backdrop saturation (`1.35x`) and lifts mid-tone luminance so dark images passing underneath never turn the glass dingy.
   - **Dark Mode:** Boosts saturation (`1.22x`) while clamping extreme white highlights to prevent harsh bright hotspots under dark glass.
3. **Mode-Separated Translucent Surface Tint (`frostedTint`):**
   - **Light Mode:** Milky Warm/Neutral White (`#FFFFFF` at `72%` alpha).
   - **Dark Mode:** Smoked Obsidian Charcoal (`#1E1E22` / `#1C1E26` at `68%` alpha) with a cool slate undertone to avoid muddy neutral-gray washout.
4. **Micro-Noise / Frosted Grain (`noiseAlpha: 0.018f` Light / `0.022f` Dark):**
   - High-frequency monochromatic grain overlay that breaks up color banding on smooth gradients and provides the tactile surface signature of etched glass.
5. **Asymmetric Specular Hairline Border (`1.dp` Directional Gradient Stroke):**
   - Simulates overhead-left light catching the cut edge of an acrylic sheet (`Brush.linearGradient` from `TopStart` to `BottomEnd`).
   - **Light Mode:** Crisp white catchlight (`strokeStartAlpha = 0.55f → strokeEndAlpha = 0.08f`).
   - **Dark Mode:** Restrained metallic/obsidian rim (`strokeStartAlpha = 0.34f → strokeEndAlpha = 0.06f`) to prevent artificial white "halo" glowing against dark canvases.
6. **Dual-Layer Ambient + Key Shadow (`AuraElevation`):**
   - Casts a wide, diffused ambient shadow (`3× elevation` blur) and a tighter key shadow (`0.4× elevation` Y-offset) strictly outside the clipped glass perimeter.

---

## 3. Liquid Glass (`AuraMaterial.LiquidGlass`) & Floating UI Architecture

While **Glass Morphism** acts as a flat, light-scattering frosted sheet, **Liquid Glass** (inspired by Apple iOS 26 / WWDC 2025) acts as a **curved, light-refracting optical lens with fluid surface tension**:

1. **Convex Bevel Refraction & Center Doming (`LIQUID_GLASS_AGSL`):**
   - Preserves a clearer interior safe zone (`liquidBlurRadius = 10.dp`, `liquidTint = 54%` Light / `66%` Dark) while bending background UV coordinates along an `11.dp` outer bevel (`refraction = 7.5.dp`) using SDF normal gradients ($\nabla \text{SDF}$).
2. **Prismatic Chromatic Dispersion (`dispersion = 1.45.dp`):**
   - Separates Red, Green, and Blue backdrop samples along the curved normal vector, producing a subtle spectral prism fringe at high-curvature corners and during fast motion.
3. **Volumetric Fresnel Rings & Secondary Bounce Rim:**
   - Renders 5 feathered concentric Fresnel inner glow rings (`7.5.dp` inset), a top-left droplet catchlight, and a bottom-right internal bounce reflection that gives the capsule physical thickness.
4. **Interactive Touch Illumination & Gel Bounce (`.interactive()` via `Modifier.fluidPressScale`):**
   - Pressing a Liquid Glass control triggers anisotropic gel compression (`scaleX` slight expansion on `scaleY` compression) and radiates an internal radial touch glow (`FluidInteractiveGlow`) from the contact region.
5. **Floating UI & Scroll-Driven Fluid Minimize (`FloatingGlassScaffold`, `AuraTopAppBar`, `AuraBottomIsland`):**
   - **Progressive Scroll Edge Effect (`ScrollEdgeEffectStyle.soft`):** Uses a clean background luminance veil gradient (`0.90f → 0.45f → 0f`) at the top/bottom viewport edges so status-bar indicators stay 100% legible while cards and imagery visibly refract beneath floating glass capsules.
   - **Contained Glass Pods & Compact Floating Title Pill:** Top navigation actions float as contained glass capsules from initial load (`Y == 0`); when the large header title scrolls away, a centered **Compact Floating Glass Title Pill** emerges smoothly instead of hiding the top bar.
   - **Sliding Glass Lens & Fluid Minimize (`minimizeOnScrollDown`):** The active tab indicator in `AuraBottomIsland` is a refractive **Sliding Glass Lens** (`SlidingGlassLensIndicator`) with velocity-coupled Squish & Stretch (`calculateLiquidSquishStretch`). On downward scroll, the bottom island does not vanish off-screen—it fluidly compacts (`60.dp → 48.dp`, folding tab labels into a slim icon-plus-lens pill) and rebounds on upward scroll or tap.

---

## 4. 3×2 Material × ColorMode Optical Token Matrix

`GlassSurface` and `LiquidMetaballBridge` read every optical value from `AuraTheme.glass` (`AuraGlassDefaults.Light` / `Dark`) and `AuraTheme.colors` (`AuraColorSchemes.Light` / `Dark`). Hardcoded glass tint, blur, or stroke literals in components are forbidden.

| Optical Property / Token | `Solid · Light` | `Solid · Dark` | `Glass Morphism · Light` (`AuraMaterial.Glass`) | `Glass Morphism · Dark` (`AuraMaterial.Glass`) | `Liquid Glass · Light` (`AuraMaterial.LiquidGlass`) | `Liquid Glass · Dark` (`AuraMaterial.LiquidGlass`) |
|---|---|---|---|---|---|---|
| **Surface Fill / Tint** | `surface.floating` (`#FFFFFF`, `98–100%`) | `surface.floating` (`#1E2028`, `98–100%`) | `frostedTint`: White `#FFFFFF` @ **`72%`** | `frostedTint`: Obsidian `#1E1E22` @ **`68%`** | `liquidTint`: White `#FFFFFF` @ **`54%`** | `liquidTint`: Deep Obsidian `#1E1E22` @ **`66%`** |
| **Metaball Bridge Tint** | `surface.floating` (`100%`) | `surface.floating` (`100%`) | `frostedTint` (`72%`) | `frostedTint` (`68%`) | `liquidBridgeTint`: White @ **`68%`** | `liquidBridgeTint`: `#22242A` @ **`62%`** |
| **Backdrop Blur Radius** | `0.dp` | `0.dp` | `blurRadius`: **`26.dp`** | `blurRadius`: **`26–28.dp`** | `liquidBlurRadius`: **`10.dp`** | `liquidBlurRadius`: **`10–12.dp`** |
| **Backdrop Saturation** | `1.0x` | `1.0x` | `saturation`: **`1.20–1.35x`** | `saturation`: **`1.20–1.25x`** | `liquidSaturation`: **`1.32x`** | `liquidSaturation`: **`1.32x`** |
| **Edge Bevel & Refraction** | None | None | None (Flat diffused sheet) | None (Flat diffused sheet) | `bevelWidth = 11.dp`, `refraction = 7.5.dp` | `bevelWidth = 11.dp`, `refraction = 7.5.dp` |
| **Chromatic Dispersion** | None | None | None | None | `dispersion = 1.45.dp` | `dispersion = 1.45.dp` |
| **Surface Texture / Volume** | Flat opaque | Flat opaque | Frosted micro-grain (`~1.8%`) | Frosted micro-grain (`~2.2%`) | 5-step Fresnel rings + Top/Bottom catchlights | 5-step Fresnel rings + Top/Bottom catchlights |
| **Perimeter Border / Rim** | `1.dp` `border.default` (`#E2E8F0`) | `1.dp` `border.default` (`#2D313E`) | `1.dp` Specular `White 55% → 8%` | `1.dp` Specular `White 34% → 6%` | `1.dp` Specular `White 55% → 8%` + Prism Rim | `1.dp` Specular `White 34% → 6%` + Prism Rim |

**Contrast floor (`VIS-RULE-003`):** `frostedTint` (`Glass Morphism`) and `liquidTint` (`Liquid Glass`) in both `Light` and `Dark` modes keep `content.primary` at `>= 4.5:1` over both a pure white (`#FFFFFF`) and a pure black (`#000000`) backdrop; `AuraGlassTest` fails the build if any tint change breaks this mathematical invariant.

---

## 5. Four-Layer Surface Model (`VIS-RULE-001`)

Every Aura screen composes UI across 4 explicit Z-layers so translucent chrome never blurs sibling chrome or competes with primary content:

| Layer | Name | Allowed Materials | Typical `AuraElevation` | Representative Components |
|---|---|---|---|---|
| **Layer 0** | `canvas` | `Solid` | `level0` (`0.dp`) | `AuraScaffold`, `AuraAdaptiveTwoPane`, scrolling feed/editor background |
| **Layer 1** | `content` | `Solid` (edge scrim zone via `FloatingGlassScaffold`) | `level0`–`level1` (`0–2.dp`, scrim `0.16f`–`0.40f`) | `AuraButton`, `AuraTextField`, `AuraNumberField`, `AuraSwitch`, `AuraCheckbox`, `AuraRadio`, `AuraSlider`, `AuraRatingBar`, `AuraDateTimePicker`, `AuraSegmentedButton`, `AuraCard` (`*`) |
| **Layer 2** | `chrome` | `Solid`, `Glass` *(Glass Morphism)*, `LiquidGlass` (`<= 1`) | `level1`–`level3` (`2–12.dp`) | `AuraTopAppBar`, `AuraBottomIsland`, `AuraSearchBar`, `AuraFab`, `AuraIconButton` (`Floating`), `AuraChipButton` (`Glass`), `AuraBadge` (`Glass`) |
| **Layer 3** | `spatial` | `Solid`, `Glass` *(Glass Morphism)*, `LiquidGlass` (`<= 1`) | `level3`–`level4` (`12–24.dp`, scrim `0.40f`–`0.56f`) | `AuraDialog`, `MorphingMorePopover`, `AuraSnackbar`, future `AuraBottomSheet` |

> **`*` Scoped Exception (`AuraCardVariant.Glass`):** `AuraCard` belongs to `layer: "content"` (`Layer 1`) by default and uses `Solid` variants (`Filled`, `Outlined`, `Elevated`) on inline canvases. Its `Glass` (Glass Morphism) variant (`material_allowed: ["Solid", "Glass"]`) is strictly reserved for **Home Launcher** wallpaper-backed widgets or isolated overlay tiles (`CNT-RULE-003`)—never standard scrollable list items.

---

## 6. Core Visual Interaction Rules (`VIS-RULE-001`–`VIS-RULE-007`)

| Rule ID | Principle | Enforcement Requirement |
|---|---|---|
| **`VIS-RULE-001`** | **Layer Boundary Discipline** | `GlassSurface` / `Glass` / `LiquidGlass` are permitted **only** in `chrome` (Layer 2) and `spatial` (Layer 3), plus documented wallpaper-backed widget exceptions. Inline content items (`AuraScaffold` body, form fields, list items) must remain `Solid`. |
| **`VIS-RULE-002`** | **Material by Surface Lifetime** | `Solid` governs persistent workspace content; `Glass` (Glass Morphism) governs persistent/transient floating navigation, sheets, & overlays; `LiquidGlass` is restricted to at most **one focal hero surface per screen**. |
| **`VIS-RULE-003`** | **Contrast & Legibility on Glass** | Foreground text over any `Glass` or `LiquidGlass` surface must maintain `>= 4.5:1` contrast (WCAG AA); icons and hairline borders must maintain `>= 3:1`. Strengthen tint opacity or progressive edge scrim (`FloatingGlassScaffold`) if dynamic backdrops reduce contrast. |
| **`VIS-RULE-004`** | **Hardware & Accessibility Degradation** | Resolve requested material through the single pure pipeline `resolveEffectiveMaterial(requested, sdkInt, reduceTransparency, ceiling)` matching the matrix in §7. **Reduced Transparency**, Battery Saver (`PowerManager.isPowerSaveMode`), or High-Contrast Text (`high_text_contrast_enabled`) must immediately fall back to `Solid`. |
| **`VIS-RULE-005`** | **Elevation & Scrim Ladder** | All shadows and protective scrims must read from `AuraTheme.elevation` (`level0`–`level4`). Hardcoded drop-shadow `Dp` or ad-hoc scrim alpha literals in components are forbidden. |
| **`VIS-RULE-006`** | **Live-Blur Performance Budget** | Render at most **3 simultaneous live-blur (`RenderEffect` / AGSL) surfaces** in a visible viewport and at most **1 `LiquidGlass`** surface per screen. Never nest a live-blur `GlassSurface` inside another live-blur `GlassSurface`. |
| **`VIS-RULE-007`** | **State & Focus Effects** | Pressed, hovered, focused, and selected feedback must be driven exclusively through `AuraColors.interaction` state layers, `Modifier.fluidPressScale`, and `AuraTheme.motion` tokens. |

---

## 7. Material Fallback Matrix (`VIS-RULE-004`)

All components resolve effective materiality via `resolveEffectiveMaterial(requested, sdkInt, reduceTransparency, ceiling)` in `AuraTheme.kt`. The requested material is first capped to `ceiling` (`AuraTheme.material`), then resolved through this matrix:

| Requested Material | `reduceTransparency = true` (All APIs) | Android API 33+ (TIRAMISU+) | Android API 31–32 (S / S_V2) | Android API 26–30 (Pre-S) |
|---|---|---|---|---|
| `AuraMaterial.Solid` | `Solid` | `Solid` | `Solid` | `Solid` |
| `AuraMaterial.Glass` *(Glass Morphism)* | `Solid` | `Glass` (`26.dp` blur `RenderEffect`) | `Glass` (`26.dp` blur `RenderEffect`) | `Solid` |
| `AuraMaterial.LiquidGlass` | `Solid` | `LiquidGlass` (AGSL lens shader + soft blur) | `Glass` (soft blur `RenderEffect`) | `Solid` |

- **Accessibility / Battery Saver (`reduceTransparency = true`):** `GlassSurface` disables blur/AGSL shaders, switches to near-opaque `colors.surface.floating` (`AuraTheme.glass.solidFallbackAlpha`, `0.98f`), and exposes `Modifier.testTag("aura_glass_surface_solid_fallback")` for automated verification.
- **Hardware Pre-Blur Fallback (`API 26–30` with `reduceTransparency = false`):** `resolveEffectiveMaterial` returns `Solid` (`RenderEffect` is unavailable), and `GlassSurface` renders the same opaque fallback as above: no backdrop sampling, no translucent tint over unblurred content.
- **Live updates:** `rememberAuraReduceTransparency()` observes `ACTION_POWER_SAVE_MODE_CHANGED` and the accessibility settings, so surfaces switch to `Solid` while the app is running.

---

## 8. Depth & Elevation Ladder (`AuraElevation`)

| Token | Elevation (`Dp`) | Key Shadow Alpha | Ambient Shadow Alpha | Scrim Alpha | Intended Surface Role |
|---|---|---|---|---|---|
| `level0` | `0.dp` | `0.00f` | `0.00f` | `0.00f` | Canvas, inline inputs, outlined/filled cards |
| `level1` | `2.dp` | `0.06f` | `0.04f` | `0.16f` | Elevated/glass cards, segmented thumb, glass chips, circular action pods, edge scrim tail / popover scrim |
| `level2` | `6.dp` | `0.14f` | `0.08f` | `0.28f` | Scrolled `AuraTopAppBar`, `Floating` icon button, `Secondary`/`Glass` `AuraFab` |
| `level3` | `12.dp` | `0.14f` | `0.08f` | `0.40f` | `Primary`/`Surface` `AuraFab`, protective top/bottom edge scrim peaks in `FloatingGlassScaffold` |
| `level4` | `24.dp` | `0.20f` | `0.12f` | `0.56f` | Modal spatial overlays (`AuraDialog`, future `AuraBottomSheet`) |

---

## 9. Planned Component Governance (Principles Only)

- **`AuraBottomSheet` (`status: planned`):**
  - **Layer:** `spatial` (Layer 3).
  - **Elevation:** `AuraTheme.elevation.level4` (`24.dp`, backdrop scrim `scrimAlpha = 0.56f`).
  - **Allowed Materials:** `["Solid", "Glass"]` (must honor Reduced Transparency -> `Solid`, `>= 4.5:1` text contrast).
  - **Motion Profile:** `Expressive` default for spatial sheet presentation and drag-to-dismiss velocity continuity (`MOT-RULE-004`). Not yet implemented in `:design-system`.

---

## 10. Implementation Files

- [`AuraElevation.kt`](../../design-system/src/main/kotlin/com/example/ds/foundation/elevation/AuraElevation.kt)
- [`AuraGlass.kt`](../../design-system/src/main/kotlin/com/example/ds/foundation/glass/AuraGlass.kt)
- [`AuraTheme.kt`](../../design-system/src/main/kotlin/com/example/ds/foundation/theme/AuraTheme.kt)
- [`GlassSurface.kt`](../../design-system/src/main/kotlin/com/example/ds/component/GlassSurface.kt)
