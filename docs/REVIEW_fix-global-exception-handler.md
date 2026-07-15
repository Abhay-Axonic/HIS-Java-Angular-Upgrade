# Review: GlobalExceptionHandler.java — Silent-Failure Catch Block Fix

**Branch:** fix/global-exception-handler
**Date:** 2026-07-03
**Role:** Maker (Claude Code, Sonnet 5)

## 1. File Changed

`legacy/HIS-WAR/src/main/java/com/param/exception/GlobalExceptionHandler.java`

No other file was modified. This is the application's single centralized exception handler (`@ControllerAdvice`), which every local `try/catch` in the codebase is expected to route unhandled errors to.

## 2. Before / After

### Catch block 1 — inner catch, ~line 121 (guards the error-logging call itself)

**Before:**
```java
                    } catch (Exception e) {

                    }
```

**After:**
```java
                    } catch (Exception e) {
                        logger.error("Failed to persist error log via backLogService; original exception was: {}",
                                ExceptionUtils.getStackTrace(ex), e);
                    }
```

### Catch block 2 — outer catch, ~line 138 (guards the entire `handleAllExceptions` method)

**Before:**
```java
        }catch (Exception e){

            return null;
        }
```

**After:**
```java
        }catch (Exception e){
            logger.error("Unhandled error in centralized exception handler", e);
            return new ResponseEntity<Object>(new Response("error",
                    ERR_500,
                    ERR_500_MESSAGE, null, null, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
```

## 3. Why Each Change Was Made

**Inner catch (~121):** This block wraps the call to `backLogService.createErrorLog(wrapper, ex)` — the code responsible for logging the *original* exception (`ex`) that triggered the handler in the first place. Before the fix, if that logging call itself failed (e.g., the log backend was down, or serialization broke), the failure was silently discarded — no trace, no log line, nothing. Worse, this meant the very exception (`ex`) the handler was trying to log could vanish entirely if `createErrorLog` happened to fail. The fix logs the logging-failure itself, and includes the original exception's full stack trace, so nothing is lost even in this double-failure case.

**Outer catch (~138):** This block wraps the entire body of `handleAllExceptions`, the app's one centralized exception handler. Before the fix, any failure inside this method (e.g., an NPE from `handler.getHandler(request)`) was silently swallowed and the method returned `null`. A Spring `@ExceptionHandler` method returning `null` typically results in an **empty HTTP 200 response** — meaning a genuine server-side failure (potentially during a billing-related request) could appear to the caller as a successful, empty response. The fix logs the failure with full detail and returns a proper `ResponseEntity` with HTTP 500 and a generic, non-leaking error message, so a real failure now surfaces as a real failure instead of a false success.

## 4. No New Dependencies Introduced

All symbols used in the fix were already present and in use in this exact file before this change:

| Symbol | Already used at |
|---|---|
| `logger` | Declared line 58; already used at lines 71, 87, 92, 115 |
| `ExceptionUtils` | Imported line 14; already used at lines 85-86 (`ExceptionUtils.getRootCauseMessage`) |
| `Response` | Already constructed identically at lines 134-137 (the method's existing 500 fallback path) |
| `ResponseEntity` | Imported line 24; already used throughout the method (e.g. lines 72, 94, 109, 128, 135) |
| `HttpStatus` | Imported line 23; already used throughout (e.g. `HttpStatus.METHOD_NOT_ALLOWED`, `HttpStatus.BAD_REQUEST`, `HttpStatus.INTERNAL_SERVER_ERROR`) |

No new imports, no new libraries, no new fields were added.

## 5. Pre-Existing, Unrelated Build Issue

A separate, pre-existing compilation failure exists in the `HIS-WAR` module:

```
ContentCachingRequestWrapper.java:[6,37] package com.beust.jcommander.internal does not exist
ContentCachingRequestWrapper.java:[27,6] cannot find symbol: class Nullable
```

This is caused by `ContentCachingRequestWrapper.java` — a file untouched by this fix — importing `com.beust.jcommander.internal.Nullable`, a package not declared as a compile-scope dependency anywhere in `HIS-WAR`'s or the parent's `pom.xml`.

**This was proven unrelated to the exception-handler fix via a revert-and-recompile test:** both catch blocks were temporarily reverted to their original broken form, the module was recompiled, and the identical error (same file, same lines) occurred. The blocks were then restored and confirmed via `git diff`/`git status` to match the approved fix exactly, with zero drift.

This build gap is tracked separately as **BR-002** in `docs/BUSINESS_RULE_MANIFEST.md`, classified as a pre-existing defect (neither Safe Refactor nor Behavioral Change) pending owner prioritization.
