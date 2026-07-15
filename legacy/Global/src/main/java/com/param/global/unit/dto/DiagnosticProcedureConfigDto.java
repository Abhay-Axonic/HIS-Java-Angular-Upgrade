package com.param.global.unit.dto;

public class DiagnosticProcedureConfigDto {
	
	    private Integer diagnosticProcedureConfigId;
		
		private Integer specialityId;
		
		private Integer procedureServiceId;
		
		private String cptCode; 
		
		private String cptDescription; 
		
		private Integer procedureDuration; 
		
		private Character isPACSRequired; 
		
		private String patientInstruction;
		
	    private Integer createdBy;
		
		private String createdDate;
		
		private Character status;
		
		private Integer updatedBy;
		
		private String updatedDate;
		
		private Integer unitId;

		private Integer organizationId;
		
		private Integer noOfRecordsPerPage;
		
		private Integer  offset;
		
		private String specialityName;
		
		private String serviceName;
		

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

		public String getCreatedDate() {
			return createdDate;
		}

		public void setCreatedDate(String createdDate) {
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

		public String getUpdatedDate() {
			return updatedDate;
		}

		public void setUpdatedDate(String updatedDate) {
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

		public Integer getNoOfRecordsPerPage() {
			return noOfRecordsPerPage;
		}

		public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
			this.noOfRecordsPerPage = noOfRecordsPerPage;
		}

		public Integer getOffset() {
			return offset;
		}

		public void setOffset(Integer offset) {
			this.offset = offset;
		}

		public String getSpecialityName() {
			return specialityName;
		}

		public void setSpecialityName(String specialityName) {
			this.specialityName = specialityName;
		}

		public Integer getProcedureDuration() {
			return procedureDuration;
		}

		public void setProcedureDuration(Integer procedureDuration) {
			this.procedureDuration = procedureDuration;
		}

		public String getServiceName() {
			return serviceName;
		}

		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}
}
