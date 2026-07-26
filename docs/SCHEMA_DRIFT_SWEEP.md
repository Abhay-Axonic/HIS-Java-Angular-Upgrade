# Schema-drift sweep — Phase 1 results (2026-07-26)

**Context:** this session found 4 instances of the same bug shape while doing unrelated rebuild work — a JPA `@Entity` maps a `@Column` that doesn't exist on the real, live table (`NutritionalMaster`→`is_calories`, already fixed; `ProductCategory`→`general_ledger_id`; `UnitMaster`→`discharge_summary_header_logo`; `GrowthChartMaster`→`gender_id`). The owner approved a dedicated sweep to find every other instance systematically. Unlike the earlier systemic duplicate-check sweep (which needed semantic judgment per screen and so had to sample 28 of a larger population), this check is purely structural — does an entity's declared column exist on the live table — so Phase 1 was scripted to be exhaustive across every entity in the codebase, not a sample.

**Methodology:** a two-stage script (not committed to the repo, methodology documented here for reproducibility): (1) parse every `.java` file with `@Entity`/`@MappedSuperclass` under `legacy/*/src/main/java/`, extracting `@Table(name=, schema=)` and every directly-declared `@Column(name="...")`, resolving the 4 real `@MappedSuperclass` base classes into their 301 subclasses; (2) batch-query `information_schema.columns`/`.tables` for all 24 referenced schemas from `his_local`, lowercase both sides (Postgres's real folding behavior), and diff every declared column against the live table, cross-referencing `docs/br076_missing_tables.sql`/`docs/br076_missing_columns.sql` (the existing staging-vs-local-dev-DB drift audit) to separate already-known local-dev-only gaps from genuinely novel findings. Validated against all 4 previously-confirmed bugs (correctly reproduced 3 unfixed, correctly did NOT flag the 1 already-fixed one) before trusting the full run, then spot-checked ~10 more hits directly via `psql \d` — all confirmed real.

**Investigation only. Nothing below has been fixed. No repo files were modified, no DDL was run.**

## Scale

- 1,432 `@Entity` classes scanned + 4 `@MappedSuperclass` base classes resolved (301 subclasses, 2,313 inherited-column checks, 0 mismatches — these are generic audit columns present everywhere, correctly clean).
- 1,335 distinct schema-qualified tables referenced, across 24 schemas.
- 18,460 total `@Column(name=...)` mappings found; 15,808 were checkable against a live table.
- 0 files failed to parse.

## Category 1 — missing-column mismatches (the confirmed bug class): 531 total

- **271 are genuinely novel** — not previously known, same shape as the 4 already-confirmed bugs (a full-entity load will crash with `SQLGrammarException`/`PSQLException: column ... does not exist` the moment anything queries this entity as a whole object, not just a hand-picked column list).
- **260 are already known** local-dev-DB drift (`docs/br076_missing_columns.sql` already documents these — the column exists in the staging reference schema, this specific dev DB just hasn't been migrated to have it; same runtime symptom today, but the fix is a local-DB migration, not a code change).

By module: Entity 519, DietAndKitchen 11, Global 1. (`legacy/Entity/` is a shared model module used across every feature area — see the full breakdown by functional package in the raw data file below.)

**Full list (all 531 rows, both known and novel, with the `known_local_dev_drift` column marking which is which): `docs/schema-drift-sweep/missing_column_mismatches.tsv`**

Recurring bug patterns observed while validating (not exhaustive):
- Literal camelCase used as the `@Column(name=...)` value instead of real snake_case (e.g. `createdDate`, `organizationId`, `unitId` typed verbatim where the real column is `created_date` etc.) — confirmed directly in `LabSampleMaster.java` and others.
- Trailing/leading whitespace baked into the literal column-name string (`"value_desc "`, `"org_unit_id "`, `"rejected_qty "` — 20+ instances) — Hibernate treats the quoted whitespace as part of the identifier, so these can never match the real column even though they look identical to a human reader.
- Consistent misspellings: `orgnisation_id` (9 instances), `cerated_date` (4 instances).

**One structural anomaly flagged separately, higher risk than a single missing column:** `ServiceTarrifStattimeManager.java` declares `@Table(name="t_service tarrif stattime manager", schema="service")` — the table name itself contains literal spaces. A table with that exact spaced name does genuinely exist in `his_local` (confirmed), so this entity technically resolves today, but a table name with embedded spaces is a landmine for any future raw-SQL tooling, migration script, or copy-paste — worth its own follow-up look independent of the missing-column sweep.

## Category 2 — entity references a table that doesn't exist in `his_local` at all: 184 total (counts only, not detailed per-item in this pass)

- 56 already known (`docs/br076_missing_tables.sql`).
- 128 novel — not previously documented. Full list preserved in `docs/schema-drift-sweep/full_diff_result.json` (`missing_table_hits` key) for future reference, not narrated individually here per the sweep's scope (Phase 1 was scoped to the missing-column bug class; missing-table entities are a related but distinct gap, likely a mix of genuinely-dead entities and further local-DB provisioning gaps — not triaged in this pass).
- 1 entity (`PostPurchaseRequest.java`) has `@Entity` with no `@Table`/`@Column` at all — a request-payload DTO mistakenly annotated `@Entity`, excluded from both categories, not a real bug.

## Known limitations of this pass (explicit, not silently dropped)

- 385 `@Column(...)` annotations with no `name=` attribute were skipped (Hibernate falls back to a naming-strategy-derived column name in these cases — not reliably checkable without knowing the exact strategy in use, so these were excluded rather than guessed).
- 14 bare `@Column` (no parentheses) skipped for the same reason.
- ~149 fields with no `@Column` annotation at all were heuristically skipped (this count is an upper bound — it includes some false positives like `serialVersionUID` constants).

## What this does NOT tell you yet

This is a raw structural inventory, not a prioritized bug list. None of the 271 novel hits have been checked for whether the entity is ever actually loaded as a full object anywhere in the live, reachable codebase — a mismatch on a genuinely dead/unused entity is a non-issue; a mismatch on an entity loaded by an actively-used screen is a live, user-facing crash waiting to happen (exactly like the 4 already-confirmed instances). That triage (Phase 2) has not been started — scope and time estimate to be proposed separately once the owner has seen these numbers, since a realistic estimate depends on decisions about how deep to go (e.g. automated reachability pre-filtering vs. full manual triage of all 271+128 candidates).
