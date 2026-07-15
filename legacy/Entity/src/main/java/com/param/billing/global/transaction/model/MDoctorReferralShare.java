package com.param.billing.global.transaction.model;



import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.ReferralMaster;
import com.param.global.common.DateConverter;
@NamedNativeQueries({
	@NamedNativeQuery(name = "GET_DOCTOR_REFERRAL_SHARE_MASTER_LIST_BY_DOCTOR_REFERRAL_ID",
					 query 	="SELECT "
							 + "  docRefShare.share "
							 + " FROM   billing.m_doctor_referral_share docRefShare "
							 + " WHERE  docRefShare.doc_referral_id =:docRefShareId "
							 + " AND docRefShare.status = 'A' "
							 + " AND docRefShare.org_id =:orgId "
							 + " AND docRefShare.org_unit_id =:orgUnitId "),
	
	@NamedNativeQuery(name="GET_DOCTOR_REFERRAL_DETAILS",
	query=" select"
+" 	docReferral.referraled_doc_id as \"referraledDocId\","
+" 	docReferral.doc_referral_id as \"docReferralId\","
+" 	docReferral.visit_type_id as \"visitTypeId\","
+" 	docReferralMppr.no_of_cases as \"noOfCases\","
+" 	docReferralMppr.share as \"share\" ,"
+" 	docReferral.duration as \"duration\","
+" 	to_char( docReferral.effective_date,"
+" 	'DD-MM-YYYY' ) as \"effectiveDate\","
+" 	docReferral.org_id as \"orgId\","
+" 	docReferral.org_unit_id as \"unitId\""
+" FROM"
+" 	billing.m_doctor_referral_share docReferral"
+" INNER join billing.t_doctor_referral_share_mapper docReferralMppr ON docReferral.referral_share_id = docReferralMppr.referral_share_id"
+" WHERE"
+" 	docReferral.status = 'A'"
+" 	and docReferral.effective_date = "
+" ( select	max( docReferral.effective_date ) from billing.m_doctor_referral_share docReferral"
	+" inner join billing.t_doctor_referral_share_mapper docReferralMppr on"
	+" 	docReferral.referral_share_id = docReferralMppr.referral_share_id"
	+" where"
	+" 	docReferral.status = 'A'"
	+" 	and docReferral.effective_date < :currentDate) "),
	
	@NamedNativeQuery(name="GET_REFERRAL_DETAILS",
	query="select refDtls.referral_type_id as \"referralTypeId\", "
	+" refDtls.referral_id as \"referraledDocId\",  "
	+" refDtls.organization_id as \"orgId\",  "
	+" refDtls.unit_id as \"orgUnitId\","
	+ "refDtls.visit_type_id as \"visitTypeId\" "
	+" from public.t_referal_details refDtls "
	+"  WHERE to_char(refDtls.created_date,'DD-MM-YYYY')=:currentDate "
	+ " AND refDtls.referral_id=:referralDocId"
	+ " AND refDtls.visit_type_id=:visitTypeId"
	+ " AND refDtls.organization_id=:orgId"
	+ " AND refDtls.unit_id=:unitId"
	+ " AND refDtls.status='A'"),
	@NamedNativeQuery(name="GET_ENCOUNTER_AND_ADMISSION_IDS",
	query="select( case when dtls.encounter_id is not null then dtls.encounter_id else dtls.admission_id end) as \"encounterId\" "
	+"from  public.t_referal_details dtls "
	+"where dtls.referral_id=:referralDocId "
	+ "AND dtls.organization_id=:orgId "
	+ "AND dtls.unit_id=:unitId "
	+ "AND dtls.visit_type_id=:visitTypeId "
	+ "AND to_timestamp(to_char(dtls.created_date,'YYYY-MM-DD'),'YYYY-MM-DD') between :fromDate and :toDate"),
	@NamedNativeQuery(name="GET_DOCTOR_REFERRAL_SHARE_DETAILS",
	query=" select distinct(mDocRefShr.referraled_doc_id)  as \"docReferralId\"," 	 
		+" 	mRefMst.referral_name as \"referralName\"" 
		+" 	from billing.m_doctor_referral_share mDocRefShr" 
		+" 	inner join adt.m_referral_master mRefMst on mRefMst.referral_id=mDocRefShr.referraled_doc_id" 
		+" 	where mDocRefShr.status='A'"
		+ " AND mDocRefShr.org_id=:orgId "
//		+ "AND mDocRefShr.org_unit_id=:unitId"
		+" "),
	@NamedNativeQuery(name="GET_DOCTOR_REFERRAL_SHARE_DETAILS_BY_DOC_REF_ID",
	query="select distinct(docRefShr.referraled_doc_id) as \"referraledDocId\","
+"mRefMst.referral_name as \"referralName\", "
+ "docRefShr.duration as \"duration\", "
+"mppr.no_of_cases as \"noOfCases\","
+"mppr.share as \"share\","
+ "docRefShr.referral_share_id as \"referralShareId\","
+ "mppr.doctor_referal_share_mapper_id as \"doctorReferalShareMapperId\", "
+"to_char(mppr.effective_date,'DD-MM-YYYY') as \"effectiveDate\","
+ "mppr.status as \"status\","
+"vmstr.visit_type_name as \"visitTypeName\""
+"	from billing.t_doctor_referral_share_mapper mppr"
+"	inner join  billing.m_doctor_referral_share docRefShr on docRefShr.referral_share_id=mppr.referral_share_id"
+"	inner join public.m_visit_type_master vmstr on docRefShr.visit_type_id=vmstr.visit_type_id"
+"	inner join adt.m_referral_master mRefMst on mRefMst.referral_id=docRefShr.referraled_doc_id"
+"	where mppr.referraled_doc_id=:referralDocId "
+"	and mppr.status='A' and docRefShr.status='A'"
+"	and docRefShr.org_id=:orgId and docRefShr.org_unit_id=:unitId"),
	
	@NamedNativeQuery(name="CHECK_DOCTOR_REFERRAL_ALREADY_EXIST_OR_NOT",
		query="SELECT mDocRefShr.referral_share_id as \"referralShareId\" "
			+ "FROM billing.m_doctor_referral_share mDocRefShr "
			+ " WHERE mDocRefShr.referraled_doc_id=:referralDocId "
			+ " AND mDocRefShr.doc_referral_id=:referralId"
			+ " AND mDocRefShr.visit_type_id=:visitType"
			+ " AND mDocRefShr.duration=:duration"
			+ " AND to_char(mDocRefShr.effective_date,'YYYY-MM-DD')=:effectiveDate"
			+ " AND mDocRefShr.org_unit_id=:unitId"
			+ " AND mDocRefShr.org_id=:orgId")
	
	
})
	
@Entity
@Table(name="m_doctor_referral_share", schema="billing")
@SequenceGenerator(name = "m_seq_doctor_referral_share", sequenceName = "billing.m_seq_doctor_referral_share", allocationSize = 1)
public class MDoctorReferralShare {
	@Id
	@Column(name="referral_share_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_seq_doctor_referral_share")
	private Integer referralShareId;
	
	@Column(name = "doc_referral_id")
	private Integer docReferralId;
	
	@Column(name = "referraled_doc_id")
	private Integer referraledDocId;
	
	@Column(name = "visit_type_id")
	private Integer visitTypeId;
	
	@Column(name = "duration")
	private Integer duration;
	
	@Column(name = "no_of_cases")
	private Integer noOfCases;
	
	@Column(name = "share")
	private Integer share;
	
	@Column(name = "revenue")
	private double revenue;
	
	@Column(name = "effective_date")
	@Convert(converter=DateConverter.class)
	private Long effectiveDate;
	
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doc_referral_id", insertable = false, nullable = false, updatable = false)
	private DoctorReferralShareMaster doctorReferralShareMaster;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "referraled_doc_id", insertable = false, nullable = false, updatable = false)
	private ReferralMaster referralMaster;

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

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getNoOfCases() {
		return noOfCases;
	}

	public void setNoOfCases(Integer noOfCases) {
		this.noOfCases = noOfCases;
	}

	public Integer getShare() {
		return share;
	}

	public void setShare(Integer share) {
		this.share = share;
	}

	public double getRevenue() {
		return revenue;
	}

	public void setRevenue(double revenue) {
		this.revenue = revenue;
	}

	public Long getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Long effectiveDate) {
		this.effectiveDate = effectiveDate;
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

	public DoctorReferralShareMaster getDoctorReferralShareMaster() {
		return doctorReferralShareMaster;
	}

	public void setDoctorReferralShareMaster(DoctorReferralShareMaster doctorReferralShareMaster) {
		this.doctorReferralShareMaster = doctorReferralShareMaster;
	}

	public Integer getReferraledDocId() {
		return referraledDocId;
	}

	public void setReferraledDocId(Integer referraledDocId) {
		this.referraledDocId = referraledDocId;
	}

	public ReferralMaster getReferralMaster() {
		return referralMaster;
	}

	public void setReferralMaster(ReferralMaster referralMaster) {
		this.referralMaster = referralMaster;
	}
	
}
