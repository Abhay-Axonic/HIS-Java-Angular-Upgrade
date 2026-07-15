package com.param.global.org.dto;

import java.util.List;

import com.param.global.procedure.VO.OrganisationMasterDtoVO;


public class UnitMasterDto {
  private Integer unitId;

  private String unitCode;

  private String unitName;

  private String unitDesc;

  private Integer untOrganizationId;
  
  private OrganisationMasterDtoVO Organization;

  private Integer unitCountryId;

  private Integer unitStateId;

  private Integer unitDistrictId;

  private Integer unitZipId;

  private String unitEmailId;

  private String unitLogo;

  private char status;

  private Integer createdBy;

  private String createdDate;

  private Integer updatedBy;

  private String updatedDate;

  private Integer organizationId;

  private String postCode;

  private Integer unitCityId;

  private String unitAddress;

  private String unitDescription;

  private Integer unitAreaId;

  private Integer offSet;

  private Integer noOfRecordsPerPage;

  private String countryName;

  private String stateName;

  private String cityName;

  private String unitContact;

  private String unitFolder;
  
  private String areaName;

  private List<OrganizationUnitLicenceDetailsDto> listOrganizationUnitLicenceDetailsDto;

  private String organizationName;
  
  private String registrationNumber;
  
  private String previlageString;
  
  private byte[] image;

  private String subscriptionDate;
  
  private String subscriptionNumber;
  
  private String connectionString;
  
  private String connectionUser;
  
  private String connectionPass;
 
  private String organizationFolder;
  
  private String applicationUrl;
  
  private String countryCallingCode;
  
  private String pdfPassword;
  
  private String pdfUserId;
  
  private String unitTimeZone;
  
  private String accountSid;
	
  private String authToken;
	
  private String fromNumber;
  
  private boolean processingUnit;
  
  private Integer medallCenterId;
  
  private String amountPaid;
  
  private Character isSuperUser;
  
public String getUnitTimeZone() {
	return unitTimeZone;
}

public void setUnitTimeZone(String unitTimeZone) {
	this.unitTimeZone = unitTimeZone;
}

public String getCountryCallingCode() {
	return countryCallingCode;
}

public void setCountryCallingCode(String countryCallingCode) {
	this.countryCallingCode = countryCallingCode;
}

public String getSubscriptionDate() {
	return subscriptionDate;
}

public void setSubscriptionDate(String subscriptionDate) {
	this.subscriptionDate = subscriptionDate;
}

public String getSubscriptionNumber() {
	return subscriptionNumber;
}

public void setSubscriptionNumber(String subscriptionNumber) {
	this.subscriptionNumber = subscriptionNumber;
}

public String getConnectionString() {
	return connectionString;
}

public void setConnectionString(String connectionString) {
	this.connectionString = connectionString;
}

public String getConnectionUser() {
	return connectionUser;
}

public void setConnectionUser(String connectionUser) {
	this.connectionUser = connectionUser;
}

public String getConnectionPass() {
	return connectionPass;
}

public void setConnectionPass(String connectionPass) {
	this.connectionPass = connectionPass;
}

public String getPrevilageString() {
	return previlageString;
}

public void setPrevilageString(String previlageString) {
	this.previlageString = previlageString;
}

public Integer getUnitAreaId() {
    return unitAreaId;
  }

  public void setUnitAreaId(Integer unitAreaId) {
    this.unitAreaId = unitAreaId;
  }

  public Integer getOrganizationId() {
    return organizationId;
  }

  public void setOrganizationId(Integer organizationId) {
    this.organizationId = organizationId;
  }
  
  public OrganisationMasterDtoVO getOrganization() {
	return Organization;
}

public void setOrganization(OrganisationMasterDtoVO organization) {
	Organization = organization;
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

  public char getStatus() {
    return status;
  }

  public void setStatus(char status) {
    this.status = status;
  }

  public Integer getUnitId() {
    return unitId;
  }

  public void setUnitId(Integer unitId) {
    this.unitId = unitId;
  }

  public String getUnitCode() {
    return unitCode;
  }

  public void setUnitCode(String unitCode) {
    this.unitCode = unitCode;
  }

  public String getUnitName() {
    return unitName;
  }

  public void setUnitName(String unitName) {
    this.unitName = unitName;
  }

  public String getUnitDesc() {
    return unitDesc;
  }

  public void setUnitDesc(String unitDesc) {
    this.unitDesc = unitDesc;
  }

  public String getUnitEmailId() {
    return unitEmailId;
  }

  public void setUnitEmailId(String unitEmailId) {
    this.unitEmailId = unitEmailId;
  }

  public Integer getUntOrganizationId() {
    return untOrganizationId;
  }

  public void setUntOrganizationId(Integer untOrganizationId) {
    this.untOrganizationId = untOrganizationId;
  }

  public Integer getUnitCountryId() {
    return unitCountryId;
  }

  public void setUnitCountryId(Integer unitCountryId) {
    this.unitCountryId = unitCountryId;
  }

  public Integer getUnitStateId() {
    return unitStateId;
  }

  public void setUnitStateId(Integer unitStateId) {
    this.unitStateId = unitStateId;
  }

  public Integer getUnitDistrictId() {
    return unitDistrictId;
  }

  public void setUnitDistrictId(Integer unitDistrictId) {
    this.unitDistrictId = unitDistrictId;
  }

  public Integer getUnitZipId() {
    return unitZipId;
  }

  public void setUnitZipId(Integer unitZipId) {
    this.unitZipId = unitZipId;
  }

  public String getUnitLogo() {
    return unitLogo;
  }

  public void setUnitLogo(String unitLogo) {
    this.unitLogo = unitLogo;
  }

  public String getPostCode() {
    return postCode;
  }

  public void setPostCode(String postCode) {
    this.postCode = postCode;
  }

  public Integer getUnitCityId() {
    return unitCityId;
  }

  public void setUnitCityId(Integer unitCityId) {
    this.unitCityId = unitCityId;
  }

  public String getUnitAddress() {
    return unitAddress;
  }

  public void setUnitAddress(String unitAddress) {
    this.unitAddress = unitAddress;
  }

  public String getUnitDescription() {
    return unitDescription;
  }

  public void setUnitDescription(String unitDescription) {
    this.unitDescription = unitDescription;
  }

  public Integer getOffSet() {
    return offSet;
  }

  public void setOffSet(Integer offSet) {
    this.offSet = offSet;
  }

  public Integer getNoOfRecordsPerPage() {
    return noOfRecordsPerPage;
  }

  public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
    this.noOfRecordsPerPage = noOfRecordsPerPage;
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

  public List<OrganizationUnitLicenceDetailsDto> getListOrganizationUnitLicenceDetailsDto() {
    return listOrganizationUnitLicenceDetailsDto;
  }

  public void setListOrganizationUnitLicenceDetailsDto(
      List<OrganizationUnitLicenceDetailsDto> listOrganizationUnitLicenceDetailsDto) {
    this.listOrganizationUnitLicenceDetailsDto = listOrganizationUnitLicenceDetailsDto;
  }

  public String getUnitContact() {
    return unitContact;
  }

  public void setUnitContact(String unitContact) {
    this.unitContact = unitContact;
  }

  public String getOrganizationName() {
    return organizationName;
  }

  public void setOrganizationName(String organizationName) {
    this.organizationName = organizationName;
  }

  public String getUnitFolder() {
    return unitFolder;
  }

  public void setUnitFolder(String unitFolder) {
    this.unitFolder = unitFolder;
  }

public String getRegistrationNumber() {
	return registrationNumber;
}

public void setRegistrationNumber(String registrationNumber) {
	this.registrationNumber = registrationNumber;
}

public byte[] getImage() {
	return image;
}

public void setImage(byte[] image) {
	this.image = image;
}

public String getOrganizationFolder() {
	return organizationFolder;
}

public void setOrganizationFolder(String organizationFolder) {
	this.organizationFolder = organizationFolder;
}

public String getApplicationUrl() {
	return applicationUrl;
}

public void setApplicationUrl(String applicationUrl) {
	this.applicationUrl = applicationUrl;
}

public String getPdfPassword() {
	return pdfPassword;
}

public void setPdfPassword(String pdfPassword) {
	this.pdfPassword = pdfPassword;
}

public String getPdfUserId() {
	return pdfUserId;
}

public void setPdfUserId(String pdfUserId) {
	this.pdfUserId = pdfUserId;
}

public String getAccountSid() {
	return accountSid;
}

public void setAccountSid(String accountSid) {
	this.accountSid = accountSid;
}

public String getAuthToken() {
	return authToken;
}

public void setAuthToken(String authToken) {
	this.authToken = authToken;
}

public String getFromNumber() {
	return fromNumber;
}

public void setFromNumber(String fromNumber) {
	this.fromNumber = fromNumber;
}

public boolean getProcessingUnit() {
	return processingUnit;
}

public void setProcessingUnit(boolean processingUnit) {
	this.processingUnit = processingUnit;
}

public String getAreaName() {
	return areaName;
}

public void setAreaName(String areaName) {
	this.areaName = areaName;
}

public Integer getMedallCenterId() {
	return medallCenterId;
}

public void setMedallCenterId(Integer medallCenterId) {
	this.medallCenterId = medallCenterId;
}

public String getAmountPaid() {
	return amountPaid;
}

public void setAmountPaid(String amountPaid) {
	this.amountPaid = amountPaid;
}

public Character getIsSuperUser() {
	return isSuperUser;
}

public void setIsSuperUser(Character isSuperUser) {
	this.isSuperUser = isSuperUser;
}


}
