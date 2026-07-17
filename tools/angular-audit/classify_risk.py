#!/usr/bin/env python3
"""
Enriches data/states.json with:
  - module/department (from the view template's folder path)
  - clinical-safety and billing risk tags (keyword + module heuristic)
  - view-file existence + rough form-field count
  - controller resolution + attributable API-call / coupling stats

Writes data/states_enriched.json. This is a heuristic first-pass sort, not
a clinical/billing sign-off -- see docs/ANGULAR_PHASE0_SCREEN_INVENTORY.md
for the full caveats. Re-run any time states.json or the controller index
changes.

Keyword matching note: uses word-boundary regex, not naive substring
matching. An earlier version of this logic (Phase 0's first pass) used
plain `keyword in text` checks and produced widespread false positives --
e.g. 'er ' matched inside "...Master " (any title ending in "Master"),
and 'lis' matched inside "...List" screens. Each keyword below is tagged
either "stem" (leading boundary only, e.g. 'surger' deliberately matches
'surgery'/'surgical') or exact-word (both boundaries + optional plural,
e.g. 'lis' must NOT match inside 'list').
"""
import json
import os
import re
import sys

from common import (
    STATES_JSON, CONTROLLER_INDEX_JSON, CONTROLLER_STATS_JSON,
    STATES_ENRICHED_JSON, WEBAPP, SHARED_CONTROLLER_THRESHOLD, ensure_data_dir,
)
from collections import Counter

# (keyword, is_stem)
CLINICAL_KEYWORDS = [
    ("medication", False), ("drug", False), ("dose", False), ("dosage", False),
    ("dosing", False), ("allerg", True), ("transfusion", False),
    ("crossmatch", False), ("cross-match", False), ("cross match", False),
    ("donor", False), ("blood component", False), ("blood bank", False),
    ("bloodbank", False), ("blood request", False), ("lis", False),
    ("pathology", False), ("radiology", False), ("report release", False),
    ("theatre", False), ("ot", False), ("otims", False), ("otms", False),
    ("surger", True), ("anesthes", True), ("anaesthes", True),
    ("icu", False), ("vital", False), ("mews", False),
    ("nursing chart", False), ("nursing order", False), ("diagnos", True),
    ("prescri", True), ("chemo", True), ("dialysis", False),
    ("emergency", False), ("discharge summary", False), ("clinical note", False),
    ("vaccin", True), ("immuniz", True), ("immunis", True), ("infection", False),
    ("isolation", False), ("code blue", False), ("resuscitat", True),
    ("consult", True), ("encounter", False), ("referral", False),
    ("triage", False), ("bed transfer", False), ("transfer of care", False),
    ("mortality", False), ("autopsy", False), ("morgue", False), ("ivf", False),
    ("physiotherapy", False), ("hemodialysis", False), ("consent", False),
    ("clinical", False), ("nursing", False), ("patient order", False),
    ("obstetric", True), ("delivery", False), ("partograph", False),
    ("lab result", False), ("specimen", False), ("sample collection", False),
    ("checklist", False), ("implant", False), ("operative note", False),
    ("discharge note", False), ("mlc", False), ("medico", True),
    ("due for discharge", False),
]
BILLING_KEYWORDS = [
    ("bill", True), ("invoice", False), ("payment", False), ("charge", False),
    ("tariff", False), ("insurance", False), ("tpa", False), ("discount", False),
    ("refund", False), ("package", True), ("estimate", True), ("ledger", False),
    ("revenue", False), ("fund", True), ("receipt", False), ("credit", True),
    ("advance", False), ("deposit", True), ("corporate", False),
    ("collection", False), ("deduction", False), ("promotion", False),
    ("loyalty", False), ("mpesa", False), ("currency", False), ("price", True),
    ("rate", False), ("doctor share", False), ("commission", False),
    ("cashier", False), ("cash", True), ("subscription", False), ("pay", True),
    ("sales return", False), ("indent", False), ("purchase", False),
    ("grn", False), ("stock", False), ("reconcil", True), ("due-deposit", False),
    ("due deposit", False), ("debit", False), ("cheque", False), ("card", False),
    ("emi", False), ("gst", False), ("tax", True), ("copay", False),
    ("co-pay", False), ("wallet", False), ("voucher", False),
]

MODULE_CLINICAL_DEFAULTS = {
    "Blood Bank", "OT/Theatre", "ICU", "Nursing", "Lab/LIS", "Radiology",
    "Clinical - IVF", "Clinical - Chemotherapy", "Clinical - Hemodialysis",
    "Clinical - Physiotherapy", "Clinical Workstation", "Clinical - Doctor Desk",
    "Clinical - Diagnostic Procedure", "Clinical - Global Master",
    "Clinical - Encounter Master", "ADT - Admission/Discharge", "Morgue",
    "Quality - Incident Reporting", "CSSD", "Diet & Nutrition",
}
MODULE_BILLING_DEFAULTS = {
    "Billing", "Billing - Fund Management", "Billing - Doctor Share",
    "Billing - Promotions/Loyalty", "Billing - Global Master", "Procurement",
    "Procurement - Inventory", "Pharmacy",
}

FORM_FIELD_RE = re.compile(r"<(input|select|textarea)\b", re.IGNORECASE)


def classify_module(template_url):
    if not template_url:
        return "Unknown (no templateUrl / multi-view state)"
    parts = template_url.split("/")
    if len(parts) < 2 or parts[0] != "views":
        return "Unknown (non-standard templateUrl)"
    p1 = parts[1] if len(parts) > 1 else ""
    p2 = parts[2] if len(parts) > 2 else ""
    p3 = parts[3] if len(parts) > 3 else ""

    if p1 == "master":
        if p2 == "UnitMaster":
            m = {
                "billingMaster": "Billing", "CSSD": "CSSD",
                "organizationMaster": "Admin - Org/Unit Master",
                "assetManagement": "Asset Management", "bloodBank": "Blood Bank",
                "userManagement": "Admin - User Management",
                "ambulanceService": "ADT - Ambulance",
                "fundManagement": "Billing - Fund Management",
                "Scheduling": "Appointment Scheduling",
                "scheduleBlockUnblock": "Appointment Scheduling",
                "Mortuary": "Morgue", "Visit Type": "ADT - Registration",
                "pharmacy": "Pharmacy", "Inventory": "Procurement - Inventory",
                "configuration": "Admin - Configuration",
                "UHIDMerge": "ADT - Registration",
            }
            return m.get(p3, f"Admin - Unit Master ({p3 or 'other'})")
        if p2 == "GlobalMaster":
            m = {
                "OTIMS": "OT/Theatre", "OTMS": "OT/Theatre",
                "DietNutrition": "Diet & Nutrition",
                "Encounter": "Clinical - Encounter Master",
                "Adt": "ADT - Registration",
                "EquipmentMaster": "Asset Management",
                "AssetManagenent": "Asset Management",
                "PromotionConfiguration": "Billing - Promotions/Loyalty",
            }
            if p3 in m:
                return m[p3]
            fn = p3.lower()
            if any(k in fn for k in ["blood-component", "transfusion"]):
                return "Blood Bank"
            if any(k in fn for k in ["allergy", "dose-master", "vaccination", "immunization", "diagnosis", "surgery", "examination", "nursing-order"]):
                return "Clinical - Global Master"
            if any(k in fn for k in ["tariff", "insurance", "tpa", "corporate-master", "currency", "loyalty", "mpesa", "due-deposit"]):
                return "Billing - Global Master"
            if any(k in fn for k in ["employee", "doctor-category", "designation", "qualification", "degree", "user"]):
                return "Admin - HR/Staff Master"
            return "Admin - Global Master (general)"
        specific = {
            "LIS": "Lab/LIS", "bloodBankDonor": "Blood Bank",
            "TheatreManagement": "OT/Theatre", "DoctorShare": "Billing - Doctor Share",
            "RadiologyWorkbench": "Radiology", "QMSMaster": "Admin - Queue Management",
            "MPShah Data": "Reports - Custom (MPShah)",
            "diagnosticProcedure": "Clinical - Diagnostic Procedure",
            "Chemotheraphy": "Clinical - Chemotherapy",
            "hemodialysis": "Clinical - Hemodialysis",
            "PhysiotherapyWorkbench": "Clinical - Physiotherapy",
        }
        return specific.get(p2, "Admin - Master (other)")

    m2 = {
        "LIS": "Lab/LIS", "common": "Common/Shared",
        "AdmissionDischarge": "ADT - Admission/Discharge", "Nursing": "Nursing",
        "procurement": "Procurement", "PharmacyInventory": "Pharmacy",
        "Linen": "Linen & Laundry", "OPD": "OPD",
        "bedManagement": "ADT - Bed Management",
        "AppointmentScheduling": "Appointment Scheduling", "ICU": "ICU",
        "Admission": "ADT - Admission", "IVF": "Clinical - IVF", "Morgue": "Morgue",
        "clinicalWorkStation": "Clinical Workstation", "Transfer": "ADT - Transfer",
        "doctorDesk": "Clinical - Doctor Desk",
        "incidentReporting": "Quality - Incident Reporting",
        "OtherApps": "Other Apps",
        "procedureConfiguration": "Admin - Procedure Configuration",
        "template": "Common - Template",
    }
    if p1 in m2:
        return m2[p1]
    fn = p1.lower()
    if "login" in fn or "reset-password" in fn or "access-" in fn or "dashboard" in fn:
        return "Common/Shared"
    if "transfer-care" in fn or "inter-consult" in fn or "discharge" in fn:
        return "ADT - Admission/Discharge"
    if "blood-request" in fn:
        return "Blood Bank"
    if "book-view" in fn:
        return "Appointment Scheduling"
    return f"Unclassified (views/{p1})"


_kw_cache = {}


def keyword_hits(text, spec):
    text_l = text.lower()
    hits = []
    for kw, is_stem in spec:
        cache_key = (kw, is_stem)
        pat = _kw_cache.get(cache_key)
        if pat is None:
            pat = re.compile(r"\b" + re.escape(kw)) if is_stem else re.compile(r"\b" + re.escape(kw) + r"s?\b")
            _kw_cache[cache_key] = pat
        if pat.search(text_l):
            hits.append(kw)
    return sorted(set(hits))


def tag_risk(state, module):
    haystack = " ".join(filter(None, [
        state.get("name") or "", state.get("title") or "",
        state.get("url") or "", state.get("templateUrl") or "",
    ]))
    clin_hits = keyword_hits(haystack, CLINICAL_KEYWORDS)
    bill_hits = keyword_hits(haystack, BILLING_KEYWORDS)

    if clin_hits:
        clinical = ("yes", f"keyword match: {', '.join(clin_hits[:4])}")
    elif module in MODULE_CLINICAL_DEFAULTS:
        clinical = ("yes", f"module default: {module}")
    elif module.startswith("Unknown") or module.startswith("Unclassified"):
        clinical = ("unclear", "module/state not statically classifiable")
    else:
        clinical = ("no", "no clinical keyword/module signal found")

    if bill_hits:
        billing = ("yes", f"keyword match: {', '.join(bill_hits[:4])}")
    elif module in MODULE_BILLING_DEFAULTS:
        billing = ("yes", f"module default: {module}")
    elif module.startswith("Unknown") or module.startswith("Unclassified"):
        billing = ("unclear", "module/state not statically classifiable")
    else:
        billing = ("no", "no billing keyword/module signal found")

    return clinical, billing


def main():
    states = json.loads(STATES_JSON.read_text())
    controller_index = json.loads(CONTROLLER_INDEX_JSON.read_text())
    controller_stats = json.loads(CONTROLLER_STATS_JSON.read_text())
    ctrl_usage = Counter(s["controller"] for s in states if s["controller"])

    view_cache = {}

    def analyze_view(template_url):
        if not template_url:
            return None
        if template_url in view_cache:
            return view_cache[template_url]
        fpath = WEBAPP / template_url
        if not fpath.is_file():
            view_cache[template_url] = {"exists": False, "fields": None}
            return view_cache[template_url]
        try:
            content = fpath.read_text(encoding="utf-8", errors="replace")
            view_cache[template_url] = {"exists": True, "fields": len(FORM_FIELD_RE.findall(content))}
        except OSError:
            view_cache[template_url] = {"exists": True, "fields": None}
        return view_cache[template_url]

    def resolve_controller(name):
        if not name:
            return {"status": "no-controller", "files": [], "shared": False, "usage": 0}
        usage = ctrl_usage.get(name, 0)
        files = controller_index.get(name, [])
        shared = usage >= SHARED_CONTROLLER_THRESHOLD
        if not files:
            return {"status": "unresolved", "files": [], "shared": shared, "usage": usage}
        return {"status": "resolved", "files": files, "shared": shared, "usage": usage}

    for s in states:
        s["module"] = classify_module(s["templateUrl"])
        clinical, billing = tag_risk(s, s["module"])
        s["clinical_risk"], s["clinical_reason"] = clinical
        s["billing_risk"], s["billing_reason"] = billing

        v = analyze_view(s["templateUrl"])
        s["view_exists"] = v["exists"] if v else None
        s["form_fields"] = v["fields"] if v else None

        c = resolve_controller(s["controller"])
        s["ctrl_status"] = c["status"]
        s["ctrl_files"] = c["files"]
        s["ctrl_shared"] = c["shared"]
        s["ctrl_usage"] = c["usage"]

        if c["status"] == "resolved" and len(c["files"]) == 1 and not c["shared"]:
            stats = controller_stats.get(c["files"][0])
            s["api_calls"] = stats["api_calls"] if stats else None
            s["broadcasts"] = stats["broadcasts"] if stats else []
            s["listens"] = stats["listens"] if stats else []
            s["rootscope_writes"] = stats["rootscope_writes"] if stats else None
        elif c["status"] == "resolved" and c["shared"]:
            agg_api, agg_bc, agg_on, agg_rw = 0, set(), set(), 0
            for f in c["files"]:
                st = controller_stats.get(f)
                if st:
                    agg_api += st["api_calls"]
                    agg_bc.update(st["broadcasts"])
                    agg_on.update(st["listens"])
                    agg_rw += st["rootscope_writes"]
            s["api_calls"] = agg_api
            s["broadcasts"] = sorted(agg_bc)
            s["listens"] = sorted(agg_on)
            s["rootscope_writes"] = agg_rw
        else:
            # Unresolved, ambiguous (multiple files registering the same
            # name), or no controller at all -- don't attribute stats to an
            # arbitrarily chosen file.
            s["api_calls"] = None
            s["broadcasts"] = []
            s["listens"] = []
            s["rootscope_writes"] = None

    ensure_data_dir()
    STATES_ENRICHED_JSON.write_text(json.dumps(states, indent=1))

    mod_counts = Counter(s["module"] for s in states)

    def tier(s):
        c, b = s["clinical_risk"], s["billing_risk"]
        if c == "yes" and b == "yes":
            return "both"
        if c == "unclear" or b == "unclear":
            return "unclear"
        if c == "yes":
            return "clinical-only"
        if b == "yes":
            return "billing-only"
        return "low-risk"

    tier_counts = Counter(tier(s) for s in states)
    print(f"Total states: {len(states)}", file=sys.stderr)
    print(f"Modules: {len(mod_counts)}", file=sys.stderr)
    print(f"Risk tiers: {dict(tier_counts)}", file=sys.stderr)
    print(f"Unresolved controllers: {sum(1 for s in states if s['ctrl_status'] == 'unresolved')}", file=sys.stderr)
    print(f"-> {STATES_ENRICHED_JSON}", file=sys.stderr)


if __name__ == "__main__":
    main()
