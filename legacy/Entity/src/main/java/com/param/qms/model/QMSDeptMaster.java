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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;




@NamedQueries({
	@NamedQuery(name="GET_QMS_DEPT_MASTER_LIST",
			query="SELECT deptMstr.qmsDeptMasterId as qmsDeptMasterId,deptMstr.qmsDeptName as qmsDeptName,"
					+ " deptMstr.startLetter as startLetter,deptMstr.startNumber as startNumber,"
					+ " deptMstr.qmsKioskMasterId as qmsKioskMasterId,kioskMstr.qmsDesc as qmsDesc,"
					+ " deptMstr.isReception as isReception,deptMstr.isVoice as isVoice ,deptMstr.status as status "
					+ " FROM QMSDeptMaster deptMstr INNER JOIN deptMstr.qmsKioskMaster kioskMstr "
					+ " WHERE deptMstr.organisationId=:orgId AND deptMstr.unitId=:unitId"
					+ " ORDER BY kioskMstr.qmsDesc ASC "),
	@NamedQuery(name="GET_QMS_DEPT_MASTER_BY_ID",
	query="SELECT deptMstr.qmsDeptMasterId as qmsDeptMasterId,deptMstr.qmsDeptName as qmsDeptName,"
			+ " deptMstr.startLetter as startLetter,deptMstr.startNumber as startNumber,"
			+ " deptMstr.qmsKioskMasterId as qmsKioskMasterId,kioskMstr.qmsDesc as qmsDesc,"
			+ " deptMstr.isReception as isReception,deptMstr.isVoice as isVoice ,deptMstr.status as status "
			+ " FROM QMSDeptMaster deptMstr INNER JOIN deptMstr.qmsKioskMaster kioskMstr "
			+ " WHERE deptMstr.organisationId=:orgId AND deptMstr.unitId=:unitId AND deptMstr.qmsDeptMasterId=:id"),
	@NamedQuery(name="GET_QMS_DEPT_MASTER_BY_NAME",
	query="SELECT deptMstr.qmsDeptMasterId as qmsDeptMasterId "
			+ " FROM QMSDeptMaster deptMstr "
			+ " WHERE deptMstr.organisationId=:orgId "
			+ " AND deptMstr.unitId=:unitId "
//			+ " AND LOWER(deptMstr.startLetter)=:startLetter "
//			+ " AND LOWER(deptMstr.startNumber)=:starNumber "
			+ " AND deptMstr.qmsKioskMasterId=:qmsKioskId "
//			+ " AND deptMstr.isReception=:isReception "
//			+ " AND deptMstr.isVoice=:isVoice "
			+ " AND LOWER(deptMstr.qmsDeptName)=:deptName"),
	@NamedQuery(name="GET_QMS_DEPT_MASTER_BY_NAME_NOT_BY_ID",
	query="SELECT deptMstr.qmsDeptMasterId as qmsDeptMasterId "
			+ " FROM QMSDeptMaster deptMstr "
			+ " WHERE deptMstr.organisationId=:orgId "
			+ " AND deptMstr.unitId=:unitId "
//			+ " AND LOWER(deptMstr.startLetter)=:startLetter "
//			+ " AND LOWER(deptMstr.startNumber)=:starNumber "
			+ " AND deptMstr.qmsKioskMasterId=:qmsKioskId "
//			+ " AND deptMstr.isReception=:isReception "
//			+ " AND deptMstr.isVoice=:isVoice "
			+ " AND LOWER(deptMstr.qmsDeptName)=:deptName"
			+ " AND deptMstr.qmsDeptMasterId!=:id"),
	@NamedQuery(name="GET_ACTIVE_QMS_DEPT_MASTER_LIST",
	query="SELECT deptMstr.qmsDeptMasterId as qmsDeptMasterId,deptMstr.qmsDeptName as qmsDeptName,"
			+ " deptMstr.startLetter as startLetter,deptMstr.startNumber as startNumber,"
			+ " deptMstr.qmsKioskMasterId as qmsKioskMasterId,kioskMstr.qmsDesc as qmsDesc,"
			+ " deptMstr.isReception as isReception,deptMstr.isVoice as isVoice ,deptMstr.status as status "
			+ " FROM QMSDeptMaster deptMstr INNER JOIN deptMstr.qmsKioskMaster kioskMstr "
			+ " WHERE deptMstr.organisationId=:orgId AND deptMstr.unitId=:unitId AND deptMstr.status='A'"),
	@NamedQuery(name="GET_RECEPTION_LIST_BY_KIOSK",
	query="SELECT deptMstr.qmsDeptMasterId as qmsDeptMasterId "
			+ " FROM QMSDeptMaster deptMstr"
			+ " WHERE deptMstr.organisationId=:orgId AND deptMstr.unitId=:unitId AND deptMstr.isReception='A'"
			+ " AND deptMstr.qmsKioskMasterId=:kioskMasterId"),
	@NamedQuery(name="GET_ACTIVE_DEPT_LIST_BY_KIOSK_ID",
	query="SELECT deptMstr.qmsDeptMasterId as qmsDeptMasterId,deptMstr.qmsDeptName as qmsDeptName,"
			+ " deptMstr.startLetter as startLetter,deptMstr.startNumber as startNumber,"
			+ " deptMstr.qmsKioskMasterId as qmsKioskMasterId,kioskMstr.qmsDesc as qmsDesc,"
			+ " deptMstr.isReception as isReception,deptMstr.isVoice as isVoice ,deptMstr.status as status "
			+ " FROM QMSDeptMaster deptMstr INNER JOIN deptMstr.qmsKioskMaster kioskMstr "
			+ " WHERE deptMstr.organisationId=:orgId "
			+ " AND deptMstr.unitId=:unitId "
			+ " AND deptMstr.status='A'"
			+ " AND deptMstr.qmsKioskMasterId=:kioskId")
})
@Entity
@Table(name="m_qms_dept_master",schema="pharmacy")
@SequenceGenerator(name="m_qms_dept_master_seq",sequenceName="pharmacy.m_qms_dept_master_seq",allocationSize=1)
public class QMSDeptMaster {
	
	@Id
	@Column(name="qms_dept_master_id ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="m_qms_dept_master_seq")
	private Integer qmsDeptMasterId ;
	
	@Column(name="qms_dept_name")
	private String qmsDeptName ;
	
	@Column(name="start_letter")
	private String startLetter ;
	
	@Column(name="start_number")
	private String startNumber ;
	
	@Column(name="qms_kiosk_master_id")
	private Integer qmsKioskMasterId ;
	
	@Column(name="is_reception")
	private Character isReception ;
	
	@Column(name="is_voice")
	private Character isVoice ;
	
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
	@JoinColumn(name = "qms_kiosk_master_id", insertable = false, updatable = false)
	private QMSKioskMaster qmsKioskMaster;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "qmsDeptMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<QMSCounterMaster> listQMSCounterMaster;

	public Integer getQmsDeptMasterId() {
		return qmsDeptMasterId;
	}

	public void setQmsDeptMasterId(Integer qmsDeptMasterId) {
		this.qmsDeptMasterId = qmsDeptMasterId;
	}

	public String getQmsDeptName() {
		return qmsDeptName;
	}

	public void setQmsDeptName(String qmsDeptName) {
		this.qmsDeptName = qmsDeptName;
	}

	public String getStartLetter() {
		return startLetter;
	}

	public void setStartLetter(String startLetter) {
		this.startLetter = startLetter;
	}

	public String getStartNumber() {
		return startNumber;
	}

	public void setStartNumber(String startNumber) {
		this.startNumber = startNumber;
	}

	public Integer getQmsKioskMasterId() {
		return qmsKioskMasterId;
	}

	public void setQmsKioskMasterId(Integer qmsKioskMasterId) {
		this.qmsKioskMasterId = qmsKioskMasterId;
	}

	public Character getIsReception() {
		return isReception;
	}

	public void setIsReception(Character isReception) {
		this.isReception = isReception;
	}

	public Character getIsVoice() {
		return isVoice;
	}

	public void setIsVoice(Character isVoice) {
		this.isVoice = isVoice;
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
	
}
