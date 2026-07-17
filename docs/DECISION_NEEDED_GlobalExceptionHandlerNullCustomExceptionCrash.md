# Decision needed: fix the null-`CustomException` crash inside the centralized exception handler itself

*Blueprint (docs only, no code applied). 2026-07-17. Found live during Step 5's wave-1 smoke pass (`BR-PENDING-globalexceptionhandler-null-customexception-crash`), fast-tracked for its own proposal now per owner instruction. No code has been changed.*

---

## The problem, in one sentence

The centralized exception handler — the single place RULES.md requires every error to be safely routed through — itself throws an uncaught `NullPointerException` on a fairly common path, silently swapping the response the client actually receives for a differently-shaped one, and silently double-counting the incident against the exception counter this session just built for the circuit breaker.

## Where, precisely

`legacy/HIS-WAR/src/main/java/com/param/exception/GlobalExceptionHandler.java:118-135`, inside `handleAllExceptions`'s final `else` branch (the generic catch-all for any exception that isn't a broken-pipe, `MethodArgumentNotValidException`/`HttpMessageNotReadableException`, `InvalidFormatException`, or `HttpRequestMethodNotSupportedException`):

```java
String correlationId = ErrorCorrelationUtil.newCorrelationId();
logger.error(ErrorCorrelationUtil.tag(correlationId, "exception log consider"), ex);
int res = handler.getHandler(request).toString().indexOf("org.springframework.http.ResponseEntity");
...
if (res > 0)
    return new ResponseEntity<Object>(new ErrorResponse("HIS-1001", ErrorCorrelationUtil.clientMessage(correlationId), null),
            HttpStatus.INTERNAL_SERVER_ERROR);
else
    return new ResponseEntity<Object>(new Response("error", ERR_500,
            ErrorCorrelationUtil.clientMessage(correlationId), null, null, null), HttpStatus.INTERNAL_SERVER_ERROR);
```

`res > 0` is true whenever the resolved controller method's own declared return type is `ResponseEntity<...>` (that's what `handler.getHandler(request).toString()` reports). **195 files in this codebase declare at least one `public ResponseEntity<...>` controller method** — this is not a narrow edge case, it's the majority shape of this codebase's REST layer.

When `res > 0`, the handler constructs `new ErrorResponse("HIS-1001", <message>, null)` — passing a literal `null` as the third argument.

## Root cause: which constructor `null` resolves to, and why it crashes

`legacy/Entity/src/main/java/com/param/common/dto/ErrorResponse.java` has two constructors whose third parameter could both accept a `null` literal:

```java
public ErrorResponse(String code, String message, Exception ex) {
    super();
    StringWriter writer = new StringWriter();
    ex.printStackTrace(new PrintWriter(writer));   // <-- unconditional, both constructors
    this.code = code;
    this.message = message;
    this.summary = ex.getMessage();
    this.stackTrace = writer.toString();
}
public ErrorResponse(String code, String message, com.param.common.dto.CustomException ex ) {
    super();
    StringWriter writer = new StringWriter();
    ex.printStackTrace(new PrintWriter(writer));   // <-- unconditional
    this.code = code;
    this.message = message;
    this.summary = ex.getMessage();
    this.stackTrace = writer.toString();
    this.errors = ex.getErrors();
}
```

`com.param.common.dto.CustomException extends Exception`, so it's the more specific of the two applicable types for a bare `null` literal — Java's overload resolution picks the **most specific** applicable method, so `new ErrorResponse("HIS-1001", <message>, null)` resolves to the `CustomException`-typed constructor, not the `Exception`-typed one. (Note: `GlobalExceptionHandler.java` itself imports a *different* class also named `CustomException`, `com.param.global.service.items.CustomException` — an unrelated coincidence-of-naming; the constructor actually invoked is the `com.param.common.dto.CustomException` one, referenced by its fully-qualified name inside `ErrorResponse.java`.)

Either constructor calls `ex.printStackTrace(...)` and `ex.getMessage()` unconditionally, with no null-check. With `ex == null`, the first of those calls throws a `NullPointerException` immediately.

## Why `null` is being passed here at all — this is a side effect of the correlation-ID hardening, not a standalone mistake

The surrounding code is full of commented-out lines showing what this file looked like before the correlation-ID mechanism was added:

```java
//                    return new ResponseEntity<Object>(new ErrorResponse("HIS-1001", "Error while processing data", ex),
//                            HttpStatus.INTERNAL_SERVER_ERROR);
                    return new ResponseEntity<Object>(new ErrorResponse("HIS-1001", ErrorCorrelationUtil.clientMessage(correlationId), null),
                            HttpStatus.INTERNAL_SERVER_ERROR);
```

The original code passed the real exception (`ex`) into the response. At some point, every `ex` argument across this whole file's error responses was deliberately replaced with `null` — correct, intentional hardening: the client should never receive the raw exception (message, stack trace) in the response body, only a generic message plus a correlation ID for support to look up server-side. That change is safe everywhere else in this file, because the other five call sites all construct `Response` objects, whose `Object resObject` field just stores whatever it's given without touching it. `ErrorResponse` is different: its constructors *actively operate on* the exception argument (`printStackTrace`, `getMessage`, `getErrors`), so passing `null` there isn't inert the way it is for `Response` — it crashes. That distinction was missed for this one call site.

## What actually happens today (confirmed live, not just theoretical)

Reproduced during this session's wave-1 smoke pass: `PATCH /api/procurements/quotation/grn/approve` returned a plain 500 whose logged cause was `NullPointerException: Cannot invoke "CustomException.printStackTrace(...)" because "ex" is null`, thrown from inside `handleAllExceptions` itself.

Walking through what happens, step by step:
1. The original exception (whatever actually went wrong in the GRN-approve endpoint) is caught by `handleAllExceptions`'s outer `try`, and correctly logged at line 120, tagged with a first correlation ID (call it `ERR-aaaaaaaa`) — this part works.
2. `res > 0` is true (the GRN endpoint returns `ResponseEntity<...>`), so line 134 constructs `new ErrorResponse(..., null)` — which throws the NPE described above.
3. That NPE is itself caught by `handleAllExceptions`'s own outer `catch (Exception e)` block (line 144-150) — the handler catching its own internal failure. This generates a **second**, different correlation ID (`ERR-bbbbbbbb`), logs it tagged `"Unhandled error in centralized exception handler"`, and returns a generic `Response`-shaped 500 to the client, referencing `ERR-bbbbbbbb`.
4. The client sees `ERR-bbbbbbbb` and, per the correlation-ID pattern's own design ("Something went wrong. Reference: ERR-bbbbbbbb"), would report that ID to support. But the log entry for `ERR-bbbbbbbb` just says "Unhandled error in centralized exception handler" plus the NPE's own trivial stack trace — it does **not** show what the original problem in the GRN-approve endpoint actually was. The real answer is sitting one correlation ID over, at `ERR-aaaaaaaa`, which the client never sees and has no way to ask for.

Nothing is silently swallowed in the sense RULES.md is most worried about — the original exception IS logged in full, server-side, at `ERR-aaaaaaaa`. But the correlation-ID mechanism's entire purpose — "give the client a short reference so support can go straight to the right log line" — is broken on this path: the reference the client actually gets points at the wrong entry.

There's also a second, quieter consequence for work done earlier today: `ErrorCorrelationUtil.countInLast24Hours()` (the new Tier 1 circuit-breaker counter for RULES.md condition 7, "unhandled exception count > 5 in 24h") increments **twice** for every single occurrence of this bug — once for the original exception (step 1) and once for the handler's own NPE (step 3). Any endpoint hitting this path inflates the condition-7 count 2x relative to the real incident count, making the new circuit-breaker scheduler trip earlier/more often than the actual exception rate would justify. This isn't data loss, but it does mean the counter isn't currently trustworthy as a precise measure on this specific path.

Finally, the JSON shape returned to the client is also not what this code path was designed to send: it's supposed to return an `ErrorResponse`-shaped body (`{code, message, summary, stackTrace, errors}`), but because construction crashes, the client instead gets a `Response`-shaped body (`{status, code, message, listObject, object}`) from the unrelated outer catch block — a different schema than any client code expecting the `ErrorResponse` shape on a `res > 0` (ResponseEntity-returning) endpoint would be expecting.

## The proposed fix — minimal, fixes the root cause once, not per-call-site

Add a null-check inside `ErrorResponse`'s two constructors (`Entity/.../com/param/common/dto/ErrorResponse.java`), so a `null` exception leaves `summary`/`stackTrace`/`errors` unset instead of crashing:

```java
public ErrorResponse(String code, String message, Exception ex) {
    super();
    this.code = code;
    this.message = message;
    if (ex != null) {
        StringWriter writer = new StringWriter();
        ex.printStackTrace(new PrintWriter(writer));
        this.summary = ex.getMessage();
        this.stackTrace = writer.toString();
    }
}
public ErrorResponse(String code, String message, com.param.common.dto.CustomException ex) {
    super();
    this.code = code;
    this.message = message;
    if (ex != null) {
        StringWriter writer = new StringWriter();
        ex.printStackTrace(new PrintWriter(writer));
        this.summary = ex.getMessage();
        this.stackTrace = writer.toString();
        this.errors = ex.getErrors();
    }
}
```

**Why fix it here, in `ErrorResponse.java`, rather than in `GlobalExceptionHandler.java`:** this is the true root cause — the constructor's own contract is broken (it assumes a non-null argument it doesn't enforce or check). Fixing it here protects every current and future call site (80+ across the codebase, all of which currently pass a real, non-null exception) against ever repeating this exact mistake, rather than special-casing the one call site known to trigger it today. It also means `GlobalExceptionHandler.java` — the file RULES.md is most protective of — doesn't need to be touched at all.

**What this fix deliberately does NOT do:**
- It does not touch `GlobalExceptionHandler.java` itself, or change the `res > 0` routing logic, or restore the raw exception to the client response (the correlation-ID hardening's actual security intent — withholding stack traces from the client — is fully preserved; only the `code`/`message` fields will now populate correctly, `summary`/`stackTrace`/`errors` will simply stay `null` when no exception is supplied, which is the correct outcome for a deliberately-withheld exception).
- It does not add a new constructor overload, change any existing call site's arguments, or change the `ErrorResponse` class's public shape/fields.
- It does not address the exception-counter double-count directly (fixing the crash means the NPE at step 3 above stops happening at all, which means `newCorrelationId()` is no longer called a second time per incident — the double-count is a direct consequence of this same crash, not a separate defect requiring its own fix).

## Classification, per RULES.md

**Robustness/Reliability**, not Financial Correctness — no money calculation is touched anywhere in this fix; both before and after, the client receives *some* generic 500 response. The defect is in observability and error-handling correctness: the wrong correlation ID reaches the client, the wrong JSON shape is returned on `res > 0` endpoints, and the just-built circuit-breaker exception counter over-counts. This falls squarely under RULES.md's "no error may be silently swallowed" principle in spirit — the original exception isn't swallowed, but its trail is broken for the person trying to follow it.

## Verification plan, if approved

1. Apply the null-guard to both `ErrorResponse` constructors (4-line change each, no signature change).
2. Add a unit test in `Entity`'s test suite constructing `new ErrorResponse("CODE", "message", (Exception) null)` and `new ErrorResponse("CODE", "message", (CustomException) null)`, asserting no exception is thrown and `getSummary()`/`getStackTrace()`/`getErrors()` return `null`.
3. Add/extend a `GlobalExceptionHandler` test (or a targeted reproduction of the `handleAllExceptions` `res > 0` + generic-exception path) confirming the client now receives the intended `ErrorResponse`-shaped body (`code="HIS-1001"`, populated `message`, `summary`/`stackTrace`/`errors` all `null`) instead of falling through to the outer catch's `Response`-shaped body.
4. Confirm `ErrorCorrelationUtil.countInLast24Hours()` increments by exactly 1 (not 2) when this path is exercised, now that the handler's own internal crash no longer occurs.
5. Full `mvn -f legacy/pom.xml clean test` (all 19 modules), confirming no other test regresses.
6. Log the outcome in `docs/BUSINESS_RULE_MANIFEST.md` as a completed Robustness fix once approved and applied; update `BR-PENDING-globalexceptionhandler-null-customexception-crash` to `BR-FIXED-...`.

No code has been changed yet. This document is the proposal only, awaiting owner review before any fix is applied.
