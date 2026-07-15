package com.param.global.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;

import com.param.duoAuthentication.dto.DuoApplicationPropertiesDto;
import com.param.global.common.Response;
import com.param.global.dto.ProductionReleaseEmailNotificationConfigDto;
import com.param.global.dto.UserLoginLogDto;
import com.param.global.dto.UserMasterDto;
import com.param.global.dto.sync.hl7ConfigListDto;
import com.param.global.org.dto.OrganizationMasterDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IUserMasterService 
{
	public Response userLogin(UserMasterDto userMasterDto,HttpServletRequest request)throws ApplicationException;
	public Response getOrganizationList(String url);
	public Response getUnitListByOrganization(OrganizationMasterDto organizationMasterDto) throws ApplicationException;
	public Response validateUniqueUserName(UserMasterDto userMasterDto);
	public Response saveUserMaster(UserMasterDto userMasterDto) throws ApplicationException;
	public Response getUsersByName(UserMasterDto userMasterDto)throws ApplicationException;
	public Response resetPassword(UserMasterDto userMasterDto)throws ApplicationException;
	public Response checkEmailAvailability(UserMasterDto userMasterDto)throws ApplicationException;
	public Response otpVerification(UserMasterDto userMasterDto)throws ApplicationException;
	public Response forgotPasswordGeneration(UserMasterDto userMasterDto)throws ApplicationException;
	public Response resendUserName(UserMasterDto userMasterDto);
	public Response resendUserNameAndPassword(UserMasterDto userMasterDto);
	public Response getUnitName(UserMasterDto userMasterDto);
	public Response getPass();
	public Response getUserNameById(UserMasterDto userMasterDto);
	public Response userLoginForVitalUploader(UserMasterDto userMasterDto)throws ApplicationException;
	public Response getPasswordAndUserName()throws ApplicationException;
	// for login logs 
	public Response saveUserLogDetails(UserLoginLogDto dto)throws ApplicationException;
	public Response checkOtherLoginByMacAddress(UserLoginLogDto dto)throws ApplicationException;
	public Response updatePreviousLogin(UserLoginLogDto dto)throws ApplicationException;
	public Response checkOtherLoginByUserId(UserLoginLogDto dto)throws ApplicationException;
	public Response getUserListOfSchedulingForUpdate(List<Integer>unitIds) throws ApplicationException;
	public Response updateSchedulingLog(List<Integer>unitIds) throws ApplicationException;
	
	//getAuthToken
	public String getAuthToken(HttpServletRequest request) throws ApplicationException;
	public Response getUsersByNameOnType(UserMasterDto userMasterDto)throws ApplicationException;
	public Response getUnitByCredentials(OrganizationMasterDto organizationMasterDto)throws ApplicationException;
	public Response getPharmacyUserRoleType(UserLoginLogDto dto)throws ApplicationException;
	public Response updatePassword(UserMasterDto dto)throws ApplicationException;
	public Response validateLoginForDoctorApp(UserMasterDto userMasterDto)throws ApplicationException;
	public Response getUserListForWebAppByOrgIdAndUnitId(UserMasterDto dto)throws ApplicationException;
	public Response getUserDetailsByUserName(String username)throws ApplicationException;
	public Response getLdapConfigurationData(DuoApplicationPropertiesDto duoApplicationPropertiesDto)throws ApplicationException;
	public Response userLoginForDuoAuthentication(UserMasterDto userMasterDto, HttpServletRequest request)throws ApplicationException;
	public Response getUnitByCredentialsForDuoLogin(OrganizationMasterDto organizationMasterDto)throws ApplicationException;
	public Response getCookieDataForNewEMR(UserMasterDto userMasterDto)throws ApplicationException;
	public List<UserMasterDto> getListOfUsers() throws ApplicationException;
	public Map<String,String> generateToken(Integer userId, Integer unitId);
	public Response saveSubscriptionDetails(UserMasterDto userMasterDto)throws ApplicationException;
	public Response getSuperAdminUser(Integer orgId, Integer unitId, Integer userId)throws ApplicationException;
	public Response getSubscriptionDetails(UserMasterDto userMasterDto)throws ApplicationException;

	public String getAllowedDomainsList();

	public CompletableFuture<Response> createOrganizationFromWeb(OrganizationMasterDto organizationMasterForWebDto) throws ApplicationException;

	public Map<String, String> getawsConfigMap() throws ApplicationException;

	public void refreshAllCaches(Integer userId, String userName);

	public Response sendNotificationOnEmailOfReleaseDetails() throws ApplicationException;

	public List<hl7ConfigListDto> gethl7ConfigList() throws ApplicationException;

	Response getWebUserId(Integer unitId) throws ApplicationException;

	Response generateSMSOtp(UserMasterDto userMasterDto,HttpServletRequest request)  throws ApplicationException;

	Response validateSMSOtp(UserMasterDto userMasterDto, HttpServletRequest request) throws ApplicationException;
}
