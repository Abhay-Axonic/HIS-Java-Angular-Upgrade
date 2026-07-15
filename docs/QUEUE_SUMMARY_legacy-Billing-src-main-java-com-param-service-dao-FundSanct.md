## Checker Review — FundSanctionRequestDaoImpl.java

**Verdict: REJECT**

### What I checked

- `git diff -- legacy/Billing/src/main/java/com/param/service/dao/FundSanctionRequestDaoImpl.java`: 13 methods, all 26 flagged findings (matching the queue row `|26|26 empty catch`), changed from
  ```java
  } catch (HibernateException he) {
  } catch (Exception e) {
  } finally {
  }
  return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
  ```
  to
  ```java
  } catch (HibernateException he) {
      logger.error("<methodName> failed", he);
      throw new ServiceException(he.getMessage());
  } catch (Exception e) {
      logger.error("<methodName> failed", e);
      throw new ServiceException(e.getMessage());
  }
  ```
  with the now-unreachable `finally {}` blocks and trailing `return new Response(ERROR, ...)` fallback lines deleted. This is structurally the correct BR-006 pattern (log via the existing class-level `Logger`, then `throw new ServiceException(...)`, matching `TariffMasterDaoImpl.java` / BR-006 and `BillingMasterServiceImpl.java` / BR-007). `grep -c catch` = 26, `grep -c logger.error` = 26 — every catch site now logs, none are empty, no `printStackTrace`-only or no-op swallows remain.
- **Re-ran the compile myself**: `cd legacy && mvn -pl Billing -am compile -DskipTests -o` → **BUILD FAILURE**. 26 errors, all `cannot find symbol: class ServiceException`, all in this file (lines 77, 80, 98, 101, 124, 127, 143, 146, 293, 296, 350, 353, 368, 371, 411, 414, 432, 435, 453, 456, 474, 477, 527, 530, 548, 551). No other error types present.
- Root cause: the fix uses `throw new ServiceException(...)` but never added `import com.param.global.common.ServiceException;`. The file's existing imports (lines 1–34) include `com.param.global.common.Response`, `.IError`, `.ICommonConstants` from the same package, but not `ServiceException`. `ServiceException` lives at `legacy/Global/src/main/java/com/param/global/common/ServiceException.java` and is a `RuntimeException` — the right class, just not imported. This is a one-line, mechanical fix, but it means the module does not currently build.
- No new row was added to `docs/BUSINESS_RULE_MANIFEST.md` for this file. Per `CLAUDE.md`, replacing masked `Response(ERROR, ...)` (HTTP 200) returns with a thrown `ServiceException` (routed to the centralized handler, HTTP 500) is a Behavioral Change requiring an owner-approved manifest entry, the same as BR-006/BR-007/BR-008. None exists for `FundSanctionRequestDaoImpl.java`.

### Why this is a reject, not "approve with concerns"

The exception-handling pattern itself is correct and consistent with the approved BR-006 precedent — this is not a design problem. But per `RULES.md`'s Maker-Checker gate ("every backend exception-handling change" must be reviewed by a separate session *before* a human ever sees it) and the project's own standard (BR-006/007/008 entries all cite `mvn ... compile` → BUILD SUCCESS as a condition of approval), a change that does not compile cannot be approved. Additionally, the missing `BUSINESS_RULE_MANIFEST.md` entry means the owner has no record to sign off on this Behavioral Change, which `CLAUDE.md` requires unconditionally, independent of the compile issue.

### Recommended next step

1. Add `import com.param.global.common.ServiceException;` to `FundSanctionRequestDaoImpl.java`.
2. Re-run `mvn -pl Billing -am compile -DskipTests -o` and confirm BUILD SUCCESS.
3. Add a `BR-009` row to `docs/BUSINESS_RULE_MANIFEST.md` describing this file's 26 findings/13 methods, marked "Behavioral Change," pending owner approval — following the BR-006/007 template.
4. Re-submit for Checker review once both are done.
