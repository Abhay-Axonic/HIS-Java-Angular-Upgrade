#!/usr/bin/env python3
"""Section 4: re-confirm the 30 SAFE + 3 NOT_STALE verdicts against the EXTRACTED list.
For each of the 33: filter membership as spelled (which copy/both/missing), the caller
search (named file:line + token-wrapper shape), the URL-constant files separately.
Verdict: SAFE TO REMOVE / NOT SAFE / DEAD."""
import re, subprocess, os, sys

ROOT = "/Users/axonichealth/Downloads/His_Java_Angular_Pgrade/HIS_Java_Angular_upgrade"
WEB = os.path.join(ROOT, "legacy/HIS-WAR/src/main/webapp")
CFG = os.path.join(WEB, "resources/assets/js/config")
FILTER = os.path.join(ROOT, "legacy/HIS-WAR/src/main/java/com/param/filter/JwtTokenFilter.java")

SAFE30 = [
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
NOT_STALE3 = ["/adt/discharge/dischargeSummaryCard", "/api/opd/getDoctorFavouriteMedicationForCoversheet", "/unit/subcription"]

src = open(FILTER, encoding="utf-8").read()
i76 = src.find('if (unitId != null && userId != null && authorizationHeader != null')
ielse = src.find('} else if (splitUrl.length <= 2')
copy1 = src[i76:ielse]
copy2 = src[ielse:src.find('chain.doFilter(request, response);', ielse)]

def grep(pattern, path):
    r = subprocess.run(["grep", "-rn", "-F", pattern, path], capture_output=True, text=True, cwd=ROOT)
    return [l for l in r.stdout.splitlines() if l.strip() and "/target/" not in l]

def membership(frag):
    in1 = re.search(r'requestUrl\.contains\(\(?"' + re.escape(frag) + r'"', copy1) is not None
    in2 = re.search(r'requestUrl\.contains\(\(?"' + re.escape(frag) + r'"', copy2) is not None
    return ("copy1+copy2" if in1 and in2 else "copy1 only" if in1 else "copy2 only" if in2 else "MISSING")

def callers(frag):
    hits = grep(frag, WEB)
    named = []
    for h in hits[:5]:
        parts = h.split(":", 2)
        if len(parts) < 3:
            continue
        rel, ln = parts[0], parts[1]
        try:
            flines = open(os.path.join(ROOT, rel), encoding="utf-8", errors="replace").read().splitlines()
            ctx = flines[max(0, int(ln) - 2):int(ln) + 8]
        except Exception:
            continue
        shape = "serviceAction" if any("serviceAction" in l for l in ctx) else (
            "RAW" if re.search(r"\$http|\$\.ajax|\$\.get|\$\.post|axios|fetch\(", " ".join(ctx)) else "?")
        named.append(f"{os.path.basename(rel)}:{ln}[{shape}]")
    cfg = grep(frag, CFG)
    return named, [c.split(":", 2)[1] for c in cfg][:3]

print("=== 30 SAFE + 3 NOT_STALE against the EXTRACTED list ===")
summary = {"SAFE TO REMOVE": [], "NOT SAFE": [], "DEAD": [], "MISSING": []}
for frag in SAFE30 + NOT_STALE3:
    mem = membership(frag)
    named, cfg = callers(frag)
    if mem == "MISSING":
        verdict = "MISSING"
    elif not named and not cfg:
        verdict = "DEAD"
    elif all("[serviceAction]" in n for n in named):
        verdict = "SAFE TO REMOVE"
    else:
        verdict = "NOT SAFE"
    summary[verdict].append(frag)
    print(f"[{verdict}] {frag}")
    print(f"   filter: {mem}")
    for n in named:
        print(f"   caller: {n}")
    if cfg:
        print(f"   config-file lines: {cfg}")

print("\n=== summary ===")
for k, v in summary.items():
    print(f"  {k}: {len(v)}")
    for e in v:
        print(f"     {e}")
