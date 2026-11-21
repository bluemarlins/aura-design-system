# Aura Navigation Principles

Aura Design System shifts top-level chrome from rigid full-bleed slabs to a **Canvas-First Floating Concept**. Whether a user is writing in My Planner, reviewing charts in My Finance, capturing wine/whiskey labels in My Drink Diary, or customizing widgets on the Home Launcher, navigation controls float lightly over live content and adapt smoothly across `Smallest Width × Display Ratio` breakpoints.

## Core Navigation Rules

- **`NAV-RULE-001` (Floating First, Docked by Exception)**: Default to `FloatingCapsule` or `FloatingSplit` top bars on canvas, journal, gallery, dashboard, and detail screens so scrolled content remains perceptible behind elevated pills. Reserve `SurfaceDocked` strictly for high-density spreadsheets or form lists where a continuous full-width bottom divider anchors structured rows. Short task screens (create / edit forms, step wizards) stay floating: keep the `FloatingCapsule` top bar and move the commit action (`Save`, `Next`) into `AuraFloatingActionBar` instead of a trailing inline button, so task screens share the top-level floating language.
- **`NAV-RULE-002` (Disciplined Action Budget)**: A top app bar holds at most **1 navigation trigger** (Back / Menu / Close) and **2–3 trailing actions** (`AuraIconButton` or a compact contextual `AuraChipButton`). Overflow additional utilities into a menu rather than squeezing >3 icons into a floating capsule.
- **`NAV-RULE-003` (Single Insets Ownership)**: Top system-bar and cutout insets (`statusBars`, `displayCutout`) are owned either by `AuraTopAppBar` directly (default when floating standalone) or explicitly coordinated with `AuraScaffold` so top margins are never double-padded.
- **`NAV-RULE-004` (Pane-Scoped Chrome in Two-Pane Mode)**: When `AuraAdaptiveTwoPane` opens side-by-side panes on `Expanded` screens (`>= 840dp`), avoid stretching one monolithic 1200dp slab across both columns. Instead, let each pane host lightweight pane-scoped chrome (`FloatingSplit` island for detail actions, clean header in the primary column).
- **`NAV-RULE-005` (Scroll-Linked Progression & Glass Restraint)**: Floating navigation stays lightweight (`0dp` resting separation or `AuraTheme.elevation.level1`/`level2` soft shadow over clean backgrounds) and morphs elevation/frost cleanly only once scrollable content passes underneath. Tied to **`VIS-RULE-001`**, **`VIS-RULE-003`**, and **`VIS-RULE-004`**: translucent navigation chrome MUST guarantee contrast (`>= 4.5:1` titles, `>= 3:1` icons/borders), permit at most one `LiquidGlass` hero per screen, and degrade cleanly to `Solid` under Reduced Transparency (`AuraTheme.reduceTransparency`), Battery Saver, High Contrast Text, or pre-API 31 devices.

## Navigation Taxonomy (Planned & Ready)

| Component | Category | Concept Role | Spec Status |
|---|---|---|---|
| `AuraTopAppBar` | Navigation | Floating Pill / Capsule / Split / Docked top bar with Morphing Popover & Dual Glass (`chrome` layer) | `ready` |
| `AuraBottomIsland` | Navigation | Floating Bottom Island with Split Action, Metaball Merge, & Elastic Sliding Pill (`chrome` layer, `Expressive` track) | `ready` |
| `AuraAdaptiveBarState` | Navigation (state) | Scroll-linked phase coordinator syncing `AuraTopAppBar` (`AdaptiveFloating`) and `FloatingBottomIsland` hide/reveal | `ready` |
| `AuraBottomSheet` | Navigation / Overlays | Floating modal & standard sheet surfaces (`spatial` layer, `level4` elevation, `Expressive` default, gesture-continuous drag/dismiss; principles only) | `planned` |
