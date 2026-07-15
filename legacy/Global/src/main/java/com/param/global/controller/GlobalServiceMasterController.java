package com.param.global.controller;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.JsonCheck;
import com.param.global.dto.UnitServiceMasterDetailsDto;
import com.param.global.service.IGlobalServiceMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.AreaMasterDto;
import com.param.global.dto.ServiceMasterDto;
import com.param.global.dto.ServiceSearchReqDto;
import com.param.global.dto.UnitServiceMapperDto;
import com.param.global.service.IUnitServiceMapperService;
import com.param.global.service.IUserMasterService;

@RestController
@RequestMapping(value="/api/global")
@SuppressWarnings({ "rawtypes"})
public class GlobalServiceMasterController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(GlobalServiceMasterController.class);

	@Autowired
	private IGlobalServiceMasterService iGlobalServiceMasterService;
	
	
	@RequestMapping(value="/searchGlablServiceMaster",method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchGlablServiceMaster(@RequestBody UnitServiceMasterDetailsDto unitServiceMasterDetailsDto) {
		try {
			return iGlobalServiceMasterService.searchGlablServiceMaster(unitServiceMasterDetailsDto);
		}catch (Exception e) {
			logger.error("Error while searching the global service master", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/glablServiceMaster",method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response glablServiceMaster(@RequestBody UnitServiceMasterDetailsDto unitServiceMasterDetailsDto) {
		try {
			return iGlobalServiceMasterService.glablServiceMaster(unitServiceMasterDetailsDto);
		}catch (Exception e) {
			logger.error("Error while saving the global service master", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getAllGlablServiceMaster",method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getAllGlablServiceMaster(@RequestBody UnitServiceMasterDetailsDto unitServiceMasterDetailsDto) {
		try {
			return iGlobalServiceMasterService.getAllGlablServiceMaster(unitServiceMasterDetailsDto);
		}catch (Exception e) {
			logger.error("Error while fetching all the global service master", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/glablServiceMasterSpecialityName",method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response glablServiceMasterSpecialityName(@RequestBody UnitServiceMasterDetailsDto unitServiceMasterDetailsDto) {
		try {
			return iGlobalServiceMasterService.glablServiceMasterSpecialityName(unitServiceMasterDetailsDto);
		}catch (Exception e) {
			logger.error("Error while fetching the global service master speciality name", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/glablServiceMasterSubSpecialityName",method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response glablServiceMasterSubSpecialityName(@RequestBody UnitServiceMasterDetailsDto unitServiceMasterDetailsDto) {
		try {
			return iGlobalServiceMasterService.glablServiceMasterSubSpecialityName(unitServiceMasterDetailsDto);
		}catch (Exception e) {
			logger.error("Error while fetching the global service master sub-speciality name", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/saveGlobalServiceMasterByExcel", method = RequestMethod.POST)
	public Response saveGlobalServiceMasterByExcel(HttpServletRequest request,HttpServletResponse res,HttpSession session,@RequestHeader Integer organizationId,
			@RequestHeader Integer unitId,@RequestHeader Integer createdBy,@RequestHeader Integer updatedBy,@RequestParam("file") MultipartFile file){
		try {
			
			ServiceMasterDto serviceMasterDto=new ServiceMasterDto();
			serviceMasterDto.setUnitId(unitId);
			serviceMasterDto.setOrganizationId(organizationId);
			serviceMasterDto.setCreatedBy(createdBy);
			serviceMasterDto.setUpdatedBy(updatedBy);
			serviceMasterDto.setFile(file);
			return iGlobalServiceMasterService.saveGlobalServiceMasterByExcel(serviceMasterDto);

			} catch (Exception e) {
				logger.error("Error while saving the global service master by excel", e);
				return new Response<Object, Serializable>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}

	}
	
@RequestMapping(value="/saveGlobalMasterData",method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveGlobalMasterData(@RequestBody UnitServiceMasterDetailsDto unitServiceMasterDetailsDto) {
		try {
			Response res =iGlobalServiceMasterService.saveGlobalMasterData(unitServiceMasterDetailsDto);
			return iGlobalServiceMasterService.callProcedure(unitServiceMasterDetailsDto);
		}catch (Exception e) {
		logger.error("saveGlobalMasterData failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

}
