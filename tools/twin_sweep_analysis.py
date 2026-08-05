#!/usr/bin/env python3
import re, glob
from collections import defaultdict

# TWIN SWEEP: query names in commented blocks + live declarations (any form), per entity file
twin = []          # (file, name) — name in both a commented and a live declaration
live_dup = []      # name in 2+ live declarations in the same file
all_live = defaultdict(list)

for f in glob.glob("legacy/**/*.java", recursive=True):
    if "/target/" in f or "Test.java" in f:
        continue
    src = open(f, encoding="utf-8", errors="replace").read()
    names = list(re.finditer(r'name\s*=\s*"([A-Z][A-Z0-9_]+)"', src))
    if not names:
        continue
    commented = set()
    live = set()
    for m in names:
        ls = src.rfind("\n", 0, m.start()) + 1
        line = src[ls:src.find("\n", m.start())]
        before = src[max(0, m.start() - 150):m.start()]
        in_block_comment = before.rfind("/*") > before.rfind("*/")
        if in_block_comment or line.lstrip().startswith("//"):
            commented.add(m.group(1))
        else:
            live.add(m.group(1))
            all_live[m.group(1)].append(f.split("/")[-1])
    for n in sorted(commented.intersection(live)):
        twin.append((f.split("/")[-1], n))

for n, files in all_live.items():
    if len(files) >= 2:
        live_dup.append((n, files))

print(f"TWINS (name in BOTH a commented block and a LIVE declaration): {len(twin)}")
for fn, n in sorted(twin):
    print(f"   {n:<55} {fn}")
print(f"\nLIVE DUPLICATES (same name declared 2+ times live in one file): {len(live_dup)}")
for n, files in live_dup:
    print(f"   {n:<55} {files}")

org22 = ["CHECK_ALREADY_EXIST_REG","DIAGNOSIS_AUTO_FILL_SEARCH","GET_ACTIVE_DISCHARGE_SUMMARY_FIELD_MASTER","GET_ACTIVE_DISCHARGE_TYPE_LIST","GET_ACTIVE_TEXTUAL_RANGE_MASTER_LIST","GET_ALL_ASSET_SCHEDULED_LIST","GET_BABY_DETAILS_BY_ID","GET_CONSULT_DOCTOR_LIST_BY_ADMISSION_ID","GET_DESIGNATION_MASTER_LIST","GET_DETAILED_SERVICE_LIST","GET_DIAGNOSIS_BY_GENDER_ID","GET_DISCHARGE_SUMMARY_FIELD_MASTER_BY_ID","GET_DISCHARGE_SUMMARY_FIELD_MASTER_BY_NAME","GET_DISCHARGE_SUMMARY_FIELD_MASTER_BY_NAME_NOT_ID","GET_DISCOUNT_CATEGORY_BY_ID","GET_HELP_VALUE_BY_PARAMETER","GET_ICPC_TEMPLATE_MAPPING_COUNT","GET_LIST_APPOINTMENT_STATUS_MASTER","GET_LIST_OF_CATEGORY_WISE_QUALIFIERS","GET_LIST_SEVERITY_MASTER","GET_MEDICATION_HISTORY_LIST","GET_ROUTE_OF_ADMINISTRATION_LIST","GET_SLIDE_LIST_FOR_RE_SLIDE_CREATION","GET_SPARE_CONSUMPTION_LIST_BY_PMS_ID","GET_TEXTUAL_RANGE_MASTER_BY_ID","GET_VITAL_MASTER_TO_SYNC_BY_ID"]
twin_names = {n for _, n in twin}
affected = sorted(twin_names.intersection(org22))
print(f"\n22-org findings with a live twin (classification invalidated): {affected}")
