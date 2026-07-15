package com.param.global.unit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.UserOtTabsMapperDto;
import com.param.global.dto.UserUnitMapperDto;
import com.param.global.unit.dto.UserRoleMapperDto;
import com.param.global.unit.service.IUserRoleMapperService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("api/global/")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UserRoleMapperContrtoller implements ICommonConstants{
	final static Logger logger = Logger.getLogger(UserRoleMapperContrtoller.class);

	@Autowired
	private IUserRoleMapperService iUserRoleMapperService;

	@RequestMapping(value = "/saveUserRoleMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveBankBranchMaster(@RequestBody UserRoleMapperDto userRoleMapperDto) {
		try {
			return iUserRoleMapperService.saveUserRoleMapper(userRoleMapperDto);
		} catch (Exception e) {
		logger.error("saveBankBranchMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateUserRoleMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateUserRoleMapper(@RequestBody UserRoleMapperDto userRoleMapperDto) {
		try {
			return iUserRoleMapperService.updateUserRoleMapper(userRoleMapperDto);
		} catch (Exception e) {
		logger.error("updateUserRoleMapper failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateUserRoleMapperStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateUserRoleMapperStatus(@RequestBody UserRoleMapperDto userRoleMapperDto) {
		try {
			return iUserRoleMapperService.updateUserRoleMapperStatus(userRoleMapperDto);
		} catch (Exception e) {
		logger.error("updateUserRoleMapperStatus failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getUserRoleMapperList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getUserRoleMapperList(@RequestBody UserRoleMapperDto userRoleMapperDto) {
		try {
			return iUserRoleMapperService.getUserRoleMapperList(userRoleMapperDto);
		} catch (Exception e) {
		logger.error("getUserRoleMapperList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	
	@RequestMapping(value = "/getUserRoleMapperById/{userRoleMpprId}/{organisationId}/{unitId}", method = RequestMethod.GET)
	public Response getUserRoleMapperById(@PathVariable ("userRoleMpprId") Integer userRoleMpprId,
										  @PathVariable ("organisationId") Integer organisationId,
										  @PathVariable ("unitId") Integer unitId) {
		try {
			return iUserRoleMapperService.getUserRoleMapperById(userRoleMpprId, organisationId, unitId);
		} catch (Exception e) {
		logger.error("getUserRoleMapperById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	
	@RequestMapping(value = "/getUserRoleMapperCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getUserRoleMapperCount(@RequestBody UserRoleMapperDto userRoleMapperDto) {
		try {
			return iUserRoleMapperService.getUserRoleMapperCount(userRoleMapperDto);
		} catch (Exception e) {
		logger.error("getUserRoleMapperCount failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	
	@RequestMapping(value = "/getActiveUserRoleMapperList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getActiveUserRoleMapperList(@RequestBody UserRoleMapperDto userRoleMapperDto) {
		try {
			return iUserRoleMapperService.getActiveUserRoleMapperList(userRoleMapperDto);
		} catch (Exception e) {
		logger.error("getActiveUserRoleMapperList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getUserById/{userId}/{organisationId}/{unitId}", method = RequestMethod.GET)
	public Response getUserIdById(@PathVariable ("userId") Integer userId,
										  @PathVariable ("organisationId") Integer organisationId,
										  @PathVariable ("unitId") Integer unitId) {
		try {
			return iUserRoleMapperService.getUserIdById(userId, organisationId, unitId);
		} catch (Exception e) {
		logger.error("getUserIdById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	@RequestMapping(value = "/getUserAuthenticationList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getUserAuthenticationList(@RequestBody UserRoleMapperDto userRoleMapperDto) {
		try {
			return iUserRoleMapperService.getUserAuthenticationList(userRoleMapperDto);
		} catch (Exception e) {
		logger.error("getUserAuthenticationList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	@RequestMapping(value = "/sendPasswordToUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response sendPassword(@RequestBody UserRoleMapperDto userRoleMapperDto) {
		try {
			return iUserRoleMapperService.sendPasswordToUser(userRoleMapperDto);
		} catch (Exception e) {
		logger.error("sendPassword failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	@RequestMapping(value = "/getUserListByRoleId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getUserListByRoleId(@RequestBody UserRoleMapperDto userRoleMapperDto) {
		try {
			return iUserRoleMapperService.getUserListByRoleId(userRoleMapperDto);
		} catch (Exception e) {
		logger.error("getUserListByRoleId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	@RequestMapping(value = "/getUserRoleNameByUserId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getUserRoleNameByUserId(@RequestBody UserRoleMapperDto userRoleMapperDto) {
		try {
			return iUserRoleMapperService.getUserRoleNameByUserId(userRoleMapperDto);
		} catch (Exception e) {
		logger.error("getUserRoleNameByUserId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getActiveOTIMSRoleMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getActiveOTIMSRoleMasterList(@RequestBody UserRoleMapperDto userRoleMapperDto) {
		try {
			return iUserRoleMapperService.getActiveOTIMSRoleMasterList(userRoleMapperDto);
		} catch (Exception e) {
		logger.error("getActiveOTIMSRoleMasterList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getHisRoleMapperListByUserId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getHisRoleMapperList(
			@RequestBody UserRoleMapperDto UserRoleMapperDto) {
		try {
			return iUserRoleMapperService.getHisRoleMapperList(UserRoleMapperDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getOTIMSRoleMapperListByUserId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getOTIMSRoleMapperListByUserId(
			@RequestBody UserRoleMapperDto UserRoleMapperDto) {
		try {
			return iUserRoleMapperService.getOTIMSRoleMapperListByUserId(UserRoleMapperDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getUserUnitListByUserId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getUserUnitListByUserId(
			@RequestBody UserUnitMapperDto userUnitMapperDto) {
		try {
			return iUserRoleMapperService.getUserUnitListByUserId(userUnitMapperDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	
	@RequestMapping(value = "/getRoleNameByRoleTypeId/{roleTypeId}/{organisationId}/{unitId}", method = RequestMethod.GET)
	public Response getRoleNameByRoleTypeId(@PathVariable ("roleTypeId") Integer roleTypeId,
										   @PathVariable ("organisationId") Integer organisationId,
										   @PathVariable ("unitId") Integer unitId) {
		try {
			return iUserRoleMapperService.getRoleNameByRoleTypeId(roleTypeId, organisationId, unitId);
		} catch (Exception e) {
		logger.error("getRoleNameByRoleTypeId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getActiveOTRoleList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getActiveOTRoleList(@RequestBody UserRoleMapperDto userRoleMapperDto) {
		try {
			return iUserRoleMapperService.getActiveOTRoleList(userRoleMapperDto);
		} catch (Exception e) {
		logger.error("getActiveOTRoleList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getDialysisTechnicianList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDialysisTechnicianList(@RequestBody UserRoleMapperDto userRoleMapperDto) {
		try {
			return iUserRoleMapperService.getDialysisTechnicianList(userRoleMapperDto);
		} catch (Exception e) {
		logger.error("getDialysisTechnicianList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getUserByRoleType", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getUserByRoleType(@RequestBody UserRoleMapperDto userRoleMapperDto) {
		try {
			return iUserRoleMapperService.getUserByRoleType(userRoleMapperDto);
		} catch (Exception e) {
		logger.error("getUserByRoleType failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/saveUserOtabsMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveUserOtabsMapper(@RequestBody UserOtTabsMapperDto userOtTabsMapperDto) {
		try {
			return iUserRoleMapperService.saveUserOtabsMapper(userOtTabsMapperDto);
		} catch (Exception e) {
		logger.error("saveUserOtabsMapper failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	
	
	@RequestMapping(value = "getUserOtTabsByUserId/{userId}", method = RequestMethod.GET)
	public Response getUserOtTabsByUserId(@PathVariable ("userId") Integer userId) {
		try {
			return iUserRoleMapperService.getUserOtTabsByUserId(userId);
		} catch (Exception e) {
		logger.error("getUserOtTabsByUserId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	
	@RequestMapping(value="/saveUserWiseMISReportsList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveUserWiseMISReportsList(@RequestBody UserRoleMapperDto userRoleMapperDto) {
		try
		{
			return iUserRoleMapperService.saveUserWiseMISReportsList(userRoleMapperDto);

		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/saveUserWiseCovrsheetTabList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveUserWiseCovrsheetTabList(@RequestBody UserRoleMapperDto userRoleMapperDto) {
		try
		{
			return iUserRoleMapperService.saveUserWiseCovrsheetTabList(userRoleMapperDto);

		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/saveUserWiseCovrsheetTabListIPD", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveUserWiseCovrsheetTabListIPD(@RequestBody UserRoleMapperDto userRoleMapperDto) {
		try
		{
			return iUserRoleMapperService.saveUserWiseCovrsheetTabListIPD(userRoleMapperDto);

		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "getUserById/{userId}/{unitId}", method = RequestMethod.GET)
	public Response getMpesaRigthById(@PathVariable ("userId") Integer userId,
										  @PathVariable ("unitId") Integer unitId) {
		try {
			return iUserRoleMapperService.getMpesaRigthById(userId, unitId);
		} catch (Exception e) {
		logger.error("getMpesaRigthById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "getEnableBlockReport/{userId}/{unitId}", method = RequestMethod.GET)
	public Response getEnableBlockReport(@PathVariable ("userId") Integer userId,
										  @PathVariable ("unitId") Integer unitId) {
		try {
			return iUserRoleMapperService.getEnableBlockReport(userId, unitId);
		} catch (Exception e) {
		logger.error("getEnableBlockReport failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "getUserRoleAccess/{userId}/{unitId}", method = RequestMethod.GET)
	public Response getUserRoleAccess(@PathVariable ("userId") Integer userId,
										  @PathVariable ("unitId") Integer unitId) throws Exception {
		try {
			return iUserRoleMapperService.getUserRoleAccess(userId, unitId);
		} catch (Exception e) {
			
			throw e;
		}
	}
	
}
