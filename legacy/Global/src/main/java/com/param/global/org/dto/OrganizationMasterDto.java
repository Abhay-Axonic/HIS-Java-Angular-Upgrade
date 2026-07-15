package com.param.global.org.dto;

import java.util.List;

import com.param.privilege.dto.ModuleDto;

import javax.persistence.Column;


public class OrganizationMasterDto {

  private Integer organizationId;

  private String organizationCode;

  private String organizationName;

  private String organizationDesc;

  private Integer countryId;

  private Integer stateId;

  private Integer districtId;

  private Integer cityId;

  private Integer areaId;

  private Integer postCode;

  private String organizationAddress;

  private String organizationContact;

  private String organizationEmailId;

  private String organizationLogo;

  private Character status;

  private String createdDate;

  private Integer createdBy;

  private String updatedDate;

  private Integer updatedBy;

  private Integer offset;

  private Integer noOfRecordsPerPage;

  private List<OrganizationUnitLicenceDetailsDto> listOrganizationUnitLicenceDetailsDto;

  private Integer licenceTypeId;

  private String licenceNumber;

  private String expiryDate;

  private String countryName;

  private String stateName;

  private String cityName;

  private String organizationFolder;
  
  private String registrationNumber;
  
  private String isUnitSelected;
  
  private String userName;
  
  private String password;
  
  private String faxNumber;
  
  private String appUrl;
  
  private String unitTimeZone;
  
  private boolean isDuoAuthenticationRequired;
  private boolean isDuoUserLogin;
  private String dateFormat;
  private String globalSchedulingLink;
  private String myLifeLink;
  
  private boolean isSyncScheduling;
  
  private String orgPrivileges;
  
  private List<ModuleDto> moduleDtosList;
  
  private Integer channelId;

  private Integer screenImageId;

  private String imageURL;

  private Integer imageTypeId;

  private  String logoName;

  private Integer userId;
  private Integer appId;
  private String contactNumber;
  private String address;
  private Integer postalCode;
  private String unitName;
  private Integer unitId;
  private String paymentDetails;
  private String emailId;
  private String contactPerson;
  private String completeSubDomainName;
  private String subDomain;

  private String videoName;
  private String videoUrl;

  private String hisLink;

  private String awsAccessKey;

  private String awsSecretKey;

  private List<OrganizationMasterDto> differentTypesScreenImageList;

  
public List<ModuleDto> getModuleDtosList() {
	return moduleDtosList;
}

public void setModuleDtosList(List<ModuleDto> moduleDtosList) {
	this.moduleDtosList = moduleDtosList;
}

public String getOrgPrivileges() {
	return orgPrivileges;
}

public void setOrgPrivileges(String orgPrivileges) {
	this.orgPrivileges = orgPrivileges;
}

public String getAppUrl() {
	return appUrl;
}

public void setAppUrl(String appUrl) {
	this.appUrl = appUrl;
}

public String getFaxNumber() {
	return faxNumber;
}

public void setFaxNumber(String faxNumber) {
	this.faxNumber = faxNumber;
}

public String getUserName() {
	return userName;
}

public void setUserName(String userName) {
	this.userName = userName;
}

public String getPassword() {
	return password;
}

public void setPassword(String password) {
	this.password = password;
}

private byte[] image;

  public Integer getOrganizationId() {
    return organizationId;
  }

  public void setOrganizationId(Integer organizationId) {
    this.organizationId = organizationId;
  }

  public String getOrganizationCode() {
    return organizationCode;
  }

  public void setOrganizationCode(String organizationCode) {
    this.organizationCode = organizationCode;
  }

  public String getOrganizationName() {
    return organizationName;
  }

  public void setOrganizationName(String organizationName) {
    this.organizationName = organizationName;
  }

  public String getOrganizationDesc() {
    return organizationDesc;
  }

  public void setOrganizationDesc(String organizationDesc) {
    this.organizationDesc = organizationDesc;
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

  public Integer getPostCode() {
    return postCode;
  }

  public void setPostCode(Integer postCode) {
    this.postCode = postCode;
  }

  public String getOrganizationAddress() {
    return organizationAddress;
  }

  public void setOrganizationAddress(String organizationAddress) {
    this.organizationAddress = organizationAddress;
  }

  public String getOrganizationContact() {
    return organizationContact;
  }

  public void setOrganizationContact(String organizationContact) {
    this.organizationContact = organizationContact;
  }

  public String getOrganizationEmailId() {
    return organizationEmailId;
  }

  public void setOrganizationEmailId(String organizationEmailId) {
    this.organizationEmailId = organizationEmailId;
  }

  public String getOrganizationLogo() {
    return organizationLogo;
  }

  public void setOrganizationLogo(String organizationLogo) {
    this.organizationLogo = organizationLogo;
  }

  public Character getStatus() {
    return status;
  }

  public void setStatus(Character status) {
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

  public List<OrganizationUnitLicenceDetailsDto> getListOrganizationUnitLicenceDetailsDto() {
    return listOrganizationUnitLicenceDetailsDto;
  }

  public void setListOrganizationUnitLicenceDetailsDto(
      List<OrganizationUnitLicenceDetailsDto> listOrganizationUnitLicenceDetailsDto) {
    this.listOrganizationUnitLicenceDetailsDto = listOrganizationUnitLicenceDetailsDto;
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

  public Integer getLicenceTypeId() {
    return licenceTypeId;
  }

  public void setLicenceTypeId(Integer licenceTypeId) {
    this.licenceTypeId = licenceTypeId;
  }

  public String getLicenceNumber() {
    return licenceNumber;
  }

  public void setLicenceNumber(String licenceNumber) {
    this.licenceNumber = licenceNumber;
  }

  public String getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(String expiryDate) {
    this.expiryDate = expiryDate;
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

  public String getOrganizationFolder() {
    return organizationFolder;
  }

  public void setOrganizationFolder(String organizationFolder) {
    this.organizationFolder = organizationFolder;
  }

public String getRegistrationNumber() {
	return registrationNumber;
}

public void setRegistrationNumber(String registrationNumber) {
	this.registrationNumber = registrationNumber;
}

public String getIsUnitSelected() {
	return isUnitSelected;
}

public void setIsUnitSelected(String isUnitSelected) {
	this.isUnitSelected = isUnitSelected;
}

public byte[] getImage() {
	return image;
}

public void setImage(byte[] image) {
	this.image = image;
}

public String getUnitTimeZone() {
	return unitTimeZone;
}

public void setUnitTimeZone(String unitTimeZone) {
	this.unitTimeZone = unitTimeZone;
}

public boolean getIsDuoAuthenticationRequired() {
	return isDuoAuthenticationRequired;
}

public void setIsDuoAuthenticationRequired(boolean isDuoAuthenticationRequired) {
	this.isDuoAuthenticationRequired = isDuoAuthenticationRequired;
}

public boolean getIsDuoUserLogin() {
	return isDuoUserLogin;
}

public void setIsDuoUserLogin(boolean isDuoUserLogin) {
	this.isDuoUserLogin = isDuoUserLogin;
}

public String getDateFormat() {
	return dateFormat;
}

public void setDateFormat(String dateFormat) {
	this.dateFormat = dateFormat;
}

public boolean getIsSyncScheduling() {
	return isSyncScheduling;
}

public void setIsSyncScheduling(boolean isSyncScheduling) {
	this.isSyncScheduling = isSyncScheduling;
}

public String getGlobalSchedulingLink() {
	return globalSchedulingLink;
}

public void setGlobalSchedulingLink(String globalSchedulingLink) {
	this.globalSchedulingLink = globalSchedulingLink;
}

public String getMyLifeLink() {
	return myLifeLink;
}

public void setMyLifeLink(String myLifeLink) {
	this.myLifeLink = myLifeLink;
}

public Integer getChannelId() {
	return channelId;
}

public void setChannelId(Integer channelId) {
	this.channelId = channelId;
}


  public Integer getScreenImageId() {
    return screenImageId;
  }

  public void setScreenImageId(Integer screenImageId) {
    this.screenImageId = screenImageId;
  }

  public String getImageURL() {
    return imageURL;
  }

  public void setImageURL(String imageURL) {
    this.imageURL = imageURL;
  }

  public Integer getImageTypeId() {
    return imageTypeId;
  }

  public void setImageTypeId(Integer imageTypeId) {
    this.imageTypeId = imageTypeId;
  }


public String getLogoName() {
  return logoName;
}

public void setLogoName(String logoName) {
  this.logoName = logoName;
}

  public List<OrganizationMasterDto> getDifferentTypesScreenImageList() {
    return differentTypesScreenImageList;
  }

  public void setDifferentTypesScreenImageList(List<OrganizationMasterDto> differentTypesScreenImageList) {
    this.differentTypesScreenImageList = differentTypesScreenImageList;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public Integer getAppId() {
    return appId;
  }

  public void setAppId(Integer appId) {
    this.appId = appId;
  }

  public String getContactNumber() {
    return contactNumber;
  }

  public void setContactNumber(String contactNumber) {
    this.contactNumber = contactNumber;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Integer getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(Integer postalCode) {
    this.postalCode = postalCode;
  }

  public String getUnitName() {
    return unitName;
  }

  public void setUnitName(String unitName) {
    this.unitName = unitName;
  }

  public Integer getUnitId() {
    return unitId;
  }

  public void setUnitId(Integer unitId) {
    this.unitId = unitId;
  }

  public String getPaymentDetails() {
    return paymentDetails;
  }

  public void setPaymentDetails(String paymentDetails) {
    this.paymentDetails = paymentDetails;
  }

  public String getEmailId() {
    return emailId;
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  public String getContactPerson() {
    return contactPerson;
  }

  public void setContactPerson(String contactPerson) {
    this.contactPerson = contactPerson;
  }

  public String getCompleteSubDomainName() {
    return completeSubDomainName;
  }

  public void setCompleteSubDomainName(String completeSubDomainName) {
    this.completeSubDomainName = completeSubDomainName;
  }

  public String getSubDomain() {
    return subDomain;
  }

  public void setSubDomain(String subDomain) {
    this.subDomain = subDomain;
  }

  public String getVideoName() {
    return videoName;
  }
  public void setVideoName(String videoName) {
    this.videoName = videoName;
  }
  public String getVideoUrl() {
    return videoUrl;
  }
  public void setVideoUrl(String videoUrl) {
    this.videoUrl = videoUrl;
  }

  public String getHisLink() {
    return hisLink;
  }

  public void setHisLink(String hisLink) {
    this.hisLink = hisLink;
  }

  public String getAwsAccessKey() {
    return awsAccessKey;
  }

  public void setAwsAccessKey(String awsAccessKey) {
    this.awsAccessKey = awsAccessKey;
  }

  public String getAwsSecretKey() {
    return awsSecretKey;
  }

  public void setAwsSecretKey(String awsSecretKey) {
    this.awsSecretKey = awsSecretKey;
  }
}
