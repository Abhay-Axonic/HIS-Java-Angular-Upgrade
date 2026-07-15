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
	
	@NamedQuery(name="GET_BLOOD_COMPONENT_MASTER_LIST",
		query=" SELECT bloodComponentMaster.bloodComponentId AS bloodComponentId, bloodComponentMaster.bloodComponentDesc AS bloodComponentDesc, "
				+ " bloodComponentMaster.bloodComponentCode AS bloodComponentCode,  bloodComponentMaster.organizationId AS organizationId,"
				+ " bloodComponentMaster.bloodComponentShortDesc AS bloodComponentShortDesc, bloodComponentMaster.expiry AS expiry, bloodComponentMaster.quantity AS quantity,"
				+ " bloodComponentMaster.byCrossChecking AS byCrossChecking, bloodComponentMaster.unitId AS unitId, bloodComponentMaster.status AS status "
				+ " FROM BloodComponentMaster bloodComponentMaster"
				+ " WHERE bloodComponentMaster.organizationId= :organizationId"
				+ " AND bloodComponentMaster.unitId= :unitId"
				+ " ORDER BY bloodComponentMaster.bloodComponentDesc asc "
				),
	
	@NamedQuery(name="GET_BLOOD_COMPONENT_MASTER_BY_ID",
		query=" SELECT bloodComponentMaster.bloodComponentId AS bloodComponentId, bloodComponentMaster.bloodComponentCode AS bloodComponentCode, "
				+ " bloodComponentMaster.bloodComponentDesc AS bloodComponentDesc,  bloodComponentMaster.organizationId AS organizationId, "
				+ " bloodComponentMaster.byCrossChecking AS byCrossChecking, bloodComponentMaster.bloodComponentShortDesc AS bloodComponentShortDesc, bloodComponentMaster.expiry AS expiry, bloodComponentMaster.quantity AS quantity,"
				+ " bloodComponentMaster.unitId AS unitId, bloodComponentMaster.status AS status "
				+ " FROM BloodComponentMaster bloodComponentMaster"
				+ " WHERE bloodComponentMaster.bloodComponentId= :bloodComponentId"
				+ " AND bloodComponentMaster.organizationId= :organizationId"
				+ " AND bloodComponentMaster.unitId= :unitId"
				),
	
	@NamedQuery(name="GET_ACTIVE_BLOOD_COMPONENT_MASTER",
		query=" SELECT bloodComponentMaster.bloodComponentId AS bloodComponentId, bloodComponentMaster.bloodComponentCode AS bloodComponentCode, "
				+ " bloodComponentMaster.bloodComponentDesc AS bloodComponentDesc,  bloodComponentMaster.organizationId AS organizationId, "
				+ " bloodComponentMaster.bloodComponentShortDesc AS bloodComponentShortDesc, bloodComponentMaster.expiry AS expiry, bloodComponentMaster.quantity AS quantity,"
				+ " bloodComponentMaster.byCrossChecking AS byCrossChecking, bloodComponentMaster.unitId AS unitId, bloodComponentMaster.status AS status "
				+ " FROM BloodComponentMaster bloodComponentMaster"
				+ " WHERE bloodComponentMaster.status= 'A'"
				+ " AND bloodComponentMaster.organizationId= :organizationId"
				+ " AND bloodComponentMaster.unitId= :unitId"
				+ " ORDER BY bloodComponentMaster.bloodComponentId asc "
				),
	
	@NamedQuery(name="GET_BLOOD_COMPONENT_MASTER_BY_NAME",
		query=" SELECT bloodComponentMaster.bloodComponentId AS bloodComponentId "
				+ " FROM BloodComponentMaster bloodComponentMaster"
				+ " WHERE LOWER(bloodComponentMaster.bloodComponentDesc)= :bloodComponentDesc"
				),
	
	@NamedQuery(name="GET_BLOOD_COMPONENT_MASTER_BY_NAME_NOT_ID",
		query=" SELECT bloodComponentMaster.bloodComponentId AS bloodComponentId "
				+ " FROM BloodComponentMaster bloodComponentMaster"
				+ " WHERE bloodComponentMaster.bloodComponentId!= :bloodComponentId"
				+ " AND LOWER(bloodComponentMaster.bloodComponentDesc)= :bloodComponentDesc"
				),
	
	
		
})
@Entity
@Table(name = "m_blood_component_master", schema = "blood_bank")
@SequenceGenerator(name = "seq_m_blood_component_master", sequenceName = "blood_bank.seq_m_blood_component_master", allocationSize = 1)
public class BloodComponentMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_blood_component_master")
	
	@Column(name = "blood_component_id")
	private Integer bloodComponentId;
	
	@Column(name = "blood_component_code")
	private String bloodComponentCode;
	
	@Column(name = "blood_component_short_desc")
	private String bloodComponentShortDesc;
	
	@Column(name = "blood_component_desc")
	private String bloodComponentDesc;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "expiry")
	private Integer expiry;
	
	@Column(name = "quantity")
	private Integer quantity;
	
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

	@Column(name = "by_cross_checking")
	private Character byCrossChecking;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bloodComponentMaster", cascade = CascadeType.ALL, orphanRemoval = true) 
	private List<BloodBankTypeComponentMaster> bloodComponentMasterList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bloodComponentMaster", cascade = CascadeType.ALL, orphanRemoval = true) 
	private List<BloodComponentOrderMaster> bloodComponentOrderMasterList;
	
	public Integer getBloodComponentId() {
		return bloodComponentId;
	}

	public void setBloodComponentId(Integer bloodComponentId) {
		this.bloodComponentId = bloodComponentId;
	}

	public String getBloodComponentCode() {
		return bloodComponentCode;
	}

	public void setBloodComponentCode(String bloodComponentCode) {
		this.bloodComponentCode = bloodComponentCode;
	}

	public String getBloodComponentDesc() {
		return bloodComponentDesc;
	}

	public void setBloodComponentDesc(String bloodComponentDesc) {
		this.bloodComponentDesc = bloodComponentDesc;
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

	public String getBloodComponentShortDesc() {
		return bloodComponentShortDesc;
	}

	public void setBloodComponentShortDesc(String bloodComponentShortDesc) {
		this.bloodComponentShortDesc = bloodComponentShortDesc;
	}

	public Integer getExpiry() {
		return expiry;
	}

	public void setExpiry(Integer expiry) {
		this.expiry = expiry;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Character getByCrossChecking() {
		return byCrossChecking;
	}

	public void setByCrossChecking(Character byCrossChecking) {
		this.byCrossChecking = byCrossChecking;
	}
	
	
}
