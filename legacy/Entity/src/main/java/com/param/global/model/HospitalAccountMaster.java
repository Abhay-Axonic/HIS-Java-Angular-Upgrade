package com.param.global.model;

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
	
	@NamedQuery(name = "GET_ACTIVE_HOSPITAL_ACCOUNT_MASTER", 
			query = "SELECT  hospitalAccountMaster.hospitalAccountId as hospitalAccountId, "
					+ " hospitalAccountMaster.hospitalAccountCode AS hospitalAccountCode,"
					+ " hospitalAccountMaster.orgnizationId AS orgnizationId,"
					+ " hospitalAccountMaster.hospitalAccountDesc AS hospitalAccountDesc,"
					+ " hospitalAccountMaster.unitId AS unitId, "
					+ " hospitalAccountMaster.status AS status "
					+ " FROM HospitalAccountMaster hospitalAccountMaster"
					+ " WHERE hospitalAccountMaster.status='A' AND"
					+ " hospitalAccountMaster.unitId=:unitId AND"
					+ " hospitalAccountMaster.orgnizationId=:orgId"),

	@NamedQuery(name = "GET_HOSPITAL_ACCOUNT_MASTER_BY_NAME",
			query = "SELECT hospitalAccountMaster.hospitalAccountId as hospitalAccountId,"
					+ " hospitalAccountMaster.hospitalAccountDesc AS hospitalAccountDesc,"
					+ " hospitalAccountMaster.orgnizationId AS orgnizationId"
					+ " FROM HospitalAccountMaster hospitalAccountMaster"
					+ " WHERE LOWER(hospitalAccountMaster.hospitalAccountDesc)=:hospitalAccountDesc "
					+ " AND hospitalAccountMaster.orgnizationId =:orgId"
					+ " AND hospitalAccountMaster.unitId=:unitId"
					),
			
	@NamedQuery(name = "GET_HOSPITAL_ACCOUNT_MASTER_BY_NAME_NOT_ID",
			query = " SELECT "
					+ " hospitalAccountMaster.hospitalAccountId as hospitalAccountId, "
					+ " hospitalAccountMaster.hospitalAccountDesc AS hospitalAccountDesc "
					+ " FROM HospitalAccountMaster hospitalAccountMaster "
					+ " WHERE hospitalAccountMaster.hospitalAccountId != :hospitalAccountId"
					+ " AND LOWER(hospitalAccountMaster.hospitalAccountDesc)=:hospitalAccountDesc "
					+ " AND hospitalAccountMaster.orgnizationId =:orgId"
					+ " AND hospitalAccountMaster.unitId=:unitId"
					),
	
	@NamedQuery(name = "GET_HOSPITAL_ACCOUNT_MASTER_LIST_BY_ID",
			query =  "SELECT  hospitalAccountMaster.hospitalAccountId as hospitalAccountId, "
					+ " hospitalAccountMaster.hospitalAccountCode AS hospitalAccountCode,"
					+ " hospitalAccountMaster.orgnizationId AS orgnizationId,"
					+ " hospitalAccountMaster.hospitalAccountDesc AS hospitalAccountDesc,"
					+ " hospitalAccountMaster.isDefault AS isDefault,"
					+ " hospitalAccountMaster.unitId AS unitId, "
					+ " hospitalAccountMaster.status AS status "
					+ " FROM HospitalAccountMaster hospitalAccountMaster"
					+ " WHERE hospitalAccountMaster.hospitalAccountId =:hospitalAccountId"
					+ " AND hospitalAccountMaster.orgnizationId =:orgId"
					+ " AND hospitalAccountMaster.unitId=:unitId"),
			
	@NamedQuery(name = "GET_HOSPITAL_ACCOUNT_MASTER_LIST",
			query = "SELECT  hospitalAccountMaster.hospitalAccountId as hospitalAccountId, "
					+ " hospitalAccountMaster.hospitalAccountCode AS hospitalAccountCode,"
					+ " hospitalAccountMaster.orgnizationId AS orgnizationId,"
					+ " hospitalAccountMaster.isDefault AS isDefault,"
					+ " hospitalAccountMaster.hospitalAccountDesc AS hospitalAccountDesc,"
					+ " hospitalAccountMaster.unitId AS unitId, "
					+ " hospitalAccountMaster.status AS status "
					+ " FROM HospitalAccountMaster hospitalAccountMaster"
					+ " WHERE hospitalAccountMaster.orgnizationId =:orgId AND hospitalAccountMaster.unitId=:unitId"
					+ " ORDER BY hospitalAccountMaster.hospitalAccountId asc"
					)
	})
@Entity
@Table(name = "m_hospital_accounts", schema = "billing")
@SequenceGenerator(name = "hospital_accounts_seq", sequenceName = "billing.hospital_accounts_seq", allocationSize = 1)
public class HospitalAccountMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hospital_accounts_seq")
	@Column(name = "hospital_account_id")
	private Integer hospitalAccountId;
	
	@Column(name = "hospital_account_code")
	private String hospitalAccountCode;
	
	@Column(name = "hospital_account_desc")
	private String hospitalAccountDesc;

	@Column(name = "is_default")
	private Character isDefault;

	@Column(name = "organization_id")
	private Integer orgnizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "status")
	private Character status;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	public Integer getHospitalAccountId() {
		return hospitalAccountId;
	}

	public String getHospitalAccountCode() {
		return hospitalAccountCode;
	}

	public String getHospitalAccountDesc() {
		return hospitalAccountDesc;
	}

	public Character getIsDefault() {
		return isDefault;
	}

	public Integer getOrgnizationId() {
		return orgnizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public Character getStatus() {
		return status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setHospitalAccountId(Integer hospitalAccountId) {
		this.hospitalAccountId = hospitalAccountId;
	}

	public void setHospitalAccountCode(String hospitalAccountCode) {
		this.hospitalAccountCode = hospitalAccountCode;
	}

	public void setHospitalAccountDesc(String hospitalAccountDesc) {
		this.hospitalAccountDesc = hospitalAccountDesc;
	}

	public void setIsDefault(Character isDefault) {
		this.isDefault = isDefault;
	}

	public void setOrgnizationId(Integer orgnizationId) {
		this.orgnizationId = orgnizationId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	
}
