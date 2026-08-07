# Deploy Procedure — HIS Java/Angular (Tomcat)

*Written 2026-08-06 after the third deployment incident of the day. The pattern across all
three: **nobody had a written definition of what a complete deploy is.** This document is
that definition. It is governance, not a one-off — every future deploy follows it.*

---

## 1. The deployment model (what "deploying" means)

The running application is an **exploded WAR** at
`/opt/homebrew/opt/tomcat@9/libexec/webapps/ROOT/`:

| Path | What ships |
|---|---|
| `ROOT/WEB-INF/classes/` | the compiled Java classes |
| `ROOT/WEB-INF/lib/` | the dependency jars (incl. the Global/Entity module jars) |
| `ROOT/WEB-INF/web.xml` | the filter chain and context config |
| `ROOT/resources/assets/` | **the entire frontend** (JS/HTML/CSS — 1,724 files) |

The build produces the same structure at `legacy/HIS-WAR/target/HIS-WAR/` (the exploded WAR).

**A complete deploy ships all four layers together.** Class-level copying is a HOTFIX
procedure (section 4), never the default — it is exactly what let 25 frontend commits
accumulate in git while production ran the baseline (the drift incident).

## 2. The two deploy modes

| Mode | When | Ships |
|---|---|---|
| **FULL** (default) | any merge that touches the frontend, web.xml, or libs; weekly cadence | the whole ROOT |
| **HOTFIX** (emergency) | a single class must change NOW (e.g. a security filter) | that class + its inner classes ONLY, with the CRITICAL confirmation below |

## 3. FULL deploy — the procedure

1. **Build:** `cd legacy && mvn -q -pl HIS-WAR -am package -DskipTests`
   — exit 0 required; the exploded WAR is at `HIS-WAR/target/HIS-WAR/`.
2. **Stop:** kill the Tomcat process (not `brew restart` — see the Tomcat note: manual
   processes must be killed + restarted via `startup.sh`, then the NEW PID verified).
3. **Replace:** remove the old ROOT and copy the freshly built exploded WAR in its place:
   `rm -rf webapps/ROOT && cp -R legacy/HIS-WAR/target/HIS-WAR webapps/ROOT`
   (or drop the WAR file and let Tomcat explode it — the exploded-copy is preferred
   because it is explicit about what shipped).
4. **Start:** `bin/startup.sh`, then **the boot-time signal** (RULES.md, standing note):
   a normal boot is **~20 seconds**; a boot of **~5 seconds means the Spring context did
   NOT load** — every request will 404 and it will read as an app defect. Treat fast boot
   as a failed deploy and check `catalina.out` for the context error before anything else.
5. **Verify the boot:** `grep 'Server startup in' logs/catalina.out | tail -1` — confirm
   ~20s; then the shell + a pre-login path return real responses (not 404).
6. **Run the verification suite in the CANONICAL ORDER** (section 5) — the rate-limit
   verifier FIRST on the fresh deployment's clean buckets, before anything else touches
   the rate-limited paths.
7. **Frontend drift check (new, after every FULL deploy):** compare the deployed
   `ROOT/resources/assets/` against main's tree (the 1,724-file sweep). Zero differences
   expected. Any difference is either an unmerged live fix or stale drift — report it,
   do not assume.
8. **The CRITICAL frontend confirmation** (until the drift check proves itself): the
   deployed `loginController.js` must carry the finding-135 guard (`found && found.organizationLogo`)
   and the four Track-A roleId edits (`cookieObject.roleId` ×4). If the class-level hotfix
   path is ever used, these are NOT shipped by it — copy the file explicitly and say so.

### 3.1 Required environment settings (setenv.sh — OUTSIDE version control)

`libexec/bin/setenv.sh` carries TWO required settings. Both live outside version control
and must be re-applied after any machine rebuild or `brew upgrade` of tomcat@9:

1. `CATALINA_OPTS="-Xmx4g"` — without it, a webapps/ backup pile exhausts the default
   heap → the stuck-boot class (GC death spiral, CPU-pinned, no startup line).
   QF-2026-08-07-D.
2. `export JWT_SIGNING_KEY=...` — REQUIRED by JwtTokenUtil (the hardcoded-key
   remediation). Without it every `POST /adt/userLogin` returns 401 EMPTY at token
   generation: `ExceptionInInitializerError` → `IllegalStateException: JWT_SIGNING_KEY
   environment variable must be set` (catalina.out). The value is recorded in
   QF-2026-08-07-D's rebuild checklist and the 2026-08-07 18V session-log entry.

setenv.sh is sourced by catalina.sh for BOTH launchd and startup.sh runs. launchd-spawned
processes do NOT inherit shell exports — this file is the only place these settings
survive reboots. Rotating JWT_SIGNING_KEY invalidates live tokens (expected; tokens are
short-lived).

## 4. HOTFIX deploy — the procedure (emergency only)

1. Build (as above).
2. Copy the class AND **all its inner classes** — the inner-class discipline from RULES.md:
   `cp HIS-WAR/target/.../classes/com/param/filter/RateLimitFilter*.class .../ROOT/WEB-INF/classes/com/param/filter/`
   A partial copy (outer class only) produces `NoClassDefFoundError`, a failed webapp
   deploy, and the fast-boot/404 signature — it reads as a filter defect, not a deploy error.
3. If the fix touches a frontend file (a `.js`/`.html`), copy it explicitly from the built
   WAR and **confirm the markers** (the loginController guard/roleId check above).
4. Restart + the boot-time signal + the verification suite.
5. **Record the hotfix in the session log** with the note "class-level — the full deploy
   of this change ships with the next FULL deploy".

## 5. The verification suite (canonical order, after EVERY deploy)

Run against the fresh deployment, in this order — the order matters:

1. `python3 tools/verify_rate_limit.py` — **FIRST** (clean buckets; the exact 429
   boundaries at requests 31/11/11/6 + the positive-control UI login TOKEN-OK + the
   no-body-destroy (HIS-400) assertions. The BR-177 lesson: a 400 on a login-group
   target is a FAILURE, never "an envelope".)
2. `python3 tools/verify_deployed_regressions.py` — every closed BR's fix signature in
   the running jars.
3. `python3 tools/verify_allowlist_reality.py` — the caller-produced-form sweep (0 failures).
4. `python3 tools/verify_master_screen.py <screen> --token <fresh-token>` ×6 (sex, relation,
   marital, religion, occupation, priority).
5. `node tools/golden-trace/verify_permissions_bootstrap.js` — the real-path permissions
   bootstrap (a–d). NOTE: the module-1068 assertion is a KNOWN stop condition — the admin
   role's privilege data lacks the MRD rack/row/box family (confirmed 2026-08-06, a data
   gap, not a defect) — it is expected to flag until the privilege data is seeded.
6. `node tools/golden-trace/positive_control_login.js` — a real UI login must print
   TOKEN-OK.
7. The boot-time + shell checks from section 3.

A fresh token (re-issued from a working UI login) is required for the authenticated checks.

## 6. Failure signals — what a bad deploy looks like

| Signal | Meaning |
|---|---|
| boot ~5s instead of ~20s | the context failed to load — check catalina.out, redeploy |
| everything 404s | same class as the fast boot — the webapp never deployed |
| login-group calls return HIS-400 | the body was consumed before the controller — the BR-177 class |
| 429s on login before any burst | the rate-limit buckets were pre-consumed by an earlier sweep — order matters (section 5) |
| deployed frontend ≠ main's tree | drift — the last deploy was class-level or the file was hand-copied |
| loginController missing the guard/roleId | the frontend was replaced without the merged fixes — re-copy and confirm |

## 7. Incident history (why this document exists)

1. **The inner-class copy (06:40-ish, 6A deploy):** only `RateLimitFilter.class` was
   copied; `$TargetConfig` stayed behind → NoClassDefFoundError → fast boot → 404s →
   read as a filter defect. (RULES.md now carries the inner-class + boot-time note.)
2. **The loginController overwrite (13:0x):** a build-copy deployment replaced the
   live-deployed finding-135 guard with main's unguarded version → the login race
   returned. The guard was restored and is now MERGED to main (65299f3a).
3. **The frontend drift (all day):** post-baseline deploys copied only changed `.class`
   files, so 25 frontend commits (fixes for live bugs) never reached production. The
   1,724-file comparison exposed it: 11 files matching the baseline byte-for-byte.
   BR-177's lesson: a check that cannot fail the way it claims lets real regressions ship.
