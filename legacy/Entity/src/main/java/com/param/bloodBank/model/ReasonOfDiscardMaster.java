package com.param.bloodBank.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries({
	
	@NamedQuery(name="GET_REASON_OF_DISCARD_MASTER_LIST",
		query=" SELECT reasonOfDiscardMaster.reasonOfDiscardId AS reasonOfDiscardId, reasonOfDiscardMaster.reasonOfDiscardCode AS reasonOfDiscardCode,"
				+ " reasonOfDiscardMaster.reasonOfDiscardDesc AS reasonOfDiscardDesc,  reasonOfDiscardMaster.organizationId AS organizationId,"
				+ " reasonOfDiscardMaster.unitId AS unitId, reasonOfDiscardMaster.status AS status "
				+ " FROM ReasonOfDiscardMaster reasonOfDiscardMaster"
				+ " WHERE reasonOfDiscardMaster.organizationId= :organizationId"
				+ " AND reasonOfDiscardMaster.unitId= :unitId"
				+ " ORDER BY reasonOfDiscardMaster.reasonOfDiscardId asc "
				),
	
	@NamedQuery(name="GET_REASON_OF_DISCARD_MASTER_BY_ID",
		query=" SELECT reasonOfDiscardMaster.reasonOfDiscardId AS reasonOfDiscardId, reasonOfDiscardMaster.reasonOfDiscardCode AS reasonOfDiscardCode,"
				+ " reasonOfDiscardMaster.reasonOfDiscardDesc AS reasonOfDiscardDesc,  reasonOfDiscardMaster.organizationId AS organizationId,"
				+ " reasonOfDiscardMaster.unitId AS unitId, reasonOfDiscardMaster.status AS status "
				+ " FROM ReasonOfDiscardMaster reasonOfDiscardMaster"
				+ " WHERE reasonOfDiscardMaster.reasonOfDiscardId= :reasonOfDiscardId"
				+ " AND reasonOfDiscardMaster.organizationId= :organizationId"
				+ " AND reasonOfDiscardMaster.unitId= :unitId"
				),
	
	@NamedQuery(name="GET_ACTIVE_REASON_OF_DISCARD_MASTER",
		query=" SELECT reasonOfDiscardMaster.reasonOfDiscardId AS reasonOfDiscardId, reasonOfDiscardMaster.reasonOfDiscardCode AS reasonOfDiscardCode,"
				+ " reasonOfDiscardMaster.reasonOfDiscardDesc AS reasonOfDiscardDesc,  reasonOfDiscardMaster.organizationId AS organizationId,"
				+ " reasonOfDiscardMaster.unitId AS unitId, reasonOfDiscardMaster.status AS status "
				+ " FROM ReasonOfDiscardMaster reasonOfDiscardMaster"
				+ " WHERE reasonOfDiscardMaster.status= 'A'"
				+ " AND reasonOfDiscardMaster.organizationId= :organizationId"
				+ " AND reasonOfDiscardMaster.unitId= :unitId"
				+ " ORDER BY reasonOfDiscardMaster.reasonOfDiscardId asc "
				),
	
	@NamedQuery(name="GET_REASON_OF_DISCARD_MASTER_BY_NAME",
		query=" SELECT reasonOfDiscardMaster.reasonOfDiscardId AS reasonOfDiscardId "
				+ " FROM ReasonOfDiscardMaster reasonOfDiscardMaster"
				+ " WHERE LOWER(reasonOfDiscardMaster.reasonOfDiscardDesc)= :reasonOfDiscardDesc"
				),
	
	@NamedQuery(name="GET_REASON_OF_DISCARD_MASTER_BY_NAME_NOT_ID",
		query=" SELECT reasonOfDiscardMaster.reasonOfDiscardId AS reasonOfDiscardId "
				+ " FROM ReasonOfDiscardMaster reasonOfDiscardMaster"
				+ " WHERE reasonOfDiscardMaster.reasonOfDiscardId!= :reasonOfDiscardId"
				+ " AND LOWER(reasonOfDiscardMaster.reasonOfDiscardDesc)= :reasonOfDiscardDesc"
				),
	
	
		
})
@Entity
@Table(name = "m_reason_of_discard_master", schema = "blood_bank")
@SequenceGenerator(name = "seq_m_reason_of_discard_master", sequenceName = "blood_bank.seq_m_reason_of_discard_master", allocationSize = 1)
public class ReasonOfDiscardMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_reason_of_discard_master")
	
	@Column(name = "reason_of_discard_id")
	private Integer reasonOfDiscardId;
	
	@Column(name = "reason_of_discard_code")
	private String reasonOfDiscardCode;
	
	@Column(name = "reason_of_discard_desc")
	private String reasonOfDiscardDesc;
	
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

	public Integer getReasonOfDiscardId() {
		return reasonOfDiscardId;
	}

	public void setReasonOfDiscardId(Integer reasonOfDiscardId) {
		this.reasonOfDiscardId = reasonOfDiscardId;
	}

	public String getReasonOfDiscardCode() {
		return reasonOfDiscardCode;
	}

	public void setReasonOfDiscardCode(String reasonOfDiscardCode) {
		this.reasonOfDiscardCode = reasonOfDiscardCode;
	}

	public String getReasonOfDiscardDesc() {
		return reasonOfDiscardDesc;
	}

	public void setReasonOfDiscardDesc(String reasonOfDiscardDesc) {
		this.reasonOfDiscardDesc = reasonOfDiscardDesc;
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
	
	
}
