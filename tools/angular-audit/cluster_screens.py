#!/usr/bin/env python3
"""
Structural clustering of screens for the template-acceleration feasibility
analysis (read-only investigation, not part of the Phase 0/1/2 pipeline
that feeds docs/ANGULAR_PHASE0_SCREEN_INVENTORY.md -- this writes its own
separate data/cluster_report.json and does not touch the inventory doc).

Extends the existing tooling rather than duplicating it: reuses
states_enriched.json (already has controllerAs, source_file, ctrl_shared,
ctrl_files, form_fields, risk tags) and adds one new signal not
previously extracted -- a per-controller-file API "endpoint fingerprint"
(the set of literal URL path fragments and ALL_CAPS URL constants
referenced in GenericService/BillingGenericService/$http calls).

Clustering strategy, deliberately split into two categories that must
NOT be conflated (see docs/ANGULAR_PENDING_FINDINGS.md entry 3 and the
scheduleWorklist/PMSchedule divergence for why):

1. SHARED-CONTROLLER screens (ctrl_shared=true): multiple states literally
   run the same controller file. These are not "template derivatives" in
   the sense this analysis is testing -- they're one piece of code with
   internal runtime branching invisible to static analysis. Reported
   separately, not folded into the "core cluster" count.

2. DEDICATED-CONTROLLER screens (ctrl_shared=false): each has its own
   controller file. These are grouped by structural signature
   (controllerAs pattern, app.js-vs-external registration, form-field
   count, and the API endpoint fingerprint) -- genuine candidates for
   "screen B's controller looks like it was copy-pasted from screen A's".
   Structural similarity here is a HYPOTHESIS to spot-check, not a
   conclusion -- this script does not read behavior, only shape.
"""
import json
import re
from collections import defaultdict

from common import DATA_DIR, STATES_ENRICHED_JSON, WEBAPP, ensure_data_dir

CLUSTER_REPORT_JSON = DATA_DIR / "cluster_report.json"

URL_LITERAL_PATTERN = re.compile(r"""["'](/[A-Za-z0-9_/\-]{3,})["']""")
URL_CONST_PATTERN = re.compile(r"\b([A-Z][A-Z0-9_]{4,})\b")
# Words that are ALL_CAPS but are not URL constants (language/framework
# tokens, common shared globals unrelated to a specific endpoint).
CONST_STOPWORDS = {
    "METHOD_GET", "METHOD_POST", "METHOD_PUT", "METHOD_DELETE",
    "BASE_URL", "ROOT_URL", "NOTIFICATION_MSG_STATUS_FALSE",
    "NOTIFICATION_MSG_STATUS_TRUE", "SUCCESS_MSG", "ERROR_MSG",
    "WARNING_MSG", "NETWORK_ERROR_MSG", "USE", "STRICT",
}


def extract_endpoint_fingerprint(fpath):
    """Best-effort structural fingerprint: the literal URL path fragments
    and ALL_CAPS URL-constant identifiers a controller file references.
    Not a claim about exact runtime endpoints (constants can vary by
    build/config) -- a structural proxy, good enough for clustering
    candidates, not for asserting two screens call the identical backend."""
    try:
        text = (WEBAPP / fpath).read_text(errors="ignore")
    except (OSError, UnicodeDecodeError):
        return frozenset()
    literals = set(URL_LITERAL_PATTERN.findall(text))
    consts = {m for m in URL_CONST_PATTERN.findall(text) if m not in CONST_STOPWORDS}
    return frozenset(literals | consts)


def form_field_bucket(n):
    if n == 0:
        return "0"
    if n <= 5:
        return "1-5"
    if n <= 15:
        return "6-15"
    if n <= 30:
        return "16-30"
    return "31+"


def main():
    ensure_data_dir()
    states = json.loads(STATES_ENRICHED_JSON.read_text())

    shared = [s for s in states if s.get("ctrl_shared")]
    dedicated = [s for s in states if not s.get("ctrl_shared")]

    # -- Category 1: shared-controller clusters (reported, not treated as
    # template-derivative candidates) --
    shared_by_file = defaultdict(list)
    for s in shared:
        for f in s.get("ctrl_files", []):
            shared_by_file[f].append(s["name"])
    shared_clusters = [
        {"controller_file": f, "screen_count": len(names), "screens": sorted(names)}
        for f, names in sorted(shared_by_file.items(), key=lambda kv: -len(kv[1]))
    ]

    # -- Category 2: dedicated-controller structural clusters --
    fingerprint_cache = {}
    signature_groups = defaultdict(list)
    for s in dedicated:
        files = tuple(sorted(s.get("ctrl_files", [])))
        if not files:
            continue
        fps = []
        for f in files:
            if f not in fingerprint_cache:
                fingerprint_cache[f] = extract_endpoint_fingerprint(f)
            fps.append(fingerprint_cache[f])
        endpoint_fp = frozenset().union(*fps) if fps else frozenset()

        sig = (
            bool(s.get("controllerAs")),
            "app.js" if s.get("source_file", "").endswith("app.js") else "external",
            form_field_bucket(s.get("form_fields") or 0),
            endpoint_fp,
        )
        signature_groups[sig].append(s)

    # Only signatures with 2+ screens are "clusters" at all.
    clusters = []
    for sig, members in signature_groups.items():
        if len(members) < 2:
            continue
        controller_as, reg, ff_bucket, endpoint_fp = sig
        clusters.append({
            "controllerAs_pattern": controller_as,
            "registration": reg,
            "form_field_bucket": ff_bucket,
            "endpoint_fingerprint_size": len(endpoint_fp),
            "endpoint_fingerprint_sample": sorted(endpoint_fp)[:8],
            "screen_count": len(members),
            "screens": sorted(m["name"] for m in members),
            "clinical_risk_count": sum(1 for m in members if m.get("clinical_risk") == "yes"),
            "billing_risk_count": sum(1 for m in members if m.get("billing_risk") == "yes"),
            "modules": sorted({m.get("module") for m in members}),
        })
    clusters.sort(key=lambda c: -c["screen_count"])

    singletons = sum(1 for members in signature_groups.values() if len(members) == 1)
    no_ctrl_file = sum(1 for s in dedicated if not s.get("ctrl_files"))

    report = {
        "total_screens": len(states),
        "shared_controller_screens": len(shared),
        "dedicated_controller_screens": len(dedicated),
        "shared_controller_clusters": shared_clusters,
        "dedicated_structural_clusters": clusters,
        "dedicated_singleton_signatures": singletons,
        "dedicated_no_controller_file": no_ctrl_file,
    }
    CLUSTER_REPORT_JSON.write_text(json.dumps(report, indent=2))

    print(f"Total screens: {len(states)}")
    print(f"Shared-controller screens: {len(shared)} (across {len(shared_clusters)} controller files)")
    print(f"Dedicated-controller screens: {len(dedicated)}")
    print(f"  -> structural clusters (2+ screens, identical signature): {len(clusters)}")
    print(f"  -> singleton signatures (no structural match found): {singletons}")
    print(f"  -> no resolved controller file: {no_ctrl_file}")
    print()
    print("Top 15 dedicated-controller clusters by size:")
    for c in clusters[:15]:
        print(f"  {c['screen_count']:4d} screens | ff={c['form_field_bucket']:>6} | "
              f"clinical={c['clinical_risk_count']:3d} billing={c['billing_risk_count']:3d} | "
              f"modules={c['modules'][:3]}")
    print()
    print(f"Full report written to {CLUSTER_REPORT_JSON}")


if __name__ == "__main__":
    main()
