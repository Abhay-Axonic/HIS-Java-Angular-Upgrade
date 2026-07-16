  package com.param.global.opd.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.param.global.billing.service.IContractConsumptionService;
import com.param.global.common.*;
import com.param.global.dto.*;
import com.param.global.dto.sync.PatientMasterDto;
import com.param.global.opd.dao.IAppointmentSchedulingDao;
import com.param.global.pharmacy.service.IWalkInSalesService;
import com.param.global.service.*;
import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.param.global.billing.service.IBillingService;
import com.param.global.dao.IPatientRegistrationDao;
import com.param.global.dao.IScheduleLogsDao;
import com.param.global.dto.sync.AppointmentSyncSchedulingDto;
import com.param.global.dto.sync.PatientSyncDto;
import com.param.global.dto.sync.ScheduleAppointmentReqDto;
import com.param.global.opd.dao.IAppointmentMasterDao;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.procedure.service.IBillOfMaterialService;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.service.ISmsUtilityConfigService;
import com.param.opd.appointment.dto.AppointmentStatusSyncDto;
import com.param.opd.appointment.dto.HemodialysisAppointmentMapperDto;
import com.param.opd.encounter.dto.AppointmentStatusMasterDto;
import com.param.opd.encounter.dto.EncounterDocDetailsDto;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.opd.encounter.dto.PatientDocReqDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
// import com.param.global.common.JsonCheck;

@RestController
@RequestMapping("api/opd")
@SuppressWarnings({"rawtypes","unchecked"})
public class EncounterController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(EncounterController.class);

	
	@Autowired
	private IEncounterMasterService iEncounterMasterService;
	
	@Autowired
	private ISmsUtilityConfigService smsUtilityService;
	
	@Autowired
	IBillingService globalBillingService;
	
	@Autowired
	IBillOfMaterialService iBillOfMaterialService;
	
	@Autowired
	IMPackageMasterService iMPackageMasterService;
	
	@Autowired
	IHemodialysisService iHemodialysisService;
	
	@Autowired
	IPatientRegistrationService iPatientRegistrationService;

	@Autowired
	private IAppointmentMasterDao iAppointmentMasterDao;
	
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	private Gson gson;
	
	@Autowired
	IScheduleLogsDao iScheduleLogsDao;
	
	@Autowired
	IPatientRegistrationDao iPatientMasterDao;
	
	@Autowired
	IUnitApplicationBillingConfigurationService iUnitApplicationBillingConfigurationService;

	@Autowired
	IAppointmentSchedulingDao iAppointmentSchedulingDao;



	@RequestMapping(value="encounter", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public synchronized Response saveEncoutnerMaster(@RequestBody EncounterMasterDto encounterMasterDto) {
		try {
			
			if(encounterMasterDto.getPatientSponsorDetailsDtosList()!=null && encounterMasterDto.getPatientSponsorDetailsDtosList().size()>0)
			{
				for(PatientSponsorDetailsDto patientSponsorDetailsDto : encounterMasterDto.getPatientSponsorDetailsDtosList())
				{
					if(patientSponsorDetailsDto.getPriorityId()==1){
						encounterMasterDto.setPaymentEntitlementId(patientSponsorDetailsDto.getPaymentEntitlementId());
						encounterMasterDto.setPaymentEntitlementTypeId(patientSponsorDetailsDto.getPaymentEntitlementId());
					}
						
				}
			}
			Response patRes=null;
			if(encounterMasterDto.getPatientId()==null || encounterMasterDto.getPatientId()==0)
			{
				 patRes = iPatientRegistrationService.registerPatient(encounterMasterDto.getPatientRegistrationDto());
				if(patRes.getStatus().equals(SUCCESS))
				{
					encounterMasterDto.setPatientRegistrationDto((PatientRegistrationDto)patRes.getResObject());
					encounterMasterDto.setPatientId((Integer)patRes.getObject());
					encounterMasterDto.setPatientCategoryId(encounterMasterDto.getPatientRegistrationDto().getPatientCategoryId());
					if(encounterMasterDto.getAppointmentObject()!=null)
						encounterMasterDto.getAppointmentObject().setPatientId((Integer)patRes.getObject());
					
					Response kssj = iPatientRegistrationService.checkSqlConnection(encounterMasterDto.getPatientRegistrationDto());
					
				}else if(patRes.getCode().equals(ALREADY_EXIST_CODE)){
					return patRes;
				}
				encounterMasterDto.setNewPatient(true);
			}else{
				encounterMasterDto.setNewPatient(false);
			}
			
			PatientRegistrationDto patientRegistrationDto = new PatientRegistrationDto();
			patientRegistrationDto.setUnitId(encounterMasterDto.getUnitId());
			patientRegistrationDto.setPatientId(encounterMasterDto.getPatientId());
			patientRegistrationDto.setSourceId(encounterMasterDto.getSourceId());
			patientRegistrationDto.setRemark(encounterMasterDto.getRemark());
			
				Response patResdd = iPatientRegistrationService.checkSqlConnectionForUpdatePatientSource(patientRegistrationDto);
				
			Response res = iEncounterMasterService.generateEncounter(encounterMasterDto);
				encounterMasterDto = (EncounterMasterDto)res.getObject();
			if(res.getStatus().equals(SUCCESS))	
			{
				if(res.getCode().equals("Y") && (encounterMasterDto.getEncounterTypeId()==1 || encounterMasterDto.getEncounterTypeId()==2)) //HIS-15029 change by Kaustubh
					iEncounterMasterService.updateAutorenderServicesAfterEncounter((EncounterMasterDto)res.getObject(),2);
				if(res.getListObject()!=null)
					iEncounterMasterService.saveOrderDetailsAfterEncounter((EncounterMasterDto)res.getObject(), (Integer)res.getListObject().getFirst(),2);

                if(encounterMasterDto.getAdminServiceMasterId() != null){
                    EncounterMasterDto adminServiceDto = encounterMasterDto;
                    adminServiceDto.setServiceAmount(null);
                    adminServiceDto.setDeptId(null);
                    adminServiceDto.setDoctorId(null);
                    adminServiceDto.setSpecialityId(null);
                    adminServiceDto.setConsultationSpecialityId(null);
					adminServiceDto.setIsAdminService(Boolean.TRUE);
                    iEncounterMasterService.saveOrderDetailsAfterEncounter(adminServiceDto, encounterMasterDto.getAdminServiceMasterId(),2);
                }

				if(encounterMasterDto.getListServiceMasterDto()!=null && encounterMasterDto.getListServiceMasterDto().size()>0)
				{
					iEncounterMasterService.updateFutureOrderedServices(encounterMasterDto);
				}

				Response responseRate =(Response) res.getResObject();
				if(encounterMasterDto.getEhcPackageId()!=null &&  encounterMasterDto.getEhcPackageId()>0 &&encounterMasterDto.getEncounterTypeId().intValue()==3 && responseRate !=null) 
				  { 
					
				     if(responseRate.getListObject()!=null && responseRate.getListObject().size()>0) {
				    	 
				    	 ServiceSearchResDto dto= (ServiceSearchResDto)responseRate.getListObject().getFirst();
				    	 encounterMasterDto.setServiceId(dto.getServiceMasterId());
				    	 encounterMasterDto.setPackageBasePrice(BigDecimal.valueOf(dto.getPackagePrice()));
				    	 encounterMasterDto.setEhcPackageId(dto.getPackageMasterId());
				    	 
				     }
					Response ehcResponse=  iMPackageMasterService.getEhcByPackageId(encounterMasterDto);			
					
					Map<Integer,Integer> packageOrders = (Map)ehcResponse.getObject();
				       for (Map.Entry<Integer,Integer> entry : packageOrders.entrySet())  
				       {
				    	   encounterMasterDto.setOrderDetailsIdEhc(entry.getValue());
						   iMPackageMasterService.saveEhcEncounterEhcConsultation(encounterMasterDto);
						   
						   
						   List<OrderDetailsMasterDto> dtoList=new ArrayList<OrderDetailsMasterDto>();
						   OrderDetailsMasterDto masterDto=new OrderDetailsMasterDto();
						    masterDto.setPackageId(encounterMasterDto.getEhcPackageId());
							masterDto.setOrderDetailsId(encounterMasterDto.getOrderDetailsIdEhc());
							masterDto.setOrgId(encounterMasterDto.getOrganizationId());
							masterDto.setEncounterId(encounterMasterDto.getEncounterId());
							masterDto.setOrgUnitId(encounterMasterDto.getUnitId());
							masterDto.setPackageId(encounterMasterDto.getEhcPackageId());
							masterDto.setCreatedBy(encounterMasterDto.getCreatedBy());
							masterDto.setUpdatedBy(encounterMasterDto.getCreatedBy());
							masterDto.setIsPackage(true);
							masterDto.setIsPackageService('N');
							masterDto.setStatus('A');
							masterDto.setPatientId(encounterMasterDto.getPatientId());
							masterDto.setPayeeId(encounterMasterDto.getPayeeId());
							masterDto.setVisitTypeId(OP);
							dtoList.add(masterDto);
							iEncounterMasterService.savePackageConsumptionForDirectEHCEncounter(dtoList);
							
						  
				       } 
				       //for updating package service in appointment master
				        SlotMasterDto slotMasterDto= new SlotMasterDto();
							slotMasterDto.setAppointmentId(encounterMasterDto.getAppointmentId());
							slotMasterDto.setServiceId(encounterMasterDto.getServiceId());
							slotMasterDto.setEhcPackageId(encounterMasterDto.getEhcPackageId());
				        iMPackageMasterService.updatePackageService(slotMasterDto);
				  } 
				
				
				if(encounterMasterDto != null  && res.getStatus().equals(SUCCESS))
				{
					String appointmentDate;
					String appointmentTime;
					
					appointmentDate = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()), "dd/MM/yyyy") ;
					appointmentTime = encounterMasterDto.getAppoitmentTime() != null
							? encounterMasterDto.getAppoitmentTime()
							: (encounterMasterDto.getAppointmentObject() != null
									? encounterMasterDto.getAppointmentObject().getFromTimeString()
									: encounterMasterDto.getEncounterTime().toString()); 
						
					SmsUtilityConfigInfoDto smsInfo=new SmsUtilityConfigInfoDto();
						smsInfo.setUnitId(encounterMasterDto.getUnitId());
						smsInfo.setPatientId(encounterMasterDto.getPatientId());
						smsInfo.setConsultingDoctorId(encounterMasterDto.getDoctorId()!= null ? encounterMasterDto.getDoctorId() : null);
						smsInfo.setReferringDoctorId(encounterMasterDto.getReferralId() != null ? encounterMasterDto.getReferralId() : null);
						if(appointmentTime != null) {
							smsInfo.setDateTime(appointmentDate+" at "+appointmentTime);
						}else {
							smsInfo.setDateTime(appointmentDate);
						}
					
						smsInfo.setEncounterTypeId(encounterMasterDto.getEncounterTypeId());
						smsInfo.setOrganizationId(encounterMasterDto.getOrganizationId());
						smsInfo.setEncounterId(encounterMasterDto.getEncounterId());
					smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.ENCOUNTER,smsInfo);
					
						
						smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.ENCOUNTER, smsInfo);
				}
				
				if(encounterMasterDto.getOtReservationList()!=null && encounterMasterDto.getOtReservationList().size()>0) {
					
					for (OTReservationDto oTReservationDto : encounterMasterDto.getOtReservationList()) {
						oTReservationDto.setEncounterId(encounterMasterDto.getEncounterId());
						oTReservationDto.setVisitTypeId(encounterMasterDto.getVisitTypeId() == null?1:encounterMasterDto.getVisitTypeId());
						iBillOfMaterialService.updateReservationAgainstAdmissionEncounter(oTReservationDto);
					}
				}
			}
			
			if(encounterMasterDto!= null) {
			if (encounterMasterDto.isSyncScheduling() == true) {
				String Global_Scheduling_URI = null;
				String MY_LIFE_URI = null;
				URI url = null;
				Response resp = iScheduleLogsDao.getConnectionStringForMyLife();
				MyLifeConnectionLinkDto connectionLinkDto = (MyLifeConnectionLinkDto) resp.getObject();
				if (connectionLinkDto != null && connectionLinkDto.getMyLifeLink() != null
						&& connectionLinkDto.getMyLifeLink() != ""
						&& connectionLinkDto.getGlobalSchedulingLink() != null
						&& connectionLinkDto.getGlobalSchedulingLink() != "") {
					Global_Scheduling_URI = connectionLinkDto.getGlobalSchedulingLink();
					MY_LIFE_URI = connectionLinkDto.getMyLifeLink();
				}

				String uuid = null;

				if (patRes != null && patRes.getResObject() != null && MY_LIFE_URI != null &&  Global_Scheduling_URI != null) {
					
					
					try {
						
						PatientRegistrationDto dto = (PatientRegistrationDto) patRes.getResObject();
					if (dto != null) {
						uuid = dto.getUuid();

						if (uuid == null) {

							Response response = iPatientMasterDao
									.getPatientByIdForSync(encounterMasterDto.getPatientId());
							PatientSyncDto patient = (PatientSyncDto) response.getObject();
							if (patient != null) {
								
								url = URI.create(MY_LIFE_URI + "/sync/patients/realtime");
								HttpHeaders headers = new HttpHeaders();
								headers.set("channelId",
										(patient.getChannelId() != null && patient.getChannelId() != 0)
												? patient.getChannelId().toString()
												: "1");
								HttpEntity<PatientSyncDto> request = new HttpEntity<>(patient, headers);
								String stringResponse = restTemplate.postForObject(url, request, String.class);
								Response commonResponse = null;
								commonResponse = gson.fromJson(stringResponse, Response.class);
								if (commonResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS)) {
									if (commonResponse.getObject() != null)
										iPatientMasterDao.updateUuidByPatient(encounterMasterDto.getPatientId(),
												(String) commonResponse.getObject());
								}
							}

							Response responseAppointment = iAppointmentMasterDao
									.getDoctorAppointmentToSync(encounterMasterDto.getAppointmentId());
							AppointmentSyncSchedulingDto appointment = (AppointmentSyncSchedulingDto) responseAppointment
									.getObject();
							if (appointment != null) {
								appointment.setCreatedDate(GlobalCommonDateUtils
										.getStringDate(TimeZoneComponent.getDateByzone(0), "dd-M-yyyy"));
								appointment.setApplicationFlag(true);
								String stringResponse = null;
								if (appointment.getDirectEncounter() == null) {
									appointment.setDirectEncounter('N');
								}
								appointment.setAppointmentStatusId(appointment.getAppointmentStatusId());

								if (appointment.getAppointmentStatusId() == 8) {
									appointment.setAppointmentStatusId(AppointmentStatusMasterConstant.PENDING);
								}
								if (appointment.getAppointmentStatusId() == AppointmentStatusMasterConstant.PENDING
										|| (appointment.getDirectEncounter().equals('Y')
												&& appointment.getAppointmentStatusId() == APPOINTMENTSTATUSID1)) {
									url = URI.create(Global_Scheduling_URI + "/appointment/bookAppointment");
									ObjectMapper objectMapper = new ObjectMapper();
									String json = objectMapper.writeValueAsString(appointment);
									System.out.println("Appointment SChedule JSON is:" + json);
									HttpHeaders headers = new HttpHeaders();
									String token = SmsUtility.generateJWTTokenMylife(connectionLinkDto.getGlobalSchedulingLink());
									headers.set("Authorization", "Bearer " + token);
									headers.set("userName", SmsUtility.USER_TOKEN_MYLIFE);
									headers.set("channelId",
											(appointment.getChannelId() != null && appointment.getChannelId() != 0)
													? appointment.getChannelId().toString()
													: "1");
									HttpEntity<AppointmentSyncSchedulingDto> request = new HttpEntity<>(appointment,
											headers);
									stringResponse = restTemplate.postForObject(url, request, String.class);
									Response commonResponse = null;
									commonResponse = gson.fromJson(stringResponse, Response.class);
									if (commonResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS)) {
										if(encounterMasterDto.getPatientId() != null && encounterMasterDto.getAppointmentId() != null){
											iAppointmentSchedulingDao.updateIsAppointmentFromMyLife(encounterMasterDto.getPatientId(), encounterMasterDto.getAppointmentId());
										}

										System.out.println("Appointment Booked Successfully");
									} else {
										System.out.println("Appointment Booking Failed");
									}
								}
							}
						}
					}
					
					} catch (Exception e) {
					logger.error("saveEncoutnerMaster failed", e);
					    return res;
					}
					
				} else {

					Response response = iAppointmentMasterDao
							.getDoctorAppointmentToSync(encounterMasterDto.getAppointmentId());
					AppointmentSyncSchedulingDto appointment = (AppointmentSyncSchedulingDto) response.getObject();
					if (appointment != null) {

						appointment.setCreatedDate(
								GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(0), "dd-M-yyyy"));
						appointment.setApplicationFlag(true);
						String stringResponse = null;
						if (appointment.getDirectEncounter() == null) {
							appointment.setDirectEncounter('N');
						}
						appointment.setAppointmentStatusId(appointment.getAppointmentStatusId());

						if (appointment.getAppointmentStatusId() == 8) {
							appointment.setAppointmentStatusId(AppointmentStatusMasterConstant.PENDING);
						}
						if (appointment.getAppointmentStatusId() == AppointmentStatusMasterConstant.PENDING
								|| (appointment.getDirectEncounter().equals('Y')
										&& appointment.getAppointmentStatusId() == APPOINTMENTSTATUSID1)) {
							try {
							url = URI.create(Global_Scheduling_URI + "/appointment/bookAppointment");
							ObjectMapper objectMapper = new ObjectMapper();
							String json = objectMapper.writeValueAsString(appointment);
							System.out.println("Appointment SChedule JSON is:" + json);
							HttpHeaders headers = new HttpHeaders();
							String token = SmsUtility.generateJWTTokenMylife(Global_Scheduling_URI);
							headers.set("Authorization", "Bearer " + token);
							headers.set("userName", SmsUtility.USER_TOKEN_MYLIFE);
							headers.set("channelId",
									(appointment.getChannelId() != null && appointment.getChannelId() != 0)
											? appointment.getChannelId().toString()
											: "1");
							HttpEntity<AppointmentSyncSchedulingDto> request = new HttpEntity<>(appointment, headers);
							stringResponse = restTemplate.postForObject(url, request, String.class);
							Response commonResponse = null;
							commonResponse = gson.fromJson(stringResponse, Response.class);
							if (commonResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS)) {
								if(encounterMasterDto.getPatientId() != null && encounterMasterDto.getAppointmentId() != null){
									iAppointmentSchedulingDao.updateIsAppointmentFromMyLife(encounterMasterDto.getPatientId(), encounterMasterDto.getAppointmentId());
								}

								System.out.println("Appointment Booked Successfully");
							} else {
								System.out.println("Appointment Booking Failed");
							}
							
							} catch (Exception e) {
							logger.error("saveEncoutnerMaster failed", e);
							    return res;
							}
							
						}
					}

				}
			}
		}
			
			return res;
		}catch(ConstraintViolationException e){
		logger.error("saveEncoutnerMaster failed", e);
			return new Response(ERROR, ALREADY_EXIST_CODE, "Encounter is alread generated for selected appointment...!!", null, null);
		}catch(DataIntegrityViolationException e){
		logger.error("saveEncoutnerMaster failed", e);
			return new Response(ERROR, ALREADY_EXIST_CODE, "Encounter is alread generated for selected appointment...!!", null, null);
		}
		catch (Exception e) {
		logger.error("saveEncoutnerMaster failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="encounterEhc", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public synchronized Response saveChildEncounter(@RequestBody List<EncounterMasterDto> encounterList) {
		try {
				for (EncounterMasterDto encounterMasterDto : encounterList) 
				{
					iEncounterMasterService.saveChildEncounter(encounterMasterDto);
				}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch (Exception e) {
		logger.error("saveChildEncounter failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/updateAiSummaryAndQuestion", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public synchronized Response updateAiSummaryAndQuestion(@RequestBody EncounterMasterDto encounterMasterDto) {
		try {
			iEncounterMasterService.updateAiSummaryAndQuestion(encounterMasterDto);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch (Exception e) {
		logger.error("updateAiSummaryAndQuestion failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	
	@RequestMapping(value="encounter/{enconterId}", method=RequestMethod.GET)
	public Response getEncounterDetailsById(@PathVariable("enconterId") Integer enconterId) {
		try {
			return iEncounterMasterService.getEncounterDetailsById(enconterId);
		}catch (Exception e) {
		logger.error("getEncounterDetailsById failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="getSystemGeneratedVisitTypeId",  method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Integer getSystemGeneratedVisitTypeId(@RequestBody SlotMasterDto encounterMasterDto) {
		try {
			if(encounterMasterDto.getPatientId()!=null && encounterMasterDto.getPatientId()>0)
				return iEncounterMasterService.assignVisitTypeIdAccordingToConditions(encounterMasterDto);
			else
				return 1;
		}catch (Exception e) {
		logger.error("getSystemGeneratedVisitTypeId failed", e);
		}
		return 0;
	}
	
	@RequestMapping(value="getEncounterDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getEncounterDetails(@RequestBody EncounterMasterDto encounterMasterDto) {
		try {
			return iEncounterMasterService.getEncounterDetails(encounterMasterDto);
		}catch (Exception e) {
		logger.error("getEncounterDetails failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="getNephrologyWorkbenchWorklist", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getNephrologyWorkbenchWorklist(@RequestBody RadiologyOrdersListDto radiologyOrdersListDto) {
		try {
			return iEncounterMasterService.getNephrologyWorkbenchWorklist(radiologyOrdersListDto);
		}catch (Exception e) {
		logger.error("getNephrologyWorkbenchWorklist failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/*@RequestMapping(value="getRadiologyListForSampleCollection", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getRadiologyListForSampleCollection(@RequestBody RadiologyOrdersListDto radiologyOrdersListDto) {
		try {
			return iEncounterMasterService.getRadiologyListForSampleCollection(radiologyOrdersListDto);
		}catch (Exception e) {
			
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}*/
	
	@RequestMapping(value="acceptRadiologyRequest", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response acceptRadiologyRequest(@RequestBody RadiologyOrdersListDto radiologyOrdersListDto) {
		try {
			return iEncounterMasterService.acceptRadiologyRequest(radiologyOrdersListDto);
		}catch (Exception e) {
		logger.error("acceptRadiologyRequest failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="retestRadiologyResultEntry", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response retestRadiologyResultEntry(@RequestBody RadiologyOrdersListDto radiologyOrdersListDto) {
		try {
			return iEncounterMasterService.retestRadiologyResultEntry(radiologyOrdersListDto);
		}catch (Exception e) {
			throw e;
		}
	}
	
	/*	ADDED BY JYOTI
	 * 	DATE 03-05-2018
	 * */
	@RequestMapping(value="/getListOfEncounterMaster", method=RequestMethod.GET)
	public @ResponseBody Response listOfEncounterMaster(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId) {
		try
		{
			SlotMasterDto slotMasterDto= new SlotMasterDto();
			slotMasterDto.setUnitId(unitId);
			return iEncounterMasterService.getListOfEncounterMasterDao(slotMasterDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/getSearchedListEncounterMaster", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSearchedListEncounterMaster(@RequestBody SlotMasterDto slotMasterDto) {
		try
		{
//			slotMasterDto.setUnitId(unitId);
//			slotMasterDto.setOrganizationId(organizationId);
			return iEncounterMasterService.getSearchedListEncounterMaster(slotMasterDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	// @RequestMapping(value="/getSearchedListEncounterMaster", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	// public @ResponseBody Response getSearchedListEncounterMaster(@RequestHeader(value="unit_id") int unitId,
	// 		@RequestHeader(value="organization_id") int organizationId, @RequestBody String slotMasterDto) {
	// 	try
	// 	{
	// 		//slotMasterDto.setUnitId(unitId);
	// 		//slotMasterDto.setOrganizationId(organizationId);
	// 		JsonCheck.mapJsonToObject(slotMasterDto,SlotMasterDto.class);
	// 		return null;
	// 		//return iEncounterMasterService.getSearchedListEncounterMaster(slotMasterDto);
	// 	}catch(Exception e)
	// 	{
	// 		
	// 	}
	// 	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	// }
	
	@RequestMapping(value="/getListOfAppointmentStatusMaster", method=RequestMethod.GET)
	public @ResponseBody Response getListOfAppointmentStatusMaster(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId) {
		try
		{
			AppointmentStatusMasterDto appointmentStatusMasterDto = new AppointmentStatusMasterDto();
			appointmentStatusMasterDto.setUnitId(unitId);
			appointmentStatusMasterDto.setOrganizationId(organizationId);
			return iEncounterMasterService.getListOfAppointmentStatus(appointmentStatusMasterDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/encounters/orgId/{orgId}/unitId/{unitId}/patientId/{patientId}", method=RequestMethod.GET)
	public @ResponseBody Response getListOfEncountersByPatientId(@PathVariable("orgId") Integer organizationId,
																@PathVariable("unitId") Integer unitId,
																@PathVariable("patientId") Integer patientId) 
	{
		try
		{
			EncounterMasterDto encounterMasterDto = new EncounterMasterDto();
				encounterMasterDto.setOrganizationId(organizationId);
				encounterMasterDto.setUnitId(unitId);
				encounterMasterDto.setPatientId(patientId);
			return iEncounterMasterService.getListOfEncountersByPatientId(encounterMasterDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/canelledBills/encounterDetails/{encounterId}", method=RequestMethod.GET)
	public @ResponseBody Response getEncountersDetailsForCanelledBills(@PathVariable("encounterId") Integer encounterId) 
	{
		try
		{
			return iEncounterMasterService.getEncountersDetailsForCanelledBills(encounterId);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	 @RequestMapping(value = "/patientDoc", headers=("content-type=multipart/*"), method = RequestMethod.POST)
     public @ResponseBody Response saveDocumentAgainstEncounter(
    		 @RequestParam("encounterId") Integer encounterId,
    		 @RequestParam("admissionId") Integer admissionId,
    		 @RequestParam("patientId") Integer patientId,
    		 @RequestParam("documentTypeId") Integer documentTypeId,
    		 @RequestParam("createdBy") Integer createdBy,
    		 @RequestParam("updatedBy") Integer updatedBy,
    		 @RequestParam("unitId") Integer unitId,
    		 @RequestParam("organisationId") Integer organisationId,
    		 @RequestParam("expenseId") Integer expenseId,
    		 @RequestParam("companyExpenseId") Integer companyExpenseId,
    		 @RequestParam("grnId") Integer grnId,
    		 @RequestParam("incidentReportingMapperId") Integer incidentReportingMapperId,
    		 @RequestParam(value="labSampleDtlsId", required = false) Integer labSampleDtlsId,
    		 @RequestParam(value="uploadDoccumentDate", required = false) String uploadDoccumentDate,
    		 @RequestParam("file") MultipartFile file,
             @RequestParam(value="remark", required = false) String remark,
             @RequestParam("poId") Integer poId,
    		 @RequestParam(value="deathPatientId", required = false) Integer deathPatientId,
    		 @RequestParam(value="createdDate", required = false) String createdDate,
    		 @RequestParam(value="fileEditedName", required = false) String fileEditedName,
    		 @RequestParam("opbId") Integer opbId,
    		 @RequestParam(value="vendorId", required = false) Integer vendorId
    		 )
         {
	      try
	      {
	    	  EncounterDocDetailsDto encounterDocDetailsDto = new EncounterDocDetailsDto();
		    	  encounterDocDetailsDto.setEncounterId(encounterId==0?null:encounterId);
		    	  encounterDocDetailsDto.setAdmissionId(admissionId==0?null:admissionId);
		    	  encounterDocDetailsDto.setPatientId(patientId==0 ? null : patientId);
		    	  encounterDocDetailsDto.setDocumentTypeId(documentTypeId);
		    	  encounterDocDetailsDto.setCreatedBy(createdBy);
		    	  encounterDocDetailsDto.setUpdatedBy(updatedBy);
		    	  encounterDocDetailsDto.setOrganisationId(organisationId);
		    	  encounterDocDetailsDto.setUnitId(unitId);
		    	  encounterDocDetailsDto.setExpenseId(expenseId !=null ? expenseId : 0);
		    	  encounterDocDetailsDto.setIncidentReportingMapperId(incidentReportingMapperId !=null ? incidentReportingMapperId : 0);
		    	  encounterDocDetailsDto.setCompanyExpenseId(companyExpenseId !=null ? companyExpenseId : 0);
		    	  encounterDocDetailsDto.setGrnId(grnId !=null ? grnId : 0);
		    	  encounterDocDetailsDto.setFile(file);
		    	  encounterDocDetailsDto.setLabSampleDtlsId(labSampleDtlsId !=null ? labSampleDtlsId : 0);
		    	  encounterDocDetailsDto.setUploadDoccumentDate(uploadDoccumentDate);
			  if (fileEditedName != null && !fileEditedName.equals("null") && !fileEditedName.equals("")) {
				  encounterDocDetailsDto.setFileName(fileEditedName);
			  } else {
				  encounterDocDetailsDto.setFileName(file.getOriginalFilename());
			  }

			  encounterDocDetailsDto.setRemark(remark!=null ? remark : null);
                  encounterDocDetailsDto.setPoId(poId!=null ? poId : null);
                  encounterDocDetailsDto.setDeathPatientId(deathPatientId !=null ? deathPatientId : 0);
                  encounterDocDetailsDto.setCreatedDate(createdDate);
                  encounterDocDetailsDto.setOpbId(opbId !=null ? opbId : 0);
                  encounterDocDetailsDto.setVendorId(vendorId !=null ? vendorId:0);
	         return iEncounterMasterService.saveDocumentAgainstEncounter(encounterDocDetailsDto);
	       }catch(Exception e)
	      {
	      logger.error("method failed", e);
	      }
	      return new Response<>(ERROR, null, null, null, null);
       }

    @RequestMapping(value="/patientDoc/fromMylife", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response patientDocFromMylife(@RequestBody PatientMasterDto encounterDocDetailsDto) {
		try {
			return iEncounterMasterService.saveDocumentFromMylife(encounterDocDetailsDto);
		}catch (Exception e) {
		logger.error("patientDocFromMylife failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	 
	
	 @RequestMapping(value="/patientDoc/details", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public Response getPatientDocDetails(@RequestBody PatientDocReqDto patientDocReqDto) {
			try {
				return iEncounterMasterService.getPatientDocDetails(patientDocReqDto);
			}catch (Exception e) {
			logger.error("getPatientDocDetails failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	 
	 @RequestMapping(value="/getInsuranceDocByPatientIDDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public Response getInsuranceDocByPatientIDDetails(@RequestBody PatientDocReqDto patientDocReqDto) {
			try {
				return iEncounterMasterService.getInsuranceDocByPatientIDDetails(patientDocReqDto);
			}catch (Exception e) {
			logger.error("getInsuranceDocByPatientIDDetails failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	 
	 @RequestMapping(value="/ehcPatients/orgId/{orgId}/unitId/{unitId}/{isPreOrPostBilling}", method=RequestMethod.GET)
		public @ResponseBody Response getEHCPatientsList(@PathVariable("orgId") Integer organizationId,
																	@PathVariable("unitId") Integer unitId,@PathVariable(value = "isPreOrPostBilling") Character isPreOrPostBilling) 
		{
			try
			{
				EncounterMasterDto encounterMasterDto = new EncounterMasterDto();
					encounterMasterDto.setOrganizationId(organizationId);
					encounterMasterDto.setUnitId(unitId);
				return iEncounterMasterService.getEHCPatientsList(encounterMasterDto,isPreOrPostBilling);
			}catch(Exception e)
			{
			logger.error("method failed", e);
			}
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	 
	 
	 @RequestMapping(value="/ehcConsultataion/encounterId/{encounterId}/{consultationMasterId}", method=RequestMethod.GET)
		public @ResponseBody Response getEHCConsultation(@PathVariable("encounterId") Integer encounterId,@PathVariable("consultationMasterId") Integer consultationMasterId) 
		{
			try
			{
				EncounterMasterDto encounterMasterDto = new EncounterMasterDto();
					encounterMasterDto.setEncounterId(encounterId);
					encounterMasterDto.setConsultationMasterId(consultationMasterId);
				return iEncounterMasterService.getEHCConsultation(encounterMasterDto);
			}catch(Exception e)
			{
			logger.error("method failed", e);
			}
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	 
	 
	 /*
	  *  Insurance Document Upload
	  */

	 @RequestMapping(value = "/preAuthorizationRequestDoc", headers=("content-type=multipart/*"), method = RequestMethod.POST)
     public @ResponseBody Response savePreAuthorizationRequestDoc(
    		 @RequestParam("encounterId") Integer encounterId,
    		 @RequestParam("patientId") Integer patientId,
    		 @RequestParam("documentTypeId") Integer documentTypeId,
    		 @RequestParam("createdBy") Integer createdBy,
    		 @RequestParam("updatedBy") Integer updatedBy,
    		 @RequestParam("unitId") Integer unitId,
    		 @RequestParam("visitTypeId") Integer visitTypeId,
    		 @RequestParam("admissionId") Integer admissionId,
    		 @RequestParam("preauthRequestId") Integer preauthRequestId,
    		 @RequestParam("organisationId") Integer organisationId,
    		 @RequestParam("documentName") String documentName,
    		 @RequestParam("file") MultipartFile file
    		 )
         {
	      try
	      {
	    	  EncounterDocDetailsDto encounterDocDetailsDto = new EncounterDocDetailsDto();
	    	  encounterDocDetailsDto.setEncounterId(encounterId);
	    	  encounterDocDetailsDto.setPatientId(patientId);
	    	  encounterDocDetailsDto.setDocumentTypeId(documentTypeId);
	    	  encounterDocDetailsDto.setCreatedBy(createdBy);
	    	  encounterDocDetailsDto.setUpdatedBy(updatedBy);
	    	  encounterDocDetailsDto.setOrganisationId(organisationId);
	    	  encounterDocDetailsDto.setUnitId(unitId);
	    	  encounterDocDetailsDto.setVisitTypeId(visitTypeId);
	    	  encounterDocDetailsDto.setAdmissionId(admissionId);
	    	  encounterDocDetailsDto.setPreauthRequestId(preauthRequestId);
	    	  encounterDocDetailsDto.setFileName(documentName);
	    	  encounterDocDetailsDto.setFile(file);
	         return iEncounterMasterService.saveDocumentAgainstEncounterInsuranceAuthorizationsDoc(encounterDocDetailsDto);
	       }catch(Exception e)
	      {
	      logger.error("method failed", e);
	      }
	      return new Response<>(ERROR, null, null, null, null);
       }

	 @RequestMapping(value="/doctorDesk/opPatientListByDocId", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public Response getOpPatientListByDocId(@RequestBody EncounterMasterDto encounterMasterDto) {
			try {
				return iEncounterMasterService.getOpPatientListByDocId(encounterMasterDto);
			}catch (Exception e) {
			logger.error("getOpPatientListByDocId failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	 
	 @RequestMapping(value="/token/getTokenList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public Response getTokenList(@RequestBody EncounterMasterDto encounterMasterDto) {
			try {
				return iEncounterMasterService.getTokenList(encounterMasterDto);
			}catch (Exception e) {
			logger.error("getTokenList failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	 @RequestMapping(value="/casualtyWorklist/opPatientListByDocIdForCasualty", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public Response opPatientListByDocIdForCasualty(@RequestBody EncounterMasterDto encounterMasterDto) {
			try {
				return iEncounterMasterService.opPatientListByDocIdForCasualty(encounterMasterDto);
			}catch (Exception e) {
			logger.error("opPatientListByDocIdForCasualty failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	 
	 @RequestMapping(value="/patientCalled", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public Response patientCalled(@RequestBody EncounterMasterDto encounterMasterDto) {
			try {
				return iEncounterMasterService.patientCalled(encounterMasterDto);
			}catch (Exception e) {
			logger.error("patientCalled failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	 
	 @RequestMapping(value="/token/getDoctorTokenList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public Response getDoctorTokenList(@RequestBody EncounterMasterDto encounterMasterDto) {
			try {
				return iEncounterMasterService.getDoctorTokenList(encounterMasterDto);
			}catch (Exception e) {
			logger.error("getDoctorTokenList failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	 
	 @RequestMapping(value="/token/getNursingTokenList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public Response getNursingTokenList(@RequestBody EncounterMasterDto encounterMasterDto) {
			try {
				return iEncounterMasterService.getNursingTokenList(encounterMasterDto);
			}catch (Exception e) {
			logger.error("getNursingTokenList failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	 
	 @RequestMapping(value="/scheduleAppointment/kiosk", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response scheduleAppointmentFromKiosk(@RequestBody EncounterMasterDto encounterMasterDto) {
			try {
				return iEncounterMasterService.scheduleAppointmentFromKiosk(encounterMasterDto);
				
			}catch (Exception e) {
			logger.error("method failed", e);
			}
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	 
	 @RequestMapping(value="/billAmount/kiosk", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response billAmountForKiosk(@RequestBody EncounterMasterDto encounterMasterDto) {
			try {
				return iEncounterMasterService.billAmountForKiosk(encounterMasterDto);
				
			}catch (Exception e) {
			logger.error("method failed", e);
			}
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	 
	 
	 @RequestMapping(value="/getCheckedInPatientByDoctorId/{orgId}/{unitId}/{doctorId}", method=RequestMethod.GET)
		public Response getCheckedInPatientByDoctorId(@PathVariable("orgId") Integer organizationId,
				@PathVariable("unitId") Integer unitId,@PathVariable("doctorId") Integer doctorId) {
			try {
				EncounterMasterDto encounterMasterDto=new EncounterMasterDto();
				encounterMasterDto.setOrganizationId(organizationId);
				encounterMasterDto.setUnitId(unitId);
				encounterMasterDto.setDoctorId(doctorId);
				
				return iEncounterMasterService.getCheckedInPatientByDoctorId(encounterMasterDto);
			}catch (Exception e) {
			logger.error("getCheckedInPatientByDoctorId failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	 
	 @RequestMapping(value="/nursing/kiosk/callCount/{encounterId}/{count}/{nursingStationId}", method=RequestMethod.GET)
		public Response updateCallCount(@PathVariable("encounterId") Integer encounterId,@PathVariable("count") Integer count,@PathVariable("nursingStationId") Integer nursingStationId) {
			try {
				EncounterMasterDto encounterMasterDto=new EncounterMasterDto();
				encounterMasterDto.setEncounterId(encounterId);
				encounterMasterDto.setCallCount(count);
				encounterMasterDto.setNursingStationId(nursingStationId!= null ? nursingStationId : null);
				return iEncounterMasterService.updateCallCount(encounterMasterDto);
			}catch (Exception e) {
			logger.error("updateCallCount failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	 
	 @RequestMapping(value="/getListOfRadiologyTests/{admissionId}/{orgId}/{orgUnitId}", method=RequestMethod.GET)
		public Response getListOfRadiologyTests(@PathVariable("admissionId") Integer admissionId,@PathVariable("orgId") Integer orgId,@PathVariable("orgUnitId") Integer orgUnitId) {
			try {
				return iEncounterMasterService.getListOfRadiologyTests(admissionId, orgId, orgUnitId);
			}catch (Exception e) {
			logger.error("getListOfRadiologyTests failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	 
	 @RequestMapping(value="/doctor/update/{encounterId}/{oldDocId}/{docId}/{reasonId}", method=RequestMethod.GET)
	public Response updateDoctorFromNursingWorkList(@PathVariable("encounterId") Integer encounterId,
			@PathVariable("oldDocId") Integer oldDocId,@PathVariable("docId") Integer docId, @PathVariable("reasonId") Integer reasonId) {
		try {
			return iEncounterMasterService.updateDoctorFromNursingWorkList(encounterId,oldDocId,docId,reasonId);
		} catch (Exception e) {
		logger.error("updateDoctorFromNursingWorkList failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	 
	 @RequestMapping(value="/bill/details/encounter",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public Response getBillDetailsOnEncouter(@RequestBody EncounterMasterDto encounterMasterDto) {
			try {
				return iEncounterMasterService.getBillDetailsOnEncouter(encounterMasterDto);
			} catch (Exception e) {
			logger.error("getBillDetailsOnEncouter failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		} 
	 
	 
	 @RequestMapping(value="/token/getTokenMsgList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public Response getTokenMsgList(@RequestBody EncounterMasterDto encounterMasterDto) {
			try {
				return iEncounterMasterService.getTokenMsgList(encounterMasterDto);
			}catch (Exception e) {
			logger.error("getTokenMsgList failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	 
	 @RequestMapping(value="/emr/encounter", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	 public Response getEnounterDataForEMR(@RequestBody EncounterMasterDto encounterMasterDto) throws ApplicationException {
			try {
				return iEncounterMasterService.getEnounterDataForEMR(encounterMasterDto);
			} catch (Exception e) {
			logger.error("getEnounterDataForEMR failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
		
		@RequestMapping(value="savePACSDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public Response savePACSDetails(@RequestBody PACSDto dto) {
			try {
				return iEncounterMasterService.savePACSDetails(dto);
			}catch (Exception e) {
			logger.error("savePACSDetails failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
		
		@RequestMapping(value="/op/pending/services/encounter/{encounterId}", method=RequestMethod.GET)
		public Response getPendingServicesOfEncounter(@PathVariable("encounterId") Integer encounterId) {
			try {
				return iEncounterMasterService.getPendingServicesOfEncounter(encounterId);
			}catch (Exception e) {
			logger.error("getPendingServicesOfEncounter failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}	
				
				
		@RequestMapping(value="acceptHemodialysisRequest", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public Response acceptHemodialysisRequest(@RequestBody HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto) {
			try {
				Response acceptanceRes=iEncounterMasterService.acceptHemodialysisRequest(hemodialysisAppointmentMapperDto);
				hemodialysisAppointmentMapperDto.setHemodialysisWorkbenchAcceptance('A');//A for Accepted
				iHemodialysisService.saveDialysisRequest(hemodialysisAppointmentMapperDto);
				return acceptanceRes;
			}catch (Exception e) {
			logger.error("acceptHemodialysisRequest failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
		
		 @RequestMapping(value="/getPatientServicesForFutureDate", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
			public Response getPatientServicesForFutureDate(@RequestBody PatientDocReqDto patientDocReqDto) {
				try {
					return iEncounterMasterService.getPatientServicesForFutureDate(patientDocReqDto);
				}catch (Exception e) {
				logger.error("getPatientServicesForFutureDate failed", e);
				}
				return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		 
			@RequestMapping(value="rejectHemodialysisRequest", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
			public Response rejectHemodialysisRequest(@RequestBody HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto) {
				try {
					Response rejectRes=iEncounterMasterService.rejectHemodialysisRequest(hemodialysisAppointmentMapperDto);
					//hemodialysisAppointmentMapperDto.setHemodialysisWorkbenchAcceptance('R');//R for rejected
					//iHemodialysisService.saveDialysisRequest(hemodialysisAppointmentMapperDto);
					return rejectRes;
				}catch (Exception e) {
				logger.error("rejectHemodialysisRequest failed", e);
				}
				return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
			
			
			/**@Code @author Ganesh Chaudhari*/
			@RequestMapping(value="/reviewApplicable/{patientId}/{orgId}/{unitId}", method=RequestMethod.GET)
			public Response getReviewApplicableServicesByPatient(@PathVariable("patientId") Integer patientId,
					@PathVariable("orgId") Integer orgId,
					@PathVariable("unitId") Integer unitId) {
				try {
					return iEncounterMasterService.getReviewApplicableServicesByPatient(patientId,orgId,unitId);
				}catch (Exception e) {
				logger.error("getReviewApplicableServicesByPatient failed", e);
				}
				return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		
			@RequestMapping(value="/rejectRadiologyRequest", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
			public Response rejectRadiologyRequest(@RequestBody RadiologyOrdersListDto radiologyOrdersListDto) {
				try {
						 CancelOrderDto cancelOrderDto =new CancelOrderDto();
							 cancelOrderDto.setAdmissionId(radiologyOrdersListDto.getAdmissionId());
							 cancelOrderDto.setEncounterId(radiologyOrdersListDto.getEncounterId());
							 cancelOrderDto.setOrdCancelReasonId(radiologyOrdersListDto.getReasonId());
							 cancelOrderDto.setOrderDetailsId(radiologyOrdersListDto.getOrderDetailsId());
							 cancelOrderDto.setPreOrPostBilling(radiologyOrdersListDto.getIsPreOrPostBilling());
							 cancelOrderDto.setSpecialityId(radiologyOrdersListDto.getSpecialityId());
							 cancelOrderDto.setUserId(radiologyOrdersListDto.getUserId());
							 cancelOrderDto.setVisitTypeId(radiologyOrdersListDto.getVisitTypeId());
						 Response res = globalBillingService.cancelPendingService(cancelOrderDto);
					if(res.getStatus().equals(SUCCESS))
						iEncounterMasterService.rejectRadiologyRequest(radiologyOrdersListDto);
						
					return res;
				}catch (Exception e) {
				logger.error("rejectRadiologyRequest failed", e);
				}
				return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
			
			@RequestMapping(value="getEncounterDetailsForCoversheet", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
			public Response getEncounterDetailsForCoversheet(@RequestBody EncounterMasterDto encounterMasterDto) {
				try {
					return iEncounterMasterService.getEncounterDetailsForCoversheet(encounterMasterDto);
				}catch (Exception e) {
				logger.error("getEncounterDetailsForCoversheet failed", e);
				}
				return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
			
			@RequestMapping(value="/pending/services/other", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
			public Response getOtherServicesForRendering(@RequestBody RadiologyOrdersListDto radiologyOrdersListDto) {
				try {
					return iEncounterMasterService.getOtherServicesForRendering(radiologyOrdersListDto);
				}catch (Exception e) {
				logger.error("getOtherServicesForRendering failed", e);
				}
				return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
			
			@RequestMapping(value="/render/services/other", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
			public Response renderOtherServices(@RequestBody RadiologyOrdersListDto radiologyOrdersListDto) {
				try {
					return iEncounterMasterService.renderOtherServices(radiologyOrdersListDto);
				}catch (Exception e) {
				logger.error("renderOtherServices failed", e);
				}
				return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
			
			@RequestMapping(value="getPatientListForPhysiotherapyWorkbench", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
			public Response getPatientListForPhysiotherapyWorkbench(@RequestBody PhysiotherapyOrdersListDto physiotherapyOrdersListDto) {
				try {
					return iEncounterMasterService.getPatientListForPhysiotherapyWorkbench(physiotherapyOrdersListDto);
				}catch (Exception e) {
				logger.error("getPatientListForPhysiotherapyWorkbench failed", e);
				}
				return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
			
			@RequestMapping(value="/encounter/future/services", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
			public synchronized Response encounterFutureServices(@RequestBody EncounterMasterDto encounterList) {
				try {
					
					iEncounterMasterService.updateFutureOrderedServices(encounterList);
					return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
				}catch (Exception e) {
				logger.error("encounterFutureServices failed", e);
				}
				return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
			
			 @RequestMapping(value="/billAmount/appointment", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
				public @ResponseBody Response billAmountForAppointment(@RequestBody EncounterMasterDto encounterMasterDto) {
					try {
						return iEncounterMasterService.billAmountForAppointment(encounterMasterDto);
						
					}catch (Exception e) {
					logger.error("method failed", e);
					}
					return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
				}
			
			 

				@RequestMapping(value="/rejectMicrobiologyRequest", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
				public  @ResponseBody Response rejectMicrobiologyRequest(@RequestBody RadiologyOrdersListDto radiologyOrdersListDto) {
					try {
							Response response = null;
							 CancelOrderDto cancelOrderDto =new CancelOrderDto();
								 cancelOrderDto.setAdmissionId(radiologyOrdersListDto.getAdmissionId());
								 cancelOrderDto.setEncounterId(radiologyOrdersListDto.getEncounterId());
								 cancelOrderDto.setOrdCancelReasonId(radiologyOrdersListDto.getReasonId());
								 cancelOrderDto.setOrderDetailsId(radiologyOrdersListDto.getOrderDetailsId());
								 cancelOrderDto.setPreOrPostBilling(radiologyOrdersListDto.getIsPreOrPostBilling());
								 cancelOrderDto.setSpecialityId(radiologyOrdersListDto.getSpecialityId());
								 cancelOrderDto.setUserId(radiologyOrdersListDto.getUserId());
								 cancelOrderDto.setVisitTypeId(radiologyOrdersListDto.getVisitTypeId());
								 response = globalBillingService.cancelPendingService(cancelOrderDto);
								 if(response.getStatus().equals(SUCCESS)) {
									 globalBillingService.updateLabSampleStatus(radiologyOrdersListDto.getLabSampleDetailsId());
								 }
								 return response;
					}catch (Exception e) {
					logger.error("method failed", e);
					}
					return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
				}
				
				
			@RequestMapping(value = "/appendCensentData", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody Response appendCensentData(@RequestBody PatientConsentMapperDto dto) {
				try {
					return iEncounterMasterService.appendCensentData(dto);
				} catch (Exception e) {
				logger.error("method failed", e);
				}
				return new Response(ERROR, null, null, null, null);
			}
			
			@RequestMapping(value="getSystemGeneratedVisitTypeForApp",  method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody Response getSystemGeneratedVisitTypeIdForApp(@RequestBody ScheduleAppointmentReqDto scheduleAppointmentReqDto) {
				try {
						return iEncounterMasterService.getSystemGeneratedVisitTypeIdForApp(scheduleAppointmentReqDto);
				}catch (Exception e) {
				logger.error("method failed", e);
				}
				return new Response(ERROR, null, null, null, null);
			}
			
			@RequestMapping(value="/updateFollowUpDateOfPatient", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
			public synchronized Response updateFollowUpDateOfPatient(@RequestBody EncounterMasterDto encounterMasterDto) {
				try {
					
					iEncounterMasterService.updateFollowUpDateOfPatient(encounterMasterDto);
					return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
				}catch (Exception e) {
				logger.error("updateFollowUpDateOfPatient failed", e);
				}
				return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
			
            @RequestMapping(value="/getSearchedListEncounterMasterForNursingAssToken", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
            public @ResponseBody Response getSearchedListEncounterMasterForNursingAssToken(@RequestHeader(value="unitId") int unitId,
                    @RequestHeader(value="organizationId") int organizationId, @RequestBody SlotMasterDto slotMasterDto) {
                try
                {
                    slotMasterDto.setUnitId(unitId);
                    slotMasterDto.setOrganizationId(organizationId);
                    return iEncounterMasterService.getSearchedListEncounterMasterorNursingAssToken(slotMasterDto);
                }catch(Exception e)
                {
                logger.error("method failed", e);
                }
                return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
            }
            
            
            @RequestMapping(value="/doctorDesk/universalSearchOpPatientListByDocId", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
            public Response universalSearchOpPatientListByDocId(@RequestBody EncounterMasterDto encounterMasterDto) {
                try {
                    return iEncounterMasterService.universalSearchOpPatientListByDocId(encounterMasterDto);
                }catch (Exception e) {
                logger.error("universalSearchOpPatientListByDocId failed", e);
                }
                return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
            }
            
            @RequestMapping(value="/getUniversalSearchedListEncounterMaster", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
            public @ResponseBody Response getUniversalSearchedListEncounterMaster(@RequestHeader(value="unit_id") int unitId,
                    @RequestHeader(value="organization_id") int organizationId, @RequestBody SlotMasterDto slotMasterDto) {
                try
                {
                    slotMasterDto.setUnitId(unitId);
                    slotMasterDto.setOrganizationId(organizationId);
                    return iEncounterMasterService.getUniversalSearchedListEncounterMaster(slotMasterDto);
                }catch(Exception e)
                {
                logger.error("method failed", e);
                }
                return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
            }
            
            
            
            @RequestMapping(value = "/updateEncounterDetailsPatientByEncounterId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
        	public @ResponseBody Response updateEncounterDetailsPatientByEncounterId(@RequestBody EncounterMasterDto encounterMasterDto) {
        		try {
        			return iEncounterMasterService.updateEncounterDetailsPatientByEncounterId(encounterMasterDto);
        		} catch (Exception e) {
        		logger.error("method failed", e);
        		}
        		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
        	}
            
            
            @RequestMapping(value = "/getPatientClassIdByEncounter", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
        	public @ResponseBody Response getPatientClassIdByEncounter(@RequestBody EncounterMasterDto encounterMasterDto) {
        		try {
        			return iEncounterMasterService.getPatientClassIdByEncounter(encounterMasterDto);
        		} catch (Exception e) {
        		logger.error("method failed", e);
        		}
        		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
        	}
            
            @RequestMapping(value="/encounters/walkinSales/orgId/{orgId}/unitId/{unitId}/patientId/{patientId}", method=RequestMethod.GET)
        	public @ResponseBody Response getListOfEncountersByPatientIdForWalkinSale(@PathVariable("orgId") Integer organizationId,
        																@PathVariable("unitId") Integer unitId,
        																@PathVariable("patientId") Integer patientId) 
        	{
        		try
        		{
        			EncounterMasterDto encounterMasterDto = new EncounterMasterDto();
        				encounterMasterDto.setOrganizationId(organizationId);
        				encounterMasterDto.setUnitId(unitId);
        				encounterMasterDto.setPatientId(patientId);
        			return iEncounterMasterService.getListOfEncountersByPatientIdForWalkinSale(encounterMasterDto);
        		}catch(Exception e)
        		{
        		logger.error("method failed", e);
        		}
        		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
        	}
            
            @RequestMapping(value = "/uploadPatientEncounter/document/{orgId}/{unitId}/{patientId}/{encounterId}/{identificationTypeId}", headers=("content-type=multipart/*"), method = RequestMethod.POST)
            public @ResponseBody Response uploadPatientEncounterDocument(@PathVariable("orgId") int orgId,
                                  @PathVariable("unitId") int unitId,
                                  @PathVariable("patientId") int patientId,
                                  @PathVariable("encounterId") int encounterId,
                                  @PathVariable("identificationTypeId") int identificationTypeId,
                                  @RequestParam("file") MultipartFile inputFile) {
        	      try
        	      {
        	    	  
        	    	 UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
        				unitApplicationConfigurationDto.setOrganizationId(orgId);
        				unitApplicationConfigurationDto.setUnitId(unitId);
        				Response appConfigRes = iUnitApplicationBillingConfigurationService.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
        				unitApplicationConfigurationDto = (UnitApplicationConfigurationDto)appConfigRes.getObject();
        	    	  
        	    	  EncounterDocDetailsDto encounterDocDetailsDto = new EncounterDocDetailsDto();
        	    	  encounterDocDetailsDto.setEncounterId(encounterId);
        	    	  encounterDocDetailsDto.setPatientId(patientId);
        	    	  encounterDocDetailsDto.setDocumentTypeId(identificationTypeId);
        	    	  encounterDocDetailsDto.setCreatedBy(unitApplicationConfigurationDto.getWebAppUserId()!=null ? unitApplicationConfigurationDto.getWebAppUserId():null);
        	    	  encounterDocDetailsDto.setUpdatedBy(unitApplicationConfigurationDto.getWebAppUserId()!=null ? unitApplicationConfigurationDto.getWebAppUserId():null);
        	    	  encounterDocDetailsDto.setOrganisationId(orgId);
        	    	  encounterDocDetailsDto.setUnitId(unitId);
        	    	  encounterDocDetailsDto.setVisitTypeId(OP);
        	    	  encounterDocDetailsDto.setAdmissionId(0);
        	    	  encounterDocDetailsDto.setPreauthRequestId(0);
        	    	  encounterDocDetailsDto.setFile(inputFile);
			    	  encounterDocDetailsDto.setExpenseId(0);
			    	  encounterDocDetailsDto.setIncidentReportingMapperId(0);
			    	  encounterDocDetailsDto.setCompanyExpenseId( 0);
			    	  encounterDocDetailsDto.setGrnId( 0);
			    	  encounterDocDetailsDto.setLabSampleDtlsId( 0);
			    	  encounterDocDetailsDto.setUploadDoccumentDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"));
			    	  encounterDocDetailsDto.setFileName(inputFile.getOriginalFilename());
        	         return iEncounterMasterService.saveDocumentAgainstEncounterInsuranceAuthorizationsDoc(encounterDocDetailsDto);
        	       }catch(Exception e)
        	      {
        	      logger.error("method failed", e);
        	      }
        	      return new Response<>(ERROR, null, null, null, null);
              }
        	
            
            @RequestMapping(value = "/getFollowUpDateEncounterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
        	public @ResponseBody Response getFollowUpDateEncounterList(@RequestBody EncounterMasterDto encounterMasterDto) {
        		try {
        			return iEncounterMasterService.getFollowUpDateEncounterList(encounterMasterDto);
        		} catch (Exception e) {
        		logger.error("method failed", e);
        		}
        		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
        	}
            
            @RequestMapping(value = "/updateFollowUpDateEncounterRemark", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
        	public @ResponseBody Response updateFollowUpDateEncounterRemark(@RequestBody EncounterMasterDto encounterMasterDto) {
        		try {
        			return iEncounterMasterService.updateFollowUpDateEncounterRemark(encounterMasterDto);
        		} catch (Exception e) {
        		logger.error("method failed", e);
        		}
        		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
        	}
            
            @RequestMapping(value = "/getLastEncounteredListOfPatient", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
        	public @ResponseBody Response getLastEncounteredListOfPatient(@RequestBody EncounterMasterDto encounterMasterDto) {
        		try {
        			return iEncounterMasterService.getLastEncounteredListOfPatient(encounterMasterDto);
        		} catch (Exception e) {
        		logger.error("method failed", e);
        		}
        		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
        	}
            
            @RequestMapping(value = "/update/latestCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
        	public @ResponseBody Response updateLatestCount(@RequestBody SlotMasterDto dto) {
        		try {
        			return iEncounterMasterService.updateLatestCount(dto);
        		} catch (Exception e) {
        		logger.error("method failed", e);
        		}
        		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
        	}
            
            @RequestMapping(value = "/update/doctor/latestCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
        	public @ResponseBody Response updateDoctorLatestCount(@RequestBody SlotMasterDto dto) {
        		try {
        			return iEncounterMasterService.updateDoctorLatestCount(dto);
        		} catch (Exception e) {
        		logger.error("method failed", e);
        		}
        		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
        	}
            
            @RequestMapping(value = "/getLastEncounterDataOfPatientForWebAPP", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
        	public @ResponseBody Response getLastEncounterDataOfPatientForWebAPP(@RequestBody EncounterMasterDto encounterMasterDto) {
        		try {
        			return iEncounterMasterService.getLastEncounterDataOfPatientForWebAPP(encounterMasterDto);
        		} catch (Exception e) {
        		logger.error("method failed", e);
        		}
        		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
        	}
            
            
            
           
            
            @RequestMapping(value = "/getReferalDetailsByEncounterId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
        	public @ResponseBody Response getReferalDetailsByEncounterId(@RequestBody EncounterMasterDto encounterMasterDto) {
        		try {
        			return iEncounterMasterService.getReferalDetailsByEncounterId(encounterMasterDto);
        		} catch (Exception e) {
        		logger.error("method failed", e);
        		}
        		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
        	}
            
            
       	 @RequestMapping(value="/patientDoc/status/update", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
 		public Response getPatientDocStatusUpdate(@RequestBody PatientDocReqDto patientDocReqDto) {
 			try {
 				return iEncounterMasterService.PatientDocStatusUpdate(patientDocReqDto);
 			}catch (Exception e) {
 			logger.error("getPatientDocStatusUpdate failed", e);
 			}
 			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
 		} 
            
       	@RequestMapping(value="/getOpPatientListByDocIdForVideo", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public Response getOpPatientListByDocIdForVideo(@RequestBody AppointmentStatusSyncDto encounterMasterDto) {
			try {
				return iEncounterMasterService.getOpPatientListByDocIdForVideo(encounterMasterDto);
			}catch (Exception e) {
			logger.error("getOpPatientListByDocIdForVideo failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
       	
       	
       	@RequestMapping(value = "/nabhPatientDoc", headers=("content-type=multipart/*"), method = RequestMethod.POST)
        public @ResponseBody Response saveDocumentAgainstNabhReporting(
       		 @RequestParam("encounterId") Integer encounterId,
       		 @RequestParam("admissionId") Integer admissionId,
       		 @RequestParam("patientId") Integer patientId,
       		 @RequestParam("documentTypeId") Integer documentTypeId,
       		 @RequestParam("createdBy") Integer createdBy,
       		 @RequestParam("updatedBy") Integer updatedBy,
       		 @RequestParam("unitId") Integer unitId,
       		 @RequestParam("organisationId") Integer organisationId,
       		 @RequestParam("expenseId") Integer expenseId,
       		 @RequestParam("companyExpenseId") Integer companyExpenseId,
       		 @RequestParam("grnId") Integer grnId,
       		 @RequestParam("nabhReportingMapperId") Integer nabhReportingMapperId,
       		 @RequestParam(value="labSampleDtlsId", required = false) Integer labSampleDtlsId,
       		 @RequestParam(value="uploadDoccumentDate", required = false) String uploadDoccumentDate,
       		 @RequestParam("files") MultipartFile[] files,
                @RequestParam(value="remark", required = false) String remark,
                @RequestParam("poId") Integer poId,
       		 @RequestParam(value="deathPatientId", required = false) Integer deathPatientId,
       		 @RequestParam(value="createdDate", required = false) String createdDate,
       		 @RequestParam("opbId") Integer opbId,
       		 @RequestParam(value="vendorId", required = false) Integer vendorId
       		 )
            {
   	      try
   	      {
   	    	  EncounterDocDetailsDto encounterDocDetailsDto = new EncounterDocDetailsDto();
   		    	  encounterDocDetailsDto.setEncounterId(encounterId==0?null:encounterId);
   		    	  encounterDocDetailsDto.setAdmissionId(admissionId==0?null:admissionId);
   		    	  encounterDocDetailsDto.setPatientId(patientId==0 ? null : patientId);
   		    	  encounterDocDetailsDto.setDocumentTypeId(documentTypeId);
   		    	  encounterDocDetailsDto.setCreatedBy(createdBy);
   		    	  encounterDocDetailsDto.setUpdatedBy(updatedBy);
   		    	  encounterDocDetailsDto.setOrganisationId(organisationId);
   		    	  encounterDocDetailsDto.setUnitId(unitId);
   		    	  encounterDocDetailsDto.setExpenseId(expenseId !=null ? expenseId : 0);
   		    	  encounterDocDetailsDto.setNabhReportingMapperId(nabhReportingMapperId);
   		    	  encounterDocDetailsDto.setCompanyExpenseId(companyExpenseId !=null ? companyExpenseId : 0);
   		    	  encounterDocDetailsDto.setGrnId(grnId !=null ? grnId : 0);
   		    	  encounterDocDetailsDto.setLabSampleDtlsId(labSampleDtlsId !=null ? labSampleDtlsId : 0);
   		    	  encounterDocDetailsDto.setUploadDoccumentDate(uploadDoccumentDate);
                     encounterDocDetailsDto.setRemark(remark!=null ? remark : null);
                     encounterDocDetailsDto.setPoId(poId!=null ? poId : null);
                     encounterDocDetailsDto.setDeathPatientId(deathPatientId !=null ? deathPatientId : 0);
                     encounterDocDetailsDto.setCreatedDate(createdDate);
                     encounterDocDetailsDto.setOpbId(opbId !=null ? opbId : 0);
                     encounterDocDetailsDto.setVendorId(vendorId !=null ? vendorId:0);
   	         return iEncounterMasterService.saveDocumentAgainstNabh(encounterDocDetailsDto,files);
   	       }catch (HibernateException e){
   	       logger.error("method failed", e);
  			 String errorMsg =  EncounterController.class.getName() + ":" +e.getMessage();
  			 return new Response<>(ERROR, ERROR_CODE, errorMsg, null, null);
   	       }
          }
       	
       	
       	@RequestMapping(value="/patientDoc/details/nabh", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public Response getPatientDocDetailsForNabh(@RequestBody PatientDocReqDto patientDocReqDto) {
			try {
				return iEncounterMasterService.getPatientDocDetailsForNabh(patientDocReqDto);
			}catch (HibernateException e){
			logger.error("getPatientDocDetailsForNabh failed", e);
				 String errorMsg =  EncounterController.class.getName() + ":" +e.getMessage();
				 return new Response<>(ERROR, ERROR_CODE, errorMsg, null, null);
			}
		}

	@RequestMapping(value = "/incidentPatientDoc", headers=("content-type=multipart/*"), method = RequestMethod.POST)
	public @ResponseBody Response saveDocumentAgainstIncidentReporting(
			@RequestParam("encounterId") Integer encounterId,
			@RequestParam("admissionId") Integer admissionId,
			@RequestParam("patientId") Integer patientId,
			@RequestParam("documentTypeId") Integer documentTypeId,
			@RequestParam("createdBy") Integer createdBy,
			@RequestParam("updatedBy") Integer updatedBy,
			@RequestParam("unitId") Integer unitId,
			@RequestParam("organisationId") Integer organisationId,
			@RequestParam("expenseId") Integer expenseId,
			@RequestParam("companyExpenseId") Integer companyExpenseId,
			@RequestParam("grnId") Integer grnId,
			@RequestParam("incidentReportingMapperId") Integer incidentReportingMapperId,
			@RequestParam(value="labSampleDtlsId", required = false) Integer labSampleDtlsId,
			@RequestParam(value="uploadDoccumentDate", required = false) String uploadDoccumentDate,
			@RequestParam("files") MultipartFile[] files,
			@RequestParam(value="remark", required = false) String remark,
			@RequestParam("poId") Integer poId,
			@RequestParam(value="deathPatientId", required = false) Integer deathPatientId,
			@RequestParam(value="createdDate", required = false) String createdDate,
			@RequestParam("opbId") Integer opbId,
			@RequestParam(value="vendorId", required = false) Integer vendorId
	)
	{
		try
		{
			EncounterDocDetailsDto encounterDocDetailsDto = new EncounterDocDetailsDto();
			encounterDocDetailsDto.setEncounterId(encounterId==0?null:encounterId);
			encounterDocDetailsDto.setAdmissionId(admissionId==0?null:admissionId);
			encounterDocDetailsDto.setPatientId(patientId==0 ? null : patientId);
			encounterDocDetailsDto.setDocumentTypeId(documentTypeId);
			encounterDocDetailsDto.setCreatedBy(createdBy);
			encounterDocDetailsDto.setUpdatedBy(updatedBy);
			encounterDocDetailsDto.setOrganisationId(organisationId);
			encounterDocDetailsDto.setUnitId(unitId);
			encounterDocDetailsDto.setExpenseId(expenseId !=null ? expenseId : 0);
			encounterDocDetailsDto.setIncidentReportingMapperId(incidentReportingMapperId);
			encounterDocDetailsDto.setCompanyExpenseId(companyExpenseId !=null ? companyExpenseId : 0);
			encounterDocDetailsDto.setGrnId(grnId !=null ? grnId : 0);
			encounterDocDetailsDto.setLabSampleDtlsId(labSampleDtlsId !=null ? labSampleDtlsId : 0);
			encounterDocDetailsDto.setUploadDoccumentDate(uploadDoccumentDate);
			encounterDocDetailsDto.setRemark(remark!=null ? remark : null);
			encounterDocDetailsDto.setPoId(poId!=null ? poId : null);
			encounterDocDetailsDto.setDeathPatientId(deathPatientId !=null ? deathPatientId : 0);
			encounterDocDetailsDto.setCreatedDate(createdDate);
			encounterDocDetailsDto.setOpbId(opbId !=null ? opbId : 0);
			encounterDocDetailsDto.setVendorId(vendorId !=null ? vendorId:0);
			return iEncounterMasterService.saveDocumentAgainstIncident(encounterDocDetailsDto,files);
		}catch (HibernateException e){
		logger.error("method failed", e);
			String errorMsg =  EncounterController.class.getName() + ":" +e.getMessage();
			return new Response<>(ERROR, ERROR_CODE, errorMsg, null, null);
		}
	}


	@RequestMapping(value="/patientDoc/details/incident", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getPatientDocDetailsForIncident(@RequestBody PatientDocReqDto patientDocReqDto) {
		try {
			return iEncounterMasterService.getPatientDocDetailsForIncident(patientDocReqDto);
		}catch (HibernateException e){
		logger.error("getPatientDocDetailsForIncident failed", e);
			String errorMsg =  EncounterController.class.getName() + ":" +e.getMessage();
			return new Response<>(ERROR, ERROR_CODE, errorMsg, null, null);
		}
	}

	@RequestMapping(value="/patientDoc/analyzeReportsWithAI", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response analyzeReportsWithAI(@RequestBody EncounterDocDetailsDto encounterDocDetailsDto, HttpServletRequest request) {
		try {
			return iEncounterMasterService.analyzeReportsWithAI(request, encounterDocDetailsDto);
		}catch (HibernateException e){
		logger.error("analyzeReportsWithAI failed", e);
			String errorMsg =  EncounterController.class.getName() + ":" +e.getMessage();
			return new Response<>(ERROR, ERROR_CODE, errorMsg, null, null);
		}
	}

	@RequestMapping(value="/patientDoc/saveAnalyzedReport", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response saveAnalyzedReport(HttpServletRequest request) throws IOException {
		try {
			StringBuilder jsonBuilder = new StringBuilder();
			try (BufferedReader reader = request.getReader()) {
				String line;
				while ((line = reader.readLine()) != null) {
					jsonBuilder.append(line);
				}
			}

			ObjectMapper objectMapper = new ObjectMapper();

			String rawJson = jsonBuilder.toString();
			rawJson = rawJson.replaceAll("\"status\":" , "\"videoStatus\":");
			EncounterDocDetailsDto encounterDocDetailsDto = objectMapper.readValue(rawJson, EncounterDocDetailsDto.class);

			System.out.println("raw Json : " + rawJson);
			return iEncounterMasterService.saveAnalyzedReport(encounterDocDetailsDto, rawJson);

		}catch (HibernateException e){
		logger.error("saveAnalyzedReport failed", e);
			String errorMsg =  EncounterController.class.getName() + ":" +e.getMessage();
			return new Response<>(ERROR, ERROR_CODE, errorMsg, null, null);
		}
	}

	@RequestMapping(value="/patientDoc/getAnalyzedReport/{encounterDocId}", method=RequestMethod.GET)
	public Response getAnalyzedReport(@PathVariable("encounterDocId") Integer encounterDocId) {
		try {
			EncounterDocDetailsDto encounterDocDetailsDto = new EncounterDocDetailsDto();
			encounterDocDetailsDto.setEncounterDocId(encounterDocId);

			return iEncounterMasterService.getAnalyzedReport(encounterDocDetailsDto);

		}catch (HibernateException e){
		logger.error("getAnalyzedReport failed", e);
			String errorMsg =  EncounterController.class.getName() + ":" +e.getMessage();
			return new Response<>(ERROR, ERROR_CODE, errorMsg, null, null);
		}
	}

	@RequestMapping(value="/doctorDesk/getAugmentDoctorList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getAugmentDoctorList(@RequestBody EncounterMasterDto encounterMasterDto) {
		try {
			return iEncounterMasterService.getAugmentDoctorList(encounterMasterDto);
		}catch (Exception e) {
		logger.error("getAugmentDoctorList failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/doctorDesk/updateAugmentDoctor", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response updateAugmentDoctor(@RequestBody EncounterMasterDto encounterMasterDto) {
		try {
			return iEncounterMasterService.updateAugmentDoctor(encounterMasterDto);
		}catch (Exception e) {
		logger.error("updateAugmentDoctor failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


}
