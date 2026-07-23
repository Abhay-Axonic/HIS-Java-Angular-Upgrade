# Decision Needed: the JWT signing key is a hardcoded literal checked into source — anyone who has seen this repo can forge a valid, signed authentication token for any user

**Prepared:** 2026-07-23, fast-tracked as the single most severe finding of this project's security-review work to date — this is a complete authentication bypass for the entire application, in every environment, not a gap in one feature. Found while investigating a fix for `docs/DECISION_NEEDED_ResetPasswordMissingIdentityBinding.md` — proposing that fix required determining whether any server-side value is actually cryptographically bound to the caller's real identity, which led directly here. No code has been touched to produce this document.

**Headline — every request this application accepts is authenticated by verifying a JWT signature against a key that is a plain string literal in a `.java` file, unconditionally, in every build, with no environment-variable override anywhere in the code.** Anyone who has ever read this source file — which, given this project's own already-documented pattern of secrets sitting in git history since the baseline commit, plausibly includes every developer, contractor, or anyone with repo access past or present — can mint a completely valid, correctly-signed authentication token for **any** username, including an administrator, entirely offline, without ever logging in or knowing any password.

---

## 1. The key, confirmed exactly

`legacy/Global/src/main/java/com/param/security/config/JwtTokenUtil.java:14`:
```java
private static final String SECRET_KEY = "CliniVantage##2017";
```

Used to both **sign** newly-issued tokens (`createToken(...)`, lines 29-47, `.signWith(SignatureAlgorithm.HS256, SECRET_KEY)`) and **verify** every incoming token (`extractAllClaims`, line 85, `Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token)`). No `System.getenv(...)`, no externalized config, no per-environment override anywhere in this file or anywhere else in the tree (grepped for any other reference to this literal string — found nowhere else, meaning it's not even duplicated, just this one unconditional constant that every environment's compiled code shares identically).

## 2. Why this is a complete bypass, not a weak-but-present protection

`JwtTokenFilter.java` (the servlet filter gating almost every request) does this, in order:
1. Extracts the `Authorization: Bearer <token>` header.
2. Calls `JwtTokenUtil.validateToken(token, userId, unitId)` — this checks the token's **signature** against `SECRET_KEY`, and cross-checks the token's own signed `subject` (username) and `unitId` claims against the request's separate `userId`/`unitId` headers (via a DB lookup, `UserComponent.getUsers()`).
3. If valid, sets `SecurityContextHolder`'s authentication principal to the JWT's own signed username and lets the request through.

Structurally, step 2's cross-check is reasonable **design** — it prevents a request from claiming a different identity via headers than what the token itself was issued for. But that protection is entirely contingent on the token's signature actually being unforgeable. Since `SECRET_KEY` is a known, publicly-readable-in-this-repo constant, anyone can:
1. Construct a JWT claiming `subject: "admin"` (or any other real username) and any `unitId`, using any standard JWT library and the exact key above.
2. Sign it with `HS256` and this key — `Jwts.parser().setSigningKey(SECRET_KEY)` will accept it as fully valid, because the signature genuinely matches.
3. Send it as the `Authorization` header, with matching `userId`/`unitId` headers for the impersonated user (both fields are plain, attacker-controlled headers, not secrets).
4. Every subsequent check in the application — including `SecurityContextHolder`'s authentication principal, which is the most authoritative "who is this" value available anywhere in this codebase — now genuinely believes the attacker is that user.

This is worse in kind than every other finding logged this session. The password-disclosure and missing-identity-binding findings require an attacker to already have *some* valid session. This one requires nothing but having seen this source file — no valid credentials, no existing session, no prior authentication of any kind.

## 3. Direct relevance to `docs/DECISION_NEEDED_ResetPasswordMissingIdentityBinding.md`

The natural-looking fix for that document — bind `resetPassword` to "the authenticated caller's own identity" — is only meaningful if "authenticated" means something. `SessionService.getUserId()` (the obvious candidate to bind to) is sourced from a plain, client-editable cookie (`SessionService.java:60-65`, populated by `SessionHandlerInterceptor` parsing the raw `cookieObject` cookie) — **not** from the JWT at all, and just as forgeable as the request body field the other document already flags. The only value in this codebase that's actually derived from the JWT's signed claim is `SecurityContextHolder.getContext().getAuthentication().getName()` (set in `JwtTokenFilter.java:342-344` directly from `JwtTokenUtil.extractUsername(token)`). Binding to *that* is the structurally correct fix for the resetPassword document — but its real-world security value is capped by this document's finding: an attacker who knows the hardcoded key can forge a token whose signed username is anything they want, making even that "authoritative" value spoofable.

**Both documents need resolving together for either fix to provide real protection.** Fixing `resetPassword` alone (binding to the JWT-derived identity) closes the "just call the API with someone else's ID" trivial exploit. Fixing this document (rotating the key, moving it to environment config) closes the "forge any identity from scratch" exploit. Either alone leaves the other path open.

## 4. Reachability beyond this local dev instance

**Almost certainly yes, more so than any other finding this session.** The DDL-default and Java-literal secrets found earlier (`docs/SECRETS_REMEDIATION.md`/`BATCH2`, the SMTP password) were at least sometimes conditional — a fresh deployment inherited a default only if nobody explicitly configured otherwise, or a literal existed in one code path among several. This key is an **unconditional compile-time constant** with no override mechanism at all in the current code — every single build of this application, from any commit at or after this file's introduction, in any environment (dev, QA, staging, production, or any customer's on-prem deployment built from this codebase), signs and verifies tokens with the exact same key. There is no configuration step, no environment variable, nothing an operator could set differently per-deployment even if they wanted to — the only way any real deployment could differ is if someone manually patched this specific source line before building, which would have to be deliberate and undocumented.

## 5. Severity assessment

**Critical — the most severe finding logged this session, and arguably the most severe class of finding this kind of review can produce.** This is not "an endpoint leaks data" or "an endpoint lacks a role check" — it's "the mechanism that is supposed to establish who anyone is, at all, can be defeated by reading one line of source code." Every other access-control finding this session (missing role checks, missing identity binding) assumed a real, trustworthy notion of "authenticated" to reason about who *should* be allowed to do what. This finding means that assumption itself doesn't hold.

## 6. Proposed fix directions (not implemented — for review)

1. **Rotate the signing key immediately** and move it to environment-based configuration (`System.getenv("JWT_SIGNING_KEY")` or equivalent), following the exact same pattern already established in `docs/SECRETS_REMEDIATION.md`/`BATCH2` for the other hardcoded secrets fixed this project. This must be a cryptographically random, sufficiently long value — not another human-chosen string.
2. **Every currently-issued token becomes invalid the moment the key rotates** — this is expected and correct (anyone with an old, forged-or-real token gets logged out), but means rotation should happen at a planned moment, not silently mid-session for active real users, if this system has any live usage today.
3. Given the key has been in this exact, unconditional, compile-time-constant form since (per this project's git history) the baseline import commit, and is derived from what looks like the product's own former/alternate name ("CliniVantage") — worth treating as already compromised regardless of whether anyone has exploited it, same reasoning already applied to the SMTP credential in the two now-resolved credential-disclosure documents.
4. Resolve alongside, not instead of, `docs/DECISION_NEEDED_ResetPasswordMissingIdentityBinding.md`'s own fix — see §3 above for why both are needed together.

## 7. What we need from you

1. **Priority** — my strong recommendation is this becomes the single top-priority item across every open document this session, above the resetPassword finding it was found while investigating.
2. **Confirm rotation approach and timing** — given every issued token would invalidate on rotation, confirm whether this environment (or any other real environment built from this codebase) has active sessions that would need advance notice, or whether immediate rotation is acceptable.
3. **Which fix direction to authorize** for the code change itself — my recommendation is (1), the standard env-var externalization pattern already used elsewhere in this codebase.

No code has been touched to produce this document. Awaiting your direction.

## 8. UPDATE — proposed fix diff, migration path, and exposure-history investigation, per owner request (nothing applied)

### 8a. Proposed fix — externalize the key, fail fast if unconfigured

**Blast radius confirmed small:** only 2 files in the entire codebase reference `JwtTokenUtil` at all (`JwtTokenFilter.java`, `UserMasterServiceImpl.java`) — both call its public methods, neither touches `SECRET_KEY` directly. The fix is fully contained to `JwtTokenUtil.java` itself; no other file needs to change.

**File:** `legacy/Global/src/main/java/com/param/security/config/JwtTokenUtil.java`

```diff
 public class JwtTokenUtil {
-	private static final String SECRET_KEY = "CliniVantage##2017";
+	private static final String SECRET_KEY = loadSigningKey();
+
+	/**
+	 * Security fix, docs/DECISION_NEEDED_HardcodedJwtSigningKey.md -- the
+	 * signing key must never be a compiled-in literal. Fails fast at class-load
+	 * time if unconfigured, rather than silently falling back to another
+	 * predictable default, since a missing signing key is a deployment error,
+	 * not a condition to degrade gracefully from.
+	 */
+	private static String loadSigningKey() {
+		String key = System.getenv("JWT_SIGNING_KEY");
+		if (key == null || key.trim().isEmpty()) {
+			throw new IllegalStateException(
+				"JWT_SIGNING_KEY environment variable must be set -- refusing to start with no signing key configured.");
+		}
+		return key;
+	}
 	private static final long JWT_EXPIRATION = 8 * 60 * 60 * 1000; //valid for 24 hrs
```

**Operator action required, every environment:** set `JWT_SIGNING_KEY` to a real, cryptographically random value before this deploys anywhere — e.g. `openssl rand -base64 64`, or equivalent. Do **not** reuse the old literal, a variant of it, or anything human-chosen/memorable — this is exactly the class of value a CSPRNG exists for. I have not generated an actual value anywhere in this proposal, deliberately — doing so myself and writing it into any file would just recreate the same problem in a new location.

### 8b. Migration path — yes, this necessarily invalidates every existing session, and that is correct, not a side effect to route around

HS256 is a **symmetric** signing scheme — the same key signs and verifies. The instant `SECRET_KEY` changes, `Jwts.parser().setSigningKey(NEW_KEY).parseClaimsJws(oldToken)` throws a signature-verification failure for **every** token issued under the old key — including any real, legitimately-issued session token currently in use, and (this is the point) any token an attacker may have forged using the compromised old key. There is no way to preserve old sessions through this change without also preserving acceptance of anything forgeable with the old key, which would defeat the fix entirely.

**Explicitly recommend against a "dual-key" transition window** (temporarily accepting both old and new key to ease rollout) — the entire point of rotating is that the old key is compromised; accepting it for any period, however brief, keeps the forgery path open during that window.

**Practical consequence: every real logged-in user gets signed out the moment this deploys, with no exception.** This is the expected, correct behavior of closing this vulnerability, not a bug to work around. For this local dev environment, that's a non-issue (re-login). For any other real environment this codebase is ever deployed to, recommend: deploy during a planned, communicated window, and expect a support-load bump immediately after as users re-authenticate — but do not delay the actual fix to avoid that inconvenience, given the severity in §5.

### 8c. Exposure-history investigation

**Within this specific tracked repository: no evidence of public exposure.**
- `gh repo view` confirms: **private**, created 2026-07-14, **zero forks**, no parent repository (not itself a fork of anything public).
- The entire legacy codebase — including this key, already present — entered this repository's history in a single commit, `96b75c4` ("Baseline before upgrade"), authored by the project owner's own account, dated 2026-07-15 (the day after the repo's creation). There is no earlier history, no merge from an external source visible in this repo's git log, and no indication this repository itself has ever been public or shared outside a trusted group.

**What this investigation cannot answer, and why:** the baseline commit's own message ("Baseline before upgrade") indicates this codebase existed somewhere else — a pre-existing production/legacy system — before being imported into this tracked repository. This repo's git history has no visibility into that prior system: whether the original codebase (wherever it was hosted/version-controlled before this migration project began) was ever public, forked, shared with contractors, posted anywhere, or otherwise exposed is a question only whoever managed that original system before this migration can answer — it is genuinely outside what this repository's own history can confirm or rule out.

**Recommendation:** treat the key as compromised regardless (§5 already recommends this, independent of provable exposure — a key this old, this guessable-in-hindsight, and this long unrotated should be rotated on general security hygiene grounds even absent confirmed exposure). If you have access to or knowledge of the original pre-migration system's own access history/hosting, that would be the only way to get a more definitive answer than "not exposed within this repo, unknown before it."

### What we need from you, specifically on this update

1. Approve/reject the fix in 8a.
2. Confirm you understand and accept the migration consequence in 8b (mass re-login) — my recommendation is to proceed anyway, given the severity, but this needs your explicit sign-off given the operational impact.
3. Whether the exposure-history answer in 8c is sufficient, or whether you have visibility into the pre-migration system I don't, that would change the urgency further.
