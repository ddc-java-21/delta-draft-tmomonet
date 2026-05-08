# PF2e Dice Roller — Feature Specification

## 1. Overview

Android application for Pathfinder 2e players. Imports a Pathbuilder 2e character JSON export, supports attack/skill/save/damage rolls with PF2e rules, custom roll expressions, named routines, active buff tracking, and spell data enrichment via aonprd.com parsing.

**Scope:** Single-character for now; multi-character architecture prepared but not exposed in UI.

---

## 2. Architecture

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | XML layouts, ViewBinding, Navigation Component |
| DI | Hilt |
| Database | Room |
| HTTP | OkHttp |
| HTML Parsing | Jsoup |
| JSON | Moshi (with Kotlin codegen) |
| Async | Kotlin Coroutines + StateFlow |
| ViewModel | AndroidX ViewModel |

---

## 3. Enums & Type Converters

All enum columns are stored in Room via a shared `Converters` class annotated with `@ProvidedTypeConverter` and registered on the `@Database`.

```kotlin
enum class ProficiencyRank(val bonus: Int, val label: String) {
    UNTRAINED(0, "Untrained"),
    TRAINED(2, "Trained"),
    EXPERT(4, "Expert"),
    MASTER(6, "Master"),
    LEGENDARY(8, "Legendary");

    fun totalBonus(level: Int) = if (this == UNTRAINED) 0 else bonus + level

    companion object {
        fun fromBonus(value: Int) = entries.first { it.bonus == value }
    }
}

enum class BonusType { STATUS, CIRCUMSTANCE, ITEM, UNTYPED }

enum class AppliesTo {
    ALL_ATTACKS, ALL_DAMAGE,
    SPECIFIC_SKILL,
    FORTITUDE, REFLEX, WILL, ALL_SAVES,
    ALL
}

enum class MagicTradition { ARCANE, DIVINE, OCCULT, PRIMAL }

enum class CastingType { PREPARED, SPONTANEOUS, INNATE }

// RollType and HeightenType defined in sections 4 and 3.5 respectively;
// all use name-based String converters except ProficiencyRank which uses its Int bonus.
```

**TypeConverter strategy:**
- `ProficiencyRank` ↔ `Int`: via `rank.bonus` / `ProficiencyRank.fromBonus(value)`
- All other enums ↔ `String`: via `enum.name` / `enumValueOf<T>(value)`

One `Converters` class handles all types; registered once at the `@Database` level.

---

## 4. Room Data Model

### 4.1 `CharacterEntity`

```
id: Long (PK, autoGenerate)
name: String
className: String
level: Int
ancestry: String
heritage: String
background: String
keyAbility: String

// Ability scores (raw)
str: Int
dex: Int
con: Int
int_: Int
wis: Int
cha: Int

// Derived (computed at import, cached)
maxHp: Int          // ancestryhp + (classhp × level) + bonushp + (bonushpPerLevel × level)
speed: Int          // attributes.speed + attributes.speedBonus
ac: Int             // acTotal.acTotal
shieldBonus: Int    // acTotal.shieldBonus (parse as Int)

// Proficiency ranks — stored as Int (bonus value) via ProficiencyRank TypeConverter
profPerception: ProficiencyRank
profFortitude: ProficiencyRank
profReflex: ProficiencyRank
profWill: ProficiencyRank
profClassDC: ProficiencyRank
profAcrobatics: ProficiencyRank
profArcana: ProficiencyRank
profAthletics: ProficiencyRank
profCrafting: ProficiencyRank
profDeception: ProficiencyRank
profDiplomacy: ProficiencyRank
profIntimidation: ProficiencyRank
profMedicine: ProficiencyRank
profNature: ProficiencyRank
profOccultism: ProficiencyRank
profPerformance: ProficiencyRank
profReligion: ProficiencyRank
profSociety: ProficiencyRank
profStealth: ProficiencyRank
profSurvival: ProficiencyRank
profThievery: ProficiencyRank
profCastingArcane: ProficiencyRank
profCastingDivine: ProficiencyRank
profCastingOccult: ProficiencyRank
profCastingPrimal: ProficiencyRank

// Item/other mods — stored as JSON blob
// Structure: Map<String, Map<String, Int>>
// e.g. {"Intimidation": {"Item Bonus": 1}, "Fortitude": {"Item Bonus": 1}}
modsJson: String

rawJson: String     // full original Pathbuilder export for re-parsing
```

**Proficiency bonus formula:** `rank.totalBonus(character.level)` — encapsulated on the enum.

### 4.2 `WeaponEntity`

```
id: Long (PK, autoGenerate)
characterId: Long (FK → CharacterEntity)
name: String
displayName: String
prof: String            // simple / martial / advanced / unarmed
dieSize: Int            // 4 / 6 / 8 / 10 / 12
numDice: Int            // derived from striking rune (see below)
damageType: String      // B / P / S
damageBonus: Int        // pre-calculated flat bonus from Pathbuilder
totalAttackBonus: Int   // pre-calculated from Pathbuilder weapon.attack
extraDamageJson: String // serialized List<String>, e.g. ["1d6 Spirit"]
runesJson: String       // serialized List<String>
```

**Striking rune → `numDice` mapping:**

| `str` field | `numDice` |
|---|---|
| `""` | 1 |
| `"striking"` | 2 |
| `"greaterStriking"` | 3 |
| `"majorStriking"` | 4 |

**`extraDamage` parsing:** Each string like `"1d6 Spirit"` → split on space →
`DiceTerm(count=1, sides=6, type="Spirit")`. Type casing normalized to lowercase.

### 4.3 `SpellCasterEntity`

```
id: Long (PK, autoGenerate)
characterId: Long (FK)
name: String                // e.g. "Well Awakening"
tradition: MagicTradition   // stored as String via TypeConverter
castingType: CastingType    // stored as String via TypeConverter
ability: String             // str / dex / con / int / wis / cha
proficiencyRank: ProficiencyRank
innate: Boolean
perDayJson: String          // serialized Int[11] — index = spell level
```

### 4.4 `SpellEntity`

```
id: Long (PK, autoGenerate)
spellCasterId: Long (FK)
spellLevel: Int             // level slot the spell occupies
name: String

// AON-enriched fields (nullable until fetched)
aonUrl: String?
traits: String?             // serialized List<String>
castingTime: String?
range: String?
area: String?
targets: String?
savingThrow: String?        // "Reflex" / "Will" / "Fortitude" / null
isBasicSave: Boolean        // true if "basic [save]"
duration: String?
description: String?
baseDamageExpression: String?   // e.g. "4d6(fire)" at base level
heightenRulesJson: String?      // serialized List<HeightenRule>
baseSpellLevel: Int?            // spell's natural level (may differ from slot level)
```

### 4.5 `HeightenRule` (serialized within SpellEntity)

```kotlin
data class HeightenRule(
    val type: HeightenType,     // PER_LEVEL or SPECIFIC
    val levelInterval: Int?,    // PER_LEVEL: e.g. 1 means "+1 level"
    val atLevel: Int?,          // SPECIFIC: e.g. 5 for "Heightened (5th)"
    val additionalDamage: String?,  // e.g. "+2d6" (added per interval or at level)
    val rawText: String         // unparsed text for non-damage effects
)

enum class HeightenType { PER_LEVEL, SPECIFIC }
```

### 4.6 `SavedRollEntity`

```
id: Long (PK, autoGenerate)
characterId: Long?          // null = global
name: String
expression: String          // e.g. "2d8+4+1d6(fire)"
rollType: RollType          // stored as String via TypeConverter
description: String?
```

### 4.7 `RoutineEntity`

```
id: Long (PK, autoGenerate)
characterId: Long?          // null = global
name: String
description: String?
```

### 4.8 `RoutineStepEntity`

```
id: Long (PK, autoGenerate)
routineId: Long (FK → RoutineEntity)
stepOrder: Int
label: String               // "Strike 1", "Double Slice Off-Hand"
hasAttackRoll: Boolean
mapPenalty: Int             // 0 / -5 / -10 (user-configured per step)
agile: Boolean              // if true, use -4 / -8 MAP instead
attackBonusOverride: Int?   // null = use linked weapon or character default
weaponId: Long?             // optional link to WeaponEntity
damageExpression: String    // normal damage
critDamageExpression: String
```

**Crit damage expression:** Stored explicitly so custom routines can define `4d8+9` for crit
when the attack die is `2d8`. For weapon-linked steps the app auto-generates:
`critDamageExpression = "(${normalTotal}) × 2"` at roll time (double the result).

### 4.9 `BuffEntity`

```
id: Long (PK, autoGenerate)
characterId: Long (FK)
name: String
bonusType: BonusType        // stored as String via TypeConverter
value: Int                  // positive = bonus, negative = penalty
appliesTo: AppliesTo        // stored as String via TypeConverter
specificTarget: String?     // skill/save name when appliesTo = SPECIFIC_SKILL
active: Boolean
```

---

## 5. Dice Expression Engine

### 4.1 Grammar

```
expression  := term ('+' term)*
term        := dice_term | flat_term
dice_term   := INT 'd' INT type_tag?
flat_term   := INT type_tag?
type_tag    := '(' WORD ')'
```

Examples:
- `2d8+4` → two terms, both implicitly the weapon's base damage type
- `2d8+4+1d6(fire)` → 2d8+4 [base type], 1d6 [fire]
- `3d12+9+1d6(spirit)` → 3d12+9 [base type], 1d6 [spirit]
- `10d6(fire)` → 10d6 [fire] (spell, explicit type)

Implicit type resolution: the calling context supplies the default type (weapon's `damageType`
or the spell's damage type). Terms with explicit `type_tag` override.

### 4.2 Roll Result Types

```kotlin
data class TermResult(
    val expression: String,     // "2d8", "4", "1d6(fire)"
    val rolls: List<Int>,       // individual die faces; empty for flat terms
    val damageType: String,
    val subtotal: Int
)

data class RollResult(
    val terms: List<TermResult>,
    val total: Int,
    val dieface: Int?           // the d20 face for ATTACK/SKILL/SAVE rolls
)
```

### 4.3 Roll Types

```kotlin
enum class RollType {
    ATTACK,     // d20 + attack bonus → AC band display
    SKILL,      // d20 + skill modifier → flat result
    SAVE,       // d20 + save modifier → flat result
    DAMAGE,     // expression → regular + crit display
    CUSTOM      // expression → flat result, no special display
}
```

### 4.4 Crit Damage Rule

Standard PF2e: **double the total damage result**.
`critTotal = regularTotal × 2`

Display both values; individual die faces shown only for the regular roll.
The crit value is derived, not re-rolled.

---

## 6. Attack Roll — AC Band Display

Given roll result `R = d20_face + totalAttackBonus`:

| Band | AC Range | Label |
|---|---|---|
| Crit Success | AC ≤ R − 10 | **CRIT** |
| Success | R−9 ≤ AC ≤ R | **HIT** |
| Failure | R+1 ≤ AC ≤ R+10 | **MISS** |
| Crit Failure | AC ≥ R+11 | **CRIT MISS** |

**Nat 20 / Nat 1:** Display a banner ("★ Natural 20 — improve all outcomes one degree" /
"✕ Natural 1 — worsen all outcomes one degree"). AC bands remain math-based and are never
shifted programmatically; the player reads the banner and applies the degree shift mentally.
The Crit Miss band is always rendered (player can visually ignore it if no fumble abilities apply).

**Display layout per attack step:**
```
Roll: 24   (d20: 11  +13 mod)
─────────────────────────────
AC ≤ 14   │ CRIT   │ 18 dmg  (9 × 2)
AC 15–24  │  HIT   │  9 dmg  [2,3,1 + 3]
AC 25–34  │  MISS  │  —
AC ≥ 35   │ C.MISS │  —
```

---

## 7. Skill & Save Rolls

### 6.1 Skill Check

```
total = d20 + ability_mod + proficiency_bonus + item_bonus + active_buffs
```

- `ability_mod` = `(score − 10) / 2`
- `proficiency_bonus` = `if (rank > 0) rank + level else 0`
- `item_bonus` = parsed from `mods` JSON (key: skill name, subkey: "Item Bonus")
- `active_buffs` = sum of applicable buff values after stacking rules applied

### 6.2 Saves

| Save | Ability |
|---|---|
| Fortitude | CON |
| Reflex | DEX |
| Will | WIS |

Same formula as skill check. Item bonuses read from `mods.Fortitude["Item Bonus"]` etc.

### 6.3 Perception

`d20 + WIS_mod + proficiency_bonus(profPerception) + item_bonus`

---

## 8. Buff System

### 7.1 Stacking Rules (PF2e)

| Type | Rule |
|---|---|
| STATUS | Take highest bonus; penalties don't stack (take most negative) |
| CIRCUMSTANCE | Same as status |
| ITEM | Same as status |
| UNTYPED | All values stack |

Implementation: before applying buffs to a roll, group by `bonusType`, then:
- For STATUS/CIRCUMSTANCE/ITEM: `max(positives)` + `min(negatives)` (separately)
- For UNTYPED: `sum(all)`

### 7.2 UI Behavior

- Persistent buff panel accessible from all roll screens (bottom sheet or collapsible header)
- Add buff form: name, type dropdown, signed int value, applies-to scope
- Toggle active/inactive per buff without deleting
- Buffs scoped to character; persist across sessions

---

## 9. Pathbuilder JSON Import

### 8.1 Import Flow

1. User pastes raw JSON or reads from clipboard
2. Validate: `success == true`, `build` object present
3. Parse into entity graph (Character + Weapons + SpellCasters + Spells)
4. Upsert into Room (match on `name + class + level`; prompt user if duplicate found)
5. Show import summary card

### 8.2 HP Formula

```kotlin
val maxHp = build.attributes.ancestryhp +
            (build.attributes.classhp * build.level) +
            build.attributes.bonushp +
            (build.attributes.bonushpPerLevel * build.level)
```

### 8.3 Weapon Parsing

```kotlin
// From weapons[] array
weapon.attack           // totalAttackBonus — use directly
weapon.die              // parse "d8" → dieSize=8
weapon.str              // striking rune → numDice lookup
weapon.damageType       // "B" / "P" / "S"
weapon.damageBonus      // flat damage bonus — use directly
weapon.extraDamage      // List<String> e.g. ["1d6 Spirit"] — parse each
```

`extraDamage` string parser: regex `(\d+)d(\d+)\s+(\w+)` → `DiceTerm`.

### 8.4 Spell Parsing

Each entry in `spellCasters[].spells[].list` → one `SpellEntity` with:
- `spellLevel` from the containing object's `spellLevel`
- `name` from the string value
- All AON fields null until enriched

---

## 10. Spell Enrichment (aonprd.com)

### 9.1 Flow

1. User pastes aonprd.com URL into spell enrichment screen
2. OkHttp fetches HTML (User-Agent: Android app)
3. Jsoup parses the page
4. Parsed data stored in matching `SpellEntity` (matched by name, case-insensitive)
   or as a standalone spell if no match

### 9.2 Parsing Targets

| Field | Selector / Strategy |
|---|---|
| Name | `h1.title` text content |
| Traits | Elements with CSS class `trait` |
| Cast / Range / Area / Targets / Duration | `<b>` label → adjacent text node |
| Saving throw | Regex on description: `(basic )?(\w+) DC` |
| Base damage | Regex: `(\d+d\d+)` in description block |
| Damage type | Adjacent word after damage expression |
| Heightening | Paragraphs starting with "Heightened" |

### 9.3 Heightening Parse

```
"Heightened (+N) ..."  → HeightenRule(type=PER_LEVEL, levelInterval=N, ...)
"Heightened (Nth) ..."  → HeightenRule(type=SPECIFIC, atLevel=N, ...)
```

Extract additional damage from the text (e.g., "+2d6" or "+1d6 fire").
Store `rawText` for non-damage heightening effects (display as-is).

### 9.4 Cast Level Selection

- UI shows base level + all specific levels with heighten rules
- User picks cast level
- App resolves final damage expression:
  - PER_LEVEL: `baseDamage + (additionalDamage × intervals)` where `intervals = castLevel - baseLevel`
  - SPECIFIC: use the rule matching `atLevel == castLevel`, else nearest below
- Resolved expression fed into standard DAMAGE roll

### 9.5 Spell with Basic Save Display

For `isBasicSave = true`, damage roll result `D`:

```
Critical Success │  No damage
Success          │  D ÷ 2  (round down)
Failure          │  D
Critical Failure │  D × 2
```

---

## 11. Routines

### 10.1 Routine Builder

User creates a named sequence of steps. Each step configures:

- **Label**: free text
- **Has attack roll**: toggle
- **MAP penalty**: picker (0 / −5 / −10) — not auto-calculated; user sets explicitly
- **Agile**: toggle (changes MAP display to −4/−8 for reference only; actual penalty stored as configured)
- **Weapon link**: optional (pulls attack bonus + damage expression from WeaponEntity)
- **Damage expression**: editable (pre-filled from weapon if linked)
- **Crit damage expression**: editable (pre-filled as "double result" from weapon)

**Double Slice example:**

| Step | Label | MAP | Notes |
|---|---|---|---|
| 1 | Main Hand | 0 | linked to weapon A |
| 2 | Off-Hand | 0 | linked to weapon B (Double Slice: no MAP) |

### 10.2 Routine Run Display

Each step renders as a card:

```
┌─ Strike 1 (Bastard Sword) ──────────────────┐
│ Roll: 28   (d20: 15  +13 mod)               │
│ AC ≤ 18   │ CRIT   │ 30 dmg                 │
│ AC 19–28  │  HIT   │ 15 dmg  [4,6,5 + 0]   │
│ AC 29–38  │  MISS  │  —                     │
│ AC ≥ 39   │ C.MISS │  —                     │
└─────────────────────────────────────────────┘
┌─ Double Slice Off-Hand ─────────────────────┐
│ Roll: 22   (d20: 8  +14 mod)               │
│ ...                                         │
└─────────────────────────────────────────────┘
```

"Roll Routine" button executes all steps in order, displaying all cards simultaneously.

---

## 12. Custom Saved Rolls

- Stored as `SavedRollEntity`
- Expression validated by the dice engine parser on save (reject if parse fails)
- Roll type determines display mode
- Accessible from Quick Rolls screen
- Can be scoped to a character or global

---

## 13. Navigation Structure

```
MainActivity (single NavHost)
│
├── HomeFragment
│     Character name/level header, active character quick-stats,
│     quick-action chips: Attack / Skill / Save / Damage / Routine
│
├── CharacterImportFragment
│     Paste JSON → preview → import
│
├── CharacterDetailFragment
│     Read-only sheet: stats, weapons, spells, saves, skills
│
├── RollHubFragment  (bottom nav or drawer entry)
│   ├── AttackRollFragment
│   │     Weapon picker → roll → AC band card
│   ├── SkillRollFragment
│   │     Skill/perception picker + active buffs → roll → result
│   ├── SaveRollFragment
│   │     Save picker + active buffs → roll → result
│   ├── DamageRollFragment
│   │     Weapon or custom expression → roll → regular + crit display
│   └── SpellRollFragment
│         Spell picker + cast level selector → roll → save band display
│
├── RoutineListFragment
│   ├── RoutineBuilderFragment   (create / edit)
│   └── RoutineRunFragment       (execute, show all step cards)
│
├── SavedRollsFragment
│     List of saved expressions → tap to roll
│
├── BuffsFragment
│     Add/toggle/delete buffs per character
│
└── SpellEnrichmentFragment
      Paste aonprd URL → parse preview → save to spell
```

---

## 14. Dependencies

```kotlin
// Room
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
kapt("androidx.room:room-compiler:2.6.1")

// Hilt
implementation("com.google.dagger:hilt-android:2.51")
kapt("com.google.dagger:hilt-compiler:2.51")

// Networking + HTML parsing
implementation("com.squareup.okhttp3:okhttp:4.12.0")
implementation("org.jsoup:jsoup:1.17.2")

// JSON
implementation("com.squareup.moshi:moshi-kotlin:1.15.1")
kapt("com.squareup.moshi:moshi-kotlin-codegen:1.15.1")

// Navigation
implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

// ViewBinding — enabled in build.gradle.kts via buildFeatures { viewBinding = true }
```

---

## 15. Open Questions / Deferred

- **Initiative / encounter tracking**: deferred
- **Condition tracking** (Frightened, Clumsy, etc.): deferred — buffs system can partially model these manually
- **Item enrichment from aonprd**: same scrape flow as spells; deferred until spell flow is validated
- **Multi-character**: Room schema supports it via `characterId` FKs; UI exposes single character for now
- **Spring Boot + MySQL backend**: Room is the data layer for now; service interfaces will be written against a Repository abstraction so the backing store can be swapped
- **Lore skills**: parsed from `lores[]` array as custom skill entries (e.g. "Boneyard Lore", "Warfare Lore"). Each entry stored with its rank value and displayed alongside standard skills in the skill roll picker. Ability modifier is INT. Example from export: `[["Boneyard", 6], ["Warfare", 6], ["Underworld", 6]]` → rank 6 (master) each.
