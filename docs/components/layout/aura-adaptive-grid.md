---
component_id: "aura.component.adaptive-grid"
name: "AuraAdaptiveGrid"
category: "layout"
status: "ready"
layer: "canvas"
material_allowed: ["Solid"]
motion_track: "none"
version: "0.1.0"
platforms:
  android: "com.example.ds.component.AuraAdaptiveGrid"
  ios: "AuraDS.AuraAdaptiveGrid (planned)"
  web: "@aura-ds/react.AuraAdaptiveGrid (planned)"
tokens:
  - "layout.gridColumns"
  - "layout.gridGutter"
---

# AuraAdaptiveGrid

> Cross-platform specification for Aura's responsive equal-width grid. Column count and gutter come from `AuraTheme.layout`, so the same screen shows 1, 2, or 3 columns across cover displays, phones, foldables, and tablets. See [05-responsive-layout.md](../../foundation/05-responsive-layout.md).

---

## 1. Intent & Usage Principles

### Problem

Collections of equal-priority cards (portfolio tiles, bottle cards, widget presets, component catalogs) look sparse as a single column on foldables and tablets, while hand-written `if (width > 600)` splits drift from the system breakpoints.

### When to Use

- Homogeneous, equal-priority items: **My Finance** asset tiles, **My Drink Diary** bottle cards, **Home Launcher** widget presets, `:design-system-preview` catalog cards and Showcase comparison cards.
- Short-to-medium collections placed inside an existing scroll container (e.g., a section of a scrolling detail screen).

### When NOT to Use & Alternatives

| Situation | Alternative | Reason |
|---|---|---|
| Master + detail of different priority | `AuraAdaptiveTwoPane` | Asymmetric panes with independent scroll |
| Long or unbounded feeds (hundreds of items) | `LazyVerticalGrid` with `GridCells.Fixed(AuraTheme.layout.gridColumns)` | `AuraAdaptiveGrid` composes every item eagerly |
| Items of very different heights in a masonry flow | `LazyVerticalStaggeredGrid` | Rows here align to the tallest cell |

### Context Rules

- **`GRD-RULE-001` (Theme-Driven Columns):** Use the default `columns = AuraTheme.layout.gridColumns`. Override only for a documented content reason (e.g., a 2-up comparison), never with raw width checks.
- **`GRD-RULE-002` (No Nested Scroll):** `AuraAdaptiveGrid` is a non-lazy `Column` of `Row`s; place it inside the screen's scroll container instead of wrapping it in its own.
- **`GRD-RULE-003` (Stable Trailing Row):** Incomplete last rows are padded with weighted spacers so cells keep the same width as full rows.

### Do / Don't

| Do | Don't |
|---|---|
| `AuraAdaptiveGrid(items = assets) { AuraCard(…) }` inside a `verticalScroll` screen body. | Put a `LazyColumn` inside each grid cell. |
| Let cards fill their cell (`Modifier.fillMaxWidth()`). | Give cells fixed widths that break when the column count changes. |
| Switch to `LazyVerticalGrid` for large feeds. | Render 500 items with `AuraAdaptiveGrid`. |

---

## 2. Anatomy & Canonical Part IDs

```text
root (Column, spacedBy(verticalSpacing))
 └── row × ceil(n / columns)  (Row, spacedBy(horizontalSpacing))
      ├── cell (Box, weight 1f) → itemContent(item)
      └── filler (Spacer, weight 1f) for incomplete last row
```

| Part ID | Responsibility | Required |
|---|---|---|
| `root` | Vertical stack of rows | Yes |
| `row` | One line of up to `columns` cells | Yes |
| `cell` | Equal-weight slot hosting `itemContent` | Yes |
| `filler` | Keeps last-row cell width equal | Auto |

`columns <= 1` renders a single full-width column. An empty `items` list renders nothing.

---

## 3. Responsive Layout Behavior (`Adaptive Behavior Matrix`)

| `AuraLayout` Profile | Default `gridColumns` | Gutter |
|---|---|---|
| **`ExtraSmall`** (Cover `sw < 360dp`) | `1` | `layout.gridGutter` |
| **`Compact`** (Phone Portrait) | `1` | `layout.gridGutter` |
| **`UltraWide`** (Phone Landscape) | `2` | `layout.gridGutter` |
| **`Medium` / `Square`** (Foldable `~1:1`) | `2` | `layout.gridGutter` |
| **`Expanded`** (Tablet / DeX) | `3` | `layout.gridGutter` |

Exact values are owned by `AuraLayoutDefaults` in [05-responsive-layout.md](../../foundation/05-responsive-layout.md); the grid only reads them.

---

## 4. Semantic Token Bindings

| Part | Token Binding |
|---|---|
| Column count | `AuraTheme.layout.gridColumns` |
| Horizontal / vertical spacing | `AuraTheme.layout.gridGutter` |

---

## 5. Interaction Motion & Accessibility Contract

- **Interaction Motion (`motion_track: "none"`):** Passive structural container with no press target or animation; cells own their motion (e.g., `AuraCard` press feedback).
- **Accessibility:**
  - Focus and TalkBack order run row by row, start to end, matching visual reading order.
  - The grid adds no semantics; collection semantics (`CollectionInfo`) are the caller's responsibility when needed.
  - Cell width never drops below what the column count allows, so child hit targets keep their `>= 48dp` minimum.

---

## 6. Platform Bindings (Layer B)

### 6.1 Android (Jetpack Compose)

<!-- API:ANDROID:BEGIN -->
```kotlin
@Composable
fun <T> AuraAdaptiveGrid(
    items: List<T>,
    modifier: Modifier = Modifier,
    columns: Int = AuraTheme.layout.gridColumns,
    horizontalSpacing: Dp = AuraTheme.layout.gridGutter,
    verticalSpacing: Dp = AuraTheme.layout.gridGutter,
    itemContent: @Composable (T) -> Unit,
)
```
<!-- API:ANDROID:END -->

### 6.2 iOS (SwiftUI Reference)

<!-- API:IOS:BEGIN -->
```swift
struct AuraAdaptiveGrid<Item: Identifiable, Cell: View>: View {
    let items: [Item]
    var columns: Int? = nil   // nil -> auraTheme.layout.gridColumns
    @ViewBuilder var cell: (Item) -> Cell
}
```
<!-- API:IOS:END -->

### 6.3 Web (React / TypeScript Reference)

<!-- API:WEB:BEGIN -->
```tsx
export interface AuraAdaptiveGridProps<T> {
  items: T[];
  columns?: number;   // defaults to var(--aura-grid-columns)
  renderItem: (item: T) => React.ReactNode;
}
```
<!-- API:WEB:END -->
