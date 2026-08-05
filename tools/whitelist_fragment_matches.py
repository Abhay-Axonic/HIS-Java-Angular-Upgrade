#!/usr/bin/env python3
import re, glob

frags = ["update/service", "opd/saveMedication", "patientRegistration", "roomRent", "/br/", "unit/org/",
         "decrypt", "deleteParticipants", "saveAndPay", "saveAnalyzedReport", "savePatientThroughMylife",
         "saveDetailsOfAbdm", "dataPush", "refreshAllCaches", "createOrganizationFromWeb",
         "saveMyLatestMedicalSummaryCallBack", "saveAIAppointmentInQueue", "glablServiceMaster",
         "getSerivceRateGlobalList", "updatePatientRegistrationFromMyLife", "getBillNoByPatient",
         "resultUrl", "validationResultResponse", "updateNABH", "saveTwilioMsg", "generateTokenForWebUser",
         "getAvailableDoctorBySpeciality", "appointment/check", "patientTypeHeadSearchForSelfRegistration"]
paths = {}
for f in glob.glob("legacy/**/controller/*.java", recursive=True):
    if "/target/" in f:
        continue
    src = open(f, encoding="utf-8", errors="replace").read()
    for m in re.finditer(r'@RequestMapping\(value\s*=\s*"([^"]+)"[^)]*method\s*=\s*RequestMethod\.(\w+)', src):
        paths.setdefault(m.group(1), set()).add(m.group(2))
    for m in re.finditer(r'@(?:Get|Post|Put|Delete)Mapping\(value\s*=\s*"([^"]+)"', src):
        paths.setdefault(m.group(1), set()).add(m.group(0)[1:4].upper())
for frag in frags:
    hits = [(p, meths) for p, meths in paths.items() if frag in p]
    if hits:
        writes = [p for p, meths in hits if meths.intersection({"POST", "PUT", "DELETE", "PATCH"})]
        reads = [p for p, meths in hits if not meths.intersection({"POST", "PUT", "DELETE", "PATCH"})]
        print(f"[{frag}]  {len(hits)} controller path(s)")
        for p in writes:
            print(f"    WRITE {p}  {sorted(paths[p])}")
        for p in reads[:4]:
            print(f"    read  {p}  {sorted(paths[p])}")
