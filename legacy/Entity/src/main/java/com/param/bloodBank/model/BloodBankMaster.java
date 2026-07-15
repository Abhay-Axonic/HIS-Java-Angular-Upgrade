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
	
	@NamedQuery(name="GET_BLOOD_BANK_MASTER_LIST",
		query=" SELECT bloodBankMaster.bloodBankId AS bloodBankId, bloodBankMaster.bloodBankCode AS bloodBankCode, bloodBankMaster.address AS address, bloodBankMaster.contactNo AS contactNo,"
				+ " bloodBankMaster.bloodBankDesc AS bloodBankDesc,  bloodBankMaster.organizationId AS organizationId,"
				+ " bloodBankMaster.unitId AS unitId, bloodBankMaster.status AS status "
				+ " FROM BloodBankMaster bloodBankMaster"
				+ " WHERE bloodBankMaster.organizationId= :organizationId"
				+ " AND bloodBankMaster.unitId= :unitId"
				+ " ORDER BY bloodBankMaster.bloodBankId asc "
				),
	
	@NamedQuery(name="GET_BLOOD_BANK_MASTER_BY_ID",
		query=" SELECT bloodBankMaster.bloodBankId AS bloodBankId, bloodBankMaster.bloodBankCode AS bloodBankCode,"
				+ " bloodBankMaster.bloodBankDesc AS bloodBankDesc,  bloodBankMaster.organizationId AS organizationId,bloodBankMaster.address AS address, bloodBankMaster.contactNo AS contactNo,"
				+ " bloodBankMaster.unitId AS unitId, bloodBankMaster.status AS status "
				+ " FROM BloodBankMaster bloodBankMaster"
				+ " WHERE bloodBankMaster.bloodBankId= :bloodBankId"
				+ " AND bloodBankMaster.organizationId= :organizationId"
				+ " AND bloodBankMaster.unitId= :unitId"
				),
	
	@NamedQuery(name="GET_ACTIVE_BLOOD_BANK_MASTER",
		query=" SELECT bloodBankMaster.bloodBankId AS bloodBankId, bloodBankMaster.bloodBankCode AS bloodBankCode, "
				+ " bloodBankMaster.bloodBankDesc AS bloodBankDesc,  bloodBankMaster.organizationId AS organizationId, bloodBankMaster.address AS address, bloodBankMaster.contactNo AS contactNo,"
				+ " bloodBankMaster.unitId AS unitId, bloodBankMaster.status AS status "
				+ " FROM BloodBankMaster bloodBankMaster"
				+ " WHERE bloodBankMaster.status= 'A'"
				+ " AND bloodBankMaster.organizationId= :organizationId"
				+ " AND bloodBankMaster.unitId= :unitId"
				+ " ORDER BY bloodBankMaster.bloodBankId asc "
				),
	
	@NamedQuery(name="GET_BLOOD_BANK_MASTER_BY_NAME",
		query=" SELECT bloodBankMaster.bloodBankId AS bloodBankId "
				+ " FROM BloodBankMaster bloodBankMaster"
				+ " WHERE LOWER(bloodBankMaster.bloodBankDesc)= :bloodBankDesc"
				),
	
	@NamedQuery(name="GET_BLOOD_BANK_MASTER_BY_NAME_NOT_ID",
		query=" SELECT bloodBankMaster.bloodBankId AS bloodBankId "
				+ " FROM BloodBankMaster bloodBankMaster"
				+ " WHERE bloodBankMaster.bloodBankId!= :bloodBankId"
				+ " AND LOWER(bloodBankMaster.bloodBankDesc)= :bloodBankDesc"
				),
	
	
		
})
@Entity
@Table(name = "m_blood_bank_master", schema = "blood_bank")
@SequenceGenerator(name = "seq_m_blood_bank_master", sequenceName = "blood_bank.seq_m_blood_bank_master", allocationSize = 1)
public class BloodBankMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_blood_bank_master")
	
	@Column(name = "blood_bank_id")
	private Integer bloodBankId;
	
	@Column(name = "blood_bank_code")
	private String bloodBankCode;
	
	@Column(name = "blood_bank_desc")
	private String bloodBankDesc;
	
	@Column(name = "contact_number")
	private String contactNo;
	
	@Column(name = "address")
	private String address;
	
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

	public Integer getBloodBankId() {
		return bloodBankId;
	}

	public void setBloodBankId(Integer bloodBankId) {
		this.bloodBankId = bloodBankId;
	}

	public String getBloodBankCode() {
		return bloodBankCode;
	}

	public void setBloodBankCode(String bloodBankCode) {
		this.bloodBankCode = bloodBankCode;
	}

	public String getBloodBankDesc() {
		return bloodBankDesc;
	}

	public void setBloodBankDesc(String bloodBankDesc) {
		this.bloodBankDesc = bloodBankDesc;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
