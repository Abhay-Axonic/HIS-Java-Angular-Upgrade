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
