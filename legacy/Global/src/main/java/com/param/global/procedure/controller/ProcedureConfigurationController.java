package com.param.global.procedure.controller;

import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.common.dto.CommonResponse;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.procedure.dto.ProcedureConfigurationDto;
import com.param.global.procedure.service.IAnesthesiaTypeService;
import com.param.global.procedure.service.IProcedureConfigurationService;
import com.param.global.procedure.service.IProcedureTypeMasterService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value = "/api/procedure")
@SuppressWarnings({"rawtypes"})
public class ProcedureConfigurationController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(ProcedureConfigurationController.class);


	@Autowired
	private IProcedureConfigurationService iProcedureConfigurationService;
	@Autowired
	private IProcedureTypeMasterService iProcedureTypeMasterService;
	@Autowired
	private IAnesthesiaTypeService iAnesthesiaTypeService;
	
	/**
	 * METHOD TO SAVE PROCEDURE CONFIGURATION
	 * @param reqDto
	 * @return
	 * @throws ApplicationException 
	 */
	@RequestMapping(value = "/config" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Create Generic", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "prucedureConfiguration Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized Create to prucedureConfiguration"),
			@ApiResponse(code = 403, message = "Create prucedureConfiguration is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	
	
	public ResponseEntity<CommonResponse> saveProcedureConfiguration(@RequestBody ProcedureConfigurationDto reqDto) throws ApplicationException{
		
			return new ResponseEntity<CommonResponse>(iProcedureConfigurationService.saveProcedureConfiguration(reqDto), HttpStatus.CREATED);
		
	}
	
	/**
	 * METHOD TO GET PROCEDURE TYPE LIST
	 * @param orgId
	 * @param unitId
	 * @return
	 */
	@RequestMapping(value = "/types/{orgId}/{unitId}" , method = RequestMethod.GET)
	public Response getProcedureTypeList(@PathVariable("orgId")int orgId, @PathVariable("unitId")int unitId){
		try{
			return iProcedureTypeMasterService.getProcedureTypeList(orgId, unitId);
		}catch(Exception e){
		logger.error("getProcedureTypeList failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	/**
	 * METHOD TO GET ANASTHESIA TYPE LIST
	 * @param orgId
	 * @param unitId
	 * @return
	 */
	@RequestMapping(value = "/anesthesia/types/{orgId}/{unitId}" , method = RequestMethod.GET)
	public Response getAnesthesiaTypeList(@PathVariable("orgId")int orgId, @PathVariable("unitId")int unitId){
		try{
			return iAnesthesiaTypeService.getAnesthesiaTypeList(orgId, unitId);
		}catch(Exception e){
		logger.error("getAnesthesiaTypeList failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	/**
	 * METHOD TO GET CONFIGURATION LIST
	 * @param orgId
	 * @param unitId
	 * @return
	 */
	@RequestMapping(value = "/configs/{orgId}/{unitId}" , method = RequestMethod.GET)
	public Response getProcedureConfigList(@PathVariable("orgId")int orgId, @PathVariable("unitId")int unitId){
		try{
			return iProcedureConfigurationService.getProcedureConfigList(orgId, unitId);
		}catch(Exception e){
		logger.error("getProcedureConfigList failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	/**
	 * METHOD TO UPDATE CONFIGURATION STATUS
	 * @param id
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "/config/status/{id}/{status}" , method = RequestMethod.GET)
	public Response updateProcConfigStatus(@PathVariable("id")int id, @PathVariable("status")char status){
		try{
			return iProcedureConfigurationService.updateProcedureConfigStatus(id, status);
		}catch(Exception e){
		logger.error("updateProcConfigStatus failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	/**
	 * UPDATE PROCEDURE CONFIGURATION
	 * @param reqDto
	 * @return
	public ResponseEntity<Object> update(@PathVariable("id") Integer id, @RequestBody GenericRequest genericRequest) {
		genericRequest.setId(id);
		try {
			return new ResponseEntity<Object>(GenericService.update(genericRequest), HttpStatus.CREATED);
		} catch (CustomeException CustomeRespone) {
			return new ResponseEntity<Object>(CustomeRespone, HttpStatus.NOT_ACCEPTABLE);
		}

	}return new ResponseEntity<CommonResponse>(iProcedureConfigurationService.saveProcedureConfiguration(reqDto), HttpStatus.CREATED);
	 */
	@RequestMapping(value = "/config" , method = RequestMethod.PUT , consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Create Generic", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "prucedureConfiguration Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized Create to prucedureConfiguration"),
			@ApiResponse(code = 403, message = "Create prucedureConfiguration is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<Object> updateProcedureConfiguration(@RequestBody ProcedureConfigurationDto reqDto) throws ApplicationException{
		try{
			return new ResponseEntity<Object>(iProcedureConfigurationService.updateProcedureConfiguration(reqDto),HttpStatus.CREATED);
			
			
		}catch(Exception e){
		logger.error("updateProcedureConfiguration failed", e);
			return new ResponseEntity<Object>(e, HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@RequestMapping(value = "/getNotesListByProConfigId/{orgId}/{unitId}/{proConfigId}", method = RequestMethod.GET)
	public Response getNotesListByProConfigId(@PathVariable("orgId")Integer orgId ,
			@PathVariable("unitId")Integer unitId , @PathVariable("proConfigId")Integer proConfigId ) {
		try {
			return iProcedureConfigurationService.getNotesListByProConfigId(orgId,unitId,proConfigId);
		} catch (Exception e) {
		logger.error("getNotesListByProConfigId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
