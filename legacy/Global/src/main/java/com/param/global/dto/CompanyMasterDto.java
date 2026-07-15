package com.param.global.dto;

import java.util.List;

public class CompanyMasterDto {
	
	private Integer companyId;
	
	private Integer contractId;
	
	private Integer serviceId;
	
	private String companyCode;
	
	private String companyDesc;
	
	private Integer companyTypeId;
	
	private Double creditLimit;
	
    private String creditDays;
	
	private String reportPath;
	
	private String contactPersonName;
	
	private String address;
	
	private Integer countryId;
	
	private Integer stateId;
	
	private Integer cityId;
	
	private Integer  areaId;
	
	private String zipcode;
	
	private String mobileNo;
	
	private String residencePhoneNo;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private String createdDate;
	
	private Integer createdBy;
	
	private Integer unitId;
	
	private Integer organizationId;
	
	private Character status;
	
	private String faxNo;
	
	private String emailId;
	
	private String startDate;
	
	private String endDate;
	
	private String paymentTerms;
	
	private Integer arCodeId;
	
	private String arCode;
	
	private Integer  districtId;
	
	private Integer offset;

	private Integer noOfRecordsPerPage;
	
	private String orderDate;
	
	private String contractNo;	
	
	private String countryName;	
	
	private String stateName;	
	
	private String cityName;
	
	private String areaName;
	
	private String districtName;
	
	private String gradeDesc;
	
	private Integer contractGradeMasterId;
	
	private Integer associateCompanyId;
	
	private Integer patientTypeId;
	
	private Integer billingBedCategoryId; 	
	
	private Integer visitTypeId;
	
	private Integer generalLedgerId;
	
    private String generalLedgerName;
	
	List<CompanyContarctGradeMapperDto> listCompanyContarctGradeMapperDto;
	List<CompanyContarctGradeMapperDto> addCompanyContarctGradeMapperDto;
	List<CompanyContarctGradeMapperDto> removedCompanyContarctGradeMapperDto;

	private String  aRCodeDesc;
	
	private String  contractDesc;
	
	private List<Integer> companyIds;
	
    private Character blockReportPrint;
	
	public String getContractDesc() {
		return contractDesc;
	}

	public void setContractDesc(String contractDesc) {
		this.contractDesc = contractDesc;
	}

	public String getArCode() {
		return arCode;
	}

	public void setArCode(String arCode) {
		this.arCode = arCode;
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

	public Integer getPatientTypeId() {
		return patientTypeId;
	}

	public void setPatientTypeId(Integer patientTypeId) {
		this.patientTypeId = patientTypeId;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public Integer getAssociateCompanyId() {
		return associateCompanyId;
	}

	public void setAssociateCompanyId(Integer associateCompanyId) {
		this.associateCompanyId = associateCompanyId;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getCompanyDesc() {
		return companyDesc;
	}

	public void setCompanyDesc(String companyDesc) {
		this.companyDesc = companyDesc;
	}

	public Integer getCompanyTypeId() {
		return companyTypeId;
	}

	public void setCompanyTypeId(Integer companyTypeId) {
		this.companyTypeId = companyTypeId;
	}

	public String getReportPath() {
		return reportPath;
	}

	public void setReportPath(String reportPath) {
		this.reportPath = reportPath;
	}

	public String getContactPersonName() {
		return contactPersonName;
	}

	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}


	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public Integer getStateId() {
		return stateId;
	}

	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}


	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getResidencePhoneNo() {
		return residencePhoneNo;
	}

	public void setResidencePhoneNo(String residencePhoneNo) {
		this.residencePhoneNo = residencePhoneNo;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}


	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Double getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(Double creditLimit) {
		this.creditLimit = creditLimit;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getFaxNo() {
		return faxNo;
	}

	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}


	public Integer getArCodeId() {
		return arCodeId;
	}

	public void setArCodeId(Integer arCodeId) {
		this.arCodeId = arCodeId;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public List<CompanyContarctGradeMapperDto> getListCompanyContarctGradeMapperDto() {
		return listCompanyContarctGradeMapperDto;
	}

	public void setListCompanyContarctGradeMapperDto(
			List<CompanyContarctGradeMapperDto> listCompanyContarctGradeMapperDto) {
		this.listCompanyContarctGradeMapperDto = listCompanyContarctGradeMapperDto;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getGradeDesc() {
		return gradeDesc;
	}

	public void setGradeDesc(String gradeDesc) {
		this.gradeDesc = gradeDesc;
	}

	public Integer getContractGradeMasterId() {
		return contractGradeMasterId;
	}

	public void setContractGradeMasterId(Integer contractGradeMasterId) {
		this.contractGradeMasterId = contractGradeMasterId;
	}

	public List<CompanyContarctGradeMapperDto> getAddCompanyContarctGradeMapperDto() {
		return addCompanyContarctGradeMapperDto;
	}

	public void setAddCompanyContarctGradeMapperDto(List<CompanyContarctGradeMapperDto> addCompanyContarctGradeMapperDto) {
		this.addCompanyContarctGradeMapperDto = addCompanyContarctGradeMapperDto;
	}

	public List<CompanyContarctGradeMapperDto> getRemovedCompanyContarctGradeMapperDto() {
		return removedCompanyContarctGradeMapperDto;
	}

	public void setRemovedCompanyContarctGradeMapperDto(
			List<CompanyContarctGradeMapperDto> removedCompanyContarctGradeMapperDto) {
		this.removedCompanyContarctGradeMapperDto = removedCompanyContarctGradeMapperDto;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public Integer getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
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

	public String getaRCodeDesc() {
		return aRCodeDesc;
	}

	public void setaRCodeDesc(String aRCodeDesc) {
		this.aRCodeDesc = aRCodeDesc;
	}

	public List<Integer> getCompanyIds() {
		return companyIds;
	}

	public void setCompanyIds(List<Integer> companyIds) {
		this.companyIds = companyIds;
	}

	public Character getBlockReportPrint() {
		return blockReportPrint;
	}

	public void setBlockReportPrint(Character blockReportPrint) {
		this.blockReportPrint = blockReportPrint;
	}
	
    public String getCreditDays() {
        return creditDays;
    }

    public void setCreditDays(String creditDays) {
        this.creditDays = creditDays;
    }

	public Integer getGeneralLedgerId() {
		return generalLedgerId;
	}

	public void setGeneralLedgerId(Integer generalLedgerId) {
		this.generalLedgerId = generalLedgerId;
	}

	public String getGeneralLedgerName() {
		return generalLedgerName;
	}

	public void setGeneralLedgerName(String generalLedgerName) {
		this.generalLedgerName = generalLedgerName;
	}

}
