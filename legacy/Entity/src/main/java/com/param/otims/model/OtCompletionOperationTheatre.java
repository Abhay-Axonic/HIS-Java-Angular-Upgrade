package com.param.otims.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_ot_completion_operation_theatre" , schema = "otims")
@SequenceGenerator(name = "t_ot_completion_operation_theatre_seq" , sequenceName = "otims.t_ot_completion_operation_theatre_seq" , allocationSize = 1)
public class OtCompletionOperationTheatre {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "t_ot_completion_operation_theatre_seq")
	@Column(name = "ot_completion_operation_theatre_id")
	private int otCompletionOperationTheatreId;
	
	@Column(name = "operation_theatre_id")
	private Integer operationTheatreId;
	
	@Column(name = "operation_table_id")
	private Integer operationTableId;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "org_id")
	private Integer orgId;
	
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
	
	@Column(name = "pro_req_id")
	private Integer proReqId;

	public Integer getProReqId() {
		return proReqId;
	}

	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}

	public int getOtCompletionOperationTheatreId() {
		return otCompletionOperationTheatreId;
	}

	public void setOtCompletionOperationTheatreId(int otCompletionOperationTheatreId) {
		this.otCompletionOperationTheatreId = otCompletionOperationTheatreId;
	}

	public Integer getOperationTheatreId() {
		return operationTheatreId;
	}

	public void setOperationTheatreId(Integer operationTheatreId) {
		this.operationTheatreId = operationTheatreId;
	}

	public Integer getOperationTableId() {
		return operationTableId;
	}

	public void setOperationTableId(Integer operationTableId) {
		this.operationTableId = operationTableId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status != '\u0000' ? status : 'I');
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
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
}
