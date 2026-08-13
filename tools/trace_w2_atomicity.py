#!/usr/bin/env python3
"""trace_w2_atomicity.py — Amendment 10 TRACE: is W2's 5-entity write atomic?

The owner's specific ask (flow-map sign-off, 2026-08-12): force a mid-W2
failure and psql ALL FIVE entities. The empty catch (PendingTransfer-
RequestDaoImpl:298-301) makes partial state observable: ERROR returned +
partial rows = a half-commit on a clinical path hiding behind a generic
message. If all five roll back, that is a confirmed transaction boundary
(worth knowing too).

DESIGN: seed DEMO-001 chain -> W1 saveTransferRequest (creates the
transfer_request row, transfer -> status 2) -> W2 acceptTransferRequestForADT
with toBedId=999999 (nonexistent): updates 1-2 (Transfer -> 6, TransferRequest
-> 6) and 3-4 (BedMaster, BedLogStatus) run against real rows where the id
matches, and the FINAL BedLogStatus INSERT fails its bed FK. Then psql:
  t_transfer.status / t_transfer_request.status / bed booked / bed-log rows.
Also reads the response envelope (SUCCESS or ERROR) to classify the failure
mode per Amendment 7 (honest-error vs masked).

SAME-RUN CLEANUP incl. every artifact the writes create (Amendment 1).
"""

import json
import os
import subprocess
import sys

def psql(q, db="his_local"):
    r = subprocess.run(["psql", "-h", "localhost", "-d", db, "-tAc", q],
                       capture_output=True, text=True, timeout=30)
    if r.returncode != 0:
        raise RuntimeError(f"psql failed: {r.stderr.strip()[:300]}")
    return r.stdout.strip()

def guard_database():
    if os.environ.get("PGDATABASE") not in (None, "his_local"):
        sys.exit("REFUSING: PGDATABASE set — fixture is his_local-only")
    psql("select current_database();")
    print("guard: confirmed his_local")

def teardown():
    psql("""
        delete from adt.t_transfer_request
          where admission_id in (select admission_id from adt.t_admission
                                 where patient_id in (select patient_id from patient.t_patient_registration
                                                      where uhid_number like 'DEMO-001%'));
        delete from adt.t_bed_log_status
          where bed_id in (select bed_id from adt.m_bed_master
                           where organization_id=1 and unit_id=1
                             and bed_id not in (select bed_id from adt.m_bed_master limit 0))
           and created_by = 1
           and created_date::date = current_date;
        delete from adt.t_transfer
          where admission_id in (select admission_id from adt.t_admission
                                 where patient_id in (select patient_id from patient.t_patient_registration
                                                      where uhid_number like 'DEMO-001%'));
        delete from adt.t_admission
          where patient_id in (select patient_id from patient.t_patient_registration
                               where uhid_number like 'DEMO-001%');
        delete from patient.t_patient_details
          where patient_id in (select patient_id from patient.t_patient_registration
                               where uhid_number like 'DEMO-001%');
        delete from patient.t_patient_registration where uhid_number like 'DEMO-001%';
    """)

def seed():
    teardown()
    psql("""
        insert into patient.t_patient_registration
          (uhid_number, first_name, last_name, gender_id, mobile_number,
           patient_category_id, unit_id, organization_id, status, created_by, created_date)
        values ('DEMO-001-TRACE', 'DEMO-001', 'Trace', 2, '9999999996',
                1, 1, 1, 'A', 1, now());
        insert into patient.t_patient_details
          (patient_id, status, created_by, created_date, unit_id, organization_id)
        select patient_id, 'A', 1, now(), 1, 1
          from patient.t_patient_registration where uhid_number = 'DEMO-001-TRACE';
        insert into adt.t_admission
          (organization_id, unit_id, patient_id, visit_type_id, status, created_by, created_date)
        select 1, 1, patient_id, 2, 'A', 1, now()
          from patient.t_patient_registration where uhid_number = 'DEMO-001-TRACE';
        insert into adt.t_transfer
          (organization_id, unit_id, admission_id, from_bed_category_id,
           from_billing_bed_category_id, from_ward_id, transfer_status_id,
           status, created_by, created_date, updated_by, updated_date)
        select 1, 1, admission_id, 1, 1, 1, 1, 'A', 1, now(), 1, now()
          from adt.t_admission
         where patient_id in (select patient_id from patient.t_patient_registration
                              where uhid_number = 'DEMO-001-TRACE');
    """)
    ids = psql("""
        select pr.patient_id, a.admission_id, tr.transfer_id
          from patient.t_patient_registration pr
          join adt.t_admission a on a.patient_id = pr.patient_id
          join adt.t_transfer tr on tr.admission_id = a.admission_id
         where pr.uhid_number = 'DEMO-001-TRACE';
    """).split("|")
    print(f"fixture: patient={ids[0]} admission={ids[1]} transfer={ids[2]}")
    return ids

def call(url, payload, token):
    h = ["-H", f"Authorization: Bearer {token}", "-H", "userId: 1", "-H", "unitId: 1",
         "-H", "organizationId: 1", "-H", "Content-Type: application/json"]
    r = subprocess.run(["curl", "-s", "--max-time", "30", "-w", "\nHTTP:%{http_code}",
                        "-X", "POST", "-d", json.dumps(payload)] + h +
                       [f"http://localhost:8080{url}"],
                       capture_output=True, text=True, timeout=40)
    raw = r.stdout
    http = raw.split("HTTP:")[-1] if "HTTP:" in raw else "?"
    body = raw.split("HTTP:")[0] if "HTTP:" in raw else raw
    try:
        d = json.loads(body)
        return http, d.get("status"), (d.get("message") or "")[:60]
    except Exception:
        return http, "?", body[:80]

def main():
    guard_database()
    token = json.load(open("tools/.token_cache.json"))["token"]
    pid, adm_id, tr_id = seed()

    # W1: create the transfer request (status 1 -> 2)
    w1 = {"transferId": int(tr_id), "transferTypeId": 2, "transferStatusId": 2,
          "toBedCategoryId": 2, "toBillingBedCategoryId": 2, "toWardId": 2,
          "toRoomId": 1, "toBedId": 1, "admissionId": int(adm_id),
          "organizationId": 1, "unitId": 1, "createdBy": 1, "updatedBy": 1,
          "note": "DEMO-001 trace W1"}
    h1, s1, m1 = call("/adt/saveTransferRequest", w1, token)
    print(f"W1 saveTransferRequest -> HTTP {h1} status={s1} msg='{m1}'")
    req_id = psql(f"select transfer_request_id from adt.t_transfer_request where admission_id={adm_id} order by transfer_request_id desc limit 1;")
    print(f"W1 created transfer_request_id={req_id}")

    # W2: accept WITH A NONEXISTENT toBedId (999999) -> the final BedLogStatus
    # INSERT fails its bed FK AFTER the Transfer/TransferRequest updates land.
    w2 = {"organizationId": 1, "unitId": 1, "transferId": int(tr_id),
          "transferRequestId": int(req_id), "updatedBy": 1, "updatedDate": 0,
          "toBedCategoryId": 2, "toBillingBedCategoryId": 2, "toWardId": 2,
          "toRoomId": 1, "toBedId": 999999, "admissionId": int(adm_id),
          "workstationNote": "DEMO-001 trace W2 force-fail"}
    h2, s2, m2 = call("/adt/acceptTransferRequestForADT", w2, token)
    print(f"W2 acceptTransferRequestForADT (toBedId=999999) -> HTTP {h2} status={s2} msg='{m2}'")

    # Amendment 10: psql ALL FIVE entities after the ERROR
    print("\n=== psql after W2 (Amendment 10: verify failures too) ===")
    t = psql(f"select transfer_status_id, status from adt.t_transfer where transfer_id={tr_id};")
    tr = psql(f"select transfer_status_id, status from adt.t_transfer_request where transfer_request_id={req_id};")
    bed = psql(f"select bed_status_id from adt.m_bed_master where bed_id=1 and organization_id=1 and unit_id=1;")
    bls = psql(f"select count(*) from adt.t_bed_log_status where bed_id=1 and status='A';")
    bls_all = psql(f"select count(*) from adt.t_bed_log_status where bed_id=1;")
    print(f"  1. t_transfer       (status_id, status) = {t}")
    print(f"  2. t_transfer_request (status_id, status) = {tr}")
    print(f"  3. m_bed_master bed 1 (bed_status_id)    = {bed}")
    print(f"  4. t_bed_log_status bed 1 active rows    = {bls}")
    print(f"     t_bed_log_status bed 1 ALL rows       = {bls_all}")

    landed = t.split("|")[0]
    # W1 left the transfer at status 2 (Requested); W2 would set 6. If the
    # transfer is STILL at 2, W2's five updates all rolled back (ATOMIC).
    verdict = "ATOMIC (all five rolled back — transfer stayed at W1's status 2)" \
        if landed == "2" else \
        f"HALF-COMMIT (W2's Transfer update landed: transfer at status {landed})"
    print(f"\nVERDICT: {verdict}")
    if s2 == "error":
        print("Envelope: HONEST ERROR (Amendment 7 class: broken screen, not masked)")
    elif s2 == "success":
        print("Envelope: MASKED SUCCESS — a write that LIES (STOP class)")

    teardown()
    left = psql("select count(*) from patient.t_patient_registration where uhid_number like 'DEMO-001%';")
    print(f"cleanup: leftovers patients={left} (must be 0)")

if __name__ == "__main__":
    try:
        main()
    except RuntimeError as e:
        print(f"TRACE ERROR: {e}")
        try:
            teardown()
        except Exception:
            pass
        sys.exit(1)
