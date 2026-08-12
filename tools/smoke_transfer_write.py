#!/usr/bin/env python3
"""smoke_transfer_write.py — Amendment 8 WRITE SMOKE for the ADMISSION chain
(direct transfer, module 65) — the new T0 gate before any flow map.

WHAT: seed a minimal DEMO-001 transfer chain (patient + admission +
transfer row status 1/Pending), call the PRIMARY WRITE
(POST /adt/saveTransferRequest), read the response envelope AND the psql
state after. The smoke's verdict gates the flow:
  - write completes + honest envelope  -> flow is buildable, draft the map
  - write masked/fails on ANY payload  -> park-candidate (OPD Bill lesson:
    five minutes of calling saveBill would have found the phantom column
    before the map was drafted)

RULES: additive/idempotent/DEMO-001-scoped/his_local-guarded/cleaned
same-run (incl. the transfer-request rows the write creates).
"""

import json
import os
import subprocess
import sys

DEMO_ORG = 1
DEMO_UNIT = 1

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
        values ('DEMO-001-TRANSFER', 'DEMO-001', 'Transfer', 2, '9999999997',
                1, 1, 1, 'A', 1, now());
        insert into patient.t_patient_details
          (patient_id, status, created_by, created_date, unit_id, organization_id)
        select patient_id, 'A', 1, now(), 1, 1
          from patient.t_patient_registration where uhid_number = 'DEMO-001-TRANSFER';
        insert into adt.t_admission
          (organization_id, unit_id, patient_id, visit_type_id, status, created_by, created_date)
        select 1, 1, patient_id, 2, 'A', 1, now()
          from patient.t_patient_registration where uhid_number = 'DEMO-001-TRANSFER';
        insert into adt.t_transfer
          (organization_id, unit_id, admission_id, from_bed_category_id,
           from_billing_bed_category_id, from_ward_id, transfer_status_id,
           status, created_by, created_date, updated_by, updated_date)
        select 1, 1, admission_id, 1, 1, 1, 1, 'A', 1, now(), 1, now()
          from adt.t_admission
         where patient_id in (select patient_id from patient.t_patient_registration
                              where uhid_number = 'DEMO-001-TRANSFER');
    """)
    ids = psql("""
        select pr.patient_id, a.admission_id, tr.transfer_id
          from patient.t_patient_registration pr
          join adt.t_admission a on a.patient_id = pr.patient_id
          join adt.t_transfer tr on tr.admission_id = a.admission_id
         where pr.uhid_number = 'DEMO-001-TRANSFER';
    """)
    print(f"fixture ids (patient, admission, transfer): {ids}")
    return ids.split("|")

def smoke():
    """Amendment 8: call the PRIMARY WRITE once, read envelope + psql."""
    pid, adm_id, tr_id = seed()
    token = json.load(open("tools/.token_cache.json"))["token"]
    base = "http://localhost:8080"
    h = ["-H", f"Authorization: Bearer {token}", "-H", "userId: 1", "-H", "unitId: 1",
         "-H", "organizationId: 1", "-H", "Content-Type: application/json"]
    # minimal payload: transfer-type 2 (bed transfer, the else branch), the
    # request carries the destination (to-bed/ward/category) + status 2
    payload = {
        "transferId": int(tr_id),
        "transferTypeId": 2,
        "transferStatusId": 2,
        "toBedCategoryId": 2,
        "toBillingBedCategoryId": 2,
        "toWardId": 2,
        "toRoomId": 1,
        "toBedId": 1,
        "admissionId": int(adm_id),
        "organizationId": 1,
        "unitId": 1,
        "createdBy": 1,
        "updatedBy": 1,
        "note": "DEMO-001 smoke",
    }
    r = subprocess.run(["curl", "-s", "--max-time", "30", "-w", "\nHTTP:%{http_code}",
                        "-X", "POST", "-d", json.dumps(payload)] + h +
                       [f"{base}/adt/saveTransferRequest"],
                       capture_output=True, text=True, timeout=40)
    raw = r.stdout
    http = raw.split("HTTP:")[-1] if "HTTP:" in raw else "?"
    body = raw.split("HTTP:")[0] if "HTTP:" in raw else raw
    try:
        d = json.loads(body)
        print(f"SMOKE POST /adt/saveTransferRequest HTTP {http} "
              f"status={d.get('status')} msg={(d.get('message') or '')[:70]}")
    except Exception:
        print(f"SMOKE HTTP {http} non-JSON: {body[:150]}")

    # psql state after: transfer-request row created? transfer updated?
    tq = psql(f"select count(*) from adt.t_transfer_request where admission_id={adm_id};")
    tu = psql(f"select transfer_status_id, to_bed_category_id, to_ward_id from adt.t_transfer where transfer_id={tr_id};")
    print(f"AFTER: transfer_request rows={tq} | transfer row (status,to_bed_cat,to_ward)={tu}")
    verdict = tq == "1"
    print(f"VERDICT: {'BUILDABLE — write completed' if verdict else 'PARK-CANDIDATE — write did not land'}")
    return verdict

if __name__ == "__main__":
    guard_database()
    try:
        ok = smoke()
        teardown()
        print("cleanup: DEMO-001 rows removed (transfer requests, transfers, admission, patient)")
        sys.exit(0 if ok else 1)
    except RuntimeError as e:
        print(f"SMOKE ERROR: {e}")
        try:
            teardown()
        except Exception:
            pass
        sys.exit(1)
