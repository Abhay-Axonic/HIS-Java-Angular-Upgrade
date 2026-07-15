package com.param.opd.coversheet.model;

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

import com.param.adt.master.global.model.UserMaster;


@NamedQueries({
	
	@NamedQuery(name="GET_COVERSHEET_INSTRUCTION_LIST_BY_ENCOUNTER_ID",query="SELECT cover.instructionId as instructionId, "
			+ " cover.instructionDetailsId as instructionDetailsId, "
			+ " instr.instructionDesc as instructionDesc, cover.activityOrIntrcs as activityOrIntrcs, cover.isOtims as isOtims,"
			+ " cover.notes as notes, cover.advicedInstruction as advicedInstruction,"
			+ " to_char(cover.createdDate, 'DD/MM/YYYY') as createdDate, "
			+ " to_char(cover.createdDate, 'DD/MM/YYYY HH24:MI') as createdDate1, "
			+ " (case "
			+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) "
			+ " when doc.doctorId is not null then concat('Dr. ', doc.firstName, ' ', doc.lastName) "
			+ " else 'Admin' end) as userName,"
			+ " concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName "
			+ " FROM CoversheetInstruction cover "
			+ " LEFT JOIN cover.instructionMaster instr "
			+ " LEFT JOIN cover.userMaster userMaster "
			+ " LEFT JOIN userMaster.doctorMastersList doc "
			+ " LEFT JOIN userMaster.employeeMastersList empmst "
			),
	
	@NamedQuery(name="GET_COVERSHEET_INSTRUCTION_FOR_BOOK_VIEW",query="SELECT cover.instructionId as instructionId, "
			+ " cover.instructionDetailsId as instructionDetailsId, cover.status as status,"
			+ " instr.instructionDesc as instructionDesc, "
			+ " cover.notes as notes, cover.advicedInstruction as advicedInstruction,"
			+ " concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName "
			+ " FROM CoversheetInstruction cover "
			+ " LEFT JOIN cover.instructionMaster instr "
			+ " LEFT JOIN cover.userMaster userMaster "
			+ " LEFT JOIN userMaster.doctorMastersList doc "
			+ " Where cover.patientId=:patientId "
			+ " AND to_char(cover.createdDate , 'YYYY-MM-DD') BETWEEN :fromDate " 
			+ " AND :toDate "
			)
	
})
@Entity
@Table(name="t_instruction",schema="opd")
@SequenceGenerator(sequenceName="opd.t_instruction_seq",name="t_instruction_seq",allocationSize=1)
public class CoversheetInstruction {

	@Id
	@Column(name="instruction_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="t_instruction_seq")
	private Integer instructionId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="instruction_details_id")
	private Integer instructionDetailsId;
	
	@Column(name="notes")
	private String notes;
	
	@Column(name="patient_id")
	private Integer patientId;
	
	@Column(name="encounter_id")
	private Integer encounterId;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="status")
	private Character status;
	
	@Column(name="admission_id")
	private Integer admissionId;

	@Column(name="adviced_instruction")
	private String advicedInstruction;
	
	@Column(name="is_otims")
	private Boolean isOtims;
	
	@Column(name="cov_template_id")
	private Integer covTemplateId;
	
	
	@Column(name="activity_or_intrcs")
	private Integer activityOrIntrcs;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "instruction_details_id", insertable = false, updatable = false)
	private InstructionMaster instructionMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by", insertable = false, nullable = false, updatable = false)
	private UserMaster userMaster;
	
	public Integer getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(Integer instructionId) {
		this.instructionId = instructionId;
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

	public Integer getInstructionDetailsId() {
		return instructionDetailsId;
	}

	public void setInstructionDetailsId(Integer instructionDetailsId) {
		this.instructionDetailsId = instructionDetailsId;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public InstructionMaster getInstructionMaster() {
		return instructionMaster;
	}

	public void setInstructionMaster(InstructionMaster instructionMaster) {
		this.instructionMaster = instructionMaster;
	}

	public String getAdvicedInstruction() {
		return advicedInstruction;
	}

	public void setAdvicedInstruction(String advicedInstruction) {
		this.advicedInstruction = advicedInstruction;
	}

	public Boolean getIsOtims() {
		return isOtims;
	}

	public void setIsOtims(Boolean isOtims) {
		this.isOtims = isOtims;
	}

	public Integer getActivityOrIntrcs() {
		return activityOrIntrcs;
	}

	public void setActivityOrIntrcs(Integer activityOrIntrcs) {
		this.activityOrIntrcs = activityOrIntrcs;
	}

	public Integer getCovTemplateId() {
		return covTemplateId;
	}

	public void setCovTemplateId(Integer covTemplateId) {
		this.covTemplateId = covTemplateId;
	}
	
}
