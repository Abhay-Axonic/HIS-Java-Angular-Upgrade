export interface CouponTemplateRecord {
  couponTemplateMasterId: number;
  couponTemplateName: string;
  couponTemplateDescription?: string | null;
  expireDays?: number | null;
  status: string;
  serviceId?: number | null;
  organizationId?: number | null;
  unitId?: number | null;
}

export interface CouponTemplateDetail {
  couponTemplateDetailsId?: number | null;
  serviceId: number;
  specialityId?: number | null;
  subSpecialityId?: number | null;
  percentage?: number | null;
  amount?: number | null;
  status?: string;
  organizationId?: number | null;
  unitId?: number | null;
}

export interface CouponTemplateSavePayload {
  couponTemplateMasterId?: number | null;
  couponTemplateName: string;
  couponTemplateDescription?: string | null;
  expireDays?: number | null;
  status?: string;
  serviceId?: number | null;
  organizationId?: number;
  unitId?: number;
  createdBy?: number;
  updatedBy?: number;
  couponTemplateDetailsDtoList: CouponTemplateDetail[];
}

export function emptyCouponTemplateRecord(): CouponTemplateRecord {
  return { couponTemplateMasterId: 0, couponTemplateName: '', status: 'A' };
}
