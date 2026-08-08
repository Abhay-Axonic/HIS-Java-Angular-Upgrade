# DECISIONS_QUEUE — owner review items queued during the campaign cycles

Each entry: what was found, why it needs a decision (not an autonomous fix), the
options, and the recommendation. Nothing here is acted on without the owner.

---

## DQ-2026-08-08-001 — Campaign 1 (drift): 35 phantom TABLES need per-case classification

Found: 35 entity-mapped tables are absent from BOTH the live DB and the
reference DDL, and are referenced by at least one query (DAO raw SQL or entity
named query). Every reference is a runtime failure today (relation does not
exist). Examples:
  - billing.t_cc_avenue_log — UPDATE path in MpesaMasterDaoImpl (CCAvenue is a
    LIVE partner config — BR-012 retracted — this path may be in use)
  - m_razorpay_order_log_details (schema-less @Table) — UPDATE in
    MpesaMasterDaoImpl (razorpay)
  - procurement.t_grn_bonus_batch_expiry — DELETE in GrnBonusBatchExpiryDao
  - opd.ai_appointment_queue — SELECT count in AiAppointmentQueueDaoImpl
  - inventory.t_returnable_bonus_batch_expiry — DELETE
  - emr.t_discharge_sum_tab_columns_print_mapper — UPDATE (ADT discharge)
  Module spread: public 12, billing 4, abdm 3, adt 3, doctor 2, emr 2, opd 2,
  procurement 2, service 2, inventory 1, patient 1, schema-less 1.

Why a decision: there is NO entity-side fix that preserves behavior. Options per
table: (a) the DAO path is dead code -> remove entity + DAO method + query
(BR-183 pattern, safe); (b) the table SHOULD exist -> schema change (NOT
permitted this campaign) -> the defect is real and the endpoint is broken for
all callers; (c) the query is dead -> remove the query only. Each of the 35
needs the caller trace (who calls the DAO method, is it reachable).

Recommendation: batch the caller trace for all 35, split dead vs should-exist,
owner decides the should-exist set (schema add is a Phase-2/3 question).

## DQ-2026-08-08-002 — Campaign 1 (drift): 60 typo-class phantom columns

Found: 60 @Column names across 15 tables carry a trailing-space/typo so the
mapped name ("patient_id ", "code ", "amount ") does not exist in live/DDL,
while the stripped name does. All 60 are query-referenced. Fix = correct the
@Column name — BUT the corrected mapping changes behavior: any Hibernate
WRITE to the field currently fails (column "patient_id " does not exist) and
would start succeeding. That is a behavioral change -> BR entry per item (or
one BR for the class) + owner sign-off, per the constitution.

Recommendation: one BR for the class (60 items, same pattern), verification =
the write endpoints execute for legitimate callers pre/post.

## DQ-2026-08-08-003 — Campaign 1 (drift): 240 true phantom columns fix plan

The 240 query-referenced phantom columns (BR-183 class) are the mechanical
fixable set: remove the phantom field + @Column + every query reference,
entity-side only. Each needs the BR-183-style Rule-10 trace (field -> all
references -> removal). NOT a decision item — executing per the mandate — listed
here for the count.

## DQ-2026-08-08-004 — Batch 28 gating: the six batch services carry 62/73
unswept org-trust methods (Campaign 2 scope). Not a decision — execution order
per the mandate.

## DQ-2026-08-08-003 — Batch-1 SILENT SAVE (owner-raised, 3rd gate item): batch 1 replaced a loud failure with silent data loss on the mailConfiguration save
- FINDING (confirmed): post-batch-1, toggling is_sms_send/is_whatsapp_send/critical_report/user_defined_mail/whatsapp_template_master_id and saving returns SUCCESS + "Saved Successfully" while the values are silently dropped (the columns exist in NO schema; the setters were removed in batch 1). Pre-fix the same save returned ERROR (the dup-check query failed) — so batch 1 flipped a loud failure into silent acceptance on a messaging-config screen. The codebase's worst documented pattern (mlc_no class), introduced by the fix itself.
- EVIDENCE the table never held a row: public.m_sms_utility_cofiguration = 0 rows; m_sms_utility_cofiguration_seq last_value=1, is_called=false — the sequence has NEVER been called → no INSERT was ever attempted by any path → the save path has never persisted, in any environment running this schema. Read paths consuming GET_SMS_UTILITY_CONFIG_MASTER_LIST: SmsUtilityConfigDaoImpl:51 (saveSmsUtilityConfig dup-check) + :179 (getSmsUtilityConfigDetails list) — both were broken pre-fix, both execute post-fix.
- UI CONTROLS bound to the 5 phantom values: legacy sms-utility-configuration.html:59 (isSMSSend checkbox), :70 (isWhatsappSend checkbox), :77-81 (whatsAppTemplateMasterId select, conditional on isWhatsappSend=='Y' && whatsAppOption==4), :336-337 (grid cells); mail-utility-configuration.html:73 (isWhatsappSend checkbox), :79-87 (whatsAppTemplateMasterId select), :99 (criticalReport checkbox — name attribute copy-pasted as isWhatsappSend), :351 (grid cell); controllers smsUtilityConfigurationController.js:88-89,322-325,458-465 + mailUtilityConfigurationController.js:88; new/ Angular sms-configuration: models.ts:20-27 + html:34-61 (isSMSSend + isWhatsappSend checkboxes + whatsAppTemplateMasterId select).
- OPTIONS (owner decision): (a) server-side guard rejecting non-'N'/non-null phantom payload values with an explicit error envelope (legacy-only, deploy-only, smallest, mandatory layer); (b) remove the dead controls from the legacy templates + new/ screen (legacy = deploy-only; new/ = needs the owner's ng build); (c) schema add for the 5 columns (Phase-2/3 gate — out of this campaign). RECOMMENDATION: (a) now + (b) legacy with the same deploy, (b)-new/ flagged for the next new/ build.

## DQ-2026-08-08-002 — (updated) deployed-vs-source hibernate config: the SOURCE cfg is stale, not the deployed WAR
- RESOLVED portion: the deployed ROOT (tomcat@9 webapps/ROOT/WEB-INF) runs WEB-INF/classes/hibernate.cfg.xml whose mapping set is IDENTICAL to the source cfg, and its WEB-INF/lib/Global-0.0.1-SNAPSHOT.jar STILL CONTAINS com.param.global.model.UnitSubscriptionDetails.class (verified by unzip). The app boots because the deployed jar predates the class's removal from the source.
- OPEN: the SOURCE modules no longer contain the class (only UnitSubscriptionDetailsDto) while the source hibernate.cfg.xml still maps it → the NEXT WAR build's SessionFactory will fail to boot (MappingException). Fix when the build next happens: remove the mapping entry from the source cfg (or restore the class). Logged, not chased.

## DQ-2026-08-08-004 — the SMS/WhatsApp channel configuration is a never-persisted feature (screen-level functional gap; the guard cannot cover it)
- EMPIRICAL (real-browser capture, 2026-08-08): the sms-utility-configuration screen's save validation AUTO-DEFAULTS isSmsSend='Y' when both channels are 'N' (smsUtilityConfigurationController.js:244-246 — verified in the live scope: isSMSSend="Y" after a save attempt on a fully untouched form) and REQUIRES a whatsapp template id when the whatsapp channel is on. So a valid save ALWAYS carries isSMSSend='Y' and/or whatsAppTemplateMasterId — the channel values are mandatory, not optional. The channel columns exist in NO schema — every valid save silently drops the channel configuration (the mlc_no class, at screen scale).
- The guard (dad44354) rejected those mandatory values → the screen became unusable (false positive) → CORRECTED (2026-08-08): the guard now covers only the NON-mandatory phantoms (critical_report, user_defined_mail, type); the channel-family values (is_sms_send, is_whatsapp_send, whatsapp_template_master_id) are exempt + their silent drop is THIS item.
- OPTIONS (owner decision): (a) schema add for the 6 channel columns (Phase-2/3 gate — the real fix; the screen becomes functional); (b) remove the screen + its routes/JS (the feature never worked — dead feature removal); (c) accept the silent drop (NOT recommended — the codebase's worst pattern, at screen scale).
- RECOMMENDATION: (a) — the columns are in the reference DDL (type IS in the DDL per the reference schema... verified: the m_sms_utility_cofiguration DDL block has 14 columns — the 6 phantoms are absent from the DDL too — so the schema add would be a NEW design, not a DDL restoration — owner decides the column definitions).
- UPDATE (2026-08-08, after the second guard correction): the mail screen's `type` select (mail-utility-configuration.html:448 — Daily/Monthly/Weekly/Yearly, required) is ALSO a screen-required phantom — every valid mail save carries a non-empty type → the guard's type condition false-positived too → the type joins the channel-family exemption. The guard's final scope: critical_report + user_defined_mail only (the genuinely-optional phantoms). The type control is included in the removal scope (item (b) below: 7 controls across the screens).
