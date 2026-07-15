package com.param.global.unit.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.unit.dto.WingMasterDto;
import com.param.global.unit.service.IWingMasterService;

@RestController
@RequestMapping(value="/adt")
@SuppressWarnings("rawtypes")
public class WingMasterController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(WingMasterController.class);

	@Autowired
	private IWingMasterService iWingMasterService;
	
	@RequestMapping(value="/getWingMasterListByOrgAndUnit/{organizationId}/{unitId}" ,method = RequestMethod.GET)
	public Response getWingMasterListByOrgAndUnit(@PathVariable("organizationId") Integer organizationId , @PathVariable("unitId") Integer unitId){
		try{
			WingMasterDto wingMasterDto = new WingMasterDto();
			wingMasterDto.setOrganizationId(organizationId);
			wingMasterDto.setUnitId(unitId);
			return iWingMasterService.getWingMasterListByOrgAndUnit(wingMasterDto);
		}catch(Exception e){
			logger.error("getWingMasterListByOrgAndUnit failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	@RequestMapping(value="/saveWingMaster" ,method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response saveWingMaster(@RequestBody WingMasterDto wingMasterDto){
		try{
				return iWingMasterService.saveWingMaster(wingMasterDto);
		}catch(Exception e){
			logger.error("saveWingMaster failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	@RequestMapping(value="/updateWingMaster" ,method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response updateWingMaster(@RequestBody WingMasterDto wingMasterDto){
		try{
				return iWingMasterService.updateWingMaster(wingMasterDto);
		}catch(Exception e){
			logger.error("updateWingMaster failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	@RequestMapping(value="/updateWingMasterStatus" ,method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response updateWingMasterStatus(@RequestBody WingMasterDto wingMasterDto){
		try{
				return iWingMasterService.updateWingMasterStatus(wingMasterDto);
		}catch(Exception e){
		logger.error("updateWingMasterStatus failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	@RequestMapping(value="/getWingMasterList" ,method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getWingMasterList(@RequestBody WingMasterDto wingMasterDto){
		try{
				return iWingMasterService.getWingMasterList(wingMasterDto);
		}catch(Exception e){
		logger.error("getWingMasterList failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	@RequestMapping(value="/getWingMasterListCount" ,method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getWingMasterListCount(@RequestBody WingMasterDto wingMasterDto){
		try{
				return iWingMasterService.getWingMasterListCount(wingMasterDto);
		}catch(Exception e){
		logger.error("getWingMasterListCount failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	@RequestMapping(value="/getWingMasterListById/{wingId}" ,method = RequestMethod.GET)
	public Response getWingMasterListById(@PathVariable("wingId") Integer wingId ){
		try{
			return iWingMasterService.getWingMasterListById(wingId);
		}catch(Exception e){
		logger.error("getWingMasterListById failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}

}
