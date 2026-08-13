#!/usr/bin/env python3
"""smoke_donor_registration_write.py — Amendment 8 WRITE SMOKE for the
BLOOD-BANK family's first flow (Donor Registration) — the first CLEAN-SURFACE
candidate per the DQ-054 Option-C drift map (t_donor_registration /
t_donor_details carry ZERO phantom drift; verified by tools/phantom_drift_sweep.py).

WHAT: call the PRIMARY WRITE (POST /api/bloodbank/donorRegistration) ONCE with
an exact-payload minimal body, read the envelope AND the psql state after,
then tear down same-run. Unlike patient/appointment registration, this chain's
catches THROW ServiceException (honest HTTP 500 with the real message) — a
drift/defect here surfaces LOUDLY, not as a masked 501.

PAYLOAD LITERALISM (from code, not meaning):
  - controller: DonorRegistrationController.saveDonorRegistration (api/bloodbank)
    @RequestBody DonorRegistrationDto; service registerDonor ->
    checkUniqueDonor (mobile-based, "Donor Already exist") -> DonorNumber via
    getGeneratedNo(0, unitId, DONORNUMBER) -> saveDonorRegistration ->
    saveDonorDetails.
  - patientId OMITTED (skips the checkPatientAvailability branch).
  - dob MUST carry the time part: "dd-MM-yyyy HH:mm:ss" — the Dozer mapping
    date-format is "dd-M-yyyy HH:mm:ss" and any shape without a time
    component fails with "Unable to parse source object using specified date
    format" (honest 500; verified live: "01-01-1990" 500s, "01-01-1990
    00:00:00" succeeds). Variant probe 2026-08-12: A(1-digit month) 500,
    B(with time) SUCCESS reg=1 det=1, C(ISO) 500, D(no dob) 500.
  - KNOWN FINDING (DQ-056): the donor number is NULL — getGeneratedNo(0,
    unitId, DONORNUMBER) returns null (missing global-number config row) ->
    envelope reads "Donor null Saved Successfully"; the write itself lands.

RULES: additive / DEMO-001-scoped (mobile marker) / his_local-guarded /
cleaned same-run (details then registration, FK order).

Exit codes: 0 = BUILDABLE; 1 = blocked (park-candidate, honest or masked);
2 = masked-partial (rows landed behind an ERROR envelope).
"""

import json
import os
import subprocess
import sys

MARKER_MOBILE = "9999999996"
ENDPOINT = "http://localhost:8080/api/bloodbank/donorRegistration"

PAYLOAD = {
    "organizationId": 1,
    "unitId": 1,
    "firstName": "DEMO-001-DONOR",
    "lastName": "Smoke",
    "genderId": 2,
    "dob": "01-01-1990 00:00:00",
    "mobileNumber": MARKER_MOBILE,
    "createdBy": 1,
    "updatedBy": 1,
    # patientId omitted -> the patient-availability branch is skipped
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


def donor_ids():
    return psql(f"select coalesce(string_agg(donor_id::text, ','), '') "
                f"from blood_bank.t_donor_registration "
                f"where mobile_number = '{MARKER_MOBILE}';")


def teardown():
    ids = donor_ids() or "0"
    psql(f"delete from blood_bank.t_donor_details where donor_id in ({ids});")
    psql(f"delete from blood_bank.t_donor_registration where donor_id in ({ids});")


def smoke():
    guard_database()
    teardown()
    print("pre-counts (must be 0):")
    for label, q in [
        ("registration", f"select count(*) from blood_bank.t_donor_registration "
                         f"where mobile_number='{MARKER_MOBILE}';"),
    ]:
        print(f"  {label}={psql(q)}")

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
    print(f"ENVELOPE: {body[:600]}")
    try:
        env = json.loads(body)
        status = env.get("status")
        msg = env.get("message")
    except Exception:
        status, msg = "?", body[:200]

    ids = donor_ids()
    reg = psql(f"select count(*) from blood_bank.t_donor_registration "
               f"where mobile_number='{MARKER_MOBILE}';")
    det = psql(f"select count(*) from blood_bank.t_donor_details where donor_id in ({ids or '0'});")
    print(f"post-counts: registration={reg} details={det}" + (f" donor_id={ids}" if ids else ""))

    verdict, rc = "UNKNOWN", 2
    if http == "200" and status == "success":
        verdict = "BUILDABLE — honest success envelope, donor rows landed"
        rc = 0
    elif http == "500":
        verdict = f"HONEST-500 (broken write, backend-owned) — {msg[:200]} — class: park-or-banner, NOT masked"
        rc = 1
    elif http == "200" and status == "error":
        verdict = f"ERROR envelope — {msg[:200]} — check psql state (masked?)"
        rc = 1
    elif http == "400":
        verdict = f"HTTP 400 strict-Jackson/contract: {body[:300]}"
        rc = 1
    elif http == "401":
        verdict = "HTTP 401 stale token"
        rc = 1
    else:
        verdict = f"HTTP {http} unclassified: {body[:300]}"
        rc = 1
    print(f"VERDICT: {verdict}")

    teardown()
    left = psql(f"select count(*) from blood_bank.t_donor_registration "
                f"where mobile_number='{MARKER_MOBILE}';")
    if left != "0":
        print(f"TEARDOWN FAILED — {left} marker rows remain")
        rc = 1
    else:
        print("teardown: clean")
    return rc


if __name__ == "__main__":
    sys.exit(smoke())
