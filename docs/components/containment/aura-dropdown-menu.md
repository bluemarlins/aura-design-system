---
id: "aura-dropdown-menu"
name: "AuraDropdownMenu"
category: "containment"
status: "ready"
version: "1.2.0"
layer: "spatial"
material_allowed: ["Solid"]
motion_track: "Expressive"
platforms:
  android: "com.example.ds.component.AuraDropdownMenu"
  ios: "AuraDropdownMenu"
  web: "AuraDropdownMenu"
tokens:
  - "color.surface.floating"
  - "color.border.subtle"
  - "color.content.primary"
  - "color.content.secondary"
  - "color.content.disabled"
  - "color.brand.primary"
  - "color.interaction.selectedContainer"
  - "elevation.level3"
  - "typography.bodyLarge"
  - "typography.labelMedium"
  - "motion.spatialSpring"
  - "motion.enterSpec"
---

# AuraDropdownMenu

Anchored overflow and filter menu that opens from a trigger (usually an `AuraIconButton` in a top app bar), with optional titled sections and single-choice check marks.

## 1. Intent & Usage Principles

### When to Use

- Filtering or sorting a long list from the top app bar so the controls stay reachable while the list scrolls (e.g. type, status, and sort order of a drink journal).
- Grouping secondary screen commands that do not deserve their own button (overflow `More`).
- Offering a short single-choice list tied to one trigger, where a full sheet or screen would be heavier than the choice.

### When NOT to Use

- **Form values the user commits with a Save button:** Use `AuraRadio`, `AuraSegmentedButton`, or a picker inside the form.
- **Two to five always-visible options that change the view immediately:** Use `AuraSegmentedButton` or `AuraChipButton` (`BTN-RULE-007`).
- **Long, searchable, or multi-step choices:** Use a dedicated screen or a future `AuraBottomSheet`.
- **Destructive confirmation:** Use `AuraDialog(Destructive)`.

### Usage Rules

- **`MENU-001` (Section per decision):** Put each independent decision in its own `AuraMenuSection` with a short `title`. Items inside one section are mutually exclusive; mark exactly one `selected = true` per filter section.
- **`MENU-002` (Show the result outside the menu):** When a filter is active, reflect it outside the menu (e.g. the top app bar `expandedSubtitle` or a `Dot` badge on the trigger) so the current state is visible without opening the menu.
- **`MENU-003` (Short labels):** Keep labels to one line. The menu is `200–280px` wide and truncates with an ellipsis.

### Do / Don't

| Do | Don't |
|---|---|
| Use sections "Type", "Status", "Sort" with one selected item each. | Mix unrelated commands and filter options in one unlabeled list. |
| Anchor the menu to the trigger by placing it in the trigger's parent `Box`. | Position the menu with hard-coded screen offsets. |
| Close the menu after a choice (`dismissOnItemClick = true`) for single-choice filters. | Leave the menu open after every tap so the user cannot see the list change. |

---

## 2. Anatomy & Variants

```text
                                   [ ⚙ ]   <- trigger (AuraIconButton)
                  ╭──────────────────────╮
                  │ Type                 │  <- section title (labelMedium, content.secondary)
                  │  All               ✓ │  <- selected item (brand.primary + check, selectedContainer)
                  │  Wine                │
                  │ ──────────────────── │  <- AuraDivider between sections
                  │ Sort                 │
                  │  Highest rated       │
                  ╰──────────────────────╯
```

| Part | Description |
|---|---|
| `root` | Solid floating surface, `20px` corner radius, `elevation.level3`, `1px` `border.subtle`, `8px` vertical padding, scrolls when taller than the window. |
| `sectionTitle` | Optional heading per section. |
| `item` | `>= 48px` tall row: label + trailing check mark when selected. |
| `divider` | `AuraDivider` between sections. |

Only one material is offered: the menu renders in its own window, where the glass backdrop of the screen cannot be sampled, so it stays `Solid`.

---

## 3. Token Bindings

| Element | State | Light / Dark Semantic Token |
|---|---|---|
| Surface | All | `surface.floating` + `border.subtle`, `elevation.level3` shadow |
| Section title | All | `content.secondary` (`typography.labelMedium`) |
| Item label | Rest | `content.primary` (`typography.bodyLarge`) |
| Item label + check | Selected | `brand.primary` on `interaction.selectedContainer` |
| Item label | Disabled | `content.disabled` |

---

## 4. State Precedence & Accessibility

### State Precedence

`Hidden` (`!expanded`) > `Disabled` item > `Pressed` (`fluidPressScale`) > `Selected` > `Rest`.

### Accessibility

- Opens in a focusable `Popup`: Back and taps outside call `onDismissRequest`.
- Each item is a `Role.Button` with `>= 48px` height and exposes `selected` so screen readers announce the current choice in a section.
- Section titles are headings (`semantics { heading() }`).
- The surface is opaque, so text keeps `>= 4.5:1` contrast regardless of the content underneath; Reduced Transparency needs no fallback.

---

## 5. Motion

Uses the `Expressive` track like other `spatial` surfaces:

1. **Entrance:** scales `0.92 -> 1.0` from the top-end corner via `AuraTheme.motion.spatialSpring()` and fades in via `AuraTheme.motion.enterSpec()`.
2. **Item press:** `fluidPressScale(pressedScale = 0.97f)`.
3. **Reduced Motion:** both animations `snap()` when `rememberAuraReducedMotion()` is active.

---

## 6. Responsive Layout Behavior

| Width / Display Profile | Placement |
|---|---|
| `ExtraSmall` (`< 360dp`) | Same `200–280px` width; clamped `12px` inside the window edges. |
| `Compact` (`360–599dp`) | Opens below the trigger, end-aligned; flips above when there is no room below. |
| `Square` (foldable) | Same anchoring; never centred across the hinge because it follows the trigger. |
| `UltraWide` (short landscape) | Flips above or clamps to the window height and scrolls its content. |
| `Expanded` (`>= 840dp`) | Stays compact next to the trigger; never stretches with the window. |

---

## 7. Cross-Platform API Contract

<!-- API:ANDROID:BEGIN -->
```kotlin
data class AuraMenuItem(
    val id: String,
    val label: String,
    val selected: Boolean = false,
    val enabled: Boolean = true,
)

data class AuraMenuSection(
    val title: String? = null,
    val items: List<AuraMenuItem>,
)

@Composable
fun AuraDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    sections: List<AuraMenuSection>,
    onItemClick: (AuraMenuItem) -> Unit,
    modifier: Modifier = Modifier,
    dismissOnItemClick: Boolean = true,
)
```
<!-- API:ANDROID:END -->

<!-- API:IOS:BEGIN -->
```swift
public struct AuraMenuItem: Identifiable, Sendable {
    public let id: String
    public let label: String
    public var selected: Bool = false
    public var enabled: Bool = true
}

public struct AuraMenuSection: Sendable {
    public var title: String? = nil
    public let items: [AuraMenuItem]
}

public struct AuraDropdownMenu: View {
    public init(
        expanded: Binding<Bool>,
        sections: [AuraMenuSection],
        onItemClick: @escaping (AuraMenuItem) -> Void,
        dismissOnItemClick: Bool = true
    )
}
```
<!-- API:IOS:END -->

<!-- API:WEB:BEGIN -->
```ts
export interface AuraMenuItem {
  id: string;
  label: string;
  selected?: boolean;
  enabled?: boolean;
}

export interface AuraMenuSection {
  title?: string;
  items: AuraMenuItem[];
}

export interface AuraDropdownMenuProps {
  expanded: boolean;
  onDismissRequest: () => void;
  sections: AuraMenuSection[];
  onItemClick: (item: AuraMenuItem) => void;
  dismissOnItemClick?: boolean;
}
```
<!-- API:WEB:END -->
