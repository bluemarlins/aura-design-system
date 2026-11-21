# 00 · Component Specification Architecture (`Specs ↔ Code`)

## 1. Purpose

Defines the bidirectional **Markdown Specification ↔ Platform Code** contract for Aura components. For the mandatory pass/fail quality gate (`EVAL-001` – `EVAL-006`) enforced against every spec and Kotlin file, see [01-component-evaluation-ruleset.md](01-component-evaluation-ruleset.md).

Each component specification serves as an **OS-agnostic behavioral and semantic contract** so that:
1. **Usage principles come first:** Designers, engineers, and AI agents understand *when* and *why* to select a component before inspecting API props.
2. **Android (Jetpack Compose) is implemented cleanly today:** Code in `:design-system` strictly satisfies the spec's variants, sizing, state precedence, token bindings, and accessibility rules.
3. **Future platforms (`iOS SwiftUI`, `Web React`) reuse the same contract:** Platform implementations map directly to the shared Layer A contract without re-inventing component intent, state rules, or token semantics.

---

## 2. Two-Layer Specification Structure

Every component spec in `docs/components/<category>/<component>.md` is divided into **Layer A (Universal Contract)** and **Layer B (Platform Bindings)**. The exact headings, frontmatter keys, and part-table format are fixed by [03-spec-template.md](03-spec-template.md); start every new spec from that template.

```text
docs/components/<category>/<component>.md
├── YAML Frontmatter          # Machine-readable identity, status, version, platforms, token dependencies
├── Layer A: Universal Contract (OS-Agnostic, ~80%)
│   ├── 1. Intent & Usage Principles (Problem / When to Use / When NOT to Use & Alternatives / Context Rules / Do / Don't)
│   ├── 2. Anatomy & Canonical Part IDs
│   ├── 3. Variant, Size & Material Matrix (dp/sp + forbidden combinations)
│   ├── 4. Deterministic State Precedence & Behavior
│   ├── 5. Semantic Token Bindings (Part × Variant × State -> AuraColors / AuraTypography / AuraLayout)
│   ├── 6. Interaction Motion Contract (AuraMotion + Reduced Motion, or passive rationale)
│   ├── 7. Responsive Layout Behavior (Smallest Width × Display Ratio)
│   └── 8. Accessibility & Guardrail Rules (Rule IDs)
└── Layer B: 9. Platform Bindings (OS-Specific, ~20%)
    ├── 9.1 Android (Jetpack Compose)        <!-- API:ANDROID:BEGIN / END -->
    ├── 9.2 iOS (SwiftUI Reference)          <!-- API:IOS:BEGIN / END -->
    └── 9.3 Web (React / TypeScript Reference) <!-- API:WEB:BEGIN / END -->
```

### Bidirectional Sync (`Specs ↔ Code`)

- **Spec → Code (Design/Contract-First):** Author Layer A rules and Layer B target signatures before writing platform code or translating to a new OS (`iOS` / `Web`).
- **Code → Spec (Implementation Sync):** When a public Kotlin API changes in `:design-system`, update the `<!-- API:ANDROID:BEGIN --> ... <!-- API:ANDROID:END -->` block and props/behavior tables in the same change so human documentation and AI contracts never drift.

---

## 3. Canonical Anatomy & State Rules

### 3.1. Canonical Part IDs

All platforms must preserve the same part identifiers across token bindings, semantics, and tests:
- `root` — Outer container, hit target boundary, accessibility role owner
- `label` — Primary text content
- `leadingIcon` / `trailingIcon` — Optional pre/post visual cues
- `progressIndicator` — Asynchronous loading overlay or indicator
- `supportingText` / `errorText` — Secondary guidance or validation feedback (input components)

### 3.2. Universal State Precedence

Unless explicitly overridden by a component spec, concurrent interactive states resolve in the following strict order:

$$\text{Loading} \succ \text{Disabled} \succ \text{Pressed} \succ \text{Focused} \succ \text{Hovered} \succ \text{Rest}$$

---

## 4. Cross-Platform Unit & Token Mapping

Layer A specs express dimensions in **density-independent `dp`** (written `16dp`), type sizes in **`sp`** (written `14sp`), and **semantic token paths** relative to `AuraTheme`. Each platform binding converts them deterministically:

| Concept | Layer A Spec | Android (Compose) | iOS (SwiftUI) | Web (CSS / React) |
|---|---|---|---|---|
| Spacing & Bounds | `16dp` | `16.dp` | `16pt` (`CGFloat`) | `1rem` (`16px`) |
| Typography Size | `14sp` | `14.sp` | `Font.system(size: 14)` / Dynamic Type | `0.875rem` |
| Minimum Hit Target | `48dp` (mobile) | `48.dp × 48.dp` | `44pt × 44pt` | `44px × 44px` (coarse) / `36px` (fine) |
| Color Tokens | `colors.brand.primary` | `AuraTheme.colors.brand.primary` | `auraTheme.colors.brand.primary` | `var(--aura-brand-primary)` |
| Surface Material | `Solid \| Glass` | `AuraMaterial` | `Material` / `.ultraThinMaterial` | Solid / `backdrop-filter: blur()` |

---

## 5. Naming & File Conventions

- **Component IDs:** frontmatter `id: "aura.component.<kebab-case-name>"` (e.g., `aura.component.button`)
- **Rule IDs:** `<CAT>-RULE-<NNN>` for category principles (e.g., `BTN-RULE-001`) and `<COMP>-<NNN>` for component validation rules (e.g., `BTN-001`)
- **Spec File Location:** `docs/components/<category>/<kebab-case-name>.md`
- **Category Principles:** `docs/components/<category>/00-<category>-principles.md`
- **Categories:** `primitives` (building blocks composed by other components), `actions`, `inputs`, `feedback`, `containment`, `navigation`, `layout`. The full tree and dependency direction are in [02-component-hierarchy.md](02-component-hierarchy.md).
