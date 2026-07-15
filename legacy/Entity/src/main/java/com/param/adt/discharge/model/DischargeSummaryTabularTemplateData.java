package com.param.adt.discharge.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "m_discharge_summary_tabular_template_data ", schema = "adt")
@SequenceGenerator(name = "seq_m_discharge_summary_tabular_template_data", sequenceName = "adt.seq_m_discharge_summary_tabular_template_data", allocationSize = 1)
public class DischargeSummaryTabularTemplateData {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_discharge_summary_tabular_template_data")
	@Column(name = "discharge_summary_tabular_template_id")
	private Integer dischargeSummaryTabularTemplateId;
	
	@Column(name = "table_type_id")
	private Integer tableTypeId;
	
	@Column(name = "discharge_template_id")
	private Integer dischargeTemplateId;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "doctor_id")
	private Integer doctorId;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
	@Column(name = "column_name")
	private String columnName;

	@Column(name = "coulmn_id")
	private Integer coulmnId;
	
	@Column(name = "is_report_release")
	private Character isDischargeSummaryRelease;
	
	@Column(name = "row_id")
	private Integer rowId;
	
	@Column(name = "row_name")
	private String rowName;
	
	@Column(name = "input_value")
	private String inputValue;
	
	@Column(name = "table_name")
	private String tableName;
	
	@Column(name = "number_of_coulmn")
	private Integer numberOfCoulmn;
	
	@Column(name = "number_of_row")
	private Integer numberOfRow;
	
	@Column(name = "discharge_pdf_path")
	private String dischargePdfPath;
	
	@Column(name = "department_id")
	private Integer departmentId;
	
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
	
	@Column(name = "discharge_summary_id")
	private Integer dischargeSummaryId;
	
	@Column(name = "discharge_field_Id")
	private Integer dischargeFieldId;

	@Column(name = "discharge_summary_detail_id")
	private Integer dischargeSummaryDetailId;

	
	public Integer getDischargeSummaryTabularTemplateId() {
		return dischargeSummaryTabularTemplateId;
	}

	public void setDischargeSummaryTabularTemplateId(Integer dischargeSummaryTabularTemplateId) {
		this.dischargeSummaryTabularTemplateId = dischargeSummaryTabularTemplateId;
	}

	public Integer getTableTypeId() {
		return tableTypeId;
	}

	public void setTableTypeId(Integer tableTypeId) {
		this.tableTypeId = tableTypeId;
	}

	public Integer getDischargeTemplateId() {
		return dischargeTemplateId;
	}

	public void setDischargeTemplateId(Integer dischargeTemplateId) {
		this.dischargeTemplateId = dischargeTemplateId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Integer getCoulmnId() {
		return coulmnId;
	}

	public void setCoulmnId(Integer coulmnId) {
		this.coulmnId = coulmnId;
	}

	public Character getIsDischargeSummaryRelease() {
		return isDischargeSummaryRelease;
	}

	public void setIsDischargeSummaryRelease(Character isDischargeSummaryRelease) {
		this.isDischargeSummaryRelease = isDischargeSummaryRelease;
	}

	public Integer getRowId() {
		return rowId;
	}

	public void setRowId(Integer rowId) {
		this.rowId = rowId;
	}

	public String getRowName() {
		return rowName;
	}

	public void setRowName(String rowName) {
		this.rowName = rowName;
	}

	public String getInputValue() {
		return inputValue;
	}

	public void setInputValue(String inputValue) {
		this.inputValue = inputValue;
	}

	public Integer getNumberOfCoulmn() {
		return numberOfCoulmn;
	}

	public void setNumberOfCoulmn(Integer numberOfCoulmn) {
		this.numberOfCoulmn = numberOfCoulmn;
	}

	public Integer getNumberOfRow() {
		return numberOfRow;
	}

	public void setNumberOfRow(Integer numberOfRow) {
		this.numberOfRow = numberOfRow;
	}

	public String getDischargePdfPath() {
		return dischargePdfPath;
	}

	public void setDischargePdfPath(String dischargePdfPath) {
		this.dischargePdfPath = dischargePdfPath;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
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

	public Integer getDischargeSummaryId() {
		return dischargeSummaryId;
	}

	public void setDischargeSummaryId(Integer dischargeSummaryId) {
		this.dischargeSummaryId = dischargeSummaryId;
	}

	public Integer getDischargeFieldId() {
		return dischargeFieldId;
	}

	public void setDischargeFieldId(Integer dischargeFieldId) {
		this.dischargeFieldId = dischargeFieldId;
	}

	public Integer getDischargeSummaryDetailId() {
		return dischargeSummaryDetailId;
	}

	public void setDischargeSummaryDetailId(Integer dischargeSummaryDetailId) {
		this.dischargeSummaryDetailId = dischargeSummaryDetailId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	
}
