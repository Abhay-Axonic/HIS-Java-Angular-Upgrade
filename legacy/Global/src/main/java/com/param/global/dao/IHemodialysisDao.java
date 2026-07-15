package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.PatientDialysisRecordDto;
import com.param.global.dto.SlotMasterDto;
import com.param.opd.appointment.dto.HemodialysisAppointmentMapperDto;


@SuppressWarnings("rawtypes")
public interface IHemodialysisDao {

	Response assignDialysisChair(SlotMasterDto slotMasterDto);

	Response saveDialysisRequest(HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto);

	Response getPatientListForDialysisWorkload(HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto);

	Response getDialysisMachines(HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto);

	Response savePatientDialysisRecord(PatientDialysisRecordDto patientDialysisRecordDto);

	Response uploadDialysisDocument(PatientDialysisRecordDto patientDialysisRecordDto);

	Response getPatientDialysisRecordDetails(HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto);

	Response searchPatientForDialysis(HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto);

	Response updatePatientDialysisRecord(PatientDialysisRecordDto dto);

}
