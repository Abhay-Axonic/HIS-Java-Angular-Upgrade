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

---

**The antidote, once, plainly:** every check needs a way to fail. If you
cannot construct the failure, the check's PASS is not evidence. The twelve
above are the project's permanent list of checks that were once believed
and are now distrusted by default.
