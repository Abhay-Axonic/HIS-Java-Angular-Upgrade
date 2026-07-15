package com.param.adt.admission.dao;

import com.param.adt.admission.dto.NabhAssessmentDTO;
import com.param.adt.admission.dto.NutritionalAssessmentScoreDTO;
import com.param.adt.admission.dto.PatientFallReportDto;
import com.param.adt.admission.dto.PatientFallReturnReportDto;
import com.param.adt.common.Response;

import java.util.List;

@SuppressWarnings("rawtypes")
public interface IPatientFallReportDao {

    public Response saveAsDraftPatientFallReporting(PatientFallReportDto patientFallReportDto);

    public Response savePatientFallReporting(PatientFallReportDto patientFallReportDto);

    public Response getSaveAsDraftPatientFallReporting(Integer unitId, Integer organizationId, Integer admissionId);

    public Response inactiveSavedDraftScoreList(List<PatientFallReturnReportDto> patientFallReturnReportDtos);

}
