package com.param.global.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.param.adt.master.global.model.UserMaster;
import com.param.global.common.DocUploadUtility;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IEmployeeRegistrationDao;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dao.IUserMasterDao;
import com.param.global.dto.DependentDetailsDto;
import com.param.global.dto.EmployeeRegistrationDto;
import com.param.global.dto.UserMasterDto;
import com.param.global.dto.UserUnitMapperDto;
import com.param.global.mail.IMailingService;
import com.param.global.org.dto.UnitMasterDto;
import com.param.global.unit.dao.IUserRoleMapperDao;
import com.param.global.unit.dto.OTIMSUserRoleMapperDto;
import com.param.global.unit.dto.UserRoleMapperDto;
import com.param.global.utility.UserComponent;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class EmployeeRegistrationServiceImpl implements IEmployeeRegistrationService,ICommonConstants{
	final static Logger logger = Logger.getLogger(EmployeeRegistrationServiceImpl.class);


	@Autowired
	IEmployeeRegistrationDao iEmployeeRegistrationDao;
	
	@Autowired
	private IUserMasterDao iUserMasterDao;
	
	@Autowired
	IDependentDetailsService iDependentDetailsService;
	
	 @Autowired
	 DocUploadUtility docUploadUtility;
	 
	 @Autowired
     private IGlobalUnitMasterDao iGlobalUnitMasterDao;
	 
	 @Autowired
	 IMailingService iMailingService;
	 
	 @Autowired
	 private IUserRoleMapperDao iUserRoleMapperDao;
	 
	// Define the length of the password
	    private static final int PASSWORD_LENGTH = 8; // Adjust as needed
	
	@Override
	public Response saveEmployeeRegistration(EmployeeRegistrationDto employeeRegistrationDto)
			throws ApplicationException {
		try {
			Integer getConfiguredUserLimit=iEmployeeRegistrationDao.getConfiguredUserLimit(employeeRegistrationDto);
			Integer getUserCount=iEmployeeRegistrationDao.getUserCount(employeeRegistrationDto);
			if(getUserCount!=null && getConfiguredUserLimit!=null && getUserCount>=getConfiguredUserLimit)
			{
                return new Response(ERROR, ALREADY_EXIST_CODE, "You have exceeded user limit...!", null,null);
			}
			UserMasterDto userMasterDto = new UserMasterDto();
			Response res = iEmployeeRegistrationDao.checkUniqueEmployee(employeeRegistrationDto);
			if (res.getListObject().size() > 0) {
				return new Response(ERROR, null, "Already exist !!", null, null);
			} else {
				
				//code added by Ganesh 15 APR 2019
				Calendar c = Calendar.getInstance();
				c.setTime(TimeZoneComponent.getDateByzone(employeeRegistrationDto.getUnitId()));
				c.add(Calendar.YEAR, 1);
				Date expDate = c.getTime();
				String passwordExpiry = GlobalCommonDateUtils.getStringDate(expDate, "dd-MM-yyyy HH:mm:ss");
				
				//if application user ?? 
				if(employeeRegistrationDto.getIsUser()=='Y')
				{
				
					
					userMasterDto.setUserName(employeeRegistrationDto.getEmpCode());
					String password = GlobalCommonDateUtils.generatePassword(PASSWORD_LENGTH);
					//String password = GlobalCommonDateUtils.generatePassword(employeeRegistrationDto.getFirstName(), employeeRegistrationDto.getLastName(), GlobalCommonDateUtils.getDate(employeeRegistrationDto.getDob(),"dd-M-yyyy HH:mm:ss"));
					/* get encrypted password*/
					String encryptedPassword = GlobalCommonDateUtils.encrypt(password);
					userMasterDto.setPassword(encryptedPassword);
					userMasterDto.setOrganizationId(employeeRegistrationDto.getOrganizationId());
					userMasterDto.setUnitId(employeeRegistrationDto.getUnitId());
					userMasterDto.setCreatedBy(employeeRegistrationDto.getCreatedBy());
					userMasterDto.setCreatedDate(userMasterDto.getCreatedDate());
					userMasterDto.setUpdatedBy(employeeRegistrationDto.getCreatedBy());
					userMasterDto.setUpdatedDate(employeeRegistrationDto.getCreatedDate());
					userMasterDto.setEmailId(employeeRegistrationDto.getEmail());
					userMasterDto.setPwdExpiry(passwordExpiry);
					userMasterDto.setStatus("A");
					userMasterDto.setLabPin(employeeRegistrationDto.getLabPin());
					Response userRes =  iUserMasterDao.saveUserMaster(userMasterDto);
					if(userRes.getStatus().equals(SUCCESS))
					{
						//updatingUserDetailsForAuth
						UserComponent.updateUserDetailsForAuth(userMasterDto);
						//String decryptedPwd = GlobalCommonDateUtils.decrypt(encryptedPassword);
						employeeRegistrationDto.setUserId((int)userRes.getObject());
					}
					else {
						return new Response(ERROR, COMMON_ERROR, "Failed To Save User", null, null);
					}
				
				}
				
				//save for selected HIS role list
				if(employeeRegistrationDto.getSelectedHisRoleList().size()>0) {
					for(UserRoleMapperDto userRoleMapperDto:employeeRegistrationDto.getSelectedHisRoleList()) {
						userRoleMapperDto.setUserId(employeeRegistrationDto.getUserId());
						iUserRoleMapperDao.saveUserRoleMapper(userRoleMapperDto);
					}
				}
				
				//save for selected OTIMS Role List
				if(employeeRegistrationDto.getSelectedOTIMSRoleList().size()>0) {
					for(OTIMSUserRoleMapperDto otimsUserRoleMapperDto:employeeRegistrationDto.getSelectedOTIMSRoleList()) {
						otimsUserRoleMapperDto.setUserId(employeeRegistrationDto.getUserId());
					  iEmployeeRegistrationDao.saveOTIMSUserRoleMapper(otimsUserRoleMapperDto);
					}
				}
				
				//save for selected Unit  List
				if(employeeRegistrationDto.getSelectedUserUnitList().size()>0) {
					for(UserUnitMapperDto userUnitMapperDto:employeeRegistrationDto.getSelectedUserUnitList()) {
						userUnitMapperDto.setUserId(employeeRegistrationDto.getUserId());
						userUnitMapperDto.setMisRights(employeeRegistrationDto.getDefaultMISRights());
						userUnitMapperDto.setCoversheetRights(employeeRegistrationDto.getDefaultCoversheetRights());
					    iUserRoleMapperDao.saveUserUnitMapper(userUnitMapperDto);
					}
				}
				
				
				
				if(employeeRegistrationDto.getStaffSpecialityMapperDtosList().size() > 0)
			 	{
				  Response docSpe = iEmployeeRegistrationDao.saveStaffSpecialityMapper(employeeRegistrationDto);
				  
				  iEmployeeRegistrationDao.saveStaffSubSpecialityMapper(employeeRegistrationDto);
				  
			 	}
				employeeRegistrationDto.setPwdExpiry(passwordExpiry);
				Response res1 = iEmployeeRegistrationDao.saveEmployeeRegistration(employeeRegistrationDto);
				employeeRegistrationDto.setEmployeeId((Integer) res1.getObject());
				if (res1.getStatus().equals(SUCCESS)) {
					userMasterDto.setEmployeeId((Integer) res1.getObject());
					userMasterDto.setPassword(GlobalCommonDateUtils.decrypt(userMasterDto.getPassword()));
					Response patientResponse = iEmployeeRegistrationDao.saveEmployeeDetails(employeeRegistrationDto);

					if (patientResponse.getStatus().equals(SUCCESS)
							&& employeeRegistrationDto.getDependentDetailsDtosList().size() > 0) 
					{
							DependentDetailsDto dependentDetailsDto = new DependentDetailsDto();
							dependentDetailsDto.setTypeId(employeeRegistrationDto.getTypeId());
							dependentDetailsDto.setEmpDocId(employeeRegistrationDto.getEmployeeId());
							dependentDetailsDto.setDependentDetailsDtoList(employeeRegistrationDto.getDependentDetailsDtosList());
							Response DependentRes = iDependentDetailsService.saveDependentDetails(dependentDetailsDto);
							if (DependentRes.getStatus().equals(SUCCESS)) 
							{
								
								//iMailingService.sendUserName(userMasterDto, RegistrationType.REG_EMPLOYEE);
								//iMailingService.sendUserPassword(userMasterDto, RegistrationType.REG_EMPLOYEE);
								
								return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, res1.getObject(),userMasterDto);
							}
							else{
								
								return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
							}
							
					}
					else{
						
						//iMailingService.sendUserName(userMasterDto, RegistrationType.REG_EMPLOYEE);
						//iMailingService.sendUserPassword(userMasterDto, RegistrationType.REG_EMPLOYEE);
						
						return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, res1.getObject(),userMasterDto);
					}
				}
			}

		} catch (Exception e) {
		logger.error("saveEmployeeRegistration failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getEmployeeDetails(EmployeeRegistrationDto employeeRegistrationDto) throws ApplicationException {
		try {
			List<EmployeeRegistrationDto> employeeRegistrationDtosList = new LinkedList<EmployeeRegistrationDto>(); 
			Response responseEmployeeDetails = iEmployeeRegistrationDao.getEmployeeDetails(employeeRegistrationDto);
			if(responseEmployeeDetails.getListObject().size()>0)
			{
				ListIterator<EmployeeRegistrationDto> iterator = responseEmployeeDetails.getListObject().listIterator();
				while(iterator.hasNext())
				{
					EmployeeRegistrationDto employeeRegistrationDtoList = iterator.next();
					
					
					DependentDetailsDto dependentDetailsDto = new DependentDetailsDto();
					dependentDetailsDto.setTypeId(employeeRegistrationDto.getTypeId());
					dependentDetailsDto.setEmpDocId(employeeRegistrationDtoList.getEmployeeId());
					
					Response dependentRes= iDependentDetailsService.getDependentDetails(dependentDetailsDto);
					employeeRegistrationDtoList.setDependentDetailsDtosList(dependentRes.getListObject());
					
					employeeRegistrationDtosList.add(employeeRegistrationDtoList);
					
				}
				
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, null, employeeRegistrationDtosList, null);
		}catch (Exception e) {
			logger.error("getEmployeeDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateEmployeeDetails(EmployeeRegistrationDto employeeRegistrationDto) throws ApplicationException {
		try {
				Response resEmpDependent=null;	
				Response res = iEmployeeRegistrationDao.checkUniqueEmployee(employeeRegistrationDto);
				if(res.getStatus().equals(SUCCESS))
				{
					//code added by Ganesh 15 APR 2019
					Calendar c = Calendar.getInstance();
					c.setTime(TimeZoneComponent.getDateByzone(employeeRegistrationDto.getUnitId()));
					c.add(Calendar.YEAR, 1);
					Date expDate = c.getTime();
					String passwordExpiry = GlobalCommonDateUtils.getStringDate(expDate, "dd-MM-yyyy HH:mm:ss");
					UserMasterDto userResponse = new UserMasterDto();
					if(employeeRegistrationDto.getIsUser()=='Y' && (employeeRegistrationDto.getUserId()==null || employeeRegistrationDto.getUserId()==0) )
					{
						UserMasterDto userMasterDto = new UserMasterDto();
						userMasterDto.setUserName(employeeRegistrationDto.getEmpCode());
						String password = GlobalCommonDateUtils.generatePassword(employeeRegistrationDto.getFirstName(), employeeRegistrationDto.getLastName(), GlobalCommonDateUtils.getDate(employeeRegistrationDto.getDob(),"dd-M-yyyy HH:mm:ss"));
						String encryptedPassword = GlobalCommonDateUtils.encrypt(password);
						userMasterDto.setPassword(encryptedPassword);
						userMasterDto.setOrganizationId(employeeRegistrationDto.getOrganizationId());
						userMasterDto.setUnitId(employeeRegistrationDto.getUnitId());
						userMasterDto.setCreatedBy(employeeRegistrationDto.getCreatedBy());
						userMasterDto.setCreatedDate(userMasterDto.getCreatedDate());
						userMasterDto.setUpdatedBy(employeeRegistrationDto.getCreatedBy());
						userMasterDto.setUpdatedDate(employeeRegistrationDto.getCreatedDate());
						userMasterDto.setEmailId(employeeRegistrationDto.getEmail());
						userMasterDto.setPwdExpiry(passwordExpiry);
						userMasterDto.setStatus("A");
						userMasterDto.setLabPin(employeeRegistrationDto.getLabPin());
						Response userRes =  iUserMasterDao.saveUserMaster(userMasterDto);
						if(userRes.getStatus().equals(SUCCESS))
						{
							//updatingUserDetailsForAuth
							UserComponent.updateUserDetailsForAuth(userMasterDto);
							
							userResponse.setEmployeeId(employeeRegistrationDto.getEmployeeId());
							userResponse.setPassword(GlobalCommonDateUtils.decrypt(userMasterDto.getPassword()));
							userResponse.setUserName(userMasterDto.getUserName());
						//String decryptedPwd = GlobalCommonDateUtils.decrypt(encryptedPassword);
						employeeRegistrationDto.setUserId((int)userRes.getObject());
						
					//	Response usernameRes = iMailingService.sendUserName(userMasterDto, RegistrationType.REG_EMPLOYEE);
						//Response userpassRes =	iMailingService.sendUserPassword(userMasterDto, RegistrationType.REG_EMPLOYEE);
						/*if(usernameRes.getStatus().equals(SUCCESS))
						 {
							
						 }*/
						
					    
						}
						else {
							return new Response(ERROR, COMMON_ERROR, "Failed To Save User", null, null);
						}
					}else if(employeeRegistrationDto.getLabPin() != null && !employeeRegistrationDto.getLabPin().equals("")) {
						UserMasterDto userMaDto = new UserMasterDto();
						userMaDto.setUserId(employeeRegistrationDto.getUserId());
						userMaDto.setLabPin(employeeRegistrationDto.getLabPin());
						Response userDetails = iUserMasterDao.updateLabPinUserMaster(userMaDto);
						if(userDetails.getStatus().equals(SUCCESS) && userDetails.getObject()!=null) {
							UserMaster usrMstr = (UserMaster) userDetails.getObject();
							userResponse.setEmployeeId(employeeRegistrationDto.getEmployeeId());
							userResponse.setPassword(GlobalCommonDateUtils.decrypt(usrMstr.getPassword()));
							userResponse.setUserName(usrMstr.getUserName());
						}
					}
					
					//Update for selected HIS role list
					if(employeeRegistrationDto.getSelectedHisRoleList().size()>0) {
						List<Integer> roles = new ArrayList<Integer>();
						for(UserRoleMapperDto userRoleMapperDto:employeeRegistrationDto.getSelectedHisRoleList()) {
							roles.add(userRoleMapperDto.getRoleId());
						}
						
						List<UserRoleMapperDto> savedRoles = new ArrayList<UserRoleMapperDto>();
						Response savedRolesRes =  iUserRoleMapperDao.inActiveRolesRecode(roles,employeeRegistrationDto.getUserId(),employeeRegistrationDto.getUnitId());
						
						if(savedRolesRes != null && savedRolesRes.getListObject().size()>0){
							
							savedRoles.addAll(savedRolesRes.getListObject());
							
							for(UserRoleMapperDto userRoleMapperDto:employeeRegistrationDto.getSelectedHisRoleList()) {
								userRoleMapperDto.setUserId(employeeRegistrationDto.getUserId());
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
							for(UserRoleMapperDto userRoleMapperDto:employeeRegistrationDto.getSelectedHisRoleList()) {
								userRoleMapperDto.setUserId(employeeRegistrationDto.getUserId());
								iUserRoleMapperDao.saveUserRoleMapper(userRoleMapperDto);
							}
						}
						
					}
					
					//Update for selected OTIMS Role List
					if(employeeRegistrationDto.getSelectedOTIMSRoleList().size()>0) {
						Boolean flag=false;
						for(OTIMSUserRoleMapperDto otimsUserRoleMapperDto:employeeRegistrationDto.getSelectedOTIMSRoleList()) {
							otimsUserRoleMapperDto.setUserId(employeeRegistrationDto.getUserId());
							if(flag==false) {
							iEmployeeRegistrationDao.inactiveRoleStatusOfOtimsUser(otimsUserRoleMapperDto);
							flag=true;
							}
							iEmployeeRegistrationDao.saveOTIMSUserRoleMapper(otimsUserRoleMapperDto);
						}
					}
					//update speciality and sub speciality
				/*	if(employeeRegistrationDto.getStaffSpecialityMapperDtosList().size() > 0)
				 	{*/
					  iEmployeeRegistrationDao.inactiveSpecialityStatusOfUser(employeeRegistrationDto);
					  
					  Response docSpe = iEmployeeRegistrationDao.saveStaffSpecialityMapper(employeeRegistrationDto);
					  
					  iEmployeeRegistrationDao.inactiveSubSpecialityStatusOfUser(employeeRegistrationDto);
					  
					  iEmployeeRegistrationDao.saveStaffSubSpecialityMapper(employeeRegistrationDto);
					  
//				 	}
					
					//Update for selected user unit List
					if(employeeRegistrationDto.getSelectedUserUnitList().size()>0) {
						
						List<Integer> unitIds = new ArrayList<Integer>();
						for(UserUnitMapperDto userUnitMapperDto:employeeRegistrationDto.getSelectedUserUnitList()) {
							unitIds.add(userUnitMapperDto.getUnitId());
						}
						
						List<UserUnitMapperDto> savedUnits = new ArrayList<UserUnitMapperDto>();
						Response savedUnitsRes =  iEmployeeRegistrationDao.inactiveUnitMapperByUserIdAndUnits(unitIds,employeeRegistrationDto.getUserId());
						if(savedUnitsRes != null && savedUnitsRes.getListObject() != null && savedUnitsRes.getListObject().size()>0){
							
							savedUnits.addAll(savedUnitsRes.getListObject());
							
							for(UserUnitMapperDto userUnitMapperDto:employeeRegistrationDto.getSelectedUserUnitList()) {
								
								boolean checkexits = false;
								
								for (UserUnitMapperDto savedUserUnitMapperDto : savedUnits) {
									if(savedUserUnitMapperDto.getUnitId().equals(userUnitMapperDto.getUnitId())){
										
										checkexits = true;
									}
								}
								
								if(checkexits == false){
									userUnitMapperDto.setUserId(employeeRegistrationDto.getUserId());
									userUnitMapperDto.setMisRights(employeeRegistrationDto.getDefaultMISRights());
									userUnitMapperDto.setOrganisationId(employeeRegistrationDto.getOrganizationId());
									Response response =iEmployeeRegistrationDao.checkTabAccessAvail(userUnitMapperDto);
									if(response.getObject()!=null){
										UserUnitMapperDto obj=(UserUnitMapperDto)response.getObject();
										userUnitMapperDto.setCoversheetRights(obj.getTabAccessIds());
										
									}else {
										userUnitMapperDto.setCoversheetRights(employeeRegistrationDto.getDefaultCoversheetRights());
									}
									
									 iUserRoleMapperDao.saveUserUnitMapper(userUnitMapperDto);
								}
								
							
							}
							
						}else{
							
							for(UserUnitMapperDto userUnitMapperDto:employeeRegistrationDto.getSelectedUserUnitList()) {
								userUnitMapperDto.setUserId(employeeRegistrationDto.getUserId());
								userUnitMapperDto.setMisRights(employeeRegistrationDto.getDefaultMISRights());
								userUnitMapperDto.setOrganisationId(employeeRegistrationDto.getOrganizationId());
								Response response =iEmployeeRegistrationDao.checkTabAccessAvail(userUnitMapperDto);
								if(response.getObject()!=null){
									UserUnitMapperDto obj=(UserUnitMapperDto)response.getObject();
									userUnitMapperDto.setCoversheetRights(obj.getTabAccessIds());
									
								}else {
									userUnitMapperDto.setCoversheetRights(employeeRegistrationDto.getDefaultCoversheetRights());
								}
								
								 iUserRoleMapperDao.saveUserUnitMapper(userUnitMapperDto);
							}
						}
						
					}
					
					Response resEmpDetails = iEmployeeRegistrationDao.updateEmployeeDetails(employeeRegistrationDto);
					if(resEmpDetails.getStatus().equals(SUCCESS)){
					
					DependentDetailsDto dependentDetailsDto=new DependentDetailsDto();
						dependentDetailsDto.setEmpDocId(employeeRegistrationDto.getEmployeeId());
						dependentDetailsDto.setTypeId(employeeRegistrationDto.getTypeId());
						dependentDetailsDto.setUpdatedBy(employeeRegistrationDto.getUpdatedBy());
						dependentDetailsDto.setUpdatedDate(employeeRegistrationDto.getUpdatedDate());
						dependentDetailsDto.setDependentDetailsDtoList(employeeRegistrationDto.getDependentDetailsDtosList());
					 resEmpDependent = iDependentDetailsService.updateDependentDetails(dependentDetailsDto);
					
					 if(resEmpDependent.getStatus().equals(SUCCESS))
					 		return new Response(SUCCESS, null, COMMON_UPDATE, null, null,userResponse);
					 else
					 		return new Response(ERROR, null, "Problem in updating Dependent Details !!", null, null);
					}
				}
				else
					return new Response(ERROR, null, "Employee does not exist !!", null, null);

		} catch (Exception e) {
		logger.error("updateEmployeeDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateEmployeeStatus(EmployeeRegistrationDto employeeRegistrationDto) throws ApplicationException {
		try{
			
			return iEmployeeRegistrationDao.updateEmployeeStatus(employeeRegistrationDto);
			
		}catch(Exception e){
			logger.error("updateEmployeeStatus failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response searchEmployeeByCriteria(EmployeeRegistrationDto employeeRegistrationDto)
			throws ApplicationException {
		try {
			List<EmployeeRegistrationDto> employeeRegistrationDtosList = new LinkedList<EmployeeRegistrationDto>();
			employeeRegistrationDto.setIsPatientSearchStaff('N');
			Response responseEmployeeDetails = iEmployeeRegistrationDao.searchEmployeeByCriteria(employeeRegistrationDto);
			if(responseEmployeeDetails.getListObject().size()>0)
			{
				ListIterator<EmployeeRegistrationDto> iterator = responseEmployeeDetails.getListObject().listIterator();
				while(iterator.hasNext())
				{
					EmployeeRegistrationDto employeeRegistrationDtoList = iterator.next();
					
					
					DependentDetailsDto dependentDetailsDto = new DependentDetailsDto();
					dependentDetailsDto.setTypeId(employeeRegistrationDto.getTypeId());
					dependentDetailsDto.setEmpDocId(employeeRegistrationDtoList.getEmployeeId());
					
					Response dependentRes= iDependentDetailsService.getDependentDetails(dependentDetailsDto);
					employeeRegistrationDtoList.setDependentDetailsDtosList(dependentRes.getListObject());
					
					employeeRegistrationDtosList.add(employeeRegistrationDtoList);
					
				}
				
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, null, employeeRegistrationDtosList, null);
		} catch (Exception e) {
			logger.error("searchEmployeeByCriteria failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveEmployeeImage(int orgId, int unitId, int employeeId, MultipartFile inputFile)
			throws ApplicationException {
		 try{
		      if(orgId>0&&unitId>0&&employeeId>0&&!inputFile.isEmpty())
		      {
		        Response unitRes =  iGlobalUnitMasterDao.getUnitFolderPath(unitId);
		        UnitMasterDto unitMasterDto = (UnitMasterDto) unitRes.getObject();
		        String employeeImagePath =  docUploadUtility.uploadEmployeeImage(inputFile, unitMasterDto, unitId, employeeId);
		        EmployeeRegistrationDto employeeRegistrationDto = new EmployeeRegistrationDto();
		        employeeRegistrationDto.setEmployeeId(employeeId);
		        employeeRegistrationDto.setOrganizationId(orgId);
		        employeeRegistrationDto.setUnitId(unitId);
		        employeeRegistrationDto.setEmployeeImage(employeeImagePath);
		        return iEmployeeRegistrationDao.updateEmployeeImage(employeeRegistrationDto);
		      }
		    return null;
		  }catch(Exception e){
		  logger.error("saveEmployeeImage failed", e);
		  }
		  return new Response<>(ERROR, null, null, null, null);
		  }

	@Override
	public Response getPlainEmployeeDetails(EmployeeRegistrationDto employeeRegistrationDto) {
		try {
			employeeRegistrationDto.setIsPatientSearchStaff('Y');
			return iEmployeeRegistrationDao.searchEmployeeByCriteria(employeeRegistrationDto);

		} catch (Exception e) {
			logger.error("getPlainEmployeeDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response searchEmployeeByName(EmployeeRegistrationDto employeeRegistrationDto) throws ApplicationException {
		try {

			return iEmployeeRegistrationDao.searchEmployeeByName(employeeRegistrationDto);

		} catch (Exception e) {
			logger.error("searchEmployeeByName failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getEmployeeRegistrationCount(EmployeeRegistrationDto employeeRegistrationDto)
			throws ApplicationException {
		try {

			return iEmployeeRegistrationDao.getEmployeeRegistrationCount(employeeRegistrationDto);

		} catch (Exception e) {
			logger.error("getEmployeeRegistrationCount failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Transactional
	@Override
	public Response getEmployeeListBySpecialityAndSubSpeciality(EmployeeRegistrationDto employeeRegistrationDto)
			throws ApplicationException {
		try {

			return iEmployeeRegistrationDao.getEmployeeListBySpecialityAndSubSpeciality(employeeRegistrationDto);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response validateEmpCode(String empCode,Integer orgId ,Integer doctorId ,Integer employeeId) {
		try {
			return iEmployeeRegistrationDao.validateEmpCode(empCode,orgId,doctorId,employeeId);
		} catch (Exception e) {
			logger.error("validateEmpCode failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response universalSearchForEmployee(EmployeeRegistrationDto employeeRegistrationDto)
			throws ApplicationException {
		try {
			List<EmployeeRegistrationDto> employeeRegistrationDtosList = new LinkedList<EmployeeRegistrationDto>();
			employeeRegistrationDto.setIsPatientSearchStaff('N');
			Response responseEmployeeDetails = iEmployeeRegistrationDao.universalSearchForEmployee(employeeRegistrationDto);
			if(responseEmployeeDetails.getListObject().size()>0)
			{
				ListIterator<EmployeeRegistrationDto> iterator = responseEmployeeDetails.getListObject().listIterator();
				while(iterator.hasNext())
				{
					EmployeeRegistrationDto employeeRegistrationDtoList = iterator.next();
					
					
					DependentDetailsDto dependentDetailsDto = new DependentDetailsDto();
					dependentDetailsDto.setTypeId(employeeRegistrationDto.getTypeId());
					dependentDetailsDto.setEmpDocId(employeeRegistrationDtoList.getEmployeeId());
					
					Response dependentRes= iDependentDetailsService.getDependentDetails(dependentDetailsDto);
					employeeRegistrationDtoList.setDependentDetailsDtosList(dependentRes.getListObject());
					
					employeeRegistrationDtosList.add(employeeRegistrationDtoList);
					
				}
				
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, null, employeeRegistrationDtosList, null);
		} catch (Exception e) {
			logger.error("universalSearchForEmployee failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response searchEmployeeByNameAndRole(EmployeeRegistrationDto employeeRegistrationDto) throws ApplicationException {
		try {

			return iEmployeeRegistrationDao.searchEmployeeByNameAndRole(employeeRegistrationDto);

		} catch (Exception e) {
			logger.error("searchEmployeeByNameAndRole failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

}
