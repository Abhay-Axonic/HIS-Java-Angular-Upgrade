#!/usr/bin/env python3
"""
Parses every live `.state('name', { ... })` call out of the AngularJS
ui-router config (app.js) and writes tools/angular-audit/data/states.json.

Why not a simple regex/grep: app.js embeds nested objects (resolve blocks,
params, views) inside each .state() call, and mixes real code with
commented-out state definitions (both /* block */ and full-line // style).
A regex alone either over-matches into a neighboring state's fields or
mis-parses commented-out states as live ones. This does a balanced-paren
scan instead, after stripping comments first.

Re-run any time app.js changes -- this is meant to be a living tool, not a
one-off snapshot script.
"""
import json
import re
import sys

from common import APP_JS, STATES_JSON, ensure_data_dir


def strip_comments(text):
    text = re.sub(r"/\*.*?\*/", "", text, flags=re.S)
    lines = text.split("\n")
    lines = [l for l in lines if not re.match(r"^\s*//", l)]
    return "\n".join(lines)


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

        line_no = text.count("\n", 0, m.start()) + 1

        results.append({
            "name": state_name,
            "url": extract_field("url"),
            "templateUrl": extract_field("templateUrl"),
            "controller": extract_field("controller"),
            "title": extract_field("title"),
            "has_resolve": "resolve" in call_args,
            "has_views": bool(re.search(r"\bviews\s*:", call_args)),
            "line": line_no,
        })
    return results


def main():
    if not APP_JS.is_file():
        print(f"ERROR: app.js not found at {APP_JS}", file=sys.stderr)
        sys.exit(1)

    raw = APP_JS.read_text(encoding="utf-8", errors="replace")
    clean = strip_comments(raw)
    states = parse_states(clean)

    names = [s["name"] for s in states if s["name"]]
    dupes = {n for n in names if names.count(n) > 1}
    if dupes:
        print(f"WARNING: {len(dupes)} duplicate state names found: {sorted(dupes)[:10]}", file=sys.stderr)

    ensure_data_dir()
    STATES_JSON.write_text(json.dumps(states, indent=1))
    print(f"Parsed {len(states)} live states -> {STATES_JSON}", file=sys.stderr)


if __name__ == "__main__":
    main()
