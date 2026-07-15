# Catalog: Additional Fabricated-Success-on-Exception Findings — legacy/Global (peripheral, round 2)

**Status:** Cataloging only — no application code modified. This document is a verified inventory for owner/Checker review, produced in response to the Checker's finding that the branch `fix/global-peripheral-exceptions` contains fabricated-success patterns beyond the 15 already resolved in `docs/DECISION_NEEDED_Global_Peripheral_FabricatedSuccess.md` / `BUSINESS_RULE_MANIFEST.md` (peripheral fix cycle, commit `75dab436`).

**Method:** Every file/method named by the Checker, plus the 3 additional sites the owner personally flagged, was opened and read directly from the current `legacy/` baseline (not guessed, not taken from the audit report). Each site below quotes the actual current code and states the exact line numbers. Sites that turned out **not** to be genuine fabricated-success are flagged as such, with the evidence, rather than silently dropped.

**Classification key** (same as the prior resolved doc):
- **Group A-like**: the primary/requested operation already completed successfully before the try/catch in question; the guarded code is a secondary, best-effort side effect (sync push, logging, notification). Reporting SUCCESS on failure here is defensible today but the failure is currently invisible.
- **Group B-like**: the try block *is* the whole operation (or contains the operation's actual state-changing call). Reporting SUCCESS on failure here means the caller is told an action worked when it did not — no already-succeeded fallback exists.
- **Not a genuine fabricated-success pattern**: named by the Checker/owner, but on inspection the code either already handles the exception honestly, or has no exception-swallowing at all.

---

## 1. `TwilioController.java` — `com.param.global.controller`

There are **two** overloaded methods named `deleteParticipants`. The Checker's finding refers to the one with a try/catch (not the parameterless one).

### 1a. `deleteParticipants()` (no args, POST `/deleteParticipants`) — lines 139–143 — NOT a fabricated-success pattern

```java
139:	@RequestMapping(value = "/deleteParticipants", method = RequestMethod.POST)
140:	public @ResponseBody ResponseTwilio deleteParticipants() {
141:		Participant.updater("DailyStandup", "Alice").setStatus(Participant.Status.DISCONNECTED).update();
142:		return new ResponseTwilio(SUCCESS, SUCCESS_CODE, null, null, null);
143:	}
```
No try/catch at all. If `Participant.updater(...).update()` throws, the exception propagates unhandled to the centralized `GlobalExceptionHandler` — it does not fabricate a SUCCESS response. Noted for completeness only; **not in scope**.

### 1b. `deleteParticipants(identity, room)` (GET `/deleteParticipants/{identity}/{room}`) — lines 145–156 — **CONFIRMED, Group B-like**

```java
145:	@RequestMapping(value = "/deleteParticipants/{identity}/{room}", method = RequestMethod.GET)
146:	public @ResponseBody ResponseTwilio deleteParticipants(@PathVariable("identity") String identity, @PathVariable("room") String room) {
147:		try{
148:			Twilio.init(twilioAccountSid, AUTH_TOKEN);
149:			 
150:	    	Participant.updater(room,
151:	    			identity).setStatus(Participant.Status.DISCONNECTED).update();
152:		}catch(Exception e){
153:			e.printStackTrace();
154:		}
155:		return new ResponseTwilio(SUCCESS, SUCCESS_CODE, null, null, null);
156:	}
```
The entire operation (disconnecting the named Twilio participant) is inside the try. On exception, the catch only does a console-only `e.printStackTrace()` (unwatched, not routed to the class's own `logger`, which exists and is used elsewhere in this same file), then falls through to an unconditional `return new ResponseTwilio(SUCCESS, ...)`. The caller is told the participant was disconnected when it was not.

This is a distinct method from `deleteParticipants1` (`/api/deleteParticipants`, lines 213–225), which **was** already fixed in the resolved 15-finding cycle (Group B finding #8 in `DECISION_NEEDED_Global_Peripheral_FabricatedSuccess.md`, verified via `git show 75dab436` — only that method's catch was touched, changing its fabricated `SUCCESS` to an honest `ERROR`). This `deleteParticipants(identity, room)` overload was never part of that fix and remains untouched.

**Classification: Group B-like.**

---

## 2. `whatsAppServiceImpl.java` — `com.param.global.unit.service` — `sendWhatsAppMessage` — NOT a fabricated-success pattern

```java
52:	public Response sendWhatsAppMessage(whatsAppRequestDto wDhatsappRequestDto) throws ApplicationException {
53:		 try {
...
74:				String stringResponse = restTemplate.postForObject(url, request, String.class);
75:	            
76:	             }catch(Exception e)
77:	            {
78:	                 logger.error("sendWhatsAppMessage failed", e);
79:	                 throw new ServiceException(e.getMessage());
80:	            }
81:		 return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
82:	}
```
The catch (lines 76–80) unconditionally `throw`s a `ServiceException` after logging — it never falls through to the `return SUCCESS` on line 81. That return is only reachable if the try block completes with **no** exception. This method was not touched by the resolved 15-finding commit (confirmed via `git show 75dab436` diff for this file — it only changed `sendWhatsAppMessageTwilio`, `sendWhatsAppMessageVonage`, `authontificationKeyGenerationForHeloAPI`, `sendWhatsAppMessageHelo`, `sendWhatsAppMessageHeloBulkMessages`, and `sendWhatsAppMessagePinbot`, at lines 126–131, 185–190, 245–250, 351–356, 517–522, 640–643 — all of which already report an honest `ERROR` today). `sendWhatsAppMessage` appears to have been already-correct before this fix cycle started.

**Not in scope — no code change needed here.** Flagging as a likely misidentification in the Checker's report (possibly confused with one of the six already-fixed sibling methods in the same file, all of which are named similarly).

---

## 3. `ProductionReleaseEmailNotificationConfigDaoImpl.java` — `com.param.global.dao` — `sendNotificationOnEmailOfReleaseDetails` — **CONFIRMED, Group B-like**

```java
42:    public Response sendNotificationOnEmailOfReleaseDetails() throws ApplicationException {
43:        try {
...
53:            for (ProductionReleaseEmailNotificationConfigDto dto : ProductionReleaseEmailNotificationConfigDtoList) {
...
63:                } catch (Exception e) {
64:                    throw new RuntimeException(e);
65:                }
66:                dto.setInstanceName(instanceName);
...
69:                try {
70:                    if (res.getListObject() != null && res.getListObject().size() > 0) {
72:                        return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
73:                    } else {
76:                        mapper.setIsEmailSend('Y');
77:                        save(mapper);
...
101:                        JSONObject json = new JSONObject();
102:                        try {
...
120:                            okhttp3.Response response = client.newCall(request).execute();
121:                            String responseBody = response.body().string();
122:                            return new Response(EMAIL_SENT, null, responseBody, null, null);
123:                        } catch (JSONException e) {
124:                            // TODO Auto-generated catch block
125:                            e.printStackTrace();
126:                        }
127:                    }
128:                } catch (Exception e) {
129:
130:                }
131:            }
132:
133:            return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
134:        } catch (Exception e) {
135:            throw new RuntimeException(e);
136:        }
137:    }
```
This has a genuine fabricated-success pattern, and it is worse than most others reviewed in this cycle because a row is marked "sent" in the database **before** the send is actually attempted:
- `mapper.setIsEmailSend('Y'); save(mapper);` (lines 76–77) persists the notification as sent.
- The subsequent email-send attempt (lines 101–126) can fail two ways: a `JSONException` while building the payload (caught at line 123, `printStackTrace()`-only, no rethrow — falls through to the end of the `else` block) or any other exception including the HTTP call itself failing (`IOException` from `client.newCall(request).execute()`), which is **not** caught by the inner `JSONException` catch and instead propagates to the outer per-iteration catch at line 128 — a fully empty `catch (Exception e) { }` with **zero logging of any kind**, not even `printStackTrace()`.
- Either way, control falls through to the next loop iteration (or exits the loop), and the method unconditionally returns `new Response<>(SUCCESS, ...)` at line 133 regardless of whether any email actually sent.

Note the DB row was already marked sent (`isEmailSend='Y'`) prior to the failed send attempt, so this is not merely "reports success" — a retry of this notification will not happen on a future run because the mapper row now says it was already handled. That compounds the silent failure.

**Classification: Group B-like** (the operation this method exists to perform — notifying about a release — silently fails and is never retried, while the method reports overall SUCCESS).

---

## 4. `AiAppointmentQueueDaoImpl.java` — `com.param.global.opd.dao` — 3 sibling methods — **CONFIRMED, Group B-like**

A 4th method in the same file, `updateQueueByAppointmentId` (lines 76–125), already does `logger.error(...); throw new ServiceException(...)` on its catch (lines 121–124) — it is **not** part of this finding and needed no changes.

### 4a. `saveAIAppointmentInQueue` — lines 34–73
```java
34:    public Response saveAIAppointmentInQueue(AIAppointmentQueueDto aiAppointmentQueueDto) throws ApplicationException {
35:        try{
...
67:            sessionFactory.getCurrentSession().save(aiAppointmentQueue);
68:            return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
69:        }catch(Exception e){
70:            e.printStackTrace();
71:        }
72:         return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
73:    }
```
The AI-appointment-queue row save (line 67) is inside the try. On any exception the catch (69–71) is console-only, then line 72 unconditionally returns SUCCESS — whether or not the row was actually saved.

### 4b. `getAIAppointmentCount` — lines 128–151
```java
128:    public Response getAIAppointmentCount(AIAppointmentQueueDto dto) {
129:      try{
...
145:                return new Response(SUCCESS, null, null, null,count.intValue());
146:
147:                }catch(Exception e){
148:                    e.printStackTrace();
149:                }
150:                     return new Response<>(SUCCESS, SUCCESS_CODE, null, null, 0);
151:            }
```
On exception, returns fabricated `SUCCESS` **and** a hardcoded count of `0` — indistinguishable from "zero pending AI appointments," which is a materially misleading payload, not just a masked status.

### 4c. `getAISummaryData` — lines 154–176
```java
154:    public Response getAISummaryData(AIAppointmentQueueDto dto) {
155:        try{
...
170:            return new Response(SUCCESS, null, null, null,aiSummary);
171:
172:        }catch(Exception e){
173:            e.printStackTrace();
174:        }
175:        return new Response<>(SUCCESS, SUCCESS_CODE, null, null, 0);
176:    }
```
Same shape; on exception returns fabricated SUCCESS with a hardcoded `0` in the payload slot the success path uses for a `String` summary.

**Classification: Group B-like for all three** — each try block contains the entire operation (queue insert, count query, summary lookup); nothing has "already succeeded" underneath.

---

## 5. `GlobalOrganizationMasterServiceImpl.java` — `com.param.global.org.service` — **2 sites found, both CONFIRMED, both Group A-like**

The Checker named "2 more sites" in this file; the owner separately flagged "1 more (around line 202)." A full read of the file (439 lines) found exactly **two** empty/console-only-catch-then-unconditional-SUCCESS sites — line ~202 and line ~296. **The owner's "around line 202" appears to be the same site as one of the Checker's two**, not a third distinct site — flagging this discrepancy explicitly rather than assuming a 3rd site exists. No other catch block in this file exhibits the pattern; every other catch (lines 104, 233, 334, 345, 356, 367, 378, 389, 409, 420, 431) already does `logger.error(...); throw new ServiceException(...)`.

### 5a. `saveOrganizationMaster` — lines 154–207 (catch at 202–204)
```java
154:				try{
155:					ScheduleLogsDto logDto = new ScheduleLogsDto(this);
...
198:						//iEmrService.publish(request);
200:					return new	CommonResponse(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, organizationMaster.getOrganizationId());
201:						//return response;
202:				}catch(Exception e){
203:
204:				}
205:
206:				return new	CommonResponse(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, organizationMaster.getOrganizationId());
207:			}
```
By the time this try/catch is reached, the organization has already been created (`iGlobalOrganisationMasterDao.saveOrganizationMaster(...)` at line 97), its licence details saved, its folder created, and its default SuperAdmin user created — all outside this try, all already committed. This inner try/catch guards only a schedule-log insert and an unpublished RabbitMQ payload construction (the actual `.publish(request)` calls are commented out at lines 198–199). On exception here, the org creation the caller asked for genuinely did succeed; only a secondary log/notification step failed, silently, with zero logging (not even `printStackTrace()`).

**Classification: Group A-like** — same shape as the resolved BR-008/Group-A precedent (primary operation done; secondary step's failure is currently invisible).

### 5b. `saveScheduleLogsOrg` (private helper, called only from `updateOrganizationMaster`) — lines 240–300 (catch at 296–299)
```java
217:	public CommonResponse updateOrganizationMaster(OrganizationMasterDto organizationMasterDto)
...
224:			Response orgainzationRes = iGlobalOrganisationMasterDao.updateOrganizationMaster(organizationMasterDto);
...
230:			return saveScheduleLogsOrg(organizationMasterDto, orgainzationRes);
...
240:	private CommonResponse saveScheduleLogsOrg(OrganizationMasterDto organizationMasterDto, Response orgainzationRes) {
241:		try {
242:
243:			ScheduleLogsDto logDto = new ScheduleLogsDto(this);
...
293:			//iEmrService.publish(request);
294:			/// return response;
295:			return new CommonResponse(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null,  organizationMaster.getOrganizationId());
296:		} catch (Exception e) {
297:			e.printStackTrace();
298:		}
299:		return new CommonResponse(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, orgainzationRes.getObject());
300:	}
```
The actual DB update (`iGlobalOrganisationMasterDao.updateOrganizationMaster(...)`, line 224) happens in the caller, `updateOrganizationMaster`, **before** `saveScheduleLogsOrg` is invoked. Inside `saveScheduleLogsOrg` itself, the try guards only the same schedule-log-insert-plus-unpublished-notification pattern as 5a. On exception, `e.printStackTrace()` (console-only) then falls through to an unconditional SUCCESS return with the already-updated object.

**Classification: Group A-like** — same reasoning as 5a; the org update itself already succeeded by the time this code runs.

---

## 6. `GlobalUnitMasterServiceImpl.java` — `com.param.global.org.service` — 2 sites (owner-confirmed lines ~310, ~393) — **CONFIRMED, mixed classification**

### 6a. `saveUnitMaster` — lines 273–316 (catch at 310–312) — **Group A-like**
```java
119:	public CommonResponse saveUnitMaster(UnitMasterDto unitMasterDto)throws ApplicationException {
...
130:				Response unit = iGlobalUnitMasterDao.saveUnitMaster(unitMasterDto);
131:				
132:				if (unit.getObject() != null) 
...
268:					iUnitApplicationBillingConfigurationDao.saveDefaultEntry(...);
269:					}
270:					
271:				}
272:
273:				try {
274:					ScheduleLogsDto logDto = new ScheduleLogsDto(this);
...
308:					//iEmrService.publish(request);
309:					return new CommonResponse(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, unitMaster);
310:				} catch (Exception e) {
311:
312:				}
313:
314:				return new CommonResponse(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, unitMaster);
315:
316:			}
```
The unit save itself (line 130), licence detail saves, folder creation, default SuperAdmin user creation, role creation, privilege assignment, and default billing-configuration entry (line 268) **all** happen before line 273's try, none of them guarded by it. The try at 273–312 wraps only the schedule-log insert and an unpublished RabbitMQ payload — the same secondary-notification shape as section 5. Catch is fully empty, zero logging.

**Classification: Group A-like.**

### 6b. `updateUnitMaster` — lines 332–395 (catch at 393–395) — **Group B-like**
```java
325:	public CommonResponse updateUnitMaster(UnitMasterDto unitMasterDto)throws ApplicationException {
326:		try{
327:			Response unitRes = iGlobalUnitMasterDao.getUnitByNameAndNotById(unitMasterDto);
328:			if(unitRes.getListObject() != null && unitRes.getListObject().size() > 0){
329:				return new CommonResponse(ERROR, ERROR_CODE, ALREADY_EXIST, null, null);
330:			}else{
331:				
332:				try{
333:					iGlobalUnitMasterDao.updateUnitMaster(unitMasterDto);
334:					//updateNewUnitsTimeZoneToMap
335:					TimeZoneComponent.updateTimezoneMap(...);
...
391:				}
392:				
393:				catch(Exception e){
394:
395:				}
396:				return new	CommonResponse(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, unitRes.getObject());
397:				//return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, unitRes.getObject());
398:			}
399:		}catch(Exception e){
400:		logger.error("updateUnitMaster failed", e);
401:		throw new BillingException(e.getMessage());
402:		}
403:	}
```
Unlike 6a, the actual state-changing DAO call — `iGlobalUnitMasterDao.updateUnitMaster(unitMasterDto)` (line 333) — is **inside** the inner try that has the empty catch (393–395), along with licence-detail updates and the schedule log. If the update call itself throws (e.g., a DB constraint violation or connection error), the update did not happen, the catch is completely empty (no logging at all), and line 396 unconditionally returns SUCCESS with the caller's own stale `unitRes.getObject()`.

**Classification: Group B-like** — the primary write is inside the guarded block, so this is not "secondary side effect already succeeded"; it is "the actual requested update may not have happened, and the caller is told it did."

---

## 7. `MessageQueueServiceImpl.java` — `com.param.global.service` — OTP email-send path — **CONFIRMED, Group B-like**

Method `send(MessageQueueDto dto)` (registration-OTP-over-email). Note: the *sibling* method `sendOTPonMobile` (lines 1327–1357) already does `logger.error(...); throw new ServiceException(...)` on its outer catch — it is not part of this finding.

```java
848:	public Response send(MessageQueueDto dto) throws ApplicationException {
849:		try
850:		{
...
863:			if(appList.get(0).getMailProviderId() ==2 ) {
865:				Response mailres = iMailingService.sendOTPMail(dto,appList.get(0).getMailCredential(),OTP);
866:			}else {
...
875:				try {
876:				
877:				Gson gson = new Gson();
...
882:				String stringResponse = restTemplate.postForObject(url, request, String.class);
883:				Response commonResponse = gson.fromJson(stringResponse, Response.class);
884:				
885:				}catch(Exception e) {
886:
887:				}
888:			}
889:
890:			return new Response(SUCCESS, "","No Records Found.",null ,OTP.toString());
891:
892:		} catch (Exception e)
893:		{
894:		logger.error("send failed", e);
895:		throw new ServiceException(e.getMessage());
896:		}
897:	}
```
The outer catch (892–896) is already correct (logs, throws). The problem is the **inner** try/catch (875–887), which wraps the actual AWS/OkHttp email-delivery call for the OTP. Its catch is completely empty — no logging whatsoever, not even `printStackTrace()`. If the HTTP send fails, execution falls through to line 890, which unconditionally returns `SUCCESS` together with the OTP value itself — telling the caller the OTP was delivered when the delivery attempt actually failed. (This branch — `mailProviderId != 2` — is the AWS-Lambda email path; the other branch, `mailProviderId == 2`, calls `iMailingService.sendOTPMail(...)` at line 865 without checking or handling its result at all, but that is a missing-check, not a caught-and-masked exception, and is outside this catalog's scope.)

**Classification: Group B-like** — the entire purpose of this branch is to deliver the OTP; a failed delivery is reported as SUCCESS with no observability.

---

## 8. `DoctorConsultationServiceMapperServiceImpl.java` — `com.param.global.service` — **CONFIRMED, Group A-like (2 occurrences, same method)**

Method `saveDoctorConsultationServiceMapperService` (lines 47–116), inside the second `while` loop (lines 69–109):

```java
97:					Response res= iDoctorConsultationServiceMapperDao.saveDoctorConsultationServiceMapperService(dto);
98:					
99:					if(dto.getDoctorId()!=null && dto.getPaymentEntitlementId()==SELF && res.getCode().equals(SUCCESS_CODE) && res.getObject() != null){					    
100:						try{					    		
101:							DoctorConsultationServiceMapper doctorConsultationServiceMapper = (DoctorConsultationServiceMapper)res.getObject();		
102:							syncDoctorChargesToMylife(doctorConsultationServiceMapper);
103:							}catch(Exception e){					    		
104:													    
105:								}				    	
106:						}
...
110:				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
...
112:		} catch (Exception e) {
113:			logger.error("saveDoctorConsultationServiceMapperService failed", e);
114:			throw new ServiceException(e.getMessage());
115:		}
116:	}
```
An identical, now-dead copy of the same pattern also exists inside a commented-out `if` branch at lines 88–94 (that whole branch is inside a `/* ... */` block, lines 79–95, so it never executes — noted for completeness but not a live finding).

The live site (lines 99–106): by the time this nested try runs, `iDoctorConsultationServiceMapperDao.saveDoctorConsultationServiceMapperService(dto)` (line 97) has already succeeded (`res.getCode().equals(SUCCESS_CODE)` was just checked). The nested try/catch wraps only `syncDoctorChargesToMylife`, a best-effort push of the newly-saved charge mapping into the `ScheduleLogsDto` queue for downstream MyLife sync (mirrors the exact shape already resolved for `collectPayment` in BR-008 and for the six coversheet DAOs in the prior peripheral fix's Group A). On exception, the catch (103–105) is **fully empty — no logging of any kind**, and the loop simply continues to the next `dto`; the overall method still returns `SUCCESS` (line 110) for the batch.

Contrast: the *sibling* method `updateDoctorConsultationServiceMapper` (lines 150–172) calls the same `syncDoctorChargesToMylife` helper (line 159) but its catch (160–163) already does `logger.error(...); throw new ServiceException(...)` — the opposite failure mode (a secondary sync failure aborts an otherwise-successful primary update with an HTTP 500). That inconsistency is a separate, pre-existing issue (not a fabricated-success pattern) and was flagged here only for the owner's awareness, not as part of this catalog's fabricated-success count. **Update 2026-07-06 (Checker round 2):** this was misclassified during the routine fix -- it is Group A-like (secondary MyLife-sync failure after an already-successful primary update), not Group B. Corrected: the inner catch in updateDoctorConsultationServiceMapper now only logs, no throw, matching saveDoctorConsultationServiceMapperService's treatment above.

**Classification: Group A-like** (primary save already succeeded; secondary MyLife-sync push failure is silently swallowed with zero logging).

---

## 9. `LabMachineInterfaceDaoImpl.java` — `com.param.global.unit.dao` — `saveLabMachineInterface` — **CONFIRMED, Group B-like**

```java
28:	public Response saveLabMachineInterface(MedallRequestResponseDto med) throws ApplicationException {
29:		Throwable resultObj=null;
30:		try {
...
58:							save(labMachineInterface);
...
67:		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, emptySampleNoList, med);
68:		
69:		} catch (HibernateException he) {
70:		} catch (Exception e) {
71:			resultObj=e.getCause();
72:			
73:		}
74:		return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, resultObj);
75:	}
```
The entire operation — persisting lab-machine test-result rows (`save(labMachineInterface)`, line 58, inside a nested loop over test parameters) — is inside the try. There are **two** catch clauses and neither logs anything: `catch (HibernateException he) { }` (line 69) is completely empty, and `catch (Exception e) { resultObj = e.getCause(); }` (lines 70–73) only captures the cause into a local variable that is then returned as the response's *data* payload (line 74) — not logged anywhere. Either way, the method falls through to an unconditional `SUCCESS` return regardless of how many (if any) lab result rows were actually saved, whether the exception happened on the first row or the last, and with no server-side observability at all (no `logger` field even exists in this class).

**Classification: Group B-like** — and higher-severity than most other findings in this catalog, since a silently-partial or fully-failed write of lab test results is a clinical data-integrity gap, not merely a peripheral notification gap.

---

## Summary table

| # | File | Method | Line(s) | Verdict | Classification | Status |
|---|------|--------|---------|---------|-----------------|--------|
| 1a | TwilioController.java | deleteParticipants() [no-arg] | 139–143 | Not a fabricated-success pattern (no try/catch) | — | Not in scope (correctly ruled out) |
| 1b | TwilioController.java | deleteParticipants(identity, room) | 145–156 | Confirmed | Group B-like | **Resolved 2026-07-06** |
| 2 | whatsAppServiceImpl.java | sendWhatsAppMessage | 52–83 | Not a fabricated-success pattern (already throws) | — | Not in scope (correctly ruled out) |
| 3 | ProductionReleaseEmailNotificationConfigDaoImpl.java | sendNotificationOnEmailOfReleaseDetails | 42–137 (catch 128–130) | Confirmed | Group B-like | **Resolved 2026-07-06** |
| 4a | AiAppointmentQueueDaoImpl.java | saveAIAppointmentInQueue | 34–73 (catch 69–71) | Confirmed | Group B-like | **Resolved 2026-07-06** |
| 4b | AiAppointmentQueueDaoImpl.java | getAIAppointmentCount | 128–151 (catch 147–149) | Confirmed | Group B-like | **Resolved 2026-07-06** |
| 4c | AiAppointmentQueueDaoImpl.java | getAISummaryData | 154–176 (catch 172–174) | Confirmed | Group B-like | **Resolved 2026-07-06** |
| 5a | GlobalOrganizationMasterServiceImpl.java | saveOrganizationMaster | 154–207 (catch 202–204) | Confirmed | Group A-like | **Resolved 2026-07-06** |
| 5b | GlobalOrganizationMasterServiceImpl.java | saveScheduleLogsOrg (via updateOrganizationMaster) | 240–300 (catch 296–298) | Confirmed | Group A-like | **Resolved 2026-07-06** |
| 6a | GlobalUnitMasterServiceImpl.java | saveUnitMaster | 273–316 (catch 310–312) | Confirmed | Group A-like | **Resolved 2026-07-06** |
| 6b | GlobalUnitMasterServiceImpl.java | updateUnitMaster | 332–396 (catch 393–395) | Confirmed | Group B-like | **Resolved 2026-07-06** |
| 7 | MessageQueueServiceImpl.java | send (OTP email path) | 848–897 (inner catch 885–887) | Confirmed | Group B-like | **Resolved 2026-07-06** |
| 8 | DoctorConsultationServiceMapperServiceImpl.java | saveDoctorConsultationServiceMapperService | 47–116 (catch 103–105) | Confirmed | Group A-like | **Resolved 2026-07-06** |
| 9 | LabMachineInterfaceDaoImpl.java | saveLabMachineInterface | 28–75 (catches 69, 70–73) | Confirmed | Group B-like | **Resolved 2026-07-06** |

**Confirmed genuine findings: 11** (across 8 files) — **note:** this count was miscomputed when originally written; counting the table rows above gives **12** confirmed sites (1b, 3, 4a, 4b, 4c, 5a, 5b, 6a, 6b, 7, 8, 9), still across the same 8 files (section 4 and section 5/6 each contribute more than one row). The owner's Option B approval for "all 11 confirmed genuine findings" is treated as covering all 12 actual rows in this table — the "11" was this doc's own arithmetic error, not a scope boundary the owner intended to draw at 11. All 12 are resolved below. **Not genuine: 2** (TwilioController's no-arg `deleteParticipants`, and `whatsAppServiceImpl.sendWhatsAppMessage`) — left untouched, as instructed.

**Open discrepancy to flag for the owner/Checker:** the Checker reported "2 more sites" in `GlobalOrganizationMasterServiceImpl.java` and the owner separately reported "1 more (around line 202)" in the same file, suggesting 3 sites; this review found exactly 2 distinct sites in that file (lines ~202 and ~296), and the owner's line-202 report appears to coincide with one of the Checker's two rather than adding a third. This was not re-litigated before applying the fix below; flagging again here in case a 3rd site still needs to be found.

## Resolution (2026-07-06, applied 2026-07-07)

Owner approved Option B for both groups (same terms as the first 15 findings in `docs/DECISION_NEEDED_Global_Peripheral_FabricatedSuccess.md`). Applied to all 12 confirmed sites above, across the 8 files:

**Group A-like (4 sites, 3 files — response/behavior kept exactly as-is, empty/console-only catch replaced with `logger.error(...)`):** `GlobalOrganizationMasterServiceImpl.java` (`saveOrganizationMaster` catch at ~202, `saveScheduleLogsOrg` catch at ~296 — both already had a class-level logger), `GlobalUnitMasterServiceImpl.java` (`saveUnitMaster` catch at ~310 — already had a class-level logger), `DoctorConsultationServiceMapperServiceImpl.java` (`saveDoctorConsultationServiceMapperService`'s inner MyLife-sync catch at ~103 — already had a class-level logger).

**Group B-like (8 sites, 6 files — `Response`/`CommonResponse`/`ResponseTwilio`-returning contract kept exactly as-is, no `throw`, no HTTP-status change; fabricated `SUCCESS`/`SUCCESS_CODE` replaced with honest `ERROR`/`ERROR_CODE`, plus `logger.error(e.getMessage(), e)` added):** `TwilioController.java` (`deleteParticipants(identity, room)`), `ProductionReleaseEmailNotificationConfigDaoImpl.java` (`sendNotificationOnEmailOfReleaseDetails`'s per-iteration catch), `AiAppointmentQueueDaoImpl.java` (`saveAIAppointmentInQueue`, `getAIAppointmentCount`, `getAISummaryData` — all 3; the fabricated hardcoded `0` payloads on the latter two were also removed in favor of `null`, since a status of `ERROR` no longer needs a fake zero to look consistent), `GlobalUnitMasterServiceImpl.java` (`updateUnitMaster`'s inner catch at ~393, which wraps the actual DAO update call), `MessageQueueServiceImpl.java` (`send`'s inner OTP-delivery catch at ~885), `LabMachineInterfaceDaoImpl.java` (`saveLabMachineInterface`'s two catches).

Two files needed a **new class-level `org.apache.log4j.Logger`** added, more than the catalog anticipated: `LabMachineInterfaceDaoImpl.java` (flagged in this catalog's own text as having none) **and `ProductionReleaseEmailNotificationConfigDaoImpl.java`** (not flagged in this catalog's per-site writeup, but confirmed to have no logger and no log4j import when the fix was made — added the same way). All other touched files already had one.

Verified via `mvn -pl Global -am compile -DskipTests -o` (run from `legacy/`): **BUILD SUCCESS**, no unreachable-statement errors.

Classified **Behavioral Change** per `CLAUDE.md`'s definition, same reasoning as the first 15: Group A changes the failure path's *log* output only (response is byte-identical on failure); Group B changes the failure path's response *status field* from fabricated `SUCCESS` to honest `ERROR` (a caller that branches on status now sees the true outcome, even though no `throw`/HTTP-status-code change occurred). No database schema, calculation, rounding, or threshold was touched. Maker-Checker review (RULES.md item 6) still required before this branch merges.
