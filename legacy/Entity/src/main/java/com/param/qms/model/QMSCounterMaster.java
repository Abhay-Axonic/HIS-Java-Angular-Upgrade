package com.param.qms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.ICommonConstants;
@NamedQueries({
	@NamedQuery(name="GET_QMS_COUNTER_MASTER_LIST",
			query="SELECT mstr.qmsCounterId as qmsCounterId ,mstr.qmsCounterCode as qmsCounterCode,"
					+ "mstr.qmsCounterDesc as qmsCounterDesc,"
					+ "mstr.status as status  "
					+ " FROM QMSCounter mstr  "
					+ " WHERE mstr.organizationId=:orgId AND mstr.unitId=:unitId "),
	
	@NamedQuery(name="GET_QMS_COUNTER_BY_ID",
	query="SELECT mstr.qmsCounterId as qmsCounterId ,mstr.qmsCounterCode as qmsCounterCode,"
			+ "mstr.qmsCounterDesc as qmsCounterDesc,"
			+ "mstr.status as status  "
			+ " FROM QMSCounter mstr  "
			+ " WHERE mstr.organizationId=:orgId AND mstr.unitId=:unitId and mstr.qmsCounterId =:id"),
	
	@NamedQuery(name="GET_QMS_COUNTER_MASTER_BY_ID",
		query="SELECT mstr.qmsCounterMasterId as qmsCounterMasterId ,mstr.qmsCounterCode as qmsCounterCode,"
			+ "mstr.qmsCounterDesc as qmsCounterDesc,mstr.qmsDeptMasterId as qmsDeptMasterId,qmsDeptMstr.qmsDeptName as qmsDeptName,"
			+ "mstr.status as status,kioskMstr.qmsKioskMasterId as qmsKioskMasterId,kioskMstr.qmsDesc as qmsDesc "
			+ " FROM QMSCounterMaster mstr INNER JOIN mstr.qmsDeptMaster qmsDeptMstr "
			+ " INNER JOIN mstr.qmsKioskMaster kioskMstr "
			+ " WHERE mstr.organisationId=:orgId AND mstr.unitId=:unitId AND mstr.qmsCounterId=:id and mstr.status = 'A' "),
	
	@NamedQuery(name="GET_QMS_COUNTER_MASTER_LIST_BY_NAME",
	query="SELECT mstr.qmsCounterMasterId as qmsCounterMasterId ,mstr.qmsCounterCode as qmsCounterCode,"
		+ "mstr.qmsCounterDesc as qmsCounterDesc,mstr.qmsDeptMasterId as qmsDeptMasterId,"
		+ "mstr.status as status "
		+ " FROM QMSCounterMaster mstr "
		+ " WHERE mstr.organisationId=:orgId AND mstr.unitId=:unitId AND LOWER(mstr.qmsCounterCode)=:code "
		+ " AND LOWER(mstr.qmsCounterDesc)=:desc  AND mstr.qmsKioskMasterId=:kioskId"),
	@NamedQuery(name="GET_QMS_COUNTER_MASTER_LIST_BY_NAME_NOT_BY_ID",
	query="SELECT mstr.qmsCounterMasterId as qmsCounterMasterId ,mstr.qmsCounterCode as qmsCounterCode,"
		+ "mstr.qmsCounterDesc as qmsCounterDesc,mstr.qmsDeptMasterId as qmsDeptMasterId,"
		+ "mstr.status as status "
		+ " FROM QMSCounterMaster mstr "
		+ " WHERE mstr.organisationId=:orgId AND mstr.unitId=:unitId AND LOWER(mstr.qmsCounterCode)=:code "
		+ " AND LOWER(mstr.qmsCounterDesc)=:desc AND  mstr.qmsKioskMasterId=:kioskId AND mstr.qmsCounterMasterId!=:id"),
	@NamedQuery(name="GET_QMS_COUNTER_LIST_BY_DEPT_ID",
	query="SELECT mstr.qmsCounterMasterId as qmsCounterMasterId ,mstr.qmsCounterCode as qmsCounterCode,"
		+ "mstr.qmsCounterDesc as qmsCounterDesc,mstr.qmsDeptMasterId as qmsDeptMasterId,"
		+ "mstr.status as status,qmsDeptMstr.qmsDeptName as qmsDeptName "
		+ " FROM QMSCounterMaster mstr INNER JOIN mstr.qmsDeptMaster qmsDeptMstr "
		+ " WHERE mstr.organisationId=:orgId AND mstr.unitId=:unitId "
		+ " AND mstr.qmsDeptMasterId=:qmsDeptId AND mstr.status='A'"
		+ " ORDER BY mstr.qmsCounterDesc ASC ")
	
})

@Entity
@Table(name="m_qms_counter_master",schema="pharmacy")
@SequenceGenerator(name="m_qms_counter_master_seq",sequenceName="pharmacy.m_qms_counter_master_seq",allocationSize=1)
public class QMSCounterMaster implements ICommonConstants{

	@Id
	@Column(name="qms_counter_master_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="m_qms_counter_master_seq")
	private Integer qmsCounterMasterId;
	
	@Column(name="qms_counter_code")
	private String qmsCounterCode;
	
	@Column(name="qms_counter_desc")
	private String qmsCounterDesc;
	
	@Column(name="qms_kiosk_master_id")
	private Integer qmsKioskMasterId;
	
	@Column(name="qms_dept_master_id")
	private Integer qmsDeptMasterId;
	
	@Column(name="qms_counter_id")
	private Integer qmsCounterId;
	
	@Column(name="status")
	private Character status ;
	
	@Column(name="unit_id")
	private Integer unitId ;
	
	@Column(name="organisation_id")
	private Integer organisationId ;
	
	@Column(name="created_by")
	private Integer createdBy ;
	
	@Column(name="updated_by")
	private Integer updatedBy ;
	
	@Column(name="created_date")
	private Date createdDate ;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "qms_dept_master_id", insertable = false, updatable = false)
	private QMSDeptMaster qmsDeptMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "qms_kiosk_master_id", insertable = false, updatable = false)
	private QMSKioskMaster qmsKioskMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "qms_counter_id", insertable = false, updatable = false)
	private QMSCounter QMSCounter;
	
	public Integer getQmsCounterMasterId() {
		return qmsCounterMasterId;
	}

	public void setQmsCounterMasterId(Integer qmsCounterMasterId) {
		this.qmsCounterMasterId = qmsCounterMasterId;
	}

	public String getQmsCounterCode() {
		return qmsCounterCode;
	}

	public void setQmsCounterCode(String qmsCounterCode) {
		this.qmsCounterCode = qmsCounterCode;
	}

	public String getQmsCounterDesc() {
		return qmsCounterDesc;
	}

	public void setQmsCounterDesc(String qmsCounterDesc) {
		this.qmsCounterDesc = qmsCounterDesc;
	}

	public Integer getQmsDeptMasterId() {
		return qmsDeptMasterId;
	}

	public void setQmsDeptMasterId(Integer qmsDeptMasterId) {
		this.qmsDeptMasterId = qmsDeptMasterId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getQmsKioskMasterId() {
		return qmsKioskMasterId;
	}

	public void setQmsKioskMasterId(Integer qmsKioskMasterId) {
		this.qmsKioskMasterId = qmsKioskMasterId;
	}

	public Integer getQmsCounterId() {
		return qmsCounterId;
	}

	public void setQmsCounterId(Integer qmsCounterId) {
		this.qmsCounterId = qmsCounterId;
	}		
	
}
