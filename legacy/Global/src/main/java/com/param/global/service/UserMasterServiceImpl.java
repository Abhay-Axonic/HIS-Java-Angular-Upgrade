package com.param.global.service;

import java.io.IOException;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.param.global.dao.IProductionReleaseEmailNotificationConfigDao;
import com.param.global.dao.kit.items.IHl7ConfigListDao;
import com.param.global.dto.*;
import com.param.global.dto.sync.hl7ConfigListDto;
import com.param.global.org.service.IGlobalUnitMasterService;
import com.param.global.unit.service.IwhatsAppServiceImpl;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.param.duoAuthentication.dto.DuoApplicationPropertiesDto;
import com.param.entity.model.master.ApplicationConfiguration;
import com.param.entity.model.master.User;
import com.param.global.common.AuthKey;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IUserLoginConstants;
import com.param.global.common.Response;
import com.param.global.common.ServletUtils;
import com.param.global.dao.IDepartmentNursingStationMapperDao;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dao.IUserMasterDao;
import com.param.global.dao.global.GlobalDao;
import com.param.global.mail.IMailingService;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.global.org.dto.UnitMasterDto;
import com.param.global.unit.dao.IMpesaAppConfigDao;
import com.param.global.unit.dto.LabConfigurationDto;
import com.param.global.unit.dto.MpesaApplicationConfigurationDto;
import com.param.global.unit.dto.UserRoleMasterDto;
import com.param.global.unit.service.ILabConfigurationService;
import com.param.global.utility.UserComponent;
import com.param.privilege.model.Privilege;
import com.param.privilege.service.PrivilegeService;
import com.param.security.config.JwtTokenUtil;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UserMasterServiceImpl implements IUserMasterService, ICommonConstants, IUserLoginConstants {
	final static Logger logger = Logger.getLogger(UserMasterServiceImpl.class);


	@Autowired
	private IUserMasterDao iUserMasterDao;

	@Autowired
	IDepartmentNursingStationMapperDao iDepartmentNursingStationMapperDao;

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	IMailingService iMailingService;

	@Autowired
	private PrivilegeService PrivilegeService;

	@Autowired
	IUnitApplicationBillingConfigurationService iUnitApplicationBillingConfigurationService;

	@Autowired
	IUserCounterMapperService iUserCounterMapperService;

	@Autowired
	private ILabConfigurationService iLabConfigurationService;

	@Autowired
	IGlobalUnitMasterDao iGlobalUnitMasterDao;

	@Autowired
	ITemplateMasterService iTemplateMasterService;

	@Autowired
	GlobalDao globalDao;

	@Autowired
	IMpesaAppConfigDao iMpesaAppConfigDao;

	@Autowired
	IGlobalUnitMasterService iGlobalUnitMasterService;

	@Autowired
	RestTemplate restTemplate;
	@Autowired
	IwhatsAppServiceImpl iwhatsAppServiceImpl;
	@Autowired
	TimeZoneComponent timeZoneComponent;

	@Autowired
	IProductionReleaseEmailNotificationConfigDao iProductionReleaseEmailNotificationConfigDao;

	@Autowired
	IHl7ConfigListDao iHl7ConfigListDao;

//	@Autowired
//	UserComponent userComponenet;
	
	// Regular expression patterns for validation
    private static final String UPPERCASE_REGEX = ".*[A-Z].*";
    private static final String LOWERCASE_REGEX = ".*[a-z].*";
    private static final String DIGIT_REGEX = ".*[0-9].*";
    private static final String SPECIAL_CHAR_REGEX = ".*[!@#$%^&*(),.?\":{}|<>].*";
    private static final int MIN_LENGTH = 8;

	@Override
	@Transactional
	public Response saveUserMaster(UserMasterDto userMasterDto) throws ApplicationException {
		try {
			return iUserMasterDao.saveUserMaster(userMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getOrganizationList(String url) throws ApplicationException {
		try {
			return iUserMasterDao.getOrganizationList(url);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getUnitListByOrganization(OrganizationMasterDto organizationMasterDto) throws ApplicationException {
		try {
			return iUserMasterDao.getUnitFromOrganizationId(organizationMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getUnitByCredentials(OrganizationMasterDto organizationMasterDto) throws ApplicationException {
		try {
			return iUserMasterDao.getUnitByCredentials(organizationMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response validateUniqueUserName(UserMasterDto userMasterDto) throws ApplicationException {
		try {
			return iUserMasterDao.validateUniqueUserName(userMasterDto);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	
	@Override
	@Transactional
	public Map<String,String> generateToken(Integer userId, Integer unitId) throws ApplicationException {
		Map<String,String> response = new HashMap<>();
		try {
			if(userId!=null && userId > 0){
				UserMasterDto masterDto = UserComponent.getUsers().stream().filter(user->user.getUserId().equals(userId)).findAny().orElse(null);
				response.put("token", JwtTokenUtil.generateToken(masterDto.getUserName(),unitId));
			}else{
				response.put("token",JwtTokenUtil.generateToken(unitId));
			}
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
		}
		return response;
	}
	
	@Override
	@Transactional
	public Response userLogin(UserMasterDto userMasterDto, HttpServletRequest request) throws ApplicationException {
		try {

			String subMsg = null;
			

			Response resUnit = iGlobalUnitMasterDao.getUnitById(userMasterDto.getUnitId());
			UnitMasterDto resUnitMasterDto = (UnitMasterDto) resUnit.getObject();


			String userName = userMasterDto.getUserName();
			String password = userMasterDto.getPassword();
			
	        String token = JwtTokenUtil.generateToken(userName,userMasterDto.getUnitId());
			
	        String encryptedPassword = GlobalCommonDateUtils.encrypt(password);
	        UserMasterDto failLogDto = new UserMasterDto();
	        Instant currentTimestamp = Instant.now();
            Timestamp otpCurrentTimeStamp = Timestamp.from(currentTimestamp);
			Response loginResponse = iUserMasterDao.userLogin(userMasterDto);
			if (loginResponse.getStatus().equals(SUCCESS)) {
				UserMasterDto masterDto = (UserMasterDto) loginResponse.getObject();

                masterDto.setUnitTimeZone(resUnitMasterDto.getUnitTimeZone());
				// token set
				masterDto.setAuthToken(token);
				
				
				Response failLog = iUserMasterDao.getFailedLog(masterDto);
				
				if (failLog.getStatus().equals(SUCCESS)) {
					
					failLogDto = (UserMasterDto) failLog.getObject();
					
					if(failLogDto.getLoginFailedCount() == 5) {
						
						Timestamp dbTimestamp = failLogDto.getLoginBlockTime();
						
						long differenceInMillis = currentTimestamp.toEpochMilli() - dbTimestamp.getTime();
						long hoursDifference = Math.abs(differenceInMillis / (1000 * 60 * 60));
						
						if(hoursDifference < 24) {
							
							return new Response(ERROR, INVALID_PWDEXPIRY_CODE, "Login attempt limit exhausted please try after 24 hours", null, null);
						}
						
					}
					
				}

				// Check If user name is valid
				if (masterDto.getUserName().equalsIgnoreCase(userName)) {
					// Check If password is valid
					if (masterDto.getPassword().equals(encryptedPassword)) {
						// code for checking user already login or not start
						UserLoginLogDto logDto = new UserLoginLogDto();
						logDto.setOrganizationId(userMasterDto.getOrganizationId());
						logDto.setUnitId(userMasterDto.getUnitId());
						logDto.setUserId(masterDto.getUserId());
						Response res = iUserMasterDao.getUserlogDetails(logDto);
						if (userMasterDto.getIsContinueLogin() != null && userMasterDto.getIsContinueLogin() == 'Y') {
							if (res.getListObject() != null && res.getListObject().size() > 0) {
								Iterator<UserLoginLogDto> itr = res.getListObject().iterator();
								while (itr.hasNext()) {
									UserLoginLogDto dto = itr.next();
									// update previous user login as logout
									if (userMasterDto.getMacAddress().equals(dto.getMacAddress())) {
										dto.setStatus('I');
									} else {
										dto.setStatus('C');
									}
									dto.setIsLogin('N');
									dto.setIsLogout('Y');

									iUserMasterDao.updateUserLog(dto);
								}
							}
						} else {

							if (res.getListObject() != null && res.getListObject().size() > 0) {
								return new Response(SUCCESS, SUCCESS_CODE, "You are already logged in...!!", null, 1);
							}

							
							  if(res.getListObject()!=null && res.getListObject().size()>0){
							  Iterator<UserLoginLogDto> itr=res.getListObject().iterator();
							  while(itr.hasNext()){ UserLoginLogDto dto=itr.next();
							  
								  // update previous user login as logout dto.setIsLogin('N');
								  dto.setIsLogout('Y'); dto.setStatus('C'); 
								  iUserMasterDao.updateUserLog(dto);
							  }
							  
							 }

						}
						// new entry for current user as login
						logDto.setCreatedBy(userMasterDto.getCreatedBy());
						logDto.setUpdatedBy(userMasterDto.getUpdatedBy());
						logDto.setIsLogin('Y');
						logDto.setIsLogout('N');
						logDto.setStatus('A');
						logDto.setMacAddress(userMasterDto.getMacAddress());

						iUserMasterDao.saveUserLogDetails(logDto);
						iUserMasterDao.updateLoginAttempt(null,0,masterDto.getUserId());
						// code for checking user already login or not end

						DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
						Date currentDater = TimeZoneComponent.getDateByzone(userMasterDto.getUnitId());
						Date expDate = dateFormat.parse(masterDto.getPwdExpiry());
						if (currentDater.before(expDate)) {
							if (masterDto.getStatus().equals("A")) {

								//Subscription Check
								String subscriptionWarningMessage = null;
								String code = VALID_PWD_CODE;
								Response subScritpionRes = iGlobalUnitMasterService.checkUnitSubscription(resUnitMasterDto,masterDto.getUserId());
								if(subScritpionRes.getStatus().equalsIgnoreCase(ERROR)) {
									return subScritpionRes;
								}else{
									subscriptionWarningMessage = subScritpionRes.getMessage();
									code = subScritpionRes.getCode();
								}


								OrganizationUnitUserMapperDto organizationUnitUserMapperDto = new OrganizationUnitUserMapperDto();
								organizationUnitUserMapperDto.setOrganizationlId(userMasterDto.getOrganizationId());
								organizationUnitUserMapperDto.setUnitId(userMasterDto.getUnitId());
								organizationUnitUserMapperDto.setUserId(masterDto.getUserId());
								Response userRoleRes = iUserMasterDao.getUserRoleByUserId(masterDto.getUserId(),
										masterDto.getOrganizationId(), masterDto.getUnitId());
								masterDto.setRoleIds(userRoleRes.getListObject());

								// return new Response(SUCCESS, VALID_PWD_CODE, VALID_PWD_MSG, null,masterDto);
								this.saveSSODetails(masterDto);
								/*UserSSODto userSSODetails = (UserSSODto) sessionFactory.getCurrentSession().createQuery(
										"SELECT sso.token as token from UserSSODto sso where sso.userId=:userId")
										.setInteger("userId", masterDto.getUserId())
										.setResultTransformer(Transformers.aliasToBean(UserSSODto.class))
										.uniqueResult();
								masterDto.setSsoToken(userSSODetails.getToken());*/

								// get application billing config data
								UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
								unitApplicationConfigurationDto.setUnitId(userMasterDto.getUnitId());
								unitApplicationConfigurationDto.setOrganizationId(userMasterDto.getOrganizationId());
								Response unitApplicationConfigurationRes = iUnitApplicationBillingConfigurationService
										.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
								masterDto.setUnitApplicationConfigurationDto(
										(UnitApplicationConfigurationDto) unitApplicationConfigurationRes.getObject());

								unitApplicationConfigurationDto  = (UnitApplicationConfigurationDto) sessionFactory.getCurrentSession().createSQLQuery(
										"select duplicate_tab as \"duplicateTab\"  from t_unit_application_billing_configuration where unit_id  = :unitId and status = 'A' ")
										.setParameter("unitId", masterDto.getUnitId())
										.setResultTransformer(Transformers.aliasToBean(UnitApplicationConfigurationDto.class))
										.uniqueResult();

								masterDto.getUnitApplicationConfigurationDto().setDuplicateTab(unitApplicationConfigurationDto.getDuplicateTab());
								// Security fix, docs/DECISION_NEEDED_CookieAppConfigCredentialEcho.md --
								// this DTO is serialized into the login response and copied into the
								// browser's cookieObjectAppConfig cookie by loginController.js; strip
								// secret-shaped fields before it ever reaches the client. Explicit here
								// as defense-in-depth (the shared service above already strips them too).
								masterDto.getUnitApplicationConfigurationDto().stripClientFacingSecrets();
								
								// get user counter mapper data
								Response userCounterRes = iUserCounterMapperService.getUserCounterMapperViewById(
										userMasterDto.getOrganizationId(), userMasterDto.getUnitId(),
										masterDto.getUserId());
								masterDto.setUserCounterMapperDtosList(userCounterRes.getListObject());
								ApplicationConfiguration appConfiguration = iUserMasterDao.getApplicationConfiguration(
										userMasterDto.getOrganizationId(), userMasterDto.getUnitId());
								if (appConfiguration != null) {
									masterDto.setSkipPharmacySettlement(
											appConfiguration.getIsSkipPharmacySettlement() != null
													? appConfiguration.getIsSkipPharmacySettlement()
													: true);
									
									masterDto.setPoApproval(
											appConfiguration.getIsPoApproval() != null
													? appConfiguration.getIsPoApproval()
													: false);
									
									masterDto.setAutoBatch(appConfiguration.getAutoBatch());
									masterDto.setatCopRequired(appConfiguration.getatCopRequired());
									masterDto.setAutoPrescribedQty(appConfiguration.getAutoPrescribedQty());
								} else {
									masterDto.setSkipPharmacySettlement(true);
									masterDto.setPoApproval(false);

								}
								// get Lab Configuration data
								Response labRes = iLabConfigurationService.getLabConfigurationByOrgUnit(
										userMasterDto.getOrganizationId(), userMasterDto.getUnitId());
								masterDto.setLabConfigurationDto((LabConfigurationDto) labRes.getObject());
								
								
								// get Template Configuration data
								UnitTemplateConfigDto reqDto = new UnitTemplateConfigDto();
								reqDto.setUnitId(userMasterDto.getUnitId());
								
								Response templateConfig = iTemplateMasterService.getTemplateConfigByUnit(reqDto);
								masterDto.setUnitTemplateConfigDto((UnitTemplateConfigDto) templateConfig.getObject());

								// Privilege
								String dashboardState = null;
								List<Integer> roleIds = new LinkedList<>();
								for (UserRoleMasterDto dto : masterDto.getRoleIds()) {
									roleIds.add(dto.getRoleId());

									if (masterDto.getUnitApplicationConfigurationDto().getDocRoleId() != null
											&& dto.getRoleId() == masterDto.getUnitApplicationConfigurationDto()
													.getDocRoleId()) {
										if (dashboardState == null && dto.getDashboadSate() != null) {
											dashboardState = dto.getDashboadSate();
										}
									} else if (masterDto.getUnitApplicationConfigurationDto().getNurseRoleId() != null
											&& dto.getRoleId() == masterDto.getUnitApplicationConfigurationDto()
													.getNurseRoleId()) {
										if (dashboardState == null && dto.getDashboadSate() != null) {
											dashboardState = dto.getDashboadSate();
										}
									} else if (dashboardState == null) {
										dashboardState = dto.getDashboadSate();
									}

								}
								masterDto.setDashboardState(dashboardState);
								masterDto.setResponseEntity(getPriviledesByRoleIds(userMasterDto.getOrganizationId(),
										userMasterDto.getUnitId(), roleIds));
								masterDto.setSubscrptionMsg(subMsg);
								masterDto.setSubscriptionEndDateUnitMaster(resUnitMasterDto.getSubscriptionDate());
								
								MpesaApplicationConfigurationDto mpesaApplicationConfigurationDto = new MpesaApplicationConfigurationDto();
								mpesaApplicationConfigurationDto.setUnitId(userMasterDto.getUnitId());
								mpesaApplicationConfigurationDto.setOrganizationId(userMasterDto.getOrganizationId());
								
								Response mpesa = iMpesaAppConfigDao.getMpesaAppConfig(mpesaApplicationConfigurationDto);
								
								if(mpesa.getObject() != null) {
									masterDto.setMpesaRequired(true);
								}else {
									masterDto.setMpesaRequired(false);
								}
								
								return new Response(SUCCESS, code, Optional.ofNullable(subscriptionWarningMessage).orElse(VALID_PWD_MSG), null, masterDto,subScritpionRes.getResObject());

							} else {
								return new Response(ERROR, INVALID_USERSTATUS_CODE, INVALID_USERSTATUS_MSG, null, null);

							}

						} else {
							return new Response(ERROR, INVALID_PWDEXPIRY_CODE, INVALID_PWDEXPIRY_MSG, null, null);
						}

					} else {
						iUserMasterDao.updateLoginAttempt(otpCurrentTimeStamp,(failLogDto.getLoginFailedCount() +1 ),masterDto.getUserId());
						return new Response(ERROR, INVALID_PWD_CODE, INVALID_PWD_MSG, null, null);
					}

				} else {
					iUserMasterDao.updateLoginAttempt(otpCurrentTimeStamp,(failLogDto.getLoginFailedCount() +1 ),masterDto.getUserId());
					return new Response(ERROR, INVALID_PWD_CODE, INVALID_USER_MSG, null, null);
				}
			}else {
				iUserMasterDao.updateLoginAttempt(otpCurrentTimeStamp,(failLogDto.getLoginFailedCount() +1 ),userMasterDto.getUserId());
				return new Response(ERROR, INVALID_PWD_CODE, INVALID_USER_MSG, null, null);
			}
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		

	}

	public void saveSSODetails(UserMasterDto masterDto) {
		UserSSODto ssoObj = new UserSSODto();

		ssoObj.setUserId(masterDto.getUserId());
		ssoObj.setLastLogin(System.currentTimeMillis());
		ssoObj.setExpiry(System.currentTimeMillis() + 3600000);
		ssoObj.setToken(UUID.randomUUID().toString());

		Session session = sessionFactory.getCurrentSession();
		UserSSODto dbObj = (UserSSODto) session.get(UserSSODto.class, ssoObj.getUserId());
		if (dbObj == null) {
			session.save(ssoObj);
		} else {
			dbObj.setLastLogin(ssoObj.getLastLogin());
			dbObj.setExpiry(ssoObj.getExpiry());
			session.update(dbObj);
		}

		/*
		 * OrganismMaster org=new OrganismMaster();
		 * org.setOrgId(masterDto.getOrganizationId());
		 * 
		 * Unit unit=new Unit(); unit.setUnitId(masterDto.getUnitId());
		 */
	}

	@Override
	@Transactional
	public Response getUsersByName(UserMasterDto userMasterDto) throws ApplicationException {
		try {
			return iUserMasterDao.getUsersByName(userMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response resetPassword(UserMasterDto userMasterDto) {
		try {
			
			if (isValidPassword(userMasterDto.getUserPwd())) {
				return iUserMasterDao.resetPassword(userMasterDto);
	        } else {
	        	return new Response(ERROR, null, "Password must be 8 to 16 characters long and include uppercase letters, lowercase letters, digits, and special characters.", null, null);
	        }


		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response checkEmailAvailability(UserMasterDto userMasterDto) {
		try {
			return iUserMasterDao.checkEmailAvailability(userMasterDto);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response otpVerification(UserMasterDto userMasterDto) {
		try {
			return iUserMasterDao.otpVerification(userMasterDto);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response forgotPasswordGeneration(UserMasterDto userMasterDto) {
		try {
			
			 if (userMasterDto.getPassword() != null) {
				 if (isValidPassword(userMasterDto.getPassword())) {
					 		   iUserMasterDao.updateLoginAttempt(null,0,userMasterDto.getUserId());
						return iUserMasterDao.forgotPasswordGeneration(userMasterDto);
			        } else {
			        	return new Response(ERROR, null, "Password must be 8 to 16 characters long and include uppercase letters, lowercase letters, digits, and special characters.", null, null);
			        }
	            }else {
	            	return new Response(ERROR, null, "Password must be 8 to 16 characters long and include uppercase letters, lowercase letters, digits, and special characters.", null, null);
	            }
			
			
		

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response resendUserName(UserMasterDto userMasterDto) {
		try {
			return iUserMasterDao.resendUserName(userMasterDto);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response resendUserNameAndPassword(UserMasterDto userMasterDto) {
		try {
			return iUserMasterDao.resendUserNameAndPassword(userMasterDto);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getUnitName(UserMasterDto userMasterDto) {
		try {
			return iUserMasterDao.getUnitName(userMasterDto);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getPass() {
		try {
			return iUserMasterDao.getPass();

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getUserNameById(UserMasterDto userMasterDto) {
		try {
			return iUserMasterDao.getUserNameById(userMasterDto);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response userLoginForVitalUploader(UserMasterDto userMasterDto) {
		try {
			String userName = userMasterDto.getUserName();
			String password = userMasterDto.getPassword();

			String encryptedPassword = GlobalCommonDateUtils.encrypt(password);

			Response loginResponse = iUserMasterDao.userLogin(userMasterDto);
			if (loginResponse.getStatus().equals(SUCCESS)) {
				UserMasterDto masterDto = (UserMasterDto) loginResponse.getObject();
				// Check If user name is valid
				if (masterDto.getUserName().equalsIgnoreCase(userName)) {
					// Check If password is valid
					if (masterDto.getPassword().equals(encryptedPassword)) {

						DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
						Date currentDater = TimeZoneComponent.getDateByzone(userMasterDto.getUnitId());
						Date expDate = dateFormat.parse(masterDto.getPwdExpiry());
						if (currentDater.before(expDate)) {
							if (masterDto.getStatus().equals("A")) {

								OrganizationUnitUserMapperDto organizationUnitUserMapperDto = new OrganizationUnitUserMapperDto();
								organizationUnitUserMapperDto.setOrganizationlId(userMasterDto.getOrganizationId());
								organizationUnitUserMapperDto.setUnitId(userMasterDto.getUnitId());
								organizationUnitUserMapperDto.setUserId(masterDto.getUserId());
								/*
								 * Response hospUnitResponse =
								 * iUserMasterDao.getHospitalUnitUserForLogin(organizationUnitUserMapperDto);
								 * //System.out.println("4"); if(hospUnitResponse.getListObject() != null &&
								 * hospUnitResponse.getListObject().size() > 0){
								 * 
								 */ Response userRoleRes = iUserMasterDao.getUserRoleByUserId(masterDto.getUserId(),
										masterDto.getOrganizationId(), masterDto.getUnitId());
								masterDto.setRoleIds(userRoleRes.getListObject());
								// return new Response(SUCCESS, VALID_PWD_CODE, VALID_PWD_MSG, null,masterDto);
								this.saveSSODetails(masterDto);
								/*UserSSODto userSSODetails = (UserSSODto) sessionFactory.getCurrentSession().createQuery(
										"SELECT sso.token as token from UserSSODto sso where sso.userId=:userId")
										.setInteger("userId", masterDto.getUserId())
										.setResultTransformer(Transformers.aliasToBean(UserSSODto.class))
										.uniqueResult();
								masterDto.setSsoToken(userSSODetails.getToken());*/
								List<UserMasterDto> list = new ArrayList<>();
								list.add(masterDto);
								return new Response(SUCCESS, VALID_PWD_CODE, VALID_PWD_MSG, list, null);
							} else {
								return new Response(ERROR, INVALID_USERSTATUS_CODE, INVALID_USERSTATUS_MSG, null, null);

							}

						} else {
							return new Response(ERROR, INVALID_PWDEXPIRY_CODE, INVALID_PWDEXPIRY_MSG, null, null);
						}

					} else {
						return new Response(ERROR, INVALID_PWD_CODE, INVALID_PWD_MSG, null, null);
					}

				} else {
					return new Response(ERROR, INVALID_PWD_CODE, INVALID_USER_MSG, null, null);
				}
			}
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		return new Response(ERROR, INVALID_USER_CODE, INVALID_USER_MSG, null, null);

	}

	private ResponseEntity<?> getPriviledesByRoleIds(int orgId, int unitId, List<Integer> roleIds) {

		Map<String, Object> responseObject;
		JSONArray moduleList = new JSONArray();

		try {
			// JsonReader jsonReader = Json.createReader(new StringReader(json));
			// JsonObject reader = jsonReader.readObject();
			// JsonArray roleIds = reader.getJsonArray("roleIds");
			// Integer organizationId = reader.getInt("organizationId");
			// Integer unitId = reader.getInt("unitId");
			for (int i = 0; i < roleIds.size(); i++) {
				int roleId = roleIds.get(i);
				List<Privilege> privileges = PrivilegeService.getPrivilegeByRoleID(orgId, unitId, roleId);
				if (!privileges.isEmpty()) {
					for (Privilege privilege : privileges) {

						String modulesJSONString = privilege.getModules();

						JsonReader modulesJsonReader = Json
								.createReader(new StringReader("{ \"modules\" : " + modulesJSONString + " }"));
						JsonObject modulesArrayJsonObject = modulesJsonReader.readObject();
						JsonArray moduleJSONArray = modulesArrayJsonObject.getJsonArray("modules");

						for (int j = 0; j < moduleJSONArray.size(); j++) {
							JsonObject moduleJSONObject = (JsonObject) moduleJSONArray.get(j);
							String jsonModule = moduleJSONObject.toString();
							JSONParser parser1 = new JSONParser();
							JSONObject jsonModuleObject = (JSONObject) parser1.parse(jsonModule);
							if (!moduleList.contains(jsonModuleObject)) {
								moduleList.add(jsonModuleObject);
							}
						}

					}

				}

			}

			List<JSONObject> responseList = PrivilegeService.prepareResponse(moduleList);

			responseObject = new HashMap<>();
			responseObject.put("status", "success");
			responseObject.put("modules", responseList);

		} catch (Exception e) {
		logger.error("getPriviledesByRoleIds failed", e);
			responseObject = new HashMap<>();
			responseObject.put("status", "fail");
			responseObject.put("error", e.toString());
			return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(responseObject, HttpStatus.OK);
	}

	@Override
	@Transactional
	public Response getPasswordAndUserName() throws ApplicationException {
		try {
			return iUserMasterDao.getPasswordAndUserName();

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	public static List<String> getMACANDIP() {

		InetAddress ip;
		try {
			ip = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			byte[] mac = network.getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
			}
			List<String> list = new LinkedList<String>();
			list.add(ip.getHostAddress());
			list.add(sb.toString());
			return list;
		} catch (Exception e) {
		logger.error("getMACANDIP failed", e);
		}
		return null;
	}

	@Override
	@Transactional
	public Response saveUserLogDetails(UserLoginLogDto dto) {
		try {
			Response res = iUserMasterDao.getUserlogDetailsByMacAddress(dto);

			if (res.getListObject() != null && res.getListObject().size() > 0) {
				Iterator<UserLoginLogDto> itr = res.getListObject().iterator();
				while (itr.hasNext()) {
					UserLoginLogDto dto1 = itr.next();

					// update previous user login as logout
					dto1.setStatus('I');
					iUserMasterDao.updateUserLog(dto1);

					// new entry for logout
					dto1.setIsLogin('N');
					dto1.setIsLogout('Y');
					dto1.setStatus('I');
					iUserMasterDao.saveUserLogDetails(dto1);

				}
			} else {
				// System.out.println("nothing to save");
			}
			/*
			 * if(res.getStatus()==SUCCESS && res.getListObject()!=null &&
			 * res.getListObject().size()>0){ UserLoginLogDto
			 * logDto1=(UserLoginLogDto)res.getListObject().get(0);
			 * if(logDto1.getIsLogin()!=null && logDto1.getIsLogin()=='Y' &&
			 * logDto1.getStatus()!=null && logDto1.getStatus()=='A'){
			 * logDto1.setIsLogin('N'); logDto1.setStatus('I');
			 * logDto1.setMacAddress(logDto1.getMacAddress()); return
			 * iUserMasterDao.saveUserLogDetails(logDto1); } }else{
			 * //System.out.println("nothing to save"); }
			 */

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		return null;
	}

	/*
	 * @Override public Response checkOtherLogin(UserLoginLogDto dto) throws
	 * ApplicationException { // TODO Auto-generated method stub return null; }
	 */
	@Override
	@Transactional
	public Response checkOtherLoginByMacAddress(UserLoginLogDto dto) throws ApplicationException {
		try {

			return iUserMasterDao.checkOtherLoginByMacAddress(dto);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response updatePreviousLogin(UserLoginLogDto dto) throws ApplicationException {
		try {
			Response res = iUserMasterDao.updatePreviousLogin(dto);
			if (res.getStatus().equalsIgnoreCase(SUCCESS)) {
				dto.setIsLogin('N');
				dto.setIsLogout('Y');
				dto.setStatus('I');
				return iUserMasterDao.saveUserLogDetails(dto);
			}
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		return null;
	}

	@Override
	@Transactional
	public Response checkOtherLoginByUserId(UserLoginLogDto dto) throws ApplicationException {
		try {

			return iUserMasterDao.checkOtherLoginByUserId(dto);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getUserListOfSchedulingForUpdate(List<Integer> unitIds) throws ApplicationException {
		try {

			return iUserMasterDao.getUserListOfSchedulingForUpdate(unitIds);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response updateSchedulingLog(List<Integer> unitIds) throws ApplicationException {
		try {
			List<Integer> ids = new LinkedList<>();
			Response res = iUserMasterDao.getUserListOfSchedulingForUpdate(unitIds);
			if (res.getListObject() != null && res.getListObject().size() > 0) {
				Iterator<UserLoginLogDto> itr = res.getListObject().iterator();
				while (itr.hasNext()) {
					UserLoginLogDto dto = itr.next();
					ids.add(dto.getLogId());
				}
				iUserMasterDao.updateSchedulingLog(ids);
			}

		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	@Override
	@Transactional
	public String getAuthToken(HttpServletRequest request) throws ApplicationException {
		try {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", AuthKey.getAuthProperties().getUserName()));
			params.add(new BasicNameValuePair("password", AuthKey.getAuthProperties().getPassword()));
			params.add(new BasicNameValuePair("grant_type", "password"));

			CloseableHttpClient httpclient = HttpClientBuilder.create().build();

			HttpPost post = new HttpPost(ServletUtils.getBaseUrl(request) + "/oauth/token");
			post.addHeader("Content-Type", "application/x-www-form-urlencoded");

			post.addHeader("Accept", "application/json");

			String auth = AuthKey.getAuthProperties().getSecretKey() + ":"
					+ AuthKey.getAuthProperties().getSecretPassword();
			byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.ISO_8859_1));
			String authHeader = "Basic " + new String(encodedAuth);
			post.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
			post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			HttpResponse response = httpclient.execute(post);
			if (response != null) {
				if (response.getStatusLine().getStatusCode() == Integer.valueOf(SUCCESS_CODE)) {
					String json = EntityUtils.toString(response.getEntity());
					org.json.JSONObject jsonObj = new org.json.JSONObject(json);
					String authToken = jsonObj.getString("value");
					if (authToken != null && !authToken.isEmpty())
						return authToken;
					else
						throw new IOException("Failed to generate auth token");
				} else if (response.getStatusLine().getStatusCode() == Integer.valueOf(UNAUTHORISED_CODE)) {
					throw new IOException("Unauthorised User");
				}
			} else {
				throw new IOException("Failed to generate auth token");

			}
			return null;
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}

	}

	@Override
	@Transactional
	public Response getUsersByNameOnType(UserMasterDto userMasterDto) throws ApplicationException {
		try {

			return iUserMasterDao.getUsersByNameOnType(userMasterDto);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getPharmacyUserRoleType(UserLoginLogDto dto) throws ApplicationException {
		try {

			return iUserMasterDao.getPharmacyUserRoleType(dto);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response updatePassword(UserMasterDto dto) throws ApplicationException {
		try {

			 if (dto.getPassword() != null) {
				 if (isValidPassword(dto.getPassword())) {
			            return iUserMasterDao.updatePassword(dto);
			        } else {
			        	return new Response(ERROR, null, "Password must be 8 to 16 characters long and include uppercase letters, lowercase letters, digits, and special characters.", null, null);
			        }
	            }else {
	            	return iUserMasterDao.updatePassword(dto);
	            }



		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response validateLoginForDoctorApp(UserMasterDto userMasterDto) throws ApplicationException {

		try {

	
			String userName = userMasterDto.getUserName();
			String password = userMasterDto.getPassword();
			/*String authToken = this.getAuthToken(request);
			if (authToken == null) {
				return new Response(ERROR, UNAUTHORISED_CODE, UNAUTHORISED_USER, null, null);
			}*/

			String encryptedPassword = GlobalCommonDateUtils.encrypt(password);

			Response loginResponse = iUserMasterDao.validateLoginForDoctorApp(userMasterDto);
			if (loginResponse.getStatus().equals(SUCCESS)) {
				UserMasterDto masterDto = (UserMasterDto) loginResponse.getObject();

				// token set
				//masterDto.setAuthToken(authToken);

				// Check If user name is valid
				if (masterDto.getUserName().equalsIgnoreCase(userName)) {
					// Check If password is valid
					if (masterDto.getPassword().equals(encryptedPassword)) {
						
						//get mapped unit list to doctor
						OrganizationMasterDto orgDto=new OrganizationMasterDto();
						orgDto.setOrganizationId(userMasterDto.getOrganizationId());
						orgDto.setUserName(userMasterDto.getUserName());
						orgDto.setPassword(userMasterDto.getPassword());
						Response unitRes=iUserMasterDao.getUnitByCredentials(orgDto);
						if(unitRes.getStatus().equals("error")) {
							return unitRes;
						}
						if(unitRes.getListObject()!=null && !unitRes.getListObject().isEmpty())
						masterDto.setUnitList(unitRes.getListObject());
					
						UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
						unitApplicationConfigurationDto.setUnitId(masterDto.getUnitList().getFirst().getUnitId());
						unitApplicationConfigurationDto.setOrganizationId(userMasterDto.getOrganizationId());
						Response unitApplicationConfigurationRes = iUnitApplicationBillingConfigurationService
								.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
						masterDto.setUnitApplicationConfigurationDto((UnitApplicationConfigurationDto) unitApplicationConfigurationRes.getObject());
						// Security fix, docs/DECISION_NEEDED_CookieAppConfigCredentialEcho.md --
						// same client-facing login response path as above, explicit defense-in-depth.
						if (masterDto.getUnitApplicationConfigurationDto() != null) {
							masterDto.getUnitApplicationConfigurationDto().stripClientFacingSecrets();
						}

						DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
						Date currentDater = TimeZoneComponent.getDateByzone(userMasterDto.getUnitId());
						Date expDate = dateFormat.parse(masterDto.getPwdExpiry());
						if (currentDater.before(expDate)) {
							if (masterDto.getStatus().equals("A")) {
								//masterDto.setSubscrptionMsg(subMsg);
								return new Response(SUCCESS, VALID_PWD_CODE, VALID_PWD_MSG, null, masterDto);

							} else {
								return new Response(ERROR, INVALID_USERSTATUS_CODE, INVALID_USERSTATUS_MSG, null, null);

							}

						} else {
							return new Response(ERROR, INVALID_PWDEXPIRY_CODE, INVALID_PWDEXPIRY_MSG, null, null);
						}

					} else {
						return new Response(ERROR, INVALID_PWD_CODE, INVALID_PWD_MSG, null, null);
					}

				} else {
					return new Response(ERROR, INVALID_PWD_CODE, INVALID_USER_MSG, null, null);
				}
			}
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		return new Response(ERROR, INVALID_USER_CODE, INVALID_USER_MSG, null, null);

	
	}

	@Override
	@Transactional
	public Response getUserListForWebAppByOrgIdAndUnitId(UserMasterDto dto) throws ApplicationException {
		// TODO Auto-generated method stub
		try {

			 List<User> users = globalDao.getUser(dto.getOrganizationId(),dto.getUnitId());
				return new Response(SUCCESS, VALID_PWD_CODE, VALID_PWD_MSG, users, null);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getUserDetailsByUserName(String username) throws ApplicationException {
		try {

			return iUserMasterDao.getUserDetailsByUserName(username);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getLdapConfigurationData(DuoApplicationPropertiesDto duoApplicationPropertiesDto)
			throws ApplicationException {
		try {

			return iUserMasterDao.getLdapConfigurationData(duoApplicationPropertiesDto);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response userLoginForDuoAuthentication(UserMasterDto userMasterDto, HttpServletRequest request)
			throws ApplicationException {
		try {

			String subMsg = null;
			//Connection connection = null;
			// End For Subscription

			String userName = userMasterDto.getUserName();
			//String password = userMasterDto.getPassword();


			Response loginResponse = iUserMasterDao.userLoginForDuoAuthentication(userMasterDto);
			if (loginResponse.getStatus().equals(SUCCESS)) {
				UserMasterDto masterDto = (UserMasterDto) loginResponse.getObject();

				// token set
				masterDto.setAuthToken("1234567890qwertyuiopasdfghjklzxcvbnm");

				// Check If user name is valid
				if (masterDto.getUserName().equalsIgnoreCase(userName)) {
					// Check If password is valid
						// code for checking user already login or not start

						UserLoginLogDto logDto = new UserLoginLogDto();

						logDto.setOrganizationId(userMasterDto.getOrganizationId());
						logDto.setUnitId(userMasterDto.getUnitId());
						logDto.setUserId(masterDto.getUserId());
						Response res = iUserMasterDao.getUserlogDetails(logDto);
						if (userMasterDto.getIsContinueLogin() != null && userMasterDto.getIsContinueLogin() == 'Y') {
							if (res.getListObject() != null && res.getListObject().size() > 0) {
								Iterator<UserLoginLogDto> itr = res.getListObject().iterator();
								while (itr.hasNext()) {
									UserLoginLogDto dto = itr.next();
									// update previous user login as logout
									if (userMasterDto.getMacAddress().equals(dto.getMacAddress())) {
										dto.setStatus('I');
									} else {
										dto.setStatus('C');
									}
									dto.setIsLogin('N');
									dto.setIsLogout('Y');

									iUserMasterDao.updateUserLog(dto);
								}
							}
						} 
						// new entry for current user as login
						logDto.setCreatedBy(userMasterDto.getCreatedBy());
						logDto.setUpdatedBy(userMasterDto.getUpdatedBy());
						logDto.setIsLogin('Y');
						logDto.setIsLogout('N');
						logDto.setStatus('A');
						logDto.setMacAddress(userMasterDto.getMacAddress());

						iUserMasterDao.saveUserLogDetails(logDto);

						// code for checking user already login or not end

						DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
						Date currentDater = TimeZoneComponent.getDateByzone(userMasterDto.getUnitId());
						Date expDate = dateFormat.parse(masterDto.getPwdExpiry());
						if (currentDater.before(expDate)) {
							if (masterDto.getStatus().equals("A")) {

								//Subscription Check
								String subscriptionWarningMessage = null;
								String code = VALID_PWD_CODE;
								UnitMasterDto unitMasterDto = new UnitMasterDto();
								unitMasterDto.setOrganizationId(masterDto.getOrganizationId());
								unitMasterDto.setUnitId(masterDto.getUnitId());
								Response subScritpionRes = iGlobalUnitMasterService.checkUnitSubscription(unitMasterDto,masterDto.getUserId());
								if(subScritpionRes.getStatus().equalsIgnoreCase(ERROR)) {
									return subScritpionRes;
								}else{
									subscriptionWarningMessage = subScritpionRes.getMessage();
									code = subScritpionRes.getCode();
								}


								OrganizationUnitUserMapperDto organizationUnitUserMapperDto = new OrganizationUnitUserMapperDto();
								organizationUnitUserMapperDto.setOrganizationlId(userMasterDto.getOrganizationId());
								organizationUnitUserMapperDto.setUnitId(userMasterDto.getUnitId());
								organizationUnitUserMapperDto.setUserId(masterDto.getUserId());
								Response userRoleRes = iUserMasterDao.getUserRoleByUserId(masterDto.getUserId(),
										masterDto.getOrganizationId(), masterDto.getUnitId());
								masterDto.setRoleIds(userRoleRes.getListObject());

								// return new Response(SUCCESS, VALID_PWD_CODE, VALID_PWD_MSG, null,masterDto);
								this.saveSSODetails(masterDto);
								/*UserSSODto userSSODetails = (UserSSODto) sessionFactory.getCurrentSession().createQuery(
										"SELECT sso.token as token from UserSSODto sso where sso.userId=:userId")
										.setInteger("userId", masterDto.getUserId())
										.setResultTransformer(Transformers.aliasToBean(UserSSODto.class))
										.uniqueResult();
								masterDto.setSsoToken(userSSODetails.getToken());*/

								// get application billing config data
								UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
								unitApplicationConfigurationDto.setUnitId(userMasterDto.getUnitId());
								unitApplicationConfigurationDto.setOrganizationId(userMasterDto.getOrganizationId());
								Response unitApplicationConfigurationRes = iUnitApplicationBillingConfigurationService
										.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
								masterDto.setUnitApplicationConfigurationDto(
										(UnitApplicationConfigurationDto) unitApplicationConfigurationRes.getObject());
								// Security fix, docs/DECISION_NEEDED_CookieAppConfigCredentialEcho.md --
								// same client-facing login response path, explicit defense-in-depth.
								if (masterDto.getUnitApplicationConfigurationDto() != null) {
									masterDto.getUnitApplicationConfigurationDto().stripClientFacingSecrets();
								}
								// get user counter mapper data
								Response userCounterRes = iUserCounterMapperService.getUserCounterMapperViewById(
										userMasterDto.getOrganizationId(), userMasterDto.getUnitId(),
										masterDto.getUserId());
								masterDto.setUserCounterMapperDtosList(userCounterRes.getListObject());
								ApplicationConfiguration appConfiguration = iUserMasterDao.getApplicationConfiguration(
										userMasterDto.getOrganizationId(), userMasterDto.getUnitId());
								if (appConfiguration != null) {
									masterDto.setSkipPharmacySettlement(
											appConfiguration.getIsSkipPharmacySettlement() != null
													? appConfiguration.getIsSkipPharmacySettlement()
													: true);
									
									masterDto.setPoApproval(
											appConfiguration.getIsPoApproval() != null
													? appConfiguration.getIsPoApproval()
													: false);
									
									masterDto.setAutoBatch(appConfiguration.getAutoBatch());
									masterDto.setatCopRequired(appConfiguration.getatCopRequired());
									masterDto.setAutoPrescribedQty(appConfiguration.getAutoPrescribedQty());
								} else {
									masterDto.setSkipPharmacySettlement(true);
									masterDto.setPoApproval(false);

								}
								// get Lab Configuration data
								Response labRes = iLabConfigurationService.getLabConfigurationByOrgUnit(
										userMasterDto.getOrganizationId(), userMasterDto.getUnitId());
								masterDto.setLabConfigurationDto((LabConfigurationDto) labRes.getObject());
								
								
								// get Template Configuration data
								UnitTemplateConfigDto reqDto = new UnitTemplateConfigDto();
								reqDto.setUnitId(userMasterDto.getUnitId());
								
								Response templateConfig = iTemplateMasterService.getTemplateConfigByUnit(reqDto);
								masterDto.setUnitTemplateConfigDto((UnitTemplateConfigDto) templateConfig.getObject());

								// Privilege
								String dashboardState = null;
								List<Integer> roleIds = new LinkedList<>();
								for (UserRoleMasterDto dto : masterDto.getRoleIds()) {
									roleIds.add(dto.getRoleId());

									if (masterDto.getUnitApplicationConfigurationDto().getDocRoleId() != null
											&& dto.getRoleId() == masterDto.getUnitApplicationConfigurationDto()
													.getDocRoleId()) {
										if (dashboardState == null && dto.getDashboadSate() != null) {
											dashboardState = dto.getDashboadSate();
										}
									} else if (masterDto.getUnitApplicationConfigurationDto().getNurseRoleId() != null
											&& dto.getRoleId() == masterDto.getUnitApplicationConfigurationDto()
													.getNurseRoleId()) {
										if (dashboardState == null && dto.getDashboadSate() != null) {
											dashboardState = dto.getDashboadSate();
										}
									} else if (dashboardState == null) {
										dashboardState = dto.getDashboadSate();
									}

								}
								masterDto.setDashboardState(dashboardState);
								masterDto.setResponseEntity(getPriviledesByRoleIds(userMasterDto.getOrganizationId(),
										userMasterDto.getUnitId(), roleIds));
								masterDto.setSubscrptionMsg(subMsg);
								return new Response(SUCCESS, code, Optional.ofNullable(subscriptionWarningMessage).orElse(VALID_PWD_MSG), null, masterDto,subScritpionRes.getResObject());

							} else {
								return new Response(ERROR, INVALID_USERSTATUS_CODE, INVALID_USERSTATUS_MSG, null, null);

							}

						} else {
							return new Response(ERROR, INVALID_PWDEXPIRY_CODE, INVALID_PWDEXPIRY_MSG, null, null);
						}

					

				} else {
					return new Response(ERROR, INVALID_PWD_CODE, INVALID_USER_MSG, null, null);
				}
			}
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		return new Response(ERROR, INVALID_USER_CODE, INVALID_USER_MSG, null, null);

	}

	@Override
	@Transactional
	public Response getUnitByCredentialsForDuoLogin(OrganizationMasterDto organizationMasterDto)
			throws ApplicationException {
		try {
			return iUserMasterDao.getUnitByCredentialsForDuoLogin(organizationMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getCookieDataForNewEMR(UserMasterDto userMasterDto) throws ApplicationException {
		try {

			String subMsg = null;
			Connection connection = null;
			
			// End For Subscription
			String userName = userMasterDto.getUserName();
			String password = userMasterDto.getPassword();
			String encryptedPassword = GlobalCommonDateUtils.encrypt(password);

			Response loginResponse = iUserMasterDao.getUserDataById(userMasterDto);
			if (loginResponse.getStatus().equals(SUCCESS)) {
				UserMasterDto masterDto = (UserMasterDto) loginResponse.getObject();

				// token set
				masterDto.setAuthToken("1234567890qwertyuiopasdfghjklzxcvbnm");

				// Check If user name is valid
//				if (masterDto.getUserName().equalsIgnoreCase(userName)) {
					// Check If password is valid
//					if (masterDto.getPassword().equals(encryptedPassword)) {
						// code for checking user already login or not start

						UserLoginLogDto logDto = new UserLoginLogDto();

						logDto.setOrganizationId(userMasterDto.getOrganizationId());
						logDto.setUnitId(userMasterDto.getUnitId());
						logDto.setUserId(masterDto.getUserId());
						Response res = iUserMasterDao.getUserlogDetails(logDto);
					

						// code for checking user already login or not end

						DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
						Date currentDater = TimeZoneComponent.getDateByzone(userMasterDto.getUnitId());
						Date expDate = dateFormat.parse(masterDto.getPwdExpiry());
						

								// get application billing config data
								UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
								unitApplicationConfigurationDto.setUnitId(userMasterDto.getUnitId());
								unitApplicationConfigurationDto.setOrganizationId(userMasterDto.getOrganizationId());
								Response unitApplicationConfigurationRes = iUnitApplicationBillingConfigurationService
										.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
								masterDto.setUnitApplicationConfigurationDto(
										(UnitApplicationConfigurationDto) unitApplicationConfigurationRes.getObject());
								// Security fix, docs/DECISION_NEEDED_CookieAppConfigCredentialEcho.md --
								// same client-facing login response path, explicit defense-in-depth.
								if (masterDto.getUnitApplicationConfigurationDto() != null) {
									masterDto.getUnitApplicationConfigurationDto().stripClientFacingSecrets();
								}
								// get user counter mapper data
								Response userCounterRes = iUserCounterMapperService.getUserCounterMapperViewById(
										userMasterDto.getOrganizationId(), userMasterDto.getUnitId(),
										masterDto.getUserId());
								masterDto.setUserCounterMapperDtosList(userCounterRes.getListObject());
								ApplicationConfiguration appConfiguration = iUserMasterDao.getApplicationConfiguration(
										userMasterDto.getOrganizationId(), userMasterDto.getUnitId());
								if (appConfiguration != null) {
									masterDto.setSkipPharmacySettlement(
											appConfiguration.getIsSkipPharmacySettlement() != null
													? appConfiguration.getIsSkipPharmacySettlement()
													: true);
									
									masterDto.setPoApproval(
											appConfiguration.getIsPoApproval() != null
													? appConfiguration.getIsPoApproval()
													: false);
									
									masterDto.setAutoBatch(appConfiguration.getAutoBatch());
									masterDto.setatCopRequired(appConfiguration.getatCopRequired());
									masterDto.setAutoPrescribedQty(appConfiguration.getAutoPrescribedQty());
								} else {
									masterDto.setSkipPharmacySettlement(true);
									masterDto.setPoApproval(false);

								}
								// get Lab Configuration data
								Response labRes = iLabConfigurationService.getLabConfigurationByOrgUnit(
										userMasterDto.getOrganizationId(), userMasterDto.getUnitId());
								masterDto.setLabConfigurationDto((LabConfigurationDto) labRes.getObject());
								
								
								// get Template Configuration data
								UnitTemplateConfigDto reqDto = new UnitTemplateConfigDto();
								reqDto.setUnitId(userMasterDto.getUnitId());
								
								Response templateConfig = iTemplateMasterService.getTemplateConfigByUnit(reqDto);
								masterDto.setUnitTemplateConfigDto((UnitTemplateConfigDto) templateConfig.getObject());

								// Privilege
								String dashboardState = null;
								List<Integer> roleIds = new LinkedList<>();
//								for (UserRoleMasterDto dto : masterDto.getRoleIds()) {
//									roleIds.add(dto.getRoleId());
//
//									if (masterDto.getUnitApplicationConfigurationDto().getDocRoleId() != null
//											&& dto.getRoleId() == masterDto.getUnitApplicationConfigurationDto()
//													.getDocRoleId()) {
//										if (dashboardState == null && dto.getDashboadSate() != null) {
//											dashboardState = dto.getDashboadSate();
//										}
//									} else if (masterDto.getUnitApplicationConfigurationDto().getNurseRoleId() != null
//											&& dto.getRoleId() == masterDto.getUnitApplicationConfigurationDto()
//													.getNurseRoleId()) {
//										if (dashboardState == null && dto.getDashboadSate() != null) {
//											dashboardState = dto.getDashboadSate();
//										}
//									} else if (dashboardState == null) {
//										dashboardState = dto.getDashboadSate();
//									}
//
//								}
								masterDto.setDashboardState(dashboardState);
								masterDto.setResponseEntity(getPriviledesByRoleIds(userMasterDto.getOrganizationId(),
										userMasterDto.getUnitId(), roleIds));
								masterDto.setSubscrptionMsg(subMsg);
								
								MpesaApplicationConfigurationDto mpesaApplicationConfigurationDto = new MpesaApplicationConfigurationDto();
								mpesaApplicationConfigurationDto.setUnitId(userMasterDto.getUnitId());
								mpesaApplicationConfigurationDto.setOrganizationId(userMasterDto.getOrganizationId());
								
								Response mpesa = iMpesaAppConfigDao.getMpesaAppConfig(mpesaApplicationConfigurationDto);
								
								if(mpesa.getObject() != null) {
									masterDto.setMpesaRequired(true);
								}else {
									masterDto.setMpesaRequired(false);
								}
								
								return new Response(SUCCESS, VALID_PWD_CODE, VALID_PWD_MSG, null, masterDto);

							
//					} else {
//						return new Response(ERROR, INVALID_PWD_CODE, INVALID_PWD_MSG, null, null);
//					}

				//} 
			}
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		return new Response(ERROR, INVALID_USER_CODE, INVALID_USER_MSG, null, null);

	}

	@Override
	@Transactional
	public List<UserMasterDto> getListOfUsers() throws ApplicationException {
		return iUserMasterDao.getListOfUsers();
	}

	@Override
	public Response saveSubscriptionDetails(UserMasterDto userMasterDto) throws ApplicationException {
		return iUserMasterDao.saveSubscriptionDetails(userMasterDto);
	}

	@Override
	public Response getSuperAdminUser(Integer orgId, Integer unitId, Integer userId) throws ApplicationException {
		return iUserMasterDao.getSuperAdminUser(orgId,unitId,userId);
	}

	@Override
	public Response getSubscriptionDetails(UserMasterDto userMasterDto) throws ApplicationException {
		return iUserMasterDao.getSubscriptionDetails(userMasterDto);
	}
	
	
	  public static boolean isValidPassword(String password) {
	        // Check password length
	        if (password.length() < MIN_LENGTH) {
	            return false;
	        }

	        // Compile the regular expressions
	        Pattern uppercasePattern = Pattern.compile(UPPERCASE_REGEX);
	        Pattern lowercasePattern = Pattern.compile(LOWERCASE_REGEX);
	        Pattern digitPattern = Pattern.compile(DIGIT_REGEX);
	        Pattern specialCharPattern = Pattern.compile(SPECIAL_CHAR_REGEX);

	        // Create matchers for the password
	        Matcher uppercaseMatcher = uppercasePattern.matcher(password);
	        Matcher lowercaseMatcher = lowercasePattern.matcher(password);
	        Matcher digitMatcher = digitPattern.matcher(password);
	        Matcher specialCharMatcher = specialCharPattern.matcher(password);

	        // Validate the password
	        return uppercaseMatcher.find() &&
	               lowercaseMatcher.find() &&
	               digitMatcher.find() &&
	               specialCharMatcher.find();
	    }

	@Override
	public String getAllowedDomainsList() {
		return iUserMasterDao.getAllowedDomainsList();
	}

	@Override
	@Transactional
	@Async
	public CompletableFuture<Response> createOrganizationFromWeb(OrganizationMasterDto organizationMasterForWebDto) throws ApplicationException {
		try{
			String stringResponse=null;
			String domain=null;


			Response res =  iUserMasterDao.createOrganizationFromWeb(organizationMasterForWebDto);

			if(SUCCESS.equals(res.getStatus())) {
				OrganizationMasterDto organizationMasterForWebDtoPass= (OrganizationMasterDto) res.getObject();
				Map<String, String> awsConfig = UserComponent.getAwsConfig();
				URI url = URI.create(awsConfig.get("url"));
				org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
				headers.set("Content-Type", "application/json");
				headers.set("x-api-key", awsConfig.get("apiKey"));

				Map<String, String> payload = new TreeMap<String, String>();
				payload.put("subdomain",organizationMasterForWebDto.getSubDomain());
				payload.put("beanstalkEnvironment", awsConfig.get("beanstalkEnvironment"));
				try{
					HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(payload, headers);
					stringResponse = restTemplate.postForObject(url, request, String.class);
				}catch(Exception awsApiE){
					logger.error(awsApiE.getMessage(), awsApiE);
					//retry if domain already exist
					payload.put("subdomain",organizationMasterForWebDto.getSubDomain()+1);
					organizationMasterForWebDto.setSubDomain(organizationMasterForWebDto.getSubDomain()+1);
					HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(payload, headers);
				 stringResponse = restTemplate.postForObject(url, request, String.class);
				}


				if(stringResponse!= null && !stringResponse.isEmpty()){

					ObjectMapper objectMapper = new ObjectMapper();

					JsonNode rootNode = objectMapper.readTree(stringResponse);

					domain = rootNode.get("domain").asText();

					System.out.println("Extracted Domain: " + domain);

				}

				String decryptedPassword  = GlobalCommonDateUtils.decrypt(organizationMasterForWebDtoPass.getPassword());
				organizationMasterForWebDto.setPassword(decryptedPassword);

				//as discussed sub domain always concatenated with .axonichealth.co.in
				organizationMasterForWebDto.setCompleteSubDomainName("https://"+domain);


				//refresh the user cache
				Integer userId = organizationMasterForWebDto.getUserId();
				String userName = organizationMasterForWebDto.getUserName();
				String unitZone = organizationMasterForWebDto.getUnitTimeZone();
				Integer unitId = organizationMasterForWebDto.getUnitId();

				OrganizationMasterDto dto = (OrganizationMasterDto) res.getObject();
				Response resList = iUserMasterDao.getAllUsersByOrgId(dto.getOrganizationId());

				List<OrganizationMasterDto> organizationUserDetailsList  = resList.getListObject();

				for (OrganizationMasterDto orgDto :organizationUserDetailsList){
					UserMasterDto userDto = new UserMasterDto();
					userDto.setUserId(orgDto.getUserId());
					userDto.setUserName(orgDto.getUserName());
					userDto.setStatus("A");
					UserComponent.updateUserDetailsForAuth(userDto);
				}

//				UserMasterDto userDto = new UserMasterDto();
//				userDto.setUserId(userId);
//				userDto.setUserName(userName);
//				userDto.setStatus("A");

//				UserComponent.updateUserDetailsForAuth(userDto);

				TimeZoneComponent.updateTimezoneMap(unitId,unitZone);

//				userComponenet.refreshDomainCache();
				UserComponent.refreshDomainCacheForAutomationFlow("http://"+domain,"https://"+domain);

				iwhatsAppServiceImpl.sendOrgCreationResponse(organizationMasterForWebDto);


			}else{
//              iwhatsAppServiceImpl.sendOrgCreationFailureResponse(organizationMasterForWebDto);
			}

			return CompletableFuture.completedFuture(res);
		} catch (Exception e)
		{
			throw new ApplicationException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional
	public Map<String, String> getawsConfigMap() throws ApplicationException {
		return iUserMasterDao.getawsConfigMap();
	}

	@Override
	public void refreshAllCaches(Integer userId, String userName) throws ApplicationException {
		timeZoneComponent.refreshTimeZoneMap();
		UserMasterDto userDto = new UserMasterDto();
		userDto.setUserId(userId);
		iUserMasterDao.getUserNameById(userDto);
		userDto.setUserName(userName);
		userDto.setStatus("A");
		UserComponent.updateUserDetailsForAuth(userDto);

	}





	@Override
	public Response sendNotificationOnEmailOfReleaseDetails() throws ApplicationException {
		try {
			return iProductionReleaseEmailNotificationConfigDao.sendNotificationOnEmailOfReleaseDetails();
		} catch (Exception e) {
		logger.error("sendNotificationOnEmailOfReleaseDetails failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	@Transactional
	public List<hl7ConfigListDto> gethl7ConfigList() throws ApplicationException {
		return iHl7ConfigListDao.gethl7ConfigList();
	}

	@Override
	@Transactional
	public Response getWebUserId(Integer unitId) throws ApplicationException {
		return iUserMasterDao.getWebUserId(unitId);
	}

	@Override
	@Transactional
	public Response generateSMSOtp(UserMasterDto userMasterDto,HttpServletRequest request) throws ApplicationException {
		return iUserMasterDao.generateSMSOtp(userMasterDto,request);
	}

	@Override
	@Transactional
	public Response validateSMSOtp(UserMasterDto userMasterDto, HttpServletRequest request) throws ApplicationException {
		return iUserMasterDao.validateSMSOtp(userMasterDto,request);
	}
}
