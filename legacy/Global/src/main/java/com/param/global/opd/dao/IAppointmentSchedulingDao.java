package com.param.global.opd.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.dto.SlotMasterDto;
import com.param.global.dto.sync.ScheduleAppointmentReqDto;
import com.param.opd.encounter.dto.EncounterMasterDto;

import in.param.exception.ApplicationException;

import javax.transaction.Transactional;

@SuppressWarnings("rawtypes")
public interface IAppointmentSchedulingDao {

	Response scheduleAppointment(SlotMasterDto slotMasterDto)throws ApplicationException;

	Response scheduledAppointmentList(SlotMasterDto slotMasterDto)throws ApplicationException;

	Response cancelScheduledAppointment(SlotMasterDto slotMasterDto)throws ApplicationException;

	Response rescheduleAppointment(SlotMasterDto slotMasterDto)throws ApplicationException;

	Response searchAppointmentsByCriteria(SlotMasterDto slotMasterDto)throws ApplicationException;
	
	Response updateAppointmentStatus(SlotMasterDto slotMasterDto)throws ApplicationException;

	Response blockedScheduleAppointmentList(SlotMasterDto slotMasterDto)throws ApplicationException;

	boolean isAppointmentValid(SlotMasterDto slotMasterDto)throws ApplicationException;

	Response isAppointmentForSameDateTimeExist(SlotMasterDto slotMasterDto) throws ApplicationException;

	Response isAppointmentIsForSameDoctorWithoutCompletionOfPreviousAppintment(SlotMasterDto slotMasterDto)throws ApplicationException;

	Response updateOrderDetailsIsAppointableServiceStatus(Integer orderDetailsId, Integer updatedBy)throws ApplicationException;

	Response updateSlotId(Integer appointmentId, Integer slotId, Integer oldSlotId)throws ApplicationException;

	Response batchJobToUpdateAppointmentStatus()throws ApplicationException;

	Response updateAppointmentTypeId(Integer appointmentId)throws ApplicationException;

	Response assignCounterNumber(SlotMasterDto slotMasterDto)throws ApplicationException;

	Integer getTokenNumber(String currentDate, Integer unitId) throws ApplicationException;

	Response markArrival(Integer appointmentId, String currentDate, Integer unitId) throws ApplicationException;

	Response searchInProcessAppointment(Integer appointmentId)throws ApplicationException;

	Response scheduleAppointmentFromMyLife(SlotMasterDto appointment) throws ApplicationException;

	Response rescheduleAppointmentFromMyMylife(SlotMasterDto appointment);

	Response updateCallCount(Integer appointmentId, Integer tokenNumber)throws ApplicationException;

	Response updateToken(Integer appointmentId, Integer tokenNumber)throws ApplicationException;

	Response assignPatientToAppointmentFromPatientRegistration(Integer appointmentId, Integer patientId)throws ApplicationException;

	Response updateTokenNursing(Integer encounterId, Integer tokenNumber, Integer orderDetailsId)throws ApplicationException;

	Response isAppointmentIsForSameSpecialityWithoutCompletionOfPreviousAppintment(SlotMasterDto slotMasterDto)throws ApplicationException;

	Response blockAppointment(SlotMasterDto slotMasterDto)throws ApplicationException;

	Integer appointmentCheckForKioskPatient(SlotMasterDto slotMasterDto);

	Response markArrivalForPaidAppointments(EncounterMasterDto encounterMasterDto);

	Response afterConversionPaidAppointment(EncounterMasterDto encounterMasterDto);

	@Transactional
	Response blockSlot(Integer slotId, Integer statusId);

	Response searchAppointmentsByPatienId(SlotMasterDto slotMasterDto)throws ApplicationException;
	
	Boolean getEHCAppointmentsByPatienId(SlotMasterDto slotMasterDto) throws ApplicationException;

	Response searchCyclicServiceAppointmentsByPatienId(SlotMasterDto slotMasterDto)throws ApplicationException;

	Response createCyclicAppointmentLog(SlotMasterDto slotDto);

	Response rescheduleCyclicServiceAppointment(SlotMasterDto slotMasterDto);

	Response updateCyclicAppointment(EncounterMasterDto encounterMasterDto);

	Response getScheduledServiceAppointmentList(SlotMasterDto slotMasterDto);
	
	Response rescheduleAppointmentForPaidAppointment(EncounterMasterDto encounterMasterDto);

	Response updateOrderDetailForServiceAppointment(SlotMasterDto slotDto);

	Response getDeptWiseServiceListForCyclicOrder(SlotMasterDto slotMasterDto)throws ApplicationException;

	Response getCyclicDocWiseAndDeptWiseAppointmentListByPatientId(Integer patientId, Integer oranizationId);

	Integer getAppointmentId(Integer encounterId);

	Character getNursingAssessmentRequired(Integer encounterId);

	Integer getEHCFreeSlot(SlotMasterDto slotMasterDto);

	Response getServiceListForCyclicOrder(SlotMasterDto slotMasterDto)throws ApplicationException;

	Response rescheduleAppointmentForMylife(Integer oldAppointmentId,Integer newAppointmentId);

	Response batchJobToUpdateMultiVisitPackage(List<Integer> unitIds);

	boolean isPatientSync(Integer patientId);

	boolean isMyLifeAppointment(Integer appointmentId);

	Response updateMultiLingCoverPath(String coversheetMlPath, Integer appointmentId);

	Response updateAppointmentPaidStatus(Integer appointmentId);

	Response getAppointMentDetailsByPatientId(SlotMasterDto slotMasterDto)throws ApplicationException;

	Response prnConfiguration(SlotMasterDto slotMasterDto);

	Response getTokenNo(Integer appointmentId)throws ApplicationException;

	Response getAppointmentUniqueId(SlotMasterDto slotMasterDtoss)throws ApplicationException;

	Response searchInProcessAppointmentWeb(Integer appointmentId)throws ApplicationException;

	Response searchFutureAppointmentsByPatientId(SlotMasterDto slotMasterDto)throws ApplicationException;

	Response updateIsAppointmentFromMyLife(Integer patientId, Integer appointmentId)throws ApplicationException;

    Response addNationalityMaster(ScheduleAppointmentReqDto reqDto) throws ApplicationException;

	Integer getNationalityIdByNationalityName(ScheduleAppointmentReqDto reqDto) throws ApplicationException;
}
