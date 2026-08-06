#!/usr/bin/env python3
"""verify_rate_limit.py — live verification of the 6A RateLimitFilter (PHASE 6).

The approved 6A verification plan, as a committed re-runnable tool:
  1. normal-rate login sequence  -> no 429 (no false positive)
  2. per-account test            -> account A's 6th attempt 429s while account B
                                   (same IP) still passes
  3. per-IP burst tests          -> login 30/10, oracle 10/2, OTP 10/2 each yield 429
                                   at the exact boundary (31st / 11th / 11th)
  4. non-target path             -> never 429

REGRESSION ASSERTIONS (added 2026-08-06, BR-177 — the body-destroy bug):
  A. Every non-429 response on a LOGIN-GROUP target must be the app's real JSON
     envelope — NOT the GlobalExceptionHandler HIS-400 "Bad request: Invalid
     parameters". The 6A filter's original body-read consumed the request stream
     (com.param.exception.ContentCachingRequestWrapper caches as-read but never
     re-serves), so the downstream @RequestBody saw an empty body and every login-
     group call 400'd — while the oracle (no body-read) returned 200s. That
     signature (oracle 200s next to login-group 400s) is now a FAILURE.
  B. POSITIVE CONTROL — one full login sequence through the actual UI endpoints
     (node/playwright: tools/golden-trace/positive_control_login.js) must return
     a session token. If the body is destroyed, the unit dropdown never populates
     and no token comes back; the control fails.

  - Run against a FRESH deployment (restart clears the in-memory buckets). The tests
    are ordered normal-rate FIRST, then per-account, then bursts, so a fresh run
    observes the correct thresholds; a re-run against a warmed deployment may see
    residual drain (the in-memory limitation is documented in the filter).
  - ORDERING vs other sweeps: run this tool BEFORE tools/verify_allowlist_reality.py
    on a fresh deployment — that sweep's ALLOW curls hit /adt/userLogin,
    /adt/checkEmailAvailability, /adt/forgotPasswordGeneration and /adt/otpVerification
    (one token each), which pre-consumes those buckets and shifts the exact-boundary
    assertions (observed 2026-08-06: 5/7 when run after the reality sweep, 7/7 when
    run first).
Usage: python3 tools/verify_rate_limit.py [base-url]
"""
import json
import subprocess
import sys
import time
import urllib.error
import urllib.request

BASE = sys.argv[1] if len(sys.argv) > 1 else "http://localhost:8080"
fails = []


def status(path, body=None):
    """Return (http_code, response_body). The body is needed to detect the
    body-destroy regression: a HIS-400 'Bad request: Invalid parameters' on a
    login-group target means the filter consumed the stream, not that the app
    rejected the credentials."""
    data = body.encode() if body else b"{}"
    try:
        with urllib.request.urlopen(urllib.request.Request(
                BASE + path, method="POST", headers={"Content-Type": "application/json"},
                data=data), timeout=15) as r:
            return r.status, r.read().decode("utf-8", "replace")
    except urllib.error.HTTPError as e:
        try:
            return e.code, e.read().decode("utf-8", "replace")
        except Exception:
            return e.code, ""
    except Exception as e:
        return f"ERR:{e}", ""


def check(name, cond, detail=""):
    print(f"[{'PASS' if cond else 'FAIL'}] {name}" + (f"  ({detail})" if detail else ""))
    if not cond:
        fails.append(name)


def assert_no_body_destroy(path, results):
    """Every non-429 response must be a real app envelope, never the HIS-400
    'Bad request: Invalid parameters' that the body-destroy bug produced."""
    bad = [b for code, b in results if code != 429 and '"HIS-400"' in b and "Invalid parameters" in b]
    check(f"no body-destroy (HIS-400) on {path}", not bad, f"{len(bad)} HIS-400 response(s)")


def positive_control_login():
    """Full UI login through the real endpoints; asserts a session token."""
    r = subprocess.run(
        ["node", "tools/golden-trace/positive_control_login.js"],
        capture_output=True, text=True, timeout=180)
    out = (r.stdout + r.stderr).strip()
    ok = r.returncode == 0 and "TOKEN-OK" in out
    check("positive control: real UI login returns a token", ok, out.splitlines()[-1] if out else "no output")
    return ok


# ---- POSITIVE CONTROL (runs first: it is the whole-flow proof) ----
positive_control_login()

# 1. normal-rate login sequence — 5 attempts spaced ~1.5s, all must pass AND the
#    non-429 bodies must be real envelopes (not HIS-400)
results = [status("/adt/userLogin", '{"userName":"u1","password":"p1","unitId":1,"organizationId":1}')
           for _ in range(5) if not time.sleep(1.5)]
codes = [c for c, _ in results]
check("normal-rate login: no 429 in 5 spaced attempts", 429 not in codes, str(codes))
assert_no_body_destroy("/adt/userLogin", results)

# 2. per-account test — forgotPasswordGeneration, same IP, two accounts
#    per-IP bucket for this path is 10 burst; 6 (A) + 1 (B) = 7 < 10, so only the
#    per-account dimension can produce the 429 on A's 6th.
a_results = [status("/adt/forgotPasswordGeneration", '{"unitId":1,"organizationId":1,"emailId":"accA@x.com"}')
             for _ in range(6)]
b_code, b_body = status("/adt/forgotPasswordGeneration", '{"unitId":1,"organizationId":1,"emailId":"accB@x.com"}')
check("per-account: account A's 6th attempt 429s", a_results[-1][0] == 429, str([c for c, _ in a_results]))
check("per-account: account B (same IP) still passes", b_code != 429, f"code={b_code}")
assert_no_body_destroy("/adt/forgotPasswordGeneration", a_results + [(b_code, b_body)])

# 3a. login per-IP burst — 35 rapid on /adt/getUnitByCredentials with a UNIQUE account
#     per request (the per-account buckets never bind; the per-IP 30-burst must bind:
#     the 31st request 429s)
results = [status("/adt/getUnitByCredentials",
                  '{"unitId":1,"organizationId":1,"userName":"u%d"}' % i) for i in range(35)]
codes = [c for c, _ in results]
first = codes.index(429) + 1 if 429 in codes else None
check("login per-IP burst: 429 at the 31st (30-burst)", first == 31, f"first429@#{first}" if first else "no 429")
assert_no_body_destroy("/adt/getUnitByCredentials", results)

# 3b. oracle per-IP burst — 15 rapid on /adt/checkEmailAvailability (10 burst)
results = [status("/adt/checkEmailAvailability", '{"emailId":"oracle%d@x.com"}' % i) for i in range(15)]
codes = [c for c, _ in results]
first = codes.index(429) + 1 if 429 in codes else None
check("oracle burst: 429 at the 11th (10-burst)", first == 11, f"first429@#{first}" if first else "no 429")
assert_no_body_destroy("/adt/checkEmailAvailability", results)

# 3c. OTP per-IP burst — 15 rapid on /adt/otpVerification with unique emails (the
#     per-account buckets never bind; the per-IP 10-burst must bind at the 11th)
results = [status("/adt/otpVerification",
                  '{"unitId":1,"emailId":"otp%d@x.com","otp":"123456"}' % i) for i in range(15)]
codes = [c for c, _ in results]
first = codes.index(429) + 1 if 429 in codes else None
check("OTP per-IP burst: 429 at the 11th (10-burst)", first == 11, f"first429@#{first}" if first else "no 429")
assert_no_body_destroy("/adt/otpVerification", results)

# 4. non-target path — never rate-limited
results = [status("/api/opd/getAIAppointmentCount") for _ in range(8)]
codes = [c for c, _ in results]
check("non-target path: no 429", 429 not in codes, str(codes))

print(f"\nRESULT: {10 - len(fails)}/10 passed")
sys.exit(1 if fails else 0)
