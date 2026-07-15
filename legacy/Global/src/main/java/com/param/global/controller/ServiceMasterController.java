package com.param.global.controller;


import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.param.common.dto.CommonResponse;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.ServiceMasterDto;
import com.param.global.service.IServiceMasterService;
import com.param.module.service.ServiceItemMapperDto;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@SuppressWarnings({ "rawtypes", "unchecked" })
@RequestMapping(value="billing")
public class ServiceMasterController implements IError, ICommonConstants{

	private static final Logger logger = Logger.getLogger(ServiceMasterController.class);

	@Autowired
	private IServiceMasterService iServiceMasterService;


	@RequestMapping(value="ok", method=RequestMethod.GET)
	public Response saveServiceMaster() {
		/*//System.out.println("Ganpati Bappa Moraya!!");*/
		return new Response("sucess", "200", "", null, null);
	}


	@RequestMapping(value = "saveOrganizationServiceMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Create Generic", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "prucedureConfiguration Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized Create to prucedureConfiguration"),
			@ApiResponse(code = 403, message = "Create prucedureConfiguration is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })


	public ResponseEntity<CommonResponse> saveOrganizationServiceMaster(@RequestBody ServiceMasterDto xServiceMasterDto) throws ApplicationException{

			return new ResponseEntity<CommonResponse>(iServiceMasterService.saveOrganizationServiceMaster(xServiceMasterDto), HttpStatus.CREATED);

	}

	/*@RequestMapping(value="saveOrganizationServiceMaster",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response saveOrganizationServiceMaster(@RequestBody ServiceMasterDto xServiceMasterDto) {
		try {
			return iServiceMasterService.saveOrganizationServiceMaster(xServiceMasterDto);
		}catch (Exception e) {

			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}*/

	@RequestMapping(value="/changeOrganizationServiceMasterStatus", method=RequestMethod.PUT, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response changeOrganizationServiceMasterStatus(@RequestBody ServiceMasterDto xServiceMasterDto) {
		try {
			return iServiceMasterService.changeOrganizationServiceMasterStatus(xServiceMasterDto);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new Response<>(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@RequestMapping(value = "/listOrganizationServiceMaster/{orgId}/{offset}/{recordPerPage}", method = RequestMethod.GET)
	public Response listOrganizationServiceMasterMaster(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "offset") Integer offset,
			@PathVariable(value = "recordPerPage") Integer recordPerPage){
		try{
			return iServiceMasterService.listOrganizationServiceMasterMaster(orgId, offset, recordPerPage);

		} catch (Exception e){
			logger.error(e.getMessage(), e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
	@RequestMapping(value = "/getOrganizationServiceMasterCount/{orgId}", method = RequestMethod.GET)
	public Response getOrganizationServiceMasterTotalCount(@PathVariable(value= "orgId") Integer orgId){
			try{
				return iServiceMasterService.getOrganizationServiceMasterTotalCount(orgId);
			}
			catch (Exception e){
					logger.error(e.getMessage(), e);
					return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
			}
		}

	@RequestMapping(value="/getOrganizationServiceMasterById", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getOrganizationServiceMasterById(@RequestBody ServiceMasterDto xServiceMasterDto) {
		try {
			return iServiceMasterService.getOrganizationServiceMasterById(xServiceMasterDto);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@RequestMapping(value = "updateGlobalServiceMaster" , method = RequestMethod.PUT , consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Create Generic", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "prucedureConfiguration Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized Create to prucedureConfiguration"),
			@ApiResponse(code = 403, message = "Create prucedureConfiguration is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })


	public ResponseEntity<CommonResponse> updateOrganizationServiceMaster(@RequestBody ServiceMasterDto xServiceMasterDto) throws ApplicationException{

			return new ResponseEntity<CommonResponse>(iServiceMasterService.updateOrganizationServiceMaster(xServiceMasterDto), HttpStatus.CREATED);

	}

	/*@RequestMapping(value="updateGlobalServiceMaster", method=RequestMethod.PUT, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response updateOrganizationServiceMaster(@RequestBody ServiceMasterDto xServiceMasterDto) {
		try {
			return iServiceMasterService.updateOrganizationServiceMaster(xServiceMasterDto);
		}catch (Exception e) {

			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}*/

	@RequestMapping(value="/organizationMaster/speciality/{specialityId}/subSpeciality/{subSpecialityId}/org/{orgId}/unit/{unitId}",method=RequestMethod.GET)
	public Response getOrganizationMasterByDepartmentAndSUbDepartment(@PathVariable(value= "specialityId") Integer specialityId,
			@PathVariable(value= "subSpecialityId") Integer subSpecialityId, @PathVariable(value= "orgId") Integer orgId,@PathVariable(value= "unitId") Integer unitId) {
		try {
			ServiceMasterDto xServiceMasterDto = new ServiceMasterDto();
			xServiceMasterDto.setOrganizationId(orgId);
			xServiceMasterDto.setUnitId(unitId);
			xServiceMasterDto.setSpecialityId(specialityId);
			xServiceMasterDto.setSubSpecialityId(subSpecialityId);
			return iServiceMasterService.getOrganizationServiceMasterBySpecialityAndSubSpeciality(xServiceMasterDto);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@RequestMapping(value = "/serviceMaster/details", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getserviceMasterDetailsById(@RequestBody ServiceMasterDto serviceMasterDto){
			try{
				return iServiceMasterService.getServiceDetailsByServiceId(serviceMasterDto);
			}
			catch (Exception e){
					logger.error(e.getMessage(), e);
					return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
			}
	}

	@RequestMapping(value = "/surcharge/service/details", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getSurchargeServiceDetails(@RequestBody ServiceMasterDto serviceMasterDto){
			try{
				return iServiceMasterService.getSurchargeServiceDetails(serviceMasterDto);
			}
			catch (Exception e){
					logger.error(e.getMessage(), e);
					return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
			}
	}


	@RequestMapping(value="/service/consultations/org/{orgId}/unit/{unitId}/splId/{splId}/subSplId/{subSplId}",method=RequestMethod.GET)
	public Response getConsultationService(@PathVariable(value= "orgId") Integer orgId,@PathVariable(value= "unitId") Integer unitId,@PathVariable(value= "splId") Integer splId,@PathVariable(value= "subSplId") Integer subSplId){
			try{
				return iServiceMasterService.getConsultationService(orgId,unitId,splId,subSplId);
			}
			catch (Exception e){
					logger.error(e.getMessage(), e);
					return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
			}
	}

	@RequestMapping(value = "/autoCompleteServicesLab/{orgId}/{orgUnitId}/{subDeptId}/{serviceName}", method = RequestMethod.GET)
	public @ResponseBody Response getAutoCompleteServicesLab(
			@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "orgUnitId") Integer orgUnitId,
			@PathVariable(value = "subDeptId") Integer subDeptId,
			@PathVariable(value = "serviceName") String serviceName)
	{
		try
		{
			return iServiceMasterService.getAutoCompleteServicesLab(orgId,orgUnitId,subDeptId,serviceName);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@RequestMapping(value="getListOfServiceMaster/{orgId}/{unitId}", method = RequestMethod.GET)
	public Response getListOfServiceMaster(@PathVariable ("orgId") Integer orgId, @PathVariable ("unitId") Integer unitId) {
		try {
			return iServiceMasterService.getListOfServiceMaster(orgId, unitId);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@RequestMapping(value = "/saveServiceMaster1", method = RequestMethod.POST)
	public Response saveData(HttpServletRequest request,HttpServletResponse res,HttpSession session,@RequestHeader Integer organizationId,
			@RequestHeader Integer unitId,@RequestHeader Integer createdBy,@RequestHeader Integer updatedBy,@RequestParam("file") MultipartFile file){
		try {

			ServiceMasterDto serviceMasterDto=new ServiceMasterDto();
			serviceMasterDto.setUnitId(unitId);
			serviceMasterDto.setOrganizationId(organizationId);
			serviceMasterDto.setCreatedBy(createdBy);
			serviceMasterDto.setUpdatedBy(updatedBy);
			serviceMasterDto.setFile(file);
			return iServiceMasterService.readCSVFile(serviceMasterDto);

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return new Response<Object, Serializable>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}

	}
	@RequestMapping(value="getCancelServices/{admissionId}/{visitTypeId}/{orgId}", method = RequestMethod.GET)
	public Response getCancelServices(@PathVariable ("admissionId") Integer admissionId, @PathVariable ("visitTypeId") Integer visitTypeId , @PathVariable ("orgId") Integer orgId) {
		try {
			return iServiceMasterService.getCancelServices(admissionId, visitTypeId,orgId);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@RequestMapping(value="/service/autoRendered/{orgId}/{unitId}",method=RequestMethod.GET)
	public Response getAutoRenderedServiceDetails(@PathVariable(value= "orgId") Integer orgId,
												  @PathVariable(value= "unitId") Integer unitId){
			try{
				return iServiceMasterService.getAutoRenderedServiceDetails(orgId,unitId);
			}
			catch (Exception e){
					logger.error(e.getMessage(), e);
					return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
			}
	}

	@RequestMapping(value="/service/outsource/{serviceName}/{unitId}",method=RequestMethod.GET)
	public Response getOutsourcedServices(@PathVariable Integer unitId,@PathVariable String serviceName){
		try {
			return iServiceMasterService.getOutsourcedServices(unitId,serviceName);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@RequestMapping(value="/service/getConsultationDeptByServiceId/{serviceId}/{unitId}",method=RequestMethod.GET)
	public Response getConsultationDeptByServiceId(@PathVariable(value= "serviceId") Integer serviceId,
												  @PathVariable(value= "unitId") Integer unitId){
			try{
				return iServiceMasterService.getConsultationDeptByServiceId(serviceId,unitId);
			}
			catch (Exception e){
					logger.error(e.getMessage(), e);
					return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
			}
	}

	@RequestMapping(value="/serviceMasterUniversalSearch" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response serviceMasterUniversalSearch(@RequestBody ServiceMasterDto serviceMasterDto){
		try{

			return iServiceMasterService.serviceMasterUniversalSearch(serviceMasterDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/getServiceItemMapperDetails" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getServiceItemMapperDetails(@RequestBody ServiceItemMapperDto serviceItemMapperDto){
		try{

			return iServiceMasterService.getServiceItemMapperDetails(serviceItemMapperDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	 @RequestMapping(value="/resultEntry/getServiceItemMapperDetailsByServiceId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getServiceItemMapperDetailsByServiceId(@RequestBody ServiceItemMapperDto serviceItemMapperDto){
			try{

				return iServiceMasterService.getServiceItemMapperDetailsByServiceId(serviceItemMapperDto);
			}catch(Exception e){
				logger.error(e.getMessage(), e);
			}
			return new Response(ERROR, null, null, null,null);
		}

	 @RequestMapping(value="/resultEntry/getBatchDetailsByItemList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getBatchDetailsByItemList(@RequestBody ServiceItemMapperDto serviceItemMapperDto){
			try{
				return iServiceMasterService.getBatchDetailsByItemList(serviceItemMapperDto);
			}catch(Exception e){
				logger.error(e.getMessage(), e);
			}
			return new Response(ERROR, null, null, null,null);
		}

	 @RequestMapping(value = "/listOfServicesForWebAppIntegration/{orgId}/{unitId}", method = RequestMethod.GET)
		public Response listOfServicesForWebAppIntegration(@PathVariable(value = "orgId") Integer orgId,@PathVariable(value = "unitId") Integer unitId){
			try{
				return iServiceMasterService.listOfServicesForWebAppIntegration(orgId, unitId);

			} catch (Exception e){
				logger.error(e.getMessage(), e);
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
			}
		}
}
