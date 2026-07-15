package com.param.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedNativeQueries({ 
	
	@NamedNativeQuery(name="GET_TABULAR_TEMPLATE_DATA_PATIENT_BY_ID",
		query="  " + 
				"select template_transaction.template_id as  \"templateId\",   " + 
				"to_char(template_transaction.created_date, 'DD/MM/YYYY HH24:MI') as \"createdDate\" ,   " + 
				"template_transaction.title as \"title\",  " + 
				"template_transaction.patient_tabular_template_data_mapper_id  as \"patientTabularTemplateDataMapperId\",  " + 
				"template_transaction.tabular_template_id as \"tabularTemplateId\",  " + 
				"template_transaction.tabular_template_id as \"formTemplateId\",  " + 
				"template_transaction.patient_id as \"patientId\",  " + 
				"template_transaction.doctor_id as \"doctorId\",  " + 
				"template_transaction.encounter_id as \"encounterId\",  " + 
				"template_transaction.admission_id as \"admissionId\",  " + 
				"template_transaction.table_name as \"tableName\",  " + 
				"template_transaction.column_name as \"columnName\",  " + 
				"template_transaction.coulmn_id as \"coulmnId\",  " + 
				"template_transaction.row_id as \"rowId\",  " + 
				"template_transaction.row_name as \"rowName\",  " + 
				"template_transaction.input_value as \"inputValue\",  " + 
				"template_transaction.number_of_coulmn as \"numberOfCoulmn\",  " + 
				"template_transaction.number_of_row as \"numberOfRow\",  "+
				"tem_mst.template_type_id as \"templateTypeId\", " + 
				" tem_mst.template_format_id as \"templateFormatId\" ,  " + 
				" tem_mst.is_html_print as \"isHtmlPrint\",   " + 
				" (case   " + 
				" when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name)   " + 
				" when docUserMst.doctor_id is not null then concat('Dr. ', docUserMst.first_name, ' ', docUserMst.last_name)   " + 
				" else 'Admin' end) as \"userName\",   " + 
				"concat('Dr. ',coalesce(dctMst.first_name,''),' ',coalesce(dctMst.middle_name,''),' ',coalesce(dctMst.last_name,'')) as \"doctorName\"   " + 
				"from public.m_patient_tabular_template_data_mapper template_transaction   " + 
				"inner join public.t_template_master tem_mst on template_transaction.template_id = tem_mst.template_id   " + 
				"left join doctor.m_doctor_master dctMst on dctMst.doctor_id=template_transaction.doctor_id   " + 
				" left join public.m_user_master um on template_transaction.created_by = um.user_id   " + 
				" left join public.m_employee_master empmst on um.user_id = empmst.user_id   " + 
				" left join doctor.m_doctor_master docUserMst on um.user_id = docUserMst.user_id   " + 
				"where   " + 
				"template_transaction.organization_id =:organizationId   " + 
				"and template_transaction.unit_id =:unitId   " + 
				"and tem_mst.template_type_id =:templateTypeId   " + 
				"and template_transaction.patient_id =:patientId   " + 
				"and template_transaction.tabular_template_id =:tabularTemplateId   " + 
				" and template_transaction.status = 'A'"
					),
	
	})

@Entity
@Table(name = "m_patient_tabular_template_data_mapper", schema = "public")
@SequenceGenerator(name = "seq_m_patient_tabular_template_data_mapper", sequenceName = "public.seq_m_patient_tabular_template_data_mapper", allocationSize = 1)
public class PatientTabularTemplateDataMapper {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_patient_tabular_template_data_mapper")
	
	@Column(name = "patient_tabular_template_data_mapper_id")
	private Integer patientTabularTemplateDataMapperId;
	
	@Column(name = "template_id")
	private Integer templateId;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "doctor_id")
	private Integer doctorId;
	
	@Column(name = "tabular_template_id")
	private Integer tabularTemplateId;
	
	@Column(name = "encounter_id")
	private Integer encounterId;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
	@Column(name = "column_name")
	private String columnName;
	
	@Column(name = "table_name")
	private String tableName;
	
	@Column(name = "coulmn_id")
	private Integer coulmnId;
	
	@Column(name = "row_id")
	private Integer rowId;
	
	@Column(name = "row_name")
	private String rowName;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "input_value")
	private String inputValue;
	
	@Column(name = "number_of_coulmn")
	private Integer numberOfCoulmn;
	
	@Column(name = "number_of_row")
	private Integer numberOfRow;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
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
	
	@Column(name = "template_type_id")
	private Integer templateTypeId;

	public Integer getPatientTabularTemplateDataMapperId() {
		return patientTabularTemplateDataMapperId;
	}

	public void setPatientTabularTemplateDataMapperId(Integer patientTabularTemplateDataMapperId) {
		this.patientTabularTemplateDataMapperId = patientTabularTemplateDataMapperId;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getTabularTemplateId() {
		return tabularTemplateId;
	}

	public void setTabularTemplateId(Integer tabularTemplateId) {
		this.tabularTemplateId = tabularTemplateId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
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

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Integer getCoulmnId() {
		return coulmnId;
	}

	public void setCoulmnId(Integer coulmnId) {
		this.coulmnId = coulmnId;
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

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getTemplateTypeId() {
		return templateTypeId;
	}

	public void setTemplateTypeId(Integer templateTypeId) {
		this.templateTypeId = templateTypeId;
	}

	
	
}
