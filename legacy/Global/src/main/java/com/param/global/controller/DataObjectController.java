package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.DataObjectMasterDto;
import com.param.global.service.IDataObjectMasterService;

@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping(value = "/api/global")
public class DataObjectController implements ICommonConstants{
	private static final Logger logger = Logger.getLogger(DataObjectController.class);

	@Autowired
	private IDataObjectMasterService iDataObjectMasterService;
	
	/**
	 *  Save Data Object 
	 * @param reqDto
	 * @return
	 */
	
	@RequestMapping(value = "/dataobject" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveDataObject(@RequestBody DataObjectMasterDto reqDto){
		try{
			return iDataObjectMasterService.saveDataObject(reqDto);
		}catch(Exception e){
			logger.error("Error while saving the data object", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/updateDataObject" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateDataObject(@RequestBody DataObjectMasterDto reqDto){
		try{
			return iDataObjectMasterService.updateDataObject(reqDto);
		}catch(Exception e){
			logger.error("Error while updating the data object", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	/**
	 *  Get Data Object List By OrgId and UnitId 
	 * @param reqDto
	 * @return
	 */
	
	@RequestMapping(value = "/dataobjects/{orgId}/{unitId}" , method = RequestMethod.GET )
	public Response saveDataObject(@PathVariable("orgId")int orgId, @PathVariable("unitId")int unitId){
		try{
			return iDataObjectMasterService.getDataObjectList(orgId, unitId);
		}catch(Exception e){
			logger.error("Error while fetching the data object list by org id and unit id", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	/**
	 *  get Data Object By Id
	 * @param reqDto
	 * @return
	 */
	
	@RequestMapping(value = "/dataobject/{id}")
	public Response getDataObjectById(@PathVariable("id")int id){
		try{
			return iDataObjectMasterService.getDataObjectById(id);
		}catch(Exception e){
			logger.error("Error while fetching the data object by id", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	
	@RequestMapping(value = "/getAllDataObject/{orgId}/{unitId}" , method = RequestMethod.GET )
	public Response getAllDataObject(@PathVariable("orgId")int orgId, @PathVariable("unitId")int unitId){
		try{
			return iDataObjectMasterService.getAllDataObject(orgId, unitId);
		}catch(Exception e){
			logger.error("Error while fetching all data objects", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/updateDataObjectStatus" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE )
	public Response updateDataObjectStatus(@RequestBody DataObjectMasterDto dataObjectMasterDto){
		try{
			return iDataObjectMasterService.updateDataObjectStatus(dataObjectMasterDto);
		}catch(Exception e){
			logger.error("Error while updating the data object status", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
}
