# 03 · Component Spec Template

Every component spec in `docs/components/<category>/<component>.md` uses this exact structure. Copy the block below into a new file and replace the placeholders. [`SpecCodeConsistencyTest`](../../design-system/src/test/kotlin/io/github/bluemarlins/aura/contract/SpecCodeConsistencyTest.kt) fails the build when a ready spec departs from it.

## Fixed Rules

| Rule | Requirement |
|---|---|
| Frontmatter keys | `id`, `name`, `category`, `status`, `version`, `layer`, `material_allowed`, `motion_track`, `platforms`, `tokens` — in this order. |
| `id` | `aura.component.<file-name-without-aura-prefix>` (e.g. `aura.component.chip-button`, `aura.component.glass-surface`). |
| `platforms` | `android` is the fully qualified Kotlin symbol; `ios` is `AuraDS.<Name> (planned)`; `web` is `@aura-ds/react.<Name> (planned)`. |
| `tokens` | Paths relative to `AuraTheme`: `colors.*`, `typography.*`, `motion.*`, `layout.*`, `shapes.*`, `elevation.*`, `glass.*`, `window.*`. Prepending `AuraTheme.` gives the Kotlin accessor. |
| Sections | Exactly the nine `##` sections below, numbered and titled as shown, in this order. |
| §1 subsections | `### Problem`, `### When to Use`, `### When NOT to Use & Alternatives`, `### Context Rules`, `### Do / Don't` — in this order. |
| §2 part table | A table whose header starts with `\| Part ID \| Responsibility \| Required \|` (add `\| Semantics Owner \|` for interactive parts). |
| §9 subsections | `### 9.1 Android (Jetpack Compose)`, `### 9.2 iOS (SwiftUI Reference)`, `### 9.3 Web (React / TypeScript Reference)`, each holding its `<!-- API:<PLATFORM>:BEGIN/END -->` block. |
| Units (Layer A, §1–§8) | Dimensions in `dp` written as `16dp` (Android `dp`, iOS `pt`, Web CSS `px`); type sizes in `sp` (`14sp`). Kotlin expressions keep Kotlin syntax (`Modifier.height(48.dp)`). |
| Not applicable | Never delete a section. A passive component states it in one line (e.g. "Passive component: no pointer, focus, or pressed states."). |

## Template

````markdown
---
id: "aura.component.<kebab-name>"
name: "Aura<Name>"
category: "<primitives|actions|inputs|feedback|containment|navigation|layout>"
status: "draft"
version: "1.0.0"
layer: "<canvas|content|chrome|spatial>"
material_allowed: ["Solid"]
motion_track: "<Standard|Expressive|none>"
platforms:
  android: "io.github.bluemarlins.aura.component.Aura<Name>"
  ios: "AuraDS.Aura<Name> (planned)"
  web: "@aura-ds/react.Aura<Name> (planned)"
tokens:
  - "colors.<group>.<role>"
  - "typography.<style>"
  - "motion.<token>"
---

# Aura<Name>

> One-sentence summary of what the component is and where it is used.

---

## 1. Intent & Usage Principles

### Problem

The user problem this component solves, in one or two sentences.

### When to Use

- Situation where this component is the right choice.

### When NOT to Use & Alternatives

| Situation | Alternative | Reason |
|---|---|---|
| Situation | `AuraOther` | Why the alternative fits better |

### Context Rules

- **`<CAT>-RULE-001`:** Placement or hierarchy rule.

### Do / Don't

| Do | Don't |
|---|---|
| Recommended usage. | Misuse to avoid. |

---

## 2. Anatomy & Canonical Part IDs

```text
ASCII diagram of the parts
```

| Part ID | Responsibility | Required | Semantics Owner |
|---|---|---|---|
| `root` | Container, hit target, accessibility role owner | Yes | `Role.<Role>` |

---

## 3. Variant, Size & Material Matrix

| Size | Visual Height | Min Touch Target | Horizontal Padding | Corner Radius | Label Type |
|---|---|---|---|---|---|
| `Medium` (default) | `48dp` | `48dp` | `16dp` | `shapes.medium` | `labelLarge` (`14sp`) |

- **Material:** Allowed materials and their fallback.

---

## 4. State Precedence & Behavior

$$\text{Loading} \succ \text{Disabled} \succ \text{Pressed} \succ \text{Focused} \succ \text{Hovered} \succ \text{Rest}$$

What changes in each state.

---

## 5. Semantic Token Bindings

| Part | Variant | State | Token |
|---|---|---|---|
| `root` (bg) | `*` | `Rest` | `colors.<group>.<role>` |

---

## 6. Interaction Motion Contract

Motion tokens used for press, state change, and enter/exit, plus the Reduced Motion fallback. Passive components state `motion_track: "none"` and why.

---

## 7. Responsive Layout Behavior (`Smallest Width × Display Ratio`)

| Profile | Behavior |
|---|---|
| `ExtraSmall` (`sw < 360dp`) | |
| `Compact` (`360–599dp`) | |
| `Medium` / `Square` (`~1:1`) | |
| `UltraWide` (`> 21:9`) | |
| `Expanded` (`>= 840dp`) | |

---

## 8. Accessibility & Guardrails

- **Role / name / state** announced on each platform.
- **Touch target:** `>= 48dp` (`44pt` iOS).
- **Contrast:** `>= 4.5:1` text, `>= 3:1` non-text.

---

## 9. Platform Bindings

### 9.1 Android (Jetpack Compose)

- **Source:** [`Aura<Name>.kt`](../../../design-system/src/main/kotlin/io/github/bluemarlins/aura/component/Aura<Name>.kt)

<!-- API:ANDROID:BEGIN -->
```kotlin
@Composable
fun Aura<Name>(
    modifier: Modifier = Modifier,
)
```
<!-- API:ANDROID:END -->

### 9.2 iOS (SwiftUI Reference)

<!-- API:IOS:BEGIN -->
```swift
public struct Aura<Name>: View { }
```
<!-- API:IOS:END -->

### 9.3 Web (React / TypeScript Reference)

<!-- API:WEB:BEGIN -->
```ts
export interface Aura<Name>Props { className?: string }
```
<!-- API:WEB:END -->
````
