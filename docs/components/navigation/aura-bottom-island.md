---
id: "aura.component.bottom-island"
name: "AuraBottomIsland"
category: "navigation"
status: "ready"
version: "1.0.0"
layer: "chrome"
material_allowed: ["Solid", "Glass", "LiquidGlass"]
motion_track: "Expressive"
platforms:
  android: "io.github.bluemarlins.aura.component.AuraBottomIsland"
  ios: "AuraDS.AuraBottomIsland (planned)"
  web: "@aura-ds/react.AuraBottomIsland (planned)"
tokens:
  - "colors.surface.floating"
  - "colors.content.primary"
  - "colors.content.secondary"
  - "colors.brand.primary"
  - "colors.brand.onPrimary"
  - "colors.border.subtle"
  - "colors.interaction.selectedContainer"
  - "colors.interaction.scrim"
  - "typography.labelMedium"
  - "typography.labelSmall"
  - "motion.pressInSpring"
  - "motion.pressOutSpring"
  - "motion.stateChangeDurationMillis"
  - "motion.minPressDurationMillis"
---

# AuraBottomIsland & FloatingBottomIsland

> Detached floating stadium navigation island (`60dp` height, full capsule radius) with Split Island secondary action support, Gooey Metaball liquid merging, and an Elastic Squish-and-Stretch spring-sliding active pill indicator.

---

## 1. Intent & Usage Principles

Traditional bottom navigation bars occupy an opaque rectangle pinned to the bottom edge, interrupting edge-to-edge scrolling depth. `AuraBottomIsland` (`FloatingBottomIsland`) elevates primary destination switching into a sculpted floating glass island separated from the navigation bar inset by `16dp`, letting live Layer 0 scrollable content flow cleanly beneath while optional primary quick-create or search actions float beside it as a Split Island.

### Problem

Apps with 3–5 top-level destinations need persistent, thumb-reachable navigation that leaves content visible behind it on edge-to-edge screens.

### When to Use

- Top-level navigation across core hubs in Aura apps (**Home Launcher**, **My Planner**, **My Finance**, **My Drink Diary**).
- Spatial & edge-to-edge screens combining destination switching with a contextual quick-create / search Split Island.

### When NOT to Use & Alternatives

- **Detail/editor flows with Back navigation:** Use `FloatingTopPillBar` alone to maximize editing space.
- **Segmented filtering within one dataset (`All | In-Stock | Wishlist`):** Use `AuraSegmentedButton` or `AuraChipButton`.
- **Two mutually exclusive inline choices inside forms:** Use `AuraRadio`.

### Context Rules

- **`BISL-RULE-001` (Split Island vs. Unified Bar):** Pair the main navigation capsule with a `60 × 60dp` trailing Split Action capsule (`10dp` spacing) when screens have a high-frequency primary action (`New Entry`, `Quick Search`); merge seamlessly via Metaball physics when contextual actions hide.
- **`BISL-RULE-002` (Bottom Safe-Zone Content Clearance):** Any scrollable container hosted under `FloatingBottomIsland` MUST apply at least `Island Height (60dp) + Navigation Bar Inset + Bottom Margin (16dp) + Breathing Room (12dp)` as bottom `contentPadding` (`FloatingGlassScaffold` provides this automatically).
- **`BISL-RULE-003` (3–5 Core Destinations Only):** Host `3–5` top-level destinations with concise single-word labels; overflow utilities belong inside `FloatingTopPillBar`'s `[More]` popover.
- **`BISL-RULE-004` (Optical Legibility Safe Zone):** Even under `GlassStyle.LIQUID` edge bevel refraction, the inner tab safe zone preserves soft backdrop blur and adaptive tint (`White 72%` light / `#1E1E22 68%` dark) so selected and unselected icons maintain `>= 4.5:1` / `>= 3:1` contrast over vivid photography.

### Do / Don't

| Do | Don't |
|---|---|
| Pair `FloatingBottomIsland` with `FloatingGlassScaffold` so bottom `contentPadding` clears the island cleanly. | Cut off the bottom `76dp` of the screen with an opaque rectangle ("fake floating"). |
| Keep `2–5` tabs with crisp Outlined (unselected, `60%` opacity) and high-contrast Filled (selected) icons. | Crowd `6+` tabs into a narrow pill causing squeezed hit areas below `48dp`. |
| Use interruptible `Spring(dampingRatio = 0.65f, stiffness = 450f)` + Liquid Squish-and-Stretch on tab hops. | Drive the sliding active pill with flat `LinearEasing` curves. |
| Provide deterministic `Canvas` glass fallback on API < 33 without crashing. | Assume `RuntimeShader` AGSL exists on every device without fallback handling. |

---

## 2. Anatomy & Canonical Part IDs

```text
  ╭───────────────────────────────────────────────╮   ╭──────╮
  │  (⌂)        (☰)        (★)        (⚙)         │   │  +   │
  │  Home      Cellar     Ratings   Settings      │   ╰──────╯
  ╰───────────────────────────────────────────────╯   splitAction
        ^ activeIndicator (sliding glass lens)
```

| Part ID | Responsibility | Required | Semantics Owner |
|---|---|---|---|
| `root` | `60dp` full-capsule navigation island (`GlassSurface`) | Yes | `selectableGroup()` |
| `tab` | One `AuraBottomIslandItem` (icon + label) | Yes | `Role.Tab` (`selectable`) |
| `tabIcon` / `tabLabel` | `icon` / `selectedIcon` and `label` of a tab | Yes | Merged into `tab` |
| `activeIndicator` | Sliding glass lens under the selected tab (`SlidingGlassLensIndicator`) | Yes | Decorative |
| `splitAction` | `60dp` circular action island (`splitActionIcon`) | No | `Role.Button` (`splitActionLabel`) |
| `metaballBridge` | `LiquidMetaballBridge` linking `root` and `splitAction` when `isActionSplit = false` | Conditional | Decorative |

---

## 3. Variant, Size & Material Matrix

### Quick Spec

Measured against the rendered component by `SpecMeasurementTest`. Keys are `<Part ID>.<metric>`; dimensions in `dp`, type in `sp`.

<!-- QUICK-SPEC:BEGIN -->
```yaml
sizes:
  Default:
    root.height: 60
    tab.minHeight: 48
    splitAction.size: 60
```
<!-- QUICK-SPEC:END -->

- **Outer Margins:** `20dp` horizontal, `16dp` above `WindowInsets.navigationBars`.
- **Main Navigation Capsule:** `60dp` height, `RoundedCornerShape(50)`, `>= 48 × 48dp` per-tab hit area.
- **Split Action Island:** `60 × 60dp` circle separated by `10dp` when `isActionSplit = true`, fused/linked by `LiquidMetaballBridge` when transitioning with `isActionSplit = false`.
- **Dual Materiality (`GlassStyle.FROSTED` / `GlassStyle.LIQUID`):**
  - **`FROSTED`:** `28dp` blur + `1.3x` saturation boost, adaptive Light/Dark tint, `1dp` diagonal specular border, dual key + ambient shadow (`Y +10dp`, `24dp` blur, `15%` alpha).
  - **`LIQUID`:** `8dp` edge bevel convex refraction (`RuntimeShader` API 33+ with Canvas fallback), `~1.2dp` RGB dispersion, `6dp` Fresnel inner volume glow, Gooey Metaball bridge (`LiquidMetaballBridge`), and velocity-coupled **Elastic Squish & Stretch** on the sliding active tab indicator.

---

## 4. State Precedence & Behavior

**State Precedence:** `Pressed (0.92x scale)` > `Transitioning (Sliding + Liquid Squish & Stretch)` > `Selected (Filled + Label)` > `Unselected (Outlined @ 60% alpha)`.

---

## 5. Semantic Token Bindings

| Element | `GlassStyle.FROSTED` | `GlassStyle.LIQUID` |
|---|---|---|
| Island Surface Tint | Light `White 72%` / Dark `#1E1E22 68%` over `28dp` blur | Light `White 56%` / Dark `#1E1E22 52%` over soft-blurred AGSL lens |
| Specular Rim | Diagonal `1dp` `White 55% -> 8%` stroke | Diagonal `1dp` specular + `6dp` Fresnel inner glow (`White 35%`) |
| Sliding Active Pill | `brand.primary @ 0.16f` / `interaction.selectedContainer` | Translucent convex lens pill + `border.subtle` highlight |
| Selected Tab Icon & Label | `brand.primary` / `content.primary`, `typography.labelSmall` | High-contrast Filled icon (`content.primary` / `brand.primary`) + expanded label |
| Unselected Tab Icon | `content.secondary` at `0.60f` alpha | Outlined icon at `0.60f` alpha |
| Split Action Island | Matching frosted circle with `brand.primary` / `content.primary` icon | Matching liquid droplet circle merged via `LiquidMetaballBridge` |
| Bottom Edge Protection Scrim | `color.interaction.scrim` progressive vertical gradient | `color.interaction.scrim` progressive vertical gradient |

---

## 6. Interaction Motion Contract

Consumes `AuraTheme.motion` (`Expressive` track recommended for floating island chrome) and interruptible spring curves (`Linear` curves prohibited):

1. **Interactive Press Scale:** Holding any tab or the Split Action island compresses smoothly to `0.92x` via `AuraTheme.motion.pressInSpring()` and rebounds on release with `AuraTheme.motion.spatialSpring()`.
2. **Sliding Active Indicator:** Animates horizontally between tab centers with `AuraTheme.motion.spatialSpring()`.
3. **Elastic Squish & Stretch (`AuraMaterial.LiquidGlass` / legacy `GlassStyle.LIQUID`):** During indicator travel between indices, horizontal stretch scales smoothly via `AuraTheme.motion.elasticStretchSpring()` mid-flight and gently squishes upon settling into the destination slot.
4. **Gooey Metaball Split/Merge:** Toggling `isActionSplit` animates the gap (`0dp <-> 10dp`) with an elastic bridge (`LiquidMetaballBridge`) whose concave liquid neck snaps cleanly past break threshold.
5. **Reduced Motion (`ANIMATOR_DURATION_SCALE == 0f`):** Replaces overshoot springs and squish deformation with instant `snap()` state transitions.

---

## 7. Responsive Layout Behavior (`Smallest Width × Display Ratio`)

| Profile | Width / Ratio Profile | Island Layout Adaptation |
|---|---|---|
| `ExtraSmall` | `sw < 360dp` | Reduces horizontal outer margin cleanly while protecting `>= 48dp` per tab; condenses tab labels gracefully if width tightens. |
| `Compact` | `360–599dp` | Standard `20dp` horizontal margins, `60dp` capsule height, `10dp` Split Island spacing over full-width edge-to-edge canvas. |
| `Square` | `~1:1` Multi-window | Caps total island group max width at `520dp` centered horizontally so stretched pop-up windows retain balanced proportions. |
| `UltraWide` | `ratio >= 2.1` | Anchors comfortably within center-bottom safe band (`maxWidth = 560.dp`) so tabs don't spread awkwardly far across landscape covers. |
| `Expanded` | `width >= 840dp` | Centers floating island within `560dp` max width (docked bottom-center over multi-column canvas) preserving ergonomic thumb reach. |

---

## 8. Accessibility & Guardrails

- [ ] Each navigation tab registers `Role.Tab` via `Modifier.selectable` inside a `Modifier.selectableGroup()` container.
- [ ] Split Action circular capsule exposes `Role.Button` with non-empty `contentDescription` (`splitActionLabel`).
- [ ] All interactive tab targets and the Split Action capsule enforce at least `48dp × 48dp` touch bounds.
- [ ] Selected vs. unselected distinction pairs color change (`brand.primary`) with structural indicator pill, icon style (`Filled` vs. `Outlined`), and visible text label.
- [ ] **Reduced Transparency & Contrast (`VIS-RULE-003`–`004`):** Maintains `>= 4.5:1` label contrast (`>= 3:1` icons/borders) over blurred backdrops and degrades cleanly to opaque `AuraMaterial.Solid` (`colors.surface.floating`) via `AuraTheme.effectiveMaterial(...)` when Reduced Transparency, Battery Saver, or High-Contrast Text is enabled.

---

## 9. Platform Bindings

### 9.1 Android (Jetpack Compose)

- **Source:** [`AuraBottomIsland.kt`](../../../design-system/src/main/kotlin/io/github/bluemarlins/aura/component/AuraBottomIsland.kt)

<!-- API:ANDROID:BEGIN -->
```kotlin
data class AuraBottomIslandItem(
    val id: String,
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector = icon,
)

@Composable
fun FloatingBottomIsland(
    items: List<AuraBottomIslandItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    glassStyle: GlassStyle = GlassStyle.LIQUID,
    splitActionIcon: ImageVector? = null,
    splitActionLabel: String? = null,
    isActionSplit: Boolean = true,
    onSplitActionClick: (() -> Unit)? = null,
)

@Composable
fun AuraBottomIsland(
    items: List<AuraBottomIslandItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    glassStyle: GlassStyle = GlassStyle.FROSTED,
    splitActionIcon: ImageVector? = null,
    splitActionLabel: String? = null,
    isActionSplit: Boolean = true,
    onSplitActionClick: (() -> Unit)? = null,
)
```
<!-- API:ANDROID:END -->

### 9.2 iOS (SwiftUI Reference)

<!-- API:IOS:BEGIN -->
```swift
public struct AuraBottomIslandItem: Identifiable, Sendable {
    public let id: String
    public let label: String
    public let systemImage: String
    public let selectedSystemImage: String
}

public struct FloatingBottomIsland: View {
    public init(
        items: [AuraBottomIslandItem],
        selectedIndex: Binding<Int>,
        glassStyle: GlassStyle = .liquid,
        splitActionSystemImage: String? = nil,
        splitActionLabel: String? = nil,
        isActionSplit: Bool = true,
        onSplitAction: (() -> Void)? = nil
    )
}
```
<!-- API:IOS:END -->

### 9.3 Web (React / TypeScript Reference)

<!-- API:WEB:BEGIN -->
```ts
export interface AuraBottomIslandItem {
  id: string;
  label: string;
  icon: React.ReactNode;
  selectedIcon?: React.ReactNode;
}

export interface FloatingBottomIslandProps {
  items: AuraBottomIslandItem[];
  selectedIndex: number;
  onItemSelected: (index: number) => void;
  glassStyle?: 'FROSTED' | 'LIQUID';
  splitActionIcon?: React.ReactNode;
  splitActionLabel?: string;
  isActionSplit?: boolean;
  onSplitActionClick?: () => void;
  className?: string;
}
```
<!-- API:WEB:END -->
