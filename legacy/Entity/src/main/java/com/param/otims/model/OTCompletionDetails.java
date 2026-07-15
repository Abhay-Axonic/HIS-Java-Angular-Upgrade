package com.param.otims.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedNativeQueries({
	@NamedNativeQuery(name = "GET_OT_COMPLETION_DETAILS_PRO_REQ_ID", query =
	"SELECT " + 
	"ot_completion_details_id as \"otCompletionDetailsId\", " + 
	"billing_bed_category_id as \"billingBedCategoryId\", " + 
	"pro_req_id as \"proReqId\", " + 
	"ot_table_id as \"otTableId\", " + 
	"anesthesia_type_id as \"anesthesiaTypeId\", " + 
	"total_hrs as \"duration\", " + 
	"encounter_id as \"encounterId\", " + 
	"encounter_number as \"encounterNumber\", " + 
	"ot_sheet_no as \"otSheetNo\", " + 
	"remark as \"remark\", " + 
	"emergancy as \"emergancy\", " + 
	"to_char(ot_date, 'DD-MM-YYYY HH24:MI:SS')as \"otDate\", " + 
	"to_char(created_date, 'DD-MM-YYYY HH24:MI:SS') as \"createdDate\", " + 
	"created_by as \"createdBy\", " + 
	"status as \"status\", " + 
	"updated_by as \"updatedBy\", " + 
	"to_char(updated_date, 'DD-MM-YYYY HH24:MI:SS') as \"updatedDate\", " + 
	"unit_id as \"unitId\", "
	+ "ot_emergancy_flow as \"otEmergancyFlow\"," + 
	"organization_id as \"organizationId\" " + 
	"FROM otims.t_ot_completion_details where pro_req_id =:id") })

@Entity
@Table(name = "t_ot_completion_details" , schema = "otims")
@SequenceGenerator(name = "ot_completion_details_seq" , sequenceName = "otims.ot_completion_details_seq" , allocationSize = 1)
public class OTCompletionDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "ot_completion_details_seq")
	@Column(name = "ot_completion_details_id")
	private Integer otCompletionDetailsId;
	
	@Column(name = "pro_req_id")
	private Integer proReqId;
	
	@Column(name = "billing_bed_category_id")
	private Integer billingBedCategoryId;

	@Column(name = "ot_table_id")
	private Integer otTableId;
	
	@Column(name = "total_hrs")
	private Integer totalHrs;
	
	@Column(name = "anesthesia_type_id")
	private Integer anesthesiaTypeId;
	
	@Column(name = "ot_Date")
	private Date otDate;
	
	@Column(name = "ot_sheet_no")
	private String otSheetNo;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "emergancy")
	private boolean emergancy;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "encounter_id")
	private Integer encounterId;
	
	@Column(name = "encounter_number")
	private String encounterNumber;
	
	@Column(name = "ot_emergancy_flow")
	private boolean otEmergancyFlow;

	public Integer getOtCompletionDetailsId() {
		return otCompletionDetailsId;
	}

	public void setOtCompletionDetailsId(Integer otCompletionDetailsId) {
		this.otCompletionDetailsId = otCompletionDetailsId;
	}

	public Integer getProReqId() {
		return proReqId;
	}

	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

	public Integer getOtTableId() {
		return otTableId;
	}

	public void setOtTableId(Integer otTableId) {
		this.otTableId = otTableId;
	}

	public Integer getTotalHrs() {
		return totalHrs;
	}

	public void setTotalHrs(Integer totalHrs) {
		this.totalHrs = totalHrs;
	}

	public Integer getAnesthesiaTypeId() {
		return anesthesiaTypeId;
	}

	public void setAnesthesiaTypeId(Integer anesthesiaTypeId) {
		this.anesthesiaTypeId = anesthesiaTypeId;
	}

	public Date getOtDate() {
		return otDate;
	}

	public void setOtDate(Date otDate) {
		this.otDate = otDate;
	}

	public String getOtSheetNo() {
		return otSheetNo;
	}

	public void setOtSheetNo(String otSheetNo) {
		this.otSheetNo = otSheetNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isEmergancy() {
		return emergancy;
	}

	public void setEmergancy(boolean emergancy) {
		this.emergancy = emergancy;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public String getEncounterNumber() {
		return encounterNumber;
	}

	public void setEncounterNumber(String encounterNumber) {
		this.encounterNumber = encounterNumber;
	}

	public boolean isOtEmergancyFlow() {
		return otEmergancyFlow;
	}

	public void setOtEmergancyFlow(boolean otEmergancyFlow) {
		this.otEmergancyFlow = otEmergancyFlow;
	}
	
	
	
}
