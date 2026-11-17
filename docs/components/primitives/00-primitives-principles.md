# 00 · Primitives Principles

## 1. Purpose

Defines the **Primitive Layer** of Aura: the small set of building blocks that every higher-level component composes instead of reaching for Compose Foundation or Material 3 directly. Primitives sit between the Foundation tokens (`AuraTheme.colors`, `typography`, `elevation`, `motion`) and the categorized components (`actions/`, `inputs/`, `feedback/`, `containment/`, `navigation/`, `layout/`).

```text
Foundation (AuraTheme tokens)
   └── Primitives (AuraText · AuraIcon · GlassSurface)
          └── Components (AuraButton, AuraTopAppBar, AuraSnackbar, …)
                 └── Patterns (screen recipes)
```

See [02-component-hierarchy.md](../02-component-hierarchy.md) for the full tree.

---

## 2. Primitive Taxonomy

| Primitive | Role | Composed By |
|---|---|---|
| [`AuraText`](aura-text.md) | Token-bound text rendering with content-color and text-style inheritance (`ProvideAuraContentColorTextStyle`) | Almost every component (`AuraButton`, `AuraTextField`, `AuraBadge`, `AuraSnackbar`, …) |
| [`AuraIcon`](aura-icon.md) | Token-tinted `ImageVector` / `Painter` glyph with inherited content color | Icon slots of `AuraButton`, `AuraIconButton`, `AuraFab`, navigation chrome |
| [`GlassSurface`](glass-surface.md) | Floating `Glass` / `LiquidGlass` material container sampling the live backdrop, with `Solid` fallback | `AuraTopAppBar`, `AuraBottomIsland`, `AuraDialog`, `AuraSearchBar`, `AuraSnackbar` |

---

## 3. Core Primitive Rules

- **`PRM-RULE-001` (Compose Through Primitives):** Components render text with `AuraText`. Icon content is exposed as `@Composable` slots (`leadingIcon`, `icon`) that callers fill with `AuraIcon`; when a component draws a glyph itself it should use `AuraIcon`. Calling `BasicText` / `Image` directly inside `:design-system` components bypasses content-color inheritance.
- **`PRM-RULE-002` (Inherit, Don't Hardcode, Content Color):** Containers publish their foreground color once via `ProvideAuraContentColorTextStyle`; descendant `AuraText` / `AuraIcon` inherit it. Pass an explicit `color` / `tint` only for intentional semantic overrides (e.g., `content.secondary` supporting text).
- **`PRM-RULE-003` (Single Glass Pipeline):** All translucent material in Aura is rendered by `GlassSurface` and resolved through `AuraTheme.effectiveMaterial(...)` (`VIS-RULE-004`). Components never build their own blur `RenderEffect`, so Reduced Transparency, API-level and Battery Saver fallbacks stay consistent.
- **`PRM-RULE-004` (Layer Discipline):** `AuraText` and `AuraIcon` are layer-agnostic and may appear on any layer. `GlassSurface` is restricted to `chrome` and `spatial` layers (`VIS-RULE-001`); content-layer components (`AuraCard` body, form fields, list rows) must not invoke it.
- **`PRM-RULE-005` (Primitives Are Not Screens):** Primitives carry no layout opinion (padding, max width, insets). Spacing and responsive behavior are owned by the composing component or by `layout/` containers.

---

## 4. Evaluation Notes

- Primitive specs follow the same two-layer structure ([00-spec-architecture.md](../00-spec-architecture.md)) and quality gate ([01-component-evaluation-ruleset.md](../01-component-evaluation-ruleset.md)).
- Passive primitives (`AuraText`, `AuraIcon`) declare `motion_track: "none"`; interactive or animated primitives (`GlassSurface`) declare `Standard` or `Expressive` (`EVAL-008`).

### Known Deviations

| Component | Deviation | Rule | Note |
|---|---|---|---|
| `AuraBottomIsland` | Draws item and split-action glyphs with `Image` + `ColorFilter.tint` instead of `AuraIcon` | `PRM-RULE-001` | Visually equivalent (explicit tint is passed). Migration candidate; no behavior change required. |
