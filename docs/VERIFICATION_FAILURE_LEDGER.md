# THE VERIFICATION-FAILURE LEDGER

2026-08-13. Twelve mechanisms in this project passed while structurally
incapable of detecting what they claimed. Each entry: the mechanism, what it
claimed to verify, what it structurally missed, and what caught it. This is
the most transferable thing the project has produced — the collection was
previously scattered across ~30 DQ entries.

The pattern shared by all twelve: a check whose PASS says nothing because
the check cannot FAIL on the defect it claims to cover. The antidote is
always a negative control — a way to prove the check can fail.

---

1. **THE VACUOUS TSC GATE.** *Claimed:* type safety. *Missed:* a route whose
   lazy-load references a wrong component class (cross-match: the route said
   `CrossMatchRegisterComponent`, the file exported `CrossMatchRegister` —
   tsc compiles the lazy import without checking the member until runtime);
   a fuzzy patch that silently destroyed the kitCreation route; a
   guard-less route. *Caught by:* the browser golden trace (the route
   rendered or it didn't) and the route-survival baseline (below). tsc is
   necessary, never sufficient — it exists in the gate list but verifies
   nothing about routing, guards, or survival.

2. **THE POSITIVE-ONLY ROUTE-TARGETS CHECK.** *Claimed:* routes resolve.
   *Missed:* a route that is MISSING (the check only walks the routes that
   exist) and a guard that is ABSENT (nothing in the file structure says a
   route SHOULD have one). *Caught by:* check_route_survival.py — the
   baseline-subset assertion (all 364 baseline routes must still exist after
   any change) — proven in both directions: PASS on the current state, and a
   synthetic file missing one route exits 1 and names the victim.

3. **THE SEEDED PERMISSIONS STORE.** *Claimed:* the route guard works.
   *Missed:* whether the guard passes because the session is real or because
   the harness itself wrote the permissions into localStorage before the
   check. A guard verified against its own seeded store can never fail.
   *Caught by:* the real browser login session and the owner's manual
   click-through — the only checks that exercise the guard against a
   genuine session.

4. **THE DEAD FILTER.** *Claimed:* the screen filters its data. *Missed:* a
   filter bound to a signal that nothing updates, or a template `@if` that
   shares none of the component's defaulting logic — rows render that the
   filter should have removed, or buttons hide while their header renders.
   (Transfer screen: the Accept button's template check didn't share the
   component's `transferStatusId ?? 4` default, so the header matched the
   probe while the button didn't.) *Caught by:* the live render — the
   snapshot's visible controls vs the probe's expectation — and the owner's
   click.

5. **THE SUCCESS-ONLY PROBE.** *Claimed:* the flow works. *Missed:* (a)
   error paths entirely — a write's empty catch returns an error envelope
   with NO information about what landed, and a success-only check is blind
   to half-commits (transfer W2/W3: error envelope + most state landed);
   (b) chained payloads — a per-endpoint probe cannot construct W2's payload
   because it is built from W1's RESPONSE object, not screen state; an
   API-only trace proves nothing the API traces haven't already. *Caught
   by:* psql after EVERY error (Amendment 10) and the golden trace through
   the whole chain in ONE browser session.

6. **THE FIXTURE-DEPENDENT TRACE.** *Claimed:* the screen's write works.
   *Missed:* that the trace ran against transient fixture rows the
   standalone screen cannot reach — the cross-match's golden trace passed
   (master id 22) while a real user's save failed ("id to load", fixtures
   absent); the same shape in bloodIssue. The trace proved the write's
   mechanics, not the screen's reachability, and the merge gate did not
   catch it. *Caught by:* the owner's click-through — twice, which is what
   turned it into a class. Rule now: a golden trace must run against seeded
   state a real user can reach, never transient fixtures.

7. **TIRITH FAIL-OPEN.** *Claimed:* the environment is safe to work in.
   *Missed:* the resolver itself being broken — the gate could PASS while
   the thing it gates on was not resolvable. *Caught by:*
   verify_tirith_resolvable.py — the check that asserts the resolver
   actually resolves.

8. **THE STALE BUNDLE.** *Claimed:* the probe hit the current code.
   *Missed:* a dev-server watcher frozen on an intermediate compile — the
   served bundle presented a false Access Denied and a stale compile-error
   overlay while the source was already fixed. Four instances of the
   environmental-fault-presenting-as-finding class. *Caught by:* the
   freshness gate (served bundle == current source) and the restart +
   re-probe ritual before believing any failure.

9. **THE TOKEN CACHE LIE.** *Claimed:* the probe is authenticated.
   *Missed:* `get_dev_token --check` returning LOGIN-OK while the cached
   token 401s, and a browser re-login silently invalidating the cache —
   which read as nine consecutive parks (an all-candidates 401 flood) before
   the environment-first diagnosis rule identified it. *Caught by:*
   re-minting on any 401, and reading the probe tool's OWN headers — the
   park_sweep post() sends token + userId/unitId/orgId, so bare-token
   scripts 401 where the sweep succeeds.

10. **THE 200+0 ENVELOPE (false-success masking).** *Claimed:* the write
    succeeded. *Missed:* "Added Successfully." with ZERO rows — the I/O
    charts master landed while the details DAO NPE'd and the service
    ignored the response; the masked-partial class (BR-039). The tells:
    200 + 0 rows = never ran; sequence advanced + row missing = rollback;
    is_called=false = never wrote. *Caught by:* the psql counts behind every
    success envelope — the only instrument that distinguishes a landed write
    from a lie.

11. **THE UNREAD TOOL / HARNESS SELF-DECEPTION.** *Claimed:* the measurement
    was what the report said. *Missed:* the BR-040 alarm — "writes accept no
    auth header" — which was FALSE: the sweep's post() had sent the cached
    token all along (the claim came from misreading the helper, and the
    twelve-instance re-measurement with the actual headers retracted it);
    plus harnesses that failed quietly: a leftover SQL with a top-level
    `(select)+(select)` syntax error that killed the sweep before its
    summary, pre-existing dev rows counted as leftovers, case-mismatched
    assertion patterns. *Caught by:* the verify-your-own-tools rule —
    re-checking every harness's preconditions and every tool's actual
    behavior before trusting its verdict (subagent work re-runs ALL gates,
    never trusts self-reports).

12. **THE SEED IDEMPOTENCY NO-OP (skip-vs-heal).** *Claimed:* the seed is
    reproducible. *Missed:* a `WHERE NOT EXISTS` guard that SKIPS rather
    than HEALS — the second run produces a DIFFERENT state from the first
    and nothing says so (the bag seeded before its donor existed was skipped
    on re-runs, leaving the donor unwired; the same class as the orphaned
    details of the teardown rule). *Caught by:* the harness's re-run
    row-count assertion and the heal UPDATE. Rule: any seed that wires a
    relationship must heal an existing row, not just skip inserting a
    duplicate.

13. **THE CONTRACT-INCOMPLETE VERDICT AS A PROBE STATEMENT (added 2026-08-13,
    the pass that moved the boundary twice).** *Claimed:* "the system rejects
    the request." *Missed:* that the rejection was MY payload's shape — the
    LIS Search family's 400s were `organizationId` vs the DTO's `orgId`/
    `orgUnitId` spelling (85 known fields, never read in full); the
    result-entry's 400/500 chain was one null DTO field at a time
    (isCompleted, createdDate, the details list); the diag-proc's 400s the
    TemplateReqDto's fields; the histo-get 404 the endpoint name
    (getReportDetails vs getReport); the incident-cat 404 the missing path
    params. SIX verdicts were reported as contract walls that were
    probe-shape statements — "I could not construct the request" dressed as
    "the system rejects the request." *Caught by:* the owner's insistence on
    the full-payload re-probe (the DTO's COMPLETE known-fields from the
    tomcat error + the controller's literal var data) — which turned two
    unknowns into one working screen + one 16th CONFIRMED phantom, and
    RETRACTED the LIS family's contract-wall verdict in favour of confirmed
    drift. Rule: a contract-incomplete verdict is a statement about the
    probe's payload, never about the backend; it may only be reported after
    the DTO's complete known-fields list and the controller's literal
    payload have been tried.

14. **THE STATIC TEMPLATE SCAN AS A SHELL VERDICT (added 2026-08-13, the
    nursing trio — 3 for 3 misclassified).** *Claimed:* "the template has
    zero data calls, therefore the screen is a shell." *Missed:* the nursing
    trio (medicationErrorReporting, UtiReporting, PatientFallReporting) are
    REAL clinical write screens — patient search, risk-factor checklists,
    signs/symptoms, laboratory criteria, GCS before/after, Draft/Save/
    Disable — whose fields live in MARKUP, not in a loaded feed, so a scan
    for data calls finds nothing and concludes there is nothing there.
    *Caught by:* a person rendering each screen (the owner). Rule: a static
    template scan cannot distinguish a shell from a form. Only a render
    can. A "shell" verdict requires a live render of the screen; every
    screen classified as a shell on a static scan alone must be
    re-examined by render.

15. **THE BIGGEST NUMBER IS THE PROBE ARTIFACT (added 2026-08-14,
    owner-named — the fourth time a large finding evaporated under a
    corrected probe).** *Claimed:* the 293 "unresolved service paths"
    across ~90 screens (the ambulance-count class at scale). *Missed:* the
    matching method — a path index built from @RequestMapping greps — was
    what failed, not the backend. The SPA's OWN calls, captured during a
    render, returned 200 for insurance-master's 5 "unresolved" paths (the
    index missed path-param/trailing-slash spellings). The same shape
    before it: the shell class (11-for-11 wrong), the contract walls
    (6-for-6), the systemic proxy gap (wrongly diagnosed). *Rule:* a large
    count produced by a matching method — a regex, a path index, a
    template scan — is a hypothesis about the METHOD until a live call or
    a render confirms a sample of it. Every time a static method produced
    a number above ~50, most of it was wrong. The drift map's PROBABLE
    band is the one surviving static count, and it survives because 3-of-3
    live-write confirmations held. A number without a live-confirmed
    sample is not yet a finding.

---

**The antidote, once, plainly:** every check needs a way to fail. If you
cannot construct the failure, the check's PASS is not evidence. The fifteen
above are the project's permanent list of checks that were once believed
and are now distrusted by default.

## 16. DISABLED-INPUT INVISIBILITY (owner-named, 2026-08-14, genericMaster)

A component-state drive sets the form's signal directly and NEVER touches an
input's disabled state — so a disabled field is invisible to the drive, and a
screen can be UNUSABLE (a required field the user cannot fill by any means)
while every endpoint it calls returns 200. The drive's "all endpoints
healthy" says nothing about the screen's usability when the UI state (disabled
fields, auto-generated values) carries the logic. The antidote: the drive's
verdict covers the endpoint, not the screen — a human render+click is the
only screen-level verdict (the same boundary as the fixture-dependent trace).
