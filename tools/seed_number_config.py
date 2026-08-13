#!/usr/bin/env python3
"""seed_number_config.py — DQ-067: seed the NUMBER-CONFIGURATION rows the
application REQUIRES to function.

OWNER DECISION (DQ-067, 2026-08-13): SEED IT — a documented, reproducible,
idempotent seed script under tools/, NOT ad-hoc rows and NOT a test fixture.
t_prefix_running_number + m_prefix_configuration are CONFIGURATION the
application requires: an empty config is a dev-environment gap, not a defect
and not test data.

DIFFERENCES FROM THE DEMO FIXTURES (deliberate, per the owner):
- PERSISTENT: these rows are NEVER torn down at the end of a run. Number
  configuration must persist, or every flow that needs it breaks again on the
  next run. (The DEMO-001 fixtures are cleaned in-run; this seed is not.)
- IDEMPOTENT: re-runnable — each row is inserted only if absent (WHERE NOT
  EXISTS on screen_id + unit_id).
- GUARDED to his_local ONLY (refuses to run anywhere else — the same guard
  as every other seed script).

WHAT EACH ROW IS FOR (which flow consumes it, which number it generates):

1. m_prefix_configuration / t_prefix_running_number, screen_id = 146
   CONSUMER: DonorRegistrationServiceImpl:102
   `getGeneratedNo(0, unitId, DONORNUMBER)` — DONORNUMBER = 146
   (IBillingCommonConstants:34). GENERATES: the donor registration number
   (donorNumber, e.g. "DON-000001"). Was blocking: DQ-056 — the donor
   smoke's donorNumber came back null; the whole registration 501'd.

2. m_prefix_configuration / t_prefix_running_number, screen_id = 164
   CONSUMER: WalkInSalesService:1485 (com.param.global.pharmacy.service)
   `getGeneratedNo(0, unitId, 164)`. GENERATES: the walk-in prescription
   number (Prescription.prescriptionNo). Was blocking: the walk-in sale
   write — "not-null property references a null or transient value:
   Prescription.prescriptionNo" (honest 500).

3. m_prefix_configuration / t_prefix_running_number, screen_id = 123
   CONSUMER: DepositMasterServiceImpl:124 + PaymentRefundDetailsServiceImpl:235
   `getGeneratedNo(0, unitId, depositScreenId)` — depositScreenId = 123
   (IBillingCommonConstants:13). GENERATES: the deposit number
   (deposit_no). Was blocking: "Please Configure Deposit Number." — the
   deposit write never reached the INSERT (the m_deposit_master drift flags
   remain unreached).

Also seeds: billing.deposit_master_seq to >= 1 (the sequence was at 0 —
"setval: value 0 is out of bounds" — the deposit master's INSERT died before
the columns).

UNBLOCKS three flows; fixes nothing (per the owner's record): the donor
number, the walk-in prescription number and the deposit number were never
broken, they were unconfigured.

USAGE:
  python3 tools/seed_number_config.py    # idempotent; safe to re-run

SKIP-VS-HEAL RULE (2026-08-13, the third instance of a fixture failing
quietly): an idempotency guard that SKIPS rather than HEALS is a silent
no-op — WHERE NOT EXISTS on an already-present pre-donor bag made the re-run
skip the insert and left the donor unwired, which looks identical to
success. Any seed that wires a RELATIONSHIP must HEAL an existing row
(an UPDATE for the missing attribute), not just skip inserting a duplicate —
otherwise the second run produces a different state from the first and
nothing says so. The three fixture-failing-quietly instances: (1) the
orphaned t_cross_match_register_details (the teardown missed the child
table — the TEARDOWN RULE), (2) the skipped donor wire (this — the
SKIP-VS-HEAL rule), (3) the [SF] token (the incomplete generated_code —
healed by the idempotent UPDATE). Every one of them presented as success
and was found by a harness check.
"""

import subprocess
import sys

DB = "his_local"

# --- his_local guard (owner rule: no other database is ever touched) ---
def current_db():
    r = subprocess.run(
        ["psql", "-h", "localhost", "-d", "postgres", "-tAc", "select current_database();"],
        capture_output=True, text=True, timeout=30,
    )
    return r.stdout.strip()

def psql(sql):
    r = subprocess.run(
        ["psql", "-h", "localhost", "-d", DB, "-v", "ON_ERROR_STOP=1", "-tAc", sql],
        capture_output=True, text=True, timeout=30,
    )
    if r.returncode != 0:
        raise RuntimeError(f"psql failed: {r.stderr.strip()[:400]}")
    return r.stdout.strip()

def main():
    # Guard: verify the TARGET database is his_local (and the shell is not
    # pointed anywhere else via PGDATABASE).
    db = current_db()
    if db != "postgres" or db == DB:
        print(f"GUARD: cannot verify environment (current_database={db!r}); aborting")
        return 1
    check = psql("select current_database();")
    if check != DB:
        print(f"GUARD: refusing to seed — target database is {check!r}, expected {DB!r}")
        return 1
    print(f"GUARD: target database = {DB} (ok)")

    # --- m_screen 164 (walk-in prescription) — the dev m_screen lacks this
    # row entirely (a SECOND seeding gap of the same class: without the screen
    # row the getGeneratedNo join can never match). The neighbours 123
    # (Deposit, code DEPO) and 146 (Donor Number, code DN) exist; 164 does not.
    psql(
        "insert into public.m_screen "
        "(id, name, is_active, updated_by, updated_date, added_by, added_date, is_deleted, "
        " code, is_prefix_required, organization_id, is_global, doctypenumber) "
        "select 164, 'Walkin Prescription', true, 1, now(), 1, now(), false, "
        " 'WP', true, 1, false, null "
        "where not exists (select 1 from public.m_screen where id = 164);"
    )
    print("SEEDED m_screen id=164 'Walkin Prescription' (was missing; the getGeneratedNo join needs it)")
    psql(
        "insert into public.m_screen "
        "(id, name, is_active, updated_by, updated_date, added_by, added_date, is_deleted, "
        " code, is_prefix_required, organization_id, is_global, doctypenumber) "
        "select 149, 'Blood Bag Issue Number', true, 1, now(), 1, now(), false, "
        " 'BBI', true, 1, false, null "
        "where not exists (select 1 from public.m_screen where id = 149);"
    )
    print("SEEDED m_screen id=149 'Blood Bag Issue Number' (the 4th config screen)")

    # Pre-check: the screens must exist in m_screen (the getGeneratedNo query
    # joins m_screen with is_global = false).
    missing = psql(
        "select string_agg(s::text, ',') from unnest(array[123,146,164,149]) s "
        "where not exists (select 1 from public.m_screen scr where scr.id = s and scr.is_global = false);"
    )
    if missing and missing.strip():
        print(f"PRE-CHECK: missing m_screen rows (is_global=false): {missing.strip()} — "
              f"the config rows would never be read; aborting")
        return 1
    print("PRE-CHECK: screens 123/146/164/149 present in m_screen (is_global=false)")

    # --- m_prefix_configuration rows (idempotent) ---
    # generated_code carries the legacy tokens: [SF] = the sequential running
    # number (GlobalUnitMasterDaoImpl:445), [YYYY]/[MM]/[DD] = date parts.
    # A code WITHOUT [SF] yields a bare prefix ("DON-") — the 4th dev gap.
    rows = [
        # (screen_id, code, generated_code, length, running_number, doc)
        (146, "DON", "DON-[SF]", 8, 1, "donor registration number (DonorRegistrationServiceImpl:102)"),
        (164, "PRE", "PRE-[SF]", 8, 1, "walk-in prescription number (WalkInSalesService:1485)"),
        (123, "DEP", "DEP-[SF]", 8, 1, "deposit number (DepositMasterServiceImpl:124)"),
        (149, "BBI", "BB-[SF]", 8, 1, "blood-bag-issue number (IssueBloodBagServiceImpl:56, BLOOD_BAG_ISSUE_NUMBER=149)"),
    ]
    for screen_id, code, generated_code, length, running_number, doc in rows:
        sql = (
            "insert into public.m_prefix_configuration "
            "(organization_id, unit_id, screen_id, running_number, reset_id, length, format_id, code, "
            " is_active, updated_by, updated_date, added_by, added_date, is_deleted, generated_code) "
            "select 1, 1, {screen}, {run}, 1, {length}, 1, '{code}', true, 1, now(), 1, now(), false, '{gen}' "
            "where not exists (select 1 from public.m_prefix_configuration "
            "where screen_id = {screen} and unit_id = 1 and is_deleted = false);"
        ).format(screen=screen_id, run=running_number, length=length, code=code, gen=generated_code)
        psql(sql)
        # Idempotent UPDATE: re-runs heal the generated_code (the [SF] token)
        psql(
            "update public.m_prefix_configuration set generated_code = '{gen}' "
            "where screen_id = {screen} and unit_id = 1 and is_deleted = false "
            "and generated_code is distinct from '{gen}';".format(screen=screen_id, gen=generated_code)
        )
        print(f"SEEDED m_prefix_configuration screen={screen_id} code={code} ({doc})")

    # --- t_prefix_running_number rows (idempotent) ---
    for screen_id, _code, _generated_code, _length, running_number, doc in rows:
        sql = (
            "insert into public.t_prefix_running_number "
            "(organization_id, unit_id, screen_id, store_id, current_running, is_active, "
            " updated_by, updated_date, added_by, added_date, is_deleted) "
            "select 1, 1, {screen}, NULL, {run}, true, 1, now(), 1, now(), false "
            "where not exists (select 1 from public.t_prefix_running_number "
            "where screen_id = {screen} and unit_id = 1 and store_id is null);"
        ).format(screen=screen_id, run=running_number)
        psql(sql)
        print(f"SEEDED t_prefix_running_number screen={screen_id} current_running={running_number} ({doc})")

    # --- deposit_master_seq to >= 1 (was 0: setval out of bounds) ---
    seq_val = psql("select last_value from billing.deposit_master_seq;")
    try:
        last = int(seq_val)
    except ValueError:
        print(f"WARN: cannot read billing.deposit_master_seq (got {seq_val!r})")
        last = 0
    if last < 1:
        psql("select setval('billing.deposit_master_seq', 1, false);")
        print("SEEDED billing.deposit_master_seq -> 1 (was 0; deposit INSERT died on nextval)")
    else:
        print(f"billing.deposit_master_seq already >= 1 (last_value={last}) — untouched")

    # --- blood-bank stock (DQ-067 extension 2026-08-13) ---
    # The dev's stock tables are EMPTY — the blood-issue (DQ-060) and the
    # cross-match's final layer (DQ-068, the detail's bloodBankStockId) both
    # load a stock row. Same class as the config: an unseeded environment,
    # not a defect — PERSISTENT baseline stock (idempotent by bag number).
    psql(
        "insert into blood_bank.t_donor_registration "
        "(donor_number, first_name, last_name, dob, mobile_number, gender_id, status, "
        " created_by, created_date, unit_id, organization_id, updated_by, updated_date) "
        "select 'DON-SEED', 'DEMO-SEED', 'Donor', '1990-01-01 00:00:00', '9999999990', 1, 'A', "
        " 1, now(), 1, 1, 1, now() "
        "where not exists (select 1 from blood_bank.t_donor_registration where donor_number = 'DON-SEED');"
    )
    print("SEEDED t_donor_registration DON-SEED (the bag's donor — the blood-issue load chain)")
    psql(
        "insert into blood_bank.m_blood_bank_stock "
        "(blood_bank_id, short_name, blood_bag_no, segment_number, batch_no, blood_group_id, "
        " blood_bag_capacity_id, blood_bag_type_id, blood_unit, donor_id, donor_number, stock_status_id, status, "
        " unit_id, organization_id, created_by, created_date) "
        "select 1, 'BB1', 'SMOKE-BAG1', 'S1', 'B1', 1, 1, 1, 1, "
        " (select donor_id from blood_bank.t_donor_registration where donor_number='DON-SEED'), "
        " 'DON-SEED', 1, 'A', 1, 1, 1, now() "
        "where not exists (select 1 from blood_bank.m_blood_bank_stock where blood_bag_no = 'SMOKE-BAG1');"
    )
    print("SEEDED m_blood_bank_stock blood_bag_no=SMOKE-BAG1 (with the donor — blood-issue + cross-match stock context)")
    # Idempotent heal: a bag row seeded BEFORE the donor existed must get the
    # donor wired on re-runs (the harness caught this — the insert was skipped
    # by the WHERE NOT EXISTS on an already-present pre-donor bag).
    psql(
        "update blood_bank.m_blood_bank_stock set "
        " donor_id = (select donor_id from blood_bank.t_donor_registration where donor_number='DON-SEED'), "
        " donor_number = 'DON-SEED' "
        "where blood_bag_no = 'SMOKE-BAG1' and (donor_id is null or donor_number is null);"
    )
    print("HEALED m_blood_bank_stock SMOKE-BAG1 donor wiring (idempotent)")
    psql(
        "insert into blood_bank.t_blood_bank_stock_details "
        "(blood_bank_id, blood_group_id, blood_bag_capacity_id, blood_bag_type_id, blood_component_id, "
        " available_stock, status, unit_id, organization_id) "
        "select 1, 1, 1, 1, 5, 5, 'A', 1, 1 "
        "where not exists (select 1 from blood_bank.t_blood_bank_stock_details "
        "where blood_bank_id = 1 and blood_component_id = 5 and unit_id = 1);"
    )
    print("SEEDED t_blood_bank_stock_details (component 5, available 5)")

    # --- verification ---
    counts = psql(
        "select (select count(*) from public.m_prefix_configuration where unit_id=1 and screen_id in (123,146,164,149) and is_deleted=false) "
        "|| ' prefix rows, ' || "
        "(select count(*) from public.t_prefix_running_number where unit_id=1 and store_id is null and screen_id in (123,146,164,149)) "
        "|| ' running rows';"
    )
    print("VERIFY:", counts)
    print("DONE — rows are PERSISTENT (never torn down). Re-smoke the donor / walk-in / deposit flows now.")
    return 0

if __name__ == "__main__":
    sys.exit(main())
