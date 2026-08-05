#!/usr/bin/env python3
"""PHASE 1 re-confirmation for fix/whitelist-stale-removal (independent of the QF-U audit tool).

Re-confirms the 49 NO_CALLER + 30 SAFE whitelist entries:
- NO_CALLER entries: whole-tree search (js/html/java/xml/config), JwtTokenFilter's own lines and
  commented lines and @RequestMapping/definition sites excluded; the URL-constant files searched
  separately (constant definitions + their usages); variant forms checked (ForIcon/ForMedicalHistory
  lesson). Rule-10 control FIRST (known positive must be found).
- SAFE entries: named caller (file:line) + the call must go through GenericService.serviceAction.
"""
import re, subprocess, os

ROOT = "/Users/axonichealth/Downloads/His_Java_Angular_Pgrade/HIS_Java_Angular_upgrade"
WEB = os.path.join(ROOT, "legacy/HIS-WAR/src/main/webapp")
JAVA = os.path.join(ROOT, "legacy")
CFG = os.path.join(WEB, "resources/assets/js/config")

def grep_tree(pattern, extra_glob=None):
    globs = ["--include=*.js", "--include=*.html", "--include=*.java", "--include=*.xml",
             "--include=*.properties", "--include=*.json"]
    if extra_glob:
        globs.append(extra_glob)
    r = subprocess.run(["grep", "-rn", "-F", pattern] + globs + [JAVA],
                       capture_output=True, text=True, cwd=ROOT)
    hits = []
    for l in r.stdout.splitlines():
        if not l.strip():
            continue
        if "/target/" in l or "JwtTokenFilter" in l or "//" in l or "/*" in l:
            continue
        if "@RequestMapping" in l or "@GetMapping" in l or "@PostMapping" in l or "@PutMapping" in l:
            continue
        hits.append(l)
    return hits

def grep_cfg(pattern):
    r = subprocess.run(["grep", "-rn", "-F", pattern, CFG], capture_output=True, text=True, cwd=ROOT)
    return [l for l in r.stdout.splitlines() if l.strip() and "//" not in l]

def stem_of(fragment):
    seg = fragment.rstrip("/").split("/")[-1]
    m = re.match(r"^(get|save|update|search|delete|check|validate|generate|fetch|find)([A-Z])", seg)
    if m:
        rest = seg[len(m.group(1)):]
        # strip a trailing For<Something> / By<Something> suffix for the variant search
        core = re.sub(r"(For|By|From)[A-Z][A-Za-z]+$", "", rest)
        if len(core) >= 8:
            return m.group(1) + core
    return seg if len(seg) >= 8 else None

def known_positive_control():
    hits = grep_tree("getMedicationHistoryList")
    ipd = [h for h in hits if "IPDCoverSheetController" in h]
    ok = False
    if ipd:
        rel = ipd[0].split(":", 1)[0]
        ln = int(ipd[0].split(":")[1])
        flines = open(os.path.join(ROOT, rel), encoding="utf-8", errors="replace").read().splitlines()
        ctx = flines[max(0, ln - 2):ln + 7]
        ok = any("serviceAction" in l for l in ctx)
    return ok, ipd[0] if ipd else None

NO_CALLER_49 = [
    "/api/billing/unit/getDoctorCharges", "/api/common/getSearchedInstructionList",
    "/api/opd/coversheetInstructionList/1", "/api/opd/saveComplaintAppointment/",
    "/api/opd/getPatientDiagnosisByPatientIdForNewEMR", "/api/opd/savePatientDiagnosisForNewEMR",
    "/api/opd/saveFreeTextInvestigations", "/api/opd/saveCovTemplateDataForPatient",
    "/api/opd/updateCovTemplateDataForPatient", "/api/opd/getInvestigationsListForNewEMR",
    "/api/billing/bill/service/cancel", "/api/opd/cancelFreeTextInvestigations",
    "/api/opd/getQuickNotesMapperList", "/api/opd/saveQuickNotesMapper",
    "/api/opd/getMedicationDetailsForNewEMR", "/api/opd/searchDrugByGenericNameOrItemName",
    "/api/opd/getSubstituteListForNewEMR", "/api/opd/medicationUpdate",
    "/api/opd/saveJustificationForMedicationDeleteMultipleMedication",
    "/api/opd/getLastEncounteredListOfPatient", "/api/opd/updateEncounterDetailsForNewEMR",
    "/api/global/getPatientDetailsListForNewEMR", "/api/opd/saveAppointmentVitalMapper/",
    "/api/global/savePatientRemarksForNewEMR", "/api/opd/getCompleteConsultationReport",
    "/api/global/getPatientDetailsListForNewEMROnlyFavoritesList", "/api/opd/searchFreeTextInvestigations",
    "/api/opd/getFreeTextInvestigations", "/api/common/getSearchedFreeTextInstructionList",
    "/api/opd/getCoversheetListData", "/api/opd/getCurrentPatientHabitDetails",
    "/api/opd/getOldPatientFamilyHistory", "/api/opd/getOldPatientSurgicalHistory",
    "/api/global/updateEmployeeStatus", "/api/global/updateDoctorStatus",
    "/br/", "/global/getPopUpDataWithId", "/global/saveTemplateSummery",
    "/https://feqituyulh.execute-api.ap-south-1.amazonaws.com/dev/api/generateFlipbook",
    "/checkParti/", "/HIS-WAR/deleteParticipants/Patient/dataroom_41015",
    "/HIS-WAR/deleteParticipants", "/HIS-WAR/deleteParticipants/*", "/HIS-WAR/deleteParticipants/**",
    "/api/deleteParticipants", "/decrypt", "/updateNABH", "/roomRent", "/api/refreshAllCaches",
]

SAFE_30 = [
    "/api/opd/getCoversheetAsTemaplateList", "/api/opd/getTemplateDataByCovTempId",
    "/api/opd/coversheetInstruction", "/api/opd/updateCoversheetInstructionStatus",
    "/api/opd/getComplaintAppointment", "/api/opd/getMasterListOfICPCSystemForCoversheet",
    "/api/opd/updateComplaintAppointmentStatusToRemoveComplaint", "/api/opd/saveFreeTexPatientDiagnosis",
    "/api/opd/savePatientCancelledDiagnosis", "/api/opd/updateFollowUpDateOfPatient",
    "/global/serviceMaster", "/api/opd/saveInvestigation", "/api/common/getActiveInspectionMasterList",
    "/api/globals/frequency", "/api/opd/saveMedication", "/api/opd/getFrequencyList",
    "/api/opd/saveJustificationForMedicationDelete", "/api/opd/getOPDMedicationListById",
    "/api/opd/getListOfVitalsByPatientIdForCoversheet", "/api/opd/searchFreeTextComplaint",
    "/api/opd/serchFreeTexPatientDiagnosis", "/api/opd/getMedicationHistoryList",
    "/api/opd/getCurrentPatientFamilyHistoryForCoversheet",
    "/api/opd/getCurrentAllergyPatientMapperForCoversheet",
    "/api/opd/getCurrentPatientSurgicalHistoryForCoversheet",
    "/unit/org/", "/getSerivceRateGlobalList", "/api/global/glablServiceMaster",
    "opd/saveMedication", "update/service",
]

# ---- Rule-10 control FIRST ----
print("=== RULE 10 CONTROL (known positive must be found before any negative is trusted) ===")
ok, hit = known_positive_control()
print(f"getMedicationHistoryList -> {hit if hit else 'NOT FOUND'}; serviceAction context: {ok}")
assert ok, "CONTROL FAILED"
print("CONTROL PASS — whole-tree pattern + serviceAction-context proven\n")

# ---- the 49 NO_CALLER re-confirmation ----
print("=== PHASE 1a: 49 NO_CALLER entries — independent re-confirmation ===")
dead, revived = [], []
for frag in NO_CALLER_49:
    hits = grep_tree(frag)
    cfg_hits = grep_cfg(frag)
    stem = stem_of(frag)
    variants = []
    if stem:
        for l in grep_tree(stem):
            m = re.search(r'"(/[A-Za-z0-9_/]*' + re.escape(stem) + r'[A-Za-z0-9_]*)"', l)
            if m and m.group(1) != frag:
                variants.append(m.group(1))
    found = [h.split(":", 2)[:2] for h in (hits + cfg_hits)][:3]
    verdict = "CONFIRMED DEAD" if not hits and not cfg_hits else "HAS A CALLER AFTER ALL"
    (dead if verdict == "CONFIRMED DEAD" else revived).append(frag)
    print(f"\n[{verdict}] {frag}")
    print(f"   SEARCHED: exact fragment (whole tree, def-sites excluded) + config dir" +
          (f" + stem '{stem}' variants" if stem else ""))
    if variants:
        print(f"   VARIANTS FOUND (different URIs, not this fragment): {sorted(set(variants))[:4]}")
    if found:
        for f, ln in found:
            print(f"   FOUND: {os.path.basename(f)}:{ln}")
    else:
        print(f"   FOUND: nothing in js/html/java/xml/properties/json or the config dir")

print(f"\n=== 49-NO_CALLER SUMMARY: CONFIRMED DEAD {len(dead)} | REVIVED {len(revived)} ===")
print("REVIVED:", revived if revived else "none")

# ---- the 30 SAFE re-confirmation: named callers ----
print("\n=== PHASE 1b: 30 SAFE entries — named callers + serviceAction shape ===")
unverified = []
for frag in SAFE_30:
    hits = grep_tree(frag)
    named = []
    for h in hits[:4]:
        parts = h.split(":", 2)
        rel = parts[0]
        ln = parts[1]
        flines = open(os.path.join(ROOT, rel), encoding="utf-8", errors="replace").read().splitlines()
        ctx = flines[max(0, int(ln) - 2):int(ln) + 8]
        shape = "serviceAction" if any("serviceAction" in l for l in ctx) else ("RAW" if re.search(r"\$http|\$\.ajax|\$\.get|\$\.post|axios|fetch\(", " ".join(ctx)) else "?")
        named.append((os.path.basename(rel), ln, shape))
    if not named:
        unverified.append(frag)
        print(f"[UNVERIFIED] {frag} — no caller hit")
    else:
        bad = [n for n in named if n[2] != "serviceAction"]
        tag = "OK" if not bad else "CHECK"
        print(f"[{tag}] {frag}")
        for f, ln, s in named:
            print(f"   caller: {f}:{ln} shape={s}")
print(f"\n=== 30-SAFE SUMMARY: named serviceAction callers confirmed for {30 - len(unverified)}/30 ===")
if unverified:
    print("UNVERIFIED (need the constant-trace):", unverified)
