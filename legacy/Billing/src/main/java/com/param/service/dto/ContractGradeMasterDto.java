package com.param.service.dto;




public class ContractGradeMasterDto {
	
		private Integer contractGradeMasterId;
	
		private String code;
	
		private String gradeDesc;
	
		private char status;
	
		private String createdDate;
	
		private Integer createdBy;
	
		private String updatedDate;
	
		private Integer updatedBy;
	
		private Integer orgId;
	
		private Integer unitId;

		private Integer offset;
		
		private Integer noOfRecordsPerPage;
		

		public Integer getContractGradeMasterId() {
			return contractGradeMasterId;
		}

		public void setContractGradeMasterId(Integer contractGradeMasterId) {
			this.contractGradeMasterId = contractGradeMasterId;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getGradeDesc() {
			return gradeDesc;
		}

		public void setGradeDesc(String gradeDesc) {
			this.gradeDesc = gradeDesc;
		}

		public char getStatus() {
			return status;
		}

		public void setStatus(char status) {
			this.status = status;
		}

		public String getCreatedDate() {
			return createdDate;
		}

		public void setCreatedDate(String createdDate) {
			this.createdDate = createdDate;
		}

		public Integer getCreatedBy() {
			return createdBy;
		}

		public void setCreatedBy(Integer createdBy) {
			this.createdBy = createdBy;
		}

		public String getUpdatedDate() {
			return updatedDate;
		}

		public void setUpdatedDate(String updatedDate) {
			this.updatedDate = updatedDate;
		}

		public Integer getUpdatedBy() {
			return updatedBy;
		}

		public void setUpdatedBy(Integer updatedBy) {
			this.updatedBy = updatedBy;
		}

		public Integer getOrgId() {
			return orgId;
		}

		public void setOrgId(Integer orgId) {
			this.orgId = orgId;
		}

		public Integer getUnitId() {
			return unitId;
		}

		public void setUnitId(Integer unitId) {
			this.unitId = unitId;
		}

		public Integer getOffset() {
			return offset;
		}

		public void setOffset(Integer offset) {
			this.offset = offset;
		}

		public Integer getNoOfRecordsPerPage() {
			return noOfRecordsPerPage;
		}

		public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
			this.noOfRecordsPerPage = noOfRecordsPerPage;
		}
		
}
