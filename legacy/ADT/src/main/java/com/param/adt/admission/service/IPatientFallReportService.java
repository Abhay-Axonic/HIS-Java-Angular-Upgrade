package com.param.adt.admission.service;

import com.param.adt.admission.dto.NutritionalAssessmentScoreDTO;
import com.param.adt.admission.dto.PatientFallReportDto;
import com.param.adt.common.Response;

import javax.transaction.Transactional;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IPatientFallReportService {

    public Response saveAsDraftPatientFallReport(PatientFallReportDto patientFallReportDto);

    public Response savePatientFallReport(PatientFallReportDto patientFallReportDto);

    public Response getSaveAsDraftPatientFallReport(Integer unitId, Integer organizationId, Integer admissionId);


}
