#!/usr/bin/env python3
"""verify_deployed_regressions.py — regression assertion that every closed BR's fix
signature is still present in the RUNNING deployed jars.

Why this exists: the per-screen harness (verify_master_screen.py) checks jar
freshness and bytecode ref counts for the screen UNDER TEST only. It has no
assertion that previously-fixed screens survive a redeploy. The 2026-08-05 batch
deployment dropped the sweep branch's fixes (gender service 5 rebinds, gender DAO
guards, patientSource service rebinds, SampleMaster entity scoping) from the
running jars and nothing failed — this tool closes that gap.

Signature types:
  vis_min     — deployed class bytecode contains >= N getVerifiedOrganizationId /
                getVerifiedUnitId references (service/DAO rebind+guard fixes)
  method_sig  — deployed class bytecode contains the exact method descriptor
                (signature-based fixes, e.g. getById(Integer,Integer))
  query       — deployed entity constant pool (javap -v) contains the exact query
                fragment (named-query org/unit scoping fixes)
  string_absent — deployed class constant pool does NOT contain the string
                (e.g. JWT-whitelist removal: the URL must be gone)

Usage:
  python3 verify_deployed_regressions.py          # run the table against the deployed jars
  python3 verify_deployed_regressions.py --self-test   # prove every assertion type can FAIL

Exit code: 0 = all rows PASS; 1 = any row FAIL; 2 = self-test FAIL.
"""
import argparse
import os
import re
import shutil
import subprocess
import sys
import tempfile

LIB = os.path.expanduser("/opt/homebrew/opt/tomcat@9/libexec/webapps/ROOT/WEB-INF/lib")
JAVAP = "/opt/homebrew/Cellar/openjdk@21/21.0.11/libexec/openjdk.jdk/Contents/Home/bin/javap"

# ---------------------------------------------------------------------------
# The per-BR signature table (spec). Expected values measured from the fix
# commits' source on 2026-08-05; deployed values verified by this tool.
# ---------------------------------------------------------------------------
TABLE = [
    # BR    label                        jar           class                                                 type           expected
    (127, "productCategory",             "Pharmacy-0.0.1-SNAPSHOT.jar", "com.param.pharmacy.master.global.service.productCategory.ProductCategoryService", "vis_min", 2),
    (128, "companyMaster",               "Global-0.0.1-SNAPSHOT.jar",    "com.param.global.service.CompanyMasterServiceImpl",                           "vis_min", 20),
    (129, "globalDao insurance list",    "Global-0.0.1-SNAPSHOT.jar",    "com.param.global.service.global.GlobalService",                               "vis_min", 2),
    (130, "mainStore",                   "Pharmacy-0.0.1-SNAPSHOT.jar",  "com.param.pharmacy.master.unit.service.mainStore.MainStoreService",           "vis_min", 12),
    (133, "bloodComponent",              "BloodBank-0.0.1-SNAPSHOT.jar", "com.param.bloodBank.service.BloodComponentMasterServiceImpl",                 "vis_min", 14),
    (134, "userMaster",                  "Global-0.0.1-SNAPSHOT.jar",    "com.param.global.dao.UserMasterDaoImpl",                                      "vis_min", 2),
    (135, "userRoleMapper",              "Global-0.0.1-SNAPSHOT.jar",    "com.param.global.unit.dao.UserRoleMapperDaoImpl",                             "vis_min", 6),
    (136, "unitAppBillingConfig",        "Global-0.0.1-SNAPSHOT.jar",    "com.param.global.dao.UnitApplicationBillingConfigurationDaoImpl",             "vis_min", 8),
    (138, "preOperativeDetails",         "OTIMS-0.0.1-SNAPSHOT.jar",     "com.param.otims.dao.PreOperativeDetailsDaoImpl",                             "vis_min", 2),
    (139, "routeOfAdministration",       "Pharmacy-0.0.1-SNAPSHOT.jar",  "com.param.pharmacy.master.global.dao.routeOfAdministration.RouteOfAdministrationDao", "method_sig", "getById(java.lang.Integer, java.lang.Integer)"),
    (142, "bedMaster list join",         "Entity-0.0.1-SNAPSHOT.jar",    "com.param.adt.master.global.model.BedMaster",                                 "query", "LEFT"),
    (143, "patientSource (BR DAO)",      "ADT-0.0.1-SNAPSHOT.jar",       "com.param.adt.master.global.dao.PatientSourceMasterDaoImpl",                  "vis_min", 4),
    (144, "referralType",                "ADT-0.0.1-SNAPSHOT.jar",       "com.param.adt.master.global.dao.ReferralTypeMasterDaoImpl",                   "vis_min", 3),
    (145, "icuType",                     "ADT-0.0.1-SNAPSHOT.jar",       "com.param.adt.master.global.dao.ICUTypeMasterDaoImpl",                        "vis_min", 2),
    (146, "billingBedCategory",          "ADT-0.0.1-SNAPSHOT.jar",       "com.param.adt.master.unit.dao.BillingBedCategoryMasterDaoImpl",               "vis_min", 2),
    (147, "allergy",                     "Global-0.0.1-SNAPSHOT.jar",    "com.param.global.dao.AllergyTypeMasterDaoImpl",                               "vis_min", 2),
    (148, "dose",                        "Global-0.0.1-SNAPSHOT.jar",    "com.param.global.org.common.dao.DoseMasterDaoImpl",                           "vis_min", 2),
    (149, "checklistItem",               "OTIMS-0.0.1-SNAPSHOT.jar",     "com.param.otims.dao.ChecklistItemMasterDaoImpl",                             "vis_min", 6),
    (150, "autoclave",                   "CSSDMaster-0.0.1-SNAPSHOT.jar","com.param.CSSDMaster.dao.AutoclaveMasterDaoImpl",                            "vis_min", 4),
    (151, "equipmentChecklistMapper",    "Global-0.0.1-SNAPSHOT.jar",    "com.param.equipmentchecklist.dao.EquipmentChecklistMapperDaoImpl",            "vis_min", 6),
    (152, "vitalQualifier",              "Global-0.0.1-SNAPSHOT.jar",    "com.param.global.dao.VitalQualifireMasterDaoImpl",                           "vis_min", 8),
    (156, "ambulanceTarrif",             "Global-0.0.1-SNAPSHOT.jar",    "com.param.global.dao.AmbulanceTarrifDaoImpl",                                "vis_min", 4),
    (157, "couponTemplate",              "Global-0.0.1-SNAPSHOT.jar",    "com.param.global.billing.dao.CouponTemplateMasterDaoImpl",                   "vis_min", 8),
    (158, "taxComponent",                "Pharmacy-0.0.1-SNAPSHOT.jar",  "com.param.pharmacy.master.global.service.taxComponent.TaxComponentService",   "vis_min", 3),
    (159, "mPackage",                    "Global-0.0.1-SNAPSHOT.jar",    "com.param.global.dao.MPackageMasterDaoImpl",                                 "vis_min", 4),
    (160, "mPackage(2)",                 "Global-0.0.1-SNAPSHOT.jar",    "com.param.global.dao.MPackageMasterDaoImpl",                                 "vis_min", 4),
    (161, "couponTemplate(2)",           "Global-0.0.1-SNAPSHOT.jar",    "com.param.global.billing.dao.CouponTemplateMasterDaoImpl",                   "vis_min", 8),
    (166, "priority list (batch)",       "Global-0.0.1-SNAPSHOT.jar",    "com.param.global.org.common.dao.PriorityMasterDaoImpl",                      "vis_min", 6),
    (167, "religion (batch)",            "ADT-0.0.1-SNAPSHOT.jar",       "com.param.adt.master.global.dao.ReligionMasterDaoImpl",                      "vis_min", 6),
    (170, "priority active (BR-170)",    "Global-0.0.1-SNAPSHOT.jar",    "com.param.global.service.UnknownPatientRegistrationServiceImpl",             "vis_min", 2),
    # --- sweep branch rows (the divergence class this tool exists to catch) ---
    (999, "SWEEP gender service (5 rebinds)", "Global-0.0.1-SNAPSHOT.jar", "com.param.global.service.GenderMasterServiceImpl", "vis_min", 5),
    (999, "SWEEP gender DAO guards",     "Global-0.0.1-SNAPSHOT.jar",    "com.param.global.dao.GenderMasterDaoImpl",                                   "vis_min", 2),
    (999, "SWEEP gender entity list/by-id org", "Entity-0.0.1-SNAPSHOT.jar", "com.param.global.model.GenderMaster", "query", "FROM GenderMaster as genderMaster WHERE genderMaster.organizationId=:orgId"),
    (999, "SWEEP patientSource service rebinds", "ADT-0.0.1-SNAPSHOT.jar", "com.param.adt.master.global.service.PatientSourceMasterServiceImpl", "vis_min", 3),
    (999, "SWEEP tariff service rebinds", "Billing-0.0.1-SNAPSHOT.jar",  "com.param.billing.unit.service.TariffMasterServiceImpl",                     "vis_min", 16),
    (999, "SWEEP tariff DAO binds",      "Billing-0.0.1-SNAPSHOT.jar",   "com.param.billing.unit.dao.TariffMasterDaoImpl",                             "vis_min", 6),
    (999, "SWEEP bank getHospitalBank",  "Billing-0.0.1-SNAPSHOT.jar",   "com.param.billing.global.service.BankMasterServiceImpl",                     "vis_min", 8),
    (999, "SWEEP LIS/common service",    "LIS-0.0.1-SNAPSHOT.jar",       "com.param.lis.common.service.CommonListServiceImpl",                         "vis_min", 53),
    (999, "SWEEP LIS/common DAO",        "LIS-0.0.1-SNAPSHOT.jar",       "com.param.lis.common.dao.CommonListDaoImpl",                                 "method_sig", "getGenderList"),
    (999, "SWEEP SampleMaster entity org", "Entity-0.0.1-SNAPSHOT.jar",  "com.param.entity.lis.global.SampleMaster",                                   "query", "organizationId"),
    # --- BR-171 (committed, deployment pending composition approval) ---
    (171, "gender active list (BR-171)", "Global-0.0.1-SNAPSHOT.jar",    "com.param.global.service.GenderMasterServiceImpl",                           "vis_min", 1),
    (171, "gender active whitelist removed", "HIS-WAR-0.0.1-SNAPSHOT.jar", "com.param.filter.JwtTokenFilter", "string_absent", "/adt/getActiveGenderList"),
]


def extract(jar, cls):
    """Return (javap -c -p stdout, javap -v stdout) or (None, None) if absent.
    Looks in the jar first, then in the exploded WAR's WEB-INF/classes (e.g. the
    JwtTokenFilter, which is not packaged in any jar)."""
    entry = cls.replace(".", "/") + ".class"
    jar_path = os.path.join(LIB, jar)
    td = tempfile.mkdtemp(prefix="hermes-regcheck-")
    try:
        r = subprocess.run(["unzip", "-o", "-q", jar_path, entry], cwd=td,
                           capture_output=True, text=True)
        if r.returncode != 0:
            classes_dir = os.path.join(os.path.dirname(LIB), "classes")
            src = os.path.join(classes_dir, entry)
            if not os.path.exists(src):
                return None, None
            os.makedirs(os.path.dirname(os.path.join(td, entry)), exist_ok=True)
            shutil.copy(src, os.path.join(td, entry))
        c = subprocess.run([JAVAP, "-c", "-p", entry], cwd=td,
                           capture_output=True, text=True).stdout
        v = subprocess.run([JAVAP, "-v", entry], cwd=td,
                           capture_output=True, text=True).stdout
        return c, v
    finally:
        shutil.rmtree(td, ignore_errors=True)


def check_row(row):
    _, label, jar, cls, typ, expected = row
    code, verbose = extract(jar, cls)
    if code is None:
        return False, f"(class {cls} not found in {jar})"
    if typ == "vis_min":
        n = code.count("getVerifiedOrganizationId") + code.count("getVerifiedUnitId")
        return n >= int(expected), f"(VIS={n}, expected>={expected})"
    if typ == "method_sig":
        found = expected in code
        return found, f"(sig present={found})"
    if typ == "query":
        found = expected in (verbose or "")
        return found, f"(query fragment present={found})"
    if typ == "string_absent":
        found = expected in (verbose or "")
        return not found, f"(string present={found}, expected absent)"
    return False, "(unknown signature type)"


def run_table():
    fails = 0
    print(f"{'BR':<5} {'label':<42} {'type':<14} result")
    print("-" * 90)
    for row in TABLE:
        br, label, *_ = row
        ok, detail = check_row(row)
        if not ok:
            fails += 1
        print(f"{br:<5} {label:<42} {row[5]:<14} {'PASS' if ok else 'FAIL'} {detail}")
    print("-" * 90)
    if fails:
        print(f"RESULT: FAIL — {fails} closed-BR fix signature(s) ABSENT from the running jars")
        return 1
    print("RESULT: PASS — every closed BR's fix signature present in the running jars")
    return 0


def self_test():
    """Prove every assertion type can FAIL on known-bad input."""
    ok = True
    tests = []

    def expect_fail(name, row, why):
        nonlocal ok
        r, detail = check_row(row)
        good = r is False
        ok = ok and good
        tests.append((name, good, why, detail))

    # vis_min negative control: demand an impossible count on a class with 0 refs
    expect_fail("vis_min can fail",
                (0, "neg", "Global-0.0.1-SNAPSHOT.jar",
                 "com.param.global.dao.GenderMasterDaoImpl", "vis_min", 999),
                "deployed gender DAO has 0 VIS; demanding 999 must FAIL")
    # method_sig negative control
    expect_fail("method_sig can fail",
                (0, "neg", "Global-0.0.1-SNAPSHOT.jar",
                 "com.param.global.dao.GenderMasterDaoImpl", "method_sig",
                 "getNonexistentMethod(java.lang.Integer)"),
                "nonexistent descriptor must FAIL")
    # query negative control
    expect_fail("query can fail",
                (0, "neg", "Entity-0.0.1-SNAPSHOT.jar",
                 "com.param.global.model.GenderMaster", "query",
                 "NO_SUCH_QUERY_FRAGMENT_zzz"),
                "nonexistent query fragment must FAIL")
    # string_absent negative control: assert the whitelist URL is absent while it IS present
    expect_fail("string_absent can fail",
                (0, "neg", "HIS-WAR-0.0.1-SNAPSHOT.jar",
                 "com.param.filter.JwtTokenFilter", "string_absent",
                 "/adt/getActiveGenderList"),
                "whitelist URL is currently PRESENT in the deployed filter; asserting absent must FAIL")
    # positive control: a known-present signature must PASS (proves the plumbing works both ways)
    pos = check_row((156, "pos", "Global-0.0.1-SNAPSHOT.jar",
                     "com.param.global.dao.AmbulanceTarrifDaoImpl", "vis_min", 4))
    good_pos = pos[0] is True
    ok = ok and good_pos
    tests.append(("positive control passes (BR-156)", good_pos, "deployed VIS=4 >= 4", pos[1]))

    for name, good, why, detail in tests:
        print(f"[{'PASS' if good else 'FAIL'}] {name}: {why} -> {detail}")
    print("=" * 90)
    if ok:
        print("SELF-TEST: PASS — every assertion type proven capable of failing")
        return 0
    print("SELF-TEST: FAIL")
    return 2


def main():
    ap = argparse.ArgumentParser(description=__doc__)
    ap.add_argument("--self-test", action="store_true", help="prove every assertion can fail")
    args = ap.parse_args()
    if args.self_test:
        return self_test()
    return run_table()


if __name__ == "__main__":
    sys.exit(main())
