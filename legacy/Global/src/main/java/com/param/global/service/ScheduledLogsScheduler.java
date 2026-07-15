package com.param.global.service;

import java.net.URI;
import java.util.List;

import javax.transaction.Transactional;

import com.param.global.common.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.internal.StringMap;
import com.param.global.dao.IAppointmentVitalMapperDAOForSync;
import com.param.global.dao.IDoctorMasterDao;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dao.IModalityMasterDao;
import com.param.global.dao.IPatientRegistrationDao;
import com.param.global.dao.IScheduleLogsDao;
import com.param.global.doctor.dao.IDoctorScheduleDao;
import com.param.global.dto.MyLifeConnectionLinkDto;
import com.param.global.dto.ScheduleLogsResDto;
import com.param.global.dto.sync.AdmissionAndDischargeSyncDto;
import com.param.global.dto.sync.AppointmentDetailsRealTimeDto;
import com.param.global.dto.sync.AppointmentSyncSchedulingDto;
import com.param.global.dto.sync.ChannelHospitalMapperDto;
import com.param.global.dto.sync.DoctorChargesSyncDto;
import com.param.global.dto.sync.DoctorScheduleDeleteReqDto;
import com.param.global.dto.sync.DoctorScheduleSyncDto;
import com.param.global.dto.sync.DoctorScheduleVisitSyncDto;
import com.param.global.dto.sync.DoctorSyncDto;
import com.param.global.dto.sync.GlobalHospitalSyncDto;
import com.param.global.dto.sync.GlobalUnitSyncDto;
import com.param.global.dto.sync.ModalityMasterDtoSync;
import com.param.global.dto.sync.OrganisationMasterDtoSync;
import com.param.global.dto.sync.PatientDocumentSyncDto;
import com.param.global.dto.sync.PatientSyncDto;
import com.param.global.dto.sync.ServiceappoServiceMpprSyncDto;
import com.param.global.dto.sync.UnitMasterDtoSync;
import com.param.global.opd.dao.IAppointmentMasterDao;
import com.param.global.opd.dao.IEncounterMasterDao;
import com.param.global.opd.service.IAppointmentMasterService;
import com.param.global.opd.service.IAppointmentSchedulingService;
import com.param.global.org.dao.IGlobalOrganisationMasterDao;
import com.param.utility.TimeZoneComponent;
import org.apache.log4j.Logger;

@Component
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ScheduledLogsScheduler  implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(ScheduledLogsScheduler.class);


	@Autowired
	IScheduleLogsDao iScheduleLogsDao;
	@Autowired
	IPatientRegistrationDao iPatientMasterDao;
	@Autowired
	IModalityMasterDao iModalityMasterDao;
	@Autowired
	IGlobalUnitMasterDao iUnitMasterDao; 
	@Autowired
	IGlobalOrganisationMasterDao iGlobalOrganisationMasterDao;
	@Autowired
	IEncounterMasterDao iEncounterMasterDao; 
	@Autowired
	IDoctorMasterDao iDoctorMasterDao;
	@Autowired
	IAppointmentVitalMapperDAOForSync iAppointmentVitalMapperDAOForSync;	
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	private Gson gson;
	@Autowired
	private IDoctorScheduleDao iDoctorScheduleDao;
	@Autowired
	private IAppointmentMasterDao iAppointmentMasterDao;
	@Autowired
	private IAppointmentMasterService iAppointmentMasterService;

	@Autowired
	IServiceIdentityDao iServiceIdentityDao;

	@Autowired
	private ServerIdentityService serverIdentityService;

	private static Character isExecuted='N';
	private static Character isLabReportSync='N';
	
	@Transactional
   @Scheduled(fixedDelay = 120000, initialDelay = 60000)
	public  void syncSchedulingLogs() {

		try {

			String serverId = serverIdentityService.getServerId();
			Boolean check = iServiceIdentityDao.checkServiceIdAvailable(serverId);

			if(check) {


				String Global_Scheduling_URI = null;
				String MY_LIFE_URI = null;
				Response res = iScheduleLogsDao.getConnectionStringForMyLife();
				MyLifeConnectionLinkDto connectionLinkDto = (MyLifeConnectionLinkDto) res.getObject();
				if (connectionLinkDto != null && connectionLinkDto.getMyLifeLink() != null && connectionLinkDto.getMyLifeLink() != "" && connectionLinkDto.getGlobalSchedulingLink() != null && connectionLinkDto.getGlobalSchedulingLink() != "") {
					Global_Scheduling_URI = connectionLinkDto.getGlobalSchedulingLink();
					MY_LIFE_URI = connectionLinkDto.getMyLifeLink();
				}

				//Global_Scheduling_URI = "https://d3b0-219-91-251-54.ngrok-free.app/MyLifeGlobal/api";
				//MY_LIFE_URI = "https://d3b0-219-91-251-54.ngrok-free.app/MyLifeGlobal/api";
				//Global_Scheduling_URI = "http://localhost:9091/MyLifeGlobal/api";
//				MY_LIFE_URI = "http://localhost:9091/MyLifeGlobal/api";
//				Global_Scheduling_URI = "http://localhost:9091/MyLifeGlobal/api";
				if (Global_Scheduling_URI != null && MY_LIFE_URI != null) {
					if (isExecuted == 'N') {
						isExecuted = 'Y';
						Response pendingSchedule = iScheduleLogsDao.getPendingScheduleLogs();

						URI url = null;
						Response response = null;
						Response commonResponse = null;
						if (pendingSchedule.getListObject() != null && pendingSchedule.getListObject().size() > 0) {

							List<ScheduleLogsResDto> listScheduleLogsDto = pendingSchedule.getListObject();

							for (ScheduleLogsResDto logsDto : listScheduleLogsDto) {

								String table = logsDto.getTableName();
								String action = logsDto.getAction();
								switch (table) {
									case ScheduleTables.PATIENT_MASTER:
										try {
											response = iPatientMasterDao.getPatientByIdForSync(logsDto.getRecordId());
											PatientSyncDto patient = (PatientSyncDto) response.getObject();
											patient.setApplicationId(2);
											//String patientJson=new Gson().toJson(patient);
											if (patient != null) {
												switch (action) {
													case ScheduleActions.INSERT:
														url = URI.create(MY_LIFE_URI + "/sync/patients/realtime");
														HttpHeaders headers = new HttpHeaders();
														//headers.set("channelId", (patient.getChannelId() != null && patient.getChannelId() != 0) ? patient.getChannelId().toString():"1");
														headers.set("channelId", (logsDto.getChannelId() != null && logsDto.getChannelId() != 0) ? logsDto.getChannelId().toString() : "1");
														patient.setPatientFromHIS(2);
														HttpEntity<PatientSyncDto> request = new HttpEntity<>(patient, headers);
														String stringResponse = restTemplate.postForObject(url, request, String.class);
														commonResponse = gson.fromJson(stringResponse, Response.class);
														if (commonResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS)) {
															if (commonResponse.getObject() != null)
																iPatientMasterDao.updateUuidByPatient(logsDto.getRecordId(), (String) commonResponse.getObject());
															iScheduleLogsDao.markScheduleLogsCompleted(logsDto.getScheduleLogsId());
														} else {
															iScheduleLogsDao.markScheduleLogsError(logsDto.getScheduleLogsId());
														}
														break;
												}
											}
										} catch (Exception e) {
										logger.error("syncSchedulingLogs PATIENT_MASTER sync failed for scheduleLogsId " + logsDto.getScheduleLogsId(), e);

										}
										break;
									case ScheduleTables.MODALITY_MASTER:
										try {
											response = iModalityMasterDao.getModalityByIdForSync(logsDto.getRecordId());
											List<ModalityMasterDtoSync> modalityMasterDtoList = response.getListObject();
											switch (action) {
												case ScheduleActions.INSERT:
													for (ModalityMasterDtoSync modalityDto : modalityMasterDtoList) {
														url = URI.create(Global_Scheduling_URI + "/modality");
//									HttpHeaders headers = new HttpHeaders();
//									headers.set("channelId", (modalityDto.getChannelId() != null && modalityDto.getChannelId() != 0) ? modalityDto.getChannelId().toString():"1");
//									HttpEntity<ModalityMasterDtoSync> request = new HttpEntity<>(modalityDto, headers);
														String stringResponse = restTemplate.postForObject(url, modalityDto, String.class);
														commonResponse = gson.fromJson(stringResponse, Response.class);
														if (commonResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS))
															iScheduleLogsDao.markScheduleLogsCompleted(logsDto.getScheduleLogsId());
														else
															iScheduleLogsDao.markScheduleLogsError(logsDto.getScheduleLogsId());
													}
													break;
											}
										} catch (Exception e) {
										logger.error("syncSchedulingLogs MODALITY_MASTER sync failed for scheduleLogsId " + logsDto.getScheduleLogsId(), e);

										}
										break;
									case ScheduleTables.UNIT_MASTER:
										try {
											response = iUnitMasterDao.getUnitMasterForSyncById(logsDto.getRecordId());
											UnitMasterDtoSync unit = (UnitMasterDtoSync) response.getObject();
											if (unit != null) {
												switch (action) {
													case ScheduleActions.INSERT:
														GlobalUnitSyncDto globalUnitSyncDto = new GlobalUnitSyncDto();
														globalUnitSyncDto.setApplicationId(2);
														globalUnitSyncDto.setCreatedDate(unit.getCreatedDate());
														globalUnitSyncDto.setLocalId(unit.getLocalUnitId());
														globalUnitSyncDto.setStatus(unit.getStatus());
														globalUnitSyncDto.setUnitName(unit.getUnitName());
														globalUnitSyncDto.setCountryId(1);
														globalUnitSyncDto.setCurrency("INR");
														globalUnitSyncDto.setUnitAddress(unit.getAddress());
														globalUnitSyncDto.setUnitContactNumber(unit.getUnitContactNumber());
														globalUnitSyncDto.setUnitEmail(unit.getUnitEmail());
														globalUnitSyncDto.setIsPreOrPostBilling('Y');
														if (unit.getGlobalUnitId() != null) {
															globalUnitSyncDto.setGlobalUnitId(unit.getGlobalUnitId());
														}
														unit.setCountryId(1);
														unit.setCurrency("INR");
														unit.setUnitAddress(unit.getAddress());
														unit.setIsPreOrPostBilling('Y');
														url = URI.create(Global_Scheduling_URI + "/unit");
														HttpHeaders headers = new HttpHeaders();
														headers.set("channelId", (logsDto.getChannelId() != null && logsDto.getChannelId() != 0) ? logsDto.getChannelId().toString() : "1");
														HttpEntity<UnitMasterDtoSync> request = new HttpEntity<>(unit, headers);
														ObjectMapper objectMapper = new ObjectMapper();
														String json = objectMapper.writeValueAsString(unit);
														System.out.println("Unit JSON is:" + json);
														//String stringResponse = restTemplate.postForObject(url, unit, String.class);
														String stringResponse = restTemplate.postForObject(url, request, String.class);
														commonResponse = gson.fromJson(stringResponse, Response.class);

														Response tempResponse = new Response<>(ICommonConstants.ERROR, null, null, null, null);

														if (commonResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS)) {
															StringMap unitMap = (StringMap) commonResponse.getObject();

															Double globalUnitId = (Double) unitMap.get("unitId");

															iUnitMasterDao.updateGlobalUnitId(unit.getLocalUnitId(), globalUnitId.intValue());

															globalUnitSyncDto.setGlobalUnitId(globalUnitId.intValue());
															globalUnitSyncDto.setIsPreOrPostBilling('Y');
															unit.setIsPreOrPostBilling('Y');

															//url = URI.create(MY_LIFE_URI + "/sync/unit");
//										HttpHeaders headers1 = new HttpHeaders();
//										headers1.set("channelId", (unit.getChannelId() != null && unit.getChannelId() != 0) ? unit.getChannelId().toString():"1");
//										HttpEntity<UnitMasterDtoSync> request1 = new HttpEntity<>(unit, headers1);
															//String tempStrResponse = restTemplate.postForObject(url, unit, String.class);
															//tempResponse = gson.fromJson(tempStrResponse, Response.class);

															if (commonResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS))
																iScheduleLogsDao.markScheduleLogsCompleted(logsDto.getScheduleLogsId());
															else
																iScheduleLogsDao.markScheduleLogsError(logsDto.getScheduleLogsId());
														} else if (commonResponse.getCode().equalsIgnoreCase(ICommonConstants.ALREADY_EXIST_CODE)) {
															iScheduleLogsDao.markScheduleLogsCompleted(logsDto.getScheduleLogsId());
														}
														break;
												}
											}
										} catch (Exception e) {
										logger.error("syncSchedulingLogs UNIT_MASTER sync failed for scheduleLogsId " + logsDto.getScheduleLogsId(), e);

										}
										break;
									case ScheduleTables.ORGANIZATION_MASTER:
										try {
											response = iGlobalOrganisationMasterDao.getGlobalOrganisationMasterForSyncById(logsDto.getRecordId());
											OrganisationMasterDtoSync org = (OrganisationMasterDtoSync) response.getObject();
											if (org != null) {
												switch (action) {
													case ScheduleActions.INSERT:
														GlobalHospitalSyncDto hospSyncDto = new GlobalHospitalSyncDto();
														hospSyncDto.setApplicationId(2);
														hospSyncDto.setCreatedDate(org.getCreatedDate());
														hospSyncDto.setLocalId(org.getLocalOrganisationId());
														hospSyncDto.setOrganisationName(org.getOrganisationName());
														hospSyncDto.setStatus(org.getStatus());
														hospSyncDto.setVisibility("Public");
														if (org.getGlobalOrgId() != null) {
															hospSyncDto.setGlobalOrgId(org.getGlobalOrgId());
														}
														url = URI.create(Global_Scheduling_URI + "/organisation");
														HttpHeaders headers = new HttpHeaders();
														org.setVisibility("Public");
														headers.set("channelId", (logsDto.getChannelId() != null && logsDto.getChannelId() != 0) ? logsDto.getChannelId().toString() : "1");
														ObjectMapper objectMapper = new ObjectMapper();
														String json = objectMapper.writeValueAsString(org);
														System.out.println("org JSON is:" + json);
														HttpEntity<OrganisationMasterDtoSync> request = new HttpEntity<>(org, headers);
														String stringResponse = restTemplate.postForObject(url, request, String.class);
														//String stringResponse = restTemplate.postForObject(url, org, String.class);
														commonResponse = gson.fromJson(stringResponse, Response.class);

														Response tempResponse = new Response<>(ICommonConstants.ERROR, null, null, null, null);

														if (commonResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS) && commonResponse.getObject() != null) {
															StringMap orgMap = (StringMap) commonResponse.getObject();

															Double globalOrgId = (Double) orgMap.get("organisationId");

															iGlobalOrganisationMasterDao.updateGlobalOrgId(org.getLocalOrganisationId(), globalOrgId.intValue());

															hospSyncDto.setGlobalOrgId(globalOrgId.intValue());
															hospSyncDto.setVisibility("Public");
//										url = URI.create(MY_LIFE_URI + "/sync/hospital");
//										HttpHeaders headers1 = new HttpHeaders();
//										headers1.set("channelId", (logsDto.getChannelId() != null && logsDto.getChannelId() != 0) ? logsDto.getChannelId().toString():"1");
//										HttpEntity<GlobalHospitalSyncDto> request1 = new HttpEntity<>(hospSyncDto, headers1);
//										 ObjectMapper objectMapper2 = new ObjectMapper();
//									        String json2 = objectMapper2.writeValueAsString(hospSyncDto);
//									        System.out.println("hospSyncDto JSON is:"+json2);
//										String tempStrResponse = restTemplate.postForObject(url, hospSyncDto, String.class);
//										tempResponse = gson.fromJson(tempStrResponse, Response.class);
//										
//										if(commonResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS) && tempResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS))
//											iScheduleLogsDao.markScheduleLogsCompleted(logsDto.getScheduleLogsId());
//										else
//											iScheduleLogsDao.markScheduleLogsError(logsDto.getScheduleLogsId());

															//channel hospital mapper sync call
															ChannelHospitalMapperDto dto = new ChannelHospitalMapperDto();
															//String array = logsDto.getPriority();
															dto.setChannelIdArray("1#");
															dto.setHospitalId(globalOrgId.intValue());
															//dto.setChannelId(1);
															dto.setApplicationId(2);
															String stringResponse1 = null;
															Response channelResponseFromMyLife = null;
															Response channelResponseFromGlobal = null;
															// System.out.println(gson.toJson(docSchedulingDto));
															url = URI.create(Global_Scheduling_URI + "/channelHospitalMapper");
															ObjectMapper objectMapper3 = new ObjectMapper();
															String json3 = objectMapper3.writeValueAsString(dto);
															System.out.println("hospSyncDto JSON is:" + json3);
															HttpHeaders headers2 = new HttpHeaders();
															headers2.set("channelId", (logsDto.getChannelId() != null && logsDto.getChannelId() != 0) ? logsDto.getChannelId().toString() : "1");
															HttpEntity<ChannelHospitalMapperDto> request2 = new HttpEntity<>(dto, headers2);
															stringResponse1 = restTemplate.postForObject(url, request2, String.class);
															//stringResponse1 = restTemplate.postForObject(url, dto, String.class);
															channelResponseFromGlobal = gson.fromJson(stringResponse1, Response.class);

															channelResponseFromMyLife = new Response<>(ICommonConstants.ERROR, null, null, null, null);
															if (channelResponseFromGlobal.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS)) {
																url = URI.create(MY_LIFE_URI + "/channelHospitalMapper");
																HttpHeaders headers4 = new HttpHeaders();
																headers4.set("channelId", (logsDto.getChannelId() != null && logsDto.getChannelId() != 0) ? logsDto.getChannelId().toString() : "1");
																HttpEntity<ChannelHospitalMapperDto> request4 = new HttpEntity<>(dto, headers4);
																String resFromMyLifeChannel = restTemplate.postForObject(url, request4, String.class);
																channelResponseFromMyLife = gson.fromJson(resFromMyLifeChannel, Response.class);
															}
															if (commonResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS))
																iScheduleLogsDao.markScheduleLogsCompleted(logsDto.getScheduleLogsId());
															else
																iScheduleLogsDao.markScheduleLogsError(logsDto.getScheduleLogsId());

														} else if (commonResponse.getCode().equalsIgnoreCase(ICommonConstants.ALREADY_EXIST_CODE)) {
															iScheduleLogsDao.markScheduleLogsCompleted(logsDto.getScheduleLogsId());
														}

														break;
												}
											}
										} catch (Exception e) {
										logger.error("syncSchedulingLogs ORGANIZATION_MASTER sync failed for scheduleLogsId " + logsDto.getScheduleLogsId(), e);
										}
										break;
									case ScheduleTables.DOCTOR_MASTER:
										try {
											response = iDoctorMasterDao.getDoctorForSyncById(logsDto.getRecordId());
											List<DoctorSyncDto> docUnitAndSpecialityList = response.getListObject();
											if (docUnitAndSpecialityList != null && docUnitAndSpecialityList.size() > 0) {
												switch (action) {
													case ScheduleActions.INSERT:
														//doc.setDoctorId(null);
														//url = URI.create(Global_Scheduling_URI + "/doctor");
														//String doctorJson=new Gson().toJson(docUnitAndSpecialityList);
														url = URI.create(Global_Scheduling_URI + "/saveDoctorUnitAndSpeciality");
//										HttpHeaders headers3 = new HttpHeaders();
//										headers3.set("channelId", (logsDto.getChannelId() != null && logsDto.getChannelId() != 0) ? logsDto.getChannelId().toString():"1");
//									HttpEntity<DoctorSyncDto> request3 = new HttpEntity<>(docUnitAndSpecialityList, headers3);
//									
//									//HttpEntity<DoctorSyncDto> request = new HttpEntity<>(docUnitAndSpecialityList, headers3);
//									 ObjectMapper objectMapper = new ObjectMapper();
//								        String json = objectMapper.writeValueAsString(docUnitAndSpecialityList);
//								        System.out.println("Unit JSON is:"+json);
//									//String stringResponse = restTemplate.postForObject(url, unit, String.class);
//									String stringResponse = restTemplate.postForObject(url, request3, String.class);

														HttpHeaders headers3 = new HttpHeaders();
														headers3.set("channelId", (logsDto.getChannelId() != null && logsDto.getChannelId() != 0) ? logsDto.getChannelId().toString() : "1");

														// Assuming docUnitAndSpecialityList is a List<DoctorSyncDto>
														HttpEntity<List<DoctorSyncDto>> request3 = new HttpEntity<>(docUnitAndSpecialityList, headers3);

														ObjectMapper objectMapper = new ObjectMapper();
														String json = objectMapper.writeValueAsString(docUnitAndSpecialityList);
														System.out.println("Doctor JSON is:" + json);

														String stringResponse = restTemplate.postForObject(url, request3, String.class);
                                                        System.out.println("stringResponse:" + stringResponse);
														//String stringResponse = restTemplate.postForObject(url, docUnitAndSpecialityList, String.class);
														commonResponse = gson.fromJson(stringResponse, Response.class);
														if (commonResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS)) {
															System.out.println("Double-first:");
															Double globalDocId = (Double) commonResponse.getObject();
															System.out.println("Double-docId:"+globalDocId);
															iDoctorMasterDao.updateGlobalDocId(logsDto.getRecordId(), globalDocId.intValue());
															System.out.println("BeforeScheduleUdate:");
															iScheduleLogsDao.markScheduleLogsCompleted(logsDto.getScheduleLogsId());
															System.out.println("AfterScheduleUdate:");
														} else
															iScheduleLogsDao.markScheduleLogsError(logsDto.getScheduleLogsId());
														break;
												}
											}
										} catch (Exception e) {
										logger.error("syncSchedulingLogs DOCTOR_MASTER sync failed for scheduleLogsId " + logsDto.getScheduleLogsId(), e);
										}
										break;

									case ScheduleTables.APPOINTMENT_MASTER:
										try {
											response = iAppointmentMasterDao.getDoctorAppointmentToSync(logsDto.getRecordId());
											AppointmentSyncSchedulingDto appointment = (AppointmentSyncSchedulingDto) response.getObject();
											if (appointment != null) {
												switch (action) {
													case ScheduleActions.INSERT:
														appointment.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(0), "dd-M-yyyy"));
														appointment.setApplicationFlag(true);
														String stringResponse = null;
														if (appointment.getDirectEncounter() == null) {
															appointment.setDirectEncounter('N');
														}
														appointment.setAppointmentStatusId(logsDto.getAppointmentStatusId());
														//String appointmentJson=new Gson().toJson(appointment);
														if (appointment.getAppointmentStatusId() == AppointmentStatusMasterConstant.PENDING ||
																(appointment.getDirectEncounter().equals('Y') && logsDto.getAppointmentStatusId() == APPOINTMENTSTATUSID1)) {
															//url = URI.create(Global_Scheduling_URI + "/appointment/bookHisAppointment");
															url = URI.create(Global_Scheduling_URI + "/appointment/bookAppointment");
															ObjectMapper objectMapper = new ObjectMapper();
															String json = objectMapper.writeValueAsString(appointment);
															System.out.println("Appointment SChedule JSON is:" + json);
															HttpHeaders headers = new HttpHeaders();
															String token = SmsUtility.generateJWTTokenMylife(connectionLinkDto.getGlobalSchedulingLink());
															headers.set("Authorization", "Bearer " + token);
															headers.set("userName", SmsUtility.USER_TOKEN_MYLIFE);
															headers.set("channelId", (logsDto.getChannelId() != null && logsDto.getChannelId() != 0) ? logsDto.getChannelId().toString() : "1");
															HttpEntity<AppointmentSyncSchedulingDto> request = new HttpEntity<>(appointment, headers);
															//stringResponse = restTemplate.postForObject(url, appointment, String.class);
															stringResponse = restTemplate.postForObject(url, request, String.class);
														} else {
															ObjectMapper objectMapper = new ObjectMapper();
															String json = objectMapper.writeValueAsString(appointment);
															System.out.println("Appointment RESChedule JSON is:" + json);
															url = URI.create(Global_Scheduling_URI + "/appointment/cancelorRescheduleAppointment");
															//HttpEntity<AppointmentSyncSchedulingDto> entity = new HttpEntity<AppointmentSyncSchedulingDto>(appointment);
															HttpHeaders headers = new HttpHeaders();
															headers.set("channelId", (logsDto.getChannelId() != null && logsDto.getChannelId() != 0) ? logsDto.getChannelId().toString() : "1");
															HttpEntity<AppointmentSyncSchedulingDto> entity = new HttpEntity<>(appointment, headers);

															ResponseEntity<String> rescheduleResponse = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
															stringResponse = rescheduleResponse.getBody();
														}
														commonResponse = gson.fromJson(stringResponse, Response.class);
														if (commonResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS))
															iScheduleLogsDao.markScheduleLogsCompleted(logsDto.getScheduleLogsId());
														else
															iScheduleLogsDao.markScheduleLogsError(logsDto.getScheduleLogsId());
														break;
												}
											}
										} catch (Exception e) {
										logger.error("syncSchedulingLogs APPOINTMENT_MASTER sync failed for scheduleLogsId " + logsDto.getScheduleLogsId(), e);

										}
										break;

									case ScheduleTables.DOCTOR_SCHEDULE:
										try {
											String stringResponse = null;
											response = iDoctorScheduleDao.getDoctorScheduleForSync(logsDto.getRecordId());
											DoctorScheduleSyncDto schedule = (DoctorScheduleSyncDto) response.getObject();
											List<DoctorScheduleVisitSyncDto> doctorScheduleVisitSyncList = null;
											if (schedule != null && schedule.getType() == 1) {
												doctorScheduleVisitSyncList = iScheduleLogsDao.getScheduleDetailByIdForGlobalScheduling(logsDto.getRecordId()).getListObject();
												schedule.setListDoctorScheduleVisitSyncDto(doctorScheduleVisitSyncList);
											}
											//String doctorSchedule=new Gson().toJson(schedule);
											if (schedule != null) {
												switch (action) {
													case ScheduleActions.INSERT:

														url = URI.create(Global_Scheduling_URI + "/doctor/schedulePlusVisitDetails");
														HttpHeaders headers = new HttpHeaders();
														headers.set("channelId", (logsDto.getChannelId() != null && logsDto.getChannelId() != 0) ? logsDto.getChannelId().toString() : "1");
														HttpEntity<DoctorScheduleSyncDto> request = new HttpEntity<>(schedule, headers);
														stringResponse = restTemplate.postForObject(url, request, String.class);
														commonResponse = gson.fromJson(stringResponse, Response.class);
														if (commonResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS))
															iScheduleLogsDao.markScheduleLogsCompleted(logsDto.getScheduleLogsId());
														else
															iScheduleLogsDao.markScheduleLogsError(logsDto.getScheduleLogsId());
														break;
													case ScheduleActions.DELETE:

														DoctorScheduleDeleteReqDto dto = new DoctorScheduleDeleteReqDto();
														dto.setApplicationId(schedule.getApplicationId());
														dto.setGlobalOrgId(schedule.getGlobalOrgId());
														dto.setGlobalUnitId(schedule.getGlobalUnitId());
														dto.setGlobalDocId(schedule.getDoctorId());
														dto.setFromDate(schedule.getFromDate());
														dto.setToDate(schedule.getToDate());
														dto.setDepartmentId(schedule.getDepartmentId());
														dto.setScheduleDate(schedule.getScheduleDate());
														dto.setDeleteAllSlots(schedule.getDeleteAllSlots());
														dto.setFromTime(schedule.getFromTime());
														dto.setToTime(schedule.getToTime());
														url = URI.create(Global_Scheduling_URI + "/doctor/schedule/delete");
														HttpHeaders headers1 = new HttpHeaders();
														headers1.set("channelId", (logsDto.getChannelId() != null && logsDto.getChannelId() != 0) ? logsDto.getChannelId().toString() : "1");
														ObjectMapper objectMapper = new ObjectMapper();
														String json = objectMapper.writeValueAsString(dto);
														System.out.println("Delete SChedule JSON is:" + json);
														HttpEntity<DoctorScheduleDeleteReqDto> request1 = new HttpEntity<>(dto, headers1);
														stringResponse = restTemplate.postForObject(url, request1, String.class);
														commonResponse = gson.fromJson(stringResponse, Response.class);
														if (commonResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS))
															iScheduleLogsDao.markScheduleLogsCompleted(logsDto.getScheduleLogsId());
														else
															iScheduleLogsDao.markScheduleLogsError(logsDto.getScheduleLogsId());

														break;

													case ScheduleActions.ACTIVE:

														DoctorScheduleDeleteReqDto activateSlot = new DoctorScheduleDeleteReqDto();
														activateSlot.setApplicationId(schedule.getApplicationId());
														activateSlot.setGlobalOrgId(schedule.getGlobalOrgId());
														activateSlot.setGlobalUnitId(schedule.getGlobalUnitId());
														activateSlot.setGlobalDocId(schedule.getDoctorId());
														activateSlot.setFromDate(schedule.getFromDate());
														activateSlot.setToDate(schedule.getToDate());
														activateSlot.setDepartmentId(schedule.getDepartmentId());
														activateSlot.setScheduleDate(schedule.getScheduleDate());
														activateSlot.setDeleteAllSlots(schedule.getDeleteAllSlots());
														activateSlot.setFromTime(schedule.getFromTime());
														activateSlot.setToTime(schedule.getToTime());
														url = URI.create(Global_Scheduling_URI + "/doctor/schedule/activate");
//										HttpHeaders headers3 = new HttpHeaders();
//										headers3.set("channelId", (activateSlot.getChannelId() != null && activateSlot.getChannelId() != 0) ? activateSlot.getChannelId().toString():"1");
//										HttpEntity<DoctorScheduleDeleteReqDto> request2 = new HttpEntity<>(activateSlot, headers3);
														stringResponse = restTemplate.postForObject(url, activateSlot, String.class);
														commonResponse = gson.fromJson(stringResponse, Response.class);
														if (commonResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS))
															iScheduleLogsDao.markScheduleLogsCompleted(logsDto.getScheduleLogsId());
														else
															iScheduleLogsDao.markScheduleLogsError(logsDto.getScheduleLogsId());

														break;

												}
											}
										} catch (Exception e) {
										logger.error("syncSchedulingLogs DOCTOR_SCHEDULE sync failed for scheduleLogsId " + logsDto.getScheduleLogsId(), e);

										}
										break;
									case ScheduleTables.DOCTOR_CHARGES:
										try {
											if (logsDto.getRecordId() != null) {
												response = iDoctorScheduleDao.getDoctorChargesForSyncByDoctorId(logsDto.getRecordId());
												List<DoctorChargesSyncDto> doctorChargesSyncDtoList = response.getListObject();
												DoctorChargesSyncDto doctorCharges = (DoctorChargesSyncDto) response.getObject();
												//String doctorChargesJson=new Gson().toJson(doctorChargesSyncDtoList);
												if (doctorChargesSyncDtoList != null && doctorChargesSyncDtoList.size() > 0) {
													for (DoctorChargesSyncDto doctorChargesSyncDto : doctorChargesSyncDtoList) {
														doctorChargesSyncDto.setChannelId(logsDto.getChannelId());
													}
													switch (action) {
														case ScheduleActions.INSERT:
															HttpHeaders headers = new HttpHeaders();
															headers.set("channelId", (logsDto.getChannelId() != null && logsDto.getChannelId() != 0) ? logsDto.getChannelId().toString() : "1");
//											HttpEntity<DoctorChargesSyncDto> request = new HttpEntity<>(doctorCharges, headers);
//												url = URI.create(Global_Scheduling_URI + "/sync/doctor/charges");
//												String stringResponse = restTemplate.postForObject(url, request, String.class);
//												//String stringResponse = restTemplate.postForObject(url, doctorChargesSyncDtoList, String.class);
//												commonResponse = gson.fromJson(stringResponse, Response.class);

															HttpEntity<List<DoctorChargesSyncDto>> request = new HttpEntity<>(doctorChargesSyncDtoList, headers);
															url = URI.create(Global_Scheduling_URI + "/sync/doctor/charges");
															String stringResponse = restTemplate.postForObject(url, request, String.class);
															commonResponse = gson.fromJson(stringResponse, Response.class);
															if (commonResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS))
																iScheduleLogsDao.markScheduleLogsCompleted(logsDto.getScheduleLogsId());
															else
																iScheduleLogsDao.markScheduleLogsError(logsDto.getScheduleLogsId());
															break;
													}
												}
											}
										} catch (Exception e) {
										logger.error("syncSchedulingLogs DOCTOR_CHARGES sync failed for scheduleLogsId " + logsDto.getScheduleLogsId(), e);
										}
										break;

									case ScheduleTables.ADMISSION_DETAILS:
										try {
											response = iAppointmentMasterDao.getIPPatientDetailsToSync(logsDto.getRecordId());
											AdmissionAndDischargeSyncDto admissionAndDischargeSyncDto = (AdmissionAndDischargeSyncDto) response.getObject();
											if (admissionAndDischargeSyncDto != null) {
												switch (action) {
													case ScheduleActions.INSERT:
														iScheduleLogsDao.markScheduleLogsCompleted(logsDto.getScheduleLogsId());
														url = URI.create(MY_LIFE_URI + "/sync/ipd/save/discharge/admissiondetail");
														HttpHeaders headers = new HttpHeaders();
														headers.set("channelId", (logsDto.getChannelId() != null && logsDto.getChannelId() != 0) ? logsDto.getChannelId().toString() : "1");
														HttpEntity<AdmissionAndDischargeSyncDto> request = new HttpEntity<>(admissionAndDischargeSyncDto, headers);
														String stringResponse = restTemplate.postForObject(url, request, String.class);
														commonResponse = gson.fromJson(stringResponse, Response.class);
														if (commonResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS))
															iScheduleLogsDao.markScheduleLogsCompleted(logsDto.getScheduleLogsId());
														else
															iScheduleLogsDao.markScheduleLogsError(logsDto.getScheduleLogsId());
														break;
												}
											}
										} catch (Exception e) {
										logger.error("syncSchedulingLogs ADMISSION_DETAILS sync failed for scheduleLogsId " + logsDto.getScheduleLogsId(), e);

										}
										break;


									case ScheduleTables.APPOINTMENT_DETAILS:
										try {
											response = iAppointmentMasterService.getAppointmentDetailsToSyncById(logsDto.getRecordId());
											if (response.getCode().equals(ICommonConstants.SUCCESS_CODE) && response.getObject() != null) {
												iScheduleLogsDao.markScheduleLogsCompleted(logsDto.getScheduleLogsId());
												AppointmentDetailsRealTimeDto appointmentDtls = (AppointmentDetailsRealTimeDto) response.getObject();
												String appointmentDetailJson = new Gson().toJson(appointmentDtls);
												switch (action) {
													case ScheduleActions.INSERT:
														if (appointmentDtls != null) {
															url = URI.create(MY_LIFE_URI + "/sync/opd/appointment/details");
															HttpHeaders headers = new HttpHeaders();
															headers.set("channelId", (appointmentDtls.getChannelId() != null && appointmentDtls.getChannelId() != 0) ? appointmentDtls.getChannelId().toString() : "1");
															HttpEntity<AppointmentDetailsRealTimeDto> request = new HttpEntity<>(appointmentDtls, headers);
															String stringResponse = restTemplate.postForObject(url, request, String.class);
															commonResponse = gson.fromJson(stringResponse, Response.class);

															if (commonResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS)) {
																iScheduleLogsDao.markScheduleLogsCompleted(logsDto.getScheduleLogsId());
															} else {
																iScheduleLogsDao.markScheduleLogsError(logsDto.getScheduleLogsId());
															}
														}
														break;
												}
											}
										} catch (Exception e) {
										logger.error("syncSchedulingLogs APPOINTMENT_DETAILS sync failed for scheduleLogsId " + logsDto.getScheduleLogsId(), e);

										}
										break;

									case ScheduleTables.PATIENT_DOCUMENT_MAPPER:
										try {
											response = iPatientMasterDao.getPatientDocumentsByEncounterDocId(logsDto.getRecordId());
											if (response.getObject() != null) {
												PatientDocumentSyncDto patientDocDto = (PatientDocumentSyncDto) response.getObject();
												if (patientDocDto.getDocumentTypeId() != null && patientDocDto.getDocumentTypeId() != 102) {
													switch (action) {
														case ScheduleActions.INSERT:
//										    iScheduleLogsDao.markScheduleLogsCompleted(logsDto.getScheduleLogsId());
															url = URI.create(MY_LIFE_URI + "/uploadPatientDocumentFromHIS");
//											HttpHeaders headers = new HttpHeaders();
//											headers.set("channelId", (patientDocDto.getChannelId() != null && patientDocDto.getChannelId() != 0) ? patientDocDto.getChannelId().toString():"1");
//											HttpEntity<PatientDocumentSyncDto> request = new HttpEntity<>(patientDocDto, headers);
															String stringResponse = restTemplate.postForObject(url, patientDocDto, String.class);
															commonResponse = gson.fromJson(stringResponse, Response.class);
															if (commonResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS))
																iScheduleLogsDao.markScheduleLogsCompleted(logsDto.getScheduleLogsId());
															else
																iScheduleLogsDao.markScheduleLogsError(logsDto.getScheduleLogsId());
															break;
														case ScheduleActions.UPDATE:
	//										iScheduleLogsDao.markScheduleLogsCompleted(logsDto.getScheduleLogsId());
															url = URI.create(MY_LIFE_URI + "/deletePatientDocumentByUuid");
//											HttpHeaders headers1 = new HttpHeaders();
//											headers1.set("channelId", (patientDocDto.getChannelId() != null && patientDocDto.getChannelId() != 0) ? patientDocDto.getChannelId().toString():"1");
//											HttpEntity<PatientDocumentSyncDto> request1 = new HttpEntity<>(patientDocDto, headers1);
															String stringResponse1 = restTemplate.postForObject(url, patientDocDto, String.class);
															commonResponse = gson.fromJson(stringResponse1, Response.class);
															if (commonResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS))
																iScheduleLogsDao.markScheduleLogsCompleted(logsDto.getScheduleLogsId());
															else
																iScheduleLogsDao.markScheduleLogsError(logsDto.getScheduleLogsId());
															break;
													}
												} else {
													iScheduleLogsDao.markScheduleLogsCompleted(logsDto.getScheduleLogsId());
												}
											}
										} catch (Exception e) {
										logger.error("syncSchedulingLogs PATIENT_DOCUMENT_MAPPER sync failed for scheduleLogsId " + logsDto.getScheduleLogsId(), e);

										}
										break;

									case ScheduleTables.PATIENT_KIN_DETAILS:
										try {
											response = iPatientMasterDao.getPatientKinDetails(logsDto.getRecordId());
											if (response.getObject() != null) {
												PatientSyncDto patientSynDto = (PatientSyncDto) response.getObject();
												switch (action) {
													case ScheduleActions.INSERT:
														String kinName[] = patientSynDto.getKinFirstName().split(" ");
														if (kinName.length == 2) {
															patientSynDto.setFirstName(kinName[0]);
															patientSynDto.setLastName(kinName[1]);
														} else if (kinName.length == 3) {
															patientSynDto.setFirstName(kinName[0]);
															patientSynDto.setMiddleName(kinName[1]);
															patientSynDto.setLastName(kinName[2]);
														} else {
															patientSynDto.setFirstName(kinName[0]);
														}
														url = URI.create(MY_LIFE_URI + "/sync/patientKinDetails");
//											HttpHeaders headers = new HttpHeaders();
//											headers.set("channelId", (patientSynDto.getChannelId() != null && patientSynDto.getChannelId() != 0) ? patientSynDto.getChannelId().toString():"1");
//											HttpEntity<PatientSyncDto> request = new HttpEntity<>(patientSynDto, headers);
														String stringResponse = restTemplate.postForObject(url, patientSynDto, String.class);
														commonResponse = gson.fromJson(stringResponse, Response.class);
														if (commonResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS))
															iScheduleLogsDao.markScheduleLogsCompleted(logsDto.getScheduleLogsId());
														else
															iScheduleLogsDao.markScheduleLogsError(logsDto.getScheduleLogsId());
														break;
												}
											}
										} catch (Exception e) {
										logger.error("syncSchedulingLogs PATIENT_KIN_DETAILS sync failed for scheduleLogsId " + logsDto.getScheduleLogsId(), e);

										}
										break;

								}

							}

						}
						isExecuted = 'N';
					}

				}
			}
		} catch (Exception e) {
			logger.error("syncSchedulingLogs failed", e);
		}

	}
	
	
	  @Transactional
	  @Scheduled(fixedDelay = 120000, initialDelay = 60000)
	  public void syncPathalogyReport() {
		try {

			String serverId = serverIdentityService.getServerId();
			Boolean check = iServiceIdentityDao.checkServiceIdAvailable(serverId);

			if(check) {
				String MY_LIFE_URI = null;
				URI url = null;
				Response response = null;
				Response res = iScheduleLogsDao.getConnectionStringForMyLife();
				MyLifeConnectionLinkDto connectionLinkDto = (MyLifeConnectionLinkDto) res.getObject();
				if (connectionLinkDto != null && connectionLinkDto.getMyLifeLink() != null && connectionLinkDto.getMyLifeLink() != "")
					MY_LIFE_URI = connectionLinkDto.getMyLifeLink();
				//MY_LIFE_URI = "https://mluat.his.clinivantage.co.in/api";
				//MY_LIFE_URI = "http://localhost:9091/MyLifeGlobal/api";
				//Global_Scheduling_URI = "http://10.10.0.195:9091/MyLifeGlobal/api";

				if (MY_LIFE_URI != null) {
					if (isLabReportSync == 'N') {
						isLabReportSync = 'Y';

						response = iAppointmentMasterService.syncPatientPathalogyReport();
						if (response.getCode().equals(ICommonConstants.SUCCESS_CODE)
								&& response.getListObject().size() > 0) {
							List<ServiceappoServiceMpprSyncDto> serviceappoServiceMpprSyncDto = response.getListObject();
							if (serviceappoServiceMpprSyncDto != null) {
								url = URI.create(MY_LIFE_URI + "/sync/opd/appointment/pathalogyreport");
								ObjectMapper objectMapper = new ObjectMapper();
								String json = objectMapper.writeValueAsString(serviceappoServiceMpprSyncDto);
								System.out.println("serviceappoServiceMpprSyncDto JSON is:" + json);
								String stringResponse = restTemplate.postForObject(url, serviceappoServiceMpprSyncDto, String.class);
								Response commonResponse = gson.fromJson(stringResponse, Response.class);
								iScheduleLogsDao.updatePathalogyLabReportStatus(serviceappoServiceMpprSyncDto);
							}
						}
						isLabReportSync = 'N';
					}
				}
			}
		} catch (Exception e) {
			logger.error("syncPathalogyReport failed", e);
		}
	}
}
	
