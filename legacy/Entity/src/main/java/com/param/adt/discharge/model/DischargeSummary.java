package com.param.adt.discharge.model;

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
	@NamedQuery(name="GET_DISCHARGE_SUMMARY_BY_PATIENTID_AND_TEMPLATEID_ID",
		query="SELECT dischargeSummary.dischargeSummaryId AS dischargeSummaryId,"
				+ " dischargeSummary.patientId AS patientId"
					+ " FROM DischargeSummary dischargeSummary"
					+ " WHERE dischargeSummary.dischargeTemplateId= :dischargeTemplateId"
					+ " AND dischargeSummary.admissionId= :admissionId"
					+ " AND dischargeSummary.unitId=:unitId"
					+ " AND dischargeSummary.organizationId=:organizationId "
					+ " AND dischargeSummary.status='A' "
					)
	})
@Entity
@Table(name = "m_discharge_Summary", schema = "adt")
@SequenceGenerator(name = "m_discharge_summary_seq", sequenceName = "adt.m_discharge_Summary_seq", allocationSize = 1)
public class DischargeSummary {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_discharge_summary_seq")
	@Column(name = "discharge_summary_id")
	private Integer dischargeSummaryId;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
	@Column(name = "department_id")
	private Integer departmentId;
	
	@Column(name = "discharge_template_id")
	private Integer dischargeTemplateId;
	
	@Column(name = "discharge_field_description")
	private String dischargeFieldDescription;
	
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
	
	@Column(name = "is_report_release")
	private Character isDischargeSummaryRelease;
	
	@Column(name = "discharge_pdf_path")
	private String dischargePdfPath;

	@Column(name = "doctor_id")
	private Integer doctorId;
	
	public Integer getDischargeSummaryId() {
		return dischargeSummaryId;
	}

	public void setDischargeSummaryId(Integer dischargeSummaryId) {
		this.dischargeSummaryId = dischargeSummaryId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getDischargeTemplateId() {
		return dischargeTemplateId;
	}

	public void setDischargeTemplateId(Integer dischargeTemplateId) {
		this.dischargeTemplateId = dischargeTemplateId;
	}

	public String getDischargeFieldDescription() {
		return dischargeFieldDescription;
	}

	public void setDischargeFieldDescription(String dischargeFieldDescription) {
		this.dischargeFieldDescription = dischargeFieldDescription;
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

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Character getIsDischargeSummaryRelease() {
		return isDischargeSummaryRelease;
	}

	public void setIsDischargeSummaryRelease(Character isDischargeSummaryRelease) {
		this.isDischargeSummaryRelease = isDischargeSummaryRelease;
	}

	public String getDischargePdfPath() {
		return dischargePdfPath;
	}

	public void setDischargePdfPath(String dischargePdfPath) {
		this.dischargePdfPath = dischargePdfPath;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
	
	
}
