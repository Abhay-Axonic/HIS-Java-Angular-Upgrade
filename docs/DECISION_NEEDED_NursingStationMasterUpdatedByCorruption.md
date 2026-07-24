# Decision Needed: `nursingStationMaster`'s UPDATE flow silently records `updated_by = 0` on every edit, in the current legacy backend

**Prepared:** 2026-07-24, during Phase 4 batch 16's Step 0/1 audit of the `nursingStationMaster` screen (clinical/billing-tier addendum, same escalation discipline as batch 15's `identificationMaster` finding). No application code has been touched to produce this document — proposal only. Step 2+ (writing the Angular feature module) has been deliberately **not started** for this screen pending your direction, per this project's standing rule that severity-1, invisible-from-the-frontend bugs get escalated rather than silently reproduced or silently fixed.

**Headline — every time a user edits an existing Nursing Station Master record (code/description/store), the backend silently overwrites the audit column `updated_by` with `0` instead of the real acting user's ID.** The frontend correctly sends the real user ID on every update call; the backend DAO method simply reads the wrong DTO field. This has apparently been the behavior since this screen was built; it is not something the rebuild introduces, and nothing in the UI shows `updated_by`, so no user has ever been in a position to notice it.

---

## 1. The bug, confirmed exactly

`NursingStationDaoImpl.updateNursingStationMaster` (`legacy/Global/src/main/java/com/param/global/dao/NursingStationDaoImpl.java:143-159`):

```java
public Response updateNursingStationMaster(NursingStationMasterDto nursingStationMasterDto)
        throws ApplicationException {
    try {
        NursingStationMaster nuStationMaster=findById(nursingStationMasterDto.getNursingStationId());
        nuStationMaster.setNursingStationCode(nursingStationMasterDto.getNursingStationCode());
        nuStationMaster.setNursingStationDesc(nursingStationMasterDto.getNursingStationDesc());	
        nuStationMaster.setStoreId(nursingStationMasterDto.getStoreId());
        nuStationMaster.setUpdatedBy(nursingStationMasterDto.getCreatedBy());   // <-- line 150, the bug
        nuStationMaster.setUpdatedDate(...);
        update(nuStationMaster);
        ...
```

`NursingStationMasterDto.createdBy` (`legacy/Global/src/main/java/com/param/global/dto/NursingStationMasterDto.java`) is a primitive `int`, not a nullable/optional field. The frontend's update call, `nursingStationMasterController.js:336-358` (`$scope.updateNursingStation`), builds this exact payload:

```js
var data = {
    nursingStationCode : $scope.nursingMasterMasterObj.nursingStationCode,
    nursingStationDesc : $scope.nursingMasterMasterObj.nursingStationDesc,
    nursingStationId : $scope.nursingMasterMasterObj.nursingStationId,	
    storeId : $scope.nursingMasterMasterObj.storeId,	
    updatedBy : $scope.userId,
    organizationId : $scope.organizationId,
    updatedDate : $scope.currentDate
}
```

There is no `createdBy` key in this payload at all — correctly so, since this is an update, not a create. On the server, Jackson deserializes the JSON body onto `NursingStationMasterDto`; since `createdBy` is absent from the JSON and is a primitive `int` field, it deserializes to its default value, `0`. `updateNursingStationMaster` then calls `.setUpdatedBy(nursingStationMasterDto.getCreatedBy())`, which reads that always-`0` field instead of the `updatedBy` value the frontend actually sent (`nursingStationMasterDto.getUpdatedBy()`, sitting unused right next to it). The result: **every single update to a nursing station master record persists `updated_by = 0`**, permanently discarding the real user ID, with no way to reconstruct who actually made the change after the fact.

## 2. Why this looks like a real, unintentional copy-paste defect, not a convention

- **The sibling method in the very same file does it correctly.** `updateNursingStationMasterStatus`, four lines below at line 168, reads `nursingStationMasterDto.getUpdatedBy()` — the correct field — for the exact same purpose (recording who flipped the active/inactive toggle). Line 150 and line 168 are two methods in one class, one right, one wrong; this is the classic signature of a copy-paste-from-`save()` slip, not a deliberate design.
- **The `save()` (create) method uses `getCreatedBy()` for the same setter, and that's correct there** (line 92: `nuStationMaster.setUpdatedBy(nursingStationMasterDto.getCreatedBy())`) — on create, the frontend does send `createdBy` equal to the same value as `updatedBy` (`nursingStationMasterController.js:217-218`), so both fields hold the same user ID and the bug is invisible on create. It looks like `updateNursingStationMaster` was written by copying `saveNursingStationMaster`'s body and only some of the field references were updated for the new DTO shape.
- **A sibling clinical-tier master handles this correctly**, confirming this isn't a codebase-wide convention of reusing `createdBy` as a generic "current actor" field: `ConsentTypeMasterDaoImpl.updateConsentTypeMaster` (`legacy/ADT/src/main/java/com/param/adt/master/global/dao/ConsentTypeMasterDaoImpl.java:99-109`) reads `consentTypeMasterDto.getUpdatedBy()` on update, and its frontend (`consentTypeMasterController.js:342-358`) likewise sends only `updatedBy`, not `createdBy`, on its update call — the same shape of request, handled correctly. (A broader grep found ~247 other `setUpdatedBy(...getCreatedBy())` call sites across the codebase; this document makes no claim about any of those — each would need its own frontend-payload check before being called a bug, since some may genuinely send `createdBy` on their update calls too. Only `nursingStationMaster`'s instance was verified for this screen's audit.)
- **The bug is invisible from the frontend/template alone** — nothing in the Angular controller or the view (`nursing-station-master.html`) displays `updated_by`, and the update call itself reports "success" normally (the write does succeed; it's just attributed to the wrong/no user). This was only found by reading the DAO layer directly and cross-checking it against the exact JSON shape the frontend sends, which is exactly the kind of thing routine frontend-only testing cannot catch.

## 3. What this does and doesn't affect

- **Not a billing calculation and not a direct clinical-safety action** — `nursingStationMaster` is a pure classification/lookup table (code + description + a store FK) referenced downstream by bed allocation, admitted-patient list, transfer, and discharge screens by its `nursingStationId`, not by its audit columns. No downstream consumer reads `updated_by` for `m_nursing_station_master` as part of any business logic found in this audit.
- **It does mean the audit trail for this table is silently wrong for every edited record** — if there is ever a need to determine who last changed a given nursing station's code, description, or assigned store (e.g. a compliance/incident review), the `updated_by` column will incorrectly show `0` (no such user) for every record that has ever been edited via this screen, with the true actor unrecoverable after the fact. `created_by` (who originally added the record) is unaffected and correct.
- **No patient data is affected**, and the record's substantive fields (code, description, store) save correctly — only the "who last touched this" attribution is silently lost.

## 4. Options (not implemented — for review)

1. **Fix in legacy**: change line 150 of `NursingStationDaoImpl.updateNursingStationMaster` from `.getCreatedBy()` to `.getUpdatedBy()`, matching the already-correct sibling method four lines below and matching `ConsentTypeMasterDaoImpl`'s pattern. This is a one-line, low-risk, application-code fix (`legacy/`), so per `CLAUDE.md` it needs its own dedicated branch (Maker-Checker), separate from the Angular rebuild branch.
2. **Leave legacy as-is; rebuild faithfully reproduces the current (broken) behavior** — the Angular update flow would send `updatedBy` exactly as legacy's frontend does (there is no client-side workaround that would fix a server-side field-read bug), and the corrupted `updated_by = 0` would continue to be written on every edit, same as today. This requires no special rebuild logic — it's simply what wiring the existing endpoint as-is produces — but it means the rebuild consciously perpetuates a known data-integrity gap rather than silently inheriting an unnoticed one.
3. **Fix only in the context of this rebuild effort, on its own tracked legacy branch, before or alongside Step 2+** — practically identical to option 1 in effect, just calling out that it could be sequenced as part of this batch's work if you'd rather not wait for a separate unrelated fix cycle. Still a legacy-code change needing its own Maker-Checker branch per `CLAUDE.md`, not bundled into the Angular rebuild branch/PR itself.

## 5. What we need from you

1. **Should legacy itself be fixed** (options 1/3), and if so, on what timeline relative to this batch's Angular rebuild work?
2. **Should Step 2+ (building the `nursing-station-master` Angular feature module) proceed now**, wiring to the endpoints exactly as they exist today (option 2, matching current — broken — `updated_by` behavior), or should it wait for the legacy fix to land first? Nothing about the frontend's own behavior needs to change either way; this only affects what a background audit column ends up holding in the database.
3. Given `nursingStationId` is a widely-referenced FK downstream but itself is classification-only, we don't believe this changes anything about `nursingStationMaster` staying a PURE-LOOKUP screen for rebuild-scope purposes — flagging this as our read, not a decision.

No code has been touched to produce this document. Awaiting your direction before Step 2+ proceeds on this screen.
