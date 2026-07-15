package com.param.opd.coversheet.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.param.global.billing.dto.SelfTriageVitalDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IScheduleLogsDao;
import com.param.global.dto.MyLifeConnectionLinkDto;
import com.param.global.service.IPatientRegistrationService;
import com.param.opd.coversheet.dto.AppointmentVitalMapperDto;
import com.param.opd.coversheet.service.IAppointmentVitalMapperService;


/*FUNCTIONALITY : APPOINTMENT VITAL MAPPER
DATE:13/04/2018
CREATED BY: JYOTI*/
@RestController
@RequestMapping("api/opd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AppointmentVitalMapperController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(AppointmentVitalMapperController.class);

	@Autowired
	private IAppointmentVitalMapperService iAppointmentVitalMapperService;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private Gson gson;
	@Autowired
	private IScheduleLogsDao iScheduleLogsDao;
	@Autowired
	private IPatientRegistrationService iPatientRegistrationService;

	@RequestMapping(value="/saveAppointmentVitalMapper/{unitId}/{organizationId}", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveAppointmentVitalMapper(@PathVariable("unitId") int unitId,
	 	@PathVariable("organizationId") int organizationId,@RequestBody AppointmentVitalMapperDto appointmentVitalMapperDto) {
		try
		{
			appointmentVitalMapperDto.setUnitId(unitId);
			appointmentVitalMapperDto.setOrganizationId(organizationId);
			return iAppointmentVitalMapperService.saveAppointmentVitalMapper(appointmentVitalMapperDto);

		}catch(Exception e)
		{
			logger.error("saveAppointmentVitalMapper failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@RequestMapping(value="/updateAppointmentVitalMapper", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateAppointmentVitalMapper(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestBody AppointmentVitalMapperDto appointmentVitalMapperDto) {
		try
		{
			appointmentVitalMapperDto.setUnitId(unitId);
			appointmentVitalMapperDto.setOrganizationId(organizationId);
			return iAppointmentVitalMapperService.updateAppointmentVitalMapper(appointmentVitalMapperDto);

		}catch(Exception e)
		{
			logger.error("updateAppointmentVitalMapper failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	/*@RequestMapping(value="/getAppointmentVitalMapperByTrand", method=RequestMethod.GET)
	public @ResponseBody Response getAppointmentVitalMapperByTrand(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestHeader(value="patient_id") int patientId,
			@RequestParam(value="FromDate") String fromDate, @RequestParam(value="ToDate") String toDate,
			@RequestParam(value="listOfSelectedVitals") List<Integer> listOfSelectedVitals ,
//			@RequestParam(value="VitalId") int vitalId,
//			@RequestParam(value="encounterId") int encounterId,
			@RequestParam(value="flag") char flag) {
		try
		{
			AppointmentVitalMapperDto appointmentVitalMapperDto= new AppointmentVitalMapperDto();
			appointmentVitalMapperDto.setPatientId(patientId);
			appointmentVitalMapperDto.setUnitId(unitId);
			appointmentVitalMapperDto.setOrganizationId(organizationId);
			appointmentVitalMapperDto.setFromDate(fromDate);
			appointmentVitalMapperDto.setToDate(toDate);
			appointmentVitalMapperDto.setListOfSelectedVitals(listOfSelectedVitals);
//			appointmentVitalMapperDto.setVitalId(vitalId);
//			appointmentVitalMapperDto.setEncounterId(encounterId);
			appointmentVitalMapperDto.setIsAdmissionFlag(flag);
			return iAppointmentVitalMapperService.getListOfAppointmentVitalMapper(appointmentVitalMapperDto);
		}catch(Exception e)
		{

		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}*/

	@RequestMapping(value="/getAppointmentVitalMapperByTrand", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAppointmentVitalMapperByTrand(@RequestBody AppointmentVitalMapperDto appointmentVitalMapperDto) {
		try
		{
			return iAppointmentVitalMapperService.getListOfAppointmentVitalMapper(appointmentVitalMapperDto);
		}catch(Exception e)
		{
			logger.error("getAppointmentVitalMapperByTrand failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getAppointmentVitalMapperByPatientIdAndAppointmentId", method=RequestMethod.GET)
	public @ResponseBody Response listOfAppointmentVitalMapperByPatientId(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestParam(value="patientId") int patientId) {
		try
		{
			AppointmentVitalMapperDto appointmentVitalMapperDto= new AppointmentVitalMapperDto();
			appointmentVitalMapperDto.setUnitId(unitId);
			appointmentVitalMapperDto.setOrganizationId(organizationId);
			appointmentVitalMapperDto.setPatientId(patientId);
			return iAppointmentVitalMapperService.getListOfAppointmentVitalMapperByPatientId(appointmentVitalMapperDto);
		}catch(Exception e)
		{
			logger.error("listOfAppointmentVitalMapperByPatientId failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getOldAppointmentVitalMapper", method=RequestMethod.GET)
	public @ResponseBody Response getOldAppointmentVitalMapper(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestParam(value="patientId") int patientId,@RequestParam(value="roleId") int roleId,@RequestParam(value="encounterId") int encounterId) {
		try
		{
			AppointmentVitalMapperDto appointmentVitalMapperDto= new AppointmentVitalMapperDto();
			appointmentVitalMapperDto.setUnitId(unitId);
			appointmentVitalMapperDto.setOrganizationId(organizationId);
			appointmentVitalMapperDto.setPatientId(patientId);
			appointmentVitalMapperDto.setEncounterId(encounterId);
			appointmentVitalMapperDto.setRoleId(roleId);
			return iAppointmentVitalMapperService.getOldAppointmentVitalMapper(appointmentVitalMapperDto);
		}catch(Exception e)
		{
			logger.error("getOldAppointmentVitalMapper failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getCurrentAppointmentVitalMapper", method=RequestMethod.GET)
	public @ResponseBody Response getCurrentAppointmentVitalMapper(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestParam(value="patientId") int patientId,@RequestParam(value="roleId") int roleId,@RequestParam(value="encounterId") int encounterId,@RequestParam(value="flag") char flag) {
		try
		{
			AppointmentVitalMapperDto appointmentVitalMapperDto= new AppointmentVitalMapperDto();
			appointmentVitalMapperDto.setUnitId(unitId);
			appointmentVitalMapperDto.setOrganizationId(organizationId);
			appointmentVitalMapperDto.setPatientId(patientId);
			appointmentVitalMapperDto.setEncounterId(encounterId);
			appointmentVitalMapperDto.setRoleId(roleId);
			appointmentVitalMapperDto.setIsAdmissionFlag(flag);
			return iAppointmentVitalMapperService.getCurrentAppointmentVitalMapper(appointmentVitalMapperDto);
		}catch(Exception e)
		{
			logger.error("getCurrentAppointmentVitalMapper failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/saveAppointmentVitalMapperExternal", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveAppointmentVitalMapperForAndroid(@RequestBody AppointmentVitalMapperDto appointmentVitalMapperDto) {
		try
		{
			List<AppointmentVitalMapperDto> appointmentVitalMapperDtolist=appointmentVitalMapperDto.getListAppointmentVitalMapperDto();
			return iAppointmentVitalMapperService.saveAppointmentVitalMapperFromAndroid(appointmentVitalMapperDtolist);

		}catch(Exception e)
		{
			logger.error("saveAppointmentVitalMapperForAndroid failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getCurrentAppointmentVitalMapperExternal", method=RequestMethod.POST)
	public @ResponseBody Response getCurrentAppointmentVitalMapper(@RequestBody AppointmentVitalMapperDto appointmentVitalMapperDto) {
		try
		{
			return iAppointmentVitalMapperService.getCurrentAppointmentVitalMapper(appointmentVitalMapperDto);
		}catch(Exception e)
		{
			logger.error("getCurrentAppointmentVitalMapperExternal failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getCurrentAppointmentVitalSelfTriage/{patientId}", method=RequestMethod.GET)
	public @ResponseBody Response getCurrentAppointmentVitalSelfTriage(@PathVariable("patientId") Integer patientId) {
		try
		{
			String uuid = iPatientRegistrationService.getUUIDByPatientId(patientId);
			List<SelfTriageVitalDto> selfTriageVitalDtos = new ArrayList<>();
			if(uuid!=null)
			{
				Response res=iAppointmentVitalMapperService.getConnectionStringForMyLife();
				//Response res=iScheduleLogsDao.getConnectionStringForMyLife();
				MyLifeConnectionLinkDto connectionLinkDto=(MyLifeConnectionLinkDto)res.getObject();
				String MY_LIFE_URI =connectionLinkDto.getMyLifeLink();
				if(MY_LIFE_URI!=null && MY_LIFE_URI.length()>0)
				{
					String stringResponse = null;
					Response commonResponse = null;
					URI uri = URI.create(MY_LIFE_URI + "/patient/HIS/selftrige/vitals/" + uuid);
					ResponseEntity<String> resEntity = restTemplate.exchange(uri, HttpMethod.GET, null, String.class);
					stringResponse = resEntity.getBody();
					commonResponse = gson.fromJson(stringResponse, Response.class);
					selfTriageVitalDtos = commonResponse.getListObject();
					return new Response(SUCCESS, SUCCESS_CODE, null, selfTriageVitalDtos, null);
				}

			}
			return new Response(SUCCESS, SUCCESS_CODE, null, selfTriageVitalDtos, null);
		}catch(Exception e)
		{
			logger.error("getCurrentAppointmentVitalSelfTriage failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getSearchAppointmentVitalSelfTriage", method=RequestMethod.POST)
	public @ResponseBody Response getSearchAppointmentVitalSelfTriage(@RequestBody AppointmentVitalMapperDto vitalDto) {
		try
		{
			String uuid = iPatientRegistrationService.getUUIDByPatientId(vitalDto.getPatientId());
			List<SelfTriageVitalDto> selfTriageVitalDtos = new ArrayList<>();
			if(uuid!=null)
			{
				Response res = iAppointmentVitalMapperService.getConnectionStringForMyLife();
				MyLifeConnectionLinkDto connectionLinkDto = (MyLifeConnectionLinkDto) res.getObject();
				String MY_LIFE_URI = connectionLinkDto.getMyLifeLink();
				if(MY_LIFE_URI!=null && MY_LIFE_URI.length()>0 && uuid!=null)
				{
					vitalDto.setLocalUhid(uuid);
					URI url = null;
					Response commonResponse = null;

					url = URI.create(MY_LIFE_URI + "/patient/selftrige/vitals/search");
					String stringResponse = restTemplate.postForObject(url, vitalDto, String.class);
					commonResponse = gson.fromJson(stringResponse, Response.class);
					selfTriageVitalDtos = commonResponse.getListObject();
					return new Response(SUCCESS, SUCCESS_CODE, null, selfTriageVitalDtos, null);
				}
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, selfTriageVitalDtos, null);

		}catch(Exception e)
		{
			logger.error("getSearchAppointmentVitalSelfTriage failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getIsCriticalByPatientId", method=RequestMethod.POST)
	public @ResponseBody Response getIsCriticalByPatientId(@RequestBody AppointmentVitalMapperDto appointmentVitalMapperDto) {
		try
		{
			return iAppointmentVitalMapperService.getIsCriticalByPatientId(appointmentVitalMapperDto);
		}catch(Exception e)
		{
			logger.error("getIsCriticalByPatientId failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/updateVitalForEnteredInError", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateVitalForEnteredInError(@RequestBody AppointmentVitalMapperDto appointmentVitalMapperDto) {
		try
		{
			return iAppointmentVitalMapperService.updateVitalForEnteredInError(appointmentVitalMapperDto);
		}catch(Exception e)
		{
			logger.error("updateVitalForEnteredInError failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getListOfVitalsByPatientIdForCoversheet", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getListOfVitalsByPatientIdForCoversheet(@RequestBody AppointmentVitalMapperDto appointmentVitalMapperDto) {
		try
		{
			return iAppointmentVitalMapperService.getListOfVitalsByPatientIdForCoversheet(appointmentVitalMapperDto);
		}catch(Exception e)
		{
			logger.error("getListOfVitalsByPatientIdForCoversheet failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getListOfEnteredInErrorVitals", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getListOfEnteredInErrorVitals(@RequestBody AppointmentVitalMapperDto appointmentVitalMapperDto) {
		try
		{
			return iAppointmentVitalMapperService.getListOfEnteredInErrorVitals(appointmentVitalMapperDto);
		}catch(Exception e)
		{
			logger.error("getListOfEnteredInErrorVitals failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getListOfVitalsByPatientIdForBookView", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getListOfVitalsByPatientIdForBookView(@RequestBody AppointmentVitalMapperDto appointmentVitalMapperDto) {
		try
		{
			return iAppointmentVitalMapperService.getListOfVitalsByPatientIdForBookView(appointmentVitalMapperDto);
		}catch(Exception e)
		{
			logger.error("getListOfVitalsByPatientIdForBookView failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getQualifiersListForPopUp", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getQualifiersListForPopUp(@RequestBody AppointmentVitalMapperDto appointmentVitalMapperDto) {
		try
		{
			return iAppointmentVitalMapperService.getQualifiersListForPopUp(appointmentVitalMapperDto);
		}catch(Exception e)
		{
			logger.error("getQualifiersListForPopUp failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
