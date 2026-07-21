# Decision Needed: `CORSFilter`'s wildcard-reflected Origin with credentials enabled

**Prepared:** 2026-07-21, at owner's explicit request, treated with the **same priority/severity-review tier** as the Razorpay access-control findings (`docs/DECISION_NEEDED_RazorPayClientTrustedAmount.md`, `docs/DECISION_NEEDED_SpringSecurityFilterChainNotWired.md`). No application code has been touched to produce this document — proposal only, held as its own track, separate from the hybrid-deployment/shell work in `docs/ANGULAR_HYBRID_DEPLOYMENT_PROPOSAL.md`.

**Headline — this is real, bad code, but a separate mechanism appears to be catching it in practice; I verified this live rather than assume either way.** The naive reading of `CORSFilter.java` in isolation is "any website on the internet can make authenticated, credentialed requests against this backend using a logged-in user's real session." Read in isolation, that's an accurate description of what the code says. But this codebase has a *second*, unrelated filter that runs earlier in the chain and performs its own real origin-allowlist check — and I ran live, read-only tests against the actual running dev instance (`localhost:8080`) to check whether that second mechanism actually closes the gap in practice, rather than just asserting it does from a code read. **It does, in every case I tested.** That changes the severity from "actively exploitable today" to "a real, single-point-of-failure defense-in-depth gap that provides zero protection on its own, currently masked by a separate, independently-maintained mechanism." Full detail and confidence levels below — I want to be precise about what's confirmed by live testing vs. what's confirmed by code reading vs. what I could not verify at all (no DB access to inspect the actual allow-list contents).

---

## 1. The vulnerable code, confirmed exactly

`legacy/HIS-WAR/src/main/java/com/param/filter/CORSFilter.java`, registered and mapped to `/*` (`legacy/HIS-WAR/src/main/webapp/WEB-INF/web.xml:29-37`), runs on every request:

```java
// lines 24-29 -- a real allow-list exists in source...
private static final List<String> ALLOWED_ORIGINS = Arrays.asList(
    "http://localhost:8081",
    "https://ociqa.his.axonichealth.com",
    "http://localhost:8090",
    "http://localhost:8080"
);

// lines 41-46 -- ...but it's never used. The check against it is commented out:
String origin = request.getHeader("Origin");
//	        if (ALLOWED_ORIGINS.contains(origin)) {
	            response.setHeader("Access-Control-Allow-Origin", origin);
	            response.setHeader("Access-Control-Allow-Credentials", "true");
//	        }
```

Read on its own: this reflects **whatever `Origin` header the request carries** back as the allowed origin, unconditionally, and sets `Access-Control-Allow-Credentials: true` alongside it. That combination is what tells a browser "the calling page's script may read the response of a credentialed cross-origin request" — normally the two most dangerous CORS settings to combine, and normally never valid to combine with a literal wildcard/reflect-anything origin.

**What "in practice" would mean if this were the only check in the system:** any website, loaded in the same browser as a logged-in hospital-staff user, could have its own JavaScript issue a `fetch(...,{credentials:'include'})` call directly to this backend. If nothing else stopped it, the browser would attach the user's session, the server would process it as that authenticated user, and — because of the reflected-origin + credentials combination — the attacker's page would be allowed to read the response. That's the "can any external site make authenticated requests using a logged-in user's session" scenario the ask specifically named. **In isolation, yes.** It is not in isolation, though — see below.

## 2. The mitigating factor found — a separate filter's real origin check, running first

`legacy/HIS-WAR/src/main/java/com/param/filter/JwtTokenFilter.java`, registered as `requestWrap` and mapped to `/*` (`web.xml:19-26`), **runs before `cors`** in the servlet filter chain — filter execution order follows `<filter-mapping>` order in `web.xml`, and `requestWrap` (line 24) is declared before `cors` (line 34).

This filter does its own, unrelated origin check, unconditionally, for every request that carries an `Origin` header (`JwtTokenFilter.java:57-63`):

```java
if(originName != null){
    boolean isoriginNameAllowed = checkDomain(UserComponent.getAllowedDomains(), originName);
    if(! isoriginNameAllowed){
        throw new ApplicationException(" Origin "+originName+" Not Allowed Please Contact Administrator.");
    }
}
```

`UserComponent.getAllowedDomains()` (`Global/src/main/java/com/param/global/utility/UserComponent.java:61-66`) is backed by a **real, DB-configured list** (`iUserMasterService.getAllowedDomainsList()`), not a hardcoded/commented-out one — this is a genuinely different, live mechanism from `CORSFilter`'s dead allow-list. If the origin isn't on that list, `checkDomain` throws, which is caught by `JwtTokenFilter`'s outer `catch (ApplicationException ae)` block and turned into a **502** response (`JwtTokenFilter.java:632-634`) — before `filterChain.doFilter(...)` is ever reached, meaning `CORSFilter` (which runs later in the chain) never executes for that request at all.

## 3. Live verification against the running dev instance — not just code reading

A separate, unrelated Java process was already running on `localhost:8080` in this environment (a live legacy deployment, discovered while investigating a different question this session — not started for this purpose). I ran read-only `curl` requests against it — no login attempted, no data touched, no requests capable of mutating anything:

| Test | Result |
|---|---|
| `GET /api/global/status` with `Origin: https://evil-attacker.example` | **502**, no `Access-Control-Allow-Origin` header anywhere in the response |
| `GET /api/global/status` with no `Origin` header | 401 (normal — no session), no CORS headers (expected, not a CORS request) |
| `GET /api/global/status` with `Origin: http://localhost:8081` (one of `CORSFilter`'s own dead-listed origins) | **401**, not 502 — this origin passed `JwtTokenFilter`'s real check; still no `Access-Control-Allow-Origin` header visible in the response, even for an origin that passed |
| `OPTIONS` preflight with `Origin: https://evil-attacker.example` | **502**, same as the plain GET |
| Root path `/` and a static resource path, both with the untrusted origin | **502** in both cases — the origin check is not limited to API paths |

**In every test run, an untrusted origin was rejected with a 502 before any CORS header could appear, and even a trusted-looking origin did not visibly trigger `CORSFilter`'s reflected header in this instance's actual responses.** I don't have a full explanation for why the allowed-origin case doesn't show `Access-Control-Allow-Origin` either (possibilities: `CORSFilter` may not be reached the way I assumed for a 401 response, or some other layer strips/overrides headers — this is a secondary, unresolved detail, not the main finding, and doesn't change the conclusion that the untrusted-origin case is blocked). **Confidence level: this specific behavior (untrusted origin → 502, before any CORS header) is empirically confirmed against a live instance, not just inferred from reading the code.**

## 4. What is NOT verified, and why this is still a real finding worth fixing

- **The actual contents of the DB-backed `getAllowedDomainsList()` were not inspected** (no DB access in this environment) — I only know empirically that `http://localhost:8081` passes and `https://evil-attacker.example` doesn't, in *this* dev instance. Production's list could be broader, narrower, or misconfigured in ways this local instance doesn't reflect.
- **`checkDomain`'s string-matching is unusual and worth a second look on its own merits**, independent of this finding: it does `domain.replace("//","")` then splits on `:`, rejoining the first two segments — e.g. `https://ociqa.his.axonichealth.com` becomes the literal string `https:ociqa.his.axonichealth.com` before being checked against the allow-list. Whatever's stored in the DB config has to already be in that exact colon-joined, slash-stripped shape for a match to succeed. This is fragile: a config entry stored in the more natural `https://...` or bare-hostname format would silently never match, which could fail in either direction (a legitimate origin wrongly rejected, or — if someone "fixes" a perceived bug in this parsing without understanding the dependency — a change that widens what matches). Not confirmed as currently broken; flagged as a fragility worth deciding whether to harden regardless of this CORS finding's own resolution.
- **This defense is a single point of failure by construction.** `CORSFilter` provides *zero* protection on its own — its own allow-list is dead code. The only reason the worst-case scenario doesn't hold today (per live testing) is that a completely separate filter, written for a different purpose (request/domain validation, not specifically "CORS"), happens to run first and happens to reject the same requests. If `JwtTokenFilter`'s origin check is ever reordered, refactored, disabled for a specific path, or its DB-backed list is ever left empty/misconfigured in some environment, `CORSFilter` would provide no backup at all — its own logic would then behave exactly as bad as the naive worst-case reading in §1, with nothing else in the codebase positioned to catch it.

## 5. Severity assessment

**Not** "critical, actively exploitable today" — live testing contradicts that. **Is** a real, confirmed code-quality and defense-in-depth failure: dead allow-list code sitting in a security-relevant filter, currently masked rather than fixed by an unrelated mechanism. This is exactly the kind of gap that stays invisible for a long time and then becomes critical with zero warning the moment the masking mechanism changes for an unrelated reason (e.g., someone reorders filters during an unrelated refactor, or the DB-backed allow-list is accidentally cleared during a migration). Recommend treating it as a real, medium-to-high priority fix — not an emergency, given the live evidence, but not a "someday" item either, given how silent its failure mode would be.

## 6. Proposed fix (not implemented — for review)

1. **Re-enable a real allow-list check in `CORSFilter.java` itself** — uncomment the `if (ALLOWED_ORIGINS.contains(origin))` guard (or better, replace the hardcoded Java list with a call into the same `UserComponent.getAllowedDomains()` mechanism `JwtTokenFilter` already uses, so there's exactly one allow-list to maintain, not two independently-drifting ones — the hardcoded list currently differs from whatever the DB-backed list contains, and nothing keeps them in sync).
2. **Only set `Access-Control-Allow-Credentials: true` when the origin is actually confirmed allowed** — never unconditionally, regardless of which allow-list mechanism is used.
3. **Separately worth deciding: harden or simplify `checkDomain`'s string-parsing** (§4) so the allow-list format isn't a fragile, undocumented convention — independent of whether fix (1) is done via the existing mechanism or a new one.

## 7. What we need from you

1. **Should I proceed with fix (1)/(2) above** (re-enable a real check, ideally unifying with the DB-backed list) once you're ready to schedule it, or do you want a different fix shape?
2. **Do you want the actual production/staging allow-list contents checked** (not something I can do from this environment) before deciding how urgent this is — i.e., confirm whoever manages that DB config knows what's actually in it and that it's intentionally scoped, not just "empirically didn't break in dev"?
3. **Priority relative to other open work:** given the live-tested finding that this isn't currently exploitable (unlike the Razorpay auth-bypass reading, which remains unconfirmed live), I'd suggest this sits a tier below that document's urgency — real, but not on fire — though the actual priority call is yours.

No code has been touched to produce this document. Awaiting your direction.
