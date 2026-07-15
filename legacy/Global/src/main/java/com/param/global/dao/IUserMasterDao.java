package com.param.global.dao;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.param.adt.master.global.model.UserMaster;
import com.param.duoAuthentication.dto.DuoApplicationPropertiesDto;
import com.param.entity.model.master.ApplicationConfiguration;
import com.param.global.common.Response;
import com.param.global.dto.MailDto;
import com.param.global.dto.OrganizationUnitUserMapperDto;
import com.param.global.dto.UserLoginLogDto;
import com.param.global.dto.UserMasterDto;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;


@SuppressWarnings("rawtypes")
public interface IUserMasterDao extends IGenericDao<UserMaster, Integer>{
	Response userLogin(UserMasterDto userMasterDto) throws ApplicationException, NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;
	Response getOrganizationList(String url)throws ApplicationException;
	Response getUnitFromOrganizationId(OrganizationMasterDto organizationMasterDto)throws ApplicationException;
	Response validateUniqueUserName(UserMasterDto userMasterDto) throws ApplicationException;
	Response getHospitalUnitUserForLogin(OrganizationUnitUserMapperDto organizationUnitUserMapperDto)throws ApplicationException;
	Response saveUserMaster(UserMasterDto userMasterDto)throws ApplicationException;
	Response saveUserMaster(UserMaster userMasterDto)throws ApplicationException;
	Response getUserRoleByUserId(int userId,int orgId,int orgUnitId)throws ApplicationException;
	public Response getUsersByName(UserMasterDto userMasterDto)throws ApplicationException;
	Response checkEmailAvailability(UserMasterDto userMasterDto);
	Response otpVerification(UserMasterDto userMasterDto);
	Response forgotPasswordGeneration(UserMasterDto userMasterDto);
	Response resetPassword(UserMasterDto userMasterDto);
	Response resendUserName(UserMasterDto userMasterDto);
	Response resendUserNameAndPassword(UserMasterDto userMasterDto);
	Response getUnitName(UserMasterDto userMasterDto);
	public Response getPass();
	Response getUserNameById(UserMasterDto userMasterDto);
	List<SmsUtilityConfigInfoDto> getAdminDetaisForSmsUtility(Integer unitId);
	Response getPasswordAndUserName()throws ApplicationException;
	Response getUserlogDetails(UserLoginLogDto dto)throws ApplicationException;
	Response saveUserLogDetails(UserLoginLogDto logDto)throws ApplicationException;
	Response updateUserLog(UserLoginLogDto logDto)throws ApplicationException;
	Response checkOtherLoginByMacAddress(UserLoginLogDto dto)throws ApplicationException;
	Response updatePreviousLogin(UserLoginLogDto dto)throws ApplicationException;
	Response getUserlogDetailsByMacAddress(UserLoginLogDto dto)throws ApplicationException;
	Response checkOtherLoginByUserId(UserLoginLogDto dto)throws ApplicationException;
	Response getUserListOfSchedulingForUpdate(List<Integer> unitIds)throws ApplicationException;
	Response updateSchedulingLog(List<Integer> logIds)throws ApplicationException;
	Response getUsersByNameOnType(UserMasterDto userMasterDto)throws ApplicationException;
	Response getUnitByCredentials(OrganizationMasterDto organizationMasterDto)throws ApplicationException;
	Integer getIsCiniUser(Integer orgId)throws ApplicationException;
	Response getPharmacyUserRoleType(UserLoginLogDto dto)throws ApplicationException;
	Response getUnitExist(UserMasterDto userMasterDto)throws ApplicationException;
	Response getUnitExistCheckByDate(UserMasterDto userMasterDto)throws ApplicationException;
	Response updatePassword(UserMasterDto dto) throws ApplicationException, NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;
	MailDto getMailDetails(Integer orgId, Integer unitId) throws ApplicationException;
	ApplicationConfiguration getApplicationConfiguration(Integer organizationId, Integer unitId);
	Response validateLoginForDoctorApp(UserMasterDto userMasterDto)throws ApplicationException;
	Response updateLabPinUserMaster(UserMasterDto userMaDto)throws ApplicationException;
	Response getUserDetailsByUserName(String username)throws ApplicationException;
	Response getLdapConfigurationData(DuoApplicationPropertiesDto duoApplicationPropertiesDto)throws ApplicationException;
	Response userLoginForDuoAuthentication(UserMasterDto userMasterDto)throws ApplicationException;
	Response getUnitByCredentialsForDuoLogin(OrganizationMasterDto organizationMasterDto)throws ApplicationException;
	Response getUserDataById(UserMasterDto userMasterDto)throws ApplicationException;
	List<UserMasterDto> getListOfUsers() throws ApplicationException;
	Response getUserDataByIdNew(UserMasterDto userMasterDtos)throws ApplicationException;
	Response saveSubscriptionDetails(UserMasterDto userMasterDto)throws ApplicationException;
	Response getSuperAdminUser(Integer orgId, Integer unitId, Integer userId)throws ApplicationException;
	Response getSubscriptionDetails(UserMasterDto userMasterDto)throws ApplicationException;
	String getAllowedDomainsList() throws ApplicationException;
	Response getFailedLog(UserMasterDto masterDto)throws ApplicationException;
	Response updateLoginAttempt(Timestamp object, Integer i, Integer integer)throws ApplicationException;

	Response createOrganizationFromWeb(OrganizationMasterDto organizationMasterForWebDto) throws ApplicationException;

	Map<String, String> getawsConfigMap()throws ApplicationException;

    Response getAllUsersByOrgId(Integer organizationId) throws ApplicationException;

    Response getWebUserId(Integer unitId)throws ApplicationException;

    Response generateSMSOtp(UserMasterDto userMasterDto, HttpServletRequest request) throws ApplicationException;

	Response validateSMSOtp(UserMasterDto userMasterDto, HttpServletRequest request)  throws ApplicationException;
}
