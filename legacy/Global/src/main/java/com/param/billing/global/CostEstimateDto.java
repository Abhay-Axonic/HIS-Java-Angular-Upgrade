package com.param.billing.global;

import java.util.List;

public class CostEstimateDto {

		private Integer costEstimateId;
		
		private Integer patientId;
		
		private Integer billingBedCategoryId;
		
		private Integer visitTypeId;
		
		private Integer patietCategoryId;
		
		private Integer unitId;
		
		private Integer orgId;
		
		private String doa;
		
		private String pdd;
		
		private Double totalAmount;
		
		private Double selfPayable;
		
		private Double creditPayable;
		
		private Double discountAmount;
		
		private Integer createdBy;
		
		private Character status;
		
		private List<CostEstimationDetailsDto> costEstimationDetailsDtosList;
		
		private String remark;
		
		private String patientName;
		
		private String mail;
		
		private Integer patientGender;
		
		private Integer registrationTypeId;
		
		private String mobileNumber;
		
		private String patientAddress;
		
		private String uhidNumber;
		
		private Integer prefixId;
		
		private Integer updatedBy;
		private String newWhatsAppNo;

		private Boolean isManuallySend;

		/*public List<CostEstimationDetailsDto> getCostEstimationDetailsDtosList() {
			return CostEstimationDetailsDtosList;
		}

		public void setCostEstimationDetailsDtosList(List<CostEstimationDetailsDto> costEstimationDetailsDtosList) {
			CostEstimationDetailsDtosList = costEstimationDetailsDtosList;
		}*/

		public List<CostEstimationDetailsDto> getCostEstimationDetailsDtosList() {
			return costEstimationDetailsDtosList;
		}

		public void setCostEstimationDetailsDtosList(List<CostEstimationDetailsDto> costEstimationDetailsDtosList) {
			this.costEstimationDetailsDtosList = costEstimationDetailsDtosList;
		}

		public Integer getCostEstimateId() {
			return costEstimateId;
		}

		public void setCostEstimateId(Integer costEstimateId) {
			this.costEstimateId = costEstimateId;
		}

		public Integer getPatientId() {
			return patientId;
		}

		public void setPatientId(Integer patientId) {
			this.patientId = patientId;
		}

		public Integer getBillingBedCategoryId() {
			return billingBedCategoryId;
		}

		public void setBillingBedCategoryId(Integer billingBedCategoryId) {
			this.billingBedCategoryId = billingBedCategoryId;
		}

		public Integer getVisitTypeId() {
			return visitTypeId;
		}

		public void setVisitTypeId(Integer visitTypeId) {
			this.visitTypeId = visitTypeId;
		}

		public Integer getPatietCategoryId() {
			return patietCategoryId;
		}

		public void setPatietCategoryId(Integer patietCategoryId) {
			this.patietCategoryId = patietCategoryId;
		}

		public Integer getUnitId() {
			return unitId;
		}

		public void setUnitId(Integer unitId) {
			this.unitId = unitId;
		}

		public Integer getOrgId() {
			return orgId;
		}

		public void setOrgId(Integer orgId) {
			this.orgId = orgId;
		}


		public Double getTotalAmount() {
			return totalAmount;
		}

		public void setTotalAmount(Double totalAmount) {
			this.totalAmount = totalAmount;
		}

		public Double getSelfPayable() {
			return selfPayable;
		}

		public void setSelfPayable(Double selfPayable) {
			this.selfPayable = selfPayable;
		}

		public Double getCreditPayable() {
			return creditPayable;
		}

		public void setCreditPayable(Double creditPayable) {
			this.creditPayable = creditPayable;
		}

		public Integer getCreatedBy() {
			return createdBy;
		}

		public void setCreatedBy(Integer createdBy) {
			this.createdBy = createdBy;
		}

		public Character getStatus() {
			return status;
		}

		public void setStatus(Character status) {
			this.status = status;
		}

		public String getDoa() {
			return doa;
		}

		public void setDoa(String doa) {
			this.doa = doa;
		}

		public String getPdd() {
			return pdd;
		}

		public void setPdd(String pdd) {
			this.pdd = pdd;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public Double getDiscountAmount() {
			return discountAmount;
		}

		public void setDiscountAmount(Double discountAmount) {
			this.discountAmount = discountAmount;
		}

		public String getPatientName() {
			return patientName;
		}

		public void setPatientName(String patientName) {
			this.patientName = patientName;
		}

		public String getMail() {
			return mail;
		}

		public void setMail(String mail) {
			this.mail = mail;
		}

		public Integer getPatientGender() {
			return patientGender;
		}

		public void setPatientGender(Integer patientGender) {
			this.patientGender = patientGender;
		}

		public Integer getRegistrationTypeId() {
			return registrationTypeId;
		}

		public void setRegistrationTypeId(Integer registrationTypeId) {
			this.registrationTypeId = registrationTypeId;
		}

		public String getMobileNumber() {
			return mobileNumber;
		}

		public void setMobileNumber(String mobileNumber) {
			this.mobileNumber = mobileNumber;
		}

		public String getPatientAddress() {
			return patientAddress;
		}

		public void setPatientAddress(String patientAddress) {
			this.patientAddress = patientAddress;
		}

		public String getUhidNumber() {
			return uhidNumber;
		}

		public void setUhidNumber(String uhidNumber) {
			this.uhidNumber = uhidNumber;
		}

		public Integer getPrefixId() {
			return prefixId;
		}

		public void setPrefixId(Integer prefixId) {
			this.prefixId = prefixId;
		}

		public Integer getUpdatedBy() {
			return updatedBy;
		}

		public void setUpdatedBy(Integer updatedBy) {
			this.updatedBy = updatedBy;
		}

	public String getNewWhatsAppNo() {
		return newWhatsAppNo;
	}

	public void setNewWhatsAppNo(String newWhatsAppNo) {
		this.newWhatsAppNo = newWhatsAppNo;
	}

	public Boolean getIsManuallySend() {
		return isManuallySend;
	}

	public void setIsManuallySend(Boolean isManuallySend) {
		this.isManuallySend = isManuallySend;
	}
}
