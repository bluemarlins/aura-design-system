---
id: "aura.component.floating-action-bar"
name: "AuraFloatingActionBar"
category: "actions"
status: "ready"
version: "1.3.0"
layer: "chrome"
material_allowed: ["Solid", "Glass", "LiquidGlass"]
motion_track: "Standard"
platforms:
  android: "com.example.ds.component.AuraFloatingActionBar"
  ios: "AuraDS.AuraFloatingActionBar (planned)"
  web: "@aura-ds/react.AuraFloatingActionBar (planned)"
tokens:
  - "glass.frostedTint"
  - "glass.blurRadius"
  - "colors.content.secondary"
  - "elevation.level2"
  - "motion.floatingBarAppearDurationMillis"
  - "motion.floatingBarDisappearDurationMillis"
---

# AuraFloatingActionBar

> A floating glass action dock for **task screens** (editors, multi-step wizards, detail screens). It keeps the screen's primary action reachable at the thumb zone while the form scrolls underneath, and it gives task screens the same floating chrome language as top-level screens (`FloatingGlassScaffold` + `AuraBottomIsland`).

---

## 1. Intent & Usage Principles

### Problem

Task screens (editors, wizards, detail views) need their commit actions to stay reachable above the keyboard and navigation bar while content scrolls underneath.

### When to Use

- An editor or create flow whose commit action (`Save`, `Next`, `Done`) would otherwise sit at the end of a long scroll.
- A detail screen whose main follow-up action (`Edit`) should stay visible without scrolling to the bottom.
- Any screen hosted in `FloatingGlassScaffold` that has no `AuraBottomIsland` (task screens replace navigation with actions).

### When NOT to Use & Alternatives

- **Top-level navigation screens:** use `AuraBottomIsland` (navigation) and its split action — never stack both docks.
- **Single floating shortcut:** use `AuraFab`.
- **Destructive confirmation:** use `AuraDialog`; a dock action must not delete without confirmation.

### Context Rules

- **`FAB-DOCK-RULE-001` (One Primary):** At most one `Primary` `AuraButton` per dock; an optional second action uses `Secondary` or `Ghost`.
- **`FAB-DOCK-RULE-002` (Clearance):** Host it in `FloatingGlassScaffold(bottomBar = …, hasBottomIsland = true, bottomIslandHeight = AuraFloatingActionBarDefaults.Height)` so the last content item scrolls clear of the dock. Add `SupportingTextHeight` when `supportingText` is shown.
- **`FAB-DOCK-RULE-003` (Keyboard):** The dock rides above the IME (`imePadding`). Do not hide it while typing — the commit action is most needed then.
- **`FAB-DOCK-RULE-004` (Material):** Default `Glass` (standard chrome). Request `LiquidGlass` only when the screen has no other hero surface (`VIS-RULE-002`).

### Do / Don't

| Do | Don't |
|---|---|
| Put `Save` in the dock and leave the form free of a trailing save button. | Keep a second, inline `Save` button at the end of the form as well. |
| Explain a disabled primary action with `supportingText` ("Add a name and a rating to save"). | Leave a disabled button with no reason. |
| Use one `Primary` plus at most one `Secondary`/`Ghost` action. | Pack three or more buttons into the dock. |

---

## 2. Anatomy & Canonical Part IDs

```text
 ┌──────────────────────────────────────────────┐  ← GlassSurface, 28.dp corners, max 560.dp
 │        supportingText (bodySmall, optional)   │
 │  [ Ghost / Secondary ]  [      Primary      ] │  ← RowScope content, 8.dp gaps
 └──────────────────────────────────────────────┘
        16.dp side margin · 12.dp bottom margin · above navigation bar / IME
```

| Part ID | Responsibility | Required | Semantics Owner |
|---|---|---|---|
| `root` | Floating dock surface (`GlassSurface`, `28dp` corners, max `560dp`) placed above navigation bar / IME | Yes | Traversal group |
| `supportingText` | Optional context line above the actions (`bodySmall`) | No | Read before actions |
| `content` | `RowScope` slot for `AuraButton`s (`8dp` gaps) | Yes | Child `Role.Button` |

---

## 3. Variant, Size & Material Matrix

- **Variant / Size:** Single variant; width fills the window minus `16dp` side margins, capped at `560dp`; corner radius `28dp`.
- **Material (`material`):** `AuraMaterial.Glass` (default), `LiquidGlass`, or `Solid`, passed to `GlassSurface`, which applies the fallback rules below.
- **Reduced Transparency:** with system Reduced Transparency, Battery Saver, high-contrast text, a `Solid` theme ceiling, or API 26–30, the dock renders the opaque `Solid` fallback (`surface.floating` 98% + `1dp` `border.default`).

---

## 4. State Precedence & Behavior

`hidden (visible = false)` → `disabled action` → `loading action` → `rest`. The dock itself is not focusable; focus and press states belong to the contained `AuraButton`s.

---

## 5. Semantic Token Bindings

| Element | State | Token |
|---|---|---|
| Dock surface | Rest | `AuraTheme.glass.frostedTint` over `blurRadius` backdrop (`Glass`), `surface.floating` 98% (`Solid` fallback) |
| Specular rim | Rest | `AuraTheme.glass.strokeStartAlpha → strokeEndAlpha` |
| Shadow | Rest | `AuraTheme.elevation.level2` |
| Supporting text | Rest | `AuraTheme.colors.content.secondary`, `typography.bodySmall` |

---

## 6. Interaction Motion Contract

1. **Show / hide:** `visible` animates a slide (`translationY`) and fade using `AuraTheme.motion.floatingBarAppearSpec()` / `floatingBarDisappearSpec()`.
2. **Reduced Motion:** `rememberAuraReducedMotion()` snaps visibility immediately.
3. Press feedback is owned by the contained `AuraButton`.

---

## 7. Responsive Layout Behavior (`Smallest Width × Display Ratio`)

| Width / Display Profile | Adaptation |
|---|---|
| `ExtraSmall` (`< 360dp`) | Full width minus 16dp margins; prefer a single action. |
| `Compact` (`360–599dp`) | Full width minus margins; primary action takes the remaining weight. |
| `Square` (`~1:1` Foldable) | Capped at 560dp and centered. |
| `UltraWide` (`> 21:9`) | Capped at 560dp and centered so actions stay in one reach zone. |
| `Expanded` (`>= 840dp`) | Capped at 560dp; in two-pane layouts host it in the detail pane. |

---

## 8. Accessibility & Guardrails

- **Contrast:** dock text and button labels keep `>= 4.5:1` over the glass tint against both pure white and pure black backdrops (`VIS-RULE-003`).
- The dock is a traversal group so TalkBack reads its actions together after the screen content.
- Touch targets come from `AuraButton` (`Large` = 56dp height).

---

## 9. Platform Bindings

### 9.1 Android (Jetpack Compose)

- **Source:** [`AuraFloatingActionBar.kt`](../../../design-system/src/main/kotlin/com/example/ds/component/AuraFloatingActionBar.kt)

<!-- API:ANDROID:BEGIN -->
```kotlin
@Composable
fun AuraFloatingActionBar(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    material: AuraMaterial = AuraMaterial.Glass,
    supportingText: String? = null,
    content: @Composable RowScope.() -> Unit,
)
```
<!-- API:ANDROID:END -->

### 9.2 iOS (SwiftUI Reference)

<!-- API:IOS:BEGIN -->
```swift
public struct AuraFloatingActionBar<Content: View>: View {
    public init(
        visible: Bool = true,
        material: AuraMaterial = .glass,
        supportingText: String? = nil,
        @ViewBuilder content: () -> Content
    )
}
```
<!-- API:IOS:END -->

### 9.3 Web (React / TypeScript Reference)

<!-- API:WEB:BEGIN -->
```ts
export interface AuraFloatingActionBarProps {
  visible?: boolean;
  material?: 'Solid' | 'Glass' | 'LiquidGlass';
  supportingText?: string;
  children: React.ReactNode;
}
```
<!-- API:WEB:END -->
