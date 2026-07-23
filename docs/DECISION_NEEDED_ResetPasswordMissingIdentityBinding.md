# Decision Needed: `/adt/resetPassword` accepts a client-supplied `userId`, letting any authenticated user change any other user's password — completes a full account-takeover chain with the already-documented password disclosure

**Prepared:** 2026-07-23, fast-tracked at the same urgency tier as the session's other credential-handling findings — arguably above all of them, since this one is a full account-takeover primitive, not just a disclosure. Found during Phase 4 batch 13's Step 0/1 source audit of `resetPwdconfirmPwd`. No application code has been touched to produce this document.

**Headline — this is not "a password can be read," it's "any authenticated user can take over any other account, including presumably an admin's."** Combined with the already-open `docs/DECISION_NEEDED_UserAuthenticationPlaintextPasswordDisclosure.md` (any authenticated user can retrieve every user's plaintext password via `/adt/getPasswordAndUserName`), the two findings compose into a complete, two-call account-takeover chain requiring nothing more than a valid session of the lowest privilege tier in the system:

1. `GET /adt/getPasswordAndUserName` → every user's real, current plaintext password, system-wide.
2. `POST /adt/resetPassword` with `{userId: <any target>, existingPassword: <the password just disclosed>, userPwd: <attacker's new password>}` → the target's password is silently changed to whatever the attacker chose.

No step here requires elevated privilege, a specific role, or any interaction from the target user. The target only finds out once they can no longer log in.

---

## 1. The vulnerable code, confirmed exactly

**Frontend:** `resetPwdconfirmPwdController.js:113-120` builds the request body directly from the client-editable `cookieObject` cookie:
```js
data = { userId: userObj.userId, existingPassword: $scope.existingPassword, userPwd: $scope.password }
```
This is the *normal* screen's own behavior (a logged-in user changing their own password, using their own session's `userId`) — the vulnerability is not in this screen's own UI, it's that the backend never verifies the `userId` in the body actually belongs to whoever is authenticated. Any direct API call (curl/Postman, no UI needed) can substitute any other user's ID.

**Backend, confirmed via direct read:**
```java
// UserMasterController.java:179-186
@RequestMapping(value = "/resetPassword", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
public @ResponseBody Response resetPassword(@RequestBody UserMasterDto userMasterDto){
    return iUserMasterService.resetPassword(userMasterDto);
}
```
```java
// UserMasterDaoImpl.java:510-534
public Response resetPassword(UserMasterDto userMasterDto) {
    UserMasterDto userDto = (UserMasterDto) sessionFactory.getCurrentSession().createSQLQuery(
            "Select user_mst.password as \"password\" from public.m_user_master user_mst where user_id = :userId")
        .setParameter("userId", userMasterDto.getUserId())   // <-- from the request body, unchecked
        .setResultTransformer(Transformers.aliasToBean(UserMasterDto.class)).uniqueResult();
    String decryptPwd = GlobalCommonDateUtils.decrypt(userDto.getPassword());
    if (decryptPwd.equals(userMasterDto.getExistingPassword())) {    // <-- the ONLY gate
        String encryptedPwd = GlobalCommonDateUtils.encrypt(userMasterDto.getUserPwd());
        ...
        sessionFactory.getCurrentSession().createSQLQuery(
            "Update public.m_user_master set password = '" + encryptedPwd + "', ... where user_id = '" + userMasterDto.getUserId() + "'")
            .executeUpdate();
        return new Response(SUCCESS, SUCCESS_CODE, PASSWORD_UPDATED, null, null);
    }
    ...
}
```

Confirmed no `@PreAuthorize`/`@Secured`/manual identity check anywhere in `UserMasterController.java` or `UserMasterServiceImpl.java` (grepped both files directly). The **only** thing gating this write is knowledge of the target's current password — not identity of the caller. This is the exact same missing-authorization pattern as the `userAuthentication` and credential-disclosure findings already on file this session (this codebase has no backend RBAC enforcement anywhere, a now well-established systemic pattern), but this instance is a **write to a security-critical field**, not a read.

## 2. Why this composes into a complete takeover, not just a theoretical gap

Unlike some "missing role check" findings where the practical impact depends on what the endpoint returns, this one's impact is unambiguous: successfully calling it **changes a real password**. The only precondition is knowing the target's *current* password — and this codebase already has a confirmed, live, unauthenticated-relative-to-role endpoint (`getPasswordAndUserName`) that hands out every user's current password in plaintext to any authenticated caller. Put together, a session with zero special privileges can:
- Enumerate every real password in the system (already-documented finding).
- Immediately overwrite any of them, including an administrator's, with a password of the attacker's own choosing.
- Log in as that user going forward — this is a full account-takeover primitive, not an information-disclosure one.

## 3. Additional findings on the same code path, lower severity, logged for completeness

- **No server-side maximum password length.** Client JS caps new passwords at 16 characters; `UserMasterServiceImpl.isValidPassword` (`MIN_LENGTH = 8`) enforces a minimum and complexity rules but no maximum — a direct API call could set an arbitrarily long password the UI would never permit. Minor, unrelated to the takeover chain above.
- **"New password must differ from existing" is UI-only**, never re-checked server-side — a direct API caller could submit an identical "new" password, silently no-op-ing an intended password rotation. Minor.
- **Underlying storage is the same compromised AES/ECB/hardcoded-key scheme** already under owner review in the password-disclosure document (`GlobalCommonDateUtils.java:223-253`, key at `ICommonConstants.java:290`) — this DAO method is one more caller of that same root cause, not a new instance of it.
- A sibling "forgot password" screen (`resetPwd`, shared `MasterController`) has its own, separate, already-broken data flow (a dead `getUnitName()` handler and no actual backend call on submit) — noted for the record, not a security finding, flagged in case anyone assumed `resetPwd` was a working self-service reset path for logged-out users; it is not, and `resetPwdconfirmPwd` itself bounces logged-out sessions straight to `/login`, so there is currently no working "I forgot my password and I'm logged out" flow at all in this application.

## 4. Severity assessment

**Critical — the most severe finding of this session's credential-handling work.** The other findings (`userAuthentication`'s disclosure, `Qmslogo`'s anonymous write, the config-endpoint credential leak) each exposed or leaked something. This one, combined with one of them, lets any authenticated user of any role **become** any other user, including an administrator, with two API calls and no UI interaction from the target. Recommend treating this as the top-priority item among all currently-open security documents.

## 5. Proposed fix directions (not implemented — for review)

1. **Bind the write to the authenticated caller's own identity.** The backend already has the caller's `userId` available from the validated JWT/session (the same value `JwtTokenFilter` checks against the `userId` header) — `resetPassword` should ignore the body's `userId` entirely and use the session-derived one instead, so this endpoint can only ever change the caller's *own* password, matching what the screen's own UI already always intends to do.
2. If an admin-driven "reset another user's password" feature is a genuine, intended capability elsewhere in the product (distinct from this self-service screen), it needs its own endpoint with a real role check (`@PreAuthorize` or equivalent) — not this one, which the frontend only ever calls for self-service password changes.
3. Independent of (1)/(2): once the `userAuthentication` password-disclosure fix direction is decided, re-examine whether `existingPassword`-as-sole-gate is sufficient at all, given how easily "knowing the current password" can be obtained system-wide today.
4. The minor findings in §3 (max-length, differs-from-existing) are worth folding into the same fix pass, low effort, no design decision needed.

## 6. What we need from you

1. **Confirm priority** — my recommendation is this becomes the top-priority open security item, above the existing three, given it's a complete takeover primitive rather than a disclosure.
2. **Which fix direction to authorize** — my recommendation is (1), binding to the session-derived identity, as the minimal correct fix; (2) only if an admin-reset feature is genuinely wanted elsewhere.
3. Should this affect `resetPwdconfirmPwd`'s own Angular rebuild scope (not yet started for batch 13)? Per standing project discipline, my default is the rebuild faithfully reproduces legacy's current behavior (same as every other open finding) unless you direct otherwise — flag here if you want this screen's rebuild to omit/alter anything pending your decision, matching how `userAuthentication`'s password-viewing feature was handled.

No code has been touched to produce this document. Awaiting your direction.
