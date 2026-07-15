package com.param.opd.coversheet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.PatientRegistrationDto;
import com.param.global.unit.dto.AllergyPatientMapperDto;
import com.param.global.unit.dto.ClinicalHeaderTabDto;
import com.param.opd.coversheet.dto.PatientPersonalHistoryDetailsDto;
import com.param.opd.coversheet.service.IAllergyPatientMapperService;



/*FUNCTIONALITY : ALLERGY PATIENT MAPPER
DATE:12/04/2018
CREATED BY: JYOTI*/
@RestController
@RequestMapping("api/opd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AllergyPatientMapperController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(AllergyPatientMapperController.class);

	@Autowired
	private IAllergyPatientMapperService iAllergyPatientMapperService;
	
	@RequestMapping(value="/saveAllergyPatientMapper", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveAllergyPatientMapper(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestBody AllergyPatientMapperDto allergyPatientMapperDto) {
		try
		{
			allergyPatientMapperDto.setUnitId(unitId);
			allergyPatientMapperDto.setOrganizationId(organizationId);
			return iAllergyPatientMapperService.saveAllergyPatientMapper(allergyPatientMapperDto);

		}catch(Exception e)
		{
			logger.error("saveAllergyPatientMapper failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/updateAllergyPatientMapper", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateAllergyPatientMapper(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestBody AllergyPatientMapperDto allergyPatientMapperDto) {
		try
		{
			allergyPatientMapperDto.setUnitId(unitId);
			allergyPatientMapperDto.setOrganizationId(organizationId);
			return iAllergyPatientMapperService.updateAllergyPatientMapper(allergyPatientMapperDto);

		}catch(Exception e)
		{
			logger.error("updateAllergyPatientMapper failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/updateEnterErrorStatusAllergyPatientMapper", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateEnterErrorStatusAllergyPatientMapper(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestBody AllergyPatientMapperDto allergyPatientMapperDto) {
		try
		{
			allergyPatientMapperDto.setUnitId(unitId);
			allergyPatientMapperDto.setOrganizationId(organizationId);
			return iAllergyPatientMapperService.updateStatusEnterInError(allergyPatientMapperDto);

		}catch(Exception e)
		{
			logger.error("updateEnterErrorStatusAllergyPatientMapper failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/getAllergyPatientMapper", method=RequestMethod.GET)
	public @ResponseBody Response listOfAllergyPatientMapper(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId) {
		try
		{
			AllergyPatientMapperDto allergyPatientMapperDto= new AllergyPatientMapperDto();
			allergyPatientMapperDto.setPatientId(patientId);
			allergyPatientMapperDto.setUnitId(unitId);
			allergyPatientMapperDto.setOrganizationId(organizationId);
			return iAllergyPatientMapperService.getListOfAllergyPatientMapper(allergyPatientMapperDto);
		}catch(Exception e)
		{
			logger.error("listOfAllergyPatientMapper failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/cancelAllergyPatientMapper", method=RequestMethod.GET)
	public @ResponseBody Response cancelAllergyPatientMapper(@RequestParam(value="allergy_id") int allergyId,@RequestParam(value="allergy_type_id") int allergyTypeId,
			@RequestParam(value="patient_id") int patientId) {
		try
		{
			AllergyPatientMapperDto allergyPatientMapperDto= new AllergyPatientMapperDto();
			allergyPatientMapperDto.setPatientId(patientId);
			allergyPatientMapperDto.setAllergyId(allergyId);
			allergyPatientMapperDto.setAllergyTypeId(allergyTypeId);
			return iAllergyPatientMapperService.cancelAllergyPatientMapper(allergyPatientMapperDto);
		}catch(Exception e)
		{
			logger.error("cancelAllergyPatientMapper failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/getOldAllergyPatientMapper", method=RequestMethod.GET)
	public @ResponseBody Response getOldAllergyPatientMapper(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestParam(value="created_date") String createdDate) {
		try
		{
			AllergyPatientMapperDto allergyPatientMapperDto= new AllergyPatientMapperDto();
			allergyPatientMapperDto.setPatientId(patientId);
			allergyPatientMapperDto.setUnitId(unitId);
			allergyPatientMapperDto.setOrganizationId(organizationId);
			allergyPatientMapperDto.setCreatedDate(createdDate);
//			allergyPatientMapperDto.setRoleId(roleId);
//			allergyPatientMapperDto.setEncounterId(encounterId);
//			allergyPatientMapperDto.setAdmissionId(admissionId);
			return iAllergyPatientMapperService.getOldAllergyPatientMapper(allergyPatientMapperDto);
		}catch(Exception e)
		{
			logger.error("getOldAllergyPatientMapper failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getCurrentAllergyPatientMapper", method=RequestMethod.GET)
	public @ResponseBody Response getCurrentAllergyPatientMapper(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestParam(value="roleId") int roleId) {
		try
		{
			AllergyPatientMapperDto allergyPatientMapperDto= new AllergyPatientMapperDto();
			allergyPatientMapperDto.setPatientId(patientId);
			allergyPatientMapperDto.setUnitId(unitId);
			allergyPatientMapperDto.setOrganizationId(organizationId);
			allergyPatientMapperDto.setRoleId(roleId);
			
			return iAllergyPatientMapperService.getCurrentAllergyPatientMapper(allergyPatientMapperDto);
		}catch(Exception e)
		{
			logger.error("getCurrentAllergyPatientMapper failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getGenericAutoFillSearch",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getGenericAutoFillSearch(@RequestBody AllergyPatientMapperDto allergyPatientMapperDto) {
		try {
			return iAllergyPatientMapperService.getGenericAutoFillSearch(allergyPatientMapperDto);
		}catch (Exception e) {
			logger.error("getGenericAutoFillSearch failed", e);
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value="/getCurrentAllergyPatientMapperForCoversheet", method=RequestMethod.GET)
	public @ResponseBody Response getCurrentAllergyPatientMapperForCoversheet(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId, @RequestParam(value="created_date") String createdDate) {
		try
		{
			AllergyPatientMapperDto allergyPatientMapperDto= new AllergyPatientMapperDto();
			allergyPatientMapperDto.setPatientId(patientId);
			allergyPatientMapperDto.setUnitId(unitId);
			allergyPatientMapperDto.setOrganizationId(organizationId);
			allergyPatientMapperDto.setCreatedDate(createdDate);
//			allergyPatientMapperDto.setRoleId(roleId);
			
			return iAllergyPatientMapperService.getCurrentAllergyPatientMapperForCoversheet(allergyPatientMapperDto);
		}catch(Exception e)
		{
			logger.error("getCurrentAllergyPatientMapperForCoversheet failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getPatientAllergyListForIcon", method=RequestMethod.GET)
	public @ResponseBody Response getPatientAllergyListForIcon(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId) {
		try
		{
			AllergyPatientMapperDto allergyPatientMapperDto= new AllergyPatientMapperDto();
			allergyPatientMapperDto.setPatientId(patientId);
			allergyPatientMapperDto.setUnitId(unitId);
			allergyPatientMapperDto.setOrganizationId(organizationId);
			
			return iAllergyPatientMapperService.getPatientAllergyListForIcon(allergyPatientMapperDto);
		}catch(Exception e)
		{
			logger.error("getPatientAllergyListForIcon failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getAllergyPatientMapperForBookView", method=RequestMethod.GET)
	public @ResponseBody Response getAllergyPatientMapperForBookView(@RequestParam(value="patientId") int patientId,
			@RequestParam(value="fromDate") String fromDate,@RequestParam(value="toDate") String toDate,
			@RequestHeader(value="organization_id") int organizationId) {
		try
		{
			AllergyPatientMapperDto allergyPatientMapperDto= new AllergyPatientMapperDto();
			allergyPatientMapperDto.setPatientId(patientId);
			allergyPatientMapperDto.setFromDate(fromDate);
			allergyPatientMapperDto.setToDate(toDate);
			allergyPatientMapperDto.setOrganizationId(organizationId);
			
			return iAllergyPatientMapperService.getAllergyPatientMapperForBookView(allergyPatientMapperDto);
		}catch(Exception e)
		{
			logger.error("getAllergyPatientMapperForBookView failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value = "/saveResolvedAllergyDate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveResolvedAllergyDate(@RequestBody AllergyPatientMapperDto allergyPatientMapperDto) {
		try {
			return iAllergyPatientMapperService.saveResolvedAllergyDate(allergyPatientMapperDto);
		} catch (Exception e) {
			logger.error("saveResolvedAllergyDate failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	
	@RequestMapping(value="/getResolvedAllergyPatientMapperForCoversheet", method=RequestMethod.GET)
	public @ResponseBody Response getResolvedAllergyPatientMapperForCoversheet(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId) {
		try
		{
			AllergyPatientMapperDto allergyPatientMapperDto= new AllergyPatientMapperDto();
			allergyPatientMapperDto.setPatientId(patientId);
			allergyPatientMapperDto.setUnitId(unitId);
			allergyPatientMapperDto.setOrganizationId(organizationId);
			
			return iAllergyPatientMapperService.getResolvedAllergyPatientMapperForCoversheet(allergyPatientMapperDto);
		}catch(Exception e)
		{
			logger.error("getResolvedAllergyPatientMapperForCoversheet failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/getActiveClinicalHeaderTabList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveClinicalHeaderTabList(@RequestBody ClinicalHeaderTabDto clinicalHeaderTabDto) {
		try
		{
			return iAllergyPatientMapperService.getActiveClinicalHeaderTabList(clinicalHeaderTabDto);
		}catch(Exception e)
		{
			logger.error("getActiveClinicalHeaderTabList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/getMappedCoversheetDataForUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getMappedCoversheetDataForUser(@RequestBody ClinicalHeaderTabDto clinicalHeaderTabDto) {
		try
		{
			return iAllergyPatientMapperService.getMappedCoversheetDataForUser(clinicalHeaderTabDto);
		}catch(Exception e)
		{
			logger.error("getMappedCoversheetDataForUser failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getFrequentlyUsedAllergies", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getFrequentlyUsedAllergies(@RequestBody AllergyPatientMapperDto allergyPatientMapperDto) {
		try
		{
			return iAllergyPatientMapperService.getFrequentlyUsedAllergies(allergyPatientMapperDto);
		}catch(Exception e)
		{
			logger.error("getFrequentlyUsedAllergies failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/searchAllergenList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response searchAllergenList(@RequestBody AllergyPatientMapperDto allergyPatientMapperDto) {
		try
		{
			return iAllergyPatientMapperService.searchAllergenList(allergyPatientMapperDto);
		}catch(Exception e)
		{
			logger.error("searchAllergenList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getOldAllergyPatientMapper/getDataForIPDCoversheetByDate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response getDataForIPDCoversheetByDate(@RequestBody AllergyPatientMapperDto dto) {
		try
		{
			return iAllergyPatientMapperService.getAllergyForIPDCoversheetByDate(dto);
		}catch(Exception e)
		{
			logger.error("getDataForIPDCoversheetByDate failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getOldAllergyPatientMapper/getUniqueDateFromAllergyMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response getUniqueDateForIPDCoversheet(@RequestBody AllergyPatientMapperDto dto) {
		try
		{
			return iAllergyPatientMapperService.getUniqueDateForIPDCoversheet(dto);
		}catch(Exception e)
		{
			logger.error("getUniqueDateForIPDCoversheet failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
