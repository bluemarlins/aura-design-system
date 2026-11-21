---
id: "aura.component.dialog"
name: "AuraDialog"
category: "containment"
status: "ready"
version: "1.0.0"
layer: "spatial"
material_allowed: ["Solid", "Glass"]
motion_track: "Expressive"
platforms:
  android: "com.example.ds.component.AuraDialog"
  ios: "AuraDS.AuraDialog (planned)"
  web: "@aura-ds/react.AuraDialog (planned)"
tokens:
  - "colors.surface.floating"
  - "colors.surface.raised"
  - "colors.content.primary"
  - "colors.content.secondary"
  - "colors.brand.primary"
  - "colors.status.error.solid"
  - "colors.status.error.subtle"
  - "colors.status.error.onSubtle"
  - "colors.border.subtle"
  - "colors.interaction.scrim"
  - "elevation.level4"
  - "typography.headlineSmall"
  - "typography.bodyMedium"
  - "motion.pressInSpring"
  - "motion.pressOutSpring"
  - "motion.stateChangeDurationMillis"
---

# AuraDialog

> Focused modal confirmation, alert, and high-consequence action dialog (`Standard`, `Destructive`, `Glass`), built on Aura tokens without Material 3 modal chrome.

---

## 1. Intent & Usage Principles

### Problem

Some decisions (confirming, alerting, or a high-consequence destructive action) must interrupt the flow and get an explicit answer before the user continues.

### When to Use

- Confirming irreversible or high-impact actions such as deleting a cellar bottle (**My Drink Diary**), clearing a schedule series (**My Planner**), or removing an asset entry (**My Finance**).
- Presenting focused, concise modal prompts that require explicit acknowledgment before returning to the underlying flow.
- Rendering a spatial fluid confirmation prompt over translucent glass backdrops (`AuraDialogVariant.Glass` with `GlassStyle.FROSTED` / `LIQUID`).

### When NOT to Use & Alternatives

- **Multi-field forms, long pickers, or secondary navigation:** Use a dedicated sheet, screen, or two-pane detail flow (`CNT-RULE-002`).
- **Passive inline grouped content:** Use `AuraCard`.
- **Transient non-blocking status feedback:** Use `AuraBadge` or an inline status banner.

### Context Rules

- **`DLG-RULE-001` (Action Verb Clarity):** Always label `confirmText` with a concrete verb (`"Delete Bottle"`, `"Clear Draft"`, `"Confirm"`) rather than ambiguous `"OK"` / `"Yes"`.
- **`DLG-RULE-002` (Destructive Isolation):** Whenever a confirmation causes irreversible deletion or financial reset, set `variant = AuraDialogVariant.Destructive` so the confirm CTA renders in `AuraButtonVariant.Destructive` (`status.error.solid`) paired with an error-tinted indicator banner.
- **`DLG-RULE-003` (Action Count Cap):** Restrict dialog footer controls to at most two clear buttons (`dismissText` Outline/Ghost secondary button on the start + `confirmText` primary action button on the end) stacked vertically on ultra-narrow cover screens.

### Do / Don't

| Do | Don't |
|---|---|
| Use `variant = AuraDialogVariant.Destructive` with specific verb `"Delete Entry"` for irreversible deletion. | Style destructive delete buttons with calm primary brand blue or vague `"OK"` labels. |
| Keep dialog messages scannable (`1–3` lines) or pair with one compact inline summary slot. | Embed full scrollable multi-step forms or 10-item pickers inside an alert modal. |
| Allow dismiss via `dismissText` button, scrim tap (`dismissOnClickOutside`), and system Back unless strict compliance requires explicit choice. | Trap users inside a dialog with no cancel/back escape path. |
| Keep both dialog surfaces and buttons anchored to `AuraTheme.colors` and `AuraButton`. | Render Material `AlertDialog` defaults breaking Light/Dark surface hierarchy. |

---

## 2. Anatomy & Canonical Part IDs

```text
  ╭──────────────────────────────────────────╮
  │  [!]  Delete tasting note?               │  <- Optional warning pill (Destructive) + Title
  │                                          │
  │  This removes the 1998 Barolo note       │  <- Concise message + optional custom content slot
  │  and tasting score permanently.          │
  │                                          │
  │            [  Cancel  ]  [ Delete Note ] │  <- Footer AuraButtons (Outline + Primary/Destructive)
  ╰──────────────────────────────────────────╯
```

| Part ID | Responsibility | Required | Semantics Owner |
|---|---|---|---|
| `scrim` | Platform window dim driven by `interaction.scrim` | Yes | Dismisses on outside tap when `dismissOnClickOutside` |
| `root` | `280–420dp` wide surface, `24dp` radius, `24dp` padding | Yes | Dialog window (focus trap) |
| `alertEmblem` | `36dp` warning pill (`Destructive` only) | Conditional | Decorative |
| `title` | Short question or statement | Yes | `heading()` |
| `message` | Supporting explanation | No | Merged into dialog content |
| `content` | Optional custom slot below `message` | No | Children keep their own semantics |
| `dismissButton` | `AuraButton` (`Outline`) when `dismissText != null` | No | `Role.Button` |
| `confirmButton` | `AuraButton` (`Primary` / `Destructive`) | Yes | `Role.Button` |

---

## 3. Variant, Size & Material Matrix

| Variant (`AuraDialogVariant`) | Surface Material | Confirm CTA Style | Accent Indicator |
|---|---|---|---|
| `Standard` (default) | `surface.floating` (`24dp` corner radius) | `AuraButtonVariant.Primary` | None |
| `Destructive` | `surface.floating` + subtle alert pill | `AuraButtonVariant.Destructive` | `status.error.subtle` / `status.error.solid` alert emblem |
| `Glass` | `GlassSurface(glassStyle, 24.dp)` | `AuraButtonVariant.Primary` | Optical blur/refraction bevel + specular stroke |

---

## 4. State Precedence & Behavior

`Hidden` (`!visible`) > `ConfirmDisabled` (`!confirmEnabled`) > `Destructive` > `Glass` > `Standard`.

---

## 5. Semantic Token Bindings

| Element | Variant / State | Light / Dark Semantic Token |
|---|---|---|
| Scrim Overlay | `Visible` | `AuraTheme.colors.interaction.scrim` |
| Modal Surface | `Standard` / `Destructive` / `Glass` | `surface.floating` (`border.subtle`) / `GlassSurface(glassStyle)` with `elevation.level4` shadow |
| Destructive Emblem | `Destructive` | `status.error.subtle` container + `status.error.solid` glyph |
| Title Text | `All` | `AuraTheme.colors.content.primary` (`typography.headlineSmall`) |
| Message Body | `All` | `AuraTheme.colors.content.secondary` (`typography.bodyMedium`) |
| Action Buttons | `Standard` / `Destructive` | `AuraButton(Primary/Destructive)` + `AuraButton(Outline)` |

---

## 6. Interaction Motion Contract

Consumes `AuraTheme.motion` (`Expressive` profile recommended for spatial modal entrance):

1. **Modal Entrance, Exit & Scrim:** Container animates smoothly in scale (`0.94f -> 1.0f`) via `AuraTheme.motion.spatialSpring()`, alpha (`0f -> 1f`) via `AuraTheme.motion.enterSpec()`, and modal scrim fades over `AuraTheme.motion.scrimFadeDurationMillis`, while state transitions (`confirmEnabled`) crossfade over `AuraTheme.motion.stateChangeDurationMillis`.
2. **Action Tactile Physics:** Footer buttons inherit `AuraButton` spring press compression (`pressInSpring()` / `pressOutSpring()`).
3. **Reduced Motion:** Uses `snap()` when `rememberAuraReducedMotion()` is active.

---

## 7. Responsive Layout Behavior (`Smallest Width × Display Ratio`)

| Width / Display Profile | Dialog Sizing & Action Button Layout |
|---|---|
| `ExtraSmall` (`< 360dp`) | Expands to `fillMaxWidth(0.94f)` with `20dp` inner padding; when action labels are long, stacks footer buttons vertically (`Confirm` above `Dismiss`) so neither truncates. |
| `Compact` (`360–599dp`) | Centred modal (`widthIn(min = 280.dp, max = 380.dp)`) with horizontal end-aligned button row (`10dp` gap). |
| `Square` (`~1:1` Pop-up / Foldable) | Centered safely above hinge/split boundaries with `widthIn(max = 400.dp)` and comfortable scrim margin. |
| `UltraWide` (`> 21:9`) | Vertical padding adjusts cleanly (`heightIn`) so title, message, and actions stay comfortably visible without clipping in short landscape frames. |
| `Expanded` (`>= 840dp` Tablet) | Remains compact and focused (`widthIn(max = 440.dp)`), never stretching across an entire tablet screen. |

---

## 8. Accessibility & Guardrails

- Wraps modal in Jetpack Compose `Dialog` (`DialogProperties`) so TalkBack traps focus cleanly inside the dialog and restores focus to the trigger control upon dismissal.
- Marks `title` with `semantics { heading() }` for immediate modal announcement.
- Enforces `>= 48dp` touch height on both confirm and dismiss `AuraButton`s.
- **Reduced Transparency & Contrast (`VIS-RULE-003`–`004`):** Maintains `>= 4.5:1` body/title text contrast (`>= 3:1` borders/actions) on `AuraDialogVariant.Glass` and falls back to opaque `Solid` (`colors.surface.floating`) via `AuraTheme.effectiveMaterial(...)` when Reduced Transparency, Battery Saver, or High-Contrast Text is active.

---

## 9. Platform Bindings

### 9.1 Android (Jetpack Compose)

- **Source:** [`AuraDialog.kt`](../../../design-system/src/main/kotlin/com/example/ds/component/AuraDialog.kt)

<!-- API:ANDROID:BEGIN -->
```kotlin
enum class AuraDialogVariant {
    Standard,
    Destructive,
    Glass,
}

@Composable
fun AuraDialog(
    visible: Boolean,
    title: String,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    variant: AuraDialogVariant = AuraDialogVariant.Standard,
    glassStyle: GlassStyle = GlassStyle.FROSTED,
    message: String? = null,
    confirmText: String = "Confirm",
    dismissText: String? = "Cancel",
    confirmEnabled: Boolean = true,
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    content: (@Composable () -> Unit)? = null,
)
```
<!-- API:ANDROID:END -->

### 9.2 iOS (SwiftUI Reference)

<!-- API:IOS:BEGIN -->
```swift
public enum AuraDialogVariant: Sendable {
    case standard
    case destructive
    case glass
}

public struct AuraDialog<Content: View>: View {
    public init(
        visible: Bool,
        title: String,
        onConfirm: @escaping () -> Void,
        onDismissRequest: @escaping () -> Void,
        variant: AuraDialogVariant = .standard,
        glassStyle: GlassStyle = .frosted,
        message: String? = nil,
        confirmText: String = "Confirm",
        dismissText: String? = "Cancel",
        confirmEnabled: Bool = true,
        dismissOnBackPress: Bool = true,
        dismissOnClickOutside: Bool = true,
        @ViewBuilder content: () -> Content
    )
}
```
<!-- API:IOS:END -->

### 9.3 Web (React / TypeScript Reference)

<!-- API:WEB:BEGIN -->
```ts
export type AuraDialogVariant = 'Standard' | 'Destructive' | 'Glass';

export interface AuraDialogProps {
  visible: boolean;
  title: string;
  onConfirm: () => void;
  onDismissRequest: () => void;
  variant?: AuraDialogVariant;
  glassStyle?: GlassStyle;
  message?: string;
  confirmText?: string;
  dismissText?: string | null;
  confirmEnabled?: boolean;
  dismissOnBackPress?: boolean;
  dismissOnClickOutside?: boolean;
  children?: unknown;
}
```
<!-- API:WEB:END -->
