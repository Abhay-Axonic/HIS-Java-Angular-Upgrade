#!/usr/bin/env python3
"""
preflight_screen.py — the five-check preflight (rules that execute).

Usage: preflight_screen.py <feature-folder> <route> [--appjs PATH] [--backend-root PATH]

C1 TEMPLATE MEMBER   — every template binding is backed by a declared member.
C2 DIRECTIVE IMPORTS — every directive/pipe/form-directive the template uses is
                       imported (CommonModule/NgIf/NgFor or the @if/@for control
                       flow; FormsModule for ngModel; RouterLink; pipes).
C3 MODULE ID         — the route's moduleId is not a placeholder (220 or any
                       unresolved value) and a permission row exists for the
                       admin role in the privilege tree.
C4 ENDPOINT HANDLERS — every endpoint the screen's service calls resolves to a
                       real @RequestMapping handler, by DIRECT grep of the
                       backend (NOT the parked deriver's matcher).
C5 LEGACY REACHABILITY — the legacy screen is registered in app.js (not
                       commented, not absent) and has a menu node. Per-screen
                       greps with the output shown.

Each check reports PASS / FAIL / UNVALIDATED separately. A check that cannot
run reports UNVALIDATED, never PASS.
"""
import argparse
import json
import os
import re
import subprocess
import sys

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DEFAULT_APPJS = f"{ROOT}/legacy/HIS-WAR/src/main/webapp/resources/assets/js/config/app.js"
DEFAULT_BACKEND = f"{ROOT}/legacy"

KNOWN_DEAD_MODULE_IDS = {220, 0, -1, 999999}


def norm(s):
    return re.sub(r"[^a-z0-9]", "", (s or "").lower())


def load_privilege_tree():
    try:
        r = subprocess.run(
            ["psql", "-h", "localhost", "-d", "his_local", "-tAc",
             "select modules from privilege.privilege where role_id=1 limit 1;"],
            capture_output=True, text=True, timeout=20)
        return json.loads(r.stdout.strip())
    except Exception:  # noqa: BLE001
        return None


def check_c1(folder):
    """Template member: every binding backed by a declared class member."""
    try:
        t = open(f"{folder}.html", encoding="utf-8").read()
        c = open(f"{folder}.ts", encoding="utf-8").read()
    except OSError:
        return 'UNVALIDATED', 'screen files not found'
    t = re.sub(r"<!--.*?-->", "", t, flags=re.S)
    t = re.sub(r"<[a-zA-Z][^>]*>", " ", t)
    # A call-like identifier is a real binding only inside {{ ... }}
    # interpolations and @if/@for/@switch(...) conditions — never in prose
    # (prose like "patient(s)" must not read as a call: the old whole-text
    # scan false-positived on it).
    exprs = set(re.findall(r"\{\{\s*([^{}]+?)\s*\}\}", t))
    exprs |= set(re.findall(r"@(?:if|for|else|switch|case|let)\s*\(([^)]*)\)", t))
    binds = set()
    for e in exprs:
        binds |= set(re.findall(r"(?<![\w.])([a-zA-Z_$][\w$]*)\s*\(", e))
    binds |= set(re.findall(r"\[ngModel\]=\"([a-zA-Z_$][\w$]*)\"", t))
    members = set(re.findall(r"(?:readonly\s+|private\s+readonly\s+)?([a-zA-Z_$][\w$]*)\s*=", c))
    members |= set(re.findall(r"\n\s*(?:private\s+)?([a-zA-Z_$][\w$]*)\s*\([^)]*\)\s*[:{]", c))
    skip = {"ngIf", "ngFor", "ngModel", "ngModelChange", "ngValue", "let", "of", "as",
            "else", "$any", "item", "hit", "b", "s", "r", "line", "i", "index",
            "record", "organizationId", "unitId"}
    missing = sorted(b for b in binds if b not in members and b not in skip)
    if missing:
        return 'FAIL', f'undeclared template bindings: {missing}'
    return 'PASS', 'every template binding is backed by a declared member'


def check_c2(folder):
    """Directive imports: what the template uses is in the component imports."""
    try:
        t = open(f"{folder}.html", encoding="utf-8").read()
        c = open(f"{folder}.ts", encoding="utf-8").read()
    except OSError:
        return 'UNVALIDATED', 'screen files not found'
    im = re.search(r"imports:\s*\[([^\]]*)\]", c)
    imports = im.group(1) if im else ""
    gaps = []
    if t.count("*ngIf") + t.count("*ngFor"):
        if "CommonModule" not in imports and "NgIf" not in imports and "NgFor" not in imports:
            gaps.append("CommonModule (template uses *ngIf/*ngFor)")
    if "[ngModel]" in t or "ngModelChange" in t:
        if "FormsModule" not in imports:
            gaps.append("FormsModule (template uses ngModel)")
    if "routerLink" in t.lower() and "RouterLink" not in imports:
        gaps.append("RouterLink (template uses routerLink)")
    for pipe in set(re.findall(r"\|\s*([a-zA-Z]+)", t)):
        if pipe in ("lineCount",):
            continue
        if not re.search(rf"import\s*{{[^}}]*{pipe}", c) and pipe not in imports:
            gaps.append(f"{pipe} pipe")
    if gaps:
        return 'FAIL', f'import gaps: {gaps}'
    return 'PASS', 'directive/pipe imports satisfy the template'


def check_c3(route, appjs_path):
    """Module id: not a placeholder, and a permission row exists.

    Reads the NEW app's routes (the moduleId lives there), not the legacy app.js.
    """
    routes_path = f"{ROOT}/new/src/app/app.routes.ts"
    try:
        routes = open(routes_path, encoding="utf-8").read()
    except OSError:
        return 'UNVALIDATED', 'new/src/app/app.routes.ts not found'
    m = re.search(
        rf"path:\s*'{route.lstrip('/')}'\s*,[^}}]*?moduleId:\s*(\d+)",
        routes, re.S)
    if not m:
        m = re.search(
            rf"path:\s*'{route.lstrip('/')}'[^}}]*?data:\s*\{{[^}}]*?moduleId:\s*(\d+)",
            routes, re.S)
    if not m:
        return 'UNVALIDATED', f"route '{route}' not found in app.routes.ts (check the route path)"
    module_id = int(m.group(1))
    if module_id in KNOWN_DEAD_MODULE_IDS:
        return 'FAIL', f'moduleId {module_id} is a known placeholder'
    tree = load_privilege_tree()
    if tree is None:
        return 'UNVALIDATED', 'privilege tree unreadable (psql)'
    found = any(n.get('id') == module_id for n in tree)
    if not found:
        return 'FAIL', f'moduleId {module_id} has no row in the privilege tree'
    return 'PASS', f'moduleId {module_id} present in the privilege tree'


def check_c4(folder, backend_root):
    """Endpoint handlers: every service URL resolves to a @RequestMapping."""
    try:
        svc = open(f"{folder}.service.ts", encoding="utf-8").read()
    except OSError:
        svc = None
    if svc is None:
        # the service may be inlined in the component or named differently
        candidates = [f"{folder}.service.ts"]
        return 'UNVALIDATED', f'no service file ({candidates[0]})'
    urls = set(re.findall(r"(?:get|post|put|patch|delete)\([^'\"]*['\"]([^'\"]+)['\"]", svc))
    urls |= set(re.findall(r"`([^`]*\$\{[^`]*\}[^`]*)`", svc))
    if not urls:
        return 'UNVALIDATED', 'no endpoint URLs found in the service'
    # the backend grep index: all the @RequestMapping values (class + method)
    r = subprocess.run(
        ["grep", "-rhno", "@RequestMapping[^)]*)", backend_root, "--include=*.java"],
        capture_output=True, text=True, timeout=120)
    mappings = r.stdout
    missing = []
    for u in sorted(urls):
        tail = re.sub(r"^https?://[^/]+", "", u)
        tail = re.sub(r"\$\{[^}]+\}", "{id}", tail)
        probe = tail.split('?')[0]
        if probe not in mappings and probe.split('/')[-1] not in mappings:
            missing.append(probe)
    if missing:
        return 'FAIL', f'endpoints with no handler: {missing}'
    return 'PASS', f'all {len(urls)} endpoint(s) resolve to @RequestMapping handlers'


def check_c5(folder, appjs_path, screen_name):
    """Legacy reachability: the STATE NAME registered in app.js + a menu node
    (hand greps).

    Matches on the STATE NAME from the route (`.state('cancellationMaster'`),
    NOT the rebuild's template filename. QF-2026-08-10-Y (owner directive,
    pre-screen-19): the old check matched the rebuild template filename
    (e.g. cancellation-master.html) against app.js, but the legacy state's
    templateUrl is the LEGACY filename (cancellation-reason.html) — a live
    screen whose template filename differs reported a FALSE DEAD. The
    dangerous direction: a false DEAD invites walking past a live screen
    (the 477 lesson). The state name is the route's own token — the
    actionLink and the state url both target it.
    """
    try:
        appjs = open(appjs_path, encoding="utf-8").read()
    except OSError:
        return 'UNVALIDATED', 'app.js not found'
    token = f".state('{screen_name}'"
    hits = [m.start() for m in re.finditer(re.escape(token), appjs)]
    if not hits:
        return 'DEAD', f"state '{screen_name}' ABSENT from app.js (no state registration)"
    idx = hits[0]
    before = appjs[max(0, idx - 400):idx]
    commented = before.rfind("/*") > before.rfind("*/")
    if commented:
        return 'DEAD', f"state '{screen_name}' found at app.js:{idx} but inside a COMMENTED region"
    tree = load_privilege_tree()
    if tree is None:
        return 'UNVALIDATED', 'privilege tree unreadable'
    name_norm = norm(screen_name)
    route_token = screen_name.lower()
    # The EXACT token first: a node's actionLink maps node -> route
    # ("#/combinationMedicationMaster" -> the state url "/combinationMedicationMaster").
    # Display-name matching is the heuristic class the parked reachability gate
    # failed on (QF-2026-08-08-P) — never trust it alone.
    node = next((n for n in tree
                 if (n.get('actionLink') or '').replace('#', '').strip('/').lower() == route_token),
                None)
    if node is None:
        node = next((n for n in tree if norm(n.get('name')) == name_norm), None)
    if node is None:
        return 'UNVALIDATED', ('registered in app.js but NO menu node resolves to this route: '
                               'actionLink mismatch (e.g. node 466 actionLink '
                               '"#/unitofMeasurement" while the route is /uomMaster — the '
                               'MENU-BROKEN shape) and the display-name match failed too. '
                               'HAND-CONFIRMATION REQUIRED (the two greps, RULES.md) — '
                               'UNVALIDATED is NOT a pass')
    chain = []
    cur, par = node.get('id'), node.get('parentId')
    for _ in range(6):
        n = next((x for x in tree if x.get('id') == cur and (par is None or x.get('parentId') == par)), None)
        if not n:
            break
        chain.append(f"{cur}('{(n.get('name') or '').strip()}')")
        cur, par = n.get('parentId'), None
        if cur == 0:
            break
    return 'PASS', f"registered (app.js:{idx}) + menu node {node.get('id')} chain: {' -> '.join(chain)}"


def _conflict_markers():
    """The four git conflict-marker patterns, built without literal occurrences
    so the scan does not flag its own source file."""
    return ('<' * 7, '>' * 7, 'Updated ' + 'upstream', 'Stashed ' + 'changes')


def check_c6():
    """C6 COMMITTED MARKERS — any git conflict marker in any committed
    .md/.py/.json/.yaml/.yml/.sh under docs/ or tools/ is an immediate FAIL.
    Reads the COMMITTED blobs at HEAD (git ls-tree + git show), so a marker
    that was committed (the DECISIONS_QUEUE stash markers, origin 63f759e4)
    is caught even if the worktree was edited around it."""
    bad = []
    markers = _conflict_markers()
    try:
        files = subprocess.run(
            ['git', 'ls-tree', '-r', '--name-only', 'HEAD', '--', 'docs', 'tools'],
            capture_output=True, text=True, cwd=ROOT).stdout.splitlines()
    except Exception as ex:  # noqa: BLE001
        return 'FAIL', f'scan error: {ex}'
    for f in files:
        if not f.endswith(('.md', '.py', '.json', '.yaml', '.yml', '.sh')):
            continue
        blob = subprocess.run(['git', 'show', f'HEAD:{f}'],
                              capture_output=True, text=True, cwd=ROOT).stdout
        for m in markers:
            if m in blob:
                bad.append(f'{f} ({m})')
                break
    if bad:
        shown = '; '.join(bad[:5]) + (' …' if len(bad) > 5 else '')
        return 'FAIL', f'{len(bad)} committed file(s) carry conflict markers: {shown}'
    return 'PASS', f'{len(files)} committed file(s) scanned, 0 conflict markers'


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument('feature')           # e.g. add-new-drug-recall
    ap.add_argument('route')             # e.g. /addNewDrugRecall
    ap.add_argument('--appjs', default=DEFAULT_APPJS)
    ap.add_argument('--backend-root', default=DEFAULT_BACKEND)
    args = ap.parse_args()
    folder = f"{ROOT}/new/src/app/features/{args.feature}/{args.feature}"

    results = [
        ('C1 TEMPLATE MEMBER', check_c1(folder)),
        ('C2 DIRECTIVE IMPORTS', check_c2(folder)),
        ('C3 MODULE ID', check_c3(args.route, args.appjs)),
        ('C4 ENDPOINT HANDLERS', check_c4(folder, args.backend_root)),
        ('C5 LEGACY REACHABILITY', check_c5(folder, args.appjs, args.route)),
        ('C6 COMMITTED MARKERS', check_c6()),
    ]
    any_fail = False
    dead_flag = False
    unvalidated = 0
    for name, (status, detail) in results:
        print(f'{name}: {status} — {detail}')
        if status == 'FAIL':
            any_fail = True
        if status == 'DEAD':
            dead_flag = True
        if status == 'UNVALIDATED':
            unvalidated += 1
    if unvalidated:
        print(f'NOTE: {unvalidated} check(s) returned UNVALIDATED — that is NOT a pass. '
              'The reachability hand-confirmation (the two greps) is REQUIRED and must '
              'appear in the screen report (RULES.md).')
    if dead_flag:
        if any_fail:
            # a FAIL alongside the DEAD flag must NOT print "passes"
            print('RESULT: FAIL — C5 reports the screen is unreachable in the '
                  'legacy app (a flag, not blocking), but another check FAILED')
        else:
            # C5 DEAD is a FLAG, not a blocking FAIL: whether to rebuild a
            # legacy-unreachable screen is the owner's decision (Q16 / the
            # resurrection pages), not the preflight's. State it prominently.
            print('RESULT: passes with DEAD-legacy flag — C5 reports the screen is '
                  'unreachable in the legacy app; this does NOT block the rebuild, '
                  'but it must be surfaced to the owner before shipping.')
    else:
        if unvalidated and not any_fail:
            print(f'RESULT: PASS with {unvalidated} UNVALIDATED check(s) — see the NOTE above; '
                  'hand-confirmation is required, this is NOT a clean pass')
        else:
            print(f'RESULT: {"FAIL" if any_fail else "PASS"}')
    sys.exit(1 if any_fail else 0)


if __name__ == '__main__':
    main()
