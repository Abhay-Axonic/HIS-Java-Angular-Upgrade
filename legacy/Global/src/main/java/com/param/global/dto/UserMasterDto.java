package com.param.global.dto;


import java.sql.Timestamp;
import java.util.Date;



import java.util.List;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.ResponseEntity;

import com.param.global.org.dto.UnitMasterDto;
import com.param.global.unit.dto.LabConfigurationDto;
import com.param.global.unit.dto.UserRoleMasterDto;

public class UserMasterDto 
{
	
	private Integer userId;
	
	private String loginName;
	
	private String password;
	
	private String confirmPassword;
	
	private String existingPassword;
	
	private String userPwd;
	
	private List<UserRoleMasterDto> roleIds;
	
	private String createdDate;
	
	private String unitName;

	private Integer createdBy;

	private String userName;
	
	private Integer unitId;
	
	private String emailId;
	
	private String roleDesc;
	
	private Integer organizationId;
	
	private String firstName;
	
	private String docFirstName;
	
	private String dateOfBirth;
	
	private String lastName;
	
	private Integer updatedBy;

	private String updatedDate;
	
	//private String ssoToken;
	
	private Integer doctorId;
	
	private Integer nursingStationId;
	
	private Integer unitCountryId;
	
	private Integer unitStateId;
	
	private Integer unitDistrictId;
	
	private Integer  unitCityId;
	
	private Integer  unitAreaId;
	
	private String pwdExpiry;

	private String status;
	
	private Character subscriptionStatus;
	
	private String doctorName;
	
	private String employeeName;
	
	private Integer employeeId;
	
	private String labPin;
	
	private String macAddress;
	
	private Character isContinueLogin;

	private List<Integer> departmentNursingStationMapperDtoslist;
	
	private UnitApplicationConfigurationDto unitApplicationConfigurationDto;
	
	private UnitTemplateConfigDto unitTemplateConfigDto;
	
	private List<UserCounterMapperDto> userCounterMapperDtosList;
	
	private LabConfigurationDto labConfigurationDto;
	
	private String otp;
	
	private Date date;
	
	private String otpExpiratyDate;
	
	private String roleName;
	
	private Character isRMO;
	
	private String authToken;
	
	private ResponseEntity<?> responseEntity;
	
	private Character isSuperUser;
	
	private Integer id;
	
	private Integer failedCount;
	
	private String subscrptionMsg;
	
	private String unitZipCode;
	
	private String dashboardState;
	private boolean isSkipPharmacySettlement;
	private boolean isPoApproval;
	private boolean isRenderedServiceCancelled;

	private boolean isUpdatePatientReg;

	private boolean autoBatch;
	private boolean atCopRequired;
	private boolean autoPrescribedQty;
	private boolean isUpdateIpdChargesRemark;
	
	private boolean  updateDischargeSummary;
	private boolean  isDuoUserLogin;
	private boolean opdBillCancellation;
	private boolean ipdBillCancellation;
	private boolean billSettlement;
	private boolean ipBillSettlement;
	private boolean serviceRateEditable;
	private String  dateFormat;
	private boolean isForceUpdateServicePackage;
	private String unitTimeZone;
	private List<UnitMasterDto> unitList;
	
	private Boolean mpesaRequired;
	
    private Boolean nursingOpScan;
    private Boolean nursingIpScan;
    private Boolean doctorOpScan;
    private Boolean doctorIpScan;
    private Boolean changePayeeRights;
    
    private Boolean extendPdd;
    private Boolean ipRenderedDateChange;
    private Boolean isBillManipulationAllowed;
    
	private Date subscriptionStartDate;

	private Date subscriptionEndDate;

	private String subscriptionType;

	private String amountPaid;
	
	private String subscriptionEndDateUnitMaster;
	
	private String subscriptionStartDateUnitMaster;
	
	private int subscriptionId;
	
	private String subscriptionDate;

	@JsonIgnore
	private Integer otpCount;

	@JsonIgnore
	private Timestamp otpTimeStamp;

	@JsonIgnore
	private boolean blocked;
	
	@JsonIgnore
	private Timestamp loginBlockTime;

	@JsonIgnore
	private Integer loginFailedCount;

	private Integer webAppUseId;

	private String mobileNumber;

	public Integer getWebAppUseId() {
		return webAppUseId;
	}

	public void setWebAppUseId(Integer webAppUseId) {
		this.webAppUseId = webAppUseId;
	}

	public String getUnitTimeZone() {
		return unitTimeZone;
	}

	public void setUnitTimeZone(String unitTimeZone) {
		this.unitTimeZone = unitTimeZone;
	}

	public Boolean getIsBillManipulationAllowed() {
		return isBillManipulationAllowed;
	}

	public void setIsBillManipulationAllowed(Boolean isBillManipulationAllowed) {
		this.isBillManipulationAllowed = isBillManipulationAllowed;
	}

	public boolean isPoApproval() {
		return isPoApproval;
	}

	public void setPoApproval(boolean isPoApproval) {
		this.isPoApproval = isPoApproval;
	}

	public boolean isSkipPharmacySettlement() {
		return isSkipPharmacySettlement;
	}

	public void setSkipPharmacySettlement(boolean isSkipPharmacySettlement) {
		this.isSkipPharmacySettlement = isSkipPharmacySettlement;
	}

	public String getDashboardState() {
		return dashboardState;
	}

	public void setDashboardState(String dashboardState) {
		this.dashboardState = dashboardState;
	}

	public String getUnitZipCode() {
		return unitZipCode;
	}

	public void setUnitZipCode(String unitZipCode) {
		this.unitZipCode = unitZipCode;
	}

	public String getSubscrptionMsg() {
		return subscrptionMsg;
	}

	public void setSubscrptionMsg(String subscrptionMsg) {
		this.subscrptionMsg = subscrptionMsg;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFailedCount() {
		return failedCount;
	}

	public void setFailedCount(Integer failedCount) {
		this.failedCount = failedCount;
	}

	public Character getIsSuperUser() {
		return isSuperUser;
	}

	public void setIsSuperUser(Character isSuperUser) {
		this.isSuperUser = isSuperUser;
	}

	public ResponseEntity<?> getResponseEntity() {
		return responseEntity;
	}

	public void setResponseEntity(ResponseEntity<?> responseEntity) {
		this.responseEntity = responseEntity;
	}

	public UnitApplicationConfigurationDto getUnitApplicationConfigurationDto() {
		return unitApplicationConfigurationDto;
	}

	public void setUnitApplicationConfigurationDto(UnitApplicationConfigurationDto unitApplicationConfigurationDto) {
		this.unitApplicationConfigurationDto = unitApplicationConfigurationDto;
	}

	public List<UserCounterMapperDto> getUserCounterMapperDtosList() {
		return userCounterMapperDtosList;
	}

	public void setUserCounterMapperDtosList(List<UserCounterMapperDto> userCounterMapperDtosList) {
		this.userCounterMapperDtosList = userCounterMapperDtosList;
	}

	public LabConfigurationDto getLabConfigurationDto() {
		return labConfigurationDto;
	}

	public void setLabConfigurationDto(LabConfigurationDto labConfigurationDto) {
		this.labConfigurationDto = labConfigurationDto;
	}

	public String getDocFirstName() {
		return docFirstName;
	}

	public void setDocFirstName(String docFirstName) {
		this.docFirstName = docFirstName;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getExistingPassword() {
		return existingPassword;
	}

	public void setExistingPassword(String existingPassword) {
		this.existingPassword = existingPassword;
	}

	public List<UserRoleMasterDto> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<UserRoleMasterDto> roleIds) {
		this.roleIds = roleIds;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	/*public String getSsoToken() {
		return ssoToken;
	}

	public void setSsoToken(String ssoToken) {
		this.ssoToken = ssoToken;
	}*/

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getNursingStationId() {
		return nursingStationId;
	}

	public void setNursingStationId(Integer nursingStationId) {
		this.nursingStationId = nursingStationId;
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

	public Integer getUnitCityId() {
		return unitCityId;
	}

	public void setUnitCityId(Integer unitCityId) {
		this.unitCityId = unitCityId;
	}

	public Integer getUnitAreaId() {
		return unitAreaId;
	}

	public void setUnitAreaId(Integer unitAreaId) {
		this.unitAreaId = unitAreaId;
	}

	public String getPwdExpiry() {
		return pwdExpiry;
	}

	public void setPwdExpiry(String pwdExpiry) {
		this.pwdExpiry = pwdExpiry;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public List<Integer> getDepartmentNursingStationMapperDtoslist() {
		return departmentNursingStationMapperDtoslist;
	}

	public void setDepartmentNursingStationMapperDtoslist(List<Integer> departmentNursingStationMapperDtoslist) {
		this.departmentNursingStationMapperDtoslist = departmentNursingStationMapperDtoslist;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getOtpExpiratyDate() {
		return otpExpiratyDate;
	}

	public void setOtpExpiratyDate(String otpExpiratyDate) {
		this.otpExpiratyDate = otpExpiratyDate;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getLabPin() {
		return labPin;
	}

	public void setLabPin(String labPin) {
		this.labPin = labPin;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public Character getIsContinueLogin() {
		return isContinueLogin;
	}

	public void setIsContinueLogin(Character isContinueLogin) {
		this.isContinueLogin = isContinueLogin;
	}

	public Character getIsRMO() {
		return isRMO;
	}

	public void setIsRMO(Character isRMO) {
		this.isRMO = isRMO;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public List<UnitMasterDto> getUnitList() {
		return unitList;
	}

	public void setUnitList(List<UnitMasterDto> unitList) {
		this.unitList = unitList;
	}
	public UnitTemplateConfigDto getUnitTemplateConfigDto() {
		return unitTemplateConfigDto;
	}

	public void setUnitTemplateConfigDto(UnitTemplateConfigDto unitTemplateConfigDto) {
		this.unitTemplateConfigDto = unitTemplateConfigDto;

	}

	public boolean isRenderedServiceCancelled() {
		return isRenderedServiceCancelled;
	}

	public void setRenderedServiceCancelled(boolean isRenderedServiceCancelled) {
		this.isRenderedServiceCancelled = isRenderedServiceCancelled;
	}
	public boolean isAutoBatch() {
		return autoBatch;
	}

	public void setAutoBatch(boolean autoBatch) {
		this.autoBatch = autoBatch;
	}

	public boolean isAutoPrescribedQty() {
		return autoPrescribedQty;
	}

	public void setAutoPrescribedQty(boolean autoPrescribedQty) {
		this.autoPrescribedQty = autoPrescribedQty;
	}

	public boolean getIsUpdatePatientReg() {
		return isUpdatePatientReg;
	}

	public void setIsUpdatePatientReg(boolean isUpdatePatientReg) {
		this.isUpdatePatientReg = isUpdatePatientReg;
	}

	public boolean getIsUpdateIpdChargesRemark() {
		return isUpdateIpdChargesRemark;
	}

	public void setIsUpdateIpdChargesRemark(boolean isUpdateIpdChargesRemark) {
		this.isUpdateIpdChargesRemark = isUpdateIpdChargesRemark;
	}

	public boolean isUpdateDischargeSummary() {
		return updateDischargeSummary;
	}

	public void setUpdateDischargeSummary(boolean updateDischargeSummary) {
		this.updateDischargeSummary = updateDischargeSummary;
	}

	public boolean getIsDuoUserLogin() {
		return isDuoUserLogin;
	}

	public void setIsDuoUserLogin(boolean isDuoUserLogin) {
		this.isDuoUserLogin = isDuoUserLogin;
	}
	public Boolean getMpesaRequired() {
		return mpesaRequired;
	}

	public void setMpesaRequired(Boolean mpesaRequired) {
		this.mpesaRequired = mpesaRequired;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public boolean getOpdBillCancellation() {
		return opdBillCancellation;
	}

	public void setOpdBillCancellation(boolean opdBillCancellation) {
		this.opdBillCancellation = opdBillCancellation;
	}

	public boolean getIpdBillCancellation() {
		return ipdBillCancellation;
	}

	public void setIpdBillCancellation(boolean ipdBillCancellation) {
		this.ipdBillCancellation = ipdBillCancellation;
	}

	public boolean getBillSettlement() {
		return billSettlement;
	}

	public void setBillSettlement(boolean billSettlement) {
		this.billSettlement = billSettlement;
	}

	public boolean getIpBillSettlement() {
		return ipBillSettlement;
	}

	public void setIpBillSettlement(boolean ipBillSettlement) {
		this.ipBillSettlement = ipBillSettlement;
	}

	public boolean isatCopRequired() {
		return atCopRequired;
	}

	public void setatCopRequired(boolean atCopRequired) {
		this.atCopRequired = atCopRequired;
	}

	public boolean getServiceRateEditable() {
		return serviceRateEditable;
	}

	public void setServiceRateEditable(boolean serviceRateEditable) {
		this.serviceRateEditable = serviceRateEditable;
	}

	public boolean getIsForceUpdateServicePackage() {
		return isForceUpdateServicePackage;
	}

	public void setIsForceUpdateServicePackage(boolean isForceUpdateServicePackage) {
		this.isForceUpdateServicePackage = isForceUpdateServicePackage;
	}

	public Boolean getNursingOpScan() {
		return nursingOpScan;
	}

	public void setNursingOpScan(Boolean nursingOpScan) {
		this.nursingOpScan = nursingOpScan;
	}

	public Boolean getNursingIpScan() {
		return nursingIpScan;
	}

	public void setNursingIpScan(Boolean nursingIpScan) {
		this.nursingIpScan = nursingIpScan;
	}

	public Boolean getDoctorOpScan() {
		return doctorOpScan;
	}

	public void setDoctorOpScan(Boolean doctorOpScan) {
		this.doctorOpScan = doctorOpScan;
	}

	public Boolean getDoctorIpScan() {
		return doctorIpScan;
	}

	public void setDoctorIpScan(Boolean doctorIpScan) {
		this.doctorIpScan = doctorIpScan;
	}

	public Boolean getChangePayeeRights() {
		return changePayeeRights;
	}

	public void setChangePayeeRights(Boolean changePayeeRights) {
		this.changePayeeRights = changePayeeRights;
	}

	public Boolean getExtendPdd() {
		return extendPdd;
	}

	public void setExtendPdd(Boolean extendPdd) {
		this.extendPdd = extendPdd;
	}

	public Boolean getIpRenderedDateChange() {
		return ipRenderedDateChange;
	}

	public void setIpRenderedDateChange(Boolean ipRenderedDateChange) {
		this.ipRenderedDateChange = ipRenderedDateChange;
	}

	public Date getSubscriptionStartDate() {
		return subscriptionStartDate;
	}

	public void setSubscriptionStartDate(Date subscriptionStartDate) {
		this.subscriptionStartDate = subscriptionStartDate;
	}

	public Date getSubscriptionEndDate() {
		return subscriptionEndDate;
	}

	public void setSubscriptionEndDate(Date subscriptionEndDate) {
		this.subscriptionEndDate = subscriptionEndDate;
	}

	public String getSubscriptionType() {
		return subscriptionType;
	}

	public void setSubscriptionType(String subscriptionType) {
		this.subscriptionType = subscriptionType;
	}

	public String getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(String amountPaid) {
		this.amountPaid = amountPaid;
	}

	public String getSubscriptionEndDateUnitMaster() {
		return subscriptionEndDateUnitMaster;
	}

	public void setSubscriptionEndDateUnitMaster(String subscriptionEndDateUnitMaster) {
		this.subscriptionEndDateUnitMaster = subscriptionEndDateUnitMaster;
	}

	public String getSubscriptionStartDateUnitMaster() {
		return subscriptionStartDateUnitMaster;
	}

	public void setSubscriptionStartDateUnitMaster(String subscriptionStartDateUnitMaster) {
		this.subscriptionStartDateUnitMaster = subscriptionStartDateUnitMaster;
	}

	public int getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(int subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public String getSubscriptionDate() {
		return subscriptionDate;
	}

	public void setSubscriptionDate(String subscriptionDate) {
		this.subscriptionDate = subscriptionDate;
	}

	public Character getSubscriptionStatus() {
		return subscriptionStatus;
	}

	public void setSubscriptionStatus(Character subscriptionStatus) {
		this.subscriptionStatus = subscriptionStatus;
	}

	public Integer getOtpCount() {
		return otpCount;
	}

	public void setOtpCount(Integer otpCount) {
		this.otpCount = otpCount;
	}

	public Timestamp getOtpTimeStamp() {
		return otpTimeStamp;
	}

	public void setOtpTimeStamp(Timestamp otpTimeStamp) {
		this.otpTimeStamp = otpTimeStamp;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public Timestamp getLoginBlockTime() {
		return loginBlockTime;
	}

	public void setLoginBlockTime(Timestamp loginBlockTime) {
		this.loginBlockTime = loginBlockTime;
	}

	public Integer getLoginFailedCount() {
		return loginFailedCount;
	}

	public void setLoginFailedCount(Integer loginFailedCount) {
		this.loginFailedCount = loginFailedCount;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
}
