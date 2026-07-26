# Decision Needed: `#/accessControl` has no permission gate at all, and the privilege-management REST API has no server-side authorization either — any logged-in user, of any role, can read (and plausibly write) the full role-to-module privilege grants for the organization

**Prepared:** 2026-07-26, fast-tracked at the owner's explicit request, same urgency class as `docs/DECISION_NEEDED_HardcodedJwtSigningKey.md`. Found during batch 20's Step 1 audit of the `accessControl` screen (part of the ongoing Angular rebuild), then live-verified end-to-end per the owner's explicit request for a definitive answer before any further work proceeds. No code has been touched to produce this document — proposal and severity assessment only, as instructed.

**Headline — this is not the "permission 677 instead of 676" mismatch the owner asked me to confirm. It's worse: the `accessControl` route has *no* permission check whatsoever, and the backend privilege-management API it (accidentally) exposes has no server-side authorization check either.** The frontend gate that was supposed to protect this feature is not merely misrouted — for this specific URL, it doesn't exist at all. And even if it did, it wouldn't be the actual line of defense: nothing stops any authenticated user from bypassing the frontend and calling the raw REST endpoints directly.

---

## 1. Direct answer to the owner's question

> Does this mean a user WITHOUT permission 676 could currently reach Role-Permissions functionality anyway, as long as they have permission 677?

**No — the real situation is broader than that framing.** A user doesn't need permission 677 either. **Any authenticated user — logged in with any role, holding any permissions or none beyond basic login — can reach it**, because:

- The `accessControl` ui-router state (`app.js:17203-17208`) has **no `resolve` block at all**. Confirmed live: navigating to `#/accessControl` with a real, authenticated session loads the page directly — no `accessDenied` redirect, no permission prompt, nothing.
- The Dashboard Quick Links tile that's the only *visible* entry point does check `checkAccess(677)` — but that only hides or shows a clickable link in the dashboard UI. It is not a route guard. Typing or navigating to `#/accessControl` directly bypasses it entirely, for anyone with any active login session.
- Permission 677 (the ID gating the quick-link tile) actually belongs to a *different* screen (`newAccessControl`) — it was never the correct gate for this feature to begin with. Permission 676 (the ID the owner asked about) belongs to yet another unrelated screen (`userAuthentication`). Neither ID is actually enforced anywhere in the path that matters.

So the honest answer isn't "677-holders get in when they shouldn't" — it's "the concept of 'holding a permission' is irrelevant to reaching this route at all."

## 2. What "reaching it" actually exposes — confirmed live, not inferred

Because of the separate controller-registration collision documented in this project's Step 1 audit (two files both register `.controller("accessControlController", ...)`; script load order makes the real 373-line Role-Permissions controller silently win over a decorative stub), navigating to `#/accessControl` **actually instantiates the real Role-Permissions controller**, not a harmless empty demo.

Live-verified in this session, using nothing but browser devtools against an already-authenticated session:

```
permissionListLen: 1
permissionSample: {"organizationId":1,"privilegeId":1,"moduleType":null,"roleId":1,"unitId":1,
  "modules":[{"moduleType":1,"name":"OPD","id":1,"parentId":0},
             {"moduleType":2,"name":"Registration","id":1,"parentId":0}, ...]}
```

**On mere page load — zero clicks, zero role selection, no user action beyond navigating to the URL — the controller's `getPermissions()` call fires automatically and populates the browser's live Angular scope with the real, current privilege/module grant list for role_id=1 in this organization.** Any authenticated user, regardless of their own role or permissions, can read this directly from their own browser's devtools console (exactly the technique used to confirm it here). This is not a hypothetical exploit path requiring special tooling — it's data sitting in an ordinary in-browser object, reachable by anyone who opens devtools after visiting an unguarded URL.

## 3. The frontend gap is not even the real line of defense — the backend has none either

I checked `legacy/Global/src/main/java/com/param/privilege/controller/PrivilegeController.java` directly for any server-side authorization check — `@PreAuthorize`, a `checkAccess`-equivalent, a module-ID check, anything gating who may call these endpoints beyond being logged in at all:

```
grep -n "checkAccess\|hasPermission\|@PreAuthorize\|moduleId\|authoriz" PrivilegeController.java
→ (no matches)
```

**Every endpoint on this controller — list, get-by-role, create, update, delete — has no authorization check beyond whatever the app's generic JWT authentication filter already provides (i.e., "is this a valid login session at all").** This means the frontend's broken permission gate was never the actual security boundary to begin with: even a perfectly-wired frontend guard on `#/accessControl` would not have stopped a technically capable user from calling `GET /privilege/1/1`, `POST /privilege`, `PUT /privilege`, or `DELETE /privilege/role/1/1/{roleId}` directly (e.g., via curl, with nothing but their own ordinary login token) and reading or rewriting **any role's** module/privilege grants for the organization — not just role_id=1, and not limited to whatever this one broken screen happens to expose.

## 4. What is and isn't reachable through the broken screen's own UI specifically

To be precise about this one screen's own blast radius (separate from point 3's broader API gap): the template actually rendered at `#/accessControl` (`views/common/access-control.html`, the decorative tree-editor mockup) has **no wired UI element** calling any of the real controller's mutating functions:

```
hasSaveClick: false      (savePermissions)
hasDeleteClick: false    (deletePermissions)
hasGetPermClick: false   (getPermissions -- fires automatically on load instead, not via click)
hasInitModalClick: false (initModal -- role selection)
hasRoleChangedClick: false (roleChanged)
```

So a casual user clicking around this specific broken screen cannot trigger a save or delete through the visible UI — the visible buttons do other, unrelated things (e.g., Save opens the global logout-confirmation modal). **But** the read-exposure in §2 happens automatically regardless, and any user willing to open devtools (or, per §3, skip the frontend entirely) has full read and write reach into privilege data that has nothing to do with whether they hold module 676, 677, or any permission at all.

## 5. Severity assessment

**High — a real, currently-live privilege-management exposure, not a theoretical one.** Scoped precisely:

- **Confirmed, automatic, zero-effort data exposure**: any logged-in user of any role gets the current role's full module/privilege grant list pushed into their browser on a routine, discoverable navigation.
- **Confirmed absence of backend authorization**: the actual privilege-management REST API can be read and (per its own code, though not exercised here to avoid mutating real data) written by any authenticated session regardless of role, independent of the frontend bug entirely.
- **Not** an unauthenticated/anonymous exposure (unlike the Qmslogo finding) — a valid login session is still required to reach any of this. That's the one boundary that does hold.
- **Not** exploitable through the broken screen's own visible UI for writes (per §4) — but that's a weak mitigation given §3's finding that the UI was never the real boundary.

This is a lower bar to clear than the JWT signing key finding (that one requires nothing but having read the source; this one requires an actual valid login, of any role) — but it means **every currently-logged-in user of this application, regardless of intended role, already has read access to the organization's full privilege model, and the backend offers no additional resistance if they go further.**

## 6. Proposed fix directions (not implemented — for review only, per instruction)

1. Add a real `resolve` permission guard to the `accessControl` state — but first resolve *which* feature this route should even serve (see the already-logged scope correction: the owner has decided to rebuild the Angular replacement against `accessControl1`'s real Role-Permissions logic, not this decorative mockup). For legacy itself, the narrowest fix is likely to point the Quick Links tile and any direct-URL access at `accessControl1`'s already-correctly-gated route (permission 673) instead, and retire the broken `accessControl` state/stub controller pair entirely.
2. Add real server-side authorization to `PrivilegeController.java` — every endpoint needs an explicit module-permission check (matching whatever the correct permission ID for privilege management turns out to be, once 673/676/677/908's actual intended meanings are reconciled), not just JWT-valid-session authentication. This is the more structurally important fix of the two, per §3 — without it, any frontend-only fix leaves the real exposure open.
3. Resolve the underlying permission-ID confusion (673 vs. 676 vs. 677 vs. 908) as its own cleanup — right now none of the four IDs floating around this feature area are consistently the "real" one everywhere they're referenced.

## 7. What I need from you

1. **Priority relative to other currently-open DECISION_NEEDED documents** — you set the fast-track framing; confirming where this sits.
2. **Whether to prepare an actual fix proposal/diff now**, or hold this as documentation-only until the accessControl Angular rebuild work (already approved, targeting `accessControl1`'s logic) reaches a point where you want the legacy-side gap addressed too.
3. **Whether this pattern (route has no `resolve`, backend controller has no authorization check beyond authentication) should be swept for elsewhere in the codebase** — this session's audits have already surfaced isolated instances of missing route guards on other screens (e.g., `growthChartMaster`, logged separately), but this is the first time it's been confirmed to pair with a *backend* authorization gap on the same feature, which is a materially more serious combination than either alone.

No code has been touched to produce this document. Awaiting your direction.
