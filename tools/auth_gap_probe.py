#!/usr/bin/env python3
"""auth_gap_probe.py — BR-040 (b): MEASURE the unauthenticated-write surface.

Posts a dozen write endpoints across families with NO Authorization header
and reports which (i) LAND rows (FULL gap), (ii) reach the app and execute
the write path (REACHED — a non-401 response), or (iii) are auth-gated (401).
The 7 park_sweep candidates use their EXACT verified payloads + id-based
markers (park_sweep already runs them auth-less); 5 new cross-family writes
are added. Each probe: pre-max capture -> POST (no auth) -> postcounts ->
same-run teardown. Ad-hoc measurement tool for the BR-040 brief; not a
project test suite."""
import json, subprocess, sys, urllib.request, urllib.error

BASE = "http://localhost:8080"
MARKER = "13-08-2026 00:00:00"

def psql(q):
    r = subprocess.run(["psql", "-h", "localhost", "-d", "his_local", "-tAc", q],
                       capture_output=True, text=True, timeout=30)
    if r.returncode != 0:
        raise RuntimeError(f"psql: {r.stderr.strip()[:200]}")
    return r.stdout.strip()

def first_patient():
    return psql("select patient_id from patient.t_patient_registration order by patient_id limit 1;")

def post_noauth(path, payload):
    req = urllib.request.Request(
        BASE + path, data=json.dumps(payload).encode(),
        headers={"Content-Type": "application/json"}, method="POST")
    try:
        with urllib.request.urlopen(req, timeout=30) as r:
            return r.status, r.read(200).decode(errors="replace")
    except urllib.error.HTTPError as e:
        return e.code, e.read(200).decode(errors="replace")
    except Exception as e:  # noqa: BLE001
        return 0, f"ERR {e}"

# (name, endpoint, payload, pre_max_sql, postcounts[(label, sql)], teardown[sql])
CFG = [
    ("io-chart", "/api/opd/saveIntakeOutputChart",
     lambda: {"organizationId": 1, "unitId": 1, "patientId": int(first_patient() or 0),
              "createdBy": 1, "updatedBy": 1, "status": "A",
              "createdDate": MARKER, "updatedDate": MARKER,
              "intakeOutputChartMasterDtoList": [{"intakeFluidId": None,
                  "intakeTypeFluidId": None, "startIntakeDate": None,
                  "startIntakeTime": None, "status": "A", "createdBy": 1,
                  "updatedBy": 1, "createdDate": MARKER, "updatedDate": MARKER}]},
     "select coalesce(max(intake_output_chart_id),0) from adt.t_intake_output_chart;",
     [("chart", "select count(*) from adt.t_intake_output_chart where intake_output_chart_id > {pre_max};")],
     ["delete from adt.t_intake_output_chart_details where intake_output_chart_id > {pre_max};",
      "delete from adt.t_intake_output_chart where intake_output_chart_id > {pre_max};"]),
    ("shift-closing", "/api/global/saveShiftCloseBalance",
     lambda: {"organizationId": 1, "unitId": 1, "createdBy": 1, "updatedBy": 1,
              "createdDate": MARKER, "updatedDate": MARKER, "status": "A", "listOfIds": []},
     "select coalesce(max(id),0) from billing.m_shift_close_balance;",
     [("balance", "select count(*) from billing.m_shift_close_balance where id > {pre_max};")],
     ["delete from billing.m_shift_close_balance where id > {pre_max};"]),
    ("blood-issue", "/global/bloodBank/saveIssueBloodBag",
     lambda: {"organizationId": 1, "unitId": 1, "patientId": int(first_patient() or 0),
              "createdBy": 1, "updatedBy": 1, "status": "A",
              "issueDate": MARKER, "requisitionDate": MARKER,
              "createdDate": MARKER, "updatedDate": MARKER,
              "bloodBagIssueList": [{"organizationId": 1, "unitId": 1,
                  "issueBloodBagStatusId": 1, "createdBy": 1, "updatedBy": 1,
                  "status": "A", "createdDate": MARKER, "updatedDate": MARKER}]},
     "select coalesce(max(issue_blood_bag_id),0) from blood_bank.t_issue_blood_bag;",
     [("issue", "select count(*) from blood_bank.t_issue_blood_bag where issue_blood_bag_id > {pre_max};")],
     ["delete from blood_bank.t_issue_blood_bag_details where issue_blood_bag_id > {pre_max};",
      "delete from blood_bank.t_issue_blood_bag where issue_blood_bag_id > {pre_max};"]),
    ("phlebotomist", "/LIS/PhlebotomistHomeCollection/save",
     lambda: [{"organizationId": 1, "unitId": 1, "userId": 1, "createdBy": 1,
               "updatedBy": 1, "status": "A", "phlebotomistCollectionDate": MARKER,
               "fromTime": "10:00:00", "toTime": "11:00:00",
               "createdDate": MARKER, "updatedDate": MARKER}],
     "select coalesce(max(phlebotomist_home_collection_id),0) from lab.t_phlebotomist_home_collection;",
     [("phleb", "select count(*) from lab.t_phlebotomist_home_collection where phlebotomist_home_collection_id > {pre_max};")],
     ["delete from lab.t_phlebotomist_home_collection where phlebotomist_home_collection_id > {pre_max};"]),
    ("cash-handover", "/api/global/saveCashHandoverMaster",
     lambda: {"organizationId": 1, "unitId": 1, "date": MARKER,
              "cashHandoverById": 1, "cashHandoverToId": 1, "cashHandoverAmount": 0,
              "userId": 1, "createdBy": 1, "updatedBy": 1, "status": "A",
              "createdDate": MARKER, "updatedDate": MARKER,
              "denominationDetailsMasterList": [{"denominationId": 1, "qty": 1,
                  "amount": 10, "createdBy": 1, "updatedBy": 1, "status": "A",
                  "createdDate": MARKER, "updatedDate": MARKER}]},
     "select coalesce(max(id),0) from public.t_cash_handover_details;",
     [("handover", "select count(*) from public.t_cash_handover_details where id > {pre_max};")],
     ["delete from public.t_denomination_details where id > {pre_max};",
      "delete from public.t_cash_handover_details where id > {pre_max};"]),
    ("lab-addSample", "/LIS/global/addSample",
     lambda: {"orgId": 1, "code": "SMP", "desc": "smoke sample type",
              "createdBy": 1, "updatedBy": 1, "status": "A",
              "createdDate": 1750000000000, "updatedDate": 1750000000000},
     "select coalesce(max(sample_id),0) from lab.m_sample_master;",
     [("sample", "select count(*) from lab.m_sample_master where sample_id > {pre_max};")],
     ["delete from lab.m_sample_master where sample_id > {pre_max};"]),
    ("admission", "/adt/savePatientAdmission",
     lambda: {"organizationId": 1, "unitId": 1, "patientId": int(first_patient() or 0),
              "bedId": 4, "createdBy": 1, "updatedBy": 1, "status": "A"},
     "select coalesce(max(admission_id),0) from adt.t_admission;",
     [("adm", "select count(*) from adt.t_admission where admission_id > {pre_max};")],
     ["delete from adt.t_admission_details where admission_id > {pre_max};",
      "delete from adt.t_admission where admission_id > {pre_max};"]),
    ("donor-register", "/api/bloodbank/registerDonor",
     lambda: {"organizationId": 1, "unitId": 1, "firstName": "AUTHGAP",
              "lastName": "PROBE", "dob": "01-01-1990 00:00:00", "genderId": 1,
              "createdBy": 1, "updatedBy": 1},
     "select coalesce(max(donor_id),0) from blood_bank.t_donor_registration;",
     [("donor", "select count(*) from blood_bank.t_donor_registration where donor_id > {pre_max};")],
     ["delete from blood_bank.t_donor_details where donor_id > {pre_max};",
      "delete from blood_bank.t_donor_registration where donor_id > {pre_max};"]),
    ("transfer-accept-W2", "/adt/acceptTransferRequestForADT",
     lambda: {"organizationId": 1, "unitId": 1, "createdBy": 1, "updatedBy": 1,
              "transferRequestId": 1},
     None, [], []),
    ("nursing-order-mapper", "/api/opd/saveNursingOrderMapper",
     lambda: {"organizationId": 1, "unitId": 1, "createdBy": 1, "updatedBy": 1},
     None, [], []),
    ("walkin-sale", "/api/pharmacy/sales/walkins/save/prescription",
     lambda: {"organizationId": 1, "unitId": 1, "storeId": 1, "tempBillId": 0,
              "createdBy": 1},
     None, [], []),
    ("appointment-schedule", "/api/opd/scheduleAppointment",
     lambda: {"organizationId": 1, "unitId": 1, "createdBy": 1, "updatedBy": 1},
     None, [], []),
]

def main():
    print(f"{'endpoint':44s} {'status':>6s} {'landed':>8s}  class")
    print("-" * 78)
    results = []
    for name, path, payload_fn, pre_sql, post_sqls, teardown in CFG:
        pre = int(psql(pre_sql).strip() or 0) if pre_sql else 0
        status, body = post_noauth(path, payload_fn())
        landed = "n/a"
        if post_sqls:
            counts = []
            for _label, q in post_sqls:
                counts.append(psql(q.replace("{pre_max}", str(pre))).strip())
            landed = "+".join(counts)
            for q in teardown:
                psql(q.replace("{pre_max}", str(pre)))
        if status == 401:
            cls = "GATED (401)"
        elif post_sqls and landed != "0" and all(v != "0" for v in landed.split("+")):
            cls = "FULL-GAP: row(s) landed without auth"
        elif post_sqls:
            cls = "REACHED (non-401, no row)"
        else:
            cls = "REACHED (write path executed, no 401)"
        results.append((path, status, landed, cls))
        print(f"{path:44s} {status:>6d} {landed:>8s}  {cls}")
    full = [r for r in results if "FULL-GAP" in r[3]]
    reached = [r for r in results if "REACHED" in r[3]]
    gated = [r for r in results if "GATED" in r[3]]
    print("-" * 78)
    print(f"TOTAL {len(results)} | FULL-GAP {len(full)} | REACHED {len(reached)} | GATED {len(gated)}")
    return 0

if __name__ == "__main__":
    sys.exit(main())
