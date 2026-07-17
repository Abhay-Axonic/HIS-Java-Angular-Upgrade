#!/usr/bin/env python3
"""
Scans the whole webapp tree for `.controller('Name', [...])` registrations
and per-file complexity/coupling signals, and writes:
  - data/controller_index.json  (controller name -> [file paths that register it])
  - data/controller_stats.json  (file path -> api-call / broadcast / on / rootScope stats)

Corrected 2026-07-17 after an owner-requested investigation of 2 flagged
"unresolved controller" findings (roleManagement, userAuthentication) turned
out to be false alarms: the original Phase 0 scan only searched
resources/assets/js/controller/, missing sibling directories
(resources/assets/accessControl/controller/, resources/assets/js/common/)
that also register real app controllers, and its regex excluded '.' from
controller names (missing dotted names like "OtBillingDetailsComp.Controller").
This version scans the whole tree and allows '.' in names. See
docs/ANGULAR_PHASE0_SCREEN_INVENTORY.md, "Known gaps", for the full story.

Re-run any time controller files change.
"""
import json
import os
import re
import sys

from common import (
    WEBAPP, CONTROLLER_INDEX_JSON, CONTROLLER_STATS_JSON, ensure_data_dir,
    VENDOR_DIR_NAMES, VENDOR_FILE_PATHS, MERGE_ARTIFACT_MARKERS,
)

REG_PATTERN = re.compile(r"\.controller\(\s*['\"]([A-Za-z0-9_.]+)['\"]")
API_CALL_PATTERN = re.compile(
    r"\b(GenericService|BillingGenericService|GenericServiceParamHeader)\s*\.\s*[A-Za-z0-9_]+\s*\("
    r"|\$http\s*\.\s*(get|post|put|delete|jsonp)\s*\("
)
BROADCAST_PATTERN = re.compile(r"\$rootScope\s*\.\s*\$broadcast\s*\(\s*['\"]([^'\"]+)['\"]")
ON_PATTERN = re.compile(r"\$(?:rootScope|scope)\s*\.\s*\$on\s*\(\s*['\"]([^'\"]+)['\"]")
ROOTSCOPE_WRITE_PATTERN = re.compile(r"\$rootScope\s*\.\s*[A-Za-z0-9_]+\s*=")


def main():
    controller_index = {}
    controller_stats = {}
    merge_artifacts_found = []

    for root, dirs, files in os.walk(WEBAPP):
        dirs[:] = [d for d in dirs if d not in VENDOR_DIR_NAMES]
        for fn in files:
            if not fn.endswith(".js"):
                continue
            fpath = os.path.join(root, fn)
            relpath = os.path.relpath(fpath, WEBAPP)
            if relpath in VENDOR_FILE_PATHS:
                continue
            if any(marker in fn for marker in MERGE_ARTIFACT_MARKERS):
                merge_artifacts_found.append(relpath)
                continue
            try:
                with open(fpath, encoding="utf-8", errors="replace") as f:
                    content = f.read()
            except OSError:
                continue

            names = REG_PATTERN.findall(content)
            for nm in names:
                controller_index.setdefault(nm, []).append(relpath)

            api_calls = len(API_CALL_PATTERN.findall(content))
            broadcasts = sorted(set(BROADCAST_PATTERN.findall(content)))
            listens = sorted(set(ON_PATTERN.findall(content)))
            rootscope_writes = len(ROOTSCOPE_WRITE_PATTERN.findall(content))

            controller_stats[relpath] = {
                "names_registered": names,
                "api_calls": api_calls,
                "broadcasts": broadcasts,
                "listens": listens,
                "rootscope_writes": rootscope_writes,
                "lines": content.count("\n") + 1,
            }

    multi = {k: v for k, v in controller_index.items() if len(v) > 1}
    print(f"Unique controller names registered: {len(controller_index)}", file=sys.stderr)
    print(f"Registered in >1 file (genuine ambiguity, vendor/merge-artifact noise already excluded): {len(multi)}", file=sys.stderr)
    for k, v in multi.items():
        print(f"  {k}: {v}", file=sys.stderr)
    print(f"Merge-conflict-artifact files excluded: {len(merge_artifacts_found)}", file=sys.stderr)

    ensure_data_dir()
    CONTROLLER_INDEX_JSON.write_text(json.dumps(controller_index, indent=1))
    CONTROLLER_STATS_JSON.write_text(json.dumps(controller_stats, indent=1))
    print(f"-> {CONTROLLER_INDEX_JSON}", file=sys.stderr)
    print(f"-> {CONTROLLER_STATS_JSON}", file=sys.stderr)


if __name__ == "__main__":
    main()
