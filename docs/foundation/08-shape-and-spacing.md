# 08 · Shape & Spacing

## 1. Purpose

Defines the corner-radius scale (`AuraShapes`) and the spacing rhythm every Aura component and consumer screen uses, so generated UI picks values from one short list instead of inventing new ones.

---

## 2. Corner Shape Scale (`AuraTheme.shapes`)

| Token | Radius | Use for |
|---|---|---|
| `shapes.extraSmall` | `4dp` | Step progress segments, compact tags, skeleton text lines |
| `shapes.small` | `8dp` | Progress bars, thumbnails, steppers, value pills, `Medium` badge |
| `shapes.medium` | `12dp` | Snackbars, menu rows, inline media cards |
| `shapes.large` | `16dp` | Cards, grouped panels, date/time picker panel |
| `shapes.extraLarge` | `24dp` | Dialogs, bottom sheets, spatial containers |
| `shapes.full` | `50%` (`CircleShape`) | Capsules, pills, switches, radios, FABs, segmented tracks |

### Rules

- **`SHP-RULE-001`:** Screens and new components take radii from `AuraTheme.shapes`. A literal `RoundedCornerShape(n.dp)` is allowed only for a component-owned size ramp listed in §2.1.
- **`SHP-RULE-002`:** Nested surfaces step down one token from their container (`large` card → `medium` inner media → `small` chip), so inner corners never look wider than outer ones.
- **`SHP-RULE-003`:** Anything that is a capsule at every size uses `shapes.full`, not a radius equal to half its height.

### 2.1 Component-owned radii (off-scale by design)

These follow a per-size ramp or optical sizing and stay in the component. Their specs state the value and `SpecMeasurementTest` measures it where a Quick Spec lists `cornerRadius`.

| Component | Radius | Why off-scale |
|---|---|---|
| `AuraButton` | `8dp` / `10dp` / `12dp` (`Small` / `Medium` / `Large`) | Radius grows with height |
| `AuraIconButton` (`RoundedSquare`) | `10dp` / `12dp` / `14dp` | Radius grows with size |
| `AuraTextField`, `AuraNumberField` | `10dp` | Optical match to `48dp` field height |
| `AuraCheckbox` | `6dp` | Small `20dp` box |
| `AuraDropdownMenu` (panel) | `20dp` | Floating glass panel |
| `AuraDialog` | `24dp` | Equals `extraLarge`; kept as a constant because it also feeds the glass `cornerRadiusHint` |

---

## 3. Spacing

Aura has no separate spacing token object. Spacing comes from two sources:

### 3.1 Screen-level spacing (`AuraTheme.layout`)

Use these for anything between a screen edge and its content, between sections, and between grid items. Values change with the window profile; see [05 · Responsive & Adaptive Layout](05-responsive-layout.md) §4.

| Token | Role |
|---|---|
| `layout.screenMarginHorizontal` | Left/right inset of screen content (`12–32dp`) |
| `layout.screenMarginVertical` | Top/bottom inset of screen content (`12–24dp`) |
| `layout.sectionSpacing` | Gap between page sections (`16–28dp`) |
| `layout.gridGutter` | Gap between grid columns and rows (`10–20dp`) |

### 3.2 Component-internal spacing (`4dp` grid)

Inside components and small groups, use multiples of `4dp`:

| Step | Typical use |
|---|---|
| `4dp` | Label-to-control gap, footer gap, step segment gap |
| `8dp` | Icon-to-text gap, chip row gap, dense list padding |
| `12dp` | Card inner padding (`Compact`), paired controls |
| `16dp` | Standard card and sheet padding, button horizontal padding (`Medium`) |
| `24dp` | Dialog padding, large section blocks |

- **`SPC-RULE-001`:** Component-internal gaps are multiples of `4dp`. Use `2dp` or `6dp` only for optical alignment inside a single control, as the specs for that control state.
- **`SPC-RULE-002`:** Never hardcode a screen margin. Use `layout.screenMarginHorizontal` so cover screens, phones, and tablets each get the right inset.

---

## 4. Implementation Files

- [`AuraShapes.kt`](../../design-system/src/main/kotlin/io/github/bluemarlins/aura/foundation/shape/AuraShapes.kt)
- [`AuraLayout.kt`](../../design-system/src/main/kotlin/io/github/bluemarlins/aura/foundation/layout/AuraLayout.kt)
