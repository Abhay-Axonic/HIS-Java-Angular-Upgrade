# RUNBOOK — Backend build, image, and deploy (his-local-tomcat)

**Status:** reconstructed from the owner's investigation, 2026-08-19. Nothing in this
procedure was recorded anywhere before this file. The facts below were captured from the
live environment (docker inspect, docker history, build runs) — do not re-derive them.

**Hard rule:** the container runs the ONLY live backend. Tag the current image before every
deploy (see Rollback). A broken WAR in this container blocks all work.

---

## 1. Build (host-side check only)

- Maven root is `legacy/` — **NOT the repo root** (`mvn` at the repo root fails with
  "no POM in this directory").
- From `legacy/`:
  ```bash
  mvn -q package -DskipTests
  ```
  Completes in ~25 seconds. Produces `legacy/HIS-WAR/target/HIS-WAR.war` (~489MB).
- **The image build does NOT use this host-built WAR.** `Dockerfile.standalone` is
  multi-stage and copies from `/build/HIS-WAR/target/HIS-WAR.war` — i.e. it compiles inside
  the builder stage. The host build is useful only for checking the code compiles.

## 2. Image

Built from `legacy/Dockerfile.standalone`:

- Builder stage: `eclipse-temurin:17-jdk-jammy`.
- Runtime stage: `tomcat:9.0-jdk17-temurin-jammy` — **UNPINNED**: a rebuild may pull a newer
  9.0.x than the 9.0.119 currently running.
- Image layers (from `docker history`):
  1. removes `webapps/*`
  2. copies the WAR to `ROOT.war`
  3. copies `devops/jdbc.local.properties`
  4. sets `JAVA_OPTS`
  5. writes `healthcheck.html`
  6. `HEALTHCHECK curl http://localhost:8080/healthcheck.html`
  7. `EXPOSE 8080`

### The dev.Dockerfile trap

`devops/tomcat/dev.Dockerfile` (and the other `devops/tomcat/*.Dockerfile` files) are for
**OTHER environments** — `dev.Dockerfile` is Tomcat **8.0.51/JRE8** and would **NOT**
reproduce the running backend. The correct Dockerfile is `Dockerfile.standalone`, period.

## 3. The running container (captured via docker inspect)

| property | value |
|---|---|
| container | `his-local-tomcat` |
| image | `his-local-tomcat:latest` |
| port | `8081 -> 8080/tcp` |
| network | `his-local-net` |
| restart | `unless-stopped` |
| volume | `his-local-tomcat-logs -> /usr/local/tomcat/logs` |
| env `JAVA_OPTS` | `-Xmx2g -Xms512m` |
| env `JDBC_URL` | `jdbc:postgresql://postgres:5432/HIS-SME` |
| env `JDBC_USER` | `hisdev` |
| env `JDBC_PASSWORD` | `hisdev_local_2024` |
| env `LOCAL_UI_DEV_MODE` | `true` |
| runtime | Tomcat 9.0.119, JDK 17.0.19 |

### Reconstructed `docker run` (from the inspect output above)

```bash
docker run -d \
  --name his-local-tomcat \
  --restart unless-stopped \
  -p 8081:8080 \
  --network his-local-net \
  -v his-local-tomcat-logs:/usr/local/tomcat/logs \
  -e JAVA_OPTS="-Xmx2g -Xms512m" \
  -e JDBC_URL=jdbc:postgresql://postgres:5432/HIS-SME \
  -e JDBC_USER=hisdev \
  -e JDBC_PASSWORD=hisdev_local_2024 \
  -e LOCAL_UI_DEV_MODE=true \
  his-local-tomcat:latest
```

## 4. Deploy (the numbered procedure)

**Invariant — the whole strategy in one line: `:latest` = last known good; dated tags =
candidate builds.** `:latest` is only ever moved to an image that has been verified; a
candidate build gets a dated tag and is never `:latest` until it proves itself. That is what
makes the rollback a single command (§5).

1. Build the candidate with an **explicit dated tag** (never `:latest` — `:latest` still
   points at the image the running container is on, and must not be overwritten mid-deploy):
   ```bash
   cd legacy && docker build -f Dockerfile.standalone -t his-local-tomcat:<YYYYMMDD-HHMM> .
   ```
2. Recreate the container from **that dated tag** with the run configuration in §3 (the
   exact `docker run` command above, with `his-local-tomcat:<YYYYMMDD-HHMM>` in place of
   `his-local-tomcat:latest` — or `docker compose`/the original run script if the server has
   one; **no compose file exists in either repo** — see Open Questions).
3. Verify: the `HEALTHCHECK` curls `http://localhost:8080/healthcheck.html` inside the
   container; from outside, `http://localhost:8081/healthcheck.html` (the mapped port).
4. Verify a real screen end-to-end (a login + one save) before calling the deploy done.
5. **Only after successful verification**, move `:latest` to the verified image — so
   `:latest` always means "last known good", never "most recently built":
   ```bash
   docker tag his-local-tomcat:<YYYYMMDD-HHMM> his-local-tomcat:latest
   ```
   If the build succeeds but the new container fails verification, `:latest` was never
   touched — it still points at the previous good image.

## 5. Rollback

- **Rollback = recreate the container directly from `:latest`** (the last known good —
  untouched by any failed candidate), with the same run configuration as §3:
  ```bash
  # docker run ... his-local-tomcat:latest   (per §3, image = his-local-tomcat:latest)
  ```
  Or, if a specific earlier build is wanted, from that dated tag — **no tag mutation during
  an incident; the re-tag-then-recreate sequence is gone**.
- The pre-correction snapshot **`his-local-tomcat:rollback-20260819`** (id `d90eaa32292d`)
  still exists as a one-off from before this strategy; it can be used the same way (direct
  recreate), but the dated-tag discipline above supersedes it for future deploys.


## 6. Open questions (unknowns, deliberately not guessed)

- **JAVA_OPTS discrepancy (flagged prominently):** the image bakes `JAVA_OPTS` with five
  `--add-opens` flags (`java.base/java.time`, `java.security`, `java.security.cert`,
  `java.security.interfaces`, `java.security.spec` — the Java 17 module-access workarounds).
  The RUNNING container overrides `JAVA_OPTS` with just `-Xmx2g -Xms512m`, dropping all of
  them. Anyone recreating the container must decide deliberately which `JAVA_OPTS` to use.
  **It is not known whether the app still needs the `--add-opens` flags** — the running
  container works without them, but whether a fresh JDK-17 runtime needs them was not
  verified.
- No compose/run script is recorded for the container (the run was reconstructed from
  `docker inspect`).
- Whether the app (as opposed to the container) is healthy under the overridden JAVA_OPTS
  beyond the healthcheck page was not verified.
