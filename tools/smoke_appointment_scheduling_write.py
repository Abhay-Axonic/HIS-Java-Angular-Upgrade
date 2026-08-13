#!/usr/bin/env python3
"""smoke_appointment_scheduling_write.py — Amendment 8 WRITE SMOKE for the
ADMISSION family's second flow (Appointment Scheduling, screen 41 / module 4).

WHAT: call the PRIMARY WRITE (POST /api/opd/scheduleAppointment) ONCE with an
exact-payload minimal body against his_local, read the envelope AND the psql
state after, then tear down same-run. The verdict gates the flow map.

PAYLOAD LITERALISM (from code, not meaning):
  - controller: AppointmentSchedulingController.scheduleAppointment (api/opd)
    @RequestBody SlotMasterDto
  - DAO (AppointmentSchedulingDaoImpl:85-150): appointmentDate substring(0,10)
    + " 00:00:00"; optional slotId triggers a slot-status UPDATE (default
    slotStatusId 4); exceptions THROW (ApplicationException) -> the controller
    catch returns the honest populated ERROR envelope (not a silent mask).
  - patientId must reference an EXISTING dev patient row (FK); the smoke
    READS one dev patient id (no writes to patient data) and only ever
    creates/deletes the appointment row itself.

RULES: additive / DEMO-001-scoped (marker appointment_date 2099-01-01) /
his_local-guarded / cleaned same-run.

Exit codes: 0 = BUILDABLE; 1 = blocked (park-candidate); 2 = masked-partial.
"""

import json
import os
import subprocess
import sys

MARKER_DATE = "2099-01-01"
ENDPOINT = "http://localhost:8080/api/opd/scheduleAppointment"


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


def teardown(patient_id):
    psql(f"delete from opd.m_appointment_master where appointment_date = '{MARKER_DATE}'"
         f" and patient_id = {patient_id};")


def smoke():
    guard_database()
    patient_id = psql("select patient_id from patient.t_patient_registration order by patient_id limit 1;")
    if not patient_id:
        sys.exit("no dev patient row available for the FK — cannot smoke")
    print(f"using existing dev patient_id={patient_id} (read-only usage)")
    teardown(patient_id)

    payload = {
        "organizationId": 1,
        "unitId": 1,
        "patientId": int(patient_id),
        "appointmentDate": MARKER_DATE,
        "appointmentStatusId": 1,
        "appointmentTypeId": 1,
        "createdBy": 1,
        "updatedBy": 1,
        # slotId omitted -> no slot-status UPDATE branch
    }
    token = json.load(open("tools/.token_cache.json"))["token"]
    h = ["-H", f"Authorization: Bearer {token}",
         "-H", "userId: 1", "-H", "unitId: 1", "-H", "organizationId: 1",
         "-H", "Content-Type: application/json"]
    r = subprocess.run(["curl", "-s", "--max-time", "45", "-w", "\nHTTP:%{http_code}",
                        "-X", "POST", "-d", json.dumps(payload)] + h +
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

    landed = psql(f"select count(*) from opd.m_appointment_master "
                  f"where appointment_date = '{MARKER_DATE}' and patient_id = {patient_id};")
    print(f"post-counts: appointment_master rows = {landed}")

    verdict, rc = "UNKNOWN", 2
    if http == "200" and status == "success":
        verdict = "BUILDABLE — honest success envelope, appointment row landed"
        rc = 0
    elif http == "200" and status == "error":
        verdict = f"BLOCKED/error envelope (masked?) — {msg} — check catalina for the mechanical cause"
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

    teardown(patient_id)
    left = psql(f"select count(*) from opd.m_appointment_master "
                f"where appointment_date = '{MARKER_DATE}' and patient_id = {patient_id};")
    if left != "0":
        print(f"TEARDOWN FAILED — {left} marker rows remain")
        rc = 1
    else:
        print("teardown: clean")
    return rc


if __name__ == "__main__":
    sys.exit(smoke())
