#!/usr/bin/env python3
"""smoke_patient_registration_write.py — Amendment 8 WRITE SMOKE for the
ADMISSION family's first flow (Patient Registration, screen 40 / module 3,
the third full flow map after loyalty-card and transfer).

WHAT: call the PRIMARY WRITE (POST /api/global/patientRegistration) ONCE with
the exact legacy payload (normal registration, registrationTypeId=1) against
the his_local dev DB, read the response envelope AND the psql state after,
then tear down same-run. The verdict gates whether the flow map is drafted:

  - write completes + honest envelope + clean teardown  -> BUILDABLE
  - write masked/fails on ANY payload shape             -> park-candidate
    (OPD Bill lesson: five minutes of calling saveBill found the phantom
    column before the map was drafted)

PAYLOAD LITERALISM (from the code, not from meaning):
  - controller: PatientRegistrationController.savePatientRegistration
    (api/global) @RequestBody PatientRegistrationDto
  - service gate: unit-application-billing-config row must exist for org/unit
    (verified present: org 1/unit 1, patient_reg_ser_id=1 -> the service then
    AUTO-CREATES an order master+details row on every NORMAL registration)
  - dob format "dd-MM-yyyy" (DAO: GlobalCommonDateUtils.getDate(dob, dd-MM-yyyy))
  - genderId sent explicitly (a null/0 genderId makes the service CREATE a
    gender master row); state/district/city/area ids OMITTED (null skips the
    master-creation branches; only explicit 0 triggers them)
  - mobileNumber must be unique (checkUniquePatient -> ALREADY_EXIST)
  - isFromSelf omitted -> the appointment branch is skipped; kinList omitted

RULES: additive / DEMO-001-scoped / his_local-guarded / cleaned same-run
(including the auto-created order rows and any patient_registration_log rows).

Exit codes: 0 = BUILDABLE; 1 = blocked (park-candidate); 2 = masked-partial
(ERROR envelope with rows landed — Amendment 10 finding, report, still
buildable pending owner note).
"""

import json
import os
import subprocess
import sys

DEMO_FIRST = "DEMO-001-PR"
DEMO_MOBILE = "9999999990"
DEMO_ORG = 1
DEMO_UNIT = 1

ENDPOINT = "http://localhost:8080/api/global/patientRegistration"

PAYLOAD = {
    "organizationId": DEMO_ORG,
    "unitId": DEMO_UNIT,
    "registrationTypeId": 1,          # 1 = Normal registration (legacy default)
    "firstName": DEMO_FIRST,
    "lastName": "Smoke",
    "genderId": 2,                    # explicit -> no gender-master creation
    "dob": "01-01-1990",              # dd-MM-yyyy per the DAO contract
    "mobileNumber": DEMO_MOBILE,
    "patientCategoryId": 1,
    "createdBy": 1,
    "updatedBy": 1,
    # isFromSelf / kinList / address ids intentionally omitted
}


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


def patient_ids():
    return psql(
        f"select coalesce(string_agg(patient_id::text, ','), '') "
        f"from patient.t_patient_registration "
        f"where first_name = '{DEMO_FIRST}';"
    )


def teardown():
    """FK-ordered: order_details -> order_master -> details -> registration."""
    psql(f"""
        delete from public.t_order_details
         where order_id in (select order_id from public.t_order_master
                            where patient_id in ({patient_ids() or '0'}));
        delete from public.t_order_master
         where patient_id in ({patient_ids() or '0'});
        delete from patient.t_patient_details
         where patient_id in ({patient_ids() or '0'});
        delete from patient.t_patient_registration
         where patient_id in ({patient_ids() or '0'});
        delete from patient.t_patient_registration_log
         where patient_id in ({patient_ids() or '0'});
    """)


def counts(label):
    pid = patient_ids()
    orders = psql(f"select count(*) from public.t_order_master where patient_id in ({pid or '0'});")
    details = psql(f"select count(*) from patient.t_patient_details where patient_id in ({pid or '0'});")
    regs = psql(f"select count(*) from patient.t_patient_registration where patient_id in ({pid or '0'});")
    print(f"{label}: registration={regs} details={details} order_master={orders}")
    return int(regs), int(details), int(orders)


def smoke():
    guard_database()
    teardown()
    print("pre-counts (must be 0):")
    counts("pre")

    token = json.load(open("tools/.token_cache.json"))["token"]
    h = ["-H", f"Authorization: Bearer {token}",
         "-H", "userId: 1", "-H", "unitId: 1", "-H", "organizationId: 1",
         "-H", "Content-Type: application/json"]
    r = subprocess.run(["curl", "-s", "--max-time", "45", "-w", "\nHTTP:%{http_code}",
                        "-X", "POST", "-d", json.dumps(PAYLOAD)] + h +
                       [ENDPOINT], capture_output=True, text=True, timeout=60)
    out = r.stdout.strip()
    http = out.rsplit("HTTP:", 1)[-1].strip()
    body = out.rsplit("\nHTTP:", 1)[0].strip()
    print(f"HTTP {http}")
    print(f"ENVELOPE: {body[:500]}")
    try:
        env = json.loads(body)
        status = env.get("status")
        code = env.get("code")
        msg = env.get("message")
    except Exception:
        status, code, msg = "?", "?", body[:200]

    print("post-counts (what landed):")
    counts("post")

    pid = patient_ids()
    if pid:
        uhid = psql(f"select uhid_number from patient.t_patient_registration where patient_id = {pid.split(',')[0]};")
        print(f"created patient_id={pid} uhid={uhid}")

    verdict = "UNKNOWN"
    rc = 2
    if http == "200" and status == "success":
        verdict = "BUILDABLE — honest success envelope, rows landed"
        rc = 0
    elif http == "200" and status == "error" and not pid:
        verdict = f"BLOCKED (park-candidate) — error envelope, nothing landed: {msg}"
        rc = 1
    elif http == "200" and status == "error" and pid:
        verdict = f"MASKED-PARTIAL (Amendment 10) — ERROR envelope with rows landed: {msg}"
        rc = 2
    elif http == "400":
        verdict = f"HTTP 400 — strict-Jackson/contract issue: {body[:300]}"
        rc = 1
    elif http == "401":
        verdict = "HTTP 401 — stale token; re-mint and re-run"
        rc = 1
    else:
        verdict = f"HTTP {http} — unclassified: {body[:300]}"
        rc = 1

    print(f"VERDICT: {verdict}")

    teardown()
    counts("teardown")
    leftover = patient_ids()
    if leftover:
        print(f"TEARDOWN FAILED — DEMO rows still present: {leftover}")
        rc = 1
    else:
        print("teardown: clean")
    return rc


if __name__ == "__main__":
    sys.exit(smoke())
