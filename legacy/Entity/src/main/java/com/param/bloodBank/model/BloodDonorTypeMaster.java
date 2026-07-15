package com.param.bloodBank.model;

import java.util.Date;
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
@NamedQueries({
	
	@NamedQuery(name="GET_BLOOD_DONOR_TYPE_MASTER_LIST",
		query=" SELECT bloodDonorTypeMaster.bloodDonorTypeId AS bloodDonorTypeId, bloodDonorTypeMaster.bloodDonorTypeCode AS bloodDonorTypeCode,"
				+ " bloodDonorTypeMaster.bloodDonorTypeDesc AS bloodDonorTypeDesc,  bloodDonorTypeMaster.organizationId AS organizationId,"
				+ " bloodDonorTypeMaster.unitId AS unitId, bloodDonorTypeMaster.status AS status "
				+ " FROM BloodDonorTypeMaster bloodDonorTypeMaster"
				+ " WHERE bloodDonorTypeMaster.organizationId= :organizationId"
				+ " AND bloodDonorTypeMaster.unitId= :unitId"
				+ " ORDER BY bloodDonorTypeMaster.bloodDonorTypeId asc "
				),
	
	@NamedQuery(name="GET_BLOOD_DONOR_TYPE_MASTER_BY_ID",
		query=" SELECT bloodDonorTypeMaster.bloodDonorTypeId AS bloodDonorTypeId, bloodDonorTypeMaster.bloodDonorTypeCode AS bloodDonorTypeCode,"
				+ " bloodDonorTypeMaster.bloodDonorTypeDesc AS bloodDonorTypeDesc,  bloodDonorTypeMaster.organizationId AS organizationId,"
				+ " bloodDonorTypeMaster.unitId AS unitId, bloodDonorTypeMaster.status AS status "
				+ " FROM BloodDonorTypeMaster bloodDonorTypeMaster"
				+ " WHERE bloodDonorTypeMaster.bloodDonorTypeId= :bloodDonorTypeId"
				+ " AND bloodDonorTypeMaster.organizationId= :organizationId"
				+ " AND bloodDonorTypeMaster.unitId= :unitId"
				),
	
	@NamedQuery(name="GET_ACTIVE_BLOOD_DONOR_TYPE_MASTER",
		query=" SELECT bloodDonorTypeMaster.bloodDonorTypeId AS bloodDonorTypeId, bloodDonorTypeMaster.bloodDonorTypeCode AS bloodDonorTypeCode,"
				+ " bloodDonorTypeMaster.bloodDonorTypeDesc AS bloodDonorTypeDesc,  bloodDonorTypeMaster.organizationId AS organizationId,"
				+ " bloodDonorTypeMaster.unitId AS unitId, bloodDonorTypeMaster.status AS status "
				+ " FROM BloodDonorTypeMaster bloodDonorTypeMaster"
				+ " WHERE bloodDonorTypeMaster.status= 'A'"
				+ " AND bloodDonorTypeMaster.organizationId= :organizationId"
				+ " AND bloodDonorTypeMaster.unitId= :unitId"
				+ " ORDER BY bloodDonorTypeMaster.bloodDonorTypeId asc "
				),
	
	@NamedQuery(name="GET_BLOOD_DONOR_TYPE_MASTER_BY_NAME",
		query=" SELECT bloodDonorTypeMaster.bloodDonorTypeId AS bloodDonorTypeId "
				+ " FROM BloodDonorTypeMaster bloodDonorTypeMaster"
				+ " WHERE LOWER(bloodDonorTypeMaster.bloodDonorTypeDesc)= :bloodDonorTypeDesc"
				),
	
	@NamedQuery(name="GET_BLOOD_DONOR_TYPE_MASTER_BY_NAME_NOT_ID",
		query=" SELECT bloodDonorTypeMaster.bloodDonorTypeId AS bloodDonorTypeId "
				+ " FROM BloodDonorTypeMaster bloodDonorTypeMaster"
				+ " WHERE bloodDonorTypeMaster.bloodDonorTypeId!= :bloodDonorTypeId"
				+ " AND LOWER(bloodDonorTypeMaster.bloodDonorTypeDesc)= :bloodDonorTypeDesc"
				),
	
	
		
})
@Entity
@Table(name = "m_blood_donor_type_master", schema = "blood_bank")
@SequenceGenerator(name = "seq_m_blood_donor_type_master", sequenceName = "blood_bank.seq_m_blood_donor_type_master", allocationSize = 1)
public class BloodDonorTypeMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_blood_donor_type_master")
	
	@Column(name = "blood_donor_type_id")
	private Integer bloodDonorTypeId;
	
	@Column(name = "blood_donor_type_code")
	private String bloodDonorTypeCode;
	
	@Column(name = "blood_donor_type_desc")
	private String bloodDonorTypeDesc;
	
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
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "bloodDonorTypeMaster", cascade = CascadeType.ALL, orphanRemoval = true) 
	private DonorRegistrationMaster donorRegistrationMaster;

	public Integer getBloodDonorTypeId() {
		return bloodDonorTypeId;
	}

	public void setBloodDonorTypeId(Integer bloodDonorTypeId) {
		this.bloodDonorTypeId = bloodDonorTypeId;
	}

	public String getBloodDonorTypeCode() {
		return bloodDonorTypeCode;
	}

	public void setBloodDonorTypeCode(String bloodDonorTypeCode) {
		this.bloodDonorTypeCode = bloodDonorTypeCode;
	}

	public String getBloodDonorTypeDesc() {
		return bloodDonorTypeDesc;
	}

	public void setBloodDonorTypeDesc(String bloodDonorTypeDesc) {
		this.bloodDonorTypeDesc = bloodDonorTypeDesc;
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
