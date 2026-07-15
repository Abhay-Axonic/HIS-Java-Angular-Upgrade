package com.param.adt.master.unit.model;

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

import com.param.global.model.ServiceMaster;
import com.param.global.model.SpecialityMaster;

@NamedQueries({
	@NamedQuery(name="GET_DIAGNOSTIC_PROCEDURE_CONFIG_MASTER_BY_ID",
			query="SELECT procConfig.diagnosticProcedureConfigId AS diagnosticProcedureConfigId,"
			+ " procConfig.specialityId AS specialityId,"
			+ " specialityMaster.specialityName as specialityName,"
			+ " procConfig.procedureServiceId AS procedureServiceId,"
			+ " procConfig.cptCode AS cptCode,"
			+ " servMaster.serviceStandardName AS serviceName,"
			+ " procConfig.cptDescription AS cptDescription,"
			+ " procConfig.procedureDuration AS procedureDuration,"
			+ " procConfig.patientInstruction AS patientInstruction,"
			+ " procConfig.status AS status"
			+ " FROM DiagnosticProcedureConfig procConfig "
			+ " LEFT JOIN procConfig.specialityMaster specialityMaster"
			+ " LEFT JOIN procConfig.serviceMaster servMaster"
			+ " WHERE procConfig.organizationId=:orgId "
			+ " AND procConfig.unitId=:unitId "
			+ " AND procConfig.diagnosticProcedureConfigId=:id "),
	
	@NamedQuery(name="GET_DIAGNOSTIC_PROCEDURE_CONFIG_MASTER_LIST",
	query="SELECT procConfig.diagnosticProcedureConfigId AS diagnosticProcedureConfigId,"
			+ " procConfig.specialityId AS specialityId,"
			+ " specialityMaster.specialityName as specialityName,"
			+ " servMaster.serviceStandardName AS serviceName,"
			+ " procConfig.procedureServiceId AS procedureServiceId,"
			+ " procConfig.cptCode AS cptCode,"
			+ " procConfig.cptDescription AS cptDescription,"
			+ " procConfig.procedureDuration AS procedureDuration,"
			+ " procConfig.patientInstruction AS patientInstruction,"
			+ " procConfig.status AS status"
			+ " FROM DiagnosticProcedureConfig procConfig "
			+ " LEFT JOIN procConfig.specialityMaster specialityMaster"
			+ " LEFT JOIN procConfig.serviceMaster servMaster"
			+ " WHERE procConfig.organizationId=:orgId "
			+ " AND procConfig.unitId=:unitId "),
	
	
	@NamedQuery(name="GET_ACTIVE_DIAGNOSTIC_PROCEDURE_CONFIG_MASTER_LIST",
	query="SELECT procConfig.diagnosticProcedureConfigId AS diagnosticProcedureConfigId,"
			+ " procConfig.specialityId AS specialityId,"
			+ " specialityMaster.specialityName as specialityName,"
			+ " servMaster.serviceStandardName AS serviceName,"
			+ " procConfig.procedureServiceId AS procedureServiceId,"
			+ " procConfig.cptCode AS cptCode,"
			+ " procConfig.cptDescription AS cptDescription,"
			+ " procConfig.procedureDuration AS procedureDuration,"
			+ " procConfig.patientInstruction AS patientInstruction,"
			+ " procConfig.status AS status"
			+ " FROM DiagnosticProcedureConfig procConfig "
			+ " LEFT JOIN procConfig.specialityMaster specialityMaster"
			+ " LEFT JOIN procConfig.serviceMaster servMaster"
			+ " WHERE procConfig.organizationId=:orgId "
			+ " AND procConfig.unitId=:unitId "
			+ " AND procConfig.status='A' "),
	
	
	
	@NamedQuery(name="GET_DIAGNOSTIC_PROCEDURE_CONFIG_MASTER_BY_NAME_NOT_ID",
	query="SELECT procConfig.diagnosticProcedureConfigId AS diagnosticProcedureConfigId,"
			+ " procConfig.specialityId AS specialityId,"
			+ " procConfig.procedureServiceId AS procedureServiceId,"
			+ " procConfig.cptCode AS cptCode,"
			+ " procConfig.cptDescription AS cptDescription,"
			+ " procConfig.procedureDuration AS procedureDuration,"
			+ " procConfig.patientInstruction AS patientInstruction,"
			+ " procConfig.status AS status"
			+ " FROM DiagnosticProcedureConfig procConfig "
			+ " WHERE procConfig.organizationId=:orgId "
			+ " AND procConfig.unitId=:unitId "
			+ " AND procConfig.procedureServiceId=:procedureServiceId"
			+ " AND procConfig.specialityId=:specId"
			+ " AND procConfig.diagnosticProcedureConfigId!=:diagnosticProcedureConfigId"),
	
	@NamedQuery(name="GET_DIAGNOSTIC_PROCEDURE_CONFIG_MASTER_BY_SERVICE_ID",
	query="SELECT procConfig.diagnosticProcedureConfigId AS diagnosticProcedureConfigId,"
			+ " procConfig.specialityId AS specialityId,"
			+ " procConfig.procedureServiceId AS procedureServiceId,"
			+ " procConfig.cptCode AS cptCode,"
			+ " procConfig.cptDescription AS cptDescription,"
			+ " procConfig.procedureDuration AS procedureDuration,"
			+ " procConfig.patientInstruction AS patientInstruction,"
			+ " procConfig.status AS status"
			+ " FROM DiagnosticProcedureConfig procConfig "
			+ " WHERE procConfig.organizationId=:orgId "
			+ " AND procConfig.unitId=:unitId AND procConfig.specialityId=:specId "
			+ " AND procConfig.procedureServiceId=:procedureServiceId")
	
	
})

@Entity
@Table(name = "m_diagnostic_nonsurgical_procedure_config", schema = "otims")
@SequenceGenerator(name = "m_diagnostic_nonsurgical_procedure_config_seq", sequenceName = "otims.m_diagnostic_nonsurgical_procedure_config_seq", allocationSize = 1)
public class DiagnosticProcedureConfig {

		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_diagnostic_nonsurgical_procedure_config_seq")
		@Column(name="diagnostic_procedure_config_id")
	    private Integer diagnosticProcedureConfigId;
		
		@Column(name="speciality_id")
		private Integer specialityId;
		
		@Column(name="procedure_service_id")
		private Integer procedureServiceId;
		
		@Column(name="cpt_code")
		private String cptCode; 
		
		@Column(name="cpt_description")
		private String cptDescription; 
		
		@Column(name="procedure_duration")
		private Integer procedureDuration; 
		
		@Column(name="is_pacs_required")
		private Character isPACSRequired; 
		
		@Column(name="patient_instruction")
		private String patientInstruction;
		
		@Column(name="created_by")
	    private Integer createdBy;
		
		@Column(name="created_date")
		private Date createdDate;
		
		@Column(name="status")
		private Character status;
		
		@Column(name="updated_by")
		private Integer updatedBy;
		
		@Column(name="updated_date")
		private Date updatedDate;
		
		@Column(name="unit_id")
		private Integer unitId;

		@Column(name="organization_id")
		private Integer organizationId;
		
		@ManyToOne(fetch= FetchType.LAZY)
		@JoinColumn(name="speciality_id", insertable=false, updatable=false)
		private SpecialityMaster specialityMaster;
		
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "procedure_service_id", insertable = false, updatable = false, nullable = false)
		private ServiceMaster serviceMaster;
		
		public Integer getDiagnosticProcedureConfigId() {
			return diagnosticProcedureConfigId;
		}

		public void setDiagnosticProcedureConfigId(Integer diagnosticProcedureConfigId) {
			this.diagnosticProcedureConfigId = diagnosticProcedureConfigId;
		}

		public Integer getSpecialityId() {
			return specialityId;
		}

		public void setSpecialityId(Integer specialityId) {
			this.specialityId = specialityId;
		}

		public Integer getProcedureServiceId() {
			return procedureServiceId;
		}

		public void setProcedureServiceId(Integer procedureServiceId) {
			this.procedureServiceId = procedureServiceId;
		}

		public String getCptCode() {
			return cptCode;
		}

		public void setCptCode(String cptCode) {
			this.cptCode = cptCode;
		}

		public String getCptDescription() {
			return cptDescription;
		}

		public void setCptDescription(String cptDescription) {
			this.cptDescription = cptDescription;
		}

		public Integer getProcedureDuration() {
			return procedureDuration;
		}

		public void setProcedureDuration(Integer procedureDuration) {
			this.procedureDuration = procedureDuration;
		}

		public Character getIsPACSRequired() {
			return isPACSRequired;
		}

		public void setIsPACSRequired(Character isPACSRequired) {
			this.isPACSRequired = isPACSRequired;
		}

		public String getPatientInstruction() {
			return patientInstruction;
		}

		public void setPatientInstruction(String patientInstruction) {
			this.patientInstruction = patientInstruction;
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

}
