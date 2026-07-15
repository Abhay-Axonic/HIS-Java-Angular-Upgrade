package com.param.global.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.transaction.Transactional;

import io.minio.errors.*;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.param.assetManagement.maintenance.dto.VendorDto;
import com.param.entity.model.master.Area;
import com.param.entity.model.master.City;
import com.param.entity.model.master.Country;
import com.param.entity.model.master.PaymentMode;
import com.param.entity.model.master.State;
import com.param.entity.model.master.Vendor;
import com.param.entity.org.model.GeneralLedgerMaster;
import com.param.global.common.DocUploadUtility;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ScheduleActions;
import com.param.global.common.ScheduleTables;
import com.param.global.dao.IDoctorDocumentMapperDao;
import com.param.global.dao.IDoctorMasterDao;
import com.param.global.dao.IEmployeeRegistrationDao;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dao.IScheduleLogsDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dao.IUserMasterDao;
import com.param.global.dto.DependentDetailsDto;
import com.param.global.dto.DoctorDocumentMapperDto;
import com.param.global.dto.DoctorMasterDto;
import com.param.global.dto.DoctorSpecialityMapperDto;
import com.param.global.dto.EmployeeRegistrationDto;
import com.param.global.dto.OrderDetailsByEncounterIdDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.ScheduleLogsDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.dto.UserMasterDto;
import com.param.global.dto.UserUnitMapperDto;
import com.param.global.mail.IMailingService;
import com.param.global.model.CabinMaster;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.global.org.dao.IGeneralLedgerMasterServiceDao;
import com.param.global.org.dto.GeneralLedgerMasterDto;
import com.param.global.org.dto.UnitMasterDto;
import com.param.global.procedure.VO.DoctorMasterVO;
import com.param.global.procedure.VO.UserMasterVO;
import com.param.global.unit.dao.IUserIndetificationDetailsDaoImpl;
import com.param.global.unit.dao.IUserRoleMapperDao;
import com.param.global.unit.dto.OTIMSUserRoleMapperDto;
import com.param.global.unit.dto.UserIndetificationDetailsDto;
import com.param.global.unit.dto.UserRoleMapperDto;
import com.param.global.unit.dto.UserRoleMasterDto;
import com.param.global.utility.UserComponent;
import com.param.opd.appointment.dto.BookAppointmentDto;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.service.OtimsService.IOtimsService;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked","unused" })
public class DoctorMasterServiceImpl implements IDoctorMasterService,ICommonConstants {
	final static Logger logger = Logger.getLogger(DoctorMasterServiceImpl.class);


	@Autowired
	private IDoctorMasterDao iDoctorMasterDao;
	
	@Autowired
	IDependentDetailsService iDependentDetailsService;
	
	@Autowired
	private IScheduleLogsDao iScheduleLogsDao;
	
	@Autowired
	private IUserMasterDao iUserMasterDao;
	
	@Autowired
	private IUserRoleMapperDao iUserRoleMapperDao;
	
	 @Autowired
	 private  DocUploadUtility docUploadUtility;
	 
	 @Autowired
	 IMailingService iMailingService;
	 
	 @Autowired
	 IOtimsService iOtimsService;
		
	 @Autowired
	 IEmrService EmrService;
	 
	 @Autowired
	IEmployeeRegistrationDao iEmployeeRegistrationDao;

	 
	 @Autowired
     private IGlobalUnitMasterDao iGlobalUnitMasterDao;
	 
	 @Autowired
     private IDoctorDocumentMapperDao iDoctorDocumentMapperDao;
	
	 @Autowired
	 private IScheduleLogsDao isheduleDao;
	 
	 @Autowired
	 private IUserIndetificationDetailsDaoImpl iUserIndetificationDetailsDaoImpl;
	 
	 @Autowired
	 @Qualifier("applicationEventMulticaster")
	 private ApplicationEventPublisher applicationEventPublisher;
	 
	 @Autowired
	 private IEncounterMasterService iEncounterMasterService;
	 
	 @Autowired
	 private IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	 
	 @Autowired
	 IGeneralLedgerMasterServiceDao iGeneralLedgerMasterServiceDao;
	 
	 @Autowired
	SessionFactory sessionFactory;
	 
	// Define the length of the password
	    private static final int PASSWORD_LENGTH = 8; // Adjust as needed

	private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	private static final Random random = new Random();
	private static final String NUMBERS = "0123456789";
	private static final String KEY_STRING = "dgg@@@dhf===1234";
	 
	@Override
	public Response getDoctorMasterList(DoctorMasterDto doctorMasterDto)throws ApplicationException {
		try{
			return iDoctorMasterDao.getDoctorMasterList(doctorMasterDto);
		}catch(Exception e)
		{
		logger.error("getDoctorMasterList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveDoctorRegistration(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try {
			EmployeeRegistrationDto employeeRegistrationDto=new EmployeeRegistrationDto();
			employeeRegistrationDto.setUnitId(doctorMasterDto.getUnitId());
			employeeRegistrationDto.setOrganizationId(doctorMasterDto.getOrganizationId());
			Integer getConfiguredUserLimit=iEmployeeRegistrationDao.getConfiguredUserLimit(employeeRegistrationDto);
			Integer getUserCount=iEmployeeRegistrationDao.getUserCount(employeeRegistrationDto);
			if(getUserCount!=null && getConfiguredUserLimit!=null && getUserCount>=getConfiguredUserLimit)
			{
                return new Response(ERROR, ALREADY_EXIST_CODE, "You have exceeded user limit...!", null,null);
			}
			/*Response res = iDoctorMasterDao.checkUniqueDoctor(doctorMasterDto);
			if (res.getListObject().size() > 0) {
				return new Response(ERROR, null, "Already exist !!", null, null);
			}*/
			//code added by Ganesh 15 APR 2019
			Calendar c = Calendar.getInstance();
			c.setTime(TimeZoneComponent.getDateByzone(doctorMasterDto.getUnitId()));
			c.add(Calendar.YEAR, 1);
			Date expDate = c.getTime();
			String passwordExpiry = GlobalCommonDateUtils.getStringDate(expDate, "dd-MM-yyyy HH:mm:ss");
			
			UserMasterDto userMasterDto = new UserMasterDto();
				userMasterDto.setUserName(doctorMasterDto.getDocCode());
				
				String password = GlobalCommonDateUtils.generatePassword(PASSWORD_LENGTH);
				//String password = GlobalCommonDateUtils.generatePassword(doctorMasterDto.getFirstName(), doctorMasterDto.getLastName(), GlobalCommonDateUtils.getDate(doctorMasterDto.getDob(),"dd-M-yyyy HH:mm:ss"));
				
				String encryptedPed = GlobalCommonDateUtils.encrypt(password);
				userMasterDto.setPassword(encryptedPed);
				userMasterDto.setOrganizationId(doctorMasterDto.getOrganizationId());
				userMasterDto.setUnitId(doctorMasterDto.getUnitId());
				userMasterDto.setCreatedBy(doctorMasterDto.getCreatedBy());
				userMasterDto.setCreatedDate(doctorMasterDto.getCreatedDate());
				userMasterDto.setUpdatedBy(doctorMasterDto.getCreatedBy());
				userMasterDto.setUpdatedDate(doctorMasterDto.getCreatedDate());
				userMasterDto.setEmailId(doctorMasterDto.getEmail());
				userMasterDto.setStatus("A");
				userMasterDto.setPwdExpiry(passwordExpiry);
			Response userRes =  iUserMasterDao.saveUserMaster(userMasterDto);
			
			//updatingUserDetailsForAuth
			UserComponent.updateUserDetailsForAuth(userMasterDto);
			
			UserMasterVO userMasterVO = new UserMasterVO(); 
			userMasterVO.setUserId((int)userRes.getObject());
			userMasterVO.setUserName(doctorMasterDto.getDocCode());
			userMasterVO.setPassword(password);
			userMasterVO.setOrganizationId(doctorMasterDto.getOrganizationId());
			userMasterVO.setUnitId(doctorMasterDto.getUnitId());
			userMasterVO.setCreatedBy(doctorMasterDto.getCreatedBy());
			userMasterVO.setCreatedDate(doctorMasterDto.getCreatedDate());
			userMasterVO.setUpdatedBy(doctorMasterDto.getCreatedBy());
			userMasterVO.setUpdatedDate(doctorMasterDto.getCreatedDate());
			userMasterVO.setEmailId(doctorMasterDto.getEmail());
			userMasterVO.setFirstName(doctorMasterDto.getFirstName());
			userMasterVO.setLastName(doctorMasterDto.getLastName());
			userMasterVO.setPwdExpiry(passwordExpiry);
			
			userMasterVO.setSetPassword("true");
			userMasterVO.setStatus("A");
			
			
			//save for selected HIS role list
			if(doctorMasterDto.getSelectedHisRoleList().size()>0) {
				for(UserRoleMapperDto userRoleMapperDto:doctorMasterDto.getSelectedHisRoleList()) {
					userRoleMapperDto.setUserId(userMasterVO.getUserId());
					 iUserRoleMapperDao.saveUserRoleMapper(userRoleMapperDto);
				}
			}
			
			//save for selected OTIMS Role List
			if(doctorMasterDto.getSelectedOTIMSRoleList().size()>0) {
				for(OTIMSUserRoleMapperDto otimsUserRoleMapperDto:doctorMasterDto.getSelectedOTIMSRoleList()) {
					otimsUserRoleMapperDto.setUserId(userMasterVO.getUserId());
				  Response otimsRes=iEmployeeRegistrationDao.saveOTIMSUserRoleMapper(otimsUserRoleMapperDto);
				  
				}
			}
			
			//save for selected Unit MIS AND COVERSHEET TAB ACCESS  List
			if(doctorMasterDto.getSelectedUserUnitList().size()>0) {
				for(UserUnitMapperDto userUnitMapperDto:doctorMasterDto.getSelectedUserUnitList()) {

					userUnitMapperDto.setUserId(userMasterVO.getUserId());
					userUnitMapperDto.setMisRights(doctorMasterDto.getDefaultMISRights());
					userUnitMapperDto.setCoversheetRights(doctorMasterDto.getDefaultCoversheetRights());
				    iUserRoleMapperDao.saveUserUnitMapper(userUnitMapperDto);
				}
			}

			if(userRes.getStatus().equals(SUCCESS))
			{
				doctorMasterDto.setUserId((int)userRes.getObject());
			}
			
			doctorMasterDto.setPwdExpiry(passwordExpiry);
			Response res1 = iDoctorMasterDao.saveDoctorRegistration(doctorMasterDto);
			doctorMasterDto.setDoctorId((Integer) res1.getObject());
			
			DoctorMasterVO doctorMasterVO = new DoctorMasterVO();
			doctorMasterVO.setDoctorId(doctorMasterDto.getDoctorId());
			doctorMasterVO.setUserId(doctorMasterDto.getUserId());
			doctorMasterVO.setFirstName(doctorMasterDto.getFirstName());
			doctorMasterVO.setMiddleName(doctorMasterDto.getMiddleName());
			doctorMasterVO.setLastName(doctorMasterDto.getLastName());
			doctorMasterVO.setStatus(doctorMasterDto.getStatus());
			doctorMasterVO.setCreatedBy(doctorMasterDto.getCreatedBy());
			doctorMasterVO.setCreatedDate(doctorMasterDto.getCreatedDate());
			doctorMasterVO.setUpdatedBy(doctorMasterDto.getUpdatedBy());
			doctorMasterVO.setUpdatedDate(doctorMasterDto.getUpdatedDate());
			doctorMasterVO.setOrganizationId(doctorMasterDto.getOrganizationId());
			doctorMasterVO.setUnitId(doctorMasterDto.getUnitId());
			doctorMasterVO.setSpecialityId(doctorMasterDto.getSpecialityId());
			doctorMasterVO.setSubSpecialityId(doctorMasterDto.getSubSpecialityId());
			doctorMasterVO.setPrefixId(doctorMasterDto.getPrefixId());
			doctorMasterVO.setGenderId(doctorMasterDto.getGenderId());
			doctorMasterVO.setMobileNumber(doctorMasterDto.getMobileNumber());
			doctorMasterVO.setEmail(doctorMasterDto.getEmail());
			doctorMasterVO.setOccupationId(doctorMasterDto.getOccupationId());
			doctorMasterVO.setDateOfJoining(doctorMasterDto.getDateOfJoining());
			doctorMasterVO.setAccessCardNumber(doctorMasterDto.getAccessCardNumber());
			doctorMasterVO.setNationalityId(doctorMasterDto.getNationalityId());
			doctorMasterVO.setRaceId(doctorMasterDto.getRaceId());
			doctorMasterVO.setMaritalStatusId(doctorMasterDto.getMaritalStatusId());
			doctorMasterVO.setAddress(doctorMasterDto.getAddress());
			doctorMasterVO.setCountryId(doctorMasterDto.getCountryId());
			doctorMasterVO.setStateId(doctorMasterDto.getStateId());
			doctorMasterVO.setDistrictId(doctorMasterDto.getDistrictId());
			doctorMasterVO.setCityId(doctorMasterDto.getCityId());
			doctorMasterVO.setAreaId(doctorMasterDto.getAreaId());
			doctorMasterVO.setZipCode(doctorMasterDto.getZipCode());
			doctorMasterVO.setPhoneNumber(doctorMasterDto.getPhoneNumber());
			doctorMasterVO.setTypeId(doctorMasterDto.getTypeId());
			doctorMasterVO.setDoctorImage(doctorMasterDto.getDoctorImage());
			doctorMasterVO.setBirthDate(doctorMasterDto.getDob());
			doctorMasterVO.setRegistrationNumber(doctorMasterDto.getRegistrationNumber());
			doctorMasterVO.setDoctorCategoryId(doctorMasterDto.getDocCategoryId());
			
			userMasterVO.setDoctorId(doctorMasterDto.getDoctorId());
			userMasterVO.setUserType("Doctor");
			
			
			/*CommonResponse response = new CommonResponse();
			response.setMessage("UserMaster Create Successfully");
			Request request = new Request();
			Payload payload = new Payload();
			payload.setActionType("create");
			payload.setData(userMasterVO);
			request.setKey(MasterKeyConstants.UserMaster.toString());
			request.setPayload(payload);
			//iOtimsService.publish(request);
			//EmrService.publish(request);
			
			CommonResponse response1 = new CommonResponse();
			response1.setMessage("DoctorMaster Create Successfully");
			Request request1 = new Request();
			Payload payload1 = new Payload();
			payload1.setActionType("create");
			payload1.setData(doctorMasterVO);
			request1.setKey(MasterKeyConstants.DoctorMaster.toString());
			request1.setPayload(payload1);
			//iOtimsService.publish(request1);
			EmrService.publish(request1);*/
			
			/*if(doctorMasterDto.getSelectedOTIMSRoleList().size()>0) 
			{
			//for otims user role 
			StaffRoleVO staffRoleVO=new StaffRoleVO();
			staffRoleVO.setId(doctorMasterVO.getDoctorId());
			staffRoleVO.setUserName(userMasterVO.getUserName());
			staffRoleVO.setPassword(userMasterVO.getPassword());
			staffRoleVO.setFirstName(doctorMasterVO.getFirstName());
			staffRoleVO.setLastName(doctorMasterVO.getLastName());
			staffRoleVO.setEmailId(doctorMasterVO.getEmail());
			staffRoleVO.setOrganizationId(doctorMasterVO.getOrganizationId());
			staffRoleVO.setUnitId(doctorMasterVO.getUnitId());
			staffRoleVO.setMobileNumber(doctorMasterVO.getMobileNumber());
			staffRoleVO.setRoleId(doctorMasterDto.getSelectedOTIMSRoleList().get(0).getRoleId());
			staffRoleVO.setRoleName(doctorMasterDto.getSelectedOTIMSRoleList().get(0).getRoleName());
		
			payload1.setData(staffRoleVO);
			request1.setKey(MasterKeyConstants.Otims_User.toString());
			request1.setPayload(payload1);
			iOtimsService.publish(request1);
			}*/
			
			if (res1.getStatus().equals(SUCCESS)) {
				
				try{
					syncDoctorMasterForMyLife(doctorMasterDto);
				}catch(Exception e){
				logger.error("saveDoctorRegistration failed", e);
				}
			 	if(doctorMasterDto.getIsRMO().equals('N'))
			 	{
				  Response docSpe = iDoctorMasterDao.saveDoctorSpecialityMapper(doctorMasterDto);
				  
				  iDoctorMasterDao.saveDoctorSubSpecialityMapper(doctorMasterDto);
				  
			 	}
				if (res1.getStatus().equals(SUCCESS)) {
					Response doctorResponse = iDoctorMasterDao.saveDoctorDetails(doctorMasterDto);
					userMasterDto.setDoctorId(doctorMasterDto.getDoctorId());
					userMasterDto.setPassword(GlobalCommonDateUtils.decrypt(userMasterDto.getPassword()));
					if (doctorResponse.getStatus().equals(SUCCESS)
							&& doctorMasterDto.getDependentDetailsDtosList().size() > 0) 
					{

						DependentDetailsDto dependentDetailsDto = new DependentDetailsDto();
						dependentDetailsDto.setTypeId(doctorMasterDto.getTypeId());
						dependentDetailsDto.setEmpDocId(doctorMasterDto.getDoctorId());
						dependentDetailsDto.setDependentDetailsDtoList(doctorMasterDto.getDependentDetailsDtosList());
						Response DependentRes = iDependentDetailsService.saveDependentDetails(dependentDetailsDto);
						if (DependentRes.getStatus().equals(SUCCESS)) {
							//START doctor Entry in log table start
							try{
							 	
							 	if(doctorResponse.getStatus().equals(SUCCESS) && userRes.getStatus().equals(SUCCESS))
								{
									//doctorMasterDto.setUserId((int)userRes.getObject());
								//	Response usernameRes = iMailingService.sendUserName(userMasterDto, RegistrationType.REG_EMPLOYEE);
									//Response userpassRes =	iMailingService.sendUserPassword(userMasterDto, RegistrationType.REG_EMPLOYEE);
									//if(usernameRes.getStatus().equals(SUCCESS))
									// {
										return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, res1.getObject(),userMasterDto);
									// }
									
								}
								else {
									return new Response(ERROR, COMMON_ERROR, "Failed To Save User", null, null);
								}
								//return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, res1.getObject());
								
							 	
							}catch(Exception e){
							logger.error("saveDoctorRegistration failed", e);
							}
						}
						
					}
					else
					{
						if(doctorResponse.getStatus().equals(SUCCESS) && userRes.getStatus().equals(SUCCESS))
						{
							
							//Response usernameRes = iMailingService.sendUserName(userMasterDto, RegistrationType.REG_EMPLOYEE);
							//Response userpassRes =	iMailingService.sendUserPassword(userMasterDto, RegistrationType.REG_EMPLOYEE);
						//	if(usernameRes.getStatus().equals(SUCCESS))
							// {
						//		return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, userMasterDto);
						//	 }
							
						}
						else {
							return new Response(ERROR, COMMON_ERROR, "Failed To Save User", null, null);
						}
						return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, res1.getObject(),userMasterDto);
					}
				}
			}
			// }

		} catch (Exception e) {
		logger.error("saveDoctorRegistration failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}


	private void syncDoctorMasterForMyLife(DoctorMasterDto doctorMasterDto) {
		String dateTime = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(doctorMasterDto.getUnitId()), "dd-M-yyyy HH:mm:ss");
		ScheduleLogsDto scheduleLogsDto=new ScheduleLogsDto(this);
	 	scheduleLogsDto.setAction(ScheduleActions.INSERT);
	 	scheduleLogsDto.setAddedBy(doctorMasterDto.getCreatedBy());
	 	scheduleLogsDto.setAddedDate(doctorMasterDto.getCreatedDate());
	 	scheduleLogsDto.setApplication(ScheduleActions.HIS);
	 	scheduleLogsDto.setDateTime(dateTime);
	 	scheduleLogsDto.setErrorCount(0);
	 	scheduleLogsDto.setPriority(ScheduleActions.PRIORITY_NORM);
	 	scheduleLogsDto.setRecordId(doctorMasterDto.getDoctorId());
	 	scheduleLogsDto.setStatus(ScheduleActions.STATUS_PENDING);
	 	scheduleLogsDto.setTableName(ScheduleTables.DOCTOR_MASTER);
	 	scheduleLogsDto.setOrgId(doctorMasterDto.getOrganizationId());
	 	//applicationEventPublisher.publishEvent(scheduleLogsDto);
	 	isheduleDao.saveScheduleLogs(scheduleLogsDto);
	}

	@Override
	public Response getDoctorDetails(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try {
			List<DoctorMasterDto> doctorMasterDtosList = new LinkedList<DoctorMasterDto>(); 
			Response responseDocDetails = iDoctorMasterDao.getDoctorDetails(doctorMasterDto);
			if(responseDocDetails.getListObject().size()>0)
			{
				ListIterator<DoctorMasterDto> iterator = responseDocDetails.getListObject().listIterator();
				while(iterator.hasNext())
				{
					DoctorMasterDto masterDtoList = iterator.next();
					
					DependentDetailsDto dependentDetailsDto = new DependentDetailsDto();
						dependentDetailsDto.setTypeId(doctorMasterDto.getTypeId());
						dependentDetailsDto.setEmpDocId(masterDtoList.getDoctorId());
					
					Response dependentRes= iDependentDetailsService.getDependentDetails(dependentDetailsDto);
						masterDtoList.setDependentDetailsDtosList(dependentRes.getListObject());
					
					doctorMasterDtosList.add(masterDtoList);
					
				}
				
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, null, doctorMasterDtosList, null);
		}catch (Exception e) {
		logger.error("getDoctorDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	
	@Override
	public Response updateDoctorDetails(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try {
			Response resEmpDependent = null;
			Response res = iDoctorMasterDao.checkUniqueDoctor(doctorMasterDto);
			if (res.getStatus().equals(SUCCESS)) {
				
				//code added by Ganesh 15 APR 2019
				Calendar c = Calendar.getInstance();
				c.setTime(TimeZoneComponent.getDateByzone(doctorMasterDto.getUnitId()));
				c.add(Calendar.YEAR, 1);
				Date expDate = c.getTime();
				String passwordExpiry = GlobalCommonDateUtils.getStringDate(expDate, "dd-MM-yyyy HH:mm:ss");
				
				/*//Update for selected HIS role list
				if(doctorMasterDto.getSelectedHisRoleList().size()>0) {
					Boolean flag=false;
					for(UserRoleMapperDto userRoleMapperDto:doctorMasterDto.getSelectedHisRoleList()) {
						userRoleMapperDto.setUserId(doctorMasterDto.getUserId());
						//userRoleMapperDto.setUnitId(doctorMasterDto.getUnitId());
						if(flag==false) 
						{
							iUserRoleMapperDao.truncteRecode(userRoleMapperDto);
							flag=true;
						}
						iUserRoleMapperDao.saveUserRoleMapper(userRoleMapperDto);
					}
				}*/
				
				//Update for selected HIS role list
				if(doctorMasterDto.getSelectedHisRoleList().size()>0) {
					List<Integer> roles = new ArrayList<Integer>();
					for(UserRoleMapperDto userRoleMapperDto:doctorMasterDto.getSelectedHisRoleList()) {
						roles.add(userRoleMapperDto.getRoleId());
					}
					
					List<UserRoleMapperDto> savedRoles = new ArrayList<UserRoleMapperDto>();
					Response savedRolesRes =  iUserRoleMapperDao.inActiveRolesRecode(roles,doctorMasterDto.getUserId(),doctorMasterDto.getUnitId());
					
					if(savedRolesRes != null && savedRolesRes.getListObject().size()>0){
						
						savedRoles.addAll(savedRolesRes.getListObject());
						
						for(UserRoleMapperDto userRoleMapperDto:doctorMasterDto.getSelectedHisRoleList()) {
							userRoleMapperDto.setUserId(doctorMasterDto.getUserId());
							boolean checkexits = false;
							for (UserRoleMapperDto saveUserRoleMapperDto : savedRoles) {
								
								if(saveUserRoleMapperDto.getRoleId().equals(userRoleMapperDto.getRoleId())){
									checkexits = true;
								}
								
							}
							if(checkexits == false){
								iUserRoleMapperDao.saveUserRoleMapper(userRoleMapperDto);
							}
							
						}
						
					}else{
						for(UserRoleMapperDto userRoleMapperDto:doctorMasterDto.getSelectedHisRoleList()) {
							userRoleMapperDto.setUserId(doctorMasterDto.getUserId());
							iUserRoleMapperDao.saveUserRoleMapper(userRoleMapperDto);
						}
					}
					
				}
				
				
				
				/*//Update for selected OTIMS Role List
				if(doctorMasterDto.getSelectedOTIMSRoleList().size()>0) 
				{
					Boolean flag=false;
					for(OTIMSUserRoleMapperDto otimsUserRoleMapperDto:doctorMasterDto.getSelectedOTIMSRoleList()) {
						otimsUserRoleMapperDto.setUserId(doctorMasterDto.getUserId());
						otimsUserRoleMapperDto.setUnitId(doctorMasterDto.getUnitId());
						if(flag==false) 
						{
							iEmployeeRegistrationDao.inactiveRoleStatusOfOtimsUser(otimsUserRoleMapperDto);
							flag=true;
						}
						iEmployeeRegistrationDao.saveOTIMSUserRoleMapper(otimsUserRoleMapperDto);
					}
				}*/
				
				//Update for selected user unit List
				if(doctorMasterDto.getSelectedUserUnitList().size()>0) {
					
					List<Integer> unitIds = new ArrayList<Integer>();
					for(UserUnitMapperDto userUnitMapperDto:doctorMasterDto.getSelectedUserUnitList()) {
						unitIds.add(userUnitMapperDto.getUnitId());
					}
					
					List<UserUnitMapperDto> savedUnits = new ArrayList<UserUnitMapperDto>();
					Response savedUnitsRes =  iEmployeeRegistrationDao.inactiveUnitMapperByUserIdAndUnits(unitIds,doctorMasterDto.getUserId());
					if(savedUnitsRes != null && savedUnitsRes.getListObject() != null && savedUnitsRes.getListObject().size()>0){
						
						savedUnits.addAll(savedUnitsRes.getListObject());
						
						for(UserUnitMapperDto userUnitMapperDto:doctorMasterDto.getSelectedUserUnitList()) {
							
							boolean checkexits = false;
							
							for (UserUnitMapperDto savedUserUnitMapperDto : savedUnits) {
								if(savedUserUnitMapperDto.getUnitId().equals(userUnitMapperDto.getUnitId())){
									
									checkexits = true;
								}
							}
							
							if(checkexits == false){
								userUnitMapperDto.setUserId(doctorMasterDto.getUserId());
								userUnitMapperDto.setMisRights(doctorMasterDto.getDefaultMISRights());
								userUnitMapperDto.setOrganisationId(doctorMasterDto.getOrganizationId());
								Response response =iEmployeeRegistrationDao.checkTabAccessAvail(userUnitMapperDto);
								if(response.getObject()!=null){
									UserUnitMapperDto obj=(UserUnitMapperDto)response.getObject();
									userUnitMapperDto.setCoversheetRights(obj.getTabAccessIds());
									
								}else {
									userUnitMapperDto.setCoversheetRights(doctorMasterDto.getDefaultCoversheetRights());
								}
								
								 iUserRoleMapperDao.saveUserUnitMapper(userUnitMapperDto);
							}
							
						
						}
						
					}else{
						
						for(UserUnitMapperDto userUnitMapperDto:doctorMasterDto.getSelectedUserUnitList()) {
							userUnitMapperDto.setUserId(doctorMasterDto.getUserId());
							userUnitMapperDto.setMisRights(doctorMasterDto.getDefaultMISRights());
							userUnitMapperDto.setOrganisationId(doctorMasterDto.getOrganizationId());
							Response response =iEmployeeRegistrationDao.checkTabAccessAvail(userUnitMapperDto);
							if(response.getObject()!=null){
								UserUnitMapperDto obj=(UserUnitMapperDto)response.getObject();
								userUnitMapperDto.setCoversheetRights(obj.getTabAccessIds());
								
							}else {
								userUnitMapperDto.setCoversheetRights(doctorMasterDto.getDefaultCoversheetRights());
							}
							
							 iUserRoleMapperDao.saveUserUnitMapper(userUnitMapperDto);
						}
					}
					
				}
				
				
				
				doctorMasterDto.setPwdExpiry(passwordExpiry);
				Response resEmpDetails = iDoctorMasterDao.updateDoctorDetails(doctorMasterDto);
				if (resEmpDetails.getStatus().equals(SUCCESS)) {

					try{
						syncDoctorMasterForMyLife(doctorMasterDto);
					}catch(Exception e){
					logger.error("updateDoctorDetails failed", e);
					}
					
					DependentDetailsDto dependentDetailsDto = new DependentDetailsDto();
					dependentDetailsDto.setEmpDocId(doctorMasterDto.getDoctorId());
					dependentDetailsDto.setTypeId(doctorMasterDto.getTypeId());
					dependentDetailsDto.setUpdatedBy(doctorMasterDto.getUpdatedBy());
					dependentDetailsDto.setUpdatedDate(doctorMasterDto.getUpdatedDate());
					dependentDetailsDto.setDependentDetailsDtoList(doctorMasterDto.getDependentDetailsDtosList());
					resEmpDependent = iDependentDetailsService.updateDependentDetails(dependentDetailsDto);

					if (resEmpDependent.getStatus().equals(SUCCESS) && doctorMasterDto.getDoctorSpecialityMapperDtosList() != null && doctorMasterDto.getDoctorSpecialityMapperDtosList().size() >= 0)
					{
						if(doctorMasterDto.getIsRMO().equals('N'))
							iDoctorMasterDao.updateDoctorSpecialityMapper(doctorMasterDto);
						return resEmpDetails;
					}else
						return new Response(ERROR, null, "Problem in updating Dependent Details !!", null, null);
				}
			} else
				return new Response(ERROR, null, "Doctor does not exist !!", null, null);

		} catch (Exception e) {
		logger.error("updateDoctorDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updateDoctorStatus(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try {

			return iDoctorMasterDao.updateDoctorStatus(doctorMasterDto);

		} catch (Exception e) {
		logger.error("updateDoctorStatus failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response searchDoctorDetailsByCriteria(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try {
			List<DoctorMasterDto> doctorMasterDtosList = new LinkedList<DoctorMasterDto>(); 
			Response responseDocDetails = iDoctorMasterDao.searchDoctorDetailsByCriteria(doctorMasterDto);
			if(responseDocDetails.getListObject().size()>0)
			{
				ListIterator<DoctorMasterDto> iterator = responseDocDetails.getListObject().listIterator();
				while(iterator.hasNext())
				{
					DoctorMasterDto masterDtoList = iterator.next();
					
					DependentDetailsDto dependentDetailsDto = new DependentDetailsDto();
						dependentDetailsDto.setTypeId(doctorMasterDto.getTypeId());
						dependentDetailsDto.setEmpDocId(masterDtoList.getDoctorId());
					
					Response dependentRes= iDependentDetailsService.getDependentDetails(dependentDetailsDto);
						masterDtoList.setDependentDetailsDtosList(dependentRes.getListObject());
					
					doctorMasterDtosList.add(masterDtoList);
					
				}
				
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, null, doctorMasterDtosList, null);
		} catch (Exception e) {
		logger.error("searchDoctorDetailsByCriteria failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveDoctorImage(int orgId, int unitId, int doctorId, MultipartFile inputFile)
			throws ApplicationException {
		 try{
			 String base64Image="";
			 
			 int bucketType = (int) sessionFactory.getCurrentSession()
						.createSQLQuery("select bucket_flag from t_unit_application_billing_configuration where unit_id ="+unitId+" and organization_id ="+orgId)
						.uniqueResult();
			 
		      if(orgId>0&&unitId>0&&doctorId>0&&!inputFile.isEmpty())
		      {
		        Response unitRes =  iGlobalUnitMasterDao.getUnitFolderPath(unitId);
		        UnitMasterDto unitMasterDto = (UnitMasterDto) unitRes.getObject();
		        String doctorImagePath =  docUploadUtility.uploadDoctorImage(inputFile, unitMasterDto, doctorId);
		        
		        if(bucketType == 2) {
		        try {
		            URL url = new URL(doctorImagePath);
		            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		            try (InputStream inputStream = url.openStream()) {
		                byte[] buffer = new byte[4096];
		                int bytesRead;
		                while ((bytesRead = inputStream.read(buffer)) != -1) {
		                    outputStream.write(buffer, 0, bytesRead);
		                }
		            }

		            byte[] imageBytes = outputStream.toByteArray();
		            base64Image = Base64.getEncoder().encodeToString(imageBytes);

		            System.out.println("MY DOCTOR IMAGE IS11:"+base64Image);
		        } catch (IOException e) {
		        logger.error("saveDoctorImage failed", e);
		        }
		        
		        DoctorMasterDto doctorMasterDto = new DoctorMasterDto();
		        doctorMasterDto.setDoctorId(doctorId);
		        doctorMasterDto.setOrganizationId(orgId);
		        doctorMasterDto.setUnitId(unitId);
		        //doctorMasterDto.setDoctorImage(doctorImagePath);
		        doctorMasterDto.setDoctorImage(base64Image);
		        		        
		        //return 
		        		iDoctorMasterDao.updateDoctorImage(doctorMasterDto);
		        return new Response(SUCCESS, SUCCESS_CODE, null, null, null , bucketType);
		        
		        }else {
		        	DoctorMasterDto doctorMasterDto = new DoctorMasterDto();
			        doctorMasterDto.setDoctorId(doctorId);
			        doctorMasterDto.setOrganizationId(orgId);
			        doctorMasterDto.setUnitId(unitId);
			        doctorMasterDto.setDoctorImage(doctorImagePath);
			        		        
			        //return 
			        iDoctorMasterDao.updateDoctorImage(doctorMasterDto);
			        return new Response(SUCCESS, SUCCESS_CODE, null, null, null , bucketType);
		        }		        
		      }
		    	  
		    	  
		    return null;
		  }catch(Exception e){
		  logger.error("saveDoctorImage failed", e);
		  }
		  return new Response<>(ERROR, null, null, null, null);
		  }

	@Override
	public Response searchPlainDoctorDetailsByCriteria(DoctorMasterDto doctorMasterDto) {
		try {
			return iDoctorMasterDao.searchDoctorDetailsByCriteria(doctorMasterDto);
		} catch (Exception e) {
		logger.error("searchPlainDoctorDetailsByCriteria failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoctorNameById(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try {
			return iDoctorMasterDao.getDoctorNameById(doctorMasterDto);
		} catch (Exception e) {
		logger.error("getDoctorNameById failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveDocList(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try {
			return iDoctorMasterDao.getActiveDocList(doctorMasterDto);
		} catch (Exception e) {
		logger.error("getActiveDocList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response saveDoctorDocuments(Integer degreeId, Integer qualificationId, String qualificationDesc,
			String registrationNumber, Integer createdBy, String createdDate, 
			String updatedDate, Integer updatedBy, Integer unitId, Integer organizationId, Integer doctorId,
			String fileName,MultipartFile file) {
		 try{
			  DoctorDocumentMapperDto dto= new DoctorDocumentMapperDto();
		      if(organizationId>0&&unitId>0&&doctorId>0 && file.getBytes().length>0)
		      {
			        DoctorMasterDto doctorMasterDto = new DoctorMasterDto();
			        doctorMasterDto.setDoctorId(doctorId);
			        doctorMasterDto.setOrganizationId(organizationId);
			        doctorMasterDto.setUnitId(unitId);
		    	  
		        String doctorImagePath =  docUploadUtility.uploadCorporateDocumentImage( file, doctorMasterDto);
		   
		        URL url = new URL(doctorImagePath);
	            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

	            try (InputStream inputStream = url.openStream()) {
	                byte[] buffer = new byte[4096];
	                int bytesRead;
	                while ((bytesRead = inputStream.read(buffer)) != -1) {
	                    outputStream.write(buffer, 0, bytesRead);
	                }
	            }

	            byte[] imageBytes = outputStream.toByteArray();
	            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

	            System.out.println("MY DOCTOR IMAGE IS222:"+base64Image);
		        
		        dto.setDegreeId(degreeId);
				dto.setQualificationId(qualificationId);
				dto.setQualificationDesc(qualificationDesc);
				dto.setRegistrationNumber(registrationNumber);
				dto.setCreatedBy(createdBy);
				dto.setCreatedDate(createdDate);
				dto.setUpdatedDate(updatedDate);
				dto.setUpdatedBy(updatedBy);
				dto.setUnitId(unitId);
				dto.setOrganizationId(organizationId);
				dto.setDoctorId(doctorId);
				dto.setFile(file.getBytes());
				dto.setDocumentFilePath(doctorImagePath);
				dto.setFileName(fileName);
		        return iDoctorDocumentMapperDao.saveDoctorQualificationDetails(dto);
		      }
		      else{
		        return iDoctorDocumentMapperDao.saveDoctorQualificationDetails(dto);
		         }
		  }catch(Exception e){
		  logger.error("saveDoctorDocuments failed", e);
		  }
		  return new Response<>(ERROR, null, null, null, null);
	}

	@Override
	public Response getListOfDoctorDegreeDoccument(DoctorDocumentMapperDto doctorDocumentMapperDto) throws ApplicationException {
		try {
			return iDoctorDocumentMapperDao.getListOfDoctorDegreeDoccument(doctorDocumentMapperDto);
		} catch (Exception e) {
		logger.error("getListOfDoctorDegreeDoccument failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getRMODoctorList(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try {
			return iDoctorMasterDao.getRMODoctorList(doctorMasterDto);
		} catch (Exception e) {
		logger.error("getRMODoctorList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response validateDoctorPin(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try {
			return iDoctorMasterDao.validateDoctorPin(doctorMasterDto);
		} catch (Exception e) {
		logger.error("validateDoctorPin failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response validateDoctorPinByDoctorId(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try {
			return iDoctorMasterDao.validateDoctorPinByDoctorId(doctorMasterDto);
		} catch (Exception e) {
		logger.error("validateDoctorPinByDoctorId failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response getDocotorListForAppoitments(Integer unitId,Integer specialityId) throws ApplicationException {
		try {
			return iDoctorMasterDao.getDocotorListForAppoitments(unitId,specialityId);
		} catch (Exception e) {
		logger.error("getDocotorListForAppoitments failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoctorCount(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try {
			return iDoctorMasterDao.getDoctorCount(doctorMasterDto);
		} catch (Exception e) {
		logger.error("getDoctorCount failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getStaffListByRoleId(UserRoleMasterDto dto) throws ApplicationException {
		try {
			return iDoctorMasterDao.getStaffListByRoleId(dto);
		} catch (Exception e) {
		logger.error("getStaffListByRoleId failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response doctorDetailsUniversalSearch(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try {
			List<DoctorMasterDto> doctorMasterDtosList = new LinkedList<DoctorMasterDto>(); 
			Response responseDocDetails = iDoctorMasterDao.doctorDetailsUniversalSearch(doctorMasterDto);
			if(responseDocDetails.getListObject().size()>0)
			{
				ListIterator<DoctorMasterDto> iterator = responseDocDetails.getListObject().listIterator();
				while(iterator.hasNext())
				{
					DoctorMasterDto masterDtoList = iterator.next();
					
					DependentDetailsDto dependentDetailsDto = new DependentDetailsDto();
						dependentDetailsDto.setTypeId(doctorMasterDto.getTypeId());
						dependentDetailsDto.setEmpDocId(masterDtoList.getDoctorId());
					
					Response dependentRes= iDependentDetailsService.getDependentDetails(dependentDetailsDto);
						masterDtoList.setDependentDetailsDtosList(dependentRes.getListObject());
					
					doctorMasterDtosList.add(masterDtoList);
					
				}
				
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, null, doctorMasterDtosList, null);
		} catch (Exception e) {
		logger.error("doctorDetailsUniversalSearch failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getPlainDocList(DoctorMasterDto doctorMasterDto) {
		try {
			return iDoctorMasterDao.getPlainDocList(doctorMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response getDoctorListBySpecialityId(DoctorSpecialityMapperDto doctorSpecialityMapper) {
		try {
			return iDoctorMasterDao.getDoctorListBySpecialityId(doctorSpecialityMapper);
		} catch (Exception e) {
		logger.error("getDoctorListBySpecialityId failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override

	public Response storeDoctorEqucationDetails(DoctorMasterDto doctorMasterDto) {
		 try{
			  DoctorDocumentMapperDto dto= new DoctorDocumentMapperDto();
		
		        dto.setDegreeId(doctorMasterDto.getDegreeId());
				dto.setQualificationId(doctorMasterDto.getQualificationId());
				dto.setQualificationDesc(doctorMasterDto.getQualificationDesc());
				dto.setRegistrationNumber(doctorMasterDto.getRegistrationNumber());
				dto.setCreatedBy(doctorMasterDto.getCreatedBy());
				dto.setCreatedDate(doctorMasterDto.getCreatedDate());
				dto.setUpdatedDate(doctorMasterDto.getUpdatedDate());
				dto.setUpdatedBy(doctorMasterDto.getUpdatedBy());
				dto.setUnitId(doctorMasterDto.getUnitId());
				dto.setOrganizationId(doctorMasterDto.getOrganizationId());
				dto.setDoctorId(doctorMasterDto.getDoctorId());
		        return iDoctorDocumentMapperDao.saveDoctorQualificationDetails(dto);
		     
		  }catch(Exception e){
		  logger.error("storeDoctorEqucationDetails failed", e);
		  }
		  return new Response<>(ERROR, null, null, null, null);
	}
	public Response getActiveDocListWithSpeciality(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try {
			return iDoctorMasterDao.getActiveDocListWithSpeciality(doctorMasterDto);
		} catch (Exception e) {
		logger.error("getActiveDocListWithSpeciality failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	
	}

	@Override
	public Response checkDoctorRegNoAvailability(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try {
			return iDoctorMasterDao.checkDoctorRegNoAvailability(doctorMasterDto);
		} catch (Exception e) {
		logger.error("checkDoctorRegNoAvailability failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Integer getGlobalDocId(Integer doctorId) throws ApplicationException {
		try {
			return iDoctorMasterDao.getGlobalDocId(doctorId);
		} catch (Exception e) {
		logger.error("getGlobalDocId failed", e);
		}
		return null;
	}

	@Override
	public Response updateGlobalDocId(Integer doctorId, Integer globalDocId) {
		try {
			return iDoctorMasterDao.updateGlobalDocId(doctorId,globalDocId);
		} catch (Exception e) {
		logger.error("updateGlobalDocId failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoctorListForSwapDoctor(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try {
			return iDoctorMasterDao.getDoctorListForSwapDoctor(doctorMasterDto);
		} catch (Exception e) {
		logger.error("getDoctorListForSwapDoctor failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateDoctorEqucationDetails(DoctorDocumentMapperDto doctorDocumentMapperDto)
			throws ApplicationException {
		 try{
			 
		        return iDoctorDocumentMapperDao.updateDoctorEqucationDetails(doctorDocumentMapperDto);
		     
		  }catch(Exception e){
		  logger.error("updateDoctorEqucationDetails failed", e);
		  }
		  return new Response<>(ERROR, null, null, null, null);
	}

	@Override
	public Response getDoctorsForWebAppointment(Integer unitId, Integer specialityId, Boolean flag)throws ApplicationException {
		try {
			return iDoctorMasterDao.getDoctorsForWebAppointment(unitId,specialityId,flag);
		} catch (Exception e) {
		logger.error("getDoctorsForWebAppointment failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDetailsForBookAppointment(BookAppointmentDto bookAppointmentDto) throws ApplicationException {
		try {
			Date currentDate = new Date();

			SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
			String strCurrentDate = formatter1.format(currentDate);
			UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
			unitApplicationConfigurationDto.setOrganizationId(bookAppointmentDto.getOrganizationId());
			unitApplicationConfigurationDto.setUnitId(bookAppointmentDto.getUnitId());
			Response appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
			UnitApplicationConfigurationDto unitApplicationConfigurationDtos = (UnitApplicationConfigurationDto) appConfigRes
					.getObject();

			// get doctor details
			DoctorMasterDto doctorDto = new DoctorMasterDto();
			doctorDto.setOrganizationId(bookAppointmentDto.getOrganizationId());
			Response responseDocDetails = iDoctorMasterDao.getDoctorDetails(doctorDto);

			if (responseDocDetails.getListObject() != null && !responseDocDetails.getListObject().isEmpty()) {
				bookAppointmentDto.setDoctorDto((DoctorMasterDto) responseDocDetails.getListObject().get(0));
			}
			// get unit list mapped with doctor
			UserUnitMapperDto unitDto = new UserUnitMapperDto();
			unitDto.setOrganisationId(bookAppointmentDto.getOrganizationId());
			unitDto.setUserId(bookAppointmentDto.getUserId());
			Response unitRes = iUserRoleMapperDao.getUserUnitDetListByUserId(unitDto);

			if (unitRes.getListObject() != null && !unitRes.getListObject().isEmpty()) {
				bookAppointmentDto.setUnitDetList(unitRes.getListObject());
			}
			// get speciality list mapped with doctor

			// get doctor qualification
			StringBuffer doctorDegrees = new StringBuffer();
			DoctorDocumentMapperDto doctorDocumentMapperDto = new DoctorDocumentMapperDto();
			doctorDocumentMapperDto.setOrganizationId(bookAppointmentDto.getOrganizationId());
			doctorDocumentMapperDto.setUnitId(bookAppointmentDto.getUnitId());
			doctorDocumentMapperDto.setDoctorId(bookAppointmentDto.getDoctorId());

			Response qualificationListRes = iDoctorDocumentMapperDao
					.getListOfDoctorDegreeDoccument(doctorDocumentMapperDto);
			if (qualificationListRes.getListObject() != null && !qualificationListRes.getListObject().isEmpty()) {

				if (qualificationListRes.getListObject().size() == 1) {
					DoctorDocumentMapperDto dto = (DoctorDocumentMapperDto) qualificationListRes.getListObject().get(0);
					bookAppointmentDto.setDoctorDegrees(dto.getDegreeDesc());
				} else {
					int listLength = qualificationListRes.getListObject().size();
					for (int i = 0; i < listLength; i++) {
						DoctorDocumentMapperDto dto = (DoctorDocumentMapperDto) qualificationListRes.getListObject()
								.get(i);
						doctorDegrees.append(dto.getDegreeDesc());
						if (i != (listLength - 1)) {
							doctorDegrees.append(", ");
						}
					}
					bookAppointmentDto.setDoctorDegrees(doctorDegrees.toString());
				}
			}

			// get doctor visit fee
			EncounterMasterDto encounterMasterDto = new EncounterMasterDto();
			encounterMasterDto.setOrganizationId(bookAppointmentDto.getOrganizationId());
			encounterMasterDto.setUnitId(bookAppointmentDto.getUnitId());
			encounterMasterDto.setUserDefinedVisitTypeId(1);
			encounterMasterDto.setDoctorId(bookAppointmentDto.getDoctorId());
			encounterMasterDto.setSpecialityId(bookAppointmentDto.getSpecialityId());
			encounterMasterDto.setDeptWiseAppointment('N');
			encounterMasterDto.setDeptId(bookAppointmentDto.getSpecialityId());
			encounterMasterDto.setSystemGeneratedVisitTypeId(1);
			encounterMasterDto.setCurrentDate(strCurrentDate);

			encounterMasterDto.setPatientCategoryId(unitApplicationConfigurationDtos.getDefaultPatientCategoryId());
			Response chargesRes = iEncounterMasterService.billAmountForAppointment(encounterMasterDto);
			if (chargesRes.getObject() != null) {
				bookAppointmentDto.setOrderMasterDto((OrderMasterDto) chargesRes.getObject());
			}
			// Response chargesRes=
			// iEncounterMasterService.getBillDetailsOnEncouter(encounterMasterDto);
			return new Response(SUCCESS, SUCCESS_CODE, null, null, bookAppointmentDto);
		} catch (Exception e) {
		logger.error("getDetailsForBookAppointment failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoctorRegistrationNumber(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try {
			return iDoctorMasterDao.getDoctorRegistrationNumber(doctorMasterDto);
		} catch (Exception e) {
		logger.error("getDoctorRegistrationNumber failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getDoctorMasterList1(DoctorMasterDto doctorMasterDto)throws ApplicationException {
		try{
			return iDoctorMasterDao.getDoctorMasterList1(doctorMasterDto);
		}catch(Exception e)
		{
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response doctorIdentificationDocumets(Integer identificationTypeId, String identificationNumber, Integer employeeId, Integer userId,
			String datepickerExpiry, Integer createdBy, String createdDate, String updatedDate, Integer updatedBy,
			Integer unitId, Integer organizationId, Integer doctorId, String fileName, String retrieveFileName, String doctorIndetificationFile, Integer userIndetificationDetailsId ,MultipartFile file) throws ServerException,  InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
		 try{
			 UserIndetificationDetailsDto dto= new UserIndetificationDetailsDto();

			 if(userIndetificationDetailsId == null  || userIndetificationDetailsId == 0) {
				 try {
					 if (organizationId > 0 && unitId > 0) {
						 DoctorMasterDto doctorMasterDto = new DoctorMasterDto();
						 doctorMasterDto.setDoctorId(doctorId);
						 doctorMasterDto.setOrganizationId(organizationId);
						 doctorMasterDto.setUnitId(unitId);


						 String doctorImagePath = "";
						 if ((fileName != null || fileName != "") && (file != null && !file.isEmpty())) {
							 doctorImagePath = docUploadUtility.uploadCorporateDocumentImage(file, doctorMasterDto);
						 }

						 dto.setIdentificationTypeId(identificationTypeId);
						 dto.setIdentificationNumber(identificationNumber);
						 dto.setEmployeeId((employeeId != 0) ? employeeId : null);
						 dto.setUserId(userId);
						 dto.setIdentificationExpiryDate(datepickerExpiry);
						 dto.setDoctorIndetificationFile((doctorImagePath != null) ? doctorImagePath : null);
						 dto.setFileName((fileName != null && fileName != "" && fileName != "undefined") ? fileName : null);
						 dto.setCreatedBy(createdBy);
						 dto.setCreatedDate(createdDate);
						 dto.setUpdatedDate(updatedDate);
						 dto.setUpdatedBy(updatedBy);
						 dto.setUnitId(unitId);
						 dto.setOrganizationId(organizationId);
						 dto.setDoctorId((doctorId != 0) ? doctorId : null);

						 iUserIndetificationDetailsDaoImpl.saveIdentificationDetailsOfUser(dto);
						 return new Response(SUCCESS, SUCCESS_CODE, null, null, dto);
					 }
				 }catch (Exception e){
					 throw e;
				 }
			 }else {

				        DoctorMasterDto doctorMasterDto = new DoctorMasterDto();
				        doctorMasterDto.setDoctorId(doctorId);
				        doctorMasterDto.setOrganizationId(organizationId);
				        doctorMasterDto.setUnitId(unitId);

				 String doctorImagePath = "";
				 dto.setDoctorIndetificationFile((doctorIndetificationFile != null || doctorIndetificationFile != "")? doctorIndetificationFile : null);
				 dto.setFileName((retrieveFileName != null && retrieveFileName != "" && !retrieveFileName.equals("undefined"))? retrieveFileName : null);
				 if((fileName != null || fileName !="") &&(file != null && !file.isEmpty()) ){
					 doctorImagePath = docUploadUtility.uploadCorporateDocumentImage( file, doctorMasterDto);
					 dto.setDoctorIndetificationFile((doctorImagePath != null)? doctorImagePath : null);
					 dto.setFileName((fileName != null || fileName != "" || !fileName.equals("undefined"))? fileName : null);
				 }


			        dto.setIdentificationTypeId(identificationTypeId);
			        dto.setIdentificationNumber(identificationNumber);
				 	dto.setEmployeeId((employeeId != 0)? employeeId : null);
			        dto.setUserId(userId);
			        dto.setIdentificationExpiryDate(datepickerExpiry);
//				 dto.setDoctorIndetificationFile((doctorImagePath != null)? doctorImagePath : null);
//				 dto.setFileName((fileName != null || fileName != "")? fileName : null);
					dto.setCreatedBy(createdBy);
					dto.setCreatedDate(createdDate);
					dto.setUpdatedDate(updatedDate);
					dto.setUpdatedBy(updatedBy);
					dto.setUnitId(unitId);
					dto.setOrganizationId(organizationId);
				 	dto.setDoctorId((doctorId != 0)? doctorId : null);
					dto.setUserIndetificationDetailsId(userIndetificationDetailsId);
					iUserIndetificationDetailsDaoImpl.updateIdentificationDetailsOfUser(dto);
					return new Response(SUCCESS, SUCCESS_CODE, null, null, dto);
			 }
		  }catch(Exception e){
		      throw e;
		  }
		  return new Response<>(ERROR, null, null, null, null);
	}

	@Override
	public Response getListOfDoctorIdentificationDocuumentList(UserIndetificationDetailsDto userIndetificationDetailsDto)
			throws ApplicationException {
		try {
			return iUserIndetificationDetailsDaoImpl.getListOfDoctorIdentificationDocuumentList(userIndetificationDetailsDto);
		} catch (Exception e) {
		logger.error("getListOfDoctorIdentificationDocuumentList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateDoctorIdentificationDetails(UserIndetificationDetailsDto userIndetificationDetailsDto)
			throws ApplicationException {
		 try{
			 
		        return iUserIndetificationDetailsDaoImpl.updateDoctorIdentificationDetails(userIndetificationDetailsDto);
		     
		  }catch(Exception e){
		  logger.error("updateDoctorIdentificationDetails failed", e);
		  }
		  return new Response<>(ERROR, null, null, null, null);
	}
	
	
	@Override
	public Response getDoctorListByOrgId(DoctorSpecialityMapperDto doctorSpecialityMapper) throws ApplicationException {
	try {
		return iDoctorMasterDao.getDoctorListByOrgId(doctorSpecialityMapper);
	} catch (Exception e) {
	logger.error("getDoctorListByOrgId failed", e);
	}
	return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response doctorRegistrationWithGLId(GeneralLedgerMasterDto generalLedgerMasterDto) throws ApplicationException {
		 try{
			 //check general ledger 
			 Response response = iGeneralLedgerMasterServiceDao.getGeneralLedgerMasterByName(generalLedgerMasterDto);
			 
				if (response.getListObject().size() > 0 && response.getListObject() != null) {
					
					List<GeneralLedgerMasterDto> generalLedgerMasterDto1 = response.getListObject();
					for (GeneralLedgerMasterDto dto : generalLedgerMasterDto1) 
					{
						generalLedgerMasterDto.setGeneralLedgerId(dto.getGeneralLedgerId());
					}
				} else {
					 Response saveGenResponse = iGeneralLedgerMasterServiceDao.saveGeneralLedgerMaster(generalLedgerMasterDto);
					 GeneralLedgerMaster generalLedgerMaster = (com.param.entity.org.model.GeneralLedgerMaster)saveGenResponse.getObject();
					 generalLedgerMasterDto.setGeneralLedgerId(generalLedgerMaster.getGeneralLedgerId());
				}
		     
				return new Response(SUCCESS, SUCCESS_CODE, null, null, generalLedgerMasterDto);
				
		  }catch(Exception e){
		  logger.error("doctorRegistrationWithGLId failed", e);
		  }
		  return new Response<>(ERROR, null, null, null, null);
		
	}

	@Override
	public Response updateDoctorMasterLedgerId(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try {

			return iDoctorMasterDao.updateDoctorMasterLedgerId(doctorMasterDto);

		} catch (Exception e) {
		logger.error("updateDoctorMasterLedgerId failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoctorDetailsEncrypted(DoctorMasterDto doctorMasterDto) {
		try {

			Response res =  iDoctorMasterDao.getDoctorDetailsEncrypted(doctorMasterDto);

			DoctorMasterDto dto = (DoctorMasterDto) res.getObject();

			String name = encrypt(dto.getFirstName());
			dto.setFirstName(name);
			String mob = encrypt(dto.getMobileNumber());
			dto.setMobileNumber(mob);

			return new Response(SUCCESS, null, null, null, dto);

		} catch (Exception e) {
		logger.error("getDoctorDetailsEncrypted failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}


	public  static String encrypt(String originalString) throws ApplicationException {
		try {
			byte[] keyBytes = KEY_STRING.getBytes();
			SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] encryptedBytes = cipher.doFinal(originalString.getBytes());
			return Base64.getEncoder().encodeToString(encryptedBytes);

		}catch(Exception e){
		logger.error("encrypt failed", e);
		}
		return null;
	}

    @Override
    @Transactional
    public Response getDoctorListForAppointmentsWebUser(Integer unitId,Integer specialityId) throws ApplicationException {
        try {
            return iDoctorMasterDao.getDoctorListForAppointmentsWebUser(unitId,specialityId);
        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
        
    }

	@Override
	public Response getAvailableDoctorBySpeciality(DoctorSpecialityMapperDto doctorSpecialityMapperDto) throws ApplicationException {
		try {
			return iDoctorMasterDao.getAvailableDoctorBySpeciality(doctorSpecialityMapperDto);
		} catch (Exception e) {
		logger.error("getAvailableDoctorBySpeciality failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
}
