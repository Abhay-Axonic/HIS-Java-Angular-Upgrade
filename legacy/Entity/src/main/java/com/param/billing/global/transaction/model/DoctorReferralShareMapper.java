package com.param.billing.global.transaction.model;

import java.util.Date;

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
	@NamedNativeQuery(name="GET_DOCTOR_REFERRAL_DETAILS_BY_DOCTOR_REFERRAL_ID",
	 query="  select mppr.no_of_cases as \"noOfCases\",mppr.share as \"share\","
+"    mppr.org_unit_id as \"unitId\","
+"    mppr.org_id as \"orgId\","
+"    mppr.referraled_doc_id as \"referraledDocId\","
+"    mRefShr.visit_type_id as \"visitTypeId\""
+"   from billing.t_doctor_referral_share_mapper mppr"
+"   inner join billing.m_doctor_referral_share mRefShr on mRefShr.referral_share_id=mppr.referral_share_id"
+"   inner join public.m_visit_type_master vm on vm.visit_type_id=mRefShr.visit_type_id"
+"   where mppr.referraled_doc_id=:referralDocId AND mppr.status='A' "
+ "  AND mRefShr.visit_type_id=:visitTypeId"
+ " AND mppr.org_id=:orgId AND mppr.org_unit_id=:unitId")
})
@Entity
@Table(name="t_doctor_referral_share_mapper",schema="billing")
@SequenceGenerator(name="doctor_referal_share_mapper_seq",sequenceName="billing.doctor_referal_share_mapper_seq",allocationSize=1)
public class DoctorReferralShareMapper {
	
	@Id
	@Column(name="doctor_referal_share_mapper_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "doctor_referal_share_mapper_seq")
	private Integer doctorReferalShareMapperId;
	
	@Column(name="referral_share_id")
	private Integer referralShareId;
	
	@Column(name="doc_referral_id")
	private Integer docReferralId;
	
	@Column(name="referraled_doc_id")
	private Integer referraledDocId;
	
	@Column(name="no_of_cases")
	private Integer noOfCases;
	
	@Column(name="share")
	private Double share;
	
	@Column(name="effective_date")
	private Date effectiveDate;
	
	@Column(name="org_unit_id")
	private Integer orgUnitId;
	
	@Column(name="org_id")
	private Integer orgId;
	
	@Column(name="status")
	private Character status;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;

	public Integer getDoctorReferalShareMapperId() {
		return doctorReferalShareMapperId;
	}

	public void setDoctorReferalShareMapperId(Integer doctorReferalShareMapperId) {
		this.doctorReferalShareMapperId = doctorReferalShareMapperId;
	}

	public Integer getReferralShareId() {
		return referralShareId;
	}

	public void setReferralShareId(Integer referralShareId) {
		this.referralShareId = referralShareId;
	}

	public Integer getDocReferralId() {
		return docReferralId;
	}

	public void setDocReferralId(Integer docReferralId) {
		this.docReferralId = docReferralId;
	}

	public Integer getReferraledDocId() {
		return referraledDocId;
	}

	public void setReferraledDocId(Integer referraledDocId) {
		this.referraledDocId = referraledDocId;
	}

	public Integer getNoOfCases() {
		return noOfCases;
	}

	public void setNoOfCases(Integer noOfCases) {
		this.noOfCases = noOfCases;
	}

	public Double getShare() {
		return share;
	}

	public void setShare(Double share) {
		this.share = share;
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

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}	

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	
}
