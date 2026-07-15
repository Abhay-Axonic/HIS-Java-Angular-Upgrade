package com.param.global.org.common.model;

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

import com.param.global.model.DoctorCabinMapper;
import com.param.global.model.VaccinationMaster;

@NamedQueries({
	@NamedQuery(name="GET_DOSE_MASTER_LIST_BY_ID",
			query="SELECT dm.doseId AS doseId,"
					+ " dm.doseCode AS doseCode,"
					+ " dm.doseDesc AS doseDesc,"
					+ " dm.status AS status "
					+ " FROM DoseMaster dm "
					+ " WHERE dm.organizationId= :orgId "
					+ " AND dm.doseId= :doseId"
					+ " ORDER BY dm.doseDesc ASC"),
	@NamedQuery(name="GET_DOSE_MASTER_LIST",
	query="SELECT dm.doseId AS doseId,"
			+ " dm.doseCode AS doseCode,"
			+ " dm.doseDesc AS doseDesc,"
			+ " dm.status AS status "
			+ " FROM DoseMaster dm "
			+ " WHERE dm.organizationId=:orgId"
			+ " ORDER BY dm.doseDesc ASC"),
	@NamedQuery(name="GET_ACTIVE_DOSE_MASTER_LIST",
	query="SELECT dm.doseId AS doseId,"
			+ " dm.doseCode AS doseCode,"
			+ " dm.doseDesc AS doseDesc,"
			+ " dm.status AS status "
			+ " FROM DoseMaster dm "
			+ " WHERE dm.organizationId=:orgId "
			+ " AND dm.status='A'"
			+ " ORDER BY dm.doseDesc ASC"),
	@NamedQuery(name="GET_DOSE_MASTER_LIST_BY_NAME",
	query="SELECT dm.doseId AS doseId "		
			+ " FROM DoseMaster dm "
			+ " WHERE LOWER(dm.doseDesc)=:name "
			+ " AND dm.organizationId=:orgId "),
	
	@NamedQuery(name="GET_DOSEMASTER_LIST_BY_NAME_NOT_BY_ID",
	query="SELECT dm.doseId AS doseId "		
			+ " FROM DoseMaster dm "
			+ " WHERE LOWER(dm.doseDesc)=:name"
			+ " AND dm.doseId!=:doseId "
			+ " AND dm.organizationId=:orgId ")
})
@Entity
@Table(name = "m_dose", schema = "public")
@SequenceGenerator(name = "dose_master_seq", sequenceName = "public.dose_master_seq", allocationSize = 1)
public class DoseMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dose_master_seq")
	@Column(name = "dose_id")
	private Integer doseId;

	@Column(name = "dose_code")
	private String doseCode;

	@Column(name = "dose_desc")
	private String doseDesc;

	@Column(name = "status")
	private Character status;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "organization_id")
	private Integer organizationId;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "doseMaster", cascade = CascadeType.ALL, orphanRemoval = true) 
	private List<VaccinationMaster> vaccinationMasterlist;
	
	public Integer getDoseId() {
		return doseId;
	}

	public void setDoseId(Integer doseId) {
		this.doseId = doseId;
	}

	public String getDoseCode() {
		return doseCode;
	}

	public void setDoseCode(String doseCode) {
		this.doseCode = doseCode;
	}

	public String getDoseDesc() {
		return doseDesc;
	}

	public void setDoseDesc(String doseDesc) {
		this.doseDesc = doseDesc;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	
	
}
