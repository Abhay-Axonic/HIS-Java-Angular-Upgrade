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
	@NamedNativeQuery(name = "GET_OT_CSSD_KIT_BY_PRO_REQ_ID", query =
	" SELECT  " + 
	" ot_completion_cssd_kit_id  as \"otCompletionCssdKitId\", "  + 
	" kit_transition_id as \"kitTransitionNo\", "    + 
	" pro_req_id as \"proReqId\", "
	+ " kit_id as \"kitId\", "    +
	" batch_id as \"batchId\", "
	+ "kit_name as \"kitName\",  "    +
	" kit_code as \"kitCode\",   "    +
	" to_char(created_date, 'DD-MM-YYYY HH24:MI:SS') as \"createdDate\",   "    +
	" created_by as \"createdBy\", "    + 
	" status as \"status\", "    +
	" updated_by as \"updatedBy\", "    + 
	" to_char(updated_date, 'DD-MM-YYYY HH24:MI:SS') as \"updatedDate\", "    + 
	" unit_id as \"unitId\","
	+ "store_id as \"storeId\", "    +
	" organization_id  as \"organizationId\" "    +
	" FROM otims.t_ot_completion_cssd_kit where pro_req_id =:id") })

@Entity
@Table(name = "t_ot_completion_cssd_kit" , schema = "otims")
@SequenceGenerator(name = "ot_completion_cssd_kit_seq" , sequenceName = "otims.ot_completion_cssd_kit_seq" , allocationSize = 1)
public class OTCompletionCssdKit {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "ot_completion_cssd_kit_seq")
	@Column(name = "ot_completion_cssd_kit_id")
	private Integer otCompletionCssdKitId;
	
	@Column(name = "kit_transition_id")
	private Integer kitTransitionNo;
	
	@Column(name = "pro_req_id")
	private Integer proReqId;
	
	@Column(name = "kit_id")
	private Integer kitId;
	
	@Column(name = "batch_id")
	private Integer batchId;
	
	@Column(name = "kit_name")
	private String kitName;
	
	@Column(name = "kit_code")
	private String kitCode;
	
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
	
	@Column(name = "store_id")
	private Integer storeId;

	public Integer getOtCompletionCssdKitId() {
		return otCompletionCssdKitId;
	}

	public void setOtCompletionCssdKitId(Integer otCompletionCssdKitId) {
		this.otCompletionCssdKitId = otCompletionCssdKitId;
	}

	public Integer getProReqId() {
		return proReqId;
	}

	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}

	public Integer getKitId() {
		return kitId;
	}

	public void setKitId(Integer kitId) {
		this.kitId = kitId;
	}

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}

	public String getKitName() {
		return kitName;
	}

	public void setKitName(String kitName) {
		this.kitName = kitName;
	}

	public String getKitCode() {
		return kitCode;
	}

	public void setKitCode(String kitCode) {
		this.kitCode = kitCode;
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

	public Integer getKitTransitionNo() {
		return kitTransitionNo;
	}

	public void setKitTransitionNo(Integer kitTransitionNo) {
		this.kitTransitionNo = kitTransitionNo;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

}
