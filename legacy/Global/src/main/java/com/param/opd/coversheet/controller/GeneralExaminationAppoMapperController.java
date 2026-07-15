package com.param.opd.coversheet.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.opd.coversheet.dto.GeneralExaminationAppoMapperDto;
import com.param.opd.coversheet.service.IGeneralExaminationAppoMapperService;

/*FUNCTIONALITY : GENERAL EXAMI
DATE:17/04/2018
CREATED BY: JYOTI*/
@RestController
@RequestMapping("api/opd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class GeneralExaminationAppoMapperController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(GeneralExaminationAppoMapperController.class);

	@Autowired
	private IGeneralExaminationAppoMapperService iGeneralExaminationAppoMapperService;

	@RequestMapping(value="/saveGeneralExaminationAppoMapper", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response savePatientHabitDetails(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestBody GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto) {
		try
		{
			generalExaminationAppoMapperDto.setUnitId(unitId);
			generalExaminationAppoMapperDto.setOrganizationId(organizationId);

			Response res =  iGeneralExaminationAppoMapperService.saveGeneralExaminationMapper(generalExaminationAppoMapperDto);
			if(res.getObject()!=null && res.getObject()!="") {
				Integer result = (Integer)res.getObject();
					if(result>0) {
						return iGeneralExaminationAppoMapperService.saveGeneralExaminationMapper(generalExaminationAppoMapperDto);
					}
			}else {
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
			}

		}catch(Exception e)
		{
			logger.error("savePatientHabitDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getGeneralExaminationAppoMapper", method=RequestMethod.GET)
	public @ResponseBody Response listOfGeneralExaminationAppoMapper(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestParam(value="typeId") int typeId) {
		try
		{
			GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto= new GeneralExaminationAppoMapperDto();
			generalExaminationAppoMapperDto.setPatientId(patientId);
			generalExaminationAppoMapperDto.setUnitId(unitId);
			generalExaminationAppoMapperDto.setOrganizationId(organizationId);
			generalExaminationAppoMapperDto.setTypeId(typeId);
			return iGeneralExaminationAppoMapperService.getListOfGeneralExaminationMapper(generalExaminationAppoMapperDto);
		}catch(Exception e)
		{
			logger.error("listOfGeneralExaminationAppoMapper failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@RequestMapping(value="/getSavedGeneralExaminationAppoMapper", method=RequestMethod.GET)
	public @ResponseBody Response listOfSavedGeneralExaminationAppoMapper(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestParam(value="typeId") int typeId,@RequestParam(value="genderId") int genderId) {
		try
		{
			GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto= new GeneralExaminationAppoMapperDto();
			generalExaminationAppoMapperDto.setPatientId(patientId);
			generalExaminationAppoMapperDto.setUnitId(unitId);
			generalExaminationAppoMapperDto.setOrganizationId(organizationId);
			generalExaminationAppoMapperDto.setTypeId(typeId);
			generalExaminationAppoMapperDto.setGenderId(genderId);
			return iGeneralExaminationAppoMapperService.getListOfSavedGeneralExaminationMapper(generalExaminationAppoMapperDto);
		}catch(Exception e)
		{
			logger.error("listOfSavedGeneralExaminationAppoMapper failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getListOfSystemsAndObservation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getListOfSystemsAndObservation(@RequestBody GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto) {
		try {
			return iGeneralExaminationAppoMapperService.getListOfSystemsAndObservation(generalExaminationAppoMapperDto);
		} catch (Exception e) {
			logger.error("getListOfSystemsAndObservation failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getListOfSystemsAndObservationForCaseSheet", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getListOfSystemsAndObservationForCaseSheet(@RequestBody GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto) {
		try {
			return iGeneralExaminationAppoMapperService.getListOfSystemsAndObservationForCaseSheet(generalExaminationAppoMapperDto);
		} catch (Exception e) {
			logger.error("getListOfSystemsAndObservationForCaseSheet failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getListOfSystemsAndObservationForBookView", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getListOfSystemsAndObservationForBookView(@RequestBody GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto) {
		try {
			return iGeneralExaminationAppoMapperService.getListOfSystemsAndObservationForBookView(generalExaminationAppoMapperDto);
		} catch (Exception e) {
			logger.error("getListOfSystemsAndObservationForBookView failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getGeneralAndSystemicExaminationMapperList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getGeneralAndSystemicExaminationMapperList(@RequestBody GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto) {
		try
		{
			return iGeneralExaminationAppoMapperService.getGeneralAndSystemicExaminationMapperList(generalExaminationAppoMapperDto);
		}catch(Exception e)
		{
			logger.error("getGeneralAndSystemicExaminationMapperList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/saveGeneralAndSystemicExaminationMapperForNewCoversheet", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveGeneralAndSystemicExaminationMapperForNewCoversheet(@RequestBody GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto) {
		try
		{
			return iGeneralExaminationAppoMapperService.saveGeneralAndSystemicExaminationMapperForNewCoversheet(generalExaminationAppoMapperDto);
		}catch(Exception e)
		{
			logger.error("saveGeneralAndSystemicExaminationMapperForNewCoversheet failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getGenAndSysExaminMapperPatDetailstForNewCov", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getGenAndSysExaminMapperPatDetailstForNewCov(@RequestBody GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto) {
		try
		{
			return iGeneralExaminationAppoMapperService.getGenAndSysExaminMapperPatDetailstForNewCov(generalExaminationAppoMapperDto);
		}catch(Exception e)
		{
			logger.error("getGenAndSysExaminMapperPatDetailstForNewCov failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/removeGenAndSysExaminMapperPatDetailstForNewCov", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response removeGenAndSysExaminMapperPatDetailstForNewCov(@RequestBody GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto) {
		try
		{
			return iGeneralExaminationAppoMapperService.removeGenAndSysExaminMapperPatDetailstForNewCov(generalExaminationAppoMapperDto);
		}catch(Exception e)
		{
			logger.error("removeGenAndSysExaminMapperPatDetailstForNewCov failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/updateDescriptiveExamForNewCov", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateDescriptiveExamForNewCov(@RequestBody GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto) {
		try
		{
			return iGeneralExaminationAppoMapperService.updateDescriptiveExamForNewCov(generalExaminationAppoMapperDto);
		}catch(Exception e)
		{
			logger.error("updateDescriptiveExamForNewCov failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getExaminationAppoMapperHistory", method=RequestMethod.GET)
	public @ResponseBody Response getExaminationAppoMapperHistory(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestParam(value="typeId") int typeId) {
		try
		{
			GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto= new GeneralExaminationAppoMapperDto();
			generalExaminationAppoMapperDto.setPatientId(patientId);
			generalExaminationAppoMapperDto.setUnitId(unitId);
			generalExaminationAppoMapperDto.setOrganizationId(organizationId);
			generalExaminationAppoMapperDto.setTypeId(typeId);
			return iGeneralExaminationAppoMapperService.getExaminationAppoMapperHistory(generalExaminationAppoMapperDto);
		}catch(Exception e)
		{
			logger.error("getExaminationAppoMapperHistory failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getExaminationAppoMapperHistoryAdmission", method=RequestMethod.GET)
	public @ResponseBody Response getExaminationAppoMapperHistoryAdmission(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestParam(value="typeId") int typeId) {
		try
		{
			GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto= new GeneralExaminationAppoMapperDto();
			generalExaminationAppoMapperDto.setPatientId(patientId);
			generalExaminationAppoMapperDto.setUnitId(unitId);
			generalExaminationAppoMapperDto.setOrganizationId(organizationId);
			generalExaminationAppoMapperDto.setTypeId(typeId);
			return iGeneralExaminationAppoMapperService.getExaminationAppoMapperHistoryAdmission(generalExaminationAppoMapperDto);
		}catch(Exception e)
		{
			logger.error("getExaminationAppoMapperHistoryAdmission failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
