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
	
	@NamedQuery(name="GET_NURSING_NOTE_LIST",
		query="SELECT nursingNote.nursingNoteId AS nursingNoteId, nursingNote.nursingNoteDesc AS nursingNoteDesc, "
					+ " nursingNote.patientId as patientId, nursingNote.template as template,"
					+ " nursingNote.doctorId as doctorId,"
					+ " to_char(nursingNote.createdDate, 'DD/MM/YYYY HH24:MI') as createdDate,"
					+ " to_char(nursingNote.createdDate, 'DD/MM/YYYY') as createdDate1,"
					+ " nursingNote.encounterId as encounterId, nursingNote.admissionId as admissionId, "
					+ " nursingNote.organizationId AS organizationId, nursingNote.unitId AS unitId, "
					+ " nursingNote.status AS status, "
					+ " concat(coalesce(user.firstName),' ',coalesce(user.middleName),' ',coalesce(user.lastName)) as employeeName, "
					+ " concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName "
					+ " FROM NursingNote nursingNote"
					+ " LEFT JOIN nursingNote.userMaster userMst"
					+ " LEFT JOIN 	userMst.employeeMastersList user"
					+ " LEFT JOIN 	userMst.doctorMastersList doc"
					+ " WHERE  nursingNote.organizationId= :organizationId "
					+ " AND nursingNote.unitId= :unitId"
					),
	
})
@Entity
@Table(name = "t_nursing_note", schema = "opd")
@SequenceGenerator(name = "t_nursing_note_seq", sequenceName = "opd.t_nursing_note_seq", allocationSize = 1)
public class NursingNote {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_nursing_note_seq")
	
	@Column(name = "nursing_note_id")
	private Integer nursingNoteId;
	
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
	
	@Column(name = "nursing_note_desc")
	private String nursingNoteDesc;
	
	@Column(name = "template")
	private String template;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by", insertable = false, nullable = false, updatable = false)
	private UserMaster userMaster;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserRoleMapper> userMasterList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DoctorMaster> doctorMastersList;

	public Integer getNursingNoteId() {
		return nursingNoteId;
	}

	public void setNursingNoteId(Integer nursingNoteId) {
		this.nursingNoteId = nursingNoteId;
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

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
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

	public String getNursingNoteDesc() {
		return nursingNoteDesc;
	}

	public void setNursingNoteDesc(String nursingNoteDesc) {
		this.nursingNoteDesc = nursingNoteDesc;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	
}
