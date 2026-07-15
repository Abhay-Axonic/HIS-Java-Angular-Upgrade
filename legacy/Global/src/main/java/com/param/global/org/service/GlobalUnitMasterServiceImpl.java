package com.param.global.org.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import javax.transaction.Transactional;

import com.param.actionmaster.DateUtils.DateUtils;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.*;
import com.param.global.dto.CommonDto;
import com.param.global.org.dto.*;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.service.ISmsUtilityConfigService;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.param.adt.master.global.model.OrganizationMaster;
import com.param.adt.master.global.model.UnitMaster;
import com.param.common.dto.CommonResponse;
import com.param.global.dao.ICompanyMasterDao;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dao.IScheduleLogsDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dao.IUserMasterDao;
import com.param.global.dto.ScheduleLogsDto;
import com.param.global.dto.UserMasterDto;
import com.param.global.dto.UserUnitMapperDto;
import com.param.global.folder.creation.BucketUtility;
import com.param.global.mail.IMailingService;
import com.param.global.org.dao.IGlobalOrganisationMasterDao;
import com.param.global.org.dao.IOrganizationUnitLicenceDetailsDao;
import com.param.global.procedure.VO.OrganisationMasterDtoVO;
import com.param.global.unit.dao.IUserRoleMapperDao;
import com.param.global.unit.dao.IUserRoleMasterDao;
import com.param.global.unit.dto.UserRoleMapperDto;
import com.param.global.unit.dto.UserRoleMasterDto;
import com.param.global.utility.UserComponent;
import com.param.privilege.model.Privilege;
import com.param.privilege.service.PrivilegeService;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
@SuppressWarnings({ "rawtypes", "unchecked" })
@Service
public class GlobalUnitMasterServiceImpl implements IGlobalUnitMasterService , ICommonConstants{
	final static Logger logger = Logger.getLogger(GlobalUnitMasterServiceImpl.class);


	@Autowired
	IGlobalUnitMasterDao iGlobalUnitMasterDao;
	
	@Autowired
	IEmrService iEmrService;
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
    BucketUtility bucketUtility;
	
	@Autowired
	IUserMasterDao iUserMasterDao;
	
    @Autowired
    DocUploadUtility docUploadUtility;
    
    @Autowired
    IUserRoleMapperDao iUserRoleMapperDao;
	
	@Autowired
	IOrganizationUnitLicenceDetailsDao iOrganizationUnitLicenceDetailsDao;
	
	@Autowired
	IGlobalOrganisationMasterDao iGlobalOrganisationMasterDao;
	
	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	
	@Autowired
	ICompanyMasterDao iCompanyMasterDao;
	
	@Autowired
	PrivilegeService privilegeService;
	
	@Autowired
	private IScheduleLogsDao isheduleDao;
	
	@Autowired
	@Qualifier("applicationEventMulticaster")
	private ApplicationEventPublisher applicationEventPublisher;
	
	@Autowired
	IUserRoleMasterDao iUserRoleMasterDao;
	
	@Autowired
	IMailingService iMailingService;
	
	@Autowired
	IScheduleLogsDao iScheduleLogsDao;

	@Autowired
	private ISmsUtilityConfigService smsUtilityService;

	@Override
	@Transactional
	public CommonResponse saveUnitMaster(UnitMasterDto unitMasterDto)throws ApplicationException {
		try{
			Response unitRes = iGlobalUnitMasterDao.getUnitByName(unitMasterDto);
			UnitMaster unitMaster = null;
			if(unitRes.getListObject() != null && unitRes.getListObject().size() > 0)
			{
				return new CommonResponse(ERROR, ERROR_CODE, ALREADY_EXIST, null, null);
			}else
			{
				OrganisationMasterDtoVO organasationMasterDtoVO = new OrganisationMasterDtoVO();
			  
				Response unit = iGlobalUnitMasterDao.saveUnitMaster(unitMasterDto);
				
				if (unit.getObject() != null) 
				{
					unitMaster = (UnitMaster) unit.getObject();
					
					//addNewUnitsTimeZoneToMap
					TimeZoneComponent.updateTimezoneMap(unitMaster.getUnitId(), unitMaster.getUnitTimeZone()!=null && unitMaster.getUnitTimeZone().length()>0 ? unitMaster.getUnitTimeZone() : "Asia/Calcutta");
					
					if (unitMasterDto.getListOrganizationUnitLicenceDetailsDto() != null
							&& unitMasterDto.getListOrganizationUnitLicenceDetailsDto().size() > 0) 
					{
						
						for (OrganizationUnitLicenceDetailsDto organizationUnitLicenceDetailsDto : unitMasterDto.getListOrganizationUnitLicenceDetailsDto()) 
						{
							if(organizationUnitLicenceDetailsDto.getLicenceTypeId()!=null)
							{
								organizationUnitLicenceDetailsDto.setOrganizationId(null);
								organizationUnitLicenceDetailsDto.setIsUnit(ACTIVE);
								organizationUnitLicenceDetailsDto.setUnitId(unitMaster.getUnitId());
								iOrganizationUnitLicenceDetailsDao.saveOrganizationUnitLicenceDetails(organizationUnitLicenceDetailsDto);
							}
							
						}
					}

					String unitFolderName = null;
					OrganizationMaster organizationMaster = (OrganizationMaster) sessionFactory.getCurrentSession()
							.getNamedQuery("GET_FOLDER_PATH_BY_ORGANIZATION_ID")
							.setInteger("orgId", unitMasterDto.getOrganizationId())
							.setResultTransformer(Transformers.aliasToBean(OrganizationMaster.class)).uniqueResult();
					if (organizationMaster != null && organizationMaster.getOrganizationFolder() != null) {
						unitFolderName = unitMasterDto.getUnitName().replaceAll("[\\s,.]", "").trim().toLowerCase();
						unitMasterDto.setUnitFolder(organizationMaster.getOrganizationFolder() + unitFolderName
								+ unitMaster.getUnitId() + "/");
						boolean flag = bucketUtility.create(1, unitMasterDto.getUnitFolder());
						if (flag) {
							unitMasterDto.setUnitFolder(organizationMaster.getOrganizationFolder() + unitFolderName
									+ unitMaster.getUnitId() + "/");
							unitMasterDto.setUnitId(unitMaster.getUnitId());
							iGlobalUnitMasterDao.updateFolderPath(unitMasterDto);
						}
					}
					
					//defaultConfiguration
					Integer userId = iUserMasterDao.getIsCiniUser(unitMasterDto.getOrganizationId());
					
					if(userId == 0)
					{
						Calendar c = Calendar.getInstance();
						c.setTime(TimeZoneComponent.getDateByzone(unitMasterDto.getUnitId()));
						c.add(Calendar.YEAR, 5);
						Date expDate = c.getTime();
						String passwordExpiry = GlobalCommonDateUtils.getStringDate(expDate, "dd-MM-yyyy HH:mm:ss");
						
						UserMasterDto userMasterDto = new UserMasterDto();
							userMasterDto.setUserName("SuperAdmin");
							String encryptedPassword = GlobalCommonDateUtils.encrypt("12345");
							userMasterDto.setPassword(encryptedPassword);
							userMasterDto.setOrganizationId(unitMasterDto.getOrganizationId());
							userMasterDto.setPwdExpiry(passwordExpiry);
							userMasterDto.setStatus("A");
							userMasterDto.setLabPin("1234");
							userMasterDto.setIsSuperUser('Y');
						Response userRes =  iUserMasterDao.saveUserMaster(userMasterDto);
						userId=(int)userRes.getObject();
						
						//updatingUserDetailsForAuth
						UserComponent.updateUserDetailsForAuth(userMasterDto);
						
					}
					if(userId>0)
					{
						UserUnitMapperDto userUnitMapperDto = new UserUnitMapperDto(); 
							userUnitMapperDto.setUserId(userId);
							userUnitMapperDto.setOrganisationId(unitMasterDto.getOrganizationId());
							userUnitMapperDto.setUnitId(unitMaster.getUnitId());
							userUnitMapperDto.setStatus('A');
							userUnitMapperDto.setCreatedBy(userId);
							userUnitMapperDto.setUpdatedBy(userId);
						iUserRoleMapperDao.saveUserUnitMapper(userUnitMapperDto);
					
						//create role
						UserRoleMasterDto userRoleMaster = new UserRoleMasterDto();
						  userRoleMaster.setRoleName("Admin");
						  userRoleMaster.setOrganisationId(unitMasterDto.getOrganizationId());
						  userRoleMaster.setIsCounterApplicable('Y');
						  userRoleMaster.setUnitId(unitMaster.getUnitId());
						  userRoleMaster.setCreatedBy(userId);
						  userRoleMaster.setUpdatedBy(userId);
						  userRoleMaster.setStatus('A');
						Response res = iUserRoleMasterDao.saveUserRoleMaster(userRoleMaster);
						Integer roleId = (Integer)res.getObject();
						
						UserRoleMasterDto userRoleMasterDoc = new UserRoleMasterDto();
							userRoleMasterDoc.setRoleName("Doctor");
							userRoleMasterDoc.setOrganisationId(unitMasterDto.getOrganizationId());
							userRoleMasterDoc.setIsCounterApplicable('Y');
							userRoleMasterDoc.setUnitId(unitMaster.getUnitId());
							userRoleMasterDoc.setCreatedBy(userId);
							userRoleMasterDoc.setUpdatedBy(userId);
							userRoleMasterDoc.setStatus('A');
						Response res2 = iUserRoleMasterDao.saveUserRoleMaster(userRoleMasterDoc);
						Integer docRoleId = (Integer)res2.getObject();
						
						UserRoleMasterDto userRoleMasterNurse = new UserRoleMasterDto();
							userRoleMasterNurse.setRoleName("Nurse");
							userRoleMasterNurse.setOrganisationId(unitMasterDto.getOrganizationId());
							userRoleMasterNurse.setIsCounterApplicable('Y');
							userRoleMasterNurse.setUnitId(unitMaster.getUnitId());
							userRoleMasterNurse.setCreatedBy(userId);
							userRoleMasterNurse.setUpdatedBy(userId);
							userRoleMasterNurse.setStatus('A');
						Response res3 = iUserRoleMasterDao.saveUserRoleMaster(userRoleMasterNurse);
						Integer nurseRoleId = (Integer)res3.getObject();
						
						//assign user role mapper
						UserRoleMapperDto userRoleMapper=new UserRoleMapperDto();
							userRoleMapper.setOrganisationId(unitMasterDto.getOrganizationId());
							userRoleMapper.setRoleId(roleId);
							userRoleMapper.setUserId(userId);
							userRoleMapper.setUnitId(unitMaster.getUnitId());	
							userRoleMapper.setUpdatedBy(userId);
							userRoleMapper.setStatus('A');
							userRoleMapper.setCreatedBy(userId);
						iUserRoleMapperDao.saveUserRoleMapper(userRoleMapper);
						
						
						Privilege p =new Privilege();
							p.setModules(unitMasterDto.getPrevilageString());
							p.setModuleType(1);
							p.setOrganisationId(unitMasterDto.getOrganizationId());
							p.setUnitId(unitMaster.getUnitId());
							p.setRoleId(roleId);
						privilegeService.addPrivilege(p);
						
						Integer selfPayeeId = iCompanyMasterDao.addDefaultEntry(unitMaster.getUnitId(),unitMasterDto.getOrganizationId());
					
						iUnitApplicationBillingConfigurationDao.saveDefaultEntry(unitMasterDto.getOrganizationId(), unitMaster.getUnitId(),selfPayeeId,roleId,userId,docRoleId,nurseRoleId);
					}
					
				}

				try {
					ScheduleLogsDto logDto = new ScheduleLogsDto(this);
					// insert into log table
					String dateTime = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitMasterDto.getUnitId()), "dd-M-yyyy HH:mm:ss");
					logDto.setAction(ScheduleActions.INSERT);
					logDto.setAddedBy(unitMasterDto.getCreatedBy());
					logDto.setAddedDate(dateTime);
					logDto.setDateTime(dateTime);
					logDto.setApplication(ScheduleActions.HIS);
					logDto.setErrorCount(0);
					logDto.setPriority(ScheduleActions.PRIORITY_NORM);
					logDto.setRecordId(unitMaster.getUnitId());
					logDto.setStatus(ScheduleActions.STATUS_PENDING);
					logDto.setTableName(ScheduleTables.UNIT_MASTER);
					logDto.setOrgId(unitMasterDto.getOrganizationId());
					// insert code pending
					//applicationEventPublisher.publishEvent(logDto);
					isheduleDao.saveScheduleLogs(logDto);
					
					int org = unitMaster.getOrganizationId();
					OrganizationMasterDto orgMaster = iGlobalOrganisationMasterDao.getOrganizationById(org);
					organasationMasterDtoVO.setOrganizationId(orgMaster.getOrganizationId());
					organasationMasterDtoVO.setOrganizationName(orgMaster.getOrganizationName());
					unitMasterDto.setOrganization(organasationMasterDtoVO);
					unitMasterDto.setUnitId(unitMaster.getUnitId());
					CommonResponse response = new CommonResponse();
					// response.setId(unitId);
					response.setMessage("Unit master Inserted Successfully");
					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(unitMasterDto);
					request.setKey(MasterKeyConstants.Unit.toString());
					request.setPayload(payload);
					//iOtimsService.publish(request);
					//iEmrService.publish(request);
					return new CommonResponse(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, unitMaster);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}

				return new CommonResponse(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, unitMaster);

			}
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public CommonResponse updateUnitMaster(UnitMasterDto unitMasterDto)throws ApplicationException {
		try{
			Response unitRes = iGlobalUnitMasterDao.getUnitByNameAndNotById(unitMasterDto);
			if(unitRes.getListObject() != null && unitRes.getListObject().size() > 0){
				return new CommonResponse(ERROR, ERROR_CODE, ALREADY_EXIST, null, null);
			}else{
				
				try{
					iGlobalUnitMasterDao.updateUnitMaster(unitMasterDto);
					//updateNewUnitsTimeZoneToMap
					TimeZoneComponent.updateTimezoneMap(unitMasterDto.getUnitId(), unitMasterDto.getUnitTimeZone()!=null && unitMasterDto.getUnitTimeZone().length()>0 ? unitMasterDto.getUnitTimeZone() : "Asia/Calcutta");

					// save licence Details
					if (unitMasterDto.getListOrganizationUnitLicenceDetailsDto() != null
						&& unitMasterDto.getListOrganizationUnitLicenceDetailsDto().size() > 0) {
					Response res=iOrganizationUnitLicenceDetailsDao.updatePreviousRecord(unitMasterDto.getUnitId());
					if(res.getStatus().equals(SUCCESS)) {						
					
						for (OrganizationUnitLicenceDetailsDto organizationUnitLicenceDetailsDto : unitMasterDto
								.getListOrganizationUnitLicenceDetailsDto()) {
							organizationUnitLicenceDetailsDto.setOrganizationId(null);
							organizationUnitLicenceDetailsDto.setIsUnit(ACTIVE);
							organizationUnitLicenceDetailsDto.setUnitId(unitMasterDto.getUnitId());
							iOrganizationUnitLicenceDetailsDao.saveOrganizationUnitLicenceDetails(organizationUnitLicenceDetailsDto);
						}
					}
				}
				
				ScheduleLogsDto logDto = new ScheduleLogsDto(this);
				String dateTime = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitMasterDto.getUnitId()), "dd-M-yyyy HH:mm:ss");
					logDto.setAction(ScheduleActions.INSERT);
					logDto.setAddedBy(unitMasterDto.getCreatedBy());
					logDto.setAddedDate(dateTime);
					logDto.setDateTime(dateTime);
					logDto.setApplication(ScheduleActions.HIS);
					logDto.setErrorCount(0);
					logDto.setPriority(ScheduleActions.PRIORITY_NORM);
					logDto.setRecordId(unitMasterDto.getUnitId());
					logDto.setStatus(ScheduleActions.STATUS_PENDING);
					logDto.setTableName(ScheduleTables.UNIT_MASTER);
					logDto.setOrgId(unitMasterDto.getOrganizationId());
				// insert code pending
				//applicationEventPublisher.publishEvent(logDto);
					isheduleDao.saveScheduleLogs(logDto);
				
				/*OrganizationMasterDto orgMaster = null;
				int org = unitMasterDto.getOrganizationId();
				Response organisation = iGlobalOrganisationMasterDao.getOrganizationById(org);
				orgMaster = (OrganizationMasterDto) organisation.getObject();
				unitMasterDto.setOrganizationId(orgMaster.getOrganizationId());
				unitMasterDto.setOrganizationName(orgMaster.getOrganizationName());
				organasationMasterDtoVO.setOrganizationId(orgMaster.getOrganizationId());
				organasationMasterDtoVO.setOrganizationName(orgMaster.getOrganizationName());
				unitMasterDto.setOrganization(organasationMasterDtoVO);
				CommonResponse response = new CommonResponse();
				//response.setId(unitId);
				response.setMessage("unitmaster Updated Successfully");
				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(unitMasterDto);
				request.setKey(MasterKeyConstants.Unit.toString());
				request.setPayload(payload);
				//iOtimsService.publish(request);
				i//EmrService.publish(request);*/
				//return response;
				}
				
				catch(Exception e){
					throw new ApplicationException(e.getMessage(), e);
				}
				return new	CommonResponse(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, unitRes.getObject());
			}
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
		//return new Response(ERROR, null, null, null, null);
	}

	@Override
	@Transactional
	public Response getUnitById(Integer unitId) throws ApplicationException {
		try{
			return iGlobalUnitMasterDao.getUnitById(unitId);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response updateUnitStatusById(UnitMasterDto unitMasterDto)throws ApplicationException {
		try{
			return iGlobalUnitMasterDao.updateUnitStatusById(unitMasterDto);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getAllUnitList(UnitMasterDto unitMasterDto){
	try{
		return iGlobalUnitMasterDao.getAllUnitList(unitMasterDto);
	}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}

	@Override
	@Transactional
	public Response getUnitTotalCount(int orgId) throws ApplicationException {//minesh
		try{
			return iGlobalUnitMasterDao.getUnitTotalCount(orgId);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

  @Override
  @Transactional
  public Response saveUnitImage(int unitId, MultipartFile inputFile) throws ApplicationException {
    try{
      if(unitId>0&&!inputFile.isEmpty())
      {
        Response unitRes =  iGlobalUnitMasterDao.getUnitFolderPath(unitId);
        UnitMasterDto unitMasterDtoDto = (UnitMasterDto) unitRes.getObject();
        String unitLogo =  docUploadUtility.uploadOrganizationUnitLogo(inputFile, unitMasterDtoDto);
        unitMasterDtoDto.setUnitLogo(unitLogo);
        unitMasterDtoDto.setImage(inputFile.getBytes());
        return iGlobalUnitMasterDao.updateImagePath(unitMasterDtoDto);
      }
    return null;
  }catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
  
  }

@Override
@Transactional
public String getGeneratedNo(int storeId, int unitId, int screenId) throws ApplicationException {
	try{
		return iGlobalUnitMasterDao.getGeneratedNo(storeId, unitId, screenId);
	}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	
}

@Override
public String getUnitImage(int unitId) throws ApplicationException {
	try{
		return iGlobalUnitMasterDao.getUnitImage(unitId);
	}catch(Exception e){
	logger.error("getUnitImage failed", e);
	}
	return null;
}

@Override
@Transactional
public Response getUnitByOrganisation(int orgId) throws ApplicationException {
	try{
		return iGlobalUnitMasterDao.getUnitByOrganisation(orgId);
	}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	
}

@Override
@Transactional
public Response getActiveUnitListForFolderCreation() throws ApplicationException {
	try{
		return iGlobalUnitMasterDao.getActiveUnitListForFolderCreation();
	}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	
}

@Override
@Transactional
public Response updateFolderPath(UnitMasterDto unitMasterDto) throws ApplicationException {
	try{
		return iGlobalUnitMasterDao.updateFolderPath(unitMasterDto);
	}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	
}


@Override
public Response sendMailInvite(String doctorMail, String patientMail, String date ,String time,String remark) throws ApplicationException {
	try{
		
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder("MT");
		Random rnd = new Random();
		while (salt.length() < 20) {
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		String url="https://meet.jit.si/"+saltStr;
//		return saltStr;
		String mails=doctorMail+ ","+patientMail;
		
		iMailingService.sendMailInvite(url, mails , date ,time , remark);
		return new Response(SUCCESS, SUCCESS_CODE, null, null, url);
	}catch(Exception e){
	logger.error("sendMailInvite failed", e);
	}
	return null;
}

@Override
@Transactional
public Response gtMyLifeBaseUrl() throws ApplicationException {
	try{
		return iScheduleLogsDao.getConnectionStringForMyLife();
	}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	
}





	@Override
	@org.springframework.transaction.annotation.Transactional
	public Response getSubScriptionTypes() throws ApplicationException
	{
			List<CommonDto> list =  Arrays.asList(new CommonDto(ICommonConstants.MONTHLY,ICommonConstants.MONTHLY),
					new CommonDto(ICommonConstants.QUARTERLY,ICommonConstants.QUARTERLY),
					new CommonDto(ICommonConstants.HALF_YEARLY,ICommonConstants.HALF_YEARLY),
					new CommonDto(ICommonConstants.YEARLY,ICommonConstants.YEARLY));
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	}

	@Override
	@org.springframework.transaction.annotation.Transactional
	public Response addUpdateSubscriptionConfig(UnitSubscriptionConfigDto dto) throws ApplicationException {
		return iGlobalUnitMasterDao.addUpdateSubscriptionConfig(dto);
	}

	@Override
	@org.springframework.transaction.annotation.Transactional
	public Response getSubScriptionConfig(UnitMasterDto dto) throws ApplicationException {
		return iGlobalUnitMasterDao.getSubScriptionConfig(dto);
	}

	@Override
	@org.springframework.transaction.annotation.Transactional
	public Response addSubScrtionDetails(UnitSubscriptionDetailsDto dto) throws ApplicationException {
		Response res = iGlobalUnitMasterDao.checkSubscriptionExist(dto);
		if(res.getObject()!=null){
			return res;
		}
		return iGlobalUnitMasterDao.addSubScrtionDetails(dto);
	}

	@Override
	@org.springframework.transaction.annotation.Transactional
	public Response getSubcriptionDetails(UnitSubscriptionDetailsDto dto) throws ApplicationException {
		this.inactivateSubcriptions();
		return iGlobalUnitMasterDao.getSubcriptionDetails(dto);
	}



	@Override
	@org.springframework.transaction.annotation.Transactional
	public Response inactivateSubcriptions() throws ApplicationException {
		return iGlobalUnitMasterDao.inactivateSubcriptions();
	}

	@Override
	@org.springframework.transaction.annotation.Transactional
	public void reminderForSubscription() throws ApplicationException {
		Response res =  iGlobalUnitMasterDao.findSubscriptionsExpiringInDays();
		if(res.getListObject()!=null){
			List<UnitSubscriptionDetailsDto> detailsDtoList = res.getListObject();
			for(UnitSubscriptionDetailsDto dto : detailsDtoList){
				try {
					//1.send SMS
					if (dto.getUnitContact() != null) {
						SmsUtilityConfigInfoDto emailinfo = new SmsUtilityConfigInfoDto();
						emailinfo.setSubscriptionId(dto.getSubscriptionId());
						emailinfo.setUniqueId(Optional.ofNullable(dto.getSubscriptionId().toString()).orElse(null));
						emailinfo.setUnitId(dto.getUnitId());
						emailinfo.setOrganizationId(dto.getOrganizationId());
						emailinfo.setMobileNo(dto.getUnitContact());
						emailinfo.setSubscriptionEndDate(dto.getSubscriptionEndDateString());
						smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.SUBSCRIPTION_REMINDER, emailinfo);
					}
					//2.send Email
					if (dto.getUnitEmailId() != null) {
						SmsUtilityConfigInfoDto smsInfo = new SmsUtilityConfigInfoDto();
						smsInfo.setSubscriptionId(dto.getSubscriptionId());
						smsInfo.setUniqueId(Optional.ofNullable(dto.getSubscriptionId().toString()).orElse(null));
						smsInfo.setUnitId(dto.getUnitId());
						smsInfo.setOrganizationId(dto.getOrganizationId());
						smsInfo.setToEmailId(dto.getUnitEmailId());
						smsInfo.setSubscriptionEndDate(dto.getSubscriptionEndDateString());
						smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.SUBSCRIPTION_REMINDER, smsInfo);
					}
				}catch (Exception e){
				logger.error("reminderForSubscription failed for unit " + dto.getUnitId(), e);
				}
			}
		}
	}

	@Override
	@org.springframework.transaction.annotation.Transactional
	public Response checkUnitSubscription(UnitMasterDto Dto,Integer userId) throws ApplicationException {
		try{

			Response superAdminRes  = iUserMasterDao.getSuperAdminUser(Dto.getOrganizationId(),Dto.getUnitId(),userId);
			if(superAdminRes.getObject()!=null){
				UserMasterDto userDto =  (UserMasterDto)superAdminRes.getObject();
				if(userDto.getIsSuperUser()!=null && userDto.getIsSuperUser().equals('Y')){
					return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
				}
			}

			UnitSubscriptionConfigDto config = null;
			Response configRes = iGlobalUnitMasterDao.getSubScriptionConfig(Dto);
			if(configRes.getObject()!=null){
				config = (UnitSubscriptionConfigDto)configRes.getObject();
			}
			else{
				return configRes;
			}

			Response res  =  iGlobalUnitMasterDao.getAciveSubscriptionByUnit(Dto);
			if(res!=null && res.getObject()!=null){
				UnitSubscriptionDetailsDto uSDto = (UnitSubscriptionDetailsDto) res.getObject();
				LocalDate currentDate = TimeZoneComponent.getLocalDateByZone(Dto.getUnitId());
				LocalDate expirationDateNext15 = currentDate.plusDays(15);
				LocalDate subscriptionEndDate = TimeZoneComponent.convertUtilDateToLocalDate(Dto.getUnitId(),uSDto.getSubscriptionEndDate());
				long daysBetween = ChronoUnit.DAYS.between(currentDate, subscriptionEndDate);
				if(subscriptionEndDate.isEqual(currentDate) && !uSDto.getFutureSubscriptionTaken()){
					return new Response<>(SUCCESS, SUCCESS_SUBSCRIPTION1, "Final reminder: Your subscription is set to expire at 12:00 AM. Access to the application will be unavailable thereafter.", null, null,config);
				}
				else if( daysBetween < 15 && !subscriptionEndDate.isEqual(currentDate) && !uSDto.getFutureSubscriptionTaken()){
					return new Response<>(SUCCESS, SUCCESS_SUBSCRIPTION15, "For Uninterrupted application usage pay your subscription charges on or before "+uSDto.getSubscriptionEndDateString(), null, null,config);
				}
				else{
					return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
				}
			 }
			else{
				return new Response<>(ERROR, ERROR_SUBSCRIPTION, "Your subscription is expired. Please proceed to pay and renew your subscription.", null, null,config);
			}
		}catch(Exception e){
		logger.error("checkUnitSubscription failed", e);
		}
		return null;
	}

	@Override
	@org.springframework.transaction.annotation.Transactional
	public Response processUnitSubscription(String razorPayOrderId) throws ApplicationException {
		try {
			Response  res =  iGlobalUnitMasterDao.getSubscriptionDetailsByROrderId(razorPayOrderId);

			//calculate Subscription end date
			if(res.getObject()!=null){
				Date nextDate = null;
				UnitSubscriptionDetailsDto dto = (UnitSubscriptionDetailsDto) res.getObject();
				dto.setPaidAmount(dto.getSubscriptionNetAmount());
				dto.setAmountPaidStatus("PAID");
				 if(dto.getLastSubscriptionEndDate()!=null){
					 Date endDate = DateUtils.getDate(dto.getLastSubscriptionEndDate(),"yyyy-MM-dd");
					 Calendar calendar = Calendar.getInstance();
					 calendar.setTime(endDate);
					 calendar.add(Calendar.DAY_OF_YEAR, 1);
					 nextDate = calendar.getTime();
				 }
				 else{
					 nextDate = new Date();
				 }

				dto.setSubscriptionStartDate(nextDate);
				dto.setSubscriptionEndDate(calculateSubscriptionEndDate(nextDate,dto.getSubscriptionType(),dto.getUnitId()));
				dto.setPaymentOrderId(razorPayOrderId);
				Response subRes = iGlobalUnitMasterDao.addSubScrtionDetails(dto);
				return subRes;
			}
			return new Response<>(ERROR, ERROR_CODE, "error in subscription", null, null);
		} catch (Exception e) {
		logger.error("processUnitSubscription failed", e);
			return new Response<>(ERROR, ERROR_CODE, "error in subscription", null, null);
		}
	}

	@Override
	@org.springframework.transaction.annotation.Transactional
	public Response getSubscriptionById(UnitSubscriptionDetailsDto dto) throws ApplicationException {
		return iGlobalUnitMasterDao.getSubscriptionById(dto);
	}

	public static Date calculateSubscriptionEndDate(Date startDate, String subscriptionType,Integer unitId) {
		LocalDate startLocalDate = TimeZoneComponent.convertUtilDateToLocalDate(unitId,startDate);
		LocalDate endLocalDate = null;
		if(MONTHLY.equalsIgnoreCase(subscriptionType)){
			endLocalDate =  startLocalDate.plusMonths(1);
		}
		else if(QUARTERLY.equalsIgnoreCase(subscriptionType)){
			endLocalDate = startLocalDate.plusMonths(3);
		}
		else if(HALF_YEARLY.equalsIgnoreCase(subscriptionType)){
			endLocalDate = startLocalDate.plusMonths(6);
		}
		else if(YEARLY.equalsIgnoreCase(subscriptionType)){
			endLocalDate = startLocalDate.plusYears(1);
		}
		else{
			throw new BillingException("Unsupported subscription type");
		}
		return TimeZoneComponent.convertToLocalDateToDate(unitId,endLocalDate);
	}
}
