#!/usr/bin/env python3
"""
Second pass on top of cluster_screens.py: exact-signature clustering (22
clusters, mostly size 2-3, 1103/1195 dedicated screens with no structural
twin at all) is almost certainly too strict -- two genuine template
siblings can still diverge by a handful of endpoint calls (confirmed
concretely: scheduleWorklist vs. PMSchedule share 4 of their combined 17
distinct endpoints, Jaccard 0.235, and do NOT land in the same
exact-match cluster despite being real, already-audited siblings from the
same Asset Management family).

This pass groups dedicated-controller screens within the same coarse
bucket (controllerAs pattern + app.js-vs-external + same form-field
bucket) using Jaccard similarity on endpoint fingerprints, unioned via a
threshold, to surface "probably related, not necessarily identical"
candidate families -- still a STRUCTURAL hypothesis to spot-check, not a
behavioral claim. Two thresholds are run (0.5 and 0.3) to show how
cluster count/coverage is sensitive to how loose "related" is allowed to
be -- a single fixed threshold would hide that sensitivity.
"""
import json
from collections import defaultdict

from common import DATA_DIR, STATES_ENRICHED_JSON
from cluster_screens import extract_endpoint_fingerprint, form_field_bucket

FUZZY_REPORT_JSON = DATA_DIR / "cluster_report_fuzzy.json"


def jaccard(a, b):
    if not a and not b:
        return 0.0
    u = a | b
    if not u:
        return 0.0
    return len(a & b) / len(u)


class UnionFind:
    def __init__(self, items):
        self.parent = {i: i for i in items}

    def find(self, x):
        while self.parent[x] != x:
            x = self.parent[x]
        return x

    def union(self, a, b):
        ra, rb = self.find(a), self.find(b)
        if ra != rb:
            self.parent[ra] = rb


def cluster_at_threshold(dedicated, fingerprint_cache, threshold):
    buckets = defaultdict(list)
    for s in dedicated:
        files = tuple(sorted(s.get("ctrl_files", [])))
        if not files:
            continue
        key = (
            bool(s.get("controllerAs")),
            "app.js" if s.get("source_file", "").endswith("app.js") else "external",
            form_field_bucket(s.get("form_fields") or 0),
        )
        buckets[key].append(s)

    all_clusters = []
    for key, members in buckets.items():
        if len(members) < 2:
            continue
        uf = UnionFind(m["name"] for m in members)
        fps = {}
        for m in members:
            files = tuple(sorted(m.get("ctrl_files", [])))
            fp = frozenset()
            for f in files:
                if f not in fingerprint_cache:
                    fingerprint_cache[f] = extract_endpoint_fingerprint(f)
                fp = fp | fingerprint_cache[f]
            fps[m["name"]] = fp

        names = [m["name"] for m in members]
        for i in range(len(names)):
            for j in range(i + 1, len(names)):
                a, b = names[i], names[j]
                if fps[a] and fps[b] and jaccard(fps[a], fps[b]) >= threshold:
                    uf.union(a, b)

        groups = defaultdict(list)
        for m in members:
            groups[uf.find(m["name"])].append(m)
        for root, group_members in groups.items():
            if len(group_members) < 2:
                continue
            all_clusters.append({
                "form_field_bucket": key[2],
                "controllerAs_pattern": key[0],
                "registration": key[1],
                "screen_count": len(group_members),
                "screens": sorted(m["name"] for m in group_members),
                "clinical_risk_count": sum(1 for m in group_members if m.get("clinical_risk") == "yes"),
                "billing_risk_count": sum(1 for m in group_members if m.get("billing_risk") == "yes"),
                "modules": sorted({m.get("module") for m in group_members}),
            })
    all_clusters.sort(key=lambda c: -c["screen_count"])
    return all_clusters


def main():
    states = json.loads(STATES_ENRICHED_JSON.read_text())
    dedicated = [s for s in states if not s.get("ctrl_shared")]
    fingerprint_cache = {}

    report = {}
    for threshold in (0.5, 0.3):
        clusters = cluster_at_threshold(dedicated, fingerprint_cache, threshold)
        covered = sum(c["screen_count"] for c in clusters)
        report[str(threshold)] = {"clusters": clusters, "cluster_count": len(clusters), "screens_covered": covered}
        print(f"Threshold {threshold}: {len(clusters)} clusters, {covered}/{len(dedicated)} dedicated screens covered")
        for c in clusters[:12]:
            print(f"    {c['screen_count']:4d} screens | ff={c['form_field_bucket']:>6} | "
                  f"clinical={c['clinical_risk_count']:3d} billing={c['billing_risk_count']:3d} | "
                  f"modules={c['modules'][:3]} | e.g. {c['screens'][:3]}")
        print()

    FUZZY_REPORT_JSON.write_text(json.dumps(report, indent=2))
    print(f"Full report written to {FUZZY_REPORT_JSON}")


if __name__ == "__main__":
    main()
