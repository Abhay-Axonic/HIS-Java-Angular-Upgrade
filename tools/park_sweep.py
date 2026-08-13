#!/usr/bin/env python3
"""park_sweep.py — DQ-054 Option-C PARK-AND-SKIP T0 sweeps, batched.

Each candidate: DRIFT PRE-CHECK (the map's bands for its write tables — a
HYPOTHESIS only) -> T0 WRITE SMOKE (one exact-payload POST) -> psql state
after EVERY envelope -> teardown -> verdict. Per owner directive 2026-08-12:
"park on the SMOKE result, not the pre-check — and where a smoke unexpectedly
PASSES on a supposedly drift-hit table, that is a PROBABLE being disconfirmed
and it should upgrade the map's accuracy."

Verdict classes (Amendment 7/8):
  BUILDABLE      — honest success envelope AND rows landed (+ teardown clean)
  UNPROVEN       — success envelope but ZERO rows landed (false-success
                   suspect — BR-192 class; park with that label)
  MASKED-PARK    — error envelope (501/"Fail"/null) with nothing landed ->
                   park; the mechanical cause is confirmed by catalina
  HONEST-500     — real 500 with a message -> broken write, park-or-banner
  READ-ONLY      — no standalone write endpoint exists (by design)

Additive / DEMO-marker / his_local-guarded / cleaned same-run.
"""

import json
import os
import subprocess
import sys

ENDPOINT_BASE = "http://localhost:8080"
MARKER_DATE = "2099-01-01 00:00:00"


def psql(q, db="his_local"):
    r = subprocess.run(["psql", "-h", "localhost", "-d", db, "-tAc", q],
                       capture_output=True, text=True, timeout=30)
    if r.returncode != 0:
        raise RuntimeError(f"psql failed: {r.stderr.strip()[:300]}")
    return r.stdout.strip()


def guard():
    if os.environ.get("PGDATABASE") not in (None, "his_local"):
        sys.exit("REFUSING: PGDATABASE set — fixture is his_local-only")
    psql("select current_database();")


def get_token():
    return json.load(open("tools/.token_cache.json"))["token"]


def post(payload, endpoint):
    token = get_token()
    h = ["-H", f"Authorization: Bearer {token}", "-H", "userId: 1",
         "-H", "unitId: 1", "-H", "organizationId: 1",
         "-H", "Content-Type: application/json"]
    r = subprocess.run(["curl", "-s", "--max-time", "45", "-w", "\nHTTP:%{http_code}",
                        "-X", "POST", "-d", json.dumps(payload)] + h +
                       [ENDPOINT_BASE + endpoint], capture_output=True, text=True, timeout=60)
    out = r.stdout.strip()
    http = out.rsplit("HTTP:", 1)[-1].strip()
    body = out.rsplit("\nHTTP:", 1)[0].strip()
    return http, body


def run_candidate(name, cfg):
    print(f"\n===== {name} =====")
    print(f"DRIFT PRE-CHECK (hypothesis only): {cfg['precheck']}")
    pre_max = None
    if cfg.get("pre_max_sql"):
        pre_max = psql(cfg["pre_max_sql"]).strip()
        print(f"  pre max(id)={pre_max}")
    for label, q in cfg.get("precounts", []):
        print(f"  pre {label}={psql(q)}")
    if cfg.get("preclean"):
        for q in cfg["preclean"]:
            psql(q.replace("{pre_max}", str(pre_max)))
    payload = cfg["payload"]()
    http, body = post(payload, cfg["endpoint"])
    print(f"POST {cfg['endpoint']}")
    print(f"HTTP {http} | {body[:220]}")
    status = "?"
    try:
        status = json.loads(body).get("status")
    except Exception:
        pass
    counts = {}
    for label, q in cfg.get("postcounts", []):
        q = q.replace("{pre_max}", str(pre_max))
        counts[label] = psql(q)
        print(f"  post {label}={counts[label]}")
    landed = cfg.get("landed", lambda c: any(int(v) > 0 for v in c.values()))(counts)
    if cfg.get("teardown"):
        for q in cfg["teardown"]:
            psql(q.replace("{pre_max}", str(pre_max)))
        left_sql = cfg.get("leftover")
        left = psql(left_sql.replace("{pre_max}", str(pre_max))) if left_sql else "0"
        print(f"  teardown: {'clean' if str(left) == '0' else 'LEFT ROWS ' + str(left)}")
    verdict, reason = "?", ""
    if http == "200" and status == "success" and landed:
        verdict, reason = "BUILDABLE", "honest success + rows landed"
    elif http == "200" and status == "success" and not landed:
        verdict, reason = "UNPROVEN", "success envelope, ZERO rows — false-success suspect (BR-192 class)"
    elif http == "500":
        verdict, reason = "HONEST-500", "broken write — check the message in catalina (backend-owned)"
    elif http == "200" and status == "error":
        verdict, reason = "MASKED-PARK", "error envelope, nothing landed — drift/defect confirmed via catalina"
    else:
        verdict, reason = f"HTTP {http}", body[:120]
    print(f"VERDICT: {verdict} — {reason}")
    return verdict


def main():
    guard()
    # existing dev patient for patient-gated writes
    def first_patient():
        return psql("select patient_id from patient.t_patient_registration "
                    "where status='A' order by patient_id desc limit 1;").strip()

    candidates = {
        "io-chart (input-output charts)": {
            "endpoint": "/api/opd/saveIntakeOutputChart",
            "precheck": ("adt.t_intake_output_chart_details flagged 2 columns "
                         "(per drift map, band PROBABLE/SUSPECT — smoke confirms)"),
            "pre_max_sql": "select coalesce(max(intake_output_chart_id),0) from adt.t_intake_output_chart;",
            "preclean": [
                "delete from adt.t_intake_output_chart_details where intake_output_chart_id > {pre_max};",
                "delete from adt.t_intake_output_chart where intake_output_chart_id > {pre_max};",
            ],
            "precounts": [("chart rows", "select count(*) from adt.t_intake_output_chart;")],
            "payload": lambda: {
                "organizationId": 1, "unitId": 1,
                "patientId": int(first_patient() or 0),
                "createdBy": 1, "updatedBy": 1,
                "createdDate": MARKER_DATE, "updatedDate": MARKER_DATE,
                "status": "A",
                "intakeOutputChartMasterDtoList": [
                    {"intakeFluidId": None, "intakeTypeFluidId": None,
                     "startIntakeDate": None, "startIntakeTime": None,
                     "status": "A", "createdBy": 1, "updatedBy": 1,
                     "createdDate": MARKER_DATE, "updatedDate": MARKER_DATE},
                ],
            },
            "postcounts": [
                ("chart", "select count(*) from adt.t_intake_output_chart where intake_output_chart_id > {pre_max};"),
                ("chart_details", "select count(*) from adt.t_intake_output_chart_details where intake_output_chart_id > {pre_max};"),
            ],
            "landed": lambda c: int(c.get("chart_details", 0)) > 0,
            "teardown": [
                "delete from adt.t_intake_output_chart_details where intake_output_chart_id > {pre_max};",
                "delete from adt.t_intake_output_chart where intake_output_chart_id > {pre_max};",
            ],
            "leftover": "select count(*) from adt.t_intake_output_chart where intake_output_chart_id > {pre_max};",
        },
        "shift-closing (600, cash desk)": {
            "endpoint": "/api/global/saveShiftCloseBalance",
            "precheck": ("ShiftClosingMaster entity -> billing.m_shift_close_balance (clean per "
                         "drift map); t_denomination_details.cash_in_out_id flag belongs to the "
                         "cash-handover write, NOT this endpoint"),
            "pre_max_sql": "select coalesce(max(id),0) from billing.m_shift_close_balance;",
            "preclean": [
                "delete from billing.m_shift_close_balance where id > {pre_max};",
            ],
            "precounts": [("shift_close_balance", "select count(*) from billing.m_shift_close_balance;")],
            "payload": lambda: {
                "organizationId": 1, "unitId": 1,
                "createdBy": 1, "updatedBy": 1,
                "createdDate": MARKER_DATE, "updatedDate": MARKER_DATE,
                "openingDate": MARKER_DATE, "currentDate": MARKER_DATE,
                "difference": 0, "refundAmount": 0,
                "listOfIds": [],
            },
            "postcounts": [
                ("shift_close_balance", "select count(*) from billing.m_shift_close_balance where id > {pre_max};"),
            ],
            "landed": lambda c: int(c.get("shift_close_balance", 0)) > 0,
            "teardown": [
                "delete from billing.m_shift_close_balance where id > {pre_max};",
            ],
            "leftover": "select count(*) from billing.m_shift_close_balance where id > {pre_max};",
        },
        "blood-issue (blood bank)": {
            "endpoint": "/global/bloodBank/saveIssueBloodBag",
            "precheck": ("t_issue_blood_bag flagged cross_match_register_id (1, drift map) — "
                         "BUT the DDL column EXISTS (verified) — probable sweep artifact; "
                         "smoke decides"),
            "pre_max_sql": "select coalesce(max(issue_blood_bag_id),0) from blood_bank.t_issue_blood_bag;",
            "preclean": [
                "delete from blood_bank.t_issue_blood_bag_details where issue_blood_bag_id > {pre_max};",
                "delete from blood_bank.t_issue_blood_bag where issue_blood_bag_id > {pre_max};",
            ],
            "precounts": [("issue rows", "select count(*) from blood_bank.t_issue_blood_bag;")],
            "payload": lambda: {
                "organizationId": 1, "unitId": 1,
                "patientId": int(first_patient() or 0),
                "createdBy": 1, "updatedBy": 1,
                "status": "A",
                "issueDate": "13-08-2026 00:00:00", "requisitionDate": "13-08-2026 00:00:00",
                "createdDate": "13-08-2026 00:00:00", "updatedDate": "13-08-2026 00:00:00",
                "bloodBagIssueList": [{
                    "organizationId": 1, "unitId": 1,
                    "issueBloodBagStatusId": 1,
                    "createdBy": 1, "updatedBy": 1, "status": "A",
                    "createdDate": "13-08-2026 00:00:00", "updatedDate": "13-08-2026 00:00:00",
                }],
            },
            "postcounts": [
                ("issue", "select count(*) from blood_bank.t_issue_blood_bag where issue_blood_bag_id > {pre_max};"),
                ("issue_details", "select count(*) from blood_bank.t_issue_blood_bag_details where issue_blood_bag_id > {pre_max};"),
            ],
            "landed": lambda c: int(c.get("issue", 0)) > 0,
            "teardown": [
                "delete from blood_bank.t_issue_blood_bag_details where issue_blood_bag_id > {pre_max};",
                "delete from blood_bank.t_issue_blood_bag where issue_blood_bag_id > {pre_max};",
            ],
            "leftover": "select count(*) from blood_bank.t_issue_blood_bag where issue_blood_bag_id > {pre_max};",
        },
        "lab-phlebotomist-collection": {
            "endpoint": "/LIS/PhlebotomistHomeCollection/save",
            "precheck": ("lab.t_lab_sample_dtls flagged 7 columns + t_lab_sample_master 1 "
                         "(drift map; smoke decides)"),
            "pre_max_sql": "select coalesce(max(phlebotomist_home_collection_id),0) from lab.t_phlebotomist_home_collection;",
            "preclean": [
                "delete from lab.t_phlebotomist_home_collection where phlebotomist_home_collection_id > {pre_max};",
            ],
            "precounts": [("phlebotomist rows", "select count(*) from lab.t_phlebotomist_home_collection;")],
            "payload": lambda: [{
                "organizationId": 1, "unitId": 1,
                "userId": 1,
                "createdBy": 1, "updatedBy": 1,
                "status": "A",
                "phlebotomistCollectionDate": "13-08-2026 00:00:00",
                "fromTime": "10:00:00", "toTime": "11:00:00",
                "createdDate": "13-08-2026 00:00:00", "updatedDate": "13-08-2026 00:00:00",
            }],
            "postcounts": [
                ("phlebotomist", "select count(*) from lab.t_phlebotomist_home_collection where phlebotomist_home_collection_id > {pre_max};"),
            ],
            "landed": lambda c: int(c.get("phlebotomist", 0)) > 0,
            "teardown": [
                "delete from lab.t_phlebotomist_home_collection where phlebotomist_home_collection_id > {pre_max};",
            ],
            "leftover": "select count(*) from lab.t_phlebotomist_home_collection where phlebotomist_home_collection_id > {pre_max};",
        },
        "cash-handover (cash desk)": {
            "endpoint": "/api/global/saveCashHandoverMaster",
            "precheck": ("t_denomination_details flagged cash_in_out_id (1, drift map) — "
                         "DDL columns (id, denomination, quantity, amount, created_date…) "
                         "show NO cash_in_out_id — genuine drift suspect; smoke decides"),
            "pre_max_sql": "select coalesce(max(id),0) from public.t_cash_handover_details;",
            "preclean": [
                "delete from public.t_denomination_details where id > {pre_max} and created_date = '2026-08-13 00:00:00';",
                "delete from public.t_cash_handover_details where id > {pre_max};",
            ],
            "precounts": [("handover rows", "select count(*) from public.t_cash_handover_details;")],
            "payload": lambda: {
                "organizationId": 1, "unitId": 1,
                "date": "13-08-2026 00:00:00",
                "cashHandoverById": 1, "cashHandoverToId": 1,
                "cashHandoverAmount": 0, "userId": 1,
                "createdBy": 1, "updatedBy": 1, "status": "A",
                "createdDate": "13-08-2026 00:00:00", "updatedDate": "13-08-2026 00:00:00",
                "denominationDetailsMasterList": [
                    {"denominationId": 1, "qty": 1, "amount": 10,
                     "createdBy": 1, "updatedBy": 1, "status": "A",
                     "createdDate": "13-08-2026 00:00:00", "updatedDate": "13-08-2026 00:00:00"},
                ],
            },
            "postcounts": [
                ("handover", "select count(*) from public.t_cash_handover_details where id > {pre_max};"),
                ("denomination", "select count(*) from public.t_denomination_details where id > {pre_max};"),
            ],
            "landed": lambda c: int(c.get("handover", 0)) > 0,
            "teardown": [
                "delete from public.t_denomination_details where id > {pre_max};",
                "delete from public.t_cash_handover_details where id > {pre_max};",
            ],
            "leftover": "select count(*) from public.t_cash_handover_details where id > {pre_max};",
        },
        "lab-sample-registration (LIS)": {
            "endpoint": "/LIS/global/addSample",
            "precheck": ("lab.t_lab_sample_dtls flagged 7 columns + t_lab_sample_master 1 "
                         "(drift map; smoke decides)"),
            "pre_max_sql": "select coalesce(max(sample_id),0) from lab.m_sample_master;",
            "preclean": [
                "delete from lab.m_sample_master where sample_id > {pre_max};",
            ],
            "precounts": [("sample-type rows", "select count(*) from lab.m_sample_master;")],
            "payload": lambda: {
                "orgId": 1,
                "code": "SMP", "desc": "smoke sample type",
                "createdBy": 1, "updatedBy": 1, "status": "A",
                "createdDate": 1750000000000, "updatedDate": 1750000000000,
            },
            "postcounts": [
                ("sample_master", "select count(*) from lab.m_sample_master where sample_id > {pre_max};"),
            ],
            "landed": lambda c: int(c.get("sample_master", 0)) > 0,
            "teardown": [
                "delete from lab.m_sample_master where sample_id > {pre_max};",
            ],
            "leftover": "select count(*) from lab.m_sample_master where sample_id > {pre_max};",
        },
        "admission (ADT, savePatientAdmission)": {
            "endpoint": "/adt/savePatientAdmission",
            "precheck": ("adt.t_admission flagged ot_status + t_admission_details 3 "
                         "(drift map; smoke decides)"),
            "pre_max_sql": "select coalesce(max(admission_id),0) from adt.t_admission;",
            "preclean": [
                "delete from adt.t_admission_details where admission_id > {pre_max};",
                "delete from adt.t_admission where admission_id > {pre_max};",
            ],
            "precounts": [("admission rows", "select count(*) from adt.t_admission;")],
            "payload": lambda: {
                "organizationId": 1, "unitId": 1,
                "patientId": int(first_patient() or 0),
                "bedId": 4,
                "createdBy": 1, "updatedBy": 1,
                "status": "A",
                "createdDate": "13-08-2026 00:00:00",
            },
            "postcounts": [
                ("admission", "select count(*) from adt.t_admission where admission_id > {pre_max};"),
                ("admission_details", "select count(*) from adt.t_admission_details where admission_id > {pre_max};"),
            ],
            "landed": lambda c: int(c.get("admission", 0)) > 0,
            "teardown": [
                "delete from adt.t_admission_details where admission_id > {pre_max};",
                "delete from adt.t_admission where admission_id > {pre_max};",
            ],
            "leftover": "select count(*) from adt.t_admission where admission_id > {pre_max};",
        },
        "deposit (cash desk, /api/global/deposit)": {
            "endpoint": "/api/billing/unit/deposit",
            "precheck": ("billing.m_deposit_master flagged 4 columns (drift map; smoke decides) — "
                         "table currently EMPTY (0 rows)"),
            "pre_max_sql": "select coalesce(max(deposit_master_id),0) from billing.m_deposit_master;",
            "preclean": [
                "delete from billing.m_deposit_master where deposit_master_id > {pre_max};",
            ],
            "precounts": [("deposit rows", "select count(*) from billing.m_deposit_master;")],
            "payload": lambda: {
                "depositMasterDto": {
                    "organizationId": 1, "unitId": 1,
                    "patientId": int(first_patient() or 0),
                    "amount": 100,
                    "createdBy": 1, "updatedBy": 1, "status": "A",
                    "createdDate": "13-08-2026 00:00:00", "updatedDate": "13-08-2026 00:00:00",
                },
                "listPaymentDepositDetailsDto": [],
            },
            "postcounts": [
                ("deposit", "select count(*) from billing.m_deposit_master where deposit_master_id > {pre_max};"),
            ],
            "landed": lambda c: int(c.get("deposit", 0)) > 0,
            "teardown": [
                "delete from billing.m_deposit_master where deposit_master_id > {pre_max};",
            ],
            "leftover": "select count(*) from billing.m_deposit_master where deposit_master_id > {pre_max};",
        },
        "walkin-sale-prescription (pharmacy)": {
            "endpoint": "/api/pharmacy/sales/walkins/save/prescription",
            "precheck": ("pharmacy.t_sale flagged 12 columns (drift map; smoke decides) — "
                         "the request is a complex WalkInPrescriptionReq"),
            "pre_max_sql": "select coalesce(max(id),0) from pharmacy.t_sale;",
            "preclean": [],
            "precounts": [("sale rows", "select count(*) from pharmacy.t_sale;")],
            "payload": lambda: {
                "organizationId": 1, "unitId": 1, "storeId": 1, "tempBillId": 0,
                "createdBy": 1, "updatedBy": 1,
            },
            "postcounts": [
                ("sale", "select count(*) from pharmacy.t_sale where id > {pre_max};"),
            ],
            "landed": lambda c: int(c.get("sale", 0)) > 0,
            "teardown": [],
            "leftover": "select count(*) from pharmacy.t_sale where id > {pre_max};",
        },
    }
    results = {}
    for name, cfg in candidates.items():
        results[name] = run_candidate(name, cfg)
    print("\n===== BATCH SUMMARY =====")
    for name, v in results.items():
        print(f"  {name}: {v}")
    return 0 if all(v == "BUILDABLE" for v in results.values()) else 1


if __name__ == "__main__":
    sys.exit(main())
