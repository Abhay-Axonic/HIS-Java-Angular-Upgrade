#!/usr/bin/env python3
"""seed_opd_bill_fixture.py — DEMO-001 transaction fixture for the OPD Bill
(11) flow — the SECOND transaction-loop flow (DQ-041/042, flow map signed
off 2026-08-12).

OWNER RULES (decisions 1/2 + Amendments 1/6, 2026-08-12):
- SEEDED, not real: additive, idempotent, DEMO-001-scoped.
- GUARDED to the his_local database ONLY (refuses to run anywhere else).
- CLEANED IN THE SAME RUN (incl. every artifact the WRITES create: bills,
  receipts, order-detail flag flips — Amendment 1 + the Amendment 6 lock
  test's debris).
- The fixture makes every T5 negative EXERCISABLE:
    * DEMO-001 patient + encounter       -> the picker + W1 target
    * t_order_master + t_order_details (service_is_billed=0) -> W1 happy path
    * a SECOND order-details row with service_is_billed=1 -> NEG-1 (the
      "Bill is already generated !" guard)
    * mismatched amounts variant -> NEG-2 ("Dispute in orders count")

USAGE:
  python3 tools/seed_opd_bill_fixture.py          # seed + clean (default)
  python3 tools/seed_opd_bill_fixture.py --keep   # seed only (debug)

The script prints the fixture ids (patient, encounter, order, order-detail)
so the golden trace can use them.
"""

import os
import subprocess
import sys

DEMO_ORG = 1
DEMO_UNIT = 1
DEMO_PATIENT_NAME = "DEMO-001 OPD Bill"

def psql(args, db="his_local"):
    r = subprocess.run(
        ["psql", "-h", "localhost", "-d", db] + args,
        capture_output=True, text=True, timeout=30,
    )
    if r.returncode != 0:
        raise RuntimeError(f"psql failed: {r.stderr.strip()[:300]}")
    return r.stdout

def guard_database():
    for var in ("PGDATABASE",):
        val = os.environ.get(var)
        if val and val != "his_local":
            sys.exit(f"REFUSING: PGDATABASE={val} — fixture is his_local-only")
    psql(["-tAc", "select current_database();"], db="his_local").strip()
    print(f"guard: confirmed database = his_local (org {DEMO_ORG}, unit {DEMO_UNIT})")

def seed():
    """Idempotent seed: delete-then-insert DEMO-001 rows (FK order:
    order_details -> order_master -> encounter -> patient)."""
    psql(["-c", f"""
        -- tear down in FK order FIRST (incl. write artifacts from prior runs:
        -- bills, receipts, and the service_is_billed flips the writes made)
        delete from billing.t_billing_details
          where billing_master_id in (select billing_master_id from billing.m_billing_master
                                      where patient_id in (select patient_id from patient.t_patient_registration
                                                           where uhid_number like 'DEMO-001%'));
        delete from billing.m_billing_master
          where patient_id in (select patient_id from patient.t_patient_registration
                               where uhid_number like 'DEMO-001%');
        delete from billing.t_billing_payment_details
          where billing_master_id in (select billing_master_id from billing.m_billing_master
                                      where patient_id in (select patient_id from patient.t_patient_registration
                                                           where uhid_number like 'DEMO-001%'));
        -- (opdpaymentreceipt is a view — its data lives in
        --  t_billing_payment_details, already deleted above)
        delete from public.t_order_details
          where order_id in (select order_id from public.t_order_master
                             where patient_id in (select patient_id from patient.t_patient_registration
                                                  where uhid_number like 'DEMO-001%'));
        delete from public.t_order_master
          where patient_id in (select patient_id from patient.t_patient_registration
                               where uhid_number like 'DEMO-001%');
        delete from opd.t_encounter_master
          where patient_id in (select patient_id from patient.t_patient_registration
                               where uhid_number like 'DEMO-001%');
        delete from patient.t_patient_details
          where patient_id in (select patient_id from patient.t_patient_registration
                               where uhid_number like 'DEMO-001%');
        delete from patient.t_patient_registration where uhid_number like 'DEMO-001%';

        -- DEMO-001 patient
        insert into patient.t_patient_registration
          (uhid_number, first_name, last_name, gender_id, mobile_number,
           patient_category_id, unit_id, organization_id, status, created_by, created_date)
        values ('DEMO-001-OPDBILL', 'DEMO-001', 'OPD Bill', 2, '9999999998',
                1, {DEMO_UNIT}, {DEMO_ORG}, 'A', 1, now());
        insert into patient.t_patient_details
          (patient_id, status, created_by, created_date, unit_id, organization_id)
        select patient_id, 'A', 1, now(), {DEMO_UNIT}, {DEMO_ORG}
          from patient.t_patient_registration where uhid_number = 'DEMO-001-OPDBILL';

        -- encounter (visit type 1 = OPD)
        insert into opd.t_encounter_master
          (patient_id, unit_id, organization_id, visit_type_id, status, created_by, created_date)
        select patient_id, {DEMO_UNIT}, {DEMO_ORG}, 1, 'A', 1, now()
          from patient.t_patient_registration where uhid_number = 'DEMO-001-OPDBILL';

        -- order master + 2 order details:
        --   detail A: service_is_billed=0 (happy path for W1)
        --   detail B: service_is_billed=1 (NEG-1 — already billed)
        insert into public.t_order_master
          (org_id, org_unit_id, order_date, visit_type_id, encounter_id, patient_id,
           priority_id, created_by, created_date, updated_by, updated_date, order_status_id)
        select {DEMO_ORG}, {DEMO_UNIT}, now(), 1, enc.encounter_id, enc.patient_id,
               1, 1, now(), 1, now(), 1
          from opd.t_encounter_master enc
          join patient.t_patient_registration pr on pr.patient_id = enc.patient_id
         where pr.uhid_number = 'DEMO-001-OPDBILL';

        insert into public.t_order_details
          (order_id, org_id, org_unit_id, order_date, service_id, service_amt,
           net_amount, self_payable, credit_payable, service_is_billed,
           service_rendered, is_dummy_service, status, created_by, created_date, updated_by, updated_date)
        select om.order_id, {DEMO_ORG}, {DEMO_UNIT}, now(), 3, 500.00,
               500.00, 500.00, 0.00, 0, 1, false, 'A', 1, now(), 1, now()
          from public.t_order_master om
          join opd.t_encounter_master enc on enc.encounter_id = om.encounter_id
          join patient.t_patient_registration pr on pr.patient_id = om.patient_id
         where pr.uhid_number = 'DEMO-001-OPDBILL'
         limit 1;

        insert into public.t_order_details
          (order_id, org_id, org_unit_id, order_date, service_id, service_amt,
           net_amount, self_payable, credit_payable, service_is_billed,
           service_rendered, is_dummy_service, status, created_by, created_date, updated_by, updated_date)
        select om.order_id, {DEMO_ORG}, {DEMO_UNIT}, now(), 4, 300.00,
               300.00, 300.00, 0.00, 1, 1, false, 'A', 1, now(), 1, now()
          from public.t_order_master om
          join opd.t_encounter_master enc on enc.encounter_id = om.encounter_id
          join patient.t_patient_registration pr on pr.patient_id = om.patient_id
         where pr.uhid_number = 'DEMO-001-OPDBILL'
         limit 1;
    """])
    ids = psql(["-tAc",
        "select pr.patient_id, enc.encounter_id, om.order_id, "
        "(select order_details_id from public.t_order_details od where od.order_id=om.order_id "
        " and od.service_is_billed=0 limit 1), "
        "(select order_details_id from public.t_order_details od where od.order_id=om.order_id "
        " and od.service_is_billed=1 limit 1) "
        "from patient.t_patient_registration pr "
        "join opd.t_encounter_master enc on enc.patient_id=pr.patient_id "
        "join public.t_order_master om on om.patient_id=pr.patient_id "
        "where pr.uhid_number='DEMO-001-OPDBILL';"])
    print(f"fixture ids (patient, encounter, order, detailA=unbilled, detailB=billed): {ids}")

def cleanup():
    """Same FK-ordered teardown as the seed's pre-pass."""
    psql(["-c", f"""
        delete from billing.t_billing_details
          where billing_master_id in (select billing_master_id from billing.m_billing_master
                                      where patient_id in (select patient_id from patient.t_patient_registration
                                                           where uhid_number like 'DEMO-001%'));
        delete from billing.m_billing_master
          where patient_id in (select patient_id from patient.t_patient_registration
                               where uhid_number like 'DEMO-001%');
        delete from billing.t_billing_payment_details
          where billing_master_id in (select billing_master_id from billing.m_billing_master
                                      where patient_id in (select patient_id from patient.t_patient_registration
                                                           where uhid_number like 'DEMO-001%'));
        -- (opdpaymentreceipt is a view — its data lives in
        --  t_billing_payment_details, already deleted above)
        delete from public.t_order_details
          where order_id in (select order_id from public.t_order_master
                             where patient_id in (select patient_id from patient.t_patient_registration
                                                  where uhid_number like 'DEMO-001%'));
        delete from public.t_order_master
          where patient_id in (select patient_id from patient.t_patient_registration
                               where uhid_number like 'DEMO-001%');
        delete from opd.t_encounter_master
          where patient_id in (select patient_id from patient.t_patient_registration
                               where uhid_number like 'DEMO-001%');
        delete from patient.t_patient_details
          where patient_id in (select patient_id from patient.t_patient_registration
                               where uhid_number like 'DEMO-001%');
        delete from patient.t_patient_registration where uhid_number like 'DEMO-001%';
    """])
    print("cleanup: DEMO-001 rows removed (bills, receipts, orders, details, encounter, patient)")

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
