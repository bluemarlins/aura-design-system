# 02 · Component Hierarchy

## 1. Purpose

Shows every public building block of `:design-system` as one tree, from Foundation tokens up to screen patterns, grouped by **kind** (category) and **function** (intent group). Use it to pick the right component, to see what a component is built on, and to find its spec.

- Spec format: [00-spec-architecture.md](00-spec-architecture.md)
- Quality gate: [01-component-evaluation-ruleset.md](01-component-evaluation-ruleset.md)
- Screen recipes: [`skills/aura-design-system/references/recipes-and-patterns.md`](../../skills/aura-design-system/references/recipes-and-patterns.md)

---

## 2. Layer Model

| Layer | Contents | Depends On | Docs |
|---|---|---|---|
| **L0 · Foundation** | Design tokens and theme (`AuraTheme`) | — | [`docs/foundation/`](../foundation/) |
| **L1 · Primitives** | `AuraText`, `AuraIcon`, `GlassSurface` | L0 | [`primitives/`](primitives/00-primitives-principles.md) |
| **L2 · Components** | Categorized UI components | L0, L1, and (rarely) other L2 components | `actions/`, `inputs/`, `feedback/`, `containment/`, `navigation/`, `layout/` |
| **L3 · Patterns** | Screen-level compositions of L2 components | L0–L2 | [Recipes](../../skills/aura-design-system/references/recipes-and-patterns.md) |

Dependencies only point downward. A primitive never depends on a component, and a component never depends on a pattern.

---

## 3. Hierarchy Tree

```text
Aura Design System
│
├── L0 · Foundation (docs/foundation/)
│   ├── Theme ─────────── AuraTheme
│   │   ├── AuraColorMode            Light / Dark
│   │   ├── AuraMaterial             Solid / Glass / LiquidGlass
│   │   │   └── resolveEffectiveMaterial(requested, sdkInt, reduceTransparency)
│   │   └── rememberAuraReduceTransparency
│   ├── Color
│   │   ├── AuraPalette              Tier 1 raw palette
│   │   ├── AuraColors               Tier 2 roles: background · surface · content · brand · border · status · interaction
│   │   └── AuraColorSchemes         Light / Dark schemes
│   ├── Typography ────── AuraTypography   display · headline · title · body · label · mono
│   ├── Shape ─────────── AuraShapes       extraSmall … extraLarge · full
│   ├── Elevation ─────── AuraElevation / AuraElevationLevel   level0 … level4 (shadow + scrimAlpha)
│   ├── Layout
│   │   ├── AuraLayout               margins · sectionSpacing · gridColumns · gridGutter · maxContentWidth · supportsTwoPane
│   │   └── AuraWindowInfo           AuraWidthClass · AuraHeightClass · AuraDisplayRatio
│   └── Motion
│       ├── AuraMotion               AuraMotionProfile: Standard / Expressive
│       ├── AuraReducedMotion        rememberAuraReducedMotion()
│       └── AuraMotionSemantics      internal mainClock test hooks
│
├── L1 · Primitives (primitives/)
│   ├── Content
│   │   ├── AuraText                 + ProvideAuraContentColorTextStyle · LocalAuraContentColor · LocalAuraTextStyle
│   │   └── AuraIcon                 ImageVector / Painter overloads
│   └── Material Surface
│       └── GlassSurface             Glass / LiquidGlass → Solid fallback
│           ├── GlassBackdropState · rememberGlassBackdropState · Modifier.glassBackdropSource
│           ├── Modifier.fluidPressScale
│           └── LiquidMetaballBridge
│
├── L2 · Components
│   │
│   ├── Actions (actions/) — run a command or switch the current view
│   │   ├── Command Actions
│   │   │   ├── AuraButton           Primary / Secondary / Outline / Ghost / Destructive · Small / Medium / Large
│   │   │   ├── AuraIconButton       Filled / Tonal / Outlined / Ghost / Floating / Destructive · Circle / RoundedSquare
│   │   │   └── AuraFab              Primary / Secondary / Surface / Glass · Small / Regular / Large
│   │   └── Selection Actions        (immediate view change, never a saved form value · BTN-RULE-007)
│   │       ├── AuraSegmentedButton  Surface / Outlined / Glass · 2–5 exclusive options
│   │       └── AuraChipButton       Filter / Tag / Glass (selection) · Assist (command)
│   │
│   ├── Inputs (inputs/) — enter or choose a value the user commits
│   │   ├── Text Entry
│   │   │   ├── AuraTextField        Outlined / Filled / Paperless
│   │   │   ├── AuraNumberField      sanitized numeric / currency + steppers
│   │   │   └── AuraSearchBar        Solid / Glass
│   │   ├── Selection Controls
│   │   │   ├── AuraCheckbox         Unchecked / Checked / Indeterminate
│   │   │   ├── AuraRadio
│   │   │   └── AuraSwitch           Medium / Small
│   │   ├── Range & Value
│   │   │   ├── AuraSlider
│   │   │   └── AuraRatingBar        Small / Medium / Large
│   │   └── Pickers
│   │       └── AuraDateTimePicker   Date / DateTime / YearVintage
│   │
│   ├── Feedback (feedback/) — report system status
│   │   ├── Status Indicator
│   │   │   └── AuraBadge · AuraBadgedBox      Brand / Neutral / Success / Warning / Error / Info · Dot / Small / Medium
│   │   ├── Progress
│   │   │   ├── AuraLinearProgressBar
│   │   │   └── AuraStepProgressBar            AuraProgressVariant: Brand / Success / Warning / Error / Info
│   │   └── Transient Message
│   │       └── AuraSnackbar                   Neutral / Success / Warning / Error
│   │           └── AuraSnackbarHost · AuraSnackbarHostState · rememberAuraSnackbarHostState
│   │
│   ├── Containment (containment/) — group or separate content
│   │   ├── Grouping ──── AuraCard             Elevated / Filled / Outlined / Glass
│   │   ├── Modal ─────── AuraDialog           Standard / Destructive / Glass
│   │   ├── Anchored ──── AuraDropdownMenu     AuraMenuSection · AuraMenuItem (single-choice check)
│   │   └── Separation ── AuraDivider          Horizontal / Vertical · Subtle / Default / Strong
│   │
│   ├── Navigation (navigation/) — move between screens, frame the screen edges
│   │   ├── Top
│   │   │   └── AuraTopAppBar · FloatingTopPillBar · MorphingMorePopover
│   │   │       Style: FloatingCapsule / FloatingSplit / SurfaceDocked
│   │   │       Surface: SolidElevated / TranslucentGlass
│   │   │       Scroll: Pinned / EnterAlways / CompactOnScroll / AdaptiveFloating
│   │   ├── Bottom
│   │   │   └── AuraBottomIsland · FloatingBottomIsland
│   │   └── Scroll-Linked State
│   │       └── AuraAdaptiveBarState           AdaptiveFloatingBarPhase (6 phases)
│   │           └── rememberAuraAdaptiveBarState · BindAdaptiveBarTo{ScrollState | LazyListState | LazyGridState}
│   │
│   └── Layout (layout/) — screen skeleton and responsive arrangement
│       ├── Screen Scaffold
│       │   ├── AuraScaffold                   docked slots, Solid canvas
│       │   └── FloatingGlassScaffold          4-layer edge-to-edge canvas for floating glass chrome
│       └── Adaptive Arrangement
│           ├── AuraAdaptiveTwoPane            master + detail split
│           └── AuraAdaptiveGrid               equal-width grid (gridColumns)
│
└── L3 · Patterns
    ├── Floating Glass Navigation  FloatingGlassScaffold + AuraTopAppBar(AdaptiveFloating) + AuraBottomIsland + AuraAdaptiveBarState
    ├── Responsive Form / Detail   AuraScaffold + maxContentWidth + Inputs
    ├── Adaptive Tablet / Foldable AuraAdaptiveTwoPane + AuraAdaptiveGrid + AuraWindowInfo
    ├── Transient Feedback         scaffold snackbarHost + AuraSnackbarHostState
    └── Modal Confirmation         AuraDialog(Destructive) + AuraButton
```

---

## 4. Composition Dependencies (L2 → L1 / L2)

Taken from direct calls in `design-system/src/main/kotlin/com/example/ds/component/`.

| Component | `AuraText` | `GlassSurface` | Other Components |
|---|---|---|---|
| `AuraTopAppBar` | ✓ | ✓ | `LiquidMetaballBridge`, `AuraAdaptiveBarState` |
| `AuraBottomIsland` | ✓ | ✓ | `LiquidMetaballBridge` |
| `AuraDialog` | ✓ | ✓ | `AuraButton` |
| `AuraSearchBar` | ✓ | ✓ | `AuraBadge` |
| `AuraSnackbar` | ✓ | ✓ | — |
| `FloatingGlassScaffold` | via `ProvideAuraContentColorTextStyle` | backdrop source only | `AuraAdaptiveBarState` |
| `AuraScaffold` | via `ProvideAuraContentColorTextStyle` | — | — |
| All other components | ✓ (except `AuraIconButton`, `AuraProgressBar`, `AuraAdaptiveTwoPane`, `AuraAdaptiveGrid`, which render no text) | — | — |

Glass rendering is concentrated in five `chrome` / `spatial` components, all through `GlassSurface` (`PRM-RULE-003`, `VIS-RULE-001`).

---

## 5. Category Index

| Category | Principles | Specs |
|---|---|---|
| Primitives | [00-primitives-principles.md](primitives/00-primitives-principles.md) | [aura-text](primitives/aura-text.md) · [aura-icon](primitives/aura-icon.md) · [glass-surface](primitives/glass-surface.md) |
| Actions | [00-button-principles.md](actions/00-button-principles.md) | [aura-button](actions/aura-button.md) · [aura-icon-button](actions/aura-icon-button.md) · [aura-fab](actions/aura-fab.md) · [aura-segmented-button](actions/aura-segmented-button.md) · [aura-chip-button](actions/aura-chip-button.md) |
| Inputs | [00-input-principles.md](inputs/00-input-principles.md) | [aura-text-field](inputs/aura-text-field.md) · [aura-number-field](inputs/aura-number-field.md) · [aura-search-bar](inputs/aura-search-bar.md) · [aura-checkbox](inputs/aura-checkbox.md) · [aura-radio](inputs/aura-radio.md) · [aura-switch](inputs/aura-switch.md) · [aura-slider](inputs/aura-slider.md) · [aura-rating-bar](inputs/aura-rating-bar.md) · [aura-date-time-picker](inputs/aura-date-time-picker.md) |
| Feedback | [00-feedback-principles.md](feedback/00-feedback-principles.md) | [aura-badge](feedback/aura-badge.md) · [aura-progress-bar](feedback/aura-progress-bar.md) · [aura-snackbar](feedback/aura-snackbar.md) |
| Containment | [00-containment-principles.md](containment/00-containment-principles.md) | [aura-card](containment/aura-card.md) · [aura-dialog](containment/aura-dialog.md) · [aura-dropdown-menu](containment/aura-dropdown-menu.md) · [aura-divider](containment/aura-divider.md) |
| Navigation | [00-navigation-principles.md](navigation/00-navigation-principles.md) | [aura-top-app-bar](navigation/aura-top-app-bar.md) · [aura-bottom-island](navigation/aura-bottom-island.md) · [aura-adaptive-bar-state](navigation/aura-adaptive-bar-state.md) |
| Layout | — | [aura-scaffold](layout/aura-scaffold.md) · [floating-glass-scaffold](layout/floating-glass-scaffold.md) · [aura-adaptive-two-pane](layout/aura-adaptive-two-pane.md) · [aura-adaptive-grid](layout/aura-adaptive-grid.md) |

Every public component file in `:design-system` now has a spec. When adding a component, add its spec and a leaf in §3 in the same change.

## 6. Showcase Groups (Preview)

The `:design-system-preview` app has a **Showcase** category. Each group is one screen with a grid of "A vs B" comparison cards that show live specimens and a one-line guide for when to use each. Groups are organized by **intent** (what the user needs to do), not by component name, so components from different categories can be compared side by side. Components that do not exist yet appear as `Planned` cards.

| Group | Question it answers | Comparison cards (✅ implemented · ⏳ planned) |
|---|---|---|
| Notify | How do I tell the user something? | ✅ Snackbar vs Dialog · ✅ Snackbar variants · ✅ Dot vs Count badge · ⏳ Toast vs Snackbar · ⏳ Modal vs Bottom sheet · ⏳ Banner vs Snackbar · ⏳ Tooltip vs Popover · ⏳ Inline error vs Dialog · ⏳ Coachmark vs Tooltip |
| Select | How do I let the user choose? | ✅ Switch vs Checkbox · ✅ Radio vs Checkbox · ✅ Segmented vs Filter chip · ⏳ Tabs vs Segmented · ⏳ Dropdown vs Radio |
| Input | How do I take a value? | ✅ TextField variants · ✅ NumberField vs Slider · ✅ SearchBar vs TextField · ⏳ Stepper vs Slider |
| Action | How do I invite an action? | ✅ Button emphasis · ✅ Button vs IconButton vs FAB · ✅ Assist chip vs Button |
| Progress | How do I show waiting and status? | ✅ Linear vs Step progress · ✅ Badge vs Chip · ⏳ Spinner vs Skeleton |
| Contain | How do I group and separate content? | ✅ Card variants · ✅ Divider vs spacing · ✅ Card vs Dialog |

Candidate follow-up groups: Navigation (top bar styles, bottom island vs tabs, scaffold choice) and Material (Solid vs Glass vs LiquidGlass). Groups are data in `design-system-preview/.../ui/showcase/`; add a new `ShowcaseGroup` and its catalog item to extend.
