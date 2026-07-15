package com.param.global.controller.sync;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IPatientRegistrationDao;
import com.param.global.dao.IScheduleLogsDao;
import com.param.global.dto.MyLifeConnectionLinkDto;
import com.param.global.dto.PatientRegistrationDto;
import com.param.global.dto.sync.PatientMasterDto;
import com.param.global.dto.sync.PatientSyncDto;
import com.param.global.dto.sync.ScheduleAppointmentReqDto;
import com.param.global.service.IPatientRegistrationService;
import com.param.global.service.sync.IAppointmentService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("/api/mylife/sync")
@SuppressWarnings({"rawtypes"})
public class SyncFromMyLifeController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(SyncFromMyLifeController.class);


	@Autowired
	private IAppointmentService iAppointmentService;
	
	@Autowired
	private IPatientRegistrationService iPatientRegistrationService;
	
	@Autowired
	private IPatientRegistrationDao iPatientMasterDao;

	@Autowired
	private IScheduleLogsDao iScheduleLogsDao;
	
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	private Gson gson;
	
	@RequestMapping(value = "/appointment" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response confirmAppointment(@RequestBody ScheduleAppointmentReqDto reqDto){
		try{
			return iAppointmentService.confirmAppointment(reqDto);
		}catch(Exception e){
		logger.error("confirmAppointment failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getCause() +":"+ e.getMessage() , null, null);
		}
	}
	
	@RequestMapping(value = "/appointment/cancelorRescheduleAppointmentFromApp" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response cancelorRescheduleAppointment(@RequestBody ScheduleAppointmentReqDto reqDto){
		try{
			return iAppointmentService.cancelorRescheduleAppointment(reqDto);
		}catch(Exception e){
		logger.error("cancelorRescheduleAppointment failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getCause() +":"+ e.getMessage() , null, null);
		}
	}
	
	@RequestMapping(value = "/savePatientThroughMylife", method = RequestMethod.POST)
	public @ResponseBody Response savePatientThroughMylife(@RequestBody PatientMasterDto patientMasterDto){
		try
		{
			Response commonResponse = null;
			Response response = null;
			URI url = null;
			String uuid = "";
			String mobileNo = "";
			Integer patientId=null;
			PatientRegistrationDto patientRegistrationDto= new PatientRegistrationDto();
			patientRegistrationDto.setIsPreReg('N');
			patientRegistrationDto.setIsPrimary('Y');
			patientRegistrationDto.setIsOtcReg('N');
			patientRegistrationDto.setRegistrationTypeId(5);
			patientRegistrationDto.setIsUnknownReg('N');
			patientRegistrationDto.setGenderId(patientMasterDto.getGenderId());
			patientRegistrationDto.setGenderCode(patientMasterDto.getGender());
			patientRegistrationDto.setFirstName(patientMasterDto.getFirstName());
			patientRegistrationDto.setMiddleName(patientMasterDto.getMiddleName() != null ? patientMasterDto.getMiddleName() : "");
			patientRegistrationDto.setLastName(patientMasterDto.getLastName() != null ? patientMasterDto.getLastName() : "");
			patientRegistrationDto.setDob(patientMasterDto.getDateOfBirth()+" 00:00:00");
			patientRegistrationDto.setOrganizationId(patientMasterDto.getHospitalId());
			patientRegistrationDto.setUnitId(patientMasterDto.getUnitId());
			if(patientMasterDto.getMobileNumber()!=null)
			patientRegistrationDto.setMobileNumber(patientMasterDto.getMobileNumber());
			
			if(patientMasterDto.getEmailId()!=null)
			patientRegistrationDto.setEmail(patientMasterDto.getEmailId());
			
			Response patRes = iPatientRegistrationService.registerPatient(patientRegistrationDto);
		
			if(patRes.getStatus().equalsIgnoreCase(SUCCESS)){
				patientId = patientRegistrationDto.getPatientId();
				Response res=iScheduleLogsDao.getConnectionStringForMyLife();
				MyLifeConnectionLinkDto connectionLinkDto=(MyLifeConnectionLinkDto)res.getObject();
				String Global_Scheduling_URI = connectionLinkDto.getGlobalSchedulingLink();
				String MY_LIFE_URI =connectionLinkDto.getMyLifeLink();

				response = iPatientMasterDao.getPatientByIdForSync(patientId);
				PatientSyncDto patient = (PatientSyncDto)response.getObject();
				List<PatientSyncDto> listPatientSyncDto = new ArrayList<PatientSyncDto>();
				listPatientSyncDto.add(patient);
//				ObjectMapper mapper = new ObjectMapper();
//				url = URI.create(MY_LIFE_URI + "/sync/patients/realtime");
//				url = URI.create("https://e478233d9631.ngrok-free.app/MyLifeGlobal/api" + "/sync/patients/realtime");
//				String stringResponse = restTemplate.postForObject(url, patient, String.class);
//				commonResponse = gson.fromJson(stringResponse, Response.class);
//				if(commonResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS)){
//					if(commonResponse.getMessage() != null && !commonResponse.getMessage().trim().isEmpty()) {
						uuid = (String) patientMasterDto.getUuid();
						iPatientMasterDao.updateUuidByPatient(patientId, uuid);
						return new Response(SUCCESS, SUCCESS_CODE, uuid, listPatientSyncDto, mobileNo);
//					}
//				}
			}else {
				return patRes;
			}
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, ERROR_CODE, null, null, null);
	}

}
