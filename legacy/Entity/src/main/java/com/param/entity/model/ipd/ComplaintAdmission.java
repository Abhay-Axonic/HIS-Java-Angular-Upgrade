package com.param.entity.model.ipd;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.UserMaster;
import com.param.opd.coversheet.model.DiagnosisMaster;
import com.param.opd.coversheet.model.Icpc;
import com.param.opd.coversheet.model.IcpcSystemMaster;
@NamedQueries({
	@NamedQuery(name="GET_COMPLAINT_ADMISSION",
				query=" SELECT comApp.complaintAdmissionId as complaintAdmissionId, comApp.admissionId as admissionId, comApp.diagnosisId as diagnosisId,"
						+ " comApp.resolvedRemark as resolvedRemark, to_char(comApp.resolvedDate, 'DD/MM/YYYY') as resolvedDate,"
						+ "(select max(tempObj.mIcpcId) from TemplateTransaction tempObj where tempObj.mIcpcId=comApp.diagnosisId "
						+ " and comApp.admissionId=tempObj.admissionId group by tempObj.mIcpcId ) as templateIcpcId, "
						+ "(select count(tempObj.mIcpcId) from TemplateTransaction tempObj where tempObj.mIcpcId=comApp.diagnosisId and tempObj.status = 'A'"
						+ " and comApp.admissionId=tempObj.admissionId group by tempObj.mIcpcId ) as complaintTempCount, "
						+ " (case " 
						+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) " 
						+ " when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) " 
						+ " else 'Admin' end) as userName,"
					+ " digMast.icpcDesc as diagnosisName, digMast.icpcCode as icpcCode, comApp.remark as remark, icpcSystemMaster.icpcSystemDesc as icpcSystemDesc, "
					+ " comApp.createdDate as createdDateNew, "
					+ " to_char(comApp.createdDate, 'dd/mm/yyyy') as createdDate, to_char(comApp.createdDate, 'DD/MM/YYYY HH24:MI') as createdDate2, comApp.status as status, comApp.since as since, comApp.duration as duration,comApp.roleId as roleId,"
					+ " digMast.icpcSystemId as icpcSystemId ,comApp.icpcCodeText as icpcCodeText, "
					+ " comApp.isReviewedFlag as isReviewedFlag "
					+ " FROM ComplaintAdmission comApp "
					+ " INNER JOIN comApp.diagnosisMaster digMast "
					+ " LEFT JOIN comApp.icpcSystemMaster icpcSystemMaster "
	                + " LEFT JOIN comApp.userMaster um " 
				    + " LEFT JOIN um.employeeMastersList empmst " 
				    + " LEFT JOIN um.doctorMastersList  docmst "
					+ " WHERE comApp.admissionId =:admissionId AND comApp.status = 'A'"
					+ " AND comApp.unitId =:unitId "
					+ " AND comApp.isComplaintStop ='N'"
					+ " AND comApp.organizationId =:organizationId "
					+ " AND comApp.isResolved ='N' "
					+ " ORDER BY icpcSystemMaster.icpcSystemDesc asc"
			),
			
			@NamedQuery(name="GET_COMPLAINT_ADMISSION_BY_ROLE_ID",
			query=" SELECT comApp.complaintAdmissionId as complaintAdmissionId,  comApp.admissionId as admissionId, comApp.diagnosisId as diagnosisId,"
				+ " digMast.icpcDesc as diagnosisName, digMast.icpcCode as icpcCode, comApp.remark as remark, icpcSystemMaster.icpcSystemDesc as icpcSystemDesc,"
				+ " to_char(comApp.createdDate, 'dd/mm/yyyy') as createdDate, comApp.status as status, comApp.since as since, comApp.duration as duration, comApp.roleId as roleId, comApp.icpcCodeText as icpcCodeText, "
				+ " digMast.icpcSystemId as icpcSystemId, "
				+ " comApp.isReviewedFlag as isReviewedFlag "
				+ " FROM ComplaintAdmission comApp "
				+ " INNER JOIN comApp.diagnosisMaster digMast "
				+ " LEFT JOIN comApp.icpcSystemMaster icpcSystemMaster "
				+ " WHERE comApp.admissionId =:admissionId AND comApp.status = 'A'"
				+ " AND comApp.unitId =:unitId "
				+ " AND comApp.organizationId =:organizationId "
				+ " AND comApp.roleId =:roleId "
				+ " AND comApp.isResolved ='N' "
				+ " ORDER BY comApp.complaintAdmissionId asc"
		),
		
		@NamedQuery(name="GET_COMPLAINT_ADMISSION_BY_COMPLAINT_ID",
		query=" SELECT comApp.complaintAdmissionId as complaintAdmissionId,  comApp.admissionId as admissionId, comApp.diagnosisId as diagnosisId,"
			+ " comApp.remark as remark,"
			+ " comApp.status as status, comApp.roleId as roleId, "
			+ " comApp.isReviewedFlag as isReviewedFlag "
			+ " FROM ComplaintAdmission comApp "
			+ " WHERE comApp.complaintAdmissionId =:complaintAdmissionId "
	)
})
@Entity
@Table(name = "t_complaint_admission", schema = "adt")
@SequenceGenerator(name = "complaint_admission_seq", sequenceName = "adt.complaint_admission_seq", allocationSize = 1)
public class ComplaintAdmission {
	@Id
	@Column(name = "complaint_admission_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "complaint_admission_seq")
	private Integer complaintAdmissionId;

	@Column(name = "admission_id")
	private Integer admissionId;

	@Column(name = "status")
	private Character status;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "diagnosis_id")
	private Integer diagnosisId;

	@Column(name = "remark")
	private String remark;

	@Column(name = "role_id")
	private Integer roleId;

	@Column(name = "since")
	private Integer since;
	
	@Column(name = "is_reviewed_flag")
	private Character isReviewedFlag;
	
	@Column(name="icpc_system_id")
	private Integer icpcSystemId;

//	@Column(name = "complaints_date_time")
//	private Date complaintsDateTime;

	@Column(name = "duration")
	private String duration;
	
	@Column(name="icpc_code_text")
	private String icpcCodeText;
	
	
	@Column(name="justification_complaint")
	private String justificationComplaint;
	
	@Column(name="is_complaint_stop")
	private Character isComplaintStop;
	
	@Column(name="patient_id")
	private Integer patientId;
	
	@Column(name="resolved_remark")
	private String resolvedRemark;
	
	@Column(name="is_resolved")
	private Character isResolved;
	
	@Column(name="resolved_date")
	private Date resolvedDate;
	
//	@OneToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "diagnosis_id", insertable = false, nullable = false, updatable = false)
//	private DiagnosisMaster diagnosisMaster;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "diagnosis_id", insertable = false, nullable = false, updatable = false)
	private Icpc diagnosisMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "icpc_system_id", insertable = false, nullable = false, updatable = false)
	private IcpcSystemMaster icpcSystemMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="created_by", insertable=false, updatable=false)
	private UserMaster userMaster;
	
	
	public Integer getComplaintAdmissionId() {
		return complaintAdmissionId;
	}

	public void setComplaintAdmissionId(Integer complaintAdmissionId) {
		this.complaintAdmissionId = complaintAdmissionId;
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

	public Integer getDiagnosisId() {
		return diagnosisId;
	}

	public void setDiagnosisId(Integer diagnosisId) {
		this.diagnosisId = diagnosisId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Character getIsReviewedFlag() {
		return isReviewedFlag;
	}

	public void setIsReviewedFlag(Character isReviewedFlag) {
		this.isReviewedFlag = isReviewedFlag;
	}

//	public Date getComplaintsDateTime() {
//		return complaintsDateTime;
//	}
//
//	public void setComplaintsDateTime(Date complaintsDateTime) {
//		this.complaintsDateTime = complaintsDateTime;
//	}

	public Integer getSince() {
		return since;
	}

	public void setSince(Integer since) {
		this.since = since;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public Icpc getDiagnosisMaster() {
		return diagnosisMaster;
	}

	public void setDiagnosisMaster(Icpc diagnosisMaster) {
		this.diagnosisMaster = diagnosisMaster;
	}

	public Integer getIcpcSystemId() {
		return icpcSystemId;
	}

	public void setIcpcSystemId(Integer icpcSystemId) {
		this.icpcSystemId = icpcSystemId;
	}

	public IcpcSystemMaster getIcpcSystemMaster() {
		return icpcSystemMaster;
	}

	public void setIcpcSystemMaster(IcpcSystemMaster icpcSystemMaster) {
		this.icpcSystemMaster = icpcSystemMaster;
	}

	public String getIcpcCodeText() {
		return icpcCodeText;
	}

	public void setIcpcCodeText(String icpcCodeText) {
		this.icpcCodeText = icpcCodeText;
	}

	public String getJustificationComplaint() {
		return justificationComplaint;
	}

	public void setJustificationComplaint(String justificationComplaint) {
		this.justificationComplaint = justificationComplaint;
	}

	public Character getIsComplaintStop() {
		return isComplaintStop;
	}

	public void setIsComplaintStop(Character isComplaintStop) {
		this.isComplaintStop = isComplaintStop;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getResolvedRemark() {
		return resolvedRemark;
	}

	public void setResolvedRemark(String resolvedRemark) {
		this.resolvedRemark = resolvedRemark;
	}

	public Character getIsResolved() {
		return isResolved;
	}

	public void setIsResolved(Character isResolved) {
		this.isResolved = isResolved;
	}

	public Date getResolvedDate() {
		return resolvedDate;
	}

	public void setResolvedDate(Date resolvedDate) {
		this.resolvedDate = resolvedDate;
	}

	
	
	

}
