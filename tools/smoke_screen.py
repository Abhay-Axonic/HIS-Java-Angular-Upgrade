#!/usr/bin/env python3
"""
smoke_screen.py — the browser smoke harness (Playwright, FIREFOX only).

Usage: smoke_screen.py <route> [--port 4201] [--module-id N] [--cookie-json {...}] [selectors...]

Q21 REBUILD (2026-08-09) — what changed vs the old harness:
  * NO permissions seeding, ever. The old harness wrote localStorage['permissions']
    (the legacy login's full set) into the new origin, so the module-access guard
    ALWAYS passed and the smoke could not see a real bootstrap failure or denial
    (QF-2026-08-09-B). The app's own /privilege/role bootstrap now decides.
  * FAILS on: any redirect to /accessDenied (any reason — the reason is
    reported), a missing/empty permissions store, the route's moduleId absent
    from the store, any console error, any uncaught page error, any failed
    request, any non-2xx response, any XHR response that is HTML where JSON was
    expected, any missing expected selector.
  * ALWAYS prints the FINAL URL (the guard's redirect is the ground truth).

AUTHENTICATION — stated plainly: the harness does NOT submit the login form. It
injects the dev credential's real-login cookieObject cookie (captured by
get_dev_token.py from the actual legacy login at :8080, cached in
tools/.token_cache.json, gitignored) into the target origin via document.cookie.
That cookie is byte-identical to a real login's artifact (same host, same value,
carries authToken — the app's authToken getter reads the cookie first).
sessionStorage is deliberately left EMPTY, matching the owner-measured shape of
a real browser session at the new origin. The login form is not used because
the login-group rate limiter 429s after ~3-6 logins, which would make repeated
harness runs impractical; the injected cookie IS a real login artifact.

ENVIRONMENT REQUIREMENT: the dev server must be serving the branch whose
angular.json wires proxyConfig (the /privilege/** -> :8080 proxy). Without it,
/privilege/role returns the SPA-fallback HTML (200 text/html), the app's
bootstrap cannot fetch, and the harness correctly FAILS with
permissionsUnavailable — that failure is the harness working, not a bug.

Q32 TREE-INTEGRITY CHECKS (2026-08-10 — the third harness blindness: the
smoke passed against a branch tree while the 4201 serve was silently
compiling main, so the owner's click hit NG04002 on a route that did not
exist in the served tree). The smoke now FAILS loudly on all three:
  * BRANCH MISMATCH — before the browser even launches, the smoke resolves
    the ng-serve process for --port, reads its working directory, and
    compares `git branch --show-current` THERE against the branch under test
    (--branch, defaulting to the repo root's current branch). A mismatch
    (or an unverifiable serve cwd) is a FAIL: a PASS against the wrong tree
    is worse than a FAIL.
  * UNMATCHED ROUTE — the route MUST exist in new/src/app/app.routes.ts as
    read from the repo root; if route_module_id() returns None the smoke
    fails on "route absent" instead of silently skipping the store check.
  * EMPTY BODY — an empty rendered body is a FAIL independent of the branch
    check (already present; kept as its own reason).

Exit: 0 = PASS, 1 = FAIL, 2 = no credentials/cache and mint failed.
"""
import argparse
import json
import os
import re
import subprocess
import sys
from typing import Optional

from playwright.sync_api import sync_playwright

LEGACY = 'http://localhost:8080'
HERE = os.path.dirname(os.path.abspath(__file__))
ROOT = os.path.normpath(os.path.join(HERE, '..'))
ROUTES_SRC = os.path.normpath(os.path.join(ROOT, 'new', 'src', 'app', 'app.routes.ts'))


def git_branch(cwd: str) -> Optional[str]:
    """`git branch --show-current` in cwd; None if not a repo / detached."""
    try:
        r = subprocess.run(['git', '-C', cwd, 'branch', '--show-current'],
                           capture_output=True, text=True, timeout=10)
        if r.returncode != 0:
            return None
        b = r.stdout.strip()
        return b or None  # detached HEAD -> empty string -> None
    except Exception:  # noqa: BLE001
        return None


def serve_branch(port: str) -> tuple[Optional[str], str]:
    """Resolve the ng-serve process for --port and report its cwd's branch.

    Returns (branch, detail) — detail is the cwd or a human-readable reason
    the branch could not be resolved. (branch=None, detail) = unverifiable.
    """
    try:
        # the process line contains the port, e.g. 'ng serve --port 4201'
        out = subprocess.run(['pgrep', '-fl', 'ng serve'],
                             capture_output=True, text=True, timeout=10).stdout
        pid = None
        for line in out.splitlines():
            if f'--port {port}' in line:
                pid = line.split()[0]
                break
        if pid is None:
            return None, f'no ng-serve process found for --port {port}'
        # cwd of the serve process (macOS lsof -Fn: 'n<path>' line)
        lsof = subprocess.run(['lsof', '-a', '-p', pid, '-d', 'cwd', '-Fn'],
                              capture_output=True, text=True, timeout=10).stdout
        cwd = None
        for line in lsof.splitlines():
            if line.startswith('n') and len(line) > 1:
                cwd = line[1:]
                break
        if cwd is None:
            return None, f'serve pid {pid} cwd unreadable'
        b = git_branch(cwd)
        if b is None:
            return None, f'serve cwd {cwd} is not on a git branch (detached/not a repo)'
        return b, cwd
    except Exception as exc:  # noqa: BLE001
        return None, f'could not resolve serve branch: {str(exc)[:80]}'


def route_module_id(route: str) -> Optional[int]:
    """The route's moduleId from the app's route config (source of truth for
    what the guard will read). Returns None for routes without one."""
    try:
        src = open(ROUTES_SRC, encoding='utf-8').read()
    except OSError:
        return None
    for m in re.finditer(r"path:\s*'([^']+)',", src):
        if m.group(1) == route:
            # the route's own data block ends at its loadComponent — never
            # let the regex window bleed into the NEXT route's moduleId
            block = src[m.end():m.end() + 600].split('loadComponent')[0]
            mm = re.search(r"moduleId:\s*(\d+)", block)
            return int(mm.group(1)) if mm else None
    return None


def build_cookie_script(cookie_object_json: str) -> str:
    # document.cookie on the target origin — same host, same value as the
    # real login's cookie; path=/ so it is sent on every navigation
    return "document.cookie = 'cookieObject=" + cookie_object_json + "; path=/';"


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument('route')
    ap.add_argument('selectors', nargs='*', default=[])
    ap.add_argument('--port', default='4201')
    ap.add_argument('--module-id', type=int, default=None,
                    help='override the route moduleId from app.routes.ts')
    ap.add_argument('--cookie-json', default=None,
                    help='raw cookieObject JSON to inject (controls: forced-empty store, etc.)')
    ap.add_argument('--role-id', type=int, default=None,
                    help='override roleId in the cached cookieObject (control: a role with no '
                         'privilege row => the bootstrap writes an empty store)')
    ap.add_argument('--branch', default=None,
                    help='the git branch under test (the tree the serve must be compiling). '
                         'Defaults to the repo root\'s current branch. A serve compiling any '
                         'other branch is a FAIL (Q32 tree-integrity).')
    ap.add_argument('--exercise-modal', nargs='?', const='para', default=None, metavar='TERM',
                    help='after load, open the screen\'s modal ("Add Item" button) and type TERM '
                         'into its first text input — exercises the modal search path so a '
                         'rendered error toast can be caught')
    args = ap.parse_args()
    route = args.route if args.route.startswith('/') else f'/{args.route}'
    module_id = args.module_id if args.module_id is not None else route_module_id(route.lstrip('/'))

    # ---- Q32 tree-integrity: the branch under test vs the serve's tree ----
    branch_under_test = args.branch or git_branch(ROOT)
    serve_b, serve_detail = serve_branch(args.port)
    print(f'SERVE-BRANCH: {serve_b or "UNRESOLVED"} (cwd: {serve_detail})')
    print(f'BRANCH-UNDER-TEST: {branch_under_test or "UNRESOLVED"}')
    tree_reasons = []
    if not branch_under_test:
        tree_reasons.append('branch under test unresolved (repo root not on a branch?)')
    if serve_b is None:
        tree_reasons.append(f'serve tree unverifiable: {serve_detail}')
    elif branch_under_test and serve_b != branch_under_test:
        tree_reasons.append(
            f'serve is compiling branch {serve_b!r} (cwd {serve_detail!r}) but the branch '
            f'under test is {branch_under_test!r} — a PASS here would be a green light for '
            f'a tree that does not contain the route')
    if module_id is None:
        tree_reasons.append(f'route {route!r} ABSENT from {os.path.relpath(ROUTES_SRC, ROOT)} '
                            f'— the repo tree does not contain this route (unmatched route)')
    if tree_reasons:
        for tr in tree_reasons:
            print(f'[TREE-FAIL] {tr}')
        print(f'RESULT: FAIL — ' + '; '.join(tree_reasons))
        sys.exit(1)

    # ---- credentials / session artifact (single mint source: get_dev_token) ----
    import get_dev_token as gdt
    cached = gdt.read_cache()
    if cached:
        cookie = cached.get('cookie', '')
    else:
        sys.stderr.write('NO-CACHE: mint a token with tools/get_dev_token.py first '
                         '(one login per 8h — the login-group rate limiter 429s on '
                         'consecutive logins)\n')
        sys.exit(2)

    cookie_object = ''
    for part in cookie.split(';'):
        if part.strip().startswith('cookieObject='):
            cookie_object = part.strip().split('=', 1)[1]
    if args.role_id is not None:
        # control: swap the roleId in the real login's cookieObject — a role
        # with no privilege row makes the bootstrap write an EMPTY store
        try:
            import urllib.parse as _up
            co = json.loads(_up.unquote(cookie_object))
            co['roleId'] = args.role_id
            cookie_object = _up.quote(json.dumps(co))
        except Exception:
            sys.stderr.write('ROLE-OVERRIDE-FAILED: could not rewrite the cached cookieObject\n')
            sys.exit(2)
    if args.cookie_json is not None:
        cookie_object = args.cookie_json  # control override (e.g. roleId=2)
    if not cookie_object:
        sys.stderr.write('NO-COOKIE: the cache holds no cookieObject\n')
        sys.exit(2)

    console_errors, console_warnings = [], []
    page_errors, failed_requests, bad_requests, html_as_json = [], [], [], []

    with sync_playwright() as p:
        browser = p.firefox.launch(headless=True)
        context = browser.new_context()
        page = context.new_page()

        # the session artifact ONLY: cookieObject. No token, no permissions,
        # no sessionStorage — the app's own bootstrap decides (Q21).
        page.add_init_script(build_cookie_script(cookie_object))
        # error-toast observer: record EVERY rendered error notice for the
        # whole session (a toast may auto-dismiss before the end scan)
        page.add_init_script("""window.__hermesToasts = [];
            const __mo = new MutationObserver(() => {
                document.querySelectorAll('.notice--error').forEach(el => {
                    const t = (el.textContent || '').trim();
                    if (t && !window.__hermesToasts.includes(t)) window.__hermesToasts.push(t);
                });
            });
            __mo.observe(document.documentElement, { childList: true, subtree: true, attributes: true, attributeFilter: ['class'] });""")

        page.on('console', lambda m: (console_errors.append(m.text)
                                      if m.type == 'error' else
                                      console_warnings.append(m.text)
                                      if m.type == 'warning' else None))
        page.on('pageerror', lambda e: page_errors.append(str(e)))
        page.on('requestfailed', lambda r: failed_requests.append(
            f'{r.url[:140]} -> {r.failure or "failed"}'))
        page.on('response', lambda r: (
            bad_requests.append(f'{r.status} {r.url[:140]}')
            if r.status >= 400 or (100 <= r.status < 200 and r.status != 101)
            else None))
        page.on('response', lambda r: (
            html_as_json.append(
                f'{r.url[:140]} -> HTTP {r.status} HTML-where-JSON-expected')
            if r.status < 400 and r.request.resource_type == 'xhr'
            and 'html' in (r.headers.get('content-type') or '')
            and 'json' not in (r.headers.get('content-type') or '')
            else None))

        base = f'http://localhost:{args.port}'
        page.goto(f'{base}{route}', wait_until='load', timeout=30000)
        page.wait_for_timeout(3000)  # settle: the bootstrap + the screen's data fetches

        if args.exercise_modal is not None:
            # exercise the modal search path so a rendered error toast can be
            # caught (the modal's search fires on input; a broken search shows
            # a red "… failed" toast — a state a user would call broken)
            try:
                page.click('button:has-text("Add Item")', timeout=8000)
                page.wait_for_timeout(700)
                box = page.locator('.modal-panel input[type="text"]').first
                box.fill(args.exercise_modal, timeout=8000)
                page.wait_for_timeout(2500)
            except Exception as exc:
                page_errors.append(f'modal exercise failed: {str(exc)[:80]}')

        url_now = page.url
        body_text = page.evaluate('document.body ? document.body.innerText.slice(0, 300) : ""')
        store = page.evaluate("""() => {
            const raw = localStorage.getItem('permissions');
            if (!raw) return {present: false};
            try {
                const p = JSON.parse(raw);
                const mods = Array.isArray(p.modules) ? p.modules : [];
                return {present: true, len: raw.length, count: mods.length,
                        ids: mods.map(m => m.id)};
            } catch (e) { return {present: true, corrupt: String(e).slice(0, 60)}; }
        }""")
        # the bootstrap's ground truth: the app's OWN write of the store
        # (fresh context => empty localStorage => a present non-empty store
        # can only have been written by fetchPermissions after a successful
        # /privilege/role fetch)
        bootstrap_seen = store.get('present') and store.get('count', 0) > 0
        toasts = page.evaluate("""() => {
            const seen = window.__hermesToasts || [];
            document.querySelectorAll('.notice--error').forEach(el => {
                const t = (el.textContent || '').trim();
                if (t && !seen.includes(t)) seen.push(t);
            });
            return seen;
        }""")
        selector_counts = {}
        for sel in args.selectors:
            selector_counts[sel] = page.locator(sel).count()
        browser.close()

    # ---------------- verdict ----------------
    reasons = []
    denied = re.search(r'/accessDenied\?reason=([^&]+)', url_now)
    if denied:
        reasons.append(f'accessDenied reason={denied.group(1)}')
    elif not url_now.rstrip('/').endswith(route):
        reasons.append(f'final URL is not the route: {url_now}')
    if module_id is not None:
        if not store.get('present'):
            reasons.append('permissions store missing (bootstrap did not run / failed)')
        elif store.get('count', 0) == 0:
            reasons.append('permissions store EMPTY (permissionsUnavailable state)')
        elif module_id not in (store.get('ids') or []):
            reasons.append(f'moduleId {module_id} NOT in the store '
                           f'({store.get("count")} modules present)')
    elif store.get('present') and store.get('count', 0) == 0:
        reasons.append('permissions store EMPTY')
    if not body_text.strip():
        reasons.append('empty body (nothing rendered)')
    if 'Access Denied' in body_text:
        reasons.append('body shows Access Denied')
    if console_errors:
        reasons.append(f'{len(console_errors)} console error(s)')
    if page_errors:
        reasons.append(f'{len(page_errors)} uncaught page error(s)')
    if failed_requests:
        reasons.append(f'{len(failed_requests)} failed request(s)')
    if bad_requests:
        reasons.append(f'{len(bad_requests)} non-2xx response(s)')
    if html_as_json:
        reasons.append(f'{len(html_as_json)} HTML-where-JSON-expected response(s)')
    if toasts:
        reasons.append(f'{len(toasts)} rendered error toast(s)')
    for sel in args.selectors:
        if selector_counts.get(sel, 0) == 0:
            reasons.append(f'selector missing: {sel}')

    # ---------------- report (FINAL URL always) ----------------
    print(f'FINAL-URL: {url_now}')
    print(f'BOOTSTRAP: /privilege/role fetch {"seen" if bootstrap_seen else "NOT seen"}')
    if store.get('present'):
        print(f'STORE: present len={store.get("len")} modules={store.get("count")}'
              + (f' has{module_id}={module_id in (store.get("ids") or [])}' if module_id is not None else ''))
    else:
        print('STORE: missing')
    if body_text:
        print(f'BODY: {body_text[:100]!r}')
    for sel in args.selectors:
        print(f'SELECTOR {sel}: {selector_counts.get(sel, 0)} found')
    for w in console_warnings[:5]:
        print(f'[WARN] {w}')
    for e in console_errors[:5]:
        print(f'[ERROR] {e}')
    for pe in page_errors[:3]:
        print(f'[PAGE-ERROR] {pe}')
    for fr in failed_requests[:5]:
        print(f'[FAILED-REQ] {fr}')
    for b in bad_requests[:5]:
        print(f'[BAD-REQ] {b}')
    for h in html_as_json[:3]:
        print(f'[HTML-AS-JSON] {h}')
    for t in toasts:
        print(f'[TOAST] {t}')

    ok = not reasons
    print(f'RESULT: {"PASS" if ok else "FAIL"}'
          + ('' if ok else ' — ' + '; '.join(reasons)))
    sys.exit(0 if ok else 1)


if __name__ == '__main__':
    main()
