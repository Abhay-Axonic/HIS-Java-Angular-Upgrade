#!/usr/bin/env python3
"""ONE-COMMAND SPA SESSION SETUP (2026-08-13, owner: the session expiry has
cost several sessions — solve it properly, not per-session).

Why the SPA session keeps dying: (1) the legacy login COLLIDES on stale
ACTIVE t_user_login_log rows and silently no-ops; (2) every browser re-login
ROTATES the token, so a cached get_dev_token goes stale mid-session; (3) the
SPA reads the token from `sessionStorage.token` (session.service.ts fallback)
or the `cookieObject` cookie. One command re-establishes all three:
  - clears the login collision (psql UPDATE, guarded to user 1 / his_local)
  - re-mints the token via the repo's get_dev_token.py
  - prints the EXACT console snippet to paste into the SPA tab (the SPA is
    at http://localhost:4201 — the snippet sets sessionStorage.token with
    the freshly minted token and reloads)

Usage:  python3 tools/setup_spa_session.py
Then:   paste the printed snippet into the :4201 tab's DevTools console.
The snippet is the ONE command; it never needs the token to be typed by hand.
"""
import json, os, subprocess, sys

REPO = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

def step(name, ok, detail=""):
    print(f"[{'ok' if ok else 'FAIL'}] {name}" + (f" — {detail}" if detail else ""))
    return ok

# 1) login-collision clear (guarded: user 1, his_local, only stale ACTIVE rows)
psql = subprocess.run(
    ["psql", "-h", "localhost", "-d", "his_local", "-c",
     "update public.t_user_login_log set is_logout='Y' "
     "where user_id=1 and is_logout is distinct from 'Y';"],
    capture_output=True, text=True, timeout=30)
ok1 = psql.returncode == 0
step("login-collision clear (t_user_login_log)", ok1, psql.stdout.strip().splitlines()[-1] if psql.stdout.strip() else psql.stderr[:120])

# 2) fresh token
tok_run = subprocess.run(["python3", "tools/get_dev_token.py"], capture_output=True, text=True, cwd=REPO, timeout=60)
lines = [l for l in tok_run.stdout.strip().splitlines() if l.strip()]
token = lines[-1] if lines else ""
ok2 = len(token) >= 100
step("token minted", ok2, f"len={len(token)}")

# 3) the ONE console snippet
if ok1 and ok2:
    import shlex
    snippet = (
        f"sessionStorage.setItem('token', '{token}'); "
        f"sessionStorage.setItem('userId', '1'); "
        f"sessionStorage.setItem('unitId', '1'); location.reload();"
    )
    print("\n=== PASTE THIS INTO THE :4201 CONSOLE ===")
    print(snippet)
    print("==========================================")
    sys.exit(0)
sys.exit(1)
