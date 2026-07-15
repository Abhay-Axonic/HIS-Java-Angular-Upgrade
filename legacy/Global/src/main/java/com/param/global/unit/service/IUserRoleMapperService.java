package com.param.global.unit.service;
import javax.transaction.Transactional;

import in.param.exception.ApplicationException;

import com.param.global.common.Response;
import com.param.global.common.UserOtTabsMapperDto;
import com.param.global.dto.UserUnitMapperDto;
import com.param.global.unit.dto.UserRoleMapperDto;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IUserRoleMapperService {

	public Response saveUserRoleMapper(UserRoleMapperDto userRoleMapperDto) throws ApplicationException;
	
	public Response updateUserRoleMapper(UserRoleMapperDto userRoleMapperDto) throws ApplicationException;
	
	public Response updateUserRoleMapperStatus(UserRoleMapperDto userRoleMapperDto) throws ApplicationException;
	
	public Response getUserRoleMapperList(UserRoleMapperDto userRoleMapperDto) throws ApplicationException;
	
	public Response getUserRoleMapperById(Integer userRoleMpprId, Integer organisationId, Integer unitId) throws ApplicationException;
	
	public Response getActiveUserRoleMapperList(UserRoleMapperDto userRoleMapperDto) throws ApplicationException;
	
	public Response getUserRoleMapperCount(UserRoleMapperDto userRoleMapperDto) throws ApplicationException;
	
	public Response getUserRoleMapperByName(UserRoleMapperDto userRoleMapperDto) throws ApplicationException;

	public Response getUserIdById(Integer userId, Integer organisationId, Integer unitId)throws ApplicationException;
	
	public Response truncteRecode(UserRoleMapperDto userRoleMapperDto)throws ApplicationException;

	public Response getUserAuthenticationList(UserRoleMapperDto userRoleMapperDto)throws ApplicationException;
	
	public Response sendPasswordToUser(UserRoleMapperDto userRoleMapperDto)throws ApplicationException;

	public Response getUserListByRoleId(UserRoleMapperDto userRoleMapperDto)throws ApplicationException;

	public Response getUserRoleNameByUserId(UserRoleMapperDto userRoleMapperDto)throws ApplicationException;

	public Response getActiveOTIMSRoleMasterList(UserRoleMapperDto userRoleMapperDto)throws ApplicationException;

	public Response getHisRoleMapperList(UserRoleMapperDto userRoleMapperDto)throws ApplicationException;

	public Response getOTIMSRoleMapperListByUserId(UserRoleMapperDto userRoleMapperDto)throws ApplicationException;

	public Response getUserUnitListByUserId(UserUnitMapperDto userUnitMapperDto)throws ApplicationException;

	public Response getRoleNameByRoleTypeId(Integer roleTypeId, Integer organisationId, Integer unitId)throws ApplicationException;

	public Response getActiveOTRoleList(UserRoleMapperDto userRoleMapperDto)throws ApplicationException;

	public Response getDialysisTechnicianList(UserRoleMapperDto userRoleMapperDto)throws ApplicationException;
	
	public Response getUserByRoleType(UserRoleMapperDto userRoleMapperDto)throws ApplicationException;

	public Response saveUserOtabsMapper(UserOtTabsMapperDto userOtTabsMapperDto)throws ApplicationException;

	public Response getUserOtTabsByUserId(Integer userId)throws ApplicationException;

	public Response saveUserWiseMISReportsList(UserRoleMapperDto userRoleMapperDto)throws ApplicationException;

	public Response saveUserWiseCovrsheetTabList(UserRoleMapperDto userRoleMapperDto)throws ApplicationException;

	public Response saveUserWiseCovrsheetTabListIPD(UserRoleMapperDto userRoleMapperDto)throws ApplicationException;

	public Response getMpesaRigthById(Integer userId, Integer unitId)throws ApplicationException;

	public Response getEnableBlockReport(Integer userId, Integer unitId)throws ApplicationException;

	public Response getUserRoleAccess(Integer userId, Integer unitId)throws ApplicationException;

}