package com.param.billing.global.transaction.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedNativeQueries({
	@NamedNativeQuery(name="GET_REFERAL_COUNT_DETAILS",
			query="select sum(mppr.no_of_referral_count) as \"noOfReferralCount\" "
			 +" from  billing.t_doctor_referal_share_count_mapper mppr "
			 +" where referraled_doc_id =:referraledDocId and is_read='A'"),
	
	@NamedNativeQuery(name="GET_ALL_REFERRAL_COUNT_DETAILS",
	query="select to_char(mppr.created_date,'DD-MM-YYYY') as \"createdDate\","
			+" mppr.referraled_doc_id as \"referraledDocId\","
			+" mppr.no_of_referral_count as \"noOfReferralCount\","
			+" mppr.org_id as \"orgId\", "
			+" mppr.org_unit_id as \"unitId\" "	
			+" FROM billing.t_doctor_referal_share_count_mapper mppr "
			+" WHERE mppr.referraled_doc_id=:referralDocId"
			+ " AND mppr.visit_type_id=:visitTypeId"
			+ " AND mppr.org_id=:orgId"
			+ " AND mppr.org_unit_id=:unitId"
			+ " AND is_read='A' and status='A' "
			+" ORDER BY doctor_referal_share_count_mapper_id asc")
})

@Entity
@Table(name="t_doctor_referal_share_count_mapper",schema="billing")
@SequenceGenerator(name="t_doctor_referral_share_count_mapper_seq",sequenceName="billing.t_doctor_referral_share_count_mapper_seq",allocationSize=1)
public class DoctorReferalShareCountMapper {
@Id
@Column(name="doctor_referal_share_count_mapper_id")
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_doctor_referral_share_count_mapper_seq")
private Integer doctorReferalShareCountMapperId;

@Column(name="doc_referral_id")
private Integer docReferralId;

@Column(name="referraled_doc_id")
private Integer referraledDocId;

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

@Column(name="no_of_referral_count")
private Integer noOfReferralCount;

@Column(name="is_read")
private Character isRead;

@Column(name="visit_type_id")
private Integer visitTypeId;

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

public Integer getDoctorReferalShareCountMapperId() {
	return doctorReferalShareCountMapperId;
}

public void setDoctorReferalShareCountMapperId(Integer doctorReferalShareCountMapperId) {
	this.doctorReferalShareCountMapperId = doctorReferalShareCountMapperId;
}

public Integer getNoOfReferralCount() {
	return noOfReferralCount;
}

public void setNoOfReferralCount(Integer noOfReferralCount) {
	this.noOfReferralCount = noOfReferralCount;
}

public Character getIsRead() {
	return isRead;
}

public void setIsRead(Character isRead) {
	this.isRead = isRead;
}

public Integer getVisitTypeId() {
	return visitTypeId;
}

public void setVisitTypeId(Integer visitTypeId) {
	this.visitTypeId = visitTypeId;
}

}
