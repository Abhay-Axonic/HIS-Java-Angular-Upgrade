package com.param.global.controller;

import java.util.Map;

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
import com.param.global.dto.PeriodicityServiceResDto;
import com.param.global.dto.UnitServiceMasterDetailsDto;
import com.param.global.service.IUnitServiceMasterDetailsService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="api/global")
@SuppressWarnings({ "rawtypes"})
public class UnitServiceMasterDetailsController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(UnitServiceMasterDetailsController.class);

	@Autowired
	private IUnitServiceMasterDetailsService iUnitServiceMasterDetailsService;
	
	/**
	 * Controller method to Save UnitServiceMasterDetails
	 * @param unitServiceMasterDetailsDto
	 * @return Response
	 */
	@RequestMapping(value="/UnitServiceMasterDetails",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response saveUnitServiceMasterDetails(@RequestBody UnitServiceMasterDetailsDto unitServiceMasterDetailsDto) {
		try {
			return iUnitServiceMasterDetailsService.saveUnitServiceMasterDetails(unitServiceMasterDetailsDto);
		}catch (Exception e) {
		logger.error("saveUnitServiceMasterDetails failed", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/**
	 * controller method to get UnitServiceMasterDetails List(Paginated)
	 * @param orgId
	 * @param unitId
	 * @param offset
	 * @param recordPerPage
	 * @return Response
	 */
	@RequestMapping(value="/UnitServiceMasterDetails/org/{orgId}/unit/{unitId}/{offset}/{recordPerPage}",method=RequestMethod.GET)
	public Response getPaginatedUnitServiceMasterDetailsList(@PathVariable("orgId") Integer orgId,
				@PathVariable("unitId") Integer unitId,
				@PathVariable("offset") Integer offset,
				@PathVariable("recordPerPage") Integer recordPerPage) {
		try {
			UnitServiceMasterDetailsDto unitServiceMasterDetailsDto = new UnitServiceMasterDetailsDto();
			unitServiceMasterDetailsDto.setOrganizationId(orgId);
			unitServiceMasterDetailsDto.setUnitId(unitId);
			unitServiceMasterDetailsDto.setOffset(offset);
			unitServiceMasterDetailsDto.setRecordPerPage(recordPerPage);
			return iUnitServiceMasterDetailsService.getPaginatedUnitServiceMasterDetailsList(unitServiceMasterDetailsDto);
		}catch (Exception e) {
		logger.error("getPaginatedUnitServiceMasterDetailsList failed", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/**
	 * Controller method to get UnitServiceMasterDetails Count(for Paginatation)
	 * @param orgId
	 * @param unitId
	 * @return Response
	 */
	@RequestMapping(value="/UnitServiceMasterDetails/count/org/{orgId}/unit/{unitId}",method=RequestMethod.GET)
	public Response getUnitServiceMasterDetailsCount(@PathVariable("orgId") Integer orgId, @PathVariable("unitId") Integer unitId) {
		try {
			UnitServiceMasterDetailsDto unitServiceMasterDetailsDto = new UnitServiceMasterDetailsDto();
			unitServiceMasterDetailsDto.setOrganizationId(orgId);
			unitServiceMasterDetailsDto.setUnitId(unitId);
			return iUnitServiceMasterDetailsService.getUnitServiceMasterDetailsCount(unitServiceMasterDetailsDto);
		}catch (Exception e) {
		logger.error("getUnitServiceMasterDetailsCount failed", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/***
	 * Controller method to get UnitServiceMasterDetails By Id
	 * @param unitServiceMasterDetailsId
	 * @return Response
	 */
	@RequestMapping(value="/UnitServiceMasterDetails/{UnitServiceMasterDetailsId}",method=RequestMethod.GET)
	public Response getUnitServiceMasterDetailsById(@PathVariable("UnitServiceMasterDetailsId") Integer unitServiceMasterDetailsId) {
		try {
			UnitServiceMasterDetailsDto unitServiceMasterDetailsDto = new UnitServiceMasterDetailsDto();
			unitServiceMasterDetailsDto.setUnitServiceMasterDetailsId(unitServiceMasterDetailsId);
			return iUnitServiceMasterDetailsService.getUnitServiceMasterDetailsById(unitServiceMasterDetailsDto);
		}catch (Exception e) {
		logger.error("getUnitServiceMasterDetailsById failed", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/***
	 * Controller method to update UnitServiceMasterDetails
	 * @param unitServiceMasterDetailsDto
	 * @return Response
	 */
	@RequestMapping(value="/UnitServiceMasterDetails",method=RequestMethod.PUT)
	public Response updateUnitServiceMasterDetails(@RequestBody UnitServiceMasterDetailsDto unitServiceMasterDetailsDto) {
		try {
			return iUnitServiceMasterDetailsService.updateUnitServiceMasterDetails(unitServiceMasterDetailsDto);
		}catch (Exception e) {
		logger.error("updateUnitServiceMasterDetails failed", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value="/getAllActiveTax/{organizationId}",method=RequestMethod.GET)
	public Response getAllActiveTax(@PathVariable("organizationId") Integer organizationId) {
		try {
			
			return iUnitServiceMasterDetailsService.getAllActiveTax(organizationId);
		}catch (Exception e) {
		logger.error("getAllActiveTax failed", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/search/services/periodicity", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response searchPeriodicityServices(@RequestBody PeriodicityServiceResDto periodicityServiceResDto){
		try{
			
			int visitTypeId = periodicityServiceResDto.getVisitTypeId();
			int unitId = periodicityServiceResDto.getUnitIdId();
			String serviceName = periodicityServiceResDto.getServiceName();
			
			return iUnitServiceMasterDetailsService.searchPeriodicityServices(visitTypeId, unitId, serviceName);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/search/services/procedure/{orgId}/{unitId}/{serviceName}/{specialityId}")
	public Response searchProcedureServices(@PathVariable Map<String,String> params){
		try{
			int orgId = Integer.parseInt(params.get("orgId"));
			int unitId = Integer.parseInt(params.get("unitId"));
			String serviceName = params.get("serviceName") != null ? params.get("serviceName") : "";
			int specialityId = Integer.parseInt(params.get("specialityId"));
			return iUnitServiceMasterDetailsService.searchProcedureServices(orgId, unitId, serviceName,specialityId);
		}catch(Exception e){
			
			throw e;
		}
	}
	@RequestMapping(value="/getProcedureTypeList",method=RequestMethod.GET)
	public Response getProcedureTypeList() {
		try {
			return iUnitServiceMasterDetailsService.getProcedureTypeList();
		}catch (Exception e) {
		logger.error("getProcedureTypeList failed", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getProcedureListByProcedureType", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getProcedureListByProcedureType(@RequestBody UnitServiceMasterDetailsDto unitServiceMasterDetailsDto) {
		try {
			return iUnitServiceMasterDetailsService.getProcedureListByProcedureType(unitServiceMasterDetailsDto);
		}catch(Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value="/UnitServiceMasterDetailsSearch",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response getPaginatedUnitServiceMasterDetailsListSearch(@RequestBody UnitServiceMasterDetailsDto unitServiceMasterDetailsDto) {
		try {
			/*
			 * UnitServiceMasterDetailsDto unitServiceMasterDetailsDto = new
			 * UnitServiceMasterDetailsDto();
			 * unitServiceMasterDetailsDto.setOrganizationId(orgId);
			 * unitServiceMasterDetailsDto.setUnitId(unitId);
			 * unitServiceMasterDetailsDto.setServiceStandardName(serviceName);
			 */
			return iUnitServiceMasterDetailsService.getPaginatedUnitServiceMasterDetailsListSearch(unitServiceMasterDetailsDto);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/UnitServiceMasterDetails/{orgId}/{unitId}",method=RequestMethod.GET)
	public Response getUnitServiceMasterDetailsListForCyclic(@PathVariable("orgId") Integer orgId,
				@PathVariable("unitId") Integer unitId) {
		try {
			UnitServiceMasterDetailsDto unitServiceMasterDetailsDto = new UnitServiceMasterDetailsDto();
			unitServiceMasterDetailsDto.setOrganizationId(orgId);
			unitServiceMasterDetailsDto.setUnitId(unitId);
			return iUnitServiceMasterDetailsService.getUnitServiceMasterDetailsListForCyclic(unitServiceMasterDetailsDto);
		}catch (Exception e) {
		logger.error("getUnitServiceMasterDetailsListForCyclic failed", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/UnitServiceMasterDetails/{patientId}/{serviceId}/{orgId}/{unitId}",method=RequestMethod.GET)
	public Response getUnitServiceMasterQtyDetailsByServiceId(@PathVariable("patientId") Integer patientId, @PathVariable("serviceId") Integer serviceId, 
			@PathVariable("orgId") Integer orgId, @PathVariable("unitId") Integer unitId) {
		try {
			UnitServiceMasterDetailsDto unitServiceMasterDetailsDto = new UnitServiceMasterDetailsDto();
			unitServiceMasterDetailsDto.setServiceMasterId(serviceId);
			unitServiceMasterDetailsDto.setPatientId(patientId);
			unitServiceMasterDetailsDto.setOrganizationId(orgId);
			unitServiceMasterDetailsDto.setUnitId(unitId);
			return iUnitServiceMasterDetailsService.getUnitServiceMasterQtyDetailsByServiceId(unitServiceMasterDetailsDto);
		}catch (Exception e) {
		logger.error("getUnitServiceMasterQtyDetailsByServiceId failed", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/unitServiceMasterDetailsForSchedulingWorking",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response UnitServiceMasterDetailsForSchedulingWorking(@RequestBody UnitServiceMasterDetailsDto unitServiceMasterDetailsDto) {
		try {
			return iUnitServiceMasterDetailsService.UnitServiceMasterDetailsForSchedulingWorking(unitServiceMasterDetailsDto);
		}catch (Exception e) {
		logger.error("UnitServiceMasterDetailsForSchedulingWorking failed", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/unitServiceMasterDetailsForSchedulingWorkingDetails",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response unitServiceMasterDetailsForSchedulingWorkingDetails(@RequestBody UnitServiceMasterDetailsDto unitServiceMasterDetailsDto) {
		try {
			return iUnitServiceMasterDetailsService.unitServiceMasterDetailsForSchedulingWorkingDetails(unitServiceMasterDetailsDto);
		}catch (Exception e) {
		logger.error("unitServiceMasterDetailsForSchedulingWorkingDetails failed", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

    @RequestMapping(value="/getAdminServicesForConfiguration",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response getAdminServicesForConfiguration(@RequestBody UnitServiceMasterDetailsDto unitServiceMasterDetailsDto) {
        try {

            return iUnitServiceMasterDetailsService.getAdminServicesForConfiguration(unitServiceMasterDetailsDto);
        }catch (Exception e) {
        logger.error("getAdminServicesForConfiguration failed", e);
        }
    return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
    }
}
