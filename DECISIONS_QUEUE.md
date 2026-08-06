# DECISIONS QUEUE — items requiring Abhay (owner/SME)

Night-shift supervisor protocol: blocked/owner-gated items are logged here, the loop pivots to
independent work, and this file is read aloud at the next "Morning Debrief". Items are numbered;
do not delete — mark status and append.

---

## 1. [OPEN] BR-173 webjar/swagger prefix decision (verification FAIL, not a regression)

- What: Task-1 step 4 ("webjar paths still serve under the prefix — swagger-ui loads") FAILS in the
  current ROOT-context deployment. The approved /HIS-WAR/webjars/ prefix matches a tree that does
  not exist here (real files under it -> 404). The actual webjar tree /webjars/** and
  /swagger-ui.html, /v2/api-docs -> 401 (no-token). Pre-rewrite filter had the same blind spot
  (contains("/HIS-WAR/webjars/...") never matched /webjars/**), so nothing regressed.
- Evidence: QF-2026-08-05-AC (docs/QA_FINDINGS.md). Login shell itself is fully verified
  (1,333 assets, 0 x 401). Zero frontend consumers of /webjars/; swagger is dev-tool-only.
- Question: (a) add /webjars/ prefix + /swagger-ui.html + /v2/api-docs exacts for ROOT dev
  convenience, (b) accept the /HIS-WAR-context coverage as-is (preprod template shape) and keep
  the no-op, or (c) drop the /HIS-WAR/webjars/ entry as dead weight. This changes the owner-
  approved prefix list, so it is your call.

## 2. [OPEN] Fresh JWT required for BR-173 Layer 3 (8h TTL expired)

- What: Layer 3 verification (six harness specs: sex, relation, marital, religion, occupation,
  priority; the four clinical live checks; coversheet + medication-history renders; login; final
  verify_deployed_regressions.py) is fully pre-flighted and token-ready — harness --self-test PASS,
  per-spec worktree pinning in place — but the token on disk has expired and there is no autonomous
  way to mint one.
- How: browser session at localhost:8080 -> DevTools -> Network -> copy the Authorization header
  value -> write to /tmp/fresh_token.txt with printf (no trailing newline, no "Bearer " prefix).
- Note: every authenticated request also needs userId and unitId request headers.

## 3. [OPEN] BR-173 Step 6 click-through + merge approval (Rule 9b, verbatim)

- What: fix/br173-default-deny is ready to merge. Branch diff vs main = ONE file
  (JwtTokenFilter.java, +174/-430, filter-only). Shell bucket deployed and verified: ALLOW 68/68
  not-401, DENY 14/14 -> 401 (incl. all previously-exempt writes), login assets 1,333/0 x 401,
  regression checker PASS on the deployed branch filter.
- Needs: your manual Step 6 click-through (login page end-to-end in a real browser, plus a
  representative authenticated screen) and verbatim merge approval per Rule 9b, then the
  post-merge sequence (rebuild from merged main, redeploy, second regression run, six harness
  specs, push) can execute.
- Caveat to weigh at approval: item 1 above (webjar prefix) — approve the merge with the finding
  open or with a decision on the prefix list.

## 4. [INFO, no action] Pre-existing working-tree state on fix/br173-default-deny, untouched

- .claude/worktrees/batch25 (gitlink) and docs/ANGULAR_PENDING_FINDINGS.md show as modified;
  20 untracked worktree dirs + SUPERVISOR.md. None are mine; all left untouched per discipline.

---

*Queue created 2026-08-05 21:40 BST by Hermes (night-shift supervisor). ntfy webhook alert not
fired: no ntfy topic is configured in ~/.hermes/config.yaml, the repo, or SUPERVISOR.md (the
placeholder says YOUR_SECRET_TOPIC_NAME) — guessing a public topic would post to a stranger's
channel, so the queue file is the durable alert. Recommend configuring the topic for future
shifts.*
