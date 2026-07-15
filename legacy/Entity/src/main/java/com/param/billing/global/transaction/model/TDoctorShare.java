package com.param.billing.global.transaction.model;



import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.DateConverter;


@NamedNativeQueries({
	
	@NamedNativeQuery(name = "GET_DOCTOR_SHARE_PAYMENT_VOUCHER_LIST",
	 query 	=" select " 
			 + " 	distinct docShareVoucher.doc_share_vouche_id as \"docShareVoucherId\", " 
			 + " 	docShareVoucher.voucher_no as \"voucherNo\", " 
			 + " 	vdtls.referral_share_id as \"referralShareId\", " 
			 + " 	docShareVoucher.total_doc_share_amout as \"totalAmount\", " 
			 + " 	to_char(docShareVoucher.created_date, 'DD-MM-YYYY') as \"voucherDate\", " 
			 + " 	docShareVoucher.processed_amt as \"processedShareAmt\", " 
			 + " 	docShareVoucher.due_share_amt as \"dueShareAmt\", " 
			 + " 	docShareVoucher.due_share_amt as \"paidAmount\", " 
			 + " 	0 as \"retainershipFee\", " 
			 + " 	0 as \"tdsAmt\", " 
			 + " 	concat(docMst.first_name, ' ', docMst.last_name) as \"doctorName\", " 
			 //+ " 	spc.speciality_name as \"specialityName\", " 
			 + " 	docShareVoucher.type as \"typeId\", " 
			 + "	docMst.doctor_id as \"docId\", " 
			 + "	docShareVoucher.due_date as \"dueDate\" " 
			 + " from " 
			 + " 	billing.t_doc_share_voucher docShareVoucher " 
			 + " inner join billing.t_doc_share_voucher_dtls vdtls on " 
			 + " 	vdtls.doc_share_vouche_id = docShareVoucher.doc_share_vouche_id " 
			 + " inner join doctor.m_doctor_master docMst on " 
			 + " 	docMst.doctor_id = docShareVoucher.doc_id " 
			 + " where " 
			 + " 	docShareVoucher.org_id =:orgId " 
			 + " 	and docShareVoucher.org_unit_id =:orgUnitId " 
			 + " 	and docShareVoucher.due_share_amt > 0 " 
			 + " 	and docMst.doctor_id =:doctorId " 
			 + " 	and to_timestamp(to_char(docShareVoucher.created_date, 'YYYY-MM-DD'), 'YYYY-MM-DD') between :fromDate and :toDate " ),
	
	@NamedNativeQuery(name = "GET_DOCTOR_SHARE_PROCESSED_VOUCHER_LIST",
	 query 	="select " 
			 + " vou.payment_voucher_no as \"voucherNo\"," 
			 + " to_char(vou.created_date, 'YYYY-MM-DD') as \"voucherDate\", " 
			 + " concat(coalesce(doc.first_name, ''), ' ', coalesce(doc.middle_name, ''), ' ', coalesce(doc.last_name, '')) as \"doctorName\", " 
			 + " ref_mst.referral_name as \"referralName\", " 
			 + " vou.total_consumed_doctor_advance as \"consumedDoctorAdvance\"," 
			 + " (select sum(tda.balance_advance) from billing.t_doctor_advance tda where tda.doctor_id = vou.doctor_id ) as \"doctorAdvance\"," 
			 + " vou.total_consumed_doctor_cr as \"consumedDoctorCredit\"," 
			 + " (select sum(tdcn.balance_credit) from billing.t_doctor_cr_note_details tdcn where tdcn.referral_id = vou.referral_id ) as \"totalReferralCredit\"," 
			 + " (select sum(tdcn.balance_credit) from billing.t_doctor_cr_note_details tdcn where tdcn.doctor_id = vou.doctor_id ) as \"totalDoctorCredit\"," 
			 + " vou.total_paid_amt as \"paidAmount\"," 
			 + " vou.total_retainer_ship_amt as \"retainerShipFee\" " 
			 + " from billing.t_doc_share_payment_voucher vou " 
			 + " left join doctor.m_doctor_master doc on doc.doctor_id = vou.doctor_id " 
			 + " left join adt.m_referral_master ref_mst on ref_mst.referral_id = vou.referral_id " 
			 + " where  vou.org_id = :organizationId AND vou.org_unit_id = :unitId "
			 + " AND to_char(vou.created_date, 'YYYY-MM-DD') between :fromDate and :toDate "),
	
	@NamedNativeQuery(name="GET_DOCTOR_REFERRAL_PAYMENT_VOUCHER_LIST",query="select " 
			 + " 	distinct docShareVoucher.doc_share_vouche_id as \"docShareVoucherId\", " 
			 + " 	docShareVoucher.voucher_no as \"voucherNo\", " 
			 + " 	vdtls.referral_share_id as \"referralShareId\", " 
			 + " 	docShareVoucher.total_doc_share_amout as \"totalAmount\", " 
			 + " 	to_char(docShareVoucher.created_date, 'DD-MM-YYYY') as \"voucherDate\", " 
			 + " 	docShareVoucher.processed_amt as \"processedShareAmt\", " 
			 + " 	docShareVoucher.due_share_amt as \"dueShareAmt\", " 
			 + " 	docShareVoucher.due_share_amt as \"paidAmount\", " 
			 + " 	0 as \"retainershipFee\", " 
			 + " 	referral.referral_name as \"referralName\", " 
			 + " 	1 as \"typeId\", " 
			 + " 	docShareVoucher.referral_id as \"referralId\" " 
			 + " from " 
			 + " 	billing.t_doc_share_voucher docShareVoucher " 
			 + " inner join billing.t_doc_share_voucher_dtls vdtls on " 
			 + " 	vdtls.doc_share_vouche_id = docShareVoucher.doc_share_vouche_id " 
			 + " inner join billing.t_doctor_share docShare on " 
			 + " 	docShare.doctor_share_id = vdtls.doctor_share_id " 
			 + " inner join adt.m_referral_master referral on " 
			 + " 	referral.referral_id = docShareVoucher.referral_id " 
			 + " where " 
			 + " 	docShareVoucher.org_id =:orgId " 
			 + " 	and docShareVoucher.org_unit_id =:orgUnitId " 
			 + " 	and docShareVoucher.due_share_amt > 0 " 
			 + " 	and docShareVoucher.referral_id =:referralId " 
			 + " 	and to_timestamp(to_char(docShareVoucher.created_date, 'YYYY-MM-DD'), 'YYYY-MM-DD') between :fromDate and :toDate  " ),
	
	@NamedNativeQuery(name = "GET_DOCTOR_SHARE_PAYMENT_VOUCHER_DTLS_BY_DOC_SHARE_ID",
	 query 	=" SELECT "
			 + "   to_char(bm.bill_date_time,'DD/MM/YYYY HH24:MI:SS')  AS \"billDateTime\", "
			 + "   bm.bill_number AS \"billNumber\", "
			 + "   concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.last_name) AS \"patientName\", "
			 + "   pat.uhid_number AS \"uhidNumber\", "
			 + "   bm.visit_type_id AS \"visitTypeId\", "
			 + "   visitmst.visit_type_code AS \"visitTypeCode\", "
			 + "   to_char(enc.encounter_date,'DD/MM/YYYY') AS \"encounterDate\", "
			 + "   enc.encounter_number AS \"encounterNumber\", "
			 + "   to_char(adm.created_date,'DD/MM/YYYY') AS \"admissionDate\", "
			 + "   adm.admission_number AS \"admissionNumber\", "
			 + "   concat(doc.first_name, ' ', doc.last_name) AS \"doctorName\", "
			 + "   doc.doctor_id AS \"docId\", "
			 + "   spl.speciality_name AS \"specialityName\", "
			 + "   service.service_standard_code AS \"serviceStandardCode\", "
			 + "   service.service_standard_name AS \"serviceStandardName\", "
			 + "   ds.unit_rate AS \"unitRate\", "
			 + "   ds.total_amount AS \"totalAmount\", "
			 + "   bdtl.quantity AS \"quantity\", " 
			 + "   ds.discount AS \"discount\", "
			 + "   ds.tax_amt AS \"taxAmt\", "
			 + "   coalesce(ds.hosp_share,0) AS \"hospShare\", "
			 + "   ds.doc_share AS \"docShare\", "
			 + "   ds.net_amount AS \"netAmount\", "
			 + "   dsvd.processed_amt AS \"processedShareAmt\" "
			 + " FROM "
			 + "   billing.t_doc_share_voucher dsv "
			 + "   INNER JOIN billing.t_doc_share_voucher_dtls dsvd on dsvd.doc_share_vouche_id = dsv.doc_share_vouche_id "
			 + "   INNER JOIN billing.t_doctor_share ds ON ds.doctor_share_id = dsvd.doctor_share_id "
			 + "   INNER JOIN billing.t_billing_details bdtl ON bdtl.billing_details_id = ds.bill_dtl_id "
			 + "   INNER JOIN billing.m_billing_master bm ON bm.billing_master_id = bdtl.billing_master_id "
			 + "  INNER JOIN patient.t_patient_registration pat ON pat.patient_id = bm.patient_id "
			 + "   inner join public.m_prefix_master pre on	pre.prefix_id = pat.prefix_id "
			 + "   INNER JOIN public.m_visit_type_master visitmst ON visitmst.visit_type_id = bm.visit_type_id "
			 //+ "   INNER join opd.t_encounter_master enc ON enc.encounter_id = bm.encounter_id "
			 + " left join opd.t_encounter_master enc on "
			 + "enc.encounter_id = bm.encounter_id and  bm.visit_type_id  = 1 "
			 + " left join adt.t_admission adm on "
			 + " adm.admission_id = bm.admission_id and  bm.visit_type_id  != 1 "
			 + "   LEFT JOIN doctor.m_doctor_master doc ON doc.doctor_id = bdtl.ord_doc_id "
			 + "   LEFT JOIN doctor.m_speciality_master spl ON spl.speciality_id = bdtl.ord_doc_spl_id "
			 + "   INNER JOIN service.m_service_master service ON service.service_master_id = ds.service_id "
			 + " WHERE "
			 + "   dsv.org_id =:orgId "
			 + "   and dsv.org_unit_id =:orgUnitId "
			 + "   and dsv.doc_share_vouche_id =:docShareVoucherId ")
})


@Entity
@Table(name="t_doctor_share", schema="billing")
@SequenceGenerator(name = "t_seq_doctor_share", sequenceName = "billing.t_seq_doctor_share", allocationSize = 1)
public class TDoctorShare implements Cloneable{
	@Id
	@Column(name="doctor_share_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_seq_doctor_share")
	private Integer doctorShareId;
	
	@Column(name = "bill_dtl_id")
	private Integer billDtlId;
	
	@Column(name = "visit_type_id")
	private Integer visitTypeId;

	@Column(name = "ord_dtl_id")
	private Integer ordDtlId;
	
	@Column(name = "visit_id")
	private Integer visitId;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "unit_rate")
	private Double unitRate;
	
	@Column(name = "total_amount")
	private Double totalAmount;
	
	@Column(name = "net_amount")
	private Double netAmount;

/*	@Column(name = "bill_date_time")
	@Convert(converter=DateConverter.class)
	private Long billDateTime;*/

	@Column(name = "tax_id")
	private Integer taxId;

	@Column(name = "tax_type")
	private Integer taxType;
	
	@Column(name = "doc_id")
	private Integer docId;

	@Column(name = "doc_spec_id")
	private Integer docSpecId;

	@Column(name = "share_per")
	private Double sharePer;
	
	@Column(name = "hosp_share")
	private Double hospShare;
	
	@Column(name = "doc_share")
	private Double docShare;
	
	@Column(name = "processed_share_amt")
	private Double processedShareAmt;
	
	@Column(name = "due_share_amt")
	private Double dueShareAmt;
	
	@Column(name = "processed_share_date")
	@Convert(converter=DateConverter.class)
	private Long processedShareDate;
	
	@Column(name = "due_share_date")
	@Convert(converter=DateConverter.class)
	private Long dueShareDate;

	@Column(name = "is_bill_cr_dr")
	private Integer isBillCrDr;
	
	@Column(name = "is_active")
	private Integer isActive;
	
	@Column(name = "is_on_bill_payment")
	private Integer isOnBillPayment;
	
	@Column(name = "discount")
	private Double discount;

	@Column(name = "concession")
	private Double concession;
	
	@Column(name = "tax_amt")
	private Double taxAmt;
	
	@Column(name = "org_unit_id")
	private Integer orgUnitId;

	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "status")
	private Character status;
	
	@Column(name = "is_share_split")
	private Character isShareSplit;
	 
	@Column(name = "created_date")
	@Convert(converter=DateConverter.class)
	private Long createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	@Convert(converter=DateConverter.class)
	private Long updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "total_doc_share")
	private Double totalDocShare;
	
	@Column(name = "parent_doc_share_id")
	private Integer parentDocShareId;
	
	@Column(name = "referral_id")
	private Integer referralId;
	
	@Column(name = "against_cr_id")
	private Integer againstCrId;
	
	@Column(name = "cr_net_amount")
	private Double crNetAmount;
	
	public Double getCrNetAmount() {
		return crNetAmount;
	}

	public void setCrNetAmount(Double crNetAmount) {
		this.crNetAmount = crNetAmount;
	}

	public Integer getAgainstCrId() {
		return againstCrId;
	}

	public void setAgainstCrId(Integer againstCrId) {
		this.againstCrId = againstCrId;
	}

	public Integer getReferralId() {
		return referralId;
	}

	public void setReferralId(Integer referralId) {
		this.referralId = referralId;
	}

	public Integer getParentDocShareId() {
		return parentDocShareId;
	}

	public void setParentDocShareId(Integer parentDocShareId) {
		this.parentDocShareId = parentDocShareId;
	}

	public Double getTotalDocShare() {
		return totalDocShare;
	}

	public void setTotalDocShare(Double totalDocShare) {
		this.totalDocShare = totalDocShare;
	}

	public Integer getDoctorShareId() {
		return doctorShareId;
	}

	public void setDoctorShareId(Integer doctorShareId) {
		this.doctorShareId = doctorShareId;
	}

	public Integer getBillDtlId() {
		return billDtlId;
	}

	public void setBillDtlId(Integer billDtlId) {
		this.billDtlId = billDtlId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getOrdDtlId() {
		return ordDtlId;
	}

	public void setOrdDtlId(Integer ordDtlId) {
		this.ordDtlId = ordDtlId;
	}

	public Integer getVisitId() {
		return visitId;
	}

	public void setVisitId(Integer visitId) {
		this.visitId = visitId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Double getUnitRate() {
		return unitRate;
	}

	public void setUnitRate(Double unitRate) {
		this.unitRate = unitRate;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

/*	public Long getBillDateTime() {
		return billDateTime;
	}

	public void setBillDateTime(Long billDateTime) {
		this.billDateTime = billDateTime;
	}*/

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public Integer getTaxType() {
		return taxType;
	}

	public void setTaxType(Integer taxType) {
		this.taxType = taxType;
	}

	public Integer getDocId() {
		return docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	public Integer getDocSpecId() {
		return docSpecId;
	}

	public void setDocSpecId(Integer docSpecId) {
		this.docSpecId = docSpecId;
	}

	public Double getSharePer() {
		return sharePer;
	}

	public void setSharePer(Double sharePer) {
		this.sharePer = sharePer;
	}

	public Double getHospShare() {
		return hospShare;
	}

	public void setHospShare(Double hospShare) {
		this.hospShare = hospShare;
	}

	public Double getDocShare() {
		return docShare;
	}

	public void setDocShare(Double docShare) {
		this.docShare = docShare;
	}

	public Double getProcessedShareAmt() {
		return processedShareAmt;
	}

	public void setProcessedShareAmt(Double processedShareAmt) {
		this.processedShareAmt = processedShareAmt;
	}

	public Integer getIsBillCrDr() {
		return isBillCrDr;
	}

	public void setIsBillCrDr(Integer isBillCrDr) {
		this.isBillCrDr = isBillCrDr;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Integer getIsOnBillPayment() {
		return isOnBillPayment;
	}

	public void setIsOnBillPayment(Integer isOnBillPayment) {
		this.isOnBillPayment = isOnBillPayment;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getConcession() {
		return concession;
	}

	public void setConcession(Double concession) {
		this.concession = concession;
	}

	
	public Double getTaxAmt() {
		return taxAmt;
	}

	public void setTaxAmt(Double taxAmt) {
		this.taxAmt = taxAmt;
	}



	public Integer getOrgUnitId() {
		return orgUnitId;
	}

	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}


	public Double getDueShareAmt() {
		return dueShareAmt;
	}

	public void setDueShareAmt(Double dueShareAmt) {
		this.dueShareAmt = dueShareAmt;
	}

	public Long getProcessedShareDate() {
		return processedShareDate;
	}

	public void setProcessedShareDate(Long processedShareDate) {
		this.processedShareDate = processedShareDate;
	}

	public Long getDueShareDate() {
		return dueShareDate;
	}

	public void setDueShareDate(Long dueShareDate) {
		this.dueShareDate = dueShareDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public Character getIsShareSplit() {
		return isShareSplit;
	}

	public void setIsShareSplit(Character isShareSplit) {
		this.isShareSplit = isShareSplit;
	}
	
}
