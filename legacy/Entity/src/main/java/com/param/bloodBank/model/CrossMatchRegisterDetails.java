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

@NamedQueries({

	@NamedQuery(name="SEARCH_CROSS_MATCH_REGISTERED_DETAILS_ON_RESERVATION",
	query="SELECT mstr.crossMatchRegisterId as crossMatchRegisterId, "
				//+ "mstr.requisitionNo  as  requisitionNo, "
				+ "bloodComponentOrderMaster.requisitionNumber  as  requisitionNumber, "
				+ "crossMatchRegisterDetails.crossMatchRegisterDetailsId as crossMatchRegisterDetailsId, "
				+ "patientRequisitionDetails.patientRequisitionId as patientRequisitionId, "
				+ "mstr.patientBloodGroup  as  patientBloodGroup, "
				+ "crossMatchRegisterDetails.bloodBagNo  as  bloodBagNo, "
				+ "crossMatchRegisterDetails.bloodBankStockId  as  bloodBankStockId, "
				+ "mstr.componentId  as componentId, "
				+ "mstr.patientId  as  patientId, "
				+ "mstr.unitId  as  unitId, "
				+ "mstr.requiredQty as requiredQty, "
				+ "mstr.organizationId  as  organizationId, "
				+ "mstr.createdBy  as createdBy, "
				+ " to_char(mstr.createdDate,'DD-MM-YYYY') as createdDate, "
				+ " to_char(mstr.issueDate,'DD-MM-YYYY') as issueDate, "
				+ " to_char(mstr.requisitionDate,'DD-MM-YYYY') as requisitionDate, "
				+ " patient.uhidNumber AS uhidNumber, "
				+ " bloodGroupMaster.bloodGroupDesc AS bloodGroupName, "
				+ " visitTypeMaster.visitTypeName AS visitTypeName, "
				+ " bloodComponentMaster.bloodComponentDesc AS bloodComponentDesc, "
				+ " concat(coalesce(patient.firstName,'') ,' ', coalesce(patient.middleName,'') ,' ', coalesce(patient.lastName,'')) AS patientName "
				+ " FROM CrossMatchRegisterDetails crossMatchRegisterDetails "
				+ " INNER JOIN crossMatchRegisterDetails.crossMatchRegister  mstr "
				+ " inner JOIN  mstr.bloodComponentOrderMaster  bloodComponentOrderMaster "
				+ " INNER JOIN mstr.patientRequisitionDetails  patientRequisitionDetails "
				+ " inner JOIN  patientRequisitionDetails.patientRegistration patient "
				+ " inner JOIN  patientRequisitionDetails.admission admission "
				+ " inner JOIN  admission.visitTypeMaster visitTypeMaster  "
				+ " inner JOIN  patientRequisitionDetails.bloodComponentMaster  bloodComponentMaster "
				+ " inner JOIN  patientRequisitionDetails.bloodGroupMaster bloodGroupMaster "
				+ " LEFT JOIN patientRequisitionDetails.bloodBankStatusMaster  bloodBankStatusMaster "
				+ " WHERE mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND patientRequisitionDetails.requisitionTypeId = 11"
				+ " AND crossMatchRegisterDetails.isExported = false"),
	
	
	@NamedQuery(name="GET_CROSS_MATCH_REGISTER_DETAILS_BY_CROSS_MATCH_ID",
	query="SELECT crossMatchRegisterDetails.crossMatchRegisterId as crossMatchRegisterId, "
				+ "crossMatchRegisterDetails.crossMatchRegisterDetailsId as crossMatchRegisterDetailsId, "
				+ "crossMatchRegisterDetails.bloodBagNo  as  bloodBagNo, "
				+ "crossMatchRegisterDetails.bloodBankStockId  as  bloodBankStockId, "
				+ "crossMatchRegisterDetails.segmentNo  as segmentNo, "
				+ "crossMatchRegisterDetails.salineMajor  as salineMajor, "
				+ "crossMatchRegisterDetails.salineMinor  as salineMinor, "
				+ "crossMatchRegisterDetails.albuminMajor  as albuminMajor, "
				+ "crossMatchRegisterDetails.albuminMinor  as albuminMinor, "
				+ "crossMatchRegisterDetails.coombsMajor  as coombsMajor, "
				+ "crossMatchRegisterDetails.coombsMinor  as coombsMinor, "
				+ "crossMatchRegisterDetails.crossMatchReportResult  as crossMatchReportResult, "
				+ "crossMatchRegisterDetails.componentId  as componentId, "
				+ "crossMatchRegisterDetails.bloodGroupId  as bloodGroupId, "
				+" crossMatchRegisterDetails.crossMatchRegisterDetailsStatus as crossMatchRegisterDetailsStatus, "
				+ " bloodGroupMaster.bloodGroupDesc AS bloodGroupName, "
				+ " bloodComponentMaster.bloodComponentDesc AS bloodComponentDesc "
				+ " FROM CrossMatchRegisterDetails crossMatchRegisterDetails "
				+ " INNER JOIN crossMatchRegisterDetails.crossMatchRegister  mstr "
				+ " inner JOIN  crossMatchRegisterDetails.bloodComponentMaster  bloodComponentMaster "
				+ " inner JOIN  crossMatchRegisterDetails.bloodGroupMaster bloodGroupMaster "
				+ " WHERE crossMatchRegisterDetails.organizationId= :organizationId "
				+ " AND crossMatchRegisterDetails.unitId= :unitId "
				+ " AND crossMatchRegisterDetails.crossMatchRegisterId= :crossMatchRegisterId "
				+ " AND crossMatchRegisterDetails.crossMatchRegisterDetailsStatus= 1 "
				+ " AND crossMatchRegisterDetails.isExported= false ")
	
})


@Entity
@Table(name="t_cross_match_register_details", schema="blood_bank")
@SequenceGenerator(name="cross_match_register_details_seq", sequenceName="blood_bank.cross_match_register_details_seq", allocationSize= 1)

public class CrossMatchRegisterDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cross_match_register_details_seq")

	@Column(name = "cross_match_register_details_id")
	private Integer crossMatchRegisterDetailsId;
	
	@Column(name = "cross_match_register_id")
	private Integer crossMatchRegisterId;
	
	@Column(name = "component_id")
	private Integer componentId;
	
	@Column(name = "blood_group_id")
	private Integer bloodGroupId;
	
	@Column(name = "segment_no")
	private String segmentNo;
	
	@Column(name = "saline_major")
	private Integer salineMajor;
	
	@Column(name = "saline_minor")
	private Integer salineMinor;
	
	@Column(name = "albumin_major")
	private Integer albuminMajor;
	
	@Column(name = "albumin_minor")
	private Integer albuminMinor;
	
	@Column(name = "coombs_major")
	private Integer coombsMajor;
	
	@Column(name = "coombs_minor")
	private Integer coombsMinor;
	
	@Column(name = "cross_match_report_result")
	private String crossMatchReportResult;
	
	@Column(name = "cross_match_register_details_status")
	private Integer crossMatchRegisterDetailsStatus;
	
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
	
	@Column(name = "blood_bag_no")
	private String bloodBagNo;

	@Column(name = "blood_bank_stock_id")
	private Integer bloodBankStockId;
	
	@Column(name = "is_exported")
	private boolean isExported;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cross_match_register_id", insertable = false, updatable = false)
	private CrossMatchRegister crossMatchRegister ;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="component_id", insertable=false, updatable=false)
	private BloodComponentMaster bloodComponentMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blood_group_id", insertable = false, updatable = false)
	private BloodGroupMaster bloodGroupMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blood_bank_stock_id", insertable = false, updatable = false)
	private BloodBankStock bloodBankStock;
	
	
	public Integer getCrossMatchRegisterDetailsId() {
		return crossMatchRegisterDetailsId;
	}

	public void setCrossMatchRegisterDetailsId(Integer crossMatchRegisterDetailsId) {
		this.crossMatchRegisterDetailsId = crossMatchRegisterDetailsId;
	}

	public Integer getCrossMatchRegisterId() {
		return crossMatchRegisterId;
	}

	public void setCrossMatchRegisterId(Integer crossMatchRegisterId) {
		this.crossMatchRegisterId = crossMatchRegisterId;
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

	public String getSegmentNo() {
		return segmentNo;
	}

	public void setSegmentNo(String segmentNo) {
		this.segmentNo = segmentNo;
	}

	public Integer getSalineMajor() {
		return salineMajor;
	}

	public void setSalineMajor(Integer salineMajor) {
		this.salineMajor = salineMajor;
	}

	public Integer getSalineMinor() {
		return salineMinor;
	}

	public void setSalineMinor(Integer salineMinor) {
		this.salineMinor = salineMinor;
	}

	public Integer getAlbuminMajor() {
		return albuminMajor;
	}

	public void setAlbuminMajor(Integer albuminMajor) {
		this.albuminMajor = albuminMajor;
	}

	public Integer getAlbuminMinor() {
		return albuminMinor;
	}

	public void setAlbuminMinor(Integer albuminMinor) {
		this.albuminMinor = albuminMinor;
	}

	public Integer getCoombsMajor() {
		return coombsMajor;
	}

	public void setCoombsMajor(Integer coombsMajor) {
		this.coombsMajor = coombsMajor;
	}

	public Integer getCoombsMinor() {
		return coombsMinor;
	}

	public void setCoombsMinor(Integer coombsMinor) {
		this.coombsMinor = coombsMinor;
	}

	public String getCrossMatchReportResult() {
		return crossMatchReportResult;
	}

	public void setCrossMatchReportResult(String crossMatchReportResult) {
		this.crossMatchReportResult = crossMatchReportResult;
	}

	public Integer getCrossMatchRegisterDetailsStatus() {
		return crossMatchRegisterDetailsStatus;
	}

	public void setCrossMatchRegisterDetailsStatus(Integer crossMatchRegisterDetailsStatus) {
		this.crossMatchRegisterDetailsStatus = crossMatchRegisterDetailsStatus;
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

	public String getBloodBagNo() {
		return bloodBagNo;
	}

	public void setBloodBagNo(String bloodBagNo) {
		this.bloodBagNo = bloodBagNo;
	}

	public Integer getBloodBankStockId() {
		return bloodBankStockId;
	}

	public void setBloodBankStockId(Integer bloodBankStockId) {
		this.bloodBankStockId = bloodBankStockId;
	}

	public boolean isExported() {
		return isExported;
	}

	public void setExported(boolean isExported) {
		this.isExported = isExported;
	}

	public CrossMatchRegister getCrossMatchRegister() {
		return crossMatchRegister;
	}

	public void setCrossMatchRegister(CrossMatchRegister crossMatchRegister) {
		this.crossMatchRegister = crossMatchRegister;
	}

	public BloodComponentMaster getBloodComponentMaster() {
		return bloodComponentMaster;
	}

	public void setBloodComponentMaster(BloodComponentMaster bloodComponentMaster) {
		this.bloodComponentMaster = bloodComponentMaster;
	}

	public BloodGroupMaster getBloodGroupMaster() {
		return bloodGroupMaster;
	}

	public void setBloodGroupMaster(BloodGroupMaster bloodGroupMaster) {
		this.bloodGroupMaster = bloodGroupMaster;
	}

	
	
}
