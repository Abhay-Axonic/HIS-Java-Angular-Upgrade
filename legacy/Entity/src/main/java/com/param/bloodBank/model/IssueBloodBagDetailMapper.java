package com.param.bloodBank.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.entity.model.patient.PatientRegistration;

@NamedQueries({
	
	@NamedQuery(name="SEARCH_ISSUED_BLOOD_BAG_ON_ISSUE_ACCEPTANCE",
	query="SELECT distinct "
				+ " mstr.issueBloodBagDetailsId AS issueBloodBagDetailsId, "
				+ " mstr.bagNo AS bloodBagNo,"
				+ " mstr.status as status, "
				+ " issueBloodBagMapper.bloodBagIssueNo AS issueNo, "
				+ " to_char(issueBloodBagMapper.issueDate,'DD/MM/YYYY' ) AS issueDate, "
				+ " patient.uhidNumber AS uhid, "
				+ " patient.patientId AS patientId, "
				+ " bloodGroupMaster.bloodGroupId AS bloodGroupId, "
				+ " bloodGroupMaster.bloodGroupDesc AS bloodGroupName, "
				+ " to_char(bloodBankStock.expiryDate,'DD/MM/YYYY' ) AS expiryDate, "
				+ " bloodBagTypeMaster.bloodBagTypeId AS bloodBagTypeId, "
				+ " bloodBagTypeMaster.bloodBagTypeDesc AS bloodBagTypeDesc, "
				+ " bloodBagCapacityMaster.bloodBagCapacityId AS bloodBagCapacityId, "
				+ " bloodBagCapacityMaster.bloodBagCapacityDesc AS bloodBagCapacityDesc, "
				+ " bloodComponentOrderMaster.requisitionNumber As requisitionNumber, "  
				+ " bloodComponentMaster.bloodComponentId AS bloodBagComponenId, "
				+ " bloodComponentMaster.bloodComponentDesc AS bloodComponentDesc, "
				+ " transfusionDetailMaster.saveAsDraftFlag AS saveAsDraftFlag, "
				+ " concat(coalesce(patient.firstName,'') ,' ', coalesce(patient.middleName,'') ,' ', coalesce(patient.lastName,'')) AS patientName ,"
				+ " patient.imagePath as imagePath, prefix.prefixCode as prefixCode  ,gender.code as genderCode ,"
				+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, patient.dob)),'M ',EXTRACT(day from AGE(CURRENT_TIMESTAMP, patient.dob)),'D ') as ageString "
				+ " FROM IssueBloodBagDetailMapper mstr "
				+ " INNER JOIN  mstr.issueBloodBagMapper  issueBloodBagMapper "
				+ " inner JOIN  issueBloodBagMapper.patientRegistration patient "
				+ " inner JOIN  issueBloodBagMapper.patientRegistration.prefixMaster prefix  "
				+ " inner JOIN  issueBloodBagMapper.patientRegistration.genderMaster gender  "
				+ " inner JOIN  issueBloodBagMapper.crossMatchRegister crossMatchRegister "
				+ " inner JOIN  crossMatchRegister.bloodComponentOrderMaster  bloodComponentOrderMaster "
				+ " inner JOIN  mstr.crossMatchRegisterDetails crossMatchRegisterDetails "
				+ " inner JOIN  crossMatchRegisterDetails.bloodBankStock bloodBankStock "
				+ " inner JOIN  bloodBankStock.bloodBagTypeMaster bloodBagTypeMaster "
				+ " inner JOIN  bloodBankStock.bloodBagCapacityMaster bloodBagCapacityMaster "
				+ " inner JOIN  mstr.bloodComponentMaster  bloodComponentMaster "
				+ " inner JOIN  mstr.bloodGroupMaster bloodGroupMaster "
				+ " left outer join mstr.bloodBagIssueAcceptance bloodBagIssueAcceptance"
				+ " left join mstr.transfusionDetailMaster transfusionDetailMaster"
				+ " WHERE mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.issueBloodBagStatusId in (14,15)  "
				+ " AND to_char(mstr.createdDate,'YYYY-MM-DD') BETWEEN :fromDate AND :toDate"), //for issued bags
	
})

@Entity
@Table(name = "t_issue_blood_bag_details", schema = "blood_bank")
@SequenceGenerator(name = "issue_blood_bag_details_seq", sequenceName = "blood_bank.issue_blood_bag_details_seq", allocationSize = 1)
public class IssueBloodBagDetailMapper {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "issue_blood_bag_details_seq")
	@Column(name = "issue_blood_bag_details_id")
	private Integer issueBloodBagDetailsId;
	
	@Column(name = "issue_blood_bag_id")
	private Integer issueBloodBagId;
	
	@Column(name = "cross_match_register_details_id")
	private Integer crossMatchRegisterDetailsId ;
	
	@Column(name = "component_id")
	private Integer componentId;

	@Column(name = "blood_group_id")
	private Integer bloodGroupId;
	
	@Column(name = "bag_no")
	private String bagNo;
	
	@Column(name = "segment_no")
	private String segmentNo;
	
	@Column(name = "issue_blood_bag_status_id")
	private Integer issueBloodBagStatusId;
	
	@Column(name = "cross_match_report_result_status")
	private String crossMatchReportResultStatus ;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "is_expoted ")
	private boolean isExpoted ;
	
	@Column(name = "reason_id")
	private Integer reasonId;
	
	@Column(name = "note")
	private String note;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issue_blood_bag_id", insertable = false, updatable = false)
	private IssueBloodBagMapper issueBloodBagMapper;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blood_group_id", insertable = false, updatable = false)
	private BloodGroupMaster bloodGroupMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "component_id", insertable = false, updatable = false)
	private BloodComponentMaster bloodComponentMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", insertable = false, updatable = false)
	private PatientRegistration patientRegistration;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cross_match_register_details_id", insertable = false, updatable = false)
	private CrossMatchRegisterDetails crossMatchRegisterDetails;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "component_id",insertable = false, updatable = false)
	private BloodBagIssueAcceptance bloodBagIssueAcceptance;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "transfusion_detail_id",insertable = false, updatable = false)
	private TransfusionDetailMaster transfusionDetailMaster;

	public Integer getIssueBloodBagDetailsId() {
		return issueBloodBagDetailsId;
	}

	public void setIssueBloodBagDetailsId(Integer issueBloodBagDetailsId) {
		this.issueBloodBagDetailsId = issueBloodBagDetailsId;
	}

	public Integer getIssueBloodBagId() {
		return issueBloodBagId;
	}

	public void setIssueBloodBagId(Integer issueBloodBagId) {
		this.issueBloodBagId = issueBloodBagId;
	}

	public Integer getComponentId() {
		return componentId;
	}

	public void setComponentId(Integer componentId) {
		this.componentId = componentId;
	}

	public Integer getBloodGroupId() {
		return bloodGroupId;
	}

	public void setBloodGroupId(Integer bloodGroupId) {
		this.bloodGroupId = bloodGroupId;
	}

	public String getBagNo() {
		return bagNo;
	}

	public void setBagNo(String bagNo) {
		this.bagNo = bagNo;
	}

	public String getSegmentNo() {
		return segmentNo;
	}

	public void setSegmentNo(String segmentNo) {
		this.segmentNo = segmentNo;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getCrossMatchRegisterDetailsId() {
		return crossMatchRegisterDetailsId;
	}

	public void setCrossMatchRegisterDetailsId(Integer crossMatchRegisterDetailsId) {
		this.crossMatchRegisterDetailsId = crossMatchRegisterDetailsId;
	}

	public String getCrossMatchReportResultStatus() {
		return crossMatchReportResultStatus;
	}

	public void setCrossMatchReportResultStatus(String crossMatchReportResultStatus) {
		this.crossMatchReportResultStatus = crossMatchReportResultStatus;
	}

	public boolean isExpoted() {
		return isExpoted;
	}

	public void setExpoted(boolean isExpoted) {
		this.isExpoted = isExpoted;
	}

	public Integer getIssueBloodBagStatusId() {
		return issueBloodBagStatusId;
	}

	public void setIssueBloodBagStatusId(Integer issueBloodBagStatusId) {
		this.issueBloodBagStatusId = issueBloodBagStatusId;
	}

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public BloodBagIssueAcceptance getBloodBagIssueAcceptance() {
		return bloodBagIssueAcceptance;
	}

	public void setBloodBagIssueAcceptance(BloodBagIssueAcceptance bloodBagIssueAcceptance) {
		this.bloodBagIssueAcceptance = bloodBagIssueAcceptance;
	}
	
	
}
