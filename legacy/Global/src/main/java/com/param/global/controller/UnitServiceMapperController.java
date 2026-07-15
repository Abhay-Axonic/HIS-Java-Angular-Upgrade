package com.param.global.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.AreaMasterDto;
import com.param.global.dto.ServiceMasterDto;
import com.param.global.dto.ServiceSearchReqDto;
import com.param.global.dto.UnitServiceMapperDto;
import com.param.global.service.IUnitServiceMapperService;
import com.param.global.service.IUserMasterService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("/global")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UnitServiceMapperController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(UnitServiceMapperController.class);

	@Autowired
	private IUnitServiceMapperService iUnitServiceMapperService;
	
	@Autowired
	IUserMasterService iUserMasterService;
	
	/***
	 * Controller method to Add UnitServiceMapper
	 * @param unitServiceMapperDto
	 * @return Response
	 */
	@RequestMapping(value="/UnitServiceMapper", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveUnitServiceMapper(@RequestBody UnitServiceMapperDto unitServiceMapperDto) {
		try {
			return iUnitServiceMapperService.saveUnitServiceMapper(unitServiceMapperDto);
		}catch (Exception e) {
		logger.error("saveUnitServiceMapper failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/**
	 * Controller method to search Services by speciality, subSpeciality, unitId, orgId (Autocomplete)
	 * @param searchKeyword
	 * @param specialityId
	 * @param subSplecialityId
	 * @param unitId
	 * @param orgId
	 * @return Response
	 */
	@RequestMapping(value="/UnitServiceMapper/search/{searchKeyword}/speciality/{specialityId}/subSpeciality/{subSplecialityId}/unit/{unitId}/org/{orgId}", method = RequestMethod.GET)
	public Response searchUnitServiceMapper(@PathVariable("searchKeyword") String searchKeyword,
			@PathVariable("specialityId") Integer specialityId,
			@PathVariable("subSplecialityId") Integer subSplecialityId,
			@PathVariable("unitId")Integer unitId,
			@PathVariable("orgId") Integer orgId) {
		try {
			return iUnitServiceMapperService.searchServiceMasterBySpecialitySubSplecialityUnitAndOrg(searchKeyword, specialityId, subSplecialityId, unitId, orgId);
		}catch (Exception e) {
		logger.error("searchUnitServiceMapper failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/**
	 * Controller method to search Services by speciality, subSpeciality, unitId, orgId (Autocomplete)
	 * @param searchKeyword
	 * @param unitId
	 * @param orgId
	 * @return Response
	 */
	@RequestMapping(value="/UnitServiceMapper/searchWithoutSpecialitySubSpeciality/{searchKeyword}/unit/{unitId}/org/{orgId}", method = RequestMethod.GET)
	public Response searchUnitServiceMapperWithoutSpecialitySubSpeciality(@PathVariable("searchKeyword") String searchKeyword,
			@PathVariable("unitId")Integer unitId,
			@PathVariable("orgId") Integer orgId) {
		try {
			return iUnitServiceMapperService.searchUnitServiceMapperWithoutSpecialitySubSpeciality(searchKeyword,unitId, orgId);
		}catch (Exception e) {
		logger.error("searchUnitServiceMapperWithoutSpecialitySubSpeciality failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/**
	 * Controller method to get Mapped ServicesList By Unit (Paginated)
	 * @param unitId
	 * @param orgId
	 * @param offset
	 * @param noOfRecordsPerPage
	 * @return Response
	 */
	@RequestMapping(value="/UnitServiceMapper/unit/{unitId}/org/{orgId}/{offset}/{noOfRecordsPerPage}", method = RequestMethod.GET)
	public Response getMappedServicesListByUnit(@PathVariable("unitId") Integer unitId,
			@PathVariable("orgId") Integer orgId,
			@PathVariable("offset")Integer offset,
			@PathVariable("noOfRecordsPerPage") Integer noOfRecordsPerPage) {
		try {
			UnitServiceMapperDto unitServiceMapperDto = new UnitServiceMapperDto();
			unitServiceMapperDto.setUnitId(unitId);
			unitServiceMapperDto.setOrgnisationId(orgId);
			unitServiceMapperDto.setOffset(offset);
			unitServiceMapperDto.setRecordPerPage(noOfRecordsPerPage);
			return iUnitServiceMapperService.getMappedServicesListByUnit(unitServiceMapperDto);
		}catch (Exception e) {
		logger.error("getMappedServicesListByUnit failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/**
	 * Controller method to get Count of Mapped Services List By Unit (for pagination)
	 * @param unitId
	 * @param orgId
	 * @return Response
	 */
	@RequestMapping(value="/UnitServiceMapper/count/unit/{unitId}/org/{orgId}", method = RequestMethod.GET)
	public Response getCountOfMappedServicesListByUnit(@PathVariable("unitId") Integer unitId,
			@PathVariable("orgId") Integer orgId){
		try {
			UnitServiceMapperDto unitServiceMapperDto = new UnitServiceMapperDto();
			unitServiceMapperDto.setUnitId(unitId);
			unitServiceMapperDto.setOrgnisationId(orgId);
			return iUnitServiceMapperService.getCountOfMappedServicesListByUnit(unitServiceMapperDto);
		}catch (Exception e) {
		logger.error("getCountOfMappedServicesListByUnit failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/**
	 * Controller method to Update UnitServiceMapper Status
	 * @param serviceId
	 * @param status
	 * @param unitId
	 * @param orgId
	 * @return Response
	 */
	@RequestMapping(value="/UnitServiceMapper/serviceId/{serviceId}/status/{status}/unit/{unitId}/org/{orgId}", method = RequestMethod.PUT)
	public Response updateUnitServiceMapperStatus(@PathVariable("serviceId") Integer serviceId,
												  @PathVariable("status") char status,
												  @PathVariable("unitId") Integer unitId,
												  @PathVariable("orgId") Integer orgId) {
		try {
			UnitServiceMapperDto unitServiceMapperDto = new UnitServiceMapperDto();
			unitServiceMapperDto.setServiceId(serviceId);
			unitServiceMapperDto.setStatus(status);
			unitServiceMapperDto.setUnitId(unitId);
			unitServiceMapperDto.setOrgnisationId(orgId);
			return iUnitServiceMapperService.updateUnitServiceMapperStatus(unitServiceMapperDto);
		}catch (Exception e) {
		logger.error("updateUnitServiceMapperStatus failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/serviceMaster",method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchServicesByNameAndCode(@RequestBody ServiceMasterDto serviceMasterDto) {
		try {
			return iUnitServiceMapperService.searchServicesByNameAndCode(serviceMasterDto);
		}catch (Exception e) {
		logger.error("searchServicesByNameAndCode failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/serviceMaster/{searchKeyword}/unit/{unitId}/org/{orgId}", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchActiveServices(@PathVariable("searchKeyword") String searchKeyword,
												  @PathVariable("unitId") Integer unitId,
												  @PathVariable("orgId") Integer orgId, @RequestBody ServiceSearchReqDto serviceSearchReqDto) {
		try {
			serviceSearchReqDto.setServiceName(searchKeyword);
			serviceSearchReqDto.setUnitId(unitId);
			serviceSearchReqDto.setOrganisationId(orgId);
			return iUnitServiceMapperService.searchActiveServices(serviceSearchReqDto);
		}catch (Exception e) {
		logger.error("searchActiveServices failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/searchUnitServiceByMultipleSpecialityAndSubSpeciality", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchUnitServiceByMultipleSpecialityAndSubSpeciality(@RequestBody UnitServiceMapperDto unitServiceMapperDto) {
		try {
			return iUnitServiceMapperService.searchUnitServiceByMultipleSpecialityAndSubSpeciality(unitServiceMapperDto);
		}catch (Exception e) {
		logger.error("searchUnitServiceByMultipleSpecialityAndSubSpeciality failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/procedureSearch/{searchKeyword}/unit/{unitId}/org/{orgId}/speciality/{specialityId}", method = RequestMethod.GET)
	public Response searchProcedure(@PathVariable Map<String,String> params) {
		try {
			int orgId = Integer.parseInt(params.get("orgId").trim());
			int unitId = Integer.parseInt(params.get("unitId").trim());
			int specialityId = Integer.parseInt(params.get("specialityId").trim());
			String searchKeyWord = params.get("searchKeyword").trim();
			return iUnitServiceMapperService.searchProcedure(orgId,unitId,specialityId,searchKeyWord);
		}catch (Exception e) {
		logger.error("searchProcedure failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value="/UnitServiceMapper/search/{searchKeyword}/speciality/{specialityId}/subSpeciality/{subSplecialityId}/unit/{unitId}/org/{orgId}/gender/{gender}", method = RequestMethod.GET)
	public Response searchUnitServiceMapperByGender(@PathVariable("searchKeyword") String searchKeyword,
			@PathVariable("specialityId") Integer specialityId,
			@PathVariable("subSplecialityId") Integer subSplecialityId,
			@PathVariable("unitId")Integer unitId,
			@PathVariable("orgId") Integer orgId,@PathVariable("gender") Integer gender) {
		try {
			return iUnitServiceMapperService.searchServiceMasterBySpecialitySubSplecialityOrgUnitAndGender(searchKeyword, specialityId, subSplecialityId, unitId, orgId,gender);
		}catch (Exception e) {
		logger.error("searchUnitServiceMapperByGender failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/UnitServiceMapper/{unitId}/{orgId}/{serviceName}", method = RequestMethod.GET)
	public Response getMappedServicesListByUnitSearch(@PathVariable("unitId") Integer unitId,
			@PathVariable("orgId") Integer orgId,
			@PathVariable("serviceName") String serviceName) {
		try {
			UnitServiceMapperDto unitServiceMapperDto = new UnitServiceMapperDto();
			unitServiceMapperDto.setUnitId(unitId);
			unitServiceMapperDto.setOrgnisationId(orgId);
			unitServiceMapperDto.setSearchKeyword(serviceName);
			return iUnitServiceMapperService.getMappedServicesListByUnitSearch(unitServiceMapperDto);
		}catch (Exception e) {
		logger.error("getMappedServicesListByUnitSearch failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/serviceMaster/checkService/{labSpecialityId}", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response checkServiceMappedWithTest(@RequestBody ServiceMasterDto dto,@PathVariable("labSpecialityId") Integer labSpecialityId) {
		try {
			return iUnitServiceMapperService.checkServiceMappedWithTest(dto,labSpecialityId);
		}catch (Exception e) {
		logger.error("checkServiceMappedWithTest failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	

	@RequestMapping(value="/itemMaster/{searchKeyword}/unit/{unitId}/org/{orgId}", method = RequestMethod.GET)
	public Response searchItemByNameAndCode(@PathVariable("searchKeyword") String searchKeyword,
												  @PathVariable("unitId") Integer unitId,
												  @PathVariable("orgId") Integer orgId) {
		try {
			//TContractGroupPharmacyDetailsDto
			ServiceMasterDto serviceMasterDto = new ServiceMasterDto();
			serviceMasterDto.setSearchKeyword(searchKeyword);
			serviceMasterDto.setUnitId(unitId);
			serviceMasterDto.setOrganizationId(orgId);
			return iUnitServiceMapperService.searchItemByNameAndCode(serviceMasterDto);
		}catch (Exception e) {
		logger.error("searchItemByNameAndCode failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/searchServiceByNameOrCode", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchServiceByNameOrCode(@RequestBody UnitServiceMapperDto unitServiceMapperDto) {
		try {
			return iUnitServiceMapperService.searchServiceByNameOrCode(unitServiceMapperDto);
		}catch (Exception e) {
		logger.error("searchServiceByNameOrCode failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/serviceMasterForUnitApplicationService",method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response serviceMasterForUnitApplicationService(@RequestBody ServiceMasterDto serviceMasterDto) {
		try {
			return iUnitServiceMapperService.serviceMasterForUnitApplicationService(serviceMasterDto);
		}catch (Exception e) {
		logger.error("serviceMasterForUnitApplicationService failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/serviceAreaBysearchKeyword",method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response serviceAreaBysearchKeyword(@RequestBody AreaMasterDto areaMasterDto) {
		try {
			return iUnitServiceMapperService.serviceAreaBysearchKeyword(areaMasterDto);
		}catch (Exception e) {
		logger.error("serviceAreaBysearchKeyword failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/serviceMaster/checkService/testParameter", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response checkServiceWithTestParameter(@RequestBody ServiceMasterDto dto) {
		try {
			return iUnitServiceMapperService.checkServiceWithTestParameter(dto);
		}catch (Exception e) {
		logger.error("checkServiceWithTestParameter failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getSuperAdminUser/{orgId}/{unitId}/{userId}", method = RequestMethod.GET)
	public Response getSuperAdminUser(@PathVariable ("orgId") Integer orgId,
														@PathVariable ("unitId") Integer unitId,
														@PathVariable ("userId") Integer userId){
		try{
			
			return iUserMasterService.getSuperAdminUser(orgId,unitId,userId);
		}catch(Exception e){
			
			throw e;
		}
	}

	@RequestMapping(value="/serviceMasterForEasyEMR",method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchServicesByNameAndCodeForEasyEMR(@RequestBody ServiceMasterDto serviceMasterDto) {
		try {
			return iUnitServiceMapperService.searchServicesByNameAndCodeForEasyEMR(serviceMasterDto);
		}catch (Exception e) {
		logger.error("searchServicesByNameAndCodeForEasyEMR failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
}
