package com.param.global.dto;

import java.util.List;

public class DoctorConsultationServiceMapperDto {

	private Integer doctorConsultationServiceMapperId;
	
	private Integer unitId;
	
	private Integer organizationId;
	
	private Integer doctorId;
	
	private String doctorName;
	
	private Integer newVisitServiceId;
	
	private String newVisitServiceName;
	
	private Integer followupVisitServiceId;
	
	private String followupVisitServiceName;
	
	private Integer secondaryVisitServiceId;
	
	private String secondaryVisitServiceName;
	
	private char status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer specialityId;
	
	private String specialityName;
	
	private Integer visitType;
	
	private Character deptWiseAppointment;
	
	private Integer paymentEntitlementId;
	
	private String paymentEntitlement;
	
	private Double newVisitRate;
	
	private Double followupVisitRate;
	
	private Double secondaryVisitRate;
	
	private Character newVisitDoctorShare;
	
	private Character followUpVisitDoctorShare;
	
	private Character secondaryVisitDoctorShare;
	
	private Character newVisitReferralShare;
	
	private Character followUpVisitReferralShare;
	
	private Character secondaryVisitReferralShare;
	
	private String newVisitSpecialityName;
	
	private String followupVisitSpecialityName;
	
	private String secondarySpecialityName;
	
	private Integer noOfRecordsPerPage;
	
	private Integer offset;
	
	private Integer tabNumber;
	
	private Integer companyId;
	
	private Integer assoCompanyId;
	
	private String companyName;
	
	private String insuranceCompanyName;
	
	private Integer type;
	
	
	
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getInsuranceCompanyName() {
		return insuranceCompanyName;
	}

	public void setInsuranceCompanyName(String insuranceCompanyName) {
		this.insuranceCompanyName = insuranceCompanyName;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getAssoCompanyId() {
		return assoCompanyId;
	}

	public void setAssoCompanyId(Integer assoCompanyId) {
		this.assoCompanyId = assoCompanyId;
	}

	public Character getNewVisitReferralShare() {
		return newVisitReferralShare;
	}

	public void setNewVisitReferralShare(Character newVisitReferralShare) {
		this.newVisitReferralShare = newVisitReferralShare;
	}

	public Character getFollowUpVisitReferralShare() {
		return followUpVisitReferralShare;
	}

	public void setFollowUpVisitReferralShare(Character followUpVisitReferralShare) {
		this.followUpVisitReferralShare = followUpVisitReferralShare;
	}

	public Character getSecondaryVisitReferralShare() {
		return secondaryVisitReferralShare;
	}

	public void setSecondaryVisitReferralShare(Character secondaryVisitReferralShare) {
		this.secondaryVisitReferralShare = secondaryVisitReferralShare;
	}

	private List<DoctorConsultationServiceMapperDto> docConsultationMapperList;
	
	public String getNewVisitSpecialityName() {
		return newVisitSpecialityName;
	}

	public void setNewVisitSpecialityName(String newVisitSpecialityName) {
		this.newVisitSpecialityName = newVisitSpecialityName;
	}

	public String getFollowupVisitSpecialityName() {
		return followupVisitSpecialityName;
	}

	public void setFollowupVisitSpecialityName(String followupVisitSpecialityName) {
		this.followupVisitSpecialityName = followupVisitSpecialityName;
	}

	public String getSecondarySpecialityName() {
		return secondarySpecialityName;
	}

	public void setSecondarySpecialityName(String secondarySpecialityName) {
		this.secondarySpecialityName = secondarySpecialityName;
	}

	public Character getNewVisitDoctorShare() {
		return newVisitDoctorShare;
	}

	public void setNewVisitDoctorShare(Character newVisitDoctorShare) {
		this.newVisitDoctorShare = newVisitDoctorShare;
	}

	public Character getFollowUpVisitDoctorShare() {
		return followUpVisitDoctorShare;
	}

	public void setFollowUpVisitDoctorShare(Character followUpVisitDoctorShare) {
		this.followUpVisitDoctorShare = followUpVisitDoctorShare;
	}

	public Character getSecondaryVisitDoctorShare() {
		return secondaryVisitDoctorShare;
	}

	public void setSecondaryVisitDoctorShare(Character secondaryVisitDoctorShare) {
		this.secondaryVisitDoctorShare = secondaryVisitDoctorShare;
	}

	public Character getDeptWiseAppointment() {
		return deptWiseAppointment;
	}

	public void setDeptWiseAppointment(Character deptWiseAppointment) {
		this.deptWiseAppointment = deptWiseAppointment;
	}

	public Integer getVisitType() {
		return visitType;
	}

	public void setVisitType(Integer visitType) {
		this.visitType = visitType;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getNewVisitServiceName() {
		return newVisitServiceName;
	}

	public void setNewVisitServiceName(String newVisitServiceName) {
		this.newVisitServiceName = newVisitServiceName;
	}

	public String getFollowupVisitServiceName() {
		return followupVisitServiceName;
	}

	public void setFollowupVisitServiceName(String followupVisitServiceName) {
		this.followupVisitServiceName = followupVisitServiceName;
	}

	public String getSecondaryVisitServiceName() {
		return secondaryVisitServiceName;
	}

	public void setSecondaryVisitServiceName(String secondaryVisitServiceName) {
		this.secondaryVisitServiceName = secondaryVisitServiceName;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public Integer getDoctorConsultationServiceMapperId() {
		return doctorConsultationServiceMapperId;
	}

	public void setDoctorConsultationServiceMapperId(Integer doctorConsultationServiceMapperId) {
		this.doctorConsultationServiceMapperId = doctorConsultationServiceMapperId;
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

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getNewVisitServiceId() {
		return newVisitServiceId;
	}

	public void setNewVisitServiceId(Integer newVisitServiceId) {
		this.newVisitServiceId = newVisitServiceId;
	}

	public Integer getFollowupVisitServiceId() {
		return followupVisitServiceId;
	}

	public void setFollowupVisitServiceId(Integer followupVisitServiceId) {
		this.followupVisitServiceId = followupVisitServiceId;
	}

	public Integer getSecondaryVisitServiceId() {
		return secondaryVisitServiceId;
	}

	public void setSecondaryVisitServiceId(Integer secondaryVisitServiceId) {
		this.secondaryVisitServiceId = secondaryVisitServiceId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
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

	public List<DoctorConsultationServiceMapperDto> getDocConsultationMapperList() {
		return docConsultationMapperList;
	}

	public void setDocConsultationMapperList(List<DoctorConsultationServiceMapperDto> docConsultationMapperList) {
		this.docConsultationMapperList = docConsultationMapperList;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public Double getNewVisitRate() {
		return newVisitRate;
	}

	public void setNewVisitRate(Double newVisitRate) {
		this.newVisitRate = newVisitRate;
	}

	public Double getFollowupVisitRate() {
		return followupVisitRate;
	}

	public void setFollowupVisitRate(Double followupVisitRate) {
		this.followupVisitRate = followupVisitRate;
	}

	public Double getSecondaryVisitRate() {
		return secondaryVisitRate;
	}

	public void setSecondaryVisitRate(Double secondaryVisitRate) {
		this.secondaryVisitRate = secondaryVisitRate;
	}

	public String getPaymentEntitlement() {
		return paymentEntitlement;
	}

	public void setPaymentEntitlement(String paymentEntitlement) {
		this.paymentEntitlement = paymentEntitlement;
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

	public Integer getTabNumber() {
		return tabNumber;
	}

	public void setTabNumber(Integer tabNumber) {
		this.tabNumber = tabNumber;
	}
	
	
}
