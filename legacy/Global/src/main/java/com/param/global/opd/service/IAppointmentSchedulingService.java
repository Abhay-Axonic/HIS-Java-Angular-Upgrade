package com.param.global.opd.service;

import java.util.List;

import javax.transaction.Transactional;

import com.param.global.dto.global.AIAppointmentQueueDto;
import org.springframework.web.multipart.MultipartFile;

import com.param.global.common.Response;
import com.param.global.dto.PatientRegistrationDto;
import com.param.global.dto.SlotMasterDto;
import com.param.opd.encounter.dto.EncounterMasterDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IAppointmentSchedulingService {

	Response scheduleAppointment(SlotMasterDto slotMasterDto)throws ApplicationException;

	@Transactional
	Response scheduledAppointmentList(SlotMasterDto slotMasterDto)throws ApplicationException;

	@Transactional
	Response cancelScheduledAppointment(SlotMasterDto slotMasterDto)throws ApplicationException;

	@Transactional
	Response rescheduleAppointment(SlotMasterDto slotMasterDto)throws ApplicationException;

	@Transactional
	Response searchAppointmentsByCriteria(SlotMasterDto slotMasterDto)throws ApplicationException;

	@Transactional
	Response updateAppointmentStatus(SlotMasterDto slotMasterDto) throws ApplicationException;

	@Transactional
	Response blockedScheduleAppointmentList(SlotMasterDto slotMasterDto)throws ApplicationException;

	Boolean isAppointmentForSameDateTimeExist(SlotMasterDto slotMasterDto)throws ApplicationException;

	@Transactional
	Response scheduleRadiologyAppointmentForInPatient(SlotMasterDto slotMasterDto);


	Response generateAppointment(SlotMasterDto slotMasterDto)throws ApplicationException;

	@Transactional
	Response markArrival(Integer appointmentId,String currentDate,Integer unitId)throws ApplicationException;

	@Transactional
	Response batchJobToUpdateAppointmentStatus()throws ApplicationException;

	@Transactional
	Response assignCounterNumber(SlotMasterDto slotMasterDto)throws ApplicationException;
	
	@Transactional
	Response searchInProcessAppointment(Integer appointmentId)throws ApplicationException;

	@Transactional
	Response updateCallCount(Integer appointmentId, Integer callCount)throws ApplicationException;

	@Transactional
	Response updateToken(Integer appointmentId, Integer callCount)throws ApplicationException;
	
	@Transactional
	Response assignPatientToAppointmentFromPatientRegistration(Integer appointmentId, Integer patientId)throws ApplicationException;
	
	@Transactional
	Response updateTokenNursing(Integer encounterId, Integer callCount, Integer orderDetailsId)throws ApplicationException;

	Response isAppointmentAlreadyExist(SlotMasterDto slotMasterDto) throws ApplicationException;

	@Transactional
	Response markArrivalForPaidAppointment(EncounterMasterDto encounterMasterDto);

	@Transactional
	Response afterConversionPaidAppointment(EncounterMasterDto encounterMasterDto);
	
	@Transactional
	Response blockSlot(Integer slotId, Integer statusId);

	@Transactional
	Response searchAppointmentsByPatienId(SlotMasterDto slotMasterDto) throws ApplicationException;

	@Transactional
	Response generateCyclicServiceAppointment(List<SlotMasterDto> slotMasterDto);

	@Transactional
	Response searchCyclicServiceAppointmentsByPatienId(SlotMasterDto slotMasterDto);

	@Transactional
	Response rescheduleCyclicServiceAppointment(SlotMasterDto dto);

	@Transactional
	Response getScheduledServiceAppointmentList(SlotMasterDto slotMasterDto);

	@Transactional
	Response getDeptWiseServiceListForCyclicOrder(SlotMasterDto slotMasterDto)throws ApplicationException;
	
	@Transactional
	Response getCyclicDocWiseAndDeptWiseAppointmentListByPatientId(Integer patientId, Integer oranizationId);
	
	@Transactional
	Response getServiceListForCyclicOrder(SlotMasterDto slotMasterDto)throws ApplicationException;

	@Transactional
	Response rescheduleAppointmentForMylife(Integer oldAppointmentId, Integer newAppointmentId)throws ApplicationException;
	
	@Transactional
	Response batchJobToUpdateMultiVisitPackage(List<Integer> unitIds)throws ApplicationException;

	@Transactional
	Response updateAppointmentPaidStatus(Integer appointmentId)throws ApplicationException;

	@Transactional
	Response savePatientDetailsFromWebApp(EncounterMasterDto encounterMasterDto)throws ApplicationException;
	
	@Transactional
	Response getAppointMentDetailsByPatientId(SlotMasterDto slotMasterDto)throws ApplicationException;


	@Transactional
	Response savePatientDetailsFromWebAndMobileApp(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;

	@Transactional
	Response scheduleAppointmentForWebAppointment(SlotMasterDto slotMasterDto)throws ApplicationException;
	
	@Transactional
	Response scheduleAppointmentForWebAppointmentt(SlotMasterDto slotMasterDto)throws ApplicationException;

	Response prnConfiguration(SlotMasterDto slotMasterDto);
	
	@Transactional
	Response getTokenNo(Integer appointmentId)throws ApplicationException;

	@Transactional
	Response searchInProcessAppointmentWeb(Integer appointmentId)throws ApplicationException;

	@Transactional
	Response searchFutureAppointmentsByPatientId(SlotMasterDto slotMasterDto)throws ApplicationException;

    Response saveAIAppointmentInQueue(AIAppointmentQueueDto dto);

	Response getAIAppointmentCount(AIAppointmentQueueDto dto);

	Response getAISummaryData(AIAppointmentQueueDto dto);
}
