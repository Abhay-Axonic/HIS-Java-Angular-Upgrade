package com.param.qms.model;

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
	@NamedQuery(name="GET_QMS_MASTER_LIST",
			query="SELECT mstr.qmsKioskMasterId as qmsKioskMasterId,mstr.qmsCode as qmsCode,mstr.qmsDesc as qmsDesc ,mstr.status as status "
					+ " FROM QMSKioskMaster mstr "
					+ " WHERE mstr.organisationId=:orgId AND mstr.unitId=:unitId"
					+ " ORDER BY mstr.qmsDesc ASC "),
	@NamedQuery(name="GET_QMS_MASTER_BY_ID",
	query="SELECT mstr.qmsKioskMasterId as qmsKioskMasterId,mstr.qmsCode as qmsCode,mstr.qmsDesc as qmsDesc ,mstr.status as status "
			+ " FROM QMSKioskMaster mstr "
			+ " WHERE mstr.organisationId=:orgId AND mstr.unitId=:unitId AND mstr.qmsKioskMasterId=:id"),
	@NamedQuery(name="GET_QMS_MASTER_LIST_BY_NAME",
	query="SELECT mstr.qmsKioskMasterId as qmsKioskMasterId,mstr.qmsCode as qmsCode,mstr.qmsDesc as qmsDesc ,mstr.status as status "
			+ " FROM QMSKioskMaster mstr "
			+ " WHERE mstr.organisationId=:orgId AND mstr.unitId=:unitId AND LOWER(mstr.qmsCode)=:code AND LOWER(mstr.qmsDesc)=:desc"),
	@NamedQuery(name="GET_QMS_MASTER_LIST_BY_NAME_NOT_BY_ID",
	query="SELECT mstr.qmsKioskMasterId as qmsKioskMasterId,mstr.qmsCode as qmsCode,mstr.qmsDesc as qmsDesc ,mstr.status as status "
			+ " FROM QMSKioskMaster mstr "
			+ " WHERE mstr.organisationId=:orgId AND mstr.unitId=:unitId AND LOWER(mstr.qmsCode)=:code AND LOWER(mstr.qmsDesc)=:desc AND mstr.qmsKioskMasterId!=:id"),
	@NamedQuery(name="GET_ACTIVE_QMS_MASTER_LIST",
	query="SELECT mstr.qmsKioskMasterId as qmsKioskMasterId,mstr.qmsCode as qmsCode,mstr.qmsDesc as qmsDesc ,mstr.status as status "
			+ " FROM QMSKioskMaster mstr "
			+ " WHERE mstr.organisationId=:orgId AND mstr.unitId=:unitId AND mstr.status='A'"
			+ " ORDER BY mstr.qmsDesc ASC "),
})
@Entity
@Table(name="m_qms_kiosk_master",schema="pharmacy")
@SequenceGenerator(name="m_qms_kiosk_master_seq",sequenceName="pharmacy.m_qms_kiosk_master_seq",allocationSize=1)
public class QMSKioskMaster {
	@Id
	@Column(name="qms_kiosk_master_id ")
	@GeneratedValue(strategy= GenerationType.SEQUENCE,generator="m_qms_kiosk_master_seq")
	private Integer qmsKioskMasterId;
	
	@Column(name="qms_code")
	private String qmsCode;
	
	@Column(name="qms_desc")
	private String qmsDesc;
	
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

	@Column(name="organisation_id")
	private Integer organisationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "qmsKioskMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<QMSDeptMaster> listQMSDeptMaster;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "qmsKioskMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<QMSCounterMaster> listQMSCounterMaster;


	public Integer getQmsKioskMasterId() {
		return qmsKioskMasterId;
	}

	public void setQmsKioskMasterId(Integer qmsKioskMasterId) {
		this.qmsKioskMasterId = qmsKioskMasterId;
	}

	public String getQmsCode() {
		return qmsCode;
	}

	public void setQmsCode(String qmsCode) {
		this.qmsCode = qmsCode;
	}

	public String getQmsDesc() {
		return qmsDesc;
	}

	public void setQmsDesc(String qmsDesc) {
		this.qmsDesc = qmsDesc;
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

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

}
