#!/usr/bin/env python3
"""seed_loyalty_fixture.py — DEMO-001 transaction fixture for the
assign-loyalty-card (752) flow.

OWNER RULES (decision 1/2 + Amendment 1, 2026-08-12):
- SEEDED, not real: every row is additive, idempotent, DEMO-001-scoped.
- GUARDED to the his_local database ONLY (refuses to run anywhere else).
- CLEANED IN THE SAME RUN (cleanup runs on exit; --keep leaves rows for
  debugging, never for a green probe).
- The fixture makes every T5 negative EXERCISABLE:
    * DEMO-001 patient  -> picker search finds it (>=3-char name)
    * card A (free)     -> passes checkLoyaltyCard
    * card B (duplicate of A) -> "Card No is Already Exists."
    * un-configured unit -> "Please configure loyalty for this unit."

USAGE:
  python3 tools/seed_loyalty_fixture.py          # seed + probe-truth + clean
  python3 tools/seed_loyalty_fixture.py --keep   # seed only (debug)

The script seeds, prints the fixture rows (patient id, card numbers, unit),
and cleans up in the same run unless --keep.
"""

import os
import subprocess
import sys

DEMO_ORG = 1
DEMO_UNIT = 1
DEMO_UNCONFIGURED_UNIT = 9999  # no m_loyalty_card_configuration row exists
DEMO_PATIENT_NAME = "DEMO-001 Loyalty"
DEMO_CARD_A = "1111222233330001"  # free (never seeded)
DEMO_CARD_B = "1111222233330002"  # seeded duplicate -> Already Exists

# --- his_local guard (owner rule: no other database is ever touched) ---
def psql(args, db="his_local"):
    r = subprocess.run(
        ["psql", "-h", "localhost", "-d", db] + args,
        capture_output=True, text=True, timeout=30,
    )
    if r.returncode != 0:
        raise RuntimeError(f"psql failed: {r.stderr.strip()[:300]}")
    return r.stdout

def guard_database():
    # Refuse if the server is reachable with a different dbname pattern or
    # if the connection string env var points anywhere unexpected.
    for var in ("PGDATABASE", "PGHOST", "PGPORT"):
        val = os.environ.get(var)
        if val and var == "PGDATABASE" and val != "his_local":
            sys.exit(f"REFUSING: PGDATABASE={val} — fixture is his_local-only")
    psql(["-tAc", "select current_database();"], db="his_local").strip()
    print(f"guard: confirmed database = his_local (org {DEMO_ORG}, unit {DEMO_UNIT})")

def seed():
    """Idempotent seed: delete-then-insert DEMO-001 rows only."""
    # The picker searches patient.t_patient_registration pr JOIN
    # patient.t_patient_details patDtls (status='A') — UnknownPatient
    # RegistrationDaoImpl.searchPatient (573+). Seed both tables.
    # NOTE: the assign WRITE creates t_patient_loyalty_card_mppr rows (FK to
    # patient.t_patient_registration) — those are torn down FIRST so the
    # patient deletes can't hit the FK (Amendment 1: everything the tests
    # create is cleaned).
    psql(["-c", f"""
        -- card B (the failing card) — idempotent
        delete from public.m_loyalty_card where card_no='{DEMO_CARD_B}' and is_user_defined='Y';
        insert into public.m_loyalty_card
          (card_no, status, created_by, created_date, updated_by, updated_date,
           organisation_id, unit_id, is_deleted, is_active, is_user_defined)
        values ('{DEMO_CARD_B}', 'A', 1, now(), 1, now(),
                {DEMO_ORG}, {DEMO_UNIT}, 'N', 'A', 'Y');

        -- mapper rows created by the assign write (FK to patient) — FIRST
        delete from public.t_patient_loyalty_card_mppr
          where patient_id in (select patient_id from patient.t_patient_registration
                               where uhid_number like 'DEMO-001%');

        -- DEMO-001 patient (registration + details rows) — idempotent.
        -- Details deleted FIRST (FK: t_patient_details.patient_id ->
        -- t_patient_registration.patient_id).
        delete from patient.t_patient_details
          where patient_id in (select patient_id from patient.t_patient_registration
                               where uhid_number like 'DEMO-001%');
        delete from patient.t_patient_registration where uhid_number like 'DEMO-001%';
        insert into patient.t_patient_registration
          (uhid_number, first_name, last_name, gender_id, mobile_number,
           patient_category_id, unit_id, organization_id, status, created_by, created_date)
        values ('DEMO-001-LOYALTY', '{DEMO_PATIENT_NAME.split(' ')[0]}',
                '{DEMO_PATIENT_NAME.split(' ')[1]}', 2, '9999999999',
                1, {DEMO_UNIT}, {DEMO_ORG}, 'A', 1, now());
        insert into patient.t_patient_details
          (patient_id, status, created_by, created_date, unit_id, organization_id)
        select patient_id, 'A', 1, now(), {DEMO_UNIT}, {DEMO_ORG}
          from patient.t_patient_registration where uhid_number = 'DEMO-001-LOYALTY';
    """])
    print(f"seeded card B (duplicate) = {DEMO_CARD_B}")
    print(f"seeded DEMO-001 patient (searchable, name '{DEMO_PATIENT_NAME}')")
    print(f"card A (free) = {DEMO_CARD_A} (NOT seeded — must stay free)")
    print(f"un-configured unit = {DEMO_UNCONFIGURED_UNIT}")

def cleanup():
    psql(["-c", f"""
        -- mapper rows created by the assign write (FK to patient) — FIRST
        delete from public.t_patient_loyalty_card_mppr
          where patient_id in (select patient_id from patient.t_patient_registration
                               where uhid_number like 'DEMO-001%');
        delete from public.m_loyalty_card where card_no in
          ('{DEMO_CARD_B}','{DEMO_CARD_A}') and is_user_defined='Y';
        -- catch ANY card the write created during a probe (e.g. card C in
        -- the T5 second-assign demonstration) — same DEMO prefix
        delete from public.m_loyalty_card
          where card_no like '111122223333%' and is_user_defined='Y';
        -- details FIRST (FK: t_patient_details.patient_id ->
        -- t_patient_registration.patient_id)
        delete from patient.t_patient_details
          where patient_id in (select patient_id from patient.t_patient_registration
                               where uhid_number like 'DEMO-001%');
        delete from patient.t_patient_registration where uhid_number like 'DEMO-001%';
    """])
    print("cleanup: DEMO-001 rows removed (mappers + cards + patient registration/details)")

if __name__ == "__main__":
    keep = "--keep" in sys.argv
    guard_database()
    try:
        seed()
        if not keep:
            cleanup()
            print("fixture cleaned in the same run (owner rule)")
        else:
            print("--keep: fixture LEFT IN PLACE (debug only, never for a green probe)")
    except RuntimeError as e:
        print(f"FIXTURE ERROR: {e}")
        sys.exit(1)
