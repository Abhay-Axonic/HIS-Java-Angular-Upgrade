# Decision Needed: `userAuthentication` screen (and a broader sibling endpoint) disclose every user's password in recoverable plaintext, to any authenticated user, regardless of role

**Prepared:** 2026-07-22, fast-tracked at owner's explicit request, same priority/severity-review tier as the Razorpay findings (`docs/DECISION_NEEDED_RazorPayClientTrustedAmount.md`, `docs/DECISION_NEEDED_RazorPay10xUndercharge.md`) and the CORS finding (`docs/DECISION_NEEDED_CorsWildcardOriginCredentials.md`). No application code has been touched to produce this document — proposal only. This is a finding about the **current legacy application's live behavior**, surfaced during Angular-rebuild research for the `userAuthentication` screen (batch 12 candidate) — it is being raised now, on its own track, independent of whether/when/how that screen gets rebuilt, per standing project discipline that real bugs are never deferred to "when the screen is built."

**Headline — this is not a UI quirk or a permissions edge case. It is a live, currently-exploitable credential-disclosure vulnerability, confirmed at the code level and reachable on the running dev instance.** Three compounding issues, each bad enough alone: (1) the backend decrypts and returns every user's password in plaintext on a routine list-load, before any "view password" UI is ever touched; (2) the only thing gating who can see it is a client-side, cookie-driven UI hide (`ng-show`) — there is **no server-side role check anywhere in the call chain**, so any authenticated user of any role can retrieve it directly; (3) the underlying "encryption" is a fixed, source-committed key with AES in ECB mode — a reversible, deterministic scheme functionally closer to plaintext than to real password hashing. A second, broader-scoped endpoint discovered during the same investigation is worse: it takes no parameters at all and returns plaintext passwords for **every user across every unit/organization in the system**.

---

## 1. The `userAuthentication` screen's disclosure, confirmed exactly

**Frontend:**
- Controller: `legacy/HIS-WAR/src/main/webapp/resources/assets/accessControl/controller/userAuthenticationMasterController.js`
- View: `legacy/HIS-WAR/src/main/webapp/views/master/UnitMaster/userManagement/user-authentication.html`

The list screen calls `POST /api/global/getUserAuthenticationList` on load (`userAuthenticationMasterController.js:143-157`). **The response already contains every listed user's password in plaintext** — decryption happens server-side, unconditionally, before the JSON is ever serialized. The "view password" icon does not trigger any further API call; it just copies data already sitting in the page's memory into a modal:

```html
<!-- user-authentication.html:136 -->
<td>{{info.userName}} <a title="Password" ng-show="isSuperUser=='Y'" data-toggle="modal" data-target="#viewPassword" ng-click="getPassword(info.password)">...</a></td>
```
```js
// userAuthenticationMasterController.js:106-109
$scope.getPassword = function(password) { $scope.viewPassword = password; }
```

Two sibling endpoints on the same controller also transmit full plaintext passwords over the wire: `POST /api/global/sendPasswordToUser` and `POST /adt/resendUserNameAndPassword` (`userAuthenticationMasterController.js:294-320`, emails `userObj.password` in plaintext).

**Backend:**
- Controller: `legacy/Global/src/main/java/com/param/global/unit/controller/UserRoleMapperContrtoller.java:116-124`
- DAO: `legacy/Global/src/main/java/com/param/global/unit/dao/UserRoleMapperDaoImpl.java:306-368` — runs a raw SQL query selecting `usrMaster.password AS "password"` straight from `public.m_user_master` for every user in the unit, then:

```java
// line 364
for (UserRoleMapperDto dto : list) {
    dto.setPassword(GlobalCommonDateUtils.decrypt(dto.getPassword()));
}
```

## 2. The "encryption" is a fixed key, ECB mode — reversible and weak, not hashing

`legacy/Entity/src/main/java/com/param/global/common/GlobalCommonDateUtils.java:223-253`:

```java
public static String encrypt(String strToEncrypt) {
    setKey(secret);
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
}
public static String decrypt(String strToDecrypt) {
    setKey(secret);
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
    cipher.init(Cipher.DECRYPT_MODE, secretKey);
    return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
}
```

The key is a **hardcoded literal, committed to source control**: `legacy/Entity/src/main/java/com/param/global/common/ICommonConstants.java:290` — `String ENCRYPTIONKEY = "ssshhhhhhhhhhh!!!!";` — used with **AES/ECB** (no IV, deterministic ciphertext per plaintext, a well-known weak mode choice for anything beyond single-block data). This confirms the scheme is reversible-by-design, not a hash: login itself (`UserMasterServiceImpl.java:249,287`) encrypts the submitted password and does a direct ciphertext-equality check (`masterDto.getPassword().equals(encryptedPassword)`) rather than `bcrypt`/salted-hash verification. Anyone with read access to the source (already true — this is the codebase under active rebuild) or the DB has the means to decrypt every password column in the system with a five-line script, independent of any application endpoint at all.

`updatePassword` (`UserMasterController.java:305-309` → `UserMasterServiceImpl.java:1028-1047` → `UserMasterDaoImpl.java:1071-1079`) re-encrypts with this same scheme on every password change — the weakness applies to current and future passwords alike, not just legacy data.

## 3. Access control is decorative on the frontend and absent on the backend

**Frontend gate is client-editable and irrelevant to actual security:** `ng-show="isSuperUser=='Y'"` reads `$scope.isSuperUser` straight from the client-side cookie (`userAuthenticationMasterController.js:25-30`):

```js
var cookieObject = $cookies.getObject('cookieObject');
$scope.isSuperUser = cookieObject.isSuperUser;
```

This is trivially bypassable (edit the cookie, or simply inspect the network response tab — the plaintext password is already present in the JSON before any UI check runs, whether or not the icon is ever clicked or visible).

**Backend enforces no role check whatsoever.** The full call chain — `UserRoleMapperContrtoller.java` → `UserRoleMapperServiceImpl.java` → `UserRoleMapperDaoImpl.java` — has no `@PreAuthorize`, `@Secured`, or manual role/permission check anywhere. The only gate present anywhere in the system is Spring Security's blanket rule, `legacy/HIS-WAR/src/main/java/com/param/config/SecurityConfig.java:29`: `.anyRequest().authenticated()`. **Any authenticated user of any role** — not just a "super-user" — can call `POST /api/global/getUserAuthenticationList` directly (curl/Postman with a valid session token, no browser or UI needed) and receive every unit user's plaintext password. This is a textbook broken-access-control / IDOR pattern: the restriction that the UI implies ("only super-users can view passwords") does not exist at the layer that actually matters.

## 4. A second, broader-scoped sibling endpoint — worse than the screen that triggered this investigation

`GET /adt/getPasswordAndUserName` (`UserMasterController.java:251-260` → `UserMasterServiceImpl.java:798-800` → `UserMasterDaoImpl.java:692-719`) takes **no parameters at all** and returns decrypted plaintext usernames/passwords for **every user across every unit and organization in the database** — not scoped to the caller's own unit the way `getUserAuthenticationList` is. It is gated by the same blanket `.anyRequest().authenticated()` and nothing else. Any authenticated user, of any role, in any unit, can dump the entire system's plaintext credential set with a single unparameterized GET request. This was found incidentally while tracing the `userAuthentication` screen's endpoint family and is being flagged in this same document as a closely-related companion finding rather than filed separately, since the root cause (missing role enforcement + reversible password storage) and the proposed fix are identical.

## 5. Live reachability — confirmed without touching any real account's data

Backend confirmed live on `localhost:8080` (root context path). Read-only, unauthenticated probes only — no login attempted, no real user's password retrieved or viewed:

| Request | Result |
|---|---|
| `POST /api/global/getUserAuthenticationList` (no auth) | `401` |
| `GET /adt/getPasswordAndUserName` (no auth) | `401` |
| `GET /` | `200` |

Both endpoints exist and are wired to live, non-dead controller methods — `401` confirms "authentication required," not "route missing." Given §3 and §4's confirmed absence of any role check beyond "authenticated," **any valid session of any role that clears that `401` gate receives full plaintext password disclosure** — this was not tested against a real authenticated session (per this project's standing no-credential-exploitation discipline), but the code path from valid-session to plaintext-password-in-response is unambiguous and complete, with no dead or commented-out code anywhere in the chain.

## 6. Why this matters beyond the two endpoints named above

- **Password reuse risk:** most users reuse passwords across systems. A plaintext HIS credential leak is not contained to this application.
- **No detection surface today:** because this isn't an exploit of a bug (SQL injection, auth bypass) but the system's own designed, working behavior, there is nothing to "patch" in the sense of closing an accidental hole — the fix is a genuine design change (real role enforcement + real hashing), not a one-line guard.
- **Every already-shipped and future screen that touches `m_user_master`** inherits this weakness implicitly — any endpoint that ever returns a `password` field is exposing recoverable plaintext, not a hash. This is a systemic property of the user-storage design, not a one-screen bug.

## 7. Severity assessment

**Critical.** Unlike the CORS finding (masked in practice by an unrelated filter, confirmed via live testing) or the reversible-but-currently-caught Razorpay findings, there is **no mitigating mechanism found anywhere in this chain** — no role check, no rate limit, no anomaly detection referenced in the code read. Any authenticated user, including the lowest-privilege role in the system, can retrieve every password in the database (system-wide, via `/adt/getPasswordAndUserName`) today, in the current dev/non-production environment, and this same code is what would ship to production unchanged if not addressed. Recommend treating this as the highest-urgency item currently open, on par with or above the Razorpay access-control findings.

## 8. Proposed fix directions (none implemented — for review and explicit direction)

1. **Stop storing/returning recoverable passwords at all.** Migrate to a proper salted one-way hash (e.g. bcrypt/Argon2) for the `password` column, verified at login by hash comparison, never decrypted for display. This is the correct long-term fix but is a real migration (existing encrypted passwords can't be converted to hashes without users resetting them, or a one-time online re-hash at next successful login) — needs explicit scoping and sequencing, not a quick patch.
2. **Immediately remove every endpoint/UI path that returns a password field in any response**, plaintext or otherwise (`getUserAuthenticationList`, `getPasswordAndUserName`, `sendPasswordToUser`'s payload, `resendUserNameAndPassword`'s payload) — replace "view password" functionality with a password-reset flow (generate a new one, email/display a reset link) rather than displaying the existing one. This can be done independently of and sooner than fix (1), since it doesn't require touching how passwords are stored — only stops surfacing them once retrieved.
3. **Add real server-side role enforcement** (`@PreAuthorize` or equivalent) on every endpoint in this family, regardless of the outcome of (1)/(2) — the missing-authorization pattern here likely recurs elsewhere in this codebase and may be worth a dedicated sweep.
4. **Rotate `ICommonConstants.ENCRYPTIONKEY`** and re-encrypt/rehash all stored passwords, once a direction on (1) is chosen — the current key has been sitting in source control (and thus in every clone/fork/backup of this repository) and must be treated as already compromised.

Given the DB/migration and user-facing-flow implications of (1) and (4), I'd recommend (2) and (3) as the fast, low-risk, high-impact first step — they stop the active disclosure immediately without touching password storage — with (1)/(4) scoped as a following, more deliberate piece of work. But the sequencing and scope call is yours.

## 9. What we need from you

1. **Priority confirmation** — do you agree this should be treated as the top-priority open item, ahead of/alongside the Razorpay findings?
2. **Which fix direction(s) to authorize, and in what order** — my recommendation is §8 items (2) and (3) first (stop returning passwords + add role checks), with (1)/(4) (hash migration + key rotation) scoped as deliberate follow-on work, but this is your call given the user-facing and migration implications.
3. **Should this block or proceed alongside `userAuthentication`'s Angular rebuild?** My default assumption, per standing project discipline, is that the rebuild would faithfully reproduce legacy's *current* (broken) behavior unless you direct otherwise here — I am not proposing to silently "improve" this in the rebuild without your explicit sign-off, since that would be exactly the kind of undisclosed behavioral change this project's discipline exists to prevent. If you'd rather the rebuild omit the password-viewing feature entirely (treating removal as the parity target, not an improvement), say so explicitly and I'll scope it that way for batch 12.
4. **Do you want the broader `/adt/getPasswordAndUserName` finding filed as fully separate from this document**, or is bundling them here (same root cause, same fix) the right level of granularity for your review?

No code has been touched to produce this document. Awaiting your direction.
