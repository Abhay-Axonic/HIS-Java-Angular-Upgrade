# Decision Needed: `organizationId` is never cryptographically verified anywhere in this application — every "org-scoped" check in the codebase, including tonight's own earlier fixes, ultimately trusts a value the client can edit

**Prepared:** 2026-07-30, escalated as the single highest-priority finding of tonight's session — above the individual IDOR findings it explains, because it is architectural, not screen-specific. Found while investigating a shared-fix proposal for 5 independently-audited screens (batch 33 candidates) that all turned out to share one root cause. No code has been touched to produce this document; this is an investigation-only write-up, application code will only change as separate, explicitly-approved fix branches.

**Headline — the JWT this application issues carries and cryptographically verifies exactly one tenant-identity claim, `unitId`. It carries no `organizationId` claim at all. Every place in the codebase that currently derives a trusted `organizationId` — including `SessionService.getOrganizationId()`, the value tonight's own BR-115 fix (`pharmacyproductCategoryMaster`) used as "the real, session-derived value" to defeat a client-supplied-IDOR — is in fact populated from a plain, unsigned, client-editable cookie, not from anything the server has verified.** This means BR-115's fix, while a real improvement over trusting the request body directly, does not fully close its own vulnerability class: an attacker who edits their `cookieObject` browser cookie (no signature, no HMAC, just URL-decoded JSON) can still make `SessionService.getOrganizationId()` return a forged organization.

---

## 1. The finding, confirmed by direct code read, not assumption

**`JwtTokenUtil.java`** (`legacy/Global/src/main/java/com/param/security/config/JwtTokenUtil.java`) — the token itself:
```java
private static String createToken(Map<String, Object> claims, String username, Integer unitId) {
    return Jwts.builder()
            .setClaims(claims)
            .claim("unitId", unitId)
            .setSubject(username.toLowerCase())
            ...
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact();
}
```
The only tenant-identity claim embedded and signed into the token is `unitId`. No `organizationId` claim exists anywhere in token generation. (Note: the signing key itself is now correctly externalized via `JWT_SIGNING_KEY`, per the earlier, separate, already-fixed `DECISION_NEEDED_HardcodedJwtSigningKey.md` — this finding is independent of that one and remains open regardless of key security.)

**`JwtTokenUtil.validateToken(token, userId, unitId)`** — what actually gets verified on every request:
```java
Integer tokenUnitId = JwtTokenUtil.extractUnitId(token);
...
return (username.equals(masterDto.getUserName().toLowerCase()) && tokenUnitId.equals(unitId) && !isTokenExpired(token)) ? true : false;
```
Cross-checks the request's `unitId` header against the token's signed `unitId` claim, and the request's `userId` header against a DB-looked-up username matching the token's signed subject. This is a genuine, sound cryptographic check — **for `unitId` only.** `organizationId` never appears in this method at all.

**`SessionHandlerInterceptor.java`** (`legacy/HIS-WAR/src/main/java/com/param/comman/SessionHandlerInterceptor.java`) — where `organizationId` actually comes from:
```java
List<Cookie> cookies = Arrays.stream(request.getCookies())
        .filter(cookie -> cookie.getName().equals("cookieObject")).collect(Collectors.toList());
if (cookies != null && cookies.size() > 0) {
    sessionService.setCookieObjectDto(new Gson().fromJson(
        java.net.URLDecoder.decode(cookies.getFirst().getValue(), StandardCharsets.UTF_8.name()),
        CookieObjectDto.class));
}
```
This runs on every request, unconditionally, reading a cookie named `cookieObject`, URL-decoding it, and JSON-deserializing it straight into the object `SessionService.getOrganizationId()`/`getUnitId()` read from. **No signature check. No HMAC. No cross-reference to the JWT at all.** Any client can set this cookie to any JSON payload they like — a browser devtools cookie edit is sufficient, no request-crafting tooling required.

**`SessionService.java`** — confirms the above is the *only* source: `getOrganizationId()` simply returns `cookieObjectDto.getOrganizationId()`, a plain getter on the object the interceptor just deserialized from that cookie.

**Schema confirms deriving `organizationId` from a verified `unitId` is safe and unambiguous:** `m_unit_master.unit_id` is the table's primary key, `organization_id` is a plain FK column on that same row (`psql -d his_local \d m_unit_master`) — a clean many-to-one, every unit belongs to exactly one organization, no ambiguity. This is the basis for the proposed fix in Section 3.

## 2. Why this is the root cause behind tonight's batch 33 findings, and why it's bigger than 5 screens

Every cross-tenant IDOR found live tonight (`checklistEquipmentMaster`, `keyIngredientsMaster`, `storewiseGenericMapper`, live-proven; `userNursingStationMapper`, `departmentNursingStationMapper`, architecturally identical but not live-provable in this single-tenant dev DB) shares the same shape: some layer of the Controller/Service/DAO chain trusts a client-supplied `organizationId`/`unitId`, whether from the request body directly, or indirectly via `SessionService`. **The 5 screens audited tonight are not the extent of the problem — they're just the 5 that happened to get a full Step 0/1 audit.** A rough scope check tonight:
- **138 files** call `sessionService.getOrganizationId()` somewhere (`grep -rl`) — every one of these is trusting the spoofable-cookie value, not a verified one.
- **At least 115 controllers** accept `organizationId` directly as a `@RequestParam`/`@PathVariable`/`@RequestBody` field by name (a conservative undercount — most endpoints take a DTO object with `organizationId` as one of several fields, which a literal-string grep like this one misses entirely).
- Out of **745 total controllers** in this codebase.

This is consistent with — and explains — the recurring pattern already found and individually fixed at least 6 times tonight across unrelated modules (taxComponentMaster, pharmacyproductCategoryMaster, productRestriction.productCategoryMapping, plus tonight's 3 live-proven batch-33 instances). It is very likely that a dedicated, systematic sweep (same shape as this project's existing schema-drift and duplicate-check sweeps) would find many more instances. **Not started tonight, per explicit owner direction — flagged here as a clear, high-priority candidate for its own dedicated future session,** given how consequential and recurring this exact pattern keeps proving to be.

## 3. Proposed mechanism

**Core principle: never trust a client-supplied `organizationId`, from any source — not the request body, not a path variable, and not the `cookieObject` cookie. Always derive it server-side from the one claim that is actually cryptographically verified: the JWT's `unitId`.**

Concretely:
1. Add a single, small, well-tested helper — e.g. `VerifiedIdentityService` (or extend `SessionService` itself) — with one method: `getVerifiedOrganizationId()`. Internally: takes the already-JWT-validated `unitId` (available in `JwtTokenFilter` at the point the token is confirmed valid, or re-derivable from the same header + token used there), looks up `SELECT organization_id FROM m_unit_master WHERE unit_id = :unitId` (a single indexed-PK lookup, cheap, cacheable per-request), and returns that as the one trustworthy `organizationId` for the rest of the request.
2. Populate this once per request, at the same point `JwtTokenFilter` already validates the token — either directly in that filter (setting a request attribute) or in a new, equally simple `HandlerInterceptor` that runs immediately after it, mirroring `SessionHandlerInterceptor`'s existing structure but keyed off the verified JWT claim instead of the unsigned cookie.
3. Existing DAOs/Services that need org-scoping call this new helper instead of `sessionService.getOrganizationId()` or a client-supplied DTO field — a one-line change per call site, not a structural rewrite of each screen's logic.
4. `unitId` itself is already cryptographically sound (JWT-verified) — no change needed there; the gap is `organizationId` specifically, since it was never part of the token to begin with.

**This does not require adding `organizationId` to the JWT itself** (which would mean re-issuing every outstanding token and touching the login flow) — deriving it fresh from the already-verified `unitId` on every request is simpler, always current even if a unit's org assignment ever changed, and requires zero changes to token issuance.

## 4. Feasibility — honest assessment, not an assumption either way

**Building the foundation described above is small, bounded, and low-risk.** One new class or one new method, one new (or extended) interceptor, a handful of unit-level tests, verifiable in isolation exactly the way tonight's individual IDOR fixes were verified (live before/after, real request/response evidence). This part could reasonably be built and merged in a single session.

**Retrofitting the ~115-138+ existing call sites to actually *use* the new trustworthy source is a different kind of work, and I don't think it's honest to call that "bounded" the same way.** The foundation makes a trustworthy value *available* everywhere — it does not automatically rewrite the ~115-138 places that currently read a client-supplied or cookie-derived value instead. Each of those is a real, individual code change (swap one field read for a method call), needs its own before/after verification given how many of these turned out to have compounding bugs alongside the IDOR itself (write-before-throw, missing `is_deleted` filters, zero-count-as-error), and — per this project's own Maker-Checker discipline — plausibly its own reviewable diff. There is no single shared class whose mere existence closes the other 100+ call sites; someone still has to touch each one.

**My honest read:** this is exactly the same shape of problem as the project's existing schema-drift sweep — a cheap, high-confidence *foundation* fix, paired with a large, mechanical, per-instance *migration* that has to happen call-site by call-site and is not meaningfully acceleratable by tooling (unlike, say, a scripted `ALTER TABLE` sweep, each of these requires reading the surrounding business logic to confirm the swap doesn't change any *other* behavior first, matching tonight's own repeated experience of finding compounding, non-IDOR bugs alongside every IDOR). Treat the foundation as buildable now if approved; treat the full migration as its own properly-scoped future initiative, same treatment as the exception-swallowing sweep and the just-flagged broader-pattern sweep above.

## 5. Immediate stopgaps proposed for the 5 confirmed-vulnerable controllers (in parallel, not blocked on the architectural decision)

Per explicit direction: these will be built as 5 individual dedicated branches, same Maker-Checker process and live before/after verification as every other fix tonight, and will use the **correct** JWT-`unitId`-derived mechanism from Section 3 above (not the weaker `SessionService`-cookie pattern used by tonight's earlier BR-115 fix) — since building the small helper method is barely more work than calling the existing `SessionService`, and only the JWT-derived version actually closes the gap rather than partially closing it. These stopgaps do not depend on whether the full shared-mechanism/interceptor foundation gets approved and built first; each can inline its own call to the verified-`unitId`→`organizationId` lookup independently, and be trivially refactored to call a shared helper later if one gets built.

Affected: `checklistEquipmentMaster`, `keyIngredientsMaster`, `storewiseGenericMapper`, `userNursingStationMapper`, `departmentNursingStationMapper` — full per-screen findings in tonight's audit reports, referenced in `docs/ANGULAR_PENDING_FINDINGS.md`.

## 6. Disposition

**Awaiting owner decision on:**
1. Approve building the shared foundation (Section 3) now, as its own dedicated branch — small, bounded, independently verifiable.
2. Confirm proceeding with the 5 individual stopgap branches (Section 5) in parallel, using the JWT-derived mechanism.
3. Confirm the broader call-site migration (Section 4's ~115-138+ instances) and the codebase-wide sweep (Section 2) are both explicitly deferred to a dedicated future session, not started now.

All 5 batch-33 candidate screens remain excluded from any Angular rebuild until their backend is fixed — same precedent as `pharmacyproductCategoryMaster`/`taxComponentMaster` earlier tonight.
