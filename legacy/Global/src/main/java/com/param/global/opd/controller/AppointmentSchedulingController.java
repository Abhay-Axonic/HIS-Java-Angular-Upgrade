package com.param.global.opd.controller;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.List;

import com.param.global.dto.global.AIAppointmentQueueDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.param.global.common.AppointmentStatusMasterConstant;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.ISmsUtilityConstants;
import com.param.global.common.Response;
import com.param.global.common.SmsUtility;
import com.param.global.dao.IScheduleLogsDao;
import com.param.global.dto.MyLifeConnectionLinkDto;
import com.param.global.dto.PatientRegistrationDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.dto.TemplateTransactionDto;
import com.param.global.dto.sync.AppointmentSyncSchedulingDto;
import com.param.global.opd.dao.IAppointmentMasterDao;
import com.param.global.opd.service.IAppointmentSchedulingService;
import com.param.global.service.ITemplateTransactionService;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.service.ISmsUtilityConfigService;
import com.param.opd.appointment.model.AppointmentMaster;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.utility.TimeZoneComponent;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("api/opd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AppointmentSchedulingController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(AppointmentSchedulingController.class);

	
	@Autowired
	IAppointmentSchedulingService iAppointmentSchedulingService;
	
	@Autowired
	ITemplateTransactionService iTemplateTransactionService;
	
	@Autowired
	private ISmsUtilityConfigService smsUtilityService;
	
	@Autowired
	private IAppointmentMasterDao iAppointmentMasterDao;
	
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	private Gson gson;
	
	@Autowired
	IScheduleLogsDao iScheduleLogsDao;
	
	@RequestMapping(value="/scheduleAppointment", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public synchronized @ResponseBody Response scheduleAppointment(@RequestBody SlotMasterDto slotMasterDto) {
		try {

			Response res = null;
			if(slotMasterDto.getIsFromKioskToken()!=null)
			{
				if(slotMasterDto.getIsBlocked()!=null && slotMasterDto.getSlotId()!=null)
				{
					return iAppointmentSchedulingService.blockSlot(slotMasterDto.getSlotId(),4);
				}
				res =  iAppointmentSchedulingService.generateAppointment(slotMasterDto);
			}else{
				res =  iAppointmentSchedulingService.scheduleAppointment(slotMasterDto);
			}


			if(res.getObject()!= null) {
				
				AppointmentMaster appointmentMaster = null;
				appointmentMaster = (AppointmentMaster)res.getObject();
				
				//Send Template
				TemplateTransactionDto dto = new TemplateTransactionDto();
				dto.setSpecialityId(slotMasterDto.getSpecialityId());
				dto.setTemplateTypeId(10);//1.Questionnarire
				dto.setPatientId(slotMasterDto.getPatientId());
				dto.setAppointmentDate(slotMasterDto.getAppointmentDate2());
				dto.setAppointmentId(appointmentMaster.getAppointmentId());
				dto.setTimeSlot(slotMasterDto.getFromTimeString());
				dto.setDoctorId(slotMasterDto.getDoctorId());
				
				if(slotMasterDto.getDoctorId()!=null)
				dto.setDoctorName(slotMasterDto.getDoctorName());
				else
				dto.setSpecialityName(slotMasterDto.getSpecialityName());
				
				dto.setOrgId(slotMasterDto.getOrganizationId());
				dto.setUnitId(slotMasterDto.getUnitId());
				iTemplateTransactionService.sendTemplateURLS(dto);
				
				
				if(res.getStatus().equals(SUCCESS) && slotMasterDto.getDoctorId()!=null){
					SmsUtilityConfigInfoDto smsInfo=new SmsUtilityConfigInfoDto();
						smsInfo.setOrganizationId(slotMasterDto.getOrganizationId());
						smsInfo.setUnitId(slotMasterDto.getUnitId());
					smsInfo.setDateTime(slotMasterDto.getAppointmentDate2() != null
							? (slotMasterDto.getAppointmentDate2().substring(0, 10) + " at "
									+ slotMasterDto.getFromTimeString())
							: (slotMasterDto.getAppointmentDate().substring(0, 10) + " at "
									+ slotMasterDto.getFromTimeString()));
						smsInfo.setPatientId(slotMasterDto.getPatientId());
						smsInfo.setConsultingDoctorId(slotMasterDto.getDoctorId());
						smsInfo.setAppointmentId(appointmentMaster.getAppointmentId());
						smsInfo.setAppointmentUniqueId(appointmentMaster.getAppointmentUniqueId() != null ? appointmentMaster.getAppointmentUniqueId() : null);
					smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.APPOITMENT,smsInfo);
					smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.APPOITMENT,smsInfo);
					
				}
				
				if(slotMasterDto!= null) {
				if(slotMasterDto.isSyncScheduling() == true) {
					
					Response response = iAppointmentMasterDao.getDoctorAppointmentToSync(appointmentMaster.getAppointmentId());
					AppointmentSyncSchedulingDto appointment = (AppointmentSyncSchedulingDto)response.getObject();
					if(appointment != null){
						URI url = null;
							appointment.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(0), "dd-M-yyyy"));
							appointment.setApplicationFlag(true);
							String stringResponse = null;
							if(appointment.getDirectEncounter()==null) {
								appointment.setDirectEncounter('N');
							}
							appointment.setAppointmentStatusId(appointmentMaster.getAppointmentStatusId());
							
							if(appointmentMaster.getAppointmentStatusId()==8) {
								appointmentMaster.setAppointmentStatusId(AppointmentStatusMasterConstant.PENDING);
							}
							//String appointmentJson=new Gson().toJson(appointment);
							if(appointment.getAppointmentStatusId()==AppointmentStatusMasterConstant.PENDING || 
									(appointment.getDirectEncounter().equals('Y') && appointmentMaster.getAppointmentStatusId()==APPOINTMENTSTATUSID1)){
								
								String Global_Scheduling_URI = null;
								String MY_LIFE_URI = null;
								Response resp=iScheduleLogsDao.getConnectionStringForMyLife();
								MyLifeConnectionLinkDto connectionLinkDto=(MyLifeConnectionLinkDto)resp.getObject();
								if(connectionLinkDto!=null && connectionLinkDto.getMyLifeLink()!=null && connectionLinkDto.getMyLifeLink()!="" && connectionLinkDto.getGlobalSchedulingLink()!=null && connectionLinkDto.getGlobalSchedulingLink()!="")
								{
									Global_Scheduling_URI = connectionLinkDto.getGlobalSchedulingLink();
									MY_LIFE_URI = connectionLinkDto.getMyLifeLink(); 
								}
								
								url = URI.create(Global_Scheduling_URI + "/appointment/bookAppointment");
								ObjectMapper objectMapper = new ObjectMapper();
						        String json = objectMapper.writeValueAsString(appointment);
						        System.out.println("Appointment SChedule JSON is:"+json);	
								HttpHeaders headers = new HttpHeaders();
								String token = SmsUtility.generateJWTTokenMylife(connectionLinkDto.getGlobalSchedulingLink());
								headers.set("Authorization", "Bearer " + token);
								headers.set("userName", SmsUtility.USER_TOKEN_MYLIFE);
								headers.set("channelId", (appointment.getChannelId() != null && appointment.getChannelId() != 0) ? appointment.getChannelId().toString():"1");
								//headers.set("channelId", "34");
								HttpEntity<AppointmentSyncSchedulingDto> request = new HttpEntity<>(appointment, headers);
							stringResponse = restTemplate.postForObject(url, request, String.class);
							Response commonResponse = null;
							commonResponse = gson.fromJson(stringResponse, Response.class);
							if(commonResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS)) {
								System.out.println("Appointment Booked Successfully");
							}else {
								System.out.println("Appointment Booking Failed");
							}
							}
				}
				
				}
			}
				
			}
			return res;
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/appointment/kiosk", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public synchronized @ResponseBody Response generateAppointment(@RequestBody SlotMasterDto slotMasterDto) {
		try {
			return iAppointmentSchedulingService.generateAppointment(slotMasterDto);
			
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/scheduleAppointmentList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response scheduledAppointmentList(@RequestBody SlotMasterDto slotMasterDto) {
		try {
				return iAppointmentSchedulingService.scheduledAppointmentList(slotMasterDto);
			
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/blockedScheduleAppointmentList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response blockedScheduleAppointmentList(@RequestBody SlotMasterDto slotMasterDto) {
		try {
				return iAppointmentSchedulingService.blockedScheduleAppointmentList(slotMasterDto);
			
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/searchAppointmentsByCriteria", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response searchAppointmentsByCriteria(@RequestBody SlotMasterDto slotMasterDto) {
		try {
				return iAppointmentSchedulingService.searchAppointmentsByCriteria(slotMasterDto);
			
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/cancelScheduledAppointment", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response cancelScheduledAppointment(@RequestBody SlotMasterDto slotMasterDto) {
		try {
				return iAppointmentSchedulingService.cancelScheduledAppointment(slotMasterDto);
			
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/rescheduleAppointment", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response rescheduleAppointment(@RequestBody SlotMasterDto slotMasterDto) {
		try {
				return iAppointmentSchedulingService.rescheduleAppointment(slotMasterDto);
			
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/updateAppointmentStatus", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateAppointmentStatus(@RequestBody SlotMasterDto slotMasterDto) {
		try {
				return iAppointmentSchedulingService.updateAppointmentStatus(slotMasterDto);
			
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/isAppointmentForSameDateTimeExist", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Boolean isAppointmentForSameDateTimeExist(@RequestBody SlotMasterDto slotMasterDto) {
		try {
				return iAppointmentSchedulingService.isAppointmentForSameDateTimeExist(slotMasterDto);

		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return false;
	}
	
	@RequestMapping(value="/scheduleRadiologyAppointmentForInPatient", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response scheduleRadiologyAppointmentForInPatient(@RequestBody SlotMasterDto slotMasterDto) {
		try {
				return iAppointmentSchedulingService.scheduleRadiologyAppointmentForInPatient(slotMasterDto);
			
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/markArrival/{appointmentId}/{currentDate}/{unitId}", method=RequestMethod.GET)
	public synchronized @ResponseBody Response markArrival(@PathVariable("appointmentId") Integer appointmentId,
			@PathVariable("currentDate") String currentDate,@PathVariable("unitId") Integer unitId) {
		try {
				return iAppointmentSchedulingService.markArrival(appointmentId,currentDate,unitId);
			
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/markArrival/paidAppointment", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public synchronized @ResponseBody Response markArrivalForPaidAppointment(@RequestBody EncounterMasterDto encounterMasterDto) {
		try {
				return iAppointmentSchedulingService.markArrivalForPaidAppointment(encounterMasterDto);
			
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/paidAppointment/afterConversion", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public synchronized @ResponseBody Response afterConversionPaidAppointment(@RequestBody EncounterMasterDto encounterMasterDto) {
		try {
				return iAppointmentSchedulingService.afterConversionPaidAppointment(encounterMasterDto);
			
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/assignCounterId", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response assignCounterId(@RequestBody SlotMasterDto slotMasterDto) {
		try {
				return iAppointmentSchedulingService.assignCounterNumber(slotMasterDto);
			
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/searchInProcessAppointment/{appointmentId}", method=RequestMethod.GET)
	public @ResponseBody Response searchInProcessAppointment(@PathVariable("appointmentId") Integer appointmentId) {
		try {
				return iAppointmentSchedulingService.searchInProcessAppointment(appointmentId);
			
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/updateCallCount/{appointmentId}/{callCount}", method=RequestMethod.GET)
	public @ResponseBody Response updateCallCount(@PathVariable("appointmentId") Integer appointmentId,@PathVariable("callCount") Integer callCount) {
		try {
				return iAppointmentSchedulingService.updateCallCount(appointmentId,callCount);
			
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/updateToken/{appointmentId}/{tokenNumber}", method=RequestMethod.GET)
	public @ResponseBody Response updateToken(@PathVariable("appointmentId") Integer appointmentId,@PathVariable("tokenNumber") Integer callCount) {
		try {
				return iAppointmentSchedulingService.updateToken(appointmentId,callCount);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/assignPatient/{appointmentId}/{patientId}", method=RequestMethod.GET)
	public @ResponseBody Response assignPatientToAppointmentFromPatientRegistration(@PathVariable("appointmentId") Integer appointmentId,@PathVariable("patientId") Integer patientId) {
		try {
				return iAppointmentSchedulingService.assignPatientToAppointmentFromPatientRegistration(appointmentId,patientId);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/updateTokenNursing/{encounterId}/{tokenNumber}/{orderDetailsId}", method=RequestMethod.GET)
	public @ResponseBody Response updateTokenNursing(@PathVariable("encounterId") Integer encounterId,@PathVariable("tokenNumber") Integer callCount,@PathVariable("orderDetailsId") Integer orderDetailsId) {
		try {
				return iAppointmentSchedulingService.updateTokenNursing(encounterId,callCount,orderDetailsId);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/appointment/check", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateTokenNursing(@RequestBody SlotMasterDto slotMasterDto) {
		try {
				return iAppointmentSchedulingService.isAppointmentAlreadyExist(slotMasterDto);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/block/slot/{statusId}/{slotId}", method=RequestMethod.GET)
	public synchronized @ResponseBody Response blockSlot(@PathVariable("slotId") Integer slotId,@PathVariable("statusId") Integer statusId) {
		try {
				return iAppointmentSchedulingService.blockSlot(slotId,statusId);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/searchAppointmentsByPatienId", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response searchAppointmentsByPatienId(@RequestBody SlotMasterDto slotMasterDto) {
		try {
				return iAppointmentSchedulingService.searchAppointmentsByPatienId(slotMasterDto);
			
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/searchCyclicServiceAppointmentsByPatienId", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response searchCyclicServiceAppointmentsByPatienId(@RequestBody SlotMasterDto slotMasterDto) {
		try {
				return iAppointmentSchedulingService.searchCyclicServiceAppointmentsByPatienId(slotMasterDto);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/cyclicAppointmentScheduling", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response cyclicAppointmentScheduling(@RequestBody List<SlotMasterDto> slotMasterDto) {
		try {
			
			for(SlotMasterDto dto:slotMasterDto) {
				 Response res= iAppointmentSchedulingService.generateAppointment(dto);
				/* if (res.getStatus().equalsIgnoreCase(SUCCESS) && res.getResObject()!=null) 
					{
					 SmsUtilityConfigInfoDto smsInfo = new SmsUtilityConfigInfoDto();
						smsInfo.setUnitId(dto.getUnitId());
						smsInfo.setPatientId(dto.getPatientId());
						smsInfo.setConsultingDoctorId(dto.getDoctorId());
						smsInfo.setTreatingDoctorId(dto.getDoctorId());
						smsInfo.setTestDesc(dto.getServiceStandardName());
						smsInfo.setCyclicOrderDate(dto.getAppointmentDate());
						smsInfo.setReferringDoctorId(null);
						smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.CYCLIC_ORDER, smsInfo);
						
						smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.CYCLIC_ORDER,smsInfo);

					 
					}*/
			}
			
			/*for(SlotMasterDto dto:slotMasterDto)
			  iAppointmentSchedulingService.generateAppointment(dto);*/
				//return iAppointmentSchedulingService.rescheduleAppointment(slotMasterDto);
		    return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/rescheduleCyclicAppointment", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response rescheduleCyclicAppointment(@RequestBody List<SlotMasterDto> slotMasterDto) {
		try {
			for(SlotMasterDto dto:slotMasterDto)
			 iAppointmentSchedulingService.rescheduleAppointment(dto);
		    return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/generateCyclicServiceAppointment", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response generateCyclicServiceAppointment(@RequestBody List<SlotMasterDto> slotMasterDto) {
		try {
			return iAppointmentSchedulingService.generateCyclicServiceAppointment(slotMasterDto);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/rescheduleCyclicServiceAppointment", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response rescheduleCyclicServiceAppointment(@RequestBody List<SlotMasterDto> slotMasterDto) {
		try {
			
			
			for(SlotMasterDto dto:slotMasterDto) {
				Response res=  iAppointmentSchedulingService.rescheduleCyclicServiceAppointment(dto);
				if (res.getStatus().equals(SUCCESS)) {
					Long d = new Date().getTime();
					Integer uniqueId = Integer.valueOf(d.intValue());
						SmsUtilityConfigInfoDto smsInfo = new SmsUtilityConfigInfoDto();
						smsInfo.setUnitId(dto.getUnitId());
						smsInfo.setOrganizationId(dto.getOrganizationId());
						smsInfo.setPatientId(dto.getPatientId());
						smsInfo.setConsultingDoctorId(dto.getDoctorId());
						smsInfo.setTreatingDoctorId(dto.getDoctorId());
						smsInfo.setTestDesc(dto.getServiceStandardName());
						smsInfo.setCyclicOrderDate(dto.getCyclicOrdScheduledDate()+" "+dto.getAppointmentTime()+":00");
						smsInfo.setUniqueId((dto.getEncounterId()!=null ? dto.getEncounterId() : (dto.getAdmissionId()!= null ? dto.getAdmissionId() : uniqueId)).toString());
						smsInfo.setReferringDoctorId(null);
						smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.CYCLIC_ORDER, smsInfo);

						smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.CYCLIC_ORDER, smsInfo);

					}
					
			}
		    return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getScheduledServiceAppointmentWorklist", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getScheduledServiceAppointmentList(@RequestBody SlotMasterDto slotMasterDto) {
		try {
			 return iAppointmentSchedulingService.getScheduledServiceAppointmentList(slotMasterDto);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getDeptWiseServiceListForCyclicOrder", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDeptWiseServiceListForCyclicOrder(@RequestBody SlotMasterDto slotMasterDto) {
		try {
			 return iAppointmentSchedulingService.getDeptWiseServiceListForCyclicOrder(slotMasterDto);
			}catch (Exception e) {
			logger.error("method failed", e);
			}
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	
	@RequestMapping(value="/getCyclicDocWiseAndDeptWiseAppointmentListByPatientId/{patientId}/{orgId}", method=RequestMethod.GET)
	public @ResponseBody Response getCyclicDocWiseAndDeptWiseAppointmentListByPatientId(@PathVariable("patientId") Integer patientId,@PathVariable("orgId") Integer oranizationId) {
		try {
			 return iAppointmentSchedulingService.getCyclicDocWiseAndDeptWiseAppointmentListByPatientId(patientId,oranizationId);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getServiceListForCyclicOrder", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getServiceListForCyclicOrder(@RequestBody SlotMasterDto slotMasterDto) {
		try {
			 return iAppointmentSchedulingService.getServiceListForCyclicOrder(slotMasterDto);
			}catch (Exception e) {
			logger.error("method failed", e);
			}
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	
	@RequestMapping(value = "/printSavedTemplate", method = RequestMethod.POST)
	public Response printSavedTemplate(@RequestBody TemplateTransactionDto dto) throws IOException {
		try {
			 return iTemplateTransactionService.printSavedTemplate(dto);
			}catch (Exception e) {
			logger.error("printSavedTemplate failed", e);
			}
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	
	@RequestMapping(value="/savePatientDetailsFromWebApp", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response savePatientDetailsFromWebApp(@RequestBody EncounterMasterDto encounterMasterDto) {
		try {
				return iAppointmentSchedulingService.savePatientDetailsFromWebApp(encounterMasterDto);
			
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

    @RequestMapping(value="/getAppointMentDetailsByPatientId", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response getAppointMentDetailsByPatientId(@RequestBody SlotMasterDto slotMasterDto) {
        try {
             return iAppointmentSchedulingService.getAppointMentDetailsByPatientId(slotMasterDto);
            }catch (Exception e) {
            logger.error("method failed", e);
            }
            return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
        }
	
	@RequestMapping(value="/webAndMobileApp/savePatientDetailsFromWebAndMobileApp", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response savePatientDetailsFromWebAndMobileApp(@RequestBody PatientRegistrationDto patientRegistrationDto) {
		try {
				return iAppointmentSchedulingService.savePatientDetailsFromWebAndMobileApp(patientRegistrationDto);
			
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/scheduleAppointmentForWebAppointment", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public synchronized @ResponseBody Response scheduleAppointmentForWebAppointment(@RequestBody SlotMasterDto slotMasterDto) {
		try {
			Response res =  iAppointmentSchedulingService.scheduleAppointmentForWebAppointment(slotMasterDto);
			return res;
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value="/prnConfiguration", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public synchronized @ResponseBody Response prnConfiguration(@RequestBody SlotMasterDto slotMasterDto) {
		try {
			Response res =  iAppointmentSchedulingService.prnConfiguration(slotMasterDto);
			return res;
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/searchInProcessAppointmentWeb/{appointmentId}", method=RequestMethod.GET)
	public @ResponseBody Response searchInProcessAppointmentWeb(@PathVariable("appointmentId") Integer appointmentId) {
		try {
				return iAppointmentSchedulingService.searchInProcessAppointmentWeb(appointmentId);
			
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/searchFutureAppointmentsByPatientId", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response searchFutureAppointmentsByPatientId(@RequestBody SlotMasterDto slotMasterDto) {
		try {
				return iAppointmentSchedulingService.searchFutureAppointmentsByPatientId(slotMasterDto);
			
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/saveAIAppointmentInQueue", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveAIAppointmentInQueue(@RequestBody AIAppointmentQueueDto dto) {
		try {
			return iAppointmentSchedulingService.saveAIAppointmentInQueue(dto);
		} catch (Exception e) {
		logger.error("method failed", e);
			return new Response(ERROR, ERROR_CODE, null, null,null);
		}
	}


	@RequestMapping(value = "/getAIAppointmentCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAIAppointmentCount(@RequestBody AIAppointmentQueueDto dto) {
		try {
			return iAppointmentSchedulingService.getAIAppointmentCount(dto);
		} catch (Exception e) {
		logger.error("method failed", e);
			return new Response(ERROR, ERROR_CODE, null, null,null);
		}
	}


	@RequestMapping(value = "/getAISummaryData", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAISummaryData(@RequestBody AIAppointmentQueueDto dto) {
		try {
			return iAppointmentSchedulingService.getAISummaryData(dto);
		} catch (Exception e) {
		logger.error("method failed", e);
			return new Response(ERROR, ERROR_CODE, null, null,null);
		}
	}
	
	

}
