# Decision Needed: pharmacyunitMaster's GetById/Update endpoints have no organization or unit scoping at all — any valid authenticated session can read and modify any org's unit-master record

**Prepared:** 2026-07-30, found during the batch 31 `pharmacyunitMaster` Step 0/1 audit, live-verified and root-caused by the orchestrating session. No application code has been touched to produce this document — investigation and proposal only.

**Headline — this is the more severe version, confirmed precisely, not the milder one. A valid session is required to reach the endpoint at all (confirmed: no token at all → HTTP 401), but once a session is valid, it is not scoped to that user's own organization or unit in any way. Any authenticated user, from any org, can read and write any other org's unit-master record by guessing or enumerating its numeric ID — the `{orgId}` segment in the URL is accepted but never checked against anything.**

---

## 1. The bug, confirmed exactly, at both layers

**Data layer — `UomUnitDao.java:34-41`:**
```java
@Override
public UomUnit getById(Integer id) {
    Criteria criteria = getSession().createCriteria(UomUnit.class);
    criteria.add(Restrictions.eq("id", id));
    criteria.add(Restrictions.eq("isDeleted", false));
    return (UomUnit) criteria.uniqueResult();
}
```
Filters only by primary key and `isDeleted`. No `organizationId` restriction, no `unitId` restriction — unlike the correctly-scoped sibling method `getAll(Integer orgId)` two lines above it, which does filter by organization. `UomUnitService.getById`/`update` (`UomUnitService.java:65,116`) both call this method and pass the `orgId` parameter to nothing — it is accepted by the controller, threaded through the service, and then silently dropped before it ever reaches a query.

**Auth layer — confirmed this is not compensated for anywhere else in the request pipeline:**
- `JwtTokenUtil.java:34-53` — the JWT embeds only a `unitId` claim. There is no `organizationId` claim anywhere in the token, by construction.
- `JwtTokenUtil.validateToken` (`:66-80`) checks: token signature valid, not expired, the `username` extracted from the token matches the real user looked up by the `userId` header, and the token's embedded `unitId` matches the `unitId` header sent with the request. This confirms the *caller's own identity* — it never checks the *resource being requested* against the caller's org or unit.
- `JwtTokenFilter.java` requires a valid, matching token for this endpoint (`/api/globals/uom/units/` is not in its ~428-entry exemption whitelist — confirmed via grep), so unauthenticated requests are correctly rejected. But the filter only validates the caller; the `{orgId}` and `{id}` path segments are entirely client-supplied and never cross-checked against the caller's actual org/unit membership anywhere in the chain.

## 2. Live-verified, both ends of the severity question

Per the explicit request to confirm precisely rather than assume:

- **No authentication at all:** `curl http://localhost:8080/api/globals/uom/units/1/1` with no Authorization header → **HTTP 401**. A valid session is required to reach the endpoint. This rules out the "completely unauthenticated" worst case.
- **Valid session, any claimed org:** using my own legitimate, unmodified session token (org 1, unit 1 — the only org/unit that exists in this dev DB), I created a real record (`id=5`), then called `GET /api/globals/uom/units/999999/5` and `PATCH /api/globals/uom/units/999999/5` — passing `999999`, an organization ID that doesn't exist anywhere in this database. **Both succeeded**: GetById returned the full record (HTTP 200), and Update actually renamed it to `"IDORTEST-MODIFIED-VIA-WRONG-ORG"` (HTTP 201). Confirmed via direct `psql` read that the database row was genuinely modified. Test row deleted after verification.

This dev database currently has only one real organization and one real unit, so a literal "Org A's session stole Org B's real data" demonstration isn't possible here — but the test above proves the actual mechanism precisely: the `{orgId}` URL segment carries zero enforcement weight for any value, real or fabricated. In any deployment with more than one organization (which is the norm this application is built for), any authenticated user's session — regardless of which org or unit they actually belong to — would be able to read and write every other org's unit-master records by enumerating sequential integer IDs, which this screen's own list view exposes directly.

## 3. Why this matters, and why it's still a real finding despite the mitigation

This is administrative reference data (units-of-measure codes like "mg," "tablet," "ml"), not patient or financial records — that materially caps the severity below anything patient-data-adjacent. But it's still a real, concrete horizontal-privilege-escalation defect: same-facility or cross-facility staff (any authenticated user at all, given no org check exists) can silently modify another organization's dispensing/stock-conversion reference data, which — per the audit's downstream-consumer trace — feeds real quantity-conversion math in pharmacy dispensing, procurement, and OP sales flows (`ComputeStockService.java`, `WalkInSalesService.java`). Corrupting another org's unit definitions this way could produce wrong dispensing/billing quantities silently, not just a cosmetic data problem.

## 4. What has and hasn't been verified

- **Confirmed via direct source read and live test:** the exact missing-scoping code, the auth layer's lack of any resource-level org check, and the practical exploitability of both `GetById` and `Update` using nothing but a legitimate session and a guessed/fabricated org value.
- **Not verified (no second org exists in this dev DB to test against):** the real-world blast radius across genuinely different organizations' data in a live multi-tenant deployment. The mechanism is proven; a true cross-tenant data-theft demonstration would require a second org's real data, which doesn't exist here.
- **Not yet checked:** whether this same missing-scoping pattern exists on other `UomUnitController`-style controllers built on the same `ResponseEntity<List<...>>` pattern (this was flagged as a possible systemic shape by the original Step 0/1 audit, D3, but that audit's cross-cutting finding was about the *broken 500-error-mapping* pattern specifically, not this IDOR pattern — whether the IDOR itself recurs elsewhere has not been checked and would need its own sweep).

## 5. Proposed fix (minimal, not yet implemented)

Add an explicit organization check in `UomUnitDao.getById`, matching the pattern `getAll` and `getUnitOfMesurementCountFromUnit` already use correctly in the same file:

```java
@Override
public UomUnit getById(Integer id, Integer orgId) {
    Criteria criteria = getSession().createCriteria(UomUnit.class);
    criteria.add(Restrictions.eq("id", id));
    criteria.add(Restrictions.eq("isDeleted", false));
    Criteria orgCriteria = criteria.createCriteria("organization");
    orgCriteria.add(Restrictions.eq("organizationId", orgId));
    return (UomUnit) criteria.uniqueResult();
}
```
(Signature change requires updating the `IUomUnitDao` interface and both call sites in `UomUnitService.getById`/`update`, which already have `orgId` available and already pass it — it's just discarded today.) A record that exists but doesn't belong to the requesting org should return not-found (matching this project's existing not-found conventions), not a permission-specific error that would leak whether a given ID exists in another org.

## 6. Options for the owner

1. **Fix now, on its own dedicated legacy branch, full Maker-Checker**, same process as every other BR fix this session — scoped narrowly to `UomUnitDao.getById`/service/controller signature changes, live-verified before/after (confirm the exact fabricated-org-999999 request from §2 now correctly fails).
2. **Hold, logged as a known gap**, if there's a reason to defer (e.g., this dev environment's single-org nature makes it low-priority relative to other open work) — but log the decision either way rather than letting it sit unflagged.
3. **Widen scope first**: before fixing just this one screen, spend a short cycle checking whether the same "accepts orgId, never uses it" pattern recurs on sibling `ResponseEntity`-style controllers, so a single fix pass could close several instances at once rather than fixing one and re-discovering the same class of bug screen-by-screen later.

No fix has been proposed or implemented beyond the sketch above. Recommend option 1 given the fix is small, well-understood, and directly informed by an already-confirmed live reproduction — but deferring to the owner on sequencing relative to the rest of tonight's open items.
