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
	@NamedNativeQuery(name = "GET_DOCTOR_REFERRAL_SHARE_LIST_BY_FILTER",
					 query 	=" SELECT "
					  + " DISTINCT  on (docreffshare.referral_share_id)"
					  + " (SELECT Count(DISTINCT bm.encounter_id) "
					  + "  FROM   billing.t_doctor_referral_share AS docreffshare "
					  + "         INNER JOIN billing.m_billing_master bm "
					  + "                 ON bm.billing_master_id = docreffshare.bill_id "
					  + "  WHERE  docreffshare.referral_doc_id = docreffshare.referral_doc_id "
					  + "    AND to_timestamp(to_char(bm.bill_date_time,'YYYY-MM-DD'),'YYYY-MM-DD') between :fromDate AND :toDate) AS \"docReferralShare\", "
					  + " docreffshare.referral_doc_id \"referralDocId\", "
					 + "   docreffshare.referral_share_id as \"referralShareId\", "
					 + "   docreffshare.total_amount as \"totalAmount\", "
					 + "   docreffshare.concession as \"concession\", "
					 + "   docreffshare.tax_amt as \"taxAmt\", "
					 + "   docreffshare.net_amount as \"netAmount\", "
					 + "   docreffshare.discount AS \"discount\", " 
					 + "   docreffshare.due_share_amt as \"dueShareAmt\", "
				/*	 + "   docreffshare.processed_share_amt as \"processedShareAmt\", "*/
					 + "   EXTRACT( "
					 + "     EPOCH "
					 + "     FROM "
					 + "       enc.encounter_date "
					 + "   ) * 1000 AS \"encounterDate\", "
					 + "   enc.encounter_number AS \"encounterNumber\", "
					 + "   EXTRACT( "
					 + "     EPOCH "
					 + "     FROM "
					 + "       bm.bill_date_time "
					 + "   ) * 1000 AS \"billDateTime\", "
					 + "   bm.bill_number AS \"billNumber\", "
					 + "   pat.uhid_number AS \"uhidNumber\", "
					 + "   bm.visit_type_id AS \"visitTypeId\", "
					 + "   concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.last_name) AS \"patientName\", "
					 + "   concat(doc.first_name, ' ', doc.last_name) AS \"doctorName\", "
					 + "   visitmst.visit_type_code AS \"visitTypeCode\", "
					 + "   bm.patient_id AS \"patientId\" "
					 + " FROM "
					 + "   billing.t_doctor_referral_share as docreffshare "
					 + "   INNER JOIN billing.m_billing_master bm on bm.billing_master_id = docreffshare.bill_id "
					 + "   INNER JOIN patient.t_patient_registration pat on pat.patient_id = bm.patient_id "
					 + "	 left join m_prefix_master pre on pre.prefix_id = pat.prefix_id "
					 + "   INNER JOIN opd.t_encounter_master enc on enc.encounter_id = bm.encounter_id "
					 + "   INNER JOIN doctor.m_doctor_master doc on doc.doctor_id = docreffshare.referral_doc_id "
					 + "   left join adt.t_admission adm on adm.admission_id = bm.admission_id "
					 + "   left join adt.t_admission_details admdt on admdt.admission_id = adm.admission_id "
					 + "   INNER JOIN public.m_visit_type_master visitmst on visitmst.visit_type_id = bm.visit_type_id "
					 + "   INNER JOIN billing.m_doctor_referral_share mDocRefShare on mDocRefShare.referraled_doc_id = docreffshare.referral_doc_id "
					 + " WHERE "
					 + "   docreffshare.org_id =:orgId "
					 + "  AND docreffshare.org_unit_id=:orgUnitId "
					 + "  AND docreffshare.due_share_amt > 0 "
					 + " AND to_timestamp(to_char(bm.bill_date_time,'YYYY-MM-DD'),'YYYY-MM-DD') between :fromDate AND :toDate"),
	/* + " AND bm .bill_date_time <=:toDate AND bm .bill_date_time >=:fromDate"*/
	
	@NamedNativeQuery(name = "GET_DOCTOR_REFERRAL_SHARE_PAYMENT_VOUCHER_DTLS_BY_REFERRAL_SHARE_ID",
	 query 	="SELECT "
			 + "   EXTRACT( "
			 + "     EPOCH "
			 + "     FROM "
			 + "       bm.bill_date_time "
			 + "   ) * 1000 AS \"billDateTime\", " 
			 + "   bm.bill_number AS \"billNumber\", " 
			 + "   concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.last_name) AS \"patientName\", " 
			 + "   pat.uhid_number AS \"uhidNumber\", " 
			 + "   bm.visit_type_id AS \"visitTypeId\", " 
			 + "   visitmst.visit_type_code AS \"visitTypeCode\", " 
			 + "   EXTRACT( "
			 + "     EPOCH "
			 + "     FROM "
			 + "       enc.encounter_date "
			 + "   ) * 1000 AS \"encounterDate\", " 
			 + "   enc.encounter_number AS \"encounterNumber\", " 
			 + "   concat(doc.first_name, ' ', doc.last_name) AS \"doctorName\", " 
			 + "   spl.speciality_name AS \"specialityName\", " 
			 + "   ds.total_amount AS \"totalAmount\", " 
			 + "   ds.discount AS \"discount\", " 
			 + "   ds.tax_amt AS \"taxAmt\", " 
			/* + "   cast(ds.referral_share_per as integer)  AS \"docShare\", " */
			 + "   ds.referral_share_per  AS \"docShare\", " 
			 + "   ds.net_amount AS \"netAmount\", " 
			 + "   dsvd.processed_amt AS \"processedShareAmt\" " 
			 + " FROM "
			 + "   billing.t_doc_share_voucher dsv "
			 + "   INNER JOIN billing.t_doc_share_voucher_dtls dsvd on dsvd.doc_share_vouche_id = dsv.doc_share_vouche_id "
			 + "   INNER JOIN billing.t_doctor_referral_share ds ON ds.referral_share_id = dsvd.referral_share_id "
			 + "   INNER JOIN billing.m_billing_master bm ON bm.billing_master_id = ds.bill_id "
			 + "   INNER JOIN patient.t_patient_registration pat ON pat.patient_id = bm.patient_id "
			 + "	 left join m_prefix_master pre on pre.prefix_id = pat.prefix_id "
			 + "   INNER JOIN public.m_visit_type_master visitmst ON visitmst.visit_type_id = bm.visit_type_id "
			 + "   INNER join opd.t_encounter_master enc ON enc.encounter_id = bm.encounter_id "
			 + "   INNER JOIN doctor.m_doctor_master doc ON doc.doctor_id = ds.referral_doc_id "
			 + "   INNER JOIN doctor.m_speciality_master spl ON spl.speciality_id = ds.referral_doc_id "
			 + " WHERE "
			 + "   dsv.org_id =:orgId"
			 + "   and dsv.org_unit_id =:orgId"
			 + "   and dsvd.referral_share_id = :referralShareId")
})

@Entity
@Table(name="t_doctor_referral_share", schema="billing")
@SequenceGenerator(name = "t_seq_doctor_referral_share", sequenceName = "billing.t_seq_doctor_referral_share", allocationSize = 1)
public class TDoctorReferralShare {
	@Id
	@Column(name="referral_share_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_seq_doctor_referral_share")
	private Integer referralShareId;
	
	@Column(name = "bill_id")
	private Integer billId;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
	@Column(name = "total_amount")
	private Double totalAmount;
	
	@Column(name = "net_amount")
	private Double netAmount;


	@Column(name = "tax_id")
	private Integer taxId;

	@Column(name = "tax_type")
	private Integer taxType;
	
	
	@Column(name = "referral_doc_id")
	private Integer referralDocId;


	@Column(name = "referral_share_per")
	private Double referralSharePer;
	
	@Column(name = "processed_share_amt")
	private Double processedShareAmt;
	
	@Column(name = "due_share_amt")
	private Double dueShareAmt;
	

	@Column(name = "is_bill_cr_dr")
	private Integer isBillCrDr;
	
	@Column(name = "is_active")
	private Integer isActive;
	
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

	public Integer getReferralShareId() {
		return referralShareId;
	}

	public void setReferralShareId(Integer referralShareId) {
		this.referralShareId = referralShareId;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
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

	public Integer getReferralDocId() {
		return referralDocId;
	}

	public void setReferralDocId(Integer referralDocId) {
		this.referralDocId = referralDocId;
	}

	public Double getReferralSharePer() {
		return referralSharePer;
	}

	public void setReferralSharePer(Double referralSharePer) {
		this.referralSharePer = referralSharePer;
	}

	public Double getProcessedShareAmt() {
		return processedShareAmt;
	}

	public void setProcessedShareAmt(Double processedShareAmt) {
		this.processedShareAmt = processedShareAmt;
	}

	public Double getDueShareAmt() {
		return dueShareAmt;
	}

	public void setDueShareAmt(Double dueShareAmt) {
		this.dueShareAmt = dueShareAmt;
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
		
}
