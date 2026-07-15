package com.param.opd.model;

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

import com.param.adt.master.global.model.UserMaster;
import com.param.global.model.DoctorMaster;
import com.param.global.model.UserRoleMapper;
@NamedQueries({
	
	@NamedQuery(name="GET_OBSERVATION_LIST",
		query="SELECT observation.observationId AS observationId, observation.observationDesc AS observationDesc, "
					+ " observation.patientId as patientId, "
					+ " observation.doctorId as doctorId,"
					+ " to_char(observation.createdDate, 'DD/MM/YYYY') as createdDate,"
					+ " observation.createdDate as createdDateNew,"
					+ " to_char(observation.createdDate, 'HH24:MI') as createdTime,"
					+ " observation.encounterId as encounterId, observation.admissionId as admissionId, "
					+ " observation.organizationId AS organizationId, observation.unitId AS unitId, "
					+ " observation.status AS status, "
					+ " concat(coalesce(user.firstName),' ',coalesce(user.middleName),' ',coalesce(user.lastName)) as employeeName, "
					+ " concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName "
					+ " FROM Observation observation"
					+ " LEFT JOIN observation.userMaster userMst"
					+ " LEFT JOIN 	userMst.employeeMastersList user"
					+ " LEFT JOIN 	userMst.doctorMastersList doc"
					+ " WHERE  observation.organizationId= :organizationId "
					+ " AND observation.unitId= :unitId"
//					+ " ORDER BY observation.observationId asc"
					),
	
	@NamedQuery(name="GET_OBSERVATION_LIST_ABDM",
	query="SELECT observation.observationId AS observationId, observation.observationDesc AS observationDesc, "
				+ " observation.patientId as patientId, "
				+ " observation.doctorId as doctorId,"
				+ " to_char(observation.createdDate, 'DD/MM/YYYY') as createdDate,"
				+ " observation.createdDate as createdDateNew,"
				+ " to_char(observation.createdDate, 'HH24:MI') as createdTime,"
				+ " observation.encounterId as encounterId, observation.admissionId as admissionId, "
				+ " observation.organizationId AS organizationId, observation.unitId AS unitId, "
				+ " observation.status AS status, "
				+ " concat(coalesce(user.firstName),' ',coalesce(user.middleName),' ',coalesce(user.lastName)) as employeeName, "
				+ " concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName "
				+ " FROM Observation observation"
				+ " LEFT JOIN observation.userMaster userMst"
				+ " LEFT JOIN 	userMst.employeeMastersList user"
				+ " LEFT JOIN 	userMst.doctorMastersList doc"
				+ " WHERE  observation.organizationId= :organizationId "
				+ " AND observation.patientId= :patientId"
//				+ " ORDER BY observation.observationId asc"
				),
	
	@NamedQuery(name="GET_OBSERVATION_LIST_COV_TEMPLATE",
	query="SELECT observation.observationId AS observationId, observation.observationDesc AS observationDesc, "
				+ " observation.patientId as patientId, "
				+ " observation.doctorId as doctorId,"
				+ " to_char(observation.createdDate, 'DD/MM/YYYY') as createdDate,"
				+ " to_char(observation.createdDate, 'HH24:MI') as createdTime,"
				+ " observation.encounterId as encounterId, observation.admissionId as admissionId, "
				+ " observation.organizationId AS organizationId, observation.unitId AS unitId, "
				+ " observation.status AS status, "
				+ " concat(coalesce(user.firstName),' ',coalesce(user.middleName),' ',coalesce(user.lastName)) as employeeName, "
				+ " concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName "
				+ " FROM Observation observation"
				+ " LEFT JOIN observation.userMaster userMst"
				+ " LEFT JOIN 	userMst.employeeMastersList user"
				+ " LEFT JOIN 	userMst.doctorMastersList doc"
				+ " WHERE  observation.organizationId= :organizationId "
				+ " AND observation.unitId= :unitId"
				+ " AND observation.covTemplateId= :covTemplateId"
//				+ " ORDER BY observation.observationId asc"
				),
	
	@NamedQuery(name="GET_OBSERVATION_BY_ID",
		query="SELECT observation.observationId AS observationId, observation.observationDesc AS observationDesc,observation.patientId as patientId, observation.doctorId as doctorId,"
					+ " observation.encounterId as encounterId, observation.admissionId as admissionId, "
					+ " observation.organizationId AS organizationId, observation.unitId AS unitId, observation.status AS status"
					+ " FROM Observation observation"
					+ " WHERE observation.observationId= :observationId"
					+ " AND observation.organizationId= :organizationId"
					+ " AND observation.unitId= :unitId"
					),
})
@Entity
@Table(name = "t_observation", schema = "opd")
@SequenceGenerator(name = "t_observation_seq", sequenceName = "opd.t_observation_seq", allocationSize = 1)
public class Observation {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_observation_seq")
	
	@Column(name = "observation_id")
	private Integer observationId;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "encounter_id")
	private Integer encounterId;
	
	@Column(name = "doctor_id")
	private Integer doctorId;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "observation_desc")
	private String observationDesc;
	
	@Column(name="cov_template_id")
	private Integer covTemplateId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by", insertable = false, nullable = false, updatable = false)
	private UserMaster userMaster;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserRoleMapper> userMasterList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DoctorMaster> doctorMastersList;
	
	public Integer getObservationId() {
		return observationId;
	}

	public void setObservationId(Integer observationId) {
		this.observationId = observationId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getObservationDesc() {
		return observationDesc;
	}

	public void setObservationDesc(String observationDesc) {
		this.observationDesc = observationDesc;
	}

	public Integer getCovTemplateId() {
		return covTemplateId;
	}

	public void setCovTemplateId(Integer covTemplateId) {
		this.covTemplateId = covTemplateId;
	}
	
	
}
