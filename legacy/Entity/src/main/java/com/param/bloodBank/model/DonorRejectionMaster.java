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
	
	@NamedQuery(name="GET_DONOR_REJECTION_MASTER_LIST",
		query=" SELECT donorRejectionMaster.donorRejectionId AS donorRejectionId, donorRejectionMaster.donorRejectionCode AS donorRejectionCode,"
				+ " donorRejectionMaster.donorRejectionDesc AS donorRejectionDesc,  donorRejectionMaster.organizationId AS organizationId,"
				+ " donorRejectionMaster.unitId AS unitId, donorRejectionMaster.status AS status "
				+ " FROM DonorRejectionMaster donorRejectionMaster"
				+ " WHERE donorRejectionMaster.organizationId= :organizationId"
				+ " AND donorRejectionMaster.unitId= :unitId"
				+ " ORDER BY donorRejectionMaster.donorRejectionId asc "
				),
	
	@NamedQuery(name="GET_DONOR_REJECTION_MASTER_BY_ID",
		query=" SELECT donorRejectionMaster.donorRejectionId AS donorRejectionId, donorRejectionMaster.donorRejectionCode AS donorRejectionCode,"
				+ " donorRejectionMaster.donorRejectionDesc AS donorRejectionDesc,  donorRejectionMaster.organizationId AS organizationId,"
				+ " donorRejectionMaster.unitId AS unitId, donorRejectionMaster.status AS status "
				+ " FROM DonorRejectionMaster donorRejectionMaster"
				+ " WHERE donorRejectionMaster.donorRejectionId= :donorRejectionId"
				+ " AND donorRejectionMaster.organizationId= :organizationId"
				+ " AND donorRejectionMaster.unitId= :unitId"
				),
	
	@NamedQuery(name="GET_ACTIVE_DONOR_REJECTION_MASTER",
		query=" SELECT donorRejectionMaster.donorRejectionId AS donorRejectionId, donorRejectionMaster.donorRejectionCode AS donorRejectionCode,"
				+ " donorRejectionMaster.donorRejectionDesc AS donorRejectionDesc,  donorRejectionMaster.organizationId AS organizationId,"
				+ " donorRejectionMaster.unitId AS unitId, donorRejectionMaster.status AS status "
				+ " FROM DonorRejectionMaster donorRejectionMaster"
				+ " WHERE donorRejectionMaster.status= 'A'"
				+ " AND donorRejectionMaster.organizationId= :organizationId"
				+ " AND donorRejectionMaster.unitId= :unitId"
				+ " ORDER BY donorRejectionMaster.donorRejectionId asc "
				),
	
	@NamedQuery(name="GET_DONOR_REJECTION_MASTER_BY_NAME",
		query=" SELECT donorRejectionMaster.donorRejectionId AS donorRejectionId "
				+ " FROM DonorRejectionMaster donorRejectionMaster"
				+ " WHERE LOWER(donorRejectionMaster.donorRejectionDesc)= :donorRejectionDesc"
				),
	
	@NamedQuery(name="GET_DONOR_REJECTION_MASTER_BY_NAME_NOT_ID",
		query=" SELECT donorRejectionMaster.donorRejectionId AS donorRejectionId "
				+ " FROM DonorRejectionMaster donorRejectionMaster"
				+ " WHERE donorRejectionMaster.donorRejectionId!= :donorRejectionId"
				+ " AND LOWER(donorRejectionMaster.donorRejectionDesc)= :donorRejectionDesc"
				),
	
	
		
})
@Entity
@Table(name = "m_donor_rejection_master", schema = "blood_bank")
@SequenceGenerator(name = "seq_m_donor_rejection_master", sequenceName = "blood_bank.seq_m_donor_rejection_master", allocationSize = 1)
public class DonorRejectionMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_donor_rejection_master")
	
	@Column(name = "donor_rejection_id")
	private Integer donorRejectionId;
	
	@Column(name = "donor_rejection_code")
	private String donorRejectionCode;
	
	@Column(name = "donor_rejection_desc")
	private String donorRejectionDesc;
	
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

	public Integer getDonorRejectionId() {
		return donorRejectionId;
	}

	public void setDonorRejectionId(Integer donorRejectionId) {
		this.donorRejectionId = donorRejectionId;
	}

	public String getDonorRejectionCode() {
		return donorRejectionCode;
	}

	public void setDonorRejectionCode(String donorRejectionCode) {
		this.donorRejectionCode = donorRejectionCode;
	}

	public String getDonorRejectionDesc() {
		return donorRejectionDesc;
	}

	public void setDonorRejectionDesc(String donorRejectionDesc) {
		this.donorRejectionDesc = donorRejectionDesc;
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
