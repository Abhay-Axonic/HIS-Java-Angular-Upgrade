package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.PatientDialysisRecordDto;
import com.param.global.dto.RadiologyOrdersListDto;
import com.param.global.dto.SlotMasterDto;
import com.param.opd.appointment.dto.HemodialysisAppointmentMapperDto;

@SuppressWarnings("rawtypes")
public interface IHemodialysisService {

	@Transactional
	Response scheduleDialysisAppointment(SlotMasterDto slotMasterDto);

	@Transactional
	Response saveDialysisRequest(HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto);

	@Transactional
	Response getPatientListForDialysisWorkload(HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto);

	@Transactional
	Response getDialysisMachines(HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto);

	@Transactional
	Response savePatientDialysisRecord(PatientDialysisRecordDto patientDialysisRecordDto);

	@Transactional
	Response uploadDialysisDocument(PatientDialysisRecordDto patientDialysisRecordDto);

	@Transactional
	Response getPatientDialysisRecordDetails(HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto);

	@Transactional
	Response searchPatientForDialysis(HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto);

	@Transactional
	Response updateDialysisStatus(PatientDialysisRecordDto patientDialysisRecordDto);

}
