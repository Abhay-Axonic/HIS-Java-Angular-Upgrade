# Local Dev Environment Setup Notes

Not committed as source changes — these are local-only runtime fixes needed to get this environment working, mirroring what real deploy scripts (legacy/devops/smeone, nchpreprod) already do automatically.

## 1. ROOT_URL must be blanked for local ROOT-context deployment

`legacy/HIS-WAR/src/main/webapp/resources/assets/js/config/url.config.js` ships with:
```js
var ROOT_URL = "/HIS-WAR";
```
This is a placeholder meant to be patched by deploy scripts (see `legacy/devops/smeone:25`, `nchpreprod:5`) via:
```bash
sed -i 's/var ROOT_URL.*/var ROOT_URL="";/g' url.config.js
```
Our local `his-local-tomcat` container deploys to the `ROOT` context (not `/HIS-WAR`), so this substitution must be applied manually. Without it, every frontend API call 404s.

**To apply:**
```bash
cd ~/Projects/legacy-modernization
sed -i 's/var ROOT_URL.*/var ROOT_URL="";/g' legacy/HIS-WAR/src/main/webapp/resources/assets/js/config/url.config.js
docker cp legacy/HIS-WAR/src/main/webapp/resources/assets/js/config/url.config.js his-local-tomcat:/usr/local/tomcat/webapps/ROOT/resources/assets/js/config/url.config.js
```
Do NOT commit this change to `url.config.js` — it will be reverted by `git checkout` on the next pull. Re-apply after any fresh checkout/redeploy.

## 2. Tomcat maxHttpHeaderSize increased (local dev safety margin)

Added `maxHttpHeaderSize="65536"` to the port-8080 connector in the container's `/usr/local/tomcat/conf/server.xml` (line ~75), to give headroom while BR-041 Phase B (slimming `cookieObject`/`cookieObjectAppConfig`) remains outstanding. Applied via `docker exec his-local-tomcat sed -i ...` — not persisted outside the container; re-apply if the container is recreated.

## 3. Known working local login

`admin` / `12345`, Unit: DEMO (org auto-fills as DEMO). Requires `t_user_unit_mapper` row for user_id=1/unit_id=1 (see BR-038/039 for other prerequisite tables).

## 4. constants.js also needs root blanked (sibling to ROOT_URL fix above)

`legacy/HIS-WAR/src/main/webapp/resources/assets/js/constants/constants.js` has its own, separate hardcoded `/HIS-WAR` prefix:
```js
var root = '/HIS-WAR';
```
Same root cause as `url.config.js`'s `ROOT_URL` (see item 1 above) -- this file is patched by the same deploy scripts (`legacy/devops/smeone:26`, likely others) but was missed when we fixed `url.config.js` earlier, since it wasn't the file blocking login specifically. It blocks `CONSTANTS.*`-routed API calls app-wide (discovered via Pharmacy/Billing E2E testing on 2026-07-09 -- BR-057).

**To apply:**
```bash
cd ~/Projects/legacy-modernization
sed -i "s/var root = '\/HIS-WAR';/var root = '';/" legacy/HIS-WAR/src/main/webapp/resources/assets/js/constants/constants.js
docker cp legacy/HIS-WAR/src/main/webapp/resources/assets/js/constants/constants.js his-local-tomcat:/usr/local/tomcat/webapps/ROOT/resources/assets/js/constants/constants.js
```
Do NOT commit this change -- same as `url.config.js`, it will be reverted by `git checkout` on the next pull. Re-apply after any fresh checkout/redeploy, alongside the `ROOT_URL` fix.

## 5. jdbc.properties deployed in container pointed at a REAL REMOTE SERVER (fixed 2026-07-11, see BR-074)

The deployed `jdbc.properties` inside `his-local-tomcat` had its active `jdbc.url`/`username`/`password` pointing at a real remote QA Postgres server (`3.7.26.135:12316/HIS-QA`, user `HIS-DEV`), not our local DB. This appears to be stale config from before this local setup was created, or an unrelated leftover — NOT something introduced by our work tonight. Verified via `pg_stat_activity` that the running JVM was, throughout tonight's testing, genuinely connected to our real local DB (`hisdev` user, Docker-internal IP) -- this stale file was inert until a restart, but any future restart would have picked up the dangerous remote config.

**Fixed by deploying corrected values directly into the running container:**
Note: from INSIDE the his-local-tomcat container, Postgres is reachable at `his-local-postgres:5432` (Docker-internal hostname/port) -- this differs from the host-machine connection string (`localhost:5434`) used in all our `psql` commands throughout this documentation.

**This fix is currently only applied to the running container, not to any tracked source file** (the tracked `legacy/HIS-WAR/src/main/resources/jdbc.properties` correctly uses a placeholder, `CHANGE_ME_SET_LOCALLY_NOT_IN_GIT`, and is not the deployed file's actual source — the deployment/build process substitutes real values, similar to the `ROOT_URL`/`root` substitution pattern in items 1 and 4 above). If this container is ever rebuilt from a fresh WAR/image, this same fix will need to be re-applied. Real credential rotation for the exposed remote-server passwords found in this file (see BR-074) is a separate, still-outstanding action item.

## 6. Tomcat is now a native Homebrew process, not the Docker container items 1/2/4/5 assume -- `brew services` restart/stop is unreliable, use `catalina.sh` directly (found 2026-07-30)

**Environment discrepancy worth flagging:** items 1, 2, 4, and 5 above all reference `docker exec his-local-tomcat` / `docker cp ... his-local-tomcat:...`. As of tonight (2026-07-30), this machine's Tomcat is a **native Homebrew install** (`tomcat@9`, `/opt/homebrew/Cellar/tomcat@9/9.0.120/libexec/`), deployed as an exploded `webapps/ROOT/` directory, managed (nominally) via a per-user launchd agent (`~/Library/LaunchAgents/homebrew.mxcl.tomcat@9.plist`) -- there is no `his-local-tomcat` Docker container running. Whether this machine migrated off Docker at some point, or this is a different machine/session than whichever one wrote items 1/2/4/5, wasn't investigated -- flagging so a future session doesn't waste time hunting for a container that isn't there. If Docker-based Tomcat is still in use elsewhere, treat this item as machine-specific.

**The actual quirk:** `brew services restart tomcat@9` and `brew services stop tomcat@9` both reliably print success (`Successfully started/stopped ...`) without actually cycling the underlying Java process. Confirmed by checking the JVM's process start timestamp (`ps -eo pid,lstart,command | grep catalina.startup.Bootstrap`) before and after a "successful" restart -- the PID and start time were unchanged. A naive redeploy workflow (copy new jar into `WEB-INF/lib/`, `brew services restart`, test) would silently test against the **old, already-running** code and produce a false "verified" result. This was caught once this session (see BR-117's process note) before trusting a live-verification result that would have been wrong.

**Working alternative, used for the rest of this session's live-deploy-verify cycles:**
```bash
# Stop: brew services stop doesn't reliably kill it -- find and kill the actual PID
ps -eo pid,command | grep "catalina.startup.Bootstrap" | grep -v grep | awk '{print $1}' | xargs -I{} kill {}
sleep 5

# Start: catalina.sh directly, with JAVA_HOME set explicitly (not on PATH in a plain shell here)
export JAVA_HOME=/opt/homebrew/Cellar/openjdk@21/21.0.11/libexec/openjdk.jdk/Contents/Home
export CATALINA_HOME=/opt/homebrew/Cellar/tomcat@9/9.0.120/libexec
export CATALINA_BASE=/opt/homebrew/Cellar/tomcat@9/9.0.120/libexec
cd "$CATALINA_HOME"
nohup ./bin/catalina.sh run > /tmp/tomcat_manual_start.log 2>&1 &
disown
sleep 25   # cold start is slow; confirm rather than assume

# Always confirm the restart actually happened before trusting any live test after it
ps -eo pid,lstart,command | grep -i "catalina.startup.Bootstrap" | grep -v grep
curl -s -o /dev/null -w "%{http_code}\n" http://localhost:8080/
```
`nohup ... &` followed by `disown` is needed so the process survives the end of the shell tool's command (otherwise it gets killed when the invoking command returns). Manual starts write to the file passed after `>`, not to the usual `logs/catalina.out` -- check that file (or wherever it was redirected to) for exceptions after a manual start, not `catalina.out`, or you'll be looking at stale output from the last container/launchd-managed run.

**Side effect worth knowing:** restarting the JVM this way invalidates any existing browser session (JWT tokens issued before the restart stop validating afterward -- not yet root-caused, possibly a non-persisted/per-boot signing key; see `fix/jwt-signing-key-externalize` if related). Anyone testing across a redeploy needs to log back in after each restart.
