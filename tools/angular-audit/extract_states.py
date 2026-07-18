#!/usr/bin/env python3
"""
Parses every live `.state('name', { ... })` call in the app and writes
tools/angular-audit/data/states.json.

Why not a simple regex/grep: state definitions embed nested objects
(resolve blocks, params, views) inside each .state() call, and mix real
code with commented-out state definitions (both /* block */ and full-line
// style). A regex alone either over-matches into a neighboring state's
fields or mis-parses commented-out states as live ones. This does a
balanced-paren scan instead, after stripping comments first.

Extended 2026-07-18 to close a scope gap found during the Phase 2 pilot
(docs/ANGULAR_PENDING_FINDINGS.md entry 3): app.js's central ui-router
config is not the only place `.state(...)` gets registered. 465 other
files self-register their own states from their own
`.config($stateProvider)` blocks (the same pattern found in
addNewPOController.js's `addItemPO` state) -- confirmed by walking the
whole webapp tree and cross-referencing every candidate file against
index.html's actual, uncommented `<script src="...">` tags, so states
defined by a file that's never loaded (dead code, or a commented-out
script tag) don't get counted as live. State names found this way are
expected to be purely additive against app.js's list (confirmed
manually before this was automated) -- but this script doesn't assume
that going forward: any name collision (with app.js, or between two
separately-loaded external files) is written to shadowed_states.json
and excluded from the live count rather than silently resolved by
picking a winner.

Re-run any time app.js or any controller file's routing changes -- this
is meant to be a living tool, not a one-off snapshot script.
"""
import json
import os
import re
import sys
from pathlib import Path

from common import (
    APP_JS, INDEX_HTML, WEBAPP, STATES_JSON, DEAD_STATES_JSON,
    SHADOWED_STATES_JSON, VENDOR_DIR_NAMES, VENDOR_FILE_PATHS,
    MERGE_ARTIFACT_MARKERS, ensure_data_dir,
)


def strip_js_comments(text):
    text = re.sub(r"/\*.*?\*/", "", text, flags=re.S)
    lines = text.split("\n")
    lines = [l for l in lines if not re.match(r"^\s*//", l)]
    return "\n".join(lines)


def strip_html_comments(text):
    return re.sub(r"<!--.*?-->", "", text, flags=re.S)


def parse_states(text):
    results = []
    n = len(text)
    for m in re.finditer(r"\.state\(", text):
        start = m.end()
        depth = 1
        i = start
        in_str = False
        str_char = ""
        while i < n and depth > 0:
            c = text[i]
            if in_str:
                if c == "\\":
                    i += 1
                elif c == str_char:
                    in_str = False
            else:
                if c in ("'", '"'):
                    in_str = True
                    str_char = c
                elif c == "(":
                    depth += 1
                elif c == ")":
                    depth -= 1
            i += 1
        call_args = text[start:i - 1]

        name_m = re.match(r"\s*['\"]([^'\"]*)['\"]", call_args)
        state_name = name_m.group(1) if name_m else None

        def extract_field(field):
            fm = re.search(field + r"\s*:\s*['\"]([^'\"]*)['\"]", call_args)
            return fm.group(1) if fm else None

        if state_name is None:
            # A `.state(` call whose first argument isn't a literal string
            # can't be a real `$stateProvider.state('name', {...})`
            # registration -- it's some other library's unrelated .state()
            # method (jQuery UI widgets, Kendo UI, etc. all have their own).
            # Confirmed 2026-07-18 while extending this scanner to the whole
            # tree: every case found was a bundled vendor file, now also
            # excluded by path in common.VENDOR_FILE_PATHS -- this check
            # stays as a second, source-agnostic line of defense rather
            # than relying solely on an exhaustive vendor-path list.
            continue

        line_no = text.count("\n", 0, m.start()) + 1

        results.append({
            "name": state_name,
            "url": extract_field("url"),
            "templateUrl": extract_field("templateUrl"),
            "controller": extract_field("controller"),
            "controllerAs": extract_field("controllerAs"),
            "title": extract_field("title"),
            "has_resolve": "resolve" in call_args,
            "has_views": bool(re.search(r"\bviews\s*:", call_args)),
            "line": line_no,
        })
    return results


def live_script_paths():
    """Every <script src="..."> path in index.html that isn't inside an
    HTML comment, with cache-busting query strings stripped (most tags
    carry a `?version=x.xx` suffix that would otherwise defeat a direct
    relpath comparison) and external (http(s)/protocol-relative) URLs
    excluded. Returned paths are relative to WEBAPP, matching the same
    convention build_controller_index.py already uses for controller
    file paths, so direct set membership works."""
    raw = INDEX_HTML.read_text(encoding="utf-8", errors="replace")
    clean = strip_html_comments(raw)
    srcs = re.findall(r'<script[^>]+src=["\']([^"\']+)["\']', clean)
    live = set()
    for s in srcs:
        if s.startswith(("http://", "https://", "//")):
            continue
        live.add(s.split("?", 1)[0])
    return live


def scan_file_for_states(fpath, relpath):
    try:
        raw = fpath.read_text(encoding="utf-8", errors="replace")
    except OSError:
        return []
    if ".state(" not in raw:
        return []
    clean = strip_js_comments(raw)
    states = parse_states(clean)
    for s in states:
        s["source_file"] = relpath
    return states


def main():
    if not APP_JS.is_file():
        print(f"ERROR: app.js not found at {APP_JS}", file=sys.stderr)
        sys.exit(1)
    if not INDEX_HTML.is_file():
        print(f"ERROR: index.html not found at {INDEX_HTML}", file=sys.stderr)
        sys.exit(1)

    live_scripts = live_script_paths()
    app_relpath = str(APP_JS.relative_to(WEBAPP))

    app_raw = APP_JS.read_text(encoding="utf-8", errors="replace")
    app_states = parse_states(strip_js_comments(app_raw))
    for s in app_states:
        s["source_file"] = app_relpath
    if app_relpath not in live_scripts:
        print(f"WARNING: app.js itself ({app_relpath}) not found among index.html's live <script> tags -- unexpected, check the query-string-stripping logic above.", file=sys.stderr)

    external_states = []
    files_with_states = set()
    for root, dirs, files in os.walk(WEBAPP):
        dirs[:] = [d for d in dirs if d not in VENDOR_DIR_NAMES]
        for fn in files:
            if not fn.endswith(".js"):
                continue
            fpath_str = os.path.join(root, fn)
            relpath = os.path.relpath(fpath_str, WEBAPP)
            if relpath == app_relpath or relpath in VENDOR_FILE_PATHS:
                continue
            if any(marker in fn for marker in MERGE_ARTIFACT_MARKERS):
                continue
            found = scan_file_for_states(Path(fpath_str), relpath)
            if found:
                files_with_states.add(relpath)
                external_states.extend(found)

    live_external = [s for s in external_states if s["source_file"] in live_scripts]
    dead_external = [s for s in external_states if s["source_file"] not in live_scripts]

    # Purely additive is the expectation (verified manually before this was
    # automated) -- but don't assume it silently going forward. Any name
    # collision with app.js is excluded from the live set and recorded,
    # not resolved by picking a winner.
    app_names = {s["name"] for s in app_states if s["name"]}
    shadowed = [s for s in live_external if s["name"] in app_names]
    live_external = [s for s in live_external if s["name"] not in app_names]

    # Same treatment for collisions between two separately-loaded external
    # files (both live, same state name) -- keep the first one encountered
    # (stable os.walk order) and flag the rest, rather than silently
    # letting AngularJS's own last-registration-wins behavior decide.
    seen_names = {}
    deduped_external = []
    for s in live_external:
        if s["name"] in seen_names:
            shadowed.append(s)
            continue
        seen_names[s["name"]] = s
        deduped_external.append(s)

    all_states = app_states + deduped_external

    names = [s["name"] for s in all_states if s["name"]]
    dupes = {n for n in names if names.count(n) > 1}
    if dupes:
        print(f"WARNING: {len(dupes)} duplicate state names found in the final merged set: {sorted(dupes)[:10]}", file=sys.stderr)

    ensure_data_dir()
    STATES_JSON.write_text(json.dumps(all_states, indent=1))
    DEAD_STATES_JSON.write_text(json.dumps(dead_external, indent=1))
    SHADOWED_STATES_JSON.write_text(json.dumps(shadowed, indent=1))

    print(f"app.js states: {len(app_states)}", file=sys.stderr)
    print(f"Other files with >=1 .state() call found: {len(files_with_states)}", file=sys.stderr)
    print(f"External live states (script tag loaded, no name collision): {len(deduped_external)}", file=sys.stderr)
    print(f"External dead states (defining file not in index.html's live <script> tags): {len(dead_external)} -> {DEAD_STATES_JSON}", file=sys.stderr)
    print(f"Shadowed states (name collision, excluded from live count): {len(shadowed)} -> {SHADOWED_STATES_JSON}", file=sys.stderr)
    print(f"TOTAL live states -> {STATES_JSON}: {len(all_states)}", file=sys.stderr)


if __name__ == "__main__":
    main()
