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
	
	@NamedQuery(name="GET_BLOOD_BAG_CAPACITY_MASTER_LIST",
		query=" SELECT bloodBagCapacityMaster.bloodBagCapacityId AS bloodBagCapacityId, bloodBagCapacityMaster.bloodBagCapacityCode AS bloodBagCapacityCode,"
				+ " bloodBagCapacityMaster.bloodBagCapacityDesc AS bloodBagCapacityDesc,  bloodBagCapacityMaster.organizationId AS organizationId,"
				+ " bloodBagCapacityMaster.unitId AS unitId, bloodBagCapacityMaster.status AS status "
				+ " FROM BloodBagCapacityMaster bloodBagCapacityMaster"
				+ " WHERE bloodBagCapacityMaster.organizationId= :organizationId"
				+ " AND bloodBagCapacityMaster.unitId= :unitId"
				+ " ORDER BY bloodBagCapacityMaster.bloodBagCapacityId asc "
				),
	
	@NamedQuery(name="GET_BLOOD_BAG_CAPACITY_MASTER_BY_ID",
		query=" SELECT bloodBagCapacityMaster.bloodBagCapacityId AS bloodBagCapacityId, bloodBagCapacityMaster.bloodBagCapacityCode AS bloodBagCapacityCode,"
				+ " bloodBagCapacityMaster.bloodBagCapacityDesc AS bloodBagCapacityDesc,  bloodBagCapacityMaster.organizationId AS organizationId,"
				+ " bloodBagCapacityMaster.unitId AS unitId, bloodBagCapacityMaster.status AS status "
				+ " FROM BloodBagCapacityMaster bloodBagCapacityMaster"
				+ " WHERE bloodBagCapacityMaster.bloodBagCapacityId= :bloodBagCapacityId"
				+ " AND bloodBagCapacityMaster.organizationId= :organizationId"
				+ " AND bloodBagCapacityMaster.unitId= :unitId"
				),
	
	@NamedQuery(name="GET_ACTIVE_BLOOD_BAG_CAPACITY_MASTER",
		query=" SELECT bloodBagCapacityMaster.bloodBagCapacityId AS bloodBagCapacityId, bloodBagCapacityMaster.bloodBagCapacityCode AS bloodBagCapacityCode,"
				+ " bloodBagCapacityMaster.bloodBagCapacityDesc AS bloodBagCapacityDesc,  bloodBagCapacityMaster.organizationId AS organizationId,"
				+ " bloodBagCapacityMaster.unitId AS unitId, bloodBagCapacityMaster.status AS status "
				+ " FROM BloodBagCapacityMaster bloodBagCapacityMaster"
				+ " WHERE bloodBagCapacityMaster.status= 'A'"
				+ " AND bloodBagCapacityMaster.organizationId= :organizationId"
				+ " AND bloodBagCapacityMaster.unitId= :unitId"
				+ " ORDER BY bloodBagCapacityMaster.bloodBagCapacityId asc "
				),
	
	@NamedQuery(name="GET_BLOOD_BAG_CAPACITY_MASTER_BY_NAME",
		query=" SELECT bloodBagCapacityMaster.bloodBagCapacityId AS bloodBagCapacityId "
				+ " FROM BloodBagCapacityMaster bloodBagCapacityMaster"
				+ " WHERE LOWER(bloodBagCapacityMaster.bloodBagCapacityCode)= :bloodBagCapacityCode"
				),
	
	@NamedQuery(name="GET_BLOOD_BAG_CAPACITY_MASTER_BY_NAME_NOT_ID",
		query=" SELECT bloodBagCapacityMaster.bloodBagCapacityId AS bloodBagCapacityId "
				+ " FROM BloodBagCapacityMaster bloodBagCapacityMaster"
				+ " WHERE bloodBagCapacityMaster.bloodBagCapacityId!= :bloodBagCapacityId"
				+ " AND LOWER(bloodBagCapacityMaster.bloodBagCapacityCode)= :bloodBagCapacityCode"
				),
	
	
		
})
@Entity
@Table(name = "m_blood_bag_capacity_master", schema = "blood_bank")
@SequenceGenerator(name = "seq_m_blood_bag_capacity_master", sequenceName = "blood_bank.seq_m_blood_bag_capacity_master", allocationSize = 1)
public class BloodBagCapacityMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_blood_bag_capacity_master")
	
	@Column(name = "blood_bag_capacity_id")
	private Integer bloodBagCapacityId;
	
	@Column(name = "blood_bag_capacity_code")
	private String bloodBagCapacityCode;
	
	@Column(name = "blood_bag_capacity_desc")
	private String bloodBagCapacityDesc;
	
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

	public Integer getBloodBagCapacityId() {
		return bloodBagCapacityId;
	}

	public void setBloodBagCapacityId(Integer bloodBagCapacityId) {
		this.bloodBagCapacityId = bloodBagCapacityId;
	}

	public String getBloodBagCapacityCode() {
		return bloodBagCapacityCode;
	}

	public void setBloodBagCapacityCode(String bloodBagCapacityCode) {
		this.bloodBagCapacityCode = bloodBagCapacityCode;
	}

	public String getBloodBagCapacityDesc() {
		return bloodBagCapacityDesc;
	}

	public void setBloodBagCapacityDesc(String bloodBagCapacityDesc) {
		this.bloodBagCapacityDesc = bloodBagCapacityDesc;
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
