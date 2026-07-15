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
	
	@NamedQuery(name="CHECK_FOR_UNIQUE_PRECHECK_MASTER",
		query="SELECT mstr.transfusionPrecheckMasterId as transfusionPrecheckMasterId "
					+ " FROM BloodTransfusionPrecheckMaster mstr"
					+ " WHERE  mstr.organizationId= :organizationId"
					+ " AND mstr.unitId= :unitId"
					+ " AND lower(mstr.code)= :code "
					+ " AND lower(mstr.description)= :description"
					),
	
	@NamedQuery(name="GET_PRECHECK_MASTER_LIST",
	query="SELECT mstr.transfusionPrecheckMasterId as transfusionPrecheckMasterId, "
				+ " mstr.code AS code, "
				+ " mstr.description AS description, "
				+ " mstr.status AS status "
				+ " FROM BloodTransfusionPrecheckMaster mstr "
				+ " WHERE  mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				),
	
	@NamedQuery(name="GET_PRECHECK_MASTER_ACTIVE_LIST",
	query="SELECT mstr.transfusionPrecheckMasterId as transfusionPrecheckMasterId, "
				+ " mstr.description AS description "
				+ " FROM BloodTransfusionPrecheckMaster mstr "
				+ " WHERE  mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.status= 'A' "
				),
})
@Entity
@Table(name="m_blood_transfusion_precheck_master", schema="blood_bank")
@SequenceGenerator(name="blood_transfusion_precheck_master_seq", sequenceName="blood_bank.blood_transfusion_precheck_master_seq", allocationSize= 1)
public class BloodTransfusionPrecheckMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blood_transfusion_precheck_master_seq")

	@Column(name = "transfusion_precheck_master_id ")
	private Integer transfusionPrecheckMasterId ;
	
	@Column(name = "code")
	private String code;
	
	@Column(name = "description")
	private String description;
	
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

	
	public Integer getTransfusionPrecheckMasterId() {
		return transfusionPrecheckMasterId;
	}

	public void setTransfusionPrecheckMasterId(Integer transfusionPrecheckMasterId) {
		this.transfusionPrecheckMasterId = transfusionPrecheckMasterId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
