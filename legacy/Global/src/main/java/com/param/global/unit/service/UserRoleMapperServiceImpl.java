package com.param.global.unit.service;


import java.util.Iterator;
import java.util.List;

import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.param.global.common.ICommonConstants;
import com.param.global.common.RegistrationType;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.common.UserOtTabsMapperDto;
import com.param.global.dao.IUserOtTabsMapperDao;
import com.param.global.dto.UserMasterDto;
import com.param.global.dto.UserUnitMapperDto;
import com.param.global.mail.IMailingService;
import com.param.global.unit.dao.IUserRoleMapperDao;
import com.param.global.unit.dto.UserRoleMapperDto;
import com.param.rabbitmq.service.EmrService.IEmrService;
import org.apache.log4j.Logger;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class UserRoleMapperServiceImpl implements IUserRoleMapperService , ICommonConstants{

	private static final Logger logger = Logger.getLogger(UserRoleMapperServiceImpl.class);

	@Autowired
	IUserRoleMapperDao iUserRoleMapperDao;
	
	 @Autowired
	 IMailingService iMailingService;
	 
	 @Autowired
	 IUserOtTabsMapperDao iUserOtTabsMapperDao;
	 
	 @Autowired
	 IEmrService EmrService;
	 
	 @Autowired
		Gson gson;

	 @Autowired
	 private com.param.global.service.VerifiedIdentityService verifiedIdentityService;
	 
	@Override
	public Response saveUserRoleMapper(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		
		try {
					
				Response res1=iUserRoleMapperDao.updateUserUnitMapper(userRoleMapperDto);
						
				for(Iterator it=userRoleMapperDto.getUserRoleMapperList().iterator();it.hasNext();){
					UserRoleMapperDto userRoleMapperDtoList=(UserRoleMapperDto)it.next();
					userRoleMapperDtoList.setRoleId(userRoleMapperDtoList.getId());
					userRoleMapperDtoList.setUserId(userRoleMapperDto.getUserId());
					userRoleMapperDtoList.setOrganisationId(userRoleMapperDto.getOrganisationId());
					userRoleMapperDtoList.setUnitId(userRoleMapperDto.getUnitId());
					Response response=iUserRoleMapperDao.getUserRoleMapperByName(userRoleMapperDtoList);
					if(response.getListObject().size()>0 && response.getListObject()!=null){
						
						return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
					}else{
							UserRoleMapperDto userRoleMapperDto1=new UserRoleMapperDto();
							userRoleMapperDto1.setUserId(userRoleMapperDto.getUserId());
							userRoleMapperDto1.setRoleId(userRoleMapperDtoList.getId());
							userRoleMapperDto1.setDashboardId(userRoleMapperDto.getDashboardId());
							userRoleMapperDto1.setUnitId(userRoleMapperDto.getUnitId());
							userRoleMapperDto1.setOrganisationId(userRoleMapperDto.getOrganisationId());
							userRoleMapperDto1.setCreatedBy(userRoleMapperDto.getCreatedBy());
							userRoleMapperDto1.setUpdatedBy(userRoleMapperDto.getUpdatedBy());
							userRoleMapperDto1.setCreatedDate(userRoleMapperDto.getCreatedDate());
							userRoleMapperDto1.setUpdatedDate(userRoleMapperDto.getUpdatedDate());
							userRoleMapperDto1.setStatus(userRoleMapperDto.getStatus());
							userRoleMapperDto1.setIsRecoll(userRoleMapperDto.getIsRecoll());
							userRoleMapperDto1.setIsRisRecall(userRoleMapperDto.getIsRisRecall()!=null ? userRoleMapperDto.getIsRisRecall() : false);
							userRoleMapperDto1.setManualMpesa(userRoleMapperDto.getManualMpesa() != null ? userRoleMapperDto.getManualMpesa() : false);
							userRoleMapperDto1.setEnableBlockPrint(userRoleMapperDto.getEnableBlockPrint()!= null ? userRoleMapperDto.getEnableBlockPrint() : false);
//							userRoleMapperDto1.setIsRenderedServiceCancelled(userRoleMapperDto.getIsRenderedServiceCancelled());
//							userRoleMapperDto1.setIsUpdatePatientReg(userRoleMapperDto.getIsUpdatePatientReg());
							iUserRoleMapperDao.saveUserRoleMapper(userRoleMapperDto1);
							
					}			
				
			}
			
		/*StaffMapperVO staffMapperVODto = new StaffMapperVO();
				staffMapperVODto.setUserId(userRoleMapperDto.getUserId());
				staffMapperVODto.setUnitId(userRoleMapperDto.getUnitId());
				staffMapperVODto.setOrganizationId(userRoleMapperDto.getOrganisationId());
				
				Response res=iUserRoleMapperDao.getUserRoleMapperDetailsByid(staffMapperVODto);
				Response resrole=iUserRoleMapperDao.getUserRoleMapperDetailsByrole(staffMapperVODto);*/
				
			/*	if(res.getObject() != null && resrole.getListObject().size()>0 && resrole.getListObject() !=null) {
					staffMapperVODto=(StaffMapperVO) res.getObject();
					String password= staffMapperVODto.getPassword();
					staffMapperVODto.setUserId(userRoleMapperDto.getUserId());
					staffMapperVODto.setStatus("A");
					staffMapperVODto.setPassword(GlobalCommonDateUtils.decrypt(password));
					staffMapperVODto.setRole(resrole.getListObject());
					
					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(staffMapperVODto);
					request.setKey(MasterKeyConstants.Otims_User.toString());
					request.setPayload(payload);
					//iOtimsService.publish(request);
					
					
				}*/
			
				
					return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
			logger.error("saveUserRoleMapper failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateUserRoleMapper(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			userRoleMapperDto.setOrganisationId(verifiedIdentityService.getVerifiedOrganizationId());
			userRoleMapperDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());

			Response res1=iUserRoleMapperDao.updateUserUnitMapper(userRoleMapperDto);
				
			Response response=iUserRoleMapperDao.truncteRecode(userRoleMapperDto);
			if(response.getStatus().equals("success")){
				for(Iterator it=userRoleMapperDto.getUserRoleMapperList().iterator();it.hasNext();){
					UserRoleMapperDto userRoleMapperDtoList=(UserRoleMapperDto)it.next();
						UserRoleMapperDto userRoleMapperDto1=new UserRoleMapperDto();
						userRoleMapperDto1.setUserId(userRoleMapperDto.getUserId());
						userRoleMapperDto1.setRoleId(userRoleMapperDtoList.getId());
						userRoleMapperDto1.setUnitId(userRoleMapperDto.getUnitId());
						userRoleMapperDto1.setDashboardId(userRoleMapperDto.getDashboardId());
						userRoleMapperDto1.setOrganisationId(userRoleMapperDto.getOrganisationId());
	//					userRoleMapperDto1.setCreatedBy(userRoleMapperDto.getCreatedBy());
						userRoleMapperDto1.setUpdatedBy(userRoleMapperDto.getUpdatedBy());
	//					userRoleMapperDto1.setCreatedDate(userRoleMapperDto.getCreatedDate());
						userRoleMapperDto1.setUpdatedDate(userRoleMapperDto.getUpdatedDate());
						userRoleMapperDto1.setStatus(userRoleMapperDto.getStatus());
						userRoleMapperDto1.setUpdateStatus('Y');
						userRoleMapperDto1.setIsRecoll(userRoleMapperDto.getIsRecoll());
						userRoleMapperDto1.setIsRisRecall(userRoleMapperDto.getIsRisRecall()!=null ? userRoleMapperDto.getIsRisRecall() : false);
						userRoleMapperDto1.setManualMpesa(userRoleMapperDto.getManualMpesa() != null ? userRoleMapperDto.getManualMpesa() : false);
						userRoleMapperDto1.setEnableBlockPrint(userRoleMapperDto.getEnableBlockPrint()!= null ? userRoleMapperDto.getEnableBlockPrint() : false);

//						userRoleMapperDto1.setIsRenderedServiceCancelled(userRoleMapperDto.getIsRenderedServiceCancelled());
//						userRoleMapperDto1.setIsUpdatePatientReg(userRoleMapperDto.getIsUpdatePatientReg());

						iUserRoleMapperDao.saveUserRoleMapper(userRoleMapperDto1);
				}
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}else{
				return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
			/*Response response=iUserRoleMapperDao.getUserRoleMapperByName(userRoleMapperDto);
			
			if(response.getListObject().size()>0 && response.getListObject()!=null){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iUserRoleMapperDao.updateUserRoleMapper(userRoleMapperDto);
			}*/
		} catch (Exception e) {
			logger.error("updateUserRoleMapper failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateUserRoleMapperStatus(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			return iUserRoleMapperDao.updateUserRoleMapperStatus(userRoleMapperDto);
		} catch (Exception e) {
			logger.error("updateUserRoleMapperStatus failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUserRoleMapperList(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			Response res =  iUserRoleMapperDao.getUserRoleMapperList(userRoleMapperDto);
			
			if(res.getListObject()!=null && res.getListObject().size()>0) {
				List<UserRoleMapperDto> list = res.getListObject();
				
	/*			for (UserRoleMapperDto dto  : list) {
					
					List<ReportDetailsDto> listMISReports=listMISReports=new ArrayList<>();
					ReportDetailsDto[] fooFromJson = gson.fromJson(dto.getMisReportIds(), ReportDetailsDto[].class);
					if(fooFromJson!=null) {
						for (ReportDetailsDto extractedData : fooFromJson) {
//							if(dto.getReportGroupHeaderId()==extractedData.getReportGroupHeaderId()) {
										
										ReportDetailsDto newDto=new ReportDetailsDto();
										newDto.setReportGroupHeaderId(extractedData.getReportGroupHeaderId());
//										newDto.setReportGroupHeaderName(dto.getReportGroupHeaderName());
//										newDto.setOrganizationId(userRoleMapperDto.getOrganisationId());
										newDto.setSelectedReprtId(extractedData.getSelectedReprtId());
										listMISReports.add(newDto);
										
//							}
						}
						dto.setListMISReports(listMISReports);
					}
					
				}*/
			
				return new Response(SUCCESS, SUCCESS_CODE, "Get Data user role", list, null);

			}
		} catch (Exception e) {
			logger.error("getUserRoleMapperList failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUserRoleMapperById(Integer userRoleMpprId, Integer organisationId, Integer unitId) throws ApplicationException {
		try {
			return iUserRoleMapperDao.getUserRoleMapperById(userRoleMpprId, organisationId, unitId);
		} catch (Exception e) {
			logger.error("getUserRoleMapperById failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveUserRoleMapperList(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			return iUserRoleMapperDao.getActiveUserRoleMapperList(userRoleMapperDto);
		} catch (Exception e) {
			logger.error("getActiveUserRoleMapperList failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUserRoleMapperCount(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			return iUserRoleMapperDao.getUserRoleMapperCount(userRoleMapperDto);
		} catch (Exception e) {
			logger.error("getUserRoleMapperCount failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUserRoleMapperByName(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			return iUserRoleMapperDao.getUserRoleMapperByName(userRoleMapperDto);
		} catch (Exception e) {
			logger.error("getUserRoleMapperByName failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUserIdById(Integer userId, Integer organisationId, Integer unitId) throws ApplicationException {
		try {
			return iUserRoleMapperDao.getUserIdById(userId, organisationId, unitId);
		} catch (Exception e) {
			logger.error("getUserIdById failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response truncteRecode(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			return iUserRoleMapperDao.truncteRecode(userRoleMapperDto);
		} catch (Exception e) {
			logger.error("truncteRecode failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUserAuthenticationList(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			return iUserRoleMapperDao.getUserAuthenticationList(userRoleMapperDto);
		} catch (Exception e) {
			logger.error("getUserAuthenticationList failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
	@Override
	public Response sendPasswordToUser(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			UserMasterDto userMasterDto=new UserMasterDto();
			userMasterDto.setFirstName(userRoleMapperDto.getFirstName());
			userMasterDto.setPassword(userRoleMapperDto.getPassword());
			userMasterDto.setEmailId(userRoleMapperDto.getEmailId());
			Response userpassRes =null;
			if(userRoleMapperDto.getEmpCode().contains("EMP")){
				 userpassRes =	iMailingService.sendUserPassword(userMasterDto, RegistrationType.REG_EMPLOYEE);
			}else{
				 userpassRes =	iMailingService.sendUserPassword(userMasterDto, RegistrationType.REG_DOCTOR);
			}
			if(!userpassRes.getStatus().equals(SUCCESS))
			 {
				return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
			 }
		} catch (Exception e) {
			logger.error("sendPasswordToUser failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUserListByRoleId(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			return iUserRoleMapperDao.getUserListByRoleId(userRoleMapperDto);
		} catch (Exception e) {
			logger.error("getUserListByRoleId failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUserRoleNameByUserId(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			return iUserRoleMapperDao.getUserRoleNameByUserId(userRoleMapperDto);
		} catch (Exception e) {
			logger.error("getUserRoleNameByUserId failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveOTIMSRoleMasterList(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			return iUserRoleMapperDao.getActiveOTIMSRoleMasterList(userRoleMapperDto);
		} catch (Exception e) {
			logger.error("getActiveOTIMSRoleMasterList failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getHisRoleMapperList(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			return iUserRoleMapperDao.getHisRoleMapperListByUserId(userRoleMapperDto);
		} catch (Exception e) {
			logger.error("getHisRoleMapperList failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOTIMSRoleMapperListByUserId(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			return iUserRoleMapperDao.getOTIMSRoleMapperListByUserId(userRoleMapperDto);
		} catch (Exception e) {
			logger.error("getOTIMSRoleMapperListByUserId failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUserUnitListByUserId(UserUnitMapperDto userUnitMapperDto) throws ApplicationException {
		try {
			return iUserRoleMapperDao.getUserUnitListByUserId(userUnitMapperDto);
		} catch (Exception e) {
			logger.error("getUserUnitListByUserId failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getRoleNameByRoleTypeId(Integer roleTypeId, Integer organisationId, Integer unitId)
			throws ApplicationException {
		try {
			return iUserRoleMapperDao.getRoleNameByRoleTypeId(roleTypeId, organisationId, unitId);
		} catch (Exception e) {
			logger.error("getRoleNameByRoleTypeId failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveOTRoleList(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			return iUserRoleMapperDao.getActiveOTRoleList(userRoleMapperDto);
		} catch (Exception e) {
			logger.error("getActiveOTRoleList failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDialysisTechnicianList(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			return iUserRoleMapperDao.getDialysisTechnicianList(userRoleMapperDto);
		} catch (Exception e) {
			logger.error("getDialysisTechnicianList failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUserByRoleType(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			return iUserRoleMapperDao.getUserByRoleType(userRoleMapperDto);
		} catch (Exception e) {
			logger.error("getUserByRoleType failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveUserOtabsMapper(UserOtTabsMapperDto userOtTabsMapperDto) throws ApplicationException {
		try {
			return iUserOtTabsMapperDao.getUserByRoleType(userOtTabsMapperDto);
		} catch (Exception e) {
			logger.error("saveUserOtabsMapper failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUserOtTabsByUserId(Integer userId) throws ApplicationException {
		try {
			return iUserOtTabsMapperDao.getUserOtTabsByUserId(userId);
		} catch (Exception e) {
			logger.error("getUserOtTabsByUserId failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveUserWiseMISReportsList(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
				return iUserRoleMapperDao.saveUserWiseMISReportsList(userRoleMapperDto);
		} catch (Exception e) {
			logger.error("saveUserWiseMISReportsList failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response saveUserWiseCovrsheetTabList(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			return iUserRoleMapperDao.saveUserWiseCovrsheetTabList(userRoleMapperDto);
	} catch (Exception e) {
		logger.error("saveUserWiseCovrsheetTabList failed", e);
	}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveUserWiseCovrsheetTabListIPD(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			return iUserRoleMapperDao.saveUserWiseCovrsheetTabListIPD(userRoleMapperDto);
	} catch (Exception e) {
		logger.error("saveUserWiseCovrsheetTabListIPD failed", e);
	}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getMpesaRigthById(Integer userId, Integer unitId) throws ApplicationException {
		try {
			return iUserRoleMapperDao.getMpesaRigthById(userId, unitId);
		} catch (Exception e) {
			logger.error("getMpesaRigthById failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getEnableBlockReport(Integer userId, Integer unitId) throws ApplicationException {
		try {
			return iUserRoleMapperDao.getEnableBlockReport(userId, unitId);
		} catch (Exception e) {
			logger.error("getEnableBlockReport failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUserRoleAccess(Integer userId, Integer unitId) throws ApplicationException {
		try {
			return iUserRoleMapperDao.getUserRoleAccess(userId, unitId);
		} catch (Exception e) {
			
			throw e;
		}
	}
	
}
