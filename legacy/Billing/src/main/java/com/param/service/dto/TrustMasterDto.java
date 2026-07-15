package com.param.service.dto;

import java.util.Date;
import java.util.List;

import com.param.global.dto.global.VisitType;

public class TrustMasterDto {


	private Integer trustId;
	
	private String trustName;
	
	private String trustAddress;

	private Integer countryId;
	
	private Integer stateId;
	
	private Integer districtId;
	
	private String districtName;
	
	private String startDate;
	

	private Integer cityId;

	private Integer areaId;
	
	private String zipCode ;

	private String contactPersonName;
	
	private String  contactPersonEmail;
	
	private String  phoneNo;
	
	private String  cellNo;
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
	private Date validFrom;
	
	private Integer patientCategoryId;
	
	private List<VisitType> visitTypeId;
	
	private String  disease;
	
	private String maxLimitPerPerson;
	
	private Date createdDate;
	
	private Date updatedDate;
	
	private Integer createdBy;
	
	private Integer updatedBy;
	
	private char trustStatus;
	
    private Integer unitId;
	
	private Integer orgId;
	
	private Character isCompanyContract;
	
	private String patientCategory;
	
	private Integer  patientTypeId;
	
	private Integer id;
	
	private  String visitTypeCode;
	
	private Integer trustCompanyId;
	
   private Integer offset;
 
   private Integer noOfRecordsPerPage;
   
   
   private Integer companyTypeId;
   
   
   private String countryName;
   
   private String stateName;
   
   private String  cityName;
   
   private String areaName;
   
   
   
   
   
   
  

   private List<TrustMasterDto> listtrustMasterDto;

	public String getPatientCategory() {
		return patientCategory;
	}

	public void setPatientCategory(String patientCategory) {
		this.patientCategory = patientCategory;
	}

	public Integer getTrustId() {
		return trustId;
	}

	public void setTrustId(Integer trustId) {
		this.trustId = trustId;
	}

	public String getTrustName() {
		return trustName;
	}

	public void setTrustName(String trustName) {
		this.trustName = trustName;
	}

	public String getTrustAddress() {
		return trustAddress;
	}

	public void setTrustAddress(String trustAddress) {
		this.trustAddress = trustAddress;
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

	public Integer getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}


	public String getContactPersonName() {
		return contactPersonName;
	}

	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}

	public String getContactPersonEmail() {
		return contactPersonEmail;
	}

	public void setContactPersonEmail(String contactPersonEmail) {
		this.contactPersonEmail = contactPersonEmail;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getCellNo() {
		return cellNo;
	}

	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
	}

	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}

	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}


	public String getDisease() {
		return disease;
	}

	public void setDisease(String disease) {
		this.disease = disease;
	}

	public String getMaxLimitPerPerson() {
		return maxLimitPerPerson;
	}

	public void setMaxLimitPerPerson(String maxLimitPerPerson) {
		this.maxLimitPerPerson = maxLimitPerPerson;
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

	public char getTrustStatus() {
		return trustStatus;
	}

	public void setTrustStatus(char trustStatus) {
		this.trustStatus = trustStatus;
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


	public List<VisitType> getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(List<VisitType> visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public List<TrustMasterDto> getListtrustMasterDto() {
		return listtrustMasterDto;
	}

	public void setListtrustMasterDto(List<TrustMasterDto> listtrustMasterDto) {
		this.listtrustMasterDto = listtrustMasterDto;
	}

	public Integer getPatientTypeId() {
		return patientTypeId;
	}

	public void setPatientTypeId(Integer patientTypeId) {
		this.patientTypeId = patientTypeId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getVisitTypeCode() {
		return visitTypeCode;
	}

	public void setVisitTypeCode(String visitTypeCode) {
		this.visitTypeCode = visitTypeCode;
	}

	

	public Integer getTrustCompanyId() {
		return trustCompanyId;
	}

	public void setTrustCompanyId(Integer trustCompanyId) {
		this.trustCompanyId = trustCompanyId;
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

	public Character getIsCompanyContract() {
		return isCompanyContract;
	}

	public void setIsCompanyContract(Character isCompanyContract) {
		this.isCompanyContract = isCompanyContract;
	}

	public Integer getCompanyTypeId() {
		return companyTypeId;
	}

	public void setCompanyTypeId(Integer companyTypeId) {
		this.companyTypeId = companyTypeId;
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

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}


	



}
