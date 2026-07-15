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
	
	@NamedQuery(name="GET_BLOOD_GROUP_MASTER_LIST",
		query=" SELECT bloodGroupMaster.bloodGroupId AS bloodGroupId, bloodGroupMaster.bloodGroupCode AS bloodGroupCode,"
				+ " bloodGroupMaster.bloodGroupDesc AS bloodGroupDesc,  bloodGroupMaster.organizationId AS organizationId,"
				+ " bloodGroupMaster.unitId AS unitId, bloodGroupMaster.status AS status "
				+ " FROM BloodGroupMaster bloodGroupMaster"
				+ " WHERE bloodGroupMaster.organizationId= :organizationId"
				+ " AND bloodGroupMaster.unitId= :unitId"
				+ " ORDER BY bloodGroupMaster.bloodGroupId asc "
				),
	
	@NamedQuery(name="GET_BLOOD_GROUP_MASTER_BY_ID",
		query=" SELECT bloodGroupMaster.bloodGroupId AS bloodGroupId, bloodGroupMaster.bloodGroupCode AS bloodGroupCode,"
				+ " bloodGroupMaster.bloodGroupDesc AS bloodGroupDesc,  bloodGroupMaster.organizationId AS organizationId,"
				+ " bloodGroupMaster.unitId AS unitId, bloodGroupMaster.status AS status "
				+ " FROM BloodGroupMaster bloodGroupMaster"
				+ " WHERE bloodGroupMaster.bloodGroupId= :bloodGroupId"
				+ " AND bloodGroupMaster.organizationId= :organizationId"
				+ " AND bloodGroupMaster.unitId= :unitId"
				),
	
	@NamedQuery(name="GET_ACTIVE_BLOOD_GROUP_MASTER",
		query=" SELECT bloodGroupMaster.bloodGroupId AS bloodGroupId, bloodGroupMaster.bloodGroupCode AS bloodGroupCode,"
				+ " bloodGroupMaster.bloodGroupDesc AS bloodGroupDesc,  bloodGroupMaster.organizationId AS organizationId,"
				+ " bloodGroupMaster.unitId AS unitId, bloodGroupMaster.status AS status "
				+ " FROM BloodGroupMaster bloodGroupMaster"
				+ " WHERE bloodGroupMaster.status= 'A'"
				+ " AND bloodGroupMaster.organizationId= :organizationId"
				+ " AND bloodGroupMaster.unitId= :unitId"
				+ " ORDER BY bloodGroupMaster.bloodGroupId asc "
				),
	
	@NamedQuery(name="GET_BLOOD_GROUP_MASTER_BY_NAME",
		query=" SELECT bloodGroupMaster.bloodGroupId AS bloodGroupId "
				+ " FROM BloodGroupMaster bloodGroupMaster"
				+ " WHERE LOWER(bloodGroupMaster.bloodGroupDesc)= :bloodGroupDesc"
				),
	
	@NamedQuery(name="GET_BLOOD_GROUP_MASTER_BY_NAME_NOT_ID",
		query=" SELECT bloodGroupMaster.bloodGroupId AS bloodGroupId "
				+ " FROM BloodGroupMaster bloodGroupMaster"
				+ " WHERE bloodGroupMaster.bloodGroupId!= :bloodGroupId"
				+ " AND LOWER(bloodGroupMaster.bloodGroupDesc)= :bloodGroupDesc"
				),
	
	
		
})
@Entity
@Table(name="m_blood_group_master", schema="blood_bank")
@SequenceGenerator(name="seq_m_blood_group_master", sequenceName="blood_bank.seq_m_blood_group_master", allocationSize= 1)
public class BloodGroupMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_blood_group_master")
	
	@Column(name = "blood_group_id")
	private Integer bloodGroupId;
	
	@Column(name = "blood_group_code")
	private String bloodGroupCode;
	
	@Column(name = "blood_group_desc")
	private String bloodGroupDesc;
	
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

	/**
	 * @return the bloodGroupId
	 */
	public Integer getBloodGroupId() {
		return bloodGroupId;
	}

	/**
	 * @param bloodGroupId the bloodGroupId to set
	 */
	public void setBloodGroupId(Integer bloodGroupId) {
		this.bloodGroupId = bloodGroupId;
	}

	/**
	 * @return the bloodGroupCode
	 */
	public String getBloodGroupCode() {
		return bloodGroupCode;
	}

	/**
	 * @param bloodGroupCode the bloodGroupCode to set
	 */
	public void setBloodGroupCode(String bloodGroupCode) {
		this.bloodGroupCode = bloodGroupCode;
	}

	/**
	 * @return the bloodGroupDesc
	 */
	public String getBloodGroupDesc() {
		return bloodGroupDesc;
	}

	/**
	 * @param bloodGroupDesc the bloodGroupDesc to set
	 */
	public void setBloodGroupDesc(String bloodGroupDesc) {
		this.bloodGroupDesc = bloodGroupDesc;
	}

	/**
	 * @return the createdBy
	 */
	public Integer getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the status
	 */
	public Character getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Character status) {
		this.status = status;
	}

	/**
	 * @return the updatedBy
	 */
	public Integer getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * @return the updatedDate
	 */
	public Date getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	/**
	 * @return the unitId
	 */
	public Integer getUnitId() {
		return unitId;
	}

	/**
	 * @param unitId the unitId to set
	 */
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	/**
	 * @return the organizationId
	 */
	public Integer getOrganizationId() {
		return organizationId;
	}

	/**
	 * @param organizationId the organizationId to set
	 */
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	
}
