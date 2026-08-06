#!/usr/bin/env python3
"""verify_rate_limit.py — live verification of the 6A RateLimitFilter (PHASE 6).

The approved 6A verification plan, as a committed re-runnable tool:
  1. normal-rate login sequence  -> no 429 (no false positive)
  2. per-account test            -> account A's 6th attempt 429s while account B
                                    (same IP, same path) still passes
  3. per-IP burst tests          -> login 30/10, oracle 10/2, OTP 10/2 each yield 429
  4. non-target path             -> never 429

Notes:
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
  - All endpoints return the app's own envelopes (200-with-error etc.); the ONLY
    429 source on these paths is the rate limiter.
Usage: python3 tools/verify_rate_limit.py [base-url]
"""
import sys
import time
import urllib.error
import urllib.request

BASE = sys.argv[1] if len(sys.argv) > 1 else "http://localhost:8080"
fails = []


def status(path, body=None):
    data = body.encode() if body else b"{}"
    try:
        with urllib.request.urlopen(urllib.request.Request(
                BASE + path, method="POST", headers={"Content-Type": "application/json"},
                data=data), timeout=15) as r:
            return r.status
    except urllib.error.HTTPError as e:
        return e.code
    except Exception as e:
        return f"ERR:{e}"


def check(name, cond, detail=""):
    print(f"[{'PASS' if cond else 'FAIL'}] {name}" + (f"  ({detail})" if detail else ""))
    if not cond:
        fails.append(name)


# 1. normal-rate login sequence — 5 attempts spaced ~1.5s, all must pass
codes = [status("/adt/userLogin", '{"userName":"u1","password":"p1","unitId":1,"organizationId":1}')
         for _ in range(5) if not time.sleep(1.5)]
check("normal-rate login: no 429 in 5 spaced attempts", 429 not in codes, str(codes))

# 2. per-account test — forgotPasswordGeneration, same IP, two accounts
#    per-IP bucket for this path is 10 burst; 6 (A) + 1 (B) = 7 < 10, so only the
#    per-account dimension can produce the 429 on A's 6th.
a_codes = [status("/adt/forgotPasswordGeneration", '{"unitId":1,"organizationId":1,"emailId":"accA@x.com"}')
           for _ in range(6)]
b_code = status("/adt/forgotPasswordGeneration", '{"unitId":1,"organizationId":1,"emailId":"accB@x.com"}')
check("per-account: account A's 6th attempt 429s", a_codes[-1] == 429, str(a_codes))
check("per-account: account B (same IP) still passes", b_code != 429, f"code={b_code}")

# 3a. login per-IP burst — 35 rapid on /adt/getUnitByCredentials with a UNIQUE account
#     per request (the per-account buckets never bind; the per-IP 30-burst must bind:
#     the 31st request 429s)
codes = [status("/adt/getUnitByCredentials",
                '{"unitId":1,"organizationId":1,"userName":"u%d"}' % i) for i in range(35)]
first = codes.index(429) + 1 if 429 in codes else None
check("login per-IP burst: 429 at the 31st (30-burst)", first == 31, f"first429@#{first}" if first else "no 429")

# 3b. oracle per-IP burst — 15 rapid on /adt/checkEmailAvailability (10 burst)
codes = [status("/adt/checkEmailAvailability", '{"emailId":"oracle%d@x.com"}' % i) for i in range(15)]
first = codes.index(429) + 1 if 429 in codes else None
check("oracle burst: 429 at the 11th (10-burst)", first == 11, f"first429@#{first}" if first else "no 429")

# 3c. OTP per-IP burst — 15 rapid on /adt/otpVerification with unique emails (the
#     per-account buckets never bind; the per-IP 10-burst must bind at the 11th)
codes = [status("/adt/otpVerification",
                '{"unitId":1,"emailId":"otp%d@x.com","otp":"123456"}' % i) for i in range(15)]
first = codes.index(429) + 1 if 429 in codes else None
check("OTP per-IP burst: 429 at the 11th (10-burst)", first == 11, f"first429@#{first}" if first else "no 429")

# 4. non-target path — never rate-limited
codes = [status("/api/opd/getAIAppointmentCount") for _ in range(8)]
check("non-target path: no 429", 429 not in codes, str(codes))

print(f"\nRESULT: {7 - len(fails)}/7 passed")
sys.exit(1 if fails else 0)
