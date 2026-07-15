package com.param.global.model;

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
	
	@NamedNativeQuery(name = "GET_INTRA_OPERATIVE_CHECKLIST_BY_PRO_REQ_ID",
			query = " select mcm.description  as \"description\" , mcm.is_who as \"isWho\" ," + 
					" mcm.checklist_master_id  as \"checklistMasterId\" "+
					" from otims.t_procedure_order_checklist tpoc " + 
					" inner join  otims.m_checklist_master mcm  on tpoc.checklist_id  = mcm.checklist_master_id  " + 
			" where procedure_request_id = :proReqId and mcm.stage_id = 3  and tpoc.status = 'A'  and mcm.status = 'A' "),
	
	@NamedNativeQuery(name = "GET_PROC_ORDER_REQ_ID",
	query = " select tpom.procedure_order_id as \"procedureOrderId\" "
			+ " from otims.t_procedure_order_master tpom " + 
			" where tpom.pro_req_id =:proReqId and tpom.status= 'A' and is_primary_secondary = 'Y' "),
})
@Entity
@Table(name = "t_ot_checklist_details", schema = "otims")
@SequenceGenerator(name = "ot_checklist_seq", sequenceName = "otims.ot_checklist_seq", allocationSize = 1)
public class OTChecklistDetails {

	@Id
	@Column(name = "ot_checklist_details_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ot_checklist_seq")
	private Integer otChecklistDetailsId ;

	@Column(name = "pro_req_id")
	private Integer proReqId;

	@Column(name = "ot_checklist_name")
	private String description;

	@Column(name = "checklist_id")
	private Integer checklistId;
	
	@Column(name = "checklist_master_id")
	private Integer checklistMasterId;
	
	@Column(name = "checklist_status_id")
	private Integer checklistStatusId;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "stage_id")
	private Integer stageId;
	
	@Column(name = "status")
	private Character status;

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

	public Integer getOtChecklistDetailsId() {
		return otChecklistDetailsId;
	}

	public void setOtChecklistDetailsId(Integer otChecklistDetailsId) {
		this.otChecklistDetailsId = otChecklistDetailsId;
	}

	public Integer getProReqId() {
		return proReqId;
	}

	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}

	public Integer getChecklistId() {
		return checklistId;
	}

	public void setChecklistId(Integer checklistId) {
		this.checklistId = checklistId;
	}

	public Integer getChecklistMasterId() {
		return checklistMasterId;
	}

	public void setChecklistMasterId(Integer checklistMasterId) {
		this.checklistMasterId = checklistMasterId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStageId() {
		return stageId;
	}

	public void setStageId(Integer stageId) {
		this.stageId = stageId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getChecklistStatusId() {
		return checklistStatusId;
	}

	public void setChecklistStatusId(Integer checklistStatusId) {
		this.checklistStatusId = checklistStatusId;
	}
	
	
	
}
