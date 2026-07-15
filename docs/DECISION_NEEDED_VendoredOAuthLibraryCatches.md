# Decision Needed: 7 exception-handling findings in vendored third-party OAuth library code (Pesapal integration)

**Status: RESOLVED 2026-07-05 — Owner approved Option A.** All 7 findings in `URLConnectionResponse.java`, `OAuthClient.java`, and `OAuthResponseMessage.java` were left functionally completely untouched — no throw, no logging change, no control-flow change. A one-line comment (`// Reviewed 2026-07 -- intentional, not a silent-swallow bug, see docs/DECISION_NEEDED_VendoredOAuthLibraryCatches.md`) was added at each of the 7 catch sites so the review is visible in the code itself. See `docs/BUSINESS_RULE_MANIFEST.md` (`BR-PENDING-global-billing-critical`) for the updated consolidated entry.

---

**Status: OPEN — awaiting owner decision.** No code has been touched for these 7 findings. They were surfaced during the Global-module billing-critical exception-handling fix (`docs/AUDIT_Global_exceptions.md`, `docs/REVIEW_global-billing-critical.md`) and deliberately excluded from that fix's routine batch. Unlike the other two open decision docs from this same fix cycle (`docs/DECISION_NEEDED_UnitServiceTariffMasterDaoImpl.md`, `docs/DECISION_NEEDED_LoyaltyCardValidationGoto.md`), none of these 7 fabricate a false success — they were held back because applying the standard fix would very likely **break working functionality**, not just add visibility to a real problem. Grouped into one doc because all three files are the same kind of case: unmodified-looking `net.oauth` library code (Apache-licensed, Netflix copyright header) vendored into the `com.param.global.pesapal` package to support the Pesapal payment-gateway integration, not application-authored billing logic.

---

## 1. `legacy/Global/src/main/java/com/param/global/pesapal/URLConnectionResponse.java` (3 findings)

- **Lines 76–79 and 85–88, `getHeaderField()` / `getHeaderFieldKey()`:** catch `NoSuchElementException` and `return null`, with the existing inline comment *"This violates the interface contract, but it happens... http://code.google.com/p/googleappengine/issues/detail?id=1945."* This `null` return is the **loop-termination signal** for header iteration, used in two places in this same file:
  ```java
  for (int i = 0; (value = getHeaderField(connection, i)) != null; ++i) { ... }
  ```
  (`getHeaders()`, and again in `dump()`). If either catch is converted to `throw new ServiceException(...)`, normal header parsing would throw partway through iterating a response's headers on any platform/JVM exhibiting the documented quirk — turning a working control-flow mechanism into a crash on an ordinary, successful gateway response, not just on genuine error cases.
- **Line 68, `openBody()`:** catches `IOException` and returns `null`. This is consistent with, not a deviation from, the base class's contract — `HttpMessage.openBody()` already defaults to returning `null` for "no body present," and `HttpMessage.getBody()` explicitly treats a `null` from `openBody()` as the normal "no body" case, not an error. Converting this to a throw would turn a legitimate "this gateway response has no body" outcome (e.g., certain HTTP status codes) into a hard failure.

## 2. `legacy/Global/src/main/java/com/param/global/pesapal/OAuthClient.java` (1 finding)

- **Line 366, inside `invoke(OAuthMessage, ParameterStyle)`:** when the HTTP response status isn't 2xx, the code builds the real, intended `OAuthProblemException problem`, then in a nested try/catch attempts to enrich it with a diagnostic signature-base-string parameter:
  ```java
  OAuthProblemException problem = response.toOAuthProblemException();
  try {
      problem.setParameter(OAuthProblemException.SIGNATURE_BASE_STRING, OAuthSignatureMethod.getBaseString(request));
  } catch (Exception ignored) {
  }
  throw problem;
  ```
  The `catch (Exception ignored) {}` only guards this best-effort annotation step — `throw problem` runs unconditionally right after, regardless of whether the annotation succeeded. Applying the standard fix here would replace the intended `throw problem` (the real HTTP-non-2xx error) with an unrelated `ServiceException` about a failed diagnostic-string computation — hiding the real error instead of surfacing it. This is the inverse of a silent swallow: it's intentional best-effort secondary work that must not derail the primary, already-correct throw.

## 3. `legacy/Global/src/main/java/com/param/global/pesapal/OAuthResponseMessage.java` (3 findings)

- **`toOAuthProblemException()`:** exists specifically to construct an `OAuthProblemException` diagnostic object by best-effort parsing of a response that is *already known to be an error*. The empty catches around `getParameters()` (`catch (IOException ignored) {}`, `catch (IllegalArgumentException ignored) {}`) and around `getBodyAsStream()`/`close()` (`catch (IOException ignored) {}`) are intentional "gather whatever diagnostic info you can, then return the problem object anyway" resilience — the variables are even named `ignored`. Applying the standard fix would not just add visibility; it would break the method's entire contract by throwing a `ServiceException`/`BillingException` mid-construction of the diagnostic object, instead of returning the intended `OAuthProblemException` to its caller (`OAuthClient.invoke`, item 2 above, which immediately does `throw problem`).

## Why this needs owner judgment rather than a mechanical fix

All 7 sit in vendored, third-party library code adapted into the Pesapal payment-gateway integration. In every case, the empty/`null`-returning catch is load-bearing — either as a documented platform-quirk workaround, a legitimate "no body" signal already handled by the base class contract, or deliberate best-effort diagnostic enrichment that must not derail an already-correct `throw`. Applying the codebase's standard "log and throw" fix here would very likely convert working gateway-response handling into a crash on ordinary responses, not surface a real hidden bug. This is a different kind of risk than the money-movement decisions in the other two open decision docs from this cycle — it's a technical/functional-correctness risk in vendored code, not a business-rule "what counts as success" question — but it still needs an explicit decision before anything is changed, since CLAUDE.md requires every local `try/catch` to eventually route to the centralized handler and none of these currently do.

## What we need from you

A decision on one of the following (or your own alternative) before any code is touched:

- **Option A:** Leave all 7 exactly as they are — accept that this vendored library's internal control-flow/contract behavior is out of scope for the "no silent swallow" rule, since these catches are not swallowing an application-level billing failure, they're implementing the library's own documented fallback contract.
- **Option B:** Add logging only, without changing control flow — e.g. `logger.warn(...)` (not `.error()`, since these aren't necessarily failures) inside each catch before the existing `return null;`/fall-through, so the underlying quirk/no-body/annotation-failure event is visible in the centralized log, while the method's return value and control flow stay byte-identical.
- **Option C:** Replace the vendored `net.oauth` library usage in the Pesapal integration with a maintained library or a smaller purpose-built HTTP client, eliminating this vendored code (and its idiosyncratic error handling) entirely — a larger, separate piece of work, not a quick fix.
- **Option D:** Something else.

No code will be touched here until you choose.
