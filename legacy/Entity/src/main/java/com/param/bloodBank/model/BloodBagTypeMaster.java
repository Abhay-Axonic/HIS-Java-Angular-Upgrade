package com.param.bloodBank.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@NamedQueries({
	
	@NamedQuery(name="GET_BLOOD_BAG_TYPE_MASTER_LIST",
		query=" SELECT bloodBagTypeMaster.bloodBagTypeId AS bloodBagTypeId, bloodBagTypeMaster.bloodBagTypeCode AS bloodBagTypeCode, bloodBagTypeMaster.itemName AS itemName,"
				+ " bloodBagTypeMaster.bloodBagTypeDesc AS bloodBagTypeDesc,  bloodBagTypeMaster.organizationId AS organizationId,bloodBagTypeMaster.capacityMax AS capacityMax,"
				+ " bloodBagTypeMaster.capacityMin AS capacityMin, bloodBagTypeMaster.unitId AS unitId, bloodBagTypeMaster.status AS status "
				+ " FROM BloodBagTypeMaster bloodBagTypeMaster"
				+ " WHERE bloodBagTypeMaster.organizationId= :organizationId"
				+ " AND bloodBagTypeMaster.unitId= :unitId"
				+ " ORDER BY bloodBagTypeMaster.bloodBagTypeId asc "
				),
	
	@NamedQuery(name="GET_BLOOD_BAG_TYPE_MASTER_BY_ID",
		query=" SELECT bloodBagTypeMaster.bloodBagTypeId AS bloodBagTypeId, bloodBagTypeMaster.bloodBagTypeCode AS bloodBagTypeCode, bloodBagTypeMaster.itemName AS itemName,"
				+ " bloodBagTypeMaster.bloodBagTypeDesc AS bloodBagTypeDesc,  bloodBagTypeMaster.organizationId AS organizationId,bloodBagTypeMaster.capacityMax AS capacityMax,"
				+ " bloodBagTypeMaster.capacityMin AS capacityMin, bloodBagTypeMaster.unitId AS unitId, bloodBagTypeMaster.status AS status "
				+ " FROM BloodBagTypeMaster bloodBagTypeMaster"
				+ " WHERE bloodBagTypeMaster.bloodBagTypeId= :bloodBagTypeId"
				+ " AND bloodBagTypeMaster.organizationId= :organizationId"
				+ " AND bloodBagTypeMaster.unitId= :unitId"
				),
	
	@NamedQuery(name="GET_ACTIVE_BLOOD_BAG_TYPE_MASTER",
		query=" SELECT bloodBagTypeMaster.bloodBagTypeId AS bloodBagTypeId, bloodBagTypeMaster.bloodBagTypeCode AS bloodBagTypeCode, bloodBagTypeMaster.itemName AS itemName,"
				+ " bloodBagTypeMaster.bloodBagTypeDesc AS bloodBagTypeDesc,  bloodBagTypeMaster.organizationId AS organizationId,bloodBagTypeMaster.capacityMax AS capacityMax,"
				+ " bloodBagTypeMaster.capacityMin AS capacityMin, bloodBagTypeMaster.unitId AS unitId, bloodBagTypeMaster.status AS status "
				+ " FROM BloodBagTypeMaster bloodBagTypeMaster"
				+ " WHERE bloodBagTypeMaster.status= 'A'"
				+ " AND bloodBagTypeMaster.organizationId= :organizationId"
				+ " AND bloodBagTypeMaster.unitId= :unitId"
				+ " ORDER BY bloodBagTypeMaster.bloodBagTypeId asc "
				),
	
	@NamedQuery(name="GET_BLOOD_BAG_TYPE_MASTER_BY_NAME",
		query=" SELECT bloodBagTypeMaster.bloodBagTypeId AS bloodBagTypeId "
				+ " FROM BloodBagTypeMaster bloodBagTypeMaster"
				+ " WHERE LOWER(bloodBagTypeMaster.bloodBagTypeDesc)= :bloodBagTypeDesc"
				),
	
	@NamedQuery(name="GET_BLOOD_BAG_TYPE_MASTER_BY_NAME_NOT_ID",
		query=" SELECT bloodBagTypeMaster.bloodBagTypeId AS bloodBagTypeId "
				+ " FROM BloodBagTypeMaster bloodBagTypeMaster"
				+ " WHERE bloodBagTypeMaster.bloodBagTypeId!= :bloodBagTypeId"
				+ " AND LOWER(bloodBagTypeMaster.bloodBagTypeDesc)= :bloodBagTypeDesc"
				),
	
	
		
})
@Entity
@Table(name = "m_blood_bag_type_master", schema = "blood_bank")
@SequenceGenerator(name = "seq_m_blood_bag_type_master", sequenceName = "blood_bank.seq_m_blood_bag_type_master", allocationSize = 1)
public class BloodBagTypeMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_blood_bag_type_master")
	
	@Column(name = "blood_bag_type_id")
	private Integer bloodBagTypeId;
	
	@Column(name = "blood_bag_type_code")
	private String bloodBagTypeCode;
	
	@Column(name = "blood_bag_type_desc")
	private String bloodBagTypeDesc;
	
	@Column(name = "item_name")
	private String itemName;
	
	@Column(name = "capacity_max")
	private Integer capacityMax;
	
	@Column(name = "capacity_min")
	private Integer capacityMin;
	
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
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bloodBagTypeMaster", cascade = CascadeType.ALL, orphanRemoval = true) 
	private List<BloodBankTypeComponentMaster> bloodBagTypeMasterList;

	public Integer getBloodBagTypeId() {
		return bloodBagTypeId;
	}

	public void setBloodBagTypeId(Integer bloodBagTypeId) {
		this.bloodBagTypeId = bloodBagTypeId;
	}

	public String getBloodBagTypeCode() {
		return bloodBagTypeCode;
	}

	public void setBloodBagTypeCode(String bloodBagTypeCode) {
		this.bloodBagTypeCode = bloodBagTypeCode;
	}

	public String getBloodBagTypeDesc() {
		return bloodBagTypeDesc;
	}

	public void setBloodBagTypeDesc(String bloodBagTypeDesc) {
		this.bloodBagTypeDesc = bloodBagTypeDesc;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getCapacityMax() {
		return capacityMax;
	}

	public void setCapacityMax(Integer capacityMax) {
		this.capacityMax = capacityMax;
	}

	public Integer getCapacityMin() {
		return capacityMin;
	}

	public void setCapacityMin(Integer capacityMin) {
		this.capacityMin = capacityMin;
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
