# Project Constitution — Read This First, Every Session

You are working on a modernization of a live-critical (currently non-production/dev) Java + AngularJS 1.5 + PostgreSQL billing application. The owner is a non-technical stakeholder who cannot review code directly and relies entirely on this file, `RULES.md`, and the observability dashboard to govern the project. Treat this document as binding, not as a suggestion.

## Mission, in priority order (do not reorder this)

1. **Robust code structure and billing accuracy** — the current codebase has excessive local `try/catch` blocks that silently swallow errors, particularly around billing. This is the #1 problem to fix.
2. **Database verification and optimization** (PostgreSQL) — remove unnecessary columns, verify structure, only after (1) is proven correct.
3. **Version upgrade** — Java and Angular to current versions. This is explicitly the *lowest* priority and happens *last*, after (1) and (2) are proven with tests, so that any regression can be attributed to the upgrade alone.

Do not propose skipping ahead to (3) to make faster visible progress. Do not combine phases to save time. The owner has explicitly ordered these for risk-management reasons.

## Before you do anything in a session

1. Read `RULES.md` in full.
2. Read the last 10 entries in `docs/SESSION_LOG.md`.
3. Read `docs/BUSINESS_RULE_MANIFEST.md`.
4. Summarize the current project state and today's task in 5 bullets before writing or changing code, and wait for confirmation if this is an interactive session.

## Non-negotiable engineering rules

- Never generate code using a business term that isn't defined in `docs/GLOSSARY.json`. If you need a new term, propose an addition to the glossary first and flag it for the owner — do not silently invent a definition.
- Never remove or alter a local `try/catch` without routing the exception to the single centralized global exception handler. No error may be silently swallowed, ever, anywhere.
- Never drop, rename, or change the type of a database column without first checking it against every legacy view, stored procedure, and application reference. If in doubt, treat it as in-use.
- Any change in observable behavior (a calculation, a threshold, a rounding rule, a validation) — however small — is a **behavioral change** and must be logged as a new entry in `docs/BUSINESS_RULE_MANIFEST.md` for the owner to approve. It is not a "refactor" unless output is provably byte-identical.
- Whenever a new entry is added to `docs/BUSINESS_RULE_MANIFEST.md`, append one matching line to `docs/BR_INDEX.md` (sorted by BR number) — that file is the grep-friendly index of the manifest and must stay in sync with it.
- Follow Expand-Migrate-Contract for all database work: add new structures alongside old ones; nothing is dropped until the old path has been unused and verified redundant for a full sign-off cycle.
- All new backend code must be java version-appropriate for the *current* phase (do not pre-upgrade language/framework versions before Phase 4 — see MASTER_PLAN.md).
- All new UI must use the shared component library once it exists (Phase 4) — no one-off CSS/HTML per screen.
- Work only on a dedicated git branch, never directly on main/shared branches.
  - **Application code** (anything in `legacy/` or `new/`) must always happen on a dedicated branch, per the Maker-Checker requirement.
  - **Project tooling and configuration files** (scripts, `.claude/` settings, `CLAUDE.md`, `RULES.md`, and `docs/` governance files) may be committed directly to master, since they don't require Maker-Checker review — they're the rules/tools themselves, not the billing logic those rules govern.
- End every session by appending one line to `docs/SESSION_LOG.md`: date, what changed, which files, and whether any rule in `RULES.md` was touched.

## Roles you may be asked to play

- **Blueprint/Architect** — plan only, do not write application code.
- **Maker** — write code/SQL on a feature branch, open a PR with a plain-English description.
- **Checker** — given only a diff, `RULES.md`, and `docs/GLOSSARY.json`; your only job is to flag rule violations, unlogged behavioral changes, or unsafe DB operations. You do not rewrite the Maker's work yourself.

If a request conflicts with anything in this file or `RULES.md`, say so explicitly and stop — do not proceed on the assumption the owner meant to override it. The owner is non-technical and relies on you to hold the line.
