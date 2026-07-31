package com.param.global.org.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.param.adt.master.global.model.OrganizationMaster;
import com.param.common.dto.CommonResponse;
import com.param.global.common.DocUploadUtility;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.HISFileUploadsPath;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ScheduleActions;
import com.param.global.common.ScheduleTables;
import com.param.global.dao.IScheduleLogsDao;
import com.param.global.dao.IUserMasterDao;
import com.param.global.dto.ScheduleLogsDto;
import com.param.global.dto.UserMasterDto;
import com.param.global.folder.creation.BucketUtility;
import com.param.global.org.dao.IGlobalOrganisationMasterDao;
import com.param.global.org.dao.IOrganizationUnitLicenceDetailsDao;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.global.org.dto.OrganizationUnitLicenceDetailsDto;
import com.param.global.procedure.VO.OrganizationMasterVO;
import com.param.global.utility.UserComponent;
import com.param.privilege.dto.ModuleDto;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings("rawtypes")
public class GlobalOrganizationMasterServiceImpl implements IGlobalOrganizationMasterService , ICommonConstants,HISFileUploadsPath{
	final static Logger logger = Logger.getLogger(GlobalOrganizationMasterServiceImpl.class);


	@Autowired
	IGlobalOrganisationMasterDao iGlobalOrganisationMasterDao;
	
	@Autowired
	IScheduleLogsDao iScheduleLogsDao;
	
	@Autowired
	IEmrService iEmrService;
	
	@Autowired
	IOrganizationUnitLicenceDetailsDao iOrganizationUnitLicenceDetailsDao;
	
	@Autowired
	BucketUtility bucketUtility;
	
	@Autowired
    DocUploadUtility docUploadUtility;
	
	@Autowired
	IUserMasterDao iUserMasterDao;
	
	@Autowired
	@Qualifier("applicationEventMulticaster")
	private ApplicationEventPublisher applicationEventPublisher;
	
	@Autowired
	private IScheduleLogsDao isheduleDao;

	@Autowired
	private com.param.global.service.VerifiedIdentityService verifiedIdentityService;

	// Define the length of the password
    private static final int PASSWORD_LENGTH = 8; // Adjust as needed
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public CommonResponse saveOrganizationMaster(OrganizationMasterDto organizationMasterDto)throws ApplicationException {
	try{
		OrganizationMaster organizationMaster = null;
			Response organizationRes = iGlobalOrganisationMasterDao.getOrganizationByName(organizationMasterDto.getOrganizationName());
			if(organizationRes.getListObject() != null && organizationRes.getListObject().size() > 0 )
			{
				return new CommonResponse(ERROR, ERROR_CODE, ALREADY_EXIST, null, null);
			}else{
				  Response orgResponse = iGlobalOrganisationMasterDao.saveOrganizationMaster(organizationMasterDto);
				if(orgResponse.getStatus() == SUCCESS && orgResponse.getObject() != null)
				{
					 organizationMaster = (OrganizationMaster) orgResponse.getObject();
					 
					 try{
					
					 }catch(Exception e){
					 logger.error("saveOrganizationMaster failed", e);
					 }
					if(organizationMasterDto.getListOrganizationUnitLicenceDetailsDto() != null && organizationMasterDto.getListOrganizationUnitLicenceDetailsDto().size() >0)
					{
						for(OrganizationUnitLicenceDetailsDto licenceDetailsDto : organizationMasterDto.getListOrganizationUnitLicenceDetailsDto()){
						  if(licenceDetailsDto!=null)
						  {
						    licenceDetailsDto.setOrganizationId(organizationMaster.getOrganizationId());
                            licenceDetailsDto.setIsUnit(INACTIVE);
                            iOrganizationUnitLicenceDetailsDao.saveOrganizationUnitLicenceDetails(licenceDetailsDto);						    
						  }
						}
					}
					String orgniazationName = organizationMasterDto.getOrganizationName().replaceAll("[\\s,.]","").trim().toLowerCase();
		            organizationMasterDto.setOrganizationFolder(orgniazationName);
					orgniazationName= orgniazationName+organizationMaster.getOrganizationId()+"/";
					boolean flag = bucketUtility.create(0, orgniazationName);
					if(flag)
					{
					  organizationMasterDto.setOrganizationFolder(orgniazationName);
					  organizationMasterDto.setOrganizationId(organizationMaster.getOrganizationId());
					  iGlobalOrganisationMasterDao.updateOrganizationFolderPath(organizationMasterDto);
					}
					
					Calendar c = Calendar.getInstance();
					c.setTime(TimeZoneComponent.getDateByzone(0));
					c.add(Calendar.YEAR, 5);
					Date expDate = c.getTime();
					String passwordExpiry = GlobalCommonDateUtils.getStringDate(expDate, "dd-MM-yyyy HH:mm:ss");
					
					UserMasterDto userMasterDto = new UserMasterDto();
						userMasterDto.setUserName("SuperAdmin");
						
						String password = "Axonic@1111&";//GlobalCommonDateUtils.generatePassword(PASSWORD_LENGTH);
						String encryptedPassword = GlobalCommonDateUtils.encrypt(password);
						userMasterDto.setPassword(encryptedPassword);
						userMasterDto.setOrganizationId(organizationMaster.getOrganizationId());
						userMasterDto.setPwdExpiry(passwordExpiry);
						userMasterDto.setStatus("A");
						userMasterDto.setLabPin("1234");
						userMasterDto.setIsSuperUser('Y');
					iUserMasterDao.saveUserMaster(userMasterDto);
					
					//updatingUserDetailsForAuth
					UserComponent.updateUserDetailsForAuth(userMasterDto);
					
				}
				
				try{
					ScheduleLogsDto logDto = new ScheduleLogsDto(this);
					// insert into log table
					 String dateTime = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(0), "dd-M-yyyy HH:mm:ss");
					 logDto.setAction(ScheduleActions.INSERT);
					 logDto.setAddedBy(organizationMasterDto.getCreatedBy());
					 logDto.setAddedDate(dateTime);
					 logDto.setDateTime(dateTime);
					 logDto.setApplication(ScheduleActions.HIS);
					 logDto.setErrorCount(0);
					 logDto.setPriority(ScheduleActions.PRIORITY_NORM);
					 logDto.setRecordId(organizationMaster.getOrganizationId());
					 logDto.setStatus(ScheduleActions.STATUS_PENDING);
					 logDto.setTableName(ScheduleTables.ORGANIZATION_MASTER);
					 logDto.setOrgId(organizationMaster.getOrganizationId());
					 //insert code pending
					// applicationEventPublisher.publishEvent(logDto);
					 isheduleDao.saveScheduleLogs(logDto);
					 
					 OrganizationMasterVO masterVO = new OrganizationMasterVO();
					 masterVO.setCreatedBy(1);
					 masterVO.setUpdatedBy(1);
					 masterVO.setOrganizationName(organizationMasterDto.getOrganizationName());
					 masterVO.setStatus(organizationMasterDto.getStatus());
					 masterVO.setOrganizationAddress(organizationMasterDto.getOrganizationAddress());
					 masterVO.setOrganizationContact(organizationMasterDto.getOrganizationContact());
					 masterVO.setCountryId(organizationMasterDto.getCountryId());
					 masterVO.setCityId(organizationMasterDto.getCityId());
					 masterVO.setAreaId(organizationMasterDto.getAreaId());
					 masterVO.setStateId(organizationMasterDto.getStateId());
					 masterVO.setDistrictId(organizationMasterDto.getDistrictId());
					 masterVO.setOrganizationEmailId(organizationMasterDto.getOrganizationEmailId());
					 masterVO.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(0), "dd-M-yyyy hh:mm:ss").toString());
					 masterVO.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(0), "dd-M-yyyy hh:mm:ss").toString());
					 masterVO.setOrganizationId(organizationMaster.getOrganizationId());
					 
					 CommonResponse response = new CommonResponse();
						response.setMessage("unitmaster Inserted Successfully");
						Request request = new Request();
						Payload payload = new Payload();
						payload.setActionType("create");
						payload.setData(masterVO);
						request.setKey(MasterKeyConstants.Organisation.toString());
						request.setPayload(payload);
						//iOtimsService.publish(request);
						//iEmrService.publish(request);
					return new	CommonResponse(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, organizationMaster.getOrganizationId());
						//return response;
				}catch(Exception e){
					logger.error(e.getMessage(), e);
				}

				return new	CommonResponse(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, organizationMaster.getOrganizationId());
			}
			
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}

	@SuppressWarnings("unchecked")
	@Override
	public CommonResponse updateOrganizationMaster(OrganizationMasterDto organizationMasterDto)
			throws ApplicationException {
		try {
			organizationMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			Response orgRes = iGlobalOrganisationMasterDao.getOrganizationByNameAndNotById(organizationMasterDto);
			if (orgRes.getStatus() == SUCCESS && orgRes.getListObject() != null && orgRes.getListObject().size() > 0) {
				return new CommonResponse(ERROR, ERROR_CODE, ALREADY_EXIST, null, null);
			} else {
				Response orgainzationRes = iGlobalOrganisationMasterDao.updateOrganizationMaster(organizationMasterDto);

				if (orgainzationRes.getStatus().equals(SUCCESS)) {
					processAndSaveOrganisationUnitLicenceDetails(organizationMasterDto, orgainzationRes);
				}

				return saveScheduleLogsOrg(organizationMasterDto, orgainzationRes);

			}
		} catch (Exception e) {
		logger.error("updateOrganizationMaster failed", e);
		}
		return new CommonResponse(ERROR, ERROR_CODE, null, null, null);
	}

	@SuppressWarnings("unchecked")
	private CommonResponse saveScheduleLogsOrg(OrganizationMasterDto organizationMasterDto, Response orgainzationRes) {
		try {

			ScheduleLogsDto logDto = new ScheduleLogsDto(this);
			// insert into log table
			String dateTime = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(0),
					"dd-M-yyyy HH:mm:ss");
			logDto.setAction(ScheduleActions.INSERT);
			logDto.setAddedBy(organizationMasterDto.getCreatedBy());
			logDto.setAddedDate(dateTime);
			logDto.setDateTime(dateTime);
			logDto.setApplication(ScheduleActions.HIS);
			logDto.setErrorCount(0);
			logDto.setPriority(ScheduleActions.PRIORITY_NORM);
			logDto.setRecordId(organizationMasterDto.getOrganizationId());
			logDto.setStatus(ScheduleActions.STATUS_PENDING);
			logDto.setTableName(ScheduleTables.ORGANIZATION_MASTER);
			logDto.setOrgId(organizationMasterDto.getOrganizationId());
			// insert code pending
			// applicationEventPublisher.publishEvent(logDto);
			isheduleDao.saveScheduleLogs(logDto);

			CommonResponse response = new CommonResponse();

			OrganizationMaster organizationMaster = (OrganizationMaster) orgainzationRes.getObject();

			OrganizationMasterVO masterVO = new OrganizationMasterVO();
			masterVO.setCreatedBy(1);
			masterVO.setUpdatedBy(1);
			masterVO.setOrganizationName(organizationMasterDto.getOrganizationName());
			masterVO.setStatus(organizationMasterDto.getStatus());
			masterVO.setOrganizationAddress(organizationMasterDto.getOrganizationAddress());
			masterVO.setOrganizationContact(organizationMasterDto.getOrganizationContact());
			masterVO.setCountryId(organizationMasterDto.getCountryId());
			masterVO.setCityId(organizationMasterDto.getCityId());
			masterVO.setAreaId(organizationMasterDto.getAreaId());
			masterVO.setStateId(organizationMasterDto.getStateId());
			masterVO.setDistrictId(organizationMasterDto.getDistrictId());
			masterVO.setOrganizationEmailId(organizationMasterDto.getOrganizationEmailId());
			masterVO.setCreatedDate(
					GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(0), "dd-M-yyyy hh:mm:ss").toString());
			masterVO.setUpdatedDate(
					GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(0), "dd-M-yyyy hh:mm:ss").toString());
			masterVO.setOrganizationId(organizationMaster.getOrganizationId());
			// response.setId(unitId);
			response.setMessage("unitmaster Updated Successfully");
			Request request = new Request();
			Payload payload = new Payload();
			payload.setActionType("update");
			payload.setData(masterVO);
			request.setKey(MasterKeyConstants.Organisation.toString());
			request.setPayload(payload);
			// iOtimsService.publish(request);
			// iEmrService.publish(request);
			/// return response;
			return new CommonResponse(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null,  organizationMaster.getOrganizationId());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new CommonResponse(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, orgainzationRes.getObject());
	}

	private void processAndSaveOrganisationUnitLicenceDetails(OrganizationMasterDto organizationMasterDto,
			Response orgainzationRes) {
		if (organizationMasterDto.getListOrganizationUnitLicenceDetailsDto() != null
				&& organizationMasterDto.getListOrganizationUnitLicenceDetailsDto().size() > 2) {
			for (OrganizationUnitLicenceDetailsDto licenceDetailsDto : organizationMasterDto
					.getListOrganizationUnitLicenceDetailsDto()) {
				if (licenceDetailsDto != null) {
					licenceDetailsDto.setOrganizationId(
							((OrganizationMaster) orgainzationRes.getObject()).getOrganizationId());
					licenceDetailsDto.setIsUnit(INACTIVE);
					licenceDetailsDto.setCreatedBy(
							((OrganizationMaster) orgainzationRes.getObject()).getCreatedBy());
					iOrganizationUnitLicenceDetailsDao
							.saveOrganizationUnitLicenceDetails(licenceDetailsDto);
				}
			}
		}
	}

	@Override
	@Transactional
	public Response getOrganizationById(int organizationId)throws ApplicationException {
		try{
			OrganizationMasterDto organizationMasterDto = iGlobalOrganisationMasterDao
					.getOrganizationById(verifiedIdentityService.getVerifiedOrganizationId());
			if(organizationMasterDto!=null && organizationMasterDto.getOrgPrivileges()!=null && organizationMasterDto.getOrgPrivileges().length()>0) 
			{
				final ObjectMapper objectMapper = new ObjectMapper();
				ModuleDto[] moduleList = objectMapper.readValue(organizationMasterDto.getOrgPrivileges(), ModuleDto[].class);
				List<ModuleDto> moduleDtoList = new ArrayList(Arrays.asList(moduleList));
				organizationMasterDto.setModuleDtosList(moduleDtoList);
			}
            return new Response<>(SUCCESS, null, null, null, organizationMasterDto);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getAllOrganizationMasterList(OrganizationMasterDto organizationMasterDto) throws ApplicationException {
		try{
			return iGlobalOrganisationMasterDao.getAllOrganizationMasterList(organizationMasterDto);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getOrganizationCount() throws ApplicationException {
		try{
			return iGlobalOrganisationMasterDao.getOrganizationCount();
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response updateOrganizationStatus(OrganizationMasterDto organizationMasterDto)throws ApplicationException {
		try{
			organizationMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			return iGlobalOrganisationMasterDao.updateOrganizationStatus(organizationMasterDto);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getLicenceDetailsByOrgUnitId(OrganizationUnitLicenceDetailsDto organizationUnitLicenceDetailsDto)throws ApplicationException {
		try{
			return iOrganizationUnitLicenceDetailsDao.getLicenceDetailsByOrgUnitId(organizationUnitLicenceDetailsDto); 
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getActiveOrganizationList() throws ApplicationException {
		try{
			return iGlobalOrganisationMasterDao.getActiveOrganizationList();
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

  @Override
  @Transactional
  public Response saveOrganizationImage(int organizationId,MultipartFile inputFile) throws ApplicationException {
    try{
      int verifiedOrganizationId = verifiedIdentityService.getVerifiedOrganizationId();
      if(verifiedOrganizationId>0&&!inputFile.isEmpty())
      {
        Response organizationRes =  iGlobalOrganisationMasterDao.getOrganiazationFolderPath(verifiedOrganizationId);
        OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) organizationRes.getObject();
        String organizationLogo =  docUploadUtility.uploadOrganiazationLogo(inputFile, organizationMasterDto);
        organizationMasterDto.setOrganizationLogo(organizationLogo);
        organizationMasterDto.setImage(inputFile.getBytes());
        return iGlobalOrganisationMasterDao.updateImagePath(organizationMasterDto);
      }
    return null;
  }catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
  
  }

@Override
@Transactional
public Response updateOrganizationFolderPath(OrganizationMasterDto organizationMasterDto) throws ApplicationException {
	try{
		return iGlobalOrganisationMasterDao.updateOrganizationFolderPath(organizationMasterDto);
	}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	
}

@Override
@Transactional
public Response getOrganizationListByOrgName(String orgName) throws ApplicationException {
	try{
		return iGlobalOrganisationMasterDao.getOrganizationListByOrgName(orgName);
	}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	
}

	
}
