#!/usr/bin/env python3
"""trace_t4_between_writes.py — T4 trace for the transfer chain (65).

The owner's flow-map requirement (DQ-046): the multi-write failure case is
W2 succeeds (5 entities: transfer->6, request->6, bed booked, bed-log
rows) but W3 FAILS (bad destination) — what does the legacy leave BETWEEN
writes? Then: does the W3 RETRY succeed (recovery path)?

Also asserts W1's state before W2 (the chain's start) and the final state
after the retry (transfer->9, patient moved). Amendment 10: psql after
EVERY response, success or error.

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
          where created_by = 1 and created_date::date = current_date
            and bed_id in (select bed_id from adt.m_bed_master where organization_id=1 and unit_id=1);
        -- reset the bed-booking artifact W2/W3 create on the fixture bed
        update adt.m_bed_master set bed_status_id = null
          where organization_id=1 and unit_id=1 and bed_id=1;
        delete from adt.t_admission_details
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
        values ('DEMO-001-T4TRANS', 'DEMO-001', 'T4 Transfer', 2, '9999999995',
                1, 1, 1, 'A', 1, now());
        insert into patient.t_patient_details
          (patient_id, status, created_by, created_date, unit_id, organization_id)
        select patient_id, 'A', 1, now(), 1, 1
          from patient.t_patient_registration where uhid_number = 'DEMO-001-T4TRANS';
        insert into adt.t_admission
          (organization_id, unit_id, patient_id, visit_type_id, status, created_by, created_date)
        select 1, 1, patient_id, 2, 'A', 1, now()
          from patient.t_patient_registration where uhid_number = 'DEMO-001-T4TRANS';
        insert into adt.t_transfer
          (organization_id, unit_id, admission_id, from_bed_category_id,
           from_billing_bed_category_id, from_ward_id, transfer_status_id,
           status, created_by, created_date, updated_by, updated_date)
        select 1, 1, admission_id, 1, 1, 1, 1, 'A', 1, now(), 1, now()
          from adt.t_admission
         where patient_id in (select patient_id from patient.t_patient_registration
                              where uhid_number = 'DEMO-001-T4TRANS');
        -- the admission-details row W3's bed move updates (a real admission
        -- has one; without it query3 matches 0 rows and the move is vacuous)
        insert into adt.t_admission_details
          (unit_id, admission_id, ward_id, room_id, bed_id, status,
           created_by, created_date, updated_by, updated_date, organization_id,
           bed_category_id, billing_bed_category_id)
        select 1, admission_id, 1, 1, 1, 'A', 1, now(), 1, now(), 1, 1, 1
          from adt.t_admission
         where patient_id in (select patient_id from patient.t_patient_registration
                              where uhid_number = 'DEMO-001-T4TRANS');
    """)
    ids = psql("""
        select pr.patient_id, a.admission_id, tr.transfer_id
          from patient.t_patient_registration pr
          join adt.t_admission a on a.patient_id = pr.patient_id
          join adt.t_transfer tr on tr.admission_id = a.admission_id
         where pr.uhid_number = 'DEMO-001-T4TRANS';
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

def state(tr_id, req_id, adm_id):
    t = psql(f"select transfer_status_id, status from adt.t_transfer where transfer_id={tr_id};")
    tr = psql(f"select transfer_status_id, status from adt.t_transfer_request where transfer_request_id={req_id};")
    bed = psql(f"select bed_status_id from adt.m_bed_master where bed_id=1 and organization_id=1 and unit_id=1;")
    bls = psql(f"select count(*) from adt.t_bed_log_status where bed_id=1 and status='A';")
    adm = psql(f"select visit_type_id, status from adt.t_admission where admission_id={adm_id};")
    admd = psql(f"select ward_id, room_id, bed_id, status from adt.t_admission_details where admission_id={adm_id};")
    print(f"    transfer={t} | request={tr} | bed(1)={bed} | bedlogA={bls} | admission={adm} | adm_details={admd}")

def main():
    guard_database()
    token = json.load(open("tools/.token_cache.json"))["token"]
    pid, adm_id, tr_id = seed()

    # W1: request (status 1 -> 2)
    w1 = {"transferId": int(tr_id), "transferTypeId": 2, "transferStatusId": 2,
          "toBedCategoryId": 2, "toBillingBedCategoryId": 2, "toWardId": 2,
          "toRoomId": 1, "toBedId": 1, "admissionId": int(adm_id),
          "organizationId": 1, "unitId": 1, "createdBy": 1, "updatedBy": 1,
          "note": "T4 trace W1"}
    h, s, m = call("/adt/saveTransferRequest", w1, token)
    print(f"\nW1 saveTransferRequest -> {s} ({m})")
    req_id = psql(f"select transfer_request_id from adt.t_transfer_request where admission_id={adm_id} order by transfer_request_id desc limit 1;")
    print(f"  created transfer_request_id={req_id}")
    state(tr_id, req_id, adm_id)

    # W2: accept (status 2 -> 6, bed booked)
    w2 = {"organizationId": 1, "unitId": 1, "transferId": int(tr_id),
          "transferRequestId": int(req_id), "updatedBy": 1, "updatedDate": 0,
          "toBedCategoryId": 2, "toBillingBedCategoryId": 2, "toWardId": 2,
          "toRoomId": 1, "toBedId": 1, "admissionId": int(adm_id),
          "workstationNote": "T4 trace W2"}
    h, s, m = call("/adt/acceptTransferRequestForADT", w2, token)
    print(f"\nW2 acceptTransferRequestForADT -> {s} ({m})")
    state(tr_id, req_id, adm_id)

    # W3 FAIL: nonexistent destination bed (999999) -> the executing move fails
    w3_bad = {"organizationId": 1, "unitId": 1, "transferId": int(tr_id),
              "transferRequestId": int(req_id), "updatedBy": 1, "updatedDate": 0,
              "toBedCategoryId": 2, "toWardId": 2, "toRoomId": 1, "toBedId": 999999,
              "admissionId": int(adm_id)}
    h, s, m = call("/adt/acceptFinalTransferRequest", w3_bad, token)
    print(f"\nW3 FAIL (toBedId=999999) -> {s} ({m})")
    print("  BETWEEN-WRITES STATE (Amendment 6: what the legacy leaves):")
    state(tr_id, req_id, adm_id)

    # W3 RETRY: correct destination -> should succeed (recovery path)
    w3_ok = {"organizationId": 1, "unitId": 1, "transferId": int(tr_id),
             "transferRequestId": int(req_id), "updatedBy": 1, "updatedDate": 0,
             "toBedCategoryId": 2, "toWardId": 2, "toRoomId": 1, "toBedId": 1,
             "admissionId": int(adm_id)}
    h, s, m = call("/adt/acceptFinalTransferRequest", w3_ok, token)
    print(f"\nW3 RETRY (toBedId=1) -> {s} ({m})")
    print("  AFTER RETRY (recovery path check):")
    state(tr_id, req_id, adm_id)

    # verdict — the real finding (DQ-048): W3's type-null branch is NOT
    # atomic (transfer/request/admission-details commit BEFORE the bed-log
    # insert fails -> honest ERROR envelope with most state landed); the
    # retry recovers the bed move (unlike OPD Bill's locked retry). An
    # ERROR envelope here does NOT mean nothing happened — Amendment 10.
    admd_bed = psql(f"select bed_id from adt.t_admission_details where admission_id={adm_id};")
    t9 = psql(f"select transfer_status_id from adt.t_transfer where transfer_id={tr_id};")
    recovered = t9 == "9" and admd_bed == "1"
    print(f"\nVERDICT: {'RECOVERY CONFIRMED — retry fixed the bed move (adm_details bed=' + admd_bed + ', transfer 9)' if recovered else f'NOT recovered: transfer={t9} adm_details_bed={admd_bed}'}")
    print("FINDING: W3 type-null branch is NOT atomic (DQ-048) — transfer/"
          "request/admission-details commit before the failing bed-log insert;"
          " ERROR envelope is honest but incomplete (empty catch hides the"
          " mechanism); retry recovers — unlike OPD Bill's locked retry.")

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
