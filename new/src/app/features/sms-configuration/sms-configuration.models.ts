/**
 * Grid row + form payload shape for `/api/globals/sms-utility-config*`, see
 * smsUtilityConfigurationController.js:305-326 (`data` object built in
 * `save()`) and :384-411 (`getSmsConfigurationList()`'s raw list rows).
 * Legacy sends/receives `isSMSSend`/`isWhatsappSend` as the literal strings
 * `'Y'`/`'N'`, not booleans -- kept as strings here for parity.
 */
export interface SmsUtilityConfiguration {
  smsUtilityId?: number;
  triggerId?: number;
  triggerName?: string;
  templateDescription?: string;
  categoryId?: number;
  categoryName?: string;
  orgId?: number;
  unitId?: number;
  status?: string; // 'A' | 'I'
  isEmail?: boolean;
  mailSubject?: string;
  isSMSSend?: string; // 'Y' | 'N'
  isWhatsappSend?: string; // 'Y' | 'N'
  userDefinedMail?: string | null;
  scheduleTypeId?: number; // 1 = Triggered SMS tab, 2 = Scheduled SMS tab
  criticalReport?: unknown;
  whatsAppTemplateDesc?: string;
  type?: unknown;
  whatsAppTemplateMasterId?: number;
  [key: string]: unknown;
}

/** `/trigger-master` row -- ctrl:151-176 buckets these by `isSms`/`triggerType`. */
export interface TriggerOption {
  triggerId: number;
  triggerName: string;
  triggerType?: number;
  isSms?: boolean;
  [key: string]: unknown;
}

/** `/category-master` row -- ctrl:179-201 buckets these by `categoryType`. */
export interface CategoryOption {
  categoryId: number;
  categoryName: string;
  categoryType?: number;
  [key: string]: unknown;
}

/** `/requiredData/trigger/{triggerId}` row -- ctrl:473-484. */
export interface RequiredDataOption {
  data: string;
  dataDesc: string;
  [key: string]: unknown;
}

/** `/getWhatsAppTemplateList/{orgId}/{unitId}` row -- ctrl:686-703. */
export interface WhatsAppTemplateOption {
  whatsAppTemplateMasterId: number;
  whatsAppTemplateDesc: string;
  [key: string]: unknown;
}

/**
 * `/getMobileList/trigger/{triggerId}/{orgId}/{unitId}` raw row shape --
 * ctrl:591-611 reads `response.data` directly (NOT `response.data.listObject`
 * like every other endpoint on this screen), i.e. this one endpoint returns
 * a bare array, not the shared `ApiResponse` envelope. Kept faithful.
 */
export interface MobileListEntry {
  mobileNo: string;
  [key: string]: unknown;
}

/** Shared shape for both tabs' small add/remove chip lists (ctrl:435, 667). */
export interface MobileNumberEntry {
  title: string;
  done?: boolean;
}

/**
 * `/api/unit/applicationConfiguration/getUnitApplicationRegistrationFieldConfig/{orgId}/{unitId}`
 * row -- ctrl:96-140. Only `mobileDigit` is used by this screen.
 */
export interface RegistrationFieldConfig {
  mobileDigit?: string;
  [key: string]: unknown;
}
